/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.ListSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.collect.ListSmsCollectDeviceRequest;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.ListSmsCollectDeviceResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.collect.ListSmsCollectDeviceDetailResultData;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.collect.ListSmsCollectDeviceServiceDaoImpl;

/**
 * 装置情報一覧取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class ListSmsCollectDeviceDao extends OsolApiDao<ListSmsCollectDeviceParameter> {

    private final ListSmsCollectDeviceServiceDaoImpl listSmsCollectDeviceServiceDaoImpl;

    public ListSmsCollectDeviceDao() {
        listSmsCollectDeviceServiceDaoImpl = new ListSmsCollectDeviceServiceDaoImpl();
    }

    @Override
    public ListSmsCollectDeviceResult query(ListSmsCollectDeviceParameter parameter) throws Exception {
        ListSmsCollectDeviceResult result = new ListSmsCollectDeviceResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new ListSmsCollectDeviceResult();
        }

        //JSON⇒Resultに変換
        ListSmsCollectDeviceRequest request = parameter.getRequest();

        Map<String, List<Object>> conditionMap = new HashMap<>();
        conditionMap.put(OsolConstants.SEARCH_CONDITION_COLLECT_ID_OR_NAME, request.getDevIdOrNameList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_COLLECT_ID, request.getDevIdList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_COLLECT_STATUS, request.getDelFlgList());

        List<ListSmsCollectDeviceDetailResultData> resultList = getResultList(listSmsCollectDeviceServiceDaoImpl, conditionMap);
        result.setDetailList(resultList);

        return result;
    }

}
