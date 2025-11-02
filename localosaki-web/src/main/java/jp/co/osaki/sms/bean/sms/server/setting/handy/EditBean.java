package jp.co.osaki.sms.bean.sms.server.setting.handy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.BooleanUtils;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.GetSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.UpdateSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.GetSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.UpdateSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.GetSmsHandyDeviceResult;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.UpdateSmsHandyDeviceResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;;

/**
 * ハンディ端末 登録・編集画面
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingHandyEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -2110875883574453987L;

    //当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private EditBeanProperty editBeanProperty;

    // ハンディ端末 検索画面
    @Inject
    private SearchBean searchBean;

    // エラーメッセージ
    private List<String> invalidComponent;

    @Override
    public String init() {
        conversationStart();

        return null;
    }

    public String init(EditBeanProperty editBeanProperty) {
        eventLogger.debug(packageName.concat("smsServerSettingHandyEditBean():START"));

        // 初期処理
        init();

        this.editBeanProperty = editBeanProperty;

        if (this.editBeanProperty.getUpdateProcessFlg()) {
            // 編集
            // ハンディ端末情報 取得
            GetSmsHandyDeviceResult result = this.getHandyDevice(this.editBeanProperty.getDevId());
            if (Objects.isNull(result)) {
                return null;
            }
            covertDisplay(result);
        }

        eventLogger.debug(packageName.concat("smsServerSettingHandyEditBean():ENDT"));

        return "handyEdit";
    }

    /**
     * 画面表示用に置き換え
     *
     * @param result
     */
    private void covertDisplay(GetSmsHandyDeviceResult result) {
        editBeanProperty.setDevId(result.getDevId());
        editBeanProperty.setDevPw(result.getDevPw());
        editBeanProperty.setName(result.getName());
        editBeanProperty.setDelFlg(BooleanUtils.toBoolean(result.getDelFlg()));
        editBeanProperty.setVersion(result.getVersion());
    }

    /**
     * ハンディ端末情報 登録・更新
     *
     * @return
     * @throws Exception
     */
    @Logged
    public String regist() throws Exception {

        // バリデーションチェック
        if (!validateInputParam()) {
            return STR_EMPTY;
        }

        // 登録・更新処理
        updateSmsHandyDevice();

        // APIエラーチェック
        if (hasError()) {
            return STR_EMPTY;
        }

        return searchBean.search();
    }

    /**
     * バリデーションチェック
     *
     * @return true:OK false:NG
     */
    private boolean validateInputParam() {
        boolean errorFlg = false;
        invalidComponent = new ArrayList<>();
        List<String> validateErrorMessageList = new ArrayList<>();

        // 装置ID
        if (!editBeanProperty.getUpdateProcessFlg()) {
            if (CheckUtility.isNullOrEmpty(editBeanProperty.getDevId())) {
                // NULLチェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[inputDevId]"));
                validateErrorMessageList
                        .add(beanMessages.getMessage("smsServerSettingHandyEditBean.error.notInputDevId"));
                invalidComponent.add("smsServerSettingHandyEditBean:inputDevId");
                errorFlg = true;
            } else if (!checkHandyDevId(editBeanProperty.getDevId())) {
                // 文字数、書式チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[inputDevId]"));
                validateErrorMessageList
                        .add(beanMessages.getMessage("smsServerSettingHandyEditBean.error.notFormatDevId"));
                invalidComponent.add("smsServerSettingHandyEditBean:inputDevId");
                errorFlg = true;
            } else {
                // 重複チェック
                GetSmsHandyDeviceResult result = this.getHandyDevice(editBeanProperty.getDevId());
                // APIエラーの際はバリデーションエラーを表示しない
                if (hasError()) {
                    invalidComponent.clear();
                    errorFlg = true;
                    return !errorFlg;
                }

                if (Objects.nonNull(result)) {
                    eventLogger.debug(
                            EditBean.class.getPackage().getName().concat(":: validateInputParam() error[inputDevId]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessageFormat("smsServerSettingHandyEditBean.error.duplicationDevId",
                                    editBeanProperty.getDevId()));
                    invalidComponent.add("smsServerSettingHandyEditBean:inputDevId");
                    errorFlg = true;
                }
            }
        }

        //装置パスワード
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getDevPw())) {
            // NULLチェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devPw]"));
            validateErrorMessageList.add(beanMessages.getMessage("smsServerSettingHandyEditBean.error.notInputDevPw"));
            invalidComponent.add("smsServerSettingHandyEditBean:devPw");
            errorFlg = true;
        } else if (!checkDevPw(editBeanProperty.getDevPw())) {
            // 文字数、書式チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devPw]"));
            validateErrorMessageList.add(beanMessages.getMessage("smsServerSettingHandyEditBean.error.notFormatDevPw"));
            invalidComponent.add("smsServerSettingHandyEditBean:devPw");
            errorFlg = true;
        }

        // 装置名称
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getName())
                && editBeanProperty.getName().getBytes().length >= 40) {
            // 文字数チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devName]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingHandyEditBean.error.lengthOverDevName"));
            invalidComponent.add("smsServerSettingHandyEditBean:devName");
            errorFlg = true;
        }

        // APIエラーがなければバリデーションエラーを追加する
        for (String msg : validateErrorMessageList) {
            addErrorMessage(msg);
        }

        return !errorFlg;
    }

    /**
     * ハンディ端末 装置IDのチェック
     *
     * @param devId
     * @return true:OK false:NG
     */
    private boolean checkHandyDevId(String devId) {
        String regrexStr = "^" + SmsConstants.DEVICE_KIND.HANDY.getVal() + "[A-Za-z0-9]{0,6}$";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(devId);
        return m.matches();
    }

    /**
     * 装置パスワードのチェック
     *
     * @param devPw
     * @return true:OK false:NG
     */
    private boolean checkDevPw(String devPw) {
        String regrexStr = "^[A-Za-z0-9]{1,12}$";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(devPw);
        return m.matches();
    }

    /**
     * ハンディ端末情報 取得API
     *
     * @param devId
     * @return
     */
    private GetSmsHandyDeviceResult getHandyDevice(String devId) {
        // 装置取得
        GetSmsHandyDeviceResponse response = new GetSmsHandyDeviceResponse();
        GetSmsHandyDeviceParameter parameter = new GetSmsHandyDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("GetSmsHandyDeviceBean");
        parameter.setDevId(devId);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (GetSmsHandyDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        GetSmsHandyDeviceResult result = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            result = response.getResult();
        }

        return result;
    }

    /**
     * ハンディ端末情報 登録・更新API
     *
     * @return
     */
    private UpdateSmsHandyDeviceResult updateSmsHandyDevice() {
        // 装置取得
        UpdateSmsHandyDeviceResponse response = new UpdateSmsHandyDeviceResponse();
        UpdateSmsHandyDeviceParameter parameter = new UpdateSmsHandyDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("UpdateSmsHandyDeviceBean");
        parameter.setDevId(editBeanProperty.getDevId());
        parameter.setDevPw(editBeanProperty.getDevPw());
        parameter.setName(editBeanProperty.getName());
        parameter.setDelFlg(BooleanUtils.toInteger(editBeanProperty.getDelFlg(), OsolConstants.FLG_ON,
                OsolConstants.FLG_OFF, OsolConstants.FLG_OFF));
        parameter.setVersion(editBeanProperty.getVersion());

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (UpdateSmsHandyDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        UpdateSmsHandyDeviceResult result = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            result = response.getResult();
        }

        return result;
    }

    /**
     * エラーの有無チェック
     * @return
     */
    public boolean hasError() {
        List<FacesMessage> messageList = getMessageList();
        if (messageList == null || messageList.isEmpty()) {
            return false;
        }
        for (FacesMessage message : messageList) {
            if (message.getSeverity().equals(FacesMessage.SEVERITY_FATAL)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_ERROR)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_WARN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * デザイン指定
     */
    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    /**
     * 確認ダイアログ(登録・更新)表示メッセージの取得
     *
     * @return
     */
    public String getDialogMessage() {

        // 更新フラグ
        if (editBeanProperty.getUpdateProcessFlg()) {
            return beanMessages.getMessage("osol.warn.beforeUpdateMessage");
        } else {
            return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
        }
    }

    /**
     * 確認ダイアログ(削除)表示メッセージの取得
     *
     * @return
     */
    public String getDeleteDialogMessage() {
        return beanMessages.getMessage("osol.warn.beforeDeleteMessage");
    }

    /**
     * 画面用データ
     *
     * @return
     */
    public EditBeanProperty getEditBeanProperty() {
        return editBeanProperty;
    }

}
