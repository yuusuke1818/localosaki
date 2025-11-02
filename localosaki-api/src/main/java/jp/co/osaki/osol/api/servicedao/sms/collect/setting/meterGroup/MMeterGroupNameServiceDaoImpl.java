/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.entity.MMeterGroupName;
import jp.co.osaki.osol.entity.MMeterGroupNamePK_;
import jp.co.osaki.osol.entity.MMeterGroupName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ名設定 ServiceDaoクラス
 * @author maruta.y
 *
 */
public class MMeterGroupNameServiceDaoImpl implements BaseServiceDao<MMeterGroupName> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroupName> getResultList(MMeterGroupName target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroupName> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        String corpId = parameterMap.get(BuildingPersonDevDataParam.CORP_ID).get(0).toString();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterGroupName> query = builder.createQuery(MMeterGroupName.class);
        Root<MMeterGroupName> root = query.from(MMeterGroupName.class);

        //検索条件
        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId), corpId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<MMeterGroupName> getResultList(List<MMeterGroupName> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroupName> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeterGroupName find(MMeterGroupName target, EntityManager em) {
        return em.find(MMeterGroupName.class, target.getId());
    }

    @Override
    public void persist(MMeterGroupName target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeterGroupName merge(MMeterGroupName target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeterGroupName target, EntityManager em) {
        em.remove(target);
    }
}
