package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MLineTargetAlarm;
import jp.co.osaki.osol.entity.MLineTargetAlarmPK_;
import jp.co.osaki.osol.entity.MLineTargetAlarm_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物系統目標超過警報 EntityServiceDaoクラス
 *
 * @author t_hirata
 */
public class MLineTargetAlarmServiceDaoImpl implements BaseServiceDao<MLineTargetAlarm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarm> getResultList(MLineTargetAlarm t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MLineTargetAlarm> query = builder.createQuery(MLineTargetAlarm.class);

        Root<MLineTargetAlarm> root = query.from(MLineTargetAlarm.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        if (t.getId() != null && !CheckUtility.isNullOrEmpty(t.getId().getCorpId())) {
            whereList.add(builder.equal(root.get(MLineTargetAlarm_.id).get(MLineTargetAlarmPK_.corpId),
                    t.getId().getCorpId()));
        }
        //建物ID
        if (t.getId() != null && t.getId().getBuildingId() != null) {
            whereList.add(builder.equal(root.get(MLineTargetAlarm_.id).get(MLineTargetAlarmPK_.buildingId),
                    t.getId().getBuildingId()));
        }
        //系統グループID
        if (t.getId() != null && t.getId().getLineGroupId() != null) {
            whereList.add(builder.equal(root.get(MLineTargetAlarm_.id).get(MLineTargetAlarmPK_.lineGroupId),
                    t.getId().getLineGroupId()));
        }
        //系統番号
        if (t.getId() != null && !CheckUtility.isNullOrEmpty(t.getId().getLineNo())) {
            whereList.add(builder.equal(root.get(MLineTargetAlarm_.id).get(MLineTargetAlarmPK_.lineNo),
                    t.getId().getLineNo()));
        }
        //削除フラグ
        if (t.getDelFlg() != null) {
            whereList.add(builder.equal(root.get(MLineTargetAlarm_.delFlg), t.getDelFlg()));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MLineTargetAlarm> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarm> getResultList(List<MLineTargetAlarm> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTargetAlarm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineTargetAlarm find(MLineTargetAlarm t, EntityManager em) {
        return em.find(MLineTargetAlarm.class, t.getId());
    }

    @Override
    public void persist(MLineTargetAlarm t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MLineTargetAlarm merge(MLineTargetAlarm t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MLineTargetAlarm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
