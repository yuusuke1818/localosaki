package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.TemperatureCtrlSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.TemperatureCtrlSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.TemperatureCtrlSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200002Param;
import jp.co.osaki.osol.mng.param.A200012Param;
import jp.co.osaki.osol.mng.param.A200030Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.SmControlUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 *
 * 温度制御(取得) Bean クラス.
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.TEMPERATURE_CTRL_SELECT)
@RequestScoped
public class TemperatureCtrlSelectBean
        extends AbstractApiBean<TemperatureCtrlSelectResult, TemperatureCtrlSelectParameter> {

    /**
     * 現在温度取得フラグ
     */
    private Integer getCurrentTemperatureFlg;

    /**
     * 建物ID
     */
    private Long buildingId;

    /**
     * 企業ID
     */
    private String corpId;

    /**
     * 機器情報
     */
    private SmPrmResultData smPrm;

    @EJB
    private TemperatureCtrlSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
	protected <T extends BaseParam> T initParam(TemperatureCtrlSelectParameter parameter) {
        A200002Param param = new A200002Param();

        if (param != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            corpId = parameter.getOperationCorpId();
            getCurrentTemperatureFlg = parameter.getGetCurrentTemperatureFlg();
            buildingId = parameter.getBuildingId();
        }
		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
    }

    //機種依存チェック(FVP2、FVP(D)、FVPa(D)以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

        this.smPrm = smPrm;

        if (!super.isFV2(smPrm) && !super.isFVPD(smPrm)
                && !super.isFVPaD(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }
        return true;
    }

    /**
     * 現在温度情報の取得
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200002Param> res = (FvpCtrlMngResponse<A200002Param>) response;
        A200002Param param = res.getParam();
        List<CurrentTempretureExtractResultData> currentTemperatureList = new ArrayList<>();
        Map<Integer, String> sensorAddressMap = new HashMap<>();

        if (OsolConstants.FLG_ON.equals(getCurrentTemperatureFlg)) {
            //現在温度取得の必要がある場合、現在温度取得を行う
            List<Map<String, String>> settingCtrlPortList = param.getSettingCtrlPortList();
            int index = 1;
            for (Map<String, String> settingCtrlPort : settingCtrlPortList) {
                //温湿度センサアドレスを取得する
                String sensoraddressTH = settingCtrlPort.get("sensoraddressTH");
                if (sensoraddressTH != null && !CheckUtility.isNullOrEmpty(sensoraddressTH.trim())
                        && !"00".equals(sensoraddressTH)) {
                    sensorAddressMap.put(index, settingCtrlPort.get("sensoraddressTH"));
                }
                index++;
            }

            if (sensorAddressMap.isEmpty()) {
                return;
            }

            //ユニット設定情報を呼び出す
            //新規parameter生成
            A200012Param unitParameter = new A200012Param();
            unitParameter.setProductCd(param.getProductCd());
            unitParameter.setAddress(param.getAddress());

            //機器通信リクエスト生成
            FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm, super.apiParameter.getLoginCorpId(),
                    super.apiParameter.getLoginPersonId(), super.loginUserId);
            req.setParam(unitParameter);

            //機器通信呼び出し
            FvpCtrlMngResponse<BaseParam> baseRes = fvpCtrlMngClient.excute(req);
            A200012Param paramU = (A200012Param) baseRes.getParam();

            //記録ポイント設定情報を取得
            List<Map<String, String>> recordPointSetList = paramU.getRecordPointSetList();

            //デマンド任意を呼び出す
            //新規parameter生成
            A200030Param demandParameter = new A200030Param();
            demandParameter.setProductCd(param.getProductCd());
            demandParameter.setAddress(param.getAddress());

            //機器通信リクエスト生成
            req = new FvpCtrlMngRequest<>(smPrm, super.apiParameter.getLoginCorpId(),
                    super.apiParameter.getLoginPersonId(), super.loginUserId);
            req.setParam(demandParameter);

            //機器通信呼び出し
            baseRes = fvpCtrlMngClient.excute(req);
            A200030Param paramF = (A200030Param) baseRes.getParam();

            //各ポイントの値を取得
            List<Map<String, String>> measurePointList = paramF.getMeasurePointList();
            //計測日時を取得
            String measureDayTime = paramF.getMeasureDayTime();

            for (Map.Entry<Integer, String> entry : sensorAddressMap.entrySet()) {
                CurrentTempretureExtractResultData currentTemperature = new CurrentTempretureExtractResultData();
                currentTemperature.setControlLoad(new BigDecimal(entry.getKey()));
                if (measureDayTime == null || CheckUtility.isNullOrEmpty(measureDayTime.trim())) {
                    currentTemperature.setMeasuredDate(null);
                } else {
                    currentTemperature.setMeasuredDate(
                            DateUtility.conversionDate(measureDayTime, DateUtility.DATE_FORMAT_YYMMDDHHMM));
                }
                //ポイント番号を特定するために、端末アドレスと内部アドレスを取得する
                String terminalAddress = entry.getValue();
                String innerAddress = null;

                if (Integer.parseInt(terminalAddress) >= 1 && Integer.parseInt(terminalAddress) <= 20) {
                    //有線温度センサなので、端末アドレスはそのまま
                    if (OsolConstants.PRODUCT_CD.FV2.getVal().equals(paramU.getProductCd())) {
                        //FV2の場合のみ、内部アドレスが02
                        innerAddress = "02";
                    } else {
                        //上記以外は内部アドレスが01
                        innerAddress = "01";
                    }
                } else if (Integer.parseInt(terminalAddress) >= 21 && Integer.parseInt(terminalAddress) <= 96) {
                    //無線温度センサなので、端末アドレスと内部アドレスを算出
                    BigDecimal tempTerminalAddress = new BigDecimal(entry.getValue());
                    BigDecimal decTerminalAddress = (tempTerminalAddress.subtract(new BigDecimal("20")))
                            .divide(new BigDecimal("4"), 0, BigDecimal.ROUND_UP);
                    BigDecimal decInnerAddress = (tempTerminalAddress.subtract(new BigDecimal("20")))
                            .remainder(new BigDecimal("4"));
                    if (decInnerAddress.compareTo(BigDecimal.ZERO) == 0) {
                        decInnerAddress = new BigDecimal("4");
                    }

                    terminalAddress = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            decTerminalAddress.intValue());
                    innerAddress = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            decInnerAddress.intValue());
                } else {
                    continue;
                }

                //ユニット設定の記録ポイント設定情報からポイント番号を特定
                int pointNo = 0;
                for (int i = 0; i < recordPointSetList.size(); i++) {
                    //端末アドレスと内部アドレスを取得
                    String recordTerminalAddress = recordPointSetList.get(i).get("terminalAddress");
                    String recordInnerAddress = recordPointSetList.get(i).get("internalAddress");
                    if (terminalAddress.equals(recordTerminalAddress) && innerAddress.equals(recordInnerAddress)) {
                        //端末アドレスと内部アドレスが一致したポイント番号が計測対象
                        pointNo = i + 1;
                        break;
                    }
                }

                if (pointNo == 0) {
                    continue;
                } else {
                    currentTemperature.setPointNo(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, pointNo));
                }

                //デマンド任意の各ポイント値から温度情報を取得
                String demand = measurePointList.get(pointNo - 1).get("demand");
                if (demand == null || CheckUtility.isNullOrEmpty(demand.trim())) {
                    currentTemperature.setCurrentTemperature(null);
                } else {
                    currentTemperature.setCurrentTemperature(SmControlUtility.pointDataConversion(demand));
                }

                currentTemperatureList.add(currentTemperature);

            }

            //建物デマンド情報の値を取得
            List<DemandBuildingSmPointListDetailResultData> pointList = dao.getSmPointData(corpId, buildingId,
                    res.getSmId());

            if (pointList != null && !pointList.isEmpty() && !currentTemperatureList.isEmpty()) {
                for (CurrentTempretureExtractResultData currentTempreture : currentTemperatureList) {
                    for (DemandBuildingSmPointListDetailResultData point : pointList) {
                        if (currentTempreture.getPointNo().equals(point.getPointNo())) {
                            currentTempreture.setPointName(point.getPointName());
                            break;
                        }
                    }
                }
            }

            //現在温度情報を設定する
            param.setCurrentTemperatureList(currentTemperatureList);

            // レスポンスに格納
            res.setParam(param);
            response = res;
        }
    }

}