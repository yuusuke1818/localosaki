/**
*
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.bean.sms.collect.setting.meter.UpdateSmsMeterBean;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterParameter;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、メーター関連テーブル ServiceDaoクラス
 * @author sagi_h
 *
 */
public class TBuildDevMeterRelationServiceDaoImpl implements BaseServiceDao<TBuildDevMeterRelation> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<TBuildDevMeterRelation> query = cb.createCriteriaUpdate(TBuildDevMeterRelation.class);
        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);

        List<Predicate> conditionList = new ArrayList<>();

        // メーター登録内容・設定内容変更から呼び出しの場合
        if (parameterMap.containsKey(UpdateSmsMeterBean.class.getTypeName())) {
            // APIパラメーターを取得
            final UpdateSmsMeterParameter newParam = (UpdateSmsMeterParameter) parameterMap
                    .get(UpdateSmsMeterBean.class.getTypeName()).get(0);
            final Long oldBuildingId = (Long) parameterMap.get("oldBuildingId").get(0);
            final Long newBuildingId = (Long) parameterMap.get("newBuildingId").get(0);
            final MDevRelation mdr = getDivisionBuilding(newParam.getResult().getDevId(), em);

            // 条件1: 変更前のテナントの建物IDに一致
            conditionList.add(cb.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                    oldBuildingId));
            // 条件2: 操作企業IDに一致
            conditionList.add(cb.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.corpId),
                    mdr.getId().getCorpId()));
            // 条件3: パラメーターの装置IDに一致
            conditionList.add(cb.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId),
                    newParam.getResult().getDevId()));
            // 条件4: パラメーターのメーター管理番号に一致
            conditionList.add(cb.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId),
                    newParam.getResult().getMeterMngId()));

            // テナント建物IDを変更後の建物IDに変更
            query.set(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                    newBuildingId).where(cb.and(conditionList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).executeUpdate();
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(TBuildDevMeterRelation target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        final String devId = (String) parameterMap.get("devId").get(0);
        final Long meterMngId = (Long) parameterMap.get("meterMngId").get(0);

        Boolean tenantSearchFlg = false;
        if (parameterMap.get("tenantSearchFlg") != null) {
            tenantSearchFlg = (Boolean) parameterMap.get("tenantSearchFlg").get(0);
        }

        // 装置IDから所属建物ID情報を取得
        MDevRelation mdr = getDivisionBuilding(devId, em);

        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildDevMeterRelation> query = builder.createQuery(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 条件1: 装置IDが引数に一致
        whereList
                .add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));
        // 条件2: メーター管理番号が引数に一致
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId),
                meterMngId));

        if (tenantSearchFlg.booleanValue()) {
            // 条件3: (テナント検索が指定されていれば) 建物IDが建物装置中間テーブルから取得したもの(これはテナントではなく施設のため)と一致しない
            whereList.add(
                    builder.notEqual(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                            mdr.getId().getBuildingId()));
            if (parameterMap.get("tenantBuildingId") != null) {
                final Long tenantBuildingId = (Long) parameterMap.get("tenantBuildingId").get(0);

                // 条件4: (テナントの建物IDが指定されていれば) 建物IDがテナントの建物IDに一致
                whereList.add(builder.equal(
                        root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                        tenantBuildingId));
            }
        }

        query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(List<TBuildDevMeterRelation> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TBuildDevMeterRelation find(TBuildDevMeterRelation target, EntityManager em) {
        return em.find(TBuildDevMeterRelation.class, target.getId());
    }

    @Override
    public void persist(TBuildDevMeterRelation target, EntityManager em) {

        // 企業IDがnullの場合は建物装置中間テーブルから照合
        if (target.getId().getCorpId() == null) {
            // 装置IDから所属建物ID情報を取得
            MDevRelation mdr = getDivisionBuilding(target.getId().getDevId(), em);
            target.getId().setCorpId(mdr.getId().getCorpId());

            // 建物IDもnullの場合は所属建物IDを登録
            if (target.getId().getBuildingId() == null) {
                target.getId().setBuildingId(mdr.getId().getBuildingId());
            }
        }
        em.persist(target);
        return;
    }

    @Override
    public TBuildDevMeterRelation merge(TBuildDevMeterRelation target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TBuildDevMeterRelation target, EntityManager em) {
        em.remove(target);
    }

    /**
     * 装置IDと対応する建物を返す
     * @param devId 装置ID
     * @param em エンティティマネージャー
     * @return 対応する建物
     */
    private MDevRelation getDivisionBuilding(final String devId, EntityManager em) {
        // 装置IDから所属建物ID情報を取得
        CriteriaBuilder buildermdr = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelation> querymdr = buildermdr.createQuery(MDevRelation.class);

        Root<MDevRelation> rootmdr = querymdr.from(MDevRelation.class);
        querymdr.select(rootmdr)
                .where(buildermdr.equal(rootmdr.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        return em.createQuery(querymdr).getResultList().get(0);

    }
}
