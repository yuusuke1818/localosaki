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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.resultset.BuildingDevMeterResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物・装置・メーター検索ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class BuildingDevMeterListServiceDaoImpl implements BaseServiceDao<BuildingDevMeterResultSet> {

    /** パラメータキー */
    public static final String PARAM_KEY = "dbParam";

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDevMeterResultSet> getResultList(BuildingDevMeterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDevMeterResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        BuildingDevMeterResultSet param = (BuildingDevMeterResultSet) parameterMap.get(PARAM_KEY).get(0);
        return selectMeterInfoDataList(em, param.getCorpId(), param.getBuildingId(), param.isTenant(),
                param.getDevId(), param.getMeterKind(), param.getMeterMngId(), param.getTenantName());
    }

    /**
     * 各メーター情報を取得.
     *
     * @param em EntityManager
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param meterMngId メーター管理番号
     * @param tenantName テナント名
     * @return 各メーター情報List
     */
    private List<BuildingDevMeterResultSet> selectMeterInfoDataList(EntityManager em, String corpId, Long buildingId,
            boolean isTenant, String devId, String meterKind, Long meterMngId, String tenantName) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildingDevMeterResultSet> query = builder.createQuery(BuildingDevMeterResultSet.class);

        Root<TBuilding> tBuilding = query.from(TBuilding.class);

        Join<TBuilding, TBuildDevMeterRelation> tBuildDevMeterRelation = tBuilding
                .join(TBuilding_.TBuildDevMeterRelations);
        Join<TBuildDevMeterRelation, MMeter> mMeter = tBuildDevMeterRelation.join(TBuildDevMeterRelation_.MMeter);

        Path<String> corpIdPath = tBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId);
        Path<Long> buildingIdPath = tBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId);
        Path<String> buildingNamePath = tBuilding.get(TBuilding_.buildingName);
        Path<String> divisionCorpIdPath = tBuilding.get(TBuilding_.divisionCorpId);
        Path<Long> divisionBuildingIdPath = tBuilding.get(TBuilding_.divisionBuildingId);
        Path<String> devIdPath = mMeter.get(MMeter_.id).get(MMeterPK_.devId);
//        Path<String> srvEntPath = mMeter.get(MMeter_.srvEnt);		// 2024-10-30 SRV_ENT対応
        Path<String> meterIdPath = mMeter.get(MMeter_.meterId);
        Path<BigDecimal> multiPath = mMeter.get(MMeter_.multi);
        Path<Long> meterMngIdPath = mMeter.get(MMeter_.id).get(MMeterPK_.meterMngId);

        Expression<?>[] targetColumns = new Expression[] {
                corpIdPath, buildingIdPath, devIdPath, meterIdPath, multiPath, meterMngIdPath };

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
//        whereClct.add(builder.or(builder.isNull(srvEntPath), builder.equal(builder.length(srvEntPath), 0)));	// 2024-10-30 SRV_ENT対応
        whereClct.add(builder.equal(mMeter.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        if (CheckUtility.isNullOrEmpty(tenantName)) {
            whereClct.add(builder.equal(corpIdPath, corpId));
            whereClct.add(builder.equal(buildingIdPath, buildingId));

        } else {
            // テナント名による検索の場合
            whereClct.add(builder.like(buildingNamePath, "%" + tenantName + "%"));
            whereClct.add(builder.equal(divisionCorpIdPath, corpId));
            whereClct.add(builder.equal(divisionBuildingIdPath, buildingId));
        }

        // devId = "0"のときはたてものすべてのメーターを取得する
        // そのためdevIdを条件に入れない
        if (!CheckUtility.isNullOrEmpty(devId) && !devId.equals("0")) {
            whereClct.add(builder.equal(devIdPath, devId));
        }

        if (!CheckUtility.isNullOrEmpty(meterKind)) {
            whereClct.add(builder.like(meterIdPath, meterKind + "%"));
        }

        if (meterMngId != null) {
            whereClct.add(builder.equal(meterMngIdPath, meterMngId));
        }

        query.select(builder.construct(BuildingDevMeterResultSet.class, targetColumns))
                .where(whereClct.toArray(new Predicate[0]))
                .groupBy(targetColumns)
                .orderBy(builder.asc(devIdPath), builder.asc(meterMngIdPath));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<BuildingDevMeterResultSet> getResultList(List<BuildingDevMeterResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDevMeterResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingDevMeterResultSet find(BuildingDevMeterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingDevMeterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingDevMeterResultSet merge(BuildingDevMeterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(BuildingDevMeterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
