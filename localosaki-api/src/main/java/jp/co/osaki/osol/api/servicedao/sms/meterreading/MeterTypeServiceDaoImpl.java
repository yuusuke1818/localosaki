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
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 種別設定情報 取得・登録 処理 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class MeterTypeServiceDaoImpl implements BaseServiceDao<MMeterType> {

    // 複数件数取得DBアクセス処理
    // メーター種別設定 + メータ種別従量値情報 取得
    @Override
    public List<MMeterType> getResultList(
            MMeterType target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterType> query = builder.createQuery(MMeterType.class);

        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> whereList = new ArrayList<>();
        // メータ種別 (任意)
        if (target.getId().getMeterType() != null) {
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), target.getId().getMeterType()));
        }
        // 企業ID (必須)
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), target.getId().getCorpId()));
        // 建物ID (必須)
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), target.getId().getBuildingId()));
        // メータ種別・メニュー番号
        whereList.add(
                builder.or(
                        builder.and(
                                // メータ種別
                                builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), 1L),
                                // メニュー番号
                                builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), 1L, 3L)),
                        builder.and(
                                // メータ種別
                                builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), 2L, 20L),
                                // メニュー番号
                                builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), 0L))));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.corpId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo))
                );

        List<MMeterType> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MMeterType find(MMeterType target, EntityManager em) {
        // 検索
        MMeterType entity = em.find(MMeterType.class, target.getId());

        return entity;
    }

    // 更新処理 ※企業ID, 建物ID, メータ種別 を条件に更新 (注：更新条件に メニュー番号 は含めない)
    @Override
    public MMeterType merge(MMeterType target, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<MMeterType> query = cb.createCriteriaUpdate(MMeterType.class);
        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> conditionList = new ArrayList<>();

        // 条件:従量値(編集前), 企業ID, 建物ID, メータ種別, メニュー番号
        conditionList.add(cb.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), target.getId().getCorpId()));
        conditionList.add(cb.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), target.getId().getBuildingId()));
        conditionList.add(cb.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), target.getId().getMeterType()));

        // 排他制御用カラム加算
        target.setVersion(target.getVersion() + 1);

        // 更新内容をセット
        query.set(root.get(MMeterType_.recDate), target.getRecDate()); // REC_DATE
        query.set(root.get(MMeterType_.recMan), target.getRecMan()); // REC_MAN
        query.set(root.get(MMeterType_.meterTypeName), target.getMeterTypeName()); // メーター種別名称]
        query.set(root.get(MMeterType_.unitUsageBased), target.getUnitUsageBased()); // 従量単位
        query.set(root.get(MMeterType_.autoInspDay), target.getAutoInspDay()); // 自動検針日時（日）（0：日の指定なし  1～31：日の指定）
        query.set(root.get(MMeterType_.co2Coefficient), target.getCo2Coefficient()); // CO2排出係数
        query.set(root.get(MMeterType_.unitCo2Coefficient), target.getUnitCo2Coefficient()); // CO2排出係数単位
        query.set(root.get(MMeterType_.autoInspHour), target.getAutoInspHour()); // 自動検針日時（時）（0～23：時の指定）
        query.set(root.get(MMeterType_.version), target.getVersion()); // 排他制御用カラム
        query.set(root.get(MMeterType_.updateUserId), target.getUpdateUserId()); // 更新ユーザー識別ID
        query.set(root.get(MMeterType_.updateDate), target.getUpdateDate()); // 更新日時
        query.where(cb.and(conditionList.toArray(new Predicate[] {})));

        em.createQuery(query).executeUpdate();

        return target;
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MMeterType> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterType> getResultList(List<MMeterType> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterType> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MMeterType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(MMeterType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
