package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SettingEventCtrlSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SettingEventCtrlSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.SettingEventCtrlSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SettingEventCtrlSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200020Param;
import jp.co.osaki.osol.mng.param.A200030Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.SmControlUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 *
 * イベント制御設定(取得) Bean クラス
 *
 * @author t_sakamoto
 *
 */
@Named(value = SmControlConstants.SETTING_EVENT_CTRL_SELECT)
@RequestScoped
public class SettingEventCtrlSelectBean
        extends AbstractApiBean<SettingEventCtrlSelectResult, SettingEventCtrlSelectParameter> {

    @EJB
    private SettingEventCtrlSelectDao dao;

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

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(SettingEventCtrlSelectParameter parameter) {
        A200020Param param = new A200020Param();

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

    //機種依存チェック(FVPa(G2), Ea, Ea2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

        this.smPrm = smPrm;

        if (!super.isFVPaG2(smPrm)
                && !super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }
        return true;
    }

    /**
     * 現在温度の取得と機器空調情報の取得を行う
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200020Param> res = (FvpCtrlMngResponse<A200020Param>) response;
        A200020Param param = res.getParam();
        List<CurrentTempretureExtractResultData> currentTemperatureList = new ArrayList<>();

        if (OsolConstants.FLG_ON.equals(getCurrentTemperatureFlg)) {
            //現在温度取得の必要がある場合、現在温度取得を行う
            List<Map<String, Object>> loadList = param.getLoadList();
            int index = 1;
            LinkedHashMap<Integer, String> measuredValueMap = new LinkedHashMap<>();
            for (Map<String, Object> load : loadList) {
                // 温度制御可否判定
                boolean controlTemperatureFlg = isControlTemperature(load);

                @SuppressWarnings("unchecked")
                List<Map<String, String>> settingEventCtrlList = (List<Map<String, String>>) load
                        .get("settingEventCtrlList");
                // 計測値
                Map<String, String> settingEventCtrl = settingEventCtrlList.get(0);
                String measuredValue = settingEventCtrl.get("measuredValue");
                if (!controlTemperatureFlg ||
                        (measuredValue == null || CheckUtility.isNullOrEmpty(measuredValue.trim()))) {
                    //温度制御以外は次のレコードへ
                    index++;
                    continue;
                } else {
                    measuredValueMap.put(index, measuredValue);
                    index++;
                }

            }

            if (!measuredValueMap.isEmpty()) {
                //計測値を元にデマンド（任意）を行う
                //新規parameter生成
                A200030Param demandParameter = new A200030Param();
                demandParameter.setProductCd(param.getProductCd());
                demandParameter.setAddress(param.getAddress());

                //機器通信リクエスト生成
                FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm, super.apiParameter.getLoginCorpId(),
                        super.apiParameter.getLoginPersonId(), super.loginUserId);
                req.setParam(demandParameter);

                //機器通信呼び出し
                FvpCtrlMngResponse<BaseParam> baseRes = fvpCtrlMngClient.excute(req);
                A200030Param paramF = (A200030Param) baseRes.getParam();

                //各ポイントの値を取得
                List<Map<String, String>> measurePointList = paramF.getMeasurePointList();
                //計測日時を取得
                String measureDayTime = paramF.getMeasureDayTime();

                for (Map.Entry<Integer, String> measuredValue : measuredValueMap.entrySet()) {
                    String demand = measurePointList.get(Integer.parseInt(measuredValue.getValue()) - 1).get("demand");
                    CurrentTempretureExtractResultData currentTemperature = new CurrentTempretureExtractResultData();
                    currentTemperature.setControlLoad(new BigDecimal(measuredValue.getKey()));
                    if (demand == null || CheckUtility.isNullOrEmpty(demand.trim())) {
                        currentTemperature.setCurrentTemperature(null);
                    } else {
                        currentTemperature.setCurrentTemperature(SmControlUtility.pointDataConversion(demand));
                    }
                    //ポイント番号は3桁に変換
                    currentTemperature.setPointNo(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3,
                            Integer.parseInt(measuredValue.getValue())));
                    if (measureDayTime == null || CheckUtility.isNullOrEmpty(measureDayTime.trim())) {
                        currentTemperature.setMeasuredDate(null);
                    } else {
                        currentTemperature.setMeasuredDate(
                                DateUtility.conversionDate(measureDayTime, DateUtility.DATE_FORMAT_YYMMDDHHMM));
                    }
                    currentTemperatureList.add(currentTemperature);
                }

            }

        }

        //建物ポイント情報と機器空調設定情報を取得する
        SettingEventCtrlSelectDetailResultData detailData = dao.getData(corpId, buildingId, res.getSmId(),
                getCurrentTemperatureFlg);

        //機器空調設定情報を設定する
        param.setLinkSettingList(detailData.getLinkSettingExtractList());

        if (detailData.getPointList() != null && !detailData.getPointList().isEmpty()
                && !currentTemperatureList.isEmpty()) {
            for (CurrentTempretureExtractResultData currentTempreture : currentTemperatureList) {
                for (DemandBuildingSmPointListDetailResultData point : detailData.getPointList()) {
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

    /**
     * 温度制御可否判定
     *
     * @param loadInfo
     * @return
     */
    private boolean isControlTemperature(Map<String, Object> loadInfo) {
        boolean ret = false;

        // FVPα(G2)の場合
        // イベント条件 = 温度制御 であれば 温度制御可
        if (super.isFVPaG2(smPrm)) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> settingEventCtrlList = (List<Map<String, String>>) loadInfo.get("settingEventCtrlList");

            //イベント1のみ見ればよい
            Map<String, String> settingEventCtrl = settingEventCtrlList.get(0);

            //イベント条件
            String eventTerms = settingEventCtrl.get("eventTerms");

            if (!CheckUtility.isNullOrEmpty(eventTerms) && "2".equals(eventTerms)) {
                ret = true;
            }
        }

        // Eα / Eα2の場合
        // イベント制御フラグ = 温度制御有効 であれば 温度制御可
        if (super.isEa(smPrm) || super.isEa2(smPrm)) {
            String eventCtrlFlg = String.valueOf(loadInfo.get("eventCtrlFlg"));
            if (!CheckUtility.isNullOrEmpty(eventCtrlFlg) && "2".equals(eventCtrlFlg)) {
                ret = true;
            }
        }

        return ret;
    }

}
