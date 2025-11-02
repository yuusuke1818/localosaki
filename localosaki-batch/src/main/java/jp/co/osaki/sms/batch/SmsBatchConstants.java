package jp.co.osaki.sms.batch;

import java.math.BigDecimal;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 * SMSバッチ定数クラス
 *
 * SMSバッチの定数を定義する。
 *
 * @author sagi_h
 *
 */
public class SmsBatchConstants extends BaseConstants {
    /**
     * REC_MANの文字列
     */
    public static enum REC_MAN {
        /** 翌日0時00分の日報空データ作成 */
        CREATE_DAILY_DATA("DailyDataBatch"),
        /** 日報データ前日分再収集予約処理実行 */
        SET_RETRY_PREV_SURVEY("RetryPrevData");

        private final String val;

        private REC_MAN(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 装置種別
     */
    public static enum DEV_KIND {
        /** 空き */
        VACANT("0"),
        /** MUDM2 */
        MUDM2("1"),
        /** SM-15 */
        SM15("2");

        private final String val;

        private DEV_KIND(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 検針種別
     */
    public static enum INSP_KIND {
        /** 自動 */
        AUTO("a"),
        /** 手動 */
        MANUAL("m"),
        /** 予約検針 */
        SCHEDULE("s");

        private final String val;

        private INSP_KIND(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 計算方法種別
     */
    public static enum CALC_TYPE {
        NONE(BigDecimal.valueOf(0)),
        ADD(BigDecimal.valueOf(1)),
        SUB(BigDecimal.valueOf(2));

        private final BigDecimal val;

        private CALC_TYPE(BigDecimal val) {
            this.val = val;
        }

        public BigDecimal getVal() {
            return val;
        }
    }

    /**
     * 小数部端数処理種別
     */
    public static enum DECIMAL_FRACTION {
        // なし
        NONE("0"),
        // 四捨五入
        HALFUP("1"),
        // 切捨て
        ROUNDDOWN("2"),
        // 切り上げ
        ROUNDUP("3");

        private final String val;

        private DECIMAL_FRACTION(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 電気契約プランID種別
     */
    public static enum POWER_PLAN {
        // その他
        OTHER(Integer.valueOf(0)),
        // 従量電灯A
        MENULIGHTA(Integer.valueOf(1)),
        // 従量電灯B
        MENULIGHTB(Integer.valueOf(2)),
        // ファミリータイププラン２
        FAMILY(Integer.valueOf(3));

        private final Integer val;

        private POWER_PLAN(Integer val) {
            this.val = val;
        }

        public Integer getVal() {
            return val;
        }

        public static POWER_PLAN getViewName(Integer val) {
            for(POWER_PLAN viewName : POWER_PLAN.values()) {
                if(viewName.getVal().equals(val)) {
                    return viewName;
                }
            }
            return null;
        }
    }

    /**
     * コマンド状態
     */
    public static enum TCOMMAND_DEV_STA {
        SUCCESS(0),
        FAIL(1);

        private final int val;

        private TCOMMAND_DEV_STA(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    /** 自動検針 */
    /**
     * 検針の際に日報データが取得出来ていなかった場合のリトライ猶予時間
     */
    public static final Integer INSPECTION_RETRY_HOUR = 12;

    /** メールテンプレート */
    /**
     * メール送信フラグ 有効
     */
    public static final String MAIL_USE_FLG_TRUE = "true";
    /**
     * メール送信フラグ 無効
     */
    public static final String MAIL_USE_FLG_FALSE = "false";

    /** メール通知送信元メールアドレス */
    public static final String MAIL_POSTMAIL_OVERRATE_FROMADDRESS = "MAIL_POSTMAIL_OVERRATE_FROMADDRESS";

    /** メールテンプレートディレクトリ */
    public static final String MAIL_TEMPLATE_DIR = "MAIL_TEMPLATE_DIR";

    /** 日報欠損確認対象日数の設定(1日～14日) */
    public static final String MISSING_LOAD_SURVEY_DAYNUM = "MISSING_LOAD_SURVEY_DAYNUM";

    /** メーター検満通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_CHECK_METER_EXPIRY = "CheckMeterExpiry.vm";
    /** 自動検針完了通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_AUTO_INSP_COMPLATE = "AutoInspComplate.vm";
    /** 日報データ欠損のお知らせ通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_LOADSURVEY_ERROR = "LoadSurveyError.vm";
    /** コマンド異常通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_CHECK_DEVCOMMAND_ALART = "CheckDevCommandAlart.vm";
    /** 処理予約異常通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_CHECK_DEVWORKHST_ALART = "CheckWorkHstAlert.vm";
    /** 日報欠損通知メールテンプレートファイル名 */
    public static final String MAIL_TEMPLATE_CHECK_SURVEY_MISSINGDATA_ALART = "CheckSurveyMissingDataAlert.vm";
    /** 日報当日分の欠損メール CSV添付テンプレートファイル名 */
    public static final String MAIL_TEMPLATE_CHECK_SURVEY_MISSINGDATA_SEND_CSV = "CheckSurveyMissingDataSendCsv.vm";
    /** 未確定検針アラートメール CSV添付テンプレートファイル名 */
    public static final String MAIL_TEMPLATE_UNSETTLED_METER_READINGDATA_SEND_CSV = "UnsettledMeterReadingSendCsv.vm";
    /** SQSサーバーのURL */
    public static final String VAL_SQS_SERVERNAME = "VAL_SQS_SERVERNAME";
    /** SQSのregion */
    public static final String SQS_REGION = "ap-northeast-1";


    /**
     * CSV取込
     */
    public static final String CSV_FILE_CHARSET_NAME = "CSV_FILE_CHARSET_NAME";

    /**
     * CSV格納場所
     */
    public static final String IMPORT_CSV_DIR = "IMPORT_CSV_DIR";

    /**
     * CSVファイル（[MuDM2] ロードサーベイ日データ）
     */
    public static final String LOAD_SURVEY_DLS_CSV = "LOAD_SURVEY_DLS_CSV";

    /**
     * CSVファイル（[MuDM2] ロードサーベイ日データ(逆)）
     */
    public static final String LOAD_SURVEY_RDLS_CSV = "LOAD_SURVEY_RDLS_CSV";

    /**
     * CSVファイル（[MuDM2] ロードサーベイ日データ(指針)）
     */
    public static final String LOAD_SURVEY_DMV_CSV = "LOAD_SURVEY_DMV_CSV";

    /**
     * CSVファイル（[MuDM2] ロードサーベイ日データ(指針逆)）
     */
    public static final String LOAD_SURVEY_RDMV_CSV = "LOAD_SURVEY_RDMV_CSV";

    /**
     * CSVファイル（[MuDM2] メータ現在値データ）
     */
    public static final String METER_DATA_CSV = "METER_DATA_CSV";

    /**
     * CSVファイル（[WALK] 無線メータ情報のデータ）
     */
    public static final String RFMETER_INFO_CSV = "RFMETER_INFO_CSV";

    /**
     * CSV出力
     */
    public static final String CSV_FILE_OUTPUT_CHARSET_NAME = "CSV_FILE_OUTPUT_CHARSET_NAME";

    /**
     * CSV格納場所（出力）
     */
    public static final String EXPORT_CSV_DIR = "EXPORT_CSV_DIR";

    /**
     * CSV格納場所_ローカル（出力）
     */
    public static final String EXPORT_CSV_DIR_WIN = "EXPORT_CSV_DIR_WIN";

    /**
     * CSVファイル（日報当日分のデータ欠損メール送信 CSV）
     */
    public static final String CHECK_SURVEY_MISSING_DATA_CSV = "CHECK_SURVEY_MISSING_DATA_CSV";

    /**
     * CSVファイル（未確定検針アラートメール送信 CSV）
     */
    public static final String UNSETTLED_METER_READING_DATA_CSV = "UNSETTLED_METER_READING_DATA_CSV";

    /** Azbilサーバ用 URL */
    public static final String VAL_AZBIL_SERVERNAME = "VAL_AZBIL_SERVERNAME";

    /** Azbilサーバ用 URL パラメータ KEY名 */
    public static final String KEY_REQPARAM_GRANT_TYPE = "grant_type";
    public static final String KEY_REQPARAM_USERNAME = "username";
    public static final String KEY_REQPARAM_PASSWORD = "password";
    public static final String KEY_REQPARAM_AUTHRIZATION = "Authorization";

    /** Azbilサーバ用 URL パラメータ VALUE固定名 */
    public static final String VAL_REQPARAM_BEARER = "Bearer ";	// 最後尾スペース

    /** Jsonデータ内タイムスタンプ文字列フォーマット */
    public static final String DATEFORMATSTRING = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Azbilサーバ用 URL 認証API名 */
    public static final String VAL_AZBIL_SERVERNAME_AUTH = "/oauth/token";

    /** Azbilサーバ用 URL 計測値送付API名 */
    public static final String VAL_AZBIL_SERVERNAME_MEASUREMENTVALUE = "/measurementvalue/minutely/1/";

}
