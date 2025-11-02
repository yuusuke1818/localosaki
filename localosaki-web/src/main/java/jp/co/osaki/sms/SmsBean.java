package jp.co.osaki.sms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiLoginParameter;
import jp.co.osaki.osol.api.OsolApiResponse;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.entity.MUrlLink;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.dao.MUrlLinkDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * OSOLの画面Bean基底クラス
 *
 * @author take_suzuki
 */
public abstract class SmsBean extends SmsLoginUserInfo {

    @Inject
    private Event<SmsConversationEvent> event;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private SmsMessages messageObj;

    @Inject
    private HttpServletRequest request;

    @EJB
    MUrlLinkDao mUrlLinkDao;

    /**
     * アクセスログ
     */
    private static final Logger accessLogger = Logger.getLogger(LOGGER_NAME.ACCESS.getVal());

    @Override
    protected void clearSession() {

        //clearSessionでConversationの紐付けを失う前に　全OsolConversationBeanの会話を終了させる
        SmsConversationEvent param = new SmsConversationEvent();
        event.fire(param);

        super.clearSession();
    }

    @Override
    protected String logout() {

        // セッションクリア
        clearSession();

        super.logout();

        //ログインページへ遷移
        return this.getWrapped().getInitParameter("LOGIN_PAGE");

    }
    //エクセルテンプレートディレクトリ
    protected String getExcelTemplateDir(){

        return this.getWrapped().getInitParameter("TEMPLATE_DIR");

    }
    //エクセル出力先ディレクトリ
    protected String getExcelOutputDir(){

        return this.getWrapped().getInitParameter("EXCEL_OUTPUT_DIR");

    }

    /**
     * CSV出力先ディレクトリパスを取得.
     *
     * @return PDFテンプレートファイルディレクトリパス
     */
    protected String getCsvOutputDir() {
        return getWrapped().getInitParameter("CSV_OUTPUT_DIR");
    }

    /**
     * PDFテンプレートファイルディレクトリパスを取得.
     *
     * @return PDFテンプレートファイルディレクトリパス
     */
    protected String getPdfTemplateDir() {
        return getWrapped().getInitParameter("PDF_TEMPLATE_DIR");
    }

    /**
     * PDF出力先ディレクトリパスを取得.
     *
     * @return PDFテンプレートファイルディレクトリパス
     */
    protected String getPdfOutputDir() {
        return getWrapped().getInitParameter("PDF_OUTPUT_DIR");
    }

    /**
     * バリデーションエラー時の上書きスタイルを返す
     * @param id style="#{xxxxBean.getInvalidStyle(component.clientId)}"
     * @return スタイル
     */
    public String getInvalidStyle(String id){
        UIComponent ui = FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
        if(ui instanceof UIInput){
            UIInput inp = (UIInput)ui;

            boolean error = false;
            List<FacesMessage> list = getMessageList();
            for(FacesMessage msg : list){
                if( msg.getSeverity() == FacesMessage.SEVERITY_ERROR ){
                    error = true;
                }
            }
            if(error){      //エラーメッセージ表示中
                if(!inp.isValid()){
                    return OsolConstants.INVALID_STYLE;
                }else if(inp.isRequired() && inp.getValue() == null ){
                    return OsolConstants.INVALID_STYLE;
                }
            }
        }
        return "";
    }

    /**
     * 画像データをBase64形式で取得
     * @param filePath
     * @return Base64データ
     */
    public String getConvertImageDataBase64(String filePath) {

        try {
            File file = new File("/", filePath);
            String contentType = Files.probeContentType(file.toPath());
            byte[] data = Files.readAllBytes(file.toPath());

            String base64str = Base64.getEncoder().encodeToString(data);

            StringBuilder sb = new StringBuilder();
            sb.append("data:");
            sb.append(contentType);
            sb.append(";base64,");
            sb.append(base64str);
            return sb.toString();
        } catch (IOException | OutOfMemoryError | SecurityException | NullPointerException e) {
            eventLogger.debug(this.getClass().getName().concat(OsolConstants.STR_HALF_SPACE).concat(this.getClass().getName()).concat(":getConvertImageBase64Data:")
                    .concat("errorCatch -> ").concat(e.getMessage()));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return OsolConstants.STR_EMPTY;
        }
    }

    /**
     * API呼出(POST)
     *
     * @param parameter APIパラメータのインスタンス
     * @param response APIレスポンスのクラスオブジェクト
     * @return APIレスポンスのインスタンス
     */
    protected <P extends OsolApiLoginParameter, R extends OsolApiResponse<?>> R callApiPost(P parameter,
            Class<R> response) {

        return callApiPost(osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON, parameter, response);
    }

    /**
     * API呼出(POST)
     *
     * @param apiServer APIサーバーのURL
     * @param path APIのパス
     * @param parameter APIパラメータのインスタンス
     * @param response APIレスポンスのクラスオブジェクト
     * @return APIレスポンスのインスタンス
     */
    @SuppressWarnings("unchecked")
    protected <P extends OsolApiLoginParameter, R extends OsolApiResponse<?>> R callApiPost(String apiServer,
            SmsApiGateway.PATH path, P parameter, Class<R> response) {

        SmsApiGateway gateway = new SmsApiGateway();
        return (R) gateway.osolApiPost(apiServer, path, parameter, response);
    }

    /**
     * API処理結果コードに基づきメッセージ追加
     *
     * @param resultCode API処理結果コード
     */
    protected void addMessageByResultCode(String resultCode) {
        String resultMessage = messageObj.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(resultCode));
        if (OsolApiResultCode.API_OK.equals(resultCode)) {
            // 正常終了時
            addMessage(resultMessage);
        } else {
            addErrorMessage(resultMessage);
        }
    }

    //外部へのリンクURLと文言をDBから取得する
    public MUrlLink getUrlLink(String urlLinkCode){
        MUrlLink resultUl = new MUrlLink();
        MUrlLink ul = mUrlLinkDao.find(urlLinkCode);
        if(ul != null){
            resultUl = ul;
        }else{
            resultUl.setUrl("");
            resultUl.setTitle("");
        }
        return resultUl;
    }

    //外部へのリンクURLをDBから取得する
    public String getLinkUrl(String urlLinkCode){
        MUrlLink ul = getUrlLink(urlLinkCode);
        return ul.getUrl();
    }

    //外部へのリンク文言をDBから取得する
    public String getLinkTitle(String urlLinkCode){
        MUrlLink ul = getUrlLink(urlLinkCode);
        return ul.getTitle();
    }

    /**
     * アクセスログ出力
     * @param methodName メソッド名
     * @param actionMessage 操作文面
     */
    public void exportAccessLog(String methodName, String actionMessage) {

        // リクエストから画面情報を取得
        String uri = request.getRequestURI();

        // クラス名取得
        Class<?> childClass = this.getClass();
        String className = childClass.getName();
        // クラス名がプロキシ名の場合
        if (childClass.getName().contains("$$")) {
            className = childClass.getSuperclass().getName();
        }

        // ログ出力の文字列を取得
        StringBuilder accessLogMessage = new StringBuilder();
        accessLogMessage.append("CorpId:");
        accessLogMessage.append(getLoginCorpId());
        accessLogMessage.append(" PersonId:");
        accessLogMessage.append(getLoginPersonId());
        accessLogMessage.append(" 画面:");
        accessLogMessage.append(uri);
        if (!CheckUtility.isNullOrEmpty(actionMessage)) {
            accessLogMessage.append(" 操作:");
            accessLogMessage.append(actionMessage);
        }
        accessLogMessage.append(" Class:");
        accessLogMessage.append(className);
        accessLogMessage.append(" Method:");
        accessLogMessage.append(methodName);
        // ログ出力
        accessLogger.info(accessLogMessage.toString());
    }

}
