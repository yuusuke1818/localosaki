package jp.co.osaki.sms.batch.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.resultset.MissngDataCsvResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.batch.BaseBatchLogic;

/**
 * SMS 日報当日分のデータ欠損メール送信 CSV添付 ロジッククラス
 * @author nishida.t
 *
 */
public class CheckSurvey_missngDataSendCsvLogic extends BaseBatchLogic {

    /**
     * csvファイル作成時の拡張子
     */
    private static final String CSV_EXTENSION = ".csv";

    /**
     * nullの場合に置換する文字
     */
    private static final String NULL_CHANGE_STR = "-";

    private SmsBatchConfigs smsBatchConfigs;

    /**
     * コンストラクター
     */
    CheckSurvey_missngDataSendCsvLogic() {
    }

    /**
     * コンストラクター
     *
     * @param batchName
     */
    public CheckSurvey_missngDataSendCsvLogic(String batchName, SmsBatchConfigs smsBatchConfigs) {
        this.batchName = batchName;
        this.smsBatchConfigs = smsBatchConfigs;
    }

    private enum COMMON_HEADER {
        CORP_NAME("企業名"),
        BUILDING_NAME("建物名"),
        JIGEN_TIME("時刻"),
        DEV_NAME("装置名"),
        METER_TYPE("種別"),
        METER_MNG_ID("管理番号"),
        METER_ID("計器ID"),
        TENANT_NAME("テナント名"),
        METER_STATE("メーター状態"),
        METER_PRES_SITU("メーター状況"),
        METER_ALERT_START("アラート停止期間（開始）"),
        METER_ALERT_END("アラート停止期間（終了）"),
        REMARKS("備考");

        private final String headerName;

        private COMMON_HEADER(String headerName) {
            this.headerName = headerName;
        }

        public String getHeaderName() {
            return headerName;
        }
    }

    /**
     * CSVファイル作成
     * @param csvDataList 欠損データリスト
     * @param svDate 現在日時
     * @return CSVファイルパス
     */
    public String createCheckSurvey_missngDataCsv(List<MissngDataCsvResultSet> csvDataList, Timestamp svDate) {

        // CSVファイル、対象ディレクトリ取得
        String csvCharset = smsBatchConfigs.getConfig(SmsBatchConstants.CSV_FILE_OUTPUT_CHARSET_NAME);
        String tempDir = smsBatchConfigs.getConfig(SmsBatchConstants.EXPORT_CSV_DIR);
        String outputFileName = smsBatchConfigs.getConfig(SmsBatchConstants.CHECK_SURVEY_MISSING_DATA_CSV);
        String fileName = createCsvFileName(outputFileName, svDate);
        outputFileName = fileName.concat(CSV_EXTENSION);

        // ファイルを作成先フォルダを作成
        String outputDir = createFolder(tempDir, svDate, fileName);

        String zipPath = "";

        try {
            // ファイルを作成
            File file = createCsvFile(outputDir, outputFileName);

            List<String> writeLine = new ArrayList<>();

            // ヘッダを設定
            String header = "";
            for (COMMON_HEADER common : COMMON_HEADER.values()) {
                header += common.getHeaderName() + ",";
            }
            writeLine.add(header);

            // データ欠損のあった建物情報を設定
            for (MissngDataCsvResultSet data : csvDataList) {

                String dataRow = "";
                // 企業名
                dataRow += Objects.toString(data.getCorpName(), NULL_CHANGE_STR) + ",";
                // 建物名
                dataRow += Objects.toString(data.getBuildingName(), NULL_CHANGE_STR) + ",";
                // 時刻
                dataRow += Objects.toString(data.getJigenTime(), NULL_CHANGE_STR) + ",";
                // 装置名
                dataRow += Objects.toString(data.getDevName(), NULL_CHANGE_STR) + ",";
                // 種別
                dataRow += Objects.toString(data.getMeterTypeName(), NULL_CHANGE_STR) + ",";
                // 管理番号
                dataRow += Objects.toString(data.getMeterMngId(), NULL_CHANGE_STR) + ",";
                // メーターID（計器ID）
                dataRow += Objects.toString(data.getMeterId(), NULL_CHANGE_STR) + ",";
                // テナント名
                dataRow += Objects.toString(data.getTenantName(), NULL_CHANGE_STR) + ",";
                // メーター状態
                dataRow += Objects.toString(convertMeterSta(data.getMeterSta()), NULL_CHANGE_STR) + ",";
                // メーター状況
                dataRow += Objects.toString(convertMeterPresSitu(data.getMeterPresSitu()), NULL_CHANGE_STR) + ",";
                // アラート停止期間（開始）
                dataRow += Objects.toString(convertAlertPause(data.getAlertPauseStart()), NULL_CHANGE_STR) + ",";
                // アラート停止期間（終了）
                dataRow += Objects.toString(convertAlertPause(data.getAlertPauseEnd()), NULL_CHANGE_STR) + ",";
                // 停止状態備考
                dataRow += Objects.toString(data.getMeterStaMemo(), NULL_CHANGE_STR);

                writeLine.add(dataRow);
            }

            // CSV出力
            writeCsvFile(file, writeLine, csvCharset);

            // ZIPファイル作成
            zipPath = createZipFile(outputDir);

            return zipPath;

        } catch (IOException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        return zipPath;
    }

    /**
     * zipファイルを作成する
     * zipファイル作成後、元のフォルダは削除
     *
     * @param zipCompressFolder
     * @return
     */
    private String createZipFile(String zipCompressFolder) {
        // 圧縮後の出力パス + ファイル名
        String zipFilePath = zipCompressFolder + ".zip";
        // Zipファイル作成
        if (jp.co.osaki.sms.batch.utility.common.SmsFileZipArchive.compressDirectory(zipCompressFolder, zipFilePath)) {
            // zip圧縮元フォルダを削除
            AnalysisEmsUtility.deleteFileDirectory(new File(zipCompressFolder));
            return zipFilePath;
        }
        return null;
    }



    /**
     * 保存ファイル名を作成 （csv）
     *
     * @param テンプレートファイル名
     * @param 日付
     */
    private String createCsvFileName(String propName, Date today) {
        return String.format(propName,
                DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
    }

    /**
     * ファイルを保存するフォルダを作成
     * @param tempDir
     * @param outputDate
     * @return
     */
    private String createFolder(String tempDir, Date outputDate, String fileName) {

        // 出力日を文字列化
        String outputDateFormat = DateUtility.changeDateFormat(outputDate, DateUtility.DATE_FORMAT_YYYYMMDD);

        // ルートフォルダパス
        String excelOutputRootPath = tempDir.concat(File.separator).concat(outputDateFormat).concat(File.separator).concat(fileName);
        File rootNewdir = new File(excelOutputRootPath);

        // フォルダーが存在しない場合作成
        if (!rootNewdir.exists()) {
            rootNewdir.mkdirs();
        }

        return rootNewdir.getPath();
    }

    /**
     * ファイルを作成
     *
     * @throws IOException
     * @return
     */
    private File createCsvFile(String tempDir, String fileName) throws IOException {

        // ファイル作成部分
        StringBuilder sb = new StringBuilder(tempDir);
        sb.append(File.separator);

        File folder = new File(sb.toString());
        folder.mkdirs();
        sb.append(fileName);
        File file = new File(sb.toString());
        file.createNewFile();

        return file;
    }

    /**
     * csvファイルへ書き込み
     *
     * @param targetFile
     * @param csvRows
     * @exception IOException
     * @return
     */
    private String writeCsvFile(File targetFile, List<String> csvRowList, String csvCharset) throws IOException {

        // ファイル書き込み
        //BOM 追加
        try (FileOutputStream os = new FileOutputStream(targetFile)) {
            os.write(0xef);
            os.write(0xbb);
            os.write(0xbf);
        }

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile, true), csvCharset))) {
            for (String row : csvRowList) {
                writer.println(row);
            }
        }

        return targetFile.getPath();
    }

    /**
     * メーター状態をStringに変換して返却
     * @param num
     * @return
     */
    private String convertMeterPresSitu(BigDecimal num) {
        BigDecimal two = new BigDecimal("2");
        if(num == null) {
            return "-";
        }else if(num.compareTo(BigDecimal.ZERO) == 0) {
            return "通常";
        }else if(num.compareTo(BigDecimal.ONE) == 0) {
            return "工事中";
        }else if(num.compareTo(two) == 0) {
            return "予備";
        }else{
            return "その他";
        }
    }

    /**
     * メーター状態をStringに変換して返却
     * @param num
     * @return
     */
    private String convertMeterSta(BigDecimal num) {
        if(num == null) {
            return "-";
        }else if(num.compareTo(BigDecimal.ZERO) == 0) {
            return "正常復帰";
        }else {
            return "異常発生";
        }
    }

    /**
     * 日付を変換
     * @param pausePeriod
     * @return
     */
    private String convertAlertPause(String pausePeriod) {
        if(CheckUtility.isNullOrEmpty(pausePeriod)) {
            return "-";
        }
        return pausePeriod.substring(0, 4) + "/" + pausePeriod.substring(4, 6) + "/" + pausePeriod.substring(6, 8);
    }

}
