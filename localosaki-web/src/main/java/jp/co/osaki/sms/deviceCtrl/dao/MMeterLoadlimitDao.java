package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MMeterLoadlimitDaoImpl;

@Stateless
public class MMeterLoadlimitDao extends ConnectionSmsDao {
    private final MMeterLoadlimitDaoImpl daoImpl;

    public MMeterLoadlimitDao() {
        daoImpl = new MMeterLoadlimitDaoImpl();
    }

    public MMeterLoadlimit find(MMeterLoadlimit entity) {
        return super.find(daoImpl, entity);
    }

    public void merge(MMeterLoadlimit entity) {
        super.merge(daoImpl, entity);
        return ;
    }

    public void persist(MMeterLoadlimit entity) {
        super.persist(daoImpl, entity);
        return ;
    }

    public List<MMeterLoadlimit> getMMeterLoadlimitList(MMeterLoadlimit entity){
        return  super.getResultList(daoImpl, entity);
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MMeterLoadlimit entity) {
        List<MMeterLoadlimit> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }


}
