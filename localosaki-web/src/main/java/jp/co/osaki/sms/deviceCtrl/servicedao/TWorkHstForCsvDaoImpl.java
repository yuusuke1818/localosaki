package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK_;
import jp.co.osaki.osol.entity.TWorkHst_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TWorkHstForCsvDaoImpl implements BaseServiceDao<TWorkHst> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        int ret = 0;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<TWorkHst> update = builder.createCriteriaUpdate(TWorkHst.class);

        Root<TWorkHst> root = update.from(TWorkHst.class);

        List<Predicate> whereList = new ArrayList<>();
        if (parameterMap.get("target") != null) {
            TWorkHst target = (TWorkHst) parameterMap.get("target").get(0);
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), target.getId().getDevId()));
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.command), target.getId().getCommand()));
            whereList.add(builder.equal(root.get(TWorkHst_.filePath), target.getFilePath()));

            update.set(root.get(TWorkHst_.srvEnt), target.getSrvEnt())
                    .set(root.get(TWorkHst_.recMan), target.getRecMan())
                    .set(root.get(TWorkHst_.writeDate), target.getWriteDate())
                    .set(root.get(TWorkHst_.id).get(TWorkHstPK_.recDate), target.getId().getRecDate())
                    .where(builder.and(whereList.toArray(new Predicate[] {})));
            ret = em.createQuery(update).executeUpdate();
        }
        return ret;
    }

    @Override
    public List<TWorkHst> getResultList(TWorkHst target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TWorkHst> query = builder.createQuery(TWorkHst.class);

        Root<TWorkHst> root = query.from(TWorkHst.class);

        List<Predicate> whereList = new ArrayList<>();

        if (target.getId().getDevId() != null) {
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), target.getId().getDevId()));
        }
        if (target.getId().getCommand() != null) {
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.command), target.getId().getCommand()));
        }
        if (target.getSrvEnt() != null) {
            whereList.add(builder.equal(root.get(TWorkHst_.srvEnt), target.getSrvEnt()));
        }
        if (target.getFilePath() != null) {
            whereList.add(builder.equal(root.get(TWorkHst_.filePath), target.getFilePath()));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TWorkHst> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWorkHst> getResultList(List<TWorkHst> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWorkHst> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TWorkHst find(TWorkHst target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TWorkHst target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TWorkHst merge(TWorkHst target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TWorkHst target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

}
