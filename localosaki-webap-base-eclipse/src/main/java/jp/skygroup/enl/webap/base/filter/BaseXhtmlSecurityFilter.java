package jp.skygroup.enl.webap.base.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * XHTMLセキュリティフィルタ
 * 
 * @author take_suzuki
 */
public class BaseXhtmlSecurityFilter extends BaseConstants implements Filter {
    
    /**
     * FacesServletUrlPattern
     */
    private String facesServletUrlPattern = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        if (httpRequest.getRequestURI().startsWith(httpRequest.getContextPath().concat(this.facesServletUrlPattern)) || 
                httpRequest.getRequestURI().equals(httpRequest.getContextPath().concat("/"))) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.facesServletUrlPattern = filterConfig.getInitParameter("facesServletUrlPattern");
    }

    @Override
    public void destroy() {
        this.facesServletUrlPattern = null;
    }
    
}