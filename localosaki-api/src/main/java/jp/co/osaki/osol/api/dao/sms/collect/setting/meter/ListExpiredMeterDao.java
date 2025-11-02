package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListExpiredMeterParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListExpiredMeterDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.DevRelationServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;

@Stateless
public class ListExpiredMeterDao extends OsolApiDao<ListExpiredMeterParameter> {

    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;
    private final ListExpiredMeterDaoImpl expiredMeterDaoImpl;
    private final DevRelationServiceDaoImpl devPrmListServiceDaoImpl;

    public ListExpiredMeterDao() {
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
        expiredMeterDaoImpl = new ListExpiredMeterDaoImpl();
        devPrmListServiceDaoImpl = new DevRelationServiceDaoImpl();
    }

    @Override
    public ListSmsMeterResult query(ListExpiredMeterParameter parameter) throws Exception {
        // クエリ生成時に必要なパラメータを設定
        ListSmsMeterResultData meterResultData = new ListSmsMeterResultData();
        meterResultData.setCorpId(parameter.getCorpId());
        meterResultData.setExamEndYm(parameter.getExpiredMeter());
        ListSmsMeterResult result = new ListSmsMeterResult();

        // 装置ID
        List<Object> devIdList = new ArrayList<>();

        if(parameter.getDevId() != null) {
            meterResultData.setDevId(parameter.getDevId());
            devIdList.add(parameter.getDevId());
            List<ListSmsMeterResultData> resultMeterInfoList = getResultList(expiredMeterDaoImpl, meterResultData);
            return commonExe(parameter, meterResultData, resultMeterInfoList);
        }else {
            // 建物、装置関連テーブル(M_DEV_RELATION) から 装置ID を取得
            MDevRelation devRelationTarget = new MDevRelation();
            devRelationTarget.setId(new MDevRelationPK());
            devRelationTarget.getId().setCorpId(parameter.getCorpId()); // 企業ID
            devRelationTarget.getId().setBuildingId(parameter.getBuildingId()); // 建物ID
            List<MDevRelation> devRelationList = getResultList(devPrmListServiceDaoImpl, devRelationTarget);
            List<ListSmsMeterResultData> meterInfoAddList = new ArrayList<>();
            for(MDevRelation ret : devRelationList) {
                meterResultData.setDevId(ret.getId().getDevId());
                List<ListSmsMeterResultData> meterInfoList = getResultList(expiredMeterDaoImpl, meterResultData);
                ListSmsMeterResult retResult = commonExe(parameter, meterResultData, meterInfoList);
                if(retResult != null && retResult.getList() != null && retResult.getList().size() > 0) {
                    meterInfoAddList.addAll(retResult.getList());
                }
            }
            result.setList(setDevName(meterInfoAddList, devRelationList));

            return result;
        }
    }

    private List<ListSmsMeterResultData> setDevName(List<ListSmsMeterResultData> list, List<MDevRelation> relationList){
        Map<String, String> devPrmMap = new HashMap<>();
        for(MDevRelation ret : relationList) {
            if(ret.getMDevPrm().getName() != null) {
                devPrmMap.put(ret.getMDevPrm().getDevId(), ret.getMDevPrm().getName());
            }else {
                // 装置名がnullの場合は空文字をset
                devPrmMap.put(ret.getMDevPrm().getDevId(), "");
            }
        }

        for(int i = 0;i < list.size(); i++) {
            list.get(i).setDevName(devPrmMap.get(list.get(i).getDevId()));
        }

        return list;
    }

    public ListSmsMeterResult commonExe(ListExpiredMeterParameter parameter, ListSmsMeterResultData meterResultData, List<ListSmsMeterResultData> resultMeterInfoList) {
        ListSmsMeterResult result = new ListSmsMeterResult();

        // メーターフィルター
        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getCorpId());
        keyList.put(BuildingPersonDevDataParam.CORP_ID, corpIdList);

        // 建物ID 必須
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(parameter.getBuildingId());
        keyList.put(BuildingPersonDevDataParam.BUILDING_ID, buildingIdList);

        // ログイン担当者企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(parameter.getLoginCorpId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_CORP_ID, loginCorpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(parameter.getLoginPersonId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_PERSON_ID, personIdList);

        // 装置ID
        List<Object> devIdList = new ArrayList<>();
        devIdList.add(meterResultData.getDevId());
        keyList.put(BuildingPersonDevDataParam.DEV_ID, devIdList);

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);
        // フィルターリストと重複しているデータ以外を削除
        resultMeterInfoList.removeAll(
                resultMeterInfoList.stream()
                        .filter(x -> filterList.stream()
                                .noneMatch(y -> y.getDevId().equals(meterResultData.getDevId())
                                        && y.getMeterMngId().equals(x.getMeterMngId())))
                        .collect(Collectors.toList()));

        result.setList(resultMeterInfoList);

        return result;
    }

}
