package jp.co.osaki.sms.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.InspectionMeterSrvBulkResultSet;
import jp.co.osaki.sms.servicedao.InspectionMeterSrvBulkDaoServiceImpl;

@Stateless
public class InspectionMeterSrvBulkDao extends SmsDao {

    private final InspectionMeterSrvBulkDaoServiceImpl daoImpl;

    public InspectionMeterSrvBulkDao() {
        daoImpl = new InspectionMeterSrvBulkDaoServiceImpl();
    }

    public List<InspectionMeterSrvBulkResultSet> getInspectionMeterSrvs(Map<String, List<Object>> parameterMap) {
        return getResultList(daoImpl, parameterMap);
    }
}
