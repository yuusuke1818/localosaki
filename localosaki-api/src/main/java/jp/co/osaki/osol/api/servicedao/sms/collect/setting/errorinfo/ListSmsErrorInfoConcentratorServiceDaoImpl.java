package jp.co.osaki.osol.api.servicedao.sms.collect.setting.errorinfo;

import java.math.BigDecimal;
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
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.errorinfo.ListSmsErrorInfoResultData;
import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK_;
import jp.co.osaki.osol.entity.MConcentrator_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * データ収集装置 機器管理 異常情報画面 データ取得API コンセントレータ情報取得ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class ListSmsErrorInfoConcentratorServiceDaoImpl implements BaseServiceDao<ListSmsErrorInfoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsErrorInfoResultData> getResultList(ListSmsErrorInfoResultData target, EntityManager em) {
        return createBuildingDevConcentratorList(target, em);
    }

    /**
     * 建物に紐付く装置のコンセントレータ情報Listを生成.
     *
     * @param param 検索条件
     * @param em エンティティマネージャ
     * @return 建物に紐付く装置のコンセントレータ情報List
     */
    private List<ListSmsErrorInfoResultData> createBuildingDevConcentratorList(ListSmsErrorInfoResultData param,
            EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsErrorInfoResultData> criteria = builder.createQuery(ListSmsErrorInfoResultData.class);

        // コンセントレータ処理用(M_CONCENTRATOR)
        Root<MConcentrator> concentrator = criteria.from(MConcentrator.class);

        // 装置情報(M_DEV_PRM)
        Join<MConcentrator, MDevPrm> devPrm = concentrator.join(MConcentrator_.MDevPrm);

        // 建物、装置関連テーブル(M_DEV_RELATION)
        Join<MDevPrm, MDevRelation> devRelation = devPrm.join(MDevPrm_.MDevRelations);
        devRelation.on(builder.equal(devPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        Path<String> devIdPath = devPrm.get(MDevPrm_.devId);
        Path<String> devNamePath = devPrm.get(MDevPrm_.name);
        Path<Long> concentIdPath = concentrator.get(MConcentrator_.id).get(MConcentratorPK_.concentId);
        Path<BigDecimal> concentStaPath = concentrator.get(MConcentrator_.concentSta);

        Expression<?>[] targetColumns = new Expression[] { devIdPath, devNamePath, concentIdPath, concentStaPath };

        Collection<Predicate> whereClct = new ArrayList<>();

        // 企業ID
        whereClct.add(builder.equal(devRelation.get(MDevRelation_.id).get(MDevRelationPK_.corpId), param.getCorpId()));
        // 建物ID
        whereClct.add(
                builder.equal(devRelation.get(MDevRelation_.id).get(MDevRelationPK_.buildingId),
                        param.getBuildingId()));

        criteria = criteria.select(builder.construct(ListSmsErrorInfoResultData.class, targetColumns))
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(builder.asc(devNamePath), builder.asc(concentIdPath));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<ListSmsErrorInfoResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsErrorInfoResultData> getResultList(List<ListSmsErrorInfoResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsErrorInfoResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsErrorInfoResultData find(ListSmsErrorInfoResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsErrorInfoResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsErrorInfoResultData merge(ListSmsErrorInfoResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsErrorInfoResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
