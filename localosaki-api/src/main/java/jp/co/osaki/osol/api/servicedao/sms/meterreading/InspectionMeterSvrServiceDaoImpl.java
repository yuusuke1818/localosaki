/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 検針結果データ(サーバ用) 登録 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class InspectionMeterSvrServiceDaoImpl implements BaseServiceDao<TInspectionMeterSvr> {

    // 新規登録処理
    @Override
    public void persist(TInspectionMeterSvr target, EntityManager em) {
        em.persist(target);
    }

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public TInspectionMeterSvr find(TInspectionMeterSvr target, EntityManager em) {
        return em.find(TInspectionMeterSvr.class, target.getId());
    }

    // 更新処理
    @Override
    public TInspectionMeterSvr merge(TInspectionMeterSvr target, EntityManager em) {
        return em.merge(target);
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(List<TInspectionMeterSvr> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(TInspectionMeterSvr t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
