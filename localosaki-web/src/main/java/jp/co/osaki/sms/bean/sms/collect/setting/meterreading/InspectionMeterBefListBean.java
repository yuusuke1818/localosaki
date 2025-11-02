package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.MeterDataFilterDao;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.DeleteSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsCheckMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.request.sms.meterreading.DeleteSmsInspectionMeterBefRequest;
import jp.co.osaki.osol.api.request.sms.meterreading.DeleteSmsInspectionMeterBefRequestSet;
import jp.co.osaki.osol.api.request.sms.meterreading.ListSmsCheckMeterRequest;
import jp.co.osaki.osol.api.response.sms.meterreading.DeleteSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsCheckMeterResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterBefResultData;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * 確定前検針データ一覧表示画面
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingInspectionMeterBefListBean")
@ConversationScoped
public class InspectionMeterBefListBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 1224998812647643465L;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    @Inject
    private InspectionMeterBefBean inspectionMeterBefBean;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    /**
     * メーターフィルター
     */
    @EJB
    private MeterDataFilterDao meterDataFilterDao;

    /** 接続先マップ. */
    private Map<String, String> devNameMap;
    /** データ表示種別マップ. */
    private Map<String, String> dispDataTypeMap;

    // データ表示種別：最新データ表示
    private static final String DISP_DATA_TYPE_LATEST = "0";
    // データ表示種別：全データ表示
    private static final String DISP_DATA_TYPE_ALL = "1";

    /** 接続先. */
    private String devId;
    /** データ表示種別. */
    private String dispDataType;

    /** (再検索処理用。検索ボタン押下時の)接続先. */
    private String searchDevId;
    /** (再検索処理用。検索ボタン押下時の)データ表示種別. */
    private String searchDispDataType;

    // ページング処理
    @Inject
    private InspectionMeterBefPaging smsCollectSettingMeterreadingInspectionMeterBefPaging;

    /** 確定登録ボタン押下時に、メーター登録用マスタと比較して、登録すべきメーター管理番号が登録対象になっていない（未計測や未選択）"メーター管理番号"のリスト(登録確認メッセージ表示用). */
    private List<Long> warnMissingIdList;

    /** 強制実行フラグ(false:[登録]ボタン押下時  true:強制実行確認ダイアログで[OK]ボタン押下時). */
    private boolean isForcedWrite;

    /**
     * 削除します。よろしいですか？
     * @return
     */
    public String getBeforeDeleteMessage() {
        return beanMessages.getMessage("osol.warn.beforeDeleteMessage");
    }

    /**
     * 「確定登録」ボタン押下にメータ登録用マスタと比較して、登録すべきメーター管理番号が登録対象になっていない（未計測や未選択）の場合の確認メッセージ
     * メッセージ：
     *      確定処理が行われないメーターがあります。
     *      選択されたメーターのみ確定処理を実行しますが、よろしいですか。
     *      （不足している管理番号:{0}）
     * @return 確認メッセージ  null:警告なし(正常)  null以外:確認ダイアログを表示する
     */
    public String getConfMissingId() {
        if (this.warnMissingIdList == null || this.warnMissingIdList.isEmpty()) {
            return null;
        }

        // メーター管理番号 が複数ある場合、","区切りに整形
        String existingIds = this.warnMissingIdList.stream().map(meterMngId -> meterMngId.toString()).collect(Collectors.joining(","));

        return beanMessages.getMessageFormat("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.confMissingId",
                new String[] { existingIds });
    }

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:init():START"));

        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        // --- プルダウンリストのMap生成 ---

        String corpType = this.getLoginCorpType();
        String authorityType = this.getLoginOperationCorpAuthorityType();
        CorpPersonAuthParam corpPersonAuthParam = new CorpPersonAuthParam(this.getLoginCorpId(), this.getLoginPersonId(), this.getLoginOperationCorpId());

        //月検針連番
        //接続先
        if(corpType.equals("1") && authorityType.equals("1") && isOcrAuth()) {
            // (パートナー企業 建物担当権限 OCR権限) OCR装置のみを表示
            this.devNameMap = toolsPullDownList.getOcrDevPrm(listInfo.getCorpId(), new Long(listInfo.getBuildingId()), corpPersonAuthParam);
        }else if(corpType.equals("3")) {
            // 契約企業権限はOCR装置を除いたものだけを表示する → 後にOCR装置も表示するように変更
//            this.devNameMap = toolsPullDownList.getNotOcrDevPrm(listInfo.getCorpId(), new Long(listInfo.getBuildingId()), corpPersonAuthParam);
            this.devNameMap = toolsPullDownList.getDevPrm(listInfo.getCorpId(), Long.parseLong(listInfo.getBuildingId()));
        } else if (corpType.equals("1") && authorityType.equals("1") && isBuildingAuth() && !isOcrAuth()) {
            //パートナー権限・建物担当権限・建物管理権限・OCRなしの場合、OCR装置を除いたものだけを表示
            this.devNameMap = toolsPullDownList.getNotOcrDevPrm(listInfo.getCorpId(), new Long(listInfo.getBuildingId()), corpPersonAuthParam);
        } else if (corpType.equals("1") && authorityType.equals("0")){
            //パートナー権限・企業担当はOCR装置を除いたものだけを表示
            this.devNameMap = toolsPullDownList.getNotOcrDevPrm(listInfo.getCorpId(), new Long(listInfo.getBuildingId()), corpPersonAuthParam);
        } else {
            this.devNameMap =  toolsPullDownList.getDevPrm(listInfo.getCorpId(), Long.parseLong(listInfo.getBuildingId()));
        }


        // データ表示種別マップ
        this.dispDataTypeMap = new LinkedHashMap<>();
        this.dispDataTypeMap.put("最新データ表示", DISP_DATA_TYPE_LATEST);
        this.dispDataTypeMap.put("全データ表示", DISP_DATA_TYPE_ALL);

        // --- 検索条件 初期値 設定 ---

        // 接続先 (プルダウンリストの先頭値を設定)
        if (this.devNameMap.size() > 0) {
            this.devId = this.devNameMap.get(this.devNameMap.keySet().iterator().next());
        }

        // データ表示種別 (最新データ表示を設定)
        this.dispDataType = DISP_DATA_TYPE_LATEST;

        // --- 検索 ---

        search();

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:init():END"));
        return "smsCollectSettingMeterreadingInspectionMeterBefList";
    }

    /**
     * 一覧画面再表示（確定前検針データ確定登録画面 で 確定登録 を行い全件正常終了したときの処理）
     * @return 画面Beanページ
     */
    public String initReturn() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:initReturn():START"));

        if (this.searchDevId != null && this.searchDispDataType != null) {
            // 前回検索時の検索条件が残っている(通常) → 再検索
            // 再検索(前回検索時の検索条件をセット)
            this.devId = this.searchDevId; // 装置ID (接続先）
            this.dispDataType = this.searchDispDataType; // データ表示種別
            search();
        } else {
            // 前回検索時の検索条件が残っていない(発生しないはず) → 画面初期化
            init();
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:initReturn():END"));
        return "smsCollectSettingMeterreadingInspectionMeterBefList";
    }

    /**
     * 「表示更新」ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String search() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:search():START"));

        // 接続先チェック
        if (CheckUtility.isNullOrEmpty(this.devId)) {
            // 入力チェック・エラー
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.devId"));
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:search():END (validate error)"));
            return "";
        }

        // 検索実処理
        if (!executeSearch(topBean.getTopBeanProperty().getListInfo())) {
            // データ異常(不正操作など)
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.parameter"));
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:search():END (error)"));
            return "";
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:search():END"));
        return "smsCollectSettingMeterreadingInspectionMeterBefList";
    }

    /**
     * 「確定登録」ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String execEntry() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:execEntry():START"));

        this.warnMissingIdList = null; // メーター登録用マスタと比較して、登録すべきメーター管理番号が登録対象になっていない メーター管理番号のリスト

        // 確定前検針データ 選択行 取得
        List<InspectionMeterBef> allList = smsCollectSettingMeterreadingInspectionMeterBefPaging.getResultList(); // 全頁取得
        List<InspectionMeterBef> selList = allList.stream().filter(meterBef -> meterBef.getCheckbox()).collect(Collectors.toList()); // 選択行を抽出

        if (!validate(selList)) {
            // 入力チェック・エラー
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:execEntry():END (validate error)"));
            return "";
        }

        // 装置ID 取得 ※検索条件欄の 接続先(装置ID)(this.devId) は、検索後に変更できるので使わない。
        final String devId = selList.get(0).getDevId();

        // 強制実行ではない（通常ケース）か？
        if (!this.isForcedWrite) {
            // メーター登録用マスタと比較して、登録すべきメーター管理番号が登録対象になっていない メーター管理番号 があるかチェックする
            this.warnMissingIdList = searchMissingIdList(
                    devId, // 装置ID
                    selList.stream().map(row -> row.getMeterMngId()).collect(Collectors.toList())); // 確定するメーター管理番号
            if (this.warnMissingIdList != null && !this.warnMissingIdList.isEmpty()) {
                // 登録対象になっていない メーター管理番号 がある → 確認ダイアログを表示
                return "";
            }
        }

        // 接続先名称 取得
        Optional<String> devName = devNameMap.keySet().stream()
            .filter(row -> devId.equals(devNameMap.get(row)))
            .findFirst();

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:execEntry():END"));
        return inspectionMeterBefBean.initEdit(devId, devName.orElse(devId), selList);
    }

    /**
     * メーター登録用マスタと比較して、登録すべきメーター管理番号が登録対象になっていない メーター管理番号のリストをDBから取得する
     * @param devId 装置ID
     * @param meterMngIdList 確定するメーター管理番号
     * @return 検針漏れ または 選択漏れ メーター管理番号一覧  null:漏れがない(正常)  ※null以外の場合は 確認ダイアログ を表示する
     */
    private List<Long> searchMissingIdList(String devId, List<Long> meterMngIdList) {

        ListSmsCheckMeterResponse response = new ListSmsCheckMeterResponse();
        ListSmsCheckMeterParameter parameter = new ListSmsCheckMeterParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ListSmsCheckMeterBean");
        parameter.setDevId(devId); // 装置ID (接続先）
        parameter.setMeterMngIds(new ListSmsCheckMeterRequest(meterMngIdList));

        response = (ListSmsCheckMeterResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        List<Long> missingMeterMngIdList = (response.getResult() == null ? null : response.getResult().getMeterMngIdList());

        return missingMeterMngIdList;
    }

    /**
     * 入力チェック.
     * @param isModeAdd 追加モード  true:追加  false:更新
     * @param selList 選択行を抽出
     * @return true:入力OK false:入力NG
     */
    private boolean validate(List<InspectionMeterBef> selList) {
        boolean isChkOk = true;

        // 選択チェック
        if (selList.isEmpty()) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.registNotSelected"));
            isChkOk = false;
        } else {
            // メーター管理番号重複チェック

            // メーター管理番号毎の件数をカウント
            Map<Long, Integer> meterMngIdCntMap = new HashMap<Long, Integer>(); // メーター管理番号毎の件数
            selList.stream().forEach(row -> {
                meterMngIdCntMap.put(row.getMeterMngId(), meterMngIdCntMap.get(row.getMeterMngId()) == null ? 1 : meterMngIdCntMap.get(row.getMeterMngId()) + 1);
            });

            // 重複チェック
            List<String> duplicateMeterMngIdList = meterMngIdCntMap.keySet().stream()
                .filter(meterMngId -> meterMngIdCntMap.get(meterMngId) > 1)
                .map(meterMngId -> meterMngId.toString())
                .collect(Collectors.toList());
            if (!duplicateMeterMngIdList.isEmpty()) {
                duplicateMeterMngIdList.forEach(meterMngId -> {
                    addErrorMessage(beanMessages.getMessageFormat("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.duplicateMeterMngId",
                            new String[] { meterMngId }));
                });
                isChkOk = false;
            }
        }

        return isChkOk;
    }

    /**
     * チェックボックス全選択/解除
     * @return 画面Beanページ
     */
    @Logged
    public String chkBoxAll(){
        List<InspectionMeterBef> list = smsCollectSettingMeterreadingInspectionMeterBefPaging.getResultList(); // 全頁取得
        boolean allDischeckFlg = true;

        // 検索結果を表示 ＆ メーター管理番号毎の件数をカウント
        Map<Long, Integer> meterMngIdCntMap = new HashMap<Long, Integer>(); // メーター管理番号毎の件数
        list.stream().forEach(row -> {
            meterMngIdCntMap.put(row.getMeterMngId(), meterMngIdCntMap.get(row.getMeterMngId()) == null ? 1 : meterMngIdCntMap.get(row.getMeterMngId()) + 1);
        });

        // チェックついていないチェックボックスをチェックする
        boolean checked;
        int max = list.size();
        for (int idx = 0; idx < max; idx++) {
            InspectionMeterBef meterBef = list.get(idx);
            if (!meterBef.getIsInspection()) {
                continue; // 検針漏れデータ行(確定前データ行ではない)のためスキップ
            }

            Long nextMeterMngId = (idx+1) < max ? list.get(idx+1).getMeterMngId() : null; // 次の行のメーター管理番号

            // 次の行と管理番号が一致しない(通常) → 重複がない または 重複があっても最新の行 のため true にする
            checked = (!meterBef.getMeterMngId().equals(nextMeterMngId)); // チェックを付けるフラグ true:チェックする false:チェックしない

            if (meterBef.getCheckbox() != checked) {
                meterBef.setCheckbox(checked);
                allDischeckFlg = false;
            }
        }

        // チェックついていないチェックボックスが存在しなかった場合、全てチェックを外す
        if (allDischeckFlg) {
            for (InspectionMeterBef meterBef : list) {
                if (!meterBef.getIsInspection()) {
                    continue; // 検針漏れデータ行(確定前データ行ではない)のためスキップ
                }
                meterBef.setCheckbox(false);
            }
        }

        return ""; // "smsCollectSettingMeterreadingInspectionMeterBefList";
    }

    /**
     * 「選択削除」ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String delete() throws ParseException {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:delete():START"));

        List<InspectionMeterBef> allList = smsCollectSettingMeterreadingInspectionMeterBefPaging.getResultList(); // 全頁取得

        // 選択チェック
        List<InspectionMeterBef> selList = allList.stream().filter(meterBef -> meterBef.getCheckbox()).collect(Collectors.toList()); // 選択行を抽出
        if (selList.isEmpty()) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefListBean.error.delNotSelected"));
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:delete():END (validate error)"));
            return "";
        }

        List<DeleteSmsInspectionMeterBefRequestSet> deleteList = new ArrayList<DeleteSmsInspectionMeterBefRequestSet>();
        if (allList != null) {
            allList.stream()
                .filter(row -> row.getCheckbox()) // 選択チェックボックスがチェックされている行のみ取得する
                .forEach(row -> {
                    deleteList.add(
                            new DeleteSmsInspectionMeterBefRequestSet(
                                    row.getDevId(),
                                    row.getMeterMngId(),
                                    row.getLatestInspDate(),
                                    row.getVersion()));
                });
        }

        DeleteSmsInspectionMeterBefResponse response = new DeleteSmsInspectionMeterBefResponse();
        DeleteSmsInspectionMeterBefParameter parameter = new DeleteSmsInspectionMeterBefParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("DeleteSmsInspectionMeterBefBean");
        parameter.setDeletes(new DeleteSmsInspectionMeterBefRequest(deleteList));

        response = (DeleteSmsInspectionMeterBefResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 正常終了
            // 表示を再検索(前回検索時の検索条件をセット)
            this.devId = this.searchDevId; // 装置ID (接続先）
            this.dispDataType = this.searchDispDataType; // データ表示種別
            search();
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefListBean:delete():END"));
        return "";
    }

    /**
     * 前ページへ送るメソッド
     *
     * @return
     */
    @Logged
    public String prevPage(ActionEvent event) {
        smsCollectSettingMeterreadingInspectionMeterBefPaging.prevPage();
        return STR_EMPTY;
    }

    /**
     * 先ページへ送るメソッド
     *
     * @return
     */
    @Logged
    public String nextPage(ActionEvent event) {
        smsCollectSettingMeterreadingInspectionMeterBefPaging.nextPage();
        return STR_EMPTY;
    }

    /**
     * ページング：ページ指定時の処理
     * @return
     */
    @Logged
    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            smsCollectSettingMeterreadingInspectionMeterBefPaging.setCurrentPage(Integer.valueOf(param));
        }
        return STR_EMPTY;
    }

    /**
     * 検索した接続先が"OCR"か否か
     * ※画面上の「データ取得日時」項目名称をOCR時に「計測日時」と表示する判定に使用する
     * @return OCRフラグ  true:OCR  false:OCR以外
     */
    public boolean isOcr() {
        if (searchDevId == null) {
            return false;
        }

        return searchDevId.startsWith(DEVICE_KIND.OCR.getVal());
    }

    /**
     * 検索実処理
     * @param listInfo 建物情報
     * @return true:正常  false:データ異常(不正操作など)
     */
    private boolean executeSearch(ListInfo listInfo) {
        ListSmsInspectionMeterBefResponse response = new ListSmsInspectionMeterBefResponse();
        ListSmsInspectionMeterBefParameter parameter = new ListSmsInspectionMeterBefParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ListSmsInspectionMeterBefBean");

        if (listInfo.getCorpId() == null || !StringUtility.isNumeric(listInfo.getBuildingId())) {
            // 検索条件エラー
            smsCollectSettingMeterreadingInspectionMeterBefPaging.init(new ArrayList<InspectionMeterBef>()); // クリア
            return false;
        }

        // 検索条件をクラス変数から取得
        this.searchDevId = this.devId; // 装置ID (接続先）
        this.searchDispDataType = this.dispDataType; // データ表示種別

        parameter.setCorpId(listInfo.getCorpId());    // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID
        parameter.setDevId(this.searchDevId); // 装置ID (接続先）
        parameter.setIsLastData(DISP_DATA_TYPE_LATEST.equals(this.searchDispDataType)); // データ表示種別

        response = (ListSmsInspectionMeterBefResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 該当データあり
            // 検索結果を表示
            dispDbData(response.getResult().getInspectionMeterBefResultDataList());
        } else {
            // 該当データなし
            smsCollectSettingMeterreadingInspectionMeterBefPaging.init(new ArrayList<InspectionMeterBef>()); // クリア
        }

        return true;
    }

    /**
     * 検索結果を表示
     * @param result 検索結果
     */
    private void dispDbData(List<InspectionMeterBefResultData> inspectionMeterBefResultDataList) {

        Map<Long, Integer> meterMngIdCntMap = new HashMap<Long, Integer>(); // メーター管理番号毎の件数

        // 検索結果を表示 ＆ メーター管理番号毎の件数をカウント
        List<InspectionMeterBef> resultList = new ArrayList<InspectionMeterBef>();
        if (inspectionMeterBefResultDataList != null) {
            inspectionMeterBefResultDataList.stream().forEach(row -> {
                resultList.add(new InspectionMeterBef(row));
                meterMngIdCntMap.put(row.getMeterMngId(), meterMngIdCntMap.get(row.getMeterMngId()) == null ? 1 : meterMngIdCntMap.get(row.getMeterMngId()) + 1);
            });
        }

        // メーター管理番号 が重複する場合は重複フラグをセットする  ※一覧表示時に 太字 のスタイルをあてる
        resultList.stream().forEach(row -> {
            row.setDuplicate(meterMngIdCntMap.get(row.getMeterMngId()) > 1); // true:重複  false:重複なし
        });

        smsCollectSettingMeterreadingInspectionMeterBefPaging.init(resultList);
    }

    /**
     * 検索結果件数取得(確定前検針データが未取得の行を除く件数)
     * @return 確定前検針データ件数取得
     */
    public int getResultCount() {
        if (this.smsCollectSettingMeterreadingInspectionMeterBefPaging.getResultCount() == 0) {
            return 0;
        }
        List<InspectionMeterBef> list = this.smsCollectSettingMeterreadingInspectionMeterBefPaging.getResultList(); // 全頁取得
        return (int) list.stream().filter(r -> r.getIsInspection()).count();
    }

    public InspectionMeterBefPaging getSmsCollectSettingMeterreadingInspectionMeterBefPaging() {
        return smsCollectSettingMeterreadingInspectionMeterBefPaging;
    }

    public void setSmsCollectSettingMeterreadingInspectionMeterBefPaging(
            InspectionMeterBefPaging smsCollectSettingMeterreadingInspectionMeterBefPaging) {
        this.smsCollectSettingMeterreadingInspectionMeterBefPaging = smsCollectSettingMeterreadingInspectionMeterBefPaging;
    }

    public Map<String, String> getDevNameMap() {
        return devNameMap;
    }

    public Map<String, String> getDispDataTypeMap() {
        return dispDataTypeMap;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDispDataType() {
        return dispDataType;
    }

    public void setDispDataType(String dispDataType) {
        this.dispDataType = dispDataType;
    }

    public boolean getIsForcedWrite() {
        return isForcedWrite;
    }

    public void setIsForcedWrite(boolean isForcedWrite) {
        this.isForcedWrite = isForcedWrite;
    }

    /**
     * OCR権限を持っていればtrueを返却
     * @return
     */
    private boolean isOcrAuth() {
        // 担当者のOCR権限を取得（SMS権限情報が見つからない場合はnullを取得）
        MCorpPersonAuth ocrAuthority = this.getLoginCorpPersonAuthList().stream()
                .filter(o -> Objects.equals(o.getMAuth().getAuthorityCd(), OsolConstants.USER_AUTHORITY.OCR.getVal()))
                .findFirst()
                .orElse(null);

        // OCR権限可否を取得（nullの場合は0:権限なしとする）
        Integer ocrAuthorityFlg = Objects.nonNull(ocrAuthority) ? ocrAuthority.getAuthorityFlg()
                : OsolConstants.FLG_OFF;

        //OCR権限がある場合はtrueを返却
        if(ocrAuthorityFlg.equals(OsolConstants.FLG_ON)) {
            return true;
        }

        return false;
    }

    /**
     * 建物権限を持っていればtrueを返却
     * @return
     */
    private boolean isBuildingAuth() {
        // 担当者の建物管理権限を取得（SMS権限情報が見つからない場合はnullを取得）
        MCorpPersonAuth buildingAuthority = this.getLoginCorpPersonAuthList().stream()
                .filter(o -> Objects.equals(o.getMAuth().getAuthorityCd(), OsolConstants.USER_AUTHORITY.BUILDING.getVal()))
                .findFirst()
                .orElse(null);

        // 建物管理権限可否を取得（nullの場合は0:権限なしとする）
        Integer buildingAuthorityFlg = Objects.nonNull(buildingAuthority) ? buildingAuthority.getAuthorityFlg()
                : OsolConstants.FLG_OFF;

        //建物管理権限がある場合はtrueを返却
        if(buildingAuthorityFlg.equals(OsolConstants.FLG_ON)) {
            return true;
        }

        return false;
    }

}
