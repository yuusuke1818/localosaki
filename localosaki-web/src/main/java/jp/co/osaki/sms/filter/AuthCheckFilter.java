package jp.co.osaki.sms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsCorpOparationAuth;
import jp.co.osaki.sms.dao.MPersonDao;

/**
 *
 * 権限チェックフィルター
 *
 * @author a_ogawa
 */
public class AuthCheckFilter extends OsolConstants implements Filter {

    /**
     * 更新チェックしないページリスト
     */
    private final List<String> noCheckPageList;

    /**
     * チェックエラー時のリダイレクト先ページ
     */
    private String redirectPage = null;

    /**
     * FacesServletUrlPattern
     */
    private String facesServletUrlPattern = null;

    private HttpSession session = null;

    @EJB
    MPersonDao mPersonDao;

    @Inject
    private OsolAccessBean osolAccessBean;

    public AuthCheckFilter() {
        this.noCheckPageList = new ArrayList<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean chkPage = false;
        //チェック対象外ページの判定
        for (String page : this.noCheckPageList) {
            if (httpRequest.getRequestURI().contains(page)) {
                chkPage = true;
                break;
            }
        }
        if (chkPage) {
            chain.doFilter(request, response);
        } else {
            session = httpRequest.getSession(false);
            if (session == null) {
                //セッションがない
                chain.doFilter(request, response);
            } else if (session.getAttribute(SESSION_KEY_LOGIN) == null) {
                //未ログインの場合
                chain.doFilter(request, response);
            } else {
                String redirectUrl = null;
                if (authCheck(httpRequest.getRequestURI()) == false) {
                    /* 権限なし */
                    redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern)
                            .concat(this.redirectPage);
                }
                if (redirectUrl != null) {
                    if (Objects.equals(httpRequest.getMethod(), "POST")
                            && Objects.equals(httpRequest.getHeader("Faces-Request"), "partial/ajax")) {
                        //Faces-Request	partial/ajax
                        //ajaxからのPOSTに対してLocationでリダイレクトしても、
                        //リダイレクトしたそのアドレスに対してajaxでpartialにGETしてくるのでサーバーがコンテンツを正常に返せない
                        //ajaxからの正常遷移時と同じ応答本文(jsf.jsの処理に合わせる)を偽装してそこにリダイレクトを仕込む
                        httpResponse.setContentType("text/xml;charset=UTF-8");
                        httpResponse.setHeader("Transfer-Encoding", "chunked");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("<?xml version='1.0' encoding='UTF-8'?>");
                            out.println("<partial-response id=\"j_id1\"><redirect url=\"" + redirectUrl
                                    + "\"></redirect></partial-response>");
                        }
                    } else {
                        //通常のcommandButtonから
                        httpResponse.sendRedirect(redirectUrl);
                    }
                } else {
                    chain.doFilter(request, response);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String noCheckPage = filterConfig.getInitParameter("noCheckPageList");
        this.noCheckPageList.clear();
        if (noCheckPage != null) {
            if (noCheckPage.length() > 0) {
                this.noCheckPageList.addAll(Arrays.asList(noCheckPage.split(",")));
            }
        }
        this.redirectPage = filterConfig.getInitParameter("redirectPage");
        this.facesServletUrlPattern = filterConfig.getInitParameter("facesServletUrlPattern");

    }

    @Override
    public void destroy() {

        this.noCheckPageList.clear();
        this.redirectPage = null;
        this.facesServletUrlPattern = null;

    }

    // Index.Bean.java を参考にしてます。
    private boolean authCheck(String requestURL) {
        if (requestURL == null || requestURL.isEmpty()) {
            return true;
        }
        SmsConstants.OPARATION_FUNCTION_RESULT res = SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;

        // ログイン担当者企業の機能利用にSMSがあるか
        boolean resFunc = getFunctionUse(OsolAccessBean.FUNCTION_CD.SMS);

        // メーター
        if (requestURL.contains("/meter/")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.METER);
        }
        // メーターテナント
        else if (requestURL.contains("/meterTenant/")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.METER_TENANT);
        }
        // 任意検針
        else if (requestURL.contains("/meterreading/manualInspection.xhtml")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.MANUAL_INSPECTION);
        }
        // 確定前検針データ
        else if (requestURL.contains("/meterreading/inspectionMeterBefList.xhtml")
                || requestURL.contains("/meterreading/inspectionMeterBef.xhtml")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.INSPECTION_METER_BEF);
        }
        // 設定一括収集
        else if (requestURL.contains("/meterreading/settingCollection.xhtml")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.SETTING_BULK);
        }
        // 機器管理
        else if (requestURL.contains("/sms/collect/setting")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.NONE);
        }
        // データ表示 日報データ系
        else if (requestURL.contains("/sms/collect/dataview/daily.xhtml")
                || requestURL.contains("/sms/collect/dataview/monthly.xhtml")
                || requestURL.contains("/sms/collect/dataview/yearly.xhtml")
                || requestURL.contains("/sms/collect/dataview/compareGraphDaily.xhtml")
                || requestURL.contains("/sms/collect/dataview/compareGraphMonthly.xhtml")
                || requestURL.contains("/sms/collect/dataview/compareGraphYearly.xhtml")
                ) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.DAY_LOAD);
        }
        // データ表示 検針データ
        else if(requestURL.contains("/sms/collect/dataview/meterReadingData/meterReadingData.xhtml")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.INSP_DATA);
        }
        // SMSサーバー設定
        else if (requestURL.contains("/sms/server/")) {
            // 操作企業情報をログイン担当者情報に戻す
            setLoginOperationCorp(getLoginCorp());
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.SMS_SERVER_SETTING);
        }
        // 建物選択
        else if (requestURL.contains("/building/info/")) {
            // 操作企業情報をログイン担当者情報に戻す
            setLoginOperationCorp(getLoginCorp());
        }
        // データ収集装置
        else if (requestURL.contains("/sms/collect/")) {
            res = getCorpOparationAuth(SmsConstants.OPARATION_FUNCTION_TYPE.DATA_COLLECT_DEVICE);
        }
        // メニュー
        else if (requestURL.contains("/menu/")) {
            // 操作企業情報をログイン担当者情報に戻す
            setLoginOperationCorp(getLoginCorp());
        }

        if (res == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE
                || res == SmsConstants.OPARATION_FUNCTION_RESULT.DISABLE) {
            return false;
        }

        //建物選択前に収集装置画面に遷移する場合はエラーとする
        if (requestURL.contains("/sms/collect/")) {
            if (session.getAttribute(SmsConstants.BUILDING_SELECTED_ATTR) == null) {
                return false;
            }
        } else {
            //収集装置画面以外に遷移した場合にセッションから建物選択フラグを削除する
            if (session.getAttribute(SmsConstants.BUILDING_SELECTED_ATTR) != null) {
                session.removeAttribute(SmsConstants.BUILDING_SELECTED_ATTR);
            }
        }
        return resFunc;
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
    *
    * ログインユーザーの企業操作権限情報
    *
    * @return ログインユーザーの企業担当権限情報
    */
    @SuppressWarnings("unchecked")
    protected List<MCorpPersonAuth> getLoginCorpPersonAuthListAll() {
        if (session.getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_AUTH_LIST.getVal()) != null) {
            return (List<MCorpPersonAuth>) session
                    .getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_AUTH_LIST.getVal());
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
    *
    * ログインユーザーの企業担当権限情報
    *
    * @return ログインユーザーの企業担当権限情報
    */
    @SuppressWarnings("unchecked")
    protected List<MCorpPerson> getLoginCorpPersonListAll() {
        if (session.getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_LIST.getVal()) != null) {
            return (List<MCorpPerson>) session
                    .getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.CORP_PERSON_LIST.getVal());
        } else {
            List<MCorpPerson> mCorpPersonList = new ArrayList<>();
            mCorpPersonList.clear();
            return mCorpPersonList;
        }
    }

    /**
     * ログイン担当者情報
     *
     * @return
     */
    protected MPerson getLoginPerson() {
        return (MPerson) session.getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());
    }

    /**
     * ログインユーザーが現在操作している企業
     *
     * @return ログインユーザーが現在操作している企業
     */
    protected MCorp getLoginOperationCorp() {
        return (MCorp) session.getAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.OPERATION_CORP.getVal());
    }

    /**
     * ログインユーザーが現在操作している企業
     *
     * @param mCorp ログインユーザーが現在操作している企業
     */
    protected void setLoginOperationCorp(MCorp mCorp) {
        session.setAttribute(OsolConstants.LOGIN_USER_SESSION_KEY.OPERATION_CORP.getVal(), mCorp);
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

    public boolean getFunctionUse(OsolAccessBean.FUNCTION_CD functionCd) {

        AccessResultSet result = osolAccessBean.getAccessEnable(
                functionCd,
                "none",
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginOperationCorpId());

        return result.getOutput().isFunctionEnable();
    }

}
