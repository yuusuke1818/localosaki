package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLog;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御スケジュール設定履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlScheduleSetLogListServiceDaoImpl
        implements BaseServiceDao<SmControlScheduleSetLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleSetLogListDetailResultData> getResultList(
            SmControlScheduleSetLogListDetailResultData target, EntityManager em) {
        Long smControlScheduleLogId = target.getSmControlScheduleLogId();
        Long smId = target.getSmId();
        BigDecimal controlLoadFrom = target.getControlLoadFrom();
        BigDecimal controlLoadTo = target.getControlLoadTo();
        BigDecimal targetMonthFrom = target.getTargetMonthFrom();
        BigDecimal targetMonthTo = target.getTargetMonthTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlScheduleSetLogListDetailResultData> query = builder
                .createQuery(SmControlScheduleSetLogListDetailResultData.class);

        Root<TSmControlScheduleSetLog> root = query.from(TSmControlScheduleSetLog.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器制御スケジュール履歴ID
        if (smControlScheduleLogId != null) {
            whereList.add(builder.equal(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smControlScheduleLogId),
                    smControlScheduleLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(
                    builder.equal(root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smId), smId));
        }
        //制御負荷From、制御負荷To
        if (controlLoadFrom != null && controlLoadTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.controlLoad),
                    controlLoadFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.controlLoad),
                    controlLoadTo));
        } else if (controlLoadFrom != null && controlLoadTo == null) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.controlLoad),
                    controlLoadFrom));
        }
        //対象月From、対象月To
        if (targetMonthFrom != null && targetMonthTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.targetMonth),
                    targetMonthFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.targetMonth),
                    targetMonthTo));
        }

        query = query.select(builder.construct(SmControlScheduleSetLogListDetailResultData.class,
                root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smControlScheduleLogId),
                root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smId),
                root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.controlLoad),
                root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.targetMonth),
                root.get(TSmControlScheduleSetLog_.sundayPatternNo),
                root.get(TSmControlScheduleSetLog_.mondayPatternNo),
                root.get(TSmControlScheduleSetLog_.tuesdayPatternNo),
                root.get(TSmControlScheduleSetLog_.wednesdayPatternNo),
                root.get(TSmControlScheduleSetLog_.thursdayPatternNo),
                root.get(TSmControlScheduleSetLog_.fridayPatternNo),
                root.get(TSmControlScheduleSetLog_.saturdayPatternNo),
                root.get(TSmControlScheduleSetLog_.holidayPatternNo),
                root.get(TSmControlScheduleSetLog_.specificDate1),
                root.get(TSmControlScheduleSetLog_.specificDate2),
                root.get(TSmControlScheduleSetLog_.specificDate3),
                root.get(TSmControlScheduleSetLog_.specificDate4),
                root.get(TSmControlScheduleSetLog_.specificDate5),
                root.get(TSmControlScheduleSetLog_.specificDate6),
                root.get(TSmControlScheduleSetLog_.specificDate7),
                root.get(TSmControlScheduleSetLog_.specificDate8),
                root.get(TSmControlScheduleSetLog_.specificDate9),
                root.get(TSmControlScheduleSetLog_.specificDate10),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo1),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo2),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo3),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo4),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo5),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo6),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo7),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo8),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo9),
                root.get(TSmControlScheduleSetLog_.specificDatePatternNo10),
                root.get(TSmControlScheduleSetLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlScheduleSetLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleSetLogListDetailResultData> getResultList(
            List<SmControlScheduleSetLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleSetLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleSetLogListDetailResultData find(SmControlScheduleSetLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlScheduleSetLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleSetLogListDetailResultData merge(SmControlScheduleSetLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlScheduleSetLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
