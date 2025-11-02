package jp.co.osaki.sms.batch.timerservice;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jboss.ejb3.annotation.TransactionTimeout;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.batch.csv.converter.RFMeterInfoCsvConverter;
import jp.co.osaki.osol.batch.csv.converter.TDayLoadSurveyCsvConverter;
import jp.co.osaki.osol.batch.csv.converter.TDayLoadSurveyRevCsvConverter;
import jp.co.osaki.osol.batch.csv.converter.TMeterDataCsvConverter;
import jp.co.osaki.osol.batch.csv.record.RFMeterInfoRecord;
import jp.co.osaki.osol.batch.csv.record.TDayLoadSurveyRecord;
import jp.co.osaki.osol.batch.csv.record.TDayLoadSurveyRevRecord;
import jp.co.osaki.osol.batch.csv.record.TMeterDataRecord;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK;
import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.osol.entity.TMeterDataPK;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.ImportFromCsvDao;
import jp.co.osaki.sms.batch.resultset.ImportCsvConcentratorInfoResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * SMS CSV取込 TimerServiceクラス
 *
 * @author yonezawa.a
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class ImportFromCsvTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 7297082275558942707L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.IMPORT_FROM_CSV.getVal();

    /**
     * 設定ファイル
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /**
     * Dao
     */
    private ImportFromCsvDao importFromCsvDao;

    /**
     * CSV変換クラス
     */
    private TDayLoadSurveyCsvConverter tDayLoadSurveyCsvConverter = new TDayLoadSurveyCsvConverter();
    private TDayLoadSurveyRevCsvConverter tDayLoadSurveyRevCsvConverter = new TDayLoadSurveyRevCsvConverter();
    private TMeterDataCsvConverter tMeterDataCsvConverter = new TMeterDataCsvConverter();
    private RFMeterInfoCsvConverter rFMeterInfoCsvConverter = new RFMeterInfoCsvConverter();

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    // 取込フラグ
    private boolean updFlg;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.importFromCsvDao = new ImportFromCsvDao(this.entityManager);

        TBatchStartupSetting tBatchStartupSetting = this.importFromCsvDao.getTBatchStartupSetting(BATCH_PROCESS_CD);
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

    /*
     * (非 Javadoc)
     *
     * @see jp.skygroup.enl.webap.base.batch.BaseBatchScheduleTimer#execute()
     */
    @TransactionTimeout(unit = TimeUnit.SECONDS, value = 10)
    @Timeout
    @Override
    protected void execute() {

        try {

            batchLogger.info(this.batchName.concat(" Start"));

            // サーバ時刻を取得
            serverDateTime = importFromCsvDao.getServerDateTime();

            // CSV取込時の情報
            String csvCharset = smsBatchConfigs.getConfig(SmsBatchConstants.CSV_FILE_CHARSET_NAME);
            String tempDir = smsBatchConfigs.getConfig(SmsBatchConstants.IMPORT_CSV_DIR);
            String nowDateStr = DateUtility.changeDateFormat(serverDateTime, DateUtility.DATE_FORMAT_YYYYMMDD);
            String lsDls = smsBatchConfigs.getConfig(SmsBatchConstants.LOAD_SURVEY_DLS_CSV);
            String lsRdls = smsBatchConfigs.getConfig(SmsBatchConstants.LOAD_SURVEY_RDLS_CSV);
            String lsDmv = smsBatchConfigs.getConfig(SmsBatchConstants.LOAD_SURVEY_DMV_CSV);
            String lsRdmv = smsBatchConfigs.getConfig(SmsBatchConstants.LOAD_SURVEY_RDMV_CSV);
            String meterData = smsBatchConfigs.getConfig(SmsBatchConstants.METER_DATA_CSV);
            String rfMeterInfo = smsBatchConfigs.getConfig(SmsBatchConstants.RFMETER_INFO_CSV);

            // 当日までのパス
            String nowDateFolderPath = tempDir.concat(File.separator).concat(nowDateStr);
            File newDir = new File(nowDateFolderPath);

            // フォルダ存在チェック
            if (!newDir.exists() || !newDir.isDirectory()) {
                newDir.mkdir();
            }

            File targetDir = new File(tempDir.concat(File.separator));

            for (File csvFiles : targetDir.listFiles()) {

                // ディレクトリは対象外
                if (csvFiles.isDirectory()) {
                    continue;
                }

                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }

                updFlg = false;

                if (entityManager.getTransaction().isActive()) {
                    // [MuDM2] ロードサーベイ日データ
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(lsDls).matcher(csvFiles.getName()).find()) {

                        updFlg = createTDayLoadSurveyRecordData(lsDls, csvFiles, csvCharset, nowDateFolderPath,
                                OsolBatchConstants.TARGET_KWH30);
                    }

                    // [MuDM2] ロードサーベイ日データ(逆)
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(lsRdls).matcher(csvFiles.getName()).find()) {

                        updFlg = createTDayLoadSurveyRevRecordData(lsRdls, csvFiles, csvCharset, nowDateFolderPath,
                                OsolBatchConstants.TARGET_KWH30);
                    }

                    // [MuDM2] ロードサーベイ日データ(指針)
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(lsDmv).matcher(csvFiles.getName()).find()) {

                        updFlg = createTDayLoadSurveyRecordData(lsDmv, csvFiles, csvCharset, nowDateFolderPath,
                                OsolBatchConstants.TARGET_DMVKWH);
                    }

                    // [MuDM2] ロードサーベイ日データ(指針逆)
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(lsRdmv).matcher(csvFiles.getName()).find()) {

                        updFlg = createTDayLoadSurveyRevRecordData(lsRdmv, csvFiles, csvCharset, nowDateFolderPath,
                                OsolBatchConstants.TARGET_DMVKWH);
                    }

                    // [MuDM2] メータ現在値データx
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(meterData).matcher(csvFiles.getName()).find()) {

                        batchLogger.info(this.batchName.concat(" START ".concat(meterData).concat(" Csv Import File[")
                                .concat(csvFiles.getAbsolutePath()).concat("]")));

                        List<TMeterDataRecord> tMeterDataRecordList = new ArrayList<>();

                        try {
                            // CSV変換
                            tMeterDataRecordList = tMeterDataCsvConverter.csvRead(csvFiles.getAbsolutePath(),
                                    csvCharset);

                            if (tMeterDataRecordList != null && tMeterDataRecordList.size() != 0) {
                                // 取込
                                tMeterDataCsvImport(tMeterDataRecordList, serverDateTime);

                                // 取り込み成功後は削除する
                                AnalysisEmsUtility.deleteFileDirectory(new File(csvFiles.getAbsolutePath()));


                                updFlg = true;
                            }
                        } catch (IOException e) {
                            batchLogger.error(this.batchName.concat(" ERROR ".concat(meterData).concat(" Csv Import File[")
                                    .concat(csvFiles.getAbsolutePath()).concat("]")));
                        }
                    }

                    // [WALK] 無線メータ情報のデータ
                    if (csvFiles.isFile() && csvFiles.exists()
                            && Pattern.compile(rfMeterInfo).matcher(csvFiles.getName()).find()) {

                        batchLogger.info(this.batchName.concat(" START ".concat(rfMeterInfo).concat(" Csv Import File[")
                                .concat(csvFiles.getAbsolutePath()).concat("]")));

                        List<RFMeterInfoRecord> rFMeterInfoRecordList = new ArrayList<>();

                        try {
                            // CSV変換
                            rFMeterInfoRecordList = rFMeterInfoCsvConverter.csvRead(csvFiles.getAbsolutePath(),
                                    csvCharset);

                            if (rFMeterInfoRecordList != null && rFMeterInfoRecordList.size() != 0) {
                                // 取込
                                rFMeterInfoCsvImport(rFMeterInfoRecordList, serverDateTime);

                                // 取り込み成功後は削除する
                                AnalysisEmsUtility.deleteFileDirectory(new File(csvFiles.getAbsolutePath()));

                                updFlg = true;
                            }
                        } catch (IOException e) {
                            batchLogger.error(this.batchName.concat(" ERROR ".concat(rfMeterInfo).concat(" Csv Import File[")
                                    .concat(csvFiles.getAbsolutePath()).concat("]")));
                        }
                    }

                    if (entityManager.getTransaction().getRollbackOnly()) {
                        batchLogger.info(this.batchName.concat(" rollback>> start"));
                        entityManager.getTransaction().rollback();
                        batchLogger.info(this.batchName.concat(" rollback>> end"));
                    } else if (updFlg && !entityManager.getTransaction().getRollbackOnly()) {
                        batchLogger.info(this.batchName.concat(" commit>> start"));
                        entityManager.getTransaction().commit();
                        batchLogger.info(this.batchName.concat(" commit>> end"));
                    }
                    entityManager.clear();
                }
            }

            batchLogger.info(this.batchName.concat(" End"));
        } catch (Exception ex) {
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            entityManager.clear();
        }
    }

    /**
     * CSVを取込み、ロードサーベイ日データ登録更新
     *
     * @param targetFile
     * @param csvFiles
     * @param csvCharset
     * @param nowDateFolderPath
     * @param dataType
     * @return 取込あり：true、取込なし：false
     */
    private Boolean createTDayLoadSurveyRecordData(String targetFile, File csvFiles, String csvCharset,
            String nowDateFolderPath, String dataType) {

        batchLogger.info(this.batchName.concat(" START ".concat(targetFile).concat(" Csv Import File[")
                .concat(csvFiles.getAbsolutePath()).concat("]")));

        List<TDayLoadSurveyRecord> tDayLoadSurveyRecordList = new ArrayList<>();

        try {
            // CSV変換
            tDayLoadSurveyRecordList = tDayLoadSurveyCsvConverter.csvRead(csvFiles.getAbsolutePath(),
                    csvCharset, dataType);

            if (tDayLoadSurveyRecordList != null && tDayLoadSurveyRecordList.size() != 0) {
                // 取込
                tDayLoadSurveyCsvImport(tDayLoadSurveyRecordList, serverDateTime, dataType);

                // 取り込み成功後は削除する
                AnalysisEmsUtility.deleteFileDirectory(new File(csvFiles.getAbsolutePath()));

                updFlg = true;
            }
        } catch (IOException e) {
            batchLogger.error(this.batchName.concat(" ERROR ".concat(targetFile).concat(" Csv Import File[")
                    .concat(csvFiles.getAbsolutePath()).concat("]")));
        }

        return updFlg;
    }

    /**
     * ロードサーベイ日データ登録更新処理
     *
     * @param tDayLoadSurveyRecordList
     * @param serverDateTime
     * @param dataType
     */
    private void tDayLoadSurveyCsvImport(List<TDayLoadSurveyRecord> tDayLoadSurveyRecordList,
            Timestamp serverDateTime, String dataType) {

        try {
            String devId = tDayLoadSurveyRecordList.get(0).getDevId();
            List<ImportCsvConcentratorInfoResultSet> multiList = importFromCsvDao.getMMeterList(devId);
            Map<Long, BigDecimal> meterMap = new HashMap<>();
            for(ImportCsvConcentratorInfoResultSet ret : multiList) {
                meterMap.put(ret.getMeterMngId(), ret.getMulti());
            }

            for (TDayLoadSurveyRecord record : tDayLoadSurveyRecordList) {

                TDayLoadSurvey tdls = new TDayLoadSurvey();
                TDayLoadSurveyPK tdlsPk = new TDayLoadSurveyPK();
                tdlsPk.setDevId(record.getDevId());
                tdlsPk.setMeterMngId(record.getMeterMngId());
                tdlsPk.setGetDate(record.getGetDate());
                tdls.setId(tdlsPk);
                if (OsolBatchConstants.TARGET_KWH30.equals(dataType)) {
                    tdls.setKwh30(record.getKwh30());
                } else if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)) {
                    tdls.setDmvKwh(record.getDmvKwh());
                }

                importFromCsvDao.createTDayLoadSurvey(tdls, serverDateTime, dataType);
                importFromCsvDao.createMaxDemand(tdls, serverDateTime, dataType);

                // コンセントレータ対応
                if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)
                        && ("XR".contentEquals(tdls.getId().getDevId().substring(0, 2))
                                || "XS".contentEquals(tdls.getId().getDevId().substring(0, 2))
                                || "XY".contentEquals(tdls.getId().getDevId().substring(0, 2))
                                || "TL".contentEquals(tdls.getId().getDevId().substring(0, 2)))) {
                    if(!meterMap.containsKey(record.getMeterMngId()) || meterMap.get(record.getMeterMngId()) == null) {
                        continue;
                    }
                    importFromCsvDao.updateTDayLoadSurveyForConsentrator(tdls, dataType, serverDateTime, meterMap.get(record.getMeterMngId()));
                }
            }
        }catch (Exception e) {
            batchLogger.error(this.batchName.concat(" tDayLoadSurveyCsvImport Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    /**
     * CSVを取込み、ロードサーベイ日データ（逆方向）登録更新
     *
     * @param targetFile
     * @param csvFiles
     * @param csvCharset
     * @param nowDateFolderPath
     * @param dataType
     * @return 取込あり：true、取込なし：false
     */
    private boolean createTDayLoadSurveyRevRecordData(String targetFile, File csvFiles, String csvCharset,
            String nowDateFolderPath, String dataType) {

        batchLogger.info(this.batchName.concat(" START ".concat(targetFile).concat(" Csv Import File[")
                .concat(csvFiles.getAbsolutePath()).concat("]")));

        List<TDayLoadSurveyRevRecord> tDayLoadSurveyRevRecordList = new ArrayList<>();

        try {
            // CSV変換
            tDayLoadSurveyRevRecordList = tDayLoadSurveyRevCsvConverter.csvRead(csvFiles.getAbsolutePath(),
                    csvCharset, dataType);

            if (tDayLoadSurveyRevRecordList != null && tDayLoadSurveyRevRecordList.size() != 0) {
                // 取込
                tDayLoadSurveyRevCsvImport(tDayLoadSurveyRevRecordList, serverDateTime,
                        dataType);

                // 取り込み成功後は削除する
                AnalysisEmsUtility.deleteFileDirectory(new File(csvFiles.getAbsolutePath()));

                updFlg = true;
            }
        } catch (IOException e) {
            batchLogger.error(this.batchName.concat(" ERROR ".concat(targetFile).concat(" Csv Import File[")
                    .concat(csvFiles.getAbsolutePath()).concat("]")));
        }

        return updFlg;
    }

    /**
     * ロードサーベイ日データ（逆方向）登録更新処理
     *
     * @param tDayLoadSurveyRevRecordList
     * @param serverDateTime
     * @param dataType
     */
    private void tDayLoadSurveyRevCsvImport(
            List<TDayLoadSurveyRevRecord> tDayLoadSurveyRevRecordList, Timestamp serverDateTime, String dataType) {

        try {
            String devId = tDayLoadSurveyRevRecordList.get(0).getDevId();
            List<ImportCsvConcentratorInfoResultSet> multiList = importFromCsvDao.getMMeterList(devId);
            Map<Long, BigDecimal> meterMap = new HashMap<>();
            for(ImportCsvConcentratorInfoResultSet ret : multiList) {
                meterMap.put(ret.getMeterMngId(), ret.getMulti());
            }
            for (TDayLoadSurveyRevRecord record : tDayLoadSurveyRevRecordList) {
                TDayLoadSurveyRev tdlsr = new TDayLoadSurveyRev();
                TDayLoadSurveyRevPK tdlsrPk = new TDayLoadSurveyRevPK();
                tdlsrPk.setDevId(record.getDevId());
                tdlsrPk.setMeterMngId(record.getMeterMngId());
                tdlsrPk.setGetDate(record.getGetDate());
                tdlsr.setId(tdlsrPk);
                if (OsolBatchConstants.TARGET_KWH30.equals(dataType)) {
                    tdlsr.setKwh30(record.getKwh30());
                } else if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)) {
                    tdlsr.setDmvKwh(record.getDmvKwh());
                }

                importFromCsvDao.createTDayLoadSurveyRev(tdlsr, serverDateTime, dataType);
                importFromCsvDao.createMaxDemandRev(tdlsr, serverDateTime, dataType);

                // コンセントレータ対応
                if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)
                        && ("XR".contentEquals(tdlsr.getId().getDevId().substring(0, 2))
                                || "XS".contentEquals(tdlsr.getId().getDevId().substring(0, 2))
                                || "XY".contentEquals(tdlsr.getId().getDevId().substring(0, 2))
                                || "TL".contentEquals(tdlsr.getId().getDevId().substring(0, 2)))) {
                    if(!meterMap.containsKey(record.getMeterMngId()) || meterMap.get(record.getMeterMngId()) == null) {
                        continue;
                    }
                    importFromCsvDao.updateTDayLoadSurveyRevForConsentrator(tdlsr, dataType, serverDateTime, meterMap.get(record.getMeterMngId()) );
                }
            }
        } catch (Exception e) {
            batchLogger.error(this.batchName.concat(" tDayLoadSurveyRevCsvImport Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    /**
     * CSVを取込み、メーター現在値データ登録更新
     *
     * @param tMeterDataRecordList
     * @param serverDateTime
     */
    private void tMeterDataCsvImport(List<TMeterDataRecord> tMeterDataRecordList, Timestamp serverDateTime) {

        try {
            for (TMeterDataRecord record : tMeterDataRecordList) {
                TMeterData tmd = new TMeterData();
                TMeterDataPK tmdPk = new TMeterDataPK();
                tmdPk.setDevId(record.getDevId());
                tmdPk.setMeterMngId(record.getMeterMngId());
                tmd.setId(tmdPk);
                tmd.setMeterId(record.getMeterId());
                tmd.setMeasureDate(record.getRecDate());
                tmd.setCurrentKwh1(record.getCurrentKwh1());
                tmd.setCurrentKwh2(record.getCurrentKwh2());
                tmd.setMomentaryPwr(record.getMomentaryPwr());
                tmd.setVoltage12(record.getVoltage12());
                tmd.setVoltage13(record.getVoltage13());
                tmd.setVoltage23(record.getVoltage23());
                tmd.setAmpere1(record.getAmpere1());
                tmd.setAmpere2(record.getAmpere2());
                tmd.setAmpere3(record.getAmpere3());
                tmd.setCircuitBreaker(record.getCircuitBreaker());
                tmd.setPowerFactor(record.getPowerFactor());
                importFromCsvDao.createTMeterData(tmd, serverDateTime);
            }
        } catch (Exception e) {
            batchLogger.error(this.batchName.concat(" tMeterDataCsvImport Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    /**
     * CSVを取込み、メーターに関連するテーブル登録更新
     *
     * @param rFMeterInfoRecordList
     * @param serverDateTime
     */
    private void rFMeterInfoCsvImport(List<RFMeterInfoRecord> rFMeterInfoRecordList,
            Timestamp serverDateTime) {
        try {
            for (RFMeterInfoRecord record : rFMeterInfoRecordList) {
                // メーターテーブル
                MMeter mm = new MMeter();
                MMeterPK mmPk = new MMeterPK();
                mmPk.setDevId(record.getDevId());
                mmPk.setMeterMngId(record.getMeterMngId());
                mm.setId(mmPk);
                mm.setWirelessType(record.getWirelessType());
                mm.setMeterId(record.getMeterId());
                // 建物IDは登録時に格納
                mm.setWirelessId(record.getWirelessId());
                mm.setHop1Id(record.getHop1Id());
                mm.setHop2Id(record.getHop2Id());
                mm.setHop3Id(record.getHop3Id());
                mm.setPollingId(record.getPollingId());
                mm.setMulti(record.getMulti());
                mm.setAlarm(record.getAlarm());

                // 建物テーブル
                TBuilding tb = new TBuilding();
                tb.setBuildingName(record.getBuildingName());
                tb.setAddress(record.getAddress());

                // 建物、装置中間テーブル
                MDevRelation mdr = new MDevRelation();
                MDevRelationPK mdrPk = new MDevRelationPK();
                mdrPk.setDevId(record.getDevId());
                mdr.setId(mdrPk);

                // 建物、メーター中間テーブル
                TBuildDevMeterRelation tbdmr = new TBuildDevMeterRelation();
                TBuildDevMeterRelationPK tbdmrPk = new TBuildDevMeterRelationPK();
                tbdmrPk.setDevId(record.getDevId());
                tbdmrPk.setMeterMngId(record.getMeterMngId());
                tbdmr.setId(tbdmrPk);

                // 確定前検針テーブル
                TInspectionMeterBef timb = new TInspectionMeterBef();
                TInspectionMeterBefPK timbPk = new TInspectionMeterBefPK();
                timbPk.setDevId(record.getDevId());
                timbPk.setMeterMngId(record.getMeterMngId());
                timbPk.setLatestInspDate(record.getLatestInspDate());
                timb.setId(timbPk);
                timb.setBuildingId(Long.valueOf(record.getBuildingNo()));
                // メーター種別は登録時に格納
                timb.setMulti(record.getMulti());
                timb.setLatestInspVal(record.getLatestInspVal());

                // テナントユーザー情報テーブル
                MTenantSm mtsms = new MTenantSm();
                mtsms.setAddress2(record.getAddress2());

                // テーブル登録
                String result = importFromCsvDao.createrRMeterInfo(mm, record.getBuildingNo(), tb, mdr, tbdmr, timb,
                        mtsms, serverDateTime);

                if (!CheckUtility.isNullOrEmpty(result)) {
                    if (result.contains("のみ")) {
                        batchLogger.warn(this.batchName.concat(" warn rfmeter_info insert " + result));
                    } else {
                        batchLogger.error(this.batchName.concat(" Error rfmeter_info insert " + result));
                    }
                }
            }
        }catch (Exception e) {
            batchLogger.error(this.batchName.concat(" rFMeterInfoCsvImport Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

    }

}
