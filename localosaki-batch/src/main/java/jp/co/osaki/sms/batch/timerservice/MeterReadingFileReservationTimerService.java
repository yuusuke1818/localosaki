package jp.co.osaki.sms.batch.timerservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TMeterReadingDownloadReservationInfo;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.MeterReadingDownloadReservationDao;
import jp.co.osaki.sms.batch.dto.InspectionMeterSrvBulkResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class MeterReadingFileReservationTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = -8042240428918032974L;

    /** CSVファイル格納ディレクトリ名フォーマット */
    private static final String CSV_DIR_NAME = "aggregate/%s/%s";
    /** CSVファイル名フォーマット */
    private static final String CSV_FILE_NAME_FORMAT = "データダウンロード_検針データ_%s_%s_%s.csv";

    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();


    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /**
     * バッチプロセスコード取得（817）
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.INSPDATA_BULK_DOWNLOAD_EXE.getVal();

    /**
     * DAO
     */
    private MeterReadingDownloadReservationDao dao;


    /** SQL用検索条件Map保持 */
    private HashMap<String, List<Object>> paramMap;


    public HashMap<String, List<Object>> getParamMap() {
		return paramMap;
	}

	public void setParamMap(HashMap<String, List<Object>> paramMap) {
		this.paramMap = paramMap;
	}


	/** 最大デマンド値 */

    private boolean outputMaxDemand;

    public boolean isOutputMaxDemand() {
		return outputMaxDemand;
	}

	public void setOutputMaxDemand(boolean outputMaxDemand) {
		this.outputMaxDemand = outputMaxDemand;
	}


	/** 前年同月データ */
    private boolean outputPrivYearAndSameMonth;

	public boolean isOutputPrivYearAndSameMonth() {
		return outputPrivYearAndSameMonth;
	}

	public void setOutputPrivYearAndSameMonth(boolean outputPrivYearAndSameMonth) {
		this.outputPrivYearAndSameMonth = outputPrivYearAndSameMonth;
	}



    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new MeterReadingDownloadReservationDao(this.entityManager); // 検針ダウンロード予約情報管理Dao
        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(),
                    SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);
    }


    @Override
    @Timeout
    protected void execute() {

    	try {

    		// ファイル未作成の予約情報を取得
    		List<TMeterReadingDownloadReservationInfo> targets = dao.getMeterReadingDownloadReservationInfoList();

    		if (targets == null || targets.isEmpty()) {

    			// TODO 処理対象が無いログを出力
    			batchLogger.info("＜一括DL＞処理対象無し");
    			return;
    		}

    		for (TMeterReadingDownloadReservationInfo target : targets) {

    			// メイン処理
    			batchLogger.info("＜一括DL＞処理対象あり");
    			targetProcess(target);
    		}

    	} catch (Throwable t) {

			// TODO バッチがうまくいかななかったログを出力
    		t.printStackTrace();
		}
    }


    private void targetProcess(TMeterReadingDownloadReservationInfo target) {

    	Long reservationId = target.getReservationId();
    	String loginCorpId = target.getCorpId();
    	serverDateTime = dao.getServerDateTime();

    	try {
    		batchLogger.info("＜一括DL＞予約ID" + reservationId + "のレコードの処理開始");
			// TODO 「処理中」にステータス更新
    		entityManager.getTransaction().begin();
    		dao.updateDB(reservationId, serverDateTime, "処理中");
			entityManager.getTransaction().commit();

			// TODO 検索条件をパース
			String searchConditionText = target.getSearchCondition();

			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();

			Map<String, String> map = gson.fromJson(searchConditionText, type);

			List<Object> loginPersonTypeValues = Stream.of(map.get("loginPersonType").split(", "))
					.collect(Collectors.toList());
			List<Object> loginCorpIdValues = Stream.of(map.get("loginCorpId").split(", "))
					.collect(Collectors.toList());
			List<Object> loginPersonIdValues = Stream.of(map.get("loginPersonId").split(", "))
					.collect(Collectors.toList());
			List<Object> loginCorpTypeValues = Stream.of(map.get("loginCorpType").split(", "))
					.collect(Collectors.toList());
			List<Object> personAuthsValues = Stream.of(map.get("personAuths").split(", "))
					.collect(Collectors.toList());

			List<Object> inspTypesValues = Stream.of(map.get("inspTypes").split(", "))
					.collect(Collectors.toList());
			List<Object> endDateValues = Stream.of(map.get("endDate").split(", "))
					.collect(Collectors.toList());
			List<Object> startDateValues = Stream.of(map.get("startDate").split(", "))
					.collect(Collectors.toList());

			List<Object> outputMaxDemandValues = Stream.of(map.get("outputMaxDemand").split(", "))
					.collect(Collectors.toList());

			List<Object> outputPrivYearAndSameMonthValues = Stream.of(map.get("outputPrivYearAndSameMonth").split(", "))
					.collect(Collectors.toList());

			List<Object> corpIdOrNameValues = Stream.of(map.get("corpParam").split(", "))
					.collect(Collectors.toList());

			List<Object> buildingNoOrNameValues = Stream.of(map.get("buildingParam").split(", "))
					.collect(Collectors.toList());



			HashMap<String, List<Object>> mapForFile = new HashMap<String, List<Object>>();
			mapForFile.put("loginPersonType", loginPersonTypeValues);
			mapForFile.put("loginCorpId", loginCorpIdValues);
			mapForFile.put("loginPersonId", loginPersonIdValues);
			mapForFile.put("loginCorpType", loginCorpTypeValues);
			mapForFile.put("personAuths", personAuthsValues);
			mapForFile.put("inspTypes", inspTypesValues);
			mapForFile.put("endDate", endDateValues);
			mapForFile.put("startDate", startDateValues);
			mapForFile.put("outputMaxDemand", outputMaxDemandValues);
			mapForFile.put("outputPrivYearAndSameMonth", outputPrivYearAndSameMonthValues);
			mapForFile.put("corpParam",
					corpIdOrNameValues.get(0).equals("null") ? null : corpIdOrNameValues);
			mapForFile.put("buildingParam",
					buildingNoOrNameValues.get(0).equals("null") ? null : buildingNoOrNameValues);

			setParamMap(mapForFile);

			//最大デマンド値
			setOutputMaxDemand(Boolean.valueOf((String)outputMaxDemandValues.get(0)));

			//前年同月データ
			setOutputPrivYearAndSameMonth(Boolean.valueOf((String)outputPrivYearAndSameMonthValues.get(0)));


			String searchDate = String.valueOf(startDateValues.get(0)).replace("/", "") + "-" + String.valueOf(endDateValues.get(0)).replace("/", "");
			String inspType = convInspType(String.valueOf(inspTypesValues.get(0)));


			// ファイル作成
			List<String> outputInfo = createFile(dao.meterReadingDataSearch(mapForFile), loginCorpId, reservationId, searchDate, inspType);
			batchLogger.debug("createFile()呼び出し後" + outputInfo);

			// 試験用ディレイ
			//System.out.println("ディレイ開始");
			//Thread.sleep(60000);
			//System.out.println("ディレイ終了");

			// TODO 「処理終了」にステータス更新、処理結果は「正常終了」
			entityManager.getTransaction().begin();
			dao.updateDB(reservationId, dao.getServerDateTime(), outputInfo.get(0), outputInfo.get(1), "処理終了", "完了");
			entityManager.getTransaction().commit();

    	} catch (Throwable t) {
			// TODO 「処理中止」にステータス更新、処理結果は「異常終了」
    		entityManager.getTransaction().begin();
    		dao.updateDB(reservationId, serverDateTime, "処理終了", "失敗");
			entityManager.getTransaction().commit();
		}
    }



    private List<String> createFile(List<InspectionMeterSrvBulkResultSet> records, String corpId, Long reservationId, String searchDate, String inspType) {

		List<List<String>> csvLines = new ArrayList<>();

		//タイトル
		List<String> titleLine = new ArrayList<>();
		titleLine.add("企業名");
		titleLine.add("建物名");
		titleLine.add("装置名");
		titleLine.add("管理番号");
		titleLine.add("月検針連番");
		titleLine.add("テナント名");
		titleLine.add("種別");
		titleLine.add("今回検針値");
		titleLine.add("前回検針値");
		titleLine.add("乗率");
		titleLine.add("今回使用量");
		titleLine.add("前回使用量");
		titleLine.add("使用量率");
		titleLine.add("ユーザーコード");
		titleLine.add("今回検針日時");
		titleLine.add("ステータス");
		titleLine.add("前回検針日時");
		titleLine.add("前年同月検針日時");
		titleLine.add("前年同月使用量");
		titleLine.add("最大デマンド値（kW）");
		csvLines.add(titleLine);


		for (InspectionMeterSrvBulkResultSet record : records) {

			// 以下のデータは別SQLにより取得する
			// TODO 最大デマンドを取得する必要があれば(検索条件から拾う)
			if (isOutputMaxDemand()) {
				Map<String, List<Object>> maxDemMap = new HashMap<String, List<Object>>();
				maxDemMap.put("devId", Arrays.asList(record.getDevId()));
				maxDemMap.put("meterMngId", Arrays.asList(record.getMeterMngId()));
				maxDemMap.put("fromGetDate", Arrays.asList(
						DateUtility.changeDateFormat(record.getPrevInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
							)
						);
				maxDemMap.put("toGetDate", Arrays.asList(
						DateUtility.changeDateFormat(record.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
							)
						);

				TDayLoadSurvey tDayLoadSurvey = dao.getMaxDemand(maxDemMap);

				if (tDayLoadSurvey != null && tDayLoadSurvey.getKwh30() != null) {

					// 取得した値は30分値なので2倍の1時間値とする
					record.setMaxDemand(tDayLoadSurvey.getKwh30().multiply(new BigDecimal(2)));
				}
			}

			// TODO 前年の検針データを取得する必要があれば(検索条件から拾う)
			if (isOutputPrivYearAndSameMonth()) {
				Map<String, List<Object>> prevInspMap = new HashMap<String, List<Object>>();
				prevInspMap.put("devId", Arrays.asList(record.getDevId()));
				prevInspMap.put("meterMngId", Arrays.asList(record.getMeterMngId()));
				prevInspMap.put("inspYear", Arrays.asList(Integer.parseInt(record.getInspYear()) - 1));
				prevInspMap.put("inspMonth", Arrays.asList(record.getInspMonth()));
				prevInspMap.put("inspType", Arrays.asList(record.getInspType()));

				TInspectionMeterSvr tInspectionMeterSvr = dao.getPrevYearMeterReading(prevInspMap);

				if (tInspectionMeterSvr != null) {

					// 前年同月検針日時
					record.setPrevYearInspDate(tInspectionMeterSvr.getLatestInspDate());
					// 前年同月使用量
					record.setPrevYearUseVal(tInspectionMeterSvr.getLatestUseVal());
				}
			}

			List<String> csvLine = new ArrayList<>();
			csvLine.add(record.getCorpName());
			csvLine.add(record.getBuildingName());
			csvLine.add(record.getDevName());
			csvLine.add(String.valueOf(record.getMeterMngId()));
			csvLine.add(StringUtils.leftPad(record.getInspMonthNo().toString(), 3, "0") + record.getInspType());
			csvLine.add(record.getTenantName());
			csvLine.add(record.getMeterTypeName());
			csvLine.add(record.getLatestInspVal() != null ? record.getLatestInspVal().toString() : "");
			csvLine.add(record.getPrevInspVal() != null ? record.getPrevInspVal().toString() : "");
			csvLine.add(record.getMultipleRate() != null ? record.getMultipleRate().toString() : "");
			csvLine.add(record.getLatestUseVal() != null ? record.getLatestUseVal().toString() : "");
			csvLine.add(record.getPrevUseVal() != null ? record.getPrevUseVal().toString() : "");
			csvLine.add(record.getUsePerRate() != null ? record.getUsePerRate().toString() : "");
			csvLine.add(record.getUserCd() != null ? String.valueOf(record.getUserCd()) : "");
			csvLine.add(DateUtility.changeDateFormat(record.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
			csvLine.add(record.getStatus());
			csvLine.add(DateUtility.changeDateFormat(record.getPrevInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
			csvLine.add(DateUtility.changeDateFormat(record.getPrevYearInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
			csvLine.add(record.getPrevYearUseVal() != null ? record.getPrevYearUseVal().toString() : "");
			csvLine.add(record.getMaxDemand() != null ? record.getMaxDemand().toString() : "");
			csvLines.add(csvLine);
		}

		// 出力ディレクトリを生成し、パスを取得
		String outputDirPath = createDirs(String.format(CSV_DIR_NAME, corpId, reservationId));

		// 出力用現在時刻
		String outputDate = String.valueOf(dao.getServerDateTime()).replace("-","").replace(" ", "").replace(":", "").replace(".", "");

		// CSVファイル名を生成
		String csvFileName = String.format( //
				CSV_FILE_NAME_FORMAT, //
				searchDate, inspType, outputDate);

		// CSVファイル出力
		csvPrint(outputDirPath, csvFileName, csvLines);

		List<String> outputInfo = new ArrayList<>(Arrays.asList(outputDirPath + "/" + csvFileName, csvFileName));
		batchLogger.debug(outputInfo);

		return outputInfo;
    }


    /**
     * ファイルを保存するフォルダを作成
     */
    private String createDirs(String meterReadingDataDirName) {

        File file;
        String path = getExcelOutputDir();
        Date svDate = (Date)dao.getServerDateTime();
        // 日付毎
        path += File.separator;
        path += DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYYMMDD);
        file = new File(path);
        // フォルダーが存在しない場合作成
        if (!file.exists()) {
            file.mkdir();
        }
        //
        path += File.separator;
        path += meterReadingDataDirName;
        file = new File(path);
        // フォルダーが存在しない場合作成
        if (!file.exists()) {
            file.mkdir();
        }

        return path;
    }



    /**
     * CSV出力先ディレクトリ
     * @return
     */
    private String getExcelOutputDir() {

        if (SystemUtils.IS_OS_WINDOWS) {
            return smsBatchConfigs.getConfig(SmsBatchConstants.EXPORT_CSV_DIR_WIN);
        } else {
            return smsBatchConfigs.getConfig(SmsBatchConstants.EXPORT_CSV_DIR);
        }
    }



    private boolean csvPrint(String csvFileDir, String csvFileName, List<List<String>> csvDataList) {

        File dir = new File(csvFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File csvFile = new File(csvFileDir.concat(File.separator).concat(csvFileName));
            OutputStream csvOut = new FileOutputStream(csvFile, false);
            OutputStreamWriter outSw = new OutputStreamWriter(csvOut, Charset.forName("Windows-31J"));
            CSVPrinter printer = new CSVPrinter(outSw, CSVFormat.EXCEL.withQuoteMode(QuoteMode.ALL));
            for (Iterator<List<String>> list = csvDataList.iterator(); list.hasNext();) {
                printer.printRecord(list.next());
            }
            printer.close();
            return true;
        } catch (IOException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return false;
    }


    private String convInspType(String inspType) {
    	String inspTypeName = "";
    	if (inspType.equalsIgnoreCase("a")) {
    		inspTypeName = "自動";
    	}else if (inspType.equalsIgnoreCase("m")) {
    		inspTypeName = "任意";
    	}else if (inspType.equalsIgnoreCase("s")) {
    		inspTypeName = "予約";
    	}else if (inspType.equalsIgnoreCase("r")) {
    		inspTypeName = "定期";
    	}else if (inspType.equalsIgnoreCase("t")) {
    		inspTypeName = "臨時";
    	}
    	return inspTypeName;
    }
}
