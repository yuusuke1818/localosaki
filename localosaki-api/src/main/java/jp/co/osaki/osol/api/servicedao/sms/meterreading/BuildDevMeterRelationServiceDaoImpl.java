package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、メーター関連テーブル 検索 ServiceDaoクラス
 * @author kobayashi.sho
 */
public class BuildDevMeterRelationServiceDaoImpl implements BaseServiceDao<TBuildDevMeterRelation> {

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public TBuildDevMeterRelation find(TBuildDevMeterRelation target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildDevMeterRelation> query = builder.createQuery(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);
        Join<TBuildDevMeterRelation, TBuilding> joinBuilding = root.join(TBuildDevMeterRelation_.TBuilding, JoinType.LEFT);
        Join<TBuilding, MTenantSm> joinTenantSms = joinBuilding.join(TBuilding_.MTenantSms, JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.corpId),
                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)));
        whereList.add(builder.equal(
                root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));
        whereList.add(builder.equal(
                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.corpId)));
        whereList.add(builder.equal(
                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.buildingId)));

        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.corpId), target.getId().getCorpId())); // 企業ID
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), target.getId().getDevId())); // 装置ID
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId), target.getId().getMeterMngId())); // メーター管理番号

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.desc(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId)));

        List<TBuildDevMeterRelation> resultList = em.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TBuildDevMeterRelation> getResultList(TBuildDevMeterRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(TBuildDevMeterRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 更新処理
    @Override
    public TBuildDevMeterRelation merge(TBuildDevMeterRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TBuildDevMeterRelation> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(List<TBuildDevMeterRelation> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TBuildDevMeterRelation target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
