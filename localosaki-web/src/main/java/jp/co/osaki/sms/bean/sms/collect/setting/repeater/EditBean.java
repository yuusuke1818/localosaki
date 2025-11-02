package jp.co.osaki.sms.bean.sms.collect.setting.repeater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.ListSmsRepeatersParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.UpdateSmsRepeaterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.repeater.UpdateSmsRepeaterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.repeater.UpdateSmsRepeaterRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.repeater.ListSmsRepeatersResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * 中継装置登録画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingRepeaterManagementEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private RepeaterManagementBean repeaterManagementBean;

    /** シリアライズID */
    private static final long serialVersionUID = -6173058878369615654L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 編集フラグ. */
    private boolean editFlg = false;

    /** 選択値 接続先(上位連携装置ID). */
    private String devId;

    /** 選択値 接続先名. */
    private String devName;

    /** 管理番号プルダウンMap. */
    private Map<String, Long> repeaterMngIdMap = new LinkedHashMap<>();

    /** 選択値 中継装置管理番号. */
    private Long repeaterMngId;

    /** 入力値 コンセントレータ名. */
    private String repeaterId;

    /** コメント */
    private String comment;

    /** バージョン */
    private Integer version;

    /** 確認メッセージ */
    private String confirmMessage;

    /** 中継装置リスト */
    private List<ListSmsRepeatersResultData> repeatersList = new ArrayList<>();

    /** エラーコンポーネントリスト */
    private List<String> invalidComponent;

    @Override
    public String init() {
        conversationStart();

        // 遷移先
        return "repeaterManagementEdit";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, String devId, String devName, Long repeaterMngId) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.editFlg = true;
        initInvalidComponent();

        //取得処理
        this.devId = devId;
        this.devName = devName;
        this.repeaterMngId = repeaterMngId;
        this.confirmMessage = "中継装置の設定変更をおこないます。<br />よろしいですか？";

        //対象装置の登録済みデータを取得
        ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();
        parameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
        parameter.setDevId(devId);
        ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsRepeatersResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        repeatersList = response.getResult().getRepeaterList();

        Optional<ListSmsRepeatersResultData> repeater = repeatersList.stream()
                .filter(x -> x.getDevId().equals(this.devId))
                .filter(x -> x.getRepeaterMngId().equals(this.repeaterMngId))
                .findFirst();
        if (repeater.isPresent()) {
            this.repeaterId = repeater.get().getRepeaterId();
            this.comment = repeater.get().getMemo();
            this.version = repeater.get().getVersion();
        } else {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingRepeaterManagementBean.error.notfound"));
            return "";
        }
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():END"));
        return init();
    }

    public String init(ListInfo buildingInfo, String devId, String devName) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.editFlg = false;
        this.devId = devId;
        this.devName = devName;
        this.confirmMessage = "中継装置の新規登録をおこないます。<br />よろしいですか？";
        initInvalidComponent();

        //入力フォームリセット
        this.repeaterId = "";
        this.comment = "";
        this.version = null;

        //対象装置の登録済みデータを取得
        ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();
        parameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
        parameter.setDevId(devId);
        ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsRepeatersResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        repeatersList = response.getResult().getRepeaterList();

        //空いているコンセントレータIDのみを表示する
        List<Long> repeaterMngIdList = repeatersList.stream().map(x -> x.getRepeaterMngId()).collect(Collectors.toList());
        repeaterMngIdMap.clear();
        for (long targetId = 1; targetId <= SmsConstants.MAX_REPEATER_COUNT; targetId++) {
            if (!repeaterMngIdList.contains(targetId)) {
                repeaterMngIdMap.put(String.format("%02d", targetId), targetId);
            }
        }
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():END"));

        return init();
    }

    /**
     * 各画面の登録ボタン押下時の処理
     * @return
     */
    @Logged
    public String execUpdate() {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:execUpdate():START"));
        //バリデーション実行
        if (!validation()) {
            return "";
        }

        //コンセントレータ追加処理
        List<UpdateSmsRepeaterRequestSet> requestSetList = new ArrayList<>();
        UpdateSmsRepeaterRequestSet requestSet = new UpdateSmsRepeaterRequestSet();
        requestSet.setRepeaterMngId(this.repeaterMngId);
        requestSet.setCommandFlg("1");
        requestSet.setSrvEnt("1");
        requestSet.setRepeaterId(this.repeaterId);
        requestSet.setMemo(this.comment);
        if (version != null) {
            requestSet.setVersion(this.version);
        }
        requestSetList.add(requestSet);

        UpdateSmsRepeaterParameter parameter = new UpdateSmsRepeaterParameter();
        parameter.setBean("smsCollectSettingRepeaterUpdateSmsRepeaterBean");
        UpdateSmsRepeaterRequest request = new UpdateSmsRepeaterRequest();
        request.setDevId(this.devId);
        request.setRepeaterList(requestSetList);
        parameter.setUpdateSmsRepeaterRequest(request);
        ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsRepeatersResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return "";
        }

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:execUpdate():END"));

        return repeaterManagementBean.init(this.buildingInfo, this.devId);
    }

    private boolean validation() {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:validation():START"));
        initInvalidComponent();
        //中継装置入力なし
        if (CheckUtility.isNullOrEmpty(this.repeaterId)) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingRepeaterManagementBean.error.RepeaterId"));
            invalidComponent.add("smsCollectSettingRepeaterManagementEditBean:repeater_name");
            return false;
        }

        //中継装置文字種エラー
        Pattern p = Pattern.compile("[^A-Za-z0-9]+");
        if (p.matcher(this.repeaterId).find()) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingRepeaterManagementBean.error.RepeaterId.charType"));
            invalidComponent.add("smsCollectSettingRepeaterManagementEditBean:repeater_name");
            return false;
        }

        //中継装置フォーマットエラー
        //先頭文字がRかどうか
        if (!this.repeaterId.substring(0, 1).equals("R")) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingRepeaterManagementBean.error.RepeaterId.format"));
            invalidComponent.add("smsCollectSettingRepeaterManagementEditBean:repeater_name");
            return false;
        }

        //中継装置文字列エラー
        //10文字以外でエラー
        if (this.repeaterId.length() != 10) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingRepeaterManagementBean.error.RepeaterId.length"));
            invalidComponent.add("smsCollectSettingRepeaterManagementEditBean:repeater_name");
            return false;
        }

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:validation():END"));

        //エラーがあれば失敗(false)、エラーなしでOK(true)
        return true;

    }

    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    public void initInvalidComponent() {

        if (this.invalidComponent == null) {
            this.invalidComponent = new ArrayList<>();
        } else {
            this.invalidComponent.clear();
        }
    }

    public boolean isEditFlg() {
        return editFlg;
    }

    public void setEditFlg(boolean editFlg) {
        this.editFlg = editFlg;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Map<String, Long> getRepeaterMngIdMap() {
        return repeaterMngIdMap;
    }

    public void setRepeaterMngIdMap(Map<String, Long> repeaterMngIdMap) {
        this.repeaterMngIdMap = repeaterMngIdMap;
    }

    public Long getRepeaterMngId() {
        return repeaterMngId;
    }

    public void setRepeaterMngId(Long repeaterMngId) {
        this.repeaterMngId = repeaterMngId;
    }

    public String getRepeaterId() {
        return repeaterId;
    }

    public void setRepeaterId(String repeaterId) {
        this.repeaterId = repeaterId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public List<ListSmsRepeatersResultData> getRepeatersList() {
        return repeatersList;
    }

    public void setRepeatersList(List<ListSmsRepeatersResultData> repeatersList) {
        this.repeatersList = repeatersList;
    }

    public List<String> getInvalidComponent() {
        return invalidComponent;
    }

    public void setInvalidComponent(List<String> invalidComponent) {
        this.invalidComponent = invalidComponent;
    }
}
