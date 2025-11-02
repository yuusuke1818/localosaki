package jp.co.osaki.osol.access.filter.datafilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;

/**
 *
 * 大崎電気企業の管理者の建物フィルターデータ取得
 *
 * @author take_suzuki
 */
public class BuildingOsakiAdminGetFilterData implements GetFilterDataInterface<BuildingDataFilterResultSet, PersonDataParam> {

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
        CriteriaQuery<BuildingDataFilterResultSet> query = cb.createQuery(BuildingDataFilterResultSet.class);

        Root<TBuilding> rootTBuilding = query.from(TBuilding.class);

        List<Predicate> conditionList = new ArrayList<>();
        //削除フラグ
        conditionList.add(cb.equal(rootTBuilding.get(TBuilding_.delFlg), 0));

        query.select(cb.construct(BuildingDataFilterResultSet.class,
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)))
                .where(cb.and(conditionList.toArray(new Predicate[]{})))
                .orderBy(
                        cb.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        cb.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

        HashMap<String, BuildingDataFilterResultSet> map = new HashMap<>();
        for (BuildingDataFilterResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getCorpId().concat("_").concat(b.getBuildingId().toString()), b);
        }
        return map;
    }

}
