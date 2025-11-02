/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice;

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

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.BUILDING_TYPE;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsBuildingDetailResultData;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 装置情報一覧取得  ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class ListSmsBuildingServiceDaoImpl implements BaseServiceDao<ListSmsBuildingDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsBuildingDetailResultData> getResultList(ListSmsBuildingDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<ListSmsBuildingDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsBuildingDetailResultData> query = builder
                .createQuery(ListSmsBuildingDetailResultData.class);
        Root<TBuilding> building = query.from(TBuilding.class);
        Join<TBuilding, MCorp> mCorp = building.join(TBuilding_.MCorp);
        Join<TBuilding, MPrefecture> mPrefecture = building.join(TBuilding_.MPrefecture);
        Join<TBuilding, MBuildingSms> mBuildingSms = building.join(TBuilding_.MBuildingSms, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // 企業(IDまたは名)
        List<Object> targetCorpIdOrNameList = map.get(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        if (!CollectionUtils.isEmpty(targetCorpIdOrNameList)) {
            for (Object s : targetCorpIdOrNameList) {
                whereList.add(builder.or(builder.like(mCorp.get(MCorp_.corpId), BaseUtility.addSqlWildcard((String) s)),
                        builder.like(mCorp.get(MCorp_.corpName), BaseUtility.addSqlWildcard((String) s))));
            }
        }

        // 建物(番号または名)
        List<Object> targetBuildingNoOrNameList = map.get(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
        if (!CollectionUtils.isEmpty(targetBuildingNoOrNameList)) {
            for (Object s : targetBuildingNoOrNameList) {
                whereList.add(
                        builder.or(
                                builder.like(building.get(TBuilding_.buildingName),
                                        BaseUtility.addSqlWildcard((String) s)),
                                builder.like(building.get(TBuilding_.buildingNo),
                                        BaseUtility.addSqlWildcard((String) s))));
            }
        }

        // 都道府県
        List<Object> targetPrefectureList = map.get(OsolConstants.SEARCH_CONDITION_PREFECTURE);
        if (!CollectionUtils.isEmpty(targetPrefectureList)) {
            whereList.add(mPrefecture.get(MPrefecture_.prefectureCd).in(targetPrefectureList));
        }

        // 入居形態
        List<Object> targetNyukyoTypeList = map.get(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD);
        if (!CollectionUtils.isEmpty(targetNyukyoTypeList)) {
            whereList.add(building.get(TBuilding_.nyukyoTypeCd).in(targetNyukyoTypeList));
        }

        // 建物状況
        List<Object> targetBuildingStateList = map.get(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        if (!CollectionUtils.isEmpty(targetBuildingStateList)) {
            Date nowDate = (Date) map.get(SmsConstants.SEARCH_CONDITON_TAGET_DATE).get(0);
            nowDate = DateUtility.plusMonth(nowDate, -1); // 現在月が終了年月(稼動月)の場合、終了に含まれるため-1ヶ月
            for (Object s : targetBuildingStateList) {
                if (OsolConstants.BUILDING_SITUATION.NOW.getVal().equals((String) s)) {
                    // 稼働中 (b.totalEndYm IS NULL OR :nowDate <= b.totalEndYm)
                    whereList.add(builder.or(
                            builder.isNull(building.get(TBuilding_.totalEndYm)),
                            builder.greaterThanOrEqualTo(building.get(TBuilding_.totalEndYm), nowDate)));
                    whereList.add(builder.isNull(building.get(TBuilding_.buildingDelDate)));
                } else if (OsolConstants.BUILDING_SITUATION.END.getVal().equals((String) s)) {
                    // 稼動終了 (b.totalEndYm < :nowDate)
                    whereList.add(builder.and(
                            builder.isNotNull(building.get(TBuilding_.totalEndYm)),
                            builder.lessThan(building.get(TBuilding_.totalEndYm), nowDate)));
                    whereList.add(builder.isNull(building.get(TBuilding_.buildingDelDate)));
                } else {
                    // 削除済
                    whereList.add(builder.isNotNull(building.get(TBuilding_.buildingDelDate)));
                }
            }
        }

        // 建物のみ
        whereList.add(builder.equal(building.get(TBuilding_.buildingType), BUILDING_TYPE.BUILDING.getVal()));

        // SMS建物情報の削除フラグが削除済以外を取得
        whereList.add(builder.notEqual(mBuildingSms.get(MBuildingSms_.delFlg), OsolConstants.FLG_ON));

        query = query.select(builder.construct(ListSmsBuildingDetailResultData.class,
                building.get(TBuilding_.id).get(TBuildingPK_.corpId),
                building.get(TBuilding_.MCorp).get(MCorp_.corpName),
                building.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                building.get(TBuilding_.buildingNo),
                building.get(TBuilding_.buildingName),
                building.get(TBuilding_.nyukyoTypeCd),
                building.get(TBuilding_.MPrefecture).get(MPrefecture_.prefectureName),
                building.get(TBuilding_.address),
                building.get(TBuilding_.addressBuilding),
                building.get(TBuilding_.totalEndYm),
                building.get(TBuilding_.buildingDelDate)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(building.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(building.get(TBuilding_.buildingNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsBuildingDetailResultData> getResultList(List<ListSmsBuildingDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsBuildingDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsBuildingDetailResultData find(ListSmsBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsBuildingDetailResultData merge(ListSmsBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsBuildingDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
