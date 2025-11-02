package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TCommandLoadSurveyMeterDaoImpl;
/**
*
* @author hayashi_tak
*
*/
@Stateless
public class TCommandLoadSurveyMeterDao extends ConnectionSmsDao {

    TCommandLoadSurveyMeterDaoImpl daoImpl;

    public TCommandLoadSurveyMeterDao() {
        daoImpl = new TCommandLoadSurveyMeterDaoImpl();
    }

    public List<TCommandLoadSurveyMeter> getResultList(TCommandLoadSurveyMeter entity){
        return super.getResultList(daoImpl, entity);
    }

    public void remove(TCommandLoadSurveyMeter entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public TCommandLoadSurveyMeter merge(TCommandLoadSurveyMeter entity) {
        return super.merge(daoImpl, entity);

    }

    public TCommandLoadSurveyMeter find(TCommandLoadSurveyMeter entity) {
        return super.find(daoImpl, entity);

    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(TCommandLoadSurveyMeter entity) {
        List<TCommandLoadSurveyMeter> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
