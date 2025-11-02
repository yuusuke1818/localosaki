package jp.co.osaki.osol.api.servicedao.entity;

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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK_;
import jp.co.osaki.osol.entity.MSmControlLoad_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御負荷
 *
 * @author t_hirata
 */
public class MSmControlLoadServiceDaoImpl implements BaseServiceDao<MSmControlLoad> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoad> getResultList(MSmControlLoad t, EntityManager em) {

        Long smId = t.getId().getSmId();
        BigDecimal controlLoad = t.getId().getControlLoad();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmControlLoad> query = builder.createQuery(MSmControlLoad.class);

        Root<MSmControlLoad> root = query.from(MSmControlLoad.class);
        List<Predicate> whereList = new ArrayList<>();

        if (smId != null) {
            whereList.add(builder.equal(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.smId), smId));
        }
        if (controlLoad != null) {
            whereList.add(builder.equal(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad), controlLoad));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MSmControlLoad_.delFlg), OsolConstants.FLG_OFF));

        // ソート
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.asc(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad)));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmControlLoad> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoad> getResultList(List<MSmControlLoad> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoad> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmControlLoad find(MSmControlLoad t, EntityManager em) {
        return em.find(MSmControlLoad.class, t.getId());
    }

    @Override
    public void persist(MSmControlLoad t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MSmControlLoad merge(MSmControlLoad t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MSmControlLoad t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
