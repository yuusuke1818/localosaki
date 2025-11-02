package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.BUILDING_TYPE;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

public class MeterTenantSmsInfoListDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> criteria = cb.createQuery(TBuilding.class);
        Root<TBuilding> building = criteria.from(TBuilding.class);
        Join<TBuilding, MPrefecture> mPrefecture = building.join(TBuilding_.MPrefecture);

        List<Predicate> searchCondition = new ArrayList<>();

        // SMSで登録されたテナントに絞り込み
        Join<TBuilding, MTenantSm> mTenantSm = building.join(TBuilding_.MTenantSms, JoinType.INNER);

        // 建物(番号または名)
        List<Object> targetBuildingNoList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME);
        if (targetBuildingNoList != null && !targetBuildingNoList.isEmpty()) {
            for (Object s : targetBuildingNoList) {
                searchCondition.add(
                        cb.or(cb.like(building.get(TBuilding_.buildingName), BaseUtility.addSqlWildcard((String) s)),
                                cb.like(building.get(TBuilding_.buildingNo), BaseUtility.addSqlWildcard((String) s))));
            }
        }

        // 都道府県
        List<Object> targetPrefectureList = parameterMap.get(OsolConstants.SEARCH_CONDITION_PREFECTURE);
        if (targetPrefectureList != null && !targetPrefectureList.isEmpty()) {
            searchCondition.add(mPrefecture.get(MPrefecture_.prefectureCd).in(targetPrefectureList));
        }

        // 建物状況
        List<Object> targetBuildingStateList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        if (targetBuildingStateList != null && !targetBuildingStateList.isEmpty()) {
            Date nowDate = (Date) parameterMap.get("targetDate").get(0);
            nowDate = DateUtility.plusMonth(nowDate, -1); // 現在月が終了年月(稼動月)の場合、終了に含まれるため-1ヶ月
            for (Object s : targetBuildingStateList) {
                if (OsolConstants.BUILDING_SITUATION.NOW.getVal().equals((String) s)) {
                    // 稼働中 (b.totalEndYm IS NULL OR :nowDate <= b.totalEndYm)
                    searchCondition.add(cb.or(
                            cb.isNull(building.get(TBuilding_.totalEndYm)),
                            cb.greaterThanOrEqualTo(building.get(TBuilding_.totalEndYm), nowDate)));
                    searchCondition.add(cb.isNull(building.get(TBuilding_.buildingDelDate)));
                } else if (OsolConstants.BUILDING_SITUATION.END.getVal().equals((String) s)) {
                    // 稼動終了 (b.totalEndYm < :nowDate)
                    searchCondition.add(cb.and(
                            cb.isNotNull(building.get(TBuilding_.totalEndYm)),
                            cb.lessThan(building.get(TBuilding_.totalEndYm), nowDate)));
                    searchCondition.add(cb.isNull(building.get(TBuilding_.buildingDelDate)));
                } else {
                    // 削除済
                    searchCondition.add(cb.isNotNull(building.get(TBuilding_.buildingDelDate)));
                }
            }
        }

        // 建物・テナント
        List<Object> targetBuildingTenantList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_TENANT);
        if (targetBuildingTenantList != null && !targetBuildingTenantList.isEmpty()) {
            for (Object s : targetBuildingTenantList) {
                if (s.toString() != null) {
                    searchCondition.add(cb.equal(building.get(TBuilding_.buildingType), s.toString()));
                }
            }
        }
        else {
            // 建物・テナントが設定されていない場合、建物検索
            searchCondition.add(cb.equal(building.get(TBuilding_.buildingType), BUILDING_TYPE.TENANT.getVal()));
        }

        // 建物に属するテナント検索
        List<Object> targetBorrowBuildingList = parameterMap
                .get(OsolConstants.SEARCH_CONDITION_TENANT_BELONG_TO_BUILDING);
        if (targetBorrowBuildingList != null && !targetBorrowBuildingList.isEmpty()) {
            for (Object s : targetBorrowBuildingList) {
                if (s != null) {
                    searchCondition.add(cb.equal(building.get(TBuilding_.divisionBuildingId), s.toString()));
                }
            }
        }

        // 建物に属するテナント検索（SMS用の検索）
        List<Object> targetBorrowCorpList = parameterMap.get("divisionCorpId");
        if (targetBorrowCorpList != null && !targetBorrowCorpList.isEmpty()) {
            for (Object s : targetBorrowCorpList) {
                if (s != null) {
                    searchCondition.add(cb.equal(building.get(TBuilding_.divisionCorpId), s.toString()));
                }
            }
        }

        // ユーザーコード検索（SMS用の検索）
        List<Object> targetTenantIdList = parameterMap.get(SmsConstants.SEARCH_CONDITION_TENANT_ID);
        if (targetTenantIdList != null && !targetTenantIdList.isEmpty()) {
            for (Object s : targetTenantIdList) {
                if (s != null) {
                    searchCondition.add(
                            cb.like(mTenantSm.get(MTenantSm_.tenantId).as(String.class), BaseUtility.addSqlWildcard(s.toString())));
                }
            }
        }

        // 削除データ
        searchCondition.add(cb.notEqual(building.get(TBuilding_.delFlg), OsolConstants.FLG_ON));

        criteria = criteria.select(building)
                .where(cb.and(searchCondition.toArray(new Predicate[] {})))
                .orderBy(cb.asc(mTenantSm.get(MTenantSm_.tenantId)),
                        cb.asc(building.get(TBuilding_.buildingNo)));

        List<TBuilding> tenantList =  em.createQuery(criteria).getResultList();

        //重複削除
        List<TBuilding> retlist = tenantList.stream().distinct().collect(Collectors.toList());
        //企業ID、建物番号、昇順
        Collections.sort(retlist, new Comparator<TBuilding>() {
            @Override
            public int compare(TBuilding o1, TBuilding o2) {
                if (o1.getId().getCorpId().equals(o2.getId().getCorpId())) {
                    return o1.getBuildingNo().compareToIgnoreCase(o2.getBuildingNo());
                } else {
                    return o1.getId().getCorpId().compareToIgnoreCase(o2.getId().getCorpId());
                }
            }
        });
        return retlist;
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
