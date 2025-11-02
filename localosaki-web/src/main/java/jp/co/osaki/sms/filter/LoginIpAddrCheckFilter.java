package jp.co.osaki.sms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import jp.co.osaki.osol.entity.MLoginIpAddr;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.dao.LoginIpAddrCheckFilterDao;

/**
 *
 * ログイン許可IPアドレスチェックフィルタ
 *
 * @author take_suzuki
 */
public class LoginIpAddrCheckFilter extends OsolConstants implements Filter {

    /**
     * チェックしないページリスト
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
    LoginIpAddrCheckFilterDao loginIpAddrCheckFilterDao;

    public LoginIpAddrCheckFilter() {
        this.noCheckPageList = new ArrayList<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

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
            HttpSession session = httpRequest.getSession(false);
            if (session == null) {
                //セッションがない
                chain.doFilter(request, response);
            } else if (session.getAttribute(SESSION_KEY_LOGIN) == null) {
                //未ログインの場合
                chain.doFilter(request, response);
            } else {
                //ログイン済の場合
                MPerson mPerson = loginIpAddrCheckFilterDao.getPerson(
                        ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getId().getCorpId(),
                        ((MPerson)session.getAttribute(LOGIN_USER_SESSION_KEY.PERSON.getVal())).getId().getPersonId());
                if (mPerson == null){
                    /* 担当者が見つからない */
                    ((HttpServletResponse)response).sendRedirect(httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage));
                } else {
                    String remoteAddr = httpRequest.getRemoteAddr();
                    String xForwardFor = httpRequest.getHeader("X-Forwarded-For");
                    if(xForwardFor != null){
                        String[] list = xForwardFor.split(",");
                        remoteAddr = list[list.length-1].trim();
                    }
                    String redirectUrl = null;
                    //ログイン許可IPアドレス件数チェック
                    List<MLoginIpAddr> mLoginIpAddrList = loginIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(),null,null);
                    if (mLoginIpAddrList.size() > 0) {
                        /* ログイン許可ステータスが 2：停止(不許可) のIPアドレス一覧取得*/
                        List<MLoginIpAddr> mLoginBlackIpAddrList = loginIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(), null, "2");
                        /* ログイン許可ステータスが 1：許可 のIPアドレス一覧取得*/
                        List<MLoginIpAddr> mLoginWhiteIpAddrList = loginIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(), null, "1");
                        /**
                         * ブラックリスト存在フラグ
                         */
                        boolean exsistBlackFlg = false;
                        /**
                         * ホワイトリスト存在フラグ
                         */
                        // 混在の状態
                        if (mLoginBlackIpAddrList.size() > 0 && mLoginWhiteIpAddrList.size() > 0) {
                            // ブラックリストのチェックを行う
                            if (checkBlackList(remoteAddr, mLoginBlackIpAddrList)) {
                                // 存在する場合
                                exsistBlackFlg = true;
                            }
                            // ホワイトリストのチェックを行う(ブラックリストに存在していたらチェックせずリダイレクト指定する)
                            if (!exsistBlackFlg && checkWhiteList(remoteAddr, mLoginWhiteIpAddrList)) {
                                // 存在する場合
                            } else {
                                // 存在しない場合
                                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                            }
                        }

                        // ブラックリストだけの状態
                        if (mLoginBlackIpAddrList.size() > 0 && mLoginWhiteIpAddrList.size() <= 0) {
                            // ブラックリストのチェックを行う
                            if (checkBlackList(remoteAddr, mLoginBlackIpAddrList)) {
                                // 存在する場合
                                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                                exsistBlackFlg = true;
                            }
                        }

                        // ホワイトリストだけの状態
                        if (mLoginBlackIpAddrList.size() <= 0 && mLoginWhiteIpAddrList.size() > 0) {
                            // ホワイトリストのチェックを行う(ブラックリストに存在していたらチェックしない)
                            if (!checkWhiteList(remoteAddr, mLoginWhiteIpAddrList)) {
                                // 存在しない場合
                                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                            }
                        }
//                        /* ログイン許可ステータスが 2：停止 のIPアドレス*/
//                        mLoginIpAddrList = loginIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(),remoteAddr,"2");
//                        if (mLoginIpAddrList.size() > 0){
//                            redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
//                        }
//                        mLoginIpAddrList = loginIpAddrCheckFilterDao.getLoginIpAddrList(mPerson.getId().getCorpId(),remoteAddr,"1");
//                        if (mLoginIpAddrList.size() <= 0){
//                            redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
//                        }
                    }
                    if (redirectUrl != null) {
                        if (httpRequest.getMethod().equals("POST") && httpRequest.getHeader("Faces-Request").equals("partial/ajax")) {
                            //Faces-Request	partial/ajax
                            //ajaxからのPOSTに対してLocationでリダイレクトしても、
                            //リダイレクトしたそのアドレスに対してajaxでpartialにGETしてくるのでサーバーがコンテンツを正常に返せない
                            //ajaxからの正常遷移時と同じ応答本文(jsf.jsの処理に合わせる)を偽装してそこにリダイレクトを仕込む
                            //　※こんな小細工をするのも本来filterでリダイレクトするのがよろしくなく
                            //　　beanでチェックactionのoutcomeで制御すべきところ → どう対応するのが良いかは追々検証
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
