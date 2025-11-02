package jp.co.osaki.sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.skygroup.enl.webap.base.BaseBean;

/**
 *
 * OSOLログインユーザー情報クラス
 *
 * OSOL固有のユーザー情報および企業担当権限、建物担当へのアクセスを提供する
 *
 * @author take_suzuki
 */
public abstract class SmsLoginUserInfo extends BaseBean {

    @Override
    protected String logout() {
        return super.logout();
    }

    /**
     * ログイン情報設定
     *
     * @param mPerson
     *
     */
    protected void setLoginInfo(MPerson mPerson) {
        //セッションにログインユーザー情報を設定
        setLoginPerson(mPerson);
        //ログインと同時に操作中企業も設定
        setLoginOperationCorp(mPerson.getMCorp());
    }

    /**
     * ログインユーザー企業情報
     *
     * @return
     *
     */
    protected MCorp getLoginCorp() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getMCorp();
        }
        return null;
    }

    /**
     * ログインユーザー企業ID
     *
     * @return 現在ログインしている担当者の企業ID
     */
    public String getLoginCorpId() {
        if (getLoginCorp() != null) {
            return getLoginCorp().getCorpId();
        }
        return STR_EMPTY;
    }

    /**
     * ログインユーザー企業名
     *
     * @return 現在ログインしている担当者の企業名
     */
    public String getLoginCorpName() {
        if (getLoginCorp() != null) {
            return getLoginCorp().getCorpName();
        }
        return STR_EMPTY;
    }

    /**
     * ログインユーザー権限グループ
     *
     * @return
     */
    public String getLoginCorpType() {
        if (getLoginCorp() != null) {
            return getLoginCorp().getCorpType();
        } else {
            return STR_EMPTY;
        }
    }

    /**
     * ログイン担当者情報
     *
     * @param mPerson
     */
    protected void setLoginPerson(MPerson mPerson) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal(), mPerson);
    }

    /**
     * ログイン担当者情報
     *
     * @return
     */
    protected MPerson getLoginPerson() {
        return (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());
    }

    /**
     * ログイン担当者ID
     *
     * @return 現在ログインしている担当者の担当者ID
     */
    public String getLoginPersonId() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getId().getPersonId();
        }
        return STR_EMPTY;
    }

    /**
     * ログイン担当者名
     *
     * @return 現在ログインしている担当者の担当者名
     */
    public String getLoginPersonName() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getPersonName();
        }
        return STR_EMPTY;
    }

    /**
     * ログインユーザー識別ID
     *
     * @return 現在ログインしている担当者のユーザー識別ID
     */
    public Long getLoginUserId() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getUserId();
        } else {
            return Long.valueOf(0);
        }
    }

    /**
     * ログインユーザーVersion
     *
     * @return 現在ログインしている担当者(M_Person)のVersion
     */
    public Integer getLoginPersonVersion() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getVersion();
        } else {
            return 0;
        }
    }

    /**
     * ログインユーザー管理権限有無
     *
     * @return
     */
    public boolean isLoginPersonAdmin() {
        if (getLoginPerson() != null) {
            return getLoginPerson().getPersonType().equals(OsolConstants.PERSON_TYPE.ADMIN.getVal());
        } else {
            return false;
        }
    }

    /**
     * ログインユーザーが現在操作している企業
     *
     * @param mCorp ログインユーザーが現在操作している企業
     */
    protected void setLoginOperationCorp(MCorp mCorp) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.OPERATION_CORP.getVal(), mCorp);
    }

    /**
     * ログインユーザーが現在操作している企業
     *
     * @return ログインユーザーが現在操作している企業
     */
    protected MCorp getLoginOperationCorp() {
        return (MCorp) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.OPERATION_CORP.getVal());
    }

    /**
     * ログインユーザーが現在操作している企業ID
     *
     * @return ログインユーザーが現在操作している企業ID
     */
    public String getLoginOperationCorpId() {
        if (getLoginOperationCorp() != null) {
            return getLoginOperationCorp().getCorpId();
        } else {
            return STR_EMPTY;
        }
    }

    /**
     * ログインユーザーが現在操作している企業名
     *
     * @return ログインユーザーが現在操作している企業名
     */
    public String getLoginOperationCorpName() {
        if (getLoginOperationCorp() != null) {
            return getLoginOperationCorp().getCorpName();
        } else {
            return STR_EMPTY;
        }
    }

    /**
     * ログインユーザーが現在操作している企業の企業種別
     *
     * @return ログインユーザーが現在操作している企業の企業種別
     */
    public String getLoginOperationCorpType() {
        if (getLoginOperationCorp() != null) {
            return getLoginOperationCorp().getCorpType();
        } else {
            return STR_EMPTY;
        }
    }

    /**
     * ログインユーザーが現在操作している担当企業の権限種別
     *
     * @return ログインユーザーが現在操作している担当企業の権限種別
     */
    public String getLoginOperationCorpAuthorityType() {

        if (getLoginPerson() != null && getLoginOperationCorp() != null && this.getLoginCorpPersonListAll() != null) {
            for (MCorpPerson mCorpPerson : this.getLoginCorpPersonListAll()) {
                if (getLoginOperationCorp().getCorpId().equals(mCorpPerson.getMCorp().getCorpId())) {
                    return mCorpPerson.getAuthorityType();
                }
            }
        }
        return STR_EMPTY;
    }

    /**
     * 企業選択後か
     *
     * @return
     */
    public boolean isLoginOperationCorpSelected() {
        //一般権限グループは選択後扱い
        if (getLoginCorpType().equals(OsolConstants.CORP_TYPE.CONTRACT.getVal())) {
            return true;
        }
        //操作企業が自企業でなければ選択後
        if ((getLoginCorpId() != null) && (getLoginOperationCorpId() != null)) {
            return !getLoginCorpId().equals(getLoginOperationCorpId());
        } else {
            return false;
        }
    }

    /**
     * 操作終了が可能か
     *
     * @return
     */
    public boolean isLoginOperationEndEnable() {
        //操作企業が自企業でなければ終了できる
        if ((getLoginCorpId() != null) && (getLoginOperationCorpId() != null)) {
            return !getLoginCorpId().equals(getLoginOperationCorpId());
        } else {
            return false;
        }
    }

    /**
     * 操作中企業での管理権限有無
     *
     * @return
     */
    public boolean isLoginOperationCorpAdmin() {
        if (!isLoginOperationCorpSelected() || getLoginCorpType().equals(OsolConstants.CORP_TYPE.CONTRACT.getVal())) {
            //特定前（一般は特定後扱いなので条件追加）はログイン企業での権限(personのperson_type)を見る
            return this.isLoginPersonAdmin();
        }
        //特定後
        if (this.getLoginCorpType().equals(OsolConstants.CORP_TYPE.OSAKI.getVal())) {
            //大崎はどこでも管理者
            return true;
        }
        //パートナーは企業担当タイプを見る
        List<MCorpPerson> mCorpPersonList = this.getLoginCorpPersonListAll();
        for (MCorpPerson mCorpPerson : mCorpPersonList) {
            if (mCorpPerson.getMCorp().getCorpId().equals(this.getLoginOperationCorpId())) {
                if (mCorpPerson.getAuthorityType().equals(OsolConstants.AUTHORITY_TYPE.CORP.getVal())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 指定企業での管理権限有無
     *
     * @param corpId
     * @return
     */
    protected boolean isTargetCorpAdmin(String corpId) {
        if (this.getLoginCorpType().equals(OsolConstants.CORP_TYPE.OSAKI.getVal())
                && this.isLoginPersonAdmin()) {
            //大崎管理はどこでも管理者
            return true;
        }
        if (corpId.equals(this.getLoginCorpId())) {
            //おそらくないはずだけど自企業指定
            return this.isLoginPersonAdmin();
        }
        if (this.getLoginCorpType().equals(OsolConstants.CORP_TYPE.MAINTENANCE.getVal())) {
            //メンテは自企業以外に管理権限を持つことはない
            return false;
        }
        List<MCorpPerson> mCorpPersonList = this.getLoginCorpPersonListAll();
        for (MCorpPerson mCorpPerson : mCorpPersonList) {
            if (mCorpPerson.getMCorp().getCorpId().equals(corpId)) {
                if (mCorpPerson.getAuthorityType().equals(OsolConstants.AUTHORITY_TYPE.CORP.getVal())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     *
     * ログインユーザーの企業担当権限情報
     *
     * @param corpPersonList
     */
    protected void setLoginCorpPersonListAll(List<MCorpPerson> corpPersonList) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_LIST.getVal(), corpPersonList);
    }

    /**
     *
     * ログインユーザーの企業担当権限情報
     *
     * @return ログインユーザーの企業担当権限情報
     */
    @SuppressWarnings("unchecked")
    protected List<MCorpPerson> getLoginCorpPersonListAll() {
        if (getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_LIST.getVal()) != null) {
            return (List<MCorpPerson>) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_LIST.getVal());
        } else {
            List<MCorpPerson> mCorpPersonList = new ArrayList<>();
            mCorpPersonList.clear();
            return mCorpPersonList;
        }
    }

    /**
     *
     * ログインユーザーの企業操作権限情報
     *
     * @param corpPersonAuthList ユーザーの企業担当権限情報
     */
    protected void setLoginCorpPersonAuthListAll(List<MCorpPersonAuth> corpPersonAuthList) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_AUTH_LIST.getVal(), corpPersonAuthList);
    }

    /**
     *
     * ログインユーザーの企業操作権限情報
     *
     * @return ログインユーザーの企業担当権限情報
     */
    @SuppressWarnings("unchecked")
    protected List<MCorpPersonAuth> getLoginCorpPersonAuthListAll() {

        if (getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_AUTH_LIST.getVal()) != null) {
            return (List<MCorpPersonAuth>) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_AUTH_LIST.getVal());
        } else {
            List<MCorpPersonAuth> modelList = new ArrayList<>();
            modelList.clear();
            return modelList;
        }
    }

    /**
     *
     * ログインユーザーが現在操作している企業に対する担当者権限
     *
     * @return ログインユーザーが現在操作している企業に対する担当者権限
     */
    protected List<MCorpPersonAuth> getLoginCorpPersonAuthList() {

        List<MCorpPersonAuth> mCorpPersonAuthList = new ArrayList<>();
        mCorpPersonAuthList.clear();
        String corpId = this.getLoginCorpId();
        if (this.isLoginOperationCorpSelected()) {
            corpId = this.getLoginOperationCorpId();
        }
        if (this.getLoginOperationCorpId() != null) {
            for (MCorpPersonAuth mCorpPersonAuth : this.getLoginCorpPersonAuthListAll()) {
                if (corpId.equals(mCorpPersonAuth.getMCorpPerson().getMCorp().getCorpId())) {
                    mCorpPersonAuthList.add(mCorpPersonAuth);
                }
            }
        }

        return mCorpPersonAuthList;
    }

    /**
     *
     * ログインユーザーの建物担当情報
     *
     * @param tbuildingPersonList ユーザーの建物担当情報
     */
    protected void setLoginBuildingPersonListAll(List<TBuildingPerson> tbuildingPersonList) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.BUILDING_PERSON_LIST.getVal(), tbuildingPersonList);
    }

    /**
     *
     * ログインユーザーの建物担当情報
     *
     * @return ログインユーザーの建物担当情報
     */
    @SuppressWarnings("unchecked")
    protected List<TBuildingPerson> getLoginBuildingPersonListAll() {
        if (getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.BUILDING_PERSON_LIST.getVal()) != null) {
            return (List<TBuildingPerson>) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.BUILDING_PERSON_LIST.getVal());
        } else {
            List<TBuildingPerson> tBuildingPersonList = new ArrayList<>();
            tBuildingPersonList.clear();
            return tBuildingPersonList;
        }
    }

    /**
     *
     * ログインユーザーが現在操作している企業に対する担当建物
     *
     * @return ログインユーザーが現在操作している企業に対する担当建物
     */
    protected List<TBuildingPerson> getLoginBuildingPersonList() {
        List<TBuildingPerson> tBuildingPersonList = new ArrayList<>();
        tBuildingPersonList.clear();
        if (this.isLoginOperationCorpSelected()) {
            //企業特定後
            for (TBuildingPerson tBuildingPerson : this.getLoginBuildingPersonListAll()) {
                if (this.getLoginOperationCorpId().equals(tBuildingPerson.getId().getCorpId())) {
                    //操作中企業で絞り込み
                    tBuildingPersonList.add(tBuildingPerson);
                }
            }
        } else {  //特定前(担当建物全て(横断))
            tBuildingPersonList = getLoginBuildingPersonListAll();
        }
        return tBuildingPersonList;
    }

    /**
     * 現在操作中企業での操作権限状況
     *
     * @param type 操作権限種別
     * @return 操作権限状態
     */
    protected SmsConstants.OPARATION_FUNCTION_RESULT getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE type) {

        List<CorpPersonAuthResultSet> corpPersonAuthDataList = new ArrayList<>();
        corpPersonAuthDataList.clear();
        for (MCorpPersonAuth mCorpPersonAuth : this.getLoginCorpPersonAuthList()) {
            corpPersonAuthDataList.add(new CorpPersonAuthResultSet(
                    this.getLoginCorpId(),
                    this.getLoginCorpType(),
                    this.getLoginPersonId(),
                    this.getLoginPerson().getPersonType(),
                    this.getLoginOperationCorpId(),
                    this.getLoginOperationCorpType(),
                    this.getLoginOperationCorpAuthorityType(),
                    mCorpPersonAuth.getMAuth().getAuthorityCd(),
                    mCorpPersonAuth.getAuthorityFlg()));
        }

        return SmsCorpOparationAuth.getCorpOparationAuth(type,
                this.getLoginCorpType(),
                getLoginPerson().getPersonType(),
                this.getLoginOperationCorpAuthorityType(),
                this.getLoginOperationCorpType(),
                corpPersonAuthDataList);
    }

    /**
     * 担当者の権限有無で登録系機能を制限
     *
     * @return true:許可、false:許可しない
     */
    protected boolean isSmsAuthControl() {
        // 企業種別
        String corpType = this.getLoginCorpType();
        // ログインユーザーが現在操作している担当企業の権限種別（企業担当 or 建物担当）
        String operationCorpAuthorityType = this.getLoginOperationCorpAuthorityType();

        // 大崎権限
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(corpType)) {
            return true;
        }
        // パートナー企業
        else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(corpType)) {
            if (OsolConstants.AUTHORITY_TYPE.CORP.getVal().equals(operationCorpAuthorityType)) {
                return true;
            }
        }
        // 契約企業
        else if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(corpType)) {
            return this.isLoginPersonAdmin();
        }
        return false;
    }

    /**
     * APIキーをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.API_KEY.getVal(), apiKey);
    }

    /**
     * APIキーを取得する
     *
     * @return
     */
    public String getApiKey() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.API_KEY.getVal());
    }

    /**
     * ドメイン名をセッション情報にセットする
     *
     * @param apiKey
     */
    public void setHostName(String hostName) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.HOSTNAME.getVal(), hostName);
    }

    /**
     * ドメインを取得する
     *
     * @return
     */
    public String getHostName() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.HOSTNAME.getVal());
    }

    /**
     * 問い合わせ先をセッション情報にセットする
     *
     * @param apiKey
     */
    public void setInqury(String inquiry) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.INQUIRY.getVal(), inquiry);
    }

    /**
     * 問い合わせ先を取得する
     *
     * @return
     */
    public String getInqury() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.INQUIRY.getVal());
    }

    /**
     * ユーザーガイドのパスをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setUserGuide(String userGuide) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.USER_GUIDE.getVal(), userGuide);
    }

    /**
     * ユーザーガイドのパスを取得する
     *
     * @return
     */
    public String getUserGuide() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.USER_GUIDE.getVal());
    }
    /**
     * ユーザーガイドPDFをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setUserGuidePdf(String userGuidePdf) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.USER_GUIDE_PDF.getVal(), userGuidePdf);
    }

    /**
     * ユーザーガイドPDFを取得する
     *
     * @return
     */
    public String getUserGuidePdf() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.USER_GUIDE_PDF.getVal());
    }

    /**
     * 利用規約pathをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setTermsOfService(String termsOfService) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.TERMS_OF_SERVICE.getVal(), termsOfService);
    }

    /**
     * 利用規約pathを取得する
     *
     * @return
     */
    public String getTermsOfService() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.TERMS_OF_SERVICE.getVal());
    }

    /**
     * 利用規約PDFをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setTermsOfServicePdf(String termsOfServicePdf) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.TERMS_OF_SERVICE_PDF.getVal(), termsOfServicePdf);
    }

    /**
     * 利用規約PDFを取得する
     *
     * @return
     */
    public String getTermsOfServicePdf() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.TERMS_OF_SERVICE_PDF.getVal());
    }

    /**
     * ロゴをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setLogo(String logo) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.LOGO.getVal(), logo);
    }

    /**
     * ロゴを取得する
     *
     * @return
     */
    public String getLogo() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.LOGO.getVal());
    }
    /**
     * プライバシーをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setPrivacy(String privacy) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PRIVACY.getVal(), privacy);
    }

    /**
     * プライバシーを取得する
     *
     * @return
     */
    public String getPrivacy() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PRIVACY.getVal());
    }

    /**
     * ファビコンをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setFavicon(String favicon) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.FAVICON.getVal(), favicon);
    }

    /**
     * ファビコンを取得する
     *
     * @return
     */
    public String getFavicon() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.FAVICON.getVal());
    }

    /**
     * コピーライトをセッション情報にセットする
     *
     * @param apiKey
     */
    public void setCopyright(String copyright) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.COPYRIGHT.getVal(), copyright);
    }

    /**
     * コピーライトを取得する
     *
     * @return
     */
    public String getCopyright() {
        return (String) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.COPYRIGHT.getVal());
    }

    /**
     * 機器制御画面機器設定時の初期表示設定値をセッション情報にセットする
     * @param val
     */
    public void setDeviceCtrlInitSettings(Map<String, Boolean> val) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.DEVICE_CTRL_INIT_SETTINGS.getVal(), val);
    }

    /**
     * 機器制御画面機器設定時の初期表示設定値を取得する
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> getDeviceCtrlInitSettings() {
        return (Map<String, Boolean>) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.DEVICE_CTRL_INIT_SETTINGS.getVal());
    }

}
