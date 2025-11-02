package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSettingCollectionParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSettingCollectionParameter;
import jp.co.osaki.osol.api.request.sms.meterreading.UpdateSettingCollectionRequest;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSettingCollectionResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSettingCollectionResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.CommandResultData;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.bean.sms.collect.TopBean;

/**
 * 設定一括収集画面.
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingSettingCollectionBean")
@ConversationScoped
public class SettingCollectionBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -2558424330047093212L;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    private List<SettingCollection> resultList;

    private static final String BLANK = "";

    // 収集コマンド・リスト
    private static final List<String> COMMAND_LIST = Arrays.asList(DeviceCtrlConstants.concentList, DeviceCtrlConstants.meterList, DeviceCtrlConstants.getMeterinfo, DeviceCtrlConstants.getMeterctrl, DeviceCtrlConstants.getAutoinsp, DeviceCtrlConstants.getSpitconf, DeviceCtrlConstants.repeaterList);
    // 収集コマンド名リスト
    private static final List<String> COMMAND_NAME_LIST = Arrays.asList("コンセントレーター一覧要求", "メーター一覧要求", "メーター情報個別要求", "メーター負荷制限状態確認", "自動検針月日時要求", "スマートパルス入力端末設定内容要求", "中継装置（無線）一覧要求");
    // 収集コマンド毎・選択活性フラグ ※装置IDがコンセントレーター(先頭2文字 "XR" or "XS")の場合の選択欄を非活性
    private static final List<Boolean> COMMAND_DISABLED_WHEN_CONCENTRATOR_LIST = Arrays.asList(true, false, false, false, true, false, false);
    // 処理名称マップ
    private static final Map<String, String> SRV_ENT_NAME_MAP = new HashMap<String, String>(){
        private static final long serialVersionUID = 1L;
        {
            put("", "正常終了");
            put("1", "要求中");
            put("2", "電文送信中");
            put("5", "要求中");
            put("8", "データ書出し中");
            put("9", "収集失敗"); // エラー色で表示
        }
    };
    private static final Map<String, Integer> SRV_ENT_PRIORITY_MAP = new HashMap<String, Integer>(){
        private static final long serialVersionUID = 1L;
        {
            put("" , 0);   // 正常終了
            put("1", 1);   // 要求中
            put("2", 3);   // 電文送信中
            put("5", 1);   // 要求中
            put("8", 2);   // データ書出し中
            put("9", 9);   // エラー色で表示
        }
    };
    // 処理エラー  ※エラー色で表示する処理フラグ値
    private static final String SRV_ENT_ERROR = "9"; // 収集失敗

    /**
     * この内容で登録します。よろしいですか？
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:init():START"));

        // 検索・表示
        executeSearch(topBean.getTopBeanProperty().getListInfo());

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:init():END"));
        return "smsCollectSettingMeterreadingSettingCollection";
    }

    /**
     * 「状態表示更新」ボタン押下時の処理
     * @return
     */
    @Logged
    public String refresh() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:refresh():START"));

        // 検索・表示
        executeSearch(topBean.getTopBeanProperty().getListInfo());

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:refresh():END"));
        return "";
    }

    /**
     * チェックボックス全選択/解除
     * @return 画面Beanページ
     */
    @Logged
    public String chkBoxAll(){
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:chkBoxAll():START"));

        List<SettingCollection> list = this.resultList; // 全頁取得
        boolean allDischeckFlg = true;

        // チェックついていないチェックボックスをチェックする
        for (SettingCollection row : list) {
            if (!row.getCheckbox()) {
                row.setCheckbox(true);
                allDischeckFlg = false;
            }
        }

        // チェックついていないチェックボックスが存在しなかった場合、全てチェックを外す
        if (allDischeckFlg) {
            for (SettingCollection row : list) {
                row.setCheckbox(false);
            }
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:chkBoxAll():END"));
        return "";
    }

    /**
     * 「一括収集」ボタン押下時の処理
     * @return
     */
    @Logged
    public String allCollect() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:allCollect():START"));

        // 選択行の 収集コマンド 一覧を取得
        List<String> commandList = this.resultList.stream().filter(row -> row.getCheckbox()).map(row -> row.getCommand()).collect(Collectors.toList());

        if (!validate(commandList)) {
            // 入力チェック・エラー
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:regist():END (validate error)"));
            return "";
        }

        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        UpdateSettingCollectionResponse response = new UpdateSettingCollectionResponse();
        UpdateSettingCollectionParameter parameter = new UpdateSettingCollectionParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSettingCollectionBean");
        parameter.setCorpId(listInfo.getCorpId());    // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID
        parameter.setCommands(new UpdateSettingCollectionRequest(commandList)); // 収集コマンド 一覧

        response = (UpdateSettingCollectionResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);


        if (OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            // 正常
            // 警告があるか？ → 警告を表示
            if (response.getResult() != null && response.getResult().getWarningCommandList() != null
                    && !response.getResult().getWarningCommandList().isEmpty()) {
                for (String warnCommand : response.getResult().getWarningCommandList()) {
                    // 「ＸＸＸＸＸはメータ一覧要求が正常終了してから実行して下さい。」メッセージを表示
                    addWarningMessage(
                            beanMessages.getMessageFormat("smsCollectSettingMeterreadingSettingCollectionBean.warn.warningCommand",
                            new String[] { getCommandName(warnCommand) }));
                }

                addWarningMessage(beanMessages.getMessage("smsCollectSettingMeterreadingSettingCollectionBean.warn.conditionalSuccess"));
            }

            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        } else {
            addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }

        // 検索結果を表示
        dispDbData(
                response.getResult() == null ? null : response.getResult().getDevIdList(),
                response.getResult() == null ? null : response.getResult().getList());

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingSettingCollectionBean:allCollect():END"));
        return "";
    }

    /**
     * 収集コマンド名(和名)を返す
     * @param command 収集コマンド
     * @return 収集コマンド名(和名) ※和名が存在しない 収集コマンド がセットされたときは、収集コマンド を返す
     */
    private String getCommandName(String command) {
        String commandName = command;
        for (int idx = 0; idx < COMMAND_LIST.size(); idx++) {
            if (COMMAND_LIST.get(idx).equals(command)) {
                commandName = COMMAND_NAME_LIST.get(idx);
                break;
            }
        }
        return commandName;
    }

    /**
     * 入力チェック.
     * @return true:入力OK false:入力NG
     */
    private boolean validate(List<String> commandList) {
        boolean isChkOk = true;

        // 選択行チェック
        if (commandList.isEmpty()) {
            // 1行も選択されていない
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingSettingCollectionBean.error.unselected"));
            isChkOk = false;
        }

        return isChkOk;
    }

    /**
     * 検索実処理
     * @param listInfo 建物情報
     */
    private ListSettingCollectionResponse executeSearch(ListInfo listInfo) {
        ListSettingCollectionResponse response = new ListSettingCollectionResponse();
        ListSettingCollectionParameter parameter = new ListSettingCollectionParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ListSettingCollectionBean");

        parameter.setCorpId(listInfo.getCorpId());    // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID

        response = (ListSettingCollectionResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        // 検索結果チェック
        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }

        // 検索結果を表示
        dispDbData(
                response.getResult() == null ? null : response.getResult().getDevIdList(),
                response.getResult() == null ? null : response.getResult().getList());

        return response;
    }

    /**
     * 検索結果を表示
     * @param devIdList 装置IDリスト
     * @param srvEntList 検索結果 ※ソート順: devId, command
     */
    private void dispDbData(List<String> devIdList, List<CommandResultData> srvEntList) {

        this.resultList = new ArrayList<SettingCollection>();

        if (devIdList == null || devIdList.isEmpty()) {
            // 処理対象の装置IDが取得できない
            // (メッセージ)処理対象の装置IDが存在しない
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingSettingCollectionBean.error.devIdNotFound"));
            // コマンド名だけ表示する
            COMMAND_NAME_LIST.stream().forEach(row -> { this.resultList.add(new SettingCollection(false, true, null, row, BLANK, false)); });
            return;
        }

        // コンセントレータのみチェック
        boolean isConcentratorOnly = true; // コンセントレータのみフラグ  ※装置ID の先頭2文字 "XR" or "XS" のみ
        for (String devId : devIdList) {
            // コンセントレータ以外かチェック
            if (!devId.startsWith(SmsConstants.DEVICE_KIND.CONCENTRATER_XR.getVal())
             && !devId.startsWith(SmsConstants.DEVICE_KIND.CONCENTRATER_XS.getVal())) {
                isConcentratorOnly = false;
            }
        }

        // コマンド毎に処理フラグを収集
        Map<String, String> srvEntMap = new HashMap<String, String>(); // 操作状態Map＜command, 操作状態＞  ※null: ＤＢにレコードがない
        if (srvEntList != null) {
            for (CommandResultData row : srvEntList) {
                String command = row.getCommand();
                String srvEnt = row.getSrvEnt() == null ? "" : row.getSrvEnt();
                // 優先度チェック → より優先度が高い 処理フラグ に差し替える
                if (nvl(SRV_ENT_PRIORITY_MAP.get(srvEnt)) >= nvl(SRV_ENT_PRIORITY_MAP.get(srvEntMap.get(command)))) {
                    srvEntMap.put(command, srvEnt);
                }
            }
        }

        // 表示内容を編集して resultList にセット
        for (int idx = 0; idx < COMMAND_LIST.size(); idx++) {
            String command = COMMAND_LIST.get(idx);
            String srvEnt = srvEntMap.get(command);
            this.resultList.add(new SettingCollection(
                false,                          // 選択
                isConcentratorOnly ? COMMAND_DISABLED_WHEN_CONCENTRATOR_LIST.get(idx) : false, // 選択欄 非活性フラグ (コンセントレータ以外が含まれる場合は全て活性)
                command,                        // 収集コマンド
                COMMAND_NAME_LIST.get(idx),   // 収集コマンド名
                srvEnt == null ? BLANK : SRV_ENT_NAME_MAP.get(srvEnt) == null ? srvEnt : SRV_ENT_NAME_MAP.get(srvEnt), // 操作状態
                SRV_ENT_ERROR.equals(srvEnt)    // 操作状態欄文字色(true:赤 false:青)
            ));;
        }
    }

    /**
     * null は 0 にする
     * @param value 値
     * @return 処理後
     */
    private int nvl(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    public List<SettingCollection> getResultList() {
        return this.resultList;
    }

    public void setResultList(List<SettingCollection> resultList) {
        this.resultList = resultList;
    }

}
