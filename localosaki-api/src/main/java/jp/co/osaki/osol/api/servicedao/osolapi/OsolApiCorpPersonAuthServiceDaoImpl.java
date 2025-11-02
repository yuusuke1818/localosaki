package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MCorpPersonAuthPK_;
import jp.co.osaki.osol.entity.MCorpPersonAuth_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業担当者権限
 *
 */
public class OsolApiCorpPersonAuthServiceDaoImpl implements BaseServiceDao<MCorpPersonAuth> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MCorpPersonAuth> getResultList(MCorpPersonAuth target, EntityManager em) {
        return null;
    }

    @Override
    public List<MCorpPersonAuth> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MCorpPersonAuth> getResultList(List<MCorpPersonAuth> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MCorpPersonAuth> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MCorpPersonAuth find(MCorpPersonAuth target, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MCorpPersonAuth> cq = cb.createQuery(MCorpPersonAuth.class);
        Root<MCorpPersonAuth> root = cq.from(MCorpPersonAuth.class);
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(MCorpPersonAuth_.id).get(MCorpPersonAuthPK_.corpId), target.getId().getCorpId()));
        whereList.add(cb.equal(root.get(MCorpPersonAuth_.id).get(MCorpPersonAuthPK_.personCorpId), target.getId().getPersonCorpId()));
        whereList.add(cb.equal(root.get(MCorpPersonAuth_.id).get(MCorpPersonAuthPK_.personId), target.getId().getPersonId()));
        whereList.add(cb.equal(root.get(MCorpPersonAuth_.id).get(MCorpPersonAuthPK_.authorityCd), target.getId().getAuthorityCd()));
        whereList.add(cb.equal(root.get(MCorpPersonAuth_.delFlg), 0));
        cq = cq.select(root).where(cb.and(whereList.toArray(new Predicate[]{})));

        List<MCorpPersonAuth> list = em.createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void persist(MCorpPersonAuth target, EntityManager em) {

    }

    @Override
    public MCorpPersonAuth merge(MCorpPersonAuth target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MCorpPersonAuth target, EntityManager em) {

    }

}
