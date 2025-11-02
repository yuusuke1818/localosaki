package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GetMeterLatestInspDateServiceDaoImpl implements BaseServiceDao<TInspectionMeterSvr> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
// select
//  a.insp_year,
//  a.insp_month,
//  a.insp_month_no
// from
//  t_inspection_meter_svr as a
// where
//  exists (
//      select
//          *
//      from
//          m_meter as b
//      where
//          a.dev_id = b.dev_id and
//          a.meter_mng_id = b.meter_mng_id and
//          dev_id = $1 and
//          meter_mng_id = $2 and
//          meter_type = $3
//  )
// order by
//  a.insp_year desc,
//  a.insp_month desc,
//  a.insp_month_no desc
// limit 1
// ;
    @Override
    public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

        // ■from句の整理
        // [MuDM2]ロードサーベイ日データTBL
        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        // ■SUBQUERY句の整理
        Subquery<MMeter> subQuery = query.subquery(MMeter.class);
        Root<MMeter> subQueryRoot = subQuery.from(MMeter.class);

        List<Predicate> subQueryWhereList = new ArrayList<>();
        subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.devId), root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)));
        subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        List<Object> paramList = parameterMap.get("select");
        if (paramList != null) {
            MMeter target = (MMeter) paramList.get(0);
            subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.devId), target.getId().getDevId()));
            subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.meterMngId), target.getId().getMeterMngId()));
            subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.meterType), target.getMeterType()));
        }
        subQuery.select(subQueryRoot);
        subQuery.where(builder.and(subQueryWhereList.toArray(new Predicate[] {})));

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.exists(subQuery));

        // ■SELECT句の整理
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

        // ■ORDER BY句の整理
        query.orderBy(
                builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear)),
                builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class)),
                builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));

        return em.createQuery(query).setMaxResults(1).getResultList();
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(List<TInspectionMeterSvr> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TInspectionMeterSvr find(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TInspectionMeterSvr merge(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

}
