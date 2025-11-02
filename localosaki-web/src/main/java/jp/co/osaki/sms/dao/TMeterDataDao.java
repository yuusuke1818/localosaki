package jp.co.osaki.sms.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.servicedao.TMeterDataDaoImpl;

/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class TMeterDataDao extends ConnectionSmsDao {
    private final TMeterDataDaoImpl daoImpl;

    public TMeterDataDao() {
        daoImpl = new TMeterDataDaoImpl();
    }

    public TMeterData findTMeterData(TMeterData entity) {
        return super.find(daoImpl, entity);
    }


}
