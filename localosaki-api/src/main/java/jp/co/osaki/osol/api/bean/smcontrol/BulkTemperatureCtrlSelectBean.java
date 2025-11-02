package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkTemperatureCtrlSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BulkTemperatureCtrlSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.BulkTemperatureCtrlSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200012Param;
import jp.co.osaki.osol.mng.param.A200030Param;
import jp.co.osaki.osol.mng.param.A200041Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.SmControlUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 複数建物・テナント一括 温度制御(取得) Bean クラス
 *
 * @author f_takemura
 */

@Named(value = SmControlConstants.BULK_TEMPERATURE_CTRL_SELECT)
@RequestScoped
public class BulkTemperatureCtrlSelectBean extends AbstructBulkApiBean <BulkTemperatureCtrlSelectResult, BulkSmControlApiParameter> {

    @EJB
    private BulkTemperatureCtrlSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    private List<SmPrmResultData> smPrmList = new ArrayList<>();

    private List<A200041Param> tempParameterList = new ArrayList<>();

    private String corpId;

    @Override
    protected List<? extends BaseParam> initParamList(List<Map<String, String>> parameterList) {

        // 一括温度制御取得
        List<A200041Param> paramList = new ArrayList<>();
        corpId = super.getParameter().getOperationCorpId();

        // リスト作成
        for (Map<String, String> p : parameterList) {

            A200041Param param = new A200041Param();

            // String型で宣言されているのでweb側でjson形式に変換した際、値に""が付与されるので削除
            String settingChangeHist = p.get("settingChangeHist").replaceAll("\"", "");
            Long smId = Long.parseLong(p.get("smId"));
            Long buildingId = null;
            if (!CheckUtility.isNullOrEmpty(p.get("buildingId"))) {
                buildingId = Long.parseLong(p.get("buildingId"));
            }
            Integer getCurrentTemperatureFlg = null;
            if (!CheckUtility.isNullOrEmpty(p.get("getCurrentTemperatureFlg"))) {
                getCurrentTemperatureFlg = Integer.parseInt(p.get("getCurrentTemperatureFlg"));
            } else {
                getCurrentTemperatureFlg = OsolConstants.FLG_OFF;
            }

            //リクエスト仕様内、固有の値を設定
            param.setSettingChangeHist(settingChangeHist);
            param.setSmId(smId);
            param.setBuildingId(buildingId);
            param.setGetCurrentTemperatureFlg(getCurrentTemperatureFlg);
            paramList.add(param);
        }

        tempParameterList.addAll(paramList);
        return paramList;
    }

    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        // 対応機器チェック
        if (!super.isFV2(smPrm) && !super.isFVPD(smPrm)
                && !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
                && !super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        //機器情報を保持
        smPrmList.add(smPrm);
        return true;
    }

    /**
     * 現在温度の取得と機器空調設定情報の取得（機器空調設定情報のG2のみ）
     */
    @Override
    protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {
        List<FvpCtrlMngRequest<BaseParam>>  fvpReqListUnit = new ArrayList<>();
        List<FvpCtrlMngRequest<BaseParam>>  fvpReqListDemandOpt = new ArrayList<>();
        List<SmPrmResultData> wkListSmPrm = new ArrayList<>();

        //対象建物の建物ポイント情報を取得する
        List<BulkTemperatureCtrlSelectDetailResultData> buildingPointList = dao.getBuildingPointInfo(corpId, tempParameterList);

        //---------------------------------------------------------------------
        // 追加でデータ取得が必要となるリクエストパラメータを生成
        //---------------------------------------------------------------------
        Iterator<BulkResultData> itr = super.resultDataList.iterator();
        for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResList) {

            // 温度制御情報が取得出来ていないものは対象外とする
            BulkResultData resultData = itr.next();
            if (!OsolApiResultCode.API_OK.equals(resultData.getRecordResult()) || !OsolApiResultCode.API_OK.equals(fvpRes.getFvpResultCd())) {
                continue;
            }

            // 対象の機器情報を取得
            SmPrmResultData smPrm = null;
            for (SmPrmResultData smPrmData : smPrmList) {
                if (fvpRes.getSmId().equals(smPrmData.getSmId())) {
                    smPrm = smPrmData;
                    break;
                }
            }
            if (smPrm == null) {
                continue;
            }

            // 対象の温度制御情報
            A200041Param fvpParam = null;
            for (A200041Param tempParam : tempParameterList) {
                if (fvpRes.getSmId().equals(tempParam.getSmId())) {
                    fvpParam = tempParam;
                    break;
                }
            }
            if (fvpParam == null || OsolConstants.FLG_OFF.equals(fvpParam.getGetCurrentTemperatureFlg())) {
                // 対象のデータが無い もしくは 現在温度情報を取得する必要がない場合は読み飛ばす
                continue;
            }

            wkListSmPrm.add(smPrm);
            A200041Param  paramBulkTemp = (A200041Param) fvpRes.getParam();

            // ユニット(取得) リクエストパラメータ生成 (FV2 / FVP(D) / FVPα(D))
            if (super.isFV2(smPrm) || super.isFVPD(smPrm) || super.isFVPaD(smPrm)) {
                FvpCtrlMngRequest<BaseParam> fvpReqUnit = createReqParamUnitSelect(smPrm,
                                                                                    super.apiParameter.getLoginCorpId(),
                                                                                    super.apiParameter.getLoginPersonId(),
                                                                                    super.loginUserId,
                                                                                    paramBulkTemp);
                // リクエストパラメータリストへ追加
                fvpReqListUnit.add(fvpReqUnit);
            }

            // デマンド任意(取得) リクエストパラメータ生成
            FvpCtrlMngRequest<BaseParam> fvpReqDemandOpt = createReqParamDemandOptSelect(smPrm,
                                                                                            super.apiParameter.getLoginCorpId(),
                                                                                            super.apiParameter.getLoginPersonId(),
                                                                                            super.loginUserId,
                                                                                            paramBulkTemp);
            // リクエストパラメータリストへ追加
            fvpReqListDemandOpt.add(fvpReqDemandOpt);
        }

        //---------------------------------------------------------------------
        // 追加のデータ取得
        //---------------------------------------------------------------------
        // デマンド任意(取得) のリクエストパラメータが生成されていれば、機器から情報を取得
        List<FvpCtrlMngResponse<BaseParam>> fvpResListUnit = new ArrayList<>();
        List<FvpCtrlMngResponse<BaseParam>> fvpResListDemandOpt = new ArrayList<>();
        if (fvpReqListDemandOpt != null && !fvpReqListDemandOpt.isEmpty()) {
            // ユニット(取得) 実行
            if (fvpReqListUnit != null && !fvpReqListUnit.isEmpty()) {
                try {
                    fvpResListUnit = fvpCtrlMngClient.excute(fvpReqListUnit);

                    // 正常にユニット(取得)できなかったものはデマンド任意(取得)の実行リストから除外
                    for (FvpCtrlMngResponse<BaseParam> fvpResUnit : fvpResListUnit) {
                        if (!OsolApiResultCode.API_OK.equals(fvpResUnit.getFvpResultCd())) {
                            for (FvpCtrlMngRequest<BaseParam> fvpReqDemandOpt : fvpReqListDemandOpt) {
                                if (fvpResUnit.getSmId().equals(fvpReqDemandOpt.getSmId())) {
                                    fvpReqListDemandOpt.remove(fvpReqDemandOpt);
                                    break;
                                }
                            }
                        }
                    }
                } catch (SmControlException e) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(e));
                    loggingError(Thread.currentThread().getStackTrace()[1], e);
                }
            }

            // デマンド任意(取得) 実行
            if (fvpReqListDemandOpt != null && !fvpReqListDemandOpt.isEmpty()) {
                try {
                    fvpResListDemandOpt = fvpCtrlMngClient.excute(fvpReqListDemandOpt);
                } catch (SmControlException e) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(e));
                    loggingError(Thread.currentThread().getStackTrace()[1], e);
                }
            }
        }

        //---------------------------------------------------------------------
        // レスポンス設定
        //---------------------------------------------------------------------
        //処理済み建物リスト
        List<A200041Param> processedBuildingList = new ArrayList<>();
        for (FvpCtrlMngResponse<BaseParam> fvpRes : fvpResList) {

            // 対象の建物機器ポイント情報を取得
            List<DemandBuildingSmPointListDetailResultData> buildingPointInfo = null;
            //建物ポイント情報取得
            for (BulkTemperatureCtrlSelectDetailResultData buildingPointData : buildingPointList) {
                if ((buildingPointData.getPointList() != null && !buildingPointData.getPointList().isEmpty()) &&
                        fvpRes.getSmId().equals(buildingPointData.getPointList().get(0).getSmId())) {
                    buildingPointInfo = buildingPointData.getPointList();
                    buildingPointList.remove(buildingPointData);
                    break;
                }
            }

            //建物情報取得
            Long buildingId = null;
            for(A200041Param buildingParam : tempParameterList) {
                if (!processedBuildingList.contains(buildingParam) && fvpRes.getSmId().equals(buildingParam.getSmId())) {
                    buildingId = buildingParam.getBuildingId();
                    processedBuildingList.add(buildingParam);
                    break;
                }
            }
            if (buildingId == null) {
                continue;
            }

            for (SmPrmResultData wkSmPrm : wkListSmPrm) {
                if (fvpRes.getSmId().equals(wkSmPrm.getSmId())) {
                    List<CurrentTempretureExtractResultData> currentTemperatureList = new ArrayList<>();
                    A200041Param  paramBulkTemp = (A200041Param) fvpRes.getParam();
                    A200012Param  paramUnitSelect = null;
                    A200030Param  paramDemandOptSelect = null;
                    if (super.isFV2(wkSmPrm) || super.isFVPD(wkSmPrm) || super.isFVPaD(wkSmPrm)) {
                        // 該当のユニット情報を特定
                        for (FvpCtrlMngResponse<BaseParam> fvpResUnit : fvpResListUnit) {
                            if (fvpRes.getSmId().equals(fvpResUnit.getSmId())) {
                                if (OsolApiResultCode.API_OK.equals(fvpResUnit.getFvpResultCd())) {
                                    paramUnitSelect = (A200012Param) fvpResUnit.getParam();
                                } else {
                                    fvpRes.setFvpResultCd(fvpResUnit.getFvpResultCd());
                                }
                                break;
                            }
                        }
                        // 該当のデマンド任意情報を特定
                        for (FvpCtrlMngResponse<BaseParam> fvpResDemandOpt : fvpResListDemandOpt) {
                            if (fvpRes.getSmId().equals(fvpResDemandOpt.getSmId())) {
                                if (OsolApiResultCode.API_OK.equals(fvpResDemandOpt.getFvpResultCd())) {
                                    paramDemandOptSelect = (A200030Param) fvpResDemandOpt.getParam();
                                } else {
                                    fvpRes.setFvpResultCd(fvpResDemandOpt.getFvpResultCd());
                                }
                                break;
                            }
                        }
                        if (!OsolApiResultCode.API_OK.equals(fvpRes.getFvpResultCd())) {
                            // ユニット情報 or デマンド任意 の情報取得に失敗していたら次のレコードへ
                            break;
                        }

                        // 現在温度情報取得
                        currentTemperatureList = getCurrentTemperatureInfoList(paramBulkTemp, paramUnitSelect, paramDemandOptSelect, buildingPointInfo);

                        // レスポンス設定
                        paramBulkTemp.setSmId(wkSmPrm.getSmId());
                        paramBulkTemp.setCurrentTemperatureList(currentTemperatureList);
                        if (buildingId != null) {
                                paramBulkTemp.setBuildingId(buildingId);
                        }
                        fvpRes.setParam(paramBulkTemp);
                    } else if (super.isFVPaG2(wkSmPrm) || super.isEa(wkSmPrm) || super.isEa2(wkSmPrm)) {
                        // 該当のデマンド任意情報を特定
                        for (FvpCtrlMngResponse<BaseParam> fvpResDemandOpt : fvpResListDemandOpt) {
                            if (fvpRes.getSmId().equals(fvpResDemandOpt.getSmId())) {
                                if (OsolApiResultCode.API_OK.equals(fvpResDemandOpt.getFvpResultCd())) {
                                    paramDemandOptSelect = (A200030Param) fvpResDemandOpt.getParam();
                                } else {
                                    fvpRes.setFvpResultCd(fvpResDemandOpt.getFvpResultCd());
                                }
                                break;
                            }
                        }
                        if (!OsolApiResultCode.API_OK.equals(fvpRes.getFvpResultCd())) {
                            // ユニット情報 or デマンド任意 の情報取得に失敗していたら次のレコードへ
                            break;
                        }

                        // 現在温度情報取得
                        currentTemperatureList = getCurrentTemperatureInfoList(paramBulkTemp, paramDemandOptSelect, buildingPointInfo, wkSmPrm);

                        // レスポンス設定
                        paramBulkTemp.setSmId(wkSmPrm.getSmId());
                        paramBulkTemp.setCurrentTemperatureList(currentTemperatureList);
                        if (buildingId != null) {
                            paramBulkTemp.setBuildingId(buildingId);
                        }
                        fvpRes.setParam(paramBulkTemp);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * ユニット(取得) リクエストパラメータ生成
     * @param smPrm
     * @param loginCorpId
     * @param loginPersonId
     * @param loginUserId
     * @param paramBulkTemp
     * @return
     */
    private FvpCtrlMngRequest<BaseParam> createReqParamUnitSelect(SmPrmResultData smPrm, String loginCorpId, String loginPersonId, Long loginUserId, A200041Param paramBulkTemp) {
        A200012Param  param = new A200012Param();
        param.setProductCd(paramBulkTemp.getProductCd());
        param.setAddress(paramBulkTemp.getAddress());

        FvpCtrlMngRequest<BaseParam> ret = new FvpCtrlMngRequest<>(smPrm,
                                                                    loginCorpId,
                                                                    loginPersonId,
                                                                    loginUserId);
        ret.setParam(param);

        return ret;
    }

    /**
     * デマンド任意(取得) リクエストパラメータ生成
     * @param smPrm
     * @param loginCorpId
     * @param loginPersonId
     * @param loginUserId
     * @param paramBulkTemp
     * @return
     */
    private FvpCtrlMngRequest<BaseParam> createReqParamDemandOptSelect(SmPrmResultData smPrm, String loginCorpId, String loginPersonId, Long loginUserId, A200041Param paramBulkTemp) {
        A200030Param  param = new A200030Param();
        param.setProductCd(paramBulkTemp.getProductCd());
        param.setAddress(paramBulkTemp.getAddress());

        FvpCtrlMngRequest<BaseParam> ret = new FvpCtrlMngRequest<>(smPrm,
                                                                    loginCorpId,
                                                                    loginPersonId,
                                                                    loginUserId);
        ret.setParam(param);

        return ret;
    }

    /**
     * 現在温度情報リスト取得
     * (ユニット情報とデマンド任意情報を基に、現在温度情報を特定)
     * @param paramBulkTemp
     * @param paramUnitSelect
     * @param paramDemandOptSelect
     * @param buildingPointInfo
     * @return
     */
    private List<CurrentTempretureExtractResultData> getCurrentTemperatureInfoList(A200041Param paramBulkTemp, A200012Param paramUnitSelect, A200030Param paramDemandOptSelect, List<DemandBuildingSmPointListDetailResultData> buildingPointInfo) {
        List<CurrentTempretureExtractResultData> ret = new ArrayList<>();
        Map<Integer, String> sensorAddressMap = new HashMap<>();
        List<Map<String, String>> settingCtrlPortList = paramBulkTemp.getSettingCtrlPortList();

        int index = 1;
        for (Map<String, String> settingCtrlPort : settingCtrlPortList) {
            //温湿度センサアドレスを取得する
            String sensoraddressTH = settingCtrlPort.get("sensoraddressTH");
            if (sensoraddressTH != null && !CheckUtility.isNullOrEmpty(sensoraddressTH.trim()) && !"00".equals(sensoraddressTH)) {
                sensorAddressMap.put(index, settingCtrlPort.get("sensoraddressTH"));
            }
            index++;
        }

        if (sensorAddressMap.isEmpty()) {
            return ret;
        }

        // 取得したユニット情報から記録ポイント設定情報を取得
        List<Map<String, String>> recordPointSetList = paramUnitSelect.getRecordPointSetList();

        // 取得したデマンド任意情報から各ポイントの値と計測日時を取得
        List<Map<String, String>> measurePointList = paramDemandOptSelect.getMeasurePointList();
        String measureDayTime = paramDemandOptSelect.getMeasureDayTime();

        for (Map.Entry<Integer, String> entry : sensorAddressMap.entrySet()) {
            CurrentTempretureExtractResultData currentTemperature = new CurrentTempretureExtractResultData();
            currentTemperature.setControlLoad(new BigDecimal(entry.getKey()));
            if (measureDayTime == null || CheckUtility.isNullOrEmpty(measureDayTime.trim())) {
                currentTemperature.setMeasuredDate(null);
            } else {
                currentTemperature.setMeasuredDate(DateUtility.conversionDate(measureDayTime, DateUtility.DATE_FORMAT_YYMMDDHHMM));
            }
            //ポイント番号を特定するために、端末アドレスと内部アドレスを取得する
            String terminalAddress = entry.getValue();
            String innerAddress = null;

            if (Integer.parseInt(terminalAddress) >= 1 && Integer.parseInt(terminalAddress) <= 20) {
                //有線温度センサなので、端末アドレスはそのまま
                if (SmControlConstants.PRODUCT_CD_FV2.equals(paramUnitSelect.getProductCd())) {
                    //FV2の場合のみ、内部アドレスが02
                    innerAddress = "02";
                } else {
                    //上記以外は内部アドレスが01
                    innerAddress = "01";
                }
            } else if (Integer.parseInt(terminalAddress) >= 21 && Integer.parseInt(terminalAddress) <= 96) {
                //無線温度センサなので、端末アドレスと内部アドレスを算出
                BigDecimal tempTerminalAddress = new BigDecimal(entry.getValue());
                BigDecimal decTerminalAddress = (tempTerminalAddress.subtract(new BigDecimal("20"))).divide(new BigDecimal("4"), 0, BigDecimal.ROUND_UP);
                BigDecimal decInnerAddress = (tempTerminalAddress.subtract(new BigDecimal("20"))).remainder(new BigDecimal("4"));
                if (decInnerAddress.compareTo(BigDecimal.ZERO) == 0) {
                    decInnerAddress = new BigDecimal("4");
                }

                terminalAddress = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, decTerminalAddress.intValue());
                innerAddress = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, decInnerAddress.intValue());
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
                currentTemperature.setPointNo(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, pointNo));
            }

            //デマンド任意の各ポイント値から温度情報を取得
            String demand = measurePointList.get(pointNo - 1).get("demand");
            if (demand == null || CheckUtility.isNullOrEmpty(demand.trim())) {
                currentTemperature.setCurrentTemperature(null);
            } else {
                currentTemperature.setCurrentTemperature(SmControlUtility.pointDataConversion(demand));
            }

            ret.add(currentTemperature);
        }
        if (buildingPointInfo == null || (buildingPointInfo.size() == 1 && CheckUtility.isNullOrEmpty(buildingPointInfo.get(0).getPointNo()))) {

            //ポイント情報がない場合は何もしない
        } else {
            for (CurrentTempretureExtractResultData currentTempreture : ret) {
                for (DemandBuildingSmPointListDetailResultData point : buildingPointInfo) {
                    if (currentTempreture.getPointNo().equals(point.getPointNo())) {
                        currentTempreture.setPointName(point.getPointName());
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 現在温度情報リスト取得
     * (イベント制御情報とデマンド任意情報を基に、現在温度情報を特定)
     * @param paramBulkTemp
     * @param paramDemandOptSelect
     * @param buildingPointInfo
     * @return
     */
    private List<CurrentTempretureExtractResultData> getCurrentTemperatureInfoList(A200041Param paramBulkTemp, A200030Param paramDemandOptSelect, List<DemandBuildingSmPointListDetailResultData> buildingPointInfo, SmPrmResultData smPrm) {
        List<CurrentTempretureExtractResultData> ret = new ArrayList<>();

        //現在温度取得の必要がある場合、現在温度取得を行う
        List<Map<String, Object>> loadList = paramBulkTemp.getLoadList();
        int index = 1;
        LinkedHashMap<Integer, String> measuredValueMap = new LinkedHashMap<>();
        for (Map<String, Object> load : loadList) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> settingEventCtrlList = (List<Map<String, String>>) load.get("settingEventCtrlList");
            // イベント1のイベント条件と計測値を取得
            Map<String, String> settingEventCtrl = settingEventCtrlList.get(0);
            String measuredValue = settingEventCtrl.get("measuredValue");
            // 温度制御可否判定
            boolean controlTemperatureFlg = isControlTemperature(load, smPrm);
            if (!controlTemperatureFlg || measuredValue == null || CheckUtility.isNullOrEmpty(measuredValue.trim())) {
                //温度制御以外は次のレコードへ
                index++;
                continue;
            } else {
                measuredValueMap.put(index, measuredValue);
                index++;
            }

        }

        if (!measuredValueMap.isEmpty()) {
            // 取得したデマンド任意情報から各ポイントの値と計測日時を取得
            List<Map<String, String>> measurePointList = paramDemandOptSelect.getMeasurePointList();
            String measureDayTime = paramDemandOptSelect.getMeasureDayTime();

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
                currentTemperature.setPointNo(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_3, Integer.parseInt(measuredValue.getValue())));
                if (measureDayTime == null || CheckUtility.isNullOrEmpty(measureDayTime.trim())) {
                    currentTemperature.setMeasuredDate(null);
                } else {
                    currentTemperature.setMeasuredDate(DateUtility.conversionDate(measureDayTime, DateUtility.DATE_FORMAT_YYMMDDHHMM));
                }

                ret.add(currentTemperature);
            }
        }
        if (buildingPointInfo == null || (buildingPointInfo.size() == 1 && CheckUtility.isNullOrEmpty(buildingPointInfo.get(0).getPointNo()))) {
            //ポイント情報がない場合は何もしない
        } else {
            for (CurrentTempretureExtractResultData currentTempreture : ret) {
                for (DemandBuildingSmPointListDetailResultData point : buildingPointInfo) {
                    if (currentTempreture.getPointNo().equals(point.getPointNo())) {
                        currentTempreture.setPointName(point.getPointName());
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 温度制御可否判定
     *
     * @param loadInfo
     * @return
     */
    private boolean isControlTemperature(Map<String, Object> loadInfo, SmPrmResultData smPrm) {
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
