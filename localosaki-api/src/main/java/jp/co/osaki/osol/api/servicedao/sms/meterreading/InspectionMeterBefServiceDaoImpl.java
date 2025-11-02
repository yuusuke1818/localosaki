/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK_;
import jp.co.osaki.osol.entity.TInspectionMeterBef_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 確定前検針データ 検索・登録・更新・削除 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class InspectionMeterBefServiceDaoImpl implements BaseServiceDao<TInspectionMeterBef> {

    // 複数件数取得DBアクセス処理
    // 確定前検針データ一覧 取得
    @Override
    public List<TInspectionMeterBef> getResultList(TInspectionMeterBef target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterBef> query = builder.createQuery(TInspectionMeterBef.class);

        Root<TInspectionMeterBef> root = query.from(TInspectionMeterBef.class);

        List<Predicate> whereList = new ArrayList<>();
        // 装置ID (必須)
        whereList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.devId), target.getId().getDevId()));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.devId)),
                        builder.asc(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.meterMngId)),
                        builder.asc(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.latestInspDate))
                        );

        List<TInspectionMeterBef> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    // 削除処理
    @Override
    public void remove(TInspectionMeterBef target, EntityManager em) {
        em.remove(target);
    }

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public TInspectionMeterBef find(TInspectionMeterBef target, EntityManager em) {
        return em.find(TInspectionMeterBef.class, target.getId());
    }

    // 更新処理
    @Override
    public TInspectionMeterBef merge(TInspectionMeterBef target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TInspectionMeterBef> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterBef> getResultList(List<TInspectionMeterBef> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterBef> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TInspectionMeterBef t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
