package jp.co.osaki.sms.bean.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.dao.MCorpSelectDao;
import jp.skygroup.enl.webap.base.BasePaging;
import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

@Named(value = "corpSelectBean")
@ViewScoped
public class CorpSelectBean extends SmsBean implements Serializable, BaseSearchInterface<Condition> {

    private static final long serialVersionUID = 6247452330800190680L;

    @Inject
    private IndexBean indexBean;

// TODO:コメント外す際は、関連Daoを移植必要
//    @Inject
//    private PullDownList toolsPullDownList;

    @Inject
    private BaseSearchBeanProperty<Condition> corpSelectBeanProperty;

    @Inject
    private BasePaging<CorpInfo> corpSelectPaging;

    private List<CorpInfo> corpList;

    @EJB
    private MCorpSelectDao mCorpSelectDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    private boolean searchedFlg = false;

    @Inject
    private SmsFileDownload smsFileDownload;

    @Override
    public String init() {
        eventLogger.debug(CorpSelectBean.class.getPackage().getName().concat(":START"));

        return "corpSelect";
    }

    public List<Condition> getConditionList() {
        //選択済み条件一覧
        return corpSelectBeanProperty.getConditionList(this);
    }

    public void deleteCondition(String orderNo) {
        List<Condition> conditionList = corpSelectBeanProperty.getConditionList(this);

        //条件削除
        corpSelectBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if(corpSelectBeanProperty.getSelectConditionMap(this).size() == 2
                || corpSelectBeanProperty.getSelectConditionMap(this).size() == 3 ){
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            Condition condition = conditionList.get(conditionList.size()-1);
            if(condition instanceof BaseSearchCondition){
                BaseSearchCondition base = (BaseSearchCondition)condition;
                if ( !base.isDefaultCondition() ) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }
        corpSelectBeanProperty.resetOrder();
        corpSelectBeanProperty.updateConditionMap(this, conditionList.size()-1);

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

    /**
     * 検索条件が追加されたときに呼ばれる
     *
     * @param orderNo 選択された検索条件
     */
    public void changeConditionCd(String orderNo) {
        corpSelectBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        List<Condition> conditionList = corpSelectBeanProperty.getConditionList(this);
        Map<String,String>dropdown = conditionList.get(conditionList.size() - 1).getSelectConditionMap();

        //テナント所属建物・建物所属テナントのどちらかが選択された場合、もう一方も非表示にする
        int prefectureCnt = 0;
        boolean prefectureFlg = false;
        for(Condition condition : conditionList){
            if(condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)){
                prefectureCnt++;
                if(prefectureCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    dropdown.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                    prefectureFlg = true;
                }
            }
        }

        //ドロップダウンリストを手動で削除した影響で条件追加のが消えないため、削除制御をここで実施。
        if(prefectureFlg && corpSelectBeanProperty.getSelectConditionMap(this).size() == 2){
            //ドロップダウンリストから項目が無くなったら、ドロップダウン自体を削除する
            conditionList.remove(conditionList.size()-1);
        }
    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        //最初に選択済み状態で表示しておく条件の一覧
        Condition condition = new Condition();
        condition.setSelectEnable(false);
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        condition.setPrefectureSelectEnable(false);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setDeleteButtonEnable(false);
        conditionList.add(condition);
    }

    @Override
    public Condition createDefaultCondition() {
        //未選択状態の条件（「条件を追加」）
        Condition condition = new Condition();
        condition.setSelectEnable(true);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        condition.setPrefectureSelectEnable(false);
        condition.setKeywordSelectEnable(false);
        condition.setCorpTypeSelectEnable(false);
        condition.setDeleteButtonEnable(false);
        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {
        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
            case (OsolConstants.SEARCH_CONDITION_PREFECTURE):
                condition.setPrefectureSelectEnable(true);
                condition.setMultiSelectEnable(true);// 複数選択可能
                condition.setKeywordSelectEnable(false);
                condition.setCorpTypeSelectEnable(false);
                condition.setMutchingTypeCd("");
                condition.setDeleteButtonEnable(true);
                condition.setSelectEnable(false);
                break;
            case (OsolConstants.SEARCH_CONDITION_CORP_TYPE):
                condition.setPrefectureSelectEnable(false);
                condition.setKeywordSelectEnable(false);
                condition.setCorpTypeSelectEnable(true);
                condition.setMutchingTypeCd("");
                condition.setDeleteButtonEnable(true);
                condition.setSelectEnable(false);
                break;
            default:
                condition.setPrefectureSelectEnable(false);
                condition.setKeywordSelectEnable(true);
                condition.setCorpTypeSelectEnable(false);
                condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
                condition.setDeleteButtonEnable(true);
                condition.setSelectEnable(false);
                break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        // 選択肢の設定
        Map<String, String> selectConditionMap;
        selectConditionMap = new LinkedHashMap<>();
        //「条件を追加」
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        // 条件の選択肢
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, OsolConstants.SEARCH_CONDITION_PREFECTURE);
        // 2017/07/31 検索改善
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME, OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME);
        // TODO:大崎権限のみ
        if(this.getLoginCorpType().equals(OsolConstants.CORP_TYPE.OSAKI.getVal())){
            selectConditionMap.put(OsolConstants.SEARCH_CONDITION_CORP_TYPE, OsolConstants.SEARCH_CONDITION_CORP_TYPE);
        }

        return selectConditionMap;
    }

    public String search() {
        eventLogger.debug(CorpSelectBean.class.getPackage().getName().concat(":search"));

        List<Condition> conditionList = corpSelectBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

//        List<Object> targetPrefectureList = new ArrayList<>();
        // 2017/07/31 検索改善
        List<Object> targetCorpIdOrNameList = new ArrayList<>();
//        List<Object> targetCorpIdList = new ArrayList<>();
//        List<Object> targetCorpNameList = new ArrayList<>();
        List<Object> targetCorpTypeList = new ArrayList<>();
        List<Object> selectCorpIdList = new ArrayList<>();
        List<Object> authorityOsakiManegeList = new ArrayList<>();
        List<Object> authorityPersonIdList = new ArrayList<>();
        List<Object> authorityCorpIdList = new ArrayList<>();
        List<Object> targetPrefectureCdList = new ArrayList<>();

        for (Condition condition : conditionList) {
            if (condition == null || condition.getMutchingTypeCd() == null) {
                continue;
            }

            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_CORP_TYPE)) {
                for (String companyKind : condition.getCorpType()) {
                    if (!"".equals(companyKind)) {
                        targetCorpTypeList.add(companyKind);
                    }
                }
            } else if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME)) {
                    targetCorpIdOrNameList.add(condition.getConditionKeyword());
                }
            }
            else if(condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                // 都道府県
                String selectedPrefectureCd = condition.getPrefectureCd();
                if (!CheckUtility.isNullOrEmpty(selectedPrefectureCd) &&
                    !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(selectedPrefectureCd) &&
                    !targetPrefectureCdList.contains(selectedPrefectureCd)) {
                    targetPrefectureCdList.add(selectedPrefectureCd);
                    eventLogger.debug(CorpSelectBean.class.getPackage().getName().concat(" prefecture(" + selectedPrefectureCd + ")"));
                }
            }
        }

        // ログインユーザーがOsaki権限の管理者の場合 2016/08/02 権限対応
        if ((OsolConstants.CORP_TYPE.OSAKI.getVal().equals(getLoginCorpType()) && isLoginPersonAdmin())) {
            // DaoImplで利用、Osaki権限管理の場合は全企業取得の為SQLを振り分ける
            authorityOsakiManegeList.add("1");
        } else {
            // ログインしている担当者ID,企業IDを設定
            authorityPersonIdList.add(getLoginPersonId());
            authorityCorpIdList.add(getLoginCorpId());
        }

        // 現在操作中の企業
        selectCorpIdList.add(this.getLoginOperationCorpId());

        // 権限による絞り込み 2016/08/02 権限対応
        List<MCorp> tCorpList
                = mCorpSelectDao.getResultList(targetPrefectureCdList, targetCorpIdOrNameList,
                         targetCorpTypeList, selectCorpIdList, authorityOsakiManegeList, authorityPersonIdList, authorityCorpIdList);

        tCorpList = corpDataFilterDao.applyDataFilter(tCorpList, new PersonDataParam(this.getLoginCorpId(), this.getLoginPersonId()));

        if (tCorpList == null) {
            tCorpList = new ArrayList<>();
        }

        // 企業種別コードの置き換え
        for (MCorp corp : tCorpList) {
            if (corp != null) {
                switch (corp.getCorpType()) {
                    case "1":
                        corp.setCorpType("パートナー");
                        break;

                    case "2":
                        corp.setCorpType("メンテナンス");
                        break;

                    case "3":
                        corp.setCorpType("契約企業");
                        break;

                    default:
                        break;
                }
            }
        }

        searchedFlg = true;

        setCorpInfoList(tCorpList);

        return "";
    }

    /**
     *
     * 編集企業選択
     *
     * @param corp 選択した企業情報
     * @return トップ画面のoutcome
     */
    public String select(CorpInfo corp) {

        //ログインユーザーが現在操作している企業
        this.setLoginOperationCorp(mCorpSelectDao.find(corp.getCorpId()));

        //編集する企業のロゴをS3からダウンロード
        smsFileDownload.S3fileDownload(getLoginOperationCorp().getCorpLogoImageFilePath());

        return indexBean.init();
    }

    private void setCorpInfoList(List<MCorp> list) {
        corpList = new ArrayList<>();

        // 企業情報の登録
        for (MCorp tCorp : list) {
            CorpInfo info = new CorpInfo();
            info.setCorpId(tCorp.getCorpId());
            info.setCorpName(tCorp.getCorpName());
            info.setCorpType(tCorp.getCorpType());
            // 住所2がnullの場合設定しないようにする
            if (!CheckUtility.isNullOrEmpty(tCorp.getAddress2())) {
                info.setCorpAddress(tCorp.getMPrefecture().getPrefectureName() + tCorp.getAddress1() + tCorp.getAddress2());
            } else {
                info.setCorpAddress(tCorp.getMPrefecture().getPrefectureName() + tCorp.getAddress1());
            }

            corpList.add(info);
        }

        corpSelectPaging.init(corpList, 10);
    }

    public int getResultListSize() {
        if (corpList == null) {
            // 画面遷移時は検索結果なし
            return 0;
            //search();
        }
        return corpList.size();
    }

    public List<CorpInfo> getPageList() {
        if (corpList == null) {
            // 画面遷移時は検索結果なし
            return new ArrayList<>();
            //search();
        }
        return corpSelectPaging.getPageList();
    }

    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            corpSelectPaging.setCurrentPage(Integer.valueOf(param));
        }
        return "";
    }

    public String prevPage(ActionEvent event) {
        corpSelectPaging.prevPage();
        return "";
    }

    public String nextPage(ActionEvent event) {
        corpSelectPaging.nextPage();
        return "";
    }

    public BasePaging<CorpInfo> getCorpSelectPaging() {
        return corpSelectPaging;
    }

// TODO:コメント外す際は、関連Daoを移植必要
//    public Map<String, String> getPrefectureMap() {
//        return toolsPullDownList.getPrefecture(true, OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal());
//    }

    public Boolean getSearchedFlg() {
        return searchedFlg;
    }

}
