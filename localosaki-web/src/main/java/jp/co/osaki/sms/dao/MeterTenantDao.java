package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MeterTenantSmsServiceDaoImpl;

/**
 * メーターテナント関連
 * @author nishida.t
 *
 */
@Stateless
public class MeterTenantDao extends SmsDao {

    private final MeterTenantSmsServiceDaoImpl meterTenantSmsServiceDaoImpl;

    public MeterTenantDao() {
        meterTenantSmsServiceDaoImpl = new MeterTenantSmsServiceDaoImpl();
    }

    /**
     * 企業内の建物番号重複チェック
     * @param corpId
     * @param buildingId
     * @param buildingNo
     * @return
     */
    public int getMeterTenatNoCount (String corpId, String buildingNo) {
        TBuildingPK tBuildingPK = new TBuildingPK();
        TBuilding tBuilding = new TBuilding();
        tBuildingPK.setCorpId(corpId);
        tBuilding.setId(tBuildingPK);
        tBuilding.setBuildingNo(buildingNo);
        List<TBuilding> resultList = getResultList(meterTenantSmsServiceDaoImpl, tBuilding);

        if (resultList == null || resultList.isEmpty()) {
            return 0;
        } else {
            return resultList.size();
        }
    }

    /**
     * SMSテナントの中でユーザーコードが重複している件数
     * @param corpId
     * @param buildingId
     * @param buildingNo
     * @return
     */
    public int getMeterTenatIdCount (String corpId, Long buildingId, Long tenantId) {
        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(OsolConstants.SEARCH_CONDITION_CORP_ID, new ArrayList<Object>(Arrays.asList(corpId)));
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_ID, new ArrayList<Object>(Arrays.asList(buildingId)));
        parameterMap.put(SmsConstants.SEARCH_CONDITION_TENANT_ID, new ArrayList<Object>(Arrays.asList(tenantId)));
        List<TBuilding> resultList = getResultList(meterTenantSmsServiceDaoImpl, parameterMap);

        if (resultList == null || resultList.isEmpty()) {
            return 0;
        } else {
            return resultList.size();
        }
    }

    /**
     * 所属建物情報を取得
     * @param corpId
     * @param buildingId
     * @return
     */
    public TBuilding getBuilding(String corpId, Long buildingId) {
        TBuildingPK tBuildingPK = new TBuildingPK();
        TBuilding tBuilding = new TBuilding();
        tBuildingPK.setCorpId(corpId);
        tBuildingPK.setBuildingId(buildingId);
        tBuilding.setId(tBuildingPK);

        // 所属建物情報は取れるはず
        TBuilding result = find(meterTenantSmsServiceDaoImpl, tBuilding);
        return result;
    }
}
