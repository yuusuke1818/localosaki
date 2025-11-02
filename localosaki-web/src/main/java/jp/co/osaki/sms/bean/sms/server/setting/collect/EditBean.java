package jp.co.osaki.sms.bean.sms.server.setting.collect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.GetSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.UpdateSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.collect.GetSmsCollectDeviceResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.collect.UpdateSmsCollectDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.GetSmsCollectDeviceResult;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.UpdateSmsCollectDeviceResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;;

/**
 * 装置 登録・編集画面
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingCollectEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -8833426600530897121L;

    //当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private EditBeanProperty editBeanProperty;

    // 装置 検索画面
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
        eventLogger.debug(packageName.concat("smsServerSettingCollectEditBean():START"));

        // 初期処理
        init();

        this.editBeanProperty = editBeanProperty;

        if (!this.editBeanProperty.getUpdateProcessFlg()) {
            // 登録
            // 初期データ
            this.editBeanProperty.setAlertDisableFlg(true);
            this.editBeanProperty.setRevFlg(false);
            this.editBeanProperty.setCommInterval("3");
        } else {
            // 編集
            // 装置情報 取得
            GetSmsCollectDeviceResult result = this.getCollectDevice(this.editBeanProperty.getDevId());
            if (Objects.isNull(result)) {
                return null;
            }
            covertDisplay(result);
        }

        eventLogger.debug(packageName.concat("smsServerSettingCollectEditBean():ENDT"));

        return "collectEdit";
    }

    /**
     * 画面表示用に置き換え
     *
     * @param result
     */
    private void covertDisplay(GetSmsCollectDeviceResult result) {
        editBeanProperty.setDevId(result.getDevId());
        editBeanProperty.setDevPw(result.getDevPw());
        editBeanProperty.setName(result.getName());
        editBeanProperty.setMemo(result.getMemo());
        editBeanProperty.setIpAddr(result.getIpAddr());
        editBeanProperty.setHomeDirectory(result.getHomeDirectory());
        editBeanProperty.setExamNoticeMonth(Objects.toString(result.getExamNoticeMonth(), null));
        if (NumberUtils.isNumber(result.getAlertDisableFlg())) {
            editBeanProperty.setAlertDisableFlg(BooleanUtils.toBoolean(Integer.parseInt(result.getAlertDisableFlg())));
        }
        if (NumberUtils.isNumber(result.getRevFlg())) {
            editBeanProperty.setRevFlg(BooleanUtils.toBoolean(Integer.parseInt(result.getRevFlg())));
        }
        editBeanProperty.setCommInterval(Objects.toString(result.getCommInterval(), null));
        editBeanProperty.setDelFlg(BooleanUtils.toBoolean(result.getDelFlg()));
        editBeanProperty.setVersion(result.getVersion());
    }

    /**
     * 装置情報 登録・更新
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
        updateSmsCollectDevice();

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
                        .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notInputDevId"));
                invalidComponent.add("smsServerSettingCollectEditBean:inputDevId");
                errorFlg = true;
            } else if (!checkCollectDevId(editBeanProperty.getDevId())) {
                // 文字数、書式チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[inputDevId]"));
                validateErrorMessageList
                        .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFormatDevId"));
                invalidComponent.add("smsServerSettingCollectEditBean:inputDevId");
                errorFlg = true;
            } else {
                // 重複チェック
                GetSmsCollectDeviceResult result = this.getCollectDevice(editBeanProperty.getDevId());
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
                            beanMessages.getMessageFormat("smsServerSettingCollectEditBean.error.duplicationDevId",
                                    editBeanProperty.getDevId()));
                    invalidComponent.add("smsServerSettingCollectEditBean:inputDevId");
                    errorFlg = true;
                }
            }
        }

        //装置パスワード
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getDevPw())) {
            // NULLチェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devPw]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notInputDevPw"));
            invalidComponent.add("smsServerSettingCollectEditBean:devPw");
            errorFlg = true;
        } else if (!checkDevPw(editBeanProperty.getDevPw())) {
            // 文字数、書式チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devPw]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFromatDevPw"));
            invalidComponent.add("smsServerSettingCollectEditBean:devPw");
            errorFlg = true;
        }

        // 装置名称
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getName())
                && editBeanProperty.getName().length() > 20) {
            // 文字数チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[devName]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.lengthOverDevName"));
            invalidComponent.add("smsServerSettingCollectEditBean:devName");
            errorFlg = true;
        }

        // IPアドレス
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getIpAddr())) {
            if (!checkIpAddress(editBeanProperty.getIpAddr())) {
                // 文字種チェック
                eventLogger
                        .debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[ipAddr]"));
                validateErrorMessageList.add(
                        beanMessages.getMessage("smsServerSettingCollectEditBean.error.notTypeIpAddress"));
                invalidComponent.add("smsServerSettingCollectEditBean:ipAddr");
                errorFlg = true;
            } else {
                // 書式チェック
                List<String> octets = Arrays.asList(editBeanProperty.getIpAddr().split("\\."));
                Boolean ipErrorFlg = false;
                if (octets.size() != 4) {
                    eventLogger.debug(
                            EditBean.class.getPackage().getName().concat(":: validateInputParam() error[ipAddr]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFormatIpAddress"));
                    invalidComponent.add("smsServerSettingCollectEditBean:ipAddr");
                    errorFlg = true;
                    ipErrorFlg = true;
                } else {
                    // 範囲チェック
                    for (String octet : octets) {
                        if (CheckUtility.isNullOrEmpty(octet)
                                || Integer.valueOf(octet).compareTo(0) < 0
                                || Integer.valueOf(octet).compareTo(255) > 0) {
                            eventLogger.debug(EditBean.class.getPackage().getName()
                                    .concat(":: validateInputParam() error[ipAddr]"));
                            validateErrorMessageList.add(
                                    beanMessages
                                            .getMessage("smsServerSettingCollectEditBean.error.rangeOverIpAddress"));
                            invalidComponent.add("smsServerSettingCollectEditBean:ipAddr");
                            errorFlg = true;
                            ipErrorFlg = true;
                            break;
                        }
                    }
                }
                if (!ipErrorFlg) {
                    // IPアドレスの各オクテットを3桁0詰め
                    editBeanProperty.setIpAddr(octets.stream().map(x -> String.format("%03d", Integer.valueOf(x)))
                            .collect(Collectors.joining(".")));
                }
            }
        }
        // メモ
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getMemo())
                && editBeanProperty.getMemo().length() > 20) {
            // 文字数チェック
            eventLogger.debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[memo]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.lengthOverMemo"));
            invalidComponent.add("smsServerSettingCollectEditBean:memo");
            errorFlg = true;
        }

        // ホームディレクトリ名
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getHomeDirectory())) {
            if (editBeanProperty.getHomeDirectory().length() > 40) {
                // 文字数チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[homeDirectory]"));
                validateErrorMessageList.add(
                        beanMessages.getMessage("smsServerSettingCollectEditBean.error.lengthOverHomeDirectry"));
                invalidComponent.add("smsServerSettingCollectEditBean:homeDirectory");
                errorFlg = true;
            } else if (!checkHomeDirectry(editBeanProperty.getHomeDirectory())) {
                // 書式チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[homeDirectory]"));
                validateErrorMessageList
                        .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFormatHomeDirectry"));
                invalidComponent.add("smsServerSettingCollectEditBean:homeDirectory");
                errorFlg = true;
            }

        }

        // 検満通知月
        if (Objects.nonNull(editBeanProperty.getExamNoticeMonth())
                && !CheckUtility.checkIntegerRange(editBeanProperty.getExamNoticeMonth(), 1, 99)) {
            // 範囲チェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[examNoticeMonth]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFormatExamNoticeMonth"));
            invalidComponent.add("smsServerSettingCollectEditBean:examNoticeMonth");
            errorFlg = true;
        }

        // サーバ通信
        if (Objects.isNull(editBeanProperty.getCommInterval())) {
            // NULLチェック
            eventLogger
                    .debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[commInterval]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notInputCommInterval"));
            invalidComponent.add("smsServerSettingCollectEditBean:commInterval");
            errorFlg = true;
        } else if (!CheckUtility.checkIntegerRange(editBeanProperty.getCommInterval(), 3, 30)) {
            // 範囲チェック
            eventLogger
                    .debug(EditBean.class.getPackage().getName().concat(":: validateInputParam() error[commInterval]"));
            validateErrorMessageList
                    .add(beanMessages.getMessage("smsServerSettingCollectEditBean.error.notFormatCommInterval"));
            invalidComponent.add("smsServerSettingCollectEditBean:commInterval");
            errorFlg = true;
        }

        // APIエラーがなければバリデーションエラーを追加する
        for (String msg : validateErrorMessageList) {
            addErrorMessage(msg);
        }

        return !errorFlg;
    }

    /**
     * 装置 装置IDのチェック
     *
     * @param devId
     * @return true:OK false:NG
     */
    private boolean checkCollectDevId(String devId) {
        String regrexStr = "^(?!" + SmsConstants.DEVICE_KIND.HANDY.getVal() + ")[A-Za-z0-9]{1,8}$";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(devId);
        if (m.matches()) {
        	regrexStr = "^(?!" + SmsConstants.DEVICE_KIND.OCR.getVal() + ")[A-Za-z0-9]{1,8}$";
            p = Pattern.compile(regrexStr);
            m = p.matcher(devId);
        }
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
     * IPアドレスのチェック
     *
     * @param ipAddress
     * @return true:OK false:NG
     */
    private boolean checkIpAddress(String ipAddress) {
        String regrexStr = "[0-9.]+";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(ipAddress);
        return m.matches();
    }

    /**
     * ホームディレクトリ名のチェック
     *
     * @param ipAddress
     * @return true:OK false:NG
     */
    private boolean checkHomeDirectry(String homeDirectry) {
        String regrexStr = "[A-Za-z0-9_/]+";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(homeDirectry);
        return m.matches();
    }

    /**
     * 装置情報 取得API
     *
     * @param devId
     * @return
     */
    private GetSmsCollectDeviceResult getCollectDevice(String devId) {
        // 装置取得
        GetSmsCollectDeviceResponse response = new GetSmsCollectDeviceResponse();
        GetSmsCollectDeviceParameter parameter = new GetSmsCollectDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("GetSmsCollectDeviceBean");
        parameter.setDevId(devId);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (GetSmsCollectDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        GetSmsCollectDeviceResult result = null;

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
     * 装置情報 登録・更新API
     *
     * @return
     */
    private UpdateSmsCollectDeviceResult updateSmsCollectDevice() {
        // 装置取得
        UpdateSmsCollectDeviceResponse response = new UpdateSmsCollectDeviceResponse();
        UpdateSmsCollectDeviceParameter parameter = new UpdateSmsCollectDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("UpdateSmsCollectDeviceBean");
        parameter.setDevId(editBeanProperty.getDevId());
        parameter.setDevPw(editBeanProperty.getDevPw());
        parameter.setName(editBeanProperty.getName());
        parameter.setMemo(editBeanProperty.getMemo());
        parameter.setIpAddr(editBeanProperty.getIpAddr());
        parameter.setHomeDirectory(editBeanProperty.getHomeDirectory());
        parameter.setExamNoticeMonth(editBeanProperty.getExamNoticeMonth() != null ? new BigDecimal(editBeanProperty.getExamNoticeMonth()) : null);
        parameter.setAlertDisableFlg(BooleanUtils.toString(editBeanProperty.getAlertDisableFlg(), "1", "0", "1"));
        parameter.setRevFlg(BooleanUtils.toString(editBeanProperty.getRevFlg(), "1", "0", "0"));
        parameter.setCommInterval(editBeanProperty.getCommInterval() != null ? new BigDecimal(editBeanProperty.getCommInterval()) : null);
        parameter.setDelFlg(BooleanUtils.toInteger(editBeanProperty.getDelFlg(), OsolConstants.FLG_ON,
                OsolConstants.FLG_OFF, OsolConstants.FLG_OFF));
        parameter.setVersion(editBeanProperty.getVersion());

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (UpdateSmsCollectDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        UpdateSmsCollectDeviceResult result = null;

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
