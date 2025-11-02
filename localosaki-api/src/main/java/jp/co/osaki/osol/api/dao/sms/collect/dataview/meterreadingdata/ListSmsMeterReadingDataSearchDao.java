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
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.GetSmsMeterReadingDataSearchMaxDemandValResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataInfoSearchResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataSearchResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.GetSmsMBuildingSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.GetSmsMeterReadingDataSearchMaxDemandValDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;

/**
 * 検針データ取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class ListSmsMeterReadingDataSearchDao extends OsolApiDao<ListSmsMeterReadingDataParameter> {

    private final ListSmsMeterReadingServiceDaoImpl listSmsMeterReadingServiceDaoImpl;
    private final ListSmsMeterReadingDataServiceDaoImpl listSmsMeterReadingDataServiceDaoImpl;
    private final GetSmsMeterReadingDataSearchMaxDemandValDaoImpl getSmsMeterReadingDataSearchMaxDemandValDaoImpl;
    private final GetSmsMBuildingSmsServiceDaoImpl getSmsMBuildingSmsServiceDaoImpl;
    public ListSmsMeterReadingDataSearchDao() {
        listSmsMeterReadingServiceDaoImpl = new ListSmsMeterReadingServiceDaoImpl();
        listSmsMeterReadingDataServiceDaoImpl = new ListSmsMeterReadingDataServiceDaoImpl();
        getSmsMeterReadingDataSearchMaxDemandValDaoImpl = new GetSmsMeterReadingDataSearchMaxDemandValDaoImpl();
        getSmsMBuildingSmsServiceDaoImpl = new GetSmsMBuildingSmsServiceDaoImpl();
    }

    @Override
    public ListSmsMeterReadingDataResult query(ListSmsMeterReadingDataParameter parameter) throws Exception {

        List<ListSmsMeterReadingDataResultData> resultMeterReadingDataList = new ArrayList<>();
        ListSmsMeterReadingDataInfoSearchResultData meterInfoSearchResultData = new ListSmsMeterReadingDataInfoSearchResultData();

        meterInfoSearchResultData.setCorpId(parameter.getCorpId());
        meterInfoSearchResultData.setBuildingId(parameter.getBuildingId());
        meterInfoSearchResultData.setDevId(parameter.getDevId());
        List<String> devIds = Objects.isNull(parameter.getDevIdListStr()) ? null : Arrays.asList(parameter.getDevIdListStr().split(","));
        meterInfoSearchResultData.setDevIdList(devIds);

        List<ListSmsMeterReadingDataInfoSearchResultData> resultMeterList = getResultList(listSmsMeterReadingServiceDaoImpl, meterInfoSearchResultData);

        List<ListSmsMeterReadingDataInfoSearchResultData> resultMeterInfoList = new ArrayList<>();
        Map<String, Map<Long, ListSmsMeterReadingDataInfoSearchResultData>> retMeterDataMap = new HashMap<>();
        for (ListSmsMeterReadingDataInfoSearchResultData data : resultMeterList) {
            if(!retMeterDataMap.containsKey(data.getDevId()) || !retMeterDataMap.get(data.getDevId()).containsKey(data.getMeterMngId())) {
                Map<Long, ListSmsMeterReadingDataInfoSearchResultData> retMeterMap = retMeterDataMap.getOrDefault(data.getDevId(), new HashMap<>());
                // 一時データなのでnull入れる
                retMeterMap.put(data.getMeterMngId(), null);
                retMeterDataMap.put(data.getDevId(), retMeterMap);
                resultMeterInfoList.add(data);
            }
        }

        // 整数フラグ 取得
        boolean isInt = isInt(parameter);

        // 昨年データのメータタイプ別検針データ
        Map<Long, List<ListSmsMeterReadingDataSearchResultData>> prevMeterSearchResultDataMap = new HashMap<>();

        for(ListSmsMeterReadingDataInfoSearchResultData meter : resultMeterInfoList){
            // 今年用データ
            ListSmsMeterReadingDataSearchResultData meterSearchResultData = new ListSmsMeterReadingDataSearchResultData();
            meterSearchResultData.setDevId(meter.getDevId());
            meterSearchResultData.setMeterMngId(meter.getMeterMngId());
            meterSearchResultData.setYear(parameter.getYear());
            meterSearchResultData.setMonth(parameter.getMonth());
            meterSearchResultData.setMonthMeterReadingNo(parameter.getMonthMeterReadingNo());
            meterSearchResultData.setMeterReadingType(parameter.getMeterReadingType());

            // 指定した表示条件の検針データ取得(Listサイズは絶対1になる)
            List<ListSmsMeterReadingDataSearchResultData> meterSearchResultDataList = getResultList(listSmsMeterReadingDataServiceDaoImpl, meterSearchResultData);
            // 対象のデータがある場合、昨年度のデータを取得
            if(meterSearchResultDataList.size() != 0) {
                // 昨年用データ
                ListSmsMeterReadingDataSearchResultData prevMeterSearchResultData = new ListSmsMeterReadingDataSearchResultData();
                prevMeterSearchResultData.setDevId(meter.getDevId());
                prevMeterSearchResultData.setMeterMngId(meter.getMeterMngId());
                prevMeterSearchResultData.setYear(String.valueOf(Integer.parseInt(parameter.getYear()) - 1));
                prevMeterSearchResultData.setMonth(parameter.getMonth());
                prevMeterSearchResultData.setMeterReadingType(parameter.getMeterReadingType());
                // 指定した表示条件の検針データから昨年同月のデータを取得する(月検針連番指定なし)
                List<ListSmsMeterReadingDataSearchResultData> prevMeterSearchResultDataList = getResultList(listSmsMeterReadingDataServiceDaoImpl, prevMeterSearchResultData);

                // メータタイプ毎にデータを加工
//                if(prevMeterSearchResultDataList.size() != 0) {
                    prevMeterSearchResultDataMap.put(meter.getMeterMngId(), prevMeterSearchResultDataList);
//                }
            }
            ListSmsMeterReadingDataResultData resultMeterReadingDataResultData = new ListSmsMeterReadingDataResultData(meter,
                    meterSearchResultDataList, prevMeterSearchResultDataMap.get(meter.getMeterMngId()), isInt, parameter.getYear());

            if (Boolean.TRUE.equals(resultMeterReadingDataResultData.getDataExistsFlg())) {

                GetSmsMeterReadingDataSearchMaxDemandValResultData getSmsMeterReadingDataSearchMaxDemandValResultData = new GetSmsMeterReadingDataSearchMaxDemandValResultData();
                getSmsMeterReadingDataSearchMaxDemandValResultData.setDevId(meter.getDevId());
                getSmsMeterReadingDataSearchMaxDemandValResultData.setMeterMngId(meter.getMeterMngId());
                getSmsMeterReadingDataSearchMaxDemandValResultData.setLatestInspDate(resultMeterReadingDataResultData.getLatestInspDate());
                getSmsMeterReadingDataSearchMaxDemandValResultData.setPrevInspDate(resultMeterReadingDataResultData.getPrevInspDate());

                List<GetSmsMeterReadingDataSearchMaxDemandValResultData> maxDemandValList = getResultList(getSmsMeterReadingDataSearchMaxDemandValDaoImpl, getSmsMeterReadingDataSearchMaxDemandValResultData);

                if (Objects.nonNull(maxDemandValList) && maxDemandValList.size() != 0) {
                    resultMeterReadingDataResultData.setMaxDemandVal(Objects.isNull(maxDemandValList.get(0).getMaxDemandVal()) ? null : maxDemandValList.get(0).getMaxDemandVal().toString());
                }
                resultMeterReadingDataList.add(resultMeterReadingDataResultData);
            }
        }

        ListSmsMeterReadingDataResult result = new ListSmsMeterReadingDataResult();
        result.setMeterReadingDataResultDataList(resultMeterReadingDataList);

        return result;
    }

    /**
     * 整数フラグ 取得
     * @param parameter 検索条件
     * @return 整数フラグ  true:整数  false:小数
     */
    private boolean isInt(ListSmsMeterReadingDataParameter parameter) {
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
