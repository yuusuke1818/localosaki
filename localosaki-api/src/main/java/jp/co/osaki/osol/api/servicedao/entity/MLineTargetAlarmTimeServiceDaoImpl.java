package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MLineTargetAlarmTime;
import jp.co.osaki.osol.entity.MLineTargetAlarmTimePK_;
import jp.co.osaki.osol.entity.MLineTargetAlarmTime_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物系統目標超過警報時間 EntityServiceDaoクラス
 *
 * @author t_hirata
 */
public class MLineTargetAlarmTimeServiceDaoImpl implements BaseServiceDao<MLineTargetAlarmTime> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarmTime> getResultList(MLineTargetAlarmTime t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        Long lineGroupId = t.getId().getLineGroupId();
        String lineNo = t.getId().getLineNo();

        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MLineTargetAlarmTime> query = builder.createQuery(MLineTargetAlarmTime.class);
        Root<MLineTargetAlarmTime> root = query.from(MLineTargetAlarmTime.class);

        // 条件
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MLineTargetAlarmTime_.id).get(MLineTargetAlarmTimePK_.corpId), corpId));
        whereList.add(builder.equal(
                root.get(MLineTargetAlarmTime_.id).get(MLineTargetAlarmTimePK_.buildingId), buildingId));
        whereList.add(builder.equal(
                root.get(MLineTargetAlarmTime_.id).get(MLineTargetAlarmTimePK_.lineGroupId), lineGroupId));
        whereList.add(builder.equal(
                root.get(MLineTargetAlarmTime_.id).get(MLineTargetAlarmTimePK_.lineNo), lineNo));
        whereList.add(builder.equal(
                root.get(MLineTargetAlarmTime_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MLineTargetAlarmTime> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarmTime> getResultList(List<MLineTargetAlarmTime> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarmTime> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineTargetAlarmTime find(MLineTargetAlarmTime t, EntityManager em) {
        return em.find(MLineTargetAlarmTime.class, t.getId());
    }

    @Override
    public void persist(MLineTargetAlarmTime t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MLineTargetAlarmTime merge(MLineTargetAlarmTime t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MLineTargetAlarmTime t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
