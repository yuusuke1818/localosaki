package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsInspectionMeterBefResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterSvrResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterSvrSearchResultData;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.MBuildingSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.AutoInspServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.DayLoadSurveyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterBefServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterSvrSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterSvrServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterServiceDaoImpl;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 確定前検針データ検針連番 登録・更新 Daoクラス
 * 参照コード
 *  MeterBeforeInspect.js
 *  MeterBeforeInspectReg.php
 *  MeterBeforeInspectUpd.php
 * @author kobayashi.sho
 */
@Stateless
public class UpdateSmsInspectionMeterBefDao extends OsolApiDao<UpdateSmsInspectionMeterBefParameter> {

    // 検針種別：定期検針
    private static final String INSP_TYPE_REGULAR = "r";
    // 検針種別：臨時検針
    private static final String INSP_TYPE_TEMPORARY = "t";

    private static final BigDecimal LATEST_USE_VAL_MAX = new BigDecimal("9999999.9");

    private static final BigDecimal KWH30_MAX = new BigDecimal("999999.99");

    // 検針最大値 (この値を超えると メーター が 0 に戻る) ※整数部4桁のメーター
    private static final BigDecimal INSP_VAL_FULL4 = new BigDecimal("10000.0");

    // 検針最大値 (この値を超えると メーター が 0 に戻る) ※整数部5桁のメーター
    private static final BigDecimal INSP_VAL_FULL5 = new BigDecimal("100000.0");

    // 使用量率最大値
    private static final BigDecimal USE_PER_RATE_MAX = new BigDecimal("999.99");

    // 割合をパーセントにする
    private static final BigDecimal PERCENTAGE_TO_PERCENTAGE = new BigDecimal("100");

    // 月検針連番上限
    private static final long INSP_MONTH_NO_MAX = 999L;

    // 自動検針日時 取得失敗フラグ:警告(一部実行不可)
    private static final String AUTO_INSP_DATE_WARNING = "WARN";

    // 自動検針日時 取得失敗フラグ:エラー
    private static final String AUTO_INSP_DATE_ERROR = "ERR";

    // 自動検針日時が指定月ではない
    private static final String AUTO_INSP_DATE_OFF = "-";

    // フラグ
    private static final String TRUE = "1";

    /** セッション. */
    @Resource
    private SessionContext sessionContext;

    /** メーター種別 取得 処理. */
    private final MeterServiceDaoImpl meterServiceDaoImpl;

    /** 自動検針月 日時設定 取得 処理. */
    private final AutoInspServiceDaoImpl autoInspServiceDaoImpl;

    /** 確定前検針データ 検索・登録・更新・削除. */
    private final InspectionMeterBefServiceDaoImpl inspectionMeterBefServiceDaoImpl;

    /** 検針結果データ(サーバ用) 取得 処理. */
    private final InspectionMeterSvrSearchServiceDaoImpl inspectionMeterSvrSearchServiceDaoImpl;

    /** 検針結果データ(サーバ用) 登録 処理. */
    private final InspectionMeterSvrServiceDaoImpl inspectionMeterSvrServiceDaoImpl;

    /** ロードサーベイ日データ. */
    private final DayLoadSurveyServiceDaoImpl dayLoadSurveyServiceDaoImpl;

    /** 建物情報取得（SMSのみで使用する）. */
    private final MBuildingSmsServiceDaoImpl buildingSmsDaoImpl;

    public UpdateSmsInspectionMeterBefDao() {
        meterServiceDaoImpl = new MeterServiceDaoImpl();
        autoInspServiceDaoImpl = new AutoInspServiceDaoImpl();
        inspectionMeterBefServiceDaoImpl = new InspectionMeterBefServiceDaoImpl();
        inspectionMeterSvrSearchServiceDaoImpl = new InspectionMeterSvrSearchServiceDaoImpl();
        inspectionMeterSvrServiceDaoImpl = new InspectionMeterSvrServiceDaoImpl();
        dayLoadSurveyServiceDaoImpl = new DayLoadSurveyServiceDaoImpl();
        buildingSmsDaoImpl = new MBuildingSmsServiceDaoImpl();
    }

    @Override
    public UpdateSmsInspectionMeterBefResult query(UpdateSmsInspectionMeterBefParameter parameter) throws Exception {

        boolean isAdd = (parameter.getInspMonthNo() == null); // 登録フラグ  true:新規登録モード  false:更新モード

        List<String> msgList = new ArrayList<String>(); // アラートメッセージ  0件:正常終了

        //  [パラメータ]確定前検針データ 存在チェック
        if (parameter.getMeters().getMeterList().isEmpty()) {
            msgList.add((isAdd ? "追加" : "更新") + "するデータを選択してください。");
            return new UpdateSmsInspectionMeterBefResult(null, 0, 0, msgList, null);
        }

        // 強制実行ではない(通常ケース) → 重複チェックを行う
        if (!parameter.getIsForcedWrite()) {
            // 確定登録しようとしている"メーター管理番号"が既に確定登録済みかチェックする(重複チェック)

            // 検索条件セット
            Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.DEV_ID,
                    Arrays.asList(parameter.getDevId()));       // 装置ID
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.METER_MNG_ID,
                    parameter.getMeters().getMeterList().stream()
                        .map(row -> row.getMeterMngId())
                        .collect(Collectors.toList()));         // メーター管理番号一覧
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.INSP_YEAR,
                    Arrays.asList(parameter.getInspYear()));    // 検針年
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.INSP_MONTH,
                    Arrays.asList(parameter.getInspMonth()));   // 検針月
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.INSP_TYPE,
                    Arrays.asList(parameter.getInspType()));   // 検針種別
            targetMap.put(InspectionMeterSvrSearchServiceDaoImpl.EXCEPTION_INSP_MONTH_NO,
                    Arrays.asList(parameter.getInspMonthNo())); // チェック対象外 月検針連番 (null:可)

            // 登録済み"メーター管理番号"チェック(重複チェック)
            List<InspectionMeterSvrSearchResultData> entityList = getResultList(inspectionMeterSvrSearchServiceDaoImpl, targetMap);
            if (entityList != null && !entityList.isEmpty()) {
                // 重複あり → 処理を中断して 強制実行確認ダイアログ を表示する
                List<Long> warnExistingIdList = entityList.stream().map(row -> row.getMeterMngId()).collect(Collectors.toList());
                return new UpdateSmsInspectionMeterBefResult(warnExistingIdList, 0, 0, msgList, null);
            }
        }

        // [パラメータ]確定前検針データ・リストをコピーして、[作業用]確定前検針データ・リストを生成
        List<InspectionMeterSvrResultData> targetList = parameter.getMeters().getMeterList().stream()
                .map(row -> new InspectionMeterSvrResultData(row))
                .collect(Collectors.toList());

        // 検針種別 が "r":定期 か？
        if (INSP_TYPE_REGULAR.equals(parameter.getInspType())) {
            // 自動検針日時 取得
            for (InspectionMeterSvrResultData target : targetList) {
                // 自動検針日時(最新検針値_日時) 算出
                String autoInspDate = calcAutoInspDate(msgList, parameter.getDevId(), target.getMeterMngId(), parameter.getInspYear(), parameter.getInspMonth(), isAdd, parameter.getLoginPersonId());
                if (AUTO_INSP_DATE_ERROR.equals(autoInspDate)) {
                    // 継続不能エラー(自動検針日時 未設定)
                    return new UpdateSmsInspectionMeterBefResult(null, 0, 0, msgList, null);
                } else if (AUTO_INSP_DATE_WARNING.equals(autoInspDate) || AUTO_INSP_DATE_OFF.equals(autoInspDate)) {
                    // 警告 または 自動検針日時が指定月ではない
                    continue;
                }

                // [確定用検針データ]自動検針日時 に [変数]自動検針日時 をセット
                target.setAutoInspDate(autoInspDate);
            }
        }

        // ※仕様変更#1494 関連 検針連番の採番ルール修正 ▽▽ここから▽▽
        // 新規登録か？
        //if (isAdd) {
        //    // 新規登録の場合 → 自動検針日時により連番振り分け
        //
        //    // 月検針連番の最大値+1を取得
        //    long inspMonthNo = getInspMonthNo(parameter.getDevId(), parameter.getInspYear(), parameter.getInspMonth());
        //
        //    // 自動検針日時の古い順から targetList に連番を振る（新しくなるにつれて+1追加される）（自動検針日時が同時刻の場合同じ連番とする）
        //    List<String> autoInspDateList = targetList.stream().map(target -> target.getAutoInspDate()).collect(Collectors.toList()); // 自動検針日時 のリスト取得
        //    autoInspDateList = new ArrayList<String>(new HashSet<>(autoInspDateList)); // 重複削除
        //    Collections.sort(autoInspDateList); // 自動検針日時リストを昇順にソート
        //    for(String autoInspDate : autoInspDateList) { // 自動検針日時リスト の数分ループ(昇順ソート済み)
        //        // [作業用]確定前検針データ・リスト から 自動検針日時 が一致するものを抽出して、月検針連番 をセット（自動検針日時 が同じものには 同じ 月検針連番 をセットする）
        //        for (InspectionMeterSvrResultData target : targetList) {
        //            if (autoInspDate.equals(target.getAutoInspDate())) {
        //                target.setInspMonthNo(inspMonthNo);
        //            }
        //        }
        //        inspMonthNo++;
        //    }
        //}
        long inspMonthNo; // 月検針連番
        if (isAdd) {
            // 新規登録の場合
            // 月検針連番の最大値+1を取得
            inspMonthNo = getInspMonthNo(parameter.getDevId(), parameter.getInspYear(), parameter.getInspMonth());
        } else {
            // 連番更新の場合
            inspMonthNo = parameter.getInspMonthNo(); // 月検針連番
        }

        // 月検針連番上限チェック
        if (inspMonthNo > INSP_MONTH_NO_MAX) {
            // 連番が999を超えた場合エラー
            String logMsg = "連番が999を超えました。" + (isAdd ? "追加" : "更新") + "を行ないません。";
            daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
            msgList.add(logMsg);
            return new UpdateSmsInspectionMeterBefResult(null, 0, 0, msgList, null);
        }
        // ※仕様変更#1494 関連 検針連番の採番ルール修正 △△ここまで△△

        // --- 確定前検針データを確定登録する --- 旧：registBeforeInspectData

        // 主要変数 メモ
        //  parameter.getDevId()        装置ID
        //  targetList                  確定用データ
        //  parameter.getInspType()     検針種別
        //  parameter.getLoginCorpId()  企業ID
        //  parameter.getLoginPersonId() parameter.getLoginPersonId() ユーザーID
        //  parameter.getInspYear()     検針年
        //  parameter.getInspMonth()    検針月
        //  parameter.getInspMonthNo()  月検針連番 (範囲：1～999) ※null:新規登録モード  null以外:更新モード

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        // ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        // [作業用]確定前検針データ失敗リスト 初期化
        List<InspectionMeterSvrResultData> failList = new ArrayList<InspectionMeterSvrResultData>();;
        int successCnt = 0; // 追加成功回数カウンタ

        // 確定処理
        for (InspectionMeterSvrResultData target : targetList) {
            Long metMngId = target.getMeterMngId();

            // 対象データが存在しない場合エラー

            // 確定前検針データ存在チェック
            boolean isMeterBef = checkMeterBef(parameter.getDevId(), target.getMeterMngId(), target.getLatestInspDate(), target.getVersion());
            if (!isMeterBef) {
                // 確定前検針データが取得できない(楽観ロックエラーなど)
                String logMsg = "[管理番号：" + metMngId + "] 選択した確定前検針データが見つかりませんでした。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                msgList.add(logMsg);
                failList.add(target); // 失敗リストに追加
                continue;
            }

            // プルダウンでの指定年月より後に、同一装置ID・メーター管理番号の検針結果データが存在するならエラー

            // 指定年月より後の検針結果データ 存在チェック
            boolean futureInspData = checkFutureInspData(parameter.getDevId(), metMngId, parameter.getInspYear(), parameter.getInspMonth(), parameter.getInspMonthNo());
            if (futureInspData) {
                // 存在する → エラー
                String logMsg = "[管理番号：" + metMngId + "] " + (isAdd ? "指定年月" : "指定年月/連番") + "より後に同一管理番号の検針結果データが存在します。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                msgList.add(logMsg);
                failList.add(target); // 失敗リストに追加
                continue;
            }

            // 各データ取得
            //InspectionMeterSvrSearchResultData latestData = null; // ※仕様変更 クラウド検針サーバ設計変更_No.7
            Date latestInspDate = null; // 最新検針値_日時
            // long inspMonthNo = 0; // 月検針連番 ※仕様変更#1494 関連 検針連番の採番ルール修正
            if (INSP_TYPE_REGULAR.equals(parameter.getInspType())) {

                // ※仕様変更 クラウド検針サーバ設計変更_No.7
                //// 前月の検針結果データ有無チェック
                //String lastYearMonth = DateUtility.plusMonth(convYyyyMm(parameter.getInspYear(), parameter.getInspMonth()), -1);
                //String lastYear = convYear(lastYearMonth);
                //String lastMonth = convMonth(lastYearMonth);
                //boolean isLastMonthData = checkInspData(parameter.getDevId(), metMngId, lastYear, lastMonth); // 前月の検針結果データの有無チェック
                //
                //if (isLastMonthData) {
                //    // 前月の検針結果データがある
                //
                //    // 直近の検針結果データを取得する
                //    latestData = getLatestInspDataAllPast(parameter.getDevId(), metMngId, parameter.getInspYear(), parameter.getInspMonth(), parameter.getInspMonthNo()); // 旧：getLatestInspData
                //
                //    // 最新の検針結果データ取得失敗の場合、エラー
                //    if (latestData == null) { // 発生しないはず
                //        String logMsg = "[管理番号：" + metMngId + "] 前月の検針データが取得できませんでした。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                //        daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                //        msgList.add(logMsg);
                //        failList.add(target); // 失敗リストに追加
                //        continue;
                //    }
                //} else {
                //    // 前月の検針結果データがない
                //
                //    latestData = null;
                //
                //    // 過去（前月以前）の検針結果データ存在チェックする
                //    boolean inspDataExists = checkExistsInspDataAllTime(parameter.getDevId(), metMngId);
                //
                //    // 前月にはないが、全年月通して1件以上は検針結果データがある場合、エラー
                //    if (inspDataExists) {
                //        String logMsg = "[管理番号：" + metMngId + "] 前月の検針データがありませんでした。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                //        daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                //        msgList.add(logMsg);
                //        failList.add(target); // 失敗リストに追加
                //        continue;
                //    }
                //}

                // 自動検針日時が取得できていなかった場合エラー
                if (target.getAutoInspDate() == null) {
                    // calcAutoInspDate()処理内でエラーログを出力しているため、ログ出力不要
                    failList.add(target); // 失敗リストに追加
                    continue;
                }

                // 検針日時 型変換
                latestInspDate = DateUtility.conversionDate(target.getAutoInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH);

                // ※仕様変更#1494 関連 検針連番の採番ルール修正
                //// 登録モード が true:新規登録モード か？
                //if (isAdd) {
                //    // 新規登録モード
                // ※仕様変更#1494 関連 検針連番の採番ルール修正
                //    // 月検針連番 取得
                //    inspMonthNo = target.getInspMonthNo();
                //} else {
                //    // 更新モード
                //
                //    // 月検針連番
                //    inspMonthNo = parameter.getInspMonthNo();
                //}
            } else if (INSP_TYPE_TEMPORARY.equals(parameter.getInspType())){
                latestInspDate = target.getLatestInspDate();

                // ※仕様変更 クラウド検針サーバ設計変更_No.7
                //// 直近の検針結果データを取得する
                //latestData = getLatestInspDataAllPast(parameter.getDevId(), metMngId, parameter.getInspYear(), parameter.getInspMonth(), parameter.getInspMonthNo());
                //if(latestData == null) {
                //    // 過去の検針結果データ存在チェックする
                //    boolean inspDataExists = checkExistsInspDataAllTime(parameter.getDevId(), metMngId);
                //
                //    // 指定年月または過去にはないが全年月通して1件以上は（＝指定年月より後に1件以上）検針結果データがある場合、エラー
                //    if (inspDataExists) {
                //        String logMsg = "[管理番号：" + metMngId + "] 過去月の検針データがありませんでした。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                //        daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                //        msgList.add(logMsg);
                //        failList.add(target); // 失敗リストに追加
                //        continue;
                //    }
                //}

                // ※仕様変更#1494 関連 検針連番の採番ルール修正
                // 登録モード が true:新規登録モード か？
                //if (isAdd) {
                //    // 新規登録モード
                //    // 月検針連番を取得する
                //    inspMonthNo = getInspMonthNo(parameter.getDevId(), parameter.getInspYear(), parameter.getInspMonth());
                //} else {
                //    // 更新モード
                //    // 月検針連番
                //    inspMonthNo = parameter.getInspMonthNo();
                //}
            }

            // 直近の検針結果データを取得する  ※仕様変更 クラウド検針サーバ設計変更_No.7
            InspectionMeterSvrSearchResultData latestData = getLatestInspDataAllPast(parameter.getDevId(), metMngId, parameter.getInspYear(), parameter.getInspMonth(), parameter.getInspMonthNo());

            // ※仕様変更 クラウド検針サーバ設計変更_No.7
            //// 月検針連番上限チェック
            //if (inspMonthNo > INSP_MONTH_NO_MAX) {
            //    // 連番が999を超えた場合エラー
            //    String logMsg = "[管理番号：" + metMngId + "] 連番が999を超えました。" + (isAdd ? "追加" : "更新") + "を行ないません。";
            //    daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
            //    msgList.add(logMsg);
            //    failList.add(target); // 失敗リストに追加
            //    continue;
            //}

            // 前回検針値計算
            BigDecimal prevInspVal = latestData == null ? null : latestData.getLatestInspVal();

            // 前々回検針値計算
            BigDecimal prevInspVal2 = latestData == null ? null : latestData.getPrevInspVal();

            BigDecimal chkInt = getChkInt(parameter.getCorpId(), parameter.getBuildingId());

            if(BigDecimal.ONE.equals(chkInt)) {
                target.setLatestInspVal(getScaleValue(target.getLatestInspVal()));
                prevInspVal = getScaleValue(prevInspVal);
                prevInspVal2 = getScaleValue(prevInspVal2);
            }

            // 今回使用量計算
            BigDecimal latestUseVal =
                    calcUseVal(
                        target.getLatestInspVal(),  // 最新検針値(整数部最大5桁、小数部最大1桁)
                        prevInspVal,                // 前回検針値
                        target.getMulti());         // 乗率
            if (latestUseVal != null && LATEST_USE_VAL_MAX.compareTo(latestUseVal) < 0) {
                // 最大値を超えている
                String logMsg = "[管理番号：" + metMngId + "] 今回使用量が9999999.9を超えました。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                msgList.add(logMsg);
                failList.add(target); // 失敗リストに追加
                continue;
            }

            // 前回使用量計算
            BigDecimal prevUseVal =
                    calcUseVal(
                        prevInspVal,            // 前回検針値(整数部最大5桁、小数部最大1桁)
                        prevInspVal2,           // 前々回検針値
                        target.getMulti());     // 乗率
            if (prevUseVal != null && LATEST_USE_VAL_MAX.compareTo(prevUseVal) < 0) {
                String logMsg = "[管理番号：" + metMngId + "] 前回使用量が9999999.9を超えました。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                msgList.add(logMsg);
                failList.add(target); // 失敗リストに追加
                continue;
            }

            // 使用量率計算
            BigDecimal usePerRate;
            if (latestUseVal == null || prevUseVal == null || prevUseVal.compareTo(BigDecimal.ZERO) == 0) {
                usePerRate = null;
            } else {
                usePerRate = latestUseVal.multiply(PERCENTAGE_TO_PERCENTAGE).divide(prevUseVal, RoundingMode.HALF_UP); // 今回使用量計算 × 100 ÷ 前回使用量計算
                // use_per_rateを0～999.99の範囲内に制限する
                if (usePerRate.compareTo(USE_PER_RATE_MAX) > 0) {
                    usePerRate = USE_PER_RATE_MAX;
                } else if (usePerRate.compareTo(BigDecimal.ZERO) < 0) {
                    usePerRate = BigDecimal.ZERO;
                }
            }

            // 検針結果データへ登録
            TInspectionMeterSvr regTarget = new TInspectionMeterSvr();
            regTarget.setId(new TInspectionMeterSvrPK());
            regTarget.getId().setDevId(parameter.getDevId());
            regTarget.getId().setMeterMngId(target.getMeterMngId());
            regTarget.getId().setInspYear(parameter.getInspYear());
            regTarget.getId().setInspMonth(parameter.getInspMonth());
            regTarget.getId().setInspMonthNo(inspMonthNo);  // 月検針連番
            regTarget.setRecDate(serverDateTime);
            regTarget.setRecMan(parameter.getLoginPersonId());
            regTarget.setInspType(parameter.getInspType());
            regTarget.setLatestInspVal(target.getLatestInspVal()); // 最新検針値
            regTarget.setPrevInspVal(prevInspVal);
            regTarget.setPrevInspVal2(prevInspVal2);
            regTarget.setMultipleRate(target.getMulti());
            regTarget.setLatestUseVal(latestUseVal);
            regTarget.setPrevUseVal(prevUseVal);
            regTarget.setUsePerRate(usePerRate);
            // 最新検針日時（AieLink装置のときは確定前検針データのデータ取得日時）
            if(parameter.getDevId().startsWith("OC") || parameter.getDevId().startsWith("MH")) {
                regTarget.setLatestInspDate(convTimestamp(target.getLatestInspDate()));
            }else {
                regTarget.setLatestInspDate(convTimestamp(latestInspDate));
            }
            regTarget.setPrevInspDate(latestData == null ? null : convTimestamp(latestData.getLatestInspDate()));
            regTarget.setPrevInspDate2(latestData == null ? null : convTimestamp(latestData.getPrevInspDate()));
            regTarget.setEndFlg(new BigDecimal(1));
            regTarget.setVersion(0);  // 排他制御用カラム
            regTarget.setCreateUserId(loginUserId);
            regTarget.setCreateDate(serverDateTime);
            regTarget.setUpdateUserId(loginUserId);        // 更新ユーザー識別ID
            regTarget.setUpdateDate(serverDateTime);       // 更新日時

            // 登録モード が true:新規登録モード か？
            if (isAdd) {
                // 新規登録モード
                registInspData(regTarget); // エラー時は Excption になり、ロールバックする
            } else {
                // 更新モード
                TInspectionMeterSvr updTarget = getInspDataPK(parameter.getDevId(), target.getMeterMngId(), parameter.getInspYear(), parameter.getInspMonth(), inspMonthNo);
                if (updTarget != null) {
                    if (INSP_TYPE_REGULAR.equals(parameter.getInspType())) {
                        Timestamp targetInspDate = updTarget.getLatestInspDate();
                        Timestamp befInspDate = convTimestamp(latestInspDate);
                        // 更新対象の検針日時と現在の自動検針日時が異なっている場合、エラー
                        if (!Objects.equals(targetInspDate, befInspDate)) {
                            String logMsg = "[管理番号：" + metMngId + "] 検針日時が異なります。" + (isAdd ? "追加" : "更新") + "を行ないません。";
                            daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                            msgList.add(logMsg);
                            failList.add(target); // 失敗リストに追加
                            continue;
                        }
                    }

                    // 更新対象のデータがあれば上書き
                    updateInspData(updTarget, regTarget); // エラー時は Excption になり、ロールバックする
                } else{
                    // 更新対象のデータが無いので新規登録
                    registInspData(regTarget); // エラー時は Excption になり、ロールバックする
                }
            }

            // 確定前検針データを削除
            if (!deleteBeforeInspData(parameter.getDevId(), target.getMeterMngId(), target.getLatestInspDate(), target.getVersion())) {
                // 削除対象データが無い または 楽観ロックエラー  ※削除エラーの場合は、Exception で終了する
                sessionContext.setRollbackOnly();
                // 排他エラー
                throw new OptimisticLockException();
            }

            // ロードサーベイ日データ(日報データ)登録
            if (INSP_TYPE_REGULAR.equals(parameter.getInspType())){
                // [リクエスト]検針種別 が "r":定期 の場合

                // 前月1日0時の0分の日報データのDMV_KWH値取得
                String lastMonth1stDate = DateUtility.plusMonth(convYyyyMm(parameter.getInspYear(), parameter.getInspMonth()), -1) + "010000";

                // ロードサーベイ日データテーブル から 前月1日00:00 の DMV_KWH を取得する
                BigDecimal lastMonth1stDmvKwh = getDmvkwhFromGetDate(parameter.getDevId(), metMngId, lastMonth1stDate); // 前月１日 指針値データ(DMV_KWH)

                // 30分使用量 算出
                // 30分使用量 = 最新検針値 - 先月の指針値データ
                BigDecimal kwh30 = calcUseVal(
                            target.getLatestInspVal(),  // 最新検針値(整数部最大5桁、小数部最大1桁)
                            lastMonth1stDmvKwh,         // 前回検針値
                            target.getMulti());         // 乗率
                if (kwh30 != null && KWH30_MAX.compareTo(kwh30) < 0) {
                    // 最大値を超えている
                    String logMsg = "[管理番号：" + metMngId + "] 使用量が999999.99を超えました。";
                    daoLogger.trace(this.getClass().getName().concat("dev_id : " + parameter.getDevId() + ", user_id : " + parameter.getLoginPersonId() + ", event_content : " + logMsg));
                    msgList.add(logMsg);
                    kwh30 = KWH30_MAX;
                }

                // ロードサーベイ日データ(日報データ)の更新または登録
                TDayLoadSurvey dls = new TDayLoadSurvey();
                dls.setId(new TDayLoadSurveyPK());
                dls.getId().setDevId(parameter.getDevId());
                dls.getId().setMeterMngId(target.getMeterMngId());
                dls.getId().setGetDate(convYyyyMm(parameter.getInspYear(), parameter.getInspMonth()) + "010000");
                dls.setRecDate(serverDateTime);
                dls.setRecMan(parameter.getLoginPersonId());
                dls.setKwh30(kwh30);    // ※カラム名は「30分使用量」だが、ハンディ検針のため1ヶ月分の使用量をセット
                dls.setDmvKwh(target.getLatestInspVal());
                dls.setVersion(0); // 排他制御用カラム
                dls.setCreateUserId(loginUserId);
                dls.setCreateDate(serverDateTime);
                dls.setUpdateUserId(loginUserId);
                dls.setUpdateDate(serverDateTime);

                upsertLoadSurveyData(dls); // エラー時は Excption になり、ロールバックする
            }

            // 追加成功回数カウント  ※DB編集エラーの場合は Exceltion で終了するので、ここに到達しない
            successCnt++;
        }

        // アラート文言作成
        int failCnt = targetList.size() - successCnt;
        if (failCnt == 0) {
            // 正常
        } else if (successCnt == 0) {
            msgList.add("指定した検針データの" + (isAdd ? "追加" : "更新") + "が正常に行なわれませんでした。");
//            msgList.add("詳細は動作ログをご確認ください。");
        } else {
            msgList.add("指定した検針データの" + (isAdd ? "追加" : "更新") + "が一部正常に行なわれませんでした。");
//            msgList.add("詳細は動作ログをご確認ください。");
        }

        return new UpdateSmsInspectionMeterBefResult(null, successCnt, failCnt, msgList, failList);
    }

    /**
     * 使用量計算.
     * 使用量計算 = 最新検針値 - 前回検針値.
     * ※必要な項目に値が無いときは、返値に 0 を返す.
     * @param latestInspVal 最新検針値(整数部最大5桁、小数部最大1桁)  ※null の場合は null を返す
     * @param beforeInspVal 前回検針値(整数部最大5桁、小数部最大1桁)  ※null の場合は 0 として計算する
     * @param multi 乗率  ※null または 0 の場合、1 として扱う
     * @return 使用量  null:計算不能
     */
    private BigDecimal calcUseVal(BigDecimal latestInspVal, BigDecimal beforeInspVal, BigDecimal multi) {
        // 乗率チェック
        if (multi == null || multi.compareTo(BigDecimal.ZERO) == 0) {
            // null または 0 の場合、1 として扱う
            multi = BigDecimal.ONE;
        }

        // 最新検針値が無い(通常発生しないはず) または 前回検針値が無い(前回計測されていない)場合 → 使用量は null にする
        if (latestInspVal == null || beforeInspVal == null) {
            return null;
        }

        if (latestInspVal.compareTo(beforeInspVal) < 0) {
            // メーターの値が前回値より小さい場合、メーターが一回転したものとして処理する
            if (beforeInspVal.compareTo(INSP_VAL_FULL4) < 0) { // ※仕様変更 クラウド検針サーバ設計変更_No.13
                latestInspVal = latestInspVal.add(INSP_VAL_FULL4); // 整数部4桁のメーター
            } else {
                latestInspVal = latestInspVal.add(INSP_VAL_FULL5); // 整数部5桁のメーター
            }
        }

        return latestInspVal.subtract(beforeInspVal).multiply(multi); // (最新検針値 - 前回検針値) × 乗率
    }

    /**
     * 自動検針日時 算出.
     * @param msgList [Out] アラートメッセージ   ※メッセージ リターン用
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param year 検針年
     * @param month 検針月
     * @param isAdd 登録モード  true:新規登録モード  false:更新モード
     * @param userId ユーザーID
     * @return 最新検針値_日時 ※書式"yyyy/MM/dd HH:mm" M、d、H、m が1桁でも 後続処理に支障がないのでチェック不要
     */
    private String calcAutoInspDate(List<String> msgList, String devId, Long meterMngId, String year, String month, boolean isAdd, String userId) {

        // メーター種別取得
        Long meterType = getMeterType(devId, meterMngId);
        if (meterType == null) {
            // メーター種別の取得に失敗した時
            String logMsg = "[管理番号：" + meterMngId + "] メーター種別の取得に失敗しました。" + (isAdd ? "追加" : "更新") + "を行ないません。";
            daoLogger.trace(this.getClass().getName().concat("dev_id : " + devId + ", user_id : " + userId + ", event_content : " + logMsg));
            msgList.add(logMsg);
            return AUTO_INSP_DATE_WARNING;
        }

        // 自動検針日時取得
        MAutoInsp autoInsp = getAutoInspect(devId, meterType);
        if (autoInsp == null) {
            // 自動検針データの取得に失敗した時
            String logMsg = "[管理番号：" + meterMngId + "] 自動検針日時の設定が取得できませんでした。" + (isAdd ? "追加" : "更新") + "を行ないません。";
            daoLogger.trace(this.getClass().getName().concat("dev_id : " + devId + ", user_id : " + userId + ", event_content : " + logMsg));
            msgList.add(logMsg);
            return AUTO_INSP_DATE_WARNING;
        }

        // 自動検針日時が指定月で設定されているか判定  ※仕様変更#1462
        int inspMonth = Integer.parseInt(month); // 検針月
        String isAutoInspSet = autoInsp.getMonth().substring(inspMonth - 1, inspMonth); // 月毎の自動検針実施有無 "1"/"0"
        if (!TRUE.equals(isAutoInspSet)) { // 自動検針実施 "0：なし"
            // 対象月に自動検針日時が設定がされていない時
            String logMsg = "[管理番号：" + meterMngId + "] 自動検針設定の検針実行月が自動検針しない設定にされています。" + (isAdd ? "追加" : "更新") + "を行ないません。";
            daoLogger.trace(this.getClass().getName().concat("dev_id : " + devId + ", user_id : " + userId + ", event_content : " + logMsg));
            msgList.add(logMsg);
            return AUTO_INSP_DATE_OFF;
        }

        // 設定されている自動検針日時(日)が存在しない日付の場合、指定月の末日にする
        int lastDay = lastDayOfMonth(year, month); // 月末日を取得
        String day = autoInsp.getDay();
        if (Integer.parseInt(day) > lastDay) {
            // 自動検針日時(日) > 月末日
            day = String.valueOf(lastDay); // 書式:dd
        } else if (Integer.parseInt(day) <= 0) {
            // 自動検針日時(日) = 0:指定なし
            String logMsg = "[管理番号：" + meterMngId + "] 自動検針設定の自動検針日が設定されていません。自動検針設定を実施してください。";
            daoLogger.trace(this.getClass().getName().concat("dev_id : " + devId + ", user_id : " + userId + ", event_content : " + logMsg));
            msgList.add(logMsg);
            return AUTO_INSP_DATE_ERROR; // エラー終了  ※仕様変更#1496
        }

        String autoInspDate = year + "/" + month + "/" + day + " " + autoInsp.getHour() + ":00"; // 自動検針日時 生成

        return autoInspDate;
    }

    /**
     * 月末日取得
     * @param year 年
     * @param month 月
     * @return 月末日
     */
    private int lastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * メーター種別を取得する
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @return メーター種別
     */
    private Long getMeterType(String devId, Long meterMngId) {
        // メータ登録用 検索条件設定
        MMeterPK targetPk = new MMeterPK();
        targetPk.setDevId(devId); // 装置ID
        targetPk.setMeterMngId(meterMngId); // メーター管理番号
        MMeter target = new MMeter();
        target.setId(targetPk);

        // 検索
        MMeter entity = find(meterServiceDaoImpl, target);
        if (entity == null) {
            // 該当データなし
            return null;
        }

        return entity.getMeterType();
    }

    /**
     * 自動検針日時を取得する。
     * @param devId 装置ID
     * @param meterType メーター種別
     * @return 自動検針日時 getMonth()：月毎の自動検針実施有無（0：なし　1：あり） ※1月, 2月, 3月 … 12月 分の12桁のフラグを内包
     */
    private MAutoInsp getAutoInspect(String devId, Long meterType) {
        // 自動検針月 日時設定 検索条件設定
        MAutoInspPK targetPk = new MAutoInspPK();
        targetPk.setMeterType(meterType); // メーター種別
        targetPk.setDevId(devId); // 装置ID
        MAutoInsp target = new MAutoInsp();
        target.setId(targetPk);

        // 検索
        MAutoInsp entity = find(autoInspServiceDaoImpl, target);

        return entity;
    }

    /**
     * 月検針連番を取得する。
     * @param devId 装置ID
     * @param inspYear 年
     * @param inspMonth 月
     * @return 月検針連番
     */
    private long getInspMonthNo(String devId,  String inspYear, String inspMonth) {
        // 検索条件設定
        InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
                InspectionMeterSvrSearchResultData.MODE_MAX_INSP_MONTH_NO, // 月検針連番を取得
                devId,      // 装置ID
                null,
                inspYear,   // 検針年
                inspMonth,  // 検針月
                null);

        // 検索
        InspectionMeterSvrSearchResultData entity = find(inspectionMeterSvrSearchServiceDaoImpl, target);
        if (entity == null || entity.getInspMonthNo() == null) {
            return 1;
        }

        return entity.getInspMonthNo().intValue() + 1;
    }

    /**
     * 確定前検針データを取得
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param latestInspDate 最新検針値_日時
     * @param version 排他制御用カラム
     * @return 確定前検針データ
     */
    private boolean checkMeterBef(String devId, Long meterMngId, Date latestInspDate, Integer version) { // 旧：getRowData
        TInspectionMeterBef target = new TInspectionMeterBef();
        target.setId(new TInspectionMeterBefPK());
        target.getId().setDevId(devId);
        target.getId().setMeterMngId(meterMngId);
        target.getId().setLatestInspDate(latestInspDate);

        TInspectionMeterBef entity = find(inspectionMeterBefServiceDaoImpl, target);
        if (entity == null) {
            return false;
        } else if (!Objects.equals(entity.getVersion(), version)) {
            return false;
        }

        return true;
    }

    ///**
    // * 年月から年を取得
    // * @param yearMonth 年月
    // * @return 年
    // */
    //private String convYear (String yearMonth) {
    //    if (yearMonth == null || yearMonth.length() < 4) {
    //        return null;
    //    }
    //    return yearMonth.substring(0, 4);
    //}

    ///**
    // * 年月から月を取得
    // * @param yearMonth 年月
    // * @return 月
    // */
    //private String convMonth(String yearMonth) {
    //    if (yearMonth == null || yearMonth.length() < 6) {
    //        return null;
    //    }
    //    return yearMonth.substring(yearMonth.length() - 2);
    //}

    /**
     * 検針結果データ(サーバ用)有無チェック
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param year 検針年
     * @param month 検針月
     * @param inspMonthNo 月検針連番(非必須)  ※未設定時は null をセット
     * @return 検針結果データ(サーバ用)有無
     */
    private boolean checkFutureInspData(String devId, Long meterMngId, String year, String month, Long inspMonthNo) {

        // 検索条件セット
        InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
                InspectionMeterSvrSearchResultData.MODE_CHECK_FUTURE_INSP_DATA, // 検針結果データ(サーバ用)有無チェック
                devId,      // 装置ID
                meterMngId, // メーター管理番号
                year,       // 検針年
                month,      // 検針月
                inspMonthNo // 月検針連番(未設定時は null をセット)
        );

        // 検索
        InspectionMeterSvrSearchResultData entity = find(inspectionMeterSvrSearchServiceDaoImpl, target);
        if (entity == null) {
            return false;
        }

        return true;
    }

    ///**
    // * 指定した検針年月の検針結果データ有無チェック。(前月のデータ有無チェックに使用)
    // * @param devId 装置ID
    // * @param meterMngId メーター管理番号
    // * @param year 検針年
    // * @param month 検針月
    // * @return true:あり  false:なし
    // */
    //private boolean checkInspData(String devId, Long meterMngId, String year, String month) { // 旧:getInspData()
    //
    //    // 検索条件セット
    //    InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
    //            InspectionMeterSvrSearchResultData.MODE_CHECK_INSP_DATA, // 指定した検針年月の検針結果データ有無チェック。
    //            devId,      // 装置ID
    //            meterMngId, // メーター管理番号
    //            year,       // 検針年
    //            month,      // 検針月
    //            null);
    //
    //    // 検索
    //    InspectionMeterSvrSearchResultData entity = find(inspectionMeterSvrSearchServiceDaoImpl, target);
    //
    //    // 取得できたかチェック → できない場合は、前月の最新を再取得する。
    //    if (entity == null) {
    //        return false;
    //    }
    //
    //    return true;
    //}

    // ※仕様変更 クラウド検針サーバ設計変更_No.7
    ///**
    // * 指定装置ID、メーター管理番号の検針結果データ存在の真偽値を取得する
    // * @param devId 装置ID
    // * @param meterMngId メーター管理番号
    // * @return 検針結果データ存在真偽値
    // */
    //private boolean checkExistsInspDataAllTime(String devId, Long meterMngId) {
    //
    //    // 検索条件セット
    //    InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
    //            InspectionMeterSvrSearchResultData.MODE_CHECK_EXISTS_INSP_DATA_ALL_TIME, // 指定装置ID、メーター管理番号の検針結果データ存在の真偽値を取得
    //            devId,      // 装置ID
    //            meterMngId, // メーター管理番号
    //            null,
    //            null,
    //            null);
    //
    //    // 検索
    //    InspectionMeterSvrSearchResultData entity = find(inspectionMeterSvrSearchServiceDaoImpl, target);
    //
    //    // 取得できたかチェック → できない場合は、前月の最新を再取得する。
    //    if (entity == null) {
    //        return false;
    //    }
    //
    //    return true;
    //}

    /**
     * 指定装置ID・メーター管理番号・検針年月・月検針連番を条件に検針結果データを取得する(直近のデータ取得する)
     * ※引数 月検針連番 が ない 場合、検針年月は"<="(当月を『含む』) を条件に検索して直近の１件を取得する
     * ※引数 月検針連番 が ある 場合、月検針連番は"<"(より前)を条件に検索して直近の１件を取得する
     * @param devId 装置ID(必須)
     * @param meterMngId メーター管理番号(必須)
     * @param year 検針年(必須)
     * @param month 検針月(必須)
     * @param inspMonthNo 月検針連番(非必須)  ※未設定時は null をセット
     * @return 直近の検針結果データ
     */
    private InspectionMeterSvrSearchResultData getLatestInspDataAllPast(String devId, Long meterMngId, String year, String month, Long inspMonthNo) { // 兼 旧：getLatestInspData

        // 検索条件セット
        InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
                InspectionMeterSvrSearchResultData.MODE_LATEST_INSP_DATA_ALL_PAST, // 過去の最終値を取得する
                devId,      // 装置ID
                meterMngId, // メーター管理番号
                year,       // 検針年
                month,      // 検針月
                inspMonthNo // 月検針連番(未設定時は null をセット)
        );

        // 検索
        InspectionMeterSvrSearchResultData entity = find(inspectionMeterSvrSearchServiceDaoImpl, target);

        return entity;
    }

    /**
     * Date型をTimestamp型に変換
     * @param date Date型の日時
     * @return Timestamp型の日時
     */
    private Timestamp convTimestamp(Date date) {
        return (date == null ? null : new Timestamp(date.getTime()));
    }

    /**
     * 確定前検針データを確定する.
     * ※登録エラー時は、Exception で終了
     * @param tms 登録用配列
     */
    private void registInspData(TInspectionMeterSvr target) {
        persist(inspectionMeterSvrServiceDaoImpl, target);
    }

    /**
     * 確定前検針データを削除
     * ※削除エラー時は、Exception で終了
     * @param devId 装置ID(画面項目)
     * @param meterMngId メーター管理番号
     * @param latestInspDate 最新検針値_日時
     * @param version 排他制御用カラム
     * @return 処理結果 true:正常終了 false:削除対象データが無い または 楽観ロックエラー
     */
    private boolean deleteBeforeInspData(String devId, Long meterMngId, Date latestInspDate, Integer version) {

        TInspectionMeterBef target = new TInspectionMeterBef();
        target.setId(new TInspectionMeterBefPK());
        target.getId().setDevId(devId);
        target.getId().setMeterMngId(meterMngId);
        target.getId().setLatestInspDate(latestInspDate);

        TInspectionMeterBef entity = find(inspectionMeterBefServiceDaoImpl, target);

        if (entity == null || !Objects.equals(entity.getVersion(), version)) {
            // 削除対象データが無い または 楽観ロックエラー
            return false;
        }

        remove(inspectionMeterBefServiceDaoImpl, entity);

        return true;
    }

    /**
     * 指定のGET_DATEのDMV_KWHの値を取得する
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param getDate 収集日時
     * @return DMV_KWH値 null:該当データなし
     */
    private BigDecimal getDmvkwhFromGetDate(String devId, Long meterMngId, String getDate){
        TDayLoadSurvey target = new TDayLoadSurvey();
        target.setId(new TDayLoadSurveyPK());
        target.getId().setDevId(devId);
        target.getId().setMeterMngId(meterMngId);
        target.getId().setGetDate(getDate);

        TDayLoadSurvey entity = find(dayLoadSurveyServiceDaoImpl, target);
        if (entity == null) {
            return null;
        }

        return entity.getDmvKwh();
    }

    /**
     * ロードサーベイ日データを登録・更新する
     * ※更新エラー時は、Exception で終了.
     * @param dls ロードサーベイ日データ
     * @return 処理結果  true:正常登録  Exception:登録失敗
     */
    private void upsertLoadSurveyData(TDayLoadSurvey target) {
        // ロードサーベイ日データ・チェック
        TDayLoadSurvey entity = find(dayLoadSurveyServiceDaoImpl, target);
        if (entity == null) {
            // 既存データなし → ロードサーベイ日データ 登録
            persist(dayLoadSurveyServiceDaoImpl, target);
        } else {
            // 既存データあり → ロードサーベイ日データ 更新

            // 更新データセット
            entity.setRecDate(target.getRecDate());
            entity.setRecMan(target.getRecMan());
            entity.setKwh30(target.getKwh30());
            entity.setDmvKwh(target.getDmvKwh());
            entity.setVersion(entity.getVersion() + 1); // 排他制御用カラム
            entity.setUpdateUserId(target.getUpdateUserId());
            entity.setUpdateDate(target.getUpdateDate());
            merge(dayLoadSurveyServiceDaoImpl, entity);
        }
    }

    /**
     * 指定装置ID、メーター管理番号、指定年月、指定連番の検針結果データを取得する
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 月検針連番(非必須)  ※未設定時は null をセット
     * @return 検針結果データ null:該当データなし
     */
    private TInspectionMeterSvr getInspDataPK(String devId, Long meterMngId, String inspYear, String inspMonth, Long inspMonthNo){
        TInspectionMeterSvr target = new TInspectionMeterSvr();
        target.setId(new TInspectionMeterSvrPK());
        target.getId().setDevId(devId);
        target.getId().setMeterMngId(meterMngId);
        target.getId().setInspYear(inspYear);
        target.getId().setInspMonth(inspMonth);
        target.getId().setInspMonthNo(inspMonthNo);

        TInspectionMeterSvr entity = find(inspectionMeterSvrServiceDaoImpl, target);
        return entity;
    }

    /**
     * 対象連番の検針確定データを更新する
     * ※更新エラー時は、Exception で終了.
     * @param updTarget 既存データ
     * @param regTarget 更新データ
     */
    private void updateInspData(TInspectionMeterSvr updTarget, TInspectionMeterSvr regTarget) {

        // 更新データセット
        updTarget.setRecDate(regTarget.getRecDate());
        updTarget.setRecMan(regTarget.getRecMan());
        updTarget.setInspType(regTarget.getInspType());
        updTarget.setLatestInspVal(regTarget.getLatestInspVal());
        updTarget.setPrevInspVal(regTarget.getPrevInspVal());
        updTarget.setPrevInspVal2(regTarget.getPrevInspVal2());
        updTarget.setMultipleRate(regTarget.getMultipleRate());
        updTarget.setLatestUseVal(regTarget.getLatestUseVal());
        updTarget.setPrevUseVal(regTarget.getPrevUseVal());
        updTarget.setUsePerRate(regTarget.getUsePerRate());
        updTarget.setLatestInspDate(regTarget.getLatestInspDate());
        updTarget.setPrevInspDate(regTarget.getPrevInspDate());
        updTarget.setPrevInspDate2(regTarget.getPrevInspDate2());
        updTarget.setEndFlg(regTarget.getEndFlg());
        updTarget.setVersion(updTarget.getVersion() + 1);
        updTarget.setUpdateUserId(regTarget.getUpdateUserId());
        updTarget.setUpdateDate(regTarget.getUpdateDate());

        merge(inspectionMeterSvrServiceDaoImpl, updTarget);
    }

    /**
     * 年 と 月 から yyyyMM の書式の文字列を生成
     * @param year 年(必須) ※4桁
     * @param month 月(必須) ※1～2桁
     * @return 年月
     */
    private String convYyyyMm(String year, String month) {
        return year + (month.length() < 2 ? "0" + month : month);
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

}
