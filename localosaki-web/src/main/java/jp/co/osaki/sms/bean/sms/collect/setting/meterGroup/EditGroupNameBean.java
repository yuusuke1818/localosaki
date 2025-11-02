package jp.co.osaki.sms.bean.sms.collect.setting.meterGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * メーターグループ名編集画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingMeterGroupManagementEditGroupNameBean")
@ConversationScoped
public class EditGroupNameBean extends SmsConversationBean implements Serializable {

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private MeterGroupManagementBean meterGroupManagementBean;

    /** シリアライズID */
    private static final long serialVersionUID = -9033331020472488615L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** メーターグループID */
    private Long meterGroupId;

    /** 入力値 メーターグループ名. */
    private String meterGroupName;

    /** バージョン */
    private Integer version;

    /** 確認メッセージ */
    private String confirmMessage;

    /** エラーコンポーネントリスト */
    private List<String> invalidComponent;

    @Override
    public String init() {
        conversationStart();

        // 遷移先
        return "meterGroupManagementGroupNameEdit";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, Long meterGroupId) {
        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:init():START"));
        this.buildingInfo = buildingInfo;

        //取得処理
        this.meterGroupId = meterGroupId;

        this.confirmMessage = "グループ名称の設定変更をおこないます。<br />よろしいですか？";

        initInvalidComponent();

      //メーターグループ名取得
        ListSmsMeterGroupNamesParameter namesParameter = new ListSmsMeterGroupNamesParameter();
        namesParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupNamesBean");
        namesParameter.setCorpId(buildingInfo.getId().getCorpId());
        namesParameter.setBuildingId(buildingInfo.getId().getBuildingId());

        ListSmsMeterGroupNamesResponse response = new ListSmsMeterGroupNamesResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMeterGroupNamesResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                namesParameter,
                response);

        List<ListSmsMeterGroupNamesResultData> meterNameList = response.getResult().getMeterGroupNameList();

        Optional<ListSmsMeterGroupNamesResultData> meterName = meterNameList.stream()
                .filter(x -> x.getMeterGroupId().equals(this.meterGroupId))
                .findFirst();
        if (meterName.isPresent()) {
            this.meterGroupName = meterName.get().getMeterGroupName();
            this.version = meterName.get().getVersion();
        } else {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterGroupManagementBean.error.meterGroupName.notfound"));
            return "";
        }
        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:init():END"));
        return init();
    }

    /**
     * 各画面の登録ボタン押下時の処理
     * @return
     */
    @Logged
    public String execUpdate() {
        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:execUpdate():START"));
        //バリデーション実行
        if (!validation()) {
            return "";
        }

        //メーターグループ名称編集処理
        UpdateSmsMeterGroupNameParameter parameter = new UpdateSmsMeterGroupNameParameter();
        parameter.setBean("smsCollectSettingMeterGroupUpdateSmsMeterGroupNameBean");
        parameter.setCorpId(this.buildingInfo.getId().getCorpId());
        parameter.setBuildingId(this.buildingInfo.getId().getBuildingId());
        parameter.setMeterGroupId(this.meterGroupId);
        parameter.setMeterGroupName(this.meterGroupName);
        parameter.setVersion(this.version);
        UpdateSmsMeterGroupNameResponse response = new UpdateSmsMeterGroupNameResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (UpdateSmsMeterGroupNameResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return "";
        }

        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:execUpdate():END"));

        return meterGroupManagementBean.init(this.buildingInfo, this.meterGroupId);
    }

    private boolean validation() {
        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:validation():START"));
        initInvalidComponent();
        //メーターグループ名入力なし
        if (CheckUtility.isNullOrEmpty(this.meterGroupName)) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterGroupManagementBean.error.MeterGroupName.noInput"));
            invalidComponent.add("smsCollectSettingMeterGroupManagementEditGroupNameBean:group_name");
            return false;
        }

        eventLogger.debug(EditGroupNameBean.class.getPackage().getName().concat("EditGroupNameBean:validation():END"));

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

    public Long getMeterGroupId() {
        return meterGroupId;
    }

    public void setMeterGroupId(Long meterGroupId) {
        this.meterGroupId = meterGroupId;
    }

    public String getMeterGroupName() {
        return meterGroupName;
    }

    public void setMeterGroupName(String meterGroupName) {
        this.meterGroupName = meterGroupName;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public List<String> getInvalidComponent() {
        return invalidComponent;
    }

    public void setInvalidComponent(List<String> invalidComponent) {
        this.invalidComponent = invalidComponent;
    }
}
