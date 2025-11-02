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
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、装置関連テーブル 検索 ServiceDaoクラス
 * @author kobayashi.sho
 */
public class DevRelationServiceDaoImpl implements BaseServiceDao<MDevRelation> {

    /** フラグOFF. */
    public final static int FLG_OFF = 0;

    // 複数件数取得DBアクセス処理
    @Override
    public List<MDevRelation> getResultList(MDevRelation target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelation> query = builder.createQuery(MDevRelation.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        Subquery<MDevPrm> subquery = query.subquery(MDevPrm.class);
        Root<MDevPrm> devPrmRoot = subquery.from(MDevPrm.class);
        subquery.select(devPrmRoot).where(
            builder.and(
                builder.equal(devPrmRoot.get(MDevPrm_.devId), root.get(MDevRelation_.id).get(MDevRelationPK_.devId)),
                builder.equal(devPrmRoot.get(MDevPrm_.delFlg), FLG_OFF)));

        List<Predicate> whereList = new ArrayList<>();
        // 装置ID (必須)
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.corpId), target.getId().getCorpId()));
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId), target.getId().getBuildingId()));
        whereList.add(builder.exists(subquery));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(MDevRelation_.id).get(MDevRelationPK_.devId))
                        );

        return em.createQuery(query).getResultList();
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MDevRelation> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MDevRelation find(MDevRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 新規登録処理
    @Override
    public void persist(MDevRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 更新処理
    @Override
    public MDevRelation merge(MDevRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevRelation> getResultList(List<MDevRelation> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevRelation> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(MDevRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
