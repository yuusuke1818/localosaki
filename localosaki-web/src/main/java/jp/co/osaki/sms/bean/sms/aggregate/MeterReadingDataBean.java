package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.TMeterReadingDownloadReservationInfo;
//import jp.co.osaki.osol.utility.BuildingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.Condition;
import jp.co.osaki.sms.dao.InspectionMeterSrvBulkDao;
import jp.co.osaki.sms.dao.MeterReadingDownloadReservationDao;
import jp.co.osaki.sms.dao.PrevYearInspectionMeterSrvDao;
import jp.co.osaki.sms.dao.TDayLoadSurveyMaxDemandDao;
import jp.co.osaki.sms.resultset.InspectionMeterSrvBulkResultSet;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

/**
 * 検針データダウンロード
 *
 */
@Named(value = "smsAggregateMeterReadingDataBean")
@ConversationScoped
public class MeterReadingDataBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

	private static final long serialVersionUID = -3887087332579567859L;

	private Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());


	@EJB
	InspectionMeterSrvBulkDao inspectionMeterSrvBulkDao;

	@EJB
	MeterReadingDownloadReservationDao meterReadingDownloadReservationDao;

	@EJB
	PrevYearInspectionMeterSrvDao prevYearInspectionMeterSrvDao;

	@EJB
	TDayLoadSurveyMaxDemandDao tDayLoadSurveyMaxDemandDao;

    @Inject
    private SmsFileDownload fileDownloader;

	// 検針データダウンロードの検索条件保持
	@Inject
	private MeterReadingDataBeanProperty meterReadingDataBeanProperty;

	@Inject
	private MeterReadingDataPagingList pagingList;

	@Inject
	private ReservationStatusDataPagingList statusPagingList;


	// 検索条件毎の指定可能上限値
	// Key:条件、Value:上限値
	private Map<String, Integer> conditionLimitCountMap;

	// 検針種別
	// Key:検針種別名(自動,任意,予約,定期,臨時)、Value:検針種別(a,m,s,r,t)
	private Map<String, String> inspTypeMap;

	@Inject
    private SmsMessages beanMessages;

	// バリデーションチェックの項目
	private List<String> invalidComponent;

    // 開始日
    private String startDate;
    // 終了日
    private String endDate;

    // 最大デマンド出力
    private boolean outputMaxDemand;
    // 前年同月出力
    private boolean outputPrivYearAndSameMonth;

    // 検針種別選択値
    private String inspType;

    public Map<String, String> getInspTypeMap() {
		return inspTypeMap;
	}

	public void setInspTypeMap(Map<String, String> inspTypeMap) {
		this.inspTypeMap = inspTypeMap;
	}

	public String getInspType() {
		return inspType;
	}

	public void setInspType(String inspType) {
		this.inspType = inspType;
	}

	public boolean isOutputMaxDemand() {
		return outputMaxDemand;
	}

	public void setOutputMaxDemand(boolean outputMaxDemand) {
		this.outputMaxDemand = outputMaxDemand;
	}

	public boolean isOutputPrivYearAndSameMonth() {
		return outputPrivYearAndSameMonth;
	}

	public void setOutputPrivYearAndSameMonth(boolean outputPrivYearAndSameMonth) {
		this.outputPrivYearAndSameMonth = outputPrivYearAndSameMonth;
	}

	public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Date getCalStartDate() {
        Date ret = null;
        if (this.startDate != null) {
            ret = DateUtility.conversionDate(this.startDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
        return ret;
    }

    public void setCalStartDate(Date calStartDate) {
        if (calStartDate != null) {
            this.startDate = DateUtility.changeDateFormat(calStartDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
    }

    public Date getCalEndDate() {
        Date ret = null;
        if (this.endDate != null) {
            ret = DateUtility.conversionDate(this.endDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
        return ret;
    }

    public void setCalEndDate(Date calEndDate) {
        if (calEndDate != null) {
            this.endDate = DateUtility.changeDateFormat(calEndDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
    }


	public MeterReadingDataBeanProperty getMeterReadingDataBeanProperty() {
		return meterReadingDataBeanProperty;
	}

	public List<Condition> getConditionList() {
		return meterReadingDataBeanProperty.getConditionList(this);
	}

	public Map<String, Integer> getConditionLimitCountMap() {
		return conditionLimitCountMap;
	}

	public void setConditionLimitCountMap(Map<String, Integer> conditionLimitCountMap) {
		this.conditionLimitCountMap = conditionLimitCountMap;
	}

	public MeterReadingDataPagingList getPagingList() {
		return pagingList;
	}

	public ReservationStatusDataPagingList getStatusPagingList() {
		return statusPagingList;
	}

	/** DB検索結果を保存 */
	private List<InspectionMeterSrvBulkResultSet> records;

	public List<InspectionMeterSrvBulkResultSet> getRecords() {
		return records;
	}

	public void setRecords(List<InspectionMeterSrvBulkResultSet> records) {
		this.records = records;
	}

	private String downloadFilePath;

	public String getDownloadFilePath() {
		return downloadFilePath;
	}

	public void setDownloadFilePath(String downloadFilePath) {
		this.downloadFilePath = downloadFilePath;
	}

	private Integer incompleteReservationCnt = 9999999;

//	private String csvFileName;
//
//	public String getCsvFileName() {
//		return csvFileName;
//	}
//
//	public void setCsvFileName(String csvFileName) {
//		this.csvFileName = csvFileName;
//	}


//	/** 参照用DB検索結果を保存（ */
//    private List<TMeterReadingDownloadReservationInfo> referedRecords;
//
//	public List<TMeterReadingDownloadReservationInfo> getReferedRecords() {
//		return referedRecords;
//	}
//
//	public void setReferedRecords(List<TMeterReadingDownloadReservationInfo> referedRecords) {
//		this.referedRecords = referedRecords;
//	}



	/**
	 * 画面初期表示
	 */
	@Override
    @Logged
	public String init() {

		conversationStart();

		setStartDate("");
		setEndDate("");
		setOutputMaxDemand(false);
		setOutputPrivYearAndSameMonth(false);

		getMeterReadingDataBeanProperty().resetConditionList();

		// 検索条件毎の指定可能上限数を設定
		Map<String, Integer> conditionLimitCountMap = new HashMap<>();

		// [建物番号または建物名]条件は10+1個まで指定可能
		conditionLimitCountMap.put( //
				OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY, //
				OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT);
		setConditionLimitCountMap(conditionLimitCountMap);

		// 検針種別をMapに設定
		inspTypeMap = new LinkedHashMap<String, String>();
		inspTypeMap.put("自動", "a");
		inspTypeMap.put("任意", "m");
		inspTypeMap.put("予約", "s");
		inspTypeMap.put("定期", "r");
		inspTypeMap.put("臨時", "t");

		setInspType("a"); // デフォルトは自動

		meterReadingDataBeanProperty.setSearchedFlg(false);

		meterReadingDataBeanProperty.setSearchResultDisabled(true);
		meterReadingDataBeanProperty.setSearchResultStyle("gradation_type_1");

		meterReadingDataBeanProperty.setReservationStatusFlg(false);

		meterReadingDataBeanProperty.setReservationStatusDisabled(false);
		meterReadingDataBeanProperty.setReservationStatusStyle("gradation_type_3");



		// 大崎、もしくはパートナー企業の場合
		if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(this.getLoginCorpType()) ||
				OsolConstants.CORP_TYPE.PARTNER.getVal().equals(this.getLoginCorpType())) {
			meterReadingDataBeanProperty.setViewCorpIdAndName(true);
		}
		else {
			meterReadingDataBeanProperty.setViewCorpIdAndName(false);
		}

		changeColumnClassesStr();

		this.invalidComponent = new ArrayList<>();

		return "meterReadingDataAggregate";
	}

	/**
	 * 検索ボタン押下
	 */
	public String search() {
        // アクセスログ出力
        exportAccessLog("search", "ボタン「検索開始」押下");

		setRecords(new ArrayList<InspectionMeterSrvBulkResultSet>());

        // 入力値チェック
        if (!inputCheck()) {

        	// 入力に問題があった場合は表示しているリストをクリア
        	pagingList.init(new ArrayList<MeterReadingData>());

        	meterReadingDataBeanProperty.setSearchedFlg(false);

        	return STR_EMPTY;
        }
        else {

        	eventLogger.info("------------------------------------------- 検索開始 -------------------------------------------");

        	setRecords(searchMeterReadingDatas());

    		// 入力に問題が無ければ検索実行
    		pagingList.init(displayMeterReadingDatas(getRecords()));

    		eventLogger.info("------------------------------------------- 検索終了 -------------------------------------------");

    		meterReadingDataBeanProperty.setSearchedFlg(true);

    		meterReadingDataBeanProperty.setSearchResultDisabled(true);
    		meterReadingDataBeanProperty.setSearchResultStyle("gradation_type_1");

    		meterReadingDataBeanProperty.setReservationStatusFlg(false);

    		meterReadingDataBeanProperty.setReservationStatusDisabled(false);
    		meterReadingDataBeanProperty.setReservationStatusStyle("gradation_type_3");

    		return "meterReadingDataAggregate";
        }
	}

	/**
	 * 予約ダウンロードボタン押下
	 */
	public String reservationStart() {
        // アクセスログ出力
        exportAccessLog("reservationStart", "ボタン「予約ダウンロード」押下");

        eventLogger.info("reservationStart called.");

		// 予約テーブルにレコードを追加
		// 検索条件を各メソッドから取得
		Map<String, List<Object>> paramMap = new HashMap<String, List<Object>>();
		setLoginInfoParams(paramMap);
		setPeriodParams(paramMap);
		setInspTypeParams(paramMap);
		setConditionParams(paramMap);

		MeterReadingDataSearchCondition text = new MeterReadingDataSearchCondition();

		text.loginPersonType = paramMap.get("loginPersonType").toString().replace("[", "").replace("]", "");
		text.loginCorpId = paramMap.get("loginCorpId").toString().replace("[", "").replace("]", "");
		text.loginPersonId = paramMap.get("loginPersonId").toString().replace("[", "").replace("]", "");
		text.loginCorpType = paramMap.get("loginCorpType").toString().replace("[", "").replace("]", "");
		text.personAuths = paramMap.get("personAuths").toString().replace("[", "").replace("]", "");
		text.inspTypes = paramMap.get("inspTypes").toString().replace("[", "").replace("]", "");
		text.endDate = paramMap.get("endDate").toString().replace("[", "").replace("]", "");
		text.startDate = paramMap.get("startDate").toString().replace("[", "").replace("]", "");
		text.corpParam = String.valueOf(paramMap.get("corpParam")).replace("[", "").replace("]", "");
		text.buildingParam = String.valueOf(paramMap.get("buildingParam")).replace("[", "").replace("]", "");
		text.outputMaxDemand = String.valueOf(isOutputMaxDemand());
		text.outputPrivYearAndSameMonth = String.valueOf(isOutputPrivYearAndSameMonth());

		Gson gson = new Gson();
		String json = gson.toJson(text);


		// DBへの登録内容
		TMeterReadingDownloadReservationInfo reserveInfo = new TMeterReadingDownloadReservationInfo();
		reserveInfo.setCorpId(this.getLoginPerson().getId().getCorpId());
		reserveInfo.setPersonId(this.getLoginPerson().getId().getPersonId());
		Timestamp serverTime = meterReadingDownloadReservationDao.getSvDate();
		reserveInfo.setReservationDate(serverTime);
		reserveInfo.setDelFlg(OsolConstants.FLG_OFF);
		reserveInfo.setSearchCondition(json.toString());
		reserveInfo.setProcessStatus("処理待ち");
		reserveInfo.setProcessResult("未完了");


		// 既存レコードに同じ検索条件のものがあるか調べる
		List<TMeterReadingDownloadReservationInfo> compareList = searchReservationStatusDatas();
		List<TMeterReadingDownloadReservationInfo> sameConditionRecords = new ArrayList<TMeterReadingDownloadReservationInfo>();
		Boolean isNewConditionRecord = true;

		for(TMeterReadingDownloadReservationInfo compareRecord : compareList) {
			String condition1 = compareRecord.getSearchCondition();
			String condition2 = reserveInfo.getSearchCondition();
			// 検索条件を比較する
			if(condition2.equals(condition1)) {
				isNewConditionRecord = false;
				sameConditionRecords.add(compareRecord);
				System.out.println(condition1);
				System.out.println(condition2);
			}
		}

        // 新規登録
		if(isNewConditionRecord) {
			meterReadingDownloadReservationDao.persist(reserveInfo);
		}
		// 既存レコードを上書き
		else {
			System.out.println("マージ！");
			TMeterReadingDownloadReservationInfo sameConditionRecord = sameConditionRecords.get(0);
			sameConditionRecord.setReservationDate(reserveInfo.getReservationDate());
			sameConditionRecord.setStartDate(null);
			sameConditionRecord.setEndDate(null);
			sameConditionRecord.setProcessStatus("処理待ち");
			sameConditionRecord.setProcessResult("未完了");
			meterReadingDownloadReservationDao.merge(sameConditionRecord);

		}


		meterReadingDataBeanProperty.setSearchedFlg(false);
		meterReadingDataBeanProperty.setSearchResultDisabled(false);
		meterReadingDataBeanProperty.setSearchResultStyle("gradation_type_3");

		meterReadingDataBeanProperty.setReservationStatusFlg(true);

		meterReadingDataBeanProperty.setReservationStatusDisabled(true);
		meterReadingDataBeanProperty.setReservationStatusStyle("gradation_type_1");

		statusPagingList.init(displayReservationStatusDatas(searchReservationStatusDatas()));

		return "meterReadingData?faces-redirect=true";    //対応するxhtmlファイル名（meterReadingData.xhtml）
	}


	/**
	 * 予約数チェック
	 * 処理結果（process_resault）が未完了のレコードがあった場合、新しい予約は出来ないようにする
	 */

	public boolean countIncompleteReservation() {

		List<TMeterReadingDownloadReservationInfo> resultList = searchReservationDatasForCount();
		incompleteReservationCnt = resultList.size();

		if(incompleteReservationCnt == 0){
			return true;
		}
		else {
			addErrorMessage(beanMessages.getMessage("smsAggregateMeterReadingDataBean.error.incompreteReservationExists"));
		}
		return false;
	}




	/**
	 * 検索結果表示ボタン押下
	 */
	public String searchResultDisplay() {

		System.out.println("searchResultDisplay called.");

		meterReadingDataBeanProperty.setSearchedFlg(true);

		meterReadingDataBeanProperty.setSearchResultDisabled(true);
		meterReadingDataBeanProperty.setSearchResultStyle("gradation_type_1");

		meterReadingDataBeanProperty.setReservationStatusFlg(false);

		meterReadingDataBeanProperty.setReservationStatusDisabled(false);
		meterReadingDataBeanProperty.setReservationStatusStyle("gradation_type_3");


		return "meterReadingDataAggregate";
	}

	/**
	 * 予約状況表示ボタン押下
	 */
	public String reservationStatusDisplay() {

		System.out.println("reservationStatusDisplay called.");

		meterReadingDataBeanProperty.setSearchedFlg(false);

		meterReadingDataBeanProperty.setSearchResultDisabled(false);
		meterReadingDataBeanProperty.setSearchResultStyle("gradation_type_3");

		meterReadingDataBeanProperty.setReservationStatusFlg(true);

		meterReadingDataBeanProperty.setReservationStatusDisabled(true);
		meterReadingDataBeanProperty.setReservationStatusStyle("gradation_type_1");

		statusPagingList.init(displayReservationStatusDatas(searchReservationStatusDatas()));

		return "meterReadingDataAggregate";
	}

	/**
	 * 予約状況更新ボタン押下
	 */
	public String statusReload() {

		System.out.println("statusReload called.");

		statusPagingList.init(displayReservationStatusDatas(searchReservationStatusDatas()));

		return "meterReadingDataAggregate";
	}

	/**
	 * ダウンロード(ダウンロードボタン押下⇒CSVファイル生成後に呼び出し)
	 */
	public String download(Long reservationId) {

		TMeterReadingDownloadReservationInfo downloadRecord = new TMeterReadingDownloadReservationInfo();
		downloadRecord.setReservationId(reservationId);

		TMeterReadingDownloadReservationInfo res = meterReadingDownloadReservationDao.find(downloadRecord);

		String downloadFilePath = res.getOutputFilePath();
		String downloadFileName = res.getOutputFileName();

		eventLogger.info("------------------------------------------- ダウンロード開始 -------------------------------------------");
		eventLogger.info("------------------------------------------- download downloadFilePath:" + downloadFilePath + " -------------------------------------------");

        // ダウンロードファイル作成
        fileDownloader.fileDownload(downloadFilePath, downloadFileName);


        eventLogger.info("------------------------------------------- ダウンロード終了 -------------------------------------------");

        return STR_EMPTY;
	}

    /**
     * 入力値チェック
     * @return
     */
    private boolean inputCheck() {

        boolean ret = true;
        invalidComponent.clear();

        if (CheckUtility.isNullOrEmpty(startDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notInputStartDate"));
            invalidComponent.add("smsAggregateDailyBean:calStartText");
        } else if (!CheckUtility.checkRegDateYmd(startDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notFormatStartDate"));
            invalidComponent.add("smsAggregateDailyBean:calStartText");
        }
        if (CheckUtility.isNullOrEmpty(endDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notInputEndDate"));
            invalidComponent.add("smsAggregateDailyBean:calEndText");
        } else if (!CheckUtility.checkRegDateYmd(endDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notFormatEndDate"));
            invalidComponent.add("smsAggregateDailyBean:calEndText");
        }
        if (ret) {

        	Date dStart = DateUtility.conversionDate(startDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        	Date dEnd = DateUtility.conversionDate(endDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);

        	// 開始日付に1ヵ月足した日付
            Date dChk = DateUtility.plusMonth(dStart, 1);

            // 日付が逆転していないか判定
            // 最大1ヵ月の期間を超えていないか判定
            if (dStart.compareTo(dEnd) > 0 || dChk.compareTo(dEnd) <= 0) {
                ret = false;
                addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notRangeDayMaxOneMonth"));
                invalidComponent.add("smsAggregateDailyBean:calEndText");
            }
        }

        return ret;
    }


	/**
	 * DBからの抽出結果をグルーピング、及びソートし画面表示用の情報として返却。
	 *
	 * @param searchResult DB抽出結果
	 */
	private List<MeterReadingData> displayMeterReadingDatas(List<InspectionMeterSrvBulkResultSet> searchResult) {

		List<MeterReadingData> meterReadingDatas = new ArrayList<MeterReadingData>();

		// 以下の順でグルーピング
		// 1.企業ID
		// 2.建物番号(テナントの場合、入っている建物)
		// 3.検針連番(検針年+検針月+検針連番+検針種別)
		// 4.検針日時(yyyyMMddHHmm)
		Map<String, Map<String, Map<String, Map<String, List<InspectionMeterSrvBulkResultSet>>>>> corpGroupingMap =
				searchResult.stream() //
				.collect(Collectors.groupingBy( // 企業IDでgrouping
						InspectionMeterSrvBulkResultSet::getCorpId,
						Collectors.groupingBy( // 建物番号でgrouping
								InspectionMeterSrvBulkResultSet::getBuildingNo,
								Collectors.groupingBy( // 検針連番でgrouping
										e -> e.getInspYear() + "-" + StringUtils.leftPad(e.getInspMonth(), 2, "0") + "-" + StringUtils.leftPad(e.getInspMonthNo().toString(), 3, "0") + "-" + e.getInspType(),
										Collectors.groupingBy( // 検針日時でgrouping
												e -> DateUtility.changeDateFormat(e.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM))))));

		// 企業IDのソート
		List<String> corpIds =corpGroupingMap.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());

		for (String corpId : corpIds) {

			Map<String, Map<String, Map<String, List<InspectionMeterSrvBulkResultSet>>>> buildingGroupingMap = corpGroupingMap.get(corpId);

			// 建物番号のソート
			List<String> buildingNos = buildingGroupingMap.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

			for (String buildingNo : buildingNos) {

				Map<String, Map<String, List<InspectionMeterSrvBulkResultSet>>> inspGroupMap = buildingGroupingMap.get(buildingNo);

				// 検針連番(検針年+検針月+検針連番+検針種別)のソート
				List<String> inspNos = inspGroupMap.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

				for (String inspNo : inspNos) {

					Map<String, List<InspectionMeterSrvBulkResultSet>> inspDateGroupMap = inspGroupMap.get(inspNo);

					// 検針日時(yyyyMMddHHmm)のソート
					List<String> inspDates = inspDateGroupMap.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

					for (String inspDate : inspDates) {

						InspectionMeterSrvBulkResultSet record = inspDateGroupMap.get(inspDate).get(0);

						MeterReadingData data = new MeterReadingData();
						data.setCorpId(record.getCorpId());
						data.setCorpName(record.getCorpName());
						data.setBuildingId(record.getBuildingId());
						data.setBuildingNo(record.getBuildingNo());
						data.setBuildingName(record.getBuildingName());
						data.setMeterReadingSerialNumber(StringUtils.leftPad(record.getInspMonthNo().toString(), 3, "0") + record.getInspType());
						data.setInspTypeName(getInspTypeName(record.getInspType()));
						data.setMeterReadingDate(DateUtility.changeDateFormat(record.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
						//data.setTenantFlg(BuildingUtility.isTenant(record.getBuildingType()));
						data.setTenantFlg(false);

						meterReadingDatas.add(data);
					}
				}
			}
		}

		return meterReadingDatas;
	}

	/**
	 * DBからのダウンロード予約抽出結果を画面表示用の情報として返却。
	 *
	 * @param reservationStatus DB抽出結果
	 */
	private List<ReservationStatusData> displayReservationStatusDatas(List<TMeterReadingDownloadReservationInfo> reservationStatus) {

		// 日付順にソート
		List<TMeterReadingDownloadReservationInfo> sortedReservationStatus = reservationStatus.stream()
																.sorted(Comparator.comparing(TMeterReadingDownloadReservationInfo::getReservationDate,Comparator.reverseOrder()))
																.collect(Collectors.toList());

		// reservationStatusからreservationStatusDatasに表示用に値を編集しながら詰める
		List<ReservationStatusData> reservationStatusDatas = new ArrayList<ReservationStatusData>();

		for (TMeterReadingDownloadReservationInfo info : sortedReservationStatus) {

			ReservationStatusData status = new ReservationStatusData();

			// 予約時刻
			status.setReservationDate(DateUtility.changeDateFormat(info.getReservationDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));

			// 処理開始時刻
			status.setStartDate(info.getStartDate() == null ? null : DateUtility.changeDateFormat(info.getStartDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH) + "～");

			// 処理終了時刻
			status.setEndDate(info.getEndDate() == null ? null : DateUtility.changeDateFormat(info.getEndDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));

			// 予約者
			status.setReservationHolder(info.getPersonId().toString());

			// 出力条件
			String searchConditionText = info.getSearchCondition();
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			Map<String, String> map = gson.fromJson(searchConditionText, type);


			String Date = map.get("startDate") + "～" + map.get("endDate");
			String inspType = map.get("inspTypes");
			String inspTypeName = getInspTypeName(inspType);

			String maxDemand = map.get("outputMaxDemand");
			String prevYearData = map.get("outputPrivYearAndSameMonth");

			String corpParam = map.get("corpParam");
            String buildingParam = map.get("buildingParam");


			status.setSearchDate(Date);
			status.setInspTypeName(inspTypeName);
			status.setMaxDemand(maxDemand.equals("true") ? "✔" : null);
			status.setPrevYearData(prevYearData.equals("true") ? "✔" : null);
			status.setCorpParam(corpParam.equals("null") ? "  ・全企業" : "  ・名前またはIDに「" + corpParam + "」を含む企業");
			status.setBuildingParam(buildingParam.equals("null") ? "  ・全建物" : "  ・番号またはIDに「" + buildingParam + "」を含む建物");


			// 処理状況
			status.setStatus(info.getProcessStatus().toString());

			// 処理結果
			status.setResult(info.getProcessResult() == null ? null : info.getProcessResult().toString());

			// ダウンロードボタン表示用フラグ
			status.setFlg(info.getProcessResult().equals("完了") ? true : false);

			// download()に渡す用に主キー(予約ID)Set
			status.setReservationId(info.getReservationId());

			reservationStatusDatas.add(status);
		}

		return reservationStatusDatas;
	}

	/**
	 * 検針種別Mapから検針種別コードを条件に名称を取得。
	 *
	 * @param inspType 検針種別コード
	 * @return 検針種別名
	 */
	private String getInspTypeName(String inspType) {

		for (Entry<String, String> insp : inspTypeMap.entrySet()) {

			String inspTypeName = insp.getKey();
			String instTypeCode = insp.getValue();

			if (instTypeCode.equals(inspType)) {
				return inspTypeName;
			}
		}

		return StringUtils.EMPTY;
	}


	/**
	 * 入力された条件より検針データを検索。
	 */
	private List<InspectionMeterSrvBulkResultSet> searchMeterReadingDatas() {

		Map<String, List<Object>> parameterMap = new HashMap<String, List<Object>>();

		// ログイン情報を検索条件に設定
		setLoginInfoParams(parameterMap);

		// 期間指定を検索条件に設定
		setPeriodParams(parameterMap);

		// 検針種別を検索条件に設定
		setInspTypeParams(parameterMap);

		// 企業、建物の入力内容を検索条件に設定
		setConditionParams(parameterMap);

		eventLogger.info("searchMeterReadingDatas called. " + parameterMap);

		return inspectionMeterSrvBulkDao.getInspectionMeterSrvs(parameterMap);
	}

	/**
	 * 予約情報を検索。
	 */
	private List<TMeterReadingDownloadReservationInfo> searchReservationStatusDatas() {

		Map<String, List<Object>> parameterMap = new HashMap<String, List<Object>>();

		// 検索条件をMapにつめる
		parameterMap.put("searchCorpId", Arrays.asList(this.getLoginPerson().getId().getCorpId()));

		// 権限があればここで一緒に詰める
		parameterMap.put("searchPersonId", Arrays.asList(this.getLoginPersonId()));


		return meterReadingDownloadReservationDao.getVisibleReservationInfo(parameterMap);
	}

	/**
	 * 予約制限のために未完了の予約情報を検索。
	 */
	private List<TMeterReadingDownloadReservationInfo> searchReservationDatasForCount() {

		Map<String, List<Object>> parameterMap = new HashMap<String, List<Object>>();

		// 検索条件をMapにつめる
		parameterMap.put("searchCorpId", Arrays.asList(this.getLoginPerson().getId().getCorpId()));

		// 権限があればここで一緒に詰める・・・
		parameterMap.put("searchPersonId", Arrays.asList(this.getLoginPersonId()));

		//
		parameterMap.put("searchResult", Arrays.asList("未完了"));

		return meterReadingDownloadReservationDao.getVisibleReservationInfo(parameterMap);
	}




	/**
	 * ログイン情報から必要な情報を集めパラメータに設定
	 */
	private void setLoginInfoParams(Map<String, List<Object>> parameterMap) {

		// AuthorityFlgが有効なAuthorityCdを取得
		List<String> personAuths = this.getLoginCorpPersonAuthListAll().stream() //
				.filter(mCorpPersonAuth -> mCorpPersonAuth.getAuthorityFlg() == OsolConstants.FLG_ON) //
				.map(personAuth -> personAuth.getMAuth().getAuthorityCd()) //
				.collect(Collectors.toList());

        // 企業ID
        parameterMap.put("loginCorpId", Arrays.asList(this.getLoginCorpId()));
        // 担当者ID
        parameterMap.put("loginPersonId", Arrays.asList(this.getLoginPersonId()));
        // 企業種別
        // 0:大崎、1:パートナー企業、3:契約企業
        parameterMap.put("loginCorpType", Arrays.asList(this.getLoginCorpType()));
        // 担当者種別
        // 0:管理者、1:担当者
        parameterMap.put("loginPersonType", Arrays.asList(this.getLoginPerson().getPersonType()));
        // 担当者権限
        // SMS権限:012、OCR権限:013
        parameterMap.put("personAuths", Arrays.asList(personAuths));
	}

	/**
	 * 期間指定を検索条件に編集しパラメータに設定
	 */
	private void setPeriodParams(Map<String, List<Object>> parameterMap) {

		parameterMap.put("startDate", Arrays.asList(startDate));
		parameterMap.put("endDate", Arrays.asList(endDate));
	}

	/**
	 * 検針種別を検索条件に編集しパラメータに設定
	 */
	private void setInspTypeParams(Map<String, List<Object>> parameterMap) {

		List<Object> inspTypes = new ArrayList<Object>();

		inspTypes.add(inspType);

		parameterMap.put("inspTypes", inspTypes);
	}

	/**
	 * 企業、建物の入力内容を検索条件に編集しパラメータに設定
	 */
	private void setConditionParams(Map<String, List<Object>> parameterMap) {

        List<Condition> conditionList = meterReadingDataBeanProperty.getConditionList(this);

        if (conditionList != null) {

            List<Object> targetCorpIdOrNameList = new ArrayList<>();
            List<Object> targetBuildingNoOrNameList = new ArrayList<>();

            for (Condition condition : conditionList) {
                if (condition == null
                        || condition.getSelectConditionCd() == null
                        || condition.getSelectConditionCd().equals(OsolConstants.DEFAULT_SELECT_BOX_VALUE)) {
                    continue;
                }

                switch (condition.getSelectConditionCd()) {
                case OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME:
                    // 企業(IDまたは名)検索
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetCorpIdOrNameList.add(condition.getConditionKeyword());
                    }
                    break;
                case OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY:
                    // 建物番号または建物名検索
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetBuildingNoOrNameList.add(condition.getConditionKeyword());
                    }
                    break;
                default:
                    break;
                }
            }

            if (!targetCorpIdOrNameList.isEmpty()) {
            	parameterMap.put("corpParam", targetCorpIdOrNameList);
            }
            if (!targetBuildingNoOrNameList.isEmpty()) {
            	parameterMap.put("buildingParam", targetBuildingNoOrNameList);
            }
        }
	}

	// ---- 検索条件関係 -------------------------------------------------------------------------------------

	/**
	 * 最初に選択済み状態で表示しておく条件を設定
	 */
	@Override
	public void initialConditionList(List<Condition> list) {
//		list.addAll(createFixedConditionList());
	}

	/**
	 * 未選択状態の条件（「条件を追加」）の作成
	 */
	@Override
	public Condition createDefaultCondition() {

		// 未選択状態の条件（「条件を追加」）
		Condition condition = new Condition();
		condition.setSelectEnable(true);
		condition.setSearchSubjectConjunctionEnable(false);
		condition.setDeleteButtonEnable(false);
		condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);

		return condition;
	}

	/**
	 * 選択した条件による表示アイテムの更新
	 */
	@Override
	public void updateCondition(Condition condition) {

		// 一致条件表示
		condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
		condition.setSelectEnable(false);
		condition.setSearchSubjectConjunctionEnable(true);
		condition.setDeleteButtonEnable(true);

		// 選ばれた検索条件によって表示するコンポーネントを変更する
		switch (condition.getSelectConditionCd()) {
		case (OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY):
			condition.setKeywordSelectEnable(true);
			condition.setMultiSelectEnable(true); // 複数選択可能
			break;
		default:
			// キーワード検索
			condition.setKeywordSelectEnable(true);
			condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
			break;
		}
	}

	/**
	 * 選択肢の一覧
	 */
	@Override
	public Map<String, String> initialConditionMap() {

		// 選択肢の設定
		Map<String, String> selectConditionMap = new LinkedHashMap<>();

		// 未設定
		selectConditionMap.put( //
				OsolConstants.DEFAULT_SELECT_BOX_KEY, //
				OsolConstants.DEFAULT_SELECT_BOX_VALUE);

		for (String selectCondition : createSelectConditionList()) {
			selectConditionMap.put(selectCondition, selectCondition);
		}

		return selectConditionMap;
	}

	/**
	 * 固定表示の検索条件を生成
	 */
	private List<Condition> createFixedConditionList() {

		// 建物(番号または名)
		Condition condition = new Condition();
		condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
		condition.setDeleteButtonEnable(true);
		condition.setKeywordSelectEnable(true);
		condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);

		List<Condition> fixedConditionList = new ArrayList<>();
		fixedConditionList.add(condition);

		return fixedConditionList;
	}

	/**
	 * 検索条件のリストを生成
	 * ・企業(IDまたは名)
	 * ・建物番号または建物名
	 */
	private List<String> createSelectConditionList() {

		List<String> selectConditionList = new ArrayList<>();

		// 大崎、パートナー企業の場合、検索条件に企業を追加
		if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(this.getLoginCorpType()) ||
				OsolConstants.CORP_TYPE.PARTNER.getVal().equals(this.getLoginCorpType())) {

			selectConditionList.add(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
		}
		selectConditionList.add(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);

		return selectConditionList;
	}

	/**
	 * 検索条件の追加
	 */
	public void changeConditionCd(String orderNo) {
        // アクセスログ出力
        exportAccessLog("changeConditionCd", "「検索条件」選択");

		meterReadingDataBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
		controlConditionMap(getConditionLimitCountMap(), meterReadingDataBeanProperty.getConditionList(this));
	}

	/**
	 * 検索条件の削除
	 */
	public void deleteCondition(String orderNo) {
        // アクセスログ出力
        exportAccessLog("deleteCondition", "検索条件にてボタン「削除」押下");

		meterReadingDataBeanProperty.deleteCondition(this, orderNo);
		controlConditionMap(getConditionLimitCountMap(), meterReadingDataBeanProperty.getConditionList(this));
	}

	/**
	 * 検索条件プルダウンMapを制御
	 */
	private void controlConditionMap(Map<String, Integer> conditionLimitCountMap, List<Condition> conditionList) {
		Set<Entry<String, Integer>> conditionLimitCountMapEntrySet = conditionLimitCountMap.entrySet();
		for (Entry<String, Integer> conditionLimitCountMapEntry : conditionLimitCountMapEntrySet) {
			String targetSelectConditionCd = conditionLimitCountMapEntry.getKey();

			int conditionLimitCount = conditionLimitCountMapEntry.getValue();
			int conditionCount = 0;
			boolean limitCountFlg = false;
			for (Condition condition : conditionList) {
				if (targetSelectConditionCd.equals(condition.getSelectConditionCd())) {
					// 当該検索条件の指定数を取得
					conditionCount++;
					if (conditionCount >= conditionLimitCount) {
						limitCountFlg = true;
						break;
					}
				}
			}
			for (Condition condition : conditionList) {
				if (targetSelectConditionCd.equals(condition.getSelectConditionCd())) {

					condition.setLimitCountFlg(limitCountFlg);
				}
			}
		}

		meterReadingDataBeanProperty.updateCondition(this, conditionList.size() - 1);

		int allSelectConditionLimitCount = 0;
		List<String> selectConditionList = createSelectConditionList();
		for (String selectConditionCd : selectConditionList) {
			if (conditionLimitCountMap.containsKey(selectConditionCd)) {
				allSelectConditionLimitCount += conditionLimitCountMap.get(selectConditionCd);
			} else {
				allSelectConditionLimitCount++;
			}
		}

		if ((conditionList.size() - (createFixedConditionList().size() + 1)) >= allSelectConditionLimitCount) {

			// 全ての検索条件の指定数が上限に達している場合
			conditionList.remove(conditionList.size() - 1);
		} else {

			boolean isExistDefaultCondition = false;
			for (Condition condition : conditionList) {
				if (condition.isDefaultCondition()) {
					isExistDefaultCondition = true;
					break;
				}
			}

			if (!isExistDefaultCondition) {
				conditionList.add(createDefaultCondition());
				meterReadingDataBeanProperty.resetOrder();
				meterReadingDataBeanProperty.updateConditionMap(this, conditionList.size() - 1);
			}
		}
	}


	/**
     * 予約ダウンロードボタン押下時の確認ダイアログメッセージ
     * @param flg 確認メッセージ
     * @return メッセージ
     */
    public String getBeforeReservationMessage(Long flg) {
    	if(flg == 1) {
            return beanMessages.getMessage("smsAggregateMeterReadingDataBean.beforeReservation.text")+beanMessages.getMessage("smsAggregateMeterReadingDataBean.beforeReservation.confirm");
        }
    	else {
    		return "";
    	}
    }










	// ---- ページング関係 -------------------------------------------------------------------------------

	/**
	 * 前ページ
	 */
	public String prevPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("prevPage", "ページング「前へ」押下");

		pagingList.prevPage();
		return STR_EMPTY;
	}

	/**
	 * 先ページ
	 */
	public String nextPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("nextPage", "ページング「後へ」押下");

		pagingList.nextPage();
		return STR_EMPTY;
	}

	/**
	 * ページ遷移
	 */
	public String pageJump() {
        // アクセスログ出力
        exportAccessLog("pageJump", "ページングリンク押下");

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String param = params.get("page");
		if (param != null) {
			pagingList.setCurrentPage(Integer.valueOf(param));
		}
		return STR_EMPTY;
	}

	/**
	 * 前ページ
	 */
	public String prevStatusPage(ActionEvent event) {
		statusPagingList.prevPage();
		return STR_EMPTY;
	}

	/**
	 * 先ページ
	 */
	public String nextStatusPage(ActionEvent event) {
		statusPagingList.nextPage();
		return STR_EMPTY;
	}

	/**
	 * ページ遷移
	 */
	public String statusPageJump() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String param = params.get("statusPage");
		if (param != null) {
			statusPagingList.setCurrentPage(Integer.valueOf(param));
		}
		return STR_EMPTY;
	}

	// ---- CSS操作関係 -------------------------------------------------------------------------------------

	/**
	 * 画面表示用カラムのcss定義の切替え
	 */
	public void changeColumnClassesStr() {

		String columnClassesStr = "";

		// 大崎、もしくはパートナー企業の場合
		if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(this.getLoginCorpType()) ||
				OsolConstants.CORP_TYPE.PARTNER.getVal().equals(this.getLoginCorpType())) {
			columnClassesStr += "column_style_corp,";
		}
		columnClassesStr += "column_style_tenant_flg,";
		columnClassesStr += "column_style_building,";
		columnClassesStr += "column_style_meter_reading_date,";
		columnClassesStr += "column_style_meter_reading_serial_number,";
		columnClassesStr += "column_style_insp_type_name";

		meterReadingDataBeanProperty.setColumnClassesStr(columnClassesStr);
	}

	/**
	 * 検索条件入力のバリデーションエラー時の上書きスタイルを返す
	 */
	@Override
	public String getInvalidStyle(String id) {
		if (invalidComponent != null && invalidComponent.contains(id)) {
			return OsolConstants.INVALID_STYLE;
		}
		return super.getInvalidStyle(id);
	}


//    public void initErrorMessages() {
//        if (beanMessages.getMessages() == null) {
//            setErrorMessages(new ArrayList<>());
//        } else {
//            getErrorMessages().clear();
//        }
//    }




}
