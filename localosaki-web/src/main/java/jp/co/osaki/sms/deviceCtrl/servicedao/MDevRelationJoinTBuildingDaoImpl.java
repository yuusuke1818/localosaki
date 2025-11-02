package jp.co.osaki.sms.deviceCtrl.servicedao;

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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.sms.deviceCtrl.resultset.MDevRelationJoinTBuildingResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MDevRelationJoinTBuildingDaoImpl implements BaseServiceDao<MDevRelationJoinTBuildingResultSet> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MDevRelationJoinTBuildingResultSet> getResultList(MDevRelationJoinTBuildingResultSet target,
            EntityManager em) {

        return null;
    }

    @Override
    public List<MDevRelationJoinTBuildingResultSet> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {

        return null;
    }

    @Override
    public List<MDevRelationJoinTBuildingResultSet> getResultList(
            List<MDevRelationJoinTBuildingResultSet> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevRelationJoinTBuildingResultSet> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MDevRelationJoinTBuildingResultSet find(MDevRelationJoinTBuildingResultSet target, EntityManager em) {
        // 装置(親子)関係情報を検索
        String devId = target.getDevId();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelationJoinTBuildingResultSet> query = cb.createQuery(MDevRelationJoinTBuildingResultSet.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        // 建物情報と結合
        Join<MDevRelation, TBuilding> joinTBuilding = root.join(MDevRelation_.TBuilding, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置関係情報の装置IDが引数の値と一致するもの
        conditionList.add(cb.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        // 条件2:削除フラグがONでない
        conditionList.add(cb.notEqual(joinTBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_ON));

        query.select(cb.construct(MDevRelationJoinTBuildingResultSet.class,
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingName))).where(cb.and(conditionList.toArray(new Predicate[] {})));
        MDevRelationJoinTBuildingResultSet tTBuilding = em.createQuery(query).getSingleResult();

        return tTBuilding;
    }

    @Override
    public void persist(MDevRelationJoinTBuildingResultSet target, EntityManager em) {


    }

    @Override
    public MDevRelationJoinTBuildingResultSet merge(MDevRelationJoinTBuildingResultSet target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MDevRelationJoinTBuildingResultSet target, EntityManager em) {


    }

}
