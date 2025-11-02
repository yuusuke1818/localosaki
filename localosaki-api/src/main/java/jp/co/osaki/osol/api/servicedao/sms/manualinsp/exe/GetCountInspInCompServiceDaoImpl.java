package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.math.BigDecimal;
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

public class GetCountInspInCompServiceDaoImpl implements BaseServiceDao<Long> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
//  SELECT
//      COUNT(*)
//    FROM
//      t_inspection_meter_svr a
//    WHERE
//      EXISTS(
//        SELECT
//          *
//        FROM
//          m_meter b
//        WHERE
//          a.dev_id = b.dev_id
//          AND a.dev_id = b.dev_id
//          AND b.meter_type = 1 /* $5 */
//      )
//
//      AND a.dev_id = '' /* $1 */
//      AND a.meter_mng_id = '' /* $2 */
//      AND a.insp_year = '' /* $3 */
//      AND a.insp_month = '' /* $4 */
//      AND a.insp_month_no = '' /* $5 */
//      AND (a.end_flg <> 1 or a.end_flg is null)
    @Override
    public List<Long> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        // ■from句の整理
        // [MuDM2]ロードサーベイ日データTBL
        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        // ■SUBQUERY句の整理
        Subquery<MMeter> subQuery = query.subquery(MMeter.class);
        Root<MMeter> subQueryRoot = subQuery.from(MMeter.class);

        List<Predicate> subQueryWhereList = new ArrayList<>();
        subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.devId), root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)));
        subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        List<Object> paramList = parameterMap.get("meter_type");
        if (paramList != null) {
            Long type = (Long) paramList.get(0);
            subQueryWhereList.add(builder.equal(subQueryRoot.get(MMeter_.meterType), type));
        }
        subQuery.select(subQueryRoot);
        subQuery.where(builder.and(subQueryWhereList.toArray(new Predicate[] {})));

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.exists(subQuery));
        List<Object> paramList2 = parameterMap.get("meter_svr");
        if (paramList2 != null) {
            TInspectionMeterSvr target = (TInspectionMeterSvr) paramList2.get(0);
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getId().getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getId().getMeterMngId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getId().getInspYear()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getId().getInspMonth()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), target.getId().getInspMonthNo()));
        }
        whereList.add(builder.or(
                builder.isNull(root.get(TInspectionMeterSvr_.endFlg)),
                builder.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        // ■SELECT句の整理
        query = query.select(builder.construct(Long.class,
                builder.count(root)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        // ■ORDER BY句の整理

        return em.createQuery(query).setMaxResults(1).getResultList();
    }

    @Override
    public List<Long> getResultList(List<Long> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long find(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long merge(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

}
