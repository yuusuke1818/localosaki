package jp.skygroup.enl.webap.base.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author take_suzuki
 * 
 * レスポンスヘッダにクリッジャキング攻撃対策をする。
 */
public class BaseXFrameOptionsFilter implements Filter {

    /**
     * X-Frame-Options Value
     */
    private String XFrameOptions = null;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.XFrameOptions  = filterConfig.getInitParameter("X-Frame-Options");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {

        //レスポンスヘッダの設定
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Frame-Options", this.XFrameOptions);
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.XFrameOptions = null;
    }

}
