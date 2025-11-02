package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.TempHumidControlLogVerifyResult;
import jp.co.osaki.osol.entity.TTempHumidControlLog;
import jp.co.osaki.osol.entity.TTempHumidControlLogPK_;
import jp.co.osaki.osol.entity.TTempHumidControlLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 温湿度制御ログ（検証用）
 *
 * @author t_hirata
 */
public class TempHumidControlLogVerifyServiceDaoImpl implements BaseServiceDao<TempHumidControlLogVerifyResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TempHumidControlLogVerifyResult> getResultList(TempHumidControlLogVerifyResult t, EntityManager em) {

        // パラメータ
        Long smId = t.getSmId();
        String recordYmdhms = t.getRecordYmdhms();
        String recordYmdFrom = t.getRecordYmdFrom();
        String recordYmdTo = t.getRecordYmdTo();
        String recordYmdhmsTo = t.getRecordYmdhmsTo();
        String recordYmdhmsMin = t.getRecordYmdhmsMin();
        String recordYmdhmsMax = t.getRecordYmdhmsMax();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TempHumidControlLogVerifyResult> query = builder
                .createQuery(TempHumidControlLogVerifyResult.class);

        Root<TTempHumidControlLog> root = query.from(TTempHumidControlLog.class);

        // 条件
        List<Predicate> whereList = new ArrayList<>();
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.smId), smId));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhms)) {
            whereList.add(builder.equal(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms), recordYmdhms));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdFrom)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms),
                    recordYmdFrom + "000000"));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdTo)) {
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms),
                    recordYmdTo + "999999"));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmsTo)) {
            whereList.add(builder.lessThan(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms), recordYmdhmsTo));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmsMin)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms), recordYmdhmsMin));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmsMax)) {
            whereList.add(builder.lessThan(
                    root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms), recordYmdhmsMax));
        }

        // ソート
        List<Order> orderList = new ArrayList<>();
        if (!CheckUtility.isNullOrEmpty(recordYmdhmsTo)) {
            // Toのみ
            orderList.add(builder.desc(root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms)));
        } else {
            // 範囲指定
            orderList.add(builder.asc(root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms)));
        }

        query = query.select(builder.construct(TempHumidControlLogVerifyResult.class,
                root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.smId),
                root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.recordYmdhms),
                root.get(TTempHumidControlLog_.id).get(TTempHumidControlLogPK_.portOutStatus),
                root.get(TTempHumidControlLog_.controlTemp),
                root.get(TTempHumidControlLog_.controlHumid),
                root.get(TTempHumidControlLog_.controlAlarmStatus),
                root.get(TTempHumidControlLog_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TempHumidControlLogVerifyResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TempHumidControlLogVerifyResult> getResultList(List<TempHumidControlLogVerifyResult> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TempHumidControlLogVerifyResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TempHumidControlLogVerifyResult find(TempHumidControlLogVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TempHumidControlLogVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TempHumidControlLogVerifyResult merge(TempHumidControlLogVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TempHumidControlLogVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
