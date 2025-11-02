package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.BuildingJoinBuildingInfoSearchDaoImpl;

/**
 * 建物情報検索Daoクラス
 *
 * @author ozaki.y
 */
@Stateless
public class BuildingInfoSearchDao extends SmsDao {

    /**
     * 建物情報検索（権限対応版）
     */
    private final BuildingJoinBuildingInfoSearchDaoImpl buildingJoinBuildingInfoSearchDaoImpl;

    public BuildingInfoSearchDao() {
        buildingJoinBuildingInfoSearchDaoImpl = new BuildingJoinBuildingInfoSearchDaoImpl();
    }

    // 建物情報検索（権限対応追加）
    public List<TBuilding> getSearchList(
            List<Object> targetCorpIdList,
            List<Object> targetCorpIdOrNameList,
            List<Object> targetNyukyoTypeList,
            List<Object> targetPrefectureList,
            List<Object> targetBuildingNoOrNameList,
            List<Object> targetBuildingStateList,
            Date targetDate,
            String personCorpId,
            String personId,
            String corpType,
            String personType) {
        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(OsolConstants.SEARCH_CONDITION_CORP_ID, targetCorpIdList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME, targetCorpIdOrNameList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, targetPrefectureList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY, targetBuildingNoOrNameList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD, targetNyukyoTypeList);
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_STATE, targetBuildingStateList);

        List<Object> targetCorpPersonList = new ArrayList<>();
        targetCorpPersonList.add(corpType);
        targetCorpPersonList.add(personType);
        parameterMap.put("CORP_PERSON_TYPE", targetCorpPersonList);

        List<Object> targetDateList = new ArrayList<>();
        targetDateList.add(targetDate);
        parameterMap.put("targetDate", targetDateList);

        List<Object> targetPersonList = new ArrayList<>();
        targetPersonList.add(personCorpId);
        targetPersonList.add(personId);
        parameterMap.put("person", targetPersonList);

        return getResultList(buildingJoinBuildingInfoSearchDaoImpl, parameterMap);
    }
}
