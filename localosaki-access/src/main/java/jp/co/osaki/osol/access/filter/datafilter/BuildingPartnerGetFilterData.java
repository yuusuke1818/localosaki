package jp.co.osaki.osol.access.filter.datafilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPersonPK_;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;

/**
 *
 * パートナー企業の建物フィルターデータ取得
 *
 * @author take_suzuki
 *
 */
public class BuildingPartnerGetFilterData implements GetFilterDataInterface<BuildingDataFilterResultSet, PersonDataParam> {

    /**
     *
     * 建物フィルターのデータ取得
     *
     * @param em エンティティマネージャ
     * @param param 担当者パラメータ
     * @return 建物フィルター用データ
     */
    @Override
    public Map<String, BuildingDataFilterResultSet> getFilterData(EntityManager em, PersonDataParam param) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        HashMap<String, BuildingDataFilterResultSet> map = new HashMap<>();

        /**
         * 権限種別 0:管理担当の場合は、担当企業の全建物
         */
        CriteriaQuery<BuildingDataFilterResultSet> queryCorp = cb.createQuery(BuildingDataFilterResultSet.class);
        Root<MCorp> rootMCorp = queryCorp.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson = rootMCorp.join(MCorp_.MPersons, JoinType.INNER);
        Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons, JoinType.INNER);
        Join<MCorpPerson, MCorp> joinMCorpOp = joinMCorpPerson.join(MCorpPerson_.MCorp, JoinType.INNER);
        Join<MCorp, TBuilding> joinTBuilding = joinMCorpOp.join(MCorp_.TBuildings, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        //企業ID
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpId), param.getLoginCorpId()));
        //パートナー
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpType), "1"));
        //担当者ID
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
        //アカウント停止フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));
        //権限種別 0:管理担当
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.authorityType), "0"));
        //削除フラグ
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), 0));
        //建物削除日付
        conditionList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
        //削除フラグ
        conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));

        queryCorp.select(cb.construct(BuildingDataFilterResultSet.class,
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[]{})))
                .orderBy(
                        cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId))
                );

        for (BuildingDataFilterResultSet data1 : em.createQuery(queryCorp).getResultList()) {
            if (!map.containsKey(data1.getCorpId().concat("_").concat(data1.getBuildingId().toString()))){
                map.put(data1.getCorpId().concat("_").concat(data1.getBuildingId().toString()), data1);
            }
        }

        /**
         * 権限種別 1:建物担当の場合は、担当建物のみ
         */
        CriteriaQuery<BuildingDataFilterResultSet> queryCorp2 = cb.createQuery(BuildingDataFilterResultSet.class);
        Root<MCorp> rootMCorp2 = queryCorp2.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson2 = rootMCorp2.join(MCorp_.MPersons, JoinType.INNER);
        Join<MPerson, MCorpPerson> joinMCorpPerson2 = joinMPerson2.join(MPerson_.MCorpPersons, JoinType.INNER);
        Join<MCorpPerson, MCorp> joinMCorpPersonMCorp2 = joinMCorpPerson2.join(MCorpPerson_.MCorp, JoinType.INNER);
        Join<MCorp, TBuilding> joinMCorpTBuilding2 = joinMCorpPersonMCorp2.join(MCorp_.TBuildings, JoinType.INNER);
        Join<TBuilding, TBuildingPerson> joinTBuildingTBuildingPerson2 = joinMCorpTBuilding2.join(TBuilding_.TBuildingPersons, JoinType.INNER);

        List<Predicate> conditionList2 = new ArrayList<>();
        //企業ID
        conditionList2.add(cb.equal(rootMCorp2.get(MCorp_.corpId), param.getLoginCorpId()));
        //パートナー
        conditionList2.add(cb.equal(rootMCorp2.get(MCorp_.corpType), "1"));
        //担当者ID
        conditionList2.add(cb.equal(joinMPerson2.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
        //アカウント停止フラグ
        conditionList2.add(cb.equal(joinMPerson2.get(MPerson_.accountStopFlg), 0));
        //削除フラグ
        conditionList2.add(cb.equal(joinMPerson2.get(MPerson_.delFlg), 0));
        //権限種別 1:建物担当
        conditionList2.add(cb.equal(joinMCorpPerson2.get(MCorpPerson_.authorityType), "1"));
        //削除フラグ
        conditionList2.add(cb.equal(joinMCorpPerson2.get(MCorpPerson_.delFlg), 0));
        //建物削除日付
        conditionList2.add(cb.isNull(joinMCorpTBuilding2.get(TBuilding_.buildingDelDate)));
        //削除フラグ
        conditionList2.add(cb.equal(joinMCorpTBuilding2.get(TBuilding_.delFlg), 0));
        //企業ID
        conditionList2.add(cb.equal(joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personCorpId), param.getLoginCorpId()));
        //担当者ID
        conditionList2.add(cb.equal(joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.personId), param.getLoginPersonId()));
        //削除フラグ
        conditionList2.add(cb.equal(joinTBuildingTBuildingPerson2.get(TBuildingPerson_.delFlg), 0));

        queryCorp2.select(cb.construct(BuildingDataFilterResultSet.class,
            joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.corpId),
            joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.buildingId)))
            .distinct(true)
            .where(cb.and(conditionList2.toArray(new Predicate[]{})))
            .orderBy(
                cb.asc(joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.corpId)),
                cb.asc(joinTBuildingTBuildingPerson2.get(TBuildingPerson_.id).get(TBuildingPersonPK_.buildingId))
            );

        for (BuildingDataFilterResultSet data2 : em.createQuery(queryCorp2).getResultList()) {
            if (!map.containsKey(data2.getCorpId().concat("_").concat(data2.getBuildingId().toString()))){
                map.put(data2.getCorpId().concat("_").concat(data2.getBuildingId().toString()), data2);
            }
        }

        return map;
    }

}
