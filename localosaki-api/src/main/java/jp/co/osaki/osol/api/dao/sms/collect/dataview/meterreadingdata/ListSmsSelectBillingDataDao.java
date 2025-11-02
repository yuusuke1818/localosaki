package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import com.cronutils.utils.StringUtils;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingResultDate;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.SmsTenantInfoResultDate;
import jp.co.osaki.osol.api.servicedao.entity.TInspectionMeterSelectBillingDateServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;

@Stateless
public class ListSmsSelectBillingDataDao extends OsolApiDao<ListSmsSelectBillingDataParameter> {

    private final ListSmsBillingAmountDataServiceDaoImpl listSmsBillingAmountDataServiceDaoImpl;
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;
    private final TInspectionMeterSelectBillingDateServiceDaoImpl tInspectionMeterSelectBillingDateServiceDaoImpl;
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;


    public ListSmsSelectBillingDataDao() {
        listSmsBillingAmountDataServiceDaoImpl = new ListSmsBillingAmountDataServiceDaoImpl();
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
        tInspectionMeterSelectBillingDateServiceDaoImpl = new TInspectionMeterSelectBillingDateServiceDaoImpl();
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
    }

    @Override
    public ListSmsSelectBillingDataResult query(ListSmsSelectBillingDataParameter parameter) throws Exception {

        /** 検針請求データ取得処理. */
        //宣言
        Map<String, List<Object>> parameterMap = new HashMap<>();
        List<Object> targetDateList = new ArrayList<>();
        List<ListSmsSelectBillingResultDate> listSmsSelectBillingResultDateList = new ArrayList<ListSmsSelectBillingResultDate>();
        ListSmsSelectBillingDataResult result = new ListSmsSelectBillingDataResult();
        ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData = new ListSmsBillingAmountDataInfoResultData();

        buildingBillingInfoResultData.setCorpId(parameter.getCorpId());
        buildingBillingInfoResultData.setBuildingId(parameter.getBuildingId());

        //建物請求情報取得処理
        List<ListSmsBillingAmountDataInfoResultData> resultBuildingBillingInfoList = getResultList(
                listSmsBillingAmountDataServiceDaoImpl, buildingBillingInfoResultData);

        // メーターフィルター
        //DevIDとMeterMngIDでMAP化
        MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData> listSmsBillingAmountDataInfoMultiKeyMap = meterDataFilter(
                resultBuildingBillingInfoList, parameter);

        //検索条件を格納
        targetDateList.add(parameter);
        parameterMap.put("ListSmsSelectBillingDataParameter", targetDateList);

        //請求対象データ取得
        List<TInspectionMeterSvr> resultTInspectionMeterSvrList = getResultList(
                tInspectionMeterSelectBillingDateServiceDaoImpl, parameterMap);

        //フィルター：月
        String fromYear = parameter.getFromYear();
        String fromMonth = parameter.getFromMonth();
        String toYear = parameter.getToYear();
        String toMonth = parameter.getToMonth();

        resultTInspectionMeterSvrList = DateList(resultTInspectionMeterSvrList, fromYear,
                fromMonth, toYear, toMonth);

        //フィルター：MeterMngId,DevId
        resultTInspectionMeterSvrList = filterIdt(resultTInspectionMeterSvrList,
                listSmsBillingAmountDataInfoMultiKeyMap);

        //メータータイプのマップを生成
        Map<Long, String> meterTypeMap = getMeterTypeName(parameter.getCorpId(), parameter.getBuildingId());

        //検針データ作成
        listSmsSelectBillingResultDateList = createMeterReadingData(resultTInspectionMeterSvrList,
                listSmsBillingAmountDataInfoMultiKeyMap, meterTypeMap);

        //取得した検針データをソート
        listSmsSelectBillingResultDateList = sort(listSmsSelectBillingResultDateList);

        result.setSelectBillingResultDateListList(listSmsSelectBillingResultDateList);

        return result;
    }

    /**
     * メーターフィルター(コメントを書く)
     *
     * @return
     */
    private MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData> meterDataFilter(
            List<ListSmsBillingAmountDataInfoResultData> targetList, ListSmsSelectBillingDataParameter parameter) {
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

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);

        // フィルターリストと重複しているデータ以外を削除
        targetList.removeAll(
                targetList.stream()
                        .filter(x -> filterList.stream().noneMatch(
                                y -> y.getDevId().equals(x.getDevId()) && y.getMeterMngId().equals(x.getMeterMngId())))
                        .collect(Collectors.toList()));

        //DevIDとMeterMngIDでMAPを作成する
        MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData> listSmsBillingAmountDataInfoMultiKeyMap = new MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData>();

        for (ListSmsBillingAmountDataInfoResultData target : targetList) {
            listSmsBillingAmountDataInfoMultiKeyMap
                    .put(new MultiKey<String>(target.getDevId(), target.getMeterMngId().toString()), target);
        }

        return listSmsBillingAmountDataInfoMultiKeyMap;
    }

    /**
     * 最新の更新日から検針日を取得
     * @return 検針日
     */

    private int convertIntMeterReadingDate(Timestamp date) {

        if (date == null) {
            return 0;
        }

        String meterReadingDate = new SimpleDateFormat("yyyyMMdd").format(date);

        return Integer.parseInt(meterReadingDate);
    }

    /**
     * 最新の更新日から検針日を取得
     * @return 検針日
     */

    private String convertMeterReadingDate(Timestamp date, String delimiter) {

        if (date == null) {
            return null;
        }

        String meterReadingDate = null;

        switch (delimiter) {
        case "/":
            meterReadingDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
            break;
        default:
            meterReadingDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
        }

        return meterReadingDate;
    }

    /**
     * 検針連番を取得
     * @return 検針日
     */

    private String getMeterReadingSerialNumber(Long inspMonthNo, String inspType) {

        String meterReadingSerialNumber = String.format("%03d", inspMonthNo) + inspType;
        String meterReadingType = null;

        //検針の種類を取得
        switch (inspType) {

        case "a":
            meterReadingType = "(自動検針)";
            break;
        case "m":
            meterReadingType = "(任意検針)";
            break;
        case "s":
            meterReadingType = "(予約検針)";
            break;
        case "r":
            meterReadingType = "(定期検針)";
            break;
        case "t":
            meterReadingType = "(臨時検針)";
        default:
            meterReadingType = "";
        }

        //検針連番と検針種別
        meterReadingSerialNumber = meterReadingSerialNumber + meterReadingType;

        return meterReadingSerialNumber;
    }

    /**
     * メーター種別名称 取得
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return Map<METER_TYPE, METER_TYPE_NAME>
     */
    private Map<Long, String> getMeterTypeName(String corpId, Long buildingId) {
        Map<Long, String> map = new HashMap<Long, String>();

        MMeterTypePK targetPk = new MMeterTypePK();
        targetPk.setCorpId(corpId); // 企業ID
        targetPk.setBuildingId(buildingId); // 建物ID
        MMeterType target = new MMeterType();
        target.setId(targetPk); // 条件

        // (補足)
        // メーター種別設定 の 主キーは「メーター種別, 企業ID, 建物ID, メニュー番号」のため、
        // 条件「企業ID, 建物ID」で検索しても、メーター種別(METER_TYPE) でユニークにならない。
        // ただし、必要な項目はメーター種別名称(METER_TYPE_NAME)のため、重複していても同じ名称が取得できる

        List<MMeterType> entityList = getResultList(meterTypeServiceDaoImpl, target);
        if (entityList != null) {
            entityList.stream().forEach(row -> {
                map.put(row.getId().getMeterType(), row.getMeterTypeName());
            });
        }

        return map;
    }

    /**
     * 月の範囲で絞込
     * @param corpId 対象データ
     * @param buildingId 開始月
     * @param buildingId 終了月
     * @return 絞込後データ
     */

    private List<TInspectionMeterSvr> DateList(List<TInspectionMeterSvr> tInspectionMeterSvrList, String fromYear,
            String fromMonth,
            String toYear, String toMonth) {

        int cnvIntFromDate = Integer.parseInt(fromYear + String.format("%02d", Integer.parseInt(fromMonth)));
        int cnvIntToDate = Integer.parseInt(toYear + String.format("%02d", Integer.parseInt(toMonth)));

        List<TInspectionMeterSvr> tmpTInspectionMeterSvrList = new ArrayList<>();

        for (TInspectionMeterSvr resultTInspectionMeterSvr : tInspectionMeterSvrList) {

            int targetDate = Integer.parseInt(
                    resultTInspectionMeterSvr.getId().getInspYear() + String.format("%02d",
                            Integer.parseInt(resultTInspectionMeterSvr.getId().getInspMonth())));

            if (cnvIntFromDate <= targetDate && targetDate <= cnvIntToDate) {
                tmpTInspectionMeterSvrList.add(resultTInspectionMeterSvr);
            }

        }

        tInspectionMeterSvrList = tmpTInspectionMeterSvrList;

        return tInspectionMeterSvrList;

    }

    /**
     * meterMngIdとdevIdで絞込
     * @return 絞込後データ
     */

    private List<TInspectionMeterSvr> filterIdt(List<TInspectionMeterSvr> tInspectionMeterSvrList,
            MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData> listSmsBillingAmountDataInfoMultiKeyMap) {

        List<TInspectionMeterSvr> tmpTInspectionMeterSvrList = new ArrayList<>();

        for (TInspectionMeterSvr resultTInspectionMeterSvr : tInspectionMeterSvrList) {

            String meterMngId = resultTInspectionMeterSvr.getId().getMeterMngId().toString();
            String devId = resultTInspectionMeterSvr.getId().getDevId();

            if (listSmsBillingAmountDataInfoMultiKeyMap.get(devId, meterMngId) != null
                    || listSmsBillingAmountDataInfoMultiKeyMap.containsKey(devId, meterMngId)) {
                tmpTInspectionMeterSvrList.add(resultTInspectionMeterSvr);
            }

        }

        tInspectionMeterSvrList = tmpTInspectionMeterSvrList;

        return tInspectionMeterSvrList;

    }

    /**
     * データ整理
     * @return
     * @throws Exception
     */
    private List<ListSmsSelectBillingResultDate> createMeterReadingData(
            List<TInspectionMeterSvr> resultTInspectionMeterSvrList,
            MultiKeyMap<String, ListSmsBillingAmountDataInfoResultData> listSmsBillingAmountDataInfoMultiKeyMap,
            Map<Long, String> meterTypeMap)
            throws Exception {

        List<ListSmsSelectBillingResultDate> listSmsSelectBillingResultDateList = new ArrayList<ListSmsSelectBillingResultDate>();

        //MAPを作成する
        MultiKeyMap<String, ListSmsSelectBillingResultDate> listSmsSelectBillingResultDateMultiKeyMap = new MultiKeyMap<String, ListSmsSelectBillingResultDate>();

        //取得結果が0件の場合はそのまま返却する
        if (!(Objects.nonNull(resultTInspectionMeterSvrList) && resultTInspectionMeterSvrList.size() != 0)) {
            return listSmsSelectBillingResultDateList;
        }

        for (TInspectionMeterSvr resultTInspectionMeterSvr : resultTInspectionMeterSvrList) {

            //宣言
            ListSmsSelectBillingResultDate listSmsSelectBillingResultDate = new ListSmsSelectBillingResultDate();

            //検針日を取得
            String meterReadingDate = convertMeterReadingDate(resultTInspectionMeterSvr.getLatestInspDate(), "");

            //検針連番を取得
            String meterReadingSerialNumber = getMeterReadingSerialNumber(
                    resultTInspectionMeterSvr.getId().getInspMonthNo(), resultTInspectionMeterSvr.getInspType());

            //メーター種別を取得
            String devId = resultTInspectionMeterSvr.getId().getDevId();
            String meterMngId = resultTInspectionMeterSvr.getId().getMeterMngId().toString();

            ListSmsBillingAmountDataInfoResultData listSmsBillingAmountDataInfoResultData = listSmsBillingAmountDataInfoMultiKeyMap
                    .get(devId, meterMngId);

            String meterTypeName = meterTypeMap.get(listSmsBillingAmountDataInfoResultData.getMeterType());

            //メーター台数を取得
            int numberOfMeters = listSmsSelectBillingResultDate.getNumberOfMeters();

            //データを検針日・検針連番単位でマージする
            //・存在しない検針日・検針連番の組み合わせの場合
            //  検針日・検針連番の組み合わせでデータをMultiMAPに格納する
            //
            //・存在する検針日・検針連番の組み合わせの場合
            //  同一のkey値のデータを取得し、メーター台数と検針未完了数のみ更新する
            if (listSmsSelectBillingResultDateMultiKeyMap.get(meterReadingDate, meterReadingSerialNumber) != null
                    || listSmsSelectBillingResultDateMultiKeyMap.containsKey(meterReadingDate,
                            meterReadingSerialNumber)) {

                ListSmsSelectBillingResultDate tmplistSmsSelectBillingResultDate = listSmsSelectBillingResultDateMultiKeyMap
                        .get(meterReadingDate, meterReadingSerialNumber);

                int tmpNumberOfMeters = tmplistSmsSelectBillingResultDate.getNumberOfMeters();

                List<String> meterTypeNameList = tmplistSmsSelectBillingResultDate.getMeterTypeNameList();

                if (meterTypeNameList.indexOf(meterTypeName) == -1) {
                    meterTypeNameList.add(meterTypeName);
                    tmplistSmsSelectBillingResultDate.setMeterTypeNameList(meterTypeNameList); //メーター種別
                }

                tmplistSmsSelectBillingResultDate.setNumberOfMeters(tmpNumberOfMeters + 1);

                //検針が未完了の場合は件数を1件増やす
                if (resultTInspectionMeterSvr.getEndFlg().equals(BigDecimal.ZERO)) {
                    tmplistSmsSelectBillingResultDate
                            .setNumberOfUnfinishedMeterReading(tmplistSmsSelectBillingResultDate
                                    .getNumberOfUnfinishedMeterReading() + 1); //検針未完了数
                }

                /** テナント情報. */
                List<SmsTenantInfoResultDate> tenantInfoList = tmplistSmsSelectBillingResultDate.getTenantInfoList();
                tenantInfoList.add(
                        getSmsTenantInfoResultDate(listSmsBillingAmountDataInfoResultData, resultTInspectionMeterSvr));

                tmplistSmsSelectBillingResultDate.setTenantInfoList(tenantInfoList);//テナント情報

                listSmsSelectBillingResultDateMultiKeyMap.put(meterReadingDate, meterReadingSerialNumber,
                        tmplistSmsSelectBillingResultDate);

            } else {

                /** 種別(結合用). */
                List<String> meterTypeNameList = listSmsSelectBillingResultDate.getMeterTypeNameList();
                meterTypeNameList.add(meterTypeName);

                /** テナント情報. */
                List<SmsTenantInfoResultDate> tenantInfoList = listSmsSelectBillingResultDate.getTenantInfoList();
                tenantInfoList.add(
                        getSmsTenantInfoResultDate(listSmsBillingAmountDataInfoResultData, resultTInspectionMeterSvr));

                //請求データを格納
                listSmsSelectBillingResultDate.setMeterReadingDate(meterReadingDate); //検針日
                listSmsSelectBillingResultDate.setMeterReadingSerialNumber(meterReadingSerialNumber); //検針連番
                listSmsSelectBillingResultDate.setMeterTypeNameList(meterTypeNameList); //メーター種別
                listSmsSelectBillingResultDate.setNumberOfMeters(numberOfMeters + 1); //メータ台数
                listSmsSelectBillingResultDate.setSelectBillingFlg(Boolean.TRUE); //選択、初回は全てチェック有
                listSmsSelectBillingResultDate.setTenantInfoList(tenantInfoList);//テナント情報

                //検針が未完了の場合は件数を1件増やす
                if (resultTInspectionMeterSvr.getEndFlg().equals(BigDecimal.ZERO)) {
                    listSmsSelectBillingResultDate.setNumberOfUnfinishedMeterReading(
                            listSmsSelectBillingResultDate.getNumberOfUnfinishedMeterReading() + 1); //検針未完了数
                }

                //データソート用にLatestInspDateをint型変換し、seqIdとして保持する
                int seqId = convertIntMeterReadingDate(resultTInspectionMeterSvr.getLatestInspDate());
                listSmsSelectBillingResultDate.setSeqId(seqId);

                listSmsSelectBillingResultDateMultiKeyMap.put(meterReadingDate, meterReadingSerialNumber,
                        listSmsSelectBillingResultDate);
            }
        }

        //データマージ用に格納していたMAPからResultDateListに詰めなおす
        for (ListSmsSelectBillingResultDate val : listSmsSelectBillingResultDateMultiKeyMap.values()) {

            List<String> meterTypeNameList = val.getMeterTypeNameList();

            String meterTypeName = null;

            //meterTypeNameが複数ある場合は1つに集約する
            for (String getMeterTypeName : meterTypeNameList) {
                if (StringUtils.isEmpty(meterTypeName)) {
                    meterTypeName = getMeterTypeName;
                } else {
                    meterTypeName = meterTypeName + "," + getMeterTypeName;
                }

            }

            val.setMeterTypeName(meterTypeName);
            listSmsSelectBillingResultDateList.add(val);
        }

        return listSmsSelectBillingResultDateList;

    }

    /**
     * ソート
     * @return
     */
    private List<ListSmsSelectBillingResultDate> sort(
            List<ListSmsSelectBillingResultDate> listSmsSelectBillingResultDateList) {

        listSmsSelectBillingResultDateList.sort((a, b) -> Integer.compare(b.getSeqId(), a.getSeqId()));

        return listSmsSelectBillingResultDateList;

    }

    /**
     * テナント情報を取得
     *
     */
    private SmsTenantInfoResultDate getSmsTenantInfoResultDate(
            ListSmsBillingAmountDataInfoResultData listSmsBillingAmountDataInfoResultData,
            TInspectionMeterSvr resultTInspectionMeterSvr) {

        SmsTenantInfoResultDate smsTenantInfoResultDate = new SmsTenantInfoResultDate();

        smsTenantInfoResultDate.setListSmsBillingAmountDataInfoResultData(listSmsBillingAmountDataInfoResultData);
        smsTenantInfoResultDate.setResultTInspectionMeterSvr(resultTInspectionMeterSvr);


        return smsTenantInfoResultDate;

    }


}
