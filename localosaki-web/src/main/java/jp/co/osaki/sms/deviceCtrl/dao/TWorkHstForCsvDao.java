package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TWorkHstForCsvDaoImpl;

@Stateless
public class TWorkHstForCsvDao extends ConnectionSmsDao  {
    TWorkHstForCsvDaoImpl daoImpl;

    public TWorkHstForCsvDao() {
        daoImpl = new TWorkHstForCsvDaoImpl();
    }
    public int updateTWorkHstList(Map<String, List<Object>> parameterMap) {
        return super.executeUpdate(daoImpl, parameterMap);
    }
    public List<TWorkHst> getTWorkHstList(TWorkHst entity) {
        return super.getResultList(daoImpl, entity);
    }
    public void persist(TWorkHst entity) {
        super.persist(daoImpl, entity);
        return ;
    }
    public TWorkHst merge(TWorkHst entity) {
        return super.merge(daoImpl, entity);
    }
}
