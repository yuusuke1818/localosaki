package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MAutoInspDaoImpl;

@Stateless
public class MAutoInspDao extends ConnectionSmsDao {

    private final MAutoInspDaoImpl daoImpl;

    public MAutoInspDao() {
        daoImpl = new MAutoInspDaoImpl();
    }


    public List<MAutoInsp> getAutoInspList(MAutoInsp entity){
        List<MAutoInsp> mAutoInspList = super.getResultList(daoImpl, entity);
        return mAutoInspList;
    }

    public void persist(MAutoInsp entity){
        super.persist(daoImpl, entity);
        return;
    }
    public void remove(MAutoInsp entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public MAutoInsp find(MAutoInsp entity) {
        return super.find(daoImpl, entity);
    }

    public void merge(MAutoInsp entity){
        super.merge(daoImpl, entity);
        return;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MAutoInsp entity) {
        List<MAutoInsp> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }
}
