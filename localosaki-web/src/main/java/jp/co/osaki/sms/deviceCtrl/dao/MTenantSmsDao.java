package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MTenantSmsDaoImpl;

/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class MTenantSmsDao extends ConnectionSmsDao {

    MTenantSmsDaoImpl daoImpl;

    public MTenantSmsDao() {
        daoImpl = new MTenantSmsDaoImpl();
    }

    public MTenantSm find(MTenantSm entity) {
        return super.find(daoImpl, entity);
    }

    public void persist(MTenantSm entity){
        super.persist(daoImpl, entity);
        return;
    }

    public void remove(MTenantSm entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public void merge(MTenantSm entity) {
        super.merge(daoImpl, entity);
        return ;
    }

    public List<MTenantSm> getMMeterList(MTenantSm entity){
        return  super.getResultList(daoImpl, entity);
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MTenantSm entity) {
        List<MTenantSm> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }


}
