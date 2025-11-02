package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK_;
import jp.co.osaki.osol.entity.MMeterGroup_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MMeterGroupServiceDaoImpl implements BaseServiceDao<MMeterGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(MMeterGroup target, EntityManager em) {
        final String devId = target.getId().getDevId();
        final Long meterMngId = target.getId().getMeterMngId();

        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterGroup> query = builder.createQuery(MMeterGroup.class);

        Root<MMeterGroup> root = query.from(MMeterGroup.class);

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 条件1: 装置IDが引数に一致
        whereList
                .add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.devId), devId));
        // 条件2: メーター管理番号が引数に一致
        whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterMngId),
                meterMngId));

        query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeterGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(List<MMeterGroup> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeterGroup find(MMeterGroup target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(MMeterGroup target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public MMeterGroup merge(MMeterGroup target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(MMeterGroup target, EntityManager em) {
        em.remove(target);
    }

}
