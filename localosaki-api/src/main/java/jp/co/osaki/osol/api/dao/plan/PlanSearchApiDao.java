package jp.co.osaki.osol.api.dao.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.PlanSearchApiParameter;
import jp.co.osaki.osol.api.result.plan.PlanSearchApiResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.plan.BuildingTenantPlanResult;
import jp.co.osaki.osol.api.resultdata.plan.PlanSearchApiDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.BuildingTenantPlanSearchDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.PlanSearchServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 計画履行検索 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class PlanSearchApiDao extends OsolApiDao<PlanSearchApiParameter> {

    private final PlanSearchServiceDaoImpl planSearchServiceDaoImpl;
    private final BuildingTenantPlanSearchDaoImpl buildingTenantPlanSearchImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public PlanSearchApiDao() {
        planSearchServiceDaoImpl = new PlanSearchServiceDaoImpl();
        buildingTenantPlanSearchImpl = new BuildingTenantPlanSearchDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public PlanSearchApiResult query(PlanSearchApiParameter parameter) throws Exception {
        PlanSearchApiResult result = new PlanSearchApiResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new PlanSearchApiResult();
        }

        Map<String, List<Object>> paramMap = createServiceDaoParameter(
                parameter.getOperationCorpId(), parameter.getPlanFulfillmentName(), parameter.getPlanStartYm(),
                parameter.getPlanEndYm(), parameter.getPlanFulfillmentTargetList());

        List<PlanSearchApiDetailResultData> resultSetList = getResultList(planSearchServiceDaoImpl, paramMap);

        for (PlanSearchApiDetailResultData resultSet : resultSetList) {
            List<BuildingTenantPlanResult> list = new ArrayList<>();
            if (ApiCodeValueConstants.PLAN_FULFILLMENT_TARGET.SELECT.getVal().equals(
                    resultSet.getPlanFulfillmentTarget())) {
                BuildingTenantPlanResult param = new BuildingTenantPlanResult();
                param.setCorpId(resultSet.getCorpId());
                param.setPlanFulfillmentId(resultSet.getPlanFulfillmentId());
                list = getResultList(buildingTenantPlanSearchImpl, param);
                // フィルター処理
                list = buildingDataFilterDao.applyDataFilter(list,
                        new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

            }
            resultSet.setBuildingTenantList(list);
            resultSet.setCorpId(exList.get(0).getCorpId());
            resultSet.setCorpVersion(exList.get(0).getVersion());
        }

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetailList(resultSetList);
        return result;
    }

    /**
    *
    * @param corpId
    * @param planName
    * @param planStartYm
    * @param planEndYm
    * @param planTargetCdList
    * @return
    */
    private Map<String, List<Object>> createServiceDaoParameter(String corpId, String planName,
            String planStartYm, String planEndYm, String planTargetCdList) throws Exception {
        Map<String, List<Object>> paramMap = new HashMap<>();

        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(corpId);
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);

        List<Object> planNameList = new ArrayList<>();
        planNameList.add(planName);
        paramMap.put(ApiParamKeyConstants.PLAN_FULFILLMENT_NAME, planNameList);

        List<Object> planStartYmList = new ArrayList<>();
        planStartYmList.add(planStartYm);
        paramMap.put(ApiParamKeyConstants.PLAN_START_YM, planStartYmList);

        List<Object> planEndYmList = new ArrayList<>();
        planEndYmList.add(planEndYm);
        paramMap.put(ApiParamKeyConstants.PLAN_END_YM, planEndYmList);

        if (!CheckUtility.isNullOrEmpty(planTargetCdList)) {
            List<Object> planTargetCdListList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(planTargetCdList, ",");
            while (st.hasMoreTokens()) {
                planTargetCdListList.add(st.nextToken());
            }
            paramMap.put(ApiParamKeyConstants.PLAN_FULFILLMENT_TARGET_LIST, planTargetCdListList);
        }
        return paramMap;
    }

}
