package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.BuildingDevMeterResultSet;
import jp.co.osaki.sms.servicedao.BuildingDevMeterListServiceDaoImpl;

/**
 * 建物・装置・メーター検索Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class BuildingDevMeterListDao extends SmsDao {

    private final BuildingDevMeterListServiceDaoImpl daoImpl;

    public BuildingDevMeterListDao() {
        daoImpl = new BuildingDevMeterListServiceDaoImpl();
    }

    /**
     * 建物・装置・メーター検索
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param meterMngId メーター管理番号
     * @param tenantName テナント名
     * @return 建物・装置・メーターList
     */
    public List<BuildingDevMeterResultSet> getBuildingDevMeterList(String corpId, Long buildingId,
            boolean isTenant, String devId, String meterKind, Long meterMngId, String tenantName) {

        BuildingDevMeterResultSet dbParam = new BuildingDevMeterResultSet();
        dbParam.setCorpId(corpId);
        dbParam.setBuildingId(buildingId);
        dbParam.setTenant(isTenant);
        dbParam.setDevId(devId);
        dbParam.setMeterKind(meterKind);
        dbParam.setMeterMngId(meterMngId);
        dbParam.setTenantName(tenantName);

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(BuildingDevMeterListServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));

        return getResultList(daoImpl, parameterMap);
    }
}
