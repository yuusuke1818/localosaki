package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection;

import java.util.ArrayList;
import java.util.Collection;
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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionDevListResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ更新API 装置List取得ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class ListSmsAutoInspectionDevListServiceDaoImpl
        implements BaseServiceDao<ListSmsAutoInspectionDevListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionDevListResultData> getResultList(ListSmsAutoInspectionDevListResultData target,
            EntityManager em) {

        List<ListSmsAutoInspectionDevListResultData> devPrmList = null;
        if (target.isTenant()) {
            // テナントの場合
            devPrmList = createTenantDevPrmList(target, em);
        } else {
            devPrmList = createBuildingDevPrmList(target, em);
        }

        return devPrmList;
    }

    @Override
    public List<ListSmsAutoInspectionDevListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionDevListResultData> getResultList(
            List<ListSmsAutoInspectionDevListResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsAutoInspectionDevListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsAutoInspectionDevListResultData find(ListSmsAutoInspectionDevListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 建物に紐付く装置情報Listを生成.
     *
     * @param param 検索条件
     * @param em エンティティマネージャ
     * @return 建物に紐付く装置情報List
     */
    private List<ListSmsAutoInspectionDevListResultData> createBuildingDevPrmList(
            ListSmsAutoInspectionDevListResultData param, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsAutoInspectionDevListResultData> criteria = builder
                .createQuery(ListSmsAutoInspectionDevListResultData.class);

        // 装置情報(M_DEV_PRM)
        Root<MDevPrm> devPrm = criteria.from(MDevPrm.class);

        // 建物、装置関連テーブル(M_DEV_RELATION)
        Join<MDevPrm, MDevRelation> devRelation = devPrm.join(MDevPrm_.MDevRelations);
        devRelation.on(builder.equal(devPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        Path<String> devIdPath = devPrm.get(MDevPrm_.devId);
        Path<String> devNamePath = devPrm.get(MDevPrm_.name);
        Path<String> revFlgPath = devPrm.get(MDevPrm_.revFlg);

        Path<MDevRelationPK> devRelationPkPath = devRelation.get(MDevRelation_.id);

        Expression<?>[] targetColumns = new Expression[] { devIdPath, devNamePath, revFlgPath };
        Collection<Predicate> whereClct = new ArrayList<>();

        // 企業ID
        whereClct.add(builder.equal(devRelationPkPath.get(MDevRelationPK_.corpId), param.getCorpId()));
        // 建物ID
        whereClct.add(builder.equal(devRelationPkPath.get(MDevRelationPK_.buildingId), param.getBuildingId()));

        criteria = criteria.select(builder.construct(ListSmsAutoInspectionDevListResultData.class, targetColumns))
                .where(whereClct.toArray(new Predicate[0])).orderBy(builder.asc(devNamePath));

        return em.createQuery(criteria).getResultList();
    }

    /**
     * メーターを介してテナントに紐付く装置情報Listを生成.
     *
     * @param param 検索条件
     * @param em エンティティマネージャ
     * @return メーターを介してテナントに紐付く装置情報List
     */
    private List<ListSmsAutoInspectionDevListResultData> createTenantDevPrmList(
            ListSmsAutoInspectionDevListResultData param, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsAutoInspectionDevListResultData> criteria = builder
                .createQuery(ListSmsAutoInspectionDevListResultData.class);

        // 装置情報(M_DEV_PRM)
        Root<MDevPrm> devPrm = criteria.from(MDevPrm.class);

        // メータ登録用(M_METER)
        Join<MDevPrm, MMeter> mMeter = devPrm.join(MDevPrm_.MMeters);
        mMeter.on(builder.equal(devPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        // 建物、メーター関連テーブル(T_BUILD_DEV_METER_RELATION)
        Join<MMeter, TBuildDevMeterRelation> buildDevMeterRelation = mMeter.join(MMeter_.TBuildDevMeterRelations);

        Path<String> devIdPath = devPrm.get(MDevPrm_.devId);
        Path<String> devNamePath = devPrm.get(MDevPrm_.name);
        Path<String> revFlgPath = devPrm.get(MDevPrm_.revFlg);

        Path<TBuildDevMeterRelationPK> buildDevMeterRelationPkPath = buildDevMeterRelation
                .get(TBuildDevMeterRelation_.id);

        Expression<?>[] targetColumns = new Expression[] { devIdPath, devNamePath, revFlgPath };
        Collection<Predicate> whereClct = new ArrayList<>();

        // 企業ID
        whereClct.add(
                builder.equal(buildDevMeterRelationPkPath.get(TBuildDevMeterRelationPK_.corpId),
                        param.getCorpId()));
        // 建物ID
        whereClct.add(builder.equal(
                buildDevMeterRelationPkPath.get(TBuildDevMeterRelationPK_.buildingId),
                param.getBuildingId()));

        criteria = criteria.select(builder.construct(ListSmsAutoInspectionDevListResultData.class, targetColumns))
                .distinct(true).where(whereClct.toArray(new Predicate[0])).orderBy(builder.asc(devNamePath));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    public void persist(ListSmsAutoInspectionDevListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsAutoInspectionDevListResultData merge(ListSmsAutoInspectionDevListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsAutoInspectionDevListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
