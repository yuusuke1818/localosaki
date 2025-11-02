///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package jp.co.osaki.osol.api.servicedao.smcontrol;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.EntityManager;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Join;
//import javax.persistence.criteria.JoinType;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import jp.co.osaki.osol.api.ApiConstants;
//import jp.co.osaki.osol.api.resultset.common.CommonBuildingListResultSet;
//import jp.co.osaki.osol.entity.MMunicipality;
//import jp.co.osaki.osol.entity.MMunicipality_;
//import jp.co.osaki.osol.entity.MPrefecture;
//import jp.co.osaki.osol.entity.MPrefecture_;
//import jp.co.osaki.osol.entity.MWeatherCity;
//import jp.co.osaki.osol.entity.MWeatherCity_;
//import jp.co.osaki.osol.entity.TBuilding;
//import jp.co.osaki.osol.entity.TBuildingPK_;
//import jp.co.osaki.osol.entity.TBuilding_;
//import jp.skygroup.enl.webap.base.BaseServiceDao;
//
///**
// * 建物・テナント一覧取得（全建物・テナント） ServiceDaoクラス
// *
// * @author ya-ishida
// */
//public class AllBuildingListServiceDaoImpl implements BaseServiceDao<CommonBuildingListResultSet> {
//
//    @Override
//    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CommonBuildingListResultSet> getResultList(CommonBuildingListResultSet t, EntityManager em) {
//        String corpId = t.getCorpId();
//        Date totalTargetYm = t.getTotalTargetYm();
//
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<CommonBuildingListResultSet> query = builder.createQuery(CommonBuildingListResultSet.class);
//
//        Root<TBuilding> root = query.from(TBuilding.class);
//
//        Join<TBuilding, MMunicipality> joinMunicipality = root.join(TBuilding_.MMunicipality, JoinType.LEFT);
//        Join<TBuilding, MPrefecture> joinPrefecture = root.join(TBuilding_.MPrefecture, JoinType.LEFT);
//        Join<TBuilding, MWeatherCity> joinWeather = root.join(TBuilding_.MWeatherCity, JoinType.LEFT);
//
//        List<Predicate> whereList = new ArrayList<>();
//
//        //企業ID
//        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
//
//        //集計開始年月・集計終了年月
//        if (totalTargetYm != null) {
//            whereList.add(builder.or(builder.and(builder.lessThanOrEqualTo(root.get(TBuilding_.totalStartYm), totalTargetYm), builder.isNull(root.get(TBuilding_.totalEndYm))),
//                    builder.and(builder.lessThanOrEqualTo(root.get(TBuilding_.totalStartYm), totalTargetYm), builder.greaterThanOrEqualTo(root.get(TBuilding_.totalEndYm), totalTargetYm))));
//        }
//        //削除フラグ
//        whereList.add(builder.equal(root.get(TBuilding_.delFlg), ApiConstants.DEL_FLG_OFF));
//
//        query = query.select(builder.construct(CommonBuildingListResultSet.class,
//                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
//                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
//                root.get(TBuilding_.buildingNo),
//                root.get(TBuilding_.buildingName),
//                root.get(TBuilding_.buildingType),
//                root.get(TBuilding_.buildingNameKana),
//                root.get(TBuilding_.buildingTansyukuName),
//                joinMunicipality.get(MMunicipality_.municipalityCd),
//                joinPrefecture.get(MPrefecture_.prefectureCd),
//                root.get(TBuilding_.zipCd),
//                root.get(TBuilding_.address),
//                root.get(TBuilding_.addressBuilding),
//                root.get(TBuilding_.telNo),
//                root.get(TBuilding_.faxNo),
//                root.get(TBuilding_.biko),
//                root.get(TBuilding_.floorCount),
//                root.get(TBuilding_.basementCount),
//                root.get(TBuilding_.nyukyoTypeCd),
//                root.get(TBuilding_.commonUsedRate),
//                root.get(TBuilding_.kubunShoyuRate),
//                root.get(TBuilding_.conpletedYm),
//                root.get(TBuilding_.engManageFactoryType),
//                root.get(TBuilding_.engManageFactoryNo),
//                root.get(TBuilding_.totalStartYm),
//                root.get(TBuilding_.totalEndYm),
//                root.get(TBuilding_.estimateUse),
//                root.get(TBuilding_.divisionCorpId),
//                root.get(TBuilding_.divisionBuildingId),
//                root.get(TBuilding_.buildingDelDate),
//                root.get(TBuilding_.buildingDelPersonCorpId),
//                root.get(TBuilding_.buildingDelPersonId),
//                root.get(TBuilding_.freonDischargeOffice),
//                joinWeather.get(MWeatherCity_.cityCd),
//                root.get(TBuilding_.publicFlg),
//                root.get(TBuilding_.delFlg),
//                root.get(TBuilding_.version))).
//                where(builder.and(whereList.toArray(new Predicate[]{})));
//
//        return em.createQuery(query).getResultList();
//    }
//
//    @Override
//    public List<CommonBuildingListResultSet> getResultList(Map<String, List<Object>> map, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CommonBuildingListResultSet> getResultList(List<CommonBuildingListResultSet> list, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CommonBuildingListResultSet> getResultList(EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public CommonBuildingListResultSet find(CommonBuildingListResultSet t, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void persist(CommonBuildingListResultSet t, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public CommonBuildingListResultSet merge(CommonBuildingListResultSet t, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void remove(CommonBuildingListResultSet t, EntityManager em) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
