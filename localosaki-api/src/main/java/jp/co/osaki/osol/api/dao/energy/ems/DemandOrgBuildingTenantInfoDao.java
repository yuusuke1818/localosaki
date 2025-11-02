/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgBuildingTenantInfoParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgBuildingTenantInfoResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgBuildingTenantInfoBuildingSumDateResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsOrgUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況（個別）_建物・テナント情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgBuildingTenantInfoDao extends OsolApiDao<DemandOrgBuildingTenantInfoParameter> {

    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public DemandOrgBuildingTenantInfoDao() {
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgBuildingTenantInfoResult query(DemandOrgBuildingTenantInfoParameter parameter) throws Exception {
        DemandOrgBuildingTenantInfoResult result = new DemandOrgBuildingTenantInfoResult();

        if (parameter.getYmd() == null) {
            //年月日の場合は、現在日付を設定
            parameter.setYmd(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD));
        }

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、四捨五入を設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
        }

        //建物・テナント情報を取得する
        AllBuildingListDetailResultData buildingParam = DemandEmsOrgUtility
                .getOrgBuildingInfoParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<AllBuildingListDetailResultData> buildingList = getResultList(idBuildingSelectServiceDaoImpl,
                buildingParam);
        // フィルター処理
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.size() != 1) {
            return new DemandOrgBuildingTenantInfoResult();
        }

        //企業デマンド情報を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandOrgBuildingTenantInfoResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        buildingDemandParam.setCorpId(parameter.getOperationCorpId());
        buildingDemandParam.setBuildingId(parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandOrgBuildingTenantInfoResult();
        }

        //集計デマンド情報を取得する
        AggregateDmListDetailResultData aggregateParam = EnergySettingUtility
                .getAggregateDmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);
        List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                aggregateParam);

        //契約電力を取得する（建物デマンドから取得）
        BigDecimal contractPower = buildingDemandList.get(0).getContractKw();

      //目標電力を取得する（建物デマンドから取得）
        BigDecimal targetPower = buildingDemandList.get(0).getTargetKw();

        //建物集計日情報を作成する
        List<DemandOrgBuildingTenantInfoBuildingSumDateResultData> buildingSummaryDateList = new ArrayList<>();
        DemandOrgBuildingTenantInfoBuildingSumDateResultData buildingSummaryDate = new DemandOrgBuildingTenantInfoBuildingSumDateResultData();
        int i = 1;

        //電力集計日をセット
        buildingSummaryDate.setDateNo(i);
        buildingSummaryDate.setAggregateDmName("電力集計日");
        buildingSummaryDate.setSumDate(buildingDemandList.get(0).getSumDate());
        buildingSummaryDateList.add(buildingSummaryDate);

        if (aggregateList != null && !aggregateList.isEmpty()) {

            //集計デマンドID順にソートする
            aggregateList = aggregateList.stream().sorted(
                    Comparator.comparing(AggregateDmListDetailResultData::getAggregateDmId, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

            //その他の集計日をセット
            for (AggregateDmListDetailResultData aggregateData : aggregateList) {
                i++;
                buildingSummaryDate = new DemandOrgBuildingTenantInfoBuildingSumDateResultData();
                buildingSummaryDate.setDateNo(i);
                buildingSummaryDate.setAggregateDmName(aggregateData.getAggregateDmName());
                buildingSummaryDate.setSumDate(aggregateData.getSumDate());
                buildingSummaryDateList.add(buildingSummaryDate);
            }
        }

        //取得結果をセットする
        result.setCorpId(buildingList.get(0).getCorpId());
        result.setBuildingId(buildingList.get(0).getBuildingId());
        result.setBuildingNo(buildingList.get(0).getBuildingNo());
        result.setBuildingType(buildingList.get(0).getBuildingType());
        result.setBuildingName(buildingList.get(0).getBuildingName());
        result.setCorpSummaryDate(corpDemandList.get(0).getSumDate());
        result.setBuildingSummaryDateList(buildingSummaryDateList);
        if (contractPower != null) {
            result.setContractPower(
                    contractPower.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        } else {
            result.setContractPower(contractPower);
        }

        if (targetPower != null) {
            result.setTargetPower(
                    targetPower.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        } else {
            result.setTargetPower(targetPower);
        }

        result.setWorkStartTime(DateUtility.changeDateFormat(buildingDemandList.get(0).getWorkStartTime(),
                DateUtility.DATE_FORMAT_HHMM_COLON));
        result.setShopOpenTime(DateUtility.changeDateFormat(buildingDemandList.get(0).getShopOpenTime(),
                DateUtility.DATE_FORMAT_HHMM_COLON));
        result.setShopCloseTime(DateUtility.changeDateFormat(buildingDemandList.get(0).getShopCloseTime(),
                DateUtility.DATE_FORMAT_HHMM_COLON));
        result.setWorkEndTime(DateUtility.changeDateFormat(buildingDemandList.get(0).getWorkEndTime(),
                DateUtility.DATE_FORMAT_HHMM_COLON));

        return result;
    }

}
