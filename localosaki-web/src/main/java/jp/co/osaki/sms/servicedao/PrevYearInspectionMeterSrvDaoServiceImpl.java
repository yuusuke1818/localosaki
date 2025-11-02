package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class PrevYearInspectionMeterSrvDaoServiceImpl implements BaseServiceDao<TInspectionMeterSvr> {

	protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

	@Override
	public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> parameterMap,
			EntityManager em) {
		return getPrevYearRecords(parameterMap, em);
	}

	private List<TInspectionMeterSvr> getPrevYearRecords(Map<String, List<Object>> parameterMap, EntityManager em) {

		String devId = String.valueOf(parameterMap.get("devId").get(0));
		Long meterMngId = Long.parseLong(String.valueOf(parameterMap.get("meterMngId").get(0)));
		String inspYear = String.valueOf(parameterMap.get("inspYear").get(0));
		String inspMonth = String.valueOf(parameterMap.get("inspMonth").get(0));
		String inspType = String.valueOf(parameterMap.get("inspType").get(0));

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), inspType));
        whereList.add(root.get(TInspectionMeterSvr_.inspType).in(Arrays.asList("a", "r")));

        query = query //
        		.select(root) //
        		.where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
	}

	@Override
	public TInspectionMeterSvr find(TInspectionMeterSvr target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TInspectionMeterSvr> getResultList(List<TInspectionMeterSvr> entityList,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TInspectionMeterSvr> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(TInspectionMeterSvr target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TInspectionMeterSvr merge(TInspectionMeterSvr target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(TInspectionMeterSvr target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
