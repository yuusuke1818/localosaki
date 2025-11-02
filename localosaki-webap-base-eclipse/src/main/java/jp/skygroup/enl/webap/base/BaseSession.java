package jp.skygroup.enl.webap.base;

import javax.servlet.http.HttpSession;

/**
 *
 * セッション情報管理クラス
 * 
 * セッションへのアクセス機能を提供する
 * 
 * @author take_suzuki
 */
public abstract class BaseSession extends BaseConstants {
    
    /**
     * セッション情報に値を登録する
     *
     * @param key
     * @param value
     */
    protected void setSessionParameter(String key, Object value) {

        if (this.getWrapped() != null) {
            HttpSession session = (HttpSession) this.getWrapped().getSession(false);
            if (session != null){
                this.getWrapped().getSessionMap().put(key, value);
            }
        }
    }

    /**
     * セッション情報から値を取得する
     *
     * @param key
     * @return
     */
    protected Object getSessionParameter(String key) {

        if (this.getWrapped() != null) {
            HttpSession session = (HttpSession) this.getWrapped().getSession(false);
            if (session != null){
                return this.getWrapped().getSessionMap().get(key);
            } else {
                return STR_EMPTY;
            }
        } else {
            return STR_EMPTY;
        }
    }

    /**
     *  セッション情報作成
     */
    protected void createSession() {

        //セッション破棄
        HttpSession session = (HttpSession) this.getWrapped().getSession(false);
        if (session != null){
            session.invalidate();
        }
        //セッション生成
        this.getWrapped().getSession(true);
        this.getWrapped().getSessionMap().put(SESSION_KEY_LOGIN, SESSION_VAL_OK);
    }

    /**
     *  セッション破棄
     */
    protected void endSession() {

        HttpSession session = (HttpSession) this.getWrapped().getSession(false);
        if (session != null){
            session.invalidate();
        }
    }

    /**
     *  セッション情報消去
     */
    protected void clearSession() {

        HttpSession session = (HttpSession) this.getWrapped().getSession(false);
        if (session != null){
            this.getWrapped().getSessionMap().clear();
        }
    }
}
