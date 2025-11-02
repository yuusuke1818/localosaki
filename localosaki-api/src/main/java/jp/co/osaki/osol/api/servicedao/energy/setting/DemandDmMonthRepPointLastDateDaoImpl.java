package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.setting.DemandDmMonthRepPointLastDatelResultData;
import jp.co.osaki.osol.entity.TDmMonthRepPoint;
import jp.co.osaki.osol.entity.TDmMonthRepPointPK_;
import jp.co.osaki.osol.entity.TDmMonthRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド製品仕様情報取得 ServiceDaoクラス
 * @author akr_iwamoto
 *
 */
public class DemandDmMonthRepPointLastDateDaoImpl implements BaseServiceDao<DemandDmMonthRepPointLastDatelResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandDmMonthRepPointLastDatelResultData> getResultList(DemandDmMonthRepPointLastDatelResultData target,
            EntityManager em) {
    	if (target == null) {
    		return null;
    	}

    	String corpId = target.getCorpId();
    	Long  buildingId = target.getBuildingId();
    	Long smId = target.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandDmMonthRepPointLastDatelResultData> query = builder
                .createQuery(DemandDmMonthRepPointLastDatelResultData.class);

        Root<TDmMonthRepPoint> root = query.from(TDmMonthRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();


        // 企業ID
        if (!CheckUtility.isNullOrEmpty(corpId)) {
            whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.corpId), corpId));
        }
        // 建物ID
        whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.buildingId), buildingId));

        // 機器ID
        whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.smId), smId));

        query = query.select(builder.construct(DemandDmMonthRepPointLastDatelResultData.class,
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.corpId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.buildingId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.smId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.measurementDate)
        		)).where(builder.and(whereList.toArray(new Predicate[] {})))
        		.orderBy(builder.desc(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.measurementDate)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DemandDmMonthRepPointLastDatelResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandDmMonthRepPointLastDatelResultData> getResultList(List<DemandDmMonthRepPointLastDatelResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandDmMonthRepPointLastDatelResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandDmMonthRepPointLastDatelResultData find(DemandDmMonthRepPointLastDatelResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandDmMonthRepPointLastDatelResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandDmMonthRepPointLastDatelResultData merge(DemandDmMonthRepPointLastDatelResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandDmMonthRepPointLastDatelResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
