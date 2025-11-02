/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.handy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.ListSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.handy.ListSmsHandyDeviceRequest;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.ListSmsHandyDeviceResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.handy.ListSmsHandyDeviceDetailResultData;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.handy.ListSmsHandyDeviceServiceDaoImpl;

/**
 * 装置情報一覧取得（ハンディ端末のみ） Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class ListSmsHandyDeviceDao extends OsolApiDao<ListSmsHandyDeviceParameter> {

    private final ListSmsHandyDeviceServiceDaoImpl listSmsHandyDeviceServiceDaoImpl;

    public ListSmsHandyDeviceDao() {
        listSmsHandyDeviceServiceDaoImpl = new ListSmsHandyDeviceServiceDaoImpl();
    }

    @Override
    public ListSmsHandyDeviceResult query(ListSmsHandyDeviceParameter parameter) throws Exception {
        ListSmsHandyDeviceResult result = new ListSmsHandyDeviceResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new ListSmsHandyDeviceResult();
        }

        //JSON⇒Resultに変換
        ListSmsHandyDeviceRequest request = parameter.getRequest();

        Map<String, List<Object>> conditionMap = new HashMap<>();
        conditionMap.put(OsolConstants.SEARCH_CONDITION_HANDY_ID_OR_NAME, request.getDevIdOrNameList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_HANDY_ID, request.getDevIdList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_HANDY_STATUS, request.getDelFlgList());

        List<ListSmsHandyDeviceDetailResultData> resultList = getResultList(listSmsHandyDeviceServiceDaoImpl, conditionMap);
        result.setDetailList(resultList);

        return result;
    }

}
