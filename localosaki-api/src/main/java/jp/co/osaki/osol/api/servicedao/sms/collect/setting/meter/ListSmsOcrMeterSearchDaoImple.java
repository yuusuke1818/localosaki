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
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MMeterLoadlimitPK_;
import jp.co.osaki.osol.entity.MMeterLoadlimit_;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター管理 AieLink用メーター情報一覧取得 ServiceDaoクラス
 * 「OCR検針」→「AieLink」へ変更
 * @author kobayashi.sho
 */
public class ListSmsOcrMeterSearchDaoImple implements BaseServiceDao<ListSmsMeterResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(ListSmsMeterResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterResultData> query = builder.createQuery(ListSmsMeterResultData.class);

        // ■from句の整理
        // [MuDM2]メータ登録用TBL
        Root<MMeter> root = query.from(MMeter.class);
        // 装置情報テーブル
        Join<MMeter, MDevPrm> joinDevPrm = root.join(MMeter_.MDevPrm, JoinType.LEFT);
        // 建物装置中間テーブル
        Join<MDevPrm, MDevRelation> joinDevRelation = joinDevPrm.join(MDevPrm_.MDevRelations, JoinType.LEFT);
        // [MuDM2]メータ負荷制限設定TBL
        Join<MMeter, MMeterLoadlimit> joinMeterLoadlimit = root.join(MMeter_.MMeterLoadlimits, JoinType.LEFT);

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

        // ■from句の結合条件の整理
        // 装置との関連(装置ID、削除フラグ)
        joinDevPrm.on(
                builder.and(
                        builder.equal(
                                root.get(MMeter_.id).get(MMeterPK_.devId),
                                joinDevPrm.get(MDevPrm_.devId)),
                        builder.equal(
                                joinDevPrm.get(MDevPrm_.delFlg),
                                OsolConstants.FLG_OFF)));

        // 建物関係との関連(装置ID)
        joinDevRelation.on(
                builder.and(
                        builder.equal(
                                root.get(MMeter_.id).get(MMeterPK_.devId),
                                joinDevRelation
                                        .get(MDevRelation_.id).get(MDevRelationPK_.devId))));

        // メーター負荷制限との関連(メーター管理番号、装置ID)
        joinMeterLoadlimit.on(
                builder.and(
                        builder.equal(
                                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                                joinMeterLoadlimit.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.meterMngId)),
                        builder.equal(
                                root.get(MMeter_.id).get(MMeterPK_.devId),
                                joinMeterLoadlimit.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.devId))));

        // 建物メーター中間テーブルとの関連(装置ID、メーター管理番号)
        joinBuildDevMeterRelation.on(builder.and(
                builder.equal(
                        root.get(MMeter_.id).get(MMeterPK_.devId),
                        joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                                .get(TBuildDevMeterRelationPK_.devId)),
                builder.equal(
                        root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                        joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                                .get(TBuildDevMeterRelationPK_.meterMngId)),
                builder.notEqual(joinBuildDevMeterRelation
                        .get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                        joinDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.buildingId))));

        // 建物との関連(建物装置関係で取得した建物ID、企業IDと、削除フラグ)
        joinBuilding.on(
                builder.and(
                        builder.equal(
                                joinDevRelation.get(MDevRelation_.id)
                                        .get(MDevRelationPK_.buildingId),
                                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.equal(
                                joinDevRelation.get(MDevRelation_.id)
                                        .get(MDevRelationPK_.corpId),
                                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.equal(
                                joinBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF)));

        // メーター種別設定との関連(企業ID、建物ID、メーター種別)
        joinMeterType.on(
                builder.and(
                        builder.equal(
                                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.corpId)),
                        builder.equal(
                                joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.buildingId)),
                        builder.equal(
                                root.get(MMeter_.meterType),
                                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType))));

        // テナントとの関連(建物メーター関係で取得した建物ID、企業IDと、削除フラグ、テナントフラグ)
        joinBuildingTenant.on(
                builder.and(
                        builder.equal(
                                joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                                        .get(TBuildDevMeterRelationPK_.buildingId),
                                joinBuildingTenant.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        builder.equal(
                                joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                                        .get(TBuildDevMeterRelationPK_.corpId),
                                joinBuildingTenant.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.equal(
                                joinBuildingTenant.get(TBuilding_.delFlg), OsolConstants.FLG_OFF),
                        builder.equal(
                                joinBuildingTenant.get(TBuilding_.buildingType),
                                OsolConstants.BUILDING_TYPE.TENANT.getVal())));

        // テナントテーブルとの関連(企業ID、建物ID)
        joinTenantSms.on(
                builder.and(
                        builder.equal(
                                joinBuildingTenant.get(TBuilding_.id).get(TBuildingPK_.corpId),
                                joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.corpId)),
                        builder.equal(
                                joinBuildingTenant.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                                joinTenantSms.get(MTenantSm_.id).get(MTenantSmPK_.buildingId))));

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        // 条件1: 装置IDがパラメーターと一致
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), target.getDevId()));

        // 条件2: 装置IDがOCから始まる
        whereList.add(builder.like(root.get(MMeter_.id).get(MMeterPK_.devId), DEVICE_KIND.OCR.getVal() + "%"));

        // 条件4: メーターの削除フラグがOFF
        whereList.add(builder.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF.toString()));

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsMeterResultData.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                joinTenantSms.get(MTenantSm_.tenantId),
                joinBuildingTenant.get(TBuilding_.buildingNo),
                joinBuildingTenant.get(TBuilding_.buildingName),
                root.get(MMeter_.meterId),
                root.get(MMeter_.memo),
                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                joinMeterType.get(MMeterType_.meterTypeName),
                root.get(MMeter_.examEndYm),
                root.get(MMeter_.multi),
                root.get(MMeter_.dispYearFlg),
                root.get(MMeter_.examNotice),
                root.get(MMeter_.version)
                ))
                .distinct(true)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        List<ListSmsMeterResultData> resultList = null;

        // 最大件数が未指定の場合は全件取得
        if (target.getAmount() == null) {
            resultList = em.createQuery(query).setFirstResult(target.getOffset()).getResultList();
        } else {
            resultList = em.createQuery(query).setFirstResult(target.getOffset()).setMaxResults(target.getAmount())
                    .getResultList();
        }

        return resultList;

    }

    @Override
    public List<ListSmsMeterResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(List<ListSmsMeterResultData> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListSmsMeterResultData find(ListSmsMeterResultData target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(ListSmsMeterResultData target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListSmsMeterResultData merge(ListSmsMeterResultData target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(ListSmsMeterResultData target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
