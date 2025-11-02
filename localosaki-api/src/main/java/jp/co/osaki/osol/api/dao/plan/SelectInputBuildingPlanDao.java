/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.SelectInputBuildingPlanParameter;
import jp.co.osaki.osol.api.result.plan.SelectInputBuildingPlanResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.plan.PlanFulfillmentResultDetailResultData;
import jp.co.osaki.osol.api.resultdata.plan.SelectInputBuildingPlanBuildingPlanResultData;
import jp.co.osaki.osol.api.resultdata.plan.SelectInputBuildingPlanDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.BuildingPlanDetailSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.PlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 計画履行入力建物検索 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class SelectInputBuildingPlanDao extends OsolApiDao<SelectInputBuildingPlanParameter> {

    private final BuildingPlanDetailSearchServiceDaoImpl buildingPlanDetailSearchServiceDaoImpl;
    private final PlanFulfillmentResultServiceDaoImpl planFulfillmentResultServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public SelectInputBuildingPlanDao() {
        buildingPlanDetailSearchServiceDaoImpl = new BuildingPlanDetailSearchServiceDaoImpl();
        planFulfillmentResultServiceDaoImpl = new PlanFulfillmentResultServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SelectInputBuildingPlanResult query(SelectInputBuildingPlanParameter parameter) throws Exception {
        SelectInputBuildingPlanResult result = new SelectInputBuildingPlanResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new SelectInputBuildingPlanResult();
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        //フィルタ処理を行う
        exBuildingList = buildingDataFilterDao.applyDataFilter(exBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new SelectInputBuildingPlanResult();
        }

        SelectInputBuildingPlanBuildingPlanResultData param = new SelectInputBuildingPlanBuildingPlanResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());

        Date startDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        Date endDate = DateUtility.plusYear(DateUtility.conversionDate(
                DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD), 1);
        endDate = DateUtility.plusDay(endDate, -1);

        param.setPlanFulfillmentStartDate(parameter.getPlanStartDate());
        param.setPlanFulfillmentEndDate(endDate);

        List<SelectInputBuildingPlanBuildingPlanResultData> bpdsrsList = getResultList(
                buildingPlanDetailSearchServiceDaoImpl, param);

        List<SelectInputBuildingPlanDetailResultData> sibprsList = new ArrayList<>();
        for (SelectInputBuildingPlanBuildingPlanResultData bpdsrs : bpdsrsList) {
            SelectInputBuildingPlanDetailResultData sibprs = new SelectInputBuildingPlanDetailResultData();
            // 実績
            Map<String, List<Object>> paramMap = new HashMap<>();
            List<Object> corpIdList = new ArrayList<>();
            corpIdList.add(bpdsrs.getCorpId());
            List<Object> buildingIdList = new ArrayList<>();
            buildingIdList.add(bpdsrs.getBuildingId());
            List<Object> planIdList = new ArrayList<>();
            planIdList.add(bpdsrs.getPlanFulfillmentId());
            List<Object> startDateList = new ArrayList<>();
            startDateList
                    .add(DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD));

            Date resultEndDate = DateUtility.plusMonth(startDate, 13);
            resultEndDate = DateUtility.plusDay(resultEndDate, -1);
            List<Object> endDateList = new ArrayList<>();
            endDateList.add(DateUtility.changeDateFormat(resultEndDate, DateUtility.DATE_FORMAT_YYYYMMDD));

            paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);
            paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
            paramMap.put(ApiParamKeyConstants.PLAN_ID, planIdList);
            paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, startDateList);
            paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, endDateList);
            List<PlanFulfillmentResultDetailResultData> tpfrsList = getResultList(planFulfillmentResultServiceDaoImpl,
                    paramMap);
            sibprs.setBuildingPlanDetailSearchResultSet(bpdsrs);
            sibprs.setPlanFulfillmentResultResultSetList(tpfrsList);
            sibprsList.add(sibprs);
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setCorpVersion(exCorpList.get(0).getVersion());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setDetailList(sibprsList);
        return result;
    }

}
