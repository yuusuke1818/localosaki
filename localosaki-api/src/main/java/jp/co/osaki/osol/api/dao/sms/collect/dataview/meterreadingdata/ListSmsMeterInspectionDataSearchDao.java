package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.GetSmsMeterInspectionDataSearchMaxDemandValResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataInfoSearchResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataSearchResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.GetSmsMBuildingSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.GetSmsMeterInspectionDataSearchMaxDemandValDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;

/**
 * 検針データ取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class ListSmsMeterInspectionDataSearchDao extends OsolApiDao<ListSmsMeterInspectionDataParameter> {

    private final ListSmsMeterInspectionServiceDaoImpl listSmsMeterInspectionServiceDaoImpl;
    private final ListSmsMeterInspectionDataServiceDaoImpl listSmsMeterInspectionDataServiceDaoImpl;
    private final GetSmsMeterInspectionDataSearchMaxDemandValDaoImpl getSmsMeterInspectionDataSearchMaxDemandValDaoImpl;
    private final GetSmsMBuildingSmsServiceDaoImpl getSmsMBuildingSmsServiceDaoImpl;
    public ListSmsMeterInspectionDataSearchDao() {
        listSmsMeterInspectionServiceDaoImpl = new ListSmsMeterInspectionServiceDaoImpl();
        listSmsMeterInspectionDataServiceDaoImpl = new ListSmsMeterInspectionDataServiceDaoImpl();
        getSmsMeterInspectionDataSearchMaxDemandValDaoImpl = new GetSmsMeterInspectionDataSearchMaxDemandValDaoImpl();
        getSmsMBuildingSmsServiceDaoImpl = new GetSmsMBuildingSmsServiceDaoImpl();
    }

    @Override
    public ListSmsMeterInspectionDataResult query(ListSmsMeterInspectionDataParameter parameter) throws Exception {

        List<ListSmsMeterInspectionDataResultData> resultMeterInspectionDataList = new ArrayList<>();
        ListSmsMeterInspectionDataInfoSearchResultData meterInfoSearchResultData = new ListSmsMeterInspectionDataInfoSearchResultData();

        meterInfoSearchResultData.setCorpId(parameter.getCorpId());
        meterInfoSearchResultData.setBuildingId(parameter.getBuildingId());
        meterInfoSearchResultData.setDevId(parameter.getDevId());
        List<String> devIds = Objects.isNull(parameter.getDevIdListStr()) ? null : Arrays.asList(parameter.getDevIdListStr().split(","));
        meterInfoSearchResultData.setDevIdList(devIds);

        List<ListSmsMeterInspectionDataInfoSearchResultData> resultMeterList = getResultList(listSmsMeterInspectionServiceDaoImpl, meterInfoSearchResultData);

        List<ListSmsMeterInspectionDataInfoSearchResultData> resultMeterInfoList = new ArrayList<>();
        Map<String, Map<Long, ListSmsMeterInspectionDataInfoSearchResultData>> retMeterDataMap = new HashMap<>();
        for (ListSmsMeterInspectionDataInfoSearchResultData data : resultMeterList) {
            if(!retMeterDataMap.containsKey(data.getDevId()) || !retMeterDataMap.get(data.getDevId()).containsKey(data.getMeterMngId())) {
                Map<Long, ListSmsMeterInspectionDataInfoSearchResultData> retMeterMap = retMeterDataMap.getOrDefault(data.getDevId(), new HashMap<>());
                // 一時データなのでnull入れる
                retMeterMap.put(data.getMeterMngId(), null);
                retMeterDataMap.put(data.getDevId(), retMeterMap);
                resultMeterInfoList.add(data);
            }
        }

        // 整数フラグ 取得
        boolean isInt = isInt(parameter);

        // 昨年データのメータタイプ別検針データ
        Map<Long, List<ListSmsMeterInspectionDataSearchResultData>> prevMeterSearchResultDataMap = new HashMap<>();

        for(ListSmsMeterInspectionDataInfoSearchResultData meter : resultMeterInfoList){
            // 今年用データ
            ListSmsMeterInspectionDataSearchResultData meterSearchResultData = new ListSmsMeterInspectionDataSearchResultData();
            meterSearchResultData.setDevId(meter.getDevId());
            meterSearchResultData.setMeterMngId(meter.getMeterMngId());
            meterSearchResultData.setYear(parameter.getYear());
            meterSearchResultData.setMonth(parameter.getMonth());
            meterSearchResultData.setMonthMeterReadingNo(parameter.getMonthInspectionNo());
            meterSearchResultData.setMeterReadingType(parameter.getInspectionType());

            // 指定した表示条件の検針データ取得(Listサイズは絶対1になる)
            List<ListSmsMeterInspectionDataSearchResultData> meterSearchResultDataList = getResultList(listSmsMeterInspectionDataServiceDaoImpl, meterSearchResultData);
            // 対象のデータがある場合、昨年度のデータを取得
            if(meterSearchResultDataList.size() != 0) {
                // 昨年用データ
                ListSmsMeterInspectionDataSearchResultData prevMeterSearchResultData = new ListSmsMeterInspectionDataSearchResultData();
                prevMeterSearchResultData.setDevId(meter.getDevId());
                prevMeterSearchResultData.setMeterMngId(meter.getMeterMngId());
                prevMeterSearchResultData.setYear(String.valueOf(Integer.parseInt(parameter.getYear()) - 1));
                prevMeterSearchResultData.setMonth(parameter.getMonth());
                prevMeterSearchResultData.setMeterReadingType(parameter.getInspectionType());
                // 指定した表示条件の検針データから昨年同月のデータを取得する(月検針連番指定なし)
                List<ListSmsMeterInspectionDataSearchResultData> prevMeterSearchResultDataList = getResultList(listSmsMeterInspectionDataServiceDaoImpl, prevMeterSearchResultData);

                // メータタイプ毎にデータを加工
//                if(prevMeterSearchResultDataList.size() != 0) {
                    prevMeterSearchResultDataMap.put(meter.getMeterMngId(), prevMeterSearchResultDataList);
//                }
            }
            ListSmsMeterInspectionDataResultData resultMeterInspectionDataResultData = new ListSmsMeterInspectionDataResultData(meter,
                    meterSearchResultDataList, prevMeterSearchResultDataMap.get(meter.getMeterMngId()), isInt, parameter.getYear());

            if (Boolean.TRUE.equals(resultMeterInspectionDataResultData.getDataExistsFlg())) {

                GetSmsMeterInspectionDataSearchMaxDemandValResultData getSmsMeterInspectionDataSearchMaxDemandValResultData = new GetSmsMeterInspectionDataSearchMaxDemandValResultData();
                getSmsMeterInspectionDataSearchMaxDemandValResultData.setDevId(meter.getDevId());
                getSmsMeterInspectionDataSearchMaxDemandValResultData.setMeterMngId(meter.getMeterMngId());
                getSmsMeterInspectionDataSearchMaxDemandValResultData.setLatestInspDate(resultMeterInspectionDataResultData.getLatestInspDate());
                getSmsMeterInspectionDataSearchMaxDemandValResultData.setPrevInspDate(resultMeterInspectionDataResultData.getPrevInspDate());

                List<GetSmsMeterInspectionDataSearchMaxDemandValResultData> maxDemandValList = getResultList(getSmsMeterInspectionDataSearchMaxDemandValDaoImpl, getSmsMeterInspectionDataSearchMaxDemandValResultData);

                if (Objects.nonNull(maxDemandValList) && maxDemandValList.size() != 0) {
                    resultMeterInspectionDataResultData.setMaxDemandVal(Objects.isNull(maxDemandValList.get(0).getMaxDemandVal()) ? null : maxDemandValList.get(0).getMaxDemandVal().toString());
                }
                resultMeterInspectionDataList.add(resultMeterInspectionDataResultData);
            }
        }

        ListSmsMeterInspectionDataResult result = new ListSmsMeterInspectionDataResult();
        result.setMeterInspectionDataResultDataList(resultMeterInspectionDataList);

        return result;
    }

    /**
     * 整数フラグ 取得
     * @param parameter 検索条件
     * @return 整数フラグ  true:整数  false:小数
     */
    private boolean isInt(ListSmsMeterInspectionDataParameter parameter) {
        MBuildingSms target = new MBuildingSms();
        target.setId(new MBuildingSmsPK());
        target.getId().setCorpId(parameter.getCorpId()); // 企業ID
        target.getId().setBuildingId(parameter.getBuildingId()); // 建物ID
        MBuildingSms entity = find(getSmsMBuildingSmsServiceDaoImpl, target);
        if (entity == null) {
            return false; // 小数
        }
        return (entity.getChkInt() != null && entity.getChkInt().compareTo(BigDecimal.ONE) == 0);
    }

}
