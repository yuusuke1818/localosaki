/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.base.Objects;

import jp.co.osaki.osol.entity.MVarious;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 各種設定情報取得処理 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class VariousServiceDaoImpl implements BaseServiceDao<MVarious> {

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MVarious find(MVarious target, EntityManager em) {
        // 検索
        MVarious entity = em.find(MVarious.class, target.getId());
        return entity;
    }

    // 更新処理
    @Override
    public MVarious merge(MVarious target, EntityManager em) {
        // DBレコード存在チェック
        MVarious entity = em.find(MVarious.class, target.getId());
        if (entity == null) {
            // 更新対象なし → 新規登録
            target.setVersion(0); // 排他制御用カラム
            em.persist(target);

            return target;
        } else {
            // 更新

            if (!Objects.equal(entity.getVersion() , target.getVersion())) {
                // 楽観ロック エラー
                return null;
            }

            // 更新データをセット
            entity.setRecDate(target.getRecDate()); // REC_DATE
            entity.setRecMan(target.getRecMan()); // REC_MAN
            entity.setSaleTaxRate(target.getSaleTaxRate()); // 消費税率
            entity.setSaleTaxDeal(target.getSaleTaxDeal()); // 消費税扱い
            entity.setDecimalFraction(target.getDecimalFraction()); // 小数部端数処理
            entity.setYearCloseMonth(target.getYearCloseMonth()); // 年報締め月
            entity.setVersion(target.getVersion() + 1); // 排他制御用カラム
            entity.setUpdateUserId(target.getUpdateUserId()); // 更新ユーザー識別ID
            entity.setUpdateDate(target.getUpdateDate()); // 更新日時

            entity = em.merge(entity);

            return entity;
        }
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MVarious> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MVarious> getResultList(
            MVarious searchResultSet, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MVarious> getResultList(List<MVarious> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MVarious> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MVarious t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MVarious t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
