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

import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK_;
import jp.co.osaki.osol.entity.MConcentrator_;
import jp.skygroup.enl.webap.base.BaseServiceDao;
/**
 * コンセントレーターDaoクラス
 *
 * @author hayashi_tak
 */
public class MConcentratorDaoImpl implements BaseServiceDao<MConcentrator> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MConcentrator> getResultList(MConcentrator target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MConcentrator> query = builder.createQuery(MConcentrator.class);

        Root<MConcentrator> root = query.from(MConcentrator.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.devId), devId));

        if(target.getId().getConcentId() != null) {
            Long concentId = target.getId().getConcentId();
            whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.concentId), concentId));
        }

        if(target.getCommandFlg() != null) {
            String cmdFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MConcentrator_.commandFlg), cmdFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MConcentrator_.srvEnt), srvEnt));
        }

        if(target.getConcentSta() != null) {
            BigDecimal consentSta = target.getConcentSta();
            whereList.add(builder.equal(root.get(MConcentrator_.concentSta), consentSta));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MConcentrator_.id).get(MConcentratorPK_.concentId)));
        return   em.createQuery(query).getResultList();

    }

    @Override
    public List<MConcentrator> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MConcentrator> getResultList(List<MConcentrator> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MConcentrator> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MConcentrator find(MConcentrator target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long concentId = target.getId().getConcentId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MConcentrator> query = builder.createQuery(MConcentrator.class);

        Root<MConcentrator> root = query.from(MConcentrator.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.devId), devId));
        whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.concentId), concentId));

        if(target.getCommandFlg() != null) {
            String cmdFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MConcentrator_.commandFlg), cmdFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MConcentrator_.srvEnt), srvEnt));
        }

        if(target.getConcentSta() != null) {
            BigDecimal consentSta = target.getConcentSta();
            whereList.add(builder.equal(root.get(MConcentrator_.concentSta), consentSta));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MConcentrator target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MConcentrator merge(MConcentrator target, EntityManager em) {

        return em.merge(target);
    }


    @Override
    public void remove(MConcentrator target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return ;
    }




}
