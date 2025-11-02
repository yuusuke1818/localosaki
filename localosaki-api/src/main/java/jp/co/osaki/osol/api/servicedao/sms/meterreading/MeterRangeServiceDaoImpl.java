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

import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK_;
import jp.co.osaki.osol.entity.MMeterRange_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メータ種別従量値情報 取得・登録 処理 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class MeterRangeServiceDaoImpl implements BaseServiceDao<MMeterRange> {

    // 複数件数取得DBアクセス処理
    // メーター種別設定 + メータ種別従量値情報 取得
    @Override
    public List<MMeterRange> getResultList(
            MMeterRange target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterRange> query = builder.createQuery(MMeterRange.class);

        Root<MMeterRange> root = query.from(MMeterRange.class);

        List<Predicate> whereList = new ArrayList<>();
        // メーター種別
        if (target.getId().getMeterType() != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.meterType), target.getId().getMeterType()));
        }
        // 企業ID
        whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.corpId), target.getId().getCorpId()));
        // 建物ID
        whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.buildingId), target.getId().getBuildingId()));
        // メニュー番号
        if (target.getId().getMenuNo() != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), target.getId().getMenuNo()));
        }
        // メータ種別・メニュー番号
        whereList.add(
                builder.or(
                        builder.and(
                                // メータ種別
                                builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.meterType), 1L),
                                // メニュー番号
                                builder.between(root.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), 1L, 3L)),
                        builder.and(
                                // メータ種別
                                builder.between(root.get(MMeterRange_.id).get(MMeterRangePK_.meterType), 2L, 20L),
                                // メニュー番号
                                builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), 0L))));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.meterType)),
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.corpId)),
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.buildingId)),
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.menuNo)),
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.rangeValue))
                );
        List<MMeterRange> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MMeterRange find(MMeterRange target, EntityManager em) {
        // DBレコード存在チェック
        return em.find(MMeterRange.class, target.getId());
    }

    // 削除処理
    @Override
    public void remove(MMeterRange target, EntityManager em) {
        em.remove(target);
    }

    // 新規登録処理
    @Override
    public void persist(MMeterRange target, EntityManager em) {
        em.persist(target);
    }

    // 更新処理
    // @return null:更新エラー
    @Override
    public MMeterRange merge(MMeterRange target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MMeterRange> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterRange> getResultList(List<MMeterRange> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterRange> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
