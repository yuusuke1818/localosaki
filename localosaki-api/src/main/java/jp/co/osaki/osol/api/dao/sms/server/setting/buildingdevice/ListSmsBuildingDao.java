/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsBuildingParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.buildingdevice.ListSmsBuildingRequest;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsBuildingResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsBuildingDetailResultData;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.ListSmsBuildingServiceDaoImpl;
import jp.co.osaki.sms.SmsConstants;

/**
 * 装置情報一覧取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class ListSmsBuildingDao extends OsolApiDao<ListSmsBuildingParameter> {

    private final ListSmsBuildingServiceDaoImpl listSmsBuildingServiceDaoImpl;

    public ListSmsBuildingDao() {
        listSmsBuildingServiceDaoImpl = new ListSmsBuildingServiceDaoImpl();
    }

    @Override
    public ListSmsBuildingResult query(ListSmsBuildingParameter parameter) throws Exception {
        ListSmsBuildingResult result = new ListSmsBuildingResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new ListSmsBuildingResult();
        }

        //DBサーバ時刻取得
        Timestamp serverDateTime = getServerDateTime();

        //JSON⇒Resultに変換
        ListSmsBuildingRequest request = parameter.getRequest();

        Map<String, List<Object>> conditionMap = new HashMap<>();
        conditionMap.put(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME, request.getCorpIdOrNameList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY,
                request.getBuildingNoOrNameList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, request.getPrefectureList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_STATE, request.getBuildingStateList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD, request.getNyukyoTypeList());

        //建物状況で使用するため、現在日時をパラメータへ追加
        List<Object> dateList = new ArrayList<>();
        Date date = new Date(serverDateTime.getTime());
        dateList.add(date);
        conditionMap.put(SmsConstants.SEARCH_CONDITON_TAGET_DATE, dateList);

        List<ListSmsBuildingDetailResultData> resultList = getResultList(listSmsBuildingServiceDaoImpl, conditionMap);
        result.setDetailList(resultList);

        return result;
    }

}
