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
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.GetSmsLatestDmvKwhResultData;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.GetSmsLatestDmvKwhDaoImple;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsIotRMeterSearchDaoImple;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsMeterSearchDaoImple;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsOcrMeterSearchDaoImple;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsPulseMeterSearchDaoImple;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsWalkMeterSearchDaoImple;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConstants.METER_KIND;

/**
 * メーター管理 メーター情報一覧取得 Daoクラス
 * @author kimura.m
 */
@Stateless
public class ListSmsMeterSearchDao extends OsolApiDao<ListSmsMeterParameter> {

    private final ListSmsMeterSearchDaoImple meterSearchDaoImple;
    private final ListSmsPulseMeterSearchDaoImple pulseMeterSearchDaoImple;
    private final ListSmsIotRMeterSearchDaoImple iotRMeterSearchDaoImple;
    private final ListSmsWalkMeterSearchDaoImple walkMeterSearchDaoImple;
    private final ListSmsOcrMeterSearchDaoImple ocrMeterSearchDaoImple;
    private final GetSmsLatestDmvKwhDaoImple getSmsLatestDmvKwhDaoImple;
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;

    public ListSmsMeterSearchDao() {
        meterSearchDaoImple = new ListSmsMeterSearchDaoImple();
        pulseMeterSearchDaoImple = new ListSmsPulseMeterSearchDaoImple();
        iotRMeterSearchDaoImple = new ListSmsIotRMeterSearchDaoImple();
        getSmsLatestDmvKwhDaoImple = new GetSmsLatestDmvKwhDaoImple();
        walkMeterSearchDaoImple = new ListSmsWalkMeterSearchDaoImple();
        ocrMeterSearchDaoImple = new ListSmsOcrMeterSearchDaoImple();
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
    }

    @Override
    public ListSmsMeterResult query(ListSmsMeterParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsMeterResultData meterResultData = new ListSmsMeterResultData();
        meterResultData.setDevId(parameter.getDevId());
        meterResultData.setCorpId(parameter.getCorpId());

        // オフセットが指定されていない場合は0を入れる
        if (parameter.getOffset() != null) {
            meterResultData.setOffset(parameter.getOffset());
        } else {
            meterResultData.setOffset(0);
        }

        meterResultData.setAmount(parameter.getAmount());
        List<ListSmsMeterResultData> resultMeterInfoList = null;

        if (!CheckUtility.isNullOrEmpty(parameter.getMeterKind())) {
        	meterResultData.setDevName("fromWeb");	// Webからの要求を表すため
	        if (parameter.getMeterKind().equals(METER_KIND.SMART.getVal())) {
	            // ServiceDaoクラスにてクエリ実行
	            resultMeterInfoList = getResultList(meterSearchDaoImple, meterResultData);

	            // 最新検針値を取得してレスポンスにセットしていく処理
	            if (resultMeterInfoList != null) {
	                for (ListSmsMeterResultData resultMeterInfo : resultMeterInfoList) {
	                    GetSmsLatestDmvKwhResultData kwhParam = new GetSmsLatestDmvKwhResultData();
	                    kwhParam.setDevId(parameter.getDevId());
	                    kwhParam.setMeterMngId(resultMeterInfo.getMeterMngId());
	                    // ServiceDaoクラスにてクエリ実行
	                    List<GetSmsLatestDmvKwhResultData> kwhList = getResultList(getSmsLatestDmvKwhDaoImple, kwhParam);
	                    if (kwhList != null && kwhList.size() > 0) {
	                        resultMeterInfo.setDmvKwh(kwhList.get(0).getDmvKwh());
	                    }
	                }
	            }
            } else if (parameter.getMeterKind().equals(METER_KIND.PULSE.getVal())) {
                // ServiceDaoクラスにてクエリ実行
                resultMeterInfoList = getResultList(pulseMeterSearchDaoImple, meterResultData);
            } else if (parameter.getMeterKind().equals(METER_KIND.IOTR.getVal())) {
                // ServiceDaoクラスにてクエリ実行
                resultMeterInfoList = getResultList(iotRMeterSearchDaoImple, meterResultData);
            } else if (parameter.getMeterKind().equals(METER_KIND.HANDY.getVal())) {
                // ServiceDaoクラスにてクエリ実行
                resultMeterInfoList = getResultList(walkMeterSearchDaoImple, meterResultData);
            } else if (parameter.getMeterKind().equals(METER_KIND.OCR.getVal())) {
                // ServiceDaoクラスにてクエリ実行
                resultMeterInfoList = getResultList(ocrMeterSearchDaoImple, meterResultData);
            } else {
                throw new Exception();
	        }
        } else {
            List<ListSmsMeterResultData> resultMeterInfoListTmp = null;
        	meterResultData.setDevName("fromApi");	// APIからの要求を表すため

            // ServiceDaoクラスにてクエリ実行
            resultMeterInfoList = getResultList(meterSearchDaoImple, meterResultData);

            // 最新検針値を取得してレスポンスにセットしていく処理
            if (resultMeterInfoList != null) {
                for (ListSmsMeterResultData resultMeterInfo : resultMeterInfoList) {
                    GetSmsLatestDmvKwhResultData kwhParam = new GetSmsLatestDmvKwhResultData();
                    kwhParam.setDevId(parameter.getDevId());
                    kwhParam.setMeterMngId(resultMeterInfo.getMeterMngId());
                    // ServiceDaoクラスにてクエリ実行
                    List<GetSmsLatestDmvKwhResultData> kwhList = getResultList(getSmsLatestDmvKwhDaoImple, kwhParam);
                    if (kwhList != null && kwhList.size() > 0) {
                        resultMeterInfo.setDmvKwh(kwhList.get(0).getDmvKwh());
                    }
                }
            }

            // クエリ生成時に必要なパラメータを設定
            meterResultData.setDevName("fromApi");	// APIからの要求を表すため
        	resultMeterInfoListTmp = getResultList(pulseMeterSearchDaoImple, meterResultData);
            if (!resultMeterInfoListTmp.isEmpty()) {
                resultMeterInfoList.addAll(resultMeterInfoListTmp);
            }

            // クエリ生成時に必要なパラメータを設定
        	meterResultData.setDevName("fromApi");	// APIからの要求を表すため
			resultMeterInfoListTmp = getResultList(walkMeterSearchDaoImple, meterResultData);
            if (!resultMeterInfoListTmp.isEmpty()) {
                resultMeterInfoList.addAll(resultMeterInfoListTmp);
            }
        }

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
        devIdList.add(parameter.getDevId());
        keyList.put(BuildingPersonDevDataParam.DEV_ID, devIdList);

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);

        // フィルターリストと重複しているデータ以外を削除
        resultMeterInfoList.removeAll(
                resultMeterInfoList.stream()
                        .filter(x -> filterList.stream()
                                .noneMatch(y -> y.getDevId().equals(parameter.getDevId())
                                        && y.getMeterMngId().equals(x.getMeterMngId())))
                        .collect(Collectors.toList()));

        result.setList(resultMeterInfoList);
        return result;
    }
}
