package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.sms.resultset.MDevPrmListResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 装置情報ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class MDevPrmListServiceDaoImpl implements BaseServiceDao<MDevPrm> {

    /** パラメータキー */
    public static final String PARAM_KEY = "dbParam";

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        MDevPrmListResultSet param = (MDevPrmListResultSet) parameterMap.get(PARAM_KEY).get(0);

        List<MDevPrm> devPrmList = null;
        if (param.isTenant()) {
            // テナントの場合
            devPrmList = createTenantDevPrmList(param, em);
        } else {
            devPrmList = createBuildingDevPrmList(param, em);
        }

        return devPrmList;
    }

    /**
     * 建物に紐付く装置情報Listを生成.
     *
     * @param param 検索条件
     * @param em エンティティマネージャ
     * @return 建物に紐付く装置情報List
     */
    private List<MDevPrm> createBuildingDevPrmList(MDevPrmListResultSet param, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MDevPrm> criteria = cb.createQuery(MDevPrm.class);

        // 装置情報(M_DEV_PRM)
        Root<MDevPrm> devPrm = criteria.from(MDevPrm.class);

        // 建物、装置関連テーブル(M_DEV_RELATION)
        Join<MDevPrm, MDevRelation> devRelation = devPrm.join(MDevPrm_.MDevRelations);
        devRelation.on(cb.equal(devPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        Collection<Predicate> whereClct = new ArrayList<>();

        // 企業ID
        whereClct.add(cb.equal(devRelation.get(MDevRelation_.id).get(MDevRelationPK_.corpId), param.getCorpId()));
        // 建物ID
        whereClct.add(
                cb.equal(devRelation.get(MDevRelation_.id).get(MDevRelationPK_.buildingId), param.getBuildingId()));

        criteria = criteria.select(devPrm)
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(cb.asc(devPrm.get(MDevPrm_.name)), cb.asc(devPrm.get(MDevPrm_.devId)));

        return em.createQuery(criteria).getResultList();
    }

    /**
     * メーターを介してテナントに紐付く装置情報Listを生成.
     *
     * @param param 検索条件
     * @param em エンティティマネージャ
     * @return メーターを介してテナントに紐付く装置情報List
     */
    private List<MDevPrm> createTenantDevPrmList(MDevPrmListResultSet param, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MDevPrm> criteria = cb.createQuery(MDevPrm.class);

        // 装置情報(M_DEV_PRM)
        Root<MDevPrm> devPrm = criteria.from(MDevPrm.class);

        // メータ登録用(M_METER)
        Join<MDevPrm, MMeter> mMeter = devPrm.join(MDevPrm_.MMeters);
        mMeter.on(cb.equal(devPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        // 建物、メーター関連テーブル(T_BUILD_DEV_METER_RELATION)
        Join<MMeter, TBuildDevMeterRelation> buildDevMeterRelation = mMeter.join(MMeter_.TBuildDevMeterRelations);

        Collection<Predicate> whereClct = new ArrayList<>();

        // 企業ID
        whereClct.add(
                cb.equal(buildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.corpId),
                        param.getCorpId()));
        // 建物ID
        whereClct.add(cb.equal(
                buildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId),
                param.getBuildingId()));

        criteria = criteria.select(devPrm).distinct(true)
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(cb.asc(devPrm.get(MDevPrm_.name)));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<MDevPrm> getResultList(List<MDevPrm> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm find(MDevPrm target, EntityManager em) {
        return em.find(MDevPrm.class, target.getDevId());
    }

    @Override
    public void persist(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm merge(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
