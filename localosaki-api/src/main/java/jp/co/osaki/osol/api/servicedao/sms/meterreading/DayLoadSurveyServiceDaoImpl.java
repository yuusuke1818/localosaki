/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * ロードサーベイ日データ 検索 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class DayLoadSurveyServiceDaoImpl implements BaseServiceDao<TDayLoadSurvey> {

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public TDayLoadSurvey find(TDayLoadSurvey target, EntityManager em) {
        return em.find(TDayLoadSurvey.class, target.getId());
    }

    // 新規登録処理
    @Override
    public void persist(TDayLoadSurvey target, EntityManager em) {
        em.persist(target);
    }

    // 更新処理
    @Override
    public TDayLoadSurvey merge(TDayLoadSurvey target, EntityManager em) {
        return em.merge(target);
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TDayLoadSurvey> getResultList(TDayLoadSurvey target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TDayLoadSurvey> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurvey> getResultList(List<TDayLoadSurvey> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurvey> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(TDayLoadSurvey t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
