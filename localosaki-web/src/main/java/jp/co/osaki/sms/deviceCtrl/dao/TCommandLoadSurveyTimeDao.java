package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TCommandLoadSurveyTime;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TCommandLoadSurveyTimeDaoImpl;
/**
*
* @author hayashi_tak
*
*/
@Stateless
public class TCommandLoadSurveyTimeDao extends ConnectionSmsDao {

    TCommandLoadSurveyTimeDaoImpl daoImpl;

    public TCommandLoadSurveyTimeDao() {
        daoImpl = new TCommandLoadSurveyTimeDaoImpl();
    }

    public List<TCommandLoadSurveyTime> getResultList(TCommandLoadSurveyTime entity){
        return super.getResultList(daoImpl, entity);
    }

    public void remove(TCommandLoadSurveyTime entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public TCommandLoadSurveyTime merge(TCommandLoadSurveyTime entity) {
        return super.merge(daoImpl, entity);
    }

    public TCommandLoadSurveyTime find(TCommandLoadSurveyTime entity) {
        return super.find(daoImpl, entity);
    }


    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(TCommandLoadSurveyTime entity) {
        List<TCommandLoadSurveyTime> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
