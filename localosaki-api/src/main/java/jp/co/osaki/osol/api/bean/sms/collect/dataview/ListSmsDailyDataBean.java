package jp.co.osaki.osol.api.bean.sms.collect.dataview;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.ListSmsDataViewDao;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.ListSmsMeterMngIdByDevIdDao;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.ListSmsDevRelationDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDailyDataParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.request.sms.collect.dataview.ListSmsDataViewRequest;
//import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDailyDataResponse;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDataViewResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsDevRelationResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DISP_TIME_UNIT;
import jp.co.osaki.sms.SmsConstants.DISP_TYPE;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 データ表示機能API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "ListSmsDailyDataBean")
@RequestScoped
public class ListSmsDailyDataBean extends OsolApiBean<ListSmsDailyDataParameter>
        implements BaseApiBean<ListSmsDailyDataParameter, ListSmsDataViewResponse> {

    private ListSmsDailyDataParameter parameter = new ListSmsDailyDataParameter();

//    private ListSmsDailyDataResponse response = new ListSmsDailyDataResponse();
    private ListSmsDataViewResponse response = new ListSmsDataViewResponse();

    @EJB
    ListSmsDataViewDao loadSurveyDataListDao;

    @EJB
    ListSmsMeterMngIdByDevIdDao listMeterMngIdByDevIdDao;

    @EJB
    ListSmsDevRelationDao listSmsDevRelationDao;

    @Override
    public ListSmsDailyDataParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsDailyDataParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsDataViewResponse execute() throws Exception {
    	String strParam;
        ListSmsDataViewParameter param = new ListSmsDataViewParameter();
//        ListSmsDailyDataParameter param = new ListSmsDailyDataParameter();
        List<String> devIdList = new ArrayList<>();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
//        if (this.parameter.getStrTenant() == null) {
//        	param.setTenant(this.parameter.isTenant());
//        } else {
//        	strParam = this.parameter.getStrTenant();
//        	if (strParam.contentEquals("true")) {
//        		param.setTenant(true);
//        	} else {
   		param.setTenant(false);
//        	}
//        }
        param.setDevId(this.parameter.getDevId());

//        if (this.parameter.isForwardDiract() == null) {
        if (this.parameter.getIsForwardDiract() == null) {
        	param.setForwardDiract(true);
        } else {
//        	strParam = this.parameter.isForwardDiract();
        	strParam = this.parameter.getIsForwardDiract();
        	if (strParam.contentEquals("true")) {
        		param.setForwardDiract(true);
        	} else {
        		param.setForwardDiract(false);
        	}
        }
//        if (this.parameter.isUse() == null) {
        if (this.parameter.getIsUse() == null) {
        	param.setUse(true);
        } else {
//        	if (this.parameter.isUse().contentEquals("true")) {
        	if (this.parameter.getIsUse().contentEquals("true")) {
        		param.setUse(true);
        	} else {
        		param.setUse(false);
        	}
        }
        param.setDispTimeUnit(this.parameter.getDispTimeUnit());
        param.setTargetDateTimeFormat(this.parameter.getTargetDateTimeFormat());
        if (!CheckUtility.isNullOrEmpty(this.parameter.getDevId())) {
        	devIdList.add(this.parameter.getDevId());
        	param.setDevId(this.parameter.getDevId());
        } else {
        	ListSmsDevRelationParameter devParameter = new ListSmsDevRelationParameter();
        	devParameter.setBean("ListSmsDevRelationBean");
        	devParameter.setCorpId(this.parameter.getCorpId());
        	devParameter.setBuildingId(this.parameter.getBuildingId());
        	ListSmsDevRelationResult devRelationResult = listSmsDevRelationDao.query(devParameter);
        	if (!devRelationResult.getDetailList().isEmpty()) {
        		for (int i = 0; i < devRelationResult.getDetailList().size(); i++) {
        			devIdList.add(devRelationResult.getDetailList().get(i).getDevId());
        		}
        	}
        }

        ListSmsDataViewResult result = new ListSmsDataViewResult();
        ListSmsDataViewResult resultTmp = new ListSmsDataViewResult();
        ListSmsDataViewResultData resultData = new ListSmsDataViewResultData();
        Map<String, ListSmsDataViewResultData> loadSurveyDataMapTmp = new LinkedHashMap<>();
        Map<String, ListSmsDataViewResultData> loadSurveyDataMapMerge = new LinkedHashMap<>();
        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        Integer dataCountX = 0;
        Integer dataCountY = 0;
        Long	meterMngId = 0L;

        for (String devIdTmp : devIdList) {
	        if (this.parameter.getRequest() == null) {
//	        	param.setRequest(apiRequest(this.parameter.getDevId(), this.parameter.getMeterMngId(), this.parameter.getTargetDate()));
	        	param.setDevId(devIdTmp);
	        	meterMngId = this.parameter.getMeterMngId();
//	        	param.setRequest(apiRequest(devIdTmp, this.parameter.getMeterMngId(), this.parameter.getTargetDate()));
	        	param.setRequest(apiRequest(devIdTmp, meterMngId, this.parameter.getTargetDate()));
	        } else {
//	        	param.setRequest(this.parameter.getRequest());
	        	param.setRequest(param.getRequest());
	        }

	        if (devIdList.size() == 1) {
	        	result = loadSurveyDataListDao.query(param);
	        	dataCountX = result.getCountX();
	        	dataCountY = result.getCountY();
	        }
	        else {
	        	resultTmp = loadSurveyDataListDao.query(param);
	        	if (resultTmp.getCountX() > 0) {
	        		if (dataCountY < resultTmp.getCountY()) {
	        			dataCountY = resultTmp.getCountY();
	        		}
		        	loadSurveyDataMapTmp = resultTmp.getLoadSurveyDataMap();
		        	for (String key : loadSurveyDataMapTmp.keySet()) {
		        		resultData = loadSurveyDataMapTmp.get(key);
		        		Integer tmpI = key.indexOf("Y");
		        		String tmpX = key.substring(6,tmpI);
		        		String tmpY = key.substring(tmpI+1);
		        		loadSurveyDataMapMerge.put(String.format(keyFormat, Integer.parseInt(tmpX)+dataCountX, Integer.parseInt(tmpY)),resultData);
		        	}
		        	dataCountX = dataCountX + resultTmp.getCountX();
	        	}
	        }
        }
        if (devIdList.size() > 1) {
        	result.setCountX(dataCountX);
        	result.setCountY(dataCountY);
        	result.setLoadSurveyDataMap(loadSurveyDataMapMerge);
        }
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
		return response;
    }

    /**
     * 表示データ
     *
     * @param
     * @param
     * @return
     */
    public ListSmsDataViewRequest apiRequest(String targetDevId, Long targetMeterMngId, String targetDateTime) {
    	ListSmsDataViewRequest request = new ListSmsDataViewRequest();
    	List<Long> getMeterMngId = new ArrayList<>();

    	if (targetMeterMngId == null) {
	    	try {
	    		getMeterMngId = getAllMeterMngIdByDevId(targetDevId);
	    	}
	    	catch (Exception e) {

	    	}
    	}
    	else {
    		getMeterMngId.add(targetMeterMngId);
    	}

    	request.setTargetDateTimeList(createTargetDateTimeList(targetDateTime));
    	request.setMeterMngIdList(getMeterMngId);	// getMeterMngId
    	return request;
    }

    /**
     * 装置IDを条件にメーター情報のメーター管理IDを全件取得する（他の条件設定なし）
     * @param devId 装置ID
     * @return メーター管理IDリスト
     */
//    private List<Long> getAllMeterMngIdByDevId(String devId) {
    private List<Long> getAllMeterMngIdByDevId(String devId) throws Exception {
    	// 検索
    	ListSmsMeterResponse response = new ListSmsMeterResponse();
    	ListSmsMeterParameter parameter = new ListSmsMeterParameter();
//    	ListSmsMeterResult result = new ListSmsMeterResult();

    	// APIBeanの指定
//    	parameter.setBean("ListSmsMeterSearchBean");	// listSmsMeterMngIdByDevIdBean
    	parameter.setBean("listSmsMeterMngIdByDevIdBean");	// ListSmsMeterSearchBean
    	parameter.setDevId(devId);

    	// DBから値取得
//    	response = callApiPost(parameter, ListSmsMeterResponse.class);
    	ListSmsMeterResult result = listMeterMngIdByDevIdDao.query(parameter);
//    	response = listMeterMngIdByDevIdDao.query(parameter);

    	List<Long> idList = new ArrayList<>();
		if (!result.getList().isEmpty()) {
			response.setResult(result);
//			idList.add(1L);
			for (ListSmsMeterResultData getData : response.getResult().getList()) {
				Long id = getData.getMeterMngId();
				idList.add(id);
			}
    	}
    	return idList;
    }


    /**
     * 対象日時Listを生成.
     *
     * @param condition 検索条件
     * @return 対象日時List
     */
/*    private List<String> createTargetDateTimeList(DataViewSearchCondition condition) {
        DataViewBeanProperty property = getProperty();*/
    private List<String> createTargetDateTimeList(String strStartDateTimeStr) {

        String startDateTimeStr = strStartDateTimeStr;

        String targetDateTimeFormat = "yyyyMMddHHmm";

        List<String> targetDateTimeList = new ArrayList<>();
/*        if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)) {*/
            targetDateTimeList = createStartTimeStrList(targetDateTimeFormat, startDateTimeStr, "0000");

/*        } else {
            targetDateTimeList = createTargetDateStrList(startDateTimeStr, targetDateTimeFormat,
                    property.getTargetDateTimeDuration(), property.getTargetDateTimeAddtion());
        }*/

        return targetDateTimeList;
    }

    /**
     * 開始時刻文字列Listを生成.
     *
     * @param targetDateTimeFormat 対象日時フォーマット
     * @param startDateStr 開始日付文字列
     * @param startTimeStr 開始時刻文字列
     * @return 開始時刻文字列List
     */
    private List<String> createStartTimeStrList(String targetDateTimeFormat, String startDateStr, String startTimeStr) {
/*        DataViewSearchCondition condition = getPresentCondition();*/

        // 表示種別
        DISP_TYPE dispTypeEnum = DISP_TYPE.getTarget("1");
        // 表示時間単位
        DISP_TIME_UNIT dispTimeUnitEnum = DISP_TIME_UNIT.getTarget("30");

        String addition = dispTypeEnum.getAddition();
        if (DISP_TIME_UNIT.M60 == dispTimeUnitEnum) {
            // 表示時間単位が1時間の場合
            addition = "PT23H";
        }

        if (CheckUtility.isNullOrEmpty(startTimeStr)) {
            startTimeStr = dispTypeEnum.getStartTime();
        }

        return createTargetDateTimeStrList((startDateStr + startTimeStr), targetDateTimeFormat,
                dispTimeUnitEnum.getDuration(), addition);
    }

    /**
     * 指定間隔及び指定日時までの対象日時文字列Listを生成.
     *
     * @param targetDateFromStr 対象日時(FROM)文字列
     * @param patternStr 日時パターン文字列
     * @param duration 刻み時間文字列
     * @param addition 取得終了までの時間数文字列
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(String targetDateFromStr, String patternStr,
            String durationStr, String additionStr) {

        return createTargetDateTimeStrList(
                LocalDateTime.parse(targetDateFromStr, DateTimeFormatter.ofPattern(patternStr)), patternStr,
                Duration.parse(durationStr), Duration.parse(additionStr));
    }

    /**
     * 指定間隔及び指定日時までの対象日時文字列Listを生成.
     *
     * @param targetDateFromStr 対象日時(FROM)文字列
     * @param patternStr 日時パターン文字列
     * @param duration 刻み時間
     * @param addition 取得終了までの時間
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(LocalDateTime targetDateFrom, String patternStr,
            Duration duration, Duration addition) {

        return createTargetDateTimeStrList(targetDateFrom, targetDateFrom.plus(addition),
                DateTimeFormatter.ofPattern(patternStr), duration);
    }

    /**
     * 対象日時文字列Listを生成.
     *
     * @param targetDateFrom 対象日時(FROM)
     * @param targetDateTo 対象日時(TO)
     * @param pattern 日時パターン
     * @param amountToAdd 加算量
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(LocalDateTime targetDateFrom, LocalDateTime targetDateTo,
            DateTimeFormatter pattern, TemporalAmount amountToAdd) {

        List<String> targetDateStrList = new ArrayList<>();
        LocalDateTime targetDate = targetDateFrom;
        while (targetDate.compareTo(targetDateTo) <= 0) {
            targetDateStrList.add(pattern.format(targetDate));
            targetDate = targetDate.plus(amountToAdd);
        }

        return targetDateStrList;
    }

}
