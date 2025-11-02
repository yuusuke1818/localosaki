package jp.co.osaki.sms.bean.sms.collect.setting.concentrator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.ListSmsConcentratorsParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.UpdateSmsConcentratorParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.concentrator.ListSmsConcentratorsResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * コンセントレーター登録・変更画面.
 *
 * @author maruta.y
 */
@Named("smsCollectSettingConcentratorManagementEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    @Inject
    private OsolConfigs osolConfigs;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private ConcentratorManagementBean concentratorManagementBean;

    /** シリアライズID */
    private static final long serialVersionUID = -4430186358261960323L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 編集フラグ. */
    private boolean editFlg = false;

    /** 選択値 接続先(上位連携装置ID). */
    private String devId;

    /** 選択値 接続先名. */
    private String devName;

    /** 管理番号プルダウンMap. */
    private Map<String, Long> concentIdMap = new LinkedHashMap<>();

    /** 選択値 管理番号. */
    private Long concentId;

    /** 入力値 コンセントレーター名. */
    private String consentratorName;

    /** 入力値 IPアドレス. */
    private String ipAddr;

    /** 更新対象IPアドレス. */
    private String updateTargeIipAddr;

    /** ゼロ詰め IPアドレス. */
    private String formedIpAddr;

    /** コメント */
    private String comment;

    /** バージョン */
    private Integer version;

    /** 確認メッセージ */
    private String confirmMessage;

    /** コンセントレーターリスト */
    private List<ListSmsConcentratorsResultData> concentratorsList = new ArrayList<>();

    /** エラーコンポーネントリスト */
    private List<String> invalidComponent;


    @Override
    public String init() {
        conversationStart();

        // 遷移先
        return "concentratorManagementEdit";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    public String init(ListInfo buildingInfo, String devId, String devName, Long concentId) {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:init():START"));
        this.buildingInfo = buildingInfo;
        this.editFlg = true;
        initInvalidComponent();

        //取得処理
        this.devId = devId;
        this.devName = devName;
        this.concentId = concentId;
        this.confirmMessage = "コンセントレーターの設定変更をおこないます。<br />よろしいですか？";

        //対象装置の登録済みデータを取得
        ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();
        parameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
        parameter.setDevId(devId);
        ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsConcentratorsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        concentratorsList = response.getResult().getConcentratorList();

        Optional<ListSmsConcentratorsResultData> concentrator = concentratorsList.stream()
                .filter(x -> x.getDevId().equals(this.devId))
                .filter(x -> x.getConcentId().equals(this.concentId))
                .findFirst();
        if (concentrator.isPresent()) {
            this.consentratorName = concentrator.get().getName();
            this.ipAddr = concentrator.get().getIpAddr();
            this.updateTargeIipAddr = concentrator.get().getIpAddr();
            this.comment = concentrator.get().getMemo();
            this.version = concentrator.get().getVersion();
        } else {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.notfound"));
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
        this.confirmMessage = "コンセントレーターの新規登録をおこないます。<br />よろしいですか？";
        initInvalidComponent();

        //入力フォームリセット
        this.consentratorName = "";
        this.ipAddr = "";
        this.updateTargeIipAddr = "";
        this.comment = "";
        this.version = null;

        //対象装置の登録済みデータを取得
        ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();
        parameter.setBean("smsCollectSettingConcentratorListSmsConcentratorsBean");
        parameter.setDevId(devId);
        ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsConcentratorsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        concentratorsList = response.getResult().getConcentratorList();

        //空いているコンセントレーターIDのみを表示する
        List<Long> concentIdList = concentratorsList.stream().map(x -> x.getConcentId()).collect(Collectors.toList());
        concentIdMap.clear();
        for (long targetId = 1; targetId <= SmsConstants.MAX_CONCENT_COUNT; targetId++) {
            if (!concentIdList.contains(targetId)) {
                concentIdMap.put(String.format("%02d", targetId), targetId);
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

        //コンセントレーター追加処理
        List<UpdateSmsConcentratorRequestSet> requestSetList = new ArrayList<>();
        UpdateSmsConcentratorRequestSet requestSet = new UpdateSmsConcentratorRequestSet();
        requestSet.setConcentId(this.concentId);
        requestSet.setCommandFlg(SmsConstants.MCONCENTRATOR_COMMAND_FLG.REGIST.getVal());
        requestSet.setSrvEnt(SmsConstants.MCONCENTRATOR_SRV_ENT.WAIT.getVal());
        requestSet.setIpAddr(this.formedIpAddr);
        requestSet.setName(this.consentratorName);
        requestSet.setMemo(this.comment);
        if (version != null) {
            requestSet.setVersion(this.version);
        }
        requestSetList.add(requestSet);

        UpdateSmsConcentratorParameter parameter = new UpdateSmsConcentratorParameter();
        parameter.setBean("smsCollectSettingConcentratorUpdateSmsConcentratorBean");
        UpdateSmsConcentratorRequest request = new UpdateSmsConcentratorRequest();
        request.setDevId(this.devId);
        request.setConcentratorList(requestSetList);
        parameter.setUpdateSmsConcentratorRequest(request);
        ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
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

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:execUpdate():END"));

        return concentratorManagementBean.init(this.buildingInfo, this.devId);
    }

    private boolean validation() {
        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:validation():START"));
        initInvalidComponent();
        boolean error = false;
        //コンセントレーター名未入力
        if (CheckUtility.isNullOrEmpty(this.consentratorName)) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.concentratorName"));
            invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:concentrator_name");
            error = true;
        }

        //IPアドレス未入力
        if (CheckUtility.isNullOrEmpty(this.ipAddr)) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.ipAddr"));
            invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:ip_addr");
            return false;
        }

        //IPアドレス文字種エラー
        Pattern p = Pattern.compile("[^0-9.]+");
        if (p.matcher(this.ipAddr).find()) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.ipAddr.charType"));
            invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:ip_addr");
            return false;
        }

        //IPアドレスフォーマットエラー
        List<String> octets = Arrays.asList(this.ipAddr.split("\\."));
        if (octets.size() != 4) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.ipAddr.format"));
            invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:ip_addr");
            return false;
        }

        for (String octet : octets) {
            //IPアドレス範囲エラー
            if (CheckUtility.isNullOrEmpty(octet)
                    || Integer.valueOf(octet).compareTo(0) < 0
                    || Integer.valueOf(octet).compareTo(255) > 0) {
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.ipAddr.range"));
                invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:ip_addr");
                return false;
            }
        }

        //IPアドレスの各オクテットを3桁0詰め
        this.formedIpAddr = octets.stream().map(x -> String.format("%03d", Integer.valueOf(x))).collect(Collectors.joining("."));
        //IPアドレス重複エラー
        if (concentratorsList.stream().anyMatch(x -> this.formedIpAddr.equals(x.getIpAddr()))) {
            //更新時にIPアドレスに変更がない場合はエラーとしない
            if (!this.updateTargeIipAddr.equals(this.formedIpAddr)) {
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingConcentratorManagementBean.error.ipAddr.duplicate"));
                invalidComponent.add("smsCollectSettingConcentratorManagementEditBean:ip_addr");
                error = true;
            }
        }

        eventLogger.debug(EditBean.class.getPackage().getName().concat("EditBean:validation():END"));

        //エラーがあれば失敗(false)、エラーなしでOK(true)
        return !error;

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

    public Map<String, Long> getConcentIdMap() {
        return concentIdMap;
    }

    public void setConcentIdMap(Map<String, Long> concentIdMap) {
        this.concentIdMap = concentIdMap;
    }

    public Long getConcentId() {
        return concentId;
    }

    public void setConcentId(Long concentId) {
        this.concentId = concentId;
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
