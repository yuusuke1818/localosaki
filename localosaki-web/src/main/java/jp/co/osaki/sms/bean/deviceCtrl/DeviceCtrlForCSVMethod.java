package jp.co.osaki.sms.bean.deviceCtrl;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.web.csv.sms.converter.DeviceCtrlCsvConverter;
import jp.co.osaki.sms.deviceCtrl.dao.MDevPrmDao;
import jp.co.osaki.sms.deviceCtrl.dao.TWorkHstForCsvDao;
import jp.skygroup.enl.webap.base.BaseConstants;

/**
 * 機器制御 CSV回りの処理 クラス. DeviceCtrlXml.javaからのみの呼び出し想定 Daoは呼び元が設定することが必要
 *
 * @author tominaga.d
 */
public class DeviceCtrlForCSVMethod {
    // dao系(呼び元から設定してもらう)
    private TWorkHstForCsvDao tWorkHstCsvDao;
    private MDevPrmDao mDevPrmDao;

    // リクエスト情報クラス
    private static class DeviceCtrlCSVRequestInfo {
        /** 名称タイプ */
        public Integer type;
        /** 要求コマンド名 */
        public String reqCommandName;

        public DeviceCtrlCSVRequestInfo(Integer type, String reqCommandName) {
            this.type = type;
            this.reqCommandName = reqCommandName;
        }
    }

    private static final Map<String, DeviceCtrlCSVRequestInfo> REQUEST_TABLE = new LinkedHashMap<>();
    static {
        // csv名称,名称タイプ,要求コマンド名
        REQUEST_TABLE.put("_DLSDATA.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.getDlsdata)); // [MuDM2] ロードサーベイ日データ
        REQUEST_TABLE.put("_DMVDATA.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.getDmvdata)); // [MuDM2] 日30分指針値データ任意要求
        REQUEST_TABLE.put("_MVAL.csv", new DeviceCtrlCSVRequestInfo(1, DeviceCtrlConstants.getMeterval)); // [MuDM2] メータ現在値データ
//        REQUEST_TABLE.put("get-meterctrl", new DeviceCtrlCSVRequestInfo(1, ""));  // [MuDM2] メータ負荷制限状態データ
        REQUEST_TABLE.put("_MLIST.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.meterList)); // [MuDM2] メータ一覧データ
        REQUEST_TABLE.put("_MINFO.csv", new DeviceCtrlCSVRequestInfo(1, DeviceCtrlConstants.getMeterinfo)); // [MuDM2] メータ情報個別データ
        REQUEST_TABLE.put("_CLIST.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.concentList)); // [MuDM2] コンセントレータ一覧データ
        REQUEST_TABLE.put("_SETBRK.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.setBreaker)); // [MuDM2] MU-DM2設定情報（メータ開閉設定）
        REQUEST_TABLE.put("_MCTRL.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.getMeterctrl)); // [MuDM2] メータ負荷制限設定
        REQUEST_TABLE.put("_ADDMETER.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.addMeter)); // [MuDM2] メータ追加
        REQUEST_TABLE.put("_DELMETER.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.delMeter)); // [MuDM2] メータ削除
        REQUEST_TABLE.put("_CHGMETER.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.chgMeter)); // [MuDM2] メータID変更
        REQUEST_TABLE.put("_ADDCONCENT.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.addConcent)); // [MuDM2] コンセントレータ登録
        REQUEST_TABLE.put("_DELCONCENT.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.delConcent)); // [MuDM2] コンセントレータ削除
        REQUEST_TABLE.put("_INITCONCENT.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.initConcent)); // [MuDM2] コンセントレータ初期化
        REQUEST_TABLE.put("_SETLDLIMIT.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.setLoadlimit)); // [MuDM2] メータ負荷制限設定要求
        REQUEST_TABLE.put("_AINSPDATA.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.getAinspdata)); // [MuDM2] 自動検針データ要求
        REQUEST_TABLE.put("_MINSPDATA.csv", new DeviceCtrlCSVRequestInfo(0, DeviceCtrlConstants.getMinspdata)); // [MuDM2] 任意検針データ要求

    }

    private static final List<String> REPLASE_PTN1 = new ArrayList<>();
    static {
        REPLASE_PTN1.add("\r\n");
        REPLASE_PTN1.add("\n");
        REPLASE_PTN1.add("\r");
        REPLASE_PTN1.add("/");
    }
    private static final List<String> REPLASE_PTN2 = new ArrayList<>();
    static {
        REPLASE_PTN2.add("\r\n");
        REPLASE_PTN2.add("\n");
        REPLASE_PTN2.add("\r");
    }

    /**
     * smControlログ
     */
    private static final Logger smCtrlLogger = Logger.getLogger(DeviceCtrlConstants.SmControl);
    /**
     * エラーログ
     */
    private static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    private static final String MUDM2 = "MU-DM2";
    private static final String EOF = "-----EOF-----";

    public DeviceCtrlForCSVMethod(TWorkHstForCsvDao tWorkHstCsvDao, MDevPrmDao mDevPrmDao) {
        this.tWorkHstCsvDao = tWorkHstCsvDao;
        this.mDevPrmDao = mDevPrmDao;
    }

    /**
     * 所定フォルダ内のCSVを解析して処理予約テーブルに追加.
     *
     * @param String devId
     * @return
     */
    public void setRequestFromCSV(String devId) {
        smCtrlLogger.info("-----csv処理-----");
        // 装置情報のホームディレクトリ設定から格納パスを取得
        Map<String, String> filePath = makeFilePath(devId);

        // 格納パスより全ファイルを捜査
        File dir = new File(filePath.get("set"));
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    // ファイルの更新日付
                    Long mod = file.lastModified();
                    String fileUpdateDay = DateUtility.changeDateFormat(new Date(mod), DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH);
                    // ファイル内容取得
                    DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                    List<String> lineList = csv.listCsvColumn(file.getPath());
                    // 装置ID
                    String fileDevId = lineList.get(0).substring(6, 6 + 8);
                    // 引数の装置IDとCSVの装置IDが一致している
                    if (devId.equals(fileDevId)) {
                        // 定義リクエスト分ループ
                        for (Map.Entry<String, DeviceCtrlCSVRequestInfo> entry : REQUEST_TABLE.entrySet()) {
                            // CSVのファイル名が異なるので、
                            // 名称タイプを持たして処理を分ける
                            String name = "";
                            if (entry.getValue().type == 0) {
                                // ファイル名＝装置ID+_HOGE.csv
                                name = fileDevId + entry.getKey();
                            } else {
                                // ファイル名＝装置ID+_メーター管理ID_HOGE.csv
                                String[] column = lineList.get(0).split(",");
                                if (column.length > 1) {
                                    name = fileDevId + "_" + column[1] + entry.getKey();
                                }
                            }
                            // ファイル名に上記名称が含まれている
                            if (!name.equals("") && file.getName().contains(name)) {
                                // 処理予約テーブルに設定(update or insert)
                                setWorkTable_csv(fileDevId, entry.getValue().reqCommandName, fileUpdateDay, file.getPath());
                                smCtrlLogger.info("setRequestFromCSV() devId=" + fileDevId
                                        + ",command=" + entry.getValue().reqCommandName
                                        + ",path=" + file.getPath());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 応答電文から応答CSVファイルを作成する
     *
     * @param String   devId
     * @param Document doc
     * @return
     */
    public void createCsvResponse(String devId, Document doc, TWorkHst workHst) {
        if (doc == null) {
            errorLogger.info("createCsvResponse() return doc=null");
            return;
        }
        // domからmap生成
        Map<String, String> nodeMap = new HashMap<>();
        allNodeSearchToMap(doc.getDocumentElement(), nodeMap);
        // mapから必要な属性を取得
        String codeName = nodeMap.get(DeviceCtrlConstants.code);
        String requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);

        // 装置情報のホームディレクトリ設定から格納パスを取得
        Map<String, String> filePath = makeFilePath(devId);
        // ファイルパスを取得
        String delFilepath = workHst.getFilePath();

        // 処理予約テーブルの処理フラグチェック
        if (workHst.getSrvEnt() == null) {
            return;
        }
        if (workHst.getSrvEnt().equals("2")) {
            // 処理予約テーブル更新2→8
            workHst = updateWorkHst(workHst, "8");
        }
        if (!workHst.getSrvEnt().equals("8")) {
            return;
        }

        smCtrlLogger.info("createCsvResponse() outputCSV Start "
                + "devId=" + devId
                + ",command=" + codeName);

        // 応答電文毎にCSVデータ作成
        List<String> csvList = new ArrayList<>();
        Boolean check = false;
        switch (codeName) {
        // [MuDM2] ロードサーベイ日データ
        case DeviceCtrlConstants.getDlsdata:
            check = chkCommandDate_csv(codeName, devId, requestDate, delFilepath);
            if (check) {
                csvList = createCsvDlsData(devId, requestDate, doc);
            }
            break;
        // [MuDM2] 日30分指針値データ任意要求
        case DeviceCtrlConstants.getDmvdata:
            check = chkCommandDate_csv(codeName, devId, requestDate, delFilepath);
            if (check) {
                csvList = createCsvDmvData(devId, requestDate, doc);
            }
            break;
        // [MuDM2] [MuDM2] メータ現在値データ
        case DeviceCtrlConstants.getMeterval:
            check = chkCommandDate_csv(codeName, devId, nodeMap.get(DeviceCtrlConstants.meterMngId), delFilepath);
            if (check) {
                csvList = createCsvMeterVal(devId, nodeMap);
            }
            break;
        // [MuDM2] メータ一覧データ
        case DeviceCtrlConstants.meterList:
            check = true;
            csvList = createCsvMeterList(devId, doc);
            break;
        // [MuDM2] メータ情報個別データ
        case DeviceCtrlConstants.getMeterinfo:
            check = chkCommandDate_csv(codeName, devId, nodeMap.get(DeviceCtrlConstants.meterMngId), delFilepath);
            if (check) {
                csvList = createCsvMeterInfo(devId, nodeMap);
            }
            break;
        // [MuDM2] コンセントレータ一覧データ
        case DeviceCtrlConstants.concentList:
            check = true;
            csvList = createCsvConcentList(devId, doc);
            break;
        // [MuDM2] MU-DM2設定情報（メータ開閉設定）
        case DeviceCtrlConstants.setBreaker:
            check = chkMeterPtn3_csv(codeName, devId,
                    nodeMap.get(DeviceCtrlConstants.meterMngId),
                    nodeMap.get(DeviceCtrlConstants.mode),
                    delFilepath);
            if (check) {
                csvList = createCsvSetBreaker(devId, nodeMap);
            }
            break;
        // [MuDM2] メータ負荷制限設定
        case DeviceCtrlConstants.getMeterctrl:
            check = true;
            csvList = createCsvGetMeterCtrl(devId, nodeMap);
            break;
        // [MuDM2] メータ負荷制限設定要求
        case DeviceCtrlConstants.setLoadlimit:
            check = chkLoadLimit_csv(codeName, devId,
                    nodeMap.get(DeviceCtrlConstants.meterMngId),
                    nodeMap.get(DeviceCtrlConstants.mode),
                    nodeMap.get(DeviceCtrlConstants.loadCurrent),
                    nodeMap.get(DeviceCtrlConstants.autoInjection),
                    nodeMap.get(DeviceCtrlConstants.breakerActCount),
                    nodeMap.get(DeviceCtrlConstants.countClear),
                    delFilepath);
            if (check) {
                csvList = createCsvSetLdLimit(devId, nodeMap);
            }
            break;
        // [MuDM2] メータ追加
        case DeviceCtrlConstants.addMeter:
            check = chkMeterPtn1_csv(codeName, devId,
                    nodeMap.get(DeviceCtrlConstants.meterMngId),
                    nodeMap.get(DeviceCtrlConstants.meterId),
                    nodeMap.get(DeviceCtrlConstants.tenantId),
                    delFilepath);
            if (check) {
                csvList = createCsvAddMeter(devId, nodeMap);
            }
            break;
        // [MuDM2] メータ削除
        case DeviceCtrlConstants.delMeter:
            check = chkMeterPtn1_csv(codeName, devId,
                    nodeMap.get(DeviceCtrlConstants.meterMngId),
                    null,
                    null,
                    delFilepath);
            if (check) {
                csvList = createCsvDelMeter(devId, nodeMap);
            }
            break;
        // [MuDM2] メータID変更
        case DeviceCtrlConstants.chgMeter:
            check = chkMeterPtn2_csv(codeName, devId,
                    nodeMap.get(DeviceCtrlConstants.meterMngId),
                    nodeMap.get(DeviceCtrlConstants.oldMeterId),
                    nodeMap.get(DeviceCtrlConstants.newIfType),
                    delFilepath);
            if (check) {
                csvList = createCsvChgMeter(devId, nodeMap);
            }
            break;
        // [MuDM2] コンセントレータ登録
        case DeviceCtrlConstants.addConcent:
            check = chkConcent_csv(codeName, devId, null,
                    nodeMap.get(DeviceCtrlConstants.concentId),
                    nodeMap.get(DeviceCtrlConstants.ipAddr),
                    delFilepath);

            if (check) {
                csvList = createCsvAddContent(devId, nodeMap);
            }
            break;
        // [MuDM2] コンセントレータ削除
        case DeviceCtrlConstants.delConcent:
            check = chkConcent_csv(codeName, devId, null,
                    nodeMap.get(DeviceCtrlConstants.concentId),
                    null,
                    delFilepath);

            if (check) {
                csvList = createCsvDelContent(devId, nodeMap);
            }
            break;
        // [MuDM2] コンセントレータ初期化
        case DeviceCtrlConstants.initConcent:
            check = chkConcent_csv(codeName, devId, null,
                    nodeMap.get(DeviceCtrlConstants.concentId),
                    null,
                    delFilepath);

            if (check) {
                csvList = createCsvInitContent(devId, nodeMap);
            }
            break;
        // [MuDM2] 自動検針データ要求
        case DeviceCtrlConstants.getAinspdata:
            check = chkCommandDate_csv(codeName, devId, requestDate, delFilepath);
            if (check) {
                csvList = createCsvInspDate(devId, requestDate, doc);
            }
            break;
        // [MuDM2] 任意検針データ要求
        case DeviceCtrlConstants.getMinspdata:
            check = chkCommandDate_csv(codeName, devId, requestDate, delFilepath);
            if (check) {
                csvList = createCsvInspDate(devId, requestDate, doc);
            }
            break;
        default:
            check = true;
            break;
        }
        // コマンド毎の応答CSVデータがある
        if (csvList.size() != 0) {
            File file = new File(delFilepath);
            // ファイル保存
            String setFileName = file.getName();
            if (!setFileName.equals("")) {
                // 要求ファイルを削除
                file.delete();
                // 処理予約テーブル更新
                workHst = updateWorkHst(workHst, null);
                // 応答CSVファイル作成
                DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                csv.csvPrint(filePath.get("data"), setFileName, csvList);
                smCtrlLogger.info("createCsvResponse() ouputCSV "
                        + "devId=" + devId
                        + ",command=" + codeName
                        + ",path=" + file.getPath());
            }
        }
        // 各コマンドのチェックがNGの場合は、エラーCSV出力
        if (!check) {
            // 処理予約テーブル更新
            workHst = updateWorkHst(workHst, "9");
            // 応答CSVファイル作成
            createCsvError(devId);
            smCtrlLogger.info("createCsvResponse() outputCSV Error "
                    + "devId=" + devId
                    + ",command=" + codeName);
        }

    }

    /**
     * 応答電文から通知CSVファイルを作成する
     *
     * @param String   devId
     * @param Document doc
     * @return
     */
    public void createCsvNotify(String devId, Document doc) {
        if (doc == null) {
            return;
        }
        // domからmap生成
        Map<String, String> nodeMap = new HashMap<>();
        allNodeSearchToMap(doc.getDocumentElement(), nodeMap);
        // mapから必要な属性を取得
        String codeName = nodeMap.get(DeviceCtrlConstants.code);

        // 装置情報のホームディレクトリ設定から格納パスを取得
        Map<String, String> filePath = makeFilePath(devId);

        // 応答CSVデータリスト生成
        List<String> csvList = new ArrayList<>();

        // ファイル名生成
        Date date = new Date(mDevPrmDao.getSvDate().getTime());
        String setFileName = DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDD) + MUDM2 + devId;
        // 以下コマンドは要求CSV無いがチェックして自動でCSV応答ファイルを作成する。
        switch (codeName) {
        case DeviceCtrlConstants.sendMetererr:
            csvList = createCsvMState(devId, DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH), doc);
            setFileName += "_MSTATE.csv";
            break;
        case DeviceCtrlConstants.sendTermerr:
            csvList = createCsvTState(devId, DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), doc);
            setFileName += "_TSTATE.csv";
            break;
        case DeviceCtrlConstants.sendConcenterr:
            csvList = createCsvCState(devId, DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), doc);
            setFileName += "_CSTATE.csv";
            break;
        default:
            break;
        }
        if (csvList.size() != 0) {
            // ファイル保存
            if (!setFileName.equals("")) {
                DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                csv.csvPrint(filePath.get("data"), setFileName, csvList);
            }
        }

    }

    /**
     * エラーCSV作成
     *
     * @param String devId
     * @return
     */
    private void createCsvError(String devId) {
        // 装置情報のホームディレクトリ設定から格納パスを取得
        Map<String, String> filePath = makeFilePath(devId);

        // 応答CSVデータリスト生成
        List<String> csvList = new ArrayList<>();
        csvList = createCsvErrorData(devId);

        // ファイル名生成
        Date date = new Date(mDevPrmDao.getSvDate().getTime());
        String setFileName = DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDD) + MUDM2 + devId + "ERR.csv";
        if (csvList.size() != 0) {
            // ファイル保存
            if (!setFileName.equals("")) {
                DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                csv.csvPrint(filePath.get("data"), setFileName, csvList);
            }
        }

    }

    /**
     * 状態監視データ応答CSV作成
     *
     * @param String devId
     * @return
     */
    public void createCsvState(String devId) {
        // 装置情報のホームディレクトリ設定から格納パスを取得
        Map<String, String> filePath = makeFilePath(devId);

        // 日付生成
        Date date = new Date(mDevPrmDao.getSvDate().getTime());
        String datestr = DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDD);

        // 応答CSVデータリスト生成
        List<String> csvList = new ArrayList<>();
        csvList = createCsvDmState(devId, DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH), "1");

        // ファイル名生成
        String setFileName = datestr + MUDM2 + devId + "_DMSTATE.csv";
        if (csvList.size() != 0) {
            // ファイル保存
            if (!setFileName.equals("")) {
                DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                csv.csvPrint(filePath.get("data"), setFileName, csvList);
            }
        }
    }

    /**
     * CSV要求共通チェック
     *
     * @param String command
     * @param String devId
     * @param String requestDate
     * @param String data
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkCommandDate_csv(String command, String devId, String data, String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);
        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            // リクエスト日時のチェック
            String reqDate = replace_array(REPLASE_PTN1, "", column[1]);
            if (!data.equals(reqDate)) {
                ret = false;
            }
        }
        return ret;
    }

    /**
     * 複数文字列の変換
     *
     * @param List<String> array
     * @param String       replace
     * @param String       org
     * @return 変換後文字列
     */
    private String replace_array(List<String> array, String replace, String org) {
        String ret = org;
        for (String target : array) {
            ret = ret.replace(target, replace);
        }
        return ret;
    }

    /**
     * コンセントレータ系チェック
     *
     * @param String command
     * @param String devId
     * @param String dm2Id
     * @param String concentId
     * @param String ipAddr
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkConcent_csv(String command, String devId,
            String dm2Id, String concentId, String ipAddr,
            String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);

        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            String work = "";
            // dm2Idチェック
            if (column.length > 1) {
                work = replace_array(REPLASE_PTN2, "", column[1]);
                if (dm2Id != null && !dm2Id.equals(work)) {
                    ret = false;
                }
            }
            // コンセントIDチェック
            if (column.length > 2) {
                work = replace_array(REPLASE_PTN2, "", column[2]);
                if (concentId != null && !concentId.equals(work)) {
                    ret = false;
                }
            }
            // IPAddressチェック
            if (column.length > 3) {
                work = replace_array(REPLASE_PTN2, "", column[3]);
                if (ipAddr != null && !ipAddr.equals(work)) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * メーター系チェック1(登録、削除兼用)
     *
     * @param String command
     * @param String devId
     * @param String meter_mng_id
     * @param String meter_id
     * @param String tenant_id
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkMeterPtn1_csv(String command, String devId,
            String meter_mng_id, String meter_id, String tenant_id,
            String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);

        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            String work = "";
            // メーター管理IDチェック
            if (column.length > 1) {
                work = replace_array(REPLASE_PTN2, "", column[1]);
                if (meter_mng_id != null && !meter_mng_id.equals(work)) {
                    ret = false;
                }
            }
            // メーターIDチェック
            if (column.length > 2) {
                work = replace_array(REPLASE_PTN2, "", column[2]);
                if (meter_id != null && !meter_id.equals(work)) {
                    ret = false;
                }
            }
            // テナントIDチェック
            if (column.length > 3) {
                work = replace_array(REPLASE_PTN2, "", column[3]);
                if (tenant_id != null && !tenant_id.equals(work)) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * メーター系チェック2(メータID変更用)
     *
     * @param String command
     * @param String devId
     * @param String meter_mng_id
     * @param String old_meter_id
     * @param String new_if_type
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkMeterPtn2_csv(String command, String devId,
            String meter_mng_id, String old_meter_id, String new_if_type,
            String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);

        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            String work = "";
            // メーター管理IDチェック
            if (column.length > 1) {
                work = replace_array(REPLASE_PTN2, "", column[1]);
                if (meter_mng_id != null && !meter_mng_id.equals(work)) {
                    ret = false;
                }
            }
            // 旧メーターIDチェック
            if (column.length > 2) {
                work = replace_array(REPLASE_PTN2, "", column[2]);
                if (old_meter_id != null && !old_meter_id.equals(work)) {
                    ret = false;
                }
            }
            // タイプチェック
            if (column.length > 3) {
                work = replace_array(REPLASE_PTN2, "", column[3]);
                if (new_if_type != null && !new_if_type.equals(work)) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * メーター系チェック3(メータ開閉設定用)
     *
     * @param String command
     * @param String devId
     * @param String meter_mng_id
     * @param String mode
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkMeterPtn3_csv(String command, String devId,
            String meter_mng_id, String mode,
            String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);

        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            String work = "";
            // メーター管理IDチェック
            if (column.length > 1) {
                work = replace_array(REPLASE_PTN2, "", column[1]);
                if (meter_mng_id != null && !meter_mng_id.equals(work)) {
                    ret = false;
                }
            }
            // モードチェック
            if (column.length > 2) {
                work = replace_array(REPLASE_PTN2, "", column[2]);
                if (mode != null && !mode.equals(work)) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * メータ負荷制限設定チェック
     *
     * @param String command
     * @param String devId
     * @param String meter_mng_id
     * @param String mode
     * @param String filePath
     * @return チェック結果
     */
    private Boolean chkLoadLimit_csv(String command, String devId,
            String meter_mng_id, String mode, String load_current,
            String auto_injection, String breaker_act_count, String count_clear,
            String filePath) {
        Boolean ret = true;
        // ファイル内容取得
        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
        List<String> lineList = csv.listCsvColumn(filePath);

        if (lineList.size() != 0) {
            String[] column = lineList.get(0).split(",");
            String work = "";
            // メーター管理IDチェック
            if (column.length > 1) {
                work = replace_array(REPLASE_PTN2, "", column[1]);
                if (meter_mng_id != null && !meter_mng_id.equals(work)) {
                    ret = false;
                }
            }
            // モードチェック
            if (column.length > 2) {
                work = replace_array(REPLASE_PTN2, "", column[3]);
                if (mode != null && !mode.equals(work)) {
                    ret = false;
                }
            }
            // load_currentチェック
            if (column.length > 3) {
                work = replace_array(REPLASE_PTN2, "", column[4]);
                if (load_current != null && !load_current.equals(work)) {
                    ret = false;
                }
            }
            // auto_injectionチェック
            if (column.length > 4) {
                work = replace_array(REPLASE_PTN2, "", column[5]);
                if (auto_injection != null && !auto_injection.equals(work)) {
                    ret = false;
                }
            }
            // breaker_act_countチェック
            if (column.length > 5) {
                work = replace_array(REPLASE_PTN2, "", column[6]);
                if (breaker_act_count != null && !breaker_act_count.equals(work)) {
                    ret = false;
                }
            }
            // count_clearチェック
            if (column.length > 6) {
                work = replace_array(REPLASE_PTN2, "", column[7]);
                if (count_clear != null && !count_clear.equals(work)) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * 状態監視データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   date(yyyy/MM/dd HH:mm)
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvDmState(String devId, String date, String state) {
        List<String> ret = new ArrayList<>();
        String line = "";

        line += MUDM2 + devId + ",";
        line += date;

        ret.add(line);
        ret.add(state);
        ret.add(EOF);
        return ret;
    }

    /**
     * 状態監視データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   date(yyyy/MM/dd HH:mm)
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvErrorData(String devId) {
        List<String> ret = new ArrayList<>();
        String line = "";

        line += MUDM2 + devId+",err";

        ret.add(line);
        ret.add(EOF);
        return ret;
    }

    /**
     * 日報データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvDlsData(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.whmLsdata);
        int setMngCnt = 1;
        int setHourCnt = 0;
        String mng_id = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // メーター数と1メーター毎の日報数取得
            String chkMatch = infoMap.get(DeviceCtrlConstants.meterMngId);
            if (!mng_id.equals("") && !mng_id.equals(chkMatch)) {
                setMngCnt++;
                setHourCnt = 0;
            }
            mng_id = chkMatch;

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterId) + ",";
            line += convDate(infoMap.get(DeviceCtrlConstants.date)) + ",";
            line += infoMap.get(DeviceCtrlConstants.lsKwh);
            ret.add(line);
            setHourCnt++;
        }
        // CSVのヘッダ部分を設定
        String head = "";
        String csvdate = convDate(requestDate);
        head += MUDM2 + devId + ",";
        head += csvdate + ",";
        head += setMngCnt + ","; // meter数
        head += setHourCnt; // 1meter毎の日報数
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * 日30分指針値データ任意要求応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvDmvData(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }

        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.whmdata);
        int setMngCnt = 1;
        int setHourCnt = 0;
        String mng_id = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // メーター数と1メーター毎の日報数取得
            String chkMatch = infoMap.get(DeviceCtrlConstants.meterMngId);
            if (!mng_id.equals("") && !mng_id.equals(chkMatch)) {
                setMngCnt++;
                setHourCnt = 0;
            }
            mng_id = chkMatch;

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterId) + ",";
            line += convDate(infoMap.get(DeviceCtrlConstants.date)) + ",";
            line += infoMap.get(DeviceCtrlConstants.kwh);
            ret.add(line);
            setHourCnt++;
        }
        // CSVのヘッダ部分を設定
        String head = "";
        String csvdate = convDate(requestDate);
        head += MUDM2 + devId + ",";
        head += csvdate + ",";
        head += setMngCnt + ","; // meter数
        head += setHourCnt; // 1meter毎の日報数
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ現在値データ応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvMeterVal(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId);
        ret.add(line);

        line = "";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += convMeasureDate(nodeMap.get(DeviceCtrlConstants.measureDate)) + ",";
        line += nodeMap.get(DeviceCtrlConstants.currentKwh1) + ",";
        line += nodeMap.get(DeviceCtrlConstants.currentKwh2) + ",";
        line += nodeMap.get(DeviceCtrlConstants.momentaryPwr) + ",";
        line += nodeMap.get(DeviceCtrlConstants.voltage12) + ",";
        line += nodeMap.get(DeviceCtrlConstants.voltage13) + ",";
        line += nodeMap.get(DeviceCtrlConstants.voltage23) + ",";
        line += nodeMap.get(DeviceCtrlConstants.ampere1) + ",";
        line += nodeMap.get(DeviceCtrlConstants.ampere2) + ",";
        line += nodeMap.get(DeviceCtrlConstants.ampere3) + ",";
        line += nodeMap.get(DeviceCtrlConstants.circuitBreaker) + ",";
        line += nodeMap.get(DeviceCtrlConstants.powerFactor);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ一覧データ応答CSVデータ作成
     *
     * @param String   devId
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvMeterList(String devId, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文からメーターリストデータ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.meterData);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterId) + ",";
            line += infoMap.get(DeviceCtrlConstants.ifType);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        head += MUDM2 + devId + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ情報個別データ応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvMeterInfo(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.ifType) + ",";
        line += nodeMap.get(DeviceCtrlConstants.concentId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.tenantId);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * コンセントレータ一覧データ応答CSVデータ作成
     *
     * @param String   devId
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvConcentList(String devId, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文からメーターリストデータ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.concentData);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.dm2Id) + ",";
            line += infoMap.get(DeviceCtrlConstants.concentId) + ",";
            line += infoMap.get(DeviceCtrlConstants.ifType) + ",";
            line += infoMap.get(DeviceCtrlConstants.ipAddr);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        head += MUDM2 + devId + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ開閉設定応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvSetBreaker(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.mode);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ負荷制限状態確認応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvGetMeterCtrl(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId);
        ret.add(line);

        line = "";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.mode) + ",";
        line += nodeMap.get(DeviceCtrlConstants.loadCurrent) + ",";
        line += nodeMap.get(DeviceCtrlConstants.autoInjection) + ",";
        line += nodeMap.get(DeviceCtrlConstants.breakerActCount) + ",";
        line += nodeMap.get(DeviceCtrlConstants.countClear);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ負荷制限設定応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvSetLdLimit(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.mode) + ",";
        line += nodeMap.get(DeviceCtrlConstants.loadCurrent) + ",";
        line += nodeMap.get(DeviceCtrlConstants.autoInjection) + ",";
        line += nodeMap.get(DeviceCtrlConstants.breakerActCount) + ",";
        line += nodeMap.get(DeviceCtrlConstants.countClear);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ登録応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvAddMeter(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.tenantId);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ削除応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvDelMeter(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId);
        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータID変更（メータ交換）応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvChgMeter(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.meterMngId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.oldMeterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.newMeterId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.newIfType);

        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * コンセントレータ登録応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvAddContent(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.dm2Id) + ",";
        line += nodeMap.get(DeviceCtrlConstants.concentId) + ",";
        line += nodeMap.get(DeviceCtrlConstants.ipAddr);

        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * コンセントレータ削除応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvDelContent(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.dm2Id) + ",";
        line += nodeMap.get(DeviceCtrlConstants.concentId);

        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * コンセントレータ初期化応答CSVデータ作成
     *
     * @param String      devId
     * @param Map<String, String> nodeMap
     * @return CSVデータ
     */
    private List<String> createCsvInitContent(String devId, Map<String, String> nodeMap) {
        List<String> ret = new ArrayList<>();
        if (nodeMap == null) {
            return ret;
        }
        String line = "";

        line += MUDM2 + devId + ",";
        line += nodeMap.get(DeviceCtrlConstants.dm2Id) + ",";
        line += nodeMap.get(DeviceCtrlConstants.concentId);

        ret.add(line);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * 検針データ要求応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvInspDate(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.inspData);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterType) + ",";
            line += infoMap.get(DeviceCtrlConstants.inspVal1) + ",";
            line += infoMap.get(DeviceCtrlConstants.inspVal2) + ",";
            line += infoMap.get(DeviceCtrlConstants.multi) + ",";
            line += infoMap.get(DeviceCtrlConstants.used1) + ",";
            line += infoMap.get(DeviceCtrlConstants.used2) + ",";
            line += infoMap.get(DeviceCtrlConstants.ratio) + ",";
            line += infoMap.get(DeviceCtrlConstants.inspDate);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        String csvdate = convDate(requestDate);
        head += MUDM2 + devId + ",";
        head += csvdate + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * メータ状態監視データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvMState(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.meterError);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterId) + ",";
            line += infoMap.get(DeviceCtrlConstants.meterState);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        head += MUDM2 + devId + ",";
        head += requestDate + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * 通信端末状態監視データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvTState(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.termError);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";
            line += infoMap.get(DeviceCtrlConstants.meterMngId) + ",";
            line += infoMap.get(DeviceCtrlConstants.termState);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        head += MUDM2 + devId + ",";
        head += requestDate + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * コンセントレータ状態監視データ応答CSVデータ作成
     *
     * @param String   devId
     * @param String   requestDate
     * @param Document doc
     * @return CSVデータ
     */
    private List<String> createCsvCState(String devId, String requestDate, Document doc) {
        List<String> ret = new ArrayList<>();
        if (doc.getDocumentElement() == null) {
            return ret;
        }
        // 電文から日報データ取得
        NodeList nodeList = doc.getDocumentElement().getElementsByTagName(DeviceCtrlConstants.concentError);
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 日報データからmap生成
            Node info = nodeList.item(i);
            Map<String, String> infoMap = new HashMap<>();
            allNodeSearchToMap(info, infoMap);

            // データを1行に設定
            String line = "";

            line += "0,"; // dm2Idは常に0を返す
            line += infoMap.get(DeviceCtrlConstants.concentId) + ",";
            line += infoMap.get(DeviceCtrlConstants.ifType) + ",";
            line += infoMap.get(DeviceCtrlConstants.ipAddr) + ",";
            line += infoMap.get(DeviceCtrlConstants.concentState);
            ret.add(line);
        }
        // CSVのヘッダ部分を設定
        String head = "";
        head += MUDM2 + devId + ",";
        head += requestDate + ",";
        head += nodeList.getLength();
        ret.add(0, head);

        // EOF
        ret.add(EOF);
        return ret;
    }

    /**
     * 日付文字列(YYYYMMDD)→(YYYY/MM/DD)
     *
     * @param String date
     * @return 変換後文字列
     */
    private String convDate(String date) {
        String ret = "";
        if (date.length() >= 12) {
            ret = date.substring(0, 0 + 4) + "/" + date.substring(4, 4 + 2) + "/" + date.substring(6, 6 + 2)
                    + " " + date.substring(8, 8 + 2) + ":" + date.substring(10, 10 + 2);
        } else if (date.length() >= 8) {
            ret = date.substring(0, 0 + 4) + "/" + date.substring(4, 4 + 2) + "/" + date.substring(6, 6 + 2);
        } else if (date.length() >= 6) {
            ret = date.substring(0, 0 + 4) + "/" + date.substring(4, 4 + 2);
        }
        return ret;
    }

    /**
     * 日付文字列(YYMMDDHHMMSS)→(YYYY/MM/DD HH:MM:SS)
     *
     * @param String date
     * @return 変換後文字列
     */
    private String convMeasureDate(String date) {
        String ret = "";
        if (date.length() >= 12) {
            ret = "20" + date.substring(0, 0 + 2) + "/" + date.substring(2, 2 + 2) + "/" + date.substring(4, 4 + 2) +
                    " " + date.substring(6, 6 + 2) + ":" + date.substring(8, 8 + 2) + ":" + date.substring(10, 10 + 2);
        }
        return ret;
    }

    /**
     * mapに要素を入れていく
     *
     * @param n
     * @param nodeMap
     */
    private void allNodeSearchToMap(Node n, Map<String, String> nodeMap) {
        // Node n の最初の子Nodeを取得 => ch
        Node ch = n.getFirstChild();

        // chがnull（子Nodeが取得できないか、NextSiblingがない）でない間ループ
        while (ch != null) {
            if (ch.getNodeName() != null || !ch.getNodeName().equals("#text")) {
                nodeMap.put(ch.getNodeName(), ch.getTextContent());
            }
            // 自メソッドを再帰的に呼び出す
            allNodeSearchToMap(ch, nodeMap);
            // Node ch の弟Nodeを取得 => ch
            ch = ch.getNextSibling();
        }
    }

    /**
     * 所定フォルダ内のCSVを解析して処理予約テーブルに追加.
     *
     * @param String devId
     * @return 格納フォルダ情報
     */
    private Map<String, String> makeFilePath(String devId) {
        Map<String, String> ret = new HashMap<String, String>();
        MDevPrm target = new MDevPrm();
        target.setDevId(devId);
        MDevPrm resInfo = mDevPrmDao.find(target);
        String filePathSet = "";
        String filePathData = "";
        if (resInfo != null) {
            if (resInfo.getDevKind() != null && resInfo.getDevKind().equals("1")) {
                if (resInfo.getHomeDirectory() != null) {
                    filePathSet = resInfo.getHomeDirectory() + "/mudm2/set/";
                    filePathData = resInfo.getHomeDirectory() + "/mudm2/data/";
                } else {
                    filePathSet = "/tmp/mudm2/set/";
                    filePathData = "/tmp/mudm2/data/";
                }
            }
        }
        smCtrlLogger.info("filePathSet" + filePathSet);
        smCtrlLogger.info("filePathData" + filePathData);
        ret.put("set", filePathSet);
        ret.put("data", filePathData);
        return ret;
    }

    /**
     * 処理予約テーブル更新、追加処理.
     *
     * @param String devId
     * @param String command
     * @param String setDate
     * @param String name
     * @return
     */
    private void setWorkTable_csv(String devId, String command, String setDate, String name) {
        // 実行時刻取得
        Timestamp svDate = tWorkHstCsvDao.getSvDate();
        // 時刻文字列からdate変換
        Date recDate = DateUtility.conversionDate(setDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH);

        // entity設定
        TWorkHst wheretarget = new TWorkHst();
        TWorkHst target = new TWorkHst();
        TWorkHstPK pk = new TWorkHstPK();
        pk.setDevId(devId);
        pk.setCommand(command);
        target.setId(pk);
        target.setFilePath(name);
        wheretarget.setId(pk);
        wheretarget.setFilePath(name);

        // 検索条件ではないが更新、挿入で使用する為設定
        target.getId().setRecDate(recDate);
        target.setRecMan("setWorkTable_csv");
        target.setSrvEnt("1");
        target.setWriteDate(svDate);

        // 更新か挿入か判断する為セレクト
        List<TWorkHst> resList = tWorkHstCsvDao.getTWorkHstList(wheretarget);
        if (resList.size() == 0) {
            // ないので挿入
            target.setCreateUserId(0L);
            target.setCreateDate(svDate);
            target.setUpdateUserId(0L);
            target.setUpdateDate(svDate);
            tWorkHstCsvDao.persist(target);
        } else {
            // ある場合、DBの日付とファイル更新日付が異なる場合は更新
            Date orgDate = resList.get(0).getId().getRecDate();
            if (orgDate.getTime() != recDate.getTime()) {
                target.setUpdateDate(svDate);

                Map<String, List<Object>> param = new HashMap<>();
                List<Object> targetList = new ArrayList<Object>();
                targetList.add(target);
                param.put("target", targetList);
                tWorkHstCsvDao.updateTWorkHstList(param);
            }
        }
    }

    /**
     * 処理予約テーブルフラグ更新処理.
     *
     * @param TWorkHst workHst
     * @param String   setSrvEnt
     * @return
     */
    private TWorkHst updateWorkHst(TWorkHst workHst, String setSrvEnt) {
        // 実行時刻取得
        Timestamp svDate = tWorkHstCsvDao.getSvDate();
        workHst.setSrvEnt(setSrvEnt);
        workHst.setWriteDate(svDate);
        workHst.setUpdateDate(svDate);
        return tWorkHstCsvDao.merge(workHst);
    }
}
