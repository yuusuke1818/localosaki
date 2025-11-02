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
import jp.co.osaki.osol.api.resultdata.plan.PlanSearchApiDetailResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfoPK_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業情報
 *
 * @author t-shibata
 */
public class PlanSearchServiceDaoImpl implements BaseServiceDao<PlanSearchApiDetailResultData> {

    @Override
    public List<PlanSearchApiDetailResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        Object corpId = Utility.getListFirstItem(parameterMap.get(ApiParamKeyConstants.CORP_ID));
        Object planName = Utility.getListFirstItem(parameterMap.get(ApiParamKeyConstants.PLAN_FULFILLMENT_NAME));
        if (planName != null && !"".equals(planName)) {
            planName = "%" + planName + "%";
        }
        Object startYm = Utility.getListFirstItem(parameterMap.get(ApiParamKeyConstants.PLAN_START_YM));
        Object endYm = Utility.getListFirstItem(parameterMap.get(ApiParamKeyConstants.PLAN_END_YM));
        List<Object> targetCdList = parameterMap.get(ApiParamKeyConstants.PLAN_FULFILLMENT_TARGET_LIST);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlanSearchApiDetailResultData> query = builder.createQuery(PlanSearchApiDetailResultData.class);
        Root<TPlanFulfillmentInfo> fromTPlanFulfillmentInfo = query.from(TPlanFulfillmentInfo.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                corpId.toString()));
        if (planName != null && !"".equals(planName)) {
            whereList.add(builder.like(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                    planName.toString()));
        }

        if (startYm != null && !"".equals(startYm)) {
            Date startDate = DateUtility.conversionDate(startYm.toString(), DateUtility.DATE_FORMAT_YYYYMM);
            if (endYm != null && !"".equals(endYm)) {
                // 開始も終了も両方入っている場合
                Date endDate = DateUtility.conversionDate(endYm.toString(), DateUtility.DATE_FORMAT_YYYYMM);
                endDate = DateUtility.plusMonth(endDate, 1);
                endDate = DateUtility.plusDay(endDate, -1);
                whereList.add(builder.or(
                        builder.between(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                startDate, endDate),
                        builder.between(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                                startDate, endDate),
                        builder.and(
                                builder.lessThanOrEqualTo(
                                        fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                        startDate),
                                builder.greaterThanOrEqualTo(
                                        fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                                        endDate))));
            } else {
                // 開始のみの場合
                Date endStartDate = DateUtility.plusMonth(startDate, 1);
                endStartDate = DateUtility.plusDay(endStartDate, -1);
                whereList
                        .add(builder.or(
                                builder.and(
                                        builder.lessThanOrEqualTo(fromTPlanFulfillmentInfo
                                                .get(TPlanFulfillmentInfo_.planFulfillmentStartDate), startDate),
                                        builder.greaterThanOrEqualTo(fromTPlanFulfillmentInfo
                                                .get(TPlanFulfillmentInfo_.planFulfillmentEndDate), endStartDate)),
                                builder.greaterThanOrEqualTo(
                                        fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                        startDate)));
            }
        }

        if (targetCdList != null && !targetCdList.isEmpty()) {
            whereList.add(builder
                    .and(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget).in(targetCdList)));
        }
        whereList.add(builder.equal(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg), 0));

        query = query.select(builder.construct(PlanSearchApiDetailResultData.class,
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(fromTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id)
                                .get(TPlanFulfillmentInfoPK_.planFulfillmentId)));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<PlanSearchApiDetailResultData> getResultList(PlanSearchApiDetailResultData target, EntityManager em) {
        return null;
    }

    @Override
    public PlanSearchApiDetailResultData find(PlanSearchApiDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(PlanSearchApiDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(PlanSearchApiDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanSearchApiDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanSearchApiDetailResultData> getResultList(List<PlanSearchApiDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanSearchApiDetailResultData merge(PlanSearchApiDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
