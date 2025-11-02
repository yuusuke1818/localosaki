package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.entity.MSmLineVerifyPK_;
import jp.co.osaki.osol.entity.MSmLineVerify_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器系統検証 EntityServiceDaoクラス
 *
 * @author t_hirata
 */
public class MSmLineVerifyServiceDaoImpl implements BaseServiceDao<MSmLineVerify> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineVerify> getResultList(MSmLineVerify t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long lineGroupId = t.getId().getLineGroupId();
        String lineNo = t.getId().getLineNo();
        Long buildingId = t.getId().getBuildingId();
        Long smId = t.getId().getSmId();
        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmLineVerify> query = builder.createQuery(MSmLineVerify.class);
        Root<MSmLineVerify> root = query.from(MSmLineVerify.class);
        // 条件
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MSmLineVerify_.id).get(MSmLineVerifyPK_.corpId), corpId));
        if (lineGroupId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineVerify_.id).get(MSmLineVerifyPK_.lineGroupId), lineGroupId));
        }
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(
                    root.get(MSmLineVerify_.id).get(MSmLineVerifyPK_.lineNo), lineNo));
        }
        if (buildingId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineVerify_.id).get(MSmLineVerifyPK_.buildingId), buildingId));
        }
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineVerify_.id).get(MSmLineVerifyPK_.smId), smId));
        }
        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmLineVerify> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineVerify> getResultList(List<MSmLineVerify> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineVerify> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmLineVerify find(MSmLineVerify t, EntityManager em) {
        return em.find(MSmLineVerify.class, t.getId());
    }

    @Override
    public void persist(MSmLineVerify t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MSmLineVerify merge(MSmLineVerify t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MSmLineVerify t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
