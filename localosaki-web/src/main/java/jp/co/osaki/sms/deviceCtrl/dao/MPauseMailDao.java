package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MPauseMailDaoImpl;

@Stateless
public class MPauseMailDao extends ConnectionSmsDao {
    MPauseMailDaoImpl daoImpl;

    public MPauseMailDao() {
        daoImpl = new MPauseMailDaoImpl();
    }

    public MPauseMail find(MPauseMail entity) {
        return super.find(daoImpl, entity);
    }

    /**
     * 存在しなければtrueを返す
     * @param entity
     * @return
     */
    public boolean isNull(MPauseMail entity) {
        List<MPauseMail> retList = super.getResultList(daoImpl, entity);
        if(retList == null || retList.size() == 0) {
            return true;
        }
        return false;
    }

    public List<MPauseMail> getMPauseMailList(MPauseMail entity){
        return  super.getResultList(daoImpl, entity);
      }

}
