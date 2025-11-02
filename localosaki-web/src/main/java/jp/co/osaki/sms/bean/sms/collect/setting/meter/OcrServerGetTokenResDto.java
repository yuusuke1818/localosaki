package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import com.google.gson.annotations.SerializedName;

/**
 * アズビルサーバトークン取得 Dtoクラス
 * @author kobayashi.sho
 */
public class OcrServerGetTokenResDto {

    /** 処理結果コード */
    private int result = 0;

    /** HTTPレスポンスコード */
    private int httpResponseCode = 0;

    /** エラー情報 */
    private String errorMessage = null; // null:正常

    // -----

    /** 結果値 0:成功. */
    private String code;    // Number

    /** メッセージ. */
    private String msg;     // String

    private Data data = null; // null:取得失敗

    public class Data {
        /** ユーザーID. */
        private String user_id;     // Number
        /** ユーザーが付与する権限. */
        private String scope;       // String
        /** プランタイプ. */
        @SerializedName("package")
        private String packageType;    // String
        /** アクセストークンタイプ. */
        private String tokenType;   // String
        /** アクセストークンの有効期限. */
        private String expiresIn;   // Number
        /** アクセストークン値. */
        private String accessToken; // String
//        /** . */
//        private String avatar;
//        /** . */
//        private String owner_id;

        /**
         * @return user_id
         */
        public String getUser_id() {
            return user_id;
        }
        /**
         * @param user_id セットする user_id
         */
        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
        /**
         * @return scope
         */
        public String getScope() {
            return scope;
        }
        /**
         * @param scope セットする scope
         */
        public void setScope(String scope) {
            this.scope = scope;
        }
        /**
         * @return packageType
         */
        public String getPackageType() {
            return packageType;
        }
        /**
         * @param packageType セットする packageType
         */
        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }
        /**
         * @return tokenType
         */
        public String getTokenType() {
            return tokenType;
        }
        /**
         * @param tokenType セットする tokenType
         */
        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }
        /**
         * @return expiresIn
         */
        public String getExpiresIn() {
            return expiresIn;
        }
        /**
         * @param expiresIn セットする expiresIn
         */
        public void setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
        }
        /**
         * @return accessToken
         */
        public String getAccessToken() {
            return accessToken;
        }
        /**
         * @param accessToken セットする accessToken
         */
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    /**
     * @param result セットする result
     */
    public int getResult() {
        return this.result;
    }

    /**
     * @return result
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @param httpResponseCode セットする httpResponseCode
     */
    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    /**
     * @return httpResponseCode
     */
    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    /**
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage セットする errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code セットする code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg セットする msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data セットする data
     */
    public void setData(Data data) {
        this.data = data;
    }
}
