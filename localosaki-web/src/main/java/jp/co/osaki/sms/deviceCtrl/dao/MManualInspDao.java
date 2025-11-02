package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MManualInsp;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MManualInspDaoImpl;

@Stateless
public class MManualInspDao extends ConnectionSmsDao {
    MManualInspDaoImpl daoImpl;

    public MManualInspDao() {
        daoImpl = new MManualInspDaoImpl();
    }

    public void persist(MManualInsp entity){
        super.persist(daoImpl, entity);
        return;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MManualInsp entity) {
        List<MManualInsp> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }
        return false;
    }

    public MManualInsp find(MManualInsp entity) {
        return super.find(daoImpl, entity);
    }

    public List<MManualInsp> getManualInspResultList(MManualInsp entity) {
        return super.getResultList(daoImpl, entity);
    }

    public void merge(MManualInsp entity) {
        super.merge(daoImpl, entity);
        return;
    }

}
