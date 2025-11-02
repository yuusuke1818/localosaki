package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TBuildingDaoImpl implements BaseServiceDao<TBuilding>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);

        Root<TBuilding> root = query.from(TBuilding.class);


        List<Predicate> whereList = new ArrayList<>();

        if(target.getId().getCorpId() != null) {
            String corpId = target.getId().getCorpId();
            whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        }

        if(target.getId().getBuildingId() != null) {
            Long buildingId = target.getId().getBuildingId();
            whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
        }

        if(target.getBuildingNo() != null) {
            String buildingNo = target.getBuildingNo();
            whereList.add(builder.equal(root.get(TBuilding_.buildingNo), buildingNo));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);

        Root<TBuilding> root = query.from(TBuilding.class);


        List<Predicate> whereList = new ArrayList<>();

        if(target.getId().getCorpId() != null) {
            String corpId = target.getId().getCorpId();
            whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        }

        if(target.getId().getBuildingId() != null) {
            Long buildingId = target.getId().getBuildingId();
            whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
        }

        if(target.getBuildingNo() != null) {
            String buildingNo = target.getBuildingNo();
            whereList.add(builder.equal(root.get(TBuilding_.buildingNo), buildingNo));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {

    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {


    }

}
