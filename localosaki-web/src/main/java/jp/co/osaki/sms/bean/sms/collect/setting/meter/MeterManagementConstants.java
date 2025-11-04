package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;

/**
 * @author kimura.m
 * メーター管理画面関連APIで使用する定数をまとめたクラス
 */
public class MeterManagementConstants {

    /** フラグON */
    public static final String FLG_ON = "1";

    /** フラグOFF */
    public static final String FLG_OFF = "0";

    /** 開閉器動作カウントプルダウン最小値 */
    public static final int MIN_OCCOUNT = 0;

    /** 開閉器動作カウントプルダウン最大値 */
    public static final int MAX_OCCOUNT = 9;

    /** 開閉器カウントクリアプルダウン最小値 */
    public static final int MIN_CNTCLR = 0;

    /** 開閉器カウントクリアプルダウン最大値 */
    public static final int MAX_CNTCLR = 60;

    /** メーター管理番号 登録可能最小値 */
    public static final Long METERM_MNG_ID_MIN = new Long(1);
    public static final int METERM_MNG_ID_INT_MIN = 1;

    /** メーター管理番号 登録可能最大値 */
    public static final Long METERM_MNG_ID_MAX = new Long(512);
    public static final int METERM_MNG_ID_INT_MAX = 512;

    /** 検満年月の元号 選択値（表示） 平成 */
    public static final String DISP_YEAR_GENGO_H = "H";

    /** 検満年月の元号 選択値（表示） 令和 */
    public static final String DISP_YEAR_GENGO_R = "R";

    /** 検満年月の元号 選択値（コード） 平成 */
    public static final String DISP_YEAR_GENGO_CODE_H = "1";

    /** 検満年月の元号 選択値（コード） 令和 */
    public static final String DISP_YEAR_GENGO_CODE_R = "2";

    /** コマンドフラグMap(表示用） */
    public static final Map<String, String> COMMAND_FLG_DISP_MAP = new HashMap<String, String>() {
        {
            put(null, "");
            put("1", "登録");
            put("2", "削除");
            put("3", "交換");
            put("4", "開閉設定");
            put("5", "登録");
        }
    };

    /** 処理フラグ(1:処理待ち) */
    public static final String SRV_ENT_REQUESTING = "1";
    /** 処理フラグ(9:エラー) */
    public static final String SRV_ENT_ERROR = "9";
    /** 処理フラグMap(表示用） */
    public static final Map<String, String> SRV_ENT_DISP_MAP = new HashMap<String, String>() {
        {
            put(null, "");
            put(SRV_ENT_REQUESTING, "要求中");
            put(SRV_ENT_ERROR, "失敗");
        }
    };

    // 入力チェック時に使用
    public static final String CHECK_PATTERN_NUM = "^[0-9]+$";
    public static final String CHECK_PATTERN_ALPHABET_NUM = "^[A-Za-z0-9]+$";
    public static final String CHECK_PATTERN_ALPHABET_NUM_12_digits = "^[A-Za-z0-9]{12}$";
    public static final String CHECK_PATTERN_REGEX_EXAM_END_YM = "^(19|20)\\d{2}(0[1-9]|1[0-2])$";
    public static final String CHECK_PATTERN_EXAM_END_YM = "YYYYMM";
    public static final int CHECK_BUILDING_ID_MIN = 1;
    public static final int CHECK_BUILDING_ID_MAX = 999999;
    public static final int CHECK_METER_ID_CNT = 10;
    public static final String CHECK_METER_ID_INITIAL_A = "A";
    public static final String CHECK_METER_ID_INITIAL_P = "P";
    public static final String CHECK_METER_ID_INITIAL_R = "R";
    public static final int CHECK_METER_TYPE_MIN = 1;
    public static final int CHECK_METER_TYPE_MAX = 20;
    public static final String CHECK_OPEN_MODE_CLOSE = "切";
    public static final String CHECK_OPEN_MODE_OPEN = "入";
    public static final String CHECK_LOAD_CURRENT = "(1,3,5,8,10,15,20,30,40,50,60,70,80)";
    public static final String CHECK_AUTO_INJECTION_ZERO = "0";
    public static final int CHECK_AUTO_INJECTION_MIN = 5;
    public static final int CHECK_AUTO_INJECTION_MAX = 300;
    public static final String CHECK_TEMP_AUTO_INJECTION_ZERO = "0";
    public static final int CHECK_TEMP_AUTO_INJECTION_MIN = 5;
    public static final int CHECK_TEMP_AUTO_INJECTION_MAX = 300;
    public static final int CHECK_PULSE_WEIGHT_MIN = 1;
    public static final int CHECK_PULSE_WEIGHT_MAX = 99999;
    public static final int CHECK_MULTI_MIN = 1;
    public static final int CHECK_MULTI_MAX = 9999;
    public static final int CHECK_CURRENT_DATA_MIN = 1;
    public static final int CHECK_CURRENT_DATA_MAX = 99999999;
    public static final int CHECK_BREAKER_ACT_COUNT_MIN = 0;
    public static final int CHECK_BREAKER_ACT_COUNT_MAX = 9;
    public static final int CHECK_TEMP_BREAKER_ACT_COUNT_MIN = 0;
    public static final int CHECK_TEMP_BREAKER_ACT_COUNT_MAX = 9;
    public static final int CHECK_COUNT_CLEAR_MIN = 0;
    public static final int CHECK_COUNT_CLEAR_MAX = 60;
    public static final int CHECK_TEMP_COUNT_CLEAR_MIN = 0;
    public static final int CHECK_TEMP_COUNT_CLEAR_MAX = 60;
    public static final int CHECK_WIRELESS_ID = 12;
    public static final int CHECK_MEMO_CNT = 20;

    /** スマートメーター */
    public static final String METER_TYPE_SMART = "A";
    /** パルスメーター */
    public static final String METER_TYPE_PULSE = "P";
    /** IoT-R連携用メーター */
    public static final String METER_TYPE_IOTR = "TW";
    /** ハンディ端末 */
    public static final String METER_TYPE_HANDY = "MH";

    /** 更新タイプ 登録内容 */
    public static final String UPDATE_TYPE_REGIST = "1";
    /** 更新タイプ 設定内容 */
    public static final String UPDATE_TYPE_SETTING = "2";

    /** 計器種別Map(表示用） */
    public static final Map<String, String> IF_TYPE_DISP_MAP = new HashMap<String, String>() {
        {
            put("1", "HEM5");
            put("2", "TEM");
        }
    };

    /** 開閉区分Map(表示用） */
    public static final Map<String, String> CIRCUIT_BREAKER_DISP_MAP = new HashMap<String, String>() {
        {
            put("0", "入→切");
            put("1", "切→入");
            put("2", "切→切");
            put("3", "入→入");
            put("4", "入→切(過電流)");
            put("5", "切→入(自動投入)");
        }
    };
    /** 開閉区分_その他(表示用） */
    public static final String CIRCUIT_BREAKER_OTHER = "機能なし";

    // 現在値データなしの場合に画面に表示するメッセージ用文言
    /** スマートメーター */
    public static final String METER_TYPE_SMART_DISP = "スマートメーター";
    /** パルスメーター */
    public static final String METER_TYPE_PULSE_DISP = "パルスメーター";

    /** アラート停止状態Map（表示用） */
    public static final Map<Integer, String> ALERT_PAUSE_DISP_MAP = new HashMap<Integer, String>() {
        {
            put(0, "");
            put(1, "停止中");
        }
    };

    // アップデート時にファイル保存するディレクトリ名
    /** スマートメーター */
    public static final String UPDATE_DIRECTORY_SMART = "smartMeter";
    /** パルスメーター */
    public static final String UPDATE_DIRECTORY_PULSE = "pulseMeter";
    /** IoT-R連携用メーター */
    public static final String UPDATE_DIRECTORY_IOTR = "iotrMeter";
    /** ハンディ端末 */
    public static final String UPDATE_DIRECTORY_HANDY = "handyMeter";
    /**
     * AieLink用メーター
     * 「OCR検針」→「AieLink」へ変更
     */
    public static final String UPDATE_DIRECTORY_OCR = "ocrMeter";

    /** 「LTE-M」装置の定数 */
    // TODO 中村　最終的にLTに変更する
//    public static final String LTE_M_DEVICE_PREFIX = "AQ";
    public static final String LTE_M_DEVICE_PREFIX = SmsConstants.DEVICE_KIND.LTEM.getVal();
    // TODO 中村　最終的に610に変更する
    /** 装置「LTE-M」の機能コード */
//    public static final String LTE_M_DEVICE_FUNCTION_CODE = "500";
    public static final String LTE_M_DEVICE_FUNCTION_CODE = "610";
    /** LTE-Mの開閉制御APIと負荷制限APIのBASIC認証の値*/
    public static final String LTEM_AUTHORIZATION = "LTEM_AUTHORIZATION";
    /** LTE-Mの企業ID*/
    public static final String LTEM_CORP_ID = "LTEM_CORP_ID";
    /** LTE-MのユーザーID*/
    public static final String LTEM_PERSON_ID = "LTEM_PERSON_ID";

    /** 一括登録の開閉制御APIと負荷制限APIの同時実行数 HTTPクライアントのコネクションプール上限：HttpClientBuilder.build()参照*/
    public static final int MAX_CONCURRENCY_LUMP_REGIST_API = 10;
    /** 一括登録の同時実行数の枠が空くのを待つ時間(秒) */
    public static final int SEMAPHORE_TIMEOUT_LUMP_REGIST_API = 60;

    /** 開閉制御APIと負荷制限API実行時タイムアウト時間 */
    public static final long TIMEOUT_SEC = 300;
    /** 「開閉制御APIと負荷制限API実行時」および「設定変更にて負荷制限「なし」に設定した際の負荷制限API2回実行時」の遅延時間  例）開閉制御APIが実行された後に設定された時間が空いて負荷制限APIが実行される*/
    public static final long DELAY_TIME = 10;
    /** 開閉制御API名 */
    public static final String SWITCH_API_NAME = "LTE-M開閉制御API";
    /** 開閉制御APIのURL */
    public static final String LTEM_SWITCH_API_EXEC_URL = "LTEM_SWITCH_API_EXEC_URL";

    /** 開閉制御APIの定数 */
    public static final String SWITCH_API = "switch";

    /** 負荷制限API名 */
    public static final String LOADLIMIT_API_NAME = "LTE-M負荷制限API";
    /** 負荷制限APIのURL */
    public static final String LTEM_LOADLIMIT_API_EXEC_URL = "LTEM_LOADLIMIT_API_EXEC_URL";
    /** 負荷制限APIの定数 */
    public static final String LOADLIMIT_API = "loadlimit";

    /** LTE-Mの場合、定格電流ごとの上限（この上限以上ならエラー）判定に使用 　設定例：RC_30(new BigDecimal("30"), Mode.LIMIT, new BigDecimal("30"))*/
    public enum RATED_CURRENT_CHECK {
        RC_5(new BigDecimal("5"),   Mode.FORBID_ALL, null),
        RC_30(new BigDecimal("30"), Mode.LIMIT, new BigDecimal("30")), // 定格電流30A → 負荷電流30A以上でエラー
        RC_60(new BigDecimal("60"), Mode.ALLOW_ALL, null), // 定格電流60A → 負荷電流80A以上でエラーのため制限解除
        RC_120(new BigDecimal("120"), Mode.ALLOW_ALL, null), // 定格電流120A → 負荷電流80A以上でエラーのため制限解除
        RC_250(new BigDecimal("250"), Mode.FORBID_ALL, null);

        public enum Mode { FORBID_ALL, LIMIT, ALLOW_ALL } // 全禁止, 制限判定, 全許可

        private final BigDecimal rated; // 定格電流
        private final Mode mode;
        private final BigDecimal limit; // エラーとなる負荷電流の下限値

        RATED_CURRENT_CHECK(BigDecimal rated, Mode mode, BigDecimal limit) {
            this.rated = rated;
            this.mode  = mode;
            this.limit = limit;
        }
        public Mode mode() { return mode; }
        public BigDecimal limitLoadCurrent() { return limit; }

        public static RATED_CURRENT_CHECK fromRated(BigDecimal rated) {
            if (rated == null) return null;
            for (RATED_CURRENT_CHECK c : values()) {
                if (rated.compareTo(c.rated) == 0) return c;
            }
            return null;
        }
    }

    /** LTE-Mの場合、テーブル「m_meter_info」の開閉コード「switch_code」 */
    public enum SWITCH_CODE {
        NONE(0, "機能なし"),
        BASIC(1, "機能あり"),
        WITH_LOADLIMIT(2, "機能あり（負荷制限付き）"),
        WITH_TS(3, "機能あり（TS付き）");

        private final int code;
        private final String label;

        SWITCH_CODE(int code, String label) {
            this.code = code;
            this.label = label;
        }
        public int getCode() {
            return code;
        }
        public String getLabel() {
            return label;
        }
    }

    /** LTE-MのAPI種別 */
    public enum LTEM_API_TYPE {
        /** 同期API */
        SYNC("sync"),
        /** 設定API */
        SET("set"),
        /** 読出API */
        READOUT("readout");

        private final String code;

        LTEM_API_TYPE(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /** LTE-Mの開閉区分 */
    public enum OPEN_MODE {
        /** 切 */
        OFF("0"),
        /** 入 */
        ON("1"),
        /** 「切*」は負荷制限によって状態変化  */
        DEVICE_CHANGE_OFF("2"),
        /** 「入*」は負荷制限によって状態変化  */
        DEVICE_CHANGE_ON("3"),
        /** 開閉器なし  */
        NO_SWITCH_DEVICE("  "),
        /** それ以外  */
        UNKNOWN(null);

        private final String value;

        OPEN_MODE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** LTE-Mの開閉区分(表示用) */
    public enum OPEN_MODE_DISP {
        /** 切 */
        OFF("切"),
        /** 入 */
        ON("入"),
        /** 「切*」は負荷制限によって状態変化  */
        DEVICE_CHANGE_OFF("切*"),
        /** 「入*」は負荷制限によって状態変化  */
        DEVICE_CHANGE_ON("入*"),
        /** 開閉器なし  */
        NO_SWITCH_DEVICE("機能なし"),
        /** それ以外  */
        UNKNOWN("-");

        private final String value;

        OPEN_MODE_DISP(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** LTE-Mの負荷制限 */
    public enum LOADLIMIT_MODE {
        /** 臨時 */
        TEMP("A"),
        /** 基本 */
        BASIC("1"),
        /** 無効 */
        DISABLED("0");

        private final String value;

        LOADLIMIT_MODE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** LTE-Mの負荷制限(表示用) */
    public enum LOADLIMIT_MODE_DISP {
        /** 臨時 */
        TEMP("臨時"),
        /** 基本 */
        BASIC("基本"),
        /** 無効 */
        DISABLED("無効"),
        /** 機能なし */
        NONE("機能なし");

        private final String value;

        LOADLIMIT_MODE_DISP(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** LTE-Mの一括登録用 負荷制限 */
    public enum LOADLIMIT_MODE_LUMP_REGIST {
        /** 基本 */
        BASIC("あり"),
        /** 無効 */
        DISABLED("なし"),
        /** 臨時設定 */
        TEMP("臨時設定"),
        /** 臨時解除 */
        TEMP_INVALID("臨時解除"),;

        private final String value;

        LOADLIMIT_MODE_LUMP_REGIST(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /** 設定内容変更画面にて「未選択」の場合、固定値を設定 */
    public static final int FIXED_LOAD_CURRENT = 1;
    public static final int FIXED_AUTO_INJECTION = 10;
    public static final int FIXED_BREAKER_ACT_COUNT = 5;
    public static final int FIXED_COUNT_CLEAR = 30;

    /** LTE-Mの自動投入時間 */
    public enum AUTO_INJECTION {
        /** なし */
        DISABLED("0", "なし");

        private final String code;
        private final String value;

        AUTO_INJECTION(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }
    }

    /** LTE-Mの現在値取得 取得中の表示 */
    public static final String GET_CURRENT_VALUE_IN_PROGRESS = "取得中...";

    /** LTE-Mの現在値取得 帳票出力用 状態表示更新ステータス */
    public enum DISP_UPDATE_STATUS {
        /** 取得中 */
        IN_PROGRESS("取得中"),
        /** 取得成功（実値あり） */
        SUCCESS("取得成功"),
        /** 取得失敗（タイムアウト等） */
        FAILED("取得失敗");

        private final String value;

        DISP_UPDATE_STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final ZoneId ZONE_JST = ZoneId.of("Asia/Tokyo");
    public static final DateTimeFormatter RFC1123 = DateTimeFormatter.RFC_1123_DATE_TIME;
    public static final DateTimeFormatter OUT_FMT = DateTimeFormatter.ofPattern(DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH).withZone(ZONE_JST);

}
