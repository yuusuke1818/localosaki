/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
 *
 * @author a.ogawa
 */
public class SessionCheckFilter extends OsolConstants implements Filter {

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

    public SessionCheckFilter() {
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
            String referer = httpRequest.getHeader("Referer");
            String redirectUrl = null;

            if (referer!=null) {
                if (session == null) {
                    redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                } else if (session.getAttribute(SESSION_KEY_LOGIN) == null) {
                    redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
                }
            }

            if (redirectUrl != null) {
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
