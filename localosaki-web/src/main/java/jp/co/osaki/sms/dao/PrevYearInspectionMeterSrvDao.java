package jp.co.osaki.sms.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.PrevYearInspectionMeterSrvDaoServiceImpl;

@Stateless
public class PrevYearInspectionMeterSrvDao extends SmsDao {

    private final PrevYearInspectionMeterSrvDaoServiceImpl daoImpl;

    public PrevYearInspectionMeterSrvDao() {
        daoImpl = new PrevYearInspectionMeterSrvDaoServiceImpl();
    }

    public TInspectionMeterSvr getPrevYearRecords(Map<String, List<Object>> parameterMap) {

    	List<TInspectionMeterSvr> tInspectionMeterSvrs = getResultList(daoImpl, parameterMap);

    	if (tInspectionMeterSvrs != null && !tInspectionMeterSvrs.isEmpty()) {

    		if (tInspectionMeterSvrs.size() == 1) {
    			return tInspectionMeterSvrs.get(0);
    		}
    	}

        return null;
    }
}
