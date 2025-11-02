package jp.co.osaki.sms.bean.login;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.time.DateUtils;

import jp.co.osaki.osol.OemData;
import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolEncipher;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.dao.MCorpFunctionUseDao;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyCreateResponse;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.HostInfo;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.common.apikey.ApikeyDialogBean;
import jp.co.osaki.sms.bean.menu.IndexBean;
import jp.co.osaki.sms.dao.MPersonDao;
import jp.co.osaki.sms.dao.TOshiraseDao;

/**
 *
 * ログイン画面
 *
 * @author d-komatsubara
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean extends SmsBean implements Serializable {

    private static final long serialVersionUID = -5305451309937115596L;

    //利用規約
    private static final String TERMS_OF_SERVICE_PATH = "/home/wildfly/osol/template/SMS-TermsOfService.pdf";

    //利用規約PDF
    private static final String TERMS_OF_SERVICE_PDF = "SMSシステム利用規約.pdf";

    //プライバシーポリシー設定
    private static final String PRIVACY_POLICY_URL = "https://www.osaki.co.jp/ja/privacy.html";

    //お問い合わせ
    private static final String INQUIRY_URL = "https://www.osaki.co.jp/ja/contact.html";

    private String personId;

    private String corpId;

    private String password;

    private String stateMessage;

    private boolean passExpirationDate;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private IndexBean indexBean;

    @Inject
    private EditPasswordBean loginEditPasswordBean;

    @Inject
    private OsolEncipher osolEncipher;

    @Inject
    private SmsFileDownload smsFileDownload;

    @Inject
    private ApikeyDialogBean apiKeyDialog;

    private OemData oemData = new OemData();

    @EJB
    MPersonDao mPersonDao;

    @EJB
    TOshiraseDao tOshiraseDao;

    @EJB
    private MCorpFunctionUseDao mCorpFunctionUseDao;

    @Override
    public String init() {
        // セッションクリア
        clearSession();
        return "login";
    }

    public String login() {
        // アクセスログ出力
        exportAccessLog("login", "ボタン「ログイン」押下");

        eventLogger.debug(LoginBean.class.getPackage().getName().concat(":START"));

        // ユーザー情報取得(企業ID/ユーザーID)
        MPerson mPerson = mPersonDao.find(corpId, personId);
        if (mPerson == null) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: User is not found in m_person."));
            addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
            return STR_EMPTY;
        }

        // 削除、アカウント停止、10回パスワード間違いを確認
        if (1 == mPerson.getDelFlg() || 1 == mPerson.getAccountStopFlg()
                || OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT <= mPerson.getPassMissCount()) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Invalid user error."));
            addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
            return STR_EMPTY;
        }

        // 企業が利用停止期間中かを判定
        if (mPerson.getMCorp().getUseStopStartDate() != null) {
            final long now = DateUtils.truncate((((Date) mPersonDao.getSvDate())), Calendar.DAY_OF_MONTH).getTime();

            // 現在日付が利用停止開始日より未来、且つ利用停止終了日より過去であれば利用停止期間中のためログイン不可
            // 現在日付が利用停止開始日より未来で利用停止終了日が空の場合もログイン不可。
            if (now >= mPerson.getMCorp().getUseStopStartDate().getTime()) {
                if (mPerson.getMCorp().getUseStopEndDate() == null) {
                    eventLogger.debug(LoginBean.class.getPackage().getName().concat(
                            ":: Corporate information is being used stop period. (Use stop start date judgment)"));
                    addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
                    return STR_EMPTY;
                } else if (now <= mPerson.getMCorp().getUseStopEndDate().getTime()) {
                    eventLogger.debug(LoginBean.class.getPackage().getName().concat(
                            ":: Corporate information is being used stop period. (Use stop end date judgment)"));
                    addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
                    return STR_EMPTY;
                }
            }
        }

        // SMS利用権限のある企業かチェック（大崎権限が無くても許可、メンテナンス企業は存在してもログイン不可）
        boolean corpFunctionUse = false;    // false:不許可, true:許可
        if (mPerson.getMCorp() != null && mPerson.getMCorp().getCorpType() != null) {
            switch (mPerson.getMCorp().getCorpType()) {
                // 大崎電気工業
                case "0":
                    corpFunctionUse = true;
                    break;
                // メンテナンス
                case "2":
                    corpFunctionUse = false;
                    break;
                // パートナー/契約企業
                case "1":
                case "3":
                    MCorpFunctionUse mCorpFunctionUse = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.SMS.getVal(), mPerson.getId().getCorpId());
                    if (mCorpFunctionUse != null && OsolConstants.FLG_ON.equals(mCorpFunctionUse.getUseFlg())) {
                        corpFunctionUse = true;
                    }
                    break;
                default:
                    break;
            }
        }

        // SMS権限チェック
        if (!corpFunctionUse) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Invalid use SMS Function error."));
            // メッセージは理由を特定させない為のふわっとした共通のものを出す。
            addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
            return STR_EMPTY;
        }


        String dbPassword;
        boolean temporary_password = false; // 正規パスワード

        // パスワード状態の判定
        if (null == mPerson.getTempPassword()) {
            // 仮パスワードがNULLの場合を正規パスワードと判断
            // 正規パスワード
            dbPassword = mPerson.getPassword();

        } else {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Temporary password user route..."));

            // 仮パスワード有効期限の確認
            Date expirationDate = mPerson.getTempPassExpirationDate();
            if (null != expirationDate) {

                // DB基準日をとる(現在日付)
                Date Date2 = mPersonDao.getSvDate();

                String tempDate = DateUtility.changeDateFormat(Date2, DateUtility.DATE_FORMAT_YYYYMMDD);
                Date currentDate = DateUtility.conversionDate(tempDate, DateUtility.DATE_FORMAT_YYYYMMDD);

                int diff = expirationDate.compareTo(currentDate);
                eventLogger.debug(LoginBean.class.getPackage().getName().concat(" diff(" + diff + ")"));

                if (diff < 0) {
                    eventLogger.debug(LoginBean.class.getPackage().getName()
                            .concat(":: Expiration date of the temporary password has been exceeded."));
                    // 仮パスワードの有効期限についてのバリデーションメッセージが設計書にない
                    // addErrorMessage("仮パスワードの有効期限を超過しています。");
                    addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
                    return STR_EMPTY;
                }
            }
            // 仮パスワード
            dbPassword = mPerson.getTempPassword();
            temporary_password = true;
        }

        // パスワードチェック
        if (!osolEncipher.verify(password, dbPassword)) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password unmatch error."));
            addErrorMessage(beanMessages.getMessage("loginBean.error.invalidUserError"));
            Integer count = mPerson.getPassMissCount() + 1;
            //10回パスワード間違いでアカウントロック。メッセージは理由を特定させない為のふわっとした共通のものを出す。
            if (count == OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {
                eventLogger.debug(
                        LoginBean.class.getPackage().getName().concat(":: login failed 10 times. account was locked."));
                // ロックしました。管理者へお問い合わせください。
                addErrorMessage(beanMessages.getMessage("loginBean.error.passwordLock"));
            }
            // カウントアップし対象ユーザーを更新
            mPerson.setPassMissCount(count);
            mPersonDao.merge(mPerson);
            return STR_EMPTY;
        }

        mPerson.setPassMissCount(0);
        mPerson.setLastLoginDate(mPersonDao.getSvDate());
        mPerson = mPersonDao.merge(mPerson);

        // セッションクリア
        clearSession();
        // セッション情報の作成
        createSession();

        this.setLoginInfo(mPerson); //ログイン情報設定
        List<MCorpPersonAuth> mCorpPersonAuthList = new ArrayList<>();
        List<MCorpPerson> mCorpPersonList = new ArrayList<>();
        List<TBuildingPerson> tBuildingPersonList = new ArrayList<>();
        this.setLoginCorpPersonAuthListAll(mCorpPersonAuthList);
        this.setLoginCorpPersonListAll(mCorpPersonList);
        this.setLoginBuildingPersonListAll(tBuildingPersonList);
        this.setHostName(oemData.getHostName());
        this.setInqury(oemData.getInquiry());
        this.setUserGuide(oemData.getUserGuide());
        this.setUserGuidePdf(oemData.getUserGuidePdf());
        this.setTermsOfService(oemData.getTermsOfService());
        this.setTermsOfServicePdf(oemData.getTermsOfServicePdf());
        this.setLogo(oemData.getLogo());
        this.setPrivacy(oemData.getPrivacy());
        this.setFavicon(oemData.getFavicon());
        this.setCopyright(oemData.getCopyright());


        //APIキー取得
        OsolApiKeyCreateResponse keyCreateResponse = new OsolApiKeyCreateResponse();
        OsolApiKeyCreateParameter keyCreateParameter = new OsolApiKeyCreateParameter();
        SmsApiGateway keyCreateGateway = new SmsApiGateway();
        keyCreateParameter.setBean("OsolApiKeyCreateBean");
        keyCreateResponse = (OsolApiKeyCreateResponse) keyCreateGateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON, keyCreateParameter, keyCreateResponse);

        if (!OsolApiResultCode.API_OK.equals(keyCreateResponse.getResultCode())) {
            addErrorMessage(
                    beanMessages
                            .getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(keyCreateResponse.getResultCode())));
            return STR_EMPTY;
        }

        this.setApiKey(keyCreateResponse.getResult().getApiKey());

        mPersonDao.setLoginUserInfo(mPerson.getId().getCorpId(), mPerson.getId().getPersonId(), mCorpPersonAuthList,
                mCorpPersonList, tBuildingPersonList);
        //担当建物情報
        //        this.setLoginBuildingPersonListAll(mPersonDao.getBuildingPersonList(mPerson.getId().getCorpId(), mPerson.getId().getPersonId()));
        //担当権限情報
        //        this.setLoginCorpPersonAuthListAll(mPersonDao.getCorpPersonAuth(mPerson.getId().getCorpId(), mPerson.getId().getPersonId()));

        //S3から企業ロゴをダウンロード
        smsFileDownload.S3fileDownload(getLoginCorp().getCorpLogoImageFilePath());

        eventLogger.debug(LoginBean.class.getPackage().getName().concat(":END"));
        // お知らせ画面を表示させるフラグ
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.FIRST_ACCESS_FLG.getVal(), "true");
        // 画面遷移
        if (temporary_password) {
            // 仮パスワードの場合はパスワード変更画面へ
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Select editPassword route..."));
            return loginEditPasswordBean.init();
        } else {
            setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.SET_PASSWORD.getVal(), SESSION_VAL_OK);
            // パスワード最終更新時間チェック
            String passwordLimitDays = this.getWrapped().getInitParameter(OsolConstants.LOGIN_PASS_EXPIRED_DAYS);
            if (!CheckUtility.isNullOrEmpty(passwordLimitDays)
                    && checkPassExpirationDate(mPerson.getUpdatePassDate(), Integer.parseInt(passwordLimitDays))) {
                return STR_EMPTY;
            }
            return indexBean.init();
        }
    }

    /**
     * 最終要求URLの保持。ブラウザバック検知に使う
     *
     * @param url
     */
    public void setRequestUrl(String url) {
        this.setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.LAST_REQUEST_URL.getVal(), url);
    }

    @Override
    @Logged
    public String logout() {
        // アクセスログ出力
        exportAccessLog("logout", "ボタン「ログアウト」押下");

        // セッションクリア
        clearSession();
        return init();
    }

    public String getLoginCorpLogo() {
        if(isMenuLogoBool()) {
            return getLoginCorp().getCorpLogoImageFilePath();
        }
        return STR_EMPTY;
    }

    public boolean isMenuLogoBool() {
        String corpLogoImageFilePath = getLoginCorp().getCorpLogoImageFilePath();
        if (CheckUtility.isNullOrEmpty(corpLogoImageFilePath)) {
            return false;
        }
        return true;
    }

    /**
     * パスワード有効期間チェック<br>
     *
     * @param timestamp
     * @param passwordStopDays
     * @return true:有効期限一ヶ月以内 false:有効期限一ヶ月より前
     */
    private boolean checkPassExpirationDate(Timestamp timestamp, int passwordStopDays) {
        // 今日
        Date today = DateUtils.truncate(mPersonDao.getSvDate(), Calendar.DAY_OF_MONTH);
        // 最終更新日時
        Date lastUpdate = DateUtils.truncate(timestamp, Calendar.DAY_OF_MONTH);
        // 利用制限日
        Date stopUseDate = DateUtility.plusDay(lastUpdate, passwordStopDays);

        if (stopUseDate.compareTo(today) < 0) {
            passExpirationDate = true;
        } else {
            passExpirationDate = false;
        }
        eventLogger.debug(this.getClass().getName().concat(
                " 今日 = " + DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH)
                        + " 最終更新日時 = "
                        + DateUtility.changeDateFormat(lastUpdate, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH)
                        + " 制限日 = "
                        + DateUtility.changeDateFormat(stopUseDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH)));
        return passExpirationDate;
    }

    /**
     * パスワード最終更新日を更新する。
     *
     * @return
     */
    public String extendPassword() {
        // アクセスログ出力
        exportAccessLog("extendPassword", "パスワード期限通知画面にてボタン「そのまま延長する」押下");

        MPerson mPerson = mPersonDao.find(corpId, personId);
        mPerson.setUpdatePassDate(mPersonDao.getSvDate());
        mPersonDao.merge(mPerson);
        this.setLoginPerson(mPerson); // 担当者
        return indexBean.init();
    }

    /**
     * パスワード変更画面へ進む。
     *
     * @return
     */
    public String goEditPassword() {
        return loginEditPasswordBean.init();
    }

    /**
     * パスワード有効期間チェック結果取得
     *
     * @return true:有効期限一ヶ月以内 false:有効期限一ヶ月より前
     */
    public boolean isPassExpirationDate() {
        return passExpirationDate;
    }

    /**
     * APIキーボタン表示
     */
    public boolean getApiKeyVisble() {
        // ログインしたユーザーがOsakiまたはパートナー
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(getLoginCorpType())
                || OsolConstants.CORP_TYPE.PARTNER.getVal().equals(getLoginCorpType())) {
            return true;
        }
        return false;
    }

    /**
     * APIキーボタン押下時処理
     */
    @Logged
    public void showApiKeyDialog() {
        apiKeyDialog.loadApiKey();
    }

    /**
     * 操作終了が可能か
     *
     * @return
     */
    @Override
    public boolean isLoginOperationEndEnable() {

        return super.isLoginOperationEndEnable();
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getHostName() {
        return HostInfo.getHostName();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getPrivacy() {
        return PRIVACY_POLICY_URL;
    }

    public String getInqury(){
        return INQUIRY_URL;
    }

    public String getLogo() {
        if(oemData.getLogo() == null) {
            oemData.init();
        }

        return oemData.getLogo();
    }
    public String getCopyright() {
        if(oemData.getCopyright() == null) {
            oemData.init();
        }

        return oemData.getCopyright();
    }
    public String getFavicon() {
        if(oemData.getFavicon() == null) {
            oemData.init();
        }

        return oemData.getFavicon();
    }

    public String getTermsOfService() {
        if(oemData.getTermsOfService() == null) {
            oemData.init();
        }
        return oemData.getTermsOfService();
    }

    /**
     * favicon 表示非表示の判定
     */
    public boolean isFaviconBool() {
        if(getFavicon().equals("osol")) {
            return true;
        }

        return false;
    }

    /**
     * 個人情報保護の取り組み 空文字・表示判定
     * @return
     */
    public boolean isPrivacyBool() {
        if(getPrivacy().equals(STR_EMPTY)) {
            return false;
        }
        return true;
    }

    /**
     * お問い合わせ 空文字・表示判定
     * @return
     */
    public boolean isInquryBool() {
        if(getInqury().equals(STR_EMPTY)) {
            return false;
        }
        return true;
    }

    public boolean isTermsOfServiceBool() {
        if(getTermsOfService().equals(STR_EMPTY)) {
            return false;
        }
        return true;
    }

    /**
     * logo 表示非表示の判定
     */
    public boolean isLogoBool() {
        if(getLogo().equals("osol")) {
            return true;
        }
        return false;
    }

    @Logged
    public String termsOfService() {
        int ret = smsFileDownload.fileDownload(TERMS_OF_SERVICE_PATH, TERMS_OF_SERVICE_PDF, true);

        if (ret != RETURN_CODE.SUCCESS.getInt()) {
            eventLogger.debug(this.getClass().getName().concat(".downloadFile():Error ret(" + ret + ")"));
            addErrorMessage(beanMessages.getMessage("osol.error.fileDownload"));
        }
        return STR_EMPTY;
    }

}
