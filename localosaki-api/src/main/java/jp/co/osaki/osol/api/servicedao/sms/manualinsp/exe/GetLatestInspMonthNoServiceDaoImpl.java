package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針実行 月検診連番取得 ServiceDaoクラス
 * @author tominaga.d
 */
public class GetLatestInspMonthNoServiceDaoImpl implements BaseServiceDao<Long>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
//  SELECT
//      COALESCE(MAX(insp_month_no),0)
//    FROM
//      t_inspection_meter_svr
//    WHERE
//      dev_id = '' /* $1 */
//      AND insp_year = '' /* $2 */
//      AND insp_month = '' /* $3 */
    @Override
    public List<Long> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        // ■from句の整理
        // [MuDM2]ロードサーベイ日データTBL
        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);


        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        List<Object> paramList = parameterMap.get("select");
        if(paramList != null) {
            TInspectionMeterSvr target = (TInspectionMeterSvr)paramList.get(0);
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getId().getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getId().getInspYear()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getId().getInspMonth()));

        }

        // ■SELECT句の整理
        query = query.select(builder.construct(Long.class,
                builder.coalesce(
                        builder.max(
                                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)
                                ), 0)
                )).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Long> getResultList(List<Long> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long find(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long merge(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Long target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
