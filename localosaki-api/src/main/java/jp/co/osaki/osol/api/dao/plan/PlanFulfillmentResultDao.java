package jp.co.osaki.osol.api.dao.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentResultParameter;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentResultResult;
import jp.co.osaki.osol.api.servicedao.plan.PlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 計画履行実績操作 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class PlanFulfillmentResultDao extends OsolApiDao<PlanFulfillmentResultParameter> {

    private final PlanFulfillmentResultServiceDaoImpl planFulfillmentResultServiceDaoImpl;

    public PlanFulfillmentResultDao() {
        planFulfillmentResultServiceDaoImpl = new PlanFulfillmentResultServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public PlanFulfillmentResultResult query(PlanFulfillmentResultParameter parameter) throws Exception {
        PlanFulfillmentResultResult result = new PlanFulfillmentResultResult();

        // 実績
        Map<String, List<Object>> paramMap = new HashMap<>();
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getOperationCorpId());
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(parameter.getBuildingId());
        List<Object> planIdList = new ArrayList<>();
        planIdList.add(parameter.getPlanFulfillmentId());
        List<Object> startDateList = new ArrayList<>();
        startDateList.add(DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD));

        Date endDate = DateUtility.plusMonth(DateUtility.conversionDate(
                DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD), 1);
        endDate = DateUtility.plusDay(endDate, -1);
        List<Object> endDateList = new ArrayList<>();

        endDateList.add(DateUtility.changeDateFormat(endDate, DateUtility.DATE_FORMAT_YYYYMMDD));
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);
        paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
        paramMap.put(ApiParamKeyConstants.PLAN_ID, planIdList);
        paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, startDateList);
        paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, endDateList);

        result.setDetailList(getResultList(planFulfillmentResultServiceDaoImpl, paramMap));
        return result;
    }

}
