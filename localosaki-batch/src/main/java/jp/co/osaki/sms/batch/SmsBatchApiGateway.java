package jp.co.osaki.sms.batch;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.OsolApiLoginParameter;
import jp.co.osaki.osol.api.OsolApiNoneParameter;
import jp.co.osaki.osol.api.OsolApiOperationParameter;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.OsolApiResponse;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyUpdateParameter;
import jp.co.osaki.osol.api.parameter.osolapi.OsolApiServerDateTimeParameter;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyCreateResponse;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyUpdateResponse;
import jp.co.osaki.osol.api.response.osolapi.OsolApiServerDateTimeResponse;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiGateway;

/**
 *
 * OSOL API 呼出 共通クラス.
 *
 * @author take_suzuki
 *
 */
public final class SmsBatchApiGateway extends BaseApiGateway {

    /**
     * バッチログ
     */
    private static Logger batchLogger = Logger.getLogger(LOGGER_NAME.BATCH.getVal());

    /**
     *  バッチ名称
     */
    private String batchName;

    /**
     *  OsolBatchApiGatewayの準備完了
     */
    private boolean ready = false;

    /**
     *  API サーバーのエンドポイント
     */
    private String osolApiServerEndPoint;

    /**
     *  API実行 企業ID
     */
    private String osolApiLoginCorpId;

    /**
     *  API実行 担当者
     */
    private String osolApiLoginPersonId;

    /**
     *  API実行 操作企業
     */
    private String osolApiOperationCorpId;

    /**
     *  API実行 APIキー
     */
    private String apiKey;

    /**
     *  API実行 ハッシュ値作成時刻
     */
    private Long setMillisec;

    /**
     *  API実行 ハッシュ値
     */
    private String authHash;

    /**
     * API PATH
     */
    public static enum PATH {
        JSON("/json"), XML("/xml");

        private final String str;

        private PATH(final String str) {
            this.str = str;
        }

        public String getVal() {
            return this.str;
        }
    }

    /**
     * @return batchName
     */
    public String getBatchName() {
        return batchName;
    }

    /**
     * @return osolApiServerEndPoint
     */
    public String getOsolApiServerEndPoint() {
        return osolApiServerEndPoint;
    }

    /**
     * @return osolApiLoginCorpId
     */
    public String getOsolApiLoginCorpId() {
        return osolApiLoginCorpId;
    }

    /**
     * @return osolApiLoginPersonId
     */
    public String getOsolApiLoginPersonId() {
        return osolApiLoginPersonId;
    }

    /**
     * @return osolApiOperationCorpId
     */
    public String getOsolApiOperationCorpId() {
        return osolApiOperationCorpId;
    }

    /**
     * @return apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @return setMillisec
     */
    public Long getSetMillisec() {
        return setMillisec;
    }

    /**
     * @return authHash
     */
    public String getAuthHash() {
        return authHash;
    }

    /**
     * @return ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * コンストラクタ
     *
     * @param batchName バッチ名
     */
    public SmsBatchApiGateway(String batchName) {

        this.batchName = batchName;

        try {

            /**
             *  API呼び出しする為の設定値を取得
             */
            SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();
            this.osolApiServerEndPoint = smsBatchConfigs.getConfig(OsolBatchConstants.OSOL_API_SERVER_ENDPOINT);
            this.osolApiLoginCorpId = smsBatchConfigs.getConfig(OsolBatchConstants.OSOL_API_LOGIN_CORP_ID);
            this.osolApiLoginPersonId = smsBatchConfigs.getConfig(OsolBatchConstants.OSOL_API_LOGIN_PERSON_ID);
            this.osolApiOperationCorpId = smsBatchConfigs.getConfig(OsolBatchConstants.OSOL_API_OPERATION_CORP_ID);

            if (this.osolApiServerEndPoint.isEmpty() ||
                    this.osolApiLoginCorpId.isEmpty() ||
                    this.osolApiLoginPersonId.isEmpty() ||
                    this.osolApiOperationCorpId.isEmpty()) {
                ready = false;
                batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                        .concat(" Constructor OsolBatchConfigs Error"));
            }

            /**
             * APIにて時刻取得
             */
            OsolApiServerDateTimeParameter osolApiServerDateTimeParameter = new OsolApiServerDateTimeParameter();
            OsolApiServerDateTimeResponse osolApiServerDateTimeResponse = new OsolApiServerDateTimeResponse();
            osolApiServerDateTimeParameter.setBean("OsolApiServerDateTimeBean");
            OsolApiResponse<?> osolApiResponse = this.osolApiPost(SmsBatchApiGateway.PATH.JSON,
                    osolApiServerDateTimeParameter, osolApiServerDateTimeResponse);
            if (osolApiResponse == null || !(osolApiResponse instanceof OsolApiServerDateTimeResponse)) {
                ready = false;
                batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                        .concat(" Constructor OsolApiServerDateTimeBean Error"));
                return;
            }
            osolApiServerDateTimeResponse = (OsolApiServerDateTimeResponse) osolApiResponse;
            if (!OsolApiResultCode.API_OK.equals(osolApiServerDateTimeResponse.getResultCode())) {
                ready = false;
                batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                        .concat(" Constructor OsolApiServerDateTimeBean Error"));
                return;
            }
            this.setMillisec = osolApiServerDateTimeResponse.getServerDateTime().getTime();

            /**
             * authHashキーを生成
             */
            this.authHash = OsolApiAuthUtil.createAuthHash(this.osolApiLoginCorpId, this.osolApiLoginPersonId,
                    String.valueOf(this.setMillisec));
            if (this.authHash == null) {
                ready = false;
                batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                        .concat(" Constructor createAuthHash Error"));
                return;
            }

            /**
             * APIキーの発行
             */
            if (!this.apiKeyCreate()) {
                batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                        .concat(" Constructor apiKeyCreate Error"));
                ready = false;
                return;
            }

            ready = true;
        } catch (Exception e) {
            ready = false;
            batchLogger.fatal(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                    .concat(" Constructor Error ").concat(e.getMessage()));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    /**
     * APIの実行
     *
     * @param apiServer APIサーバーのURL
     * @param apiServerEndPoint APIのパス
     * @param apiParameter APIパラメータのインスタンス
     * @param apiResponse APIレスポンスのインスタンス
     * @return APIレスポンスのインスタンス
     */
    public final <P extends OsolApiNoneParameter> OsolApiResponse<?> osolApiPost(
            SmsBatchApiGateway.PATH path, P apiParameter, OsolApiResponse<?> apiResponse) {

        /**
         * ログイン情報の取得
         */
        if (apiParameter instanceof OsolApiLoginParameter) {
            ((OsolApiLoginParameter) apiParameter).setLoginCorpId(this.osolApiLoginCorpId);
            ((OsolApiLoginParameter) apiParameter).setLoginPersonId(this.osolApiLoginPersonId);
            ((OsolApiLoginParameter) apiParameter).setAuthHash(this.authHash);
            ((OsolApiLoginParameter) apiParameter).setMillisec(this.setMillisec);
        }

        /**
         * 操作企業の取得
         */
        if (apiParameter instanceof OsolApiOperationParameter) {
            if(CheckUtility.isNullOrEmpty(((OsolApiOperationParameter) apiParameter).getOperationCorpId())) {
                ((OsolApiOperationParameter) apiParameter).setOperationCorpId(this.osolApiOperationCorpId);
            }
        }

        /**
         * APIキーの取得
         */
        if (apiParameter instanceof OsolApiParameter) {
            ((OsolApiParameter) apiParameter).setApiKey(this.apiKey);
        }

        /**
         * 指定のAPIを実行
         */
        OsolApiResponse<?> retResponse;
        String response = "";
        try {
            try {
                response = this.post(this.osolApiServerEndPoint, path.getVal(), apiParameter);
                if (response.isEmpty()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                retResponse = apiResponse.getClass().newInstance();
                retResponse.setResultCode(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
                return retResponse;
            }
            retResponse = (OsolApiResponse<?>) gson.fromJson(response, apiResponse.getClass());
            if (OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_EXPIRED.equals(retResponse.getResultCode())) {
                if (!this.apiKeyUpdate()) {
                    retResponse = apiResponse.getClass().newInstance();
                    retResponse.setResultCode(OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_FAILED);
                    return retResponse;
                }
            } else if (OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL.equals(retResponse.getResultCode())) {
                if (!this.apiKeyCreate()) {
                    retResponse = apiResponse.getClass().newInstance();
                    retResponse.setResultCode(OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_FAILED);
                    return retResponse;
                }
            } else {
                return retResponse;
            }
            try {
                //APIキーの再設定
                if (apiParameter instanceof OsolApiParameter) {
                    ((OsolApiParameter) apiParameter).setApiKey(this.apiKey);
                }
                response = this.post(this.osolApiServerEndPoint, path.getVal(), apiParameter);
                if (response.isEmpty()) {
                    throw new Exception();
                }
                retResponse = (OsolApiResponse<?>) gson.fromJson(response, apiResponse.getClass());
                return retResponse;
            } catch (Exception e) {
                retResponse = apiResponse.getClass().newInstance();
                retResponse.setResultCode(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
                return retResponse;
            }
        } catch (Exception e) {
            batchLogger.error(this.batchName.concat(" ").concat(this.getClass().getSimpleName())
                    .concat(" ".concat(apiParameter.getBean().concat(" ").concat(e.getMessage()))));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            retResponse = null;
        }
        return retResponse;
    }

    /**
     *
     * APIキーの発行
     *
     * @return  (true:成功 false:失敗)
     */
    private boolean apiKeyCreate() {

        OsolApiKeyCreateParameter osolApiKeyCreateParameter = new OsolApiKeyCreateParameter();
        OsolApiKeyCreateResponse osolApiKeyCreateResponse = new OsolApiKeyCreateResponse();
        osolApiKeyCreateParameter.setBean("OsolApiKeyCreateBean");
        osolApiKeyCreateParameter.setLoginCorpId(this.osolApiLoginCorpId);
        osolApiKeyCreateParameter.setLoginPersonId(this.osolApiLoginPersonId);
        osolApiKeyCreateParameter.setOperationCorpId(this.osolApiOperationCorpId);
        osolApiKeyCreateParameter.setAuthHash(authHash);
        osolApiKeyCreateParameter.setMillisec(setMillisec);
        try {
            String response = this.post(this.osolApiServerEndPoint, SmsBatchApiGateway.PATH.JSON.getVal(),
                    osolApiKeyCreateParameter);
            osolApiKeyCreateResponse = (OsolApiKeyCreateResponse) gson.fromJson(response,
                    OsolApiKeyCreateResponse.class);
            if (!OsolApiResultCode.API_OK.equals(osolApiKeyCreateResponse.getResultCode())) {
                return false;
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return false;
        }
        this.apiKey = osolApiKeyCreateResponse.getResult().getApiKey();
        return true;
    }

    /**
     *
     * APIキーの再発行
     *
     * @return  (true:成功 false:失敗)
     */
    private boolean apiKeyUpdate() {

        OsolApiKeyUpdateParameter osolApiKeyUpdateParameter = new OsolApiKeyUpdateParameter();
        OsolApiKeyUpdateResponse osolApiKeyUpdateResponse = new OsolApiKeyUpdateResponse();
        osolApiKeyUpdateParameter.setBean("OsolApiKeyUpdateBean");
        osolApiKeyUpdateParameter.setLoginCorpId(this.osolApiLoginCorpId);
        osolApiKeyUpdateParameter.setLoginPersonId(this.osolApiLoginPersonId);
        osolApiKeyUpdateParameter.setOperationCorpId(this.osolApiOperationCorpId);
        osolApiKeyUpdateParameter.setAuthHash(authHash);
        osolApiKeyUpdateParameter.setMillisec(setMillisec);
        try {
            String response = this.post(this.osolApiServerEndPoint, SmsBatchApiGateway.PATH.JSON.getVal(),
                    osolApiKeyUpdateParameter);
            osolApiKeyUpdateResponse = (OsolApiKeyUpdateResponse) gson.fromJson(response,
                    OsolApiKeyUpdateResponse.class);
            if (!OsolApiResultCode.API_OK.equals(osolApiKeyUpdateResponse.getResultCode())) {
                return false;
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return false;
        }
        this.apiKey = osolApiKeyUpdateResponse.getResult().getApiKey();
        return true;
    }

}
