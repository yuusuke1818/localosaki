package jp.co.osaki.osol.api.dao.sms.manualinsp.exe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.exe.ManualInspExeParameter;
import jp.co.osaki.osol.api.result.sms.manualinsp.exe.ManualInspExeResult;
import jp.co.osaki.osol.api.resultdata.sms.manualinsp.exe.ManualInspExeResultData;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetBuildingNameDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetCountInspInCompServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetDevNameServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetLatestInspMonthNoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetMeterLatestInspDateServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.GetMeterTypeNameServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.MBuildingSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.MMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.MPauseMailServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.TDayLoadSurveyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.TInspectionMeterSvrServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.TWkInspectionIncompServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.listAlertMailServiceDaoImpl;
import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.osol.entity.TWkInspectionIncomp;
import jp.co.osaki.osol.entity.TWkInspectionIncompPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;

/**
 * 任意検針 任意検針実行 Daoクラス.
 *
 * @author tominaga.d
 */
@Stateless
public class ManualInspExeDao extends OsolApiDao<ManualInspExeParameter> {

    // 任意検針実施チェック
    private final GetMeterLatestInspDateServiceDaoImpl meterLatestInspDateDaoImpl;
    private final GetCountInspInCompServiceDaoImpl countInspInCompDaoImpl;
    private final GetMeterTypeNameServiceDaoImpl meterTypeNameDaoImpl;

    // 任意検針メール送信
    private final GetBuildingNameDaoImpl buildingNameDaoImpl;
    private final GetDevNameServiceDaoImpl devNameDaoImpl;
    private final listAlertMailServiceDaoImpl listMailDaoImpl;

    // 任意検針実行
    private final GetLatestInspMonthNoServiceDaoImpl latestInspMonthNoDaoImpl;
    private final MBuildingSmsServiceDaoImpl buildingSmsDaoImpl;
    private final MMeterServiceDaoImpl meterDaoImpl;
    private final TDayLoadSurveyServiceDaoImpl loadSurveyDaoImpl;
    private final TInspectionMeterSvrServiceDaoImpl inspMeterSvrDaoImpl;
    private final TWkInspectionIncompServiceDaoImpl wkInspIncompDaoImpl;

    // メール停止
    private final MPauseMailServiceDaoImpl mPauseMailServiceDaoImpl;


    public ManualInspExeDao() {
        // 任意検針実施チェック
        meterLatestInspDateDaoImpl = new GetMeterLatestInspDateServiceDaoImpl();
        countInspInCompDaoImpl = new GetCountInspInCompServiceDaoImpl();
        meterTypeNameDaoImpl = new GetMeterTypeNameServiceDaoImpl();

        // 任意検針メール送信
        buildingNameDaoImpl = new GetBuildingNameDaoImpl();
        devNameDaoImpl = new GetDevNameServiceDaoImpl();
        listMailDaoImpl = new listAlertMailServiceDaoImpl();

        // 任意検針実行
        latestInspMonthNoDaoImpl = new GetLatestInspMonthNoServiceDaoImpl();
        buildingSmsDaoImpl = new MBuildingSmsServiceDaoImpl();
        meterDaoImpl = new MMeterServiceDaoImpl();
        loadSurveyDaoImpl = new TDayLoadSurveyServiceDaoImpl();
        inspMeterSvrDaoImpl = new TInspectionMeterSvrServiceDaoImpl();
        wkInspIncompDaoImpl = new TWkInspectionIncompServiceDaoImpl();

        // メール停止
        mPauseMailServiceDaoImpl = new MPauseMailServiceDaoImpl();

    }

    // 未使用
    @Override
    public ManualInspExeResult query(ManualInspExeParameter parameter) throws Exception {
        return null;
    }

    /**
     * 前回検針データ未完了チェック処理.
     *
     * @param String devId
     * @param Long meterMngId
     * @param Long   meterType
     * @return 前回検針データ未完了
     */
    public ManualInspExeResultData getDisableManualInsp(String devId, Long meterMngId, Long meterType) throws Exception {
        ManualInspExeResultData resinfo = null;
        // メーター種別毎の前回検針年月、連番を取得する
        TInspectionMeterSvr svr = getPrevDateYM(devId, meterMngId, meterType);
        if (svr != null) {
            String year = svr.getId().getInspYear();
            String month = svr.getId().getInspMonth();
            Long month_no = svr.getId().getInspMonthNo();
            // 前回検針データの未完了件数を取得
            Long count = getCountPrevInspData(devId, meterMngId, year, month, month_no, meterType);
            if (count != null && count > 0) {
                // メーター種別名取得
                String typeName = getMeterTypeNameForDevId(devId, "1", meterType);
                if (typeName != null && !typeName.equals("")) {
                    resinfo = new ManualInspExeResultData(devId, typeName);
                }
            }
        }
        return resinfo;
    }

    /**
     * 検針日時の12時間前（リトライの時間分）までに自動か予約の検針未完了のデータを取得.
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 月検針連番
     * @param svDate 実施日時
     * @return 検針結果データ(サーバ用)
     */
    public TInspectionMeterSvr getInspectionMeterSvr(
            String devId, Long meterMngId,
            String inspYear, String inspMonth, Long inspMonthNo, Timestamp svDate) throws Exception {

        // 検針日時の12時間前（リトライの時間分）までに自動か予約の検針未完了のデータが存在した場合、検針確定せずにメッセージを表示する。
        // 登録済み検針結果 取得
        TInspectionMeterSvr tInspectionMeterSvr = new TInspectionMeterSvr();
        TInspectionMeterSvrPK tInspectionMeterSvrPK = new TInspectionMeterSvrPK();

        tInspectionMeterSvrPK.setDevId(devId);
        tInspectionMeterSvrPK.setMeterMngId(meterMngId);
        tInspectionMeterSvrPK.setInspYear(inspYear);
        tInspectionMeterSvrPK.setInspMonth(inspMonth);
        tInspectionMeterSvr.setId(tInspectionMeterSvrPK);
        List<TInspectionMeterSvr> timsList = getResultList(inspMeterSvrDaoImpl, tInspectionMeterSvr);

        // 登録済みチェック
        if (timsList.size() != 0) {
            for (TInspectionMeterSvr tims : timsList) {
                // 登録済みデータあり
                // 「未完了」かつ「最新検針日時がリトライ猶予時間以内」の場合
                String latestInspYmdH = DateUtility.changeDateFormat(tims.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHH);
                String targetDate = DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

                if (BigDecimal.ZERO.equals(tims.getEndFlg())
                        && getAddDate(latestInspYmdH, 0).getTime() <= getAddDate(targetDate, 0).getTime()
                        && getAddMinute(targetDate, -30).getTime() <= getAddDate(latestInspYmdH, SmsConstants.INSPECTION_RETRY_HOUR).getTime()) {

                    // 自動検針のレコードで「未完了」かつ「最新検針日時がリトライ猶予時間以内」の場合
                    if (SmsConstants.INSP_KIND.AUTO.getVal().equals(tims.getInspType())) {
                        daoLogger.info(this.getClass().getName().concat(" 「自動検針」かつ「未完了」かつ「最新検針日時がリトライ猶予時間以内」( dev_id = " + devId
                        + " meter_mng_id = " + meterMngId
                        + " target_date = " + targetDate));

                        return tims;
                    }

                    // 予約検針のレコードで「未完了」かつ「最新検針日時がリトライ猶予時間以内」の場合
                    if (SmsConstants.INSP_KIND.SCHEDULE.getVal().equals(tims.getInspType())) {
                        daoLogger.info(this.getClass().getName().concat(" 「予約検針」かつ「未完了」かつ「最新検針日時がリトライ猶予時間以内」( dev_id = " + devId
                        + " meter_mng_id = " + meterMngId
                        + " target_date = " + targetDate));

                        return tims;
                    }

                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 任意検針実行.
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param chkInt 小数、整数確認フラグ（0、null：小数あり、1：整数のみ）
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 月検針連番
     * @param svDate 実施日時
     * @param loginCorpId String
     * @param loginPersonId String
     * @return 任意検針実行結果 true：正常 false：異常
     */
    public Boolean exeManualInspMeter(
            String devId, Long meterMngId, BigDecimal chkInt,
            String inspYear, String inspMonth, Long inspMonthNo, Timestamp svDate,
            String loginCorpId, String loginPersonId) throws Exception {

        // ログインユーザーIDを取得
        Long loginUserId = getMPerson(loginCorpId, loginPersonId).getUserId();

        // メーター情報取得
        MMeter meter = getMeter(devId, meterMngId);

        // メーター情報あり
        if (meter != null) {

            // 検針日時の生成
            String latest_date_str = getLoadSurveyGetDate(devId, meterMngId);
            // 乗率算出
            BigDecimal multiRate = meter.getMulti();
            // 検針日時が取得できない または 乗率が0またはnullの場合は、検針結果未完了でInsert
            if (latest_date_str == null || latest_date_str.isEmpty() || multiRate == null || multiRate.compareTo(BigDecimal.ZERO) == 0) {
                // 検針結果未完了インサート
                TInspectionMeterSvr target = new TInspectionMeterSvr();
                TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                pk.setDevId(devId);
                pk.setMeterMngId(meterMngId);
                pk.setInspYear(inspYear);
                pk.setInspMonth(inspMonth);
                pk.setInspMonthNo(inspMonthNo);
                target.setId(pk);
                target.setRecDate(svDate);
                target.setRecMan("makeManualInspData");
                target.setInspType("m");
                target.setLatestInspVal(null);
                target.setEndFlg(BigDecimal.ZERO); // 未完了

                target.setCreateUserId(loginUserId);
                target.setCreateDate(svDate);
                target.setUpdateUserId(loginUserId);
                target.setUpdateDate(svDate);
                insertInsp(target);

                return true;
            }
              // 乗率が未登録（m_meter.multi=null）または0の場合、検針未完了(t_inspection_meter_srv.end_flg=0)の検針データを作成するためコメントアウト
//            // 乗率算出
//            BigDecimal multiRate = meter.getMulti();
//            if (multiRate == null || multiRate.compareTo(BigDecimal.ZERO) == 0) {
//                multiRate = BigDecimal.ONE;
//            }
            // 前回検針結果取得
            TInspectionMeterSvr prev = getPrevInspValues(devId, meterMngId);
            // 直前の検針結果のLatestを前回値
            // Prevを前前回値とする
            BigDecimal prevLatestVal = (prev == null ? null : prev.getLatestInspVal());
            BigDecimal prevLatestVal2 = (prev == null ? null : prev.getPrevInspVal());
            Timestamp prevDate = (prev == null ? null : prev.getLatestInspDate());
            Timestamp prevDate2 = (prev == null ? null : prev.getPrevInspDate());

            // 前回の検針値と検針日の差異がった場合の処理
            if (prevLatestVal != null && prevDate == null) {
                // 検針結果未完了インサート
                TInspectionMeterSvr target = new TInspectionMeterSvr();
                TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                pk.setDevId(devId);
                pk.setMeterMngId(meterMngId);
                pk.setInspYear(inspYear);
                pk.setInspMonth(inspMonth);
                pk.setInspMonthNo(inspMonthNo);
                target.setId(pk);
                target.setRecDate(svDate);
                target.setRecMan("makeManualInspData");
                target.setInspType("m");
                target.setEndFlg(BigDecimal.ZERO);

                target.setCreateUserId(loginUserId);
                target.setCreateDate(svDate);
                target.setUpdateUserId(loginUserId);
                target.setUpdateDate(svDate);
                insertInsp(target);
                return true;

            }

            // 最新検針値取得
            BigDecimal latestVal = getLoadSurveyLatestValue(devId, meterMngId, latest_date_str);

            // 少数・整数確認フラグによる補正処理
            if (BigDecimal.ONE.equals(chkInt)) {
                latestVal = getScaleValue(latestVal);
                prevLatestVal = getScaleValue(prevLatestVal);
                prevLatestVal2 = getScaleValue(prevLatestVal2);
            }

            // 今回使用量算出
            BigDecimal latestUseVal = BigDecimal.ZERO;
            if (latestVal != null && prevLatestVal != null) {
                BigDecimal calcWk = getUseValue(latestVal, prevLatestVal, multiRate);
                // 超過が起きた場合
                if (calcWk.compareTo(BigDecimal.valueOf(9999999.9)) > 0) {
                    // エラーデータINSERT
                    TInspectionMeterSvr target = new TInspectionMeterSvr();
                    TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                    pk.setDevId(devId);
                    pk.setMeterMngId(meterMngId);
                    pk.setInspYear(inspYear);
                    pk.setInspMonth(inspMonth);
                    pk.setInspMonthNo(inspMonthNo);
                    target.setId(pk);
                    target.setRecDate(svDate);
                    target.setRecMan("makeManualInspData");
                    target.setInspType("m");
                    target.setEndFlg(BigDecimal.valueOf(2));

                    target.setCreateUserId(loginUserId);
                    target.setCreateDate(svDate);
                    target.setUpdateUserId(loginUserId);
                    target.setUpdateDate(svDate);
                    insertInsp(target);
                    return true;

                } else {
                    latestUseVal = calcWk;
                }
            }

            // 前回使用量算出
            BigDecimal prevUseVal = BigDecimal.ZERO;
            if (prevLatestVal != null && prevLatestVal2 != null) {
                BigDecimal calcWk = getUseValue(prevLatestVal, prevLatestVal2, multiRate);
                // 超過が起きた場合
                if (calcWk.compareTo(BigDecimal.valueOf(9999999.9)) > 0) {
                    // エラーデータINSERT
                    TInspectionMeterSvr target = new TInspectionMeterSvr();
                    TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                    pk.setDevId(devId);
                    pk.setMeterMngId(meterMngId);
                    pk.setInspYear(inspYear);
                    pk.setInspMonth(inspMonth);
                    pk.setInspMonthNo(inspMonthNo);
                    target.setId(pk);
                    target.setRecDate(svDate);
                    target.setRecMan("makeManualInspData");
                    target.setInspType("m");
                    target.setEndFlg(BigDecimal.valueOf(2));

                    target.setCreateUserId(loginUserId);
                    target.setCreateDate(svDate);
                    target.setUpdateUserId(loginUserId);
                    target.setUpdateDate(svDate);
                    insertInsp(target);
                    return true;

                } else {
                    prevUseVal = calcWk;
                }
            }

            // 使用量率算出
            BigDecimal usePerRate = getUsePerValue(latestUseVal, prevUseVal);

            // 検針結果の登録(正常)
            {
                TInspectionMeterSvr target = new TInspectionMeterSvr();
                TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
                pk.setDevId(devId);
                pk.setMeterMngId(meterMngId);
                pk.setInspYear(inspYear);
                pk.setInspMonth(inspMonth);
                pk.setInspMonthNo(inspMonthNo);
                target.setId(pk);
                target.setRecDate(svDate);
                target.setRecMan("makeManualInspData");
                target.setInspType("m");
                target.setEndFlg(BigDecimal.valueOf(2));
                target.setLatestInspVal(latestVal);
                target.setPrevInspVal(prevLatestVal);
                target.setPrevInspVal2(prevLatestVal2);
                target.setMultipleRate(multiRate);
                target.setLatestUseVal(latestUseVal);
                target.setPrevUseVal(prevUseVal);
                target.setUsePerRate(usePerRate);
                Timestamp latestDate = new Timestamp(DateUtility.conversionDate(latest_date_str + "01", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
                target.setLatestInspDate(latestDate);
                target.setPrevInspDate((Timestamp) prevDate);
                target.setPrevInspDate2((Timestamp) prevDate2);
                target.setEndFlg(BigDecimal.ONE);

                target.setCreateUserId(loginUserId);
                target.setCreateDate(svDate);
                target.setUpdateUserId(loginUserId);
                target.setUpdateDate(svDate);

                insertInsp(target);
            }

            // 最新検針結果が取得できていない？
            if (latestVal == null) {
                // 不完全データの登録
                TWkInspectionIncomp target = new TWkInspectionIncomp();
                TWkInspectionIncompPK pk = new TWkInspectionIncompPK();
                pk.setDevId(devId);
                pk.setMeterMngId(meterMngId);
                pk.setInspYear(inspYear);
                pk.setInspMonth(inspMonth);
                pk.setInspMonthNo(inspMonthNo);
                pk.setInspDate(latest_date_str);
                target.setId(pk);
                target.setRecDate(svDate);
                target.setRecMan("makeManualInspData");
                target.setEndFlg(BigDecimal.ZERO);

                target.setCreateUserId(loginUserId);
                target.setCreateDate(svDate);
                target.setUpdateUserId(loginUserId);
                target.setUpdateDate(svDate);
                insertInspcomp(target);
            }

        }
        return true;
    }

    /**
     * メーター種別毎の前回レコード取得
     *
     * @param String devId
     * @param Long meterMngId
     * @param Long   meter_type
     * @return 前回レコード
     */
    private TInspectionMeterSvr getPrevDateYM(String devId,Long meterMngId, Long meter_type) {
        TInspectionMeterSvr ret = null;
        MMeter target = new MMeter();
        MMeterPK pk = new MMeterPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meterMngId);
        target.setId(pk);
        target.setMeterType(meter_type);

        List<Object> targetList = new ArrayList<>();
        Map<String, List<Object>> param = new HashMap<>();
        targetList.add(target);
        param.put("select", targetList);

        List<TInspectionMeterSvr> resList = getResultList(meterLatestInspDateDaoImpl, param);
        if (resList.size() != 0) {
            ret = resList.get(0);
        }
        return ret;
    }

    /**
     * 前回検針データの未完了件数取得
     *
     * @param String devId
     * @param Long meterMngId
     * @param String year
     * @param String month
     * @param Long   month_no
     * @param Long   meter_type
     * @return 未完了件数
     */
    private Long getCountPrevInspData(String devId, Long meterMngId, String year, String month, Long month_no, Long meter_type) {
        Long ret = null;
        TInspectionMeterSvr target = new TInspectionMeterSvr();
        TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meterMngId);
        pk.setInspYear(year);
        pk.setInspMonth(month);
        pk.setInspMonthNo(month_no);
        target.setId(pk);

        List<Object> targetList = new ArrayList<>();
        Map<String, List<Object>> param = new HashMap<>();
        targetList.add(meter_type);
        param.put("meter_type", targetList);

        List<Object> targetList2 = new ArrayList<>();
        targetList2.add(target);
        param.put("meter_svr", targetList2);

        List<Long> resList = getResultList(countInspInCompDaoImpl, param);
        if (resList.size() != 0) {
            ret = resList.get(0);
        }
        return ret;
    }

    /**
     * 前回検針データの未完了件数取得
     *
     * @param String devId
     * @param String devKind
     * @param Long   meter_type
     * @return 未完了件数
     */
    private String getMeterTypeNameForDevId(String devId, String devKind, Long meter_type) {
        String ret = null;

        List<Object> targetList = new ArrayList<>();
        Map<String, List<Object>> param = new HashMap<>();
        targetList.add(devId);
        param.put("dev_id", targetList);

        List<Object> targetList2 = new ArrayList<>();
        targetList2.add(devKind);
        param.put("dev_kind", targetList2);

        List<Object> targetList3 = new ArrayList<>();
        targetList3.add(meter_type);
        param.put("meter_type", targetList3);

        List<String> resList = getResultList(meterTypeNameDaoImpl, param);
        if (resList.size() != 0) {
            ret = resList.get(0);
        }
        return ret;
    }

    /**
     * マンション名称取得
     *
     * @param String devId
     * @return マンション名
     */
    public String getBuildName(String devId) throws Exception {
        String ret = null;

        List<Object> targetList = new ArrayList<>();
        Map<String, List<Object>> param = new HashMap<>();
        targetList.add(devId);
        param.put("dev_id", targetList);

        List<String> resList = getResultList(buildingNameDaoImpl, param);
        if (resList.size() != 0) {
            ret = resList.get(0);
        }
        return ret;

    }

    /**
     * 装置名称取得
     *
     * @param String devId
     * @return 装置名称
     */
    public String getDevName(String devId) throws Exception {
        String ret = "";

        MDevPrm target = new MDevPrm();
        target.setDevId(devId);

        MDevPrm res = find(devNameDaoImpl, target);
        if (res != null) {
            ret = res.getName();
        }
        return ret;

    }

    /**
     * 送信先リスト取得
     *
     * @param String devId
     * @return 送信先リスト
     */
    public List<String> getMailList(String devId) throws Exception {
        List<String> ret = new ArrayList<>();
        MAlertMail target = new MAlertMail();
        MAlertMailPK pk = new MAlertMailPK();
        pk.setDevId(devId);
        target.setId(pk);
        List<MAlertMail> resList = getResultList(listMailDaoImpl, target);
        for (MAlertMail info : resList) {
            ret.add(info.getEmail());
        }
        return ret;
    }

    /**
     * 少数・整数確認フラグ取得
     *
     * @param String corpId
     * @param Long   builingId
     * @return 少数・整数確認フラグ
     */
    public BigDecimal getChkInt(String corpId, Long builingId) throws Exception {
        BigDecimal ret = new BigDecimal(0);
        MBuildingSms target = new MBuildingSms();
        MBuildingSmsPK pk = new MBuildingSmsPK();
        pk.setCorpId(corpId);
        pk.setBuildingId(builingId);
        target.setId(pk);

        MBuildingSms res = find(buildingSmsDaoImpl, target);
        if (res != null) {
            ret = res.getChkInt();
        }
        return ret;
    }

    /**
     * 月検診連番取得
     *
     * @param devId 装置ID
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @return 月検診連番
     */
    public Long getInspMonthNo(String devId, String inspYear, String inspMonth) throws Exception {
        Long ret = 0L;
        TInspectionMeterSvr target = new TInspectionMeterSvr();
        TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
        pk.setDevId(devId);
        pk.setInspYear(inspYear);
        pk.setInspMonth(inspMonth);
        target.setId(pk);

        List<Object> targetList = new ArrayList<>();
        Map<String, List<Object>> param = new HashMap<>();
        targetList.add(target);
        param.put("select", targetList);

        List<Long> resList = getResultList(latestInspMonthNoDaoImpl, param);
        if (resList.size() != 0) {
            // COALESCE(MAX(insp_month_no),0) + 1の+1をここで行う
            ret = resList.get(0) + 1;
        }
        return ret;
    }

    /**
     * メーター取得
     *
     * @param String devId
     * @param Long   meter_no
     * @param String inspMonth
     * @return メーター情報
     */
    public MMeter getMeter(String devId, Long meter_no) throws Exception {
        MMeter ret = null;

        MMeter target = new MMeter();
        MMeterPK pk = new MMeterPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meter_no);
        target.setId(pk);

        ret = find(meterDaoImpl, target);

        return ret;
    }

    /**
     * 日報より最新の取得日時取得
     *
     * @param String devId
     * @param Long   meter_no
     * @return 最新の取得日時
     */
    private String getLoadSurveyGetDate(String devId, Long meter_no) {
        String ret = null;
        TDayLoadSurvey target = new TDayLoadSurvey();
        TDayLoadSurveyPK pk = new TDayLoadSurveyPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meter_no);
        target.setId(pk);

        List<TDayLoadSurvey> resList = getResultList(loadSurveyDaoImpl, target);
        if (resList.size() != 0) {
            ret = resList.get(0).getId().getGetDate();
        }
        return ret;
    }

    /**
     * 前回検針結果取得
     *
     * @param String devId
     * @param Long   meter_no
     * @return 最新の取得日時
     */
    private TInspectionMeterSvr getPrevInspValues(String devId, Long meter_no) {
        TInspectionMeterSvr ret = null;
        TInspectionMeterSvr target = new TInspectionMeterSvr();
        TInspectionMeterSvrPK pk = new TInspectionMeterSvrPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meter_no);
        target.setId(pk);

        List<TInspectionMeterSvr> resList = getResultList(inspMeterSvrDaoImpl, target);
        if (resList.size() != 0) {
            ret = resList.get(0);
        }
        return ret;
    }

    /**
     * 検針結果登録
     *
     * @param TInspectionMeterSvr target
     * @return 成功:true/失敗:false
     */
    private Boolean insertInsp(TInspectionMeterSvr target) {
        Boolean ret = true;

        persist(inspMeterSvrDaoImpl, target);
        return ret;
    }

    /**
     * 日報より最新検針値取得
     *
     * @param devId 装置ID(接続先)
     * @param meterMngId メーター管理番号
     * @return 最新の検針値  null:該当データなし(発生しないはず：直前で dmv_kwh is not null の get_date を取得してから検索しているため)
     */
    private BigDecimal getLoadSurveyLatestValue(String devId, Long meterMngId, String getDate) {
        BigDecimal ret = null;
        TDayLoadSurvey target = new TDayLoadSurvey();
        TDayLoadSurveyPK pk = new TDayLoadSurveyPK();
        pk.setDevId(devId);
        pk.setMeterMngId(meterMngId);
        pk.setGetDate(getDate);
        target.setId(pk);

        TDayLoadSurvey res = find(loadSurveyDaoImpl, target);
        if (res != null) { // ← 直前の処理でデータの確認をしているため通常 true になる
            ret = res.getDmvKwh(); // ← 直前の処理で not null を確認しているため、通常 null にはならない
        }
        return ret;
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
     * 不完全データ結果登録
     *
     * @param TWkInspectionIncomp target
     * @return 成功:true/失敗:false
     */
    private Boolean insertInspcomp(TWkInspectionIncomp target) {
        Boolean ret = true;

        try {
            persist(wkInspIncompDaoImpl, target);
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    /**
     * 停止するときはtrue, 停止しないときはfalseを返却する
     * @param devId
     * @return
     */
    public boolean getPauseMailFlag(String devId) {
        MPauseMail target = new MPauseMail();
        target.setDevId(devId);

        List<MPauseMail> list = getResultList(mPauseMailServiceDaoImpl, target);
        if((list != null && list.size() > 0) && (list.get(0).getManualInsp() != null && list.get(0).getManualInsp().equals("1"))) {
            return true;
        }
        return false;
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
     * 時間を加算した日付生成（分用）
     *
     * @param String execDayModWk  書式:yyyyMMddHH or yyyyMMddHHmm
     * @param String waitMinute 加算分数
     * @return Date 加算された日時
     */
    private Date getAddMinute(String execDayModWk, int waitMinute) {
        Date date_mod = DateUtility.conversionDate(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHH);

        if (date_mod == null) {
            date_mod = DateUtility.conversionDate(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        }
        date_mod = DateUtility.plusMinute(date_mod, waitMinute);

        return date_mod;
    }

}
