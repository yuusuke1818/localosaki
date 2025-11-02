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

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針実行 TInspectionMeterSvr ServiceDaoクラス
 *
 * @author tominaga.d
 */
public class TInspectionMeterSvrServiceDaoImpl implements BaseServiceDao<TInspectionMeterSvr> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
//  SELECT
//      *
//    FROM
//      t_inspection_meter_svr
//    WHERE
//      dev_id = '' /* $1 */
//      AND meter_mng_id = 1 /* $2 */
//      AND end_flg = 1
//    ORDER BY
//      insp_year desc,
//      insp_month desc,
//      insp_month_no desc
//    LIMIT 1
    @Override
    public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target, EntityManager em) {

        if (target.getId().getInspYear() == null) {
            // クエリ生成
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

            // ■from句の整理
            // [MuDM2] 検針結果データ(サーバ用)
            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            // ■WHERE句の整理
            List<Predicate> whereList = new ArrayList<>();
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getId().getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getId().getMeterMngId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE));

            // ■SELECT句の整理
            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

            // ■ORDER BY句の整理
            query.orderBy(
                    builder.desc(
                            root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear)),
                    builder.desc(
                            root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class)),
                    builder.desc(
                            root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));

            return em.createQuery(query).setMaxResults(1).getResultList();

        } else {

            String inspMonth = Integer.valueOf(target.getId().getInspMonth()).toString(); // ゼロサプレス

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            List<Predicate> whereList = new ArrayList<>();

            //装置ID
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getId().getDevId()));
            //メーター管理番号
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getId().getMeterMngId()));

            // 年月をまたいでも抽出可能ｒとなるようにコメントアウト
//            //表示年
//            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getId().getInspYear()));
//            //表示月
//            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));

            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

            return em.createQuery(query).getResultList();
        }
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
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
        em.persist(target);
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
