package jp.co.osaki.sms.servicedao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CriteriaUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * データ収集装置 データ表示機能 装置に紐付くメーター情報取得 ServiceDaoクラス
 *
 * @author ozaki.y
 */
public class ListSmsDataViewMeterInfoServiceDaoImpl implements BaseServiceDao<ListSmsDataViewResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(ListSmsDataViewResultData target, EntityManager em) {
        return selectMeterInfoDataList(em, target.getCorpId(), target.getBuildingId(), target.isTenant(),
                target.getDevId(), target.getMeterMngIdList());
    }

    /**
     * 各メーター情報を取得.
     *
     * @param em EntityManager
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterMngIdClct 対象メーター管理番号Collection
     * @return 各メーター情報List
     */
    private List<ListSmsDataViewResultData> selectMeterInfoDataList(EntityManager em, String corpId, Long buildingId,
            boolean isTenant, String devId, Collection<Long> meterMngIdClct) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);

        Root<TBuilding> tBuilding = query.from(TBuilding.class);

        Join<TBuilding, TBuildDevMeterRelation> tBuildDevMeterRelation = tBuilding
                .join(TBuilding_.TBuildDevMeterRelations);
        Join<TBuildDevMeterRelation, MMeter> mMeter = tBuildDevMeterRelation.join(TBuildDevMeterRelation_.MMeter);
        Join<TBuilding, MMeterType> mMeterType = tBuilding.join(TBuilding_.MMeterTypes);

        Path<String> corpIdPath = tBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId);
        Path<Long> buildingIdPath = tBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId);
        Path<Long> meterMngIdPath = mMeter.get(MMeter_.id).get(MMeterPK_.meterMngId);
        Path<String> devIdPath = mMeter.get(MMeter_.id).get(MMeterPK_.devId);
//        Path<String> srvEntPath = mMeter.get(MMeter_.srvEnt);		// 2024-10-30 SRV_ENT対応
        Path<BigDecimal> multiPath = mMeter.get(MMeter_.multi);
        Path<String> meterTypeNamePath = mMeterType.get(MMeterType_.meterTypeName);
        Path<BigDecimal> co2CoefficientPath = mMeterType.get(MMeterType_.co2Coefficient);
        Path<String> unitCo2CoefficientPath = mMeterType.get(MMeterType_.unitCo2Coefficient);
        Path<String> unitUsageBasedPath = mMeterType.get(MMeterType_.unitUsageBased);

        Expression<?>[] targetColumns = new Expression[] {
                corpIdPath, buildingIdPath, devIdPath, multiPath, meterTypeNamePath, co2CoefficientPath,
                unitCo2CoefficientPath, unitUsageBasedPath, meterMngIdPath };

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        whereClct.add(builder.equal(devIdPath, devId));
//        whereClct.add(builder.or(builder.isNull(srvEntPath), builder.equal(builder.length(srvEntPath), 0)));	// 2024-10-30 SRV_ENT対応
        whereClct.add(builder.equal(mMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                mMeter.get(MMeter_.meterType)));
        whereClct.add(builder.equal(mMeter.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        if (CollectionUtils.isNotEmpty(meterMngIdClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdClct));
        }

        String targetCorpId = corpId;
        Long targetBuildingId = buildingId;

        if (isTenant) {
            // テナントの場合
            TBuilding divisionBuildingInfo = getDivisionBuildingInfoOfTenant(em, corpId, buildingId);
            TBuildingPK divisionBuildingInfoPk = divisionBuildingInfo.getId();

            targetCorpId = divisionBuildingInfoPk.getCorpId();
            targetBuildingId = divisionBuildingInfoPk.getBuildingId();
        }

        whereClct.add(builder.equal(corpIdPath, targetCorpId));
        whereClct.add(builder.equal(buildingIdPath, targetBuildingId));

        query.select(builder.construct(ListSmsDataViewResultData.class, targetColumns))
                .where(whereClct.toArray(new Predicate[0]))
                .groupBy(targetColumns)
                .orderBy(builder.asc(devIdPath), builder.asc(meterMngIdPath));

        return em.createQuery(query).getResultList();
    }

    /**
     * テナントの所属建物情報を取得.
     *
     * @param em EntityManager
     * @param corpId テナントの企業ID
     * @param buildingId テナントの建物ID
     * @return テナントの所属建物情報
     */
    private TBuilding getDivisionBuildingInfoOfTenant(EntityManager em, String corpId, Long buildingId) {
        // テナントの建物情報
        TBuilding tenantBuildingInfo = getBuildingInfo(em, corpId, buildingId);

        // テナントの所属建物情報
        TBuildingPK divisionBuildingInfoPk = new TBuildingPK();
        divisionBuildingInfoPk.setCorpId(tenantBuildingInfo.getDivisionCorpId());
        divisionBuildingInfoPk.setBuildingId(tenantBuildingInfo.getDivisionBuildingId());

        TBuilding divisionBuildingInfo = new TBuilding();
        divisionBuildingInfo.setId(divisionBuildingInfoPk);

        return divisionBuildingInfo;
    }

    /**
     * 建物情報を取得.
     *
     * @param em EntityManager
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 建物情報
     */
    private TBuilding getBuildingInfo(EntityManager em, String corpId, Long buildingId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = builder.createQuery(TBuilding.class);

        Root<TBuilding> tBuilding = query.from(TBuilding.class);

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query.select(tBuilding).where(whereClct.toArray(new Predicate[0]));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(List<ListSmsDataViewResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData find(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData merge(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
