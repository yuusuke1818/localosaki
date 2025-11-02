package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;
/**
 * @author hayashi_tak
 *
 */
public class MMeterDaoImpl implements BaseServiceDao<MMeter>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MMeter> getResultList(MMeter target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = builder.createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

        if(target.getDelFlg() != null) {
            Integer delFlg = target.getDelFlg();
            whereList.add(builder.equal(root.get(MMeter_.delFlg), delFlg));
        }
        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.meterMngId), meterMngId));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MMeter_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MMeter_.srvEnt), srvEnt));
        }

        if(target.getMeterId() != null) {
            String meterId = target.getMeterId();
            whereList.add(builder.equal(root.get(MMeter_.meterId), meterId));
        }

        if(target.getMeterSta() != null) {
            BigDecimal meterSta = target.getMeterSta();
            whereList.add(builder.equal(root.get(MMeter_.meterSta), meterSta));
        }

        if(target.getTermSta() != null) {
            BigDecimal termSta = target.getTermSta();
            whereList.add(builder.equal(root.get(MMeter_.termSta), termSta));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeter> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeter> getResultList(List<MMeter> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeter> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MMeter find(MMeter target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = builder.createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.meterMngId), meterMngId));

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MMeter_.commandFlg), commandFlg));
        }
        if(target.getDelFlg() != null) {
            Integer delFlg = target.getDelFlg();
            whereList.add(builder.equal(root.get(MMeter_.delFlg), delFlg));
        }
        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MMeter_.srvEnt), srvEnt));
        }
        if(target.getMeterId() != null) {
            String meterId = target.getMeterId();
            whereList.add(builder.equal(root.get(MMeter_.meterId), meterId));
        }

        if(target.getMeterSta() != null) {
            BigDecimal meterSta = target.getMeterSta();
            whereList.add(builder.equal(root.get(MMeter_.meterSta), meterSta));
        }

        if(target.getTermSta() != null) {
            BigDecimal termSta = target.getTermSta();
            whereList.add(builder.equal(root.get(MMeter_.termSta), termSta));
        }
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }


    @Override
    public void persist(MMeter target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeter merge(MMeter target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeter target, EntityManager em) {
        em.remove(target);
    }

}
