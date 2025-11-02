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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonPK_;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPersonPK_;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 建物選択画面_検索用（権限対応済み） <br />
 * 建物のみ取得 <br />
 * テナントは所属している建物を取得
 *
 */
public class BuildingJoinBuildingInfoSearchDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        boolean noCheckPerson = false; // 大崎権限：管理者は、担当条件を指定しない
        boolean checkPublicFlg = false;    // 契約企業権限は、公開フラグのチェックを指定する

        List<Object> targetCorpPersonList = parameterMap.get("CORP_PERSON_TYPE");
        if (targetCorpPersonList != null && !targetCorpPersonList.isEmpty()) {
            if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(targetCorpPersonList.get(0).toString())
                    && OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(targetCorpPersonList.get(1).toString())) {
                //大崎権限：管理者
                noCheckPerson = true;
            }

            if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(targetCorpPersonList.get(0).toString())) {
                //契約企業権限
                checkPublicFlg = true;
            }
        }

        // ログイン担当者情報
        List<Object> targetPersonList = parameterMap.get("person");
        if(targetPersonList == null || targetPersonList.size() != 2){
            noCheckPerson = true;
        }

        // 大崎権限：管理者
        if (noCheckPerson) {
            return getResultListJoinSwitch(parameterMap, em, false, false, checkPublicFlg);
        } else {
            // 大崎権限：管理者以外
            List<TBuilding> corp = getResultListJoinSwitch(parameterMap, em, true, false, checkPublicFlg);  // 企業担当分（建物のみ）
            List<TBuilding> resultBuildingList = getResultListJoinSwitch(parameterMap, em, false, true, checkPublicFlg); // 建物担当分（建物）
            List<TBuilding> tenantList = getTenantList(parameterMap, em, checkPublicFlg); // テナント担当分（テナント）
            List<TBuilding> resultTenantList = getResultListJoinSwitch(parameterMap, em, tenantList, checkPublicFlg); // 建物担当分（テナント所属の建物）
            corp.addAll(resultBuildingList);
            corp.addAll(resultTenantList);
            //重複削除
            List<TBuilding> retlist = corp.stream().distinct().collect(Collectors.toList());
            //企業ID、建物番号、昇順
            Collections.sort(retlist, new Comparator<TBuilding>() {
                @Override
                public int compare(TBuilding o1, TBuilding o2) {
                    if( o1.getId().getCorpId().equals(o2.getId().getCorpId()) ){
                        return o1.getBuildingNo().compareToIgnoreCase(o2.getBuildingNo());
                    }else{
                        return o1.getId().getCorpId().compareToIgnoreCase(o2.getId().getCorpId());
                    }
                }
            });

            return retlist;
        }
    }

    public List<TBuilding> getResultListJoinSwitch(Map<String, List<Object>> parameterMap, EntityManager em,
            boolean joinCorpPerson, boolean joinBuildingPerson, boolean checkPublicFlg) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> criteria = cb.createQuery(TBuilding.class);
        Root<TBuilding> building = criteria.from(TBuilding.class);
        Join<TBuilding, MCorp> mCorp = building.join(TBuilding_.MCorp);
        Join<TBuilding, MPrefecture> mPrefecture = building.join(TBuilding_.MPrefecture, JoinType.INNER);

        // SMS権限のある建物
        Join<TBuilding, MBuildingSms> mBuildingSms = building.join(TBuilding_.MBuildingSms, JoinType.INNER);

        Join<TBuilding, TBuildingPerson> tBuildingPerson;
        Join<MCorp, MCorpPerson> mCorpPerson;

        List<Predicate> searchCondition = new ArrayList<>();

        // 装置が存在する建物サブクエリ―
        searchCondition.add(cb.exists(createDevExistBuildingSubquery(cb, criteria, building)));

        // 権限
        boolean osakiAdminFlag = false; // 全件取得(大崎権限：管理者)
        boolean osakiFlag = false;      // 削除済みをデフォルト取得(大崎権限)
        List<Object> targetCorpPersonList = parameterMap.get("CORP_PERSON_TYPE");
        if (targetCorpPersonList != null && !targetCorpPersonList.isEmpty()) {
            if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(targetCorpPersonList.get(0).toString())) {
                osakiFlag = true;
                if (OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(targetCorpPersonList.get(1).toString())) {
                    osakiAdminFlag = true;
                }
            }
        }

        // 担当者
        String personCorpId = null;
        String personId = null;
        List<Object> targetPersonList = parameterMap.get("person");
        if (targetPersonList != null && targetPersonList.size() == 2) {
            personCorpId = targetPersonList.get(0).toString();
            personId = targetPersonList.get(1).toString();
        }

        // 大崎権限：管理者以外
        if (!osakiAdminFlag) {
            // 企業担当
            if (joinCorpPerson) {
                mCorpPerson = mCorp.join(MCorp_.MCorpPersons, JoinType.INNER);
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personCorpId), personCorpId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personId), personId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.authorityType), OsolConstants.AUTHORITY_TYPE.CORP.getVal()));
                searchCondition.add(cb.notEqual(mCorpPerson.get(MCorpPerson_.delFlg), OsolConstants.FLG_ON));
            }
            // 建物担当
            if (joinBuildingPerson) {
                mCorpPerson = mCorp.join(MCorp_.MCorpPersons, JoinType.INNER);
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personCorpId), personCorpId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.id).get(MCorpPersonPK_.personId), personId));
                searchCondition.add(cb.equal(mCorpPerson.get(MCorpPerson_.authorityType), OsolConstants.AUTHORITY_TYPE.BUILDING.getVal()));
                searchCondition.add(cb.notEqual(mCorpPerson.get(MCorpPerson_.delFlg), OsolConstants.FLG_ON));

                tBuildingPerson = building.join(TBuilding_.TBuildingPersons, JoinType.INNER);
                searchCondition.add(cb.equal(tBuildingPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personCorpId), personCorpId));
                searchCondition.add(cb.equal(tBuildingPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personId), personId));
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
        List<Object> targetBuildingNoList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
        if (targetBuildingNoList != null && !targetBuildingNoList.isEmpty()) {
            for (Object s : targetBuildingNoList) {
                searchCondition.add(cb.or(cb.like(building.get(TBuilding_.buildingName), BaseUtility.addSqlWildcard((String) s)),
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
            }
        }

        // 建物状況
        List<Object> targetBuildingStateList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        if (targetBuildingStateList != null && !targetBuildingStateList.isEmpty()) {
            Date nowDate = (Date) parameterMap.get("targetDate").get(0);
            nowDate = DateUtility.plusMonth(nowDate, -1);    // 現在月が終了年月(稼動月)の場合、終了に含まれるため-1ヶ月
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

        // 建物のみ取得
        searchCondition.add(cb.equal(building.get(TBuilding_.buildingType), OsolConstants.BUILDING_TENANT.BUILDING.getVal()));

        // 契約企業権限の場合、公開可否をチェック
        if (checkPublicFlg) {
            searchCondition.add(cb.equal(building.get(TBuilding_.publicFlg), OsolConstants.FLG_ON));
        }

        // 削除データ
        searchCondition.add(cb.notEqual(building.get(TBuilding_.delFlg), OsolConstants.FLG_ON));
        searchCondition.add(cb.notEqual(mBuildingSms.get(MBuildingSms_.delFlg), OsolConstants.FLG_ON));

        criteria = criteria.select(building).distinct(true)
                .where(cb.and(searchCondition.toArray(new Predicate[] {})))
                .orderBy(cb.asc(building.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(building.get(TBuilding_.buildingNo))
                );
        return em.createQuery(criteria).getResultList();
    }

    // 担当テナント一覧
    public List<TBuilding> getTenantList(Map<String, List<Object>> parameterMap, EntityManager em, boolean checkPublicFlg) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = cb.createQuery(TBuilding.class);
        Root<TBuilding> tenant = query.from(TBuilding.class);
        Join<TBuilding, TBuildingPerson> tTenantPerson = tenant.join(TBuilding_.TBuildingPersons, JoinType.INNER);

        // SMSで利用するテナント（担当しているテナントがSMS利用テナント）
        tenant.join(TBuilding_.MTenantSms, JoinType.INNER);

        List<Predicate> whereListSub = new ArrayList<>();
        // 担当者
        String personCorpId = null;
        String personId = null;
        List<Object> targetPersonList = parameterMap.get("person");
        if (targetPersonList != null && targetPersonList.size() == 2) {
            personCorpId = targetPersonList.get(0).toString();
            personId = targetPersonList.get(1).toString();
        }
        whereListSub.add(cb.equal(tTenantPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personCorpId), personCorpId));
        whereListSub.add(cb.equal(tTenantPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personId), personId));
        whereListSub.add(cb.notEqual(tTenantPerson.get(TBuildingPerson_.delFlg), OsolConstants.FLG_ON));

        whereListSub.add(cb.equal(tenant.get(TBuilding_.buildingType), OsolConstants.BUILDING_TENANT.TENANT.getVal()));

        // 契約企業権限の場合、公開可否をチェック
        if (checkPublicFlg) {
            whereListSub.add(cb.equal(tenant.get(TBuilding_.publicFlg), OsolConstants.FLG_ON));
        }

        // 削除データ
        whereListSub.add(cb.notEqual(tenant.get(TBuilding_.delFlg), OsolConstants.FLG_ON));

        query.select(tenant)
            .where(cb.and(whereListSub.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    public List<TBuilding> getResultListJoinSwitch(Map<String, List<Object>> parameterMap, EntityManager em,
            List<TBuilding> tenantList, boolean checkPublicFlg) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> criteria = cb.createQuery(TBuilding.class);
        Root<TBuilding> building = criteria.from(TBuilding.class);
        Join<TBuilding, MCorp> mCorp = building.join(TBuilding_.MCorp, JoinType.INNER);
        Join<TBuilding, MPrefecture> mPrefecture = building.join(TBuilding_.MPrefecture, JoinType.INNER);

        // SMS権限のある建物
        Join<TBuilding, MBuildingSms> mBuildingSms = building.join(TBuilding_.MBuildingSms, JoinType.INNER);

        List<Predicate> searchCondition = new ArrayList<>();
        List<Predicate> whereOrList = new ArrayList<>();

        // 装置が存在する建物サブクエリ―
        searchCondition.add(cb.exists(createDevExistBuildingSubquery(cb, criteria, building)));

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
        List<Object> targetBuildingNoList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
        if (targetBuildingNoList != null && !targetBuildingNoList.isEmpty()) {
            for (Object s : targetBuildingNoList) {
                searchCondition.add(cb.or(cb.like(building.get(TBuilding_.buildingName), BaseUtility.addSqlWildcard((String) s)),
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
            }
        }

        // 建物状況
        List<Object> targetBuildingStateList = parameterMap.get(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        if (targetBuildingStateList != null && !targetBuildingStateList.isEmpty()) {
            Date nowDate = (Date) parameterMap.get("targetDate").get(0);
            nowDate = DateUtility.plusMonth(nowDate, -1);    // 現在月が終了年月(稼動月)の場合、終了に含まれるため-1ヶ月
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

        // 建物のみ取得
        searchCondition.add(cb.equal(building.get(TBuilding_.buildingType), OsolConstants.BUILDING_TENANT.BUILDING.getVal()));

        // 契約企業権限の場合、公開可否をチェック（建物公開ON、テナント公開OFFの時用）
        if (checkPublicFlg) {
            searchCondition.add(cb.equal(building.get(TBuilding_.publicFlg), OsolConstants.FLG_ON));
        }

        // 削除データ
        searchCondition.add(cb.notEqual(building.get(TBuilding_.delFlg), OsolConstants.FLG_ON));
        searchCondition.add(cb.notEqual(mBuildingSms.get(MBuildingSms_.delFlg), OsolConstants.FLG_ON));

        // 所属建物検索
        for (TBuilding tenant : tenantList) {
            if (tenant.getDivisionCorpId() != null && tenant.getDivisionBuildingId() != null) {
                whereOrList.add(
                        cb.and(
                                cb.equal(building.get(TBuilding_.id).get(TBuildingPK_.corpId), tenant.getDivisionCorpId()),
                                cb.equal(building.get(TBuilding_.id).get(TBuildingPK_.buildingId), tenant.getDivisionBuildingId())
                                )
                       );
            }
        }

        criteria = criteria.select(building).distinct(true)
                .where(cb.or(whereOrList.toArray(new Predicate[] {})), cb.and(searchCondition.toArray(new Predicate[] {})))
                .orderBy(cb.asc(building.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(building.get(TBuilding_.buildingNo))
                );
        return em.createQuery(criteria).getResultList();
    }

    /**
     * 装置が存在する建物サブクエリ―を生成.
     *
     * @param cb CriteriaBuilder
     * @param criteria CriteriaQuery
     * @param buildingPath 建物
     * @return 装置が存在する建物サブクエリ―
     */
    private Subquery<TBuilding> createDevExistBuildingSubquery(CriteriaBuilder cb, CriteriaQuery<TBuilding> criteria,
            Path<TBuilding> buildingPath) {

        Path<TBuildingPK> buildingPathPk = buildingPath.get(TBuilding_.id);

        Subquery<TBuilding> subquery = criteria.subquery(TBuilding.class);
        Root<TBuilding> buildingPathSq = subquery.from(TBuilding.class);
        Join<TBuilding, MDevRelation> mDevRelationPathSq = buildingPathSq.join(TBuilding_.MDevRelations);
        Join<MDevRelation, MDevPrm> mDevPrmPathSq = mDevRelationPathSq.join(MDevRelation_.MDevPrm);

        Path<TBuildingPK> buildingPathPkSq = buildingPathSq.get(TBuilding_.id);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(buildingPathPkSq.get(TBuildingPK_.corpId), buildingPathPk.get(TBuildingPK_.corpId)));
        whereList.add(
                cb.equal(buildingPathPkSq.get(TBuildingPK_.buildingId), buildingPathPk.get(TBuildingPK_.buildingId)));
        whereList.add(cb.equal(mDevPrmPathSq.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        subquery.select(buildingPathSq).where(cb.and(whereList.toArray(new Predicate[] {})));

        return subquery;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
