package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsBuildingParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.buildingdevice.ListSmsBuildingRequest;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.ListSmsBuildingResponse;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsBuildingDetailResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MPersonDao;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

/**
 * 建物装置設定 建物検索画面
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingBuildingDeviceSearchBean")
@ConversationScoped
public class SearchBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    //シリアライズID
    private static final long serialVersionUID = 1439874465604179945L;

    //当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private SearchBeanProperty searchBeanProperty;

    // ページング処理
    @Inject
    private InfoPagingList infoPagingList;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // 汎用区分マスタ
    @Inject
    private GenericTypeList genericTypeList;

    // 時間取得用
    @EJB
    MPersonDao mPersonDao;

    // 装置 登録・編集画面

    @Inject
    private EditBean editBean;

    //装置 登録・編集画面用プロパティ
    @Inject
    private EditBeanProperty editBeanProperty;

    // 検索条件リスト
    private List<Condition> conditionList;

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceSearchBean:init():START"));

        conversationStart();

        // 検索未実行
        searchBeanProperty.setSearchedFlg(false);

        // 検索条件作成
        conditionList = searchBeanProperty.getConditionList(this);

        // セレクトボックス作成
        Map<String, String> prefectureMap = toolsPullDownList.getPrefecture();
        searchBeanProperty.setPrefectureMap(prefectureMap);

        Map<String, String> nyukyoTypeMap = toolsPullDownList.getTenantsForm();
        searchBeanProperty.setNyukyoTypeMap(nyukyoTypeMap);

        Map<String, String> buildingStatusMap = toolsPullDownList.getBuildingSituation();
        searchBeanProperty.setBuildingStatusMap(buildingStatusMap);

        // 検索実行 装置情報取得
        executeSearch(conditionList);

        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceSearchBean:init():END"));

        return "buildingdeviceSearch";
    }

    /**
     * 検索処理
     *
     * @return
     */
    @Logged
    public String search() {
        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceSearchBean:search():START"));

        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceSearchBean:search():END"));

        return "buildingdeviceSearch";
    }

    /**
     * 検索実処理
     *
     * @param conditionList 検索条件リスト
     */
    private void executeSearch(List<Condition> conditionList) {

        // 建物取得処理
        List<ListSmsBuildingDetailResultData> buildingInfoList = searchBuildingInfo(conditionList);

        // 画面用オブジェクトへ乗せ換え
        List<BuildingInfo> dispBuildingInfoList = convertBuildingInfoList(buildingInfoList);

        // ページングリスト初期化
        infoPagingList.init(dispBuildingInfoList);

        // 検索実行済
        searchBeanProperty.setSearchedFlg(true);
    }

    /**
     *  装置情報取得
     *
     * @param conditionList
     * @return
     */
    private List<ListSmsBuildingDetailResultData> searchBuildingInfo(List<Condition> conditionList) {

        List<Object> targetCorpIdOrNameList = new ArrayList<>();
        List<Object> targetBuildingNoOrNameList = new ArrayList<>();
        List<Object> targetPrefectureList = new ArrayList<>();
        List<Object> targetBuildingStateList = new ArrayList<>();
        List<Object> targetNyukyoTypeList = new ArrayList<>();

        // パラメータ用 検索条件作成
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

            switch (condition.getSelectConditionCd()) {
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

            case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                // 都道府県検索
                if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                    targetPrefectureList.add(condition.getPrefectureCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" prefectureCd(" + condition.getPrefectureCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_BUILDING_STATE:
                // 稼働状況検索
                if (!CheckUtility.isNullOrEmpty(condition.getBuildingStatusCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getBuildingStatusCd())) {
                    targetBuildingStateList.add(condition.getBuildingStatusCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" buildingStatusCd(" + condition.getBuildingStatusCd() + ")"));
                }
                break;

            case OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD:
                // 入居形態検索
                if (!CheckUtility.isNullOrEmpty(condition.getNyukyoTypeCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getNyukyoTypeCd())) {
                    targetNyukyoTypeList.add(condition.getNyukyoTypeCd());
                    eventLogger.debug(this.getClass().getPackage().getName()
                            .concat(" nyukyoTypeCd(" + condition.getBuildingStatusCd() + ")"));
                }
                break;

            default:
                break;
            }
        }

        // 装置取得
        ListSmsBuildingResponse response = new ListSmsBuildingResponse();
        ListSmsBuildingParameter parameter = new ListSmsBuildingParameter();
        ListSmsBuildingRequest request = new ListSmsBuildingRequest();

        request.setCorpIdOrNameList(targetCorpIdOrNameList);
        request.setBuildingNoOrNameList(targetBuildingNoOrNameList);
        request.setPrefectureList(targetPrefectureList);
        request.setBuildingStateList(targetBuildingStateList);
        request.setNyukyoTypeList(targetNyukyoTypeList);

        //リクエストパラメータセット
        parameter.setBean("ListSmsBuildingBean");
        parameter.setRequest(request);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsBuildingResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        List<ListSmsBuildingDetailResultData> list = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            list = response.getResult().getDetailList();
        }

        return list;
    }

    /**
     * 画面用オブジェクトに置き換え
     *
     * @param orgList
     * @return
     */
    private List<BuildingInfo> convertBuildingInfoList(List<ListSmsBuildingDetailResultData> orgList) {
        List<BuildingInfo> buildingInfoList = new ArrayList<>();

        if (Objects.isNull(orgList)) {
            return buildingInfoList;
        }

        Date nowDate = mPersonDao.getSvDate();
        String nowDateString = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMM);

        for (ListSmsBuildingDetailResultData result : orgList) {
            BuildingInfo info = new BuildingInfo();
            info.setCorpId(result.getCorpId());
            info.setCorpName(result.getCorpName());
            info.setBuildingId(result.getBuildingId());
            info.setBuildingName(result.getBuildingName());
            info.setBuildingNo(result.getBuildingNo());

            // 入居形態
            if (Objects.nonNull(result.getNyukyoTypeCd())
                    && !OsolConstants.NYUKYO_TYPE.NONE.getVal().equals(result.getNyukyoTypeCd())) {
                for (String key : searchBeanProperty.getNyukyoTypeMap().keySet()) {
                    if (searchBeanProperty.getNyukyoTypeMap().get(key).equals(result.getNyukyoTypeCd())) {
                        info.setNyukyoType(key);
                        break;
                    }
                }
            }

            // 住所 (住所情報の結合)
            String buildingAddress = STR_EMPTY;
            if (Objects.nonNull(result.getPrefectureName())) {
                buildingAddress += result.getPrefectureName();
            }
            if (Objects.nonNull(result.getAddress())) {
                buildingAddress += result.getAddress();
            }
            if (Objects.nonNull(result.getAddressBuilding())) {
                buildingAddress += result.getAddressBuilding();
            }
            info.setAddress(buildingAddress);

            // 建物状況
            String buildingStatus = STR_EMPTY;
            String totalEndYmString = DateUtility.changeDateFormat(result.getTotalEndYm(),
                    DateUtility.DATE_FORMAT_YYYYMM);
            String delDateString = DateUtility.changeDateFormat(result.getBuildingDelDate(),
                    DateUtility.DATE_FORMAT_YYYYMM);
            if ((Objects.isNull(totalEndYmString) || totalEndYmString.compareTo(nowDateString) >= 0)
                    && Objects.isNull(delDateString)) {
                // 稼働中
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.NOW.getVal());
            } else if (Objects.nonNull(totalEndYmString) && totalEndYmString.compareTo(nowDateString) < 0
                    && Objects.isNull(delDateString)) {
                // 稼動終了
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.END.getVal());
            } else if (Objects.nonNull(delDateString)) {
                // 削除済
                buildingStatus = genericTypeList
                        .getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.DELETED.getVal());
            }
            info.setBuildingStatus(buildingStatus);

            buildingInfoList.add(info);
        }
        return buildingInfoList;

    }

    /**
     * 登録・編集画面 遷移処理
     *
     * @return
     */
    @Logged
    public String execNextScreen(BuildingInfo item) {
        editBeanProperty = new EditBeanProperty();
        editBeanProperty.setBuildingInfo(item);
        return editBean.init(editBeanProperty);
    }

    /**
     * 画面用データ
     *
     * @return
     */
    public SearchBeanProperty getSearchBeanProperty() {
        return searchBeanProperty;
    }

    /**
     * 前ページへ送るメソッド
     *
     * @param event
     * @return
     */
    @Logged
    public String prevPage(ActionEvent event) {
        infoPagingList.prevPage();
        return STR_EMPTY;
    }

    /**
     * 先ページへ送るメソッド
     *
     * @param event
     * @return
     */
    @Logged
    public String nextPage(ActionEvent event) {
        infoPagingList.nextPage();
        return STR_EMPTY;
    }

    @Logged
    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            infoPagingList.setCurrentPage(Integer.valueOf(param));
        }
        return STR_EMPTY;
    }

    public InfoPagingList getInfoPagingList() {
        return infoPagingList;
    }

    public void setInfoPagingList(InfoPagingList infoPagingList) {
        this.infoPagingList = infoPagingList;
    }

    /**
     * 検索条件が追加されたときに呼ばれる
     *
     * @param orderNo 選択された検索条件
     */
    @Logged
    public void changeConditionCd(String orderNo) {
        searchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
    }

    /**
     * 検索条件が削除されたときに呼ばれる
     *
     * @param orderNo
     */
    @Logged
    public void deleteCondition(String orderNo) {
        //テナント所属建物・建物所属テナントの検索が削除された場合は内容を初期化する
        List<Condition> conditionList = searchBeanProperty.getConditionList(this);

        //条件削除
        searchBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if (searchBeanProperty.getSelectConditionMap(this).size() == 3
                || searchBeanProperty.getSelectConditionMap(this).size() == 4) {
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            Condition condition = conditionList.get(conditionList.size() - 1);
            if (condition instanceof BaseSearchCondition) {
                BaseSearchCondition base = (BaseSearchCondition) condition;
                if (!base.isDefaultCondition()) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }

        searchBeanProperty.resetOrder();
        searchBeanProperty.updateConditionMap(this, conditionList.size() - 1);
    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        // 最初に選択済み状態で表示しておく条件の一覧
        Condition condition = new Condition();
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_BUILDING_ONLY);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setDeleteButtonEnable(false);
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

        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {
        // 一致条件表示
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
        condition.setSelectEnable(false);
        condition.setDeleteButtonEnable(true);
        condition.setSearchSubjectConjunctionEnable(true);

        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        case (OsolConstants.SEARCH_CONDITION_PREFECTURE):
            // 都道府県検索
            condition.setPrefectureSelectEnable(true);
            condition.setMultiSelectEnable(true); // 複数選択可能
            break;

        case (OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD):
            // 入居形態検索
            condition.setNyukyoTypeSelectEnable(true);
            break;

        case (OsolConstants.SEARCH_CONDITION_BUILDING_STATE):
            // 建物状況検索
            condition.setBuildingStatusSelectEnable(true);
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
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_PREFECTURE);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_NYUKYO_TYPE_CD);
        for (String selectCondition : selectConditionList) {
            selectConditionMap.put(selectCondition, selectCondition);
        }

        return selectConditionMap;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

}
