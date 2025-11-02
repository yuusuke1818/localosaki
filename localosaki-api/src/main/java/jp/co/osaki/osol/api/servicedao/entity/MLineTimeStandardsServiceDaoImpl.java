package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MLineTimeStandard;
import jp.co.osaki.osol.entity.MLineTimeStandardPK_;
import jp.co.osaki.osol.entity.MLineTimeStandard_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物系統時限標準値テーブル EntityServiceDaoクラス
 *
 * @author t_hirata
 */
public class MLineTimeStandardsServiceDaoImpl implements BaseServiceDao<MLineTimeStandard> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTimeStandard> getResultList(MLineTimeStandard t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MLineTimeStandard> query = builder.createQuery(MLineTimeStandard.class);

        Root<MLineTimeStandard> root = query.from(MLineTimeStandard.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        if (t.getId() != null && !CheckUtility.isNullOrEmpty(t.getId().getCorpId())) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.corpId),
                    t.getId().getCorpId()));
        }
        //建物ID
        if (t.getId() != null && t.getId().getBuildingId() != null) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.buildingId),
                    t.getId().getBuildingId()));
        }
        //系統グループID
        if (t.getId() != null && t.getId().getLineGroupId() != null) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineGroupId),
                    t.getId().getLineGroupId()));
        }
        //系統番号
        if (t.getId() != null && !CheckUtility.isNullOrEmpty(t.getId().getLineNo())) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineNo),
                    t.getId().getLineNo()));
        }
        //時限No
        if (t.getId() != null && t.getId().getJigenNo() != null) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.jigenNo),
                    t.getId().getJigenNo()));
        }
        //削除フラグ
        if (t.getDelFlg() != null) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.delFlg), t.getDelFlg()));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MLineTimeStandard> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTimeStandard> getResultList(List<MLineTimeStandard> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineTimeStandard> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineTimeStandard find(MLineTimeStandard t, EntityManager em) {
        return em.find(MLineTimeStandard.class, t.getId());
    }

    @Override
    public void persist(MLineTimeStandard t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MLineTimeStandard merge(MLineTimeStandard t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MLineTimeStandard t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
