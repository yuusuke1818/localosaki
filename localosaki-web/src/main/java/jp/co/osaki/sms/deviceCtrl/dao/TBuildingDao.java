package jp.co.osaki.sms.deviceCtrl.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.TBuildingDaoImpl;

@Stateless
public class TBuildingDao extends ConnectionSmsDao {
    TBuildingDaoImpl daoImpl;

    public TBuildingDao() {
        daoImpl = new TBuildingDaoImpl();
    }

    public TBuilding find(TBuilding entity) {
        return super.find(daoImpl, entity);
    }


}
