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
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;

/**
 *
 * 契約企業の建物フィルターデータ取得
 *
 * @author take_suzuki
 */
public class BuildingContractPersonGetFilterData implements GetFilterDataInterface<BuildingDataFilterResultSet, PersonDataParam> {

    /**
     *
     * 建物フィルターのデータ取得
     *
     * @param em エンティティマネージャ
     * @param param ログイン担当者情報
     * @return 建物フィルター用データ
     */
    @Override
    public Map<String, BuildingDataFilterResultSet> getFilterData(EntityManager em, PersonDataParam param) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BuildingDataFilterResultSet> query = cb.createQuery(BuildingDataFilterResultSet.class);

        Root<MCorp> rootMCorp = query.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson = rootMCorp.join(MCorp_.MPersons, JoinType.INNER);
        Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons, JoinType.INNER);
        Join<MPerson, TBuildingPerson> joinTBuildingPerson = joinMPerson.join(MPerson_.TBuildingPersons, JoinType.INNER);
        Join<TBuildingPerson, TBuilding> joinTBuilding = joinTBuildingPerson.join(TBuildingPerson_.TBuilding, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 企業ID
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpId), param.getLoginCorpId()));
        // 担当者ID
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
        //企業種別 3:契約企業
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpType), "3"));
        //権限種別 1:建物担当
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.authorityType), "1"));
        //公開フラグ
        conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.publicFlg), 1));
        //アカウント停止フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
        //建物削除日付
        conditionList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
        //削除フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinTBuildingPerson.get(TBuildingPerson_.delFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));

        query.distinct(true).select(cb.construct(BuildingDataFilterResultSet.class,
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)))
                .where(cb.and(conditionList.toArray(new Predicate[]{})))
                .orderBy(
                        cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

        HashMap<String, BuildingDataFilterResultSet> map = new HashMap<>();
        for (BuildingDataFilterResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getCorpId().concat("_").concat(b.getBuildingId().toString()), b);
        }
        return map;
    }

}
