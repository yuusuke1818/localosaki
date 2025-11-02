package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.BUILDING_TYPE;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonPK_;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPersonPK_;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * @author n-takada
 */
public class TBuildingJoinBuildingInfoListDaoImpl implements BaseServiceDao<TBuilding> {

    /**
     * 建物検索結果取得
     *
     * @param target
     * @param em
     * @return 建物検索結果リスト
     */
    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {

        TypedQuery<TBuilding> query = em.createNamedQuery("TBuilding.findJoin", TBuilding.class);

        query.setParameter("corpId", target.getId().getCorpId());

        if (StringUtils.isEmpty(target.getBuildingNo())) {
            query.setParameter("buildingNo", null);
        } else {
            query.setParameter("buildingNo", BaseUtility.addSqlWildcard(target.getBuildingNo()));
        }

        if (StringUtils.isEmpty(target.getBuildingName())) {
            query.setParameter("buildingName", null);
        } else {
            query.setParameter("buildingName", BaseUtility.addSqlWildcard(target.getBuildingName()));
        }

        if (StringUtils.isEmpty(target.getMMunicipality().getMPrefecture().getPrefectureCd())) {
            query.setParameter("prefectureCd", null);
        } else {
            query.setParameter("prefectureCd", target.getMMunicipality().getMPrefecture().getPrefectureCd());
        }

        if (target.getTBuildingGroups().get(0).getId().getChildGroupId() <= 0) {
            query.setParameter("childGroupId", null);
        } else {
            query.setParameter("childGroupId", target.getTBuildingGroups().get(0).getId().getChildGroupId());
        }

        //        if (target.getMSubtype().getSubtypeNo() == null) {
        //            query.setParameter("subtypeId", null);
        //        } else {
        //            query.setParameter("subtypeId", target.getMSubtype().getSubtypeNo());
        //        }
        if (target.getTBuildingGroups().get(0).getId().getParentGroupId() <= 0) {
            query.setParameter("parentGroupId", null);
        } else {
            query.setParameter("parentGroupId", target.getTBuildingGroups().get(0).getId().getParentGroupId());
        }

        if (StringUtils.isEmpty(target.getTBuildingPersons().get(0).getId().getPersonId())) {
            query.setParameter("searchUserId", null);
        } else {
            query.setParameter("searchUserId", target.getTBuildingPersons().get(0).getId().getPersonId());
        }

        return query.getResultList();
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        boolean noCheckPerson = false; //担当条件指定しない
        List<Object> targetOsakiFlagList = parameterMap.get(OsolConstants.SEARCH_CONDITION_OSAKI_FLG);
        if (targetOsakiFlagList != null && !targetOsakiFlagList.isEmpty()) {
            if ((boolean) targetOsakiFlagList.get(0)) { //osakiAdmin
                noCheckPerson = true;
            }
        }
        List<Object> targetPersonList = parameterMap.get("person");
        if (targetPersonList == null || targetPersonList.size() != 2) {
            noCheckPerson = true;
        }

        boolean excludeBuidingPerson = false;
        List<Object> targetExcludeList = parameterMap.get("excludeBuidingPerson");
        if (targetExcludeList != null && targetExcludeList.size() == 1) {
            excludeBuidingPerson = true;
        }

        if (noCheckPerson) {
            return getResultListJoinSwitch(parameterMap, em, false, false);
        } else {
            List<TBuilding> corp = getResultListJoinSwitch(parameterMap, em, true, false); //企業担当分
            if (!excludeBuidingPerson) {
                List<TBuilding> build = getResultListJoinSwitch(parameterMap, em, false, true); //建物担当分
                corp.addAll(build);
            }
            //重複削除
            List<TBuilding> retlist = corp.stream().distinct().collect(Collectors.toList());
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
    }

    @SuppressWarnings("unchecked")
    private List<TBuilding> getResultListJoinSwitch(Map<String, List<Object>> parameterMap, EntityManager em,
            boolean joinCorpPerson, boolean joinBuildingPerson) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> criteria = cb.createQuery(TBuilding.class);
        Root<TBuilding> building = criteria.from(TBuilding.class);
        Join<TBuilding, MCorp> mCorp = building.join(TBuilding_.MCorp);
        Join<TBuilding, MPrefecture> mPrefecture = building.join(TBuilding_.MPrefecture);

        Join<TBuilding, TBuildingPerson> tBuildingPerson;
        Join<MCorp, MCorpPerson> mCorpPerson;

        List<Predicate> searchCondition = new ArrayList<>();

        Join<TBuilding, TBuildingGroup> joinTBuildingGroup = building.join(TBuilding_.TBuildingGroups, JoinType.LEFT);
        Join<TBuildingGroup, MChildGroup> joinChildGroup = joinTBuildingGroup.join(TBuildingGroup_.MChildGroup,
                JoinType.LEFT);
        Join<MChildGroup, MParentGroup> joinParentGroup = joinChildGroup.join(MChildGroup_.MParentGroup, JoinType.LEFT);

        //削除フラグ
        joinTBuildingGroup.on(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        joinChildGroup.on(cb.equal(joinChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        joinParentGroup.on(cb.equal(joinParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        //権限
        boolean osakiAdminFlag = false; //全件取得(特定前大崎管理者)
        boolean osakiFlag = false; //削除済みをデフォルト取得(大崎権限)
        List<Object> targetOsakiFlagList = parameterMap.get(OsolConstants.SEARCH_CONDITION_OSAKI_FLG);
        if (targetOsakiFlagList != null && !targetOsakiFlagList.isEmpty()) {
            osakiAdminFlag = (boolean) targetOsakiFlagList.get(0);
            osakiFlag = (boolean) targetOsakiFlagList.get(1);
        }

        //担当
        String personCorpId = null;
        String personId = null;
        List<Object> targetPersonList = parameterMap.get("person");
        if (targetPersonList != null && targetPersonList.size() == 2) {
            personCorpId = targetPersonList.get(0).toString();
            personId = targetPersonList.get(1).toString();
        }
        if (!osakiAdminFlag) {
            if (joinCorpPerson) {
                mCorpPerson = mCorp.join(MCorp_.MCorpPersons);
                searchCondition
                        .add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personCorpId), personCorpId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personId), personId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.authorityType),
                        OsolConstants.AUTHORITY_TYPE.CORP.getVal()));
                searchCondition.add(cb.notEqual(mCorpPerson.get(MCorpPerson_.delFlg), OsolConstants.FLG_ON));
            }
            if (joinBuildingPerson) {
                tBuildingPerson = building.join(TBuilding_.TBuildingPersons);
                searchCondition.add(cb.equal(
                        tBuildingPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personCorpId), personCorpId));
                searchCondition.add(
                        cb.equal(tBuildingPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personId), personId));
                searchCondition.add(cb.notEqual(tBuildingPerson.get(TBuildingPerson_.delFlg), OsolConstants.FLG_ON));
            }
        }

        // 企業ID
        List<Object> targetCorpIdList = parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID);
        if (targetCorpIdList != null && !targetCorpIdList.isEmpty()) {
            for (Object s : targetCorpIdList) {
                searchCondition.add(cb.equal(mCorp.get(MCorp_.corpId), (String) s));
            }
        }

        // 企業(IDまたは名)
        List<Object> targetCorpIdOrNameList = parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        if (targetCorpIdOrNameList != null && !targetCorpIdOrNameList.isEmpty()) {
            for (Object s : targetCorpIdOrNameList) {
                searchCondition.add(cb.or(cb.like(mCorp.get(MCorp_.corpId), BaseUtility.addSqlWildcard((String) s)),
                        cb.like(mCorp.get(MCorp_.corpName), BaseUtility.addSqlWildcard((String) s))));
            }
        }

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
        // 入居形態
        List<Object> targetNyukyoTypeList = parameterMap.get(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD);
        if (targetNyukyoTypeList != null && !targetNyukyoTypeList.isEmpty()) {
            for (Object s : targetNyukyoTypeList) {
                searchCondition.add(cb.equal(building.get(TBuilding_.nyukyoTypeCd), (String) s));
                //TBuildingList.add(cb.like(building.<String>get("nyukyoTypeCd"), (String) s));
            }
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
        } else {
            // 未指定時。大崎権限ユーザーでなければ、運用削除されていない建物で絞り込む
            if (!osakiFlag) {
                searchCondition.add(cb.isNull(building.get(TBuilding_.buildingDelDate)));
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

        // テナントが属する建物検索
        List<Object> targetBelongTenantList = parameterMap
                .get(OsolConstants.SEARCH_CONDITION_BUILDING_BORROW_BY_TENANT);
        boolean divisionBuildingNothingFlg = false;
        if (targetBelongTenantList != null && !targetBelongTenantList.isEmpty()) {
            for (Object s : targetBelongTenantList) {
                if (s != null) {
                    HashMap<String, Object> divisionBuildingInfo = (HashMap<String, Object>) s;
                    if (divisionBuildingInfo.containsKey("divisionCorpId")) {
                        searchCondition.add(cb.equal(building.get(TBuilding_.id).get(TBuildingPK_.corpId),
                                divisionBuildingInfo.get("divisionCorpId")));
                    } else {
                        divisionBuildingNothingFlg = true;
                    }

                    if (divisionBuildingInfo.containsKey("divisionBuildingId")) {
                        searchCondition.add(cb.equal(building.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                                divisionBuildingInfo.get("divisionBuildingId")));
                    } else {
                        divisionBuildingNothingFlg = true;
                    }

                }
            }

        }

        // 建物グルーピング
        List<Object> parentGroupIdList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_GROUPING_PARENT);
        if (parentGroupIdList != null && !parentGroupIdList.isEmpty()) {
            searchCondition
                    .add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId),
                            Long.parseLong(parentGroupIdList.get(0).toString())));
        }

        List<Object> childGroupIdList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_GROUPING_CHILD);
        if (childGroupIdList != null && !childGroupIdList.isEmpty()) {
            searchCondition.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                    Long.parseLong(childGroupIdList.get(0).toString())));
        }

        // TODO
        // 建物種別
        searchCondition.add(cb.equal(building.get(TBuilding_.buildingType), BUILDING_TYPE.BUILDING.getVal()));

        // 削除データ
        searchCondition.add(cb.notEqual(building.get(TBuilding_.delFlg), OsolConstants.FLG_ON));

        criteria = criteria.select(building).distinct(true)
                .where(cb.and(searchCondition.toArray(new Predicate[] {})))
                .orderBy(cb.asc(building.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(building.get(TBuilding_.buildingNo)));
        List<TBuilding> resultTBuilding = new ArrayList<TBuilding>();

        //所属建物検索が有効で所属建物が存在しない場合は空で返す
        if (!divisionBuildingNothingFlg) {
            resultTBuilding = em.createQuery(criteria).getResultList();
        }
        return resultTBuilding;
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        TypedQuery<TBuilding> query = em.createNamedQuery("TBuilding.findByBuildingNo", TBuilding.class);
        query.setParameter("corpId", target.getId().getCorpId());
        query.setParameter("buildingNo", target.getBuildingNo());
        List<TBuilding> list = query.getResultList();
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
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
