package jp.co.osaki.osol.api.servicedao.analysis;

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
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroupPK_;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.entity.MMunicipality_;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroupPK_;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.MWeatherCity;
import jp.co.osaki.osol.entity.MWeatherCity_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 *集計分析 EMS実績 対象建物取得(グループ指定) ServiceDaoクラス
 *
 * @author y-maruta
 *
 */

public class AnalysisGroupBuildingListServiceDaoImpl implements BaseServiceDao<AnalysisAllBuildingListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisAllBuildingListResultData> getResultList(AnalysisAllBuildingListResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long parentGroupId = target.getParentGroupId();
        Long childGroupId = target.getChildGroupId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisAllBuildingListResultData> query = builder.createQuery(AnalysisAllBuildingListResultData.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MMunicipality> joinMunicipality = root.join(TBuilding_.MMunicipality, JoinType.LEFT);
        Join<TBuilding, MPrefecture> joinPrefecture = root.join(TBuilding_.MPrefecture, JoinType.LEFT);
        Join<TBuilding, MWeatherCity> joinWeather = root.join(TBuilding_.MWeatherCity, JoinType.LEFT);
        Join<TBuilding,MBuildingDm> joinBuildingDm = root.join(TBuilding_.MBuildingDms);
        Join<TBuilding,TBuildingGroup> joinBuildingGroup = root.join(TBuilding_.TBuildingGroups,JoinType.LEFT);
        Join<TBuildingGroup,MChildGroup> joinChildGroup = joinBuildingGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
        Join<MChildGroup,MParentGroup> joinParentGroup = joinChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);

        //建物グループ削除フラグ
        joinBuildingGroup.on(builder.equal(joinBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        //子グループ削除フラグ
        joinChildGroup.on(builder.equal(joinChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        //親グループ削除フラグ
        joinParentGroup.on(builder.equal(joinParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));

        //親グループ
        whereList.add(builder.equal(joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId), parentGroupId));

      //子グループ指定が有る場合
        if(childGroupId != null) {
            whereList.add(builder.equal(joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId), childGroupId));
        }

      //建物削除フラグ
        whereList.add(builder.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        //建物デマンド削除フラグ
        whereList.add(builder.equal(joinBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AnalysisAllBuildingListResultData.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                root.get(TBuilding_.buildingNo),
                root.get(TBuilding_.buildingName),
                root.get(TBuilding_.buildingNameKana),
                root.get(TBuilding_.buildingTansyukuName),
                joinMunicipality.get(MMunicipality_.municipalityCd),
                joinPrefecture.get(MPrefecture_.prefectureCd),
                root.get(TBuilding_.zipCd),
                root.get(TBuilding_.address),
                root.get(TBuilding_.addressBuilding),
                root.get(TBuilding_.telNo),
                root.get(TBuilding_.faxNo),
                root.get(TBuilding_.biko),
                root.get(TBuilding_.floorCount),
                root.get(TBuilding_.basementCount),
                root.get(TBuilding_.nyukyoTypeCd),
                root.get(TBuilding_.commonUsedRate),
                root.get(TBuilding_.kubunShoyuRate),
                root.get(TBuilding_.conpletedYm),
                root.get(TBuilding_.engManageFactoryType),
                root.get(TBuilding_.engManageFactoryNo),
                root.get(TBuilding_.totalStartYm),
                root.get(TBuilding_.totalEndYm),
                root.get(TBuilding_.estimateUse),
                root.get(TBuilding_.divisionCorpId),
                root.get(TBuilding_.divisionBuildingId),
                root.get(TBuilding_.buildingDelDate),
                root.get(TBuilding_.buildingDelPersonCorpId),
                root.get(TBuilding_.buildingDelPersonId),
                root.get(TBuilding_.freonDischargeOffice),
                joinWeather.get(MWeatherCity_.cityCd),
                root.get(TBuilding_.publicFlg),
                root.get(TBuilding_.buildingType),
                joinParentGroup.get(MParentGroup_.id).get(MParentGroupPK_.parentGroupId),
                joinParentGroup.get(MParentGroup_.parentGroupName),
                joinParentGroup.get(MParentGroup_.displayOrder),
                joinChildGroup.get(MChildGroup_.id).get(MChildGroupPK_.childGroupId),
                joinChildGroup.get(MChildGroup_.childGroupName),
                joinChildGroup.get(MChildGroup_.displayOrder),
                joinBuildingDm.get(MBuildingDm_.sumDate),
                joinBuildingDm.get(MBuildingDm_.weekClosingDayOfWeek),
                joinBuildingDm.get(MBuildingDm_.weekStartDay),
                joinBuildingDm.get(MBuildingDm_.targetKw),
                joinBuildingDm.get(MBuildingDm_.contractKw),
                joinBuildingDm.get(MBuildingDm_.workStartTime),
                joinBuildingDm.get(MBuildingDm_.shopOpenTime),
                joinBuildingDm.get(MBuildingDm_.shopCloseTime),
                joinBuildingDm.get(MBuildingDm_.workEndTime),
                joinBuildingDm.get(MBuildingDm_.outAirTempDispFlg)
                )).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisAllBuildingListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisAllBuildingListResultData> getResultList(List<AnalysisAllBuildingListResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisAllBuildingListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisAllBuildingListResultData find(AnalysisAllBuildingListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisAllBuildingListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public AnalysisAllBuildingListResultData merge(AnalysisAllBuildingListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisAllBuildingListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }



}
