package jp.co.osaki.osol.api.servicedao.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsSmListResultData;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 *集計・分析 EMS実績 対象機器取得  ServiceDaoクラス
 *
 * @author y-maruta
 *
 */

public class AnalysisEmsSmListServiceDaoImpl implements BaseServiceDao<AnalysisEmsSmListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsSmListResultData> getResultList(AnalysisEmsSmListResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsSmListResultData> query = builder.createQuery(AnalysisEmsSmListResultData.class);

        Root<MBuildingSm> root = query.from(MBuildingSm.class);
        Join<MBuildingSm, MSmPrm> joinSm = root.join(MBuildingSm_.MSmPrm);
        Join<MSmPrm, MProductSpec> joinProductSpec = joinSm.join(MSmPrm_.MProductSpec);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), corpId));

        //建物ID
        whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId), buildingId));

        //建物削除フラグ
        whereList.add(builder.equal(joinSm.get(MSmPrm_.delFlg), OsolConstants.FLG_OFF));

        //建物削除フラグ
        whereList.add(builder.equal(joinProductSpec.get(MProductSpec_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AnalysisEmsSmListResultData.class,
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId),
                joinProductSpec.get(MProductSpec_.productCd),
                joinSm.get(MSmPrm_.smAddress),
                joinSm.get(MSmPrm_.ipAddress),
                joinSm.get(MSmPrm_.startDate),
                joinSm.get(MSmPrm_.endDate),
                joinSm.get(MSmPrm_.plotAnalogPointNo1),
                joinSm.get(MSmPrm_.plotAnalogPointNo2),
                joinSm.get(MSmPrm_.aielMasterConnectFlg),
                joinSm.get(MSmPrm_.note),
                joinProductSpec.get(MProductSpec_.productType),
                joinProductSpec.get(MProductSpec_.productName),
                joinProductSpec.get(MProductSpec_.measurementPoint),
                joinProductSpec.get(MProductSpec_.loadControlOutput)
                )).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsSmListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsSmListResultData> getResultList(List<AnalysisEmsSmListResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsSmListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsSmListResultData find(AnalysisEmsSmListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisEmsSmListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public AnalysisEmsSmListResultData merge(AnalysisEmsSmListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisEmsSmListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }



}
