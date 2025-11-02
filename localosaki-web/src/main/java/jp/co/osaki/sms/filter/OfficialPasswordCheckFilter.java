/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * 本パスワードチェックフィルター
 *
 * @author n-takada
 */
public class OfficialPasswordCheckFilter extends OsolConstants implements Filter {

    /**
     * 本パスワードチェックしないページリスト
     */
    private final List<String> noCheckPageList;

    /**
     * 本パスワードチェックエラー時のリダイレクト先ページ
     */
    private String redirectPage = null;

    /**
     * FacesServletUrlPattern
     */
    private String facesServletUrlPattern = null;

    public OfficialPasswordCheckFilter() {
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
            String redirectUrl = null;
            if (session == null) {
                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
            } else if (session.getAttribute(LOGIN_USER_SESSION_KEY.SET_PASSWORD.getVal()) == null) {
                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
            }

            if (redirectUrl != null) {
                httpResponse.sendRedirect(redirectUrl);

            } else {
                chain.doFilter(request, response);
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
}
