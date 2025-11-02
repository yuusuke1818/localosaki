package jp.co.osaki.sms.batch.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.osol.entity.TWkInspectionIncomp;
import jp.co.osaki.osol.entity.TWkInspectionIncompPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.dao.AutoInspExecDao;
import jp.co.osaki.sms.batch.resultset.AutoInspLatestMonthNoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterSvrResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 検針結果登録Logic.
 * @author kobayashi.sho
 */
public class InspectionLogic {

    /** バッチログ. */
    private Logger batchLogger;

    /** エラーログ. */
    private Logger errorLogger;

    /** EntityManager. */
    private EntityManager entityManager;

    /** 自動検針Dao. */
    private AutoInspExecDao dao;

    /** システム日時. */
    private Timestamp serverDateTime;

    /** バッチ処理名称. */
    private String batchName;

    public InspectionLogic(Logger batchLogger, Logger errorLogger, EntityManager entityManager, AutoInspExecDao dao, Timestamp serverDateTime, String batchName) {
        this.batchLogger = batchLogger;
        this.errorLogger = errorLogger;
        this.entityManager = entityManager;
        this.dao = dao;
        this.serverDateTime = serverDateTime;
        this.batchName = batchName;
    }

    /**
     * 検針結果登録.
     * ※検針結果データ(サーバ用)(T_INSPECTION_METER_SVR) 登録.
     *
     * @param adjustDate 自動検針実施日(補正) / 予約検針実施日(補正)  ※書式:yyyyMMddHH
     * @param inComplateList 日報データ異常リスト生成  ※null:予約検針
     * @param meterInfo メーター情報
     * @param latestMonth 最新検針月情報  getInspMonthNo():月検針連番1から999  getIsFind():データ有無フラグ true:過去に登録あり false:新規自動検針
     * @param inspType 検針種別  SmsBatchConstants.INSP_KIND.AUTO.getVal() または SmsBatchConstants.INSP_KIND.SCHEDULE.getVal()
     * @param isIncomplete 検針データ未完了有無  true:未完了あり(または予約検針)  false:未完了なし(通常ケース)
     * @param targetYmdHm 自動検針実施日 / 予約検針実施日  ※書式:yyyyMMddHHmm (mm:自動検針では"00"、予約検針では"00"または"30")
     * @param isChkInt 整数フラグ true:整数(端数切捨て)
     * @param currentDate 現在日付
     * @return 検針結果データ登録有無フラグ true:登録あり(END_FLG=1)  false:登録なし
     */
    public boolean inspMeter(String adjustDate, List<Long> inComplateList, AutoInspMeterResultSet meterInfo, AutoInspLatestMonthNoResultSet latestMonth, String inspType, boolean isIncomplete, String targetYmdHm, boolean isChkInt, Date currentDate) {
        boolean inspFlg = false; // 検針結果データ登録有無フラグ 初期化

        // 検針実施日時
        Timestamp latestDate = new Timestamp(DateUtility.conversionDate(targetYmdHm + "01", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());

        try {
            // 自動検針実施日(補正)を取得
            String fixedAdjustDate = adjustDate;

            // 今回検針日時よりリトライ猶予時間を遡った場合に月を跨いでいる場合を考慮
            // リトライ猶予時間を遡った日時
            Timestamp previousDate = new Timestamp(latestDate.getTime() - SmsBatchConstants.INSPECTION_RETRY_HOUR.longValue() * 60L * 60L * 1000L);

            // Calendarに変換
            Calendar calLatest = Calendar.getInstance();
            calLatest.setTime(latestDate);

            Calendar calPrevious = Calendar.getInstance();
            calPrevious.setTime(previousDate);

            // 月跨ぎ判定
            boolean isCrossMonth = calLatest.get(Calendar.YEAR) != calPrevious.get(Calendar.YEAR) || calLatest.get(Calendar.MONTH) != calPrevious.get(Calendar.MONTH);

            // 月跨ぎの場合
            String previousAdjustDate = null;
            if (isCrossMonth) {
                // adjustDateが"yyyyMMddHH"形式である前提
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
                Date adjustDateParsed = sdf.parse(adjustDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(adjustDateParsed);

                // 月を1つ減らす
                cal.add(Calendar.MONTH, -1);

                // フォーマットし直してadjustDateへ再セット
                previousAdjustDate = sdf.format(cal.getTime());
            }

            // 比較元の検針データ取得
            List<TInspectionMeterSvr> originalTimsList = dao.findInspMeterSvrByInspType(
                    meterInfo.getDevId(),
                    meterInfo.getMeterMngId(),
                    getYear(adjustDate),
                    getMonth(adjustDate),
                    inspType);

            // 今回検針日時よりリトライ猶予時間を遡った場合に月を跨いでいる場合
            if (isCrossMonth) {
                // 月を跨いだ場合の比較元の検針データ取得（自動検針実行の場合は自動検針、予約検針実行の場合は予約検針）
                List<TInspectionMeterSvr> previousOriginalTimsList = dao.findInspMeterSvrByInspType(
                        meterInfo.getDevId(),
                        meterInfo.getMeterMngId(),
                        getYear(previousAdjustDate),
                        getMonth(previousAdjustDate),
                        inspType);

                // 取得できた場合targetTimsListに追加
                if (previousOriginalTimsList != null && !previousOriginalTimsList.isEmpty()) {
                    originalTimsList.addAll(previousOriginalTimsList);
                    batchLogger.info("前月分の検針データをtargetTimsListに追加： " + previousOriginalTimsList.size() + "件");
                }
            }

            // 比較元の検針データにて最新検針日時が最新のレコードを取得
            Optional<TInspectionMeterSvr> latestOriginalTim = originalTimsList.stream()
                    .filter(t -> t.getEndFlg() != null && BigDecimal.ZERO.compareTo(t.getEndFlg()) == 0)
                    .max(Comparator.comparing(TInspectionMeterSvr::getLatestInspDate));

            // 取得できた場合　リトライ中であればリトライ中の検針の最新検針日時 リトライ中でなければ今回の検針実施日時を設定
            Timestamp originalLatestInspDate = null;
            if (latestOriginalTim.isPresent()) {
                // 今回の検針がリトライ中（現在日時が今回の検針実施日時と比較してリトライ猶予時間以内）の場合
                if (currentDate.getTime() <= (latestOriginalTim.get().getLatestInspDate().getTime() + SmsBatchConstants.INSPECTION_RETRY_HOUR.longValue() * 60L * 60L * 1000L)) {
                    TInspectionMeterSvr tim = latestOriginalTim.get();
                    originalLatestInspDate = tim.getLatestInspDate(); // リトライ中の検針の最新検針日時を設定
                    latestMonth = new AutoInspLatestMonthNoResultSet(tim.getId().getInspMonthNo(), latestMonth.getIsFind()); // 月検針連番を設定
                    adjustDate = tim.getId().getInspYear() + String.format("%02d", Integer.parseInt(tim.getId().getInspMonth())) + adjustDate.substring(6); // リトライ対象レコードの情報に更新
                } else {
                    // リトライ猶予時間以内ではない場合、今回の検針実施日時を設定
                    originalLatestInspDate = latestDate;
                }
            } else {
                // 取得できない場合、今回の検針実施日時を設定
                originalLatestInspDate = latestDate;
            }

            // 日報データ異常リストにある?
            if (isMeterIncomplate(inComplateList, meterInfo.getMeterMngId())) {
                // 日報終了日レコード欠損異常
                batchLogger.info(this.batchName.concat(" 日報終了日レコード欠損異常( dev_id = " + meterInfo.getDevId()
                        + " meter_mng_id = " + meterInfo.getMeterMngId()
                        + " target_date = " + targetYmdHm));

                // 日報データ要求及び未完了データの登録
                incomplateProc(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, getYMD(targetYmdHm), adjustDate, latestMonth.getInspMonthNo(), latestDate);
                return false;
            }

            // 乗率取得
            // m_meterの乗率が0またはnullの場合
            BigDecimal multiRate = meterInfo.getMulti();
            if (multiRate == null || multiRate.compareTo(BigDecimal.ZERO) == 0) {
                // 乗率が未登録（m_meter.multi=null）または0の場合、検針未完了(t_inspection_meter_srv.end_flg=0)の検針データを作成するためコメントアウト
//                multiRate = BigDecimal.ONE;
                batchLogger.info(this.batchName.concat(" 乗率が0または未登録( dev_id = " + meterInfo.getDevId()
                        + " meter_mng_id = " + meterInfo.getMeterMngId()
                        + " target_date = " + targetYmdHm
                        + " multi = " + multiRate));

                // 検針結果未完了でInsert
                insertAutoInsp(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, adjustDate, latestMonth.getInspMonthNo(), BigDecimal.ZERO, latestDate);
                return false;
            }

            // メータ取得ログ出力
            batchLogger.info(this.batchName.concat(" メーター取得( dev_id = " + meterInfo.getDevId()
                    + " meter_mng_id = " + meterInfo.getMeterMngId()
                    + " multi = " + multiRate));

            // 前回、前々回検針値取得処理
            AutoInspMeterSvrResultSet latestValue = dao.getAutoInspLatestResult(meterInfo.getDevId(), meterInfo.getMeterMngId());
            // 直前の検針結果のLatestを前回値
            // Prevを前前回値とする
            BigDecimal prevLatestVal = null;
            Date prevDate = null;
            BigDecimal prevLatestVal2 = null;
            Date prevDate2 = null;
            if (latestValue != null) {
                prevLatestVal = latestValue.getLatestInspVal();
                prevDate = latestValue.getLatestInspDate();
                prevLatestVal2 = latestValue.getPrevInspVal();
                prevDate2 = latestValue.getPrevInspDate();
            }

            // 締め月の検針データチェック
            if (latestMonth.getIsFind()) {
                String prev = DateUtility.changeDateFormat(prevDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                // 締め月の検診データが全て締まっている、かつ前回と今回の検針年月日時が同じ場合は次のメータへ処理スキップ
                if (!isIncomplete && targetYmdHm.equals(prev)) {
                    return false; // 同じ targetYmdHm のレコードが登録されないように制御されている
                }
            }

            // 検針種別を取得
            String targetInspType = "";
            // 自動検針の場合は予約検針を設定し、予約検針の場合は自動検針を設定
            if (SmsBatchConstants.INSP_KIND.AUTO.getVal().equals(inspType)) {
                targetInspType = SmsBatchConstants.INSP_KIND.SCHEDULE.getVal(); // 予約検針の検針種別
            } else if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                targetInspType = SmsBatchConstants.INSP_KIND.AUTO.getVal(); // 自動検針の検針種別
            }

            // 対象の検針データ取得（自動検針実行の場合は予約検針、予約検針実行の場合は自動検針）
            List<TInspectionMeterSvr> targetTimsList = dao.findInspMeterSvrByInspType(
                    meterInfo.getDevId(),
                    meterInfo.getMeterMngId(),
                    getYear(adjustDate),
                    getMonth(adjustDate),
                    targetInspType);

            // 今回検針日時よりリトライ猶予時間を遡った場合に月を跨いでいる場合
            if (isCrossMonth) {
                // 月を跨いだ場合の対象の検針データ取得（自動検針実行の場合は予約検針、予約検針実行の場合は自動検針）
                List<TInspectionMeterSvr> previousTargetTimsList = dao.findInspMeterSvrByInspType(
                        meterInfo.getDevId(),
                        meterInfo.getMeterMngId(),
                        getYear(previousAdjustDate),
                        getMonth(previousAdjustDate),
                        targetInspType);

                // 取得できた場合targetTimsListに追加
                if (previousTargetTimsList != null && !previousTargetTimsList.isEmpty()) {
                    targetTimsList.addAll(previousTargetTimsList);
                    batchLogger.info("前月分の検針データをtargetTimsListに追加： " + previousTargetTimsList.size() + "件");
                }
            }

            if (targetTimsList.size() != 0) {
                for (TInspectionMeterSvr tims : targetTimsList) {
                    String latestInspYmdHm = DateUtility.changeDateFormat(tims.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                    // 「未完了」 かつ 「対象レコードの最新検針日時 <= 比較元レコードの最新検針日時」 かつ 「現在日時 <= (対象レコードの最新検針日時 + リトライ猶予時間)」の場合
                    if (BigDecimal.ZERO.equals(tims.getEndFlg())
                            && getAddDate(latestInspYmdHm, 0).getTime() <= originalLatestInspDate.getTime()
                            && currentDate.getTime() <= getAddDate(latestInspYmdHm, SmsBatchConstants.INSPECTION_RETRY_HOUR).getTime()) {

                        // 予約検針実行で自動検針を比較する場合
                        if (SmsBatchConstants.INSP_KIND.AUTO.getVal().equals(tims.getInspType())) {
                            batchLogger.info(this.batchName.concat(" 「自動検針」かつ「未完了」かつ「最新検針日時がリトライ猶予時間以内」( dev_id = " + meterInfo.getDevId()
                            + " meter_mng_id = " + meterInfo.getMeterMngId()
                            + " [現在日時]" + new Timestamp(currentDate.getTime())
                            + " [予約検針最新検針日時]" + originalLatestInspDate
                            + " [自動検針最新検針日時]" + latestInspYmdHm));
                        }

                        // 自動検針実行で予約検針を比較する場合
                        if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(tims.getInspType())) {
                            batchLogger.info(this.batchName.concat(" 「予約検針」かつ「未完了」かつ「最新検針日時がリトライ猶予時間以内」( dev_id = " + meterInfo.getDevId()
                            + " meter_mng_id = " + meterInfo.getMeterMngId()
                            + " [現在日時]" + new Timestamp(currentDate.getTime())
                            + " [自動検針最新検針日時]" + originalLatestInspDate
                            + " [予約検針最新検針日時]" + latestInspYmdHm));
                        }

                        // 日報データ要求及び未完了データの登録
                        incomplateProc(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, getYMD(targetYmdHm), adjustDate, latestMonth.getInspMonthNo(), latestDate);
                        return false;
                    }
                }
            }

            // 登録済み自動検針結果 取得
            TInspectionMeterSvr tims = dao.findAutoInspMeterSvr(
                    meterInfo.getDevId(),
                    meterInfo.getMeterMngId(),
                    getYear(adjustDate),
                    getMonth(adjustDate),
                    latestMonth.getInspMonthNo());
            // 登録済みチェック
            if (tims != null) {
                // 登録済みデータあり

                // 完了済みチェック
                String latestInspYmdHm = DateUtility.changeDateFormat(tims.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                // 完了済み かつ 検針年月日時が一致する場合は次のメータへ処理スキップ
                if (BigDecimal.ONE.equals(tims.getEndFlg()) && targetYmdHm.equals(latestInspYmdHm)) {
                    // 完了レコード 且つ 処理対象の検針年月日時 と 最新検針日時 が一致
                    return false;
                }

                // 未完了データ削除
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (entityManager.getTransaction().isActive()) {
                    this.dao.removeAutoInspMeterSvr(
                            meterInfo.getDevId(),
                            getYear(adjustDate),
                            getMonth(adjustDate),
                            meterInfo.getMeterMngId(),
                            latestMonth.getInspMonthNo());
                    if (entityManager.getTransaction().getRollbackOnly()) {
                        entityManager.getTransaction().rollback();
                    } else {
                        entityManager.getTransaction().commit();
                    }
                }
                entityManager.clear();
            }

            // 前回の検針値と検針日のデータ不正合チェック
            if (prevLatestVal != null && prevDate == null) {
                batchLogger.info(this.batchName.concat(" 最新検針値日時取得エラー( dev_id = " + meterInfo.getDevId()
                        + " meter_mng_id = " + meterInfo.getMeterMngId()));
                // 未完了データINSERT
                insertAutoInsp(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, adjustDate, latestMonth.getInspMonthNo(), BigDecimal.ZERO, latestDate);
                return false;
            }

            batchLogger.info(this.batchName.concat(" 前回・前々回の検針データ取得( prev_latest_val = " + prevLatestVal
                    + " prev_date = " + prevDate
                    + " prev_latest_val2 = " + prevLatestVal2
                    + " prev_date2 = " + prevDate2));
            // 終了日・開始日算出(yyyyMMddHHmm)
            String endDay = targetYmdHm;
            String startDay = getStartDate(prevDate, endDay);

            // 日報取得日時(yyyyMMddHHmm)
            String v_get_date = targetYmdHm;

            // 最新検針値取得
            BigDecimal latestVal = null;
            // 日報データ から 検針実施日 の DMV値 を取得
            latestVal = dao.getLoadSurveyDMVKWH(meterInfo.getDevId(), meterInfo.getMeterMngId(), v_get_date);

            // 少数、整数確認
            if (isChkInt) {
                latestVal = getScaleValue(latestVal);
                prevLatestVal = getScaleValue(prevLatestVal);
                prevLatestVal2 = getScaleValue(prevLatestVal2);
            }

            // 前回使用量算出
            BigDecimal prevUseVal = BigDecimal.ZERO;
            if (prevLatestVal != null && prevLatestVal2 != null) {
                BigDecimal calcWk = getUseValue(prevLatestVal, prevLatestVal2, multiRate);
                // 超過が起きた場合
                if (calcWk.compareTo(BigDecimal.valueOf(9999999.9)) > 0) {
                    batchLogger.info(this.batchName.concat(" 前回使用量超過 ( dev_id = " + meterInfo.getDevId()
                    + " meter_mng_id = " + meterInfo.getMeterMngId()
                    + ") 収集日時：" + startDay + "～" + endDay
                    + " 使用量：" + calcWk));
                    // エラーデータINSERT
                    insertAutoInsp(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, adjustDate, latestMonth.getInspMonthNo(), BigDecimal.valueOf(2), latestDate);
                    return false;

                } else {
                    prevUseVal = calcWk;
                }
            }

            // 水道メーターなら逆流を確認する(水道メーターはmeterId PTXから始まる 又は dev_id TWから始まる)
            if((meterInfo.getMeterId().startsWith("PXT") || meterInfo.getDevId().startsWith("TW")) && latestVal != null && prevLatestVal != null) {

                // 閾値<= 今回検針値 <= 前回検針値の場合は逆流発生
                if(reverseJud(latestVal, prevLatestVal)) {

                    // 逆流が起こった場合は使用量はブランク

                    // 検針結果データの登録(不完全データなのでinspFlg=falseを返却)
                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    if (entityManager.getTransaction().isActive()) {
                        TInspectionMeterSvr t = new TInspectionMeterSvr();
                        TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                        pk.setDevId(meterInfo.getDevId());
                        pk.setMeterMngId(meterInfo.getMeterMngId());
                        pk.setInspYear(getYear(adjustDate));
                        pk.setInspMonth(Integer.valueOf(getMonth(adjustDate)).toString());
                        pk.setInspMonthNo(latestMonth.getInspMonthNo());
                        t.setId(pk);

                        t.setRecDate(serverDateTime);
                        if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                            t.setRecMan("make_schedule_insp_data");
                        } else {
                            t.setRecMan("make_auto_insp_data");
                        }
                        t.setInspType(inspType); // "a":自動検針  "s":予約検針
                        t.setLatestInspVal(latestVal);
                        t.setPrevInspVal(prevLatestVal);
                        t.setPrevInspVal2(prevLatestVal2);
                        t.setMultipleRate(multiRate);
                        t.setLatestUseVal(null);
                        t.setPrevUseVal(prevUseVal);
                        t.setUsePerRate(null);
                        t.setLatestInspDate(latestDate);
                        t.setPrevInspDate((Timestamp) prevDate);
                        t.setPrevInspDate2((Timestamp) prevDate2);
                        t.setEndFlg(BigDecimal.ZERO);

                        t.setCreateUserId(Long.valueOf(0));
                        t.setCreateDate(serverDateTime);
                        t.setUpdateUserId(Long.valueOf(0));
                        t.setUpdateDate(serverDateTime);

                        dao.createAutoInspMeterSvr(t);
                        if (entityManager.getTransaction().getRollbackOnly()) {
                            entityManager.getTransaction().rollback();
                        } else {
                            entityManager.getTransaction().commit();
                        }
                    }
                    entityManager.clear();
                    return inspFlg;
                }
            }

            // 今回使用量算出
            BigDecimal latestUseVal = BigDecimal.ZERO;
            if (latestVal != null && prevLatestVal != null) {
                BigDecimal calcWk = getUseValue(latestVal, prevLatestVal, multiRate);
                // 超過が起きた場合
                if (calcWk.compareTo(BigDecimal.valueOf(9999999.9)) > 0) {
                    batchLogger.info(this.batchName.concat(" 今回使用量超過 ( dev_id = " + meterInfo.getDevId()
                    + " meter_mng_id = " + meterInfo.getMeterMngId()
                    + ") 収集日時：" + startDay + "～" + endDay
                    + " 使用量：" + calcWk));
                    // エラーデータINSERT
                    insertAutoInsp(meterInfo.getDevId(), meterInfo.getMeterMngId(), inspType, adjustDate, latestMonth.getInspMonthNo(), BigDecimal.valueOf(2), latestDate);
                    return false;

                } else {
                    latestUseVal = calcWk;
                }
            }

            // 使用料率算出
            BigDecimal usePerRate = getUsePerValue(latestUseVal, prevUseVal);

            batchLogger.info(this.batchName.concat(" 最新検針値取得 ( dev_id = " + meterInfo.getDevId()
                    + " meter_mng_id = " + meterInfo.getMeterMngId()
                    + " get_date = " + v_get_date
                    + " latest_val = " + latestVal));

            // 検針結果データの登録
            inspFlg = true;
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            if (entityManager.getTransaction().isActive()) {
                TInspectionMeterSvr t = new TInspectionMeterSvr();
                TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                pk.setDevId(meterInfo.getDevId());
                pk.setMeterMngId(meterInfo.getMeterMngId());
                pk.setInspYear(getYear(adjustDate));
                pk.setInspMonth(Integer.valueOf(getMonth(adjustDate)).toString());
                pk.setInspMonthNo(latestMonth.getInspMonthNo());
                t.setId(pk);

                t.setRecDate(serverDateTime);
                if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                    t.setRecMan("make_schedule_insp_data");
                } else {
                    t.setRecMan("make_auto_insp_data");
                }
                t.setInspType(inspType); // "a":自動検針  "s":予約検針
                t.setLatestInspVal(latestVal);
                t.setPrevInspVal(prevLatestVal);
                t.setPrevInspVal2(prevLatestVal2);
                t.setMultipleRate(multiRate);
                t.setLatestUseVal(latestUseVal);
                t.setPrevUseVal(prevUseVal);
                t.setUsePerRate(usePerRate);
                t.setLatestInspDate(latestDate);
                t.setPrevInspDate((Timestamp) prevDate);
                t.setPrevInspDate2((Timestamp) prevDate2);
                t.setEndFlg(BigDecimal.ONE);

                t.setCreateUserId(Long.valueOf(0));
                t.setCreateDate(serverDateTime);
                t.setUpdateUserId(Long.valueOf(0));
                t.setUpdateDate(serverDateTime);

                dao.createAutoInspMeterSvr(t);
                if (entityManager.getTransaction().getRollbackOnly()) {
                    entityManager.getTransaction().rollback();
                } else {
                    entityManager.getTransaction().commit();
                }
            }
            entityManager.clear();

            // 最新検針値未取得時、不完全データを登録
            if (latestVal == null) {
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (entityManager.getTransaction().isActive()) {
                    TWkInspectionIncomp t = new TWkInspectionIncomp();
                    TWkInspectionIncompPK pk = new TWkInspectionIncompPK();

                    pk.setDevId(meterInfo.getDevId());
                    pk.setMeterMngId(meterInfo.getMeterMngId());
                    pk.setInspYear(getYear(adjustDate));
                    pk.setInspMonth(Integer.valueOf(getMonth(adjustDate)).toString());
                    pk.setInspMonthNo(latestMonth.getInspMonthNo());
                    t.setId(pk);

                    t.setRecDate(serverDateTime);
                    if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                        t.setRecMan("make_schedule_insp_data");
                    } else {
                        t.setRecMan("make_auto_insp_data");
                    }
                    t.getId().setInspDate(v_get_date);
                    t.setEndFlg(BigDecimal.ZERO);

                    t.setCreateUserId(Long.valueOf(0));
                    t.setCreateDate(serverDateTime);
                    t.setUpdateUserId(Long.valueOf(0));
                    t.setUpdateDate(serverDateTime);
                    dao.createIncomplateAutoInspMeterSvr(t);
                    if (entityManager.getTransaction().getRollbackOnly()) {
                        entityManager.getTransaction().rollback();
                    } else {
                        entityManager.getTransaction().commit();
                    }
                }
                entityManager.clear();
            }

        } catch (Exception e) {
            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                entityManager.clear();
            }
        }

        return inspFlg;
    }

    /**
     * 逆流判定
     * @param latestVal 今回検針値
     * @param prevLatestVal 前回検針値
     * @return true：逆流 false:正常
     */
    private boolean reverseJud(BigDecimal latestVal, BigDecimal prevLatestVal) {
        //比較するためにdouble型へ変換
        double d_latestVal = latestVal.doubleValue();
        double d_prevLatestVal = prevLatestVal.doubleValue();

        if(0 < (d_prevLatestVal - d_latestVal) && (d_prevLatestVal - d_latestVal) < 100) {
            return true;
        }
        else if(((0 <= d_prevLatestVal && d_prevLatestVal <= 100) && (9900 <= d_latestVal && d_latestVal <= 10000)) && (0 < (10000 + d_prevLatestVal - d_latestVal) && (10000 + d_prevLatestVal - d_latestVal) < 100)) {
            return true;
        }
        else if(((0 <= d_prevLatestVal && d_prevLatestVal <= 100) && (99000 <= d_latestVal && d_latestVal <= 100000)) && (0 < (100000 + d_prevLatestVal - d_latestVal) && (100000 + d_prevLatestVal - d_latestVal) < 100)) {
            return true;
        }
        else if(((0 <= d_prevLatestVal && d_prevLatestVal <= 100) && (990000 <= d_latestVal && d_latestVal <= 1000000)) && (0 < (1000000 + d_prevLatestVal - d_latestVal) && (1000000 + d_prevLatestVal - d_latestVal) < 100)) {
            return true;
        }

        return false;
    }

    /**
     * 最新検針月連番 取得.
     * ※自動検針用。予約検針は対象外。
     *
     * (自動検針用).
     * ※連番 採番ルール.
     * 　①DEV_ID (装置ID)
     * 　②INSP_YEAR (検針年)
     * 　③INSP_MONTH (検針月)
     * 　④LATEST_INSP_DATE (最新検針値_日時)　　※年月日時分 まで比較 (秒は比較対象外)
     * 　⑤INSP_TYPE が "a"（自動検針）
     * 　⑥ ①～⑤の条件で連番が取得できない場合(新規の自動検針)は ①～③の条件で取得した"連番の最大値"+1 を使用する
     *
     * ※自動検針と検針日時が重なった場合でも、連番は自動検針とは別の番号を採番します。
     *
     * @param devId 装置ID
     * @param inspType 検針種別  自動検針:SmsBatchConstants.INSP_KIND.AUTO.getVal()  予約検針:SmsBatchConstants.INSP_KIND.SCHEDULE.getVal()
     * @param adjustDate 自動検針実施日(補正)(yyyyMMddHH)
     * @param targetYmdHm 自動検針実施日(yyyyMMddHHmm)  ※mmは 自動検針 では"00"固定、予約検針 では "00" または "30"
     * @return AutoInspLatestMonthNoResultSet
     */
    public AutoInspLatestMonthNoResultSet getLatestMonthNo(String devId, String inspType, String adjustDate, String targetYmdHm) {
        long registeredMonthNo = 0L;   // 過去に採番された枝番 (自動検針実施日が一致するもの)  ※0:過去に採番された連番がない(新規の自動検針)
        long maxMonthNo = 0L;          // 枝番の最大値

        // 検針結果データ(サーバ用)データ ※検索条件: 装置ID, 検針年, 検針月 ※採番ルール:①②③を満たしたデータのリスト
        List<AutoInspMeterSvrResultSet> meterSvrList = dao.getAutoInspMeterSvr(devId, getYear(adjustDate), getMonth(adjustDate));

        for (AutoInspMeterSvrResultSet info1 : meterSvrList) {
            long wk = info1.getInspMonthNo();
            String latestInspDateTmp = DateUtility.changeDateFormat(info1.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

            // 採番ルール:④⑤
            if (targetYmdHm.equals(latestInspDateTmp) && inspType.equals(info1.getInspType())) {
                if (registeredMonthNo < wk) {
                    registeredMonthNo = wk;
                }
            }

            // 枝番 の最大値 取得
            if (maxMonthNo < wk) {
                maxMonthNo = wk;
            }
        }

        // 採番ルール:⑥
        long monthNo = (registeredMonthNo == 0L ? maxMonthNo + 1 : registeredMonthNo);

        return new AutoInspLatestMonthNoResultSet(monthNo, registeredMonthNo > 0L);
    }

    /**
     * 実施月の補正
     * ※例外: 1日0時の場合の検針月を前月にする
     * @param Date execDayModWk
     * @return 書式:yyyyMMddHH
     */
    public String getAdjustRetryDate(Date execDayModWk) {
        String dateWk = DateUtility.changeDateFormat(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHH);
        String yearWk = getYear(dateWk);
        String monthWk = getMonth(dateWk);
        String dayhourWk = getDayHour(dateWk);

        // 1日0時の場合の検針月を前月に変更
        // 1月1日0時の場合は前年の検針月が12月になるよう変更
        if (dayhourWk.equals("0100")) {
            if (monthWk.equals("01")) {
                int ynum = Integer.valueOf(yearWk) - 1;
                yearWk = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, ynum);
                monthWk = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, 12);
            } else {
                int mnum = Integer.valueOf(monthWk) - 1;
                monthWk = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, mnum);
            }
        }
        String ret = yearWk + monthWk + dayhourWk;
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 年文字列
     */
    public String getYear(String targetDate) {
        String ret = targetDate.substring(0, 4);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 月文字列
     */
    public String getMonth(String targetDate) {
        String ret = targetDate.substring(4, 6);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 年月日時文字列
     */
    public String getYMDH(String targetDate) {
        String ret = targetDate.substring(0, 10);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 分文字列
     */
    public String getMin(String targetDate) {
        String ret = targetDate.substring(10, 12);
        return ret;
    }

    /**
     * 時間を加算した日付生成
     *
     * @param String execDayModWk  書式:yyyyMMddHH or yyyyMMddHHmm
     * @param String waitHour
     * @return Date
     */
    private Date getAddDate(String execDayModWk, int waitHour) {
        Date date_mod = DateUtility.conversionDate(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHH);

        if (date_mod == null) {
            date_mod = DateUtility.conversionDate(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        }
        date_mod = DateUtility.plusHour(date_mod, waitHour);

        return date_mod;
    }

    /**
     * 不完全な日報データリスト取得かつ未完了データインサート
     *
     * @param devId String
     * @param meterMngId Long
     * @param inspType 検針種別  SmsBatchConstants.INSP_KIND.AUTO.getVal() または SmsBatchConstants.INSP_KIND.SCHEDULE.getVal()
     * @param targetYmd String
     * @param adjustDate String
     * @param monthNo String
     * @param latestDate 最新検針値_日時
     */
    private void incomplateProc(String devId, Long meterMngId, String inspType, String targetYmd, String adjustDate, Long monthNo, Timestamp latestDate) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        if (entityManager.getTransaction().isActive()) {
            // コマンド予約(使用量)
            String com = SmsConstants.CMD_KIND.GET_DLSDATA.getVal();
            Long count = this.dao.getCountCommand(devId, targetYmd, com);
            if (count == 0) {
                this.dao.removeCommand(devId, targetYmd, com);
                this.dao.createCommand(devId, targetYmd, com, serverDateTime, inspType);
            }
            // コマンド予約(指針値)
            com = SmsConstants.CMD_KIND.GET_DMVDATA.getVal();
            count = this.dao.getCountCommand(devId, targetYmd, com);
            if (count == 0) {
                this.dao.removeCommand(devId, targetYmd, com);
                this.dao.createCommand(devId, targetYmd, com, serverDateTime, inspType);
            }
            // 未完了データINSERT
            this.dao.removeAutoInspMeterSvr(
                    devId,
                    getYear(adjustDate),
                    getMonth(adjustDate),
                    meterMngId,
                    monthNo);

            TInspectionMeterSvr t = new TInspectionMeterSvr();
            TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
            pk.setDevId(devId);
            pk.setMeterMngId(meterMngId);
            pk.setInspYear(getYear(adjustDate));
            pk.setInspMonth(Integer.valueOf(getMonth(adjustDate)).toString());
            pk.setInspMonthNo(monthNo);
            t.setId(pk);
            t.setRecDate(serverDateTime);
            if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                t.setRecMan("makeScheduleInspData");
            } else {
                t.setRecMan("makeAutoInspData");
            }
            t.setInspType(inspType); // "a":自動検針  "s":予約検針
            t.setLatestInspDate(latestDate);
            t.setEndFlg(BigDecimal.ZERO);

            t.setCreateUserId(Long.valueOf(0));
            t.setCreateDate(serverDateTime);
            t.setUpdateUserId(Long.valueOf(0));
            t.setUpdateDate(serverDateTime);

            dao.createAutoInspMeterSvr(t);

            if (entityManager.getTransaction().getRollbackOnly()) {
                entityManager.getTransaction().rollback();
            } else {
                entityManager.getTransaction().commit();
            }
        }
        entityManager.clear();

    }

    /**
     * 開始日算出
     *
     * @param Date   latestDate
     * @param String endDay(yyyyMMddHHmm)
     * @return 開始日(yyyyMMddHHmm)
     */
    private String getStartDate(Date latestDate, String endDay) {
        String startDay = DateUtility.changeDateFormat(latestDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        if (startDay != null) {
            String dayWork = getMin(startDay);
            if (Integer.valueOf(dayWork) > 30) {
                dayWork = "30";
            } else {
                dayWork = "00";
            }
            startDay = getYMDH(startDay) + dayWork;
            Date dateWk = DateUtility.plusMinute(DateUtility.conversionDate(startDay, DateUtility.DATE_FORMAT_YYYYMMDDHHMM), 30);
            startDay = DateUtility.changeDateFormat(dateWk, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        } else {
            Date dateWk = DateUtility.plusMonth(DateUtility.conversionDate(endDay, DateUtility.DATE_FORMAT_YYYYMMDDHHMM), -1);
            dateWk = DateUtility.plusMinute(dateWk, 30);
            startDay = DateUtility.changeDateFormat(dateWk, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        }
        return startDay;
    }

    /**
     * 使用量算出
     *
     * @param BigDecimal val1
     * @param BigDecimal val2
     * @param BigDecimal rate
     * @return 使用量
     */
    private BigDecimal getUseValue(BigDecimal val1, BigDecimal val2, BigDecimal rate) {
        BigDecimal calcWk = BigDecimal.ZERO;
        if (val1.compareTo(val2) < 0) {
            int precision = val2.precision() - val2.scale();
            calcWk = BigDecimal.valueOf(10);
            calcWk = calcWk.pow(precision);
            calcWk = calcWk.add(val1);
            calcWk = calcWk.subtract(val2);
            calcWk = calcWk.multiply(rate);
        } else {
            calcWk = calcWk.add(val1);
            calcWk = calcWk.subtract(val2);
            calcWk = calcWk.multiply(rate);
        }
        return calcWk;
    }

    /**
     * 使用率算出
     *
     * @param BigDecimal latestUseVal
     * @param BigDecimal prevUseVal
     * @return 使用率
     */
    private BigDecimal getUsePerValue(BigDecimal latestUseVal, BigDecimal prevUseVal) {
        BigDecimal usePerRate = BigDecimal.ZERO;
        if (prevUseVal.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal calcWk = latestUseVal.divide(prevUseVal, 4, RoundingMode.HALF_UP);
            calcWk = calcWk.multiply(BigDecimal.valueOf(100));
            if (calcWk.compareTo(BigDecimal.valueOf(1000)) >= 0) {
                usePerRate = BigDecimal.valueOf(999.99);
            } else {
                usePerRate = calcWk.setScale(2, RoundingMode.HALF_UP);
            }
        }
        return usePerRate;
    }

    /**
     * 検針結果データINSERT(未完了/エラー)
     *
     * @param devId String
     * @param meterMngId Long
     * @param inspType 検針種別  SmsBatchConstants.INSP_KIND.AUTO.getVal() または SmsBatchConstants.INSP_KIND.SCHEDULE.getVal()
     * @param adjustDate String
     * @param monthNo String
     * @param endFlg BigDecimal
     * @param latestDate 最新検針値_日時
     */
    private void insertAutoInsp(String devId, Long meterMngId, String inspType, String adjustDate, Long monthNo, BigDecimal endFlg, Timestamp latestDate) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        if (entityManager.getTransaction().isActive()) {
            TInspectionMeterSvr t = new TInspectionMeterSvr();
            TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
            pk.setDevId(devId);
            pk.setMeterMngId(meterMngId);
            pk.setInspYear(getYear(adjustDate));
            pk.setInspMonth(Integer.valueOf(getMonth(adjustDate)).toString());
            pk.setInspMonthNo(monthNo);
            t.setId(pk);
            t.setRecDate(serverDateTime);
            if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
                t.setRecMan("makeScheduleInspData");
            } else {
                t.setRecMan("makeAutoInspData");
            }
            t.setInspType(inspType); // "a":自動検針  "s":予約検針
            t.setLatestInspDate(latestDate);
            t.setEndFlg(endFlg);

            t.setCreateUserId(Long.valueOf(0));
            t.setCreateDate(serverDateTime);
            t.setUpdateUserId(Long.valueOf(0));
            t.setUpdateDate(serverDateTime);

            dao.createAutoInspMeterSvr(t);

            if (entityManager.getTransaction().getRollbackOnly()) {
                entityManager.getTransaction().rollback();
            } else {
                entityManager.getTransaction().commit();
            }
        }
        entityManager.clear();
    }

    /**
     * 少数なし数値取得(切捨て)
     *
     * @param BigDecimal val
     * @return 整数
     */
    private BigDecimal getScaleValue(BigDecimal val) {
        BigDecimal ret = null;
        if (val != null) {
            ret = val.setScale(0, RoundingMode.DOWN);
        }
        return ret;
    }

    /**
     * 不完全な日報データリストから対象メータがあるか？
     *
     * @param incompList List<Long>
     * @param meterMngId メーター管理番号
     * @return 検索結果  true:あり / false:なし
     */
    private Boolean isMeterIncomplate(List<Long> incompList, Long meterMngId) {
        Boolean ret = false;

        if (incompList != null && incompList.size() > 0) {
            for (Long mngId : incompList) {
                if (mngId.equals(meterMngId)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 日時文字列
     */
    private String getDayHour(String targetDate) {
        String ret = targetDate.substring(6, 10);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 年月日文字列
     */
    private String getYMD(String targetDate) {
        String ret = targetDate.substring(0, 8);
        return ret;
    }

    /**
     * 桁数の取得
     * @param val
     * @return メーターの桁数（4桁か5桁かの判別に使う)
     */
    private int getPrecision(BigDecimal val) {
        return val.precision() - val.scale();
    }

}
