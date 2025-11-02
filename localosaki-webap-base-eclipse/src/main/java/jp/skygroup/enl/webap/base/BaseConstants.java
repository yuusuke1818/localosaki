package jp.skygroup.enl.webap.base;

/**
 *
 * Base定数クラス
 *
 * Baseクラスのみで使用する定数を定義するクラス
 *
 * @author d-komatsubara
 */
public class BaseConstants extends BaseExternalContext {

    public static final String SESSION_KEY_LOGIN = "login";

    public static final String SESSION_VAL_OK = "OK";

    public static final String STR_EMPTY = "";

    // /** クッキー用 */
    //protected final static String COOKIE_OLD_LOGIN = "OLD_LOGIN";

    // /** クッキー用 */
    //protected final static String COOKIE_LOGIN_TIMES = "LOGIN_TIMES";

    /**
     * ロガー名称
     */
    public static enum LOGGER_NAME {
        /**
         * リクエストログ "RequestLog"
         */
        REQUEST("RequestLog"),
        /**
         * イベントログ "EventLog"
         */
        EVENT("EventLog"),
        /**
         * DBアクセスログ"DaoLog"
         */
        DAO("DaoLog"),
        /**
         * エラーログ"ErrorLog"
         */
        ERROR("ErrorLog"),
        /**
         * バッチ "BatchLog"
         */
        BATCH("BatchLog"),
        /**
         * アクセスログ"AccessLog"
         */
        ACCESS("AccessLog");

        private final String str;

        private LOGGER_NAME(final String str) {
            this.str = str;
        }

        public String getVal() {
            return this.str;
        }
    };

    /**
     * HTTPヘッダ用
     */
    public static enum HTTP_HEADER {

        CONTENT_DISPOSITION("Content-Disposition"),

        ATTACHMENT("attachment; filename="),

        INLINE("inline; filename="),

        FILENAME("filename");

        private final String str;

        private HTTP_HEADER(final String str) {
            this.str = str;
        }

        public String getVal() {
            return this.str;
        }
    }
    /**
     * リターンコード定数
     */
    public static enum RETURN_CODE {
        /**
         * 成功 戻り値 0(数値)
         */
        SUCCESS("0"),
        /**
         * 失敗 戻り値 -1(数値)
         */
        FAILED("-1"),
        /**
         * 例外 戻り値 9(数値)
         */
        EXCEPTION("9");

        private final String str;

        private RETURN_CODE(final String str) {
            this.str = str;
        }

        public int getInt() {
            return Integer.parseInt(this.str);
        }
    }

    /**
     * リクエストトークン定数
     */
    public static enum REQUEST_TOKEN {

        /**
         * リクエストトークン開始タグ
         */
        START_TAG("<input type=\"hidden\" name=\"tState\" value=\""),
        /**
         * リクエストトークン終了タグ
         */
        END_TAG("\" />"),
        /**
         * リクエストトークンセッション保存名
         */
        SESSION_KEY("tState"),
        /**
         * リクエストトークンチェック結果セッション保存名
         */
        SESSION_KEY_CHECK("tStateCheck"),
        /**
         * リクエストトークンチェック結果OK
         */
        SESSION_KEY_CHECK_OK("OK"),
        /**
         * リクエストトークンチェック結果NG
         */
        SESSION_KEY_CHECK_NG("NG"),
        /**
         * リクエストトークン暗号化方式指定:SHA1PRNG
         */
        SHA1("SHA1PRNG"),
        /**
         * リクエストトークンフォーマット形式:%02x
         */
        FORMAT("%02x");

        private final String str;

        private REQUEST_TOKEN(final String str) {
            this.str = str;
        }

        public String getVal() {
            return this.str;
        }
    }
}
