package jp.co.osaki.sms.bean.common.municipality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MMunicipalityDao;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

@Named(value = "commonSearchMunicipalityBean")
@ConversationScoped
public class SearchBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    private static final long serialVersionUID = 5448943654464036682L;

    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private SearchBeanProperty searchBeanProperty;

    @Inject
    private MunicipalitySelectPaging municipalitySelectPaging;

    @EJB
    private MMunicipalityDao mMunicipalityDao;

    private Map<String, String> prefectureMap;

    // 検索実行済みフラグ
    private boolean searchedFlg;

    @Override
    public String init() {
        conversationStart();
        return "";
    }

    public String select(MunicipalityInfo param) {
        // アクセスログ出力
        exportAccessLog("select", "ボタン「選択」押下");

        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elc = context.getELContext();
        UIComponent root = context.getViewRoot();//.getChildren().get(1);
        UIComponent ui;
        ui = findComponent(root, "prefectureCd");
        if (ui instanceof UIOutput) {
            String prefectureCd = prefectureMap.get(param.getPrefectureCd());
            UIOutput inp = (UIOutput) ui;
            inp.setValue(prefectureCd);

            ValueExpression ve = inp.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elc, prefectureCd);
            }
        }
        ui = findComponent(root, "municipalityCd");
        if (ui instanceof UIOutput) {
            UIOutput inp = (UIOutput) ui;
            inp.setValue(param.getMunicipalityCd());

            ValueExpression ve = inp.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elc, param.getMunicipalityCd());
            }
        }
        ui = findComponent(root, "municipalityName");
        if (ui instanceof UIOutput) {
            UIOutput inp = (UIOutput) ui;
            inp.setValue(param.getMunicipalityName());

            ValueExpression ve = inp.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elc, param.getMunicipalityName());
            }
        }

        // 選択後初期化処理してみる
        clearMunicipalityForm();
        return "";
    }

    @Override
    public void initialConditionList(List<Condition> list) {
        //最初に選択済み状態で表示しておく条件の一覧
        Condition condition = new Condition();
        condition.setSelectEnable(false);
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_PREFECTURE);
        condition.setPrefectureSelectEnable(true);
        condition.setKeywordSelectEnable(false);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
        condition.setDeleteButtonEnable(false);
        condition.setMultiSelectEnable(true);
        list.add(condition);
    }

    @Override
    public Condition createDefaultCondition() {
        //未選択状態の条件（「条件を追加」）
        Condition condition = new Condition();
        condition.setSelectEnable(true);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        condition.setPrefectureSelectEnable(false);
        condition.setKeywordSelectEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setMultiSelectEnable(false);
        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {

        // MultiSelectEnableが有効なままだと条件追加のボックスが消えない為
        // 一度設定済みの条件を見ないといけない
        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        int prefectureCnt = 0;
        @SuppressWarnings("unused")
        boolean prefectureLimitFlg = false;
        for(Condition condition2 : conditionList){
            if(condition2.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)){
                prefectureCnt++;
                if(prefectureCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    prefectureLimitFlg = true;
                }
            }
        }

        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
            case (OsolConstants.SEARCH_CONDITION_PREFECTURE):// 都道府県検索の場合
                condition.setPrefectureSelectEnable(true);
                condition.setKeywordSelectEnable(false);
                condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
                condition.setDeleteButtonEnable(true);
                condition.setSelectEnable(false);
                // 制限を超えていた場合複数選択をオフにする
                condition.setMultiSelectEnable(true);// 複数選択可能
                break;
            default:
                condition.setPrefectureSelectEnable(false);// キーワード検索の場合
                condition.setKeywordSelectEnable(true);
                condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
                condition.setDeleteButtonEnable(true);
                condition.setSelectEnable(false);
                break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        //選択肢の設定
        Map<String, String> selectConditionMap;
        selectConditionMap = new LinkedHashMap<>();
        //「条件を追加」
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        // 条件の選択肢
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, OsolConstants.SEARCH_CONDITION_PREFECTURE);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_MUNICIPALITY_NAME, OsolConstants.SEARCH_CONDITION_MUNICIPALITY_NAME);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_MUNICIPALITY_CODE, OsolConstants.SEARCH_CONDITION_MUNICIPALITY_CODE);

        return selectConditionMap;
    }

    public String search() {
        // アクセスログ出力
        exportAccessLog("search", "ボタン「検索開始」押下");

        eventLogger.debug(SearchBean.class.getPackage().getName().concat(":search"));

        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        List<Object> targetPrefectureList = new ArrayList<>();
        List<Object> targetMunicipalityNameList = new ArrayList<>();
        List<Object> targetMunicipalityCodeList = new ArrayList<>();

        for (Condition condition : conditionList) {
            if (condition == null || condition.getMutchingTypeCd() == null) {
                continue;
            }

            switch (condition.getSelectConditionCd()) {
                case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                    // 都道府県検索
                    if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                            && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                        targetPrefectureList.add(condition.getPrefectureCd());
                        eventLogger.debug(SearchBean.class.getPackage().getName().concat(" prefectureCd(" + condition.getPrefectureCd() + ")"));
                    }
                    break;

                case OsolConstants.SEARCH_CONDITION_MUNICIPALITY_NAME:
                    // 自治体名
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetMunicipalityNameList.add(condition.getConditionKeyword());
                        eventLogger.debug(SearchBean.class.getPackage().getName().concat(" MunicipalityName(" + condition.getConditionKeyword() + ")"));
                    }
                    break;

                case OsolConstants.SEARCH_CONDITION_MUNICIPALITY_CODE:
                    // 自治体コード
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetMunicipalityCodeList.add(condition.getConditionKeyword());
                        eventLogger.debug(SearchBean.class.getPackage().getName().concat(" MunicipalityCode(" + condition.getConditionKeyword() + ")"));
                    }
                    break;
                default:
                    break;
            }

        }

        List<MMunicipality> mMunicipalityList
                = mMunicipalityDao.getResultList(targetPrefectureList, targetMunicipalityNameList, targetMunicipalityCodeList);

        if (mMunicipalityList == null) {
            mMunicipalityList = new ArrayList<>();
        }

        setMunicipalityInfoList(mMunicipalityList);
        setSearchedFlg(true);
        return "";
    }

    /**
     * 検索結果クリア処理
     */
//    @Logged
    public void clearMunicipalityForm() {
        // アクセスログ出力 画面側にて属性「onclick」としており＠Loggedが動かないためスーパークラスにてアクセスログ出力する
        exportAccessLog("clearMunicipalityForm", "");

        List<MMunicipality> mMunicipalityList = new ArrayList<>();
        setMunicipalityInfoList(mMunicipalityList);
        // 検索フラグを未検索状態
        setSearchedFlg(false);
    }

    /**
     * 検索条件クリア処理
     */
    public void resetConditionList() {
        searchBeanProperty.resetConditionList();
    }

    private void setMunicipalityInfoList(List<MMunicipality> list) {

        List<MunicipalityInfo> municipalityList = new ArrayList<>();

        // 企業情報の登録
        for (MMunicipality MMunicipality : list) {
            MunicipalityInfo info = new MunicipalityInfo();
            info.setPrefectureCd(MMunicipality.getMPrefecture().getPrefectureName());
            info.setMunicipalityCd(MMunicipality.getMunicipalityCd());
            info.setMunicipalityName(MMunicipality.getMunicipalityName());
            municipalityList.add(info);
        }
        municipalitySelectPaging.init(municipalityList, 10);
    }

    public void changeConditionCd(String orderNo) {
        // アクセスログ出力
        exportAccessLog("changeConditionCd", "「検索条件」選択");

        searchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        Map<String,String>dropdown = conditionList.get(conditionList.size() - 1).getSelectConditionMap();

        //テナント所属建物・建物所属テナントのどちらかが選択された場合、もう一方も非表示にする
        int precCnt= 0;
        boolean prefectureFlg = false;
        for(Condition condition : conditionList){
            if (condition.getSelectConditionCd()
                    .equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                precCnt++;
                if (precCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    dropdown.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                    prefectureFlg = true;
                }
            }
        }

        //ドロップダウンリストを手動で削除した影響で条件追加のが消えないため、削除制御をここで実施。
        if(prefectureFlg && searchBeanProperty.getSelectConditionMap(this).size() == 2){
            //ドロップダウンリストから項目が無くなったら、ドロップダウン自体を削除する
            conditionList.remove(conditionList.size()-1);
        }
    }

    public List<Condition> getConditionList() {
        //選択済み条件一覧
        return searchBeanProperty.getConditionList(this);
    }

    public void deleteCondition(String orderNo) {
        // アクセスログ出力
        exportAccessLog("deleteCondition", "検索条件にてボタン「削除」押下");

        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        //条件削除
        searchBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if(searchBeanProperty.getSelectConditionMap(this).size() == 3
                || searchBeanProperty.getSelectConditionMap(this).size() == 2){
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            Condition condition = conditionList.get(conditionList.size()-1);
            if(condition instanceof BaseSearchCondition){
                BaseSearchCondition base = (BaseSearchCondition)condition;
                if ( !base.isDefaultCondition() ) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }
        searchBeanProperty.resetOrder();
        searchBeanProperty.updateConditionMap(this, conditionList.size()-1);

        //ドロップダウンリスト復活時にテナント所属建物・建物所属テナントが同時に復元されるため、片方を除外する
        int precCnt= 0;
        Map<String, String> conditionListMap = conditionList.get(conditionList.size() - 1).getSelectConditionMap();
        for (Condition condition : conditionList) {
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_TENANT_BELONG_TO_BUILDING)) {
                conditionListMap.remove(OsolConstants.SEARCH_CONDITION_BUILDING_BORROW_BY_TENANT);
            } else if (condition.getSelectConditionCd()
                    .equals(OsolConstants.SEARCH_CONDITION_BUILDING_BORROW_BY_TENANT)) {
                conditionListMap.remove(OsolConstants.SEARCH_CONDITION_TENANT_BELONG_TO_BUILDING);
            } else if (condition.getSelectConditionCd()
                    .equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                precCnt++;
                if(precCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    conditionListMap.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                }
            }
        }
    }

    /**
     * 都道府県リストboxの取得
     *
     * @return
     */
    public Map<String, String> getPrefectureMap() {
        if (prefectureMap == null) {
            prefectureMap = toolsPullDownList.getPrefecture(true, OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal());
        }
        return prefectureMap;
    }

    public MunicipalitySelectPaging getMunicipalitySelectPaging() {
        return municipalitySelectPaging;
    }

    public String pageJump() {
        // アクセスログ出力
        exportAccessLog("pageJump", "ページングリンク押下");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            municipalitySelectPaging.setCurrentPage(Integer.valueOf(param));
        }
        return "";
    }

    public String prevPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("prevPage", "ページング「前へ」押下");

        municipalitySelectPaging.prevPage();
        return "";
    }

    public String nextPage(ActionEvent event) {
        // アクセスログ出力
        exportAccessLog("nextPage", "ページング「後へ」押下");

        municipalitySelectPaging.nextPage();
        return "";
    }

    public boolean isSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

}
