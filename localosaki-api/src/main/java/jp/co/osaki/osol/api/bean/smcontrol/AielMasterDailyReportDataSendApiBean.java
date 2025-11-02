package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterDailyReportDataSendApiDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterDailyReportDataSendApiParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.weather.AmedasWeatherListDetailResultData;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210008Param;
import jp.co.osaki.osol.mng.param.A210011Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;


/**
 * [AielMaster]日報・アメダスデータ送信 Bean クラス
 * アメダスデータ送信も同時に行う
 * @author nishida.t
 *
 */
@Named(value = "AielMasterDailyReportDataSendApiBean")
@RequestScoped
public class AielMasterDailyReportDataSendApiBean  extends OsolApiBean<AielMasterDailyReportDataSendApiParameter>
            implements BaseApiBean<AielMasterDailyReportDataSendApiParameter,SmControlApiResponse<BaseSmControlApiResult>> {

    private AielMasterDailyReportDataSendApiParameter parameter = new AielMasterDailyReportDataSendApiParameter();

    private SmControlApiResponse<BaseSmControlApiResult> response = new SmControlApiResponse<>();

    SmControlApiResponse<BaseSmControlApiResult> execRes;

    List<CommonDemandDayReportPointListResult> demandDayReportPointList;
    List<DemandBuildingSmListDetailResultData> demandBuildingSmListDetailList ;
    List<SmPointListDetailResultData> smPointList;
    BuildingDemandListDetailResultData buildingDemandListDetailResultData;
    List<AmedasWeatherListDetailResultData> amedasWeatherListDetailResultDataList;

    @Inject
    private FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

    @EJB
    private AielMasterDailyReportDataSendApiDao dao;

    private SmPrmResultData smPrm;

    private SmPrmResultData collectSmPrm;

    private Long loginUserId = null;

    private String loginCorpId = null;

    private String loginPersonId = null;

    private Date measurementDateFrom = null;

    private Date measurementDateTo = null;

    private List<String> resultCodeList;

    //マイナス符号
    private static final String STRING_MINUS_SIGN = "-";

    //プラス符号
    private static final String STRING_PLUS_SIGN = "+";

    //小数
    private static final String STRING_DECIMAL = ".";

    //ポイント種別「P」の最大値
    private static final String POINT_TYPE_P_MAX_VALUE = "9999";

    //ポイント種別「P」の最小値
    private static final String POINT_TYPE_P_MIN_VALUE = "0000";

    //ポイント種別「A」の最大値
    private static final String POINT_TYPE_A_MAX_VALUE = STRING_PLUS_SIGN + "999";

    //ポイント種別「A」の最小値
    private static final String POINT_TYPE_A_MIN_VALUE = STRING_MINUS_SIGN + "999";

    //ポイント値のnull対応値
    private static final String POINT_NULL_VALUE = "    ";

    //ポイント番号「SRC」
    private static final String POINT_NO_SRC = "SRC";

    //ポイント番号「SRC」の最大値
    private static final String POINT_NO_SRC_MAX_VALUE = "9999";

    //ポイント番号「SRC」の最小値
    private static final String POINT_NO_SRC_MIN_VALUE = "0000";

    //0時の判定用 → DBでは24時は0時のため
    public static final String TIME_HOUR_00 = "00";

     //24時制の最大時間
    public static final int TIME_HOUR_MAX = 24;

    //外気温
    public static final String AMEDAS_DATA_TEMPERATURE = "temperature";

    //湿度
    public static final String AMEDAS_DATA_HUMIDITY = "humidity";

    /**
     * メイン処理
     *
     * @return レスポンス
     */
    @Override
    public SmControlApiResponse<BaseSmControlApiResult> execute() throws Exception {
        AielMasterDailyReportDataSendApiParameter param = new AielMasterDailyReportDataSendApiParameter();

        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setMeasurementDateFrom(this.parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(this.parameter.getMeasurementDateTo());
        param.setCollectDataSmId(this.parameter.getCollectDataSmId());

        response = executeApi(param);

        int errorCount = 0;

        //応答コードがエラーのものをカウントする
        for(String details: resultCodeList) {
            if(!OsolApiResultCode.API_OK.equals(details)) {
                errorCount++;
            }
        }

        //全て成功
        if(errorCount == 0) {
            response.setResultCode(OsolApiResultCode.API_OK);
        }
        //全て失敗
        else if(errorCount == resultCodeList.size()) {
            response.setResultCode(OsolApiResultCode.API_ERROR_DAILYREPORT_AMEDAS_DATA_SEND_COLLECTFAILED);
        }
        //成功/失敗が混在
        else {
            response.setResultCode(OsolApiResultCode.API_ERROR_DAILYREPORT_AMEDAS_DATA_SEND_COLLECTWARN);
        }
        return response;
    }

    /**
     * 日報・アメダスデータ送信 メイン
     *
     * @param param
     * @return
     */
    private SmControlApiResponse<BaseSmControlApiResult> executeApi(AielMasterDailyReportDataSendApiParameter param) throws SmControlException{

        //初期化
       this.execRes = new SmControlApiResponse<>();

        resultCodeList = new ArrayList<String>();

        // loginCorpId 取得
        this.loginCorpId = param.getLoginCorpId();

        // loginPersonId 取得
        this.loginPersonId = param.getLoginPersonId();

        // userID 取得
        MPerson mPerson = getPerson(param);
        if (mPerson == null) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" corpId=%s,personId=%s", this.loginCorpId, this.loginCorpId));
            resultCodeList.add(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return execRes;
        }
        this.loginUserId = mPerson.getUserId();

        this.measurementDateFrom = DateUtility.conversionDate(param.getMeasurementDateFrom(), DateUtility.DATE_FORMAT_YYYYMMDD);
        this.measurementDateTo = DateUtility.conversionDate(param.getMeasurementDateTo(), DateUtility.DATE_FORMAT_YYYYMMDD);

        //建物機器 取得
        getBuildingSmList(param);

        // 機器が含まれていない場合はエラー
        if (this.demandBuildingSmListDetailList.stream()
                .filter(o -> o.getSmId().equals(param.getSmId()))
                .count() != 1) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("SM_ID=%s", param.getSmId()));
            resultCodeList.add(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
        }

        // 実績データ取得機器が含まれていない場合はエラー
        if (this.demandBuildingSmListDetailList.stream()
                .filter(o -> o.getSmId().equals(param.getCollectDataSmId()))
                .count() != 1) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("COLLECT_DATA_SM_ID=%s", param.getCollectDataSmId()));
            resultCodeList.add(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
        }

        //機器情報 取得
        this.smPrm = getSmInfo(param.getSmId());

        // 送信対象の機器がOSOLで利用できる機種であること
        if (!(SmControlConstants.PRODUCT_CD_FV2.equals(this.smPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_D.equals(this.smPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(this.smPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(this.smPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_E_ALPHA.equals(this.smPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.smPrm.getProductCd())
                )) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
              errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
              resultCodeList.add(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
        }

        //実績データ取得対象の機器情報 取得
        this.collectSmPrm = getSmInfo(param.getCollectDataSmId());

        // 実績データ取得対象の機器がOSOLで利用できる機種であること
        if (!(SmControlConstants.PRODUCT_CD_FV2.equals(this.collectSmPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_D.equals(this.collectSmPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(this.collectSmPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(this.collectSmPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_E_ALPHA.equals(this.collectSmPrm.getProductCd())
                || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.collectSmPrm.getProductCd())
                )) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
              errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("COLLECT_SM_PRODUCT_CD=%s", collectSmPrm.getProductCd()));
              resultCodeList.add(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
        }

        //機器ポイント 取得
        getSmPointInfo(param);

        //デマンド日報ポイントのポイントNoと機器ポイントのポイント種別を突き合わせるために機器ポイントをMapに書き換える
        Map<String, SmPointListDetailResultData> smPointMap = new HashMap<String, SmPointListDetailResultData>();

        //ListからMapに置き換える
        for(SmPointListDetailResultData smPoint : smPointList) {
            smPointMap.put(smPoint.getPointNo(), smPoint);
        }

        //建物デマンド 取得
        getBuildingDemand(param);

        //開始日が終了日より前の日 または開始日と終了日が同じ日の間繰り返す
        while(this.measurementDateFrom.before(this.measurementDateTo) || DateUtility.checkSameYearMonthDay(this.measurementDateFrom, this.measurementDateTo)){

            //進めている現在日をパラメータにセット
            param.setMeasurementDateFrom(DateUtility.changeDateFormat((Date) this.measurementDateFrom.clone(), DateUtility.DATE_FORMAT_YYYYMMDD));
            param.setMeasurementDateTo(DateUtility.changeDateFormat((Date) this.measurementDateFrom.clone(), DateUtility.DATE_FORMAT_YYYYMMDD));

            //デマンド日報ポイント 取得
            getDemandDayRepPointInfo(param);

            //建物デマンドにアメダス観測所番号がある場合、アメダス気象データ取得
            if(buildingDemandListDetailResultData != null && buildingDemandListDetailResultData.getAmedasObservatoryNo() != null) {
                param.setAmedasObservatoryNo(buildingDemandListDetailResultData.getAmedasObservatoryNo());

                //アメダス気象データ 取得
                getAmedasWeatherObservatoryNoList(param);
            }

            //機器リクエスト用の日報データ作成
            CreateDailyReportDataReqInfo createDailyReportDataReqInfo = null;

            //機器リクエスト用のアメダスデータ作成
            CreateAmedasDataReqInfo createAmedasDataReqInfo = null;


            //対象日のデマンド日報ポイントが１件以上存在する場合
            if(this.demandDayReportPointList != null && this.demandDayReportPointList.size() > 0) {
                //必要なデマンド日報ポイント情報を抽出
                Map<String, Map<String, AielMasterDailyReportDataSendApiPointInfo>> jigenNoMap = mixPointValueAndPointType(smPointMap, this.demandDayReportPointList);

                //機器リクエスト用の日報データ作成
                createDailyReportDataReqInfo = createDailyReportDataReq(jigenNoMap, this.measurementDateFrom);
            }

            //対象日のアメダスデータが１件以上存在する場合
            if(this.amedasWeatherListDetailResultDataList != null && this.amedasWeatherListDetailResultDataList.size() > 0) {
                //機器リクエスト用のアメダスデータ作成
                createAmedasDataReqInfo = createAmedasDataReq(amedasWeatherListDetailResultDataList, this.measurementDateFrom);
            }

            //比較元の日付を１日進める
            this.measurementDateFrom = DateUtility.plusDay(this.measurementDateFrom, 1);

            //取得日のデマンド日報ポイントが１件も存在しない場合は、機器制御を省略
            if(createDailyReportDataReqInfo != null) {
                //Pramリストを生成
                List<Map<String,String>> demandDayReportList = createDailyReportDataReqInfo.getDemandDayReportList();
                A210011Param paramReq = createDailyReportDataSendParam(createDailyReportDataReqInfo.getSettingDate(), demandDayReportList);

                //日報データ送信実行
                executeFvpControl(paramReq);
            }

            //取得日のアメダス気象データが１件も存在しない場合は、機器制御を省略
            if(createAmedasDataReqInfo != null) {
                //Pramリストを生成
                List<Map<String,String>> amedastList = createAmedasDataReqInfo.getAmedasList();
                A210008Param paramReq = createAmedasDataSendParam(createAmedasDataReqInfo.getSendDate(), amedastList);

                //アメダスデータ送信実行
                executeFvpControl(paramReq);
            }

        }
        return execRes;
    }

    /**
     * デマンド日報ポイント 取得
     * @param param
     */
    private void getDemandDayRepPointInfo(AielMasterDailyReportDataSendApiParameter param) {

        FvpCtrlMngResponse<BaseParam> fvpRes = new FvpCtrlMngResponse<BaseParam>();
        List<CommonDemandDayReportPointListResult> demandDayReportPointList = null;
        try {
            demandDayReportPointList = this.dao.getDemandDailyReportPointList(fvpRes, param);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
            // エラーの場合でも空のデマンド日報ポイントをセットする。
            if (demandDayReportPointList == null) {
                demandDayReportPointList = new ArrayList<CommonDemandDayReportPointListResult>();
            }
        }
        this.demandDayReportPointList = demandDayReportPointList;
    }

    /**
     * 建物機器リスト 取得
     * @param param
     */
    private void getBuildingSmList(AielMasterDailyReportDataSendApiParameter param) {

        List<DemandBuildingSmListDetailResultData> demandBuildingSmListDetailList = null;
        try {
            demandBuildingSmListDetailList = this.dao.getBuildingSmList(param);
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
            // エラーの場合でも空のデマンド日報ポイントをセットする。
            if (demandBuildingSmListDetailList == null) {
                demandBuildingSmListDetailList = new ArrayList<DemandBuildingSmListDetailResultData>();
            }
        }
        this.demandBuildingSmListDetailList = demandBuildingSmListDetailList;
    }

    /**
     * 機器情報 取得
     * @param param
     *
     */
    private SmPrmResultData getSmInfo(Long smId) {

        SmPrmResultData smPrm = null;

        // 機器IDが正しく取得できていれば、機器情報取得
        try {
            if (smId != null) {
                smPrm = this.dao.findSmPrm(smId);
            }
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
            // エラーの場合でも空の機器情報をセットする。
            if (smPrm == null) {
                smPrm = new SmPrmResultData();
            }
            smPrm.setSmId(smId);
        }
        return smPrm;
    }

    /**
     * 機器ポイント 取得
     * @param param
     */
    private void getSmPointInfo(AielMasterDailyReportDataSendApiParameter param) {

        FvpCtrlMngResponse<BaseParam> fvpRes = new FvpCtrlMngResponse<BaseParam>();
        List<SmPointListDetailResultData> smPointList = null;

        try {
            smPointList = this.dao.getSmPointList(fvpRes, param);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
         // エラーの場合でも空の機器ポイントをセットする。
            if (smPointList == null) {
                smPointList = new ArrayList<SmPointListDetailResultData>();
            }
        }
        this.smPointList = smPointList;
    }

    /**
     * 建物デマンド 取得
     * @param param
     *
     */
    private void getBuildingDemand(AielMasterDailyReportDataSendApiParameter param) {

        FvpCtrlMngResponse<BaseParam> fvpRes = new FvpCtrlMngResponse<BaseParam>();
        List<BuildingDemandListDetailResultData> buildingDemandListDetailResultData = null;

        try {
            buildingDemandListDetailResultData = this.dao.getBuildingDemandList(fvpRes, param);

            if(buildingDemandListDetailResultData == null
                    || buildingDemandListDetailResultData.size() != 1
                    || OsolConstants.FLG_OFF.equals(buildingDemandListDetailResultData.get(0).getOutAirTempDispFlg())
                    || CheckUtility.isNullOrEmpty(buildingDemandListDetailResultData.get(0).getAmedasObservatoryName())) {
                this.buildingDemandListDetailResultData = new BuildingDemandListDetailResultData();
            } else {
                this.buildingDemandListDetailResultData = buildingDemandListDetailResultData.get(0);
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
            // エラーの場合でも空の建物デマンドをセットする。
            if (buildingDemandListDetailResultData == null) {
                this.buildingDemandListDetailResultData = new BuildingDemandListDetailResultData();
            }
        }
    }

    /**
     * アメダス気象データ 取得
     * @param param
     */
    private void getAmedasWeatherObservatoryNoList(AielMasterDailyReportDataSendApiParameter param) {

        FvpCtrlMngResponse<BaseParam> fvpRes = new FvpCtrlMngResponse<BaseParam>();
        List<AmedasWeatherListDetailResultData> amedasWeatherListDetailResultDataList = null;
        try {
            amedasWeatherListDetailResultDataList = this.dao.getAmedasWeatherObservatoryNoList(fvpRes, param);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } finally {
            // エラーの場合でも空のアメダス気象データをセットする。
            if (amedasWeatherListDetailResultDataList == null) {
                amedasWeatherListDetailResultDataList = new ArrayList<AmedasWeatherListDetailResultData>();
            }
        }
        this.amedasWeatherListDetailResultDataList = amedasWeatherListDetailResultDataList;
    }

    /**
     * [AielMAster]日報データ送信用 Paramリスト生成
     * @param parameter
     * @return
     */
    private A210011Param createDailyReportDataSendParam(Date settingDate , List<Map<String,String>> demandDayReportList) {

        // Paramリストを生成
        A210011Param paramReq = new A210011Param();

        paramReq.setSettingDate(DateUtility.changeDateFormat(settingDate, DateUtility.DATE_FORMAT_YYMMDDHHMM));
        paramReq.setDemandDayReportList(demandDayReportList);

        return paramReq;
    }

    /**
     * [AielMAster]アメダスデータ送信用 Paramリスト生成
     * @param parameter
     * @return
     */
    private A210008Param createAmedasDataSendParam(Date sendDate , List<Map<String,String>> amedasDaytList) {

        // Paramリストを生成
        A210008Param paramReq = new A210008Param();

        paramReq.setSendDate(DateUtility.changeDateFormat(sendDate, DateUtility.DATE_FORMAT_YYMMDDHHMM));
        paramReq.setAmedasList(amedasDaytList);

        return paramReq;
    }

    /**
     * [AielMAster]日報データ送信 機器通信リクエスト生成
     * または
     * [AielMAster]アメダスデータ送信 機器通信リクエスト生成
     *
     * @param paramReq
     * @return リクエストリスト
     *
     */
    private FvpCtrlMngRequest<BaseParam> createFvpReq(BaseParam paramReq) {

            FvpCtrlMngRequest<BaseParam> fvpReq = null;
            fvpReq = new FvpCtrlMngRequest<>(this.smPrm,
                                            this.loginCorpId,
                                            this.loginPersonId,
                                            this.loginUserId);

            fvpReq.setParam(paramReq);

        return fvpReq;
    }

    private void executeFvpControl(BaseParam paramReq) {

        // 機器通信リクエスト生成
        FvpCtrlMngRequest<BaseParam> fvpReq = createFvpReq(paramReq);
        FvpCtrlMngResponse<BaseParam> res = null;

        try {
            // 機器制御を実行し、結果を返却
            res = executeFvp(fvpReq);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        if(res == null) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("Response=%s", "NULL"));
            resultCodeList.add(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
        }
        else {
            if(!OsolApiResultCode.API_OK.equals(res.getFvpResultCd())) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("FvpResultCode=%s", res.getFvpResultCd()));
                resultCodeList.add(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
            }
            else {
                this.execRes.setResultCode(res.getFvpResultCd());
                resultCodeList.add(OsolApiResultCode.API_OK);
            }
        }
    }

    /**
     * デマンド日報ポイントを扱うクラス
     * @author nishida.t
     *
     */
    private class AielMasterDailyReportDataSendApiPointInfo {

        //ポイント値
        private BigDecimal pointValue;

        //ポイント種別
        private String pointType;

        //ポイント番号
        private String pointNo;

        public BigDecimal getPointValue() {
            return pointValue;
        }

        public void setPointValue(BigDecimal pointValue) {
            this.pointValue = pointValue;
        }

        public String getPointType() {
            return pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
        }

        public String getPointNo() {
            return pointNo;
        }

        public void setPointNo(String pointNo) {
            this.pointNo = pointNo;
        }
    }

    /**
     * リクエスト用のデマンド日報ポイントを扱うクラス
     * @author nishida.t
     *
     */
    private class CreateDailyReportDataReqInfo{

        private List<Map<String,String>> demandDayReportList;

        private Date settingDate;

        public CreateDailyReportDataReqInfo(List<Map<String,String>> demandDayReportList, Date settingDate) {
            setDemandDayReportList(demandDayReportList);
            setSettingDate(settingDate);
        }

        public List<Map<String, String>> getDemandDayReportList() {
            return demandDayReportList;
        }

        public void setDemandDayReportList(List<Map<String, String>> demandDayReportList) {
            this.demandDayReportList = demandDayReportList;
        }

        public Date getSettingDate() {
            return settingDate;
        }

        public void setSettingDate(Date settingDate) {
            this.settingDate = settingDate;
        }
    }

    /**
     *リクエスト用のアメダスデータを扱うクラス
     * @author nishida.t
     *
     */
    private class CreateAmedasDataReqInfo{

        private List<Map<String,String>> amedasList;

        private Date sendDate;

        public CreateAmedasDataReqInfo(List<Map<String,String>> amedasList, Date sendDate) {
            setAmedasList(amedasList);
            setSendDate(sendDate);
        }

        public List<Map<String, String>> getAmedasList() {
            return amedasList;
        }

        public void setAmedasList(List<Map<String, String>> amedasList) {
            this.amedasList = amedasList;
        }

        public Date getSendDate() {
            return sendDate;
        }

        public void setSendDate(Date sendDate) {
            this.sendDate = sendDate;
        }
    }

    /**
     * デマンド日報ポイントと機器ポイントで突き合わせて必要データ抽出
     * @param smPointMap
     * @param demandDayReportPointList
     * @return
     */
    private Map<String, Map<String, AielMasterDailyReportDataSendApiPointInfo>> mixPointValueAndPointType(Map<String, SmPointListDetailResultData> smPointMap,
            List<CommonDemandDayReportPointListResult> demandDayReportPointList){

        Map<String, Map<String, AielMasterDailyReportDataSendApiPointInfo>> jigenNoMap = new LinkedHashMap<String, Map<String, AielMasterDailyReportDataSendApiPointInfo>>();
        for(int i = 1; i <= 48; i++) {

            Map<String, AielMasterDailyReportDataSendApiPointInfo> pointMap = new LinkedHashMap<String, AielMasterDailyReportDataSendApiPointInfo>();
            pointMap.put(POINT_NO_SRC, null);
            for(int j = 1; j <= 256; j++) {
                pointMap.put(String.valueOf(String.format("%03d", j)), null);
            }
            jigenNoMap.put(String.valueOf(i), pointMap);
        }

        //対応した時限に詰める
        for(CommonDemandDayReportPointListResult demandDayReportPoint  : demandDayReportPointList) {

            AielMasterDailyReportDataSendApiPointInfo aielMasterDailyReportDataSendApiPointInfo = new AielMasterDailyReportDataSendApiPointInfo();
            aielMasterDailyReportDataSendApiPointInfo.setPointValue(demandDayReportPoint.getPointVal());
            aielMasterDailyReportDataSendApiPointInfo.setPointType(smPointMap.get(demandDayReportPoint.getPointNo()).getPointType());
            aielMasterDailyReportDataSendApiPointInfo.setPointNo(demandDayReportPoint.getPointNo());

            jigenNoMap.get(String.valueOf(demandDayReportPoint.getJigenNo())).put(demandDayReportPoint.getPointNo(), aielMasterDailyReportDataSendApiPointInfo);
        }
        return jigenNoMap;
    }

    private CreateDailyReportDataReqInfo createDailyReportDataReq(Map<String, Map<String, AielMasterDailyReportDataSendApiPointInfo>> jigenNoMap, Date settingDate){

        //Listを48時限リスト作成
        List<Map<String,String>> demandDayReportList = new ArrayList<Map<String,String>>();

        // 計測日（YYMMDD0000）
        settingDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(settingDate, DateUtility.DATE_FORMAT_YYMMDD).concat("0000"), DateUtility.DATE_FORMAT_YYMMDDHHMM) ;

        for(int i = 1; i <= 48; i++) {
            Map<String, String> demandDayReport = new LinkedHashMap<String, String>();
            AielMasterDailyReportDataSendApiPointInfo pointInfo = new AielMasterDailyReportDataSendApiPointInfo();

            //ポイント番号「SRC」
            String pointNo = POINT_NO_SRC;
            pointInfo = jigenNoMap.get(String.valueOf(i)).get(pointNo);

            //DBにデマンド日報データが存在しない
            if(pointInfo == null) {
                demandDayReport.put(pointNo, convertPointValue(null, null, null));
            }
            else {
                demandDayReport.put(pointNo, convertPointValue(pointInfo.getPointValue(), pointInfo.getPointType(), pointInfo.getPointNo()));
            }

            //ポイント番号「1」～「256」
            for(int j = 1; j <= 256; j++) {
                pointNo = String.valueOf(String.format("%03d", j));
                pointInfo = jigenNoMap.get(String.valueOf(i)).get(pointNo);

                //DBにデマンド日報データが存在しない
                if(pointInfo == null) {
                    demandDayReport.put(pointNo, convertPointValue(null, null, null));
                }
                else {
                    demandDayReport.put(pointNo, convertPointValue(pointInfo.getPointValue(), pointInfo.getPointType(), pointInfo.getPointNo()));
                }
            }
            demandDayReportList.add(demandDayReport);
        }
        return new CreateDailyReportDataReqInfo(demandDayReportList, settingDate);
    }

    private CreateAmedasDataReqInfo createAmedasDataReq(List<AmedasWeatherListDetailResultData> amedasWeatherListDetailResultDataList, Date sendDate){

        List<Map<String, String>> amedasList = new ArrayList<>();

        // 取得したアメダス気象データを観測日時順にソートする
        amedasWeatherListDetailResultDataList = amedasWeatherListDetailResultDataList.stream().sorted(Comparator.comparing(AmedasWeatherListDetailResultData::getObservationDate, Comparator.naturalOrder())).collect(Collectors.toList());

        // 送信日時（YYMMDD0000）
        sendDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(sendDate, DateUtility.DATE_FORMAT_YYMMDD).concat("0000"), DateUtility.DATE_FORMAT_YYMMDDHHMM) ;

        int jigenCount = 1;     //アメダス時限カウント用

        for(AmedasWeatherListDetailResultData targetData : amedasWeatherListDetailResultDataList) {
            Map<String, String> detail = new HashMap<>();

            while(true) {
                String jigenCountStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, jigenCount);

                if(DateUtility.changeDateFormat(targetData.getObservationDate(), DateUtility.DATE_FORMAT_HH).equals(jigenCountStr)
                        || (jigenCount == TIME_HOUR_MAX && DateUtility.changeDateFormat(targetData.getObservationDate(), DateUtility.DATE_FORMAT_HH).equals(TIME_HOUR_00))) {

                    // 外気温（符号 + 整数2桁、小数1桁の数値を小数なしで連携）
                    DecimalFormat temperatureDf = new DecimalFormat("+00.0;-#");
                    detail.put(AMEDAS_DATA_TEMPERATURE, temperatureDf.format(targetData.getOutAirTemp()).replace(OsolConstants.STR_PERIOD, OsolConstants.STR_EMPTY));

                    // 湿度（今は連携がないので、スペース3）
                    detail.put(AMEDAS_DATA_HUMIDITY, OsolConstants.STR_HALF_SPACE.concat(OsolConstants.STR_HALF_SPACE).concat(OsolConstants.STR_HALF_SPACE));

                    amedasList.add(detail);
                    jigenCount++;
                    break;
                }
                else if(jigenCount > TIME_HOUR_MAX) {
                    break;
                }
                //データなしの場合
                else {
                    // 外気温（データなし、スペース4）
                    detail.put(AMEDAS_DATA_TEMPERATURE, OsolConstants.STR_HALF_SPACE.concat(OsolConstants.STR_HALF_SPACE).concat(OsolConstants.STR_HALF_SPACE).concat(OsolConstants.STR_HALF_SPACE));
                    // 湿度（今は連携がないので、スペース3）
                    detail.put(AMEDAS_DATA_HUMIDITY, OsolConstants.STR_HALF_SPACE.concat(OsolConstants.STR_HALF_SPACE).concat(OsolConstants.STR_HALF_SPACE));

                    amedasList.add(detail);
                    detail = new HashMap<>();
                    jigenCount++;
                }
            }
        }
        return new CreateAmedasDataReqInfo(amedasList, sendDate);
    }

    /**
     * ポイント種別ごとにポイント値を変換する
     * @param pointValue
     * @param pointType
     * @return
     */
    private String convertPointValue(BigDecimal pointValue, String pointType, String pointNo) {

        String pointValueStr;

        //nullの場合 → スペース4つ
        if(pointType == null) {
            pointValueStr = POINT_NULL_VALUE;
        }
        //ポイント番号が「SRC」の場合
        else if(pointNo.equals(POINT_NO_SRC)) {
            if(pointValue == null) {
                pointValueStr = POINT_NULL_VALUE;
             }

            //ポイント値がnull以外
            else {
                pointValueStr = pointValue.toString();

                //文字列の先頭１文字を取得 → 先頭１文字がマイナス符号か判定に用いる
                String minusSignStr = pointValueStr.substring(0, 1);

                //取り出した１文字目が"-"(マイナス)だった場合
                if(minusSignStr.equals(STRING_MINUS_SIGN)) {
                    pointValueStr = POINT_NO_SRC_MIN_VALUE;     //ポイント番号「SRC」の最小値
                }

                //マイナスではない場合 → 正の数値
                else {
                    //文字列に小数部が存在する場合
                    if(!(pointValueStr.indexOf(STRING_DECIMAL) == -1)) {
                        int decimalPoint = pointValueStr.indexOf(STRING_DECIMAL);
                        //文字列の頭から「.」の位置まで切り取る
                        pointValueStr = pointValueStr.substring(0, decimalPoint);

                        //小数部を除いた文字列５桁以上の場合
                        if(pointValueStr.length() >= 5) {
                            pointValueStr = POINT_NO_SRC_MAX_VALUE;     //ポイント番号「SRC」の最大値
                        }
                        else {
                            //4桁に満たない場合は０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_4, Integer.parseInt(pointValueStr));
                        }
                    }
                    //文字列が4桁以下の場合
                    else if(pointValueStr.length() <= 4) {
                        //4桁に満たない場合は０埋め
                        pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_4, Integer.parseInt(pointValueStr));
                    }
                    //小数部が存在しない5桁以上の文字列
                    else {
                        pointValueStr = POINT_NO_SRC_MAX_VALUE;     //ポイント番号「SRC」の最大値
                    }
                }
            }

        }
        //アナログ（A）の場合 → "-999"～"+999"
        else if(pointType.equals(ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal())) {
            if(pointValue == null) {
                pointValueStr = POINT_NULL_VALUE;
             }

            //ポイント値がnull以外
            else {
                pointValueStr = pointValue.toString();

                //文字列の先頭１文字を取得 → 先頭１文字がマイナス符号か判定に用いる
                String minusSignStr = pointValueStr.substring(0, 1);

                //取り出した１文字目が"-"(マイナス)だった場合
                if(minusSignStr.equals(STRING_MINUS_SIGN)) {

                    //文字列に小数部が存在する場合
                    if(!(pointValueStr.indexOf(STRING_DECIMAL) == -1)) {

                        //小数点を取り除いた文字列を生成
                        pointValueStr = pointValueStr.replace(STRING_DECIMAL, "");

                        //マイナス符号を取り除いた文字列を生成
                        pointValueStr = pointValueStr.replace(STRING_MINUS_SIGN, "");

                        //小数点とマイナス符号を取り除いた文字列が3桁以下の場合
                        if (pointValueStr.length() <= 3) {
                            //3桁満たない場合は０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, Integer.parseInt(pointValueStr));

                            //０埋めした３桁文字にマイナス符号を加えた文字列を生成
                            pointValueStr = minusSignStr.concat(pointValueStr);
                        }
                        //小数点含む６桁以上
                        else {
                            pointValueStr = POINT_TYPE_A_MIN_VALUE;     //ポイント種別「A」の最小値
                        }
                    }

                    //小数部が存在しない
                    else {
                        //"-"を含め文字列が４未満の場合
                        if(pointValueStr.length() < 4) {
                            //先頭の「-」以降の文字列を取り出す
                            pointValueStr = pointValueStr.substring(1);

                            //３桁に満たない場合は０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, Integer.parseInt(pointValueStr));

                            //０埋めした３桁文字にマイナス符号を加えた文字列を生成
                            pointValueStr = minusSignStr.concat(pointValueStr);
                        }
                        //小数部が存在しないマイナス符号付き４桁以上
                        else {
                            pointValueStr = POINT_TYPE_A_MIN_VALUE;     //ポイント種別「A」の最小値
                        }
                    }
                }

                //取り出した１文字目が"-"(マイナス)でない場合 → 正の数値
                else {
                    //文字列に小数部が存在する場合
                    if(!(pointValueStr.indexOf(STRING_DECIMAL) == -1)) {

                        //小数点を取り除いた文字列を生成
                        pointValueStr = pointValueStr.replace(STRING_DECIMAL, "");

                        //小数点を取り除いた文字列が３桁以下の場合
                        if(pointValueStr.length() <= 3) {

                            //３桁に満たない場合は０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, Integer.parseInt(pointValueStr));

                            //０埋めした３桁文字にプラス符号を加えた文字列を生成
                            pointValueStr = STRING_PLUS_SIGN.concat(pointValueStr);
                        }
                        else {
                            //小数点を含む5桁以上
                            pointValueStr = POINT_TYPE_A_MAX_VALUE;     //ポイント種別「A」の最大値
                        }
                    }

                    //文字列に小数部が存在しない場合
                    else {
                        if(pointValueStr.length() <= 2) {
                            //末尾に０を追加
                            pointValueStr = pointValueStr.concat("0");

                            //３桁になるように０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, Integer.parseInt(pointValueStr));

                            //０埋めした３桁文字にプラス符号を加えた文字列を生成
                            pointValueStr = STRING_PLUS_SIGN.concat(pointValueStr);
                        }
                        else {
                            //小数部が存在しない3桁以上の文字列
                            pointValueStr = POINT_TYPE_A_MAX_VALUE;     //ポイント種別「A」の最大値
                        }
                    }
                }
            }
        }
        //パルス（P）の場合 → "0000"～"9999"
        else if(pointType.equals(ApiGenericTypeConstants.POINT_TYPE.PULSE.getVal())) {
            if(pointValue == null) {
               pointValueStr = POINT_NULL_VALUE;
            }
            else {
                pointValueStr = pointValue.toString();

                //文字列の先頭１文字を取得 → 先頭１文字がマイナス符号か判定に用いる
                String minusSignStr = pointValueStr.substring(0, 1);

                //取り出した１文字目が"-"(マイナス)だった場合
                if(minusSignStr.equals(STRING_MINUS_SIGN)) {
                    pointValueStr = POINT_TYPE_P_MIN_VALUE;     //ポイント種別「P」の最小値
                }

                //マイナスではない場合 → 正の数値
                else {
                    //文字列に小数部が存在する場合
                    if(!(pointValueStr.indexOf(STRING_DECIMAL) == -1)) {
                        //小数点を取り除いた文字列を生成
                        pointValueStr = pointValueStr.replace(STRING_DECIMAL, "");

                        //小数点を取り除いた文字列が４桁以下の場合
                        if (pointValueStr.length() <= 4) {
                            //４桁満たない場合は０埋め
                            pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_4, Integer.parseInt(pointValueStr));
                        }
                        //小数点を取り除いた文字列が5桁以上の場合
                        else {
                            pointValueStr = POINT_TYPE_P_MAX_VALUE;     //ポイント種別「P」の最大値
                        }
                    }

                    //文字列が３桁以下の場合 → MAXが999.9xxになるため４桁以上は9999に置き換える
                    else if(pointValueStr.length() <= 3) {

                        //末尾に０を追加
                        pointValueStr = pointValueStr.concat("0");

                        //４桁になるように０埋めする
                        pointValueStr = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_4, Integer.parseInt(pointValueStr));
                    }
                    else {
                        //小数部が存在しない４桁以上の文字列
                        pointValueStr = POINT_TYPE_P_MAX_VALUE;     //ポイント種別「P」の最大値
                    }
                }
            }
        }
        //状態監視（K）の場合 → スペース4つ
        else if(pointType.equals(ApiGenericTypeConstants.POINT_TYPE.CONDITION_MONITORING.getVal())) {
            pointValueStr = POINT_NULL_VALUE;
        }
        //なし（0）の場合 → スペース4つ
        else if(pointType.equals(ApiGenericTypeConstants.POINT_TYPE.NONE.getVal())) {
            pointValueStr = POINT_NULL_VALUE;
        }
        //予期しない値
        else {
            pointValueStr = POINT_NULL_VALUE;
        }
        return pointValueStr;
    }

    /**
     * 機器制御処理
     *
     * @param fvpReqList
     * @return 実行結果
     * @throws Exception
     *
     */
    private FvpCtrlMngResponse<BaseParam> executeFvp(FvpCtrlMngRequest<BaseParam> fvpReq) throws Exception {
        return fvpCtrlMngClient.excute(fvpReq);
    }


    @Override
    public AielMasterDailyReportDataSendApiParameter getParameter() {
        return this.parameter;
    }

    @Override
    public void setParameter(AielMasterDailyReportDataSendApiParameter parameter) {
        this.parameter = parameter;

    }
}
