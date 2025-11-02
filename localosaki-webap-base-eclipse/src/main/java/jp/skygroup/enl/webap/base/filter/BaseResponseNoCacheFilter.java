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
 * レスポンスヘッダにキャッシュ無効設化定する。
 */
public class BaseResponseNoCacheFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {

        String contextType = response.getContentType();
        if (contextType == null || (!contextType.startsWith("image/"))) {
            //レスポンスヘッダの設定
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Cache-Control", "private,no-store,no-cache,must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "-1");    //https://support.microsoft.com/ja-jp/kb/234067
        }
        
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }

}
