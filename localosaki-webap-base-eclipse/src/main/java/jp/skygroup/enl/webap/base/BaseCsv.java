package jp.skygroup.enl.webap.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

/**
 *
 * CSV操作クラス
 *
 * @author take_suzuki
 */
public abstract class BaseCsv {

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
     */
    protected List<List<String>> csvParser(String csvFilePath, String charsetName) {

        List<List<String>> parseRecordList = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvFilePath)), Charset.forName(charsetName)));
            try (CSVParser parse = CSVFormat.EXCEL.parse(br)) {
                List<CSVRecord> csvRecordList = parse.getRecords();
                for (CSVRecord csvRecord : csvRecordList) {
                    List<String> recordColumns = new ArrayList<>();
                    for (String csvColumn : csvRecord){
                        recordColumns.add(csvColumn);
                    }
                    parseRecordList.add(recordColumns);
                }
                parse.close();
            } catch (IOException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        } catch (FileNotFoundException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return parseRecordList;
    }
}
