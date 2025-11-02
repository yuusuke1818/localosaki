package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewParameter;
import jp.co.osaki.osol.api.request.sms.collect.dataview.ListSmsDataViewRequest;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDataViewResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.BuildingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsFileZipArchive;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.Condition;
import jp.co.osaki.sms.bean.building.info.InfoPagingList;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.BuildingInfoSearchDao;
import jp.co.osaki.sms.dao.ListSmsDataViewDao;
import jp.skygroup.enl.webap.base.BaseSearchInterface;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 複数日 日報ダウンロード
 *
 */
@Named(value = "smsAggregateDailyBean")
@ConversationScoped
public class DailyBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    /**
     *
     */
    private static final long serialVersionUID = -3887087331579449859L;

    /** ZIPファイル名フォーマット. */
    private static final String CSV_ZIP_NAME_FORMAT = "日報データ_%s";
    /** CSVファイル名フォーマット. */
    private static final String CSV_FILE_NAME_FORMAT = "日報データ_%s_%s_%s-%s.csv";

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;
    private List<String> invalidComponent;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // 検索条件格納クラス
    @Inject
    private DailyBeanProperty dailyBeanProperty;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // 検索条件指定数上限Map
    private Map<String, Integer> conditionLimitCountMap;

    // 都道府県用セレクトボックス
    private Map<String, String> prefectureMap;

    // 表示種別プルダウンMap.
    private Map<String, String> dispTypeMap;

    // ページング処理
    @Inject
    private InfoPagingList pagingList;

    // 建物検索Dao
    @EJB
    private BuildingInfoSearchDao buildingInfoSearchDao;
    @EJB
    private ListSmsDataViewDao listSmsDataViewDao;

    @Inject
    private SmsFileDownload fileDownloader;

    // 開始日
    private String startDate;
    // 終了日
    private String endDate;
    // 表示種別
    private String dispType;

    // ダウンロードファイル名
    private String downloadFileName;
    private String saveFileName;

    //---------------------------------------------------
    // Getter / Setter
    //---------------------------------------------------
    public DailyBeanProperty getDailyBeanProperty() {
        return dailyBeanProperty;
    }

    public List<Condition> getConditionList() {
        return dailyBeanProperty.getConditionList(this);
    }

    public void setConditionLimitCountMap(Map<String, Integer> conditionLimitCountMap) {
        this.conditionLimitCountMap = conditionLimitCountMap;
    }

    public Map<String, Integer> getConditionLimitCountMap() {
        return conditionLimitCountMap;
    }

    public Map<String, String> getPrefectureMap() {
        return prefectureMap;
    }

    public InfoPagingList getPagingList() {
        return pagingList;
    }

    public Map<String, String> getDispTypeMap() {
        return dispTypeMap;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Date getCalStartDate() {
        Date ret = null;
        if (this.startDate != null) {
            ret = DateUtility.conversionDate(this.startDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
        return ret;
    }

    public void setCalStartDate(Date calStartDate) {
        if (calStartDate != null) {
            this.startDate = DateUtility.changeDateFormat(calStartDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Date getCalEndDate() {
        Date ret = null;
        if (this.endDate != null) {
            ret = DateUtility.conversionDate(this.endDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
        return ret;
    }

    public void setCalEndDate(Date calEndDate) {
        if (calEndDate != null) {
            this.endDate = DateUtility.changeDateFormat(calEndDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        }
    }

    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    //---------------------------------------------------
    // Method
    //---------------------------------------------------
    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsAggregateDailyBean:init():START"));

        conversationStart();

        // 検索条件指定数上限Mapを生成
        setConditionLimitCountMap(createConditionLimitCountMap());
        // プルダウンリスト
        prefectureMap = toolsPullDownList.getPrefecture();
        dispTypeMap = toolsPullDownList.getDispType();
        if (dispTypeMap != null && dispTypeMap.size() > 0) {
            Iterator<String> it = dispTypeMap.values().iterator();
            if (it.hasNext()) {
                setDispType(it.next());
            }
        }

        //
        dailyBeanProperty.setSearchedFlg(false);
        changeColumnClassesStr();

        this.invalidComponent = new ArrayList<>();

        eventLogger.debug(packageName.concat(" smsAggregateDailyBean:init():END"));
        return "dailyAggregate";
    }

    /**
     * 画面表示用カラムのcss定義の切替え
     *
     */
    public void changeColumnClassesStr() {
        String columnClassesStr = "";
        if (this.isLoginOperationCorpSelected()) {
            // 企業特定後
            dailyBeanProperty.setCorpId(this.getLoginOperationCorpId());
        } else {
            // 企業特定前
            columnClassesStr += "column_style_corp,";
        }
        columnClassesStr += "column_style_tenant_flg,";
        columnClassesStr += "column_style_building,";
        columnClassesStr += "column_style_address,";
        columnClassesStr += "column_style_auto_insp_btn";
        dailyBeanProperty.setColumnClassesStr(columnClassesStr);
    }

    /**
     * 検索処理
     * @return
     */
    public String search() {
        // アクセスログ出力
        exportAccessLog("search", "ボタン「検索開始」押下");

        eventLogger.debug(packageName.concat(" smsAggregateDailyBean:search():START"));

        List<Condition> conditionList = dailyBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        // 検索処理
        executeSearch(conditionList);
        dailyBeanProperty.setSearchedFlg(true);

        eventLogger.debug(packageName.concat(" smsAggregateDailyBean:search():END"));
        return "dailyAggregate";
    }

    @Override
    public void initialConditionList(List<Condition> list) {
        list.addAll(this.createFixedConditionList());
    }

    /**
     * 固定表示の検索条件Listを生成.
     *
     * @return 固定表示の検索条件List
     */
    private List<Condition> createFixedConditionList() {
        List<Condition> fixedConditionList = new ArrayList<>();

        Condition condition = new Condition();
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
        condition.setDeleteButtonEnable(false);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        fixedConditionList.add(condition);

        return fixedConditionList;
    }

    @Override
    public Condition createDefaultCondition() {
        // 未選択状態の条件（「条件を追加」）
        Condition condition = new Condition();
        condition.setSelectEnable(true);
        condition.setSearchSubjectConjunctionEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);

        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {

        // 一致条件表示
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
        condition.setSelectEnable(false);
        condition.setSearchSubjectConjunctionEnable(true);
        condition.setDeleteButtonEnable(true);

        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        case (OsolConstants.SEARCH_CONDITION_PREFECTURE):
            // 都道府県検索
            condition.setPrefectureSelectEnable(true);
            condition.setMultiSelectEnable(true); // 複数選択可能
            break;
        default:
            // キーワード検索
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        // 選択肢の設定
        Map<String, String> selectConditionMap = new LinkedHashMap<>();
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);

        List<String> selectConditionList = createSelectConditionList();

        for (String selectCondition : selectConditionList) {
            selectConditionMap.put(selectCondition, selectCondition);
        }

        return selectConditionMap;
    }

    /**
     * 検索条件Listを生成.
     *
     * @return 検索条件List
     */
    private List<String> createSelectConditionList() {
        List<String> selectConditionList = new ArrayList<>();

        selectConditionList.add(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_PREFECTURE);

        return selectConditionList;
    }

    /**
     * 検索条件追加時.
     *
     * @param orderNo 選択された検索条件
     */
    public void changeConditionCd(String orderNo) {
        // アクセスログ出力
        exportAccessLog("changeConditionCd", "「検索条件」選択");

        dailyBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        controlConditionMap(getConditionLimitCountMap(), dailyBeanProperty.getConditionList(this));
    }

    /**
     * 検索条件削除時.
     *
     * @param orderNo 削除された検索条件
     */
    public void deleteCondition(String orderNo) {
        // アクセスログ出力
        exportAccessLog("deleteCondition", "検索条件にてボタン「削除」押下");

        dailyBeanProperty.deleteCondition(this, orderNo);
        controlConditionMap(getConditionLimitCountMap(), dailyBeanProperty.getConditionList(this));
    }

    /**
     * 検索条件プルダウンMapを制御.
     *
     * @param conditionList 検索条件List
     */
    private void controlConditionMap(Map<String, Integer> conditionLimitCountMap, List<Condition> conditionList) {
        Set<Entry<String, Integer>> conditionLimitCountMapEntrySet = conditionLimitCountMap.entrySet();
        for (Entry<String, Integer> conditionLimitCountMapEntry : conditionLimitCountMapEntrySet) {
            String targetSelectConditionCd = conditionLimitCountMapEntry.getKey();
            int conditionLimitCount = conditionLimitCountMapEntry.getValue();
            int conditionCount = 0;
            boolean limitCountFlg = false;
            for (Condition condition : conditionList) {
                if (targetSelectConditionCd.equals(condition.getSelectConditionCd())) {
                    // 当該検索条件の指定数を取得
                    conditionCount++;
                    if (conditionCount >= conditionLimitCount) {
                        limitCountFlg = true;
                        break;
                    }
                }
            }
            for (Condition condition : conditionList) {
                if (targetSelectConditionCd.equals(condition.getSelectConditionCd())) {
                    condition.setLimitCountFlg(limitCountFlg);
                }
            }
        }
        dailyBeanProperty.updateCondition(this, conditionList.size() - 1);

        int allSelectConditionLimitCount = 0;
        List<String> selectConditionList = createSelectConditionList();
        for (String selectConditionCd : selectConditionList) {
            if (conditionLimitCountMap.containsKey(selectConditionCd)) {
                allSelectConditionLimitCount += conditionLimitCountMap.get(selectConditionCd);
            } else {
                allSelectConditionLimitCount++;
            }
        }

        if ((conditionList.size() - (createFixedConditionList().size() + 1)) >= allSelectConditionLimitCount) {
            // 全ての検索条件の指定数が上限に達している場合
            conditionList.remove(conditionList.size() - 1);
        } else {
            boolean isExistDefaultCondition = false;
            for (Condition condition : conditionList) {
                if (condition.isDefaultCondition()) {
                    isExistDefaultCondition = true;
                    break;
                }
            }
            if (!isExistDefaultCondition) {
                conditionList.add(createDefaultCondition());
                dailyBeanProperty.resetOrder();
                dailyBeanProperty.updateConditionMap(this, conditionList.size() - 1);
            }
        }
    }

    /**
     * 検索条件指定数上限Mapを生成.
     *
     * @return 検索条件指定数上限Map
     */
    private Map<String, Integer> createConditionLimitCountMap() {
        Map<String, Integer> conditionLimitCountMap = new HashMap<>();
        conditionLimitCountMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE,
                OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT);
        return conditionLimitCountMap;
    }

    /**
     * 検索実処理
     *
     * @param conditionList
     */
    private void executeSearch(List<Condition> conditionList) {
        pagingList.init(searchBuildingInfo(conditionList));
    }

    /**
     * 建物情報検索
     *
     * @param conditionList
     * @return
     */
    private List<ListInfo> searchBuildingInfo(List<Condition> conditionList) {
        List<Object> targetCorpIdOrNameList = new ArrayList<>();
        List<Object> targetBuildingNoOrNameList = new ArrayList<>();
        List<Object> targetPrefectureList = new ArrayList<>();

        Date targetDate = buildingInfoSearchDao.getSvDate();

        for (Condition condition : conditionList) {
            if (condition == null
                    || condition.getSelectConditionCd() == null
                    || condition.getSelectConditionCd().equals(OsolConstants.DEFAULT_SELECT_BOX_VALUE)) {
                continue;
            }

            switch (condition.getSelectConditionCd()) {
            case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                // 都道府県検索
                if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                    targetPrefectureList.add(condition.getPrefectureCd());
                }
                break;
            case OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME:
                // 企業(IDまたは名)検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetCorpIdOrNameList.add(condition.getConditionKeyword());
                }
                break;
            case OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY:
                // 建物番号または建物名検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetBuildingNoOrNameList.add(condition.getConditionKeyword());
                }
                break;
            default:
                break;
            }
        }

        // 建物一覧取得
        List<TBuilding> resultlist = buildingInfoSearchDao.getSearchList(
                null,
                targetCorpIdOrNameList,
                null,
                targetPrefectureList,
                targetBuildingNoOrNameList,
                null,
                targetDate,
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginCorpType(),
                this.getLoginPerson().getPersonType());

        return convertList(resultlist);
    }

    /**
     * エンティティを画面表示用オブジェクトに詰め替える
     *
     * @param orgList
     * @return
     */
    private List<ListInfo> convertList(List<TBuilding> orgList) {
        List<ListInfo> buildingInfoList = new ArrayList<>();
        for (TBuilding tBuilding : orgList) {
            ListInfo info = new ListInfo();
            info.setId(tBuilding.getId());
            info.setBuildingId(tBuilding.getId().getBuildingId().toString());
            info.setBuildingName(tBuilding.getBuildingName());
            info.setBuildingNo(tBuilding.getBuildingNo());
            //建物・テナント
            info.setTenantFlg(BuildingUtility.isTenant(tBuilding.getBuildingType()));
            info.setBuildingTenant(BuildingUtility.getBuildingTenant(tBuilding.getBuildingType()));
            info.setCorpId(tBuilding.getMCorp().getCorpId());
            info.setCorpName(tBuilding.getMCorp().getCorpName());
            // 住所成形(住所情報の結合)
            String buildingAddress = STR_EMPTY;
            if (tBuilding.getMPrefecture().getPrefectureName() != null) {
                buildingAddress += tBuilding.getMPrefecture().getPrefectureName();
            }
            if (tBuilding.getAddress() != null) {
                buildingAddress += tBuilding.getAddress();
            }
            if (tBuilding.getAddressBuilding() != null) {
                buildingAddress += tBuilding.getAddressBuilding();
            }
            info.setBuildingAddress(buildingAddress);

            buildingInfoList.add(info);
        }
        return buildingInfoList;
    }

    /**
     * ダウンロード可能か権限チェック <br>
     * 『データ表示』権限相当があるか
     * @param buildingCorpId 建物の企業ID
     * @return true:権限あり、false:権限なし
     */
    public boolean isAuthCheck(String buildingCorpId) {
        boolean ret = false;

        // 企業種別
        String corpType = this.getLoginCorpType();

        // 担当者のSMS権限を取得（SMS権限情報が見つからない場合はnullを取得）
        MCorpPersonAuth smsAuthority = this.getLoginCorpPersonAuthList().stream()
                .filter(o -> Objects.equals(o.getMAuth().getAuthorityCd(), OsolConstants.USER_AUTHORITY.SMS.getVal()))
                .findFirst()
                .orElse(null);

        // 担当者のOCR権限を取得（SMS権限情報が見つからない場合はnullを取得）
        MCorpPersonAuth ocrAuthority = this.getLoginCorpPersonAuthList().stream()
                .filter(o -> Objects.equals(o.getMAuth().getAuthorityCd(), OsolConstants.USER_AUTHORITY.OCR.getVal()))
                .findFirst()
                .orElse(null);

        // SMS権限可否を取得（nullの場合は0:権限なしとする）
        Integer smsAuthorityFlg = Objects.nonNull(smsAuthority) ? smsAuthority.getAuthorityFlg()
                : OsolConstants.FLG_OFF;

        // OCR権限可否を取得（nullの場合は0:権限なしとする）
        Integer ocrAuthorityFlg = Objects.nonNull(ocrAuthority) ? ocrAuthority.getAuthorityFlg()
                : OsolConstants.FLG_OFF;

        // 担当者の機能権限チェック
        switch (corpType) {
        case "0": // 大崎権限
            ret = true;
            break;

        case "1": // パートナー権限
            /*
             * SMS権限なしは、企業担当/建物担当をチェックする
             * 企業担当のSMS権限なしはOK
             * 建物・テナント担当のSMS権限なしはNG
             */
            List<MCorpPerson> corpPersonList = this.getLoginCorpPersonListAll();
            ret = corpPersonList.stream()
                    .anyMatch(o -> Objects.equals(buildingCorpId, o.getId().getCorpId())
                            && Objects.equals(o.getAuthorityType(), OsolConstants.AUTHORITY_TYPE.CORP.getVal()));
            // 企業担当ではない場合
            if(!ret) {
                if (Objects.equals(ocrAuthorityFlg, OsolConstants.FLG_ON)) {
                    // OCR権限ならば非表示
                    ret = false;
                }else if (Objects.equals(smsAuthorityFlg, OsolConstants.FLG_ON)) {
                    // OCR権限がなく、SMS権限がある
                    ret = true;
                }
            }
            break;

        case "3": // 契約企業権限
            ret = Objects.equals(smsAuthorityFlg, OsolConstants.FLG_ON);
            break;
        }


        return ret;


    }

    /**
     * 前ページへ送るメソッド
     *
     * @return
     */
    public String prevPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("prevPage", "ページング「前へ」押下");

        pagingList.prevPage();
        return STR_EMPTY;
    }

    /**
     * 先ページへ送るメソッド
     *
     * @return
     */
    public String nextPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("nextPage", "ページング「後へ」押下");

        pagingList.nextPage();
        return STR_EMPTY;
    }

    /**
     * ページ遷移
     * @return
     */
    public String pageJump() {
        // アクセスログ出力
        exportAccessLog("pageJump", "ページングリンク押下");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            pagingList.setCurrentPage(Integer.valueOf(param));
        }
        return STR_EMPTY;
    }

    /**
     * ファイルダウンロード開始.
     *
     * @return 遷移先
     */
    public String downloadFileStart() {

        // ダウンロードファイル作成
        String downloadFilePath = getDownloadFileName();
        String saveFilename = getSaveFileName();
        fileDownloader.fileDownload(downloadFilePath, saveFilename);

        return STR_EMPTY;
    }

    /**
     * ダウンロードファイル作成
     * @param event
     * @throws IOException
     */
    public void mkFile(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("mkFile", "ボタン「ダウンロード」押下");

        // 初期化
        setDownloadFileName(null);
        setSaveFileName(null);

        // 入力値チェック
        if (!inputCheck()) {
            return;
        }

        // パラメータ
        String corpId = String.valueOf(event.getComponent().getAttributes().get("corpId"));
        Long buildingId = Long.parseLong(event.getComponent().getAttributes().get("buildingId").toString());
        String buildingName = String.valueOf(event.getComponent().getAttributes().get("buildingName"));
        boolean isTenant = Boolean.parseBoolean(event.getComponent().getAttributes().get("tenantFlg").toString());

        try {
            String saveFileNm = String.format(CSV_ZIP_NAME_FORMAT, buildingName);
            setSaveFileName(saveFileNm + ".zip");

            // パス
            String path = createFolder(saveFileNm);

            // 装置
            Map<String, String> devIdMap = toolsPullDownList.getDevPrm(corpId, buildingId, isTenant);

            for (Map.Entry<String, String> entry : devIdMap.entrySet()) {
                String devId = entry.getValue();
                String devNm = entry.getKey();
                // ファイル名
                String _fileNm = this.createCsvFileName(buildingName, devNm);
                // ファイルデータ
                String[][] dataArray = mkList(corpId, buildingId, isTenant, devId);
                // ファイル作成
                createFile(path, _fileNm, dataArray);
            }

            // ZIPファイル作成
            String zipPath = createZipFile(path);

            // ダウンロードファイル名
            setDownloadFileName(zipPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 入力値チェック
     * @return
     */
    private boolean inputCheck() {

        boolean ret = true;
        invalidComponent.clear();

        if (CheckUtility.isNullOrEmpty(startDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notInputStartDate"));
            invalidComponent.add("smsAggregateDailyBean:calStartText");
        } else if (!CheckUtility.checkRegDateYmd(startDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notFormatStartDate"));
            invalidComponent.add("smsAggregateDailyBean:calStartText");
        }
        if (CheckUtility.isNullOrEmpty(endDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notInputEndDate"));
            invalidComponent.add("smsAggregateDailyBean:calEndText");
        } else if (!CheckUtility.checkRegDateYmd(endDate)) {
            ret = false;
            addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notFormatEndDate"));
            invalidComponent.add("smsAggregateDailyBean:calEndText");
        }
        if (ret) {
            Date dChk = DateUtility
                    .plusYear(DateUtility.conversionDate(startDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), 1);
            Date dEnd = DateUtility.conversionDate(endDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            if (dChk.compareTo(dEnd) < 0) {
                ret = false;
                addErrorMessage(beanMessages.getMessage("smsAggregateDailyBean.error.notRangeDayMaxOneYear"));
                invalidComponent.add("smsAggregateDailyBean:calEndText");
            }
        }

        return ret;
    }

    /**
     * CSVファイル名
     * @param buildingName
     * @param devName
     * @return
     */
    private String createCsvFileName(String buildingName, String devName) {
        String startDt = this.startDate.replaceAll("/", "");
        String endDt = this.endDate.replaceAll("/", "");
        return String.format(CSV_FILE_NAME_FORMAT, buildingName, devName, startDt, endDt);
    }

    /**
     * zipファイルを作成する
     * zipファイル作成後、元のフォルダは削除
     *
     * @param zipCompressFolder
     * @return
     */
    private String createZipFile(String zipCompressFolder) {
        // 圧縮後の出力パス + ファイル名
        String zipFilePath = zipCompressFolder + ".zip";
        // Zipファイル作成
        if (SmsFileZipArchive.compressDirectory(zipCompressFolder, zipFilePath)) {
            // zip圧縮元フォルダを削除
            AnalysisEmsUtility.deleteFileDirectory(new File(zipCompressFolder));
            return zipFilePath;
        }
        return null;
    }

    /**
     * ファイルを保存するフォルダを作成
     * @return
     */
    private String createFolder(String saveFileNm) {

        File file;
        String path = this.getCsvOutputDir();
        Date svDate = buildingInfoSearchDao.getSvDate();
        // 日付毎
        path += File.separator;
        path += DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYYMMDD);
        file = new File(path);
        // フォルダーが存在しない場合作成
        if (!file.exists()) {
            file.mkdir();
        }
        // ユーザーIDフォルダパス(同時出力時のファイル名かぶり回避)
        path += File.separator;
        path += String.valueOf(getLoginUserId());
        file = new File(path);
        // フォルダーが存在しない場合作成
        if (!file.exists()) {
            file.mkdir();
        }
        //
        path += File.separator;
        path += saveFileNm;
        file = new File(path);
        // フォルダーが存在しない場合作成
        if (!file.exists()) {
            file.mkdir();
        }

        return path;
    }

    /**
     * ファイル作成
     * @param path
     * @param fileNm
     * @param dataArray
     * @throws IOException
     */
    private void createFile(String path, String fileNm, String[][] dataArray) throws IOException {
        Path _path = Paths.get(path, fileNm);
        File _file = _path.toFile();
        OutputStream csvOut = new FileOutputStream(_file, false);
        csvOut.write(0xef);
        csvOut.write(0xbb);
        csvOut.write(0xbf);

        try(OutputStreamWriter osw = new OutputStreamWriter(csvOut, Charset.forName("UTF-8"));
            BufferedWriter writer = new BufferedWriter(osw);) {
            if (dataArray != null) {
                for (String[] data : dataArray) {
                    StringJoiner sj = new StringJoiner(",");
                    Arrays.stream(data).forEach(str -> sj.add(str == null ? "" : str));
                    writer.write(sj.toString());
                    writer.newLine();
                }
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    /**
     *
     * @return
     */
    private String[][] mkList(String corpId, Long buildingId, boolean isTenant, String devId) {

        /** 管理番号行 */
        final int ROW_MNGNO = 0;
        /** テナント名行 */
        final int ROW_TENANT = 1;
        /** メーター種別行 */
        final int ROW_TYPE = 2;
        /** 単位行 */
        final int ROW_UNIT = 3;
        /** データ開始行 */
        final int ROW_START = 4;
        /** 日付列 */
        final int COL_DATE = 0;
        /** 時間列 */
        final int COL_TIME = 1;
        /** データ開始列 */
        final int COL_START = 2;

        // メーター管理番号
        Map<String, Long> meterMngIdMap = toolsPullDownList.getMeterMngId(corpId, buildingId, isTenant, devId);
        List<Long> meterMngIdList = new ArrayList<>(meterMngIdMap.values());

        // 日時
        Map<String, List<String>> targetDateTimeMap = new HashMap<>();
        List<String> targetDateTimeList = new ArrayList<>();
        Date curDate = DateUtility.conversionDate(startDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        Date _endDate = DateUtility.conversionDate(endDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        Date lstDate = DateUtility.plusDay(_endDate, 1);
        String keyDt = DateUtility.changeDateFormat(curDate, DateUtility.DATE_FORMAT_YYYYMMDD);
        int fromIndex = 0;
        int toIndex = 0;
        while (curDate.compareTo(lstDate) < 0) {
            targetDateTimeList.add(DateUtility.changeDateFormat(curDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
            curDate = DateUtility.plusMinute(curDate, 30);
            toIndex++;
            if (!keyDt.equals(DateUtility.changeDateFormat(curDate, DateUtility.DATE_FORMAT_YYYYMMDD))) {
                List<String> _list = new ArrayList<>(targetDateTimeList.subList(fromIndex, toIndex));
                targetDateTimeMap.put(keyDt, _list);
                keyDt = DateUtility.changeDateFormat(curDate, DateUtility.DATE_FORMAT_YYYYMMDD);
                fromIndex = toIndex;
            }
        }
        if (fromIndex < toIndex) {
            List<String> _list = new ArrayList<>(targetDateTimeList.subList(fromIndex, toIndex));
            targetDateTimeMap.put(keyDt, _list);
        }

        // 行：日時、列：メーター管理番号、ヘッダー4行2列
        String[][] values = new String[targetDateTimeList.size() + ROW_START][meterMngIdList.size() + COL_START];
        // 見出し
        values[ROW_MNGNO][COL_TIME] = "管理番号";
        values[ROW_TENANT][COL_TIME] = "テナント名";
        values[ROW_TYPE][COL_TIME] = "メーター種別";
        values[ROW_UNIT][COL_TIME] = "単位";

        // API実行
        ListSmsDataViewResult res = this.getData(corpId,
                buildingId,
                isTenant,
                devId,
                meterMngIdList,
                targetDateTimeList);
        if (res != null) {
            Map<String, ListSmsDataViewResultData> targetDataMap = res.getLoadSurveyDataMap();
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/M/d");
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            for (int i = ROW_START; i < values.length; i++) {
                // 日付
                Date _dt = DateUtility.conversionDate(
                        targetDateTimeList.get(i - ROW_START),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                values[i][COL_DATE] = sdf0.format(_dt);
                values[i][COL_TIME] = sdf1.format(_dt);
                if (SmsConstants.DISP_TYPE.USE.getVal().equals(dispType)) {
                    values[i][COL_TIME] += '\u301c';
                }
                for (int j = COL_START; j < values[i].length; j++) {
                    if (targetDataMap != null) {
                        int x = j; // 日付列が入っているため、シフト
                        int y = i + 1;
                        String key = String.format(ListSmsDataViewResult.LOAD_SURVEY_DATA_MAP_KEY_FORMAT, x, y);
                        ListSmsDataViewResultData data = targetDataMap.get(key);
                        if (data != null) {
                            // メーター管理番号
                            if (CheckUtility.isNullOrEmpty(values[ROW_MNGNO][j])) {
                                values[ROW_MNGNO][j] = data.getMeterMngIdDisp();
                            }
                            // テナント名
                            if (CheckUtility.isNullOrEmpty(values[ROW_TENANT][j])) {
                                values[ROW_TENANT][j] = data.getTenantName();
                            }
                            // メーター種別
                            if (CheckUtility.isNullOrEmpty(values[ROW_TYPE][j])) {
                                values[ROW_TYPE][j] = data.getMeterTypeName();
                            }
                            // 単位
                            if (CheckUtility.isNullOrEmpty(values[ROW_UNIT][j])) {
                                values[ROW_UNIT][j] = data.getUnitUsageBased();
                            }
                            values[i][j] = SmsConstants.DISP_TYPE.USE.getVal().equals(dispType) ? data.getKwh30Disp()
                                    : data.getDmvKwhDisp();
                        }
                    }
                }
            }
        }

        return values;
    }

    /**
     * API実行
     *
     * @param meterMngIdList
     * @param targetDateTimeList
     * @param corpId
     * @param buildingId
     * @param isTenant
     * @param devId
     * @return
     */
    private ListSmsDataViewResult doApi(List<Long> meterMngIdList, List<String> targetDateTimeList, String corpId,
            Long buildingId, boolean isTenant, String devId) {
        // Request
        ListSmsDataViewRequest request = new ListSmsDataViewRequest();
        request.setMeterMngIdList(meterMngIdList);
        request.setTargetDateTimeList(targetDateTimeList);

        // RequestParameter
        ListSmsDataViewParameter parameter = new ListSmsDataViewParameter();
        parameter.setCorpId(corpId);
        parameter.setBuildingId(buildingId);
        parameter.setTenant(isTenant);
        parameter.setDevId(devId);
        parameter.setForwardDiract(true);
        parameter.setUse(SmsConstants.DISP_TYPE.USE.getVal().equals(dispType));
        parameter.setDispTimeUnit(SmsConstants.DISP_TIME_UNIT.M30.getVal());
        parameter.setTargetDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        parameter.setRequest(request);

        // API
        return callApiPost(parameter, ListSmsDataViewResponse.class).getResult();
    }

    private ListSmsDataViewResult getData(
            String corpId,
            Long buildingId,
            boolean isTenant,
            String devId,
            List<Long> meterMngIdList,
            List<String> targetDateTimeList) {

        List<String> _targetDateTimeList;
        if (SmsConstants.DISP_TYPE.USE.getVal().equals(dispType)) {
            // 日報30分使用量取得時
            _targetDateTimeList = new ArrayList<>();
            for (String _s : targetDateTimeList) {
                Date _d = DateUtility.conversionDate(_s, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                _d = DateUtility.plusMinute(_d, 30);
                _targetDateTimeList.add(DateUtility.changeDateFormat(_d, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
            }
        } else {
            _targetDateTimeList = targetDateTimeList;
        }

        return listSmsDataViewDao.query(
                corpId,
                buildingId,
                isTenant,
                devId,
                true,
                _targetDateTimeList,
                meterMngIdList,
                SmsConstants.DISP_TYPE.USE.getVal().equals(dispType),
                SmsConstants.DISP_TIME_UNIT.M30.getVal(),
                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
    }

    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

}
