package jp.co.osaki.sms.bean.sms.collect.setting.meterGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupsResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResultData;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * メーターグループ管理画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingMeterGroupManagementBean")
@ConversationScoped
public class MeterGroupManagementBean extends SmsConversationBean implements Serializable {

    @Inject
    private EditBean editBean;

    @Inject
    private EditGroupNameBean editGroupNameBean;

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    /** シリアライズID */
    private static final long serialVersionUID = 5717739481305690362L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 選択値 グループID. */
    private Long meterGroupId;

    /** 選択値 グループ名. */
    private String meterGroupName;

    /** 表示値 グループID. */
    private Long dispMeterGroupId;

    /** 表示値 グループ名. */
    private String dispMeterGroupName;

    /** グループプルダウンMap. */
    private Map<String, Long> meterGroupIdMap;

    /** 表示内容List. */
    private List<MeterGroupInfo> meterGroupContents = new ArrayList<>();

    /** 確認メッセージ */
    private String confirmMessage;

    /** 新規作成ボタン活性/非活性フラグ */
    private Boolean registButtonDisableFlg;

    /** 計算方法Map */
    private Map<Integer, String> calcTypeMap = new LinkedHashMap<>();

    @Override
    public String init() {
        conversationStart();
        for (SmsConstants.METER_GROUP_CALC_TYPE type : SmsConstants.METER_GROUP_CALC_TYPE.values()) {
            calcTypeMap.put(type.getVal(), type.getName());
        }

        // 遷移先
        return "meterGroupManagement";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.confirmMessage = "";
        this.registButtonDisableFlg = false;
        this.meterGroupIdMap = new LinkedHashMap<>();
        this.meterGroupContents.clear();

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

        //メーターグループ名をドロップダウンリストに登録
        if (response != null && response.getResult() != null && !response.getResult().getMeterGroupNameList().isEmpty()) {
            for (ListSmsMeterGroupNamesResultData meterGroupName : response.getResult().getMeterGroupNameList()) {
                this.meterGroupIdMap.put(meterGroupName.getMeterGroupName(), meterGroupName.getMeterGroupId());
            }
        }

        if(!this.meterGroupIdMap.isEmpty()) {
            //初期値として先頭の装置の情報を取得する
            this.meterGroupId = meterGroupIdMap.entrySet().stream().findFirst().get().getValue();
            this.meterGroupName = meterGroupIdMap.entrySet().stream().findFirst().get().getKey();
            this.dispMeterGroupId = meterGroupIdMap.entrySet().stream().findFirst().get().getValue();
            this.dispMeterGroupName = meterGroupIdMap.entrySet().stream().findFirst().get().getKey();

            ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
            groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
            groupsParameter.setMeterGroupId(this.meterGroupId);
            groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
            groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());

            List<GetSmsMeterGroupResultData> meters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();
            //表示用List作成
            for (GetSmsMeterGroupResultData meter : meters) {
                MeterGroupInfo info = new MeterGroupInfo();
                info.setMeterGroup(meter);
                meterGroupContents.add(info);
            }
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }

        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:init():END"));

        return init();
    }

    /**
     * 初期処理(登録・編集後遷移).
     *
     * @param buildingInfo 建物・テナント情報
     * @param meterGroupId メーターグループID
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, Long meterGroupId) {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.confirmMessage = "";
        this.registButtonDisableFlg = false;
        this.meterGroupIdMap = new LinkedHashMap<>();
        this.meterGroupContents.clear();

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

        //メーターグループ名をドロップダウンリストに登録
        if (response != null && response.getResult() != null && !response.getResult().getMeterGroupNameList().isEmpty()) {
            for (ListSmsMeterGroupNamesResultData meterGroupName : response.getResult().getMeterGroupNameList()) {
                this.meterGroupIdMap.put(meterGroupName.getMeterGroupName(), meterGroupName.getMeterGroupId());
            }
        }

        if(!this.meterGroupIdMap.isEmpty()) {
            //初期値として先頭の装置の情報を取得する
            this.meterGroupId = meterGroupId;
            this.meterGroupName = getGroupNameById(meterGroupId);
            this.dispMeterGroupId = meterGroupId;
            this.dispMeterGroupName = getGroupNameById(meterGroupId);

            ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
            groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
            groupsParameter.setMeterGroupId(this.meterGroupId);
            groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
            groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());

            List<GetSmsMeterGroupResultData> meters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();
            //表示用List作成
            for (GetSmsMeterGroupResultData meter : meters) {
                MeterGroupInfo info = new MeterGroupInfo();
                info.setMeterGroup(meter);
                meterGroupContents.add(info);
            }
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:init():END"));
        return init();
    }

    /**
     * メーターグループ一覧取得処理
     * @return
     */
    private ListSmsMeterGroupsResponse getListMeterGroups(ListSmsMeterGroupsParameter parameter) {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:getListMeterGroups():START"));
        ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMeterGroupsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:getListMeterGroups():END"));
        return response;
    }

    /**
     * 表示更新ボタン押下時の処理
     * @return
     */
    @Logged
    public String execSearch() {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:execSearch():START"));
        this.meterGroupContents.clear();
        this.registButtonDisableFlg = false;
        if (!meterGroupIdMap.isEmpty()) {
            ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
            groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
            groupsParameter.setMeterGroupId(this.meterGroupId);
            groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
            groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());

            List<GetSmsMeterGroupResultData> meters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();
            //表示用List作成
            for (GetSmsMeterGroupResultData meter : meters) {
                MeterGroupInfo info = new MeterGroupInfo();
                info.setMeterGroup(meter);
                meterGroupContents.add(info);
            }
            this.dispMeterGroupId = this.meterGroupId;
            this.dispMeterGroupName = getGroupNameById(this.meterGroupId);
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:execSearch():END"));

        return "meterGroupManagement";
    }

    /**
     * 名称設定ボタン押下時の処理
     * @return
     */
    @Logged
    public String execRename() {
        return editGroupNameBean.init(this.buildingInfo, this.dispMeterGroupId);
    }

    /**
     * 登録ボタン押下時の処理
     * @return
     */
    @Logged
    public String execRegist() {
        return editBean.init(this.buildingInfo, this.dispMeterGroupId, this.dispMeterGroupName);
    }

    /**
     * 選択変更ボタン押下時の処理
     * @return
     */
    @Logged
    public String execMultiEdit() {
        List<GetSmsMeterGroupResultData> editList = meterGroupContents.stream()
                .filter(x -> x.getCheckBox())
                .map(x -> x.getMeterGroup())
                .collect(Collectors.toList());
        return editBean.init(this.buildingInfo, this.dispMeterGroupId, this.dispMeterGroupName, editList);
    }

    /**
     * 設定変更ボタン押下時の処理
     * @return
     */
    @Logged
    public String execEdit(GetSmsMeterGroupResultData resultData) {
        return editBean.init(this.buildingInfo, this.dispMeterGroupId, this.dispMeterGroupName, resultData.getDevId(), resultData.getMeterMngId());
    }

    /**
     * 削除ボタン押下時のメッセージ変更処理
     * @return
     */
    @Logged
    public void setDeleteMessage() {
        this.confirmMessage = "チェックされているメーターを削除<br />します。よろしいですか？";
    }

    /**
     * 削除ボタン押下時の処理
     * @return
     */
    @Logged
    public String execDelete() {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:execDelete():START"));
        //削除処理リクエスト作成
        List<DeleteSmsMeterGroupsRequestSet> requestSetList = new ArrayList<>();
        for (MeterGroupInfo content: meterGroupContents) {
            if (content.getCheckBox()) {
                DeleteSmsMeterGroupsRequestSet requestSet = new DeleteSmsMeterGroupsRequestSet();
                requestSet.setDevId(content.getMeterGroup().getDevId());
                requestSet.setMeterMngId(content.getMeterGroup().getMeterMngId());
                requestSet.setVersion(content.getMeterGroup().getVersion());
                requestSetList.add(requestSet);
            }
        }
        DeleteSmsMeterGroupsParameter parameter = new DeleteSmsMeterGroupsParameter();
        parameter.setBean("smsCollectSettingMeterGroupDeleteSmsMeterGroupsBean");
        DeleteSmsMeterGroupsRequest request = new DeleteSmsMeterGroupsRequest();
        request.setCorpId(this.buildingInfo.getId().getCorpId());
        request.setBuildingId(this.buildingInfo.getId().getBuildingId());
        request.setMeterGroupId(this.dispMeterGroupId);
        request.setMeterGroupList(requestSetList);
        parameter.setDeleteMeterGroupsRequest(request);
        ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
        //更新処理API実行
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMeterGroupsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return "";
        }

        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("MeterGroupManagementBean:execDelete():END"));

        return execSearch();
    }

    /**
     * MeterGroupIdMapからvalueでkeyを取得する
     * @param id
     * @return
     */
    private String getGroupNameById(Long id) {
        return meterGroupIdMap.entrySet().stream().filter(x -> x.getValue().equals(id)).findFirst().get().getKey();
    }

    public String getCalcTypeStr(Integer calcType) {
        return calcTypeMap.get(calcType);
    }

    public String getDevNameFromMap(Long value) {
        return meterGroupIdMap.entrySet().stream().filter(x -> x.getValue().equals(value)).findFirst().get().getKey();
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

    public Map<String, Long> getMeterGroupIdMap() {
        return meterGroupIdMap;
    }

    public void setMeterGroupIdMap(Map<String, Long> meterGroupIdMap) {
        this.meterGroupIdMap = meterGroupIdMap;
    }

    public List<MeterGroupInfo> getMeterGroupContents() {
        return meterGroupContents;
    }

    public void setMeterGroupContents(List<MeterGroupInfo> meterGroupContents) {
        this.meterGroupContents = meterGroupContents;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public Boolean getRegistButtonDisableFlg() {
        return registButtonDisableFlg;
    }

    public void setRegistButtonDisableFlg(Boolean registButtonDisableFlg) {
        this.registButtonDisableFlg = registButtonDisableFlg;
    }

}
