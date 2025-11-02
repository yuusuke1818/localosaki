package jp.skygroup.enl.webap.base.filter;

import java.io.IOException;
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
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseUtility;
import org.jboss.logging.Logger;

/**
 * 
 * リクエストトークンフィルター
 *
 * @author d-komatsubara
 */
public class BaseRequestTokenFilter extends BaseConstants implements Filter {

    /**
     * イベント用ログ
     */
    private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean notErrorPageFlg = true;
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 前処理でリクエストパラメータのトークンとセッションに保存されたトークンを比較する
        HttpSession session = req.getSession(false);
        if (session != null){
            // トークン(request/session)と結果を取得
            String req_token = req.getParameter(REQUEST_TOKEN.SESSION_KEY.getVal());
            String ses_token = session.getAttribute(REQUEST_TOKEN.SESSION_KEY.getVal()).toString();
            String check_str = session.getAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal()).toString();

            // POSTかGETか
            if (req.getMethod().equals("POST")) {
                // POSTの場合 トークンをチェックするのは保護対象前のページ
                if (isBeforePage(req.getRequestURI()) && Objects.equals(req_token, ses_token)) {
                    // トークンが同じで且つチェック対象前の場合
                    session.setAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal(), REQUEST_TOKEN.SESSION_KEY_CHECK_OK.getVal());
                } else {
                    // 保護対象前のページではない場合
                    session.setAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal(), REQUEST_TOKEN.SESSION_KEY_CHECK_NG.getVal());
                }

            } else {
                // それ以外の場合
                eventLogger.debug(req.getMethod());

                // チェック結果を対象ページの時にチェックする
                if (isProtectedPage(req.getRequestURI())) {
                    if (check_str != null && !REQUEST_TOKEN.SESSION_KEY_CHECK_OK.getVal().equals(check_str)) {
                        // トークンが合わない場合
                        eventLogger.debug("------------------------------------------------------");
                        eventLogger.debug("URL直うち");
                        eventLogger.debug("------------------------------------------------------");
                        // エラーページへ
                        notErrorPageFlg = false;
                    } else {
                        // 一致している場合チェック結果をNGに設定し通常遷移処理へ
                        session.setAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal(), REQUEST_TOKEN.SESSION_KEY_CHECK_NG.getVal());
                    }
                } else {
                    eventLogger.debug("check対象外");
                    // チェック対象に遷移しない場合NGに設定し通常遷移処理へ
                    session.setAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal(), REQUEST_TOKEN.SESSION_KEY_CHECK_NG.getVal());
                }

                // 新たなトークンを生成(アクセストークンの発行クラスを実装)
                String now = BaseUtility.createRequestToken();

                // セッションに登録(このセッション名はHiddenTokenでも設定)
                session.setAttribute(REQUEST_TOKEN.SESSION_KEY.getVal(), now);
            }

            // 別ウィンドウの場合はチェック結果を保持する
            if (isSubWindowPage(req.getRequestURI())) {
                eventLogger.debug("別ウィンドウ");
                notErrorPageFlg = true;
                session.setAttribute(REQUEST_TOKEN.SESSION_KEY_CHECK.getVal(), REQUEST_TOKEN.SESSION_KEY_CHECK_OK.getVal());
            }

        }
        
        // エラーの場合、不正遷移画面へ
        if (notErrorPageFlg) {
            chain.doFilter(request, response);
        } else {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "不正な遷移が行われました。");
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * URIの一部が保護対象前のページに一致するかチェック
     *
     * @param target 対象URI
     * @return TRUE:存在する,FALSE:存在しない
     */
    private boolean isBeforePage(String target) {
        // 保護対象前のページ郡
        // URLに、次に挙げる文字列を含んでいる場合、POSTに乗ってくるtokenとセッションで持っている最後に発行したtokenが
        // 一致していれば、次のGETを許容するフラグを立てる
        // ただし、画面遷移を伴わない場合は、次のGETが来なくてフラグが立ったままとなり、URL直打ちが許容されてしまう為
        // actionにtokenが乗らないように実装する必要がある
        String[] beforeProtectedView = {"list.xhtml", "index.xhtml", "search.xhtml", "register.xhtml"};
        for (String str : beforeProtectedView) {
            if (target.contains(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * URIの一部が保護対象のページに一致するかチェック
     *
     * @param target 対象URI
     * @return TRUE:存在する,FALSE:存在しない
     */
    private boolean isProtectedPage(String target) {
        // 保護対象ページ郡
        // GETで要求するURLに、次に挙げる文字列を含んでいる場合は、
        // POST時に行ったtokenチェックによってGETを許可するフラグが立っていなければエラー画面に遷移する
        // 対象外のページはフラグが立っていなくても遷移が可能
        String[] protectedView = {"change", "edit", "register", "del", "conservation", "add", "repeal", "usage", "building.xhtml", "aggregate.xhtml", "categry.xhtml"};
        for (String str : protectedView) {
            if (target.contains(str)) {
                return true;
            }
        }

        return false;
    }

    /**
     * URIがサ別ウィンドウページに一致するかチェック
     *
     * @param target 対象URI
     * @return TRUE:存在する,FALSE:存在しない
     */
    private boolean isSubWindowPage(String target) {
        // サブウィンドウ対象ページ
        // GETで要求するURLに、次に挙げる文字列を含んでいる場合は、問答無用で遷移許容される。
        // つまり直打ちが可能。なので単独で何らかの操作が不可能であることが前提。本当に指定が必要なのかも要確認
        String[] subWindowView = {"editEnergy.xhtml"};
        for (String str : subWindowView) {
            if (target.contains(str)) {
                return true;
            }
        }

        return false;
    }
}
