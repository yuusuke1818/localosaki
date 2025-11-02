package jp.co.osaki.sms.bean.deviceCtrl;

import java.math.BigDecimal;

public class DeviceCtrlConstants {

    //MUDM2
    public final static String addRepeater  = "add-repeater";
    public final static String delRepeater = "del-repeater";
    public final static String addConcent = "add-concent";
    public final static String delConcent = "del-concent";
    public final static String initConcent = "init-concent";
    public final static String concentList = "concent-list";
    public final static String repeaterList = "repeater-list";
    public final static String addMeter = "add-meter";
    public final static String delMeter = "del-meter";
    public final static String chgMeter = "chg-meter";
    public final static String setSpitconf = "set-spitconf";
    public final static String meterList = "meter-list";
    public final static String getMeterinfo = "get-meterinfo";
    public final static String getSpitconf = "get-spitconf";
    public final static String getMeterval = "get-meterval";
    public final static String setBreaker = "set-breaker";
    public final static String setLoadlimit = "set-loadlimit";
    public final static String getMeterctrl = "get-meterctrl";
    public final static String getMvalfile = "get-mvalfile";
    public final static String getDlsfile  = "get-dlsfile";
    public final static String getMlsfile = "get-mlsfile";
    public final static String getDlsdata = "get-dlsdata";
    public final static String getRdlsdata = "get-rdlsdata";
    public final static String getDemdata = "get-demdata";
    public final static String sendMetererr = "send-metererr";
    public final static String sendTermerr = "send-termerr";
    public final static String sendConcenterr = "send-concenterr";
    public final static String getAutoinsp = "get-autoinsp";
    public final static String setAutoinsp = "set-autoinsp";
    public final static String getDemconf = "get-demconf";
    public final static String setDemconf = "set-demconf";
    public final static String sendDemalarm = "send-demalarm";
    public final static String getDmvdata = "get-dmvdata";
    public final static String getRdmvdata = "get-rdmvdata";
    public final static String getDemalarm  = "get-demalarm";
    public final static String manualInspect = "manual-inspect";
    public final static String getAinspdata = "get-ainspdata";
    public final static String getMinspdata = "get-minspdata";
    public final static String getAinspfile = "get-ainspfile";
    public final static String getRainspfile = "get-rainspfile";
    public final static String getMinspfile = "get-minspfile";
    public final static String getRminspfile = "get-rminspfile";
    public final static String getDemfile = "get-demfile";

    //テーブル名
    public final static String m_meter = "M_METER";
    public final static String m_concentrator = "M_CONCENTRATOR";
    public final static String m_repeater = "M_REPEATER";
    public final static String m_meter_loadlimit = "M_METER_LOADLIMIT";
    public final static String t_command = "T_COMMAND";
    public final static String t_command_load_survey_meter = "T_COMMAND_LOAD_SURVEY_METER";
    public final static String t_command_load_survey_time = "T_COMMAND_LOAD_SURVEY_TIME";
    public final static String m_auto_insp = "M_AUTO_INSP";

    public final static String fPathCsv = "/home/wildfly/osol/tempfile/";

    public final static String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";


    //MUDM3
    public final static String getDlsmeter = "get-dlsmeter";
    public final static String getDlsperiod = "get-dlsperiod";
    public final static String getRdlsmeter = "get-rdlsmeter";
    public final static String getRdlsperiod = "get-rdlsperiod";
    public final static String getDmvmeter = "get-dmvmeter";
    public final static String getDmvperiod = "get-dmvperiod";
    public final static String getRdmvmeter = "get-rdmvmeter";
    public final static String getRdmvperiod = "get-rdmvperiod";
    public final static String setInterval = "set-interval";

    //ハンディ端末
    public final static String getRfmeterinfo = "get-rfmeterinfo";
    public final static String sendRfmeterinfo = "send-rfmeterinfo";
    public final static String setRfmeterinfo = "set-rfmeterinfo";

    //LTE端末
    public final static String sendMeteradd = "send-meteradd";	// 2023-12-25
    public final static String fwVersion = "ver";

    //dom解析・生成用
    public final static String codeS = "<code>";
    public final static String codeE = "</code>";
    public final static String authS = "<auth>";
    public final static String authE = "</auth>";

    public final static String code = "code";
    public final static String contents = "contents";
    public final static String auth = "auth";
    public final static String date = "date";
    public final static String command = "command";
    public final static String data = "data";
    public final static String error = "error";

    //共通
    public final static String dev_id = "dev_id";
    public final static Long create_userId = (long) 0;
    public final static Long update_userId = (long) 0;

    //コンセントレーター
    public final static String concentData = "concent_data";
    public final static String dm2Id = "dm2_id";
    public final static String concentId = "concent_id";
    public final static String ifType = "if_type";
    public final static String ipAddr = "ip_addr";
    public final static String concentError = "concent_error";
    public final static String concentState = "concent_state";

    //メーター
    public final static String meterMngId = "meter_mng_id";
    public final static String meterId = "meter_id";
    public final static String tenantId = "tenant_id";
    public final static String oldMeterId = "old_meter_id";
    public final static String newMeterId = "new_meter_id";
    public final static String newIfType = "new_if_type";
    public final static String meterData = "meter_data";
    public final static String multi = "multi";
    public final static String mode = "mode";
    public final static String measureDate = "measure_date";
    public final static String currentKwh1 = "current_kwh1";
    public final static String currentKwh2 = "current_kwh2";
    public final static String momentaryPwr = "momentary_pwr";
    public final static String voltage12 = "voltage12";
    public final static String voltage13 = "voltage13";
    public final static String voltage23 = "voltage23";
    public final static String ampere1 = "ampere1";
    public final static String ampere2 = "ampere2";
    public final static String ampere3 ="ampere3";
    public final static String circuitBreaker = "circuit_breaker";
    public final static String powerFactor = "power_factor";
    public final static String loadCurrent = "load_current";
    public final static String autoInjection = "auto_injection";
    public final static String breakerActCount = "breaker_act_count";
    public final static String countClear = "count_clear";
    public final static String lsKwh = "ls_kwh";
    public final static String kwh = "kwh";
    public final static String inspData = "insp_data";
    public final static String meterType = "meter_type";
    public final static String inspVal1 = "insp_val1";
    public final static String inspVal2 = "insp_val2";
    public final static String used1 = "used1";
    public final static String used2 = "used2";
    public final static String ratio = "ratio";
    public final static String inspDate = "insp_date";
    public final static String ratioNotCalc = "---.--";
    public final static String meterError = "meter_error";
    public final static String meterState = "meter_state";
    public final static String startTime = "start_time";
    public final static String endTime = "end_time";
    public final static String pulseWeight = "pulse_weight";
    public final static String pulseType = "pulse_type";
    public final static String currentData = "current_data";
    public final static String interval = "interval";
    public final static String dataCount = "data_count";
    public final static String name = "name";
    public final static String address1 = "address1";
    public final static String address2 = "address2";
    public final static String wirelessId = "wireless_id";
    public final static String hop1Id = "hop1_id";
    public final static String hop2Id = "hop2_id";
    public final static String hop3Id = "hop3_id";
    public final static String pollingId = "polling_id";
    public final static String alarm = "alarm";
    public final static String latestInspVal = "latest_insp_val";
    public final static String latestInspDate = "latest_insp_date";
    public final static String prevInspVal = "prev_insp_val";
    public final static String prevInspDate = "prev_insp_date";
    public final static String inspPoint = "insp_point";
    public final static String emptyRatio = "---.--";
    public final static String busyCode = "busy";


    public final static String what = "what";
    public final static String none = "none";
    public final static String noneWithVersion = "none-with-version";
    public final static String end = "end";


    //通信端末
    public final static String termState = "term_state";
    public final static String termError = "term_error";


    //日付
    public final static String requestDate = "request_date";

    //中継装置
    public final static String repeaterMngId = "repeater_mng_id";
    public final static String repeaterId = "repeater_id";
    public final static String repeaterData = "repeater_data";

    //エラーコード
    public final static String busy = "00";
    public final static String commandErr = "01";
    public final static String syntaxErr = "02";
    public final static String boundaryValueErr = "03";
    public final static String noData = "04";
    public final static String ftpErr = "10";
    public final static String devErr = "20";
    public final static String err21 = "21";
    public final static String InternalErr = "70";

    //コマンドフラグなどで使う
    public final static Integer zero = 0;
    public final static Integer one = 1;
    public final static Integer two = 2;
    public final static Integer three = 3;
    public final static Integer four = 4;
    public final static Integer five = 5;
    public final static Integer eight = 8;
    public final static Integer nine = 9;
    public final static Integer twelve = 12;


    //REC_MAN 機器制御用
    public final static String concentratorRecMan = "setConcentState";
    public final static String meterRecMan = "setMeter";
    public final static String mDevPrmRecMan = "setDevPrm";
    public final static String repeaterRecMan = "setRepeater";
    public final static String inspMeterRecMan = "setInspMeter";
    public final static String setManualInspRecMan = "setManualInsp_flg";
    public final static String firstConnection = "firstConnection";	// 2023-12-27
    public final static String lteAutoRegist = "lteAutoRegist";

    public final static String whmLsdata = "whm_lsdata";
    public final static String whmdata = "whm_data";

    public final static String a = "a";
    public final static String m = "m";
    public final static String A = "A";
    public final static String P = "P";
    public final static String R = "R";

    //文字列操作用
    public final static String coron = ":";
    public final static String percent ="%";

    //Log用
    public final static String SmControl = "SmControlLog";

    //コンセントレーターIDの境界値
    public final static int concentMin = 1;
    public final static int concentMax = 16;

    //メーターの閾値
    public final static int meterMin = 1;
    public final static int meterMax = 2048;

    //中継装置の閾値
    public final static int repeaterMin = 1;
    public final static int repeaterMax = 99;

    //ratio境界値
    public final static BigDecimal ratioMin = new BigDecimal("0.00");
    public final static BigDecimal ratioMax = new BigDecimal("999.99");

    public final static String normal = "正常復帰";
    public final static String abnormal = "異常発生";

    //メールタイトル
    public final static String mailMeterTitle = "SMSメーター異常通知メール";
    public final static String mailTermTitle = "SMS通信端末異常通知メール";
    public final static String mailConcentTitle = "SMSコンセントレーター異常通知メール";

    //dm2_id固定で返す
    public final static String dm2IdConstant = "1";

    //spaceで返すスマートパルス返信用
    public final static String spFiveConstant = "     ";
    public final static String spEightConstant = "        ";
    public final static String spOneConstant = " ";
    public final static String spTwoConstant = "  ";
    public final static String spThreeConstant = "   ";
    public final static String empty = "";


    //API resultCoce
    public final static String apiSuccess = "00000";

    //メッセージ
    public final static String alertDateMessage = "指定日時なし";




}
