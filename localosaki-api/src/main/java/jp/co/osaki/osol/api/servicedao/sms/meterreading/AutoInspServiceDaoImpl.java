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

import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 自動検針月 日時設定 検索 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class AutoInspServiceDaoImpl implements BaseServiceDao<MAutoInsp> {

    /** 検索条件: デバイスID. */
    public static final String DEV_ID = "devId";

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MAutoInsp find(MAutoInsp target, EntityManager em) {
        return em.find(MAutoInsp.class, target.getId());
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MAutoInsp> getResultList(Map<String, List<Object>> map, EntityManager em) {
        List<Object> devIdList = map.get(DEV_ID);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAutoInsp> query = builder.createQuery(MAutoInsp.class);
        Root<MAutoInsp> root = query.from(MAutoInsp.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(root.get(MAutoInsp_.id).get(MAutoInspPK_.devId).in(devIdList));

        query = query.select(root)
            .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MAutoInsp> getResultList(MAutoInsp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 更新処理
    @Override
    public MAutoInsp merge(MAutoInsp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(List<MAutoInsp> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MAutoInsp t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(MAutoInsp t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
