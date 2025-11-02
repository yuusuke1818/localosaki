package jp.co.osaki.sms.servicedao;

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
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MeterTenantSmsServiceDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 企業内の建物番号重複のチェックに使用（運用削除と論理削除されていない建物を取得）
    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        String corpId = target.getId().getCorpId();
        String buildingNo = target.getBuildingNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);
        Root<TBuilding> root = query.from(TBuilding.class);

        List<Predicate> whereList = new ArrayList<>();

        // 企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 建物番号チェックする
        whereList.add(builder.equal(root.get(TBuilding_.buildingNo), buildingNo));
        // 削除済みを除く
        whereList.add(builder.notEqual(root.get(TBuilding_.delFlg), OsolConstants.FLG_ON));
        whereList.add(builder.isNull(root.get(TBuilding_.buildingDelDate)));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.asc(root.get(TBuilding_.buildingNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 建物に所属するテナントのSMS利用テナント一覧
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);
        Root<TBuilding> root = query.from(TBuilding.class);

        // 建物に所属するテナント一覧を取得する場合（SMS用テナント）
        Join<TBuilding, MTenantSm> mTenantSm = root.join(TBuilding_.MTenantSms , JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // 所属企業ID
        List<Object> targetCorpIdList = parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID);
        if (targetCorpIdList != null && !targetCorpIdList.isEmpty()) {
            for (Object s : targetCorpIdList) {
                // 同企業内の建物でSMSテナント検索のため（SMSで登録されたテナントであればID同じのはず）
                whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), (String) s));
                whereList.add(builder.equal(root.get(TBuilding_.divisionCorpId), (String) s));
            }
        }

        // 所属建物ID
        List<Object> targetBuildingIdList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_ID);
        if (targetBuildingIdList != null && !targetBuildingIdList.isEmpty()) {
            for (Object s : targetBuildingIdList) {
                whereList.add(builder.equal(root.get(TBuilding_.divisionBuildingId), (Long) s));
            }
        }

        // ユーザーコード
        List<Object> targetTenantIdList = parameterMap.get(SmsConstants.SEARCH_CONDITION_TENANT_ID);
        if (targetTenantIdList != null && !targetTenantIdList.isEmpty()) {
            for (Object s : targetTenantIdList) {
                whereList.add(builder.equal(mTenantSm.get(MTenantSm_.tenantId), (Long) s));
            }
        }

        // 削除済みを除く
        whereList.add(builder.notEqual(root.get(TBuilding_.delFlg), OsolConstants.FLG_ON));
        whereList.add(builder.isNull(root.get(TBuilding_.buildingDelDate)));
        whereList.add(builder.notEqual(mTenantSm.get(MTenantSm_.delFlg), OsolConstants.FLG_ON));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.asc(root.get(TBuilding_.buildingNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        return em.find(TBuilding.class, target.getId());
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
