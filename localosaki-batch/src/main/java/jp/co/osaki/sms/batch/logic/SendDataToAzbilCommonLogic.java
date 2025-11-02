package jp.co.osaki.sms.batch.logic;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import jp.co.osaki.osol.entity.TResendInfo;
import jp.co.osaki.osol.entity.TResendInfoPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.dao.SendDataToAzbilTimeInfoDao;
import jp.co.osaki.sms.batch.dto.AzbilServerTokenEndpointPostResDto;
import jp.co.osaki.sms.batch.resultset.AzbilExternalBuildingInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilExternalBuildingMeterInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilMeterDataResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilResendInfoResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.batch.BaseBatchLogic;

/**
 * アズビル送信用共通処理 ロジッククラス
 *
 * @author akr_iwamoto
 *
 */
@Stateless
public final class SendDataToAzbilCommonLogic extends BaseBatchLogic {

    /**
     * データタイプ(指針値)
     */
    private static final String DATATYPE_COL = "col";

    /**
     * データタイプ(使用量)
     */
    private static final String DATATYPE_DEV = "dev";

    private static final String REC_MAN = "end_data_to_azbil";

    /**
     * 再送処理
     */
    private static final int MAX_RESEND_COUNT = 6;;

    /**
     * EntityManager
     */
    private EntityManager entityManager;

    /**
     * Dao
     */
    private SendDataToAzbilTimeInfoDao dao;

    /**
     * コンストラクター
     */
    SendDataToAzbilCommonLogic(){}

    /**
     * コンストラクター
     *
     * @param batchName
     */
    public SendDataToAzbilCommonLogic(EntityManager entityManager, SendDataToAzbilTimeInfoDao dao, String batchName){
        this.entityManager = entityManager;
        this.dao = dao;
        this.batchName = batchName;
    }

    /**
     * アズビルサーバからアクセストークンを取得する
     *
     * @param InfoId
     * @param name
     * @param pass
     * @return トークン取得結果
     */
    public boolean startLogic(List<String> targetDatetimeList, Date svDate) {

        // パラメータチェック
        if (targetDatetimeList == null || svDate == null || dao == null) {
            // パラメータ不正
            errorLogger.error(this.batchName.concat("startLogic() fail invalid parameter:"));
            return false;
        }

        try {
            List<AzbilExternalBuildingInfoResultSet> tExternalBuildingInfoList = null;

            // 外部連携用 建物情報リストを取得リスト取得
            tExternalBuildingInfoList = dao.getExternalBuildingInfo();

            //  外部連携用 建物情報に対して処理実行
            for (AzbilExternalBuildingInfoResultSet tBuildingInfo : tExternalBuildingInfoList) {

                String infoId = tBuildingInfo.getBuildngInfoId();
                String corpId = tBuildingInfo.getCorpId();
                String name = tBuildingInfo.getUsername();
                String pass = tBuildingInfo.getPassword();
                long buildingId = tBuildingInfo.getBuildingId();

                // トークン認証(500の場合は2回リトライ)
                AzbilServerSendRequestLogic azbilServerRequest = new AzbilServerSendRequestLogic(batchName);
                AzbilServerTokenEndpointPostResDto tokenDto = azbilServerRequest.getTokenEndpoint(
                        infoId, name, pass);

                if (tokenDto != null &&
                        tokenDto.getAccess_token() != null &&
                        tokenDto.getHttpResponseCode() == HttpServletResponse.SC_OK) {
                    // トークン取得成功
                    //  開始日時リスト分ループ実行
                    for (String strTargetDatetime : targetDatetimeList) {

                        // 分文字列を切り出し
                        String min = strTargetDatetime.substring(10, 12);

                        // 装置ID取得
                        List<AzbilExternalBuildingMeterInfoResultSet> tMeterInfoList =
                                dao.getExternalBuildingMeterInfo(corpId, buildingId);

                        if (tMeterInfoList != null && !tMeterInfoList.isEmpty()) {
                            boolean isResend = false;
                            StringBuilder data = new StringBuilder();

                            //  装置毎にメーター実績を取得
                            for (AzbilExternalBuildingMeterInfoResultSet tMeterInfo : tMeterInfoList) {
                                String devId = tMeterInfo.getDevId();
                                long meterMngId = tMeterInfo.getMeterMngId();
                                String meterId = tMeterInfo.getMeterId();

                                AzbilMeterDataResultSet meterData = dao.getMeterData(devId, meterMngId, strTargetDatetime);
                                String dmvKwhStr = "";
                                String kwh30Str = "";
                                if (meterData == null || meterData.getDmvKwh() == null || meterData.getKwh30() == null) {
                                    // 欠測がある場合は再送情報へ登録対象とする
                                    isResend = true;
                                } else {
                                    // 送信用データの文字列セット
                                    if (meterData.getDmvKwh() != null) {
                                        dmvKwhStr = meterData.getDmvKwh().toString();
                                    }
                                    if (meterData.getKwh30() != null) {
                                        kwh30Str = meterData.getKwh30().toString();
                                    }
                                }

                                // 終端に改行を挿入
                                if (data.length() > 0) {
                                    data.append("\r\n");
                                }

                                // データフォーマット
                                // "分","識別子","データタイプ","計測値"  ← インデックス部
                                // "00","A1234567891a","col","12345"  ← データ部

                                // データ部 作成(指針値)
                                // 分
                                data.append("\"" + min + "\"");
                                data.append(",");
                                // メーターID
                                data.append("\"" + meterId + "\"");
                                data.append(",");
                                // データタイプ
                                data.append("\"" + DATATYPE_COL + "\"");
                                data.append(",");
                                // 指針値
                                data.append("\"" + dmvKwhStr + "\"");

                                data.append("\r\n");

                                // データ部 作成(使用量)
                                // 分
                                data.append("\"" + min + "\"");
                                data.append(",");
                                // メーターID
                                data.append("\"" + meterId + "\"");
                                data.append(",");
                                // データタイプ
                                data.append("\"" + DATATYPE_DEV + "\"");
                                data.append(",");
                                // 指針値
                                data.append("\"" + kwh30Str + "\"");
                            }

                            // インデックス部 作成(使用量)
                            StringBuilder index = new StringBuilder("\"分\",\"識別子\",\"データタイプ\",\"計測値\""+ "\r\n");
                            // コンテンツとして結合
                            String contents = new String(index.toString().getBytes("SJIS"), "SJIS");
                            contents = contents + data.toString();

                            // 計測値送信
                            azbilServerRequest.sendDayLoadServey(
                                    infoId, strTargetDatetime, tokenDto.getAccess_token(), contents);

                            // 再送処理
                            Calendar tmpSvDate = Calendar.getInstance();
                            tmpSvDate.setTime(svDate);
                            int nowHour = Integer.valueOf(tmpSvDate.get(Calendar.HOUR_OF_DAY));

                            // 8:00から11:00の間
                            if(8 <= nowHour && nowHour <= 11) {
                                // 再送信時刻を前日分でフィルターをかけたlistに対して6回の再送信を行う
                                List<AzbilResendInfoResultSet> resendInfoList = dao.getResendInfo(buildingId);
                                List<AzbilResendInfoResultSet> yesterdayResendInfoList = null;
                                if(resendInfoList != null && resendInfoList.size() != 0) {
                                    yesterdayResendInfoList = resendInfoList.stream()
                                            .filter(day -> day.getResendDatetime().contains(getBeforeTargetDate()))
                                            .collect(Collectors.toList());
                                }

                                // 1施設に対して1回最大6レコード分の再送を行う
                                if(yesterdayResendInfoList != null && yesterdayResendInfoList.size() != 0) {
                                    boolean nextDayFlg = false;
                                    int resendCount = yesterdayResendInfoList.size() > MAX_RESEND_COUNT ? MAX_RESEND_COUNT: yesterdayResendInfoList.size();
                                    for(int resendIdx = 0; resendIdx < resendCount; resendIdx++ ) {
                                        sendResendInfo(yesterdayResendInfoList.get(resendIdx), tMeterInfoList, infoId, tokenDto, azbilServerRequest, nextDayFlg);
                                    }
                                    // 再送信後、再送対象日付の翌日00:00データを送信することでアズビルサーバ側で集計処理が実行される
                                    AzbilResendInfoResultSet nextResendData = new AzbilResendInfoResultSet(buildingId, getNextTargetDate());
                                    nextDayFlg = true;
                                    sendResendInfo(nextResendData, tMeterInfoList, infoId, tokenDto, azbilServerRequest, nextDayFlg);
                                }
                            }

                            // 通常送信分の再送情報登録
                            if (isResend) {
                                // 欠測がある場合は再送情報へ登録
                                // 対象日時かつ建物の再送情報が存在するかチェック
                                AzbilResendInfoResultSet resendInfo = dao.getResendInfo(buildingId, strTargetDatetime);

                                // 再送情報が未登録の場合のみ、追加
                                if (resendInfo == null) {
                                    if (!entityManager.getTransaction().isActive()) {
                                        entityManager.getTransaction().begin();
                                    }

                                    if (entityManager.getTransaction().isActive()) {
                                        // アズビル再送情報 登録処理
                                        addResendInfo(buildingId, strTargetDatetime, svDate);

                                        if (entityManager.getTransaction().getRollbackOnly()) {
                                            entityManager.getTransaction().rollback();
                                        } else {
                                            entityManager.getTransaction().commit();
                                        }
                                    }
                                    entityManager.clear();
                                }
                            }

                        }
                    }

                }
            }
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }


        return true;
    }

    /**
     * 再送情報追加
     * @param buildingId
     * @return アドレスリスト
     */
    private void addResendInfo(long buildingId, String date, Date svDate) {

        // 新規追加
        TResendInfo newTResendInfo = new TResendInfo();
        TResendInfoPK newTResendInfoPK = new TResendInfoPK();
        newTResendInfoPK.setBuildingId(buildingId);
        newTResendInfoPK.setResendDatetime(date);
        newTResendInfo.setId(newTResendInfoPK);
        newTResendInfo.setRecDate(new Timestamp(svDate.getTime()));
        newTResendInfo.setRecMan(REC_MAN);

        // DB登録
        dao.createMEnergyUseTargetMonth(newTResendInfo, new Timestamp(svDate.getTime()));
        batchLogger.info(batchName.concat("MEnergyUseTargetMonthly Add ")
                .concat(" BuildingId = ").concat(newTResendInfo.getId().getBuildingId().toString())
                .concat(" ResendDatetime = ").concat(newTResendInfo.getId().getResendDatetime()));

    }

    /**
     * 再送処理
     * @param buildingId
     * @return アドレスリスト
     * @throws UnsupportedEncodingException
     */
    private void sendResendInfo(AzbilResendInfoResultSet resendInfo, List<AzbilExternalBuildingMeterInfoResultSet> tMeterInfoList, String infoId,
            AzbilServerTokenEndpointPostResDto tokenDto, AzbilServerSendRequestLogic azbilServerRequest, boolean nextDayFlg) throws UnsupportedEncodingException {

        // 再送処理実行
        String resendDate = resendInfo.getResendDatetime();
        // 分文字列を切り出し
        String min = resendDate.substring(10, 12);
        StringBuilder data = new StringBuilder();
        //  装置毎にメーター実績を取得
        for (AzbilExternalBuildingMeterInfoResultSet tMeterInfo : tMeterInfoList) {
            String devId = tMeterInfo.getDevId();
            long meterMngId = tMeterInfo.getMeterMngId();
            String meterId = tMeterInfo.getMeterId();

            AzbilMeterDataResultSet meterData = dao.getMeterData(devId, meterMngId, resendDate);
            String dmvKwhStr = "";
            String kwh30Str = "";
            // 欠測がある場合は処理なし
            if (meterData != null) {
                // 送信用データの文字列セット
                if (meterData.getDmvKwh() != null) {
                    dmvKwhStr = meterData.getDmvKwh().toString();
                }
                if (meterData.getKwh30() != null) {
                    kwh30Str = meterData.getKwh30().toString();
                }
            }

            // 終端に改行を挿入
            if (data.length() > 0) {
                data.append("\r\n");
            }

            // データ部 作成(指針値)
            // 分
            data.append("\"" + min + "\"");
            data.append(",");
            // メーターID
            data.append("\"" + meterId + "\"");
            data.append(",");
            // データタイプ
            data.append("\"" + DATATYPE_COL + "\"");
            data.append(",");
            // 指針値
            data.append("\"" + dmvKwhStr + "\"");

            data.append("\r\n");

            // データ部 作成(使用量)
            // 分
            data.append("\"" + min + "\"");
            data.append(",");
            // メーターID
            data.append("\"" + meterId + "\"");
            data.append(",");
            // データタイプ
            data.append("\"" + DATATYPE_DEV + "\"");
            data.append(",");
            // 指針値
            data.append("\"" + kwh30Str + "\"");
        }

        // インデックス部 作成(使用量)
        StringBuilder index = new StringBuilder("\"分\",\"識別子\",\"データタイプ\",\"計測値\""+ "\r\n");
        // コンテンツとして結合
        String contents = new String(index.toString().getBytes("SJIS"), "SJIS");
        contents = contents + data.toString();

        // 再送信
        azbilServerRequest.sendDayLoadServey(infoId, resendDate, tokenDto.getAccess_token(), contents);

        if(nextDayFlg) {
            return;
        }

        // 再送結果にかかわらず、再送レコード削除
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        if (entityManager.getTransaction().isActive()) {
            // アズビルバッチ外部起動設定削除
            dao.removeResendInfo(resendInfo.getBuildingId(), resendInfo.getResendDatetime());

            if (entityManager.getTransaction().getRollbackOnly()) {
                entityManager.getTransaction().rollback();
            } else {
                entityManager.getTransaction().commit();
            }
        }
        entityManager.clear();
    }

    /**
     * 前日の指定日を取得
     * @return 前日
     */
    private String getBeforeTargetDate() {
          Date date = new Date();
          String beforeDay = DateUtility.changeDateFormat(DateUtility.plusDay(date, -1), DateUtility.DATE_FORMAT_YYYYMMDD);
          return beforeDay;
    }

    /**
     * 翌日の00:00を取得
     * @return 翌日00:00
     */
    private String getNextTargetDate() {
        Date date = new Date();
        String nextDay = DateUtility.changeDateFormat(DateUtility.plusDay(date, 0), DateUtility.DATE_FORMAT_YYYYMMDD);
        return nextDay+"0000";
    }
}
