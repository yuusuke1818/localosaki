package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TDayLoadSurveyDaoImpl;
/**
*
* @author hayashi_tak
*/
@Stateless
public class TDayLoadSurveyDao extends ConnectionSmsDao {

    private final TDayLoadSurveyDaoImpl daoImpl;

    public TDayLoadSurveyDao() {
        daoImpl = new TDayLoadSurveyDaoImpl();
    }


    public List<TDayLoadSurvey> getTDayLoadSurveyAllList(TDayLoadSurvey entity){
        return super.getResultList(daoImpl, entity);
    }

    public TDayLoadSurvey find(TDayLoadSurvey entity){
        return super.find(daoImpl, entity);
    }

    public void persist(TDayLoadSurvey entity){
        super.persist(daoImpl, entity);
        return ;
    }

    public void merge(TDayLoadSurvey entity){
        super.merge(daoImpl, entity);
        return ;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(TDayLoadSurvey entity) {
        List<TDayLoadSurvey> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }
}
