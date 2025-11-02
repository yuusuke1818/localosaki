package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection;

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
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ取得API 自動検針データ取得ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class ListSmsAutoInspectionInfoServiceDaoImpl implements BaseServiceDao<ListSmsAutoInspectionResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionResultData> getResultList(ListSmsAutoInspectionResultData target,
            EntityManager em) {

        return selectAutoInspectionInfoList(em, target.getCorpId(), target.getBuildingId(), target.isTenant());
    }

    /**
     * 指定の建物の装置に紐付く自動検針データを取得.
     *
     * @param em EntityManager
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 自動検針データList
     */
    private List<ListSmsAutoInspectionResultData> selectAutoInspectionInfoList(EntityManager em, String corpId,
            Long buildingId, boolean isTenant) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsAutoInspectionResultData> query = builder
                .createQuery(ListSmsAutoInspectionResultData.class);

        // 自動検針月 日時設定(M_AUTO_INSP)
        Root<MAutoInsp> mAutoInsp = query.from(MAutoInsp.class);

        // 装置情報(M_DEV_PRM)
        Join<MAutoInsp, MDevPrm> mDevPrm = mAutoInsp.join(MAutoInsp_.MDevPrm);
        // 建物、装置関連テーブル(M_DEV_RELATION)
        Join<MDevPrm, MDevRelation> mDevRelation = mDevPrm.join(MDevPrm_.MDevRelations);

        Path<String> corpIdPath = mDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.corpId);
        Path<Long> buildingIdPath = mDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.buildingId);
        Path<Long> meterTypePath = mAutoInsp.get(MAutoInsp_.id).get(MAutoInspPK_.meterType);
        Path<String> devIdPath = mAutoInsp.get(MAutoInsp_.id).get(MAutoInspPK_.devId);
        Path<String> monthPath = mAutoInsp.get(MAutoInsp_.month);
        Path<String> dayPath = mAutoInsp.get(MAutoInsp_.day);
        Path<BigDecimal> hourPath = mAutoInsp.get(MAutoInsp_.hour);
        Path<Integer> versionPath = mAutoInsp.get(MAutoInsp_.version);

        Expression<?>[] targetColumns = new Expression[] { devIdPath, meterTypePath, monthPath, dayPath, hourPath,
                versionPath };

        Collection<Predicate> whereClct = new ArrayList<>();

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

        query.select(builder.construct(ListSmsAutoInspectionResultData.class, targetColumns))
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(builder.asc(devIdPath), builder.asc(meterTypePath));

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
    public List<ListSmsAutoInspectionResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionResultData> getResultList(List<ListSmsAutoInspectionResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsAutoInspectionResultData find(ListSmsAutoInspectionResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsAutoInspectionResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsAutoInspectionResultData merge(ListSmsAutoInspectionResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsAutoInspectionResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
