package jp.co.osaki.sms.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.TDayLoadSurveyMaxDemandServiceDaoImpl;

@Stateless
public class TDayLoadSurveyMaxDemandDao extends SmsDao {

    private final TDayLoadSurveyMaxDemandServiceDaoImpl daoImpl;

    public TDayLoadSurveyMaxDemandDao() {
        daoImpl = new TDayLoadSurveyMaxDemandServiceDaoImpl();
    }

    public TDayLoadSurvey getMaxDemand(Map<String, List<Object>> parameterMap) {

    	List<TDayLoadSurvey> entities = getResultList(daoImpl, parameterMap);

    	if (entities != null && !entities.isEmpty()) {
    		return entities.get(0);
    	}

        return null;
    }
}
