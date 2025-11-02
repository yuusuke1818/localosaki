package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.deviceCtrl.servicedao.MAlertMailDaoImpl;

@Stateless
public class MAlertMailDao extends ConnectionSmsDao {
    MAlertMailDaoImpl daoImpl;

    public MAlertMailDao() {
        daoImpl = new MAlertMailDaoImpl();
    }

    public List<MAlertMail> getMAlertMailList(MAlertMail entity){
        return  super.getResultList(daoImpl, entity);
      }

    /**
     * 存在しなければtrueを返す
     * @param entity
     * @return
     */
    public boolean isNull(MAlertMail entity) {
        List<MAlertMail> retList = new ArrayList<>();
        retList.addAll(getMAlertMailList(entity));
        if(retList.size() == DeviceCtrlConstants.zero) {
            return true;
        }
        return false;
    }


}
