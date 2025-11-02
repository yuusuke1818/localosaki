package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.UnitSelectEaDao;
import jp.co.osaki.osol.api.parameter.smcontrol.UnitSelectEaParameter;
import jp.co.osaki.osol.api.result.smcontrol.SmPointResult;
import jp.co.osaki.osol.api.result.smcontrol.UnitSelectEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200053Param;
import jp.co.osaki.osol.mng.param.A200055Param;
import jp.co.osaki.osol.mng.param.BaseParam;


/**
*
* ユニット(取得) Eα Bean クラス
*
* @autho t_hayama
*
*/
@Named(value = SmControlConstants.UNIT_SELECT_EA)
@RequestScoped
public class UnitSelectEaBean extends AbstractApiBean<UnitSelectEaResult, UnitSelectEaParameter> {

    @EJB
    private UnitSelectEaDao dao;

    /**
     * 機器情報
     */
    private SmPrmResultData smPrm;


    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(UnitSelectEaParameter parameter) {
        A200053Param param = new A200053Param();

        if (parameter != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        this.smPrm = smPrm;

        return true;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        //if DBフラグがOFFなら処理無し
        if (!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // Paramクラス変更
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200053Param> res = (FvpCtrlMngResponse<A200053Param>) response;
        A200053Param param = res.getParam();

        // ユニット(取得) Eαを実行し、アドレスリストを取得
        A200053Param resUnitSelectEa = execUnitSelectEa(param.getProductCd(), param.getAddress());
        List<Map<String, String>> addressList = resUnitSelectEa.getAddressList();
        if (addressList == null || addressList.isEmpty()) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "addressList", "null or empty");
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
        }

        // 計測ポイント設定 1情報を取得し、計測ポイント情報リストを取得
        A200055Param resMeasurePointSelect1 = execMeasurePointSelect(param.getProductCd(), param.getAddress(), SmControlConstants.MEASURE_POINT_INFO_1);
        List<Map<String, Object>> measurePointInfo1List = resMeasurePointSelect1.getMeasurePointInfoList();
        if (measurePointInfo1List == null || measurePointInfo1List.isEmpty()) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "measurePointInfo1List", "null or empty");
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
        }

        // 計測ポイント設定 2情報を取得し、計測ポイント情報リストを取得
        A200055Param resMeasurePointSelect2 = execMeasurePointSelect(param.getProductCd(), param.getAddress(), SmControlConstants.MEASURE_POINT_INFO_2);
        List<Map<String, Object>> measurePointInfo2List = resMeasurePointSelect2.getMeasurePointInfoList();
        if (measurePointInfo2List == null || measurePointInfo2List.isEmpty()) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "measurePointInfo2List", "null or empty");
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
        }

        // 取得した情報を基に、DB登録用のデータを生成
        // 計測ポイント設定 1情報
        List<SmPointResult> smPointResultList = createSmPointResultList(res.getSmId(), measurePointInfo1List, addressList, 1);
        if (smPointResultList == null || smPointResultList.isEmpty()) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "smPointResultListInfo1", "null or empty");
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
        }
        // 計測ポイント設定 2情報
        List<SmPointResult> smPointResultList2 = createSmPointResultList(res.getSmId(), measurePointInfo2List, addressList, 129);
        if (smPointResultList2 == null || smPointResultList2.isEmpty()) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "smPointResultListInfo2", "null or empty");
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
        }
        // 計測ポイント設定 1情報と2情報を結合
        smPointResultList.addAll(smPointResultList2);

        // dao呼出
        dao.UpdateSmPoint(smPointResultList);
    }

    /**
     * ユニット(取得) Eα 実行
     *
     * @param productCd
     * @param address
     * @return
     * @throws Exception
     */
    private A200053Param execUnitSelectEa(String productCd, String address) throws Exception {
        A200053Param reqParam = new A200053Param();

        // マネージャリクエスト生成
        reqParam.setProductCd(productCd);
        reqParam.setAddress(address);
        FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm, super.apiParameter.getLoginCorpId(), super.apiParameter.getLoginPersonId(), super.loginUserId);
        req.setParam(reqParam);

        // 機器制御
        FvpCtrlMngResponse<BaseParam> res = fvpCtrlMngClient.excute(req);

        return (A200053Param)res.getParam();
    }

    /**
     * 計測ポイント(取得) 実行
     *
     * @param productCd
     * @param address
     * @param measurePointInfo
     * @return
     * @throws Exception
     */
    private A200055Param execMeasurePointSelect(String productCd, String address, String measurePointInfo) throws Exception {
        A200055Param reqParam = new A200055Param();

        // コマンド生成
        if (SmControlConstants.MEASURE_POINT_INFO_1.equals(measurePointInfo)) {
            reqParam.setCommand("V0" + SmControlConstants.MEASURE_POINT_INFO_1_CMD);
        } else if (SmControlConstants.MEASURE_POINT_INFO_2.equals(measurePointInfo)) {
            reqParam.setCommand("V0" + SmControlConstants.MEASURE_POINT_INFO_2_CMD);
        } else {
            return null;
        }

        // マネージャリクエスト生成
        reqParam.setProductCd(productCd);
        reqParam.setAddress(address);
        FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm, super.apiParameter.getLoginCorpId(), super.apiParameter.getLoginPersonId(), super.loginUserId);
        req.setParam(reqParam);

        // 機器制御
        FvpCtrlMngResponse<BaseParam> res = fvpCtrlMngClient.excute(req);

        return (A200055Param)res.getParam();
    }

    /**
     * DB登録用のデータリストを生成
     *
     * @param smId
     * @param measurePointInfoList
     * @param unitAddressList
     * @param pointStartIndx
     * @return
     */
    private List<SmPointResult> createSmPointResultList(Long smId, List<Map<String, Object>> measurePointInfoList, List<Map<String, String>> unitAddressList, int pointStartIndx) {
        List<SmPointResult> ret = new ArrayList<>();
        BigDecimal dmValue = new BigDecimal(SmControlConstants.DM_VALUE);
        BigDecimal offSetValue = new BigDecimal(SmControlConstants.OFF_SET_VALUE);
        BigDecimal factorValue = new BigDecimal(SmControlConstants.FACTOR_VALUE);
        String portNo = null;
        String pointType = null;
        String targetSmPointType = null;
        int pointIndex = pointStartIndx;
        int addressNo = 0;
        int terminalKind = 0;

        for (Map<String, Object> measurePointInfo : measurePointInfoList) {
            // アドレス番号取得
            try {
                addressNo = Integer.parseInt(measurePointInfo.get("addressNo").toString());
            } catch (NumberFormatException e) {
                return null;
            }

            // 不正なアドレス番号が含まれていた場合、処理中断
            if (addressNo > unitAddressList.size()) {
                return null;
            }

            // 対象アドレスの端末種別を取得
            if (addressNo > 0) {
                try {
                    terminalKind = Integer.parseInt(unitAddressList.get(addressNo - 1).get("terminalKind"));
                } catch (NumberFormatException e) {
                    return null;
                }

                // 取得した情報を基にポイント種別を判定
                portNo = measurePointInfo.get("portNo").toString();
                pointType = measurePointInfo.get("pointType").toString();
                targetSmPointType = getTargetSmPointType(terminalKind, portNo, pointType);
                if (targetSmPointType == null) {
                    return null;
                }
            } else {
                targetSmPointType = SmControlConstants.TARGET_SM_POINT_TYPE_0;
            }

            // SmPointResultを生成
            SmPointResult smPointResult = new SmPointResult();
            smPointResult.setSmId(smId);
            smPointResult.setPointNo(String.format("%03d", pointIndex));
            pointIndex++;
            smPointResult.setPointType(targetSmPointType);
            smPointResult.setDmCorrectionFactor(dmValue);
            smPointResult.setAnalogOffSetValue(offSetValue);
            smPointResult.setAnalogConversionFactor(factorValue);
            smPointResult.setUpdateUserId(super.loginUserId);

            ret.add(smPointResult);
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得
     *
     * @param terminalKind
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointType(int terminalKind, String portNo, String pointType) {
        String ret = null;

        switch(terminalKind) {
            case SmControlConstants.TERMINAL_KIND_01:
                ret = getTargetSmPointTypeForInputTerminal(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_02:
                ret = getTargetSmPointTypeForWiredTempSensor(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_03:
                ret = getTargetSmPointTypeForCem4(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_04:
                ret = getTargetSmPointTypeForWirelessUnit(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_05:
                ret = getTargetSmPointTypeForSmallElecEneMonitor(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_06:
                ret = getTargetSmPointTypeForPulseInputModule(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_07:
                ret = getTargetSmPointTypeForAnalogInputModule(portNo, pointType);
                break;
            case SmControlConstants.TERMINAL_KIND_00:
            case SmControlConstants.TERMINAL_KIND_20:
            case SmControlConstants.TERMINAL_KIND_21:
            case SmControlConstants.TERMINAL_KIND_22:
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
                break;
            default:
                // NOT PROCESS
                break;
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (入力端末器用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForInputTerminal(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            if (SmControlConstants.MEASURE_POINT_TYPE_1.equals(pointType) ||
                    SmControlConstants.MEASURE_POINT_TYPE_2.equals(pointType)) {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_P;
            } else if (SmControlConstants.MEASURE_POINT_TYPE_3.equals(pointType)) {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_K;
            } else {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
            }
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (有線温度センサ用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForWiredTempSensor(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_A;
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (CEM4用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForCem4(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else if (SmControlConstants.MEASURE_POINT_PORT_01.equals(portNo) ||
                        SmControlConstants.MEASURE_POINT_PORT_02.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_P;
        } else {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_A;
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (無線環境ユニット用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForWirelessUnit(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            if (SmControlConstants.MEASURE_POINT_TYPE_1.equals(pointType) ||
                    SmControlConstants.MEASURE_POINT_TYPE_2.equals(pointType) ||
                    SmControlConstants.MEASURE_POINT_TYPE_3.equals(pointType)) {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_A;
            } else {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
            }
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (小型電力量モニタ用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForSmallElecEneMonitor(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_P;
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (パルス入力モジュール用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForPulseInputModule(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            if (SmControlConstants.MEASURE_POINT_TYPE_1.equals(pointType)) {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_P;
            } else if (SmControlConstants.MEASURE_POINT_TYPE_3.equals(pointType)) {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_K;
            } else {
                ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
            }
        }

        return ret;
    }

    /**
     * 対象のポイント種別を取得 (アナログ入力モジュール用)
     *
     * @param portNo
     * @param pointType
     * @return
     */
    private String getTargetSmPointTypeForAnalogInputModule(String portNo, String pointType) {
        String ret = null;

        if (SmControlConstants.MEASURE_POINT_PORT_00.equals(portNo)) {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_0;
        } else {
            ret = SmControlConstants.TARGET_SM_POINT_TYPE_A;
        }

        return ret;
    }

}
