package jp.co.osaki.sms.servicedao;

import java.math.BigDecimal;
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

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TDayLoadSurveyMaxDemandServiceDaoImpl implements BaseServiceDao<TDayLoadSurvey> {

	protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

	@Override
	public List<TDayLoadSurvey> getResultList(Map<String, List<Object>> parameterMap,
			EntityManager em) {

		TDayLoadSurvey tDayLoadSurvey = getMaxDemand(parameterMap, em);

		if (tDayLoadSurvey != null) {
			return Arrays.asList(tDayLoadSurvey);
		}

		return null;
	}

	private TDayLoadSurvey getMaxDemand(Map<String, List<Object>> parameterMap, EntityManager em) {

		String devId = String.valueOf(parameterMap.get("devId").get(0));
		Long meterMngId = Long.parseLong(String.valueOf(parameterMap.get("meterMngId").get(0)));
		String fromGetDate = String.valueOf(parameterMap.get("fromGetDate").get(0));
		String toGetDate = String.valueOf(parameterMap.get("toGetDate").get(0));

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), fromGetDate)); // from
        whereList.add(builder.lessThanOrEqualTo(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), toGetDate)); // to

        query = query //
        		.select( //
        			builder.max(root.get(TDayLoadSurvey_.kwh30))) //
        		.where(builder.and(whereList.toArray(new Predicate[] {})));

        BigDecimal maxKWh30 = em.createQuery(query).getSingleResult();

        if (maxKWh30 != null) {

            TDayLoadSurveyPK id = new TDayLoadSurveyPK();
            id.setDevId(devId);
            id.setMeterMngId(meterMngId);

            TDayLoadSurvey tDayLoadSurvey = new TDayLoadSurvey();
            tDayLoadSurvey.setId(id);
            tDayLoadSurvey.setKwh30(maxKWh30);

            return tDayLoadSurvey;
        }

        return null;
	}

	@Override
	public TDayLoadSurvey find(TDayLoadSurvey target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TDayLoadSurvey> getResultList(TDayLoadSurvey target,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TDayLoadSurvey> getResultList(List<TDayLoadSurvey> entityList,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TDayLoadSurvey> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(TDayLoadSurvey target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TDayLoadSurvey merge(TDayLoadSurvey target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(TDayLoadSurvey target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
