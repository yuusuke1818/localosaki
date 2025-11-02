/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.ocr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.ListSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.ocr.ListSmsOcrDeviceRequest;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.ListSmsOcrDeviceResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.ocr.ListSmsOcrDeviceDetailResultData;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.ocr.ListSmsOcrDeviceServiceDaoImpl;

/**
 * 装置情報一覧取得（AieLinkのみ） Daoクラス
 * 「OCR検針」→「AieLink」へ変更
 * @author iwasaki_y
 */
@Stateless
public class ListSmsOcrDeviceDao extends OsolApiDao<ListSmsOcrDeviceParameter> {

    private final ListSmsOcrDeviceServiceDaoImpl listSmsOcrDeviceServiceDaoImpl;

    public ListSmsOcrDeviceDao() {
        listSmsOcrDeviceServiceDaoImpl = new ListSmsOcrDeviceServiceDaoImpl();
    }

    @Override
    public ListSmsOcrDeviceResult query(ListSmsOcrDeviceParameter parameter) throws Exception {
        ListSmsOcrDeviceResult result = new ListSmsOcrDeviceResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new ListSmsOcrDeviceResult();
        }

        //JSON⇒Resultに変換
        ListSmsOcrDeviceRequest request = parameter.getRequest();

        Map<String, List<Object>> conditionMap = new HashMap<>();
        conditionMap.put(OsolConstants.SEARCH_CONDITION_OCR_ID_OR_NAME, request.getDevIdOrNameList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_OCR_ID, request.getDevIdList());
        conditionMap.put(OsolConstants.SEARCH_CONDITION_OCR_STATUS, request.getDelFlgList());

        List<ListSmsOcrDeviceDetailResultData> resultList = getResultList(listSmsOcrDeviceServiceDaoImpl, conditionMap);
        result.setDetailList(resultList);

        return result;
    }

}
