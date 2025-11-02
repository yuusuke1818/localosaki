package jp.co.osaki.osol.access.filter.servicedao;

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
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.access.filter.datafilter.BuildingContractAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingContractPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingOsakiAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingOsakiPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingPartnerGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.GetFilterDataInterface;
import jp.co.osaki.osol.access.filter.datafilter.PersonTypeGetFilterData;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.access.filter.resultset.DevPrmDataFilterResultSet;
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 装置情報 データフィルターserviceDaoクラス
 * @author nishida.t
 *
 */
public class DevPrmDataFilterServiceDaoImpl implements BaseServiceDao<DevPrmDataFilterResultSet> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DevPrmDataFilterResultSet> getResultList(DevPrmDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
    *
    * 装置情報 データフィルター取得
    *
    * @param parameterMap 検索条件 <br />
    * CORP_ID:企業ID BUILDING_ID:建物ID <br />
    * LOGIN_CORP_ID:ログイン企業ID PERSON_ID:担当者ID
    * @param em エンティティマネージャ
    * @return 装置データフィルター用 取得結果リスト
    */
    @Override
    public List<DevPrmDataFilterResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        List<BuildingDataFilterResultSet> ListBuilding = new ArrayList<>();
        List<DevPrmDataFilterResultSet> ListDevPrm= new ArrayList<>();
        //必須チェック
        if (parameterMap.get(BuildingPersonDevDataParam.CORP_ID) == null || parameterMap.get(BuildingPersonDevDataParam.BUILDING_ID) == null
                || parameterMap.get(PersonDataParam.LOGIN_CORP_ID) == null || parameterMap.get(PersonDataParam.LOGIN_PERSON_ID) == null) {
            return ListDevPrm;
        }
        // 建物パラメータ
        String corpId = parameterMap.get(BuildingPersonDevDataParam.CORP_ID).get(0).toString();
        Long buildingId = Long.parseLong(parameterMap.get(BuildingPersonDevDataParam.BUILDING_ID).get(0).toString());

        // 担当者パラメータ
        PersonDataParam param = new PersonDataParam(
                parameterMap.get(PersonDataParam.LOGIN_CORP_ID).get(0).toString(),
                parameterMap.get(PersonDataParam.LOGIN_PERSON_ID).get(0).toString());
        PersonTypeGetFilterData ptgf = new PersonTypeGetFilterData();
        Map<String, PersonTypeResultSet> personMap = ptgf.getFilterData(em, param);
        PersonTypeResultSet typeResultSet = null;
        if (!personMap.isEmpty()) {
            typeResultSet = personMap.entrySet().iterator().next().getValue();
        }
        if (typeResultSet != null) {
            GetFilterDataInterface<BuildingDataFilterResultSet, PersonDataParam> gl = null;
            // 企業種別、担当者種別の判定
            switch (typeResultSet.getCorpType().concat(typeResultSet.getPersonType())) {
                // 大崎電気/管理者
                case "00":
                    gl = new BuildingOsakiAdminGetFilterData();
                    break;
                // 大崎電気/担当者
                case "01":
                    gl = new BuildingOsakiPersonGetFilterData();
                    break;
                // パートナー/管理者/担当者
                case "10":
                case "11":
                    gl = new BuildingPartnerGetFilterData();
                    break;
                // メンテナンス/管理者・担当者（SMSでは利用しない）
                case "20":
                case "21":
                    break;
                // 契約企業/管理者
                case "30":
                    gl = new BuildingContractAdminGetFilterData();
                    break;
                // 契約企業/担当者
                case "31":
                    gl = new BuildingContractPersonGetFilterData();
                    break;
                default:
                    break;
            }

            // 権限のある建物・テナント取得
            if (gl != null) {
                for(Map.Entry<String, BuildingDataFilterResultSet> entry : gl.getFilterData(em, param).entrySet()) {
                    if (entry.getValue().getCorpId().equals(corpId) && entry.getValue().getBuildingId().equals(buildingId)) {
                        ListBuilding.add(entry.getValue());
                    }
                }
            } else {
                return ListDevPrm;
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<DevPrmDataFilterResultSet> query = cb.createQuery(DevPrmDataFilterResultSet.class);
            List<Predicate> conditionList = new ArrayList<>();

            // 選択した建物の権限がある（建物に紐づくSMSテナント全て）
            if (!ListBuilding.isEmpty()) {
                Root<TBuilding> root = query.from(TBuilding.class);
                Join<TBuilding, MDevRelation> joinMDevRelation = root.join(TBuilding_.MDevRelations, JoinType.INNER);

                // 企業ID
                conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
                // 建物D
                conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

                // 大崎権限以外は削除済みは表示しない
                if (!typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("00")
                        || typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("01")) {
                    // 建物削除日付
                    conditionList.add(cb.isNull(root.get(TBuilding_.buildingDelDate)));
                    // 削除フラグ
                    conditionList.add(cb.equal(root.get(TBuilding_.delFlg), 0));
                }

                query.select(cb.construct(DevPrmDataFilterResultSet.class,
                        joinMDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.devId)))
                        .where(cb.and(conditionList.toArray(new Predicate[]{})))
                        .groupBy(joinMDevRelation.get(MDevRelation_.id).get(MDevRelationPK_.devId));

            } else {
                // 選択建物に権限がない（権限のあるSMSテナントのみ取得して絞り込む）
                Root<TBuilding> root = query.from(TBuilding.class);
                Join<TBuilding, TBuildDevMeterRelation> joinTBuildDevMeterRelation = root.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);

                // サブクエリ
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<MCorp> subRoot = subQuery.from(MCorp.class);
                Join<MCorp, MPerson> joinMPerson = subRoot.join(MCorp_.MPersons, JoinType.INNER);
                Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons, JoinType.INNER);
                Join<MPerson, TBuildingPerson> joinTBuildingPerson = joinMPerson.join(MPerson_.TBuildingPersons, JoinType.INNER);
                Join<TBuildingPerson, TBuilding> joinTBuilding = joinTBuildingPerson.join(TBuildingPerson_.TBuilding, JoinType.INNER);
                Join<TBuilding, MTenantSm> joinMTenantSm = joinTBuilding.join(TBuilding_.MTenantSms, JoinType.INNER);

                List<Predicate> conditionSubList = new ArrayList<>();

                // 建物種別 1:テナント
                conditionSubList.add(cb.equal(joinTBuilding.get(TBuilding_.buildingType), "1"));
                // 権限種別 1:建物担当
                conditionSubList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.authorityType), "1"));
                // 所属建物の企業ID
                conditionSubList.add(cb.equal(joinTBuilding.get(TBuilding_.divisionCorpId), corpId));
                // 所属建物の建物ID
                conditionSubList.add(cb.equal(joinTBuilding.get(TBuilding_.divisionBuildingId), buildingId));
                // 企業ID
                conditionSubList.add(cb.equal(subRoot.get(MCorp_.corpId), param.getLoginCorpId()));
                // 担当者ID
                conditionSubList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));

                // 契約企業権限は公開フラグ：ONのみ表示
                if (typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("30")
                        || typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("31")) {
                    // 公開フラグ
                    conditionSubList.add(cb.equal(joinTBuilding.get(TBuilding_.publicFlg), 1));
                }

                // アカウント停止フラグ
                conditionSubList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
                // 建物削除日付
                conditionSubList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
                // 削除フラグ
                conditionSubList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));
                // 削除フラグ
                conditionSubList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), 0));
                // 削除フラグ
                conditionSubList.add(cb.equal(joinTBuildingPerson.get(TBuildingPerson_.delFlg), 0));
                // 削除フラグ
                conditionSubList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));
                // 削除フラグ
                conditionSubList.add(cb.equal(joinMTenantSm.get(MTenantSm_.delFlg), 0));

                // サブクエリ
                subQuery.distinct(true).select(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId))
                    .where(cb.and(conditionSubList.toArray(new Predicate[] {})));

                // 企業ID
                conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
                // 建物D
                conditionList.add(root.get(TBuilding_.id).get(TBuildingPK_.buildingId).in(subQuery));

                query.select(cb.construct(DevPrmDataFilterResultSet.class,
                        joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId)))
                        .where(cb.and(conditionList.toArray(new Predicate[]{})))
                        .groupBy(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId));

            }

            // 権限のある装置を取得
            ListDevPrm = em.createQuery(query).getResultList();
        }
        return ListDevPrm;
    }

    @Override
    public List<DevPrmDataFilterResultSet> getResultList(List<DevPrmDataFilterResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DevPrmDataFilterResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DevPrmDataFilterResultSet find(DevPrmDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DevPrmDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DevPrmDataFilterResultSet merge(DevPrmDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DevPrmDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
