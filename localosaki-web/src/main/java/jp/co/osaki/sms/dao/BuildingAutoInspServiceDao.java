package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.parameter.BuildingAutoInspSearchCondition;
import jp.co.osaki.sms.resultset.BuildingAutoInspResultSet;
import jp.co.osaki.sms.servicedao.BuildingMeterTypeAutoInspServiceDaoImpl;

/**
 * 建物に紐付く自動検針データ取得Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class BuildingAutoInspServiceDao extends SmsDao {

    /** 建物に紐付くメーター種別の自動検針データ取得Dao. */
    private final BuildingMeterTypeAutoInspServiceDaoImpl meterTypeAutoInspServicedaoImpl;

    public BuildingAutoInspServiceDao() {
        meterTypeAutoInspServicedaoImpl = new BuildingMeterTypeAutoInspServiceDaoImpl();
    }

    /**
     * 建物に紐付くメーター種別の自動検針データListのMapを取得.
     *
     * @param buildingList 建物List
     * @param autoInspDateList 自動検針日List
     * @return 建物に紐付くメーター種別の自動検針データListのMap
     */
    public Map<String, Map<Long, List<BuildingAutoInspResultSet>>> getMeterTypeAutoInspMap(
            List<TBuilding> buildingList, List<BuildingAutoInspSearchCondition> autoInspDateList) {

        Map<String, Map<Long, List<BuildingAutoInspResultSet>>> autoInspMap = new LinkedHashMap<>();

        List<BuildingAutoInspResultSet> autoInspList = getResultList(meterTypeAutoInspServicedaoImpl,
                new BuildingAutoInspResultSet(buildingList, autoInspDateList));
        if (CollectionUtils.isEmpty(autoInspList)) {
            return autoInspMap;
        }

        for (BuildingAutoInspResultSet autoInspData : autoInspList) {
            String corpId = autoInspData.getCorpId();
            Long buildingId = autoInspData.getBuildingId();

            Map<Long, List<BuildingAutoInspResultSet>> autoInspInnerMap = autoInspMap.getOrDefault(corpId,
                    new LinkedHashMap<>());

            List<BuildingAutoInspResultSet> resultAutoInspDateList = autoInspInnerMap.getOrDefault(buildingId,
                    new ArrayList<>());

            resultAutoInspDateList.add(autoInspData);

            autoInspInnerMap.put(buildingId, resultAutoInspDateList);
            autoInspMap.put(corpId, autoInspInnerMap);
        }

        return autoInspMap;
    }
}
