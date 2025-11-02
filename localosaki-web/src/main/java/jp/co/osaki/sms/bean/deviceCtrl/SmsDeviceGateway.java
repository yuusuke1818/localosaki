package jp.co.osaki.sms.bean.deviceCtrl;

import java.lang.reflect.Type;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiLoginParameter;
import jp.co.osaki.osol.api.OsolApiOperationParameter;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.OsolApiResponse;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyUpdateParameter;
import jp.co.osaki.osol.api.parameter.osolapi.OsolApiServerDateTimeParameter;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyUpdateResponse;
import jp.co.osaki.osol.api.response.osolapi.OsolApiServerDateTimeResponse;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsApiGateway;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiGateway;

public class SmsDeviceGateway extends BaseApiGateway {
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
     * ログイン情報の取得
     *
     * @param apiParameter
     * @return
     */
    private final void setOsolApiLoginParameter(OsolApiLoginParameter apiParameter) {

        try {
            MPerson mPerson = (MPerson) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());
            if (mPerson != null) {
                apiParameter.setLoginCorpId(mPerson.getId().getCorpId());
                apiParameter.setLoginPersonId(mPerson.getId().getPersonId());
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
    }

    /**
     * 操作企業の取得
     *
     * @param apiParameter
     * @return
     */
    private final void setOsolApiAuthParameter(OsolApiOperationParameter apiParameter) {

        try {
            MCorp operationCorp = (MCorp) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get(OsolConstants.LOGIN_USER_SESSION_KEY.OPERATION_CORP.getVal());
            if (operationCorp != null) {
                apiParameter.setOperationCorpId(operationCorp.getCorpId());
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
    }

    /**
     * APIキーの取得
     *
     * @param apiParameter
     * @return
     */
    private final void setOsolApiParameter(OsolApiParameter apiParameter) {

        try {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get(OsolConstants.LOGIN_USER_SESSION_KEY.API_KEY.getVal()) != null) {
                apiParameter.setApiKey(FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .get(OsolConstants.LOGIN_USER_SESSION_KEY.API_KEY.getVal()).toString());
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
    }

    /**
     * APIからレスポンスを取得する.
     *
     * @param apiServer APIサーバーのURL
     * @param path APIのパス
     * @param apiParameter APIパラメータのインスタンス
     * @param apiResponse APIレスポンスのインスタンス
     * @return APIレスポンスのインスタンス
     */
    public final <P extends OsolApiLoginParameter> OsolApiResponse<?> osolApiPost(String apiServer,
            SmsApiGateway.PATH path,
            P apiParameter, OsolApiResponse<?> apiResponse) {
        return osolApiPost(apiServer, path, apiParameter, apiResponse.getClass());
    }

    /**
     * APIからレスポンスを取得する.
     *
     * @param apiServer APIサーバーのURL
     * @param path APIのパス
     * @param apiParameter APIパラメータのインスタンス
     * @param type APIレスポンスの型
     * @return APIレスポンスのインスタンス
     */
    public final <P extends OsolApiLoginParameter> OsolApiResponse<?> osolApiPost(String apiServer,
            SmsApiGateway.PATH path,
            P apiParameter, Type type) {

        /**
         * セッションから値をパラメータにセット
         */
        if (FacesContext.getCurrentInstance() != null) {
            if (FacesContext.getCurrentInstance().getExternalContext() != null) {
                if (apiParameter instanceof OsolApiLoginParameter) {
                    setOsolApiLoginParameter(apiParameter);
                }
                if (apiParameter instanceof OsolApiOperationParameter) {
                    setOsolApiAuthParameter((OsolApiOperationParameter) apiParameter);
                }
                if (apiParameter instanceof OsolApiParameter) {
                    setOsolApiParameter((OsolApiParameter) apiParameter);
                }
            }
        }

        /**
         * APIにて時刻取得
         */
        OsolApiServerDateTimeParameter osolApiServerDateTimeParameter = new OsolApiServerDateTimeParameter();
        osolApiServerDateTimeParameter.setBean("OsolApiServerDateTimeBean");
        String response = post(apiServer, PATH.JSON.getVal(), osolApiServerDateTimeParameter);
        OsolApiServerDateTimeResponse osolApiServerDateTimeResponse = gson.fromJson(response,
                OsolApiServerDateTimeResponse.class);

        /**
         * authHashキーを生成
         */
        apiParameter.setMillisec(osolApiServerDateTimeResponse.getServerDateTime().getTime());
        apiParameter.setAuthHash(
                OsolApiAuthUtil.createAuthHash(apiParameter.getLoginCorpId(), apiParameter.getLoginPersonId(),
                        String.valueOf(osolApiServerDateTimeResponse.getServerDateTime().getTime())));


        /**
         * 指定のAPIを実行
         */
        response = post(apiServer, path.getVal(), apiParameter);
        OsolApiResponse<?> retResponse = (OsolApiResponse<?>) gson.fromJson(response, type);
        if (OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_EXPIRED.equals(retResponse.getResultCode())) {
            /**
             * 期限切れの場合、APIキー再発行を行う
             */
            OsolApiKeyUpdateParameter osolApiKeyUpdateParameter = new OsolApiKeyUpdateParameter();
            osolApiKeyUpdateParameter.setBean("OsolApiKeyUpdateBean");
            osolApiKeyUpdateParameter.setLoginCorpId(apiParameter.getLoginCorpId());
            osolApiKeyUpdateParameter.setLoginPersonId(apiParameter.getLoginPersonId());
            osolApiKeyUpdateParameter.setAuthHash(apiParameter.getAuthHash());
            osolApiKeyUpdateParameter.setMillisec(apiParameter.getMillisec());

            if (apiParameter instanceof OsolApiOperationParameter) {
                osolApiKeyUpdateParameter.setOperationCorpId(((OsolApiOperationParameter)apiParameter).getOperationCorpId());
            }
            response = post(apiServer, path.getVal(), osolApiKeyUpdateParameter);
            OsolApiKeyUpdateResponse osolApiKeyUpdateResponse = gson.fromJson(response, OsolApiKeyUpdateResponse.class);
            if (OsolApiResultCode.API_OK.equals(osolApiKeyUpdateResponse.getResultCode())) {
                if (apiParameter instanceof OsolApiParameter) {
                    /**
                     * パラメータのAPIキーを上書きする
                     */
                    ((OsolApiParameter) apiParameter).setApiKey(osolApiKeyUpdateResponse.getResult().getApiKey());
                }
                if (FacesContext.getCurrentInstance().getExternalContext() != null) {
                    /**
                     * セッションのAPIキーを上書きする
                     */
                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                            .getSession(false);
                    if (session != null) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                                OsolConstants.LOGIN_USER_SESSION_KEY.API_KEY.getVal(),
                                osolApiKeyUpdateResponse.getResult().getApiKey());
                    }
                }
                /**
                 * 指定のAPIを再実行
                 */
                response = post(apiServer, path.getVal(), apiParameter);
                retResponse = (OsolApiResponse<?>) gson.fromJson(response, type);
            } else {
                //APIキー再発行不可
                retResponse = osolApiKeyUpdateResponse;
                retResponse.setResultCode(OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_FAILED);
            }
        }

        return retResponse;
    }

}

