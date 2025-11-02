package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

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

import jp.co.osaki.osol.api.resultdata.sms.meterTenant.BuildDevMeterResultData;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物装置メーター取得
 * @author nishida.t
 *
 */
public class BuildDevMeterServiceDaoImpl implements BaseServiceDao<BuildDevMeterResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<BuildDevMeterResultData> getResultList(BuildDevMeterResultData target, EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildDevMeterResultData> query = builder.createQuery(BuildDevMeterResultData.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, TBuildDevMeterRelation> joinTBuildDevMeterRelation = root.join(TBuilding_.TBuildDevMeterRelations , JoinType.INNER);
        Join<TBuildDevMeterRelation, MMeter> joinMMeter = joinTBuildDevMeterRelation.join(TBuildDevMeterRelation_.MMeter , JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // 企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 建物ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query = query.select(builder.construct(BuildDevMeterResultData.class,
                joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId),
                joinMMeter.get(MMeter_.meterId)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<BuildDevMeterResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<BuildDevMeterResultData> getResultList(List<BuildDevMeterResultData> entityList,
            EntityManager em) {
        return null;
    }

    @Override
    public List<BuildDevMeterResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public BuildDevMeterResultData find(BuildDevMeterResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(BuildDevMeterResultData target, EntityManager em) {

    }

    @Override
    public BuildDevMeterResultData merge(BuildDevMeterResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(BuildDevMeterResultData target, EntityManager em) {

    }


}
