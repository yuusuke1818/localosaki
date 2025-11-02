package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MMeterTypeDaoImpl;

/**
*
* @author hayashi_tak
*
*/
@Stateless
public class MMeterTypeDao extends ConnectionSmsDao {
    MMeterTypeDaoImpl daoImpl;

    public MMeterTypeDao() {
        daoImpl = new MMeterTypeDaoImpl();
    }

    public MMeterType find(MMeterType entity) {
        return super.find(daoImpl, entity);
    }

    public List<MMeterType> getResultList(MMeterType entity){
        return super.getResultList(daoImpl, entity);
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MMeterType entity) {
        List<MMeterType> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
