package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TDayLoadSurveyRevDaoImpl;

@Stateless
public class TDayLoadSurveyRevDao extends ConnectionSmsDao {

    private final TDayLoadSurveyRevDaoImpl daoImpl;

    public TDayLoadSurveyRevDao() {
        daoImpl = new TDayLoadSurveyRevDaoImpl();
    }


    public List<TDayLoadSurveyRev> getTDayLoadSurveyRevAllList(TDayLoadSurveyRev entity){
        return super.getResultList(daoImpl, entity);
    }

    public TDayLoadSurveyRev find(TDayLoadSurveyRev entity){
        return super.find(daoImpl, entity);
    }

    public void persist(TDayLoadSurveyRev entity){
        super.persist(daoImpl, entity);
        return ;
    }

    public void merge(TDayLoadSurveyRev entity){
        super.merge(daoImpl, entity);
        return ;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(TDayLoadSurveyRev entity) {
        List<TDayLoadSurveyRev> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
