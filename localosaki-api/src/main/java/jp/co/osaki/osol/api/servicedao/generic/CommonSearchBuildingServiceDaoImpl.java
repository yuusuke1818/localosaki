/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.generic;

import java.util.ArrayList;
import java.util.Date;
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
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.resultdata.generic.CommonSearchBuildingDetailResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.entity.MMunicipality_;
import jp.co.osaki.osol.entity.MParentGroup;
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
 * 建物・テナント一覧取得（全建物・テナント） ServiceDaoクラス
 *
 * @author n-takada
 */
public class CommonSearchBuildingServiceDaoImpl implements BaseServiceDao<CommonSearchBuildingDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonSearchBuildingDetailResultData> getResultList(
            CommonSearchBuildingDetailResultData searchResultSet, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonSearchBuildingDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        // 企業ID(必須)
        Object corpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID));
        // 建物名称または建物番号(任意)
        Object buildingTenantNameNo = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_NO));
        // 都道府県
        List<Object> prefectureList = map.get(ApiParamKeyConstants.PREFECTURE_CD);

        // 建物状況(任意)
        Object buildingStateCd = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_STATUS));
        // 建物・テナント選択(任意)
        Object buildingTenantCd = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_TENANT_CD));
        // 入居形態(任意)
        Object nyukyoTypeCd = Utility.getListFirstItem(map.get(ApiParamKeyConstants.NYUKYO_TYPE_CD));
        // 入居形態(任意)
        Object osakiFlg = Utility.getListFirstItem(map.get(ApiParamKeyConstants.OSAKI_FLG));

        Date nowDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.DAY));

        //建物の企業ID
        Object buildingCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_CORP_ID));
        //建物ID
        Object buildingId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        //所属企業ID
        Object divisionCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.DIVISION_CORP_ID));
        //所属建物ID
        Object divisionBuildingId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.DIVISION_BUILDING_ID));
        //テナント所属建物 テナント入力フラグ
        Object inputBuildingBorrowByTenant = Utility
                .getListFirstItem(map.get(ApiParamKeyConstants.INPUT_BUILDING_BORROW_BY_TENANT_FLG));

        // 建物グルーピング
        Object parentGroupId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PARENT_GROUP_ID));
        Object childGroupId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CHILD_GROUP_ID));

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonSearchBuildingDetailResultData> query = builder
                .createQuery(CommonSearchBuildingDetailResultData.class);

        Root<TBuilding> rootTBuilding = query.from(TBuilding.class);

        Join<TBuilding, MCorp> joinMCorp = rootTBuilding.join(TBuilding_.MCorp);
        Join<TBuilding, MMunicipality> joinMunicipality = rootTBuilding.join(TBuilding_.MMunicipality, JoinType.LEFT);
        Join<TBuilding, MPrefecture> joinPrefecture = rootTBuilding.join(TBuilding_.MPrefecture);
        Join<TBuilding, MWeatherCity> joinWeatherCity = rootTBuilding.join(TBuilding_.MWeatherCity, JoinType.LEFT);
        Join<TBuilding, TBuildingGroup> joinTbuildingGroups = rootTBuilding.join(TBuilding_.TBuildingGroups, JoinType.LEFT);
        Join<TBuildingGroup,MChildGroup> joinChildGroup = joinTbuildingGroups.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
        Join<MChildGroup,MParentGroup> joinParentGroup = joinChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);

        //削除フラグ
        joinTbuildingGroups.on(builder.equal(joinTbuildingGroups.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        joinChildGroup.on(builder.equal(joinChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        joinParentGroup.on(builder.equal(joinParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> whereList = new ArrayList<>();
        //企業ID
        whereList.add(builder.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 建物名称または建物番号(任意)
        if (buildingTenantNameNo != null && !"".equals(buildingTenantNameNo)) {
            String likeString = "%" + buildingTenantNameNo + "%";
            whereList.add(builder.or(builder.like(rootTBuilding.get(TBuilding_.buildingName), likeString),
                    builder.like(rootTBuilding.get(TBuilding_.buildingNo), likeString)));
        }
        // 県コード(任意)
        if(prefectureList != null && !prefectureList.isEmpty()) {
            whereList.add(joinPrefecture.get(MPrefecture_.prefectureCd).in( prefectureList));
        }
        // 建物状況(任意)
        if (buildingStateCd != null && !"".equals(buildingStateCd)) {
            switch (buildingStateCd.toString()) {
            case "1":
                whereList.add(builder.or(
                        builder.and(
                                builder.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), nowDate),
                                builder.isNull(rootTBuilding.get(TBuilding_.totalEndYm))),
                        builder.and(
                                builder.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), nowDate),
                                builder.greaterThanOrEqualTo(rootTBuilding.get(TBuilding_.totalEndYm), nowDate))));

                break;
            case "2":
                whereList.add(builder.lessThan(rootTBuilding.get(TBuilding_.totalEndYm), nowDate));
                break;
            case "3":
                whereList.add(builder.isNotNull(rootTBuilding.get(TBuilding_.buildingDelDate)));
                break;
            default:
                break;
            }
        }
        // 建物・テナント選択(任意)
        if (buildingTenantCd != null && !"".equals(buildingTenantCd)) {
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.buildingType), buildingTenantCd));
        }
        // 入居形態(任意)
        if (nyukyoTypeCd != null && !"".equals(nyukyoTypeCd)) {
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.nyukyoTypeCd), nyukyoTypeCd.toString()));
        }
        // 大崎の管理者以外
        if ("false".equals(osakiFlg)) {
            whereList.add(builder.isNull(rootTBuilding.get(TBuilding_.buildingDelDate)));
        }

        if (buildingCorpId != null && !"".equals(buildingCorpId) && buildingId != null && !"".equals(buildingId)) {
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.divisionCorpId), buildingCorpId.toString()));
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.divisionBuildingId),
                    Long.parseLong(buildingId.toString())));
        }
        if (divisionCorpId != null && !"".equals(divisionCorpId) && divisionBuildingId != null
                && !"".equals(divisionBuildingId)) {
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                    divisionCorpId.toString()));
            whereList.add(builder.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                    Long.parseLong(divisionBuildingId.toString())));
        }

        // 建物グルーピング
        if(parentGroupId != null && !"".equals(parentGroupId)) {
            whereList.add(builder.equal(joinTbuildingGroups.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId), parentGroupId));
        }
        if(childGroupId != null && !"".equals(childGroupId)) {
            whereList.add(builder.equal(joinTbuildingGroups.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId), childGroupId));
        }

        //削除フラグ
        whereList.add(builder.equal(rootTBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(CommonSearchBuildingDetailResultData.class,
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                joinMCorp.get(MCorp_.corpName),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                rootTBuilding.get(TBuilding_.buildingNo),
                rootTBuilding.get(TBuilding_.buildingName),
                rootTBuilding.get(TBuilding_.buildingNameKana),
                rootTBuilding.get(TBuilding_.buildingTansyukuName),
                joinMunicipality.get(MMunicipality_.municipalityCd),
                joinPrefecture.get(MPrefecture_.prefectureCd),
                rootTBuilding.get(TBuilding_.zipCd),
                rootTBuilding.get(TBuilding_.address),
                rootTBuilding.get(TBuilding_.addressBuilding),
                rootTBuilding.get(TBuilding_.telNo),
                rootTBuilding.get(TBuilding_.faxNo),
                rootTBuilding.get(TBuilding_.biko),
                rootTBuilding.get(TBuilding_.floorCount),
                rootTBuilding.get(TBuilding_.basementCount),
                rootTBuilding.get(TBuilding_.nyukyoTypeCd),
                rootTBuilding.get(TBuilding_.commonUsedRate),
                rootTBuilding.get(TBuilding_.kubunShoyuRate),
                rootTBuilding.get(TBuilding_.conpletedYm),
                rootTBuilding.get(TBuilding_.engManageFactoryType),
                rootTBuilding.get(TBuilding_.engManageFactoryNo),
                rootTBuilding.get(TBuilding_.totalStartYm),
                rootTBuilding.get(TBuilding_.totalEndYm),
                rootTBuilding.get(TBuilding_.estimateUse),
                rootTBuilding.get(TBuilding_.buildingDelDate),
                rootTBuilding.get(TBuilding_.buildingDelPersonCorpId),
                rootTBuilding.get(TBuilding_.buildingDelPersonId),
                rootTBuilding.get(TBuilding_.freonDischargeOffice),
                rootTBuilding.get(TBuilding_.divisionCorpId),
                rootTBuilding.get(TBuilding_.divisionBuildingId),
                joinWeatherCity.get(MWeatherCity_.cityCd),
                rootTBuilding.get(TBuilding_.buildingType),
                joinPrefecture.get(MPrefecture_.prefectureName)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .groupBy(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                        joinMCorp.get(MCorp_.corpName),
                        rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                        rootTBuilding.get(TBuilding_.buildingNo),
                        rootTBuilding.get(TBuilding_.buildingName),
                        rootTBuilding.get(TBuilding_.buildingNameKana),
                        rootTBuilding.get(TBuilding_.buildingTansyukuName),
                        joinMunicipality.get(MMunicipality_.municipalityCd),
                        joinPrefecture.get(MPrefecture_.prefectureCd),
                        rootTBuilding.get(TBuilding_.zipCd),
                        rootTBuilding.get(TBuilding_.address),
                        rootTBuilding.get(TBuilding_.addressBuilding),
                        rootTBuilding.get(TBuilding_.telNo),
                        rootTBuilding.get(TBuilding_.faxNo),
                        rootTBuilding.get(TBuilding_.biko),
                        rootTBuilding.get(TBuilding_.floorCount),
                        rootTBuilding.get(TBuilding_.basementCount),
                        rootTBuilding.get(TBuilding_.nyukyoTypeCd),
                        rootTBuilding.get(TBuilding_.commonUsedRate),
                        rootTBuilding.get(TBuilding_.kubunShoyuRate),
                        rootTBuilding.get(TBuilding_.conpletedYm),
                        rootTBuilding.get(TBuilding_.engManageFactoryType),
                        rootTBuilding.get(TBuilding_.engManageFactoryNo),
                        rootTBuilding.get(TBuilding_.totalStartYm),
                        rootTBuilding.get(TBuilding_.totalEndYm),
                        rootTBuilding.get(TBuilding_.estimateUse),
                        rootTBuilding.get(TBuilding_.buildingDelDate),
                        rootTBuilding.get(TBuilding_.buildingDelPersonCorpId),
                        rootTBuilding.get(TBuilding_.buildingDelPersonId),
                        rootTBuilding.get(TBuilding_.freonDischargeOffice),
                        rootTBuilding.get(TBuilding_.divisionCorpId),
                        rootTBuilding.get(TBuilding_.divisionBuildingId),
                        joinWeatherCity.get(MWeatherCity_.cityCd),
                        rootTBuilding.get(TBuilding_.buildingType),
                        joinPrefecture.get(MPrefecture_.prefectureName))
                .orderBy(builder.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(rootTBuilding.get(TBuilding_.buildingNo)));

        //テナント所属建物検索で所属建物が存在しない場合は0件にする
        if (Boolean.valueOf(inputBuildingBorrowByTenant.toString())) {
            return new ArrayList<>();
        } else {
            return em.createQuery(query).getResultList();
        }
    }

    @Override
    public List<CommonSearchBuildingDetailResultData> getResultList(List<CommonSearchBuildingDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonSearchBuildingDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonSearchBuildingDetailResultData find(CommonSearchBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonSearchBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonSearchBuildingDetailResultData merge(CommonSearchBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonSearchBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
