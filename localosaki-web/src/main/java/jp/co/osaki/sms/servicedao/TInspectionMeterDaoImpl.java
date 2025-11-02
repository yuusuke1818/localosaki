package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.osol.entity.TInspectionMeterPK_;
import jp.co.osaki.osol.entity.TInspectionMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TInspectionMeterDaoImpl implements BaseServiceDao<TInspectionMeter>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TInspectionMeter> getResultList(TInspectionMeter target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeter> query = builder.createQuery(TInspectionMeter.class);

        Root<TInspectionMeter> root = query.from(TInspectionMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.devId), devId));

        if(target.getId().getInspYear() != null) {
            String inspYear = target.getId().getInspYear();
            whereList.add(builder.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspYear), inspYear));
        }

        if(target.getId().getInspMonth() != null) {
            String inspMonth = target.getId().getInspMonth();
            whereList.add(builder.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspMonth), inspMonth));
        }

        if(target.getInspType() != null) {
            String inspType = target.getInspType();
            whereList.add(builder.equal(root.get(TInspectionMeter_.inspType), inspType));
        }

        if(target.getId().getInspYear() == null && target.getId().getInspMonth() == null) {
            //ハンディ端末コマンド用
            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).
                    orderBy(builder.asc(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.meterMngId)),
                            builder.desc(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspYear)),
                            builder.desc(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspMonth)),
                            builder.desc(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspMonthNo)));
        }else {
            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        }


        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TInspectionMeter> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TInspectionMeter> getResultList(List<TInspectionMeter> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TInspectionMeter> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TInspectionMeter find(TInspectionMeter target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(TInspectionMeter target, EntityManager em) {
        em.persist(target);
        return ;
    }

    @Override
    public TInspectionMeter merge(TInspectionMeter target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TInspectionMeter target, EntityManager em) {
        em.remove(target);
        return ;

    }

}
