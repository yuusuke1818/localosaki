package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 担当建物情報
 *
 */
public class TBuildingPersonJoinMPeronServiceDaoImpl implements BaseServiceDao<TBuildingPerson> {

    /**
     * 担当建物検索
     *
     * @param parameterMap 検索条件Map
     * @param em エンティティーマネージャ
     * @return 検索結果(複数レコード)
     */
    @Override
    public List<TBuildingPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 担当建物新規登録
     *
     * @param target 担当建物情報
     * @param em エンティティーマネージャ
     */
    @Override
    public void persist(TBuildingPerson target, EntityManager em) {
        em.persist(target);
    }

    /**
     * 担当建物検索処理(単一レコード)
     *
     * @param target 担当建物情報
     * @param em エンティティーマネージャ
     * @return 担当建物情報
     */
    @Override
    public TBuildingPerson find(TBuildingPerson target, EntityManager em) {
        return em.find(TBuildingPerson.class, target.getId());
    }

    @Override
    public TBuildingPerson merge(TBuildingPerson target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TBuildingPerson target, EntityManager em) {
        TBuildingPerson buildingPerson = em.find(TBuildingPerson.class, target.getId());
        em.remove(buildingPerson);
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingPerson> getResultList(TBuildingPerson target, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuildingPerson> query = cb.createQuery(TBuildingPerson.class);

        Root<MPerson> mPerson = query.from(MPerson.class);
        Join<MPerson, TBuildingPerson> tBuildingPerson = mPerson.join(MPerson_.TBuildingPersons);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(mPerson.get(MPerson_.id).get(MPersonPK_.corpId), target.getId().getPersonCorpId()));
        whereList.add(cb.equal(mPerson.get(MPerson_.id).get(MPersonPK_.personId), target.getId().getPersonId()));

        query = query.select(tBuildingPerson)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        List<TBuildingPerson> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    @Override
    public List<TBuildingPerson> getResultList(List<TBuildingPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
