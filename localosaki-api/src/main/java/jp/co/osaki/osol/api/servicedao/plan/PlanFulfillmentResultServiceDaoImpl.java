/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.resultdata.plan.PlanFulfillmentResultDetailResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.TPlanFulfillmentResult;
import jp.co.osaki.osol.entity.TPlanFulfillmentResultPK_;
import jp.co.osaki.osol.entity.TPlanFulfillmentResult_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class PlanFulfillmentResultServiceDaoImpl implements BaseServiceDao<PlanFulfillmentResultDetailResultData> {

    @Override
    public List<PlanFulfillmentResultDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {

        Object corpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID));
        Object buildingId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        Object planId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_ID));
        Date planStartDate = DateUtility.conversionDate(
                Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_START_DATE)).toString(),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        List<Object> endDateStringList = map.get(ApiParamKeyConstants.PLAN_END_DATE);
        String endDateString = endDateStringList.get(0).toString();
        Date planEndDate = DateUtility.conversionDate(endDateString, DateUtility.DATE_FORMAT_YYYYMMDD);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlanFulfillmentResultDetailResultData> query = builder
                .createQuery(PlanFulfillmentResultDetailResultData.class);
        Root<TPlanFulfillmentResult> tPlanFulfillmentResultRoot = query.from(TPlanFulfillmentResult.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.corpId),
                corpId.toString()));
        whereList.add(builder.equal(
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.buildingId),
                new Long(buildingId.toString())));
        whereList.add(builder.equal(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id)
                .get(TPlanFulfillmentResultPK_.planFulfillmentId), new Long(planId.toString())));
        whereList.add(builder.greaterThanOrEqualTo(
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.planFulfillmentDate), planStartDate));
        whereList.add(builder.lessThanOrEqualTo(
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.planFulfillmentDate), planEndDate));
        whereList.add(builder.equal(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.delFlg), 0));

        // 検索条件を組み込む
        query = query.select(builder.construct(PlanFulfillmentResultDetailResultData.class,
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.corpId),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.buildingId),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id)
                        .get(TPlanFulfillmentResultPK_.planFulfillmentId),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id)
                        .get(TPlanFulfillmentResultPK_.planFulfillmentResultId),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.planFulfillmentDate),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.planFulfillmentResult),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.delFlg),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.version),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.createDate),
                tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.updateDate)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.planFulfillmentDate)),
                        builder.asc(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id)
                                .get(TPlanFulfillmentResultPK_.planFulfillmentResultId)));

        // 検索実行
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<PlanFulfillmentResultDetailResultData> getResultList(PlanFulfillmentResultDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanFulfillmentResultDetailResultData> getResultList(List<PlanFulfillmentResultDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanFulfillmentResultDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanFulfillmentResultDetailResultData find(PlanFulfillmentResultDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(PlanFulfillmentResultDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanFulfillmentResultDetailResultData merge(PlanFulfillmentResultDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(PlanFulfillmentResultDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
