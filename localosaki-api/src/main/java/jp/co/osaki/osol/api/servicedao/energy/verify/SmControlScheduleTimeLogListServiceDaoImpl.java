package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御スケジュール時間帯履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlScheduleTimeLogListServiceDaoImpl
        implements BaseServiceDao<SmControlScheduleTimeLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleTimeLogListDetailResultData> getResultList(
            SmControlScheduleTimeLogListDetailResultData target, EntityManager em) {
        Long smControlScheduleLogId = target.getSmControlScheduleLogId();
        Long smId = target.getSmId();
        String patternNoFrom = target.getPatternNoFrom();
        String patternNoTo = target.getPatternNoTo();
        Integer timeSlotNoFrom = target.getTimeSlotNoFrom();
        Integer timeSlotNoTo = target.getTimeSlotNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlScheduleTimeLogListDetailResultData> query = builder
                .createQuery(SmControlScheduleTimeLogListDetailResultData.class);

        Root<TSmControlScheduleTimeLog> root = query.from(TSmControlScheduleTimeLog.class);
        List<Predicate> whereList = new ArrayList<>();

        //機器制御スケジュール履歴ID
        if (smControlScheduleLogId != null) {
            whereList.add(builder.equal(root.get(TSmControlScheduleTimeLog_.id)
                    .get(TSmControlScheduleTimeLogPK_.smControlScheduleLogId), smControlScheduleLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder
                    .equal(root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.smId), smId));
        }
        //パターン番号From、パターン番号To
        if (!CheckUtility.isNullOrEmpty(patternNoFrom) && !CheckUtility.isNullOrEmpty(patternNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.patternNo),
                    patternNoFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.patternNo),
                    patternNoTo));
        } else if (!CheckUtility.isNullOrEmpty(patternNoFrom) && CheckUtility.isNullOrEmpty(patternNoTo)) {
            whereList.add(builder.equal(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.patternNo),
                    patternNoFrom));
        }
        //時間帯番号From、時間帯番号To
        if (timeSlotNoFrom != null && timeSlotNoTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.timeSlotNo),
                    timeSlotNoFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.timeSlotNo),
                    timeSlotNoTo));
        } else if (timeSlotNoFrom != null && timeSlotNoTo == null) {
            whereList.add(builder.equal(
                    root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.timeSlotNo),
                    timeSlotNoFrom));
        }

        query = query.select(builder.construct(SmControlScheduleTimeLogListDetailResultData.class,
                root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.smControlScheduleLogId),
                root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.smId),
                root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.patternNo),
                root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.timeSlotNo),
                root.get(TSmControlScheduleTimeLog_.startTimeHour),
                root.get(TSmControlScheduleTimeLog_.startTimeMinute),
                root.get(TSmControlScheduleTimeLog_.endTimeHour),
                root.get(TSmControlScheduleTimeLog_.endTimeMinute),
                root.get(TSmControlScheduleTimeLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<SmControlScheduleTimeLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleTimeLogListDetailResultData> getResultList(
            List<SmControlScheduleTimeLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleTimeLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleTimeLogListDetailResultData find(SmControlScheduleTimeLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlScheduleTimeLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleTimeLogListDetailResultData merge(SmControlScheduleTimeLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlScheduleTimeLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
