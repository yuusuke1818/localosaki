package jp.skygroup.enl.webap.base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;

/**
 *
 * BaseBeanクラス
 * 
 * 画面BeanにBase機能を提供するクラス
 *
 * @author take_suzuki
 */
public abstract class BaseBean extends BaseSession {

    /**
     * イベントログ
     */
    protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    /**
     * エラー用ログ
     */
    protected static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     *
     * 初期表示処理
     *
     * @return 画面Beanページ
     */
    public abstract String init();

    /**
     * ログアウト処理
     *
     * @return ログインページ
     */
    protected String logout(){

        this.endSession();

        return null;
    }
    
    /**
     * アプリケーション名取得
     * 
     * @return アプリケーション名
     */
    public String getApplicationName() {

        return this.getWrapped().getInitParameter("APPLICATION_NAME");
    }

    /**
     * アプリケーションバージョン
     * 
     * @return アプリケーションバージョン
     */
    public String getApplicationVersion() {

        return this.getWrapped().getInitParameter("APPLICATION_VERSION");
    }
            
    /**
     * OEM名取得
     * 
     * @return OEM名
     */
    public String getOemName() {

        return this.getWrapped().getInitParameter("OEM_NAME");
    }

    /**
     * OEMバージョン
     * 
     * @return OEMバージョン
     */
    public String getOemVersion() {

        return this.getWrapped().getInitParameter("OEM_VERSION");
    }

    /**
     * ブラウザチェック有無
     * 
     * @return ブラウザチェック有無
     */
    public String getWebBrowserCheck() {

        return this.getWrapped().getInitParameter("WEB_BROWSER_CHECK");
    }   

    public String getProjectStage() {

        return FacesContext.getCurrentInstance().getApplication().getProjectStage().name();
    }   
    
    /**
     * 画面表示のためのメッセージ取得
     *
     * @return メッセージリスト
     */
    public List<FacesMessage> getMessageList() {

        return FacesContext.getCurrentInstance().getMessageList();

    }

    /**
     * 画面表示のための情報メッセージを追加
     *
     * @param message
     */
    protected void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    /**
     * 画面表示のための情報メッセージを追加
     *
     * @param message
     * @param detailMessage
     */
    protected void addMessage(String message, String detailMessage) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, detailMessage));
    }

    /**
     * 画面表示のための警告メッセージを追加
     *
     * @param message
     */
    protected void addWarningMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    /**
     * 画面表示のための警告メッセージを追加
     *
     * @param message
     * @param detailMessage
     */
    protected void addWarningMessage(String message, String detailMessage) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, detailMessage));
    }

    /**
     * 画面表示のためのエラーメッセージを追加
     *
     * @param message
     */
    protected void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    /**
     * 画面表示のためのエラーメッセージを追加
     *
     * @param message
     * @param detailMessage
     */
    protected void addErrorMessage(String message, String detailMessage) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detailMessage));
    }

    /**
     * 
     *  クッキー読み込み
     * 
     * @param key
     * @return
     */
    protected String getCookie(String key) {

        HttpServletRequest request = (HttpServletRequest) this.getWrapped().getRequest();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    /**
     * 
     *  クッキー書き込み
     * 
     * @param key
     * @param value
     */
    protected void addCookie(String key, String value) {

        HttpServletResponse response = (HttpServletResponse) this.getWrapped().getResponse();
        response.addCookie(new Cookie(key, value));
    }

    /**
    * IDによるコンポーネント検索
    * @param base rootとなるコンポ―ネント(FacesContext.getCurrentInstance().getViewRoot())
    * @param id
    * @return nullか見つかったコンポーネント
    */  
    public static UIComponent findComponent(UIComponent base, final String id) {
        if (id.equals(base.getId()))
            return base;
 
        UIComponent child;
        UIComponent result = null;
        Iterator<?> childrens = base.getFacetsAndChildren();
        while (childrens.hasNext() && (result == null))
        {
            child = (UIComponent) childrens.next();
            if (id.equals(child.getId()))
            {     
                result = child;
                break;
            }
            result = findComponent(child, id);
            if (result != null)
            {
                break;
            }
        }
        return result;
    }
    
    /**
     * メソッド取得<br>
     * EL式を変換する
     *
     * @param el
     * @param returnClass
     * @param parameter
     * @return
     */
    protected MethodExpression createMethodExpression(String el, Class<?> returnClass, Class<?>[] parameter) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ELContext elCtx = currentInstance.getELContext();
        ExpressionFactory expFact = currentInstance.getApplication().getExpressionFactory();
        return expFact.createMethodExpression(elCtx, el, returnClass, parameter);
    }
    
    /**
     * イメージファイル出力
     * 
     * @param outputStream
     * @param filePath 
     */
    public void imageOutput(OutputStream outputStream, Object filePath) {
        
        if (filePath instanceof String) {
            try {
                File mediaFile = new File(filePath.toString());
                if (mediaFile.exists() && mediaFile.isFile() && mediaFile.canRead()){
                    int lastDotPosition = mediaFile.getName().lastIndexOf(".");
                    String extension = null;
                    if (lastDotPosition != -1) {
                        extension = mediaFile.getName().substring(lastDotPosition + 1);
                    }
                    if (extension != null){
                        BufferedImage bufferedImage = ImageIO.read(mediaFile);
                        ImageIO.write(bufferedImage,extension,outputStream);
                        outputStream.close();
                    }
                }
            } catch (IOException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }
    }
}
