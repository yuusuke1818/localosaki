package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionTodaySelectParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionTodaySelectResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastTimeListResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgAiPredictionTodaySelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgAiPredictionTodaySelectSummaryResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandPowerForecastTimeListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsOrgUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・AI予測・本日の予測電力） Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class DemandOrgAiPredictionTodaySelectDao extends OsolApiDao<DemandOrgAiPredictionTodaySelectParameter> {

    private final int MAX_JIGEN_NO = 48;

    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandPowerForecastTimeListServiceDaoImpl commonDemandPowerForecastTimeListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;


    public DemandOrgAiPredictionTodaySelectDao() {
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandPowerForecastTimeListServiceDaoImpl = new CommonDemandPowerForecastTimeListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgAiPredictionTodaySelectResult query(DemandOrgAiPredictionTodaySelectParameter parameter) throws Exception {
        DemandOrgAiPredictionTodaySelectResult result = new DemandOrgAiPredictionTodaySelectResult();
        Date targetDate = DateUtility.conversionDate(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);

        DemandOrgAiPredictionTodaySelectSummaryResultData summary = new DemandOrgAiPredictionTodaySelectSummaryResultData();
        List<DemandOrgAiPredictionTodaySelectDetailResultData> detailList = new ArrayList<>();


        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return result;
        }

        //建物・テナント情報を取得する
        AllBuildingListDetailResultData buildingParam = DemandEmsOrgUtility
                .getOrgBuildingInfoParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        buildingParam.setCorpId(parameter.getOperationCorpId());
        buildingParam.setBuildingId(parameter.getBuildingId());
        List<AllBuildingListDetailResultData> buildingList = getResultList(idBuildingSelectServiceDaoImpl,
                buildingParam);
        // フィルター処理
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.size() != 1) {
            return result;
        }

        //建物機器情報を取得する
        DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSmId());

        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(demandBuildingSmListServiceDaoImpl,
                buildingSmParam);
        if (buildingSmList == null || buildingSmList.size() != 1) {
            return result;
        }

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        List<SmSelectResult> smInfoList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smInfoList == null || smInfoList.isEmpty()
                || !OsolConstants.PRODUCT_CD.FVP_E_ALPHA2.getVal().equals(smInfoList.get(0).getProductCd())
                || OsolConstants.FLG_OFF.equals(smInfoList.get(0).getAielMasterConnectFlg())
                || (smInfoList.get(0).getStartDate() != null && smInfoList.get(0).getStartDate().after(targetDate))
                || (smInfoList.get(0).getEndDate() != null && targetDate.after(smInfoList.get(0).getEndDate()))) {
            //機器情報が取得できない、AielMasterに接続されていない場合、期間外は処理を終了する
            return result;
        }

        //建物デマンドからデータを取得する
        BuildingDemandListDetailResultData buildingDmParam = DemandEmsUtility.getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDmList = getResultList(buildingDemandListServiceDaoImpl, buildingDmParam);
        if(buildingDmList == null || buildingDmList.size() != 1) {
            //建物デマンド情報が取得できない場合は処理を終了する
            return result;
        }

        //デマンド日報系統からデータを取得する
        CommonDemandDayReportLineListResult dayReportParam = DemandEmsUtility.getDayReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), targetDate, BigDecimal.ONE,
                targetDate, BigDecimal.valueOf(MAX_JIGEN_NO));
        List<CommonDemandDayReportLineListResult> lineResultList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportParam);

        //需要電力予測30分値データからデータを取得する
        CommonDemandPowerForecastTimeListResult forecastParam = DemandEmsOrgUtility.getDemandPowerForecastTimeParam(parameter.getSmId(), targetDate);
        List<CommonDemandPowerForecastTimeListResult> forecastList = getResultList(commonDemandPowerForecastTimeListServiceDaoImpl, forecastParam);

        // 明細部のベースを作成する
        for(int i = 1;i <= MAX_JIGEN_NO; i++) {
            DemandOrgAiPredictionTodaySelectDetailResultData detail = new DemandOrgAiPredictionTodaySelectDetailResultData();
            detail.setMeasurementDate(targetDate);
            detail.setHhmm(DemandDataUtility.changeJigenNoToHHMM(BigDecimal.valueOf(i), true));
            detail.setActualFlg(Boolean.FALSE);
            detailList.add(detail);
        }

        //デマンド日報系統のデータをListに追加する
        if(lineResultList != null && !lineResultList.isEmpty()) {
            for(CommonDemandDayReportLineListResult lineResult : lineResultList) {
                detailList.get(lineResult.getJigenNo().intValue() - 1).setActualLineKw(lineResult.getLineValueKw());
            }

            //デマンド日報系統の最大時限Noを取得するため、デマンド日報系統のデータを時限No降順でソートする
            lineResultList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(lineResultList, ApiCodeValueConstants.SORT_ORDER.DESC.getVal());
            BigDecimal maxJigenNo = lineResultList.get(0).getJigenNo();

            //時限No.1～maxJigenNoまでは実績フラグをTrueにしておく
            for(int i = 1;i <= maxJigenNo.intValue();i++) {
                detailList.get(i - 1).setActualFlg(Boolean.TRUE);
            }

        }


        //予測電力30分値データをListに追加する
        if(forecastList != null && !forecastList.isEmpty()) {
            for(CommonDemandPowerForecastTimeListResult forecast : forecastList) {
                detailList.get(forecast.getJigenNo().intValue() - 1).setStandardModeValue(forecast.getNormalForecastValue());
                detailList.get(forecast.getJigenNo().intValue() - 1).setComfortableModeValue(forecast.getComfortableForecastValue());
                detailList.get(forecast.getJigenNo().intValue() - 1).setEcoModeValue(forecast.getEcoForecastValue());
            }
        }

        // サマリ部を作成する
        if(buildingDmList.get(0).getDayCommodityChargeUnitPrice() == null) {
            //昼間従量料金単価が設定できていない場合はnullをセットする
            summary.setStandardModeCharge(null);
            summary.setComfortableModeCharge(null);
            summary.setChargeCompareStandardComfortable(null);
            summary.setEcoModeCharge(null);
            summary.setChargeCompareStandardEco(null);
        } else {
            //昼間従量料金単価が設定できている場合は各項目の計算を行うため、初期値をセット
            summary.setStandardModeCharge(null);
            summary.setComfortableModeCharge(null);
            summary.setChargeCompareStandardComfortable(null);
            summary.setEcoModeCharge(null);
            summary.setChargeCompareStandardEco(null);

            for(DemandOrgAiPredictionTodaySelectDetailResultData detail : detailList) {
                if(detail.getActualFlg()) {
                    //実績値が設定されている場合は実績値を加算
                    if (detail.getActualLineKw() != null) {
                        if(summary.getStandardModeCharge() == null) {
                            summary.setStandardModeCharge(detail.getActualLineKw());
                        } else {
                            summary.setStandardModeCharge(summary.getStandardModeCharge().add(detail.getActualLineKw()));
                        }
                        if(summary.getComfortableModeCharge() == null) {
                            summary.setComfortableModeCharge(detail.getActualLineKw());
                        } else {
                            summary.setComfortableModeCharge(summary.getComfortableModeCharge().add(detail.getActualLineKw()));
                        }
                        if(summary.getEcoModeCharge() == null) {
                            summary.setEcoModeCharge(detail.getActualLineKw());
                        } else {
                            summary.setEcoModeCharge(summary.getEcoModeCharge().add(detail.getActualLineKw()));
                        }
                    }
                } else {
                    //実績値が設定されていない場合は各モードの予測値を加算
                    if(detail.getStandardModeValue() != null) {
                        if(summary.getStandardModeCharge() == null) {
                            summary.setStandardModeCharge(detail.getStandardModeValue());
                        } else {
                            summary.setStandardModeCharge(summary.getStandardModeCharge().add(detail.getStandardModeValue()));
                        }
                    }
                    if(detail.getComfortableModeValue() != null) {
                        if(summary.getComfortableModeCharge() == null) {
                            summary.setComfortableModeCharge(detail.getComfortableModeValue());
                        } else {
                            summary.setComfortableModeCharge(summary.getComfortableModeCharge().add(detail.getComfortableModeValue()));
                        }

                    }
                    if(detail.getEcoModeValue() != null) {
                        if(summary.getEcoModeCharge() == null) {
                            summary.setEcoModeCharge(detail.getEcoModeValue());
                        } else {
                            summary.setEcoModeCharge(summary.getEcoModeCharge().add(detail.getEcoModeValue()));
                        }

                    }
                }
            }

            //各項目2で割って、昼間従量料金単価を掛ける
            if(summary.getStandardModeCharge() != null) {
                summary.setStandardModeCharge(summary.getStandardModeCharge().divide(BigDecimal.valueOf(2)).multiply(buildingDmList.get(0).getDayCommodityChargeUnitPrice()));
            }
            if(summary.getComfortableModeCharge() != null) {
                summary.setComfortableModeCharge(summary.getComfortableModeCharge().divide(BigDecimal.valueOf(2)).multiply(buildingDmList.get(0).getDayCommodityChargeUnitPrice()));
            }
            if(summary.getEcoModeCharge() != null) {
                summary.setEcoModeCharge(summary.getEcoModeCharge().divide(BigDecimal.valueOf(2)).multiply(buildingDmList.get(0).getDayCommodityChargeUnitPrice()));
            }


            //通常モード料金との差分を算出する
            if(summary.getStandardModeCharge() != null && summary.getComfortableModeCharge() != null) {
                summary.setChargeCompareStandardComfortable(summary.getComfortableModeCharge().subtract(summary.getStandardModeCharge()));
            }
            if(summary.getStandardModeCharge() != null && summary.getEcoModeCharge() != null) {
                summary.setChargeCompareStandardEco(summary.getEcoModeCharge().subtract(summary.getStandardModeCharge()));
            }
        }

        // 従量料金単価をセットする
        summary.setDayCommodityChargeUnitPrice(buildingDmList.get(0).getDayCommodityChargeUnitPrice());
        summary.setNightCommodityChargeUnitPrice(buildingDmList.get(0).getNightCommodityChargeUnitPrice());
        result.setSummaryData(summary);
        result.setDetailList(detailList);
        return result;
    }


}
