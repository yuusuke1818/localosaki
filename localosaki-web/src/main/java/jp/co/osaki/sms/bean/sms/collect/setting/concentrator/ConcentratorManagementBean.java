package jp.co.osaki.sms.bean.sms.collect.setting.concentrator;

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
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.ListSmsConcentratorsParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.UpdateSmsConcentratorParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.concentrator.ListSmsConcentratorsResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * コンセントレーター管理画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingConcentratorManagementBean")
@ConversationScoped
public class ConcentratorManagementBean extends SmsConversationBean implements Serializable {

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
    private static final long serialVersionUID = 411682100609340548L;

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

    /** 入力値 コンセントレーター名. */
    private String consentratorName;

    /** 入力値 IPアドレス. */
    private String ipAddr;

    /** コメント */
    private String comment;

    /** 表示内容List. */
    private List<ConcentratorInfo> concentratorContents = new ArrayList<>();

    /** 確認メッセージ */
    private String confirmMessage;

    /** 初期化・削除確認用チェックボックス */
    private Boolean confirmationCheckBox;

    /** 初期化・削除確認用チェックボックス活性/非活性フラグ */
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
        COMMAND_FLG.put(SmsConstants.MCONCENTRATOR_COMMAND_FLG.REGIST.getVal(), "登録");
        COMMAND_FLG.put(SmsConstants.MCONCENTRATOR_COMMAND_FLG.DELETE.getVal(), "削除");
        COMMAND_FLG.put(SmsConstants.MCONCENTRATOR_COMMAND_FLG.RESET.getVal(), "初期化");

        //処理フラグ初期化
        SRV_ENT.put(SmsConstants.MCONCENTRATOR_SRV_ENT.WAIT.getVal(), "要求中");
        SRV_ENT.put(SmsConstants.MCONCENTRATOR_SRV_ENT.ERR.getVal(), "失敗");
    }

    @Override
    public String init() {
        conversationStart();

        // 遷移先
        return "concentratorManagement";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:init():START"));
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
            if (devId.indexOf(SmsConstants.DEVICE_KIND.HANDY.getVal()) == 0) {
                this.handyFlg = true;
                this.registButtonDisableFlg = true;
            } else {
                this.handyFlg = false;
                ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();
                parameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
                parameter.setDevId(this.devId);

                List<ListSmsConcentratorsResultData> concentrators = getListConcentrators(parameter).getResult().getConcentratorList();
                if (concentrators.size() >= SmsConstants.MAX_CONCENT_COUNT) {
                    //登録件数がすでに16件ある場合は新規登録ボタン非活性
                    this.registButtonDisableFlg = true;
                }
                //表示用List作成
                concentratorContents = createConcentratorContents(concentrators);
            }

        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }

        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:init():END"));

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
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:init():START"));
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
            this.dispDevName =  this.devName;
            ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();
            parameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
            parameter.setDevId(this.devId);

            List<ListSmsConcentratorsResultData> concentrators = getListConcentrators(parameter).getResult().getConcentratorList();
            if (concentrators.size() >= SmsConstants.MAX_CONCENT_COUNT) {
                //登録件数がすでに16件ある場合は新規登録ボタン非活性
                this.registButtonDisableFlg = true;
            }
            //表示用List作成
            concentratorContents = createConcentratorContents(concentrators);
        } else {
            //装置が存在しない場合は新規登録ボタン非活性
            this.registButtonDisableFlg = true;
        }
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:init():END"));
        return init();
    }

    /**
     * コンセントレーター一覧取得処理
     * @return
     */
    ListSmsConcentratorsResponse getListConcentrators(ListSmsConcentratorsParameter parameter) {
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:getListConcentrators():START"));
        ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsConcentratorsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:getListConcentrators():END"));
        return response;
    }

    /**
     * 表示更新ボタン押下時の処理
     * @return
     */
    @Logged
    public String execSearch() {
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:execSearch():START"));
        this.registButtonDisableFlg = false;
        if (!devIdMap.isEmpty()) {
            if (devId.indexOf(SmsConstants.DEVICE_KIND.HANDY.getVal()) == 0) {
                this.handyFlg = true;
                this.registButtonDisableFlg = true;
            } else {
                this.handyFlg = false;
                this.registButtonDisableFlg = false;
                ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();
                parameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
                parameter.setDevId(this.devId);
                List<ListSmsConcentratorsResultData> concentrators = getListConcentrators(parameter).getResult().getConcentratorList();

                //表示用List作成
                concentratorContents = createConcentratorContents(concentrators);
                if (concentrators.size() >= SmsConstants.MAX_CONCENT_COUNT) {
                    //登録件数がすでに16件ある場合は新規登録ボタン非活性
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

        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:execSearch():END"));

        return "concentratorManagement";
    }

    /**
     * コンセントレーター一覧表示用リストへの詰め替え処理
     * @return
     */
    private List<ConcentratorInfo> createConcentratorContents(List<ListSmsConcentratorsResultData> concentrators) {
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:createConcentratorContents():START"));
        List<ConcentratorInfo> concentratorList = new ArrayList<>();
        for (ListSmsConcentratorsResultData concentrator: concentrators) {
            ConcentratorInfo content = new ConcentratorInfo();
            //チェックボックスON/OFF
            content.setCheckBox(false);
            //ステータス表示文字列
            StringBuilder sb = new StringBuilder();
            if (COMMAND_FLG.containsKey(concentrator.getCommandFlg())) {
                sb.append(COMMAND_FLG.get(concentrator.getCommandFlg()));
            }
            if (SRV_ENT.containsKey(concentrator.getSrvEnt())) {
                sb.append(SRV_ENT.get(concentrator.getSrvEnt()));
            }
            content.setStatus(sb.toString());
            //コンセントレーター情報
            content.setConcentrator(concentrator);
            concentratorList.add(content);
        }

        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:createConcentratorContents():END"));

        return concentratorList;
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
    public String execEdit(ListSmsConcentratorsResultData resultData) {
        return editBean.init(this.buildingInfo, resultData.getDevId(), this.dispDevName, resultData.getConcentId());
    }

    /**
     * 初期化ボタン押下時のメッセージ変更処理
     * @return
     */
    @Logged
    public void setResetMessage() {
        this.confirmMessage = "チェックされているコンセントレーターを初期化<br />します。よろしいですか？";
    }

    /**
     * 削除ボタン押下時のメッセージ変更処理
     * @return
     */
    @Logged
    public void setDeleteMessage() {
        this.confirmMessage = "チェックされているコンセントレーターを削除<br />します。よろしいですか？";
    }

    /**
     * 初期化ボタン押下時の処理
     * @return
     */
    @Logged
    public String execReset() {
        return execUpdate(SmsConstants.MCONCENTRATOR_COMMAND_FLG.RESET);
    }

    /**
     * 削除ボタン押下時の処理
     * @return
     */
    @Logged
    public String execDelete() {
        return execUpdate(SmsConstants.MCONCENTRATOR_COMMAND_FLG.DELETE);
    }

    /**
     * 初期化/削除ボタン押下時の処理
     * @return
     */
    public String execUpdate(SmsConstants.MCONCENTRATOR_COMMAND_FLG cmdFlg) {
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:execDelete():START"));
        //更新処理リクエスト作成
        List<UpdateSmsConcentratorRequestSet> requestSetList = new ArrayList<>();
        for (ConcentratorInfo content: concentratorContents) {
            if (content.getCheckBox()) {
                UpdateSmsConcentratorRequestSet requestSet = new UpdateSmsConcentratorRequestSet();
                requestSet.setConcentId(content.getConcentrator().getConcentId());
                requestSet.setCommandFlg(cmdFlg.getVal());
                requestSet.setSrvEnt(SmsConstants.MCONCENTRATOR_SRV_ENT.WAIT.getVal());
                requestSet.setIpAddr(content.getConcentrator().getIpAddr());
                requestSet.setName(content.getConcentrator().getName());
                requestSet.setMemo(content.getConcentrator().getMemo());
                requestSet.setVersion(content.getConcentrator().getVersion());

                requestSetList.add(requestSet);
            }
        }
        UpdateSmsConcentratorParameter parameter = new UpdateSmsConcentratorParameter();
        parameter.setBean("smsCollectSettingConcentratorUpdateSmsConcentratorBean");
        UpdateSmsConcentratorRequest request = new UpdateSmsConcentratorRequest();
        request.setDevId(this.dispDevId);
        request.setConcentratorList(requestSetList);
        parameter.setUpdateSmsConcentratorRequest(request);
        ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
        //更新処理API実行
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsConcentratorsResponse) gateway.osolApiPost(
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
        ListSmsConcentratorsParameter listParameter = new ListSmsConcentratorsParameter();
        listParameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
        listParameter.setDevId(this.dispDevId);

        List<ListSmsConcentratorsResultData> concentrators = getListConcentrators(listParameter).getResult().getConcentratorList();
        concentratorContents = createConcentratorContents(concentrators);

        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName().concat("ConcentratorManagementBean:execDelete():END"));

        return "concentratorManagement";
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

    public String getConsentratorName() {
        return consentratorName;
    }

    public void setConsentratorName(String consentratorName) {
        this.consentratorName = consentratorName;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ConcentratorInfo> getConcentratorContents() {
        return concentratorContents;
    }

    public void setConcentratorContents(List<ConcentratorInfo> concentratorContents) {
        this.concentratorContents = concentratorContents;
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
