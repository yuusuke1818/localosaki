package jp.co.osaki.sms.bean.sms.server.setting.ocr;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.GetSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.UpdateSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.ocr.GetSmsOcrDeviceResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.ocr.UpdateSmsOcrDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.GetSmsOcrDeviceResult;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.UpdateSmsOcrDeviceResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;;

/**
 * AieLink 登録・編集画面
 * 「OCR検針」→「AieLink」へ変更
 *
 * @author iwasaki_y
 */
@Named(value = "smsServerSettingOcrEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -3399891089083953220L;

    //当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private EditBeanProperty editBeanProperty;

    // AieLink 検索画面
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
        eventLogger.debug(packageName.concat("smsServerSettingOcrEditBean():START"));

        // 初期処理
        init();

        this.editBeanProperty = editBeanProperty;

        if (this.editBeanProperty.getUpdateProcessFlg()) {
            // 編集
            // 「OCR検針」→「AieLink」へ変更
            // AieLink情報 取得
            GetSmsOcrDeviceResult result = this.getOcrDevice(this.editBeanProperty.getDevId());
            if (Objects.isNull(result)) {
                return null;
            }
            covertDisplay(result);
        }

        eventLogger.debug(packageName.concat("smsServerSettingOcrEditBean():ENDT"));

        return "ocrEdit";
    }

    /**
     * 画面表示用に置き換え
     *
     * @param result
     */
    private void covertDisplay(GetSmsOcrDeviceResult result) {
        editBeanProperty.setDevId(result.getDevId());
        editBeanProperty.setName(result.getName());
        editBeanProperty.setExamNoticeMonth(Objects.toString(result.getExamNoticeMonth(), null));
        editBeanProperty.setDelFlg(BooleanUtils.toBoolean(result.getDelFlg()));
        editBeanProperty.setVersion(result.getVersion());
    }

    /**
     * AieLink情報 登録・更新
     * 「OCR検針」→「AieLink」へ変更
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
        updateSmsOcrDevice();

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
                        .add(beanMessages.getMessage("smsServerSettingOcrEditBean.error.notInputDevId"));
                invalidComponent.add("smsServerSettingOcrEditBean:inputDevId");
                errorFlg = true;
            } else if (!checkOcrDevId(editBeanProperty.getDevId())) {
                // 文字数、書式チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[inputDevId]"));
                validateErrorMessageList
                        .add(beanMessages.getMessage("smsServerSettingOcrEditBean.error.notFormatDevId"));
                invalidComponent.add("smsServerSettingOcrEditBean:inputDevId");
                errorFlg = true;
            } else {
                // 重複チェック
                GetSmsOcrDeviceResult result = this.getOcrDevice(editBeanProperty.getDevId());
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
                            beanMessages.getMessageFormat("smsServerSettingOcrEditBean.error.duplicationDevId",
                                    editBeanProperty.getDevId()));
                    invalidComponent.add("smsServerSettingOcrEditBean:inputDevId");
                    errorFlg = true;
                }
            }
        }

        // 装置名称
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getName())
                && editBeanProperty.getName().length() > 20) {
            // 文字数チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devName]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingOcrEditBean.error.lengthOverDevName"));
            invalidComponent.add("smsServerSettingOcrEditBean:devName");
            errorFlg = true;
        }

        // 検満通知月
        if (Objects.nonNull(editBeanProperty.getExamNoticeMonth())
                && !CheckUtility.checkIntegerRange(editBeanProperty.getExamNoticeMonth(), 1, 99)) {
            // 範囲チェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[examNoticeMonth]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingOcrEditBean.error.notFormatExamNoticeMonth"));
            invalidComponent.add("smsServerSettingOcrEditBean:examNoticeMonth");
            errorFlg = true;
        }

        // APIエラーがなければバリデーションエラーを追加する
        for (String msg : validateErrorMessageList) {
            addErrorMessage(msg);
        }

        return !errorFlg;
    }

    /**
     * AieLink 装置IDのチェック
     * 「OCR検針」→「AieLink」へ変更
     *
     * @param devId
     * @return true:OK false:NG
     */
    private boolean checkOcrDevId(String devId) {
        String regrexStr = "^" + SmsConstants.DEVICE_KIND.OCR.getVal() + "[A-Za-z0-9]{0,6}$";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(devId);
        return m.matches();
    }

    /**
     * AieLink情報 取得API
     * 「OCR検針」→「AieLink」へ変更
     *
     * @param devId
     * @return
     */
    private GetSmsOcrDeviceResult getOcrDevice(String devId) {
        // 装置取得
        GetSmsOcrDeviceResponse response = new GetSmsOcrDeviceResponse();
        GetSmsOcrDeviceParameter parameter = new GetSmsOcrDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("GetSmsOcrDeviceBean");
        parameter.setDevId(devId);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (GetSmsOcrDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        GetSmsOcrDeviceResult result = null;

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
     * AieLink情報 登録・更新API
     * 「OCR検針」→「AieLink」へ変更
     *
     * @return
     */
    private UpdateSmsOcrDeviceResult updateSmsOcrDevice() {
        // 装置取得
        UpdateSmsOcrDeviceResponse response = new UpdateSmsOcrDeviceResponse();
        UpdateSmsOcrDeviceParameter parameter = new UpdateSmsOcrDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("UpdateSmsOcrDeviceBean");
        parameter.setDevId(editBeanProperty.getDevId());
        parameter.setName(editBeanProperty.getName());
        parameter.setExamNoticeMonth(editBeanProperty.getExamNoticeMonth() != null ? new BigDecimal(editBeanProperty.getExamNoticeMonth()) : null);
        parameter.setDelFlg(BooleanUtils.toInteger(editBeanProperty.getDelFlg(), OsolConstants.FLG_ON,
                OsolConstants.FLG_OFF, OsolConstants.FLG_OFF));
        parameter.setVersion(editBeanProperty.getVersion());

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (UpdateSmsOcrDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        UpdateSmsOcrDeviceResult result = null;

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
