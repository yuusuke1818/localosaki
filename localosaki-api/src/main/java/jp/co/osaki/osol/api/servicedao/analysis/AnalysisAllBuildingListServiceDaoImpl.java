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
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.entity.MMunicipality_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.MWeatherCity;
import jp.co.osaki.osol.entity.MWeatherCity_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 *集計分析 EMS実績 対象建物取得(全建物) ServiceDaoクラス
 *
 * @author y-maruta
 *
 */

public class AnalysisAllBuildingListServiceDaoImpl implements BaseServiceDao<AnalysisAllBuildingListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisAllBuildingListResultData> getResultList(AnalysisAllBuildingListResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisAllBuildingListResultData> query = builder.createQuery(AnalysisAllBuildingListResultData.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MMunicipality> joinMunicipality = root.join(TBuilding_.MMunicipality, JoinType.LEFT);
        Join<TBuilding, MPrefecture> joinPrefecture = root.join(TBuilding_.MPrefecture, JoinType.LEFT);
        Join<TBuilding, MWeatherCity> joinWeather = root.join(TBuilding_.MWeatherCity, JoinType.LEFT);
        Join<TBuilding,MBuildingDm> joinBuildingDm = root.join(TBuilding_.MBuildingDms);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));

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
