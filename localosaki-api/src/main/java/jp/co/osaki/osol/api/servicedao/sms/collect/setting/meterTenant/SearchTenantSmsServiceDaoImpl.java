package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

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
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.SearchTenantSmsResultData;
import jp.co.osaki.osol.entity.MMunicipality_;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class SearchTenantSmsServiceDaoImpl implements BaseServiceDao<SearchTenantSmsResultData>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchTenantSmsResultData> getResultList(SearchTenantSmsResultData target, EntityManager em) {

        // 建物に所属するテナント一覧
        String corpId = target.getDivisionCorpId();
        Long buildingId = target.getDivisionBuildingId();
        String buildingNo = target.getBuildingNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchTenantSmsResultData> query = builder.createQuery(SearchTenantSmsResultData.class);
        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MTenantSm> joinMTenantSm;

        // 所属建物の他のテナントと建物番号が被っているかチェックする場合
        if (buildingNo != null) {
            joinMTenantSm = root.join(TBuilding_.MTenantSms , JoinType.LEFT);
        }
        // 建物に所属するテナント一覧を取得する場合（SMS用テナント）
        else {
            joinMTenantSm = root.join(TBuilding_.MTenantSms , JoinType.INNER);
        }


        List<Predicate> whereList = new ArrayList<>();
        // 所属企業ID
        whereList.add(builder.equal(root.get(TBuilding_.divisionCorpId), corpId));
        // 所属建物ID
        whereList.add(builder.equal(root.get(TBuilding_.divisionBuildingId), buildingId));
        // 同企業内の建物でSMSテナント検索のため（SMSで登録されたテナントであればID同じのはず）
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));

        // 所属建物の他のテナントと建物番号が被っているかチェックする場合（削除されているテナントも含めて）
        if (buildingNo != null) {
            whereList.add(builder.equal(root.get(TBuilding_.buildingNo), buildingNo));
        }
        // 所属建物の他SMSテナント検索（削除済みは除く）
        else {
            // 建物.削除フラグ
            whereList.add(builder.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
            // 建物.建物削除日時
            whereList.add(builder.isNull(root.get(TBuilding_.buildingDelDate)));
            // テナントユーザー（情報）.削除フラグ
            whereList.add(builder.equal(joinMTenantSm.get(MTenantSm_.delFlg), OsolConstants.FLG_OFF));
        }

        query = query.select(builder.construct(SearchTenantSmsResultData.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                root.get(TBuilding_.buildingNo),
                root.get(TBuilding_.buildingName),
                root.get(TBuilding_.buildingNameKana),
                root.get(TBuilding_.buildingTansyukuName),
                root.get(TBuilding_.zipCd),
                root.get(TBuilding_.MPrefecture).get(MPrefecture_.prefectureCd),
                root.get(TBuilding_.MMunicipality).get(MMunicipality_.municipalityCd),
                root.get(TBuilding_.address),
                root.get(TBuilding_.addressBuilding),
                root.get(TBuilding_.telNo),
                root.get(TBuilding_.faxNo),
                root.get(TBuilding_.biko),
                root.get(TBuilding_.nyukyoTypeCd),
                root.get(TBuilding_.totalStartYm),
                root.get(TBuilding_.totalEndYm),
                root.get(TBuilding_.estimateUse),
                root.get(TBuilding_.createDate),
                root.get(TBuilding_.createUserId),
                root.get(TBuilding_.updateDate),
                root.get(TBuilding_.updateUserId),
                root.get(TBuilding_.delFlg),
                root.get(TBuilding_.version),
                root.get(TBuilding_.buildingDelDate),
                root.get(TBuilding_.divisionCorpId),
                root.get(TBuilding_.divisionBuildingId),
                root.get(TBuilding_.publicFlg),
                joinMTenantSm.get(MTenantSm_.tenantId),
                joinMTenantSm.get(MTenantSm_.fixed1Name),
                joinMTenantSm.get(MTenantSm_.fixed1Price),
                joinMTenantSm.get(MTenantSm_.fixed2Name),
                joinMTenantSm.get(MTenantSm_.fixed2Price),
                joinMTenantSm.get(MTenantSm_.fixed3Name),
                joinMTenantSm.get(MTenantSm_.fixed3Price),
                joinMTenantSm.get(MTenantSm_.fixed4Name),
                joinMTenantSm.get(MTenantSm_.fixed4Price),
                joinMTenantSm.get(MTenantSm_.priceMenuNo),
                joinMTenantSm.get(MTenantSm_.contractCapacity),
                joinMTenantSm.get(MTenantSm_.divRate1),
                joinMTenantSm.get(MTenantSm_.divRate2),
                joinMTenantSm.get(MTenantSm_.divRate3),
                joinMTenantSm.get(MTenantSm_.divRate4),
                joinMTenantSm.get(MTenantSm_.divRate5),
                joinMTenantSm.get(MTenantSm_.divRate6),
                joinMTenantSm.get(MTenantSm_.divRate7),
                joinMTenantSm.get(MTenantSm_.divRate8),
                joinMTenantSm.get(MTenantSm_.divRate9),
                joinMTenantSm.get(MTenantSm_.divRate10)
                )).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.asc(root.get(TBuilding_.buildingNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SearchTenantSmsResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchTenantSmsResultData> getResultList(List<SearchTenantSmsResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchTenantSmsResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SearchTenantSmsResultData find(SearchTenantSmsResultData target, EntityManager em) {
        // 建物に所属するテナント（SMSテナント用）
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchTenantSmsResultData> query = builder.createQuery(SearchTenantSmsResultData.class);
        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MTenantSm> joinMTenantSm = root.join(TBuilding_.MTenantSms , JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();
        // 所属企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 所属建物ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query = query.select(builder.construct(SearchTenantSmsResultData.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                root.get(TBuilding_.buildingNo),
                root.get(TBuilding_.buildingName),
                root.get(TBuilding_.buildingNameKana),
                root.get(TBuilding_.buildingTansyukuName),
                root.get(TBuilding_.zipCd),
                root.get(TBuilding_.MPrefecture).get(MPrefecture_.prefectureCd),
                root.get(TBuilding_.MMunicipality).get(MMunicipality_.municipalityCd),
                root.get(TBuilding_.address),
                root.get(TBuilding_.addressBuilding),
                root.get(TBuilding_.telNo),
                root.get(TBuilding_.faxNo),
                root.get(TBuilding_.biko),
                root.get(TBuilding_.nyukyoTypeCd),
                root.get(TBuilding_.totalStartYm),
                root.get(TBuilding_.totalEndYm),
                root.get(TBuilding_.estimateUse),
                root.get(TBuilding_.createDate),
                root.get(TBuilding_.createUserId),
                root.get(TBuilding_.updateDate),
                root.get(TBuilding_.updateUserId),
                root.get(TBuilding_.delFlg),
                root.get(TBuilding_.version),
                root.get(TBuilding_.buildingDelDate),
                root.get(TBuilding_.divisionCorpId),
                root.get(TBuilding_.divisionBuildingId),
                root.get(TBuilding_.publicFlg),
                joinMTenantSm.get(MTenantSm_.tenantId),
                joinMTenantSm.get(MTenantSm_.fixed1Name),
                joinMTenantSm.get(MTenantSm_.fixed1Price),
                joinMTenantSm.get(MTenantSm_.fixed2Name),
                joinMTenantSm.get(MTenantSm_.fixed2Price),
                joinMTenantSm.get(MTenantSm_.fixed3Name),
                joinMTenantSm.get(MTenantSm_.fixed3Price),
                joinMTenantSm.get(MTenantSm_.fixed4Name),
                joinMTenantSm.get(MTenantSm_.fixed4Price),
                joinMTenantSm.get(MTenantSm_.priceMenuNo),
                joinMTenantSm.get(MTenantSm_.contractCapacity),
                joinMTenantSm.get(MTenantSm_.divRate1),
                joinMTenantSm.get(MTenantSm_.divRate2),
                joinMTenantSm.get(MTenantSm_.divRate3),
                joinMTenantSm.get(MTenantSm_.divRate4),
                joinMTenantSm.get(MTenantSm_.divRate5),
                joinMTenantSm.get(MTenantSm_.divRate6),
                joinMTenantSm.get(MTenantSm_.divRate7),
                joinMTenantSm.get(MTenantSm_.divRate8),
                joinMTenantSm.get(MTenantSm_.divRate9),
                joinMTenantSm.get(MTenantSm_.divRate10)
                )).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.asc(root.get(TBuilding_.buildingNo)));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(SearchTenantSmsResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SearchTenantSmsResultData merge(SearchTenantSmsResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SearchTenantSmsResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
