package jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection.MAutoInspServiceDaoImpl;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ登録API Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class InsertSmsAutoInspectionDao extends OsolApiDao<InsertSmsAutoInspectionParameter>  {

    private final MAutoInspServiceDaoImpl autoInspDaoImpl;

    public InsertSmsAutoInspectionDao() {
        autoInspDaoImpl = new MAutoInspServiceDaoImpl();
    }

	@Override
	public InsertSmsAutoInspectionResult query(InsertSmsAutoInspectionParameter parameter) throws Exception {

        String loginPersonId = parameter.getLoginPersonId();
        InsertSmsAutoInspectionRequest request = parameter.getRequest();
        executeUpdate(request.getInsertDataList(), loginPersonId,
                getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId(), getServerDateTime());

        return new InsertSmsAutoInspectionResult();
	}

	/**
	 *
	 * @param updateDataList
	 * @param devId
	 * @param loginPersonId
	 * @param userId
	 * @param currentDateTime
	 */
    private void executeUpdate(List<InsertSmsAutoInspectionRequestSet> insertDataList, String loginPersonId, Long userId, Timestamp currentDateTime) {

    	for(InsertSmsAutoInspectionRequestSet insertData : insertDataList) {

    		MAutoInspPK targetPk = new MAutoInspPK();
    		targetPk.setDevId(insertData.getDevId());
    		targetPk.setMeterType(insertData.getMeterType());

    		MAutoInsp target = new MAutoInsp();
    		target.setId(targetPk);
    		target.setRecMan(loginPersonId);
    		target.setRecDate(currentDateTime);
    		target.setMonth(insertData.getInspectionMonth());
    		target.setDay(insertData.getInspectionDay());
    		target.setHour(insertData.getInspectionHour());
    		target.setCreateUserId(userId);
    		target.setCreateDate(currentDateTime);
    		target.setUpdateUserId(userId);
    		target.setUpdateDate(currentDateTime);

    		MAutoInsp currentData = find(autoInspDaoImpl, target);

            if (currentData == null) {
                // 新規登録
                persist(autoInspDaoImpl, target);
            }
    	}
    }


}
