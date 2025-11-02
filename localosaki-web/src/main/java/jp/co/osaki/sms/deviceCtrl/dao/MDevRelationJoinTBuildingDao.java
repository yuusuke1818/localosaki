package jp.co.osaki.sms.deviceCtrl.dao;

import javax.ejb.Stateless;

import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.resultset.MDevRelationJoinTBuildingResultSet;
import jp.co.osaki.sms.deviceCtrl.servicedao.MDevRelationJoinTBuildingDaoImpl;

/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class MDevRelationJoinTBuildingDao extends ConnectionSmsDao {
    MDevRelationJoinTBuildingDaoImpl daoImpl;

    public MDevRelationJoinTBuildingDao() {
        daoImpl = new MDevRelationJoinTBuildingDaoImpl();
    }

    public MDevRelationJoinTBuildingResultSet find(MDevRelationJoinTBuildingResultSet mMeterJoinTBuildingRelationResultSet) {
        return super.find(daoImpl, mMeterJoinTBuildingRelationResultSet);
    }

}
