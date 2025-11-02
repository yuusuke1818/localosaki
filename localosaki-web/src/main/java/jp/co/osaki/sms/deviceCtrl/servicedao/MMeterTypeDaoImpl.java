package jp.co.osaki.sms.deviceCtrl.servicedao;

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

public class MMeterTypeDaoImpl implements BaseServiceDao<MMeterType>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MMeterType> getResultList(MMeterType target, EntityManager em) {
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterType> query = builder.createQuery(MMeterType.class);

        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), buildingId));

        if(target.getId().getMeterType() != null) {
            Long meterType = target.getId().getMeterType();
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), meterType));
        }


        if(target.getId().getMenuNo() != null) {
            Long menuNo = target.getId().getMenuNo();
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), menuNo));
        }

        if(target.getId().getCorpId() != null) {
            String corpId = target.getId().getCorpId();
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), corpId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

        return em.createQuery(query).getResultList();
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
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterType> query = builder.createQuery(MMeterType.class);

        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), meterType));
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), buildingId));

        if(target.getId().getMenuNo() != null) {
            Long menuNo = target.getId().getMenuNo();
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), menuNo));
        }

        if(target.getId().getCorpId() != null) {
            String corpId = target.getId().getCorpId();
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), corpId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MMeterType target, EntityManager em) {
        em.persist(target);
        return ;
    }

    @Override
    public MMeterType merge(MMeterType target, EntityManager em) {

        return em.merge(target);
    }

    @Override
    public void remove(MMeterType target, EntityManager em) {
        em.remove(target);
        return;
    }

}
