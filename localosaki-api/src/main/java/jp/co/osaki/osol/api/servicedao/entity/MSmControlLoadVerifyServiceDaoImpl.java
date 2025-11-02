package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MSmControlLoadVerify;
import jp.co.osaki.osol.entity.MSmControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmControlLoadVerify_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御負荷設定
 *
 * @author t_hirata
 */
public class MSmControlLoadVerifyServiceDaoImpl implements BaseServiceDao<MSmControlLoadVerify> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerify> getResultList(MSmControlLoadVerify t, EntityManager em) {

        Long smId = t.getId().getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmControlLoadVerify> query = builder.createQuery(MSmControlLoadVerify.class);

        Root<MSmControlLoadVerify> root = query.from(MSmControlLoadVerify.class);

        // 条件
        List<Predicate> whereList = new ArrayList<>();
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.smId), smId));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmControlLoadVerify> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerify> getResultList(List<MSmControlLoadVerify> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerify> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmControlLoadVerify find(MSmControlLoadVerify t, EntityManager em) {
        return em.find(MSmControlLoadVerify.class, t.getId());
    }

    @Override
    public void persist(MSmControlLoadVerify t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MSmControlLoadVerify merge(MSmControlLoadVerify t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MSmControlLoadVerify t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
