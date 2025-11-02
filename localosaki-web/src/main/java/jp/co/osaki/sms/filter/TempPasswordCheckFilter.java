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
 * 仮パスワードチェックフィルター
 *
 * @author take_suzuki
 */
public class TempPasswordCheckFilter extends OsolConstants implements Filter {

    /**
     * 仮パスワードチェック対象ページリスト
     */
    private final List<String> checkPageList;

    /**
     * 仮パスワードチェックエラー時のリダイレクト先ページ
     */
    private String redirectPage = null;

    /**
     * FacesServletUrlPattern
     */
    private String facesServletUrlPattern = null;

    public TempPasswordCheckFilter() {
        this.checkPageList = new ArrayList<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean chkPage = false;
        //チェック対象ページの判定
        for (String page : this.checkPageList) {
            if (httpRequest.getRequestURI().contains(page)) {
                chkPage = true;
                break;
            }
        }
        if (chkPage) {
            HttpSession session = httpRequest.getSession(false);
            String redirectUrl = null;
            if (session == null) {
                redirectUrl = httpRequest.getContextPath().concat(this.facesServletUrlPattern).concat(this.redirectPage);
            }
            if (redirectUrl != null) {
                httpResponse.sendRedirect(redirectUrl);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String noCheckPage = filterConfig.getInitParameter("checkPageList");
        this.checkPageList.clear();
        if (noCheckPage != null) {
            if (noCheckPage.length() > 0) {
                this.checkPageList.addAll(Arrays.asList(noCheckPage.split(",")));
            }
        }
        this.redirectPage = filterConfig.getInitParameter("redirectPage");
        this.facesServletUrlPattern = filterConfig.getInitParameter("facesServletUrlPattern");

    }

    @Override
    public void destroy() {

        this.checkPageList.clear();
        this.redirectPage = null;
        this.facesServletUrlPattern = null;

    }
}
