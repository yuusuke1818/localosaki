package jp.co.osaki.sms.bean.sms.server.setting.handy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.ListSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.handy.ListSmsHandyDeviceRequest;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.ListSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.handy.ListSmsHandyDeviceDetailResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

/**
 * ハンディ端末 検索画面
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingHandySearchBean")
@ConversationScoped
public class SearchBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    //シリアライズID
    private static final long serialVersionUID = -1559572501989173120L;

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

    // ハンディ端末 登録・編集画面
    @Inject
    private EditBean editBean;

    //ハンディ端末 登録・編集画面用プロパティ
    @Inject
    private EditBeanProperty editBeanProperty;

    // 検索条件リスト
    private List<Condition> conditionList;

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat("smsServerSettingHandySearchBean:init():START"));

        conversationStart();

        // 検索未実行
        searchBeanProperty.setSearchedFlg(false);

        // 検索条件作成
        conditionList = searchBeanProperty.getConditionList(this);

        // セレクトボックス作成
        Map<String, String> handyDeviceIdMap = toolsPullDownList.getHandyDeviceId();
        searchBeanProperty.setHandyDeviceIdMap(handyDeviceIdMap);

        Map<String, String> handyDeviceStatusMap = toolsPullDownList.getDevStatus();
        searchBeanProperty.setHandyDeviceStatusMap(handyDeviceStatusMap);

        // 検索実行 ハンディ端末情報取得
        executeSearch(conditionList);

        eventLogger.debug(packageName.concat("smsServerSettingHandySearchBean:init():END"));

        return "handySearch";
    }

    /**
     * 検索処理
     *
     * @return
     */
    @Logged
    public String search() {
        eventLogger.debug(packageName.concat("smsServerSettingHandySearchBean:search():START"));

        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat("smsServerSettingHandySearchBean:search():END"));

        return "handySearch";
    }

    /**
     * 検索実処理
     *
     * @param conditionList 検索条件リスト
     */
    private void executeSearch(List<Condition> conditionList) {

        //ハンディ端末取得処理
        List<ListSmsHandyDeviceDetailResultData> list = searchHandyInfo(conditionList);

        // ページングリスト初期化
        infoPagingList.init(list);

        // 検索実行済
        searchBeanProperty.setSearchedFlg(true);
    }

    /**
     *  ハンディ端末情報取得
     *
     * @param conditionList
     * @return
     */
    private List<ListSmsHandyDeviceDetailResultData> searchHandyInfo(List<Condition> conditionList) {
        List<Object> targetDevIdOrNameList = new ArrayList<>();
        List<Object> targetDevIdList = new ArrayList<>();
        List<Object> targetDelFlgList = new ArrayList<>();

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
            case (OsolConstants.SEARCH_CONDITION_HANDY_ID_OR_NAME):
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getConditionKeyword())) {
                    targetDevIdOrNameList.add(condition.getConditionKeyword());
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" HandyNoOrName(" + condition.getConditionKeyword() + ")"));
                break;

            case (OsolConstants.SEARCH_CONDITION_HANDY_ID):
                if (!CheckUtility.isNullOrEmpty(condition.getDevId())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getDevId())) {
                    targetDevIdList.add(condition.getDevId());
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" devId(" + condition.getDevId() + ")"));
                break;

            case (OsolConstants.SEARCH_CONDITION_HANDY_STATUS):
                if (!CheckUtility.isNullOrEmpty(condition.getDelFlg())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getDelFlg())) {
                    targetDelFlgList.add(condition.getDelFlg());
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" delFlg(" + condition.getDelFlg() + ")"));
                break;

            default:
                break;
            }
        }

        // 装置取得
        ListSmsHandyDeviceResponse response = new ListSmsHandyDeviceResponse();
        ListSmsHandyDeviceParameter parameter = new ListSmsHandyDeviceParameter();
        ListSmsHandyDeviceRequest request = new ListSmsHandyDeviceRequest();

        request.setDevIdOrNameList(targetDevIdOrNameList);
        request.setDevIdList(targetDevIdList);
        request.setDelFlgList(targetDelFlgList);

        //リクエストパラメータセット
        parameter.setBean("ListSmsHandyDeviceBean");
        parameter.setRequest(request);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsHandyDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        List<ListSmsHandyDeviceDetailResultData> list = null;

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
     * 登録・編集画面 遷移処理
     *
     * @return
     */
    @Logged
    public String execNextScreen(ListSmsHandyDeviceDetailResultData item) {
        editBeanProperty = new EditBeanProperty();
        if (Objects.isNull(item)) {
            // 新規登録
            editBeanProperty.setUpdateProcessFlg(false);
        } else {
            // 編集
            editBeanProperty.setUpdateProcessFlg(true);
            editBeanProperty.setDevId(item.getDevId());
        }
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
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_HANDY_ID_OR_NAME);
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
        case (OsolConstants.SEARCH_CONDITION_HANDY_ID):
            // 装置検索
            condition.setDevIdSelectEnable(true);
            condition.setMultiSelectEnable(true); // 複数選択可能
            break;

        case (OsolConstants.SEARCH_CONDITION_HANDY_STATUS):
            // 状態検索
            condition.setDelFlgSelectEnable(true);
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
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_HANDY_ID_OR_NAME);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_HANDY_ID);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_HANDY_STATUS);
        for (String selectCondition : selectConditionList) {
            selectConditionMap.put(selectCondition, selectCondition);
        }

        return selectConditionMap;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

}
