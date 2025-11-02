package jp.skygroup.enl.webap.base;

import java.io.IOException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * リクエストトークンタグ発行
 *
 * リクエストトークンタグ出力を行う
 * BaseRequestTokenFilterが必須となる。
 *
 * @author d-komatsubara
 */
@FacesComponent(createTag = true, tagName = "requestToken", namespace = "http://osaki.co.jp/base-component")
public class BaseRequestTokenComponent extends UIComponentBase {

    @Override
    public String getFamily() {
        return this.getClass().getPackage().getName();
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
    }

    /**
     *
     * リクエストトークンタグ発行
     *
     * @param context
     * @throws IOException
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        // 前処理
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);

        //セッションからトークンを取得
        String token = (String) session.getAttribute(BaseConstants.REQUEST_TOKEN.SESSION_KEY.getVal());
        if (null == token) {
            token = "";
            // ログイン時のためだけ対応
            if (request.getRequestURI().contains(context.getExternalContext().getInitParameter("LOGIN_PAGE"))) {
                token = BaseUtility.createRequestToken();
                session.setAttribute(BaseConstants.REQUEST_TOKEN.SESSION_KEY.getVal(), token);
            }
        }
        ResponseWriter rw = context.getResponseWriter();
        rw.write(BaseConstants.REQUEST_TOKEN.START_TAG.getVal().concat(token).concat(BaseConstants.REQUEST_TOKEN.END_TAG.getVal()));
    }
}
