/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.manualinsp.search;

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
import jp.co.osaki.osol.api.resultdata.sms.manualinsp.search.ListManualInspectionResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 任意検針一覧取得  ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class ListManualInspectionServiceDaoImpl implements BaseServiceDao<ListManualInspectionResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListManualInspectionResultData> getResultList(ListManualInspectionResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<ListManualInspectionResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {

        String corpId = map.get("corpId").get(0).toString();
        String buildingId = map.get("buildingId").get(0).toString();
        String userName = map.get("userNameCondition").get(0) != null ? map.get("userNameCondition").get(0).toString() : "";
        String userCd = map.get("userCdCondition").get(0) != null ? map.get("userCdCondition").get(0).toString() : "";
        String meterTypeName = map.get("meterTypeNameCondition").get(0) != null ? map.get("meterTypeNameCondition").get(0).toString() : "";
        String reserveInspDate = map.get("reserveInspDateCondition").get(0) != null ? map.get("reserveInspDateCondition").get(0).toString() : "";

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListManualInspectionResultData> query = builder.createQuery(ListManualInspectionResultData.class);

        // [MuDM2]メータ登録用TBL
        Root<MMeter> root = query.from(MMeter.class);
        // 装置情報テーブル
        Join<MMeter, MDevPrm> joinDevPrm = root.join(MMeter_.MDevPrm, JoinType.LEFT);
        // 建物装置中間テーブル
        Join<MDevPrm, MDevRelation> joinDevRelation = joinDevPrm.join(MDevPrm_.MDevRelations, JoinType.LEFT);
        // 建物メーター中間テーブル
        Join<MMeter, TBuildDevMeterRelation> joinBuildDevMeterRelation = root.join(MMeter_.TBuildDevMeterRelations,
                JoinType.LEFT);
        // テナント
        Join<TBuildDevMeterRelation, TBuilding> joinBuildingTenant = joinBuildDevMeterRelation
                .join(TBuildDevMeterRelation_.TBuilding, JoinType.LEFT);
        // 建物
        Join<MDevRelation, TBuilding> joinBuilding = joinDevRelation.join(MDevRelation_.TBuilding, JoinType.LEFT);
        // [MuDM2]メーター種別設定TBL
        Join<TBuilding, MMeterType> joinMeterType = joinBuilding.join(TBuilding_.MMeterTypes, JoinType.LEFT);
        // SMSのテナントテーブル
        Join<TBuilding, MTenantSm> joinTenantSms = joinBuildingTenant.join(TBuilding_.MTenantSms, JoinType.LEFT);

        // 装置との関連(装置ID、削除フラグ)
        joinDevPrm.on(builder.equal(joinDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        // 建物メーター中間テーブルとの関連(装置ID、メーター管理番号)
        joinBuildDevMeterRelation.on(builder.notEqual(
                joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                joinDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.buildingId)));

        // 建物との関連(建物装置関係で取得した建物ID、企業IDと、削除フラグ)
        joinBuilding.on(builder.equal(joinBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        // テナントとの関連(建物ID、企業ID、削除フラグ、テナントフラグ、所属企業ID、所属建物ID)
        joinBuildingTenant
                .on(builder.and(builder.equal(joinBuildingTenant.get(TBuilding_.delFlg), OsolConstants.FLG_OFF),
                        builder.equal(joinBuildingTenant.get(TBuilding_.buildingType),
                                OsolConstants.BUILDING_TYPE.TENANT.getVal()),
                        builder.equal(joinBuildingTenant.get(TBuilding_.divisionCorpId), corpId),
                        builder.equal(joinBuildingTenant.get(TBuilding_.divisionBuildingId), buildingId)));

        // 建物とメーター種別設定との関連(企業ID、建物ID、メーター種別)
        joinMeterType.on(builder.equal(root.get(MMeter_.meterType),
                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

        // テナントテーブルとの関連(企業ID、建物ID、削除フラグ)
        joinTenantSms.on(builder.equal(joinTenantSms.get(MTenantSm_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> whereList = new ArrayList<>();
        // ハンディ装置でない
        whereList.add(builder.notLike(root.get(MMeter_.id).get(MMeterPK_.devId), DEVICE_KIND.HANDY.getVal() + "%"));
        // IoT-Rではない
        whereList.add(builder.notLike(root.get(MMeter_.id).get(MMeterPK_.devId), DEVICE_KIND.IOTR.getVal() + "%"));
        // AieLink装置ではない
        //whereList.add(builder.notLike(root.get(MMeter_.id).get(MMeterPK_.devId), DEVICE_KIND.OCR.getVal() + "%"));
        whereList.add(builder.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        // 削除済みの装置でない
        whereList.add(builder.equal(joinDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        // 入力項目による検索
        if (!CheckUtility.isNullOrEmpty(userName)) {
            // テナントの建物名と中間一致
            whereList.add(builder.like(joinBuildingTenant.get(TBuilding_.buildingName),
                    BaseUtility.addSqlWildcard((String) userName)));
        }
        if (!CheckUtility.isNullOrEmpty(userCd)) {
            // テナントIDと完全一致
            whereList.add(builder.equal(joinTenantSms.get(MTenantSm_.tenantId), userCd));
        }
        if (!CheckUtility.isNullOrEmpty(meterTypeName)) {
            whereList.add(builder.equal(joinMeterType.get(MMeterType_.meterTypeName), meterTypeName));
        }
        if (!CheckUtility.isNullOrEmpty(reserveInspDate)) {
            whereList.add(builder.equal(root.get(MMeter_.reserveInspDate), DateUtility.conversionDate(reserveInspDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH)));
        }

        // 重複を削除したSQLを発行
        query = query
                .select(builder.construct(ListManualInspectionResultData.class,
                        root.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(MMeter_.meterId),
                        joinMeterType.get(MMeterType_.meterTypeName), joinBuildingTenant.get(TBuilding_.buildingName),
                        root.get(MMeter_.meterType),
                        root.get(MMeter_.id).get(MMeterPK_.devId),
                        joinDevPrm.get(MDevPrm_.name),
                        joinTenantSms.get(MTenantSm_.tenantId),
                        root.get(MMeter_.reserveInspDate),
                        root.get(MMeter_.version)))
                .distinct(true)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)),
                        builder.asc(joinTenantSms.get(MTenantSm_.tenantId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListManualInspectionResultData> getResultList(List<ListManualInspectionResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListManualInspectionResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListManualInspectionResultData find(ListManualInspectionResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListManualInspectionResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListManualInspectionResultData merge(ListManualInspectionResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListManualInspectionResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
