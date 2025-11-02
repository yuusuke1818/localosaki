package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MConcentratorDaoImpl;

/**
 *
 * @author hayashi_tak
 */
@Stateless
public class MConcentratorDao extends ConnectionSmsDao {

    private final MConcentratorDaoImpl daoImpl;

    public MConcentratorDao() {
        daoImpl = new MConcentratorDaoImpl();
    }


    public List<MConcentrator> getMConcentrotorList(MConcentrator entity){
        List<MConcentrator> mConcentratorList = super.getResultList(daoImpl, entity);
        return mConcentratorList;
    }

    public void persist(MConcentrator entity){
        super.persist(daoImpl, entity);
        return;
    }
    public void remove(MConcentrator entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public MConcentrator find(MConcentrator entity) {
        return super.find(daoImpl, entity);
    }

    public void merge(MConcentrator entity){
        super.merge(daoImpl, entity);
        return;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MConcentrator entity) {
        List<MConcentrator> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
