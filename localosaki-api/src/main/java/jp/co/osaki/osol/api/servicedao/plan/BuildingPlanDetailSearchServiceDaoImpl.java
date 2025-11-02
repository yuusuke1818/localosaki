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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.plan.SelectInputBuildingPlanBuildingPlanResultData;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class BuildingPlanDetailSearchServiceDaoImpl
        implements BaseServiceDao<SelectInputBuildingPlanBuildingPlanResultData> {

    @Override
    public List<SelectInputBuildingPlanBuildingPlanResultData> getResultList(
            SelectInputBuildingPlanBuildingPlanResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        long buildingId = t.getBuildingId();
        Date startDate = t.getPlanFulfillmentStartDate();
        Date endDate = t.getPlanFulfillmentEndDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SelectInputBuildingPlanBuildingPlanResultData> query = builder
                .createQuery(SelectInputBuildingPlanBuildingPlanResultData.class);
        Root<TBuildingPlanFulfillment> fromTBuildingPlanFulfillment = query.from(TBuildingPlanFulfillment.class);
        Join<TBuildingPlanFulfillment, TPlanFulfillmentInfo> joinTPlanFulfillmentInfo = fromTBuildingPlanFulfillment
                .join(TBuildingPlanFulfillment_.TPlanFulfillmentInfo);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(
                TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId), corpId));
        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(
                TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.buildingId), buildingId));

        whereList.add(
                builder.or(
                        builder.and(
                                builder.lessThanOrEqualTo(
                                        joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                        startDate),
                                builder.greaterThanOrEqualTo(
                                        joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                                        endDate)),
                        builder.and(
                                builder.greaterThanOrEqualTo(
                                        joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                        startDate),
                                builder.lessThanOrEqualTo(
                                        joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                        endDate)),
                        builder.and(builder.greaterThanOrEqualTo(
                                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), startDate),
                                builder.lessThanOrEqualTo(
                                        joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                                        endDate))));

        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg), 0));
        whereList.add(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg), 0));

        query = query.select(builder.construct(SelectInputBuildingPlanBuildingPlanResultData.class,
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id)
                        .get(TBuildingPlanFulfillmentPK_.buildingId),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id)
                        .get(TBuildingPlanFulfillmentPK_.planFulfillmentId),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthInputCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthNeedCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthAchieveCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthTargetInputCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthInputCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthNeedCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthAchieveCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthTargetInputCount),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.version),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id)
                        .get(TBuildingPlanFulfillmentPK_.planFulfillmentId)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectInputBuildingPlanBuildingPlanResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectInputBuildingPlanBuildingPlanResultData> getResultList(
            List<SelectInputBuildingPlanBuildingPlanResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectInputBuildingPlanBuildingPlanResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SelectInputBuildingPlanBuildingPlanResultData find(SelectInputBuildingPlanBuildingPlanResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SelectInputBuildingPlanBuildingPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SelectInputBuildingPlanBuildingPlanResultData merge(SelectInputBuildingPlanBuildingPlanResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SelectInputBuildingPlanBuildingPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
