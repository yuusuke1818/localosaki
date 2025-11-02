package jp.co.osaki.sms.bean.common.apikey;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyCreateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.ApiKeyCreateResponse;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.dao.TApiKeyDao;

@Named(value = "commonApikeyDialogBean")
@RequestScoped
public class ApikeyDialogBean extends SmsBean implements Serializable {

    private static final long serialVersionUID = -4317354983022148787L;

    @EJB
    private TApiKeyDao tApiKeyDao;

    // API存在状況フラグ
    private boolean apikeyExistFlg;

    //APIキー
    private String apiKeyString;

    //有効期限
    private String expirationDate;

    // リフレッシュキー
    private String refreshKeyString;

    //発行ボタン表示内容
    private String buttonTitle;

    /**
     * APIキーテーブルのVERSION
     */
    private Integer version;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String init() {
        return "";
    }

    /**
     * APIキーを取得する
     */
    public void loadApiKey() {
        TApiKey apiKey = tApiKeyDao.find(this.getLoginCorpId(), this.getLoginPersonId());

        if (apiKey != null) {
            apiKeyString = apiKey.getApiKey();
            setExpirationDate(DateUtility.changeDateFormat(apiKey.getExpirationDate(),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
            setRefreshKeyString(apiKey.getRefreshKey());
            apikeyExistFlg = true;
            setButtonTitle("再発行");
            this.setVersion(apiKey.getVersion());
        } else {
            apiKeyString = "";
            setExpirationDate("");
            setRefreshKeyString("");
            apikeyExistFlg = false;
            setButtonTitle("発行");
            this.setVersion(null);
        }
    }

    /**
     * APIキーを発行する
     */
    public void issueApiKey() {
        // アクセスログ出力
        exportAccessLog("issueApiKey", "APIキー ボタン「発行」押下");

        // APIキー発行
        ApiKeyCreateResponse response = new ApiKeyCreateResponse();
        ApiKeyCreateParameter parameter = new ApiKeyCreateParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ApiKeyCreateBean");
        parameter.setVersion(this.getVersion());
        response = (ApiKeyCreateResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON, parameter, response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return;
        }

        //APIキー発行後の再取得
        loadApiKey();
    }

    public boolean isApikeyExistFlg() {
        return apikeyExistFlg;
    }

    public void setApikeyExistFlg(boolean apikeyExistFlg) {
        this.apikeyExistFlg = apikeyExistFlg;
    }

    public String getApiKeyString() {
        return apiKeyString;
    }

    public void setApiKeyString(String apiKeyString) {
        this.apiKeyString = apiKeyString;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRefreshKeyString() {
        return refreshKeyString;
    }

    public void setRefreshKeyString(String refreshKeyString) {
        this.refreshKeyString = refreshKeyString;
    }

}
