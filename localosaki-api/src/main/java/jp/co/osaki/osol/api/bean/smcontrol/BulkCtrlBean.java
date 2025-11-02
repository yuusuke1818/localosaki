package jp.co.osaki.osol.api.bean.smcontrol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkCtrlDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkCtrlParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.BulkApiResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.BulkAPIMailSendCallUtility;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200004Param;
import jp.co.osaki.osol.mng.param.A200005Param;
import jp.co.osaki.osol.mng.param.A200006Param;
import jp.co.osaki.osol.mng.param.A200007Param;
import jp.co.osaki.osol.mng.param.A200041Param;
import jp.co.osaki.osol.mng.param.A200042Param;
import jp.co.osaki.osol.mng.param.A200049Param;
import jp.co.osaki.osol.mng.param.A200050Param;
import jp.co.osaki.osol.mng.param.A200059Param;
import jp.co.osaki.osol.mng.param.A200060Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 複数建物・テナント一括 制御 Bean クラス
 *
 * @author da_yamano
 *
 */
@Named(value = SmControlConstants.BULK_CTRL)
@RequestScoped
public class BulkCtrlBean extends OsolApiBean<BulkCtrlParameter>
            implements BaseApiBean<BulkCtrlParameter,SmControlApiResponse<BaseSmControlApiResult>> {

    private BulkCtrlParameter parameter = new BulkCtrlParameter();

    private SmControlApiResponse<BaseSmControlApiResult> response = new SmControlApiResponse<>();

    @Inject
    private FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

    @Inject
    private BulkAPIMailSendCallUtility bulkAPIMailSendCallUtility;

    @EJB
    private BulkCtrlDao dao;

    private Long loginUserId = null;

    private String loginCorpId = null;

    private String loginPersonId = null;

    private List<Boolean> updateDBflgListTargetPower = new ArrayList<>();	// 目標電力設定 DBフラグリスト
    private List<A200007Param> newDataListTargetPower = new ArrayList<>();	// 目標電力設定 設定情報
    private List<A200007Param> oldDataListTargetPower = new ArrayList<>();	// 目標電力設定 設定前情報
    private List<A200050Param> newDataListTargetPowerEa = new ArrayList<>();	// 目標電力設定(Eα) 設定情報
    private List<A200050Param> oldDataListTargetPowerEa = new ArrayList<>();	// 目標電力設定(Eα) 設定前情報

    private List<Boolean> updateDBflgListSchedule = new ArrayList<>();		// スケジュール設定 DBフラグリスト
    private List<A200005Param> newDataListSchedule = new ArrayList<>();	// スケジュール設定 設定情報
    private List<A200005Param> oldDataListSchedule = new ArrayList<>();	// スケジュール設定 設定前情報
    private List<A200060Param> newDataListScheduleEa = new ArrayList<>();	// スケジュール設定(Eα) 設定情報
    private List<A200060Param> oldDataListScheduleEa = new ArrayList<>();	// スケジュール設定(Eα) 設定前情報

    private List<A200042Param> newDataListTemperature = new ArrayList<>();	// 温度制御設定 設定情報
    private List<A200042Param> oldDataListTemperature = new ArrayList<>();	// 温度制御設定 設定前情報
    private String SETTING_CONDITON = "";									// 温度制御設定 冷暖房判定
    private String COMMAND_CHECK = "";										// 温度制御設定 コマンド判定

    private List<FvpCtrlMngResponse<BaseParam>> fvpResTempSelectHistList = new ArrayList<>(); // 温度系の履歴取得結果


    @Override
    public BulkCtrlParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(BulkCtrlParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SmControlApiResponse<BaseSmControlApiResult> execute() throws Exception {
        BulkCtrlParameter param = new BulkCtrlParameter();

        copyOsolApiParameter(this.parameter, param);
        param.setTargetPowerList(this.parameter.getTargetPowerList());
        param.setTempControlList(this.parameter.getTempControlList());
        param.setCoolingList(this.parameter.getCoolingList());
        param.setHeatingList(this.parameter.getHeatingList());
        param.setScheduleList(this.parameter.getScheduleList());

        // 一括制御
        response = executeBulkControl(param);

        return response;
    }

    /**
     * 複数建物・テナント一括 制御 メイン
     *
     * @param param
     * @return
     */
    private SmControlApiResponse<BaseSmControlApiResult> executeBulkControl(BulkCtrlParameter param) {
        SmControlApiResponse<BaseSmControlApiResult> execRes = new SmControlApiResponse<>();
        String execResTargetPower = OsolApiResultCode.API_OK;
        String execResTempControl = OsolApiResultCode.API_OK;
        String execResCooling = OsolApiResultCode.API_OK;
        String execResHeating = OsolApiResultCode.API_OK;
        String execResSchedule = OsolApiResultCode.API_OK;
        List<BulkCtrlResultData> resultDataBulkTargetPowerList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkTempControlList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkCoolingList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkHeatingList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkTempHistList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkScheduleList = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkTargetPowerListEa = new ArrayList<>();
        List<BulkCtrlResultData> resultDataBulkScheduleListEa = new ArrayList<>();
        List<Map<String,String>> tempHistParamList = new ArrayList<>();

        // loginCorpId 取得
        this.loginCorpId = param.getLoginCorpId();

        // loginPersonId 取得
        this.loginPersonId = param.getLoginPersonId();

        // userID 取得
        MPerson mPerson = getPerson(param);
        if (mPerson == null) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" corpId=%s,personId=%s", this.loginCorpId, this.loginCorpId));
            execRes.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return execRes;
        }
        this.loginUserId = mPerson.getUserId();


        //===========================================================
        // 機器制御
        //===========================================================

        // 目標電力設定
        if (!CheckUtility.isNullOrEmpty(param.getTargetPowerList())) {
            List<Map<String,String>> targetPowerParamList = getParameterListFromJson(param.getTargetPowerList(), resultDataBulkTargetPowerList, resultDataBulkTargetPowerListEa);
            List<FvpCtrlMngResponse<BaseParam>> fvpResList = new ArrayList<>();
            List<FvpCtrlMngResponse<BaseParam>> fvpResListEa = new ArrayList<>();
            List<SmControlVerocityResult> sendMailTargetList = new ArrayList<>();

            // ParamListを Ea/Ea2用 と その他の機種用 に分ける
            List<Map<String,String>> paramList = new ArrayList<>();      // Ea/Ea2以外用
            List<Map<String,String>> paramListEa = new ArrayList<>();    // Ea/Ea2用
            splitParamListBySmEa(targetPowerParamList, paramList, paramListEa);

            try {
                //-----------------------------------------------------------
                // Ea / Ea2以外
                //-----------------------------------------------------------
                if (!paramList.isEmpty()) {
                    fvpResList = executeBulkTargetPowerUpdate(paramList, resultDataBulkTargetPowerList);
                    // DB更新
                    updateDbBulkTargetPower(fvpResList);
                    // メール送信リストを生成
                    sendMailTargetList.addAll(createSendMailTargetList(fvpResList, resultDataBulkTargetPowerList, SmControlConstants.DEMAND_COMMAND));
                }

                //-----------------------------------------------------------
                // Ea / Ea2
                //-----------------------------------------------------------
                if (!paramListEa.isEmpty()) {
                    fvpResListEa = executeBulkTargetPowerUpdateEa(paramListEa, resultDataBulkTargetPowerListEa);
                    // DB更新
                    updateDbBulkTargetPowerEa(fvpResListEa);
                    // メール送信リストを生成
                    sendMailTargetList.addAll(createSendMailTargetList(fvpResListEa, resultDataBulkTargetPowerListEa, SmControlConstants.DEMAND_COMMAND_E_ALPHA));
                }

                // メールAPI呼出処理
                sendMailBulk(sendMailTargetList, SmControlConstants.BULK_TARGET_POWER_COMMAND);
                // 実行結果取得
                execResTargetPower = isExecuteResult(fvpResList, resultDataBulkTargetPowerList);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                execResTargetPower = e.getErrorCode();
            }
        }

        try {
            // 温度系の履歴
            if (!CheckUtility.isNullOrEmpty(param.getTempControlList())) {
                // 温度制御設定リストからパラメータ取得
                tempHistParamList = getParameterListFromJson(param.getTempControlList(), resultDataBulkTempHistList);
                fvpResTempSelectHistList = executeBulkTemperatureCtrlSelectHistList(tempHistParamList, resultDataBulkTempHistList, SmControlConstants.TEMP_CONTROL);
            } else if (!CheckUtility.isNullOrEmpty(param.getCoolingList())) {
                // 冷房温度設定リストからパラメータ取得
                tempHistParamList = getParameterListFromJson(param.getCoolingList(), resultDataBulkTempHistList);
                fvpResTempSelectHistList = executeBulkTemperatureCtrlSelectHistList(tempHistParamList, resultDataBulkTempHistList, SmControlConstants.COOLER);
            } else if (!CheckUtility.isNullOrEmpty(param.getHeatingList())) {
                // 暖房温度設定リストからパラメータ取得
                tempHistParamList = getParameterListFromJson(param.getHeatingList(), resultDataBulkTempHistList);
                fvpResTempSelectHistList = executeBulkTemperatureCtrlSelectHistList(tempHistParamList, resultDataBulkTempHistList, SmControlConstants.HEATER);
            }
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            execResCooling = e.getErrorCode();
        }


        // 温度制御設定
        if (!CheckUtility.isNullOrEmpty(param.getTempControlList())) {
            List<Map<String,String>> tempControlParamList = getParameterListFromJson(param.getTempControlList(), resultDataBulkTempControlList);
            List<FvpCtrlMngResponse<BaseParam>> fvpResList = new ArrayList<>();
            List<SmControlVerocityResult> sendMailTargetList = new ArrayList<>();

            try {
                fvpResList = executeBulkTemperatureCtrlUpdate(tempControlParamList, resultDataBulkTempControlList, SmControlConstants.TEMP_CONTROL);
                // メール送信リストを生成
                sendMailTargetList.addAll(createSendMailTargetList(fvpResList, resultDataBulkTempControlList, SmControlConstants.BULK_TEMPERATURE_COMMAND));
                // メールAPI呼出処理
                sendMailBulk(sendMailTargetList, SmControlConstants.BULK_TEMPERATURE_COMMAND);
                // 実行結果取得
                execResCooling = isExecuteResult(fvpResList, resultDataBulkTempControlList);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                execResCooling = e.getErrorCode();
            }
        }

        // 冷房温度設定
        if (!CheckUtility.isNullOrEmpty(param.getCoolingList())) {
            List<Map<String,String>> coolingParamList = getParameterListFromJson(param.getCoolingList(), resultDataBulkCoolingList);
            List<FvpCtrlMngResponse<BaseParam>> fvpResList = new ArrayList<>();
            List<SmControlVerocityResult> sendMailTargetList = new ArrayList<>();

            try {
                fvpResList = executeBulkTemperatureCtrlUpdate(coolingParamList, resultDataBulkCoolingList, SmControlConstants.COOLER);
                // メール送信リストを生成
                sendMailTargetList.addAll(createSendMailTargetList(fvpResList, resultDataBulkCoolingList, SmControlConstants.BULK_TEMPERATURE_COMMAND));
                // メールAPI呼出処理
                sendMailBulk(sendMailTargetList, SmControlConstants.BULK_TEMPERATURE_COMMAND);
                // 実行結果取得
                execResCooling = isExecuteResult(fvpResList, resultDataBulkCoolingList);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                execResCooling = e.getErrorCode();
            }
        }

        // 暖房温度設定
        if (!CheckUtility.isNullOrEmpty(param.getHeatingList())) {
            List<Map<String,String>> heatingParamList = getParameterListFromJson(param.getHeatingList(), resultDataBulkHeatingList);
            List<FvpCtrlMngResponse<BaseParam>> fvpResList = new ArrayList<>();
            List<SmControlVerocityResult> sendMailTargetList = new ArrayList<>();

            try {
                fvpResList = executeBulkTemperatureCtrlUpdate(heatingParamList, resultDataBulkHeatingList, SmControlConstants.HEATER);
                // メール送信リストを生成
                sendMailTargetList.addAll(createSendMailTargetList(fvpResList, resultDataBulkHeatingList, SmControlConstants.BULK_TEMPERATURE_COMMAND));
                // メールAPI呼出処理
                sendMailBulk(sendMailTargetList, SmControlConstants.BULK_TEMPERATURE_COMMAND);
                // 実行結果取得
                execResHeating = isExecuteResult(fvpResList, resultDataBulkHeatingList);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                execResHeating = e.getErrorCode();
            }
        }

        // スケジュール設定
        if (!CheckUtility.isNullOrEmpty(param.getScheduleList())) {
            List<Map<String,String>> scheduleParamList = getParameterListFromJson(param.getScheduleList(), resultDataBulkScheduleList, resultDataBulkScheduleListEa);
            List<FvpCtrlMngResponse<BaseParam>> fvpResList = new ArrayList<>();
            List<FvpCtrlMngResponse<BaseParam>> fvpResListEa = new ArrayList<>();
            List<SmControlVerocityResult> sendMailTargetList = new ArrayList<>();

            // ParamListを Ea/Ea2用 と その他の機種用 に分ける
            List<Map<String,String>> paramList = new ArrayList<>();      // Ea/Ea2以外用
            List<Map<String,String>> paramListEa = new ArrayList<>();    // Ea/Ea2用
            splitParamListBySmEa(scheduleParamList, paramList, paramListEa);

            try {
                //-----------------------------------------------------------
                // Ea / Ea2以外
                //-----------------------------------------------------------
                if (!paramList.isEmpty()) {
                    fvpResList = executeBulkScheduleUpdate(paramList, resultDataBulkScheduleList);
                    // DB更新
                    updateDbBulkSchedule(fvpResList);
                    // メール送信リストを生成
                    sendMailTargetList.addAll(createSendMailTargetList(fvpResList, resultDataBulkScheduleList, SmControlConstants.SCHEDULE_COMMAND));
                }

                //-----------------------------------------------------------
                // Ea / Ea2
                //-----------------------------------------------------------
                if (!paramListEa.isEmpty()) {
                    fvpResListEa = executeBulkScheduleUpdateEa(paramListEa, resultDataBulkScheduleListEa);
                    // DB更新
                    updateDbBulkScheduleEa(fvpResListEa);
                    // メール送信リストを生成
                    sendMailTargetList.addAll(createSendMailTargetList(fvpResListEa, resultDataBulkScheduleListEa, SmControlConstants.SCHEDULE_UPDATE_COMMAND_E_ALPHA));
                }

                // メールAPI呼出処理
                sendMailBulk(sendMailTargetList, SmControlConstants.BULK_SCHEDULE_COMMAND);
                // 実行結果取得
                execResSchedule = isExecuteResult(fvpResList, resultDataBulkScheduleList);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                execResSchedule = e.getErrorCode();
            }
        }

        if (!OsolApiResultCode.API_OK.equals(execResTargetPower)) {
            execRes.setResultCode(execResTargetPower);
        } else if (!OsolApiResultCode.API_OK.equals(execResTempControl)) {
            execRes.setResultCode(execResTempControl);
        } else if (!OsolApiResultCode.API_OK.equals(execResCooling)) {
            execRes.setResultCode(execResCooling);
        } else if (!OsolApiResultCode.API_OK.equals(execResHeating)) {
            execRes.setResultCode(execResHeating);
        } else if (!OsolApiResultCode.API_OK.equals(execResSchedule)) {
            execRes.setResultCode(execResSchedule);
        } else {
            execRes.setResultCode(OsolApiResultCode.API_OK);
        }

        return execRes;
    }


    //----------------------------------------------------------------------------------------------------------------
    //
    // 複数建物・テナント一括 制御機能 個別処理
    //
    //----------------------------------------------------------------------------------------------------------------

    /**
     * 複数建物・テナント一括 目標電力(設定) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerUpdate(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        List<BulkCtrlResultData> resultDataBulkTargetPowerSelectList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkTargetPowerUpdateList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkTargetPowerSelectHistList;
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResUpdateList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectHistList = new ArrayList<>();
        List<A200007Param> oldParamList = new ArrayList<>();
        List<A200007Param> newParamList = new ArrayList<>();

        // 複数建物・テナント一括 目標電力(取得)を実行
        try {
            fvpResSelectList = executeBulkTargetPowerSelect(parameterList, resultDataBulkTargetPowerSelectList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        resultDataBulkTargetPowerSelectHistList = new ArrayList<>(resultDataBulkTargetPowerSelectList);
        // 履歴取得
        try {
            fvpResSelectHistList = executeBulkTargetPowerSelectHist(parameterList, resultDataBulkTargetPowerSelectHistList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        // Paramリストを生成
        try {
            newParamList = createParamListForBulkTargetPowerUpdate(parameterList, fvpResSelectList, fvpResSelectHistList, oldParamList);
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(newParamList, oldParamList, resultDataBulkTargetPowerUpdateList);

        // 機器情報を取得
        getSmInfo(resultDataBulkTargetPowerUpdateList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataBulkTargetPowerUpdateList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerUpdate(resultDataBulkTargetPowerUpdateList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataBulkTargetPowerUpdateList);

        // 機器制御を実行し、結果を返却
        fvpResUpdateList = executeBulkFvpCtrl(fvpReqList);

        // 設定情報保持
        this.newDataListTargetPower.addAll(newParamList);
        // 設定前情報保持
        this.oldDataListTargetPower.addAll(oldParamList);

        return fvpResUpdateList;
    }

    /**
     * 複数建物・テナント一括 目標電力(設定)用 Paramリスト生成
     *   複数建物・テナント一括 目標電力(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     *
     */
    private List<A200007Param> createParamListForBulkTargetPowerUpdate(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList,
            List<FvpCtrlMngResponse<BaseParam>> fvpResHistList, List<A200007Param> oldParamList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<A200007Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200007Param newParam = new A200007Param();
            A200007Param oldParam = new A200007Param();
            String targetPower = null;

            // 履歴取得している場合、設定する
            FvpCtrlMngResponse<BaseParam> fvpResHist = null;

            // 履歴以外で差分の場合、設定する
            String targetPowerDiff = null;
            String targetPowerDiffSign = null;

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            // 履歴取得していた場合
            for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResHistList) {
                if (smId.equals(fvpRes.getSmId())) {
                    fvpResHist = fvpRes;
                    break;
                }
            }

            // 履歴取得していない場合
            if (fvpResHist == null) {
                // 入力値から取得
                if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER) == null) {
                    // 差分設定
                    targetPowerDiff = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_DIFF);
                    targetPowerDiffSign = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_DIFF_SIGN);
                    if (!CheckUtility.isNullOrEmpty(targetPowerDiff)) {
                        targetPowerDiff = targetPowerDiff.replace("\"", "");
                    }

                    if (!CheckUtility.isNullOrEmpty(targetPowerDiffSign)) {
                        targetPowerDiffSign = targetPowerDiffSign.replace("\"", "");
                    }
                } else {
                    // 直接入力
                    targetPower = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER);
                    if (CheckUtility.isNullOrEmpty(targetPower)) {
                        targetPower = "0000";
                    } else {
                        targetPower = targetPower.replace("\"", "");
                    }
                }
            }


            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200006Param res = (A200006Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        //   BeanUtils.copyPropetriesだと正常に複製されない為、一度、JSONに変換し、
                        //   JSON変換したものを基に、設定データを作成する

                        // BeanからMapへ変換
                        @SuppressWarnings("unchecked")
                        Map<String, Object> mapParam = BeanUtils.describe(res);

                        // 不要な情報を削除
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_SMADDR);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_CLASS);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_ADDR);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_COMMANDCD);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_COMMAND);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_SETTING_DATE);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_PRODUCTCD);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_MIN_LOAD_BLOCK_TIME_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TEMP_LOAD_INFO_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_LOAD_BLOCK_CAP_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TEMP_MONTH_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TEMP_MAX_DEMAND_MONTH_LIST);

                        // 取得した負荷情報リスト, 月リストを設定用に変換
                        List<Map<String,String>> curLoadInfoList = res.getLoadInfoList();
                        List<Map<String,String>> curMonthList = res.getMonthList();
                        mapParam.put(SmControlConstants.BULK_CTRL_PARAM_LOAD_INFO_LIST, curLoadInfoList);
                        mapParam.put(SmControlConstants.BULK_CTRL_PARAM_MONTH_LIST, curMonthList);

                        // MapからJsonへ変換
                        Gson gson = new Gson();
                        String jsonParam = gson.toJson(mapParam);

                        // 更新データを設定
                        oldParam = gson.fromJson(jsonParam.toString(), A200007Param.class);
                        newParam = gson.fromJson(jsonParam.toString(), A200007Param.class);

                        // 履歴設定
                        if (fvpResHist != null) {
                            // 履歴が正常に取得できている場合
                            if (OsolApiResultCode.API_OK.equals(fvpResHist.getFvpResultCd())) {
                                A200006Param resHist = (A200006Param) fvpResHist.getParam();
                                // 履歴取得した目標電力を設定
                                targetPower = resHist.getTargetPower();
                                newParam.setTargetPower(targetPower);
                                newParam.setUpdateDBflg(true);
                            }
                        } else {
                            // 差分値チェック
                            if (CheckUtility.isNullOrEmpty(targetPower)) {
                                targetPower = calcTargetPowerDiff(newParam.getTargetPower(), targetPowerDiff, targetPowerDiffSign);
                            }
                            newParam.setTargetPower(targetPower);
                            newParam.setUpdateDBflg(true);
                        }
                    }
                    break;
                }
            }

            oldParamList.add(oldParam);
            retList.add(newParam);

            // DBフラグを保持
            this.updateDBflgListTargetPower.add(newParam.isUpdateDBflg());
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 目標電力(設定)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTargetPowerUpdate(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // 対応機器チェック
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }

                // listサイズチェック
                List<Map<String, String>> loadInfoList = ((A200007Param) resultData.getParam()).getLoadInfoList();
                if ((SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_FV2)) ||
                    (SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_FVPD)) ||
                    (SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_D)) ||
                    (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_G2))
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("LoadInfoList=%d", loadInfoList.size()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerSelect(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200006Param> paramList = createParamListForBulkTargetPowerSelect(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerSelect(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataList);

        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 目標電力(取得) 実行 履歴取得
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerSelectHist(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200006Param> paramList = createParamListForBulkTargetPowerSelectHist(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerSelect(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqListHist(resultDataList, SmControlConstants.BULK_CTRL_CAST_TARGET_POWER_SELECT);

        // 履歴取得が無ければ空のリストを返却
        if (fvpReqList.isEmpty()) {
            return new ArrayList<>();
        }
        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 Paramリスト生成
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200006Param> createParamListForBulkTargetPowerSelect(List<Map<String,String>> parameterList) {
        List<A200006Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200006Param param = new A200006Param();

            // 最新履歴
            String settingChangeHist = SmControlConstants.SETTING_CHG_HIST_LATEST;
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            param.setSettingChangeHist(settingChangeHist);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 Paramリスト生成
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200006Param> createParamListForBulkTargetPowerSelectHist(List<Map<String,String>> parameterList) {
        List<A200006Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200006Param param = new A200006Param();

            String targetPowerHistory = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_HIST);
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            if (!CheckUtility.isNullOrEmpty(targetPowerHistory)) {
                targetPowerHistory = targetPowerHistory.replace("\"", "");
            }

            param.setSettingChangeHist(targetPowerHistory);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTargetPowerSelect(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 目標電力(設定) Eα 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerUpdateEa(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        List<BulkCtrlResultData> resultDataBulkTargetPowerSelectList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkTargetPowerUpdateList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkTargetPowerSelectHistList;
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResUpdateList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectHistList = new ArrayList<>();
        List<A200050Param> oldParamList = new ArrayList<>();
        List<A200050Param> newParamList = new ArrayList<>();

        // 複数建物・テナント一括 目標電力(取得)を実行
        try {
            fvpResSelectList = executeBulkTargetPowerSelectEa(parameterList, resultDataBulkTargetPowerSelectList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        resultDataBulkTargetPowerSelectHistList = new ArrayList<>(resultDataBulkTargetPowerSelectList);
        // 履歴取得
        try {
            fvpResSelectHistList = executeBulkTargetPowerSelectEaHist(parameterList, resultDataBulkTargetPowerSelectHistList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        // Paramリストを生成
        try {
            newParamList = createParamListForBulkTargetPowerUpdateEa(parameterList, fvpResSelectList, fvpResSelectHistList, oldParamList);
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(newParamList, oldParamList, resultDataBulkTargetPowerUpdateList);

        // 機器情報を取得
        getSmInfo(resultDataBulkTargetPowerUpdateList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataBulkTargetPowerUpdateList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerUpdateEa(resultDataBulkTargetPowerUpdateList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataBulkTargetPowerUpdateList);

        // 機器制御を実行し、結果を返却
        fvpResUpdateList = executeBulkFvpCtrl(fvpReqList);

        // 設定情報保持
        this.newDataListTargetPowerEa.addAll(newParamList);
        // 設定前情報保持
        this.oldDataListTargetPowerEa.addAll(oldParamList);

        return fvpResUpdateList;
    }

    /**
     * 複数建物・テナント一括 目標電力(設定) Eα 用 Paramリスト生成
     *   複数建物・テナント一括 目標電力(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     *
     */
    private List<A200050Param> createParamListForBulkTargetPowerUpdateEa(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList,
            List<FvpCtrlMngResponse<BaseParam>> fvpResHistList, List<A200050Param> oldParamList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<A200050Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200050Param newParam = new A200050Param();
            A200050Param oldParam = new A200050Param();
            String targetPower = null;

            // 履歴取得している場合、設定する
            FvpCtrlMngResponse<BaseParam> fvpResHist = null;

            // 履歴以外で差分の場合、設定する
            String targetPowerDiff = null;
            String targetPowerDiffSign = null;

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            // 履歴取得していた場合
            for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResHistList) {
                if (smId.equals(fvpRes.getSmId())) {
                    fvpResHist = fvpRes;
                    break;
                }
            }

            // 履歴取得していない場合
            if (CheckUtility.isNullOrEmpty(targetPower)) {
                // 入力値から取得
                if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER) == null) {
                    // 差分設定
                    targetPowerDiff = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_DIFF);
                    targetPowerDiffSign = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_DIFF_SIGN);
                    if (!CheckUtility.isNullOrEmpty(targetPowerDiff)) {
                        targetPowerDiff = targetPowerDiff.replace("\"", "");
                    }

                    if (!CheckUtility.isNullOrEmpty(targetPowerDiffSign)) {
                        targetPowerDiffSign = targetPowerDiffSign.replace("\"", "");
                    }
                } else {
                    // 直接入力
                    targetPower = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER);
                    if (CheckUtility.isNullOrEmpty(targetPower)) {
                        targetPower = "0000";
                    } else {
                        targetPower = targetPower.replace("\"", "");
                    }
                }
            }

            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200049Param res = (A200049Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        //   BeanUtils.copyPropetriesだと正常に複製されない為、一度、JSONに変換し、
                        //   JSON変換したものを基に、設定データを作成する

                        // BeanからMapへ変換
                        @SuppressWarnings("unchecked")
                        Map<String, Object> mapParam = BeanUtils.describe(res);

                        // 不要な情報を削除
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_SMADDR);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_CLASS);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_ADDR);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_COMMANDCD);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_COMMAND);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_SETTING_DATE);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_PRODUCTCD);
                        mapParam.remove("_" + SmControlConstants.BULK_CTRL_PARAM_MIN_LOAD_BLOCK_TIME_LIST);
                        mapParam.remove("_" + SmControlConstants.BULK_CTRL_PARAM_LOAD_BLOCK_METHOD_LIST);
                        mapParam.remove("_" + SmControlConstants.BULK_CTRL_PARAM_LOAD_BLOCK_RANK_LIST);
                        mapParam.remove("_" + SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_REF_POINT_LIST);
                        mapParam.remove("_" + SmControlConstants.BULK_CTRL_PARAM_LOAD_BLOCK_CAP_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TEMP_MAX_DEMAND_MONTH_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TEMP_TARGET_POWER_MONTH_LIST);
                        mapParam.remove(SmControlConstants.BULK_CTRL_PARAM_TIME_LIST);

                        // 取得した負荷情報リスト, 月リストを設定用に変換
                        List<Map<String,String>> curLoadInfoList = res.getLoadInfoList();
                        List<Map<String,String>> curMonthList = res.getMonthList();
                        List<Map<String,String>> curTimeList = res.getTimeList();
                        mapParam.put(SmControlConstants.BULK_CTRL_PARAM_LOAD_INFO_LIST, curLoadInfoList);
                        mapParam.put(SmControlConstants.BULK_CTRL_PARAM_MONTH_LIST, curMonthList);
                        mapParam.put(SmControlConstants.BULK_CTRL_PARAM_TIME_LIST, curTimeList);

                        // MapからJsonへ変換
                        Gson gson = new Gson();
                        String jsonParam = gson.toJson(mapParam);

                        // 更新データを設定
                        oldParam = gson.fromJson(jsonParam.toString(), A200050Param.class);
                        newParam = gson.fromJson(jsonParam.toString(), A200050Param.class);

                        // 履歴設定
                        if (fvpResHist != null) {
                            // 履歴が正常に取得できている場合
                            if (OsolApiResultCode.API_OK.equals(fvpResHist.getFvpResultCd())) {
                                A200049Param resHist = (A200049Param) fvpResHist.getParam();
                                // 履歴取得した目標電力を設定
                                targetPower = resHist.getTargetPower();
                                newParam.setTargetPower(targetPower);
                                newParam.setUpdateDBflg(true);
                            }
                        } else {
                            // 差分値チェック
                            if (CheckUtility.isNullOrEmpty(targetPower)) {
                                targetPower = calcTargetPowerDiff(newParam.getTargetPower(), targetPowerDiff, targetPowerDiffSign);
                            }
                            newParam.setTargetPower(targetPower);
                            newParam.setUpdateDBflg(true);
                        }
                    }
                    break;
                }
            }

            oldParamList.add(oldParam);
            retList.add(newParam);

            // DBフラグを保持
            this.updateDBflgListTargetPower.add(newParam.isUpdateDBflg());
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 目標電力(設定)用 Eα バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTargetPowerUpdateEa(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // 対応機器チェック
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }

                // listサイズチェック
                List<Map<String, String>> loadInfoList = ((A200050Param) resultData.getParam()).getLoadInfoList();
                if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_E_ALPHA)) ||
                    (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd()) && !(loadInfoList.size() == SmControlConstants.DEMAND_LOAD_LIST_E_ALPHA_2))
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("LoadInfoList=%d", loadInfoList.size()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得) Eα 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerSelectEa(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200049Param> paramList = createParamListForBulkTargetPowerSelectEa(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerSelectEa(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataList);

        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 目標電力(取得) Eα 実行（履歴）
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTargetPowerSelectEaHist(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200049Param> paramList = createParamListForBulkTargetPowerSelectEaHist(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 固有のバリデーションを実行
        validationBulkTargetPowerSelectEa(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqListHist(resultDataList, SmControlConstants.BULK_CTRL_CAST_TARGET_POWER_SELECT_EA);

        // 履歴取得が無ければ空のリストを返却
        if (fvpReqList.isEmpty()) {
            return new ArrayList<>();
        }
        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 Eα Paramリスト生成
     *
     * @param parameterList
     * @param histFlg   履歴フラグ
     * @return Paramリスト
     *
     */
    private List<A200049Param> createParamListForBulkTargetPowerSelectEa(List<Map<String,String>> parameterList) {
        List<A200049Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200049Param param = new A200049Param();

            // 最新履歴
            String settingChangeHist = SmControlConstants.SETTING_CHG_HIST_LATEST;
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            param.setSettingChangeHist(settingChangeHist);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 Eα Paramリスト生成
     *
     * @param parameterList
     * @param histFlg   履歴フラグ
     * @return Paramリスト
     *
     */
    private List<A200049Param> createParamListForBulkTargetPowerSelectEaHist(List<Map<String,String>> parameterList) {
        List<A200049Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200049Param param = new A200049Param();

            String settingChangeHist = null;
            settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TARGET_POWER_HIST);

            // パラメータから履歴情報が取得できない場合は、最新履歴を設定
            if (!CheckUtility.isNullOrEmpty(settingChangeHist)) {
                settingChangeHist = settingChangeHist.replace("\"", "");
            }
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            param.setSettingChangeHist(settingChangeHist);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 目標電力(取得)用 Eα バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTargetPowerSelectEa(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 目標電力用 DB更新
     *
     * @param fvpResList
     * @return
     *
     */
    private void updateDbBulkTargetPower(List<FvpCtrlMngResponse<BaseParam>> fvpResList){

        for (int i = 0; i < fvpResList.size(); i++) {

            // DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
            if(!this.updateDBflgListTargetPower.get(i)
                    || !(fvpResList.get(i).getFvpResultCd().equals(OsolApiResultCode.API_OK))){
                continue;
            }

            FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
            A200007Param res = (A200007Param) fvpRes.getParam();

            // 登録用ResultSet
            BuildingDmResult param = new BuildingDmResult();

            param.setSmId(fvpRes.getSmId());

            BigDecimal targetPower = new BigDecimal((String) res.getTargetPower());

            param.setTargetPower(targetPower);
            param.setUpdateUserId(this.loginUserId);

            // dao呼出
            try {
                dao.updateBuildingDM(param);
            } catch (Exception e) {
                // 更新エラー時はfvpResListの結果コードに例外を格納
                fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
        }
    }

    /**
     * 複数建物・テナント一括 目標電力用 DB更新 (Eα用)
     *
     * @param fvpResList
     */
    private void updateDbBulkTargetPowerEa(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {

        for (int i = 0; i < fvpResList.size(); i++) {
            // DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
            if (!this.updateDBflgListTargetPower.get(i)
                    || !(fvpResList.get(i).getFvpResultCd().equals(OsolApiResultCode.API_OK))){
                continue;
            }

            FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
            A200050Param res = (A200050Param) fvpRes.getParam();

            // 登録用ResultSet
            BuildingDmResult param = new BuildingDmResult();

            param.setSmId(fvpRes.getSmId());

            BigDecimal targetPower = new BigDecimal((String) res.getTargetPower());

            param.setTargetPower(targetPower);
            param.setUpdateUserId(this.loginUserId);

            // dao呼出
            try {
                dao.updateBuildingDM(param);
            } catch (Exception e) {
                // 更新エラー時はfvpResListの結果コードに例外を格納
                fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
        }
    }

    /**
     * 複数建物・テナント一括 目標電力用 メール内容設定
     *
     * @param fvpResList
     * @param resultDataList
     * @return targetList
     *
     */
    private List<SmControlVerocityResult> mailBodySettingTargetPower(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        // 検索用リスト生成
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        for (int i = 0; i < fvpResList.size(); i++) {
            List<String> buildingIdList = resultDataList.get(i).getBuildingIdList();
            for (String buildingId : buildingIdList) {
                // メールAPI用 Resultクラス
                SmControlVerocityResult target = new SmControlVerocityResult();

                // 設定情報レコード取得
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);

                // 機器IDが未存在でエラーの場合、処理しない
                if(fvpRes.getSmId() == null ) {
                    continue;
                }

                // リクエスト新設定情報レコード取得
                A200007Param newRecord = this.newDataListTargetPower.get(i);
                // リクエスト旧設定情報レコード取得
                A200007Param oldRecord = this.oldDataListTargetPower.get(i);

                // 新旧設定情報のどちらかでも存在しない場合スキップ
                if (oldRecord == null || newRecord == null) {
                    continue;
                }

                target.setSmId(fvpRes.getSmId());

                target.setCorpId(this.loginCorpId);
                target.setPersonId(String.valueOf(this.loginPersonId));
                target.setSmAddress(fvpRes.getSmAddress());
                target.setIpAddress(fvpRes.getIpAddress());
                target.setCommand(SmControlConstants.BULK_TARGET_POWER_COMMAND);
                target.setBuildingId(buildingId);
                // 設定前後情報格納
                if (oldRecord != null && newRecord != null) {
                    if(oldRecord.getTargetPower() == null || oldRecord.getTargetPower().isEmpty()) {
                        target.setOldData("－");
                    } else {
                        target.setOldData(oldRecord.getTargetPower());
                    }
                    if(newRecord.getTargetPower() == null || newRecord.getTargetPower().isEmpty()) {
                        target.setNewData("－");
                    } else {
                        target.setNewData(newRecord.getTargetPower());
                    }
                }

                // 新旧レコードチェック
                if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
                    targetList.add(target);
                    continue;
                }
                // 例外が発生している場合はRECORD_NG
                else if (fvpRes.getFvpResultCd() != OsolApiResultCode.API_OK) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NG);
                    targetList.add(target);
                    continue;
                }
                target.setResult(SmControlConstants.MAIL_SETTING_OK);

                targetList.add(target);
            }
        }
        return targetList;
    }

    /**
     * 複数建物・テナント一括 目標電力用 Eα メール内容設定
     *
     * @param fvpResList
     * @param resultDataList
     * @return targetList
     *
     */
    private List<SmControlVerocityResult> mailBodySettingTargetPowerEa(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        // 検索用リスト生成
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        for (int i = 0; i < fvpResList.size(); i++) {
            List<String> buildingIdList = resultDataList.get(i).getBuildingIdList();
            for (String buildingId : buildingIdList) {
                // メールAPI用 Resultクラス
                SmControlVerocityResult target = new SmControlVerocityResult();

                // 設定情報レコード取得
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);

                // 機器IDが未存在でエラーの場合、処理しない
                if(fvpRes.getSmId() == null ) {
                    continue;
                }

                // リクエスト新設定情報レコード取得
                A200050Param newRecord = this.newDataListTargetPowerEa.get(i);
                // リクエスト旧設定情報レコード取得
                A200050Param oldRecord = this.oldDataListTargetPowerEa.get(i);

                // 新旧設定情報のどちらかでも存在しない場合スキップ
                if (oldRecord == null || newRecord == null) {
                    continue;
                }

                target.setSmId(fvpRes.getSmId());

                target.setCorpId(this.loginCorpId);
                target.setPersonId(String.valueOf(this.loginPersonId));
                target.setSmAddress(fvpRes.getSmAddress());
                target.setIpAddress(fvpRes.getIpAddress());
                target.setCommand(SmControlConstants.BULK_TARGET_POWER_COMMAND);
                target.setBuildingId(buildingId);
                // 設定前後情報格納
                if (oldRecord != null && newRecord != null) {
                    if(oldRecord.getTargetPower() == null || oldRecord.getTargetPower().isEmpty()) {
                        target.setOldData("－");
                    } else {
                        target.setOldData(oldRecord.getTargetPower());
                    }
                    if(newRecord.getTargetPower() == null || newRecord.getTargetPower().isEmpty()) {
                        target.setNewData("－");
                    } else {
                        target.setNewData(newRecord.getTargetPower());
                    }
                }

                // 新旧レコードチェック
                if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
                    targetList.add(target);
                    continue;
                }
                // 例外が発生している場合はRECORD_NG
                else if (fvpRes.getFvpResultCd() != OsolApiResultCode.API_OK) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NG);
                    targetList.add(target);
                    continue;
                }
                target.setResult(SmControlConstants.MAIL_SETTING_OK);

                targetList.add(target);
            }
        }
        return targetList;
    }

    /**
     * 複数建物・テナント一括 温度制御(設定) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTemperatureCtrlUpdate(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList, String type) throws SmControlException {
        List<BulkCtrlResultData> resultDataTemperatureCtrlSelectList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataTemperatureCtrlUpdateList = new ArrayList<>(resultDataList);
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResUpdateList = new ArrayList<>();
        List<A200042Param> oldParamList = new ArrayList<>();
        List<A200042Param> newParamList = new ArrayList<>();

        // 複数建物・テナント一括 温度制御(取得)を実行
        try {
            fvpResSelectList = executeBulkTemperatureCtrlSelect(parameterList, resultDataTemperatureCtrlSelectList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        // Paramリストを生成
        try {
            if (SmControlConstants.TEMP_CONTROL.equals(type)) {
                // 温度制御設定
                newParamList = createParamListForBulkTemperatureFlgUpdate(parameterList, fvpResSelectList, fvpResTempSelectHistList, oldParamList, type);
            } else {
                // 冷房･暖房設定
                newParamList = createParamListForBulkTemperatureCtrlUpdate(parameterList, fvpResSelectList, fvpResTempSelectHistList, oldParamList, type);
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        // 生成したParamリストからバリデーションを実行
        if (!fvpResTempSelectHistList.isEmpty()) {
            // 履歴
            validationBulkCtrlHist(newParamList, oldParamList, resultDataTemperatureCtrlUpdateList);
        } else {
            // 履歴以外
            validationBulkCtrl(newParamList, oldParamList, resultDataTemperatureCtrlUpdateList);
        }

        // 機器情報を取得
        getSmInfo(resultDataTemperatureCtrlUpdateList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataTemperatureCtrlUpdateList);

        // 固有のバリデーションを実行
        validationBulkTemperatureCtrlUpdate(resultDataTemperatureCtrlUpdateList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataTemperatureCtrlUpdateList);

        // 機器制御を実行し、結果を返却
        fvpResUpdateList = executeBulkFvpCtrl(fvpReqList);

        // 設定情報が保持されていれば消去
        if(newDataListTemperature.size() != 0) {
            newDataListTemperature.clear();
        }
        // 設定情報保持
        this.newDataListTemperature.addAll(newParamList);

        // 設定前情報が保持されていれば消去
        if(oldDataListTemperature.size() != 0) {
            oldDataListTemperature.clear();
        }
        // 設定前情報保持
        this.oldDataListTemperature.addAll(oldParamList);
        // 温度制御･冷暖房判定保持
        this.SETTING_CONDITON = type;

        return fvpResUpdateList;
    }

    /**
     * 複数建物・テナント一括 温度制御(履歴取得) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTemperatureCtrlSelectHistList(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList, String type) throws SmControlException {
        List<BulkCtrlResultData> resultDataTemperatureCtrlSelectHistList = new ArrayList<>(resultDataList);
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectHistList = new ArrayList<>();

        // 複数建物・テナント一括 温度制御(取得)を実行
        try {
            fvpResSelectHistList = executeBulkTemperatureCtrlSelectHist(parameterList, resultDataTemperatureCtrlSelectHistList, type);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        return fvpResSelectHistList;
    }

    /**
     * 複数建物・テナント一括 温度制御(設定) 温度制御設定用 Paramリスト生成
     *   複数建物・テナント一括 温度制定(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *
     */
    private List<A200042Param> createParamListForBulkTemperatureFlgUpdate(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList,
            List<FvpCtrlMngResponse<BaseParam>> fvpResHistList, List<A200042Param> oldParamList, String type) throws IllegalAccessException, InvocationTargetException {
        List<A200042Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200042Param newParam = new A200042Param();
            A200042Param oldParam = new A200042Param();

            // 履歴取得している場合、設定する
            FvpCtrlMngResponse<BaseParam> fvpResHist = null;

            // 履歴以外の場合、設定する
            String temperatureFlg = null;

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            // 履歴取得していた場合
            for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResHistList) {
                if (smId.equals(fvpRes.getSmId())) {
                    fvpResHist = fvpRes;
                    break;
                }
            }

            // 履歴取得していない場合
            if (fvpResHist == null) {
                // 入力値から取得
                temperatureFlg = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_FLG);

                if (!CheckUtility.isNullOrEmpty(temperatureFlg)) {
                    temperatureFlg = temperatureFlg.replace("\"", "");
                }
            }

            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200041Param res = (A200041Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        BeanUtils.copyProperties(oldParam, res);
                        // BeanUtils.copyProperties処理にて Commandが設定される為、Commandをnullにする
                        oldParam.setCommand(null);

                        // 更新データを設定
                        BeanUtils.copyProperties(newParam, oldParam);

                        if (fvpResHist != null) {
                            // 履歴が正常に取得できている場合
                            if (OsolApiResultCode.API_OK.equals(fvpResHist.getFvpResultCd())) {
                                // 履歴から設定
                                A200041Param resHist = (A200041Param) fvpResHist.getParam();

                                if (newParam.getSettingCtrlPortList() != null && resHist.getSettingCtrlPortList() != null) {
                                    // 温度制御から取得した場合
                                    List<Map<String, String>> tempSettingCtrlPortList = new ArrayList<>();
                                    int index = 0;
                                    for (Map<String, String> settingCtrlPort : newParam.getSettingCtrlPortList()) {
                                        Map<String, String> tempSettingCtrlPort = new HashMap<String, String>(settingCtrlPort);
                                        // 念のためindexの存在チェック
                                        if (index < resHist.getSettingCtrlPortList().size()) {
                                            Map<String, String> tempSettingCtrlPortHist = new HashMap<String, String>(resHist.getSettingCtrlPortList().get(index));
                                            // 履歴取得した方の温度制御をチェック
                                            if (tempSettingCtrlPortHist.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                                                // 温湿度制御を有効に設定
                                                tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH, SmControlConstants.ABLE_PORT);
                                            } else if (tempSettingCtrlPortHist.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.DISABLE_PORT)) {
                                                // 温湿度制御を無効に設定
                                                tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH, SmControlConstants.DISABLE_PORT);
                                            }
                                            tempSettingCtrlPortList.add(tempSettingCtrlPort);
                                            index++;
                                        }
                                    }
                                    newParam.setSettingCtrlPortList(tempSettingCtrlPortList);
                                } else if (newParam.getLoadList() != null && resHist.getLoadList() != null) {
                                    // イベント制御から取得した場合
                                    List<Map<String, Object>> tempLoadList = new ArrayList<>();
                                    int index = 0;

                                    for (Map<String, Object> load : newParam.getLoadList()) {
                                        Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                                        // 念のためindexの存在チェック
                                        if (index < resHist.getLoadList().size()) {
                                            Map<String, Object> tempLoadHist = new HashMap<String, Object>(resHist.getLoadList().get(index));

                                            // イベント制御(温度制御) 有効/無効を設定
                                            settingEventControlTemperatureStatus(newParam.getProductCd(), tempLoad, tempLoadHist, type);
                                            tempLoadList.add(tempLoad);
                                            index++;
                                        }
                                    }
                                    newParam.setLoadList(tempLoadList);
                                } else {
                                    // NOT PROCESS
                                }
                                // 変更あり/なしに関わらず、新旧チェックのためONにしておく
                                temperatureFlg = String.valueOf(OsolConstants.FLG_ON);
                            }
                        } else {
                            // 履歴以外から設定
                            if (newParam.getSettingCtrlPortList() != null
                                    && SmControlConstants.TEMP_CONTROL_ENABLED_TO_DISABLED.equals(temperatureFlg)) {
                                // 温度制御から取得した場合
                                List<Map<String, String>> tempSettingCtrlPortList = new ArrayList<>();
                                for (Map<String, String> settingCtrlPort : newParam.getSettingCtrlPortList()) {
                                    Map<String, String> tempSettingCtrlPort = new HashMap<String, String>(settingCtrlPort);
                                    // 温湿度制御が有効の場合
                                    if (tempSettingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                                        // 無効にする
                                        tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH, SmControlConstants.DISABLE_PORT);
                                    }
                                    tempSettingCtrlPortList.add(tempSettingCtrlPort);
                                }
                                newParam.setSettingCtrlPortList(tempSettingCtrlPortList);
                            } else if (newParam.getLoadList() != null
                                    && SmControlConstants.TEMP_CONTROL_ENABLED_TO_DISABLED.equals(temperatureFlg)) {
                                // イベント制御から取得した場合
                                List<Map<String, Object>> tempLoadList = new ArrayList<>();
                                for (Map<String, Object> load : newParam.getLoadList()) {
                                    Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                                    Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                                    String strEventCtrlFlg = String.valueOf(tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                                    int intEventCtrlFlg = (int) Double.parseDouble(strEventCtrlFlg);
                                    List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>(){}.getType());
                                    // 温度制御可否判定
                                    if (isEventControlTemperature(newParam.getProductCd(), intEventCtrlFlg, settingEventCtrlList, type)) {
                                        // イベント制御フラグを無効にする
                                        tempLoad.put(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG, SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE);
                                    }
                                    tempLoadList.add(tempLoad);
                                }
                                newParam.setLoadList(tempLoadList);
                            } else {
                                // NOT PROCESS
                            }
                        }
                    }
                    break;
                }
            }
            // 温度制御設定フラグ
            newParam.setTemperatureCtrlFlg(temperatureFlg);
            // 温度制御種別（新旧チェック対象判別用）
            newParam.setTempControlType(type);
            oldParamList.add(oldParam);
            retList.add(newParam);
        }
        return retList;
    }

    /**
     * 複数建物・テナント一括 温度制御(設定) 冷房･暖房用 Paramリスト生成
     *   複数建物・テナント一括 温度制御(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *
     */
    private List<A200042Param> createParamListForBulkTemperatureCtrlUpdate(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList,
            List<FvpCtrlMngResponse<BaseParam>> fvpResHistList, List<A200042Param> oldParamList, String type) throws IllegalAccessException, InvocationTargetException {
        List<A200042Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200042Param newParam = new A200042Param();
            A200042Param oldParam = new A200042Param();

            // 履歴取得している場合、設定する
            FvpCtrlMngResponse<BaseParam> fvpResHist = null;

            // 履歴以外の場合、設定する
            String temperature = null;
            String ctrThreshold = null;

            // 履歴以外で差分の場合、設定する
            String tempCoolDiff = null;
            String tempCoolDiffSign = null;
            String tempHeatDiff = null;
            String tempHeatDiffSign = null;

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            // 履歴取得していた場合
            for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResHistList) {
                if (smId.equals(fvpRes.getSmId())) {
                    fvpResHist = fvpRes;
                    break;
                }
            }

            // 履歴取得していない場合
            if (fvpResHist == null) {
                // 入力値から取得
                if (SmControlConstants.COOLER.equals(type)) {
                    if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX) == null
                            && parameter.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD) == null) {
                        // 冷房差分
                        tempCoolDiff = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_COOL_DIFF);
                        tempCoolDiffSign = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_COOL_DIFF_SIGN);
                        if (!CheckUtility.isNullOrEmpty(tempCoolDiff)) {
                            tempCoolDiff = tempCoolDiff.replace("\"", "");
                        }

                        if (!CheckUtility.isNullOrEmpty(tempCoolDiffSign)) {
                            tempCoolDiffSign = tempCoolDiffSign.replace("\"", "");
                        }

                    } else if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX) != null) {
                        // G2/Eα/Eα2以外
                        temperature = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX);
                    } else {
                        // G2/Eα/Eα2
                        ctrThreshold = parameter.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
                    }
                } else if (SmControlConstants.HEATER.equals(type)) {
                    if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN) == null
                            && parameter.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD) == null) {
                        // 暖房差分
                        tempHeatDiff = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_HEAT_DIFF);
                        tempHeatDiffSign = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_HEAT_DIFF_SIGN);
                        if (!CheckUtility.isNullOrEmpty(tempHeatDiff)) {
                            tempHeatDiff = tempHeatDiff.replace("\"", "");
                        }

                        if (!CheckUtility.isNullOrEmpty(tempHeatDiffSign)) {
                            tempHeatDiffSign = tempHeatDiffSign.replace("\"", "");
                        }

                    } else if (parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN) != null) {
                        // G2/Eα/Eα2以外の直接入力
                        temperature = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN);
                    } else {
                        // G2/Eα/Eα2直接入力
                        ctrThreshold = parameter.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
                    }

                } else {
                    // NOT PROCESS
                }

                if (!CheckUtility.isNullOrEmpty(temperature)) {
                    temperature = temperature.replace("\"", "");
                }

                if (!CheckUtility.isNullOrEmpty(ctrThreshold)) {
                    ctrThreshold = ctrThreshold.replace("\"", "");
                }
            }


            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200041Param res = (A200041Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        BeanUtils.copyProperties(oldParam, res);
                        // BeanUtils.copyProperties処理にて Commandが設定される為、Commandをnullにする
                        oldParam.setCommand(null);

                        // 更新データを設定
                        BeanUtils.copyProperties(newParam, oldParam);

                        if (fvpResHist != null) {
                            // 履歴が正常に取得できている場合
                            if (OsolApiResultCode.API_OK.equals(fvpResHist.getFvpResultCd())) {
                                // 履歴から設定
                                A200041Param resHist = (A200041Param) fvpResHist.getParam();

                                if (newParam.getSettingCtrlPortList() != null && resHist.getSettingCtrlPortList() != null) {
                                    // 温度制御から取得した場合
                                    List<Map<String, String>> tempSettingCtrlPortList = new ArrayList<>();
                                    int index = 0;
                                    for (Map<String, String> settingCtrlPort : newParam.getSettingCtrlPortList()) {
                                        Map<String, String> tempSettingCtrlPort = new HashMap<String, String>(settingCtrlPort);
                                        if (tempSettingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                                            // 念のためindexの存在チェック
                                            if (index < resHist.getSettingCtrlPortList().size()) {
                                                Map<String, String> tempSettingCtrlPortHist = new HashMap<String, String>(resHist.getSettingCtrlPortList().get(index));
                                                // 履歴取得した方も温度制御かチェック
                                                if (tempSettingCtrlPortHist.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                                                    // 温湿度制御 有効
                                                    if (SmControlConstants.COOLER.equals(type)) {
                                                        tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX,
                                                                tempSettingCtrlPortHist.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX));
                                                    } else if (SmControlConstants.HEATER.equals(type)) {
                                                        tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN,
                                                                tempSettingCtrlPortHist.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN));
                                                    }
                                                }
                                            }
                                        }
                                        tempSettingCtrlPortList.add(tempSettingCtrlPort);
                                        index++;
                                    }
                                    newParam.setSettingCtrlPortList(tempSettingCtrlPortList);
                                } else if (newParam.getLoadList() != null && resHist.getLoadList() != null) {
                                    // イベント制御から取得した場合// イベント制御から取得した場合
                                    List<Map<String, Object>> tempLoadList = new ArrayList<>();
                                    List<String> thresholdList = new ArrayList<>();
                                    int index = 0;
                                    for (Map<String, Object> load : newParam.getLoadList()) {
                                        Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                                        Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                                        String strEventCtrlFlg = String.valueOf(tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                                        int intEventCtrlFlg = (int) Double.parseDouble(strEventCtrlFlg);
                                        List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>(){}.getType());

                                        // 温度制御可否判定
                                        if (isEventControlTemperature(newParam.getProductCd(), intEventCtrlFlg, settingEventCtrlList, type)) {
                                            // 念のためindexの存在チェック
                                            if (index < resHist.getLoadList().size()) {
                                                Map<String, Object> tempLoadHist = new HashMap<String, Object>(resHist.getLoadList().get(index));
                                                Object strSettingEventListHist = tempLoadHist.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                                                String strEventCtrlFlgHist = String.valueOf(tempLoadHist.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                                                int intEventCtrlFlgHist = (int) Double.parseDouble(strEventCtrlFlgHist);
                                                List<Map<String, String>> settingEventCtrlListHist = new Gson().fromJson(String.valueOf(strSettingEventListHist), new TypeToken<Collection<Map<String, String>>>(){}.getType());

                                                // 履歴取得した方も温度制御かチェック
                                                if (isEventControlTemperature(resHist.getProductCd(), intEventCtrlFlgHist, settingEventCtrlListHist, type)) {
                                                    // 履歴から制御閾値を取得
                                                    String threshold = getCtrlThreshold(resHist.getProductCd(), settingEventCtrlListHist, type);
                                                    // 制御閾値リストを初期化
                                                    thresholdList = getCtrlThresholdList(newParam.getProductCd(), settingEventCtrlList, threshold, type);

                                                    // 設定用のパラメータとして登録
                                                    settingEventCtrlList.get(0).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(0));
                                                    settingEventCtrlList.get(1).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(1));
                                                    settingEventCtrlList.get(2).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(2));

                                                    tempLoad.put(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST, settingEventCtrlList);

                                                }
                                            }
                                        }
                                        tempLoadList.add(tempLoad);
                                        index++;
                                    }
                                    newParam.setLoadList(tempLoadList);
                                }
                            }

                        } else {
                            // 履歴以外から設定
                            if (newParam.getSettingCtrlPortList() != null) {
                                // 温度制御から取得した場合
                                List<Map<String, String>> tempSettingCtrlPortList = new ArrayList<>();
                                for (Map<String, String> settingCtrlPort : newParam.getSettingCtrlPortList()) {
                                    Map<String, String> tempSettingCtrlPort = new HashMap<String, String>(settingCtrlPort);
                                    if (tempSettingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                                        // 温湿度制御 有効
                                        if (SmControlConstants.COOLER.equals(type)) {
                                            if (temperature == null) {
                                                // 差分設定
                                                String temperatureMax = tempSettingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX);
                                                temperature = getDiffCalcValue(newParam.getProductCd(), temperatureMax, tempCoolDiff, tempCoolDiffSign, type);
                                            }
                                            tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX, temperature);
                                        } else if (SmControlConstants.HEATER.equals(type)) {
                                            if (temperature == null) {
                                                // 差分設定
                                                String temperatureMin = tempSettingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN);
                                                temperature = getDiffCalcValue(newParam.getProductCd(), temperatureMin, tempHeatDiff, tempHeatDiffSign, type);
                                            }
                                            tempSettingCtrlPort.put(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN, temperature);
                                        } else {
                                            // NOT PROCESS
                                        }
                                    }
                                    tempSettingCtrlPortList.add(tempSettingCtrlPort);
                                }
                                newParam.setSettingCtrlPortList(tempSettingCtrlPortList);
                            } else if (newParam.getLoadList() != null) {
                                // イベント制御から取得した場合
                                List<Map<String, Object>> tempLoadList = new ArrayList<>();
                                List<String> thresholdList = new ArrayList<>();
                                for (Map<String, Object> load : newParam.getLoadList()) {
                                    Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                                    Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                                    String strEventCtrlFlg = String.valueOf(tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                                    int intEventCtrlFlg = (int) Double.parseDouble(strEventCtrlFlg);
                                    List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>(){}.getType());

                                    // 温度制御可否判定
                                    if (isEventControlTemperature(newParam.getProductCd(), intEventCtrlFlg, settingEventCtrlList, type)) {

                                        if (SmControlConstants.COOLER.equals(type)) {
                                            if (ctrThreshold == null) {
                                                // 差分設定
                                                String ctrThresholdCooler = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
                                                ctrThreshold = getDiffCalcValue(newParam.getProductCd(), ctrThresholdCooler, tempCoolDiff, tempCoolDiffSign, type);
                                            }
                                        } else if (SmControlConstants.HEATER.equals(type)) {
                                            if (ctrThreshold == null) {
                                                // 差分設定
                                                String ctrThresholdHeater = settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
                                                ctrThreshold = getDiffCalcValue(newParam.getProductCd(), ctrThresholdHeater, tempHeatDiff, tempHeatDiffSign, type);
                                            }
                                        }

                                        // 制御閾値リストを初期化
                                        thresholdList = getCtrlThresholdList(newParam.getProductCd(), settingEventCtrlList, ctrThreshold, type);

                                        // 設定用のパラメータとして登録
                                        settingEventCtrlList.get(0).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(0));
                                        settingEventCtrlList.get(1).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(1));
                                        settingEventCtrlList.get(2).put(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD, thresholdList.get(2));

                                        tempLoad.put(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST, settingEventCtrlList);
                                    }

                                    tempLoadList.add(tempLoad);
                                }
                                newParam.setLoadList(tempLoadList);
                            } else {
                                // NOT PROCESS
                            }
                        }
                    }
                    break;
                }
            }

            oldParamList.add(oldParam);
            // 温度制御種別（新旧チェック対象判別用）
            newParam.setTempControlType(type);
            retList.add(newParam);
        }

        return retList;
    }

    private static String toNumParam(String arg, int size) {
        int _arg = (arg != null && arg.length() > 0) ? Integer.parseInt(arg) : 0;
        return toNumParam(_arg, size);
    }
    private static String toNumParam(int arg, int size) {
        String format = "%0" + String.valueOf(size) + "d";
        return String.format(format, arg);

    }

    /**
     * 制御閾値情報リストを取得する
     *
     * @param productCd
     * @param settingEventCtrlList
     * @param ctrThreshold
     * @param controlType
     * @return
     */
    private List<String> getCtrlThresholdList(String productCd, List<Map<String, String>> settingEventCtrlList, String ctrThreshold, String controlType) {
        List<String> ret = new ArrayList<>();

        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            // Eα/Eα2の場合（ctrThreshold：+00xxx）形式

            // イベント1 の制御閾値
            ret.add(SmControlConstants.COOLER.equals(controlType) ? getCtrlThresholdEa(productCd, settingEventCtrlList, ctrThreshold, 0)
                                                                   : settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD));
            // イベント2 の制御閾値
            ret.add(SmControlConstants.HEATER.equals(controlType) ? getCtrlThresholdEa(productCd, settingEventCtrlList, ctrThreshold, 1)
                                                                   : settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD));
            // イベント3 の制御閾値
            ret.add(getCtrlThresholdEa(productCd, settingEventCtrlList, settingEventCtrlList.get(2).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD), 2));
        } else {
            // G2の場合（ctrThreshold：0xxx）形式

            // イベント1 の制御閾値
            ret.add(SmControlConstants.COOLER.equals(controlType) ? toNumParam(ctrThreshold, 4) : settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD));
            // イベント2 の制御閾値
            ret.add(SmControlConstants.HEATER.equals(controlType) ? toNumParam(ctrThreshold, 4) : settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD));
            // イベント3 の制御閾値
            ret.add(toNumParam(settingEventCtrlList.get(2).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD), 4));
        }

        return ret;
    }

    /**
     * 制御閾値情報を取得する
     *
     * @param productCd
     * @param settingEventCtrlList
     * @param ctrThreshold
     * @param controlType
     * @return
     */
    private String getCtrlThreshold(String productCd, List<Map<String, String>> settingEventCtrlList, String controlType) {
        String ret = null;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            if (SmControlConstants.COOLER.equals(controlType) ) {
                // イベント1 の制御閾値
                ret = getCtrlThresholdEa(productCd, settingEventCtrlList, settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD), 0);
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // イベント1 の制御閾値
                ret = getCtrlThresholdEa(productCd, settingEventCtrlList, settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD), 1);
            } else {
                // イベント３の制御閾値
                ret = getCtrlThresholdEa(productCd, settingEventCtrlList, settingEventCtrlList.get(2).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD), 2);
            }
        } else {
            if (SmControlConstants.COOLER.equals(controlType) ) {
                // イベント1 の制御閾値
                ret = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // イベント1 の制御閾値
                ret = settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
            } else {
                // イベント３の制御閾値
                ret = settingEventCtrlList.get(2).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
            }
        }

        return ret;
    }

    /**
     * Eα系機種向けに設定する制御閾値を整形して取得する
     *
     * @param settingEventCtrlList
     * @param ctrThreshold
     * @param eventIndex  0:イベント1, 1:イベント2, 2:イベント3
     * @return
     */
    private String getCtrlThresholdEa(String productCd, List<Map<String, String>> settingEventCtrlList, String ctrThreshold, int eventIndex) {
        String ret;
        int ctrlTerms = Integer.parseInt(settingEventCtrlList.get(eventIndex).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_TERMS));

        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) && ((ctrlTerms == 2) || (ctrlTerms == 3))) {
            ret = "   " + settingEventCtrlList.get(eventIndex).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD);
        } else {
            // 先頭文字を取得
            String firstChar = ctrThreshold.substring(0, 1);
            if ("+".equals(firstChar)
                    || "-".equals(firstChar)) {
                // 符号を付ける
                ret =  firstChar + toNumParam(ctrThreshold.substring(1, ctrThreshold.length()), 5);
            } else {
                // 符号を付ける
                ret =  "+" + toNumParam(ctrThreshold, 5);
            }

        }

        return ret;
    }

    /**
     * イベント制御(温度制御) 可否判定
     *
     * @param productCd
     * @param eventCtrlFlg
     * @param settingEventCtrlList
     * @param controlType
     * @return 判定結果
     */
    private boolean isEventControlTemperature(String productCd, int eventCtrlFlg, List<Map<String, String>> settingEventCtrlList, String controlType) {
        boolean ret = false;

        // FVPα(G2)の場合
        // イベント制御フラグ = 有効 且つ イベント条件 = 温度制御 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(productCd)) {
            // イベント条件取得
            String eventTerms = null;
            if (SmControlConstants.COOLER.equals(controlType) || SmControlConstants.TEMP_CONTROL.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else {
                // NOT PROCESS
            }

            // 温度制御可否判定
            if (String.valueOf(eventCtrlFlg).equals(SmControlConstants.ABLE_PORT)
                    && (!CheckUtility.isNullOrEmpty(eventTerms) && eventTerms.equals("2"))) {
                ret = true;
            }
        }

        // Eα / Eα2の場合
        // イベント制御フラグ = 温度制御有効 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            // 温度制御可否判定
            if (String.valueOf(eventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                ret = true;
            }
        }

        return ret;
    }

    /**
     * イベント制御(温度制御) 有効/無効
     *
     * @param productCd
     * @param eventCtrlFlg
     * @param settingEventCtrlList
     * @param controlType
     * @return 判定結果 true:有効、false:無効、null:温度制御以外
     */
    private Boolean isEventControlTemperatureValidation(String productCd, int eventCtrlFlg, List<Map<String, String>> settingEventCtrlList, String controlType) {
        Boolean ret = null;

        // FVPα(G2)の場合
        // イベント制御フラグ = 有効 且つ イベント条件 = 温度制御 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(productCd)) {
            // イベント条件取得
            String eventTerms = null;
            if (SmControlConstants.COOLER.equals(controlType) || SmControlConstants.TEMP_CONTROL.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else {
                // NOT PROCESS
            }

            // 温度制御可否判定
            if (!CheckUtility.isNullOrEmpty(eventTerms) && eventTerms.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                ret = String.valueOf(eventCtrlFlg).equals(SmControlConstants.ABLE_PORT);
            }
        }

        // Eα / Eα2の場合
        // イベント制御フラグ = 温度制御有効 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            // 温度制御可否判定
            if (String.valueOf(eventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE)
                    || String.valueOf(eventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                ret = String.valueOf(eventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE);
            }
        }

        return ret;
    }

    /**
     * イベント制御(温度制御) 有効/無効設定
     * @param productCd
     * @param tempLoad
     * @param tempLoadHist
     * @param controlType
     */
    private void settingEventControlTemperatureStatus(String productCd, Map<String, Object> tempLoad, Map<String, Object> tempLoadHist, String controlType) {

        // 最新値
        Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
        String strEventCtrlFlg= String.valueOf(tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
        int intEventCtrlFlg= (int) Double.parseDouble(strEventCtrlFlg);
        List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>(){}.getType());

        // 履歴値
        Object strSettingEventListHist = tempLoadHist.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
        String strEventCtrlFlgHist = String.valueOf(tempLoadHist.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
        int intEventCtrlFlgHist = (int) Double.parseDouble(strEventCtrlFlgHist);
        List<Map<String, String>> settingEventCtrlListHist = new Gson().fromJson(String.valueOf(strSettingEventListHist), new TypeToken<Collection<Map<String, String>>>(){}.getType());

        // FVPα(G2)の場合
        // イベント制御フラグ = 有効 且つ イベント条件 = 温度制御 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(productCd)) {
            // イベント条件取得
            String eventTerms = null;
            String eventTermsHist = null;
            if (SmControlConstants.COOLER.equals(controlType) || SmControlConstants.TEMP_CONTROL.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
                eventTermsHist = settingEventCtrlListHist.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                eventTerms = settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
                eventTermsHist = settingEventCtrlListHist.get(1).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
            } else {
                // NOT PROCESS
            }

            // 温度制御可否判定（最新値および履歴値が温度制御）
            if (!CheckUtility.isNullOrEmpty(eventTerms) && eventTerms.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)
                    && !CheckUtility.isNullOrEmpty(eventTermsHist) && eventTermsHist.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                // イベント条件を温度制御に設定
                tempLoad.put(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG, String.valueOf(intEventCtrlFlgHist));
            }
        }

        // Eα / Eα2の場合
        // イベント制御フラグ = 温度制御有効 であれば 温度制御可
        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            boolean isTempCtrl = false;
            boolean isTempCtrlHist = false;
            // 最新値のイベント制御が温度制御有効/無効の場合
            if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE)
                    || String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                isTempCtrl = true;
            }

            // 履歴値のイベント制御が温度制御有効/無効の場合
            if (String.valueOf(intEventCtrlFlgHist).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE)
                    || String.valueOf(intEventCtrlFlgHist).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                isTempCtrlHist = true;
            }

            // 温度制御可否判定
            if (isTempCtrl && isTempCtrlHist) {
                tempLoad.put(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG, String.valueOf(intEventCtrlFlgHist));
            }
        }
    }

    /**
     * 複数建物・テナント一括 温度制御(設定)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTemperatureCtrlUpdate(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // 対応機器チェック
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }

                // listサイズチェック
                if (SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) ||
                    SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) ||
                    SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd())
                    ) {
                    List<Map<String, String>> ctrlTimeZoneTHList = ((A200042Param) resultData.getParam()).getCtrlTimeZoneTHList();
                    if (ctrlTimeZoneTHList.size() != SmControlConstants.CTRL_TIME_ZONE_LIST) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("ctrlTimeZoneTHList.size()=%d", ctrlTimeZoneTHList.size()));
                        resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        continue;
                    }

                    List<Map<String, String>> settingCtrlPortList = ((A200042Param) resultData.getParam()).getSettingCtrlPortList();
                    if (settingCtrlPortList.size() != SmControlConstants.CTRL_PORT_LIST) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("ctrlTimeZoneTHList.size()=%d", ctrlTimeZoneTHList.size()));
                        resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        continue;
                    }
                }

                if (SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) ||
                    SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd())
                    ) {
                    // list内の機種依存の項目をバリデーションチェック
                    List<Map<String, String>> settingCtrlPortList = ((A200042Param) resultData.getParam()).getSettingCtrlPortList();
                    for (Map<String, String> map : settingCtrlPortList) {
                        String demandGangCtrlPermission = map.get(SmControlConstants.BULK_CTRL_PARAM_DEMAND_GANG_PERMISSION);
                        // 桁数チェックとnullチェックを行う
                        if (demandGangCtrlPermission == null || !(demandGangCtrlPermission.matches("[0-9]"))) {
                            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("demandGangCtrlPermission=%s", demandGangCtrlPermission));
                            resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        }
                    }
                }

                if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd())) {
                    List<Map<String, String>> settingCtrlPortList = ((A200042Param) resultData.getParam()).getSettingCtrlPortList();
                    // list内の機種依存の項目をバリデーションチェック
                    for (Map<String, String> map : settingCtrlPortList) {
                        String switchChoiceCW = map.get(SmControlConstants.BULK_CTRL_PARAM_SWITCH_CHOICE_CW);
                        // 桁数チェックとnullチェックを行う
                        if (switchChoiceCW == null || !(switchChoiceCW.matches("[0-9]"))) {
                            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("switchChoiceCW=%s", switchChoiceCW));
                            resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        }
                    }
                }

                if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) ||
                    SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) ||
                    SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    // List数チェック
                    List<Map<String, Object>> loadList = ((A200042Param) resultData.getParam()).getLoadList();
                    if ((SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST) ||
                        (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST_E_ALPHA) ||
                        (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd()) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST_E_ALPHA_2)
                        ) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("loadList.size()=%d", loadList.size()));
                        resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        continue;
                    }

                    for (Map<String, Object> load : loadList) {
                        Object strSettingEventList = load.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                        // 入れ子リスト取得
                        List<Map<String, String>> eventList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>(){}.getType());
                        if (eventList.size() != SmControlConstants.SETTING_EVENT_LIST) {
                            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("eventList.size()=%d", eventList.size()));
                            resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                        }

                        // 機種固有パラメータチェック
                        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())) {
                            for (Map<String, String> eventInfo : eventList) {
                                String comparePoint = eventInfo.get("comparePoint");

                                if ((comparePoint == null) || !(comparePoint.matches("[0-9]{1,3}"))) {
                                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("comparePoint=%s", comparePoint));
                                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 温度制御(取得) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTemperatureCtrlSelect(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200041Param> paramList = createParamListForBulkTemperatureCtrlSelect(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkTemperatureCtrlSelect(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataList);

        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 温度制御(取得) 実行 履歴取得
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkTemperatureCtrlSelectHist(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList, String type) throws SmControlException {
        // Paramリストを生成
        List<A200041Param> paramList = createParamListForBulkTemperatureCtrlSelectHist(parameterList, type);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkTemperatureCtrlSelect(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqListHist(resultDataList, SmControlConstants.BULK_CTRL_CAST_TEMPERATURE_CTRL_SELECT);

        // 履歴取得が無ければ空のリストを返却
        if (fvpReqList.isEmpty()) {
            return new ArrayList<>();
        }
        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 温度制御(取得)用 Paramリスト生成
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200041Param> createParamListForBulkTemperatureCtrlSelect(List<Map<String,String>> parameterList) {
        List<A200041Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200041Param param = new A200041Param();

            // 最新履歴
            String settingChangeHist = SmControlConstants.SETTING_CHG_HIST_LATEST;
            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));
            Long buildingId = null;
            if (!CheckUtility.isNullOrEmpty(parameter.get(SmControlConstants.BULK_CTRL_PARAM_BUILDING_ID))) {
                buildingId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_BUILDING_ID));
            }
            Integer getCurrentTemperatureFlg = OsolConstants.FLG_OFF;
            if (!CheckUtility.isNullOrEmpty(parameter.get(SmControlConstants.BULK_CTRL_PARAM_GET_CUR_TEMPERATURE_FLG))) {
                getCurrentTemperatureFlg = Integer.parseInt(parameter.get(SmControlConstants.BULK_CTRL_PARAM_GET_CUR_TEMPERATURE_FLG));
            }

            param.setSettingChangeHist(settingChangeHist);
            param.setSmId(smId);
            param.setBuildingId(buildingId);
            param.setGetCurrentTemperatureFlg(getCurrentTemperatureFlg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 温度制御(取得)用 Paramリスト生成（履歴取得）
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200041Param> createParamListForBulkTemperatureCtrlSelectHist(List<Map<String,String>> parameterList, String type) {
        List<A200041Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200041Param param = new A200041Param();

            String settingChangeHist = null;
            if (SmControlConstants.COOLER.equals(type)) {
                settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_COOL_HIST);
            } else if (SmControlConstants.HEATER.equals(type)) {
                settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_HEAT_HIST);
            } else if (SmControlConstants.TEMP_CONTROL.equals(type)) {
                settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_TEMP_CTRL_HIST);
            }

            if (!CheckUtility.isNullOrEmpty(settingChangeHist)) {
                settingChangeHist = settingChangeHist.replace("\"", "");
            }

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));
            Long buildingId = null;
            if (!CheckUtility.isNullOrEmpty(parameter.get(SmControlConstants.BULK_CTRL_PARAM_BUILDING_ID))) {
                buildingId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_BUILDING_ID));
            }
            Integer getCurrentTemperatureFlg = OsolConstants.FLG_OFF;
            if (!CheckUtility.isNullOrEmpty(parameter.get(SmControlConstants.BULK_CTRL_PARAM_GET_CUR_TEMPERATURE_FLG))) {
                getCurrentTemperatureFlg = Integer.parseInt(parameter.get(SmControlConstants.BULK_CTRL_PARAM_GET_CUR_TEMPERATURE_FLG));
            }

            param.setSettingChangeHist(settingChangeHist);
            param.setSmId(smId);
            param.setBuildingId(buildingId);
            param.setGetCurrentTemperatureFlg(getCurrentTemperatureFlg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 温度制御(取得)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkTemperatureCtrlSelect(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }


    /**
     * 複数建物・テナント一括 温度制御用 メール内容設定
     *
     * @param fvpResList
     * @param resultDataList
     * @return targetList
     *
     */
    private List<SmControlVerocityResult> mailBodySettingTemperatureCtrl(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        // 検索用リスト生成
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        for (int i = 0; i < fvpResList.size(); i++) {
            List<String> buildingIdList = resultDataList.get(i).getBuildingIdList();
            for (String buildingId : buildingIdList) {
                // メールAPI用 Resultクラス
                SmControlVerocityResult target = new SmControlVerocityResult();

                // 設定情報レコード取得
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);

                // 機器IDが未存在でエラーの場合、処理しない
                if(fvpRes.getSmId() == null ) {
                    continue;
                }

                // リクエスト新設定情報レコード取得
                A200042Param newRecord = this.newDataListTemperature.get(i);
                // リクエスト旧設定情報レコード取得
                A200042Param oldRecord = this.oldDataListTemperature.get(i);

                // 新旧設定情報のどちらかでも存在しない場合スキップ
                if (oldRecord == null || newRecord == null) {
                    continue;
                }

                // 温度制御,イベント制御判定
                checkCommand(newRecord);

                target.setSmId(fvpRes.getSmId());
                target.setCorpId(this.loginCorpId);
                target.setPersonId(String.valueOf(this.loginPersonId));
                target.setSmAddress(fvpRes.getSmAddress());
                target.setIpAddress(fvpRes.getIpAddress());
                target.setCommand(SmControlConstants.BULK_TEMPERATURE_COMMAND);
                target.setBuildingId(buildingId);

                // 冷房・暖房設定
                if (SETTING_CONDITON.equals(SmControlConstants.COOLER)
                        || SETTING_CONDITON.equals(SmControlConstants.HEATER)) {
                    // 設定前後情報格納
                    if (oldRecord != null && newRecord != null) {
                        if(settingOldData(oldRecord) == null || settingOldData(oldRecord).isEmpty()) {
                            target.setOldData("－");
                        } else {
                            target.setOldData(settingOldData(oldRecord));
                        }
                        if(settingNewData(newRecord) == null || settingNewData(newRecord).isEmpty()) {
                            target.setNewData("－");
                        } else {
                            target.setNewData(settingNewData(newRecord));
                        }
                    }
                }

                // 温度制御設定
                if (SETTING_CONDITON.equals(SmControlConstants.TEMP_CONTROL)) {
                    // 設定前後情報格納
                    if (oldRecord != null && newRecord != null) {
                        String[] resultData = settingData(oldRecord, newRecord);
                        if (resultData[0] != null && resultData[1] != null) {
                            target.setOldData(resultData[0]);
                            target.setNewData(resultData[1]);
                        }
                    }
                }

                // 新旧レコードチェック
                if (fvpRes.getFvpResultCd() == SmControlConstants.RECORD_NO_CHANGE) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
                    targetList.add(target);
                    continue;
                }
                // 例外が発生している場合はRECORD_NG
                else if (fvpRes.getFvpResultCd() != OsolApiResultCode.API_OK) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NG);
                    targetList.add(target);
                    continue;
                }
                target.setResult(SmControlConstants.MAIL_SETTING_OK);

                targetList.add(target);
            }
        }
        return targetList;
    }

    /**
     * 複数建物・テナント一括 温度制御用 コマンド判定
     *
     * @param newRecord
     *
     */
    private void checkCommand(A200042Param newRecord) {
        // 設定情報の項目により判定
        if (newRecord.getLoadList() == null) {
            this.COMMAND_CHECK = SmControlConstants.TEMPERATURE_COMMAND;
        }else{
            this.COMMAND_CHECK = SmControlConstants.EVENT_COMMAND;
        }
    }

    /**
     * 複数建物・テナント一括 温度制御用 設定前情報格納
     *
     * @param oldRecord
     *
     */
    private String settingOldData(A200042Param oldRecord) {
        String oldData = null;

        // 設定前情報が未存在
        if((oldRecord.getLoadList() == null || oldRecord.getLoadList().size() == 0) &&
                (oldRecord.getSettingCtrlPortList() == null || oldRecord.getSettingCtrlPortList().size() == 0)) {
            return oldData;
        }

        // 冷房設定かつイベント制御
        if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
            for (Map<String,Object> load :oldRecord.getLoadList()) {
                Object strSettingEventList = load.get("settingEventCtrlList");
                // 制御ポートが有効な値を設定
                String strValue = String.valueOf(load.get("eventCtrlFlg"));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                // 温度制御可否判定
                if (isEventControlTemperature(oldRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.COOLER)) {
                    // 制御閾値
                    oldData = settingEventCtrlList.get(0).get("ctrlThreshold");
                    break;
                }
            }
            // 制御ポートがすべて無効なら任意の値を設定する
            if (oldData == null ) {
                Object strSettingEventList = oldRecord.getLoadList().get(0).get("settingEventCtrlList");
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                oldData = settingEventCtrlList.get(0).get("ctrlThreshold");
            }
        }
        // 冷房設定かつ温度制御
        else if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)) {
            // 制御ポートが有効な値を設定
            for (Map<String,String> settingCtrlPort :oldRecord.getSettingCtrlPortList()) {
                if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                    // 温度上限
                    oldData = settingCtrlPort.get("temperatureMax");
                    break;
                }
            }
            // 制御ポートがすべて無効なら任意の値を設定する
            if (oldData == null ) {
                oldData = oldRecord.getSettingCtrlPortList().get(0).get("temperatureMax");
            }
        }
        // 暖房設定かつイベント制御
        else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
            // 制御ポートが有効な値を設定
            for (Map<String,Object> load :oldRecord.getLoadList()) {
                Object strSettingEventList = load.get("settingEventCtrlList");
                String strValue = String.valueOf(load.get("eventCtrlFlg"));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                // 温度制御可否判定
                if (isEventControlTemperature(oldRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.HEATER)) {
                    // 制御閾値
                    oldData = settingEventCtrlList.get(1).get("ctrlThreshold");
                    break;
                }
            }
            // 制御ポートがすべて無効なら任意の値を設定する
            if (oldData == null ) {
                Object strSettingEventList = oldRecord.getLoadList().get(0).get("settingEventCtrlList");
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                oldData = settingEventCtrlList.get(1).get("ctrlThreshold");
            }
        }
        // 暖房設定かつ温度制御
        else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)){
            // 制御ポートが有効な値を設定
            for (Map<String,String> settingCtrlPort :oldRecord.getSettingCtrlPortList()) {
                if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                    // 温度下限
                    oldData = settingCtrlPort.get("temperatureMin");
                    break;
                }
            }
            // 制御ポートがすべて無効なら任意の値を設定する
            if (oldData == null ) {
                oldData = oldRecord.getSettingCtrlPortList().get(0).get("temperatureMin");
            }
        }

        return oldData;
    }

    /**
     * 複数建物・テナント一括 温度制御用 設定情報格納
     *
     * @param newRecord
     *
     */
    private String settingNewData(A200042Param newRecord) {
        String newData = null;

        // 設定情報が未存在
        if((newRecord.getLoadList() == null || newRecord.getLoadList().size() == 0) &&
                (newRecord.getSettingCtrlPortList() == null || newRecord.getSettingCtrlPortList().size() == 0)) {
            return newData;
        }

        // 冷房設定かつイベント制御
        if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
            for (Map<String,Object> load :newRecord.getLoadList()) {
                Object strSettingEventList = load.get("settingEventCtrlList");
                // 制御ポートが有効な値を設定
                String strValue = String.valueOf(load.get("eventCtrlFlg"));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                // 温度制御可否判定
                if (isEventControlTemperature(newRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.COOLER)) {
                    // 制御閾値
                    newData = settingEventCtrlList.get(0).get("ctrlThreshold");
                    break;
                }
            }
        }
        // 冷房設定かつ温度制御
        else if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)) {
            // 制御ポートが有効な値を設定
            for (Map<String, String> settingCtrlPort :newRecord.getSettingCtrlPortList()) {
                if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                    // 温度上限
                    newData = settingCtrlPort.get("temperatureMax");
                    break;
                }
            }
        }
        // 暖房設定かつイベント制御
        else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
            // 制御ポートが有効な値を設定
            for (Map<String,Object> load :newRecord.getLoadList()) {
                Object strSettingEventList = load.get("settingEventCtrlList");
                String strValue = String.valueOf(load.get("eventCtrlFlg"));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                // 温度制御可否判定
                if (isEventControlTemperature(newRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.HEATER)) {
                    // 制御閾値
                    newData = settingEventCtrlList.get(1).get("ctrlThreshold");
                    break;
                }
            }
        }
        // 暖房設定かつ温度制御
        else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)){
            // 制御ポートが有効な値を設定
            for (Map<String,String> settingCtrlPort :newRecord.getSettingCtrlPortList()) {
                if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                    // 温度下限
                    newData = settingCtrlPort.get("temperatureMin");
                    break;
                }
            }
        }
        return newData;
    }

    /**
     * 温度制御設定のメール用 新旧データ取得
     * @param oldRecord
     * @param newRecord
     * @return 変更前：String[0]、変更後：String[1]
     */
    private String[] settingData(A200042Param oldRecord, A200042Param newRecord) {
        String[] resultData = {null, null};

        // 設定情報が未存在
        if((oldRecord.getLoadList() == null || oldRecord.getLoadList().size() == 0) &&
                (oldRecord.getSettingCtrlPortList() == null || oldRecord.getSettingCtrlPortList().size() == 0)) {
            return resultData;
        }

        if((newRecord.getLoadList() == null || newRecord.getLoadList().size() == 0) &&
                (newRecord.getSettingCtrlPortList() == null || newRecord.getSettingCtrlPortList().size() == 0)) {
            return resultData;
        }

        // 温度制御設定かつイベント制御
        if(SETTING_CONDITON.equals(SmControlConstants.TEMP_CONTROL) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
            int index = 0;
            for (Map<String,Object> load : oldRecord.getLoadList()) {
                Object strSettingEventList = load.get("settingEventCtrlList");
                // 制御ポートが有効な値を設定
                String strValue = String.valueOf(load.get("eventCtrlFlg"));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                if (index < newRecord.getLoadList().size()) {
                    Map<String,Object> loadNew = newRecord.getLoadList().get(index);
                    Object strSettingEventListNew = loadNew.get("settingEventCtrlList");
                    // 制御ポートが有効な値を設定
                    String strValueNew = String.valueOf(loadNew.get("eventCtrlFlg"));
                    int intEventCtrlFlgNew = (int)Double.parseDouble(strValueNew);
                    List<Map<String,String>> settingEventCtrlListNew = new Gson().fromJson(String.valueOf(strSettingEventListNew), new TypeToken<Collection<Map<String,String>>>(){}.getType());

                    // oldチェック
                    if (isEventControlTemperature(oldRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.TEMP_CONTROL)
                            && !isEventControlTemperature(newRecord.getProductCd(), intEventCtrlFlgNew, settingEventCtrlListNew, SmControlConstants.TEMP_CONTROL)) {
                        resultData[0] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE_NAME;
                        resultData[1] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE_NAME;
                        break;
                    }
                    // newチェック
                    if (isEventControlTemperature(newRecord.getProductCd(), intEventCtrlFlgNew, settingEventCtrlListNew, SmControlConstants.TEMP_CONTROL)
                            && !isEventControlTemperature(oldRecord.getProductCd(), intEventCtrlFlg, settingEventCtrlList, SmControlConstants.TEMP_CONTROL)) {
                        resultData[0] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE_NAME;
                        resultData[1] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE_NAME;
                        break;
                    }
                }
                index++;
            }
        }
        // 温度制御設定かつ温度制御
        else if(SETTING_CONDITON.equals(SmControlConstants.TEMP_CONTROL) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)) {
            int index = 0;

            // 制御ポートが有効な値を設定
            for (Map<String,String> settingCtrlPort : oldRecord.getSettingCtrlPortList()) {
                if (index < newRecord.getSettingCtrlPortList().size()) {
                    Map<String,String> settingCtrlPortNew = newRecord.getSettingCtrlPortList().get(index);

                    if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                        resultData[0] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE_NAME;
                        resultData[1] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE_NAME;
                        break;
                    }

                    if (settingCtrlPortNew.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                        resultData[0] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE_NAME;
                        resultData[1] = SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE_NAME;
                        break;
                    }
                }
                index++;
            }
        }
        return resultData;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkScheduleUpdate(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        List<BulkCtrlResultData> resultDataBulkScheduleSelectList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkScheduleUpdateList = new ArrayList<>(resultDataList);
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResUpdateList = new ArrayList<>();
        List<A200005Param> oldParamList = new ArrayList<>();
        List<A200005Param> newParamList = new ArrayList<>();

        // 複数建物・テナント一括 スケジュール(取得)を実行
        try {
            fvpResSelectList = executeBulkScheduleSelect(parameterList, resultDataBulkScheduleSelectList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        // Paramリストを生成
        try {
            newParamList = createParamListForBulkScheduleUpdate(parameterList, fvpResSelectList, oldParamList);
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(newParamList, oldParamList, resultDataBulkScheduleUpdateList);

        // 機器情報を取得
        getSmInfo(resultDataBulkScheduleUpdateList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataBulkScheduleUpdateList);

        // 固有のバリデーションを実行
        validationBulkScheduleUpdate(resultDataBulkScheduleUpdateList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataBulkScheduleUpdateList);

        // 機器制御を実行し、結果を返却
        fvpResUpdateList = executeBulkFvpCtrl(fvpReqList);

        // 設定情報保持
        this.newDataListSchedule.addAll(newParamList);
        // 設定前情報保持
        this.oldDataListSchedule.addAll(oldParamList);

        return fvpResUpdateList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定)用 Paramリスト生成
     *   複数建物・テナント一括 スケジュール(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *
     */
    private List<A200005Param> createParamListForBulkScheduleUpdate(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<A200005Param> oldParamList) throws IllegalAccessException, InvocationTargetException {
        List<A200005Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200005Param newParam = new A200005Param();
            A200005Param oldParam = new A200005Param();

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            String mngAssignment = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SCHEDULE_MNG_ASSIGNMENT);
            if (CheckUtility.isNullOrEmpty(mngAssignment)) {
                mngAssignment = "0";
            } else {
                mngAssignment = mngAssignment.replace("\"", "");
            }

            String pageAssignment = parameter.get(SmControlConstants.BULK_CTRL_PARAM_PAGE_ASSIGNMENT);
            if (CheckUtility.isNullOrEmpty(pageAssignment)) {
                pageAssignment = "0";
            } else {
                pageAssignment = pageAssignment.replace("\"", "");
            }

            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200004Param res = (A200004Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        BeanUtils.copyProperties(oldParam, res);
                        // BeanUtils.copyProperties処理にて Commandが設定される為、Commandをnullにする
                        oldParam.setCommand(null);

                        // 更新データを設定
                        BeanUtils.copyProperties(newParam, oldParam);
                        newParam.setScheduleManageAssignment(mngAssignment);
                        newParam.setPageAssignment(pageAssignment);
                        newParam.setUpdateDBflg(true);
                    }
                    break;
                }
            }

            oldParamList.add(oldParam);
            retList.add(newParam);

            // DBフラグを保持
            this.updateDBflgListSchedule.add(newParam.isUpdateDBflg());
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkScheduleUpdate(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // 対応機器チェック
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }

                // listサイズチェック
                List<Map<String, Object>> loadList = ((A200005Param) resultData.getParam()).getLoadList();
                if ((SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FV2)) ||
                    (SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FVP_ALPHA_D)) ||
                    (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd()) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FVP_ALPHA_G2))
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("loadList.size()=%d", loadList.size()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }

                for (Map<String, Object> load : loadList) {
                    Object strSettingMonthScheduleList = load.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_MONTH_SCHEDULE_LIST);
                    List<Map<String,String>> mmScheduleList = new Gson().fromJson(String.valueOf(strSettingMonthScheduleList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                    if (mmScheduleList.size() != SmControlConstants.SCHEDULE_LOAD_LIST_MM_SCHEDULELIST) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("mmScheduleList.size()=%d", mmScheduleList.size()));
                        resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    }
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkScheduleSelect(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200004Param> paramList = createParamListForBulkScheduleSelect(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkSchedule(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataList);

        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得)用 Paramリスト生成
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200004Param> createParamListForBulkScheduleSelect(List<Map<String,String>> parameterList) {
        List<A200004Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200004Param param = new A200004Param();

            String settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_CHANGE_HIST);
            String pageAssignment = parameter.get(SmControlConstants.BULK_CTRL_PARAM_PAGE_ASSIGNMENT);
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            // 未設定時、0をセット
            if(pageAssignment == null || pageAssignment.isEmpty()) {
                pageAssignment = "0";
            }

            param.setSettingChangeHist(settingChangeHist);
            param.setPageAssignment(pageAssignment);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkSchedule(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定) 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkScheduleUpdateEa(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        List<BulkCtrlResultData> resultDataBulkScheduleSelectList = new ArrayList<>(resultDataList);
        List<BulkCtrlResultData> resultDataBulkScheduleUpdateList = new ArrayList<>(resultDataList);
        List<FvpCtrlMngResponse<BaseParam>> fvpResSelectList = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResUpdateList = new ArrayList<>();
        List<A200060Param> oldParamList = new ArrayList<>();
        List<A200060Param> newParamList = new ArrayList<>();

        // 複数建物・テナント一括 スケジュール(取得) Eα を実行
        try {
            fvpResSelectList = executeBulkScheduleSelectEa(parameterList, resultDataBulkScheduleSelectList);
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }

        // Paramリストを生成
        try {
            newParamList = createParamListForBulkScheduleUpdateEa(parameterList, fvpResSelectList, oldParamList);
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(newParamList, oldParamList, resultDataBulkScheduleUpdateList);

        // 機器情報を取得
        getSmInfo(resultDataBulkScheduleUpdateList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataBulkScheduleUpdateList);

        // 固有のバリデーションを実行
        validationBulkScheduleUpdateEa(resultDataBulkScheduleUpdateList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataBulkScheduleUpdateList);

        // 機器制御を実行し、結果を返却
        fvpResUpdateList = executeBulkFvpCtrl(fvpReqList);

        // 設定情報保持
        this.newDataListScheduleEa.addAll(newParamList);
        // 設定前情報保持
        this.oldDataListScheduleEa.addAll(oldParamList);

        return fvpResUpdateList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定)用 Paramリスト生成
     *   複数建物・テナント一括 スケジュール(取得)で取得した内容を基に設定用 Paramリストを生成
     *
     * @param parameterList
     * @param fvpResList
     * @param oldParamList
     * @return Paramリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *
     */
    private List<A200060Param> createParamListForBulkScheduleUpdateEa(List<Map<String,String>> parameterList, List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<A200060Param> oldParamList) throws IllegalAccessException, InvocationTargetException {
        List<A200060Param> retList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200060Param newParam = new A200060Param();
            A200060Param oldParam = new A200060Param();

            Long smId = Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID));

            String mngAssignment = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SCHEDULE_MNG_ASSIGNMENT);
            if (CheckUtility.isNullOrEmpty(mngAssignment)) {
                mngAssignment = "0";
            } else {
                mngAssignment = mngAssignment.replace("\"", "");
            }

            String scheduleControlInfo = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SCHEDULE_CONTROL_INFO);
            if (CheckUtility.isNullOrEmpty(scheduleControlInfo)) {
                scheduleControlInfo = "1";
            } else {
                scheduleControlInfo = scheduleControlInfo.replace("\"", "");
            }

            // コマンド設定
            String command = null;
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)) {
                command = "X0" + SmControlConstants.SCHEDULE_CONTROL_INFO_1_CMD;
            } else if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
                command = "X0" + SmControlConstants.SCHEDULE_CONTROL_INFO_2_CMD;
            } else {
                // Not Process
            }

            for (int cnt = 0; cnt < fvpResList.size(); cnt++) {
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(cnt);
                if (smId.equals(fvpRes.getSmId())) {
                    A200059Param res = (A200059Param) fvpRes.getParam();

                    if (OsolApiResultCode.API_OK.equals(fvpResList.get(cnt).getFvpResultCd())) {
                        // 取得したデータを設定データとして複製
                        BeanUtils.copyProperties(oldParam, res);
                        // BeanUtils.copyProperties処理にて Commandが設定される為、Commandをnullにする
                        oldParam.setCommand(null);

                        // 更新データを設定
                        BeanUtils.copyProperties(newParam, oldParam);
                        newParam.setScheduleControl(mngAssignment);
                        newParam.setScheduleControlInfo(scheduleControlInfo);
                        newParam.setUpdateDBflg(true);
                        newParam.setCommand(command);
                    }
                    break;
                }
            }

            oldParamList.add(oldParam);
            retList.add(newParam);

            // DBフラグを保持
            this.updateDBflgListSchedule.add(newParam.isUpdateDBflg());
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(設定)用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkScheduleUpdateEa(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // 対応機器チェック
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }

                // listサイズチェック
                List<Map<String, Object>> loadList = ((A200060Param) resultData.getParam()).getLoadList();
                if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_E_ALPHA)) ||
                    (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd()) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_E_ALPHA_2))
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("loadList.size()=%d", loadList.size()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }

                for (Map<String, Object> load : loadList) {
                    Object strSettingMonthScheduleList = load.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_MONTH_SCHEDULE_LIST);
                    List<Map<String,String>> mmScheduleList = new Gson().fromJson(String.valueOf(strSettingMonthScheduleList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                    if (mmScheduleList.size() != SmControlConstants.SCHEDULE_LOAD_LIST_MM_SCHEDULELIST) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("mmScheduleList.size()=%d", mmScheduleList.size()));
                        resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    }
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得) Eα 実行
     *
     * @param parameterList
     * @param resultDataList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkScheduleSelectEa(List<Map<String,String>> parameterList, List<BulkCtrlResultData> resultDataList) throws SmControlException {
        // Paramリストを生成
        List<A200059Param> paramList = createParamListForBulkScheduleSelectEa(parameterList);

        // 生成したParamリストからバリデーションを実行
        validationBulkCtrl(paramList, null, resultDataList);

        // 機器情報を取得
        getSmInfo(resultDataList);

        // 建物IDリストを取得
        getBuildingIdList(resultDataList);

        // 固有のバリデーションを実行
        validationBulkScheduleEa(resultDataList);

        // 機器通信リクエスト生成
        List<FvpCtrlMngRequest<BaseParam>> fvpReqList = createFvpReqList(resultDataList);

        // 機器制御を実行し、結果を返却
        return executeBulkFvpCtrl(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得) Eα 用 Paramリスト生成
     *
     * @param parameterList
     * @return Paramリスト
     *
     */
    private List<A200059Param> createParamListForBulkScheduleSelectEa(List<Map<String,String>> parameterList) {
        List<A200059Param> paramList = new ArrayList<>();

        for (Map<String, String> parameter : parameterList) {
            A200059Param param = new A200059Param();

            String settingChangeHist = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_CHANGE_HIST);
            String scheduleControlInfo = parameter.get(SmControlConstants.BULK_CTRL_PARAM_SCHEDULE_CONTROL_INFO);
            boolean updateDBflg = Boolean.valueOf(parameter.get(SmControlConstants.BULK_CTRL_PARAM_UPDATE_DB_FLG));

            if (CheckUtility.isNullOrEmpty(scheduleControlInfo)) {
                scheduleControlInfo = "1";
            }
            if (CheckUtility.isNullOrEmpty(settingChangeHist)) {
                settingChangeHist = "0";
            }

            // コマンド設定
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)) {
                param.setCommand("V" + settingChangeHist + SmControlConstants.SCHEDULE_CONTROL_INFO_1_CMD);
            } else if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
                param.setCommand("V" + settingChangeHist + SmControlConstants.SCHEDULE_CONTROL_INFO_2_CMD);
            } else {
                // Not Process
            }

            param.setSettingChangeHist(settingChangeHist);
            param.setScheduleControlInfo(scheduleControlInfo);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }

    /**
     * 複数建物・テナント一括 スケジュール(取得) Eα 用 バリデーション
     *
     * @param resultDataList
     * @return バリデーション結果
     *
     */
    private boolean validationBulkScheduleEa(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            // レコード処理結果にエラーがある場合はチェックはしない
            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                SmPrmResultData smPrm = resultData.getSmPrm();
                if (!SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd()) &&
                    !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())
                    ) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("PRODUCT_CD=%s", smPrm.getProductCd()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }

                String scheduleControlInfo = ((A200059Param)resultData.getParam()).getScheduleControlInfo();
                if (!SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)
                        && !SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format("scheduleControlInfo=%s", scheduleControlInfo));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                }
            }
        }

        return true;
    }

    /**
     * 複数建物・テナント一括 スケジュール用 DB更新
     *
     * @param fvpResList
     * @return
     *
     */
    private void updateDbBulkSchedule(List<FvpCtrlMngResponse<BaseParam>> fvpResList){

        for (int i = 0; i < fvpResList.size(); i++) {

            // DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
            if(!this.updateDBflgListSchedule.get(i)
                    || !(fvpResList.get(i).getFvpResultCd().equals(OsolApiResultCode.API_OK))){
                continue;
            }

            FvpCtrlMngResponse<BaseParam> fvpRes = (FvpCtrlMngResponse<BaseParam>) fvpResList.get(i);

            // dao呼出
            try {
                dao.updateSchedule(fvpRes,this.loginUserId);
            } catch (Exception e) {
                // 更新エラー時はfvpResListの結果コードに例外を格納
                fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
        }
    }

    /**
     * 複数建物・テナント一括 スケジュール用 DB更新 (Eα用)
     *
     * @param fvpResList
     */
    private void updateDbBulkScheduleEa(List<FvpCtrlMngResponse<BaseParam>> fvpResList){

        for (int i = 0; i < fvpResList.size(); i++) {

            // DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
            if (!this.updateDBflgListSchedule.get(i)
                    || !(fvpResList.get(i).getFvpResultCd().equals(OsolApiResultCode.API_OK))){
                continue;
            }

            FvpCtrlMngResponse<BaseParam> fvpRes = (FvpCtrlMngResponse<BaseParam>) fvpResList.get(i);

            // dao呼出
            try {
                dao.updateScheduleEa(fvpRes,this.loginUserId);
            } catch (Exception e) {
                // 更新エラー時はfvpResListの結果コードに例外を格納
                fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
        }
    }

    /**
     * 複数建物・テナント一括 スケジュール用 メール内容設定
     *
     * @param fvpResList
     * @param resultDataList
     * @return targetList
     *
     */
    private List<SmControlVerocityResult> mailBodySettingSchedule(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        // 検索用リスト生成
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        for (int i = 0; i < fvpResList.size(); i++) {
            List<String> buildingIdList = resultDataList.get(i).getBuildingIdList();
            for (String buildingId : buildingIdList) {
                // メールAPI用 Resultクラス
                SmControlVerocityResult target = new SmControlVerocityResult();

                // 設定情報レコード取得
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
                // 機器IDが未存在でエラーの場合、処理しない
                if(fvpRes.getSmId() == null ) {
                    continue;
                }

                // リクエスト新設定情報レコード取得
                A200005Param newRecord = this.newDataListSchedule.get(i);
                // リクエスト旧設定情報レコード取得
                A200005Param oldRecord = this.oldDataListSchedule.get(i);
                // 新旧設定情報のどちらかでも存在しない場合スキップ
                if (oldRecord == null || newRecord == null) {
                    continue;
                }

                target.setSmId(fvpRes.getSmId());
                target.setCorpId(this.loginCorpId);
                target.setPersonId(String.valueOf(this.loginPersonId));
                target.setSmAddress(fvpRes.getSmAddress());
                target.setIpAddress(fvpRes.getIpAddress());
                target.setCommand(SmControlConstants.BULK_SCHEDULE_COMMAND);
                target.setBuildingId(buildingId);
                // 設定前後情報格納

                if (oldRecord != null && newRecord != null) {
                    // スケジュール管理
                    if (oldRecord.getScheduleManageAssignment() == null || oldRecord.getScheduleManageAssignment().isEmpty()) {
                        target.setOldData("－");
                    }else if (oldRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
                        target.setOldData(SmControlConstants.NUM_TO_WORD_NO_SET);
                    }else if(oldRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
                        target.setOldData(SmControlConstants.NUM_TO_WORD_WITH_SET);
                    }
                    if (newRecord.getScheduleManageAssignment() == null || newRecord.getScheduleManageAssignment().isEmpty()) {
                        target.setNewData("－");
                    }else if (newRecord.getScheduleManageAssignment().equals( SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
                        target.setNewData(SmControlConstants.NUM_TO_WORD_NO_SET);
                    }else if(newRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
                        target.setNewData(SmControlConstants.NUM_TO_WORD_WITH_SET);
                    }
                }

                // 新旧レコードチェック
                if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
                    targetList.add(target);
                    continue;
                }
                // 例外が発生している場合はRECORD_NG
                else if (!(fvpRes.getFvpResultCd().equals(OsolApiResultCode.API_OK))) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NG);
                    targetList.add(target);
                    continue;
                }

                target.setResult(SmControlConstants.MAIL_SETTING_OK);
                targetList.add(target);
            }
        }
        return targetList;
    }

    /**
     * 複数建物・テナント一括 スケジュール用 Eα メール内容設定
     *
     * @param fvpResList
     * @param resultDataList
     * @return
     */
    private List<SmControlVerocityResult> mailBodySettingScheduleEa(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        // 検索用リスト生成
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        for (int i = 0; i < fvpResList.size(); i++) {
            List<String> buildingIdList = resultDataList.get(i).getBuildingIdList();
            for (String buildingId : buildingIdList) {
                // メールAPI用 Resultクラス
                SmControlVerocityResult target = new SmControlVerocityResult();

                // 設定情報レコード取得
                FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
                // 機器IDが未存在でエラーの場合、処理しない
                if(fvpRes.getSmId() == null ) {
                    continue;
                }

                // リクエスト新設定情報レコード取得
                A200060Param newRecord = this.newDataListScheduleEa.get(i);
                // リクエスト旧設定情報レコード取得
                A200060Param oldRecord = this.oldDataListScheduleEa.get(i);
                // 新旧設定情報のどちらかでも存在しない場合スキップ
                if (oldRecord == null || newRecord == null) {
                    continue;
                }

                target.setSmId(fvpRes.getSmId());
                target.setCorpId(this.loginCorpId);
                target.setPersonId(String.valueOf(this.loginPersonId));
                target.setSmAddress(fvpRes.getSmAddress());
                target.setIpAddress(fvpRes.getIpAddress());
                target.setCommand(SmControlConstants.BULK_SCHEDULE_COMMAND);
                target.setBuildingId(buildingId);
                // 設定前後情報格納

                if (oldRecord != null && newRecord != null) {
                    // スケジュール管理
                    if (oldRecord.getScheduleControl() == null || oldRecord.getScheduleControl().isEmpty()) {
                        target.setOldData("－");
                    }else if (oldRecord.getScheduleControl().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
                        target.setOldData(SmControlConstants.NUM_TO_WORD_NO_SET);
                    }else if(oldRecord.getScheduleControl().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
                        target.setOldData(SmControlConstants.NUM_TO_WORD_WITH_SET);
                    }
                    if (newRecord.getScheduleControl() == null || newRecord.getScheduleControl().isEmpty()) {
                        target.setNewData("－");
                    }else if (newRecord.getScheduleControl().equals( SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
                        target.setNewData(SmControlConstants.NUM_TO_WORD_NO_SET);
                    }else if(newRecord.getScheduleControl().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
                        target.setNewData(SmControlConstants.NUM_TO_WORD_WITH_SET);
                    }
                }

                // 新旧レコードチェック
                if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
                    targetList.add(target);
                    continue;
                }
                // 例外が発生している場合はRECORD_NG
                else if (!(fvpRes.getFvpResultCd().equals(OsolApiResultCode.API_OK))) {
                    target.setResult(SmControlConstants.MAIL_SETTING_NG);
                    targetList.add(target);
                    continue;
                }

                target.setResult(SmControlConstants.MAIL_SETTING_OK);
                targetList.add(target);
            }
        }
        return targetList;
    }

    //----------------------------------------------------------------------------------------------------------------
    //
    // 複数建物・テナント一括 制御 ユーティリティ
    //
    //----------------------------------------------------------------------------------------------------------------

    /**
     * JSON文字列からParameterリストを取得
     *
     * @param jsonString
     * @param resultDataList
     * @return
     */
    private List<Map<String,String>> getParameterListFromJson(String jsonString, List<BulkCtrlResultData> resultDataList) {
        Type t = new TypeToken<List<Map<String, JsonElement>>>(){}.getType();
        List<Map<String, JsonElement>> paramList = new Gson().fromJson(jsonString, t);
        List<Map<String,String>> retList = new ArrayList<>();

        for (Map<String, JsonElement> map : paramList) {
            HashMap<String,String> parameter = new HashMap<>();
            for (Entry<String, JsonElement> entry : map.entrySet()) {
                parameter.put(entry.getKey(), entry.getValue().toString());
            }
            retList.add(parameter);

            // 各レコードの処理結果保持インスタンス作成
            BulkCtrlResultData resultData = new BulkCtrlResultData();
            resultData.setRecordResult(OsolApiResultCode.API_OK);
            try {
                resultData.setSmId(Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID)));
            } catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            }
            resultData.setParameter(parameter);
            resultDataList.add(resultData);
        }

        return retList;
    }

    /**
     * JSON文字列からParameterリストを取得 (Eα系の機種と分割)
     *
     * @param jsonString
     * @param resultDataList
     * @return
     */
    private List<Map<String,String>> getParameterListFromJson(String jsonString, List<BulkCtrlResultData> resultDataList, List<BulkCtrlResultData> resultDataListEa) {
        Type t = new TypeToken<List<Map<String, JsonElement>>>(){}.getType();
        List<Map<String, JsonElement>> paramList = new Gson().fromJson(jsonString, t);
        List<Map<String,String>> retList = new ArrayList<>();

        for (Map<String, JsonElement> map : paramList) {
            HashMap<String,String> parameter = new HashMap<>();
            for (Entry<String, JsonElement> entry : map.entrySet()) {
                parameter.put(entry.getKey(), entry.getValue().toString());
            }
            retList.add(parameter);

            // 各レコードの処理結果保持インスタンス作成
            BulkCtrlResultData resultData = new BulkCtrlResultData();
            resultData.setRecordResult(OsolApiResultCode.API_OK);
            try {
                resultData.setSmId(Long.parseLong(parameter.get(SmControlConstants.BULK_CTRL_PARAM_SMID)));
            } catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            }
            resultData.setParameter(parameter);


            // SM IDより機器情報を取得
            SmPrmResultData smPrm = null;
            try {
                smPrm = this.dao.findSmPrm(resultData.getSmId());
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            } catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            } finally {
                // エラーの場合でも空の機器情報をセットする。
                if (smPrm == null) {
                    smPrm = new SmPrmResultData();
                }
            }

            // 製品コードを判定し、ParamListを分割
            if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd())
                    || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())) {
                resultDataListEa.add(resultData);
            } else {
                resultDataList.add(resultData);
            }
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 制御 バリデーション
     *
     * @param paramList
     * @param oldParamList
     * @param resultDataList
     * @param type
     * @return バリデーション結果
     *
     */
    private void validationBulkCtrl(List<? extends BaseParam> paramList, List<? extends BaseParam> oldParamList, List<BulkCtrlResultData> resultDataList) {
        Iterator<BulkCtrlResultData> itr = resultDataList.iterator();
        int index = 0;

        for (BaseParam param : paramList) {
            BulkCtrlResultData resultData = itr.next();

            resultData.setParam(param);
            if (param == null) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" smId=%s:param=null", resultData.getSmId()));
                resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                continue;
            } else {
                // Beanバリデーション
                Set<?> validateErrorSet = validator.validate(param);
                if (validateErrorSet.size() > 0) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" smId=%s:validate=%s", resultData.getSmId(), validateErrorSet.toString()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }
            }

            // 新旧設定情報チェック(設定系のみ)
            if (oldParamList != null) {
                index = paramList.indexOf(param);
                BaseParam oldParam = oldParamList.get(index);
                if (param.partDataComparison(oldParam)) {
                    // 設定情報が等しい場合は通信しない
                    resultData.setRecordResult(SmControlConstants.RECORD_NO_CHANGE);
                }
            }
        }
    }

    /**
     * 複数建物・テナント一括 制御 バリデーション（履歴用）
     *
     * @param paramList
     * @param oldParamList
     * @param resultDataList
     * @param type
     * @return バリデーション結果
     *
     */
    private void validationBulkCtrlHist(List<? extends BaseParam> paramList, List<? extends BaseParam> oldParamList, List<BulkCtrlResultData> resultDataList) {
        Iterator<BulkCtrlResultData> itr = resultDataList.iterator();
        int index = 0;

        for (BaseParam param : paramList) {
            BulkCtrlResultData resultData = itr.next();

            resultData.setParam(param);
            if (param == null) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" smId=%s:param=null", resultData.getSmId()));
                resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                continue;
            } else {
                // Beanバリデーション
                Set<?> validateErrorSet = validator.validate(param);
                if (validateErrorSet.size() > 0) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), String.format(" smId=%s:validate=%s", resultData.getSmId(), validateErrorSet.toString()));
                    resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                    continue;
                }
            }

            // 新旧設定情報チェック(設定系のみ)
            if (oldParamList != null) {
                index = paramList.indexOf(param);
                BaseParam oldParam = oldParamList.get(index);
                if (param.partDataComparison(oldParam, param)) {
                    // 設定情報が等しい場合は通信しない
                    resultData.setRecordResult(SmControlConstants.RECORD_NO_CHANGE);
                }
            }
        }
    }

    /**
     * 複数建物・テナント一括 制御 機器情報取得
     *
     * @param resultDataList
     *
     */
    private void getSmInfo(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            Long smId = resultData.getSmId();
            SmPrmResultData smPrm = null;

            try {
                // 機器IDが正しく取得できていれば、機器情報取得
                if (smId != null) {
                    smPrm = this.dao.findSmPrm(smId);
                }
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                resultData.setRecordResult(e.getErrorCode());
            } catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                throw e;
            } finally {
                // エラーの場合でも空の機器情報をセットする。
                if (smPrm == null) {
                    smPrm = new SmPrmResultData();
                }
                smPrm.setSmId(smId);
                resultData.setSmPrm(smPrm);
            }
        }
    }

    /**
     * 複数建物・テナント一括 制御 建物IDリスト取得
     *
     * @param resultDataList
     *
     */
    private void getBuildingIdList(List<BulkCtrlResultData> resultDataList) {
        for (BulkCtrlResultData resultData : resultDataList) {
            Map<String, String> parameter = resultData.getParameter();
            String strBuildingList = parameter.get(SmControlConstants.BULK_CTRL_PARAM_BUILDING_LIST);
            String tempBuildingList = strBuildingList.replace("]", "").replace("[", "");
            List<String> buildingList = Arrays.asList(tempBuildingList.split(","));

            resultData.setBuildingIdList(buildingList);
        }
    }

    /**
     * 複数建物・テナント一括 制御 機器通信リクエスト生成
     *
     * @param resultDataList
     * @return リクエストリスト
     *
     */
    private List<FvpCtrlMngRequest<BaseParam>> createFvpReqList(List<BulkCtrlResultData> resultDataList) {
        List<FvpCtrlMngRequest<BaseParam>> retList = new ArrayList<>();

        for (BulkCtrlResultData resultData : resultDataList) {
            FvpCtrlMngRequest<BaseParam> fvpReq = null;
            fvpReq = new FvpCtrlMngRequest<>(resultData.getSmPrm(),
                                            this.loginCorpId,
                                            this.loginPersonId,
                                            this.loginUserId);

            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // FvpCtrlMngRequest作成
                fvpReq.setParam(resultData.getParam());
            } else {
                // エラーレコードにエラー内容をセット
                fvpReq.setCommandCd(resultData.getRecordResult());
            }
            retList.add(fvpReq);
        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 制御 機器通信リクエスト生成（履歴取得用）
     *
     * @param resultDataList
     * @return リクエストリスト
     *
     */
    private List<FvpCtrlMngRequest<BaseParam>> createFvpReqListHist(List<BulkCtrlResultData> resultDataList, String paramClassName) {
        List<FvpCtrlMngRequest<BaseParam>> retList = new ArrayList<>();


        // パラメータから履歴情報が取得できない場合は、履歴取得を行わない
        for (BulkCtrlResultData resultData : resultDataList) {

            // デマンド(取得)
            if (SmControlConstants.BULK_CTRL_CAST_TARGET_POWER_SELECT.equals(paramClassName)) {
                A200006Param param = (A200006Param) resultData.getParam();
                if (param == null || param.getSettingChangeHist() == null) continue;
            }
            // デマンド(取得) Eα
            else if (SmControlConstants.BULK_CTRL_CAST_TARGET_POWER_SELECT_EA.equals(paramClassName)) {
                A200049Param param = (A200049Param) resultData.getParam();
                if (param == null || param.getSettingChangeHist() == null) continue;
            }
            // 温度制御(取得)
            else if (SmControlConstants.BULK_CTRL_CAST_TEMPERATURE_CTRL_SELECT.equals(paramClassName)) {
                A200041Param param = (A200041Param) resultData.getParam();
                if (param == null || param.getSettingChangeHist() == null) continue;
            }

            FvpCtrlMngRequest<BaseParam> fvpReq = null;
            fvpReq = new FvpCtrlMngRequest<>(resultData.getSmPrm(),
                                            this.loginCorpId,
                                            this.loginPersonId,
                                            this.loginUserId);

            if (OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
                // FvpCtrlMngRequest作成
                fvpReq.setParam(resultData.getParam());
            } else {
                // エラーレコードにエラー内容をセット
                fvpReq.setCommandCd(resultData.getRecordResult());
            }
            retList.add(fvpReq);

        }

        return retList;
    }

    /**
     * 複数建物・テナント一括 制御 機器制御処理
     *
     * @param fvpReqList
     * @return 実行結果
     * @throws SmControlException
     *
     */
    private List<FvpCtrlMngResponse<BaseParam>> executeBulkFvpCtrl(List<FvpCtrlMngRequest<BaseParam>> fvpReqList) throws SmControlException {
        return fvpCtrlMngClient.excute(fvpReqList);
    }

    /**
     * 複数建物・テナント一括 制御 結果判定
     *
     * @param fvpResList
     * @param resultDataList
     * @return 判定結果
     *
     */
    private String isExecuteResult(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList) {
        String ret = OsolApiResultCode.API_OK;
        int errCount = 0;
        Iterator<BulkCtrlResultData> itr = resultDataList.iterator();

        for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResList) {
            BulkCtrlResultData resultData = itr.next();

            if (SmControlConstants.RECORD_NO_CHANGE.equals(resultData.getRecordResult())) {
                continue;
            } else if (!OsolApiResultCode.API_OK.equals(resultData.getRecordResult()) || !OsolApiResultCode.API_OK.equals(fvpRes.getFvpResultCd())) {
                errCount++;
            } else {
                // NOT PROCESS
            }
        }

        if (errCount != 0) {
            if (errCount == resultDataList.size()) {
                // 全て異常
                ret = OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTFAILED;
            } else {
                // 正常/異常レコード混在
                ret = OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTWARN;
            }
        }
        return ret;
    }

    /**
     * ParamListをEa/Ea2用とその他用に分割
     *
     * @param baseList
     * @param paramList
     * @param paramListEa
     */
    private boolean splitParamListBySmEa(List<Map<String,String>> baseList, List<Map<String,String>> paramList, List<Map<String,String>> paramListEa) {
        for (Map<String, String> baseParam : baseList) {
            Long smId = Long.parseLong(baseParam.get(SmControlConstants.BULK_CTRL_PARAM_SMID));
            SmPrmResultData smPrm = null;

            // SM IDより機器情報を取得
            try {
                smPrm = this.dao.findSmPrm(smId);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                return false;
            } catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                return false;
            } finally {
                // エラーの場合でも空の機器情報をセットする。
                if (smPrm == null) {
                    smPrm = new SmPrmResultData();
                }
            }

            // 製品コードを判定し、ParamListを分割
            if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd())
                    || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())) {
                paramListEa.add(baseParam);
            } else {
                paramList.add(baseParam);
            }
        }

        return true;
    }


    /**
     * 一括制御の各レコード処理結果
     *
     * @author hayama
     *
     */
    private class BulkCtrlResultData extends BulkApiResultData<BaseSmControlApiResult> {

        private Long smId;

        public Long getSmId() {
            return smId;
        }

        public void setSmId(Long smId) {
            this.smId = smId;
        }

        private Map<String,String> parameter;

        private BaseParam param;

        private SmPrmResultData smPrm;

        private List<String> buildingIdList;

        public Map<String, String> getParameter() {
            return parameter;
        }

        public void setParameter(Map<String, String> parameter) {
            this.parameter = parameter;
        }

        public BaseParam getParam() {
            return param;
        }

        public void setParam(BaseParam param) {
            this.param = param;
        }

        public SmPrmResultData getSmPrm() {
            return smPrm;
        }

        public void setSmPrm(SmPrmResultData smPrm) {
            this.smPrm = smPrm;
        }

        public List<String> getBuildingIdList() {
            return buildingIdList;
        }

        public void setBuildingIdList(List<String> buildingIdList) {
            this.buildingIdList = buildingIdList;
        }
    }


    /**
     * 複数建物・テナント一括 制御 メール内容生成
     *
     * @param fvpResList
     * @param resultDataList
     * @param commandCd
     * @return
     */
    private List<SmControlVerocityResult> createSendMailTargetList(List<FvpCtrlMngResponse<BaseParam>> fvpResList, List<BulkCtrlResultData> resultDataList, String commandCd) {
        List<SmControlVerocityResult> targetList = new ArrayList<>();

        // 機能を判別
        if(commandCd.equals(SmControlConstants.DEMAND_COMMAND) ) {
            // 目標電力設定 メール内容設定
            targetList = mailBodySettingTargetPower(fvpResList, resultDataList);
        } else if(commandCd.equals(SmControlConstants.SCHEDULE_COMMAND) ) {
            // スケジュール設定 メール内容設定
            targetList = mailBodySettingSchedule(fvpResList, resultDataList);
        } else if(commandCd.equals(SmControlConstants.BULK_TEMPERATURE_COMMAND) ) {
            // 温度制御設定 メール内容設定
            targetList = mailBodySettingTemperatureCtrl(fvpResList, resultDataList);
        } else if (commandCd.equals(SmControlConstants.DEMAND_COMMAND_E_ALPHA)) {
            targetList = mailBodySettingTargetPowerEa(fvpResList, resultDataList);
        } else if(commandCd.equals(SmControlConstants.SCHEDULE_UPDATE_COMMAND_E_ALPHA) ) {
            targetList = mailBodySettingScheduleEa(fvpResList, resultDataList);
        }

        return targetList;
    }

    /**
     * 複数建物・テナント一括 制御 メール送信
     *
     * @param targetList
     * @param command
     */
    private void sendMailBulk(List<SmControlVerocityResult> targetList, String command) {
        // メール送信API呼出
        try {
            if (command.equals(SmControlConstants.BULK_TEMPERATURE_COMMAND)) {
                // 温度制御設定
                bulkAPIMailSendCallUtility.bulkMailSend(targetList , command , SETTING_CONDITON);
            } else {
                bulkAPIMailSendCallUtility.bulkMailSend(targetList , command);
            }
        } catch (Exception e) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(),st.getMethodName(),st.getLineNumber(),"メール送信に失敗しました");
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

    }

    /**
     * 差分計算
     * @param productCd 製品コード
     * @param settingCtrlValue 差分計算元の値
     * @param tempDiff 差分値
     * @param tempDiffSign 0：+、1：-
     * @param controlType 0：温度制御、1：冷房設定、2：暖房設定
     * @return
     */
    private String getDiffCalcValue(String productCd, String settingCtrlValue, String tempDiff, String tempDiffSign, String controlType) {
        String ret = settingCtrlValue;

        switch (productCd) {
            case SmControlConstants.PRODUCT_CD_FV2 :
            case SmControlConstants.PRODUCT_CD_FVP_D :
            case SmControlConstants.PRODUCT_CD_FVP_ALPHA_D :
                ret = calcTempDiff(settingCtrlValue, tempDiff, tempDiffSign, controlType);
                break;

            case SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2 :
                ret = calcTempDiffEa(settingCtrlValue, tempDiff, tempDiffSign, controlType);
                break;

            case SmControlConstants.PRODUCT_CD_E_ALPHA :
            case SmControlConstants.PRODUCT_CD_E_ALPHA_2 :
                ret = calcTempDiffEa(settingCtrlValue, tempDiff, tempDiffSign, controlType);
                break;

            default :
                ret = null;
                break;
        }
        return ret;
    }

    /**
     * 差分計算 FV2/FVP(D)/FVPα(D)
     * @param settingCtrlValue
     * @param tempDiff
     * @param tempDiffSign
     * @param controlType
     * @return
     */
    private String calcTempDiff(String settingCtrlValue, String tempDiff, String tempDiffSign, String controlType) {
        String ret = null;
        String[] coolerRange = {"20.0", "40.0"};
        String[] heaterRange = {"10.0", "30.0"};
        BigDecimal target = new BigDecimal(settingCtrlValue);
        BigDecimal diff = new BigDecimal(tempDiff);

        if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.PLUS.getVal().equals(tempDiffSign)) {
            // 温度を上げる
            target = target.add(diff);
            ret = target.toString();

            if (SmControlConstants.COOLER.equals(controlType)) {
                // 冷房設定
                if (target.compareTo(new BigDecimal(coolerRange[1])) > 0) {
                    // 上限値を超えていた場合、上限値を設定
                    ret = coolerRange[1];
                }
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // 暖房設定
                if (target.compareTo(new BigDecimal(heaterRange[1])) > 0) {
                    // 上限値を超えていた場合、上限値を設定
                    ret = heaterRange[1];
                }
            }
        } else if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.MINUS.getVal().equals(tempDiffSign)) {
            // 温度を下げる
            target = target.subtract(diff);
            ret = target.toString();

            if (SmControlConstants.COOLER.equals(controlType)) {
                // 冷房設定
                if (target.compareTo(new BigDecimal(coolerRange[0])) < 0) {
                    // 下限値を未満の場合、下限値を設定
                    ret = coolerRange[0];
                }
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // 暖房設定
                if (target.compareTo(new BigDecimal(heaterRange[0])) < 0) {
                    // 下限値を未満の場合、下限値を設定
                    ret = heaterRange[0];
                }
            }
        }

        return new BigDecimal(ret).setScale(0, BigDecimal.ROUND_DOWN).toString();
    }

    /**
     * 差分計算 G2/Eα/Eα２
     * @param settingCtrlValue
     * @param tempDiff
     * @param tempDiffSign
     * @param controlType
     * @return
     */
    private String calcTempDiffEa(String settingCtrlValue, String tempDiff, String tempDiffSign, String controlType) {
        String ret = null;
        String[] range  = {"00.0", "99.9"};
        BigDecimal target = new BigDecimal(settingCtrlValue).scaleByPowerOfTen(-1);
        BigDecimal diff = new BigDecimal(tempDiff).scaleByPowerOfTen(-1);

        if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.PLUS.getVal().equals(tempDiffSign)) {
            // 温度を上げる
            target = target.add(diff);
            ret = target.toString();

            if (SmControlConstants.COOLER.equals(controlType)) {
                // 冷房設定
                if (target.compareTo(new BigDecimal(range[1])) > 0) {
                    // 上限値を超えていた場合、上限値を設定
                    ret = range[1];
                }
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // 暖房設定
                if (target.compareTo(new BigDecimal(range[1])) > 0) {
                    // 上限値を超えていた場合、上限値を設定
                    ret = range[1];
                }
            }
        } else if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.MINUS.getVal().equals(tempDiffSign)) {
            // 温度を下げる
            target = target.subtract(diff);
            ret = target.toString();

            if (SmControlConstants.COOLER.equals(controlType)) {
                // 冷房設定
                if (target.compareTo(new BigDecimal(range[0])) < 0) {
                    // 下限値を未満の場合、下限値を設定
                    ret = range[0];
                }
            } else if (SmControlConstants.HEATER.equals(controlType)) {
                // 暖房設定
                if (target.compareTo(new BigDecimal(range[0])) < 0) {
                    // 下限値を未満の場合、下限値を設定
                    ret = range[0];
                }
            }
        }

        ret = ret.replace(".", "");
        if (ret.length() == 2) {
            // 3桁に合わせる
            ret = ret + "0";
        }
        return ret;
    }

    /**
     * 差分計算 目標電力
     * @param settingCtrlValue
     * @param tempDiff
     * @param tempDiffSign
     * @param controlType
     * @return
     */
    private String calcTargetPowerDiff(String settingCtrlValue, String targetPowerDiff, String targetPowerDiffSign) {
        String ret = null;
        String[] range  = {"1", "9999"};
        BigDecimal target = new BigDecimal(settingCtrlValue);
        BigDecimal diff = new BigDecimal(targetPowerDiff);

        if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.PLUS.getVal().equals(targetPowerDiffSign)) {
            // +差分
            target = target.add(diff);
            ret = target.toString();

            if (target.compareTo(new BigDecimal(range[1])) > 0) {
                // 上限値を超えていた場合、上限値を設定
                ret = range[1];
            }
        } else if (SmControlConstants.BULK_CTRL_TEMP_DIFF_SIGN.MINUS.getVal().equals(targetPowerDiffSign)) {
            // -差分
            target = target.subtract(diff);
            ret = target.toString();

            if (target.compareTo(new BigDecimal(range[0])) < 0) {
                // 下限値を未満の場合、下限値を設定
                ret = range[0];
            }
        }

        ret = ret.replace(".", "");
        if (ret.length() < 4) {
            // 4桁に合わせる
            ret = toNumParam(ret, 4);

        }
        return ret;
    }
}
