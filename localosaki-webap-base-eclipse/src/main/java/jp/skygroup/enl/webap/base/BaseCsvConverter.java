package jp.skygroup.enl.webap.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.jboss.logging.Logger;

/**
 *
 * CSV変換クラス
 *
 * @author take_suzuki
 */
public abstract class BaseCsvConverter {

    /**
     * エラーログ
     */
    protected static final Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    /**
     *
     * CSVファイルを読み込みListに設定する。
     *
     * @param csvFilePath
     * @param charsetName
     * @return CSV読み込み結果
     * @throws IOException
     */
    protected List<List<String>> csvParse(String csvFilePath, String charsetName) throws IOException {

        try {
            List<List<String>> parseRecordList = new ArrayList<>();
            FileInputStream is = new FileInputStream(new File(csvFilePath));
            InputStreamReader ir = new InputStreamReader(is, Charset.forName(charsetName));
            BufferedReader br = new BufferedReader(ir);
            CSVParser parse = CSVFormat.EXCEL.parse(br);
            List<CSVRecord> csvRecordList = parse.getRecords();
            for (CSVRecord csvRecord : csvRecordList) {
                List<String> recordColumns = new ArrayList<>();
                for (String csvColumn : csvRecord){
                    recordColumns.add(csvColumn);
                }
                parseRecordList.add(recordColumns);
            }
            parse.close();
            return parseRecordList;

        } catch (IOException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        }
    }

    /**
     *
     * List<List<String>>を読み込みCSVファイルに保存する。
     *
     * @param csvFileDir
     * @param csvFileName
     * @param charsetName
     * @param csvDataList
     * @return true:成功 false:失敗
     */
    protected boolean csvPrint(String csvFileDir, String csvFileName, String charsetName, List<List<String>> csvDataList) {

        File dir = new File(csvFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File csvFile = new File(csvFileDir.concat(File.separator).concat(csvFileName));
            OutputStream csvOut = new FileOutputStream(csvFile, false);
            OutputStreamWriter outSw = new OutputStreamWriter(csvOut, Charset.forName(charsetName));
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
}
