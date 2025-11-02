package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MTenantSmsServiceDaoImpl;
import jp.co.osaki.sms.servicedao.MeterTenantSmsInfoListDaoImpl;

/**
 * SMSテナント検索
 */
@Stateless
public class TBuildingJoinBuildingInfoListDao extends SmsDao {

    // メーターテナント検索
    private final MeterTenantSmsInfoListDaoImpl meterTenantSmsInfoListDaoImpl;

    // SMSテナント情報
    private final MTenantSmsServiceDaoImpl mTenantSmsServiceDaoImpl;

    public TBuildingJoinBuildingInfoListDao() {
        meterTenantSmsInfoListDaoImpl = new MeterTenantSmsInfoListDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
    }


    public List<TBuilding> getSearchSmsList(
            List<Object> targetPrefectureList,
            List<Object> targetBuildingNoOrNameList,
            List<Object> targetBuildingStateList,
            List<Object> targetBuildingTenantList,
            List<Object> targetBorrowCorpList,
            List<Object> targetBorrowBuildingList,
            List<Object> targetTenatIdList,
            Date targetDate
            ) {
        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, targetPrefectureList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME, targetBuildingNoOrNameList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_STATE, targetBuildingStateList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_TENANT, targetBuildingTenantList);
        parameterMap.put("divisionCorpId", targetBorrowCorpList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_TENANT_BELONG_TO_BUILDING, targetBorrowBuildingList);
        parameterMap.put(SmsConstants.SEARCH_CONDITION_TENANT_ID, targetTenatIdList);

        List<Object> targetDateList = new ArrayList<>();
        targetDateList.add(targetDate);
        parameterMap.put("targetDate", targetDateList);

        return getResultList(meterTenantSmsInfoListDaoImpl, parameterMap);
    }

    /**
     * テナント情報 検索
     *
     * @param divistionCorpId
     * @param divistionBuildingId
     * @return テナント情報
     */
    public MTenantSm getSearchSmsTenant(String corpId, Long buildingId) {

        MTenantSm mTenantSm = new MTenantSm();
        MTenantSmPK mTenantSmPK = new MTenantSmPK();
        mTenantSmPK.setCorpId(corpId);
        mTenantSmPK.setBuildingId(buildingId);
        mTenantSm.setId(mTenantSmPK);
        MTenantSm entity = find(mTenantSmsServiceDaoImpl, mTenantSm);

        return entity;
    }
}
