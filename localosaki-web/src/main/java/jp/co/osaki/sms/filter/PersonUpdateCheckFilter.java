package jp.co.osaki.sms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
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
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.dao.MPersonDao;

/**
 *
 * 担当者の最終ログイン日時・権限最終更新日時チェックフィルター
 *
 * @author take_suzuki
 */
public class PersonUpdateCheckFilter extends OsolConstants implements Filter {

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

    @EJB
    MPersonDao mPersonDao;

    public PersonUpdateCheckFilter() {
        this.noCheckPageList = new ArrayList<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        boolean chkPage = false;
        //チェック対象外ページの判定
        for (String page : this.noCheckPageList) {
            if (httpRequest.getRequestURI().contains(page)){
                chkPage = true;
                break;
            }
        }
        if (chkPage){
            chain.doFilter(request, response);
        } else {
            HttpSession session = httpRequest.getSession(false);
            if (session == null){
                //セッションがない
                chain.doFilter(request, response);
            } else if (session.getAttribute(SESSION_KEY_LOGIN) == null) {
                //未ログインの場合
                chain.doFilter(request, response);
            } else {
                //ログイン済の場合
                MPerson mPerson = mPersonDao.find(
                    ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getId().getCorpId(),
                    ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getId().getPersonId());
                String redirectUrl = null;
                if (mPerson == null){
                    /* 担当者が見つからない */
                    redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                } else {
                    String mPersonLastLoginDate = null;
                    if(mPerson.getLastLoginDate() != null){
                        mPersonLastLoginDate = mPerson.getLastLoginDate().toString();
                    }
                    String sPersonLastLoginDate = ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getLastLoginDate().toString();
                    String mPersonAuthLastUpdateDate = mPerson.getAuthLastUpdateDate().toString();
                    String sPersonAuthLastUpdateDate = ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getAuthLastUpdateDate().toString();

                    if (!Objects.equals(mPersonLastLoginDate,sPersonLastLoginDate)) {
                        /* 担当者の最終ログイン日時が更新されている */
                        redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                    } else if (!mPersonAuthLastUpdateDate.equals(sPersonAuthLastUpdateDate)){
                        /* 担当者の権限最終更新日時が更新されている */
                        redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                    } else if (Objects.equals(mPerson.getAccountStopFlg(), FLG_ON)
                            || Objects.equals(mPerson.getDelFlg(), FLG_ON)
                            ){
                        /* アカウントを停止・削除にされた */
                        redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                    } else if (mPerson.getPassMissCount() >= LOGIN_PASS_MISS_LOCK_COUNT ){
                        /* アカウントをロックされた */
                        redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                    }
                }
                if(redirectUrl != null){
                    if( Objects.equals(httpRequest.getMethod(), "POST")
                            && Objects.equals(httpRequest.getHeader("Faces-Request"), "partial/ajax" )
                            ){
                        //Faces-Request	partial/ajax
                        //ajaxからのPOSTに対してLocationでリダイレクトしても、
                        //リダイレクトしたそのアドレスに対してajaxでpartialにGETしてくるのでサーバーがコンテンツを正常に返せない
                        //ajaxからの正常遷移時と同じ応答本文(jsf.jsの処理に合わせる)を偽装してそこにリダイレクトを仕込む
                        httpResponse.setContentType("text/xml;charset=UTF-8");
                        httpResponse.setHeader("Transfer-Encoding","chunked");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("<?xml version='1.0' encoding='UTF-8'?>");
                            out.println("<partial-response id=\"j_id1\"><redirect url=\""+redirectUrl+"\"></redirect></partial-response>");
                        }
                    }else{
                        //通常のcommandButtonから
                        httpResponse.sendRedirect(redirectUrl);
                    }
                }else{
                    chain.doFilter(request, response);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String noCheckPage = filterConfig.getInitParameter("noCheckPageList");
        this.noCheckPageList.clear();
        if (noCheckPage != null){
            if (noCheckPage.length() > 0){
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
}
