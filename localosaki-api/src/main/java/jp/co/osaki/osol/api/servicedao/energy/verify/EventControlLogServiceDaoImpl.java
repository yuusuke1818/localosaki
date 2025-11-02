package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.entity.TEventControlLog;
import jp.co.osaki.osol.entity.TEventControlLogPK_;
import jp.co.osaki.osol.entity.TEventControlLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * イベント制御ログ
 *
 * @author t_hirata
 */
public class EventControlLogServiceDaoImpl implements BaseServiceDao<EventControlLogResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EventControlLogResult> getResultList(EventControlLogResult t, EntityManager em) {

        // パラメータ
        Long smId = t.getSmId();
        String recordYmdhms = t.getRecordYmdhms();
        String recordYmdFrom = t.getRecordYmdFrom();
        String recordYmdTo = t.getRecordYmdTo();
        String recordYmdMin = t.getRecordYmdhmsMin();
        String recordYmdMax = t.getRecordYmdhmsMax();

        BigDecimal controlLoad = t.getControlLoad();
        String recordYmdhmsTo = t.getRecordYmdhmsTo();
        Integer orderDesc = t.getOrderDesc();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<EventControlLogResult> query = builder.createQuery(EventControlLogResult.class);

        Root<TEventControlLog> root = query.from(TEventControlLog.class);

        // 条件
        List<Predicate> whereList = new ArrayList<>();
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.smId), smId));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhms)) {
            whereList.add(builder.equal(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdhms));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdFrom)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdFrom + "000000"));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdTo)) {
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdTo + "999999"));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdMin)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdMin));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdMax)) {
            whereList.add(builder.lessThan(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdMax));
        }

        if (controlLoad != null) {
            whereList.add(builder.equal(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.controlLoad), controlLoad));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmsTo)) {
            whereList.add(builder.lessThan(
                    root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms), recordYmdhmsTo));
        }

        // ソート
        List<Order> orderList = new ArrayList<>();
        if (orderDesc != null && orderDesc == 1) {
            orderList.add(builder.desc(root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms)));
        } else {
            orderList.add(builder.asc(root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms)));
        }

        query = query.select(builder.construct(EventControlLogResult.class,
                root.get(TEventControlLog_.id).get(TEventControlLogPK_.smId),
                root.get(TEventControlLog_.id).get(TEventControlLogPK_.recordYmdhms),
                root.get(TEventControlLog_.id).get(TEventControlLogPK_.controlLoad),
                root.get(TEventControlLog_.controlStatus),
                root.get(TEventControlLog_.controlEvent1Kind),
                root.get(TEventControlLog_.controlEvent2Kind),
                root.get(TEventControlLog_.controlEvent3Kind),
                root.get(TEventControlLog_.controlEvent1Val),
                root.get(TEventControlLog_.controlEvent2Val),
                root.get(TEventControlLog_.controlEvent3Val),
                root.get(TEventControlLog_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<EventControlLogResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EventControlLogResult> getResultList(List<EventControlLogResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EventControlLogResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventControlLogResult find(EventControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(EventControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventControlLogResult merge(EventControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(EventControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
