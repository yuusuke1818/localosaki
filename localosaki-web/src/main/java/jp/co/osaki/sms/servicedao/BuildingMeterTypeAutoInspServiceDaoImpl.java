package jp.co.osaki.sms.servicedao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.parameter.BuildingAutoInspSearchCondition;
import jp.co.osaki.sms.resultset.BuildingAutoInspResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物に紐付くメーター種別の自動検針データ取得ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class BuildingMeterTypeAutoInspServiceDaoImpl implements BaseServiceDao<BuildingAutoInspResultSet> {

    @Override
    public List<BuildingAutoInspResultSet> getResultList(BuildingAutoInspResultSet target, EntityManager em) {
        return getMeterTypeAutoInspList(em, target);
    }

    /**
     * 建物に紐付くメーター種別の自動検針データListを取得.
     *
     * @param em EntityManager
     * @param target 対象データ
     * @return 建物に紐付くメーター種別の自動検針データList
     */
    private List<BuildingAutoInspResultSet> getMeterTypeAutoInspList(EntityManager em,
            BuildingAutoInspResultSet target) {

        List<TBuilding> buildingList = target.getBuildingList();
        if (CollectionUtils.isEmpty(buildingList)) {
            return null;
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildingAutoInspResultSet> query = builder.createQuery(BuildingAutoInspResultSet.class);

        // 建物
        Root<TBuilding> building = query.from(TBuilding.class);
        // 建物、装置関連テーブル
        Join<TBuilding, MDevRelation> mDevRelation = building.join(TBuilding_.MDevRelations);
        // メーター種別
        Join<TBuilding, MMeterType> mMeterType = building.join(TBuilding_.MMeterTypes);
        // 装置
        Join<MDevRelation, MDevPrm> mDevPrm = mDevRelation.join(MDevRelation_.MDevPrm);
        // 自動検針日時
        Join<MDevPrm, MAutoInsp> mAutoInsp = mDevPrm.join(MDevPrm_.MAutoInsps);

        Path<String> corpIdPath = building.get(TBuilding_.id).get(TBuildingPK_.corpId);
        Path<Long> buildingIdPath = building.get(TBuilding_.id).get(TBuildingPK_.buildingId);
        Path<Long> meterTypePath = mMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType);
        Path<String> meterTypeNamePath = mMeterType.get(MMeterType_.meterTypeName);
        Path<String> autoInspMonthPath = mAutoInsp.get(MAutoInsp_.month);
        Path<String> autoInspDayPath = mAutoInsp.get(MAutoInsp_.day);
        Path<BigDecimal> autoInspHourPath = mAutoInsp.get(MAutoInsp_.hour);

        Expression<?>[] targetColumns = new Expression[] { corpIdPath, buildingIdPath, meterTypePath, meterTypeNamePath,
                autoInspDayPath, autoInspHourPath };

        List<Predicate> targetCorpBuildingList = new ArrayList<>();
        for (TBuilding targetBuilding : buildingList) {
            TBuildingPK tBuildingPk = targetBuilding.getId();
            String corpId = tBuildingPk.getCorpId();
            Long buildingId = tBuildingPk.getBuildingId();

            targetCorpBuildingList
                    .add(builder.and(builder.equal(corpIdPath, corpId), builder.equal(buildingIdPath, buildingId)));
        }

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.or(targetCorpBuildingList.toArray(new Predicate[0])));
        whereList.add(builder.equal(meterTypePath, mAutoInsp.get(MAutoInsp_.id).get(MAutoInspPK_.meterType)));
        whereList.add(builder.notEqual(autoInspMonthPath, SmsConstants.DEFAULT_VALUE_INSPECTION_MONTH));

        List<BuildingAutoInspSearchCondition> autoInspDateList = target.getAutoInspDateList();
        if (CollectionUtils.isNotEmpty(autoInspDateList)) {
            List<Predicate> targetAutoInspDateList = new ArrayList<>();
            for (BuildingAutoInspSearchCondition autoInspDate : autoInspDateList) {
                List<Predicate> targetAutoInspDateInnerList = new ArrayList<>();
                Integer autoInspMonth = autoInspDate.getAutoInspMonth();
                if (autoInspMonth != null) {
                    targetAutoInspDateInnerList
                            .add(builder.like(autoInspMonthPath, createAutoInspMonthLikeCondition(autoInspMonth)));
                }

                String autoInspDay = autoInspDate.getAutoInspDay();
                if (!CheckUtility.isNullOrEmpty(autoInspDay)) {
                    targetAutoInspDateInnerList.add(builder.equal(autoInspDayPath, autoInspDay));
                }

                targetAutoInspDateList.add(builder.and(targetAutoInspDateInnerList.toArray(new Predicate[0])));
            }

            whereList.add(builder.or(targetAutoInspDateList.toArray(new Predicate[0])));
        }

        query.select(builder.construct(BuildingAutoInspResultSet.class, targetColumns)).distinct(true)
                .where(whereList.toArray(new Predicate[0]))
                .groupBy(targetColumns)
                .orderBy(builder.asc(corpIdPath), builder.asc(buildingIdPath), builder.asc(meterTypePath));

        return em.createQuery(query).getResultList();
    }

    /**
     * 検針実行月 LIKE条件を生成.
     *
     * @param targetAutoInspMonth 検針実行月
     * @return 検針実行月 LIKE条件
     */
    private String createAutoInspMonthLikeCondition(int targetAutoInspMonth) {
        StringBuilder likeConditionBuf = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            String condition = "_";
            if (i == targetAutoInspMonth) {
                condition = "1";
            }

            likeConditionBuf.append(condition);
        }

        return likeConditionBuf.toString();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingAutoInspResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingAutoInspResultSet> getResultList(List<BuildingAutoInspResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingAutoInspResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingAutoInspResultSet find(BuildingAutoInspResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingAutoInspResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingAutoInspResultSet merge(BuildingAutoInspResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(BuildingAutoInspResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
