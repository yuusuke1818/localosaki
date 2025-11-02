package jp.co.osaki.sms.bean.sms.collect.setting.meterGroup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMetersParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupsResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMetersResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMetersResultData;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * メーターグループ登録画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingMeterGroupManagementEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private MeterGroupManagementBean meterGroupManagementBean;

    /** シリアライズID */
    private static final long serialVersionUID = -3795158105942207506L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 編集フラグ. */
    private boolean editFlg = false;

    /** 選択編集フラグ. */
    private boolean multiEditFlg = false;

    /** 登録ボタン無効化フラグ. */
    private boolean registDisabledFlg = false;

    /** 選択値 メーターグループID. */
    private Long meterGroupId;

    /** 選択値 メーターグループ名. */
    private String meterGroupName;

    /** 選択値 登録装置. */
    private String devId;

    /** 選択値 登録装置名. */
    private String devName;

    /** 登録装置プルダウンMap. */
    private Map<String, String> devIdMap = new LinkedHashMap<>();

    /** 選択値 メーター管理ID. */
    private Long meterMngId;

    /** メーター管理IDプルダウンMap. */
    private Map<String, Long> meterMngIdMap = new LinkedHashMap<>();

    /** 選択値 加減算. */
    private Integer calcType;

    /** 加減算プルダウンMap. */
    private Map<String, Integer> calcTypeMap = new LinkedHashMap<>();

    /** 登録済みメーターリスト */
    private List<GetSmsMeterGroupResultData> existMeters;

    /** 一括変更対象メーターリスト */
    private List<GetSmsMeterGroupResultData> multiEditMeters;

    /** バージョン */
    private Integer version;

    /** 確認メッセージ */
    private String confirmMessage;


    @Override
    public String init() {
        //計算方法のドロップダウンリストを作成
        for (SmsConstants.METER_GROUP_CALC_TYPE type : SmsConstants.METER_GROUP_CALC_TYPE.values()) {
            calcTypeMap.put(type.getName(), type.getVal());
        }
        conversationStart();

        // 遷移先
        return "meterGroupManagementEdit";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, Long meterGroupId, String meterGroupName, String devId, Long meterMngId) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        //取得処理
        this.buildingInfo = buildingInfo;
        this.meterGroupId = meterGroupId;
        this.meterGroupName = meterGroupName;
        this.editFlg = true;
        this.multiEditFlg = false;
        this.meterMngId = meterMngId;
        this.devIdMap = toolsPullDownList.getDevPrm(buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));
        this.devId = devId;
        this.devName = getDevNameById(devId);
        this.confirmMessage = "メーターグループの設定変更をおこないます。<br />よろしいですか？";

        //登録済みメーター情報を取得
        ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
        groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
        groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
        groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());
        groupsParameter.setMeterGroupId(this.meterGroupId);
        List<GetSmsMeterGroupResultData> meters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();

        Optional<GetSmsMeterGroupResultData> meter = meters.stream()
                .filter(x -> x.getDevId().equals(this.devId))
                .filter(x -> x.getMeterMngId().equals(this.meterMngId))
                .findFirst();

        if (meter.isPresent()) {
            this.meterMngId = meter.get().getMeterMngId();
            this.calcType = meter.get().getCalcType().intValue();
            this.version = meter.get().getVersion();
        } else {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterGroupManagementEditBean.error.notfound"));
            return "";
        }
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():END"));
        return init();
    }

    /**
     * 初期処理. 複数選択更新
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, Long meterGroupId, String meterGroupName, List<GetSmsMeterGroupResultData> editList) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.meterGroupId = meterGroupId;
        this.meterGroupName = meterGroupName;
        this.version = null;
        this.multiEditMeters = new ArrayList<>();
        this.editFlg = true;
        this.multiEditFlg = true;
        this.calcType = SmsConstants.METER_GROUP_CALC_TYPE.ADDITION.getVal();

        //取得処理
        this.meterGroupId = meterGroupId;
        this.confirmMessage = "メーターグループの設定変更をおこないます。<br />よろしいですか？";

        //登録済みメーター情報を取得
        ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
        groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
        groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
        groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());
        groupsParameter.setMeterGroupId(this.meterGroupId);
        existMeters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();

        for (GetSmsMeterGroupResultData editMeter : editList) {
            Optional<GetSmsMeterGroupResultData> meter = existMeters.stream()
                    .filter(x -> x.getDevId().equals(editMeter.getDevId()))
                    .filter(x -> x.getMeterMngId().equals(editMeter.getMeterMngId()))
                    .findFirst();

            if (meter.isPresent()) {
                multiEditMeters.add(editMeter);
            } else {
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingMeterGroupManagementBean.error.meterGroup.notfound"));
                return "";
            }
        }

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():END"));
        return init();
    }


    public String init(ListInfo buildingInfo, Long meterGroupId, String meterGroupName) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.meterGroupId = meterGroupId;
        this.meterGroupName = meterGroupName;
        this.version = null;
        this.editFlg = false;
        this.multiEditFlg = false;
        this.registDisabledFlg = false;
        this.confirmMessage = "メーターグループの新規登録をおこないます。<br />よろしいですか？";
        this.calcType = SmsConstants.METER_GROUP_CALC_TYPE.ADDITION.getVal();
        this.devIdMap = toolsPullDownList.getDevPrm(buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));

        //初期値として先頭の装置の情報を取得する
        this.devId = devIdMap.entrySet().stream().findFirst().get().getValue();
        this.devName = devIdMap.entrySet().stream().findFirst().get().getKey();

        //登録済みメーター情報を取得
        ListSmsMeterGroupsParameter groupsParameter = new ListSmsMeterGroupsParameter();
        groupsParameter.setBean("smsCollectSettingmeterGroupListSmsMeterGroupsBean");
        groupsParameter.setCorpId(buildingInfo.getId().getCorpId());
        groupsParameter.setBuildingId(buildingInfo.getId().getBuildingId());
        existMeters = getListMeterGroups(groupsParameter).getResult().getMeterGroupList();
        updateMeterMndIdMap();
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():END"));

        return init();
    }

    /**
     * メーターリスト更新処理
     */
    @Logged
    public void updateMeterMndIdMap() {
        //登録対象メーターから登録済みメーターを除外してプルダウンリストを作成
        this.registDisabledFlg = false;
        List<GetSmsMeterGroupResultData> filteredMeter = existMeters.stream()
                .filter(x -> x.getDevId().equals(this.devId))
                .collect(Collectors.toList());

        ListSmsMetersParameter metersParameter = new ListSmsMetersParameter();
        metersParameter.setBean("smsCollectSettingmeterGroupListSmsMetersBean");
        metersParameter.setCorpId(buildingInfo.getId().getCorpId());
        metersParameter.setBuildingId(buildingInfo.getId().getBuildingId());

        ListSmsMetersResponse response = new ListSmsMetersResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMetersResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                metersParameter,
                response);
        if (response != null) {
            this.meterMngIdMap = createMeterDropDown(response.getResult().getMetersList(),filteredMeter);
        }

    }

    /**
     * メーターグループ一覧取得処理
     * @return
     */
    private ListSmsMeterGroupsResponse getListMeterGroups(ListSmsMeterGroupsParameter parameter) {
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("EditBean:getListMeterGroups():START"));
        ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMeterGroupsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        eventLogger.debug(MeterGroupManagementBean.class.getPackage().getName().concat("EditBean:getListMeterGroups():END"));
        return response;
    }

    /**
     * メータードロップダウンリスト作成処理
     */
    private Map<String, Long> createMeterDropDown(List<ListSmsMetersResultData> meterList,List<GetSmsMeterGroupResultData> existMeterList) {
        Map<String,Long> returnMap = new LinkedHashMap<>();
        List<ListSmsMetersResultData> filterdMeters = meterList.stream().filter(x -> x.getDevId().equals(this.devId)).collect(Collectors.toList());

        //除外対象メーターが存在しない場合はそのまま登録
        if (existMeterList.isEmpty()) {
            for (ListSmsMetersResultData meter : filterdMeters) {
                returnMap.put(String.format("%04d", meter.getMeterMngId()), meter.getMeterMngId());
            }
        } else {
            //除外対象メーターが存在する場合は除外して登録
            List<Long> existMeterMngIdList = existMeterList.stream().map(x -> x.getMeterMngId()).collect(Collectors.toList());
            for (ListSmsMetersResultData meter : filterdMeters) {
                if (!existMeterMngIdList.contains(meter.getMeterMngId())) {
                    returnMap.put(String.format("%04d", meter.getMeterMngId()), meter.getMeterMngId());
                }
            }
        }
        //メーター数が0の場合は-を登録し、登録ボタンを非活性にする
        if (returnMap.isEmpty()) {
            returnMap.put("-", 0L);
            this.registDisabledFlg = true;
        }
        this.meterMngId = returnMap.entrySet().stream().findFirst().get().getValue();
        return returnMap;
    }

    /**
     * 各画面の登録ボタン押下時の処理
     * @return
     */
    @Logged
    public String execUpdate() {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:execUpdate():START"));

        //メーター追加処理
        List<UpdateSmsMeterGroupsRequestSet> requestSetList = new ArrayList<>();
        //単独の場合は1件のみ、マルチの場合はリストの内容を登録
        if (this.multiEditFlg) {
            for (GetSmsMeterGroupResultData meter : multiEditMeters) {
                UpdateSmsMeterGroupsRequestSet requestSet = new UpdateSmsMeterGroupsRequestSet();
                requestSet.setMeterMngId(meter.getMeterMngId());
                requestSet.setDevId(meter.getDevId());
                requestSet.setCalcType(BigDecimal.valueOf(this.calcType));
                requestSet.setVersion(meter.getVersion());
                requestSetList.add(requestSet);
            }
        } else {
            UpdateSmsMeterGroupsRequestSet requestSet = new UpdateSmsMeterGroupsRequestSet();
            requestSet.setMeterMngId(this.meterMngId);
            requestSet.setDevId(this.devId);
            requestSet.setCalcType(BigDecimal.valueOf(this.calcType));
            if (version != null) {
                requestSet.setVersion(this.version);
            }
            requestSetList.add(requestSet);
        }

        UpdateSmsMeterGroupsParameter parameter = new UpdateSmsMeterGroupsParameter();
        parameter.setBean("smsCollectSettingMeterGroupUpdateSmsMeterGroupsBean");
        UpdateSmsMeterGroupsRequest request = new UpdateSmsMeterGroupsRequest();
        request.setCorpId(this.buildingInfo.getId().getCorpId());
        request.setBuildingId(this.buildingInfo.getId().getBuildingId());
        request.setMeterGroupId(this.meterGroupId);
        request.setMeterGroupList(requestSetList);
        parameter.setUpdateMeterGroupsRequest(request);
        ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
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

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:execUpdate():END"));

        return meterGroupManagementBean.init(this.buildingInfo,this.meterGroupId);
    }

    /**
     * devIdMapからvalueでkeyを取得する
     * @param id
     * @return
     */
    private String getDevNameById(String id) {
        return devIdMap.entrySet().stream().filter(x -> x.getValue().equals(id)).findFirst().get().getKey();
    }

    public boolean isEditFlg() {
        return editFlg;
    }

    public void setEditFlg(boolean editFlg) {
        this.editFlg = editFlg;
    }

    public boolean isMultiEditFlg() {
        return multiEditFlg;
    }

    public void setMultiEditFlg(boolean multiEditFlg) {
        this.multiEditFlg = multiEditFlg;
    }

    public boolean isRegistDisabledFlg() {
        return registDisabledFlg;
    }

    public void setRegistDisabledFlg(boolean registDisabledFlg) {
        this.registDisabledFlg = registDisabledFlg;
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

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public void setDevIdMap(Map<String, String> devIdMap) {
        this.devIdMap = devIdMap;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public Map<String, Long> getMeterMngIdMap() {
        return meterMngIdMap;
    }

    public void setMeterMngIdMap(Map<String, Long> meterMngIdMap) {
        this.meterMngIdMap = meterMngIdMap;
    }

    public Integer getCalcType() {
        return calcType;
    }

    public void setCalcType(Integer calcType) {
        this.calcType = calcType;
    }

    public Map<String, Integer> getCalcTypeMap() {
        return calcTypeMap;
    }

    public void setCalcTypeMap(Map<String, Integer> calcTypeMap) {
        this.calcTypeMap = calcTypeMap;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }


}
