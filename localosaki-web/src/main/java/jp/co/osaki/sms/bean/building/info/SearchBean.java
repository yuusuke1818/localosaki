package jp.co.osaki.sms.bean.building.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.BuildingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.sms.collect.setting.meterreading.autoinspection.AutoInspectionEditData;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.BuildingAutoInspServiceDao;
import jp.co.osaki.sms.dao.BuildingInfoSearchDao;
import jp.co.osaki.sms.parameter.BuildingAutoInspSearchCondition;
import jp.co.osaki.sms.resultset.BuildingAutoInspResultSet;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

@Named("buildingInfoSearchBean")
@ConversationScoped
public class SearchBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    // シリアライズID
    private static final long serialVersionUID = -2007622176532697923L;

    /** 自動検針日時 検針実行月プルダウン 最小値. */
    private static final int AUTO_INSP_MONTH_MIN = 1;

    /** 自動検針日時 検針実行月プルダウン 最大値. */
    private static final int AUTO_INSP_MONTH_MAX = 12;

    /** 自動検針日時 日プルダウン 最小値. */
    private static final int AUTO_INSP_DAY_MIN = 1;

    /** 自動検針日時 日プルダウン 最大値. */
    private static final int AUTO_INSP_DAY_MAX = 31;

    /** 自動検針日時 表示行数. */
    private static final int AUTO_INSP_DISP_ROW_COUNT = 3;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // 画面受け渡し用
    private String selectedCorpId;

    // 検索条件リスト
    private List<Condition> conditionList;

    // 都道府県用セレクトボックス
    private Map<String, String> prefectureMap;

    // 入居形態用セレクトボックス
    private Map<String, String> nyukyoTypeMap;

    // 建物状況用セレクトボックス
    private Map<String, String> buildingStatusMap;

    /** 検針実行月 プルダウンMap. */
    private Map<String, String> autoInspMonthMap;

    /** 自動検針日 日プルダウンMap. */
    private Map<String, String> autoInspDayMap;

    /** 検索条件指定数上限Map. */
    private Map<String, Integer> conditionLimitCountMap;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // ページング処理
    @Inject
    private InfoPagingList buildingInfoPagingList;

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    // 汎用区分マスタ
    @Inject
    private GenericTypeList genericTypeList;

    // 検索条件格納クラス
    @Inject
    private SearchBeanProperty buildingInfoSearchBeanProperty;

    /** 自動検針データList. */
    private List<AutoInspectionEditData> autoInspDataList;

    // 建物検索Dao
    @EJB
    private BuildingInfoSearchDao buildingInfoSearchDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    /** 建物に紐付く自動検針データ取得Dao. */
    @EJB
    private BuildingAutoInspServiceDao autoInspDao;

    @Inject
    private OsolAccessBean osolAccessBean;

    @Inject
    private TopBean topBean;

    /**
     * 親グループのドロップダウンリスト
     */
    private Map<String, String> parentGroupMap;

    /**
     * 子グループのドロップダウンリスト
     */
    private Map<String, String> childGroupMap;

    // デフォルトコンストラクタ
    public SearchBean() {
    }

    /**
     * 管理者権限の有無で建物新規作成ボタンの表示/非表示を決める
     *
     * @return
     */
    public boolean getBuildingCreateFlg() {

        // 企業選択前は非表示
        if (!this.isLoginOperationCorpSelected()) {
            return false;
        }
        // 管理者権限チェック
        AccessResultSet result = osolAccessBean.getAccessEnable(
                OsolAccessBean.FUNCTION_CD.NONE,
                "Administrator",
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginOperationCorpId());
        return result.getOutput().isRoleGroupEnable() && result.getOutput().isFunctionEnable();
    }

    /**
     * 初期処理
     *
     * @return
     */
    @Override
    @Logged
    public String init() {
        // アクセスログ出力
        exportAccessLog("init", "ボタン「建物選択」押下");

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:init():START"));

        // 建物選択後、選択建物の企業情報を設定しているので建物選択に戻った時に
        // 操作中企業を自企業に戻しておく
        this.setLoginOperationCorp(this.getLoginCorp());

        conversationStart();

        buildingInfoSearchBeanProperty.setSearchedFlg(false); // 検索未実行

        changeColumnClassesStr();

        conditionList = getConditionList();
        prefectureMap = toolsPullDownList.getPrefecture();
        nyukyoTypeMap = toolsPullDownList.getTenantsForm();
        buildingStatusMap = createBuildingStatusMap();

        setAutoInspMonthMap(createAutoInspMonthMap("%d月", OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal()));
        setAutoInspDayMap(createAutoInspDayMap("%d日", OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal()));

        setConditionLimitCountMap(createConditionLimitCountMap());

        // 元のページを取得
        int currentPage = buildingInfoPagingList.getCurrentPage();
        executeSearch(conditionList);

        if (currentPage != 1) {
            // 元のページを設定
            buildingInfoPagingList.setPage(currentPage);
        }

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:init():END"));
        return "buildingSearch";
    }

    /**
     * 検索処理
     *
     * @return
     */
    @Logged
    public String search() {
        // アクセスログ出力
        exportAccessLog("search", "ボタン「検索開始」押下");

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:search():START"));

        List<Condition> conditionList = buildingInfoSearchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:search():END"));
        return "buildingSearch";
    }

    /**
     * 自動検針日 検針実行月プルダウンMapを生成.
     *
     * @param dayDispFormat 検針実行月表示フォーマット
     * @param noSelectFormat 未選択表示フォーマット
     * @return 自動検針日時 検針実行月プルダウンMap
     */
    private Map<String, String> createAutoInspMonthMap(String dispFormat, String noSelectFormat) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put(noSelectFormat, "");
        resultMap.putAll(createRangeValuePulldownMap(AUTO_INSP_MONTH_MIN, AUTO_INSP_MONTH_MAX, dispFormat));

        return resultMap;
    }

    /**
     * 自動検針日 日プルダウンMapを生成.
     *
     * @param dayDispFormat 自動検針日表示フォーマット
     * @param noSelectFormat 未選択表示フォーマット
     * @return 自動検針日時 日プルダウンMap
     */
    private Map<String, String> createAutoInspDayMap(String dispFormat, String noSelectFormat) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put(noSelectFormat, "");
        resultMap.putAll(createRangeValuePulldownMap(AUTO_INSP_DAY_MIN, AUTO_INSP_DAY_MAX, dispFormat));

        return resultMap;
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
        conditionLimitCountMap.put(SmsConstants.SEARCH_CONDITION_AUTO_INSP_MONTH,
                OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT);
        conditionLimitCountMap.put(SmsConstants.SEARCH_CONDITION_AUTO_INSP_DAY,
                OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT);

        return conditionLimitCountMap;
    }

    /**
     * 範囲値プルダウン用Mapを生成.
     *
     * @param minValue 最小値
     * @param maxValue 最大値
     * @param labelFormat 表示ラベルフォーマット
     * @return 範囲値プルダウン用Map
     */
    private Map<String, String> createRangeValuePulldownMap(int minValue, int maxValue, String labelFormat) {
        Map<String, String> rangeValuePulldownMap = new LinkedHashMap<>();

        for (int i = minValue; i <= maxValue; i++) {
            String dispStr = String.valueOf(i);
            if (!CheckUtility.isNullOrEmpty(labelFormat)) {
                dispStr = String.format(labelFormat, i);
            }

            rangeValuePulldownMap.put(dispStr, String.valueOf(i));
        }

        return rangeValuePulldownMap;
    }

    /**
     * 検索実処理
     *
     * @param conditionList 検索条件リスト
     */
    private void executeSearch(List<Condition> conditionList) {
        buildingInfoPagingList.init(searchBuildingInfo(conditionList));

        buildingInfoSearchBeanProperty.setSearchedFlg(true); // 検索実行済
    }

    /**
     * 建物情報検索
     *
     * @param conditionList 検索条件リスト
     * @return 建物情報リスト
     */
    private List<ListInfo> searchBuildingInfo(List<Condition> conditionList) {
        List<Object> targetCorpIdList = new ArrayList<>();
        List<Object> targetCorpIdOrNameList = new ArrayList<>();
        List<Object> targetBuildingNoOrNameList = new ArrayList<>();
        List<Object> targetPrefectureList = new ArrayList<>();
        List<Object> targetNyukyoTypeList = new ArrayList<>();
        List<Object> targetBuildingStateList = new ArrayList<>();
        List<BuildingAutoInspSearchCondition> targetAutoInspDateList = new ArrayList<>();

        Date targetDate = buildingInfoSearchDao.getSvDate();

        for (Condition condition : conditionList) {
            if (condition == null || condition.getSelectConditionCd() == null
                    || condition.getSelectConditionCd().equals(OsolConstants.DEFAULT_SELECT_BOX_VALUE)) {
                continue;
            }

            eventLogger.debug(this.getClass().getPackage().getName()
                    .concat(" selectConditionCd(" + condition.getSelectConditionCd() + ")"));
            switch (condition.getSelectConditionCd()) {

            case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                // 都道府県検索
                if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                    targetPrefectureList.add(condition.getPrefectureCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" prefectureCd(" + condition.getPrefectureCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD:
                // 入居形態検索
                if (!CheckUtility.isNullOrEmpty(condition.getNyukyoTypeCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getNyukyoTypeCd())) {
                    targetNyukyoTypeList.add(condition.getNyukyoTypeCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" nyukyoTypeCd(" + condition.getNyukyoTypeCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_BUILDING_STATE:
                // 建物状況検索
                if (!CheckUtility.isNullOrEmpty(condition.getBuildingStatusCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getBuildingStatusCd())) {
                    targetBuildingStateList.add(condition.getBuildingStatusCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" buildingStatusCd(" + condition.getBuildingStatusCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME:
                // 企業(IDまたは名)検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetCorpIdOrNameList.add(condition.getConditionKeyword());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" corpIdOrName(" + condition.getConditionKeyword() + ")"));
                }
                break;
            case OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY:
                // 建物番号または建物名検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetBuildingNoOrNameList.add(condition.getConditionKeyword());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" buildingNoOrName(" + condition.getConditionKeyword() + ")"));
                }
                break;

            case SmsConstants.SEARCH_CONDITION_AUTO_INSP_MONTH:
                // 検針実行月検索
                String autoInspMonth = condition.getAutoInspMonth();
                if (!CheckUtility.isNullOrEmpty(autoInspMonth)) {
                    targetAutoInspDateList.add(new BuildingAutoInspSearchCondition(autoInspMonth, null));
                    eventLogger.debug(
                            this.getClass().getPackage().getName().concat(" autoInspMonth(" + autoInspMonth + ")"));
                }

                break;

            case SmsConstants.SEARCH_CONDITION_AUTO_INSP_DAY:
                // 自動検針日検索
                String autoInspDay = condition.getAutoInspDay();
                if (!CheckUtility.isNullOrEmpty(autoInspDay)) {
                    targetAutoInspDateList.add(new BuildingAutoInspSearchCondition(null, autoInspDay));
                    eventLogger.debug(
                            this.getClass().getPackage().getName().concat(" autoInspDay(" + autoInspDay + ")"));
                }

                break;

            default:
                break;
            }
        }

        List<TBuilding> resultlist = buildingInfoSearchDao.getSearchList(targetCorpIdList, targetCorpIdOrNameList,
                targetNyukyoTypeList,
                targetPrefectureList, targetBuildingNoOrNameList, targetBuildingStateList, targetDate,
                this.getLoginCorpId(), this.getLoginPersonId(), this.getLoginCorpType(),
                this.getLoginPerson().getPersonType());

        return convertList(resultlist, targetAutoInspDateList);
    }

    public SearchBeanProperty getBuildingInfoSearchBeanProperty() {
        return buildingInfoSearchBeanProperty;
    }

    public void setBuildingInfoSearchBeanProperty(SearchBeanProperty buildingInfoSearchBeanProperty) {
        this.buildingInfoSearchBeanProperty = buildingInfoSearchBeanProperty;
    }

    /**
     * 都道府県リストboxの取得
     *
     * @return 都道府県リストbox
     */
    public Map<String, String> getPrefectureMap() {
        return prefectureMap;
    }

    /**
     * 入居形態リストboxの取得
     *
     * @return 入居形態リストbox
     */
    public Map<String, String> getNyukyoTypeMap() {
        return nyukyoTypeMap;
    }

    /**
     * 建物状況リストboxの取得
     *
     * @return 建物状況リストbox
     */
    public Map<String, String> getBuildingStatusMap() {
        return buildingStatusMap;
    }

    /**
     * 建物状況リストbox生成
     *
     * @return 建物状況リストbox
     */
    private Map<String, String> createBuildingStatusMap() {
        Map<String, String> targetMap = toolsPullDownList.getBuildingSituation();

        // Osaki権限以外は、削除済み　を表示しない
        if (!OsolConstants.CORP_TYPE.OSAKI.getVal().contains(getLoginCorpType())) {
            // 削除済みの値を送って名前を取得
            String deleteName = MapUtility.searchValueOfKeyName(targetMap,
                    OsolConstants.BUILDING_SITUATION.DELETED.getVal());
            if (!CheckUtility.isNullOrEmpty(deleteName)) {
                // nullか空出ない場合削除
                targetMap.remove(deleteName);
            }
        }

        return targetMap;
    }

    /**
     * エンティティを画面表示用オブジェクトに詰め替える
     *
     * @param orgList 建物DBデータ
     * @param targetAutoInspDateList 自動検針データ検索条件List
     * @return 建物選択画面表示データList
     */
    private List<ListInfo> convertList(List<TBuilding> orgList,
            List<BuildingAutoInspSearchCondition> targetAutoInspDateList) {

        // 自動検針データ検索条件指定フラグ
        boolean isSelectAutoInspDate = CollectionUtils.isNotEmpty(targetAutoInspDateList);

        // 自動検針日データListのMap
        Map<String, Map<Long, List<BuildingAutoInspResultSet>>> autoInspMap = autoInspDao
                .getMeterTypeAutoInspMap(orgList, targetAutoInspDateList);

        List<ListInfo> buildingInfoList = new ArrayList<>();

        Date nowDate = buildingInfoSearchDao.getSvDate();
        String nowDateString = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMM);

        for (TBuilding tBuilding : orgList) {
            TBuildingPK tBuildingPk = tBuilding.getId();
            String corpId = tBuildingPk.getCorpId();
            Long buildingId = tBuildingPk.getBuildingId();

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

            // 入居形態の取得
            if (nyukyoTypeMap == null) {
                nyukyoTypeMap = getNyukyoTypeMap();
            }
            if (tBuilding.getNyukyoTypeCd() != null
                    && !OsolConstants.NYUKYO_TYPE.NONE.getVal().equals(tBuilding.getNyukyoTypeCd())) {
                for (String key : nyukyoTypeMap.keySet()) {
                    if (nyukyoTypeMap.get(key).equals(tBuilding.getNyukyoTypeCd())) {
                        info.setNyukyoType(key);
                        break;
                    }
                }
            }

            // 建物状況
            String buildingStatus = STR_EMPTY;
            String totalEndYmString = DateUtility.changeDateFormat(tBuilding.getTotalEndYm(),
                    DateUtility.DATE_FORMAT_YYYYMM);
            String delDateString = DateUtility.changeDateFormat(tBuilding.getBuildingDelDate(),
                    DateUtility.DATE_FORMAT_YYYYMM);
            if ((totalEndYmString == null || totalEndYmString.compareTo(nowDateString) >= 0)
                    && delDateString == null) {
                // 稼働中
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.NOW.getVal());
            } else if (totalEndYmString != null && totalEndYmString.compareTo(nowDateString) < 0
                    && delDateString == null) {
                // 稼動終了
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.END.getVal());
            } else if (delDateString != null) {
                // 削除済
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.DELETED.getVal());
            }
            info.setBuildingStatus(buildingStatus);
            //建物・テナント
            info.setTenantFlg(BuildingUtility.isTenant(tBuilding.getBuildingType()));
            info.setBuildingTenant(BuildingUtility.getBuildingTenant(tBuilding.getBuildingType()));
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

            // 自動検針データ
            if (MapUtils.isEmpty(autoInspMap)) {
                if (isSelectAutoInspDate) {
                    // 検索条件指定時かつ自動検針データが取得できなかった場合
                    continue;
                }

            } else {
                Map<Long, List<BuildingAutoInspResultSet>> autoInspInnerMap = autoInspMap.get(corpId);
                List<BuildingAutoInspResultSet> autoInspDataList = null;
                if (MapUtils.isNotEmpty(autoInspInnerMap)) {
                    autoInspDataList = autoInspInnerMap.get(buildingId);
                }

                if (isSelectAutoInspDate && CollectionUtils.isEmpty(autoInspDataList)) {
                    // 検索条件指定時かつ自動検針データが取得できなかった場合
                    continue;
                }

                info.setAutoInspDateDispList(createAutoInspDateDispList(autoInspDataList, AUTO_INSP_DISP_ROW_COUNT));
            }

            buildingInfoList.add(info);
        }

        return buildingInfoList;
    }

    /**
     * 表示用自動検針日時Listを生成.
     *
     * @param autoInspDataList 自動検針日List
     * @param dispRowCount 表示行数
     * @return 表示用自動検針日時List
     */
    private List<AutoInspectionEditData> createAutoInspDateDispList(List<BuildingAutoInspResultSet> autoInspDataList,
            int dispRowCount) {

        if (CollectionUtils.isEmpty(autoInspDataList)) {
            return null;
        }

        List<AutoInspectionEditData> autoInspDateDispList = new ArrayList<>();
        for (int i = 0; i < autoInspDataList.size(); i++) {
            autoInspDateDispList.add(new AutoInspectionEditData(autoInspDataList.get(i)));

            if ((i + 1) >= dispRowCount) {
                break;
            }
        }
        return autoInspDateDispList;
    }

    /**
     * 画面表示用カラムのcss定義の切替え
     *
     */
    public void changeColumnClassesStr() {
        eventLogger.debug(packageName
                .concat(" buildingInfoSearchBean:changeColumnClassesStr():START"));

        if (this.isLoginOperationCorpSelected()) {
            // 企業特定後
            buildingInfoSearchBeanProperty.setColumnClassesStr(
                    "column_style_tenant_flg, "
                            + "column_style_building, "
                            + "column_style_address, "
                            + "column_style_status, "
                            + "column_style_auto_insp, "
                            + "column_style_auto_insp_btn, "
                            + "column_style_building_edit, "
                            + "column_style_facility_edit");

            buildingInfoSearchBeanProperty.setCorpId(this.getLoginOperationCorpId());

        } else {
            // 企業特定前
            buildingInfoSearchBeanProperty.setColumnClassesStr(
                    "column_style_corp, "
                            + "column_style_tenant_flg, "
                            + "column_style_building, "
                            + "column_style_address, "
                            + "column_style_status, "
                            + "column_style_auto_insp, "
                            + "column_style_auto_insp_btn, "
                            + "column_style_building_edit,"
                            + "column_style_facility");
            buildingInfoSearchBeanProperty.setCorpId(null);
        }

        eventLogger.debug(packageName
                .concat(" ColumnClassesStr(" + buildingInfoSearchBeanProperty.getColumnClassesStr() + ")"));
        eventLogger.debug(
                packageName.concat(" buildingInfoSearchBean:changeColumnClassesStr():END"));
    }

    /**
     * 自動検針ボタン押下時.
     *
     * @param item 建物情報
     * @throws Exception 例外
     */
    public void clickAutoInspectionBtn(ListInfo item) throws Exception {
        // アクセスログ出力
        exportAccessLog("clickAutoInspectionBtn", "ボタン「自動検針」押下");

        prepareAutoInspectionDataList(item);
    }

    /**
     * 自動検針データList準備処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @throws Exception 例外
     */
    private void prepareAutoInspectionDataList(ListInfo buildingInfo) throws Exception {
        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        Entry<String, String> devPrmMapEntry = toolsPullDownList.getDevPrm(corpId, buildingId).entrySet().stream()
                .findFirst().orElse(null);
        if (devPrmMapEntry != null) {
            ListSmsAutoInspectionResult result = getAutoInspectionData(buildingInfo);

            setAutoInspDataList(createAutoInspDataList(result.getAutoInspectionInfoList()));
        }
    }

    /**
     * 自動検針データを取得.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 自動検針データ
     */
    private ListSmsAutoInspectionResult getAutoInspectionData(ListInfo buildingInfo) {
        ListSmsAutoInspectionParameter parameter = new ListSmsAutoInspectionParameter();
        parameter.setCorpId(buildingInfo.getCorpId());
        parameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
        parameter.setTenant(buildingInfo.getTenantFlg());

        return callApiPost(parameter, ListSmsAutoInspectionResponse.class).getResult();
    }

    /**
     * 自動検針データListを生成.
     *
     * @param targetDataList 自動検針データList
     * @return 自動検針編集用データList
     * @throws Exception 例外
     */
    private List<AutoInspectionEditData> createAutoInspDataList(List<ListSmsAutoInspectionResultData> targetDataList)
            throws Exception {

        Map<Long, AutoInspectionEditData> editDataMap = new LinkedHashMap<>();
        for (long i = 1; i <= SmsConstants.DEV_METER_TYPE_COUNT; i++) {
            editDataMap.put(i, null);
        }

        for (ListSmsAutoInspectionResultData targetData : targetDataList) {
            editDataMap.replace(targetData.getMeterType(), new AutoInspectionEditData(targetData));
        }

        List<AutoInspectionEditData> editDataList = new ArrayList<>();
        Set<Entry<Long, AutoInspectionEditData>> editDataMapEntrySet = editDataMap.entrySet();
        for (Entry<Long, AutoInspectionEditData> editDataMapEntry : editDataMapEntrySet) {
            AutoInspectionEditData editData = editDataMapEntry.getValue();
            if (editData == null) {
                // 取得できないメーター種別行が存在する場合
                editData = new AutoInspectionEditData(editDataMapEntry.getKey());
            }

            editDataList.add(editData);
        }

        return editDataList;
    }

    public List<AutoInspectionEditData> getAutoInspDataList() {
        return autoInspDataList;
    }

    public void setAutoInspDataList(List<AutoInspectionEditData> autoInspDataList) {
        this.autoInspDataList = autoInspDataList;
    }

    /**
     * 画面遷移処理
     *
     * @return
     */
    public String execNextScreen(ListInfo item) {
        // アクセスログ出力
        exportAccessLog("execNextScreen", "ボタン「選択」押下");

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:execNextScreen():START"));
        //セッションの建物選択フラグを追加する
        setSessionParameter(SmsConstants.BUILDING_SELECTED_ATTR, SmsConstants.BUILDING_SELECTED);
        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:execNextScreen():END"));
        return topBean.initBuildingInfo(item);
    }

    public String getSelectedCorpId() {
        return selectedCorpId;
    }

    public void setSelectedCorpId(String selectedCorpId) {
        this.selectedCorpId = selectedCorpId;
    }

    /**
     * 前ページへ送るメソッド
     *
     * @return
     */
    public String prevPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("prevPage", "ページング「前へ」押下");

        buildingInfoPagingList.prevPage();
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

        buildingInfoPagingList.nextPage();
        return STR_EMPTY;
    }

    public String pageJump() {
        // アクセスログ出力
        exportAccessLog("pageJump", "ページングリンク押下");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            buildingInfoPagingList.setCurrentPage(Integer.valueOf(param));
        }
        return STR_EMPTY;
    }

    public InfoPagingList getBuildingInfoPagingList() {
        return buildingInfoPagingList;
    }

    public void setBuildingInfoPagingList(InfoPagingList buildingInfoPagingList) {
        this.buildingInfoPagingList = buildingInfoPagingList;
    }

    public List<Condition> getConditionList() {
        return buildingInfoSearchBeanProperty.getConditionList(this);
    }

    /**
     * 検索条件追加時.
     *
     * @param orderNo 選択された検索条件
     */
    public void changeConditionCd(String orderNo) {
        // アクセスログ出力
        exportAccessLog("changeConditionCd", "「検索条件」選択");

        buildingInfoSearchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        controlConditionMap(getConditionLimitCountMap(), buildingInfoSearchBeanProperty.getConditionList(this));
    }

    /**
     * 検索条件削除時.
     *
     * @param orderNo 削除された検索条件
     */
    public void deleteCondition(String orderNo) {
        // アクセスログ出力
        exportAccessLog("deleteCondition", "検索条件にてボタン「削除」押下");

        buildingInfoSearchBeanProperty.deleteCondition(this, orderNo);
        controlConditionMap(getConditionLimitCountMap(), buildingInfoSearchBeanProperty.getConditionList(this));
    }

    /**
     * 検索条件プルダウンMapを制御.
     *
     * @param conditionLimitCountMap 検索条件指定数上限Map
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

        buildingInfoSearchBeanProperty.updateConditionMap(this, conditionList.size() - 1);

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
                buildingInfoSearchBeanProperty.resetOrder();
                buildingInfoSearchBeanProperty.updateConditionMap(this, conditionList.size() - 1);
            }
        }
    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        // 最初に選択済み状態で表示しておく条件の一覧
        conditionList.addAll(createFixedConditionList());
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

        case (OsolConstants.SEARCH_CONDITION_BUILDING_GROUPING):
            // 建物グルーピング検索
            condition.setGroupingEnable(true);
            break;

        case (OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD):
            // 入居形態検索
            condition.setNyukyoTypeSelectEnable(true);
            break;

        case (OsolConstants.SEARCH_CONDITION_BUILDING_STATE):
            // 建物状況検索
            condition.setBuildingStatusSelectEnable(true);
            break;

        case SmsConstants.SEARCH_CONDITION_AUTO_INSP_MONTH:
            // 検針実行月検索
            condition.setAutoInspMonthSelectEnable(true);
            condition.setMultiSelectEnable(true); // 複数選択可能

            break;

        case SmsConstants.SEARCH_CONDITION_AUTO_INSP_DAY:
            // 自動検針日検索
            condition.setAutoInspDaySelectEnable(true);
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
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD);
        selectConditionList.add(SmsConstants.SEARCH_CONDITION_AUTO_INSP_MONTH);
        selectConditionList.add(SmsConstants.SEARCH_CONDITION_AUTO_INSP_DAY);

        return selectConditionList;
    }

    /**
     * 企業特定前の建物一覧出力確認メッセージ取得
     *
     * @return
     */
    public String getBuildingGroupingNotOutputMessage() {
        return beanMessages.getMessage("buildingExcelBean.warn.buildingGroupingNotOutput");
    }

    /**
     * 親グループリストBOXの取得
     * @return
     */
    public Map<String, String> getParentGroupMap() {
        parentGroupMap = toolsPullDownList.getParentGroup(true, OsolConstants.SELECT_BOX_KEY_SHITEI_NASHI,
                getLoginOperationCorpId());
        return parentGroupMap;
    }

    /**
     * 子グループリストBOXの取得
     * @return
     */
    public Map<String, String> getChildGroupMap() {
        List<Condition> conditionList = buildingInfoSearchBeanProperty.getConditionList(this);
        childGroupMap = new LinkedHashMap<>();
        childGroupMap.put(OsolConstants.SELECT_BOX_KEY_SHITEI_NASHI, OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE);
        if (conditionList == null) {
            return childGroupMap;
        }
        for (Condition condition : conditionList) {
            if (!OsolConstants.SEARCH_CONDITION_BUILDING_GROUPING.equals(condition.getSelectConditionCd())) {
                continue;
            }
            if (condition.getParentGroupId() == null) {
                // 親グループIDの指定無し
                return childGroupMap;
            }
            childGroupMap = toolsPullDownList.getChildGroup(
                    true, OsolConstants.SELECT_BOX_KEY_SHITEI_NASHI, getLoginOperationCorpId(),
                    new Long(condition.getParentGroupId()));
        }

        return childGroupMap;
    }

    public Map<String, String> getAutoInspMonthMap() {
        return autoInspMonthMap;
    }

    public void setAutoInspMonthMap(Map<String, String> autoInspMonthMap) {
        this.autoInspMonthMap = autoInspMonthMap;
    }

    public Map<String, String> getAutoInspDayMap() {
        return autoInspDayMap;
    }

    public void setAutoInspDayMap(Map<String, String> autoInspDayMap) {
        this.autoInspDayMap = autoInspDayMap;
    }

    public Map<String, Integer> getConditionLimitCountMap() {
        return conditionLimitCountMap;
    }

    public void setConditionLimitCountMap(Map<String, Integer> conditionLimitCountMap) {
        this.conditionLimitCountMap = conditionLimitCountMap;
    }
}
