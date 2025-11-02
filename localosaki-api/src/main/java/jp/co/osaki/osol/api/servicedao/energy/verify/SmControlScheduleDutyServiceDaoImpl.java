package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleDutyLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlScheduleDutyLog;
import jp.co.osaki.osol.entity.TSmControlScheduleDutyLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleDutyLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御スケジュールデューティ履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlScheduleDutyServiceDaoImpl
        implements BaseServiceDao<SmControlScheduleDutyLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleDutyLogListDetailResultData> getResultList(
            SmControlScheduleDutyLogListDetailResultData target, EntityManager em) {
        Long smControlScheduleLogId = target.getSmControlScheduleLogId();
        Long smId = target.getSmId();
        String patternNoFrom = target.getPatternNoFrom();
        String patternNoTo = target.getPatternNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlScheduleDutyLogListDetailResultData> query = builder
                .createQuery(SmControlScheduleDutyLogListDetailResultData.class);

        Root<TSmControlScheduleDutyLog> root = query.from(TSmControlScheduleDutyLog.class);
        List<Predicate> whereList = new ArrayList<>();

        //機器制御スケジュール履歴ID
        if (smControlScheduleLogId != null) {
            whereList.add(builder.equal(root.get(TSmControlScheduleDutyLog_.id)
                    .get(TSmControlScheduleDutyLogPK_.smControlScheduleLogId), smControlScheduleLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder
                    .equal(root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.smId), smId));
        }
        //パターン番号From、パターン番号To
        if (!CheckUtility.isNullOrEmpty(patternNoFrom) && !CheckUtility.isNullOrEmpty(patternNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.patternNo),
                    patternNoFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.patternNo),
                    patternNoTo));
        } else if (!CheckUtility.isNullOrEmpty(patternNoFrom) && CheckUtility.isNullOrEmpty(patternNoTo)) {
            whereList.add(builder.equal(
                    root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.patternNo),
                    patternNoFrom));
        }

        //TODO スケジュール対応で改めて対応
//        query = query.select(builder.construct(SmControlScheduleDutyLogListDetailResultData.class,
//                root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.smControlScheduleLogId),
//                root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.smControlScheduleLogId),
//                root.get(TSmControlScheduleDutyLog_.id).get(TSmControlScheduleDutyLogPK_.patternNo),
//                root.get(TSmControlScheduleDutyLog_.dutyOnTimeMinute),
//                root.get(TSmControlScheduleDutyLog_.dutyOffTimeMinute),
//                root.get(TSmControlScheduleDutyLog_.dutyFlg),
//                root.get(TSmControlScheduleDutyLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlScheduleDutyLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleDutyLogListDetailResultData> getResultList(
            List<SmControlScheduleDutyLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleDutyLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleDutyLogListDetailResultData find(SmControlScheduleDutyLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlScheduleDutyLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleDutyLogListDetailResultData merge(SmControlScheduleDutyLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlScheduleDutyLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
