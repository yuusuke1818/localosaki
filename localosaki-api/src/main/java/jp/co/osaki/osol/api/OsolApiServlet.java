package jp.co.osaki.osol.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.api.auth.OsolApiAuthentication;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiUseConstants;
import jp.co.osaki.osol.api.constants.ApiUseConstants.API_LOGGER_NAME;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiIpAddrCheckFilterDao;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiKeyDao;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiPersonDao;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiServerDateTimeDao;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiServletAuthDao;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiUseManagementDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyUpdateParameter;
import jp.co.osaki.osol.api.resultdata.osolapi.OsolApiUseResultListDetailResultData;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.batch.util.CalendarUtil;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MLoginIpAddr;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TApiAlertMailSetting;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiUseSetting;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.web.csv.converter.WebMailCsvConverter;
import jp.co.osaki.osol.web.csv.record.WebMailRecord;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseFileZipArchive;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;
import jp.skygroup.enl.webap.base.api.BaseApiBean;
import jp.skygroup.enl.webap.base.api.BaseApiParameter;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;
import jp.skygroup.enl.webap.base.api.BaseApiServlet;

/**
 *
 * OSOL API サーブレット共通クラス.
 *
 * @author take_suzuki
 *
 */
public abstract class OsolApiServlet extends BaseApiServlet {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 7853921682266249788L;

    /**
     * イベント用ログ
     */
    static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    /**
     * API利用時ログ
     */
    private static Logger apiLogger = Logger.getLogger(API_LOGGER_NAME.API.getVal());

    @EJB
    private OsolApiServerDateTimeDao osolApiServerDateTimeDao;

    @EJB
    private OsolApiPersonDao osolApiPersonDao;

    @EJB
    private OsolApiKeyDao osolApiKeyDao;

    @EJB
    private OsolApiServletAuthDao osolApiAuthDao;
    @EJB
    private CorpDataFilterDao corpDataFilterDao;
    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;
    @EJB
    private OsolApiIpAddrCheckFilterDao osolApiIpAddrCheckFilterDao;

    @EJB
    private OsolApiUseManagementDao osolApiUseManagementDao;

    @Inject
    private OsolConfigs osolConfigs;

    //apiBeanの文字操作用
    private String strBean= "Bean";

    /**
     *  (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.BaseApiServlet#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void execute(HttpServletRequest request, HttpServletResponse response) {

        BaseApiResponse apiResponse = null;

        // API利用カウントチェック (外部からAPIを使用したか)
        boolean apiUseCountCheckFlg = false;

        // apiBean名
        String apiBeanName;

        //リクエストからbeanパラメータを取得
        OsolApiNoneParameter osolParam = new OsolApiNoneParameter();
        getRequestParameter(request, osolParam);

        if (osolParam.getBean() == null || osolParam.getBean().isEmpty()) {
            apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_BEAN_UNSPECIFIED, "API_ERROR_BEAN_UNSPECIFIED");
            settingResponse(response, apiResponse);
        }

        //apiBeanを取得
        BaseApiBean<?, ?> apiBean = null;
        try {
            apiBean = (BaseApiBean<?, ?>) getBean(osolParam.getBean());
            apiBeanName = osolParam.getBean();
            if(apiBean == null) {
                apiBean = (BaseApiBean<?, ?>) getBean(osolParam.getBean().replace(strBean, ""));
                apiBeanName = osolParam.getBean().replace(strBean, "");
                if(apiBean == null) {
                    apiBean = (BaseApiBean<?, ?>) getBean(osolParam.getBean() + strBean);
                    apiBeanName = osolParam.getBean() + strBean;
                    if (apiBean == null) {
                        apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_BEAN_UNKNOWN, "API_ERROR_BEAN_UNKNOWN");
                        settingResponse(response, apiResponse);
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_BEAN_IMPLEMENTATION_MISMATCH, "API_ERROR_BEAN_IMPLEMENTATION_MISMATCH");
            settingResponse(response, apiResponse);
            return;
        }

        //リクエストからAPIパラメータを設定
        getRequestParameter(request, apiBean.getParameter());

        // resultがrequestパラメータに設定されていない、かつbodyにあればbodyの内容をセットする
        getRequestBodyParameter(request, apiBean.getParameter());

        //リクエストヘッダに含まれているapiキーを取得
        if (apiBean.getParameter() instanceof OsolApiParameter) {
            OsolApiParameter authParam = (OsolApiParameter)apiBean.getParameter();
            if (authParam.getApiKey() == null || authParam.getApiKey().isEmpty() && request.getHeader("Osol-Api-Key") != null) {
                ((OsolApiParameter)apiBean.getParameter()).setApiKey(request.getHeader("Osol-Api-Key"));
            }
        }

        //担当者の有効性チェック
        Long loginUserId = new Long(0);
        MPerson mPerson = null;
        if (apiBean.getParameter() instanceof OsolApiLoginParameter) {
            try {
                mPerson = osolApiPersonDao.query((OsolApiLoginParameter)apiBean.getParameter());
            } catch (Exception ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
            if (mPerson == null) {
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_PERSON,
                        "API_ERROR_AUTHORIZATION_PERSON");
                settingResponse(response, apiResponse);
                return;
            }
            loginUserId = mPerson.getUserId();
        }

        //APIの権限チェック
        if (apiBean.getParameter() instanceof OsolApiParameter) {
            OsolApiParameter authParam = (OsolApiParameter)apiBean.getParameter();
            String osolApiResultCode = OsolApiResultCode.API_OK;
            //権限チェックのパラメータのチェックを行う
            if (!OsolApiAuthentication.checkParameter(authParam)){
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_PARAMETER_GET,
                        "API_ERROR_PARAMETER_GET");
                settingResponse(response, apiResponse);
                return;
            }
            if (!CheckUtility.isNullOrEmpty(authParam.getAuthHash())) {
                //ハッシュ値が設定されている場合
                osolApiResultCode = OsolApiAuthentication.checkApiAuthForPrivate(authParam, osolApiKeyDao.getServerDateTime(), true);
            } else {
                //上記以外はOsol外部からとみなす
                TApiKey tApiKey = osolApiKeyDao.getApiKey(authParam.getLoginCorpId(), authParam.getLoginPersonId());
                osolApiResultCode = OsolApiAuthentication.checkApiAuthForPublic(authParam, osolApiKeyDao.getServerDateTime(),tApiKey, mPerson);
                if (OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_EXPIRED.equals(osolApiResultCode)) {
                    //期限切れの場合、対象のデータを論理削除する
                    osolApiKeyDao.removeApiKey(tApiKey, loginUserId);
                }
                // API呼出チェック
                if (OsolApiResultCode.API_OK.equals(osolApiResultCode)){
                    String operationCorpId = authParam.getOperationCorpId();
                    String buildingId = getAuthParamProperty(authParam, "buildingId");
                    String buildingNo = getAuthParamProperty(authParam, "buildingNo");
                    String smId = getAuthParamProperty(authParam, "smId");
                    BaseApiResponse apiErrResponse = chkCallApi(apiBean.toString(), mPerson, operationCorpId, buildingId, buildingNo, smId, request);
                    if (apiErrResponse != null) {
                        settingResponse(response, apiErrResponse);
                        return;
                    }

                    // API利用許可チェック (OKの場合API利用カウントフラグをtrue)
                    String corpType = mPerson.getMCorp().getCorpType();
                    if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(corpType)
                            && Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName)
                            || Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName.replace(strBean, ""))
                            || Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName + strBean)) {
//                    if (Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName)
//                            || Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName.replace(strBean, ""))
//                            || Arrays.asList(OsolApiAuthentication.getPartnerAccessApiBeanList()).contains(apiBeanName + strBean)) {
                        apiErrResponse = checkApiUseAuth(mPerson.getId().getCorpId(), getAPiKind(apiBean.toString()));
                        if (apiErrResponse != null) {
                            settingResponse(response, apiErrResponse);
                            return;
                        }

                        // API利用カウントチェックするフラグをON
                        apiUseCountCheckFlg = true;
                    }
                }
            }
            if (!OsolApiResultCode.API_OK.equals(osolApiResultCode)){
                apiResponse = createErrorMessageResponse(osolApiResultCode,"Error");
                settingResponse(response, apiResponse);
                return;
            }
        } else if (apiBean.getParameter() instanceof OsolApiKeyCreateParameter) {
            // 内部用APIキー発行が外部からの呼び出しの場合
            OsolApiKeyCreateParameter authParam = (OsolApiKeyCreateParameter)apiBean.getParameter();
            // 権限チェックのパラメータのチェックを行う
            if (!OsolApiAuthentication.checkParameter(authParam)){
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_PARAMETER_GET,
                        "API_ERROR_PARAMETER_GET");
                settingResponse(response, apiResponse);
                return;
            }

            String millisecStr = authParam.getMillisec() == null ? null : authParam.getMillisec().toString();
            // ハッシュ値チェック
            if (CheckUtility.isNullOrEmpty(authParam.getAuthHash())
                    || !OsolApiAuthUtil.checkAuthHash(
                            authParam.getLoginCorpId(), authParam.getLoginPersonId(),
                            millisecStr, authParam.getAuthHash())) {
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_BEAN_UNKNOWN, "Error");
                settingResponse(response, apiResponse);
                return;
            }
        } else if (apiBean.getParameter() instanceof OsolApiKeyUpdateParameter) {
            // APIキー更新が外部からの呼び出しの場合
            OsolApiKeyUpdateParameter authParam = (OsolApiKeyUpdateParameter)apiBean.getParameter();
            // 権限チェックのパラメータのチェックを行う
            if (!OsolApiAuthentication.checkParameter(authParam)){
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_PARAMETER_GET,
                        "API_ERROR_PARAMETER_GET");
                settingResponse(response, apiResponse);
                return;
            }

            String millisecStr = authParam.getMillisec() == null ? null : authParam.getMillisec().toString();
            // ハッシュ値チェック
            if (CheckUtility.isNullOrEmpty(authParam.getAuthHash())
                    || !OsolApiAuthUtil.checkAuthHash(
                            authParam.getLoginCorpId(), authParam.getLoginPersonId(),
                            millisecStr, authParam.getAuthHash())) {
                apiResponse = createErrorMessageResponse(OsolApiResultCode.API_ERROR_BEAN_UNKNOWN, "Error");
                settingResponse(response, apiResponse);
                return;
            }
        }

        //apiBeanを実行
        try {
            apiResponse = apiBean.execute();

            // API利用カウント（Osol外部かつ、パートナー企業かつ、パートナー公開APIの場合）
            if (apiUseCountCheckFlg) {
                // 担当者の企業ID取得（操作企業のIDではなく、API利用者の企業ID）
                String loginCorpId = mPerson.getId().getCorpId();
                String apiKind = getAPiKind(apiBean.toString());
                String remoteAddr = getRemoteAddr(request);

                // 正常応答の場合
                if (apiUseCount(apiResponse, loginCorpId, apiKind, mPerson, apiBeanName, remoteAddr)) {

                    // API利用設定取得
                    TApiUseSetting tApiUseSetting = osolApiUseManagementDao.getApiUseSetting(loginCorpId, apiKind);

                    if (tApiUseSetting != null) {
                        // API警告メール送信チェック
                        ApiUseConstants.API_ALERT_MAIL_SEND_TYPE ret = checkApiAlertMail(tApiUseSetting);
                        if (!ApiUseConstants.API_ALERT_MAIL_SEND_TYPE.NONE_MAIL.equals(ret)) {
                            // API警告メールを送信
                            sendApiAlertMail(tApiUseSetting, ret);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            errorLogger.error(BaseUtility.getStackTraceMessage(exception));
            //原因となった例外を取得
            Throwable throwable = exception;
            while (throwable.getCause() != null){
                throwable = throwable.getCause();
            }
            //原因となった例外から処理結果コードを設定
            apiResponse = createErrorMessageResponse(createErrorResultCode(throwable), "error");
        }

        //APIレスポンスを設定
        settingResponse(response, apiResponse);
    }

    /**
     *  (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.BaseApiServlet#createErrorMessageResponse()
     */
    @Override
    protected final BaseApiResponse createErrorMessageResponse(String resultCode, String errorMessage) {

        OsolApiErrorMessageResponse osolApiErrorMessageResponse = new OsolApiErrorMessageResponse();
        osolApiErrorMessageResponse.setResultCode(resultCode);

        OsolApiErrorMessageResult osolApiErrorMessageResult = new OsolApiErrorMessageResult();
        osolApiErrorMessageResult.setErrorMessage(errorMessage);

        osolApiErrorMessageResponse.setResult(osolApiErrorMessageResult);

        return osolApiErrorMessageResponse;
    }

    /**
     * (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiServlet#createErrorMessageListResponse(java.lang.String, java.util.List)
     */
    @Override
    protected BaseApiResponse createErrorMessageListResponse(String resultCode, List<String> errorMessageList) {

        OsolApiErrorMessageListResponse osolApiErrorMessageListResponse = new OsolApiErrorMessageListResponse();
        osolApiErrorMessageListResponse.setResultCode(resultCode);

        OsolApiErrorMessageListResult osolApiErrorMessageListResult = new OsolApiErrorMessageListResult();
        osolApiErrorMessageListResult.setErrorMessageList(errorMessageList);

        osolApiErrorMessageListResponse.setResult(osolApiErrorMessageListResult);

        return osolApiErrorMessageListResponse;
    }

    /**
     * (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiServlet#settingResponse(javax.servlet.http.HttpServletResponse, jp.skygroup.enl.webap.base.api.BaseApiResponse)
     */
    @Override
    protected void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse) {

        //DBサーバー時刻を設定
        OsolApiResponse<?> osolApiResponse = (OsolApiResponse<?>) apiResponse;
        osolApiResponse.setServerDateTime(osolApiServerDateTimeDao.query(null));

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /**
     * API処理結果コード生成メソッド
     *
     * @param throwable 原因となった例外
     * @return 処理結果コード
     */
    private String createErrorResultCode(Throwable throwable) {

        String osolApiResultCode = OsolApiResultCode.API_OK;

        if (throwable == null) {
            osolApiResultCode = OsolApiResultCode.API_OK;
        }
        if (throwable instanceof EntityExistsException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_ENTITY_EXISTS;
        } else if (throwable instanceof EntityNotFoundException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_ENTITY_NOT_FOUND;
        } else if (throwable instanceof LockTimeoutException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_LOCK_TIMEOUT;
        } else if (throwable instanceof NonUniqueResultException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_NON_UNIQUE_RESULT;
        } else if (throwable instanceof NoResultException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_NO_RESULT;
        } else if (throwable instanceof OptimisticLockException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_OPTIMISTIC_LOCK;
        } else if (throwable instanceof PessimisticLockException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_PESSIMISTIC_LOCK;
        } else if (throwable instanceof QueryTimeoutException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_QUERY_TIMEOUT;
        } else if (throwable instanceof RollbackException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_ROLLBACK;
        } else if (throwable instanceof TransactionRequiredException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_TRANSACTION_REQUIRED;
        } else if (throwable instanceof org.hibernate.exception.JDBCConnectionException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_JDBC_CONNECTION;
        } else if (throwable instanceof PersistenceException) {
            osolApiResultCode = OsolApiResultCode.API_ERROR_DATABASE_PRESISTENCE;
        } else {
            osolApiResultCode = OsolApiResultCode.API_ERROR_BEAN_APPLICATION;
        }

        return osolApiResultCode;
    }

    /**
     * API実行権限チェック
     *
     * @param apiBean
     * @param mPerson
     * @param operationCorpId
     * @return 実行権限なしの場合、falseを返します
     */
    private BaseApiResponse chkCallApi(String apiBean, MPerson mPerson, String operationCorpId, String buildingId, String buildingNo, String smId, HttpServletRequest request) {

        BaseApiResponse apiErrResponse1 = createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_PERSON,
                "API_ERROR_AUTHORIZATION_PERSON");
        BaseApiResponse apiErrResponse2 = createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_IP_ADDR,
                "API_ERROR_AUTHORIZATION_IP_ADDR");

        // 操作権限
        if (!getCorpOparationAuth(apiBean.toString(), mPerson, operationCorpId)) {
            return apiErrResponse1;
        }

        // 企業
        PersonDataParam personDataParam = new PersonDataParam(mPerson.getId().getCorpId(), mPerson.getId().getPersonId());
        List<InputListItem> inputList = new ArrayList<>();
        inputList.add(new InputListItem(operationCorpId));
        List<InputListItem> ret = corpDataFilterDao.applyDataFilter(inputList, personDataParam);
        if (ret == null || ret.size() == 0) {
            return apiErrResponse1;
        }

        // 建物
        inputList.clear();
        if(smId != null) {
            // 機器IDから建物IDを取得
            List<String> buildingIdList = getBuildingIdList(operationCorpId, smId);
            if(buildingIdList == null) {
                // 取得した建物IDリストがnullだったら空文字を代入して、フィルターで権限なしにする
                buildingIdList = Arrays.asList(OsolConstants.STR_EMPTY);
            }

            for (String _buildingId: buildingIdList) {
                if(buildingId != null) {
                    // 機器ID、建物IDが指定されていた場合に整合性チェック
                    // 不整合の場合は、建物IDに空文字を代入してフィルターで権限なしにする
                    if(!buildingId.equals(_buildingId)) {
                        _buildingId = OsolConstants.STR_EMPTY;
                    }
                }
                inputList.add(new InputListItem(operationCorpId, _buildingId));
            }
        }
        // 建物番号
        else if (buildingNo != null) {
            // 建物番号から建物IDを取得
            String _buildingId = getBuildingId(operationCorpId, buildingNo);
            if(_buildingId == null) {
                // 取得した建物IDがnullだったら空文字を代入して、フィルターで権限なしにする
                _buildingId  = OsolConstants.STR_EMPTY;
            }
            inputList.add(new InputListItem(operationCorpId, _buildingId));
        }
        // 建物ID
        else if (buildingId != null) {
            inputList.add(new InputListItem(operationCorpId, buildingId));
        }

        if (inputList.size() > 0) {
            ret = buildingDataFilterDao.applyDataFilter(inputList, personDataParam);
            if (ret == null || ret.size() == 0) {
                return apiErrResponse1;
            }
        }

        // API実行許可チェック
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteAddr = httpRequest.getRemoteAddr();
        String xForwardFor = httpRequest.getHeader("X-Forwarded-For");
        if(xForwardFor != null){
            String[] list = xForwardFor.split(",");
            remoteAddr = list[list.length-1].trim();
        }
        // API実行許可IPアドレス件数チェック
        List<MLoginIpAddr> mLoginIpAddrList = osolApiIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(),null,null, ApiGenericTypeConstants.LOGIN_PERMIT_TARGET_121.LOGIN.getVal());
        if (mLoginIpAddrList.size() > 0) {
            /* ログイン許可ステータスが 2：停止(不許可) のIPアドレス一覧取得*/
            List<MLoginIpAddr> mLoginBlackIpAddrList = osolApiIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(), null, "2", ApiGenericTypeConstants.LOGIN_PERMIT_TARGET_121.LOGIN.getVal());
            /* ログイン許可ステータスが 1：許可 のIPアドレス一覧取得*/
            List<MLoginIpAddr> mLoginWhiteIpAddrList = osolApiIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(), null, "1", ApiGenericTypeConstants.LOGIN_PERMIT_TARGET_121.LOGIN.getVal());

            // ホワイトリスト、ブラックリスト混在の状態
            if (mLoginBlackIpAddrList.size() > 0 && mLoginWhiteIpAddrList.size() > 0) {
                // ブラックリストに存在するかまたは、ホワイトリストに存在しない場合、認証エラー
                if (checkBlackList(remoteAddr, mLoginBlackIpAddrList) || !checkWhiteList(remoteAddr, mLoginWhiteIpAddrList)) {
                    return apiErrResponse2;
                }
            }

            // ブラックリストだけの状態
            if (mLoginBlackIpAddrList.size() > 0 && mLoginWhiteIpAddrList.size() <= 0) {
                // ブラックリストのチェックを行う
                if (checkBlackList(remoteAddr, mLoginBlackIpAddrList)) {
                    // 存在する場合
                    return apiErrResponse2;
                }
            }

            // ホワイトリストだけの状態
            if (mLoginBlackIpAddrList.size() <= 0 && mLoginWhiteIpAddrList.size() > 0) {
                // ホワイトリストのチェックを行う(ブラックリストに存在していたらチェックしない)
                if (!checkWhiteList(remoteAddr, mLoginWhiteIpAddrList)) {
                    // 存在しない場合
                    return apiErrResponse2;
                }
            }
        }

        return null;
    }

    /**
     * 操作権限
     *
     * @param apiBean
     * @param mPerson
     * @param operationCorpId
     * @return 実行権限なしの場合、falseを返します
     */
    private boolean getCorpOparationAuth(String apiBean, MPerson mPerson, String operationCorpId) {
        // 企業種別
        String corpType = mPerson.getMCorp().getCorpType();
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(corpType)) {
            // 大崎
            return true;
        }
        else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(corpType)) {
//        else {

            // パートナー
            //企業機能利用権限
            MCorpFunctionUse corpFunc = null;
            //担当者機能権限
            String authCd = null;

            //管理者権限(2024-10-21）
            if (mPerson.getPersonType().contentEquals(OsolConstants.PERSON_CORP_TYPE_CORP_MANAGE)) {
            	return true;
            }

            //EMS 機能
            if(apiBean.indexOf("api.bean.energy.ems") >= 0 || apiBean.indexOf("api.bean.energy.setting") >= 0 || apiBean.indexOf("api.bean.energy.verify") >= 0) {
                corpFunc = osolApiAuthDao.getCorpFunctionUse(OsolAccessBean.FUNCTION_CD.ENERGY_USAGE_STATUS.getVal(), mPerson.getId().getCorpId());
                authCd = OsolConstants.USER_AUTHORITY.ENERGY_USAGE_STATUS.getVal();
            }
            //機器制御 機能
            else if(apiBean.indexOf("api.bean.smcontrol") >= 0) {
                corpFunc = osolApiAuthDao.getCorpFunctionUse(OsolAccessBean.FUNCTION_CD.EQUIP_CONTROL.getVal(), mPerson.getId().getCorpId());
                authCd = OsolConstants.USER_AUTHORITY.EQUIP_CONTROL.getVal();
            }
            //SMS API公開　2024-10-17
            else if(apiBean.indexOf("api.bean.sms") >= 0) {
                corpFunc = osolApiAuthDao.getCorpFunctionUse(OsolAccessBean.FUNCTION_CD.SMS.getVal(), mPerson.getId().getCorpId());
                authCd = OsolConstants.USER_AUTHORITY.SMS.getVal();
            }
            //企業機能利用/企業担当者権限のチェックが必要なし
            else {
                return true;
            }

            if (corpFunc != null && authCd  != null) {
                //企業機能利用可
                if(OsolConstants.FLG_ON.compareTo(corpFunc.getUseFlg()) == 0) {
                    // 企業担当者権限
                    return osolApiAuthDao.isCorpPersonAuth(operationCorpId,
                                                           mPerson.getId().getCorpId(),
                                                           mPerson.getId().getPersonId(),
                                                           authCd);
                }
            }
        }
        return false;
    }

    /**
     * プロパティ値取得
     *
     * @param bean
     * @param name
     * @return 存在しない場合、nullを返します
     */
    private String getAuthParamProperty(OsolApiParameter bean, String name) {
        String ret = null;
        try {
            ret = BeanUtils.getProperty(bean, name);
        } catch (Exception e) {
            return null;
        }
        return ret;
    }

    /**
     * 建物番号から建物IDを取得
     *
     * @param corpId
     * @param buildingNo
     * @return
     */
    private String getBuildingId(String corpId, String buildingNo) {
        Long ret = osolApiAuthDao.getBuildingId(corpId, buildingNo);
        if (ret == null) return null;
        return String.valueOf(ret);
    }

    /**
     * 機器IDから建物リストを取得
     *
     * @param corpId
     * @param smId
     * @return
     */
    private List<String> getBuildingIdList(String corpId, String smId) {
        Long _smId = Long.valueOf(smId);
        return osolApiAuthDao.getBuildingIdList(corpId, _smId);
    }

    /**
     * 企業・建物フィルタ用クラス
     *
     */
    private class InputListItem {
        private String corpId;
        private String buildingId;
        public InputListItem(String corpId) {
            super();
            this.corpId = corpId;
        }
        public InputListItem(String corpId, String buildingId) {
            super();
            this.corpId = corpId;
            this.buildingId = buildingId;
        }
        @SuppressWarnings("unused")
        public String getCorpId() {
            return corpId;
        }
        @SuppressWarnings("unused")
        public void setCorpId(String corpId) {
            this.corpId = corpId;
        }
        @SuppressWarnings("unused")
        public String getBuildingId() {
            return buildingId;
        }
        @SuppressWarnings("unused")
        public void setBuildingId(String buildingId) {
            this.buildingId = buildingId;
        }
    }

    /**
     * リクエストパラメータからAPIパラメータクラスに設定する.
     *
     * @param httpServletRequest HttpServletRequest
     * @param parameter APIパラメータクラスのインスタンス
     * @return APIパラメータクラスのインスタンス
     */
    private <P extends BaseApiParameter> P getRequestBodyParameter(
            HttpServletRequest httpServletRequest,
            P parameter) {

        // resultがrequestパラメータに設定されていない、かつbodyにあればbodyの内容をセットする
        try {
            if (BeanUtils.describe(parameter).keySet().contains("result")) {
                String result = BeanUtils.getProperty(parameter, "result");
                if (result == null || result.isEmpty()) {
                    String bodyStr = httpServletRequest.getReader().lines().collect(Collectors.joining("\r\n"));
                    if (bodyStr != null && !bodyStr.isEmpty()) {
                        BeanUtils.setProperty(parameter, "result", bodyStr);
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return parameter;
    }

    private boolean checkBlackList(String remoteAddr, List<MLoginIpAddr> mLoginBlackIpAddrList) {
        for (MLoginIpAddr targetBlack : mLoginBlackIpAddrList) {
            if (targetBlack.getIpAddress().equals(remoteAddr)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkWhiteList(String remoteAddr, List<MLoginIpAddr> mLoginWhiteIpAddrList) {
        for (MLoginIpAddr targetWhite : mLoginWhiteIpAddrList) {
            if (targetWhite.getIpAddress().equals(remoteAddr)) {
                return true;
            }
        }

        return false;
    }

    /**
     * APIBeanからAPI種別を取得
     * @param apiBean
     * @return
     */
    private String getAPiKind(String apiBean) {
        // EMS 機能
        if(apiBean.indexOf("api.bean.energy.ems") >= 0 || apiBean.indexOf("api.bean.energy.setting") >= 0 || apiBean.indexOf("api.bean.energy.verify") >= 0) {
            return ApiUseConstants.API_KIND.EMS.getVal();
        }
        // 機器制御 機能
        else if(apiBean.indexOf("api.bean.smcontrol") >= 0) {
            return ApiUseConstants.API_KIND.DEVICE_CONTROL.getVal();
        }
        // その他
        else {
            return ApiUseConstants.API_KIND.OTHER.getVal();
        }
    }

    // API利用許可チェック
    private BaseApiResponse checkApiUseAuth(String corpId, String apiKind) {

        TApiUseSetting tApiUseSetting = osolApiUseManagementDao.getApiUseSetting(corpId, apiKind);

        // API利用設定にレコードが存在しない（上限到達エラーを設定）
        if (tApiUseSetting == null) {
            return createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED,
                    "API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED");
        }

        // API利用設定の上限値がnullでない
        if (tApiUseSetting.getMaxVal() != null) {

            DemandCalendarYearData demandCalendarYearData;
            try {
                // API利用設定を元に対象日を基準に範囲日を取得
                demandCalendarYearData = getApiUseResultRangeDate(tApiUseSetting, new Date(osolApiServerDateTimeDao.query(null).getTime()));
            } catch (NumberFormatException e) {
                // API利用設定.締め日(開始日)に不正な値が設定されている場合
                return createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED,
                        "API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED");
            }

            // API利用実績取得（API利用設定に応じた範囲取得）
            List<OsolApiUseResultListDetailResultData> resultList =
                    osolApiUseManagementDao.getApiUseResultList(corpId, apiKind, demandCalendarYearData.getMonthStartDate(), demandCalendarYearData.getMonthEndDate());

            // API利用回数を集計
            int count = resultList.stream()
                    .mapToInt(result -> result.getApiCount())
                    .sum();

            // API利用実績カウントがAPI利用設定の上限値以上の場合はエラー応答
            if (count >= tApiUseSetting.getMaxVal().intValue()) {
                return createErrorMessageResponse(OsolApiResultCode.API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED,
                        "API_ERROR_AUTHORIZATION_USE_LIMIT_COUNT_EXCEEDED");
            }
        }

        return null;
    }

    // API利用カウント（返却値：正常true、異常false）
    private boolean apiUseCount(BaseApiResponse baseApiResponse, String corpId, String apiKind, MPerson mPerson,
            String apiBeanName, String remoteAddr) {

        boolean ret = true;
        OsolApiResponse<?> apiResponse = (OsolApiResponse<?>) baseApiResponse;

        // ログ出力
        apiLogger.infof("%s %s %s [%s] [%s] [%s]", corpId, mPerson.getId().getPersonId(), remoteAddr, apiKind, apiBeanName.replace(strBean, ""),  apiResponse.getResultCode());

        // 正常応答
        if (OsolApiResultCode.API_OK.equals(apiResponse.getResultCode())) {
            // API利用実績をカウント
            osolApiUseManagementDao.createApiUseResult(corpId, apiKind, mPerson.getUserId());
        } else {
            ret = false;
        }

        return ret;
    }

    // API警告メールを送信チェック
    private ApiUseConstants.API_ALERT_MAIL_SEND_TYPE checkApiAlertMail(TApiUseSetting tApiUseSetting) {

        // デフォルトでメール送信なしを設定
        ApiUseConstants.API_ALERT_MAIL_SEND_TYPE ret = ApiUseConstants.API_ALERT_MAIL_SEND_TYPE.NONE_MAIL;

        // API利用設定を元に対象日を基準に範囲日を取得
        DemandCalendarYearData demandCalendarYearData =
                getApiUseResultRangeDate(tApiUseSetting, new Date(osolApiServerDateTimeDao.query(null).getTime()));

        // API利用実績情報を取得
        List<OsolApiUseResultListDetailResultData> resultList =
                osolApiUseManagementDao.getApiUseResultList(tApiUseSetting.getId().getCorpId(), tApiUseSetting.getId().getApiKind(),
                        demandCalendarYearData.getMonthStartDate(), demandCalendarYearData.getMonthEndDate());

        // API利用回数を集計
        int count =  resultList.stream()
                .mapToInt(result -> result.getApiCount())
                .sum();

        // API利用設定.上限値がnullでない、かつAPI利用実績のカウント集計がAPI利用設定.上限値と一致した場合
        if (tApiUseSetting.getMaxVal() != null && count == tApiUseSetting.getMaxVal().intValue()) {
            // メール送信（上限メール）
            ret = ApiUseConstants.API_ALERT_MAIL_SEND_TYPE.MAX_VALUE_MAIL;
        }
        // API利用設定.予測値がnullでない、かつAPI利用実績のカウント集計がAPI利用設定.予測値と一致した場合
        else if (tApiUseSetting.getPredictionVal() != null && count == tApiUseSetting.getPredictionVal().intValue()) {
            // メール送信（警告メール）
            ret = ApiUseConstants.API_ALERT_MAIL_SEND_TYPE.ALERT_VALUE_MAIL;
        }
        return ret;
    }

    // API警告メールを送信
    private boolean sendApiAlertMail(TApiUseSetting tApiUseSetting, ApiUseConstants.API_ALERT_MAIL_SEND_TYPE mailType) {

        Date today = new Date(osolApiServerDateTimeDao.query(null).getTime());

        // 初期値として、「-」を設定
        String alertValue = ApiUseConstants.NOT_SETTING_MESSAGE;
        String maxValue = ApiUseConstants.NOT_SETTING_MESSAGE;

        // 件名に追加する文字（EMS or 機器制御 or その他）
        String subjectApiKind = OsolConstants.STR_EMPTY;
        for (ApiUseConstants.API_KIND apiKind : ApiUseConstants.API_KIND.values()) {
            if (apiKind.getVal().equals(tApiUseSetting.getId().getApiKind())) {
                subjectApiKind = apiKind.getStr();
            }
        }

        // 件名に追加する文字（上限到達 or 警告）
        String subjectMailType = mailType.equals(ApiUseConstants.API_ALERT_MAIL_SEND_TYPE.MAX_VALUE_MAIL)
                ? mailType.getStr().concat(ApiUseConstants.SUBJECT_MAX_VALUE_MAIL) : mailType.getStr();

        // API利用可能期間取得
        DemandCalendarYearData demandCalendarYearData =
                getApiUseResultRangeDate(tApiUseSetting, today);

        String fromToDateStr = DateUtility.changeDateFormat(
                demandCalendarYearData.getMonthStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                .concat(" ～ ")
                .concat(DateUtility.changeDateFormat(
                demandCalendarYearData.getMonthEndDate(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));

        // 途中で予測という単語は、警告に変更になったので単語が混在している。（予測 → 警告）
        if (tApiUseSetting.getPredictionVal() != null) {
            alertValue = tApiUseSetting.getPredictionVal().toString();
        }

        if (tApiUseSetting.getMaxVal() != null) {
            maxValue = tApiUseSetting.getMaxVal().toString();
        }

        // メールの本文を設定
        String mailTemplateDir = osolConfigs.getConfig(OsolConstants.MAIL_TEMP_DIR);
        BaseVelocity apiAlertMailVm = new BaseVelocity("ApiAlertMail.vm", mailTemplateDir);
        apiAlertMailVm.put("mailType", mailType.getStr());
        apiAlertMailVm.put("alertValue", alertValue);
        apiAlertMailVm.put("maxValue", maxValue);
        apiAlertMailVm.put("fromToDate", fromToDateStr);
        String messageContent = apiAlertMailVm.merge();
        // メールの件名を設定
        //【O-SOL】【EMS/機器制御/その他】【警告/上限到達】企業名：API利用回数のお知らせ [YYYY/MM/DD]
        StringBuilder buf = new StringBuilder();
        buf.append("【O-SOL】");
        buf.append("【");
        buf.append(subjectApiKind);
        buf.append("】");
        buf.append("【");
        buf.append(subjectMailType);
        buf.append("】");
        buf.append(tApiUseSetting.getId().getCorpId());
        buf.append("：");
        buf.append("API利用回数のお知らせ");
        buf.append(" ");
        buf.append("[");
        buf.append(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
        buf.append("]");
        String messageSubject = buf.toString();
        eventLogger.debug("Subject:".concat(messageSubject));
        eventLogger.debug("body:".concat(messageContent));

        // 送信元アドレス
        String mailFrom = osolConfigs.getConfig("mail.postmail.fromaddress");

        // デフォルト宛先(メーリングリストから取得)
        List<String> defaultAddressList = getDefaultAddressList();

        //API警告メール送信設定から対象企業のメール設定を取得
        List<TApiAlertMailSetting> mailSettingList = osolApiUseManagementDao.getApiAlertMailSettingList(tApiUseSetting.getId().getCorpId());

        //TO,CC,BCCのそれぞれのアドレスリストを用意
        List<String> toAddressList = new ArrayList<>();
        List<String> ccAddressList = new ArrayList<>();
        List<String> bccAddressList = new ArrayList<>();

        if (mailSettingList == null || mailSettingList.isEmpty()) {
            //メール送信設定が取得できていない場合は、デフォルトの宛先をTOに設定して送信
            toAddressList.addAll(defaultAddressList);
        } else {
            //メール送信設定が取得できる場合は、設定内容に従って、送信
            for (TApiAlertMailSetting mailSetting : mailSettingList) {

                if (OsolConstants.FLG_ON.equals(mailSetting.getDeliveryStopFlg())) {
                    //配信停止の場合、宛先から除く
                    continue;
                }

                if (OsolConstants.FLG_ON.equals(mailSetting.getDefaultDestinationFlg())) {
                    //デフォルト送信先フラグがたっている場合、メールアドレスはデフォルトの宛先を設定する
                    if (OsolConstants.DESTINATION_KBN.TO.getVal().equals(mailSetting.getDestinationType())) {
                        toAddressList.addAll(defaultAddressList);
                    } else if (OsolConstants.DESTINATION_KBN.CC.getVal().equals(mailSetting.getDestinationType())) {
                        ccAddressList.addAll(defaultAddressList);
                    } else {
                        bccAddressList.addAll(defaultAddressList);
                    }
                } else {
                    //デフォルト送信先フラグがたっていない場合は、設定されているメールアドレスを設定する
                    if (OsolConstants.DESTINATION_KBN.TO.getVal().equals(mailSetting.getDestinationType())) {
                        toAddressList.add(mailSetting.getMailAddress());
                    } else if (OsolConstants.DESTINATION_KBN.CC.getVal().equals(mailSetting.getDestinationType())) {
                        ccAddressList.add(mailSetting.getMailAddress());
                    } else {
                        bccAddressList.add(mailSetting.getMailAddress());
                    }
                }
            }
        }

        eventLogger.info(this.getClass().getSimpleName() + " Subject：" + messageSubject);
        eventLogger.info(this.getClass().getSimpleName() + " toAddressList：" + toAddressList);
        eventLogger.info(this.getClass().getSimpleName() + " ccAddressList：" + ccAddressList);
        eventLogger.info(this.getClass().getSimpleName() + " bccAddressList：" + bccAddressList);

        if (toAddressList.isEmpty() && ccAddressList.isEmpty() && bccAddressList.isEmpty()) {
            //TO、CC、BCCのいずれにもアドレスの設定がない場合はメール送信をしないで処理を終了する（すべて配信停止の場合）
            return true;
        }

        // メール送信フラグ
        String useSendStr = osolConfigs.getConfig("mail.send.available");
        boolean useSendMailFlg = false;
        if (useSendStr != null && OsolConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }
        // パラメータ作成
        OsolMailParameter osolmailParameter = new OsolMailParameter(messageSubject, messageContent, mailFrom,
                toAddressList, ccAddressList, bccAddressList, useSendMailFlg);

        // メール送信
        OsolApiMailService osolApiMailService = new OsolApiMailService();
        if (!osolApiMailService.mailSend(osolmailParameter)) {
            errorLogger.error(this.getClass().getSimpleName() + " sendApiAlertMail failure");
            return false;
        }
        return true;
    }

    /**
     * API利用設定を元に対象日を基準に範囲日を取得
     * @param tApiUseSetting
     * @param today
     * @return
     */
    private DemandCalendarYearData getApiUseResultRangeDate(TApiUseSetting tApiUseSetting, Date tagetDate) throws NumberFormatException {

        // 基準日
        Date today = DateUtility.conversionDate(
                DateUtility.changeDateFormat(tagetDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        String closingDay = String.format("%02d", Integer.parseInt(tApiUseSetting.getClosingDay()));

        //mmdd
        String sumDate = DateUtility.changeDateFormat(DateUtility.plusMonth(today, -1),DateUtility.DATE_FORMAT_MM)
                .concat(closingDay);

        if (OsolConstants.FLG_OFF.compareTo(tApiUseSetting.getClosingDaySettingFlg()) == 0) {
            // mmddhh（開始日）
            sumDate = sumDate.concat(ApiUseConstants.START_SUM_DATE);
        } else {
            // mmddhh（締め日）
            sumDate = sumDate.concat(ApiUseConstants.STOP_SUM_DATE);
        }

        // カレンダを取得する
        DemandCalendarYearData demandCalendarYearData = DemandCalendarYearUtility.getCalendarYear(
                CalendarUtil.dateFormat_yyyyMMdd.format(today), sumDate);

        return demandCalendarYearData;
    }

    /**
     * 宛先メーリングリスト取得処理
     *
     * @return メーリングリスト
     */
    private List<String> getDefaultAddressList() {

        List<String> retList = new ArrayList<>();

        // メーリングリストのディレクトリ
        String templateDirStr = osolConfigs.getConfig(OsolConstants.MAIL_TEMP_DIR);

        String zipFilePath = templateDirStr + "/"
                + osolConfigs.getConfig(OsolConstants.MAIL_TEMP_ADDRZIPFILE);

        // 展開先は同じディレクトリにしている
        if (BaseFileZipArchive.unZip(zipFilePath, templateDirStr)) {
            // メーリングリストCSVファイルの読み込み

            String csvFilePath = templateDirStr + "/"
                    + osolConfigs.getConfig(OsolConstants.MAIL_TEMP_ADDRFILE);

            WebMailCsvConverter webMailCsvConverter = new WebMailCsvConverter();
            List<WebMailRecord> tempList = webMailCsvConverter.getMaillingList(csvFilePath,
                    osolConfigs.getConfig(OsolConstants.MAIL_TEMP_CSVCHARSET));
            if (tempList == null || tempList.isEmpty()) {
                eventLogger.error(this.getClass().getName() + " getMaillingList failure");
                return null;
            }
            for (WebMailRecord target : tempList) {
                retList.add(target.getMailAddress());
            }

        } else {
            // 解凍できていない場合
            eventLogger.error(this.getClass().getName() + " unZip failure zipFilePath=" + zipFilePath);
            return null;
        }
        return retList;
    }

    /**
     * リクエスト元のIPアドレス取得
     * @param request
     * @return
     */
    private String getRemoteAddr(HttpServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteAddr = httpRequest.getRemoteAddr();
        String xForwardFor = httpRequest.getHeader("X-Forwarded-For");
        if(xForwardFor != null){
            String[] list = xForwardFor.split(",");
            remoteAddr = list[list.length-1].trim();
        }
        return remoteAddr;
    }

}
