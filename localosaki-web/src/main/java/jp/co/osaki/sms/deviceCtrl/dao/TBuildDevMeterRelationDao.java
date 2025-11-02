package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TBuildDevMeterRelationDaoImpl;
/**
 * @author hayashi_tak
 *
 */
@Stateless
public class TBuildDevMeterRelationDao extends ConnectionSmsDao  {
    TBuildDevMeterRelationDaoImpl daoImpl;

    public TBuildDevMeterRelationDao() {
        daoImpl = new TBuildDevMeterRelationDaoImpl();
    }

    public TBuildDevMeterRelation find(TBuildDevMeterRelation entity) {
        return super.find(daoImpl, entity);
    }

    public void persist(TBuildDevMeterRelation entity) {
        super.persist(daoImpl, entity);
        return;
    }

    public List<TBuildDevMeterRelation> getResultList(TBuildDevMeterRelation entity) {
        return super.getResultList(daoImpl, entity);
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(TBuildDevMeterRelation entity) {
        List<TBuildDevMeterRelation> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
