package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MMeterTypeServiceDaoImpl implements BaseServiceDao<MMeterType> {
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MMeterType> getResultList(MMeterType target, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterType> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterType> getResultList(List<MMeterType> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterType> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MMeterType find(MMeterType target, EntityManager em) {
        Long meterType = target.getId().getMeterType();
        String corpId = target.getId().getCorpId();
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterType> query = builder.createQuery(MMeterType.class);

        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> whereList = new ArrayList<>();
        //メーター種別
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), meterType));
        //企業ID
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), buildingId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MMeterType target, EntityManager em) {

    }

    @Override
    public MMeterType merge(MMeterType target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MMeterType target, EntityManager em) {


    }
}
