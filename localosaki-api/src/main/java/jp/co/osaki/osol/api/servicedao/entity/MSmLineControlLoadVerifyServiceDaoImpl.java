package jp.co.osaki.osol.api.servicedao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御系統制御負荷検証
 *
 * @author t_hirata
 */
public class MSmLineControlLoadVerifyServiceDaoImpl implements BaseServiceDao<MSmLineControlLoadVerify> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineControlLoadVerify> getResultList(MSmLineControlLoadVerify t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long lineGroupId = t.getId().getLineGroupId();
        String lineNo = t.getId().getLineNo();
        Long buildingId = t.getId().getBuildingId();
        Long smId = t.getId().getSmId();
        BigDecimal controlLoad = t.getId().getControlLoad();
        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmLineControlLoadVerify> query = builder.createQuery(MSmLineControlLoadVerify.class);
        Root<MSmLineControlLoadVerify> root = query.from(MSmLineControlLoadVerify.class);
        // 条件
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.corpId), corpId));
        if (lineGroupId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineGroupId), lineGroupId));
        }
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineNo), lineNo));
        }
        if (buildingId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.buildingId), buildingId));
        }
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.smId), smId));
        }
        if (controlLoad != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.controlLoad), controlLoad));
        }
        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.controlLoad)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmLineControlLoadVerify> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineControlLoadVerify> getResultList(List<MSmLineControlLoadVerify> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLineControlLoadVerify> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmLineControlLoadVerify find(MSmLineControlLoadVerify t, EntityManager em) {
        return em.find(MSmLineControlLoadVerify.class, t.getId());
    }

    @Override
    public void persist(MSmLineControlLoadVerify t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MSmLineControlLoadVerify merge(MSmLineControlLoadVerify t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MSmLineControlLoadVerify t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
