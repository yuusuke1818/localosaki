package jp.co.osaki.sms.bean.sms.collect.setting.repeater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * 中継装置管理画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingRepeaterManagementBean")
@ConversationScoped
public class RepeaterManagementBean extends SmsConversationBean implements Serializable {

    @Inject
    private EditBean editBean;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    /** シリアライズID */
    private static final long serialVersionUID = 8133091884703291354L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 選択値 接続先(上位連携装置ID). */
    private String devId;

    /** 選択値 接続先名. */
    private String devName;

    /** 表示値 接続先. */
    private String dispDevId;

    /** 表示値 接続先名. */
    private String dispDevName;

    /** 接続先プルダウンMap. */
    private Map<String, String> devIdMap;

    /** 表示内容List. */
    private List<RepeaterInfo> repeaterContents = new ArrayList<>();

    /** 確認メッセージ */
    private String confirmMessage;

    /** 削除確認用チェックボックス */
    private Boolean confirmationCheckBox;

    /** 削除確認用チェックボックス活性/非活性フラグ */
    private Boolean selectCheckBoxDisableFlg;

    /** 新規作成ボタン活性/非活性フラグ */
    private Boolean registButtonDisableFlg;

    /** ハンディ端末フラグ **/
    private Boolean handyFlg;

    /** コマンドフラグ **/
    private static final Map<String, String> COMMAND_FLG = new HashMap<>();

    /** 処理フラグ **/
    private static final Map<String, String> SRV_ENT = new HashMap<>();


    static {
        //コマンドフラグ初期化
        COMMAND_FLG.put(SmsConstants.MREPEATER_COMMAND_FLG.REGIST.getVal(), "登録");
        COMMAND_FLG.put(SmsConstants.MREPEATER_COMMAND_FLG.DELETE.getVal(), "削除");

        //処理フラグ初期化
        SRV_ENT.put(SmsConstants.MREPEATER_SRV_ENT.WAIT.getVal(), "要求中");
        SRV_ENT.put(SmsConstants.MREPEATER_SRV_ENT.ERR.getVal(), "失敗");
    }

    @Override
    public String init() {
        conversationStart();

        // 遷移先
        return "repeaterManagement";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.confirmMessage = "";
        this.confirmationCheckBox = false;
        this.selectCheckBoxDisableFlg = true;
        this.registButtonDisableFlg = false;

        this.devIdMap = toolsPullDownList.getDevPrm(buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));

        if(!this.devIdMap.isEmpty()) {
            //初期値として先頭の装置の情報を取得する
            this.devId = devIdMap.entrySet().stream().findFirst().get().getValue();
            this.devName = devIdMap.entrySet().stream().findFirst().get().getKey();
            this.dispDevId = devIdMap.entrySet().stream().findFirst().get().getValue();
            this.dispDevName = devIdMap.entrySet().stream().findFirst().get().getKey();

            //対象装置がハンディ端末か確認 ハンディの場合は一覧を表示しない
            if (devId.indexOf("MH") == 0) {
                this.handyFlg = true;
                this.registButtonDisableFlg = true;
            } else {
                this.handyFlg = false;
                ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();
                parameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
                parameter.setDevId(this.devId);

                List<ListSmsRepeatersResultData> repeaters = getListRepeaters(parameter).getResult().getRepeaterList();
                if (repeaters.size() >= SmsConstants.MAX_REPEATER_COUNT) {
                    //登録件数がすでに99件ある場合は新規登録ボタン非活性
                    this.registButtonDisableFlg = true;
                }
                //表示用List作成
                repeaterContents = createRepeaterContents(repeaters);
            }
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }

        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:init():END"));

        return init();
    }

    /**
     * 初期処理(登録・編集後遷移).
     *
     * @param buildingInfo 建物・テナント情報
     * @param devId 装置ID
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, String devId) {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.confirmMessage = "";
        this.confirmationCheckBox = false;
        this.selectCheckBoxDisableFlg = true;
        this.registButtonDisableFlg = false;
        this.handyFlg = false;

        this.devIdMap = toolsPullDownList.getDevPrm(buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));

        if(!this.devIdMap.isEmpty()) {
            //初期値として先頭の装置の情報を取得する
            this.devId = devId;
            this.devName = devIdMap.entrySet().stream().filter(x -> devId.equals(x.getValue())).findFirst().get().getKey();
            this.dispDevId = this.devId;
            this.dispDevName = this.devName;
            ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();
            parameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
            parameter.setDevId(this.devId);

            List<ListSmsRepeatersResultData> repeaters = getListRepeaters(parameter).getResult().getRepeaterList();
            if (repeaters.size() >= SmsConstants.MAX_REPEATER_COUNT) {
                //登録件数がすでに99件ある場合は新規登録ボタン非活性
                this.registButtonDisableFlg = true;
            }
            //表示用List作成
            repeaterContents = createRepeaterContents(repeaters);
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:init():END"));
        return init();
    }

    /**
     * 中継装置一覧取得処理
     * @return
     */
    private ListSmsRepeatersResponse getListRepeaters(ListSmsRepeatersParameter parameter) {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:getListRepeaters():START"));
        ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsRepeatersResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:getListRepeaters():END"));
        return response;
    }

    /**
     * 表示更新ボタン押下時の処理
     * @return
     */
    @Logged
    public String execSearch() {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:execSearch():START"));
        this.registButtonDisableFlg = false;
        if (!devIdMap.isEmpty()) {
            if (devId.indexOf("MH") == 0) {
                this.handyFlg = true;
                this.registButtonDisableFlg = true;
            } else {
                this.handyFlg = false;
                this.registButtonDisableFlg = false;
                ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();
                parameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
                parameter.setDevId(this.devId);
                List<ListSmsRepeatersResultData> repeaters = getListRepeaters(parameter).getResult().getRepeaterList();

                //表示用List作成
                repeaterContents = createRepeaterContents(repeaters);
                if (repeaters.size() >= SmsConstants.MAX_REPEATER_COUNT) {
                    //登録件数がすでに99件ある場合は新規登録ボタン非活性
                    this.registButtonDisableFlg = true;
                }

                //devName更新
                this.setDevName(getDevNameFromMap(this.devId));

                this.dispDevId = this.devId;
                this.dispDevName = getDevNameFromMap(this.devId);
            }

        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:execSearch():END"));

        return "repeaterManagement";
    }

    /**
     * 中継装置一覧表示用リストへの詰め替え処理
     * @return
     */
    private List<RepeaterInfo> createRepeaterContents(List<ListSmsRepeatersResultData> repeaters) {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:createRepeaterContents():START"));
        List<RepeaterInfo> repeaterList = new ArrayList<>();
        for (ListSmsRepeatersResultData repeater: repeaters) {
            RepeaterInfo content = new RepeaterInfo();
            //チェックボックスON/OFF
            content.setCheckBox(false);
            //ステータス表示文字列
            StringBuilder sb = new StringBuilder();
            if (COMMAND_FLG.containsKey(repeater.getCommandFlg())) {
                sb.append(COMMAND_FLG.get(repeater.getCommandFlg()));
            }
            if (SRV_ENT.containsKey(repeater.getSrvEnt())) {
                sb.append(SRV_ENT.get(repeater.getSrvEnt()));
            }
            content.setStatus(sb.toString());
            //中継装置情報
            content.setRepeater(repeater);
            repeaterList.add(content);
        }

        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:createRepeaterContents():END"));

        return repeaterList;
    }

    /**
     * 登録ボタン押下時の処理
     * @return
     */
    @Logged
    public String execRegist() {
        return editBean.init(this.buildingInfo, this.dispDevId, this.dispDevName);
    }

    /**
     * 設定変更ボタン押下時の処理
     * @return
     */
    @Logged
    public String execEdit(ListSmsRepeatersResultData resultData) {
        return editBean.init(this.buildingInfo, resultData.getDevId(), this.dispDevName, resultData.getRepeaterMngId());
    }

    /**
     * 削除ボタン押下時のメッセージ変更処理
     * @return
     */
    @Logged
    public void setDeleteMessage() {
        this.confirmMessage = "チェックされている中継装置を削除<br />します。よろしいですか？";
    }

    /**
     * 削除ボタン押下時の処理
     * @return
     */
    @Logged
    public String execDelete() {
        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:execDelete():START"));
        //更新処理リクエスト作成
        List<UpdateSmsRepeaterRequestSet> requestSetList = new ArrayList<>();
        for (RepeaterInfo content: repeaterContents) {
            if (content.getCheckBox()) {
                UpdateSmsRepeaterRequestSet requestSet = new UpdateSmsRepeaterRequestSet();
                requestSet.setRepeaterMngId(content.getRepeater().getRepeaterMngId());
                requestSet.setCommandFlg("2");
                requestSet.setSrvEnt("1");;
                requestSet.setRepeaterId(content.getRepeater().getRepeaterId());
                requestSet.setMemo(content.getRepeater().getMemo());
                requestSet.setVersion(content.getRepeater().getVersion());

                requestSetList.add(requestSet);
            }
        }
        UpdateSmsRepeaterParameter parameter = new UpdateSmsRepeaterParameter();
        parameter.setBean("smsCollectSettingRepeaterUpdateSmsRepeaterBean");
        UpdateSmsRepeaterRequest request = new UpdateSmsRepeaterRequest();
        request.setDevId(this.dispDevId);
        request.setRepeaterList(requestSetList);
        parameter.setUpdateSmsRepeaterRequest(request);
        ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
        //更新処理API実行
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

        //表示用List作成
        ListSmsRepeatersParameter listParameter = new ListSmsRepeatersParameter();
        listParameter.setBean("smsCollectSettingRepeaterListSmsRepeatersBean");
        listParameter.setDevId(this.dispDevId);

        List<ListSmsRepeatersResultData> repeaters = getListRepeaters(listParameter).getResult().getRepeaterList();
        repeaterContents = createRepeaterContents(repeaters);

        eventLogger.debug(RepeaterManagementBean.class.getPackage().getName().concat("RepeaterManagementBean:execDelete():END"));

        return "repeaterManagement";
    }

    public String getDevNameFromMap(String value) {
        return devIdMap.entrySet().stream().filter(x -> x.getValue().equals(value)).findFirst().get().getKey();
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public List<RepeaterInfo> getRepeaterContents() {
        return repeaterContents;
    }

    public void setRepeaterContents(List<RepeaterInfo> RepeaterContents) {
        this.repeaterContents = RepeaterContents;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public Boolean getConfirmationCheckBox() {
        return confirmationCheckBox;
    }

    public void setConfirmationCheckBox(Boolean confirmationCheckBox) {
        this.confirmationCheckBox = confirmationCheckBox;
    }

    public Boolean getSelectCheckBoxDisableFlg() {
        return selectCheckBoxDisableFlg;
    }

    public void setSelectCheckBoxDisableFlg(Boolean selectCheckBoxDisableFlg) {
        this.selectCheckBoxDisableFlg = selectCheckBoxDisableFlg;
    }

    public Boolean getRegistButtonDisableFlg() {
        return registButtonDisableFlg;
    }

    public void setRegistButtonDisableFlg(Boolean registButtonDisableFlg) {
        this.registButtonDisableFlg = registButtonDisableFlg;
    }

    public Boolean getHandyFlg() {
        return handyFlg;
    }

    public void setHandyFlg(Boolean handyFlg) {
        this.handyFlg = handyFlg;
    }
}
