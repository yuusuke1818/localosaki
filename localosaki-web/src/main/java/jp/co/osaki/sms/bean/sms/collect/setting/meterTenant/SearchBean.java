package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.TenantSmsDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.BuildingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.TBuildingJoinBuildingInfoListDao;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

@Named("smsCollectSettingMeterTenantSearchBean")
@ConversationScoped
public class SearchBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    // シリアライズID
    private static final long serialVersionUID = 6999026810860110367L;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // 画面受け渡し用
    private String selectedCorpId;

    // 検索条件リスト
    private List<Condition> conditionList;

    // 都道府県用セレクトボックス
    private Map<String, String> prefectureMap;

    // 建物状況用セレクトボックス
    private Map<String, String> buildingStatusMap;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // ページング処理
    @Inject
    private InfoPagingList tenantInfoPagingList;

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    // 汎用区分マスタ
    @Inject
    private GenericTypeList genericTypeList;

    // メーターテナント管理画面
    @Inject
    private EditBean tenantInfoEditBean;

    // メーターテナント管理画面プロパティ
    @Inject
    private EditBeanProperty editBeanProperty;

    // 検索条件格納クラス
    @Inject
    private SearchBeanProperty buildingInfoSearchBeanProperty;

    // メーターテナント一括登録画面
    @Inject
    private BulkEditBean tenantInfoBulkEditBean;

    // 建物検索（テナント）Dao
    @EJB
    private TBuildingJoinBuildingInfoListDao buildingJoinBuildingInfoListDao;

    // SMSテナントフィルター
    @EJB
    private TenantSmsDataFilterDao tenantSmsDataFilterDao;

    @Inject
    private TopBean topBean;

    /**
     * 初期処理
     *
     * @return
     */
    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:init():START"));

        conversationStart();

        editBeanProperty = new EditBeanProperty();
        buildingInfoSearchBeanProperty.setSearchedFlg(false); // 検索未実行

        changeColumnClassesStr();
        conditionList = buildingInfoSearchBeanProperty.getConditionList(this);
        prefectureMap = toolsPullDownList.getPrefecture();

        buildingStatusMap = createBuildingStatusMap();

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:init():END"));
        return "meterTenantSearch";
    }

    /**
     * 検索処理
     *
     * @return
     */
    @Logged
    public String search() {
        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:search():START"));

        List<Condition> conditionList = buildingInfoSearchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat(" buildingInfoSearchBean:search():END"));
        return "meterTenantSearch";
    }

    /**
     * 検索実処理
     *
     * @param conditionList 検索条件リスト
     */
    private void executeSearch(List<Condition> conditionList) {
        tenantInfoPagingList.init(searchBuildingInfo(conditionList));

        buildingInfoSearchBeanProperty.setSearchedFlg(true); // 検索実行済
    }

    /**
     * 建物情報検索
     *
     * @param conditionList 検索条件リスト
     * @return 建物情報リスト
     */
    private List<ListInfo> searchBuildingInfo(List<Condition> conditionList) {

        List<Object> targetBuildingNoOrNameList = new ArrayList<>();
        List<Object> targetPrefectureList = new ArrayList<>();
        List<Object> targetBuildingStateList = new ArrayList<>();
        List<Object> targetBuildingTenantList = new ArrayList<>();
        List<Object> targetBorrowCorpList = new ArrayList<>();
        List<Object> targetBorrowBuildingList = new ArrayList<>();
        List<Object> targetTenantIdList = new ArrayList<>();
        Date targetDate = buildingJoinBuildingInfoListDao.getSvDate();

        for (Condition condition : conditionList) {
            if (condition == null) {
                continue;
            }

            String selectConditionCd = condition.getSelectConditionCd();
            if (CheckUtility.isNullOrEmpty(selectConditionCd)
                    || OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(selectConditionCd)) {
                continue;
            }

            eventLogger.debug(packageName.concat(" selectConditionCd(" + selectConditionCd + ")"));

            switch (selectConditionCd) {
            case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                // 都道府県検索
                if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                    targetPrefectureList.add(condition.getPrefectureCd());
                    eventLogger.debug(this.getClass().getPackage().getName().concat(" prefectureCd(" + condition.getPrefectureCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_BUILDING_STATE:
                // 稼働状況検索
                if (!CheckUtility.isNullOrEmpty(condition.getBuildingStatusCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getBuildingStatusCd())) {
                    targetBuildingStateList.add(condition.getBuildingStatusCd());
                    eventLogger.debug(this.getClass().getPackage().getName().concat(" buildingStatusCd(" + condition.getBuildingStatusCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY:
                //テナント番号またはテナント名検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetBuildingNoOrNameList.add(condition.getConditionKeyword());
                    eventLogger.debug(this.getClass().getPackage().getName().concat(" buildingNoOrName(" + condition.getConditionKeyword() + ")"));
                }
                break;

            case SmsConstants.SEARCH_CONDITION_TENANT_ID:
                //ユーザーコード検索
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetTenantIdList.add(condition.getConditionKeyword());
                    eventLogger.debug(this.getClass().getPackage().getName().concat(" tenantId(" + condition.getConditionKeyword() + ")"));
                }
                break;

            default:
                break;
            }
        }

        // 建物・テナント検索（テナントを指定）
        targetBuildingTenantList.add(OsolConstants.BUILDING_TYPE.TENANT.getVal());

        // 建物に属するテナント検索（企業ID指定）
        targetBorrowCorpList.add(topBean.getTopBeanProperty().getListInfo().getCorpId());

        // 建物に属するテナント検索（建物ID指定）
        targetBorrowBuildingList.add(topBean.getTopBeanProperty().getListInfo().getBuildingId());

        // 建物に紐づくテナント一覧取得
        List<TBuilding> resultlist = buildingJoinBuildingInfoListDao.getSearchSmsList(
                targetPrefectureList, targetBuildingNoOrNameList, targetBuildingStateList, targetBuildingTenantList, targetBorrowCorpList, targetBorrowBuildingList,
                targetTenantIdList, targetDate);

        // SMSテナントフィルター
        List<ListInfo>convertedList = convertList(tenantSmsDataFilterDao.applyDataFilter(resultlist, new BuildingPersonDevDataParam(
                                topBean.getTopBeanProperty().getListInfo().getCorpId(), Long.valueOf(topBean.getTopBeanProperty().getListInfo().getBuildingId()),
                                this.getLoginCorpId(), this.getLoginPersonId())));

        // ユーザーコードを昇順にソートする
        Collections.sort(convertedList, new Comparator<ListInfo>() {
            @Override
            public int compare(ListInfo o1, ListInfo o2) {
                return Long.valueOf(o1.getTenantId()).compareTo(Long.valueOf(o2.getTenantId())) < 0 ? -1 : 1;
            }
        });
        return convertedList;
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
     * @param orgList
     * @return
     */
    private List<ListInfo> convertList(List<TBuilding> orgList) {
        List<ListInfo> buildingInfoList = new ArrayList<>();

        Date nowDate = buildingJoinBuildingInfoListDao.getSvDate();
        String nowDateString = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMM);

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

            // ユーザーコード（SMSテナント識別ID）取得（SMSテナント情報は登録されているはず）
            MTenantSm mTenantSm = buildingJoinBuildingInfoListDao.getSearchSmsTenant(tBuilding.getId().getCorpId(), tBuilding.getId().getBuildingId());
            if (mTenantSm != null) {
                info.setTenantId(mTenantSm.getTenantId().toString());
            }

            buildingInfoList.add(info);
        }
        return buildingInfoList;
    }

    /**
     * 画面表示用カラムのcss定義の切替え
     *
     */
    public void changeColumnClassesStr() {
        eventLogger.debug(packageName
                .concat(" buildingInfoSearchBean:changeColumnClassesStr():START"));

        buildingInfoSearchBeanProperty.setColumnClassesStr(
                "column_style_tenant_flg, "
                        + "column_style_tenant_id, "
                        + "column_style_building, "
                        + "column_style_address, "
                        + "column_style_status, "
                        + "column_style_building_edit, "
                        + "column_style_building_copy");

        eventLogger.debug(packageName
                .concat(" ColumnClassesStr(" + buildingInfoSearchBeanProperty.getColumnClassesStr() + ")"));
        eventLogger.debug(
                packageName.concat(" buildingInfoSearchBean:changeColumnClassesStr():END"));
    }

    /**
     * 画面遷移処理
     *
     * @return
     */
    @Logged
    public String execNextScreen(ListInfo item, int updateFlg) {
        eventLogger.debug(packageName.concat(" smsCollectSettingTenantSearchBean:execNextScreen():START"));

        // 初期化
        editBeanProperty = new EditBeanProperty();

        // 複製の場合
        if (OsolConstants.FLG_OFF.compareTo(updateFlg) == 0 && item != null) {
            // 親となる建物情報
            // 所属企業ID
            editBeanProperty.setDivisionCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
            // 所属建物ID
            editBeanProperty.setDivisionBuildingId(Long.parseLong(topBean.getTopBeanProperty().getListInfo().getBuildingId()));
            // 所属建物番号
            editBeanProperty.setDivisionBuildingNo(topBean.getTopBeanProperty().getListInfo().getBuildingNo());
            // 所属建物名
            editBeanProperty.setDivisionBuildingName(topBean.getTopBeanProperty().getListInfo().getBuildingName());

            // 選択テナントの検索条件
            // 企業ID
            editBeanProperty.setCorpId(item.getCorpId());
            // 建物ID
            editBeanProperty.setBuildingId(Long.parseLong(item.getBuildingId()));

            // 建物・テナント
            editBeanProperty.setBuildingTenant(OsolConstants.BUILDING_TENANT.TENANT.getName());
            // テナントフラグ
            editBeanProperty.setTenantFlg(true);

            // 複製（新規登録扱い）
            editBeanProperty.setUpdateProcessFlg(false);

            eventLogger.debug(packageName.concat(" smsCollectSettingTenantSearchBean:execNextScreen():END"));
            return tenantInfoEditBean.init(editBeanProperty);
        }
        // 新規または、更新の場合
        else {
            eventLogger.debug(packageName.concat(" smsCollectSettingTenantSearchBean:execNextScreen():END"));
            return execNextScreen(item);
        }
    }

    /**
     * 画面遷移処理
     *
     * @return
     */
    public String execNextScreen(ListInfo item) {

        // 初期化
        editBeanProperty = new EditBeanProperty();

        // 新規登録ボタン押下
        if (item == null) {
            // 企業ID
            editBeanProperty.setCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
            // 所属企業ID
            editBeanProperty.setDivisionCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
            // 所属建物ID
            editBeanProperty.setDivisionBuildingId(Long.parseLong(topBean.getTopBeanProperty().getListInfo().getBuildingId()));
            // 所属建物番号
            editBeanProperty.setDivisionBuildingNo(topBean.getTopBeanProperty().getListInfo().getBuildingNo());
            // 所属建物名
            editBeanProperty.setDivisionBuildingName(topBean.getTopBeanProperty().getListInfo().getBuildingName());
            // 建物・テナント
            editBeanProperty.setBuildingTenant(OsolConstants.BUILDING_TENANT.TENANT.getName());
            // テナントフラグ
            editBeanProperty.setTenantFlg(true);

            // 新規登録
            editBeanProperty.setUpdateProcessFlg(false);
        } else {
            // 編集ボタン押下

            // 選択テナントの検索条件
            // 企業ID
            editBeanProperty.setCorpId(item.getCorpId());
            // 建物ID
            editBeanProperty.setBuildingId(Long.parseLong(item.getBuildingId()));
            // 所属企業ID
            editBeanProperty.setDivisionCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
            // 所属建物ID
            editBeanProperty.setDivisionBuildingId(Long.parseLong(topBean.getTopBeanProperty().getListInfo().getBuildingId()));
            // 所属建物番号
            editBeanProperty.setDivisionBuildingNo(topBean.getTopBeanProperty().getListInfo().getBuildingNo());
            // 所属建物名
            editBeanProperty.setDivisionBuildingName(topBean.getTopBeanProperty().getListInfo().getBuildingName());

            // 建物・テナント
            editBeanProperty.setBuildingTenant(OsolConstants.BUILDING_TENANT.TENANT.getName());
            // テナントフラグ
            editBeanProperty.setTenantFlg(true);

            // 更新
            editBeanProperty.setUpdateProcessFlg(true);

        }
        return tenantInfoEditBean.init(editBeanProperty);
    }

    //一括登録 画面遷移
    @Logged
    public String execMoveBulkEdit() {
        return tenantInfoBulkEditBean.init();
    }


    public String registerBulkScreen() {

        return "meterTenantBulk";
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
    @Logged
    public String prevPage(ActionEvent event) {
        tenantInfoPagingList.prevPage();
        return STR_EMPTY;
    }

    /**
     * 先ページへ送るメソッド
     *
     * @return
     */
    @Logged
    public String nextPage(ActionEvent event) {
        tenantInfoPagingList.nextPage();
        return STR_EMPTY;
    }

    @Logged
    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            tenantInfoPagingList.setCurrentPage(Integer.valueOf(param));
        }
        return STR_EMPTY;
    }

    public InfoPagingList getTenantInfoPagingList() {
        return tenantInfoPagingList;
    }

    public void setTenantInfoPagingList(InfoPagingList tenantInfoPagingList) {
        this.tenantInfoPagingList = tenantInfoPagingList;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    /**
     * 検索条件が追加されたときに呼ばれる
     *
     * @param orderNo 選択された検索条件
     */
    @Logged
    public void changeConditionCd(String orderNo) {
        buildingInfoSearchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        List<Condition> conditionList = buildingInfoSearchBeanProperty.getConditionList(this);
        Map<String, String> dropdown = conditionList.get(conditionList.size() - 1).getSelectConditionMap();

        int prefectureCnt = 0;
        boolean prefectureFlg = false;
        for (Condition condition : conditionList) {
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                prefectureCnt++;
                if (prefectureCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    dropdown.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                    prefectureFlg = true;
                }
            }
        }

        //ドロップダウンリストを手動で削除した影響で条件追加のが消えないため、削除制御をここで実施。
        if (prefectureFlg && buildingInfoSearchBeanProperty.getSelectConditionMap(this).size() == 2) {
            //ドロップダウンリストから項目が無くなったら、ドロップダウン自体を削除する
            conditionList.remove(conditionList.size() - 1);
        }

    }

    @Logged
    public void deleteCondition(String orderNo) {
        List<Condition> conditionList = buildingInfoSearchBeanProperty.getConditionList(this);

        //条件削除
        buildingInfoSearchBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if (buildingInfoSearchBeanProperty.getSelectConditionMap(this).size() == 2
                || buildingInfoSearchBeanProperty.getSelectConditionMap(this).size() == 3) {
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            Condition condition = conditionList.get(conditionList.size() - 1);
            if (condition instanceof BaseSearchCondition) {
                BaseSearchCondition base = (BaseSearchCondition) condition;
                if (!base.isDefaultCondition()) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }
        buildingInfoSearchBeanProperty.resetOrder();
        buildingInfoSearchBeanProperty.updateConditionMap(this, conditionList.size() - 1);

        int prefectureCnt = 0;
        Map<String, String> conditionListMap = conditionList.get(conditionList.size() - 1).getSelectConditionMap();
        for (Condition condition : conditionList) {
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                prefectureCnt++;
                if (prefectureCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    conditionListMap.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                }
            }
        }

    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        // 最初に選択済み状態で表示しておく条件の一覧
        Condition condition = new Condition();
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY);
        condition.setDeleteButtonEnable(false);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setTenantIdSelectEnable(false);

        conditionList.add(condition);
    }

    @Override
    public Condition createDefaultCondition() {
        // 未選択状態の条件（「条件を追加」）
        Condition condition = new Condition();
        condition.setSelectEnable(true);
        condition.setSearchSubjectConjunctionEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        condition.setTenantIdSelectEnable(false);

        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {
        // 一致条件表示
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
        condition.setSearchSubjectConjunction("が");        //接続詞文言
        condition.setSearchSubjectConjunctionEnable(true);  //接続詞
        condition.setDeleteButtonEnable(true);
        condition.setSelectEnable(false);

        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        case (OsolConstants.SEARCH_CONDITION_PREFECTURE):
            // 都道府県検索
            condition.setPrefectureSelectEnable(true);
            condition.setMultiSelectEnable(true); // 複数選択可能
            break;

        case (OsolConstants.SEARCH_CONDITION_BUILDING_STATE):
            // 建物状況検索
            condition.setBuildingStatusSelectEnable(true);
            break;

        case (SmsConstants.SEARCH_CONDITION_TENANT_ID):
            // ユーザーコード検索
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setTenantIdSelectEnable(true);
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

        List<String> selectConditionList = new ArrayList<>();
        // プルダウンの検索条件には表示されないが、検索条件を使い切ったかの判定に必要
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY);

        selectConditionList.add(OsolConstants.SEARCH_CONDITION_PREFECTURE);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        selectConditionList.add(SmsConstants.SEARCH_CONDITION_TENANT_ID);

        for (String selectCondition : selectConditionList) {
            selectConditionMap.put(selectCondition, selectCondition);
        }

        return selectConditionMap;
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
     * 検索条件テキストボックスの最大文字数 <br />
     * デフォルト：50文字 <br />
     * ユーザーコード：6文字
     * @param tenantIdSelectEnable
     * @return
     */
    public int getConditionKeywordMaxLength(boolean tenantIdSelectEnable) {
        return tenantIdSelectEnable ? 6 : 50;
    }

    /**
     * 担当者の権限有無で登録系機能を制限
     * @return true:許可、false:許可しない
     */
    public boolean isSmsAuthControl() {
        return super.isSmsAuthControl();
    }

}
