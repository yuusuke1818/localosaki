/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.manualinsp.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.search.ListManualInspectionParameter;
import jp.co.osaki.osol.api.result.sms.manualinsp.search.ListManualInspectionResult;
import jp.co.osaki.osol.api.resultdata.sms.manualinsp.search.ListManualInspectionResultData;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.search.ListManualInspectionServiceDaoImpl;

/**
 * 任意検針一覧取得 Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class ListManualInspectionDao extends OsolApiDao<ListManualInspectionParameter> {

    private final ListManualInspectionServiceDaoImpl listManualInspectionServiceDaoImpl;
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;

    public ListManualInspectionDao() {
        listManualInspectionServiceDaoImpl = new ListManualInspectionServiceDaoImpl();
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
    }

    @Override
    public ListManualInspectionResult query(ListManualInspectionParameter parameter) throws Exception {
        ListManualInspectionResult result = new ListManualInspectionResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new ListManualInspectionResult();
        }

        Map<String, List<Object>> paramMap = new HashMap<>();
        List<Object> corpIdList = new ArrayList<>();
        List<Object> buildingIdList = new ArrayList<>();
        List<Object> userNameConditionList = new ArrayList<>();
        List<Object> userCdConditionList = new ArrayList<>();
        List<Object> meterTypeNameConditionList = new ArrayList<>();
        List<Object> reserveInspDateConditionList = new ArrayList<>();

        corpIdList.add(parameter.getRequest().getCorpId());
        buildingIdList.add(parameter.getRequest().getBuildingId());
        userNameConditionList.add(parameter.getRequest().getUserName());
        userCdConditionList.add(parameter.getRequest().getUserCd());
        meterTypeNameConditionList.add(parameter.getRequest().getMeterTypeName());
        reserveInspDateConditionList.add(parameter.getRequest().getReserveInspDate());

        paramMap.put("corpId", corpIdList);
        paramMap.put("buildingId", buildingIdList);
        paramMap.put("userNameCondition", userNameConditionList);
        paramMap.put("userCdCondition", userCdConditionList);
        paramMap.put("meterTypeNameCondition", meterTypeNameConditionList);
        paramMap.put("reserveInspDateCondition", reserveInspDateConditionList);

        List<ListManualInspectionResultData> resultList = getResultList(listManualInspectionServiceDaoImpl, paramMap);

        // メーターフィルター
        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        keyList.put(BuildingPersonDevDataParam.CORP_ID, corpIdList);

        // 建物ID 必須
        keyList.put(BuildingPersonDevDataParam.BUILDING_ID, buildingIdList);

        // ログイン担当者企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(parameter.getLoginCorpId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_CORP_ID, loginCorpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(parameter.getLoginPersonId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_PERSON_ID, personIdList);

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);

        // フィルターリストと重複しているデータ以外を削除
        resultList.removeAll(
                resultList.stream()
                .filter(x -> filterList.stream().noneMatch(y -> y.getDevId().equals(x.getDevId()) && y.getMeterMngId().equals(x.getMeterMngId())))
                .collect(Collectors.toList()));
        result.setResultList(resultList);

        return result;
    }

}
