package jp.co.osaki.sms.deviceCtrl.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MBuildingSmsDaoImpl;

@Stateless
public class MBuildingSmsDao extends ConnectionSmsDao {
    MBuildingSmsDaoImpl daoImpl;

    public MBuildingSmsDao() {
        daoImpl = new MBuildingSmsDaoImpl();
    }

    public MBuildingSms find(MBuildingSms entity) {
        return super.find(daoImpl, entity);
    }


}
