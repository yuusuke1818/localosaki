package jp.co.osaki.sms.deviceCtrl.dao;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevFwVersion;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MDevFwVersionDaoImpl;
/**
*
* @author kansaku
*
*/
@Stateless
public class MDevFwVersionDao extends ConnectionSmsDao {
	MDevFwVersionDaoImpl daoImpl;

    public MDevFwVersionDao() {
        daoImpl = new MDevFwVersionDaoImpl();
    }

    public MDevFwVersion getRecord(MDevFwVersion entity) {
    	return super.find(daoImpl, entity);
    }

    public MDevFwVersion updateRecord(MDevFwVersion entity) {
    	Timestamp insertDate = getSvDate();
    	entity.setUpdateUserId(0L);
    	entity.setUpdateDate(insertDate);
        return super.merge(daoImpl, entity);
    }

    public void insertRecord(MDevFwVersion entity) {
    	Timestamp insertDate = getSvDate();
    	entity.setVersion(0);
    	entity.setCreateUserId(0L);
    	entity.setCreateDate(insertDate);
    	entity.setUpdateUserId(0L);
    	entity.setUpdateDate(insertDate);
    	super.persist(daoImpl, entity);
    }


}
