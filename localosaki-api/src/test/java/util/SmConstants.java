package util;

public class SmConstants {

    public static final boolean IS_STUB = true;

    // ローカルテスト用
    public static final String SERVER_URL = "http://localhost:8080/api/json";
    public static final String PARAM_CORPID = "loginCorpId=fdsa";
    public static final String PARAM_PERSONID = "loginPersonId=ogawa";
    public static final String PARAM_OPECORPID = "operationCorpId=fdsa";

    // 実機テスト用
//	public static final String SERVER_URL = "https://dev.o-sol.jp/api/json";
//	public static final String PARAM_CORPID = "loginCorpId=iwasetest000";
//	public static final String PARAM_PERSONID = "loginPersonId=Sky1";




    // httpリクエスト構成用文字列
    public static final String PARAM_BEAN = "bean";
    public static final String PARAM_SMID = "smId";
    public static final String PARAM_SETTING_CHANGE_HIST = "settingChangeHist";
    public static final String PARAM_PAGE_ASSIGNMENT = "pageAssignment";
    public static final String PARAM_DATA_HIST = "dataHist";
    public static final String PARAM_UPDATE_DB_FLG = "updateDBflg";
    public static final String PARAM_RESULT_SET = "result";
    public static final String PARAM_SM_ID_LIST = "smIdList";
    public static final String PARAM_REPORT_HIST = "reportHist";
    public static final String PARAM_LOAD_CTRL_ASSIGNMENT = "loadCtrlAssignment";
    public static final String PARAM_COMMAND = "commandParameter";
    public static final String PARAM_MEASURE_POINT_LIST = "measurePointList";
    public static final String PARAM_TARGET_DATE = "targetDate";
    public static final String PARAM_SELECT_TARGET = "selectTarget";
    public static final String PARAM_HIST = "hist";
    public static final String PARAM_ANY_MESSAGE = "anyMessage";
    public static final String PARAM_IP_ADDRESS = "ipAddress";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_SETTING_CONDITION = "settingCondition";
    public static final String PARAM_TARGET_POWER_LIST = "targetPowerList";
    public static final String PARAM_COOLING_LIST = "coolingList";
    public static final String PARAM_HEATING_LIST = "heatingList";
    public static final String PARAM_SCHEDULE_LIST = "scheduleList";
    public static final String PARAM_SCHEDULE_CONTROL_INFO = "scheduleControlInfo";
    public static final String PARAM_MEASURE_POINT_INFO = "measurePointInfo";
    public static final String PARAM_DATETIME = "dateTime";

    // テストデータ取得用
    public static final String TEST_DATA_DIRECTORY = "\\src\\test\\resources\\data\\";
    public static final String TEST_PROPERTY_DIRECTORY = "\\src\\test\\resources\\property\\";
    public static final String INPUT_FILE = "_req";
    public static final String OUTPUT_FILE = "_res";
    public static final String SM_RESPONSE_FILE = "_stub";
    public static final String TEST_LOG_DIR = "\\log";
    public static final String LOG_EXTENSION = ".log";
    public static final String SM_SEND_FILE = "_send";
    public static final String SM_RECV_FILE = "_recv";
    public static final String PROPERTY_EXTENSION = ".properties";

    // ログファイル内,時間フォーマット
    public static final String LOG_TIME_FORMAT = "[yyyy/MM/dd HH:mm:ss] ";


    // 機種判別用
    public static final String SM_KIND = "smKind";
    // 結果取得用
    public static final String RESULT_CODE = "resultCd";
    public static final String JSON_RESULT = "strJson";


    // 各機能ID
    public static final String A200001_TEST = "A200001";
    public static final String A200002_TEST = "A200002";
    public static final String A200003_TEST = "A200003";
    public static final String A200004_TEST = "A200004";
    public static final String A200005_TEST = "A200005";
    public static final String A200006_TEST = "A200006";
    public static final String A200007_TEST = "A200007";
    public static final String A200008_TEST = "A200008";
    public static final String A200009_TEST = "A200009";
    public static final String A200010_TEST = "A200010";
    public static final String A200011_TEST = "A200011";
    public static final String A200012_TEST = "A200012";
    public static final String A200013_TEST = "A200013";
    public static final String A200014_TEST = "A200014";
    public static final String A200015_TEST = "A200015";
    public static final String A200016_TEST = "A200016";
    public static final String A200017_TEST = "A200017";
    public static final String A200018_TEST = "A200018";
    public static final String A200019_TEST = "A200019";
    public static final String A200020_TEST = "A200020";
    public static final String A200021_TEST = "A200021";
    public static final String A200022_TEST = "A200022";
    public static final String A200023_TEST = "A200023";
    public static final String A200024_TEST = "A200024";
    public static final String A200025_TEST = "A200025";
    public static final String A200026_TEST = "A200026";
    public static final String A200027_TEST = "A200027";
    public static final String A200028_TEST = "A200028";
    public static final String A200029_TEST = "A200029";
    public static final String A200030_TEST = "A200030";
    public static final String A200031_TEST = "A200031";
    public static final String A200032_TEST = "A200032";
    public static final String A200033_TEST = "A200033";
    public static final String A200034_TEST = "A200034";
    public static final String A200035_TEST = "A200035";
    public static final String A200036_TEST = "A200036";
    public static final String A200037_TEST = "A200037";
    public static final String A200038_TEST = "A200038";
    public static final String A200039_TEST = "A200039";
    public static final String A200040_TEST = "A200040";
    public static final String A200041_TEST = "A200041";
    public static final String A200042_TEST = "A200042";
    public static final String A200043_TEST = "A200043";
    public static final String A200044_TEST = "A200044";
    public static final String A200045_TEST = "A200045";
    public static final String A200046_TEST = "A200046";
    public static final String A200047_TEST = "A200047";
    public static final String A200048_TEST = "A200048";
    public static final String A200049_TEST = "A200049";
    public static final String A200050_TEST = "A200050";
    public static final String A200051_TEST = "A200051";
    public static final String A200052_TEST = "A200052";
    public static final String A200053_TEST = "A200053";
    public static final String A200054_TEST = "A200054";
    public static final String A200055_TEST = "A200055";
    public static final String A200056_TEST = "A200056";
    public static final String A200057_TEST = "A200057";
    public static final String A200058_TEST = "A200058";
    public static final String A200059_TEST = "A200059";
    public static final String A200060_TEST = "A200060";
    public static final String A200061_TEST = "A200061";
    public static final String A200062_TEST = "A200062";
    public static final String A200101_TEST = "A200101";
    public static final String A210001_TEST = "A210001";
    public static final String A210002_TEST = "A210002";
    public static final String A210003_TEST = "A210003";
    public static final String A210004_TEST = "A210004";
    public static final String A210005_TEST = "A210005";
    public static final String A210006_TEST = "A210006";


    /* *** SM_STUB *** */
    public static final int UDP_LENGTH = 1400;
    public static final int SM_TIMEOUT = 10000 * 10;
    public static final int SM_HEAD_LENGTH = 9;

    /* *** BULK_DUMMY_PATTERN *** */
    public static final String DUMMY_PATTERN_1 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:1,settingChangeHist:0,updateDBflg:TRUE}]";
    public static final String DUMMY_PATTERN_2 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:2,settingChangeHist:0,updateDBflg:TRUE}]";
    public static final String DUMMY_PATTERN_3 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:3,settingChangeHist:0,updateDBflg:TRUE}]";
    public static final String DUMMY_PATTERN_4 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:4,settingChangeHist:0,updateDBflg:TRUE}]";
    public static final String DUMMY_PATTERN_5 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:5,settingChangeHist:0,updateDBflg:TRUE}]";
    public static final String DUMMY_PATTERN_6 = "bean" + "="+ "BulkTargetPowerSelect&smIdList" + "=" + "[{smId:6,settingChangeHist:0,updateDBflg:TRUE}]";

}
