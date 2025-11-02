package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MClaimantInfo;
import jp.co.osaki.osol.entity.MClaimantInfoPK;
import jp.co.osaki.osol.entity.MClaimantInfoPK_;
import jp.co.osaki.osol.entity.MClaimantInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 請求者情報
 *
 * @author hosono.s
 */
public class MClaimantInfoServiceDaoImpl implements BaseServiceDao<MClaimantInfo> {
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MClaimantInfo> getResultList(MClaimantInfo target, EntityManager em) {

        return null;
    }

    @Override
    public List<MClaimantInfo> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MClaimantInfo> getResultList(List<MClaimantInfo> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MClaimantInfo> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MClaimantInfo find(MClaimantInfo target, EntityManager em) {
        String corpId = target.getId().getCorpId();
        Long buildingId = target.getId().getBuildingId();
        String personCorpId = target.getId().getPersonCorpId();
        String personId = target.getId().getPersonId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MClaimantInfo> query = builder.createQuery(MClaimantInfo.class);

        Root<MClaimantInfo> root = query.from(MClaimantInfo.class);

        List<Predicate> whereList = new ArrayList<>();
        //企業ID
        whereList.add(builder.equal(root.get(MClaimantInfo_.id).get(MClaimantInfoPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MClaimantInfo_.id).get(MClaimantInfoPK_.buildingId), buildingId));
        //担当企業ID
        whereList.add(builder.equal(root.get(MClaimantInfo_.id).get(MClaimantInfoPK_.personCorpId), personCorpId));
        //担当者ID
        whereList.add(builder.equal(root.get(MClaimantInfo_.id).get(MClaimantInfoPK_.personId), personId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        List<MClaimantInfo> list = em.createQuery(query).getResultList();

        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            MClaimantInfoPK pk = new MClaimantInfoPK();
            MClaimantInfo info = new MClaimantInfo();
            info.setId(pk);
            return info;
        }
    }

    @Override
    public void persist(MClaimantInfo target, EntityManager em) {

        em.persist(target);
    }

    @Override
    public MClaimantInfo merge(MClaimantInfo target, EntityManager em) {

        return em.merge(target);
    }

    @Override
    public void remove(MClaimantInfo target, EntityManager em) {


    }
}
