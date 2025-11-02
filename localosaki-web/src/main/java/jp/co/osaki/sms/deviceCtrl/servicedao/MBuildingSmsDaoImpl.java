package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK_;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MBuildingSmsDaoImpl implements BaseServiceDao<MBuildingSms> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MBuildingSms> getResultList(MBuildingSms target, EntityManager em) {

        return null;
    }

    @Override
    public List<MBuildingSms> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MBuildingSms> getResultList(List<MBuildingSms> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MBuildingSms> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MBuildingSms find(MBuildingSms target, EntityManager em) {
        String corpId = target.getId().getCorpId();
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MBuildingSms> query = builder.createQuery(MBuildingSms.class);

        Root<MBuildingSms> root = query.from(MBuildingSms.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MBuildingSms_.id).get(MBuildingSmsPK_.corpId), corpId));
        whereList.add(builder.equal(root.get(MBuildingSms_.id).get(MBuildingSmsPK_.buildingId), buildingId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MBuildingSms target, EntityManager em) {


    }

    @Override
    public MBuildingSms merge(MBuildingSms target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MBuildingSms target, EntityManager em) {


    }

}
