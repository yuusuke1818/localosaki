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
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター（SMS）データフィルターserviceDaoクラス
 * @author nishida.t
 *
 */
public class MeterDataFilterServiceDaoImpl implements BaseServiceDao<MeterDataFilterResultSet> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MeterDataFilterResultSet> getResultList(MeterDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
    *
    * メーター（SMS）データフィルター取得
    *
    * @param parameterMap 検索条件 <br />
    * CORP_ID:企業ID BUILDING_ID:建物ID <br />
    * LOGIN_CORP_ID:ログイン企業ID PERSON_ID:担当者ID DEV_ID:装置ID
    * @param em エンティティマネージャ
    * @return メーターデータフィルター用 取得結果リスト
    */
    @Override
    public List<MeterDataFilterResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        List<BuildingDataFilterResultSet> ListBuilding = new ArrayList<>();
        List<MeterDataFilterResultSet> ListMeter = new ArrayList<>();
        //必須チェック
        if (parameterMap.get(BuildingPersonDevDataParam.CORP_ID) == null || parameterMap.get(BuildingPersonDevDataParam.BUILDING_ID) == null
                || parameterMap.get(PersonDataParam.LOGIN_CORP_ID) == null || parameterMap.get(PersonDataParam.LOGIN_PERSON_ID) == null) {
            return ListMeter;
        }
        // 建物、装置パラメータ
        String corpId = parameterMap.get(BuildingPersonDevDataParam.CORP_ID).get(0).toString();
        Long buildingId = Long.parseLong(parameterMap.get(BuildingPersonDevDataParam.BUILDING_ID).get(0).toString());
        String devId = null;
        if (parameterMap.get(BuildingPersonDevDataParam.DEV_ID) != null) {
            devId = parameterMap.get(BuildingPersonDevDataParam.DEV_ID).get(0).toString();
        }
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
                return ListMeter;
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<MeterDataFilterResultSet> query = cb.createQuery(MeterDataFilterResultSet.class);
            List<Predicate> conditionList = new ArrayList<>();

            // 選択建物の権限がある（建物と装置に紐づくメーター全て）
            if (!ListBuilding.isEmpty()) {
                // メインクエリ（建物に紐づくメーターからサブクエリと突き合わせて取得）
                Root<TBuilding> root = query.from(TBuilding.class);
                Join<TBuilding, TBuildDevMeterRelation> joinTBuildDevMeterReleation = root.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);
                Join<TBuildDevMeterRelation, MMeter> joinMMeter = joinTBuildDevMeterReleation.join(TBuildDevMeterRelation_.MMeter, JoinType.INNER);

                // サブクエリ（テナントが紐づかないメーター取得）
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<TBuilding> subRoot = subQuery.from(TBuilding.class);
                Join<TBuilding, TBuildDevMeterRelation> joinSubTBuildDevMeterReleation = subRoot.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);
                Join<TBuildDevMeterRelation, MMeter> joinSubMMeter = joinSubTBuildDevMeterReleation.join(TBuildDevMeterRelation_.MMeter, JoinType.INNER);

                List<Predicate> conditionSubList = new ArrayList<>();

                // サブクエリ（テナントに紐づいているメーターのみ取得）
                Subquery<Long> subQuery2 = query.subquery(Long.class);
                Root<TBuilding> subRoot2 = subQuery2.from(TBuilding.class);
                Join<TBuilding, TBuildDevMeterRelation> joinSubTBuildDevMeterReleation2 = subRoot2.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);
                Join<TBuildDevMeterRelation, MMeter> joinSubMMeter2 = joinSubTBuildDevMeterReleation2.join(TBuildDevMeterRelation_.MMeter, JoinType.INNER);

                List<Predicate> conditionSubList2 = new ArrayList<>();


                // サブクエリ（テナントが紐づかないメーター取得）条件指定
                // 企業ID
                conditionSubList.add(cb.equal(subRoot.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
                // 所属企業ID
                conditionSubList.add(cb.equal(subRoot.get(TBuilding_.divisionCorpId), corpId));
                // 所属建物ID
                conditionSubList.add(cb.equal(subRoot.get(TBuilding_.divisionBuildingId), buildingId));
                // 建物種別:テナント
                conditionSubList.add(cb.equal(subRoot.get(TBuilding_.buildingType), "1"));
                // 建物削除日付
                conditionSubList.add(cb.isNull(subRoot.get(TBuilding_.buildingDelDate)));
                // 削除フラグ
                conditionSubList.add(cb.equal(subRoot.get(TBuilding_.delFlg), 0));

                // サブクエリ（テナントに紐づいているメーターのみ取得）条件指定
                // 企業ID
                conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
                // 所属企業ID
                conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.divisionCorpId), corpId));
                // 所属建物ID
                conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.divisionBuildingId), buildingId));
                // 建物種別:テナント
                conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.buildingType), "1"));
                // 建物削除日付
                conditionSubList2.add(cb.isNull(subRoot2.get(TBuilding_.buildingDelDate)));
                // 削除フラグ
                conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.delFlg), 0));
                // 契約企業権限は公開フラグ：ONのみ表示
                if (typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("30")
                        || typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("31")) {
                    // 公開フラグ
                    conditionSubList2.add(cb.equal(subRoot2.get(TBuilding_.publicFlg), 1));
                }

                // サブクエリ（テナントが紐づかないメーター取得）
                subQuery.select(joinSubMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId))
                    .where(cb.and(conditionSubList.toArray(new Predicate[] {})));

                // サブクエリ（テナントに紐づいているメーターのみ取得）
                subQuery2.select(joinSubMMeter2.get(MMeter_.id).get(MMeterPK_.meterMngId))
                    .where(cb.and(conditionSubList2.toArray(new Predicate[] {})));

                // メインクエリの条件指定
                // 企業ID
                conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
                // 建物ID
                conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
                if (devId != null) {
                    // 装置ID
                    conditionList.add(cb.equal(joinMMeter.get(MMeter_.id).get(MMeterPK_.devId), devId));
                }
                // 建物削除日付
                conditionList.add(cb.isNull(root.get(TBuilding_.buildingDelDate)));
                // 削除フラグ
                conditionList.add(cb.equal(root.get(TBuilding_.delFlg), 0));

                // 大崎権限以外は削除済みメーターを表示しない
                if (!typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("00")
                        || typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("01")) {
                    // 削除フラグ
                    conditionList.add(cb.equal(joinMMeter.get(MMeter_.delFlg), 0));
                }

                // サブクエリで取得した、テナントに紐づかないメーターと、権限のあるメーターで突き合わせ
                conditionList.add(
                        cb.or(cb.not(joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId).in(subQuery)),
                                joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId).in(subQuery2)));

                query.distinct(true).select(cb.construct(MeterDataFilterResultSet.class,
                        joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId),
                        joinMMeter.get(MMeter_.id).get(MMeterPK_.devId)))
                        .where(cb.and(conditionList.toArray(new Predicate[]{})))
                        .orderBy(
                                cb.asc(joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId)));
                // ---------------------------------------------------------------------------------------------
                // 上記の方が少し条件が増えてるが、だいたい以下のSQLの内容
                /**
                 select * from t_building t

                inner join t_build_dev_meter_relation tb
                on t.corp_id = tb.corp_id
                and t.building_id = tb.building_id

                inner join m_meter m
                on tb.dev_id = m.dev_id
                and tb.meter_mng_id = m.meter_mng_id

                // テナントに紐づいていないメーター
                where m.meter_mng_id not in (
                    select m2.meter_mng_id from t_building t2

                    inner join t_build_dev_meter_relation tb2
                    on t2.corp_id = tb2.corp_id
                    and t2.building_id = tb2.building_id

                    inner join m_meter m2
                    on tb2.dev_id = m2.dev_id
                    and tb2.meter_mng_id = m2.meter_mng_id

                    where t2.corp_id = ?
                    and t2.division_corp_id = ?
                    and t2.division_building_id = ?
                    and t2.building_type = '1'
                )
                and t.corp_id = ?
                and tb.building_id = ?
                and m.dev_id = ?


                // テナントに紐づいているメーターかつ公開フラグ:ON（契約企業権限）
                or m.meter_mng_id in (
                    select m2.meter_mng_id from t_building t2

                    inner join t_build_dev_meter_relation tb2
                    on t2.corp_id = tb2.corp_id
                    and t2.building_id = tb2.building_id

                    inner join m_meter m2
                    on tb2.dev_id = m2.dev_id
                    and tb2.meter_mng_id = m2.meter_mng_id

                    where t2.corp_id = ?
                    and t2.division_corp_id = ?
                    and t2.division_building_id = ?
                    and t2.building_type = '1'
                    and t2.public_flg = '1'  // 契約企業権限だとON
                )
                and t.corp_id = ?
                and t.building_id = ?
                and m.dev_id = ?
                 */
            } else {
                // 選択建物に権限がない（権限のあるテナントに紐づくメーター）
                Root<MCorp> rootMCorp = query.from(MCorp.class);
                Join<MCorp, MPerson> joinMPerson = rootMCorp.join(MCorp_.MPersons, JoinType.INNER);
                Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons, JoinType.INNER);
                Join<MPerson, TBuildingPerson> joinTBuildingPerson = joinMPerson.join(MPerson_.TBuildingPersons, JoinType.INNER);
                Join<TBuildingPerson, TBuilding> joinTBuilding = joinTBuildingPerson.join(TBuildingPerson_.TBuilding, JoinType.INNER);
                Join<TBuilding, MTenantSm> joinMTenantSm = joinTBuilding.join(TBuilding_.MTenantSms, JoinType.INNER);
                Join<TBuilding, TBuildDevMeterRelation> joinTBuildDevMeterReleation = joinTBuilding.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);
                Join<TBuildDevMeterRelation, MMeter> joinMMeter = joinTBuildDevMeterReleation.join(TBuildDevMeterRelation_.MMeter, JoinType.INNER);

                // 建物種別 1:テナント
                conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.buildingType), "1"));
                // 権限種別 1:建物担当
                conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.authorityType), "1"));
                // 所属建物の企業ID
                conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.divisionCorpId), corpId));
                // 所属建物の建物ID
                conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.divisionBuildingId), buildingId));
                // 企業ID
                conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpId), param.getLoginCorpId()));
                // 担当者ID
                conditionList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
                if (devId != null) {
                    // 装置ID
                    conditionList.add(cb.equal(joinMMeter.get(MMeter_.id).get(MMeterPK_.devId), devId));
                }

                // 契約企業権限は公開フラグ：ONのみ表示
                if (typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("30")
                        || typeResultSet.getCorpType().concat(typeResultSet.getPersonType()).equals("31")) {
                    // 公開フラグ
                    conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.publicFlg), 1));
                }

                // アカウント停止フラグ
                conditionList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
                // 建物削除日付
                conditionList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
                // 削除フラグ
                conditionList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));
                // 削除フラグ
                conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), 0));
                // 削除フラグ
                conditionList.add(cb.equal(joinTBuildingPerson.get(TBuildingPerson_.delFlg), 0));
                // 削除フラグ
                conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));
                // 削除フラグ
                conditionList.add(cb.equal(joinMTenantSm.get(MTenantSm_.delFlg), 0));
                // 削除フラグ
                conditionList.add(cb.equal(joinMMeter.get(MMeter_.delFlg), 0));

                query.distinct(true).select(cb.construct(MeterDataFilterResultSet.class,
                        joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId),
                        joinMMeter.get(MMeter_.id).get(MMeterPK_.devId)))
                        .where(cb.and(conditionList.toArray(new Predicate[]{})))
                        .orderBy(
                                cb.asc(joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId)));
            }

            // 権限のあるメーターを取得
            ListMeter = em.createQuery(query).getResultList();
        }
        return ListMeter;
    }

    @Override
    public List<MeterDataFilterResultSet> getResultList(List<MeterDataFilterResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MeterDataFilterResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MeterDataFilterResultSet find(MeterDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MeterDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MeterDataFilterResultSet merge(MeterDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MeterDataFilterResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
