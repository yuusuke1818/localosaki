package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MMeterDaoImpl;
/**
 * @author hayashi_tak
 *
 */
@Stateless
public class MMeterDao extends ConnectionSmsDao {
    MMeterDaoImpl daoImpl;

    public MMeterDao() {
        daoImpl = new MMeterDaoImpl();
    }

    public void persist(MMeter entity){
        super.persist(daoImpl, entity);
        return;
    }

    public void remove(MMeter entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public MMeter find(MMeter entity) {
        return super.find(daoImpl, entity);
    }

    public void merge(MMeter entity) {
        super.merge(daoImpl, entity);
        return ;
    }

    public List<MMeter> getMMeterList(MMeter entity){
        return  super.getResultList(daoImpl, entity);
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MMeter entity) {
        List<MMeter> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
