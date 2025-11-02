/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物 ServiceDaoクラス
 * @author sagi_h
 *
 */
public class TBuildingServiceDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        final String devId = (String) parameterMap.get("devId").get(0);
        final MDevRelation mdr = getDivisionBuilding(devId, em);
        Long tenantId = null;

        if (parameterMap.get("tenantId") != null) {
            tenantId = (Long) parameterMap.get("tenantId").get(0);
        }

        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);

        Root<TBuilding> root = query.from(TBuilding.class);

        // テナント情報テーブル
        Join<TBuilding, MTenantSm> joinTenantSms;

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 条件1: 建物の削除フラグがOFF
        whereList.add(builder.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // 条件2: 建物の削除日時がNULL
        whereList.add(builder.isNull(root.get(TBuilding_.buildingDelDate)));

        if (parameterMap.get("tenantId") != null) {
            // 当該ユーザーコードのテナントを返す
            // m_tenant_smsと結合(企業ID、建物ID、パラメーターのユーザーコード、削除フラグ)
            joinTenantSms = root.join(TBuilding_.MTenantSms, JoinType.INNER);
            joinTenantSms.on(builder.and(
                    builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                            joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.corpId)),
                    builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                            joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.buildingId)),
                    builder.equal(joinTenantSms.get(MTenantSm_.tenantId), tenantId),
                    builder.equal(joinTenantSms.get(MTenantSm_.delFlg), OsolConstants.FLG_OFF)));
            // 条件3: 所属企業IDが建物装置関係から取得した企業ID
            whereList.add(builder.equal(root.get(TBuilding_.divisionCorpId), mdr.getId().getCorpId()));

            // 条件4: 所属建物IDが建物装置関係から取得した建物ID
            whereList.add(
                    builder.equal(root.get(TBuilding_.divisionBuildingId), mdr.getId().getBuildingId()));
        } else {
            // ユーザーコードが与えられない場合は、装置と紐づく建物を返す
            // 条件3: 企業IDが建物装置関係から取得した企業ID
            whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), mdr.getId().getCorpId()));

            // 条件4: 建物IDが建物装置関係から取得した建物ID
            whereList.add(
                    builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), mdr.getId().getBuildingId()));
        }

        query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        return em.find(TBuilding.class, target.getId());
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 装置IDと対応する建物を返す
     * @param devId 装置ID
     * @param em エンティティマネージャー
     * @return 対応する建物
     */
    private MDevRelation getDivisionBuilding(final String devId, EntityManager em) {
        // 装置IDから所属建物ID情報を取得
        CriteriaBuilder buildermdr = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelation> querymdr = buildermdr.createQuery(MDevRelation.class);

        Root<MDevRelation> rootmdr = querymdr.from(MDevRelation.class);
        querymdr.select(rootmdr)
                .where(buildermdr.equal(rootmdr.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        return em.createQuery(querymdr).getResultList().get(0);

    }

}
