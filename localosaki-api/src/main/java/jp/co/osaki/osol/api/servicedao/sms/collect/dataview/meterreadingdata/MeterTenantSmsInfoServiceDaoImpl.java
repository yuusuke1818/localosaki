package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MeterTenantSmsInfoServiceDaoImpl implements BaseServiceDao<MTenantSm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<MTenantSm> getResultList(MTenantSm target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<MTenantSm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        //Mapからリクエストを取得する
        List<Object> parameterList =  parameterMap.get("conditions");
        String parameter = (String) parameterList.get(0);

        String corpId = parameter;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MTenantSm> query = builder.createQuery(MTenantSm.class);
        Root<MTenantSm> root = query.from(MTenantSm.class);

        List<Predicate> whereList = new ArrayList<>();

        // 建物.企業ID（テナント）
        whereList.add(builder.equal(root.get(MTenantSm_.id).get(MTenantSmPK_.corpId), corpId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MTenantSm> getResultList(List<MTenantSm> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<MTenantSm> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public MTenantSm find(MTenantSm target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(MTenantSm target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public MTenantSm merge(MTenantSm target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(MTenantSm target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
