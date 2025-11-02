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
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionWeekSelectParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionWeekSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastDayListResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgAiPredictionWeekSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgAiPredictionWeekSelectSummaryResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.weather.TimelyWeatherListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandPowerForecastDayListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.weather.TimelyWeatherListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsOrgUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・AI予測・週間予測電力量） Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class DemandOrgAiPredictionWeekSelectDao extends OsolApiDao<DemandOrgAiPredictionWeekSelectParameter> {

    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final CommonDemandPowerForecastDayListServiceDaoImpl commonDemandPowerForecastDayListServiceDaoImpl;
    private final TimelyWeatherListServiceDaoImpl timelyWeatherListServiceDaoImpl;

    public DemandOrgAiPredictionWeekSelectDao() {
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        commonDemandPowerForecastDayListServiceDaoImpl = new CommonDemandPowerForecastDayListServiceDaoImpl();
        timelyWeatherListServiceDaoImpl = new TimelyWeatherListServiceDaoImpl();
    }

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;


    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgAiPredictionWeekSelectResult query(DemandOrgAiPredictionWeekSelectParameter parameter) throws Exception {
        DemandOrgAiPredictionWeekSelectResult result = new DemandOrgAiPredictionWeekSelectResult();
        DemandOrgAiPredictionWeekSelectSummaryResultData summary = new DemandOrgAiPredictionWeekSelectSummaryResultData();
        List<DemandOrgAiPredictionWeekSelectDetailResultData> detailList = new ArrayList<>();

        // この先1週間の初日（= 当日）
        Date afterOneWeekFrom = DateUtility.conversionDate(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
        // この先1週間の最終日（= 当日の6日後）
        Date afterOneWeekTo = DateUtility.plusDay(afterOneWeekFrom, 6);
        // 過去1週間の初日（= 当日の7日前）
        Date beforeOneWeekFrom = DateUtility.plusDay(afterOneWeekFrom, -7);
        // 過去1週間の最終日（ = 前日）
        Date beforeOneWeekTo = DateUtility.plusDay(afterOneWeekFrom, -1);

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
                || (smInfoList.get(0).getStartDate() != null && smInfoList.get(0).getStartDate().after(afterOneWeekFrom))
                || (smInfoList.get(0).getEndDate() != null && afterOneWeekFrom.after(smInfoList.get(0).getEndDate()))) {
            //機器情報が取得できない、AielMasterに接続されていない場合、期間外（当日基準）は処理を終了する
            return result;
        }

        //建物デマンドからデータを取得する
        BuildingDemandListDetailResultData buildingDmParam = DemandEmsUtility.getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDmList = getResultList(buildingDemandListServiceDaoImpl, buildingDmParam);
        if(buildingDmList == null || buildingDmList.size() != 1) {
            //建物デマンド情報が取得できない場合は処理を終了する
            return result;
        }

        //デマンド月報系統からデータを取得する（過去1週間分 + 当日）
        CommonDemandMonthReportLineListResult monthReportLineParam = DemandEmsUtility.getMonthReportLineListParam(parameter.getOperationCorpId(),
                parameter.getBuildingId(), parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), beforeOneWeekFrom, afterOneWeekFrom);
        List<CommonDemandMonthReportLineListResult> monthReportLineList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportLineParam);

        //デマンド月報からデータを取得する（過去1週間分）
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), beforeOneWeekFrom, beforeOneWeekTo);
        List<CommonDemandMonthReportListResult> monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);

        //需要電力予測1日値データからデータを取得する（この先1週間分）
        CommonDemandPowerForecastDayListResult forecastParam = DemandEmsOrgUtility.getDemandPowerForecastDayParam(parameter.getSmId(), afterOneWeekFrom, afterOneWeekTo);
        List<CommonDemandPowerForecastDayListResult> forecastList = getResultList(commonDemandPowerForecastDayListServiceDaoImpl, forecastParam);

        //時間別天気の取得
        Date targetDate = super.getServerDateTime();
        // 当日以降が対象
        targetDate = DateUtility.conversionDate(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
        TimelyWeatherListDetailResultData param = new TimelyWeatherListDetailResultData();
        param.setTargetDateTime(targetDate);
        param.setCityCd(buildingList.get(0).getCityCd());
        List<TimelyWeatherListDetailResultData> timelyWeatherDetailList = getResultList(timelyWeatherListServiceDaoImpl, param);
        int loop=0;//時間別天気データセット用

        //明細部のデータを作成する
        Date currentDate = beforeOneWeekFrom;
        do {
            DemandOrgAiPredictionWeekSelectDetailResultData detail = new DemandOrgAiPredictionWeekSelectDetailResultData();
            detail.setMeasurementDate(currentDate);
            if(currentDate.equals(afterOneWeekFrom)) {
                // 当日の場合は実績と予測両方
                detail.setActualFlg(Boolean.TRUE);
                detail.setPredictionFlg(Boolean.TRUE);
            } else if(currentDate.before(afterOneWeekFrom)) {
                // 当日より前の場合は実績のみ
                detail.setActualFlg(Boolean.TRUE);
                detail.setPredictionFlg(Boolean.FALSE);
            } else {
                // 上記以外の場合は予測のみ
                detail.setActualFlg(Boolean.FALSE);
                detail.setPredictionFlg(Boolean.TRUE);
            }

            //デマンド月報系統
            if(monthReportLineList != null && !monthReportLineList.isEmpty()) {
                if(currentDate.before(DateUtility.plusDay(afterOneWeekFrom, 1))) {
                    //期間内だけチェックする
                    for(CommonDemandMonthReportLineListResult monthReportLine : monthReportLineList) {
                        if(monthReportLine.getMeasurementDate().equals(currentDate)) {
                            detail.setActualLineKwh(monthReportLine.getLineValueKwh());
                            break;
                        }
                    }
                }
            }

            //デマンド月報
            if(monthReportList != null && !monthReportList.isEmpty()) {
                if(currentDate.before(afterOneWeekFrom)) {
                    //期間内だけチェックする
                    for(CommonDemandMonthReportListResult monthReport : monthReportList) {
                        if(monthReport.getMeasurementDate().equals(currentDate)) {
                            detail.setMaxTemperature(monthReport.getOutAirTempMax());
                            detail.setMinTemperature(monthReport.getOutAirTempMin());
                            break;
                        }
                    }
                }
            }

            //需要電力予測1日値データ
            if(forecastList != null && !forecastList.isEmpty()) {
                if(currentDate.after(beforeOneWeekTo)) {
                    //期間内だけチェックする
                    for(CommonDemandPowerForecastDayListResult forecast : forecastList) {
                        if(forecast.getForecastDate().equals(currentDate)) {
                            detail.setPredictionLineKwh(forecast.getTotalDemandValue());
                            break;
                        }
                    }
                }
            }

            //時間別天気 当日から1週間の気温をセット
            if(currentDate.equals(afterOneWeekFrom) || currentDate.after(afterOneWeekFrom)) {
                if(timelyWeatherDetailList != null && !timelyWeatherDetailList.isEmpty()) {
                    BigDecimal maxTemp = new BigDecimal(-999);
                    BigDecimal minTemp = new BigDecimal(999);
                    //期間内だけチェックする
                    for(;loop < timelyWeatherDetailList.size();loop++) {
                        if(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD).equals(DateUtility.changeDateFormat(timelyWeatherDetailList.get(loop).getTargetDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD))) { // ここで日付変換いるかも？
                            if(maxTemp.compareTo(timelyWeatherDetailList.get(loop).getTemperature()) <= 0) {
                                maxTemp = timelyWeatherDetailList.get(loop).getTemperature();
                            }
                            if(minTemp.compareTo(timelyWeatherDetailList.get(loop).getTemperature()) >= 0) {
                                minTemp = timelyWeatherDetailList.get(loop).getTemperature();
                            }
                        }else {
                            break;
                        }
                    }
                    detail.setMaxTemperature(maxTemp);
                    detail.setMinTemperature(minTemp);
                }
            }


            detailList.add(detail);
            //1日進める
            currentDate = DateUtility.plusDay(currentDate, 1);
        }while(!currentDate.after(afterOneWeekTo));

        //サマリ部のデータを作成する
        if(buildingDmList.get(0).getDayCommodityChargeUnitPrice() == null) {
            //昼間従量料金単価が設定できていない場合はnullをセットする
            summary.setBeforeOneWeekCharge(null);
            summary.setAfterOneWeekCharge(null);
            summary.setChargeCompareBeforeAfter(null);
        } else {
            //昼間従量料金単価が設定できている場合は各項目の計算を行うため、初期値をセット
            summary.setBeforeOneWeekCharge(null);
            summary.setAfterOneWeekCharge(null);
            summary.setChargeCompareBeforeAfter(null);

            for(DemandOrgAiPredictionWeekSelectDetailResultData detail : detailList) {
                if(detail.getMeasurementDate().before(afterOneWeekFrom)) {
                    //当日より前は過去1週間
                    if(detail.getActualLineKwh() != null) {
                        if(summary.getBeforeOneWeekCharge() == null) {
                            summary.setBeforeOneWeekCharge(detail.getActualLineKwh());
                        } else {
                            summary.setBeforeOneWeekCharge(summary.getBeforeOneWeekCharge().add(detail.getActualLineKwh()));
                        }
                    }
                } else {
                    //当日以降はこの先1週間
                    if(detail.getPredictionLineKwh() != null) {
                        if(summary.getAfterOneWeekCharge() == null) {
                            summary.setAfterOneWeekCharge(detail.getPredictionLineKwh());
                        } else {
                            summary.setAfterOneWeekCharge(summary.getAfterOneWeekCharge().add(detail.getPredictionLineKwh()));
                        }

                    }
                }
            }

            //各項目従量料金単価を掛ける
            if(summary.getBeforeOneWeekCharge() != null) {
                summary.setBeforeOneWeekCharge(summary.getBeforeOneWeekCharge().multiply(buildingDmList.get(0).getDayCommodityChargeUnitPrice()));
            }
            if(summary.getAfterOneWeekCharge() != null) {
                summary.setAfterOneWeekCharge(summary.getAfterOneWeekCharge().multiply(buildingDmList.get(0).getDayCommodityChargeUnitPrice()));
            }

            //過去1週間とこの先1週間の差分をセットする
            if(summary.getBeforeOneWeekCharge() != null && summary.getAfterOneWeekCharge() != null) {
                summary.setChargeCompareBeforeAfter(summary.getBeforeOneWeekCharge().subtract(summary.getAfterOneWeekCharge()));
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
