package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.TenantSmsDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.utility.BuildingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MCorpDao;
import jp.co.osaki.sms.dao.MPersonDao;
import jp.co.osaki.sms.dao.TBuildingJoinBuildingInfoListDao;
import jp.co.osaki.sms.dao.TBuildingPersonJoinMPeronSmsDao;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

@Named(value = "smsCollectSettingMeterUserTenantPersonBean")
@ConversationScoped
public class TenantPersonBean extends SmsConversationBean
        implements Serializable, BaseSearchInterface<TenantPersonCondition> {

    private static final long serialVersionUID = -4264171388657952722L;

    @EJB
    MPersonDao mPersonDao;

    @EJB
    MCorpDao mCorpDao;

    // 建物検索（テナント）Dao
    @EJB
    private TBuildingJoinBuildingInfoListDao buildingJoinBuildingInfoListDao;

    // 担当テナントDao
    @EJB
    private TBuildingPersonJoinMPeronSmsDao tBuildingPersonJoinMPeronSmsDao;

    // SMSテナントフィルター
    @EJB
    private TenantSmsDataFilterDao tenantSmsDataFilterDao;

    /**
     * 都道府県用セレクトボックス
     */
    private Map<String, String> prefectureMap;

    /**
     * 建物状況セレクトボックス
     */
    private Map<String, String> buildingSituationMap;

    /**
     * 建物・テナントブルダウン
     */
    private Map<String, String> buildingTenantMap;

    @Inject
    private TopBean topBean;

    /**
     * 汎用区分マスタ
     */
    @Inject
    private GenericTypeList genericTypeList;

    /**
     * プルダウンリストクラス
     */
    @Inject
    private PullDownList toolsPullDownList;

    /**
     * 検索条件用
     */
    @Inject
    private TenantPersonSearchBeanProperty tenantPersonSearchBeanProperty;

    /**
     * ページング処理
     */
    @Inject
    private TenantPersonPaging userTenantPersonPaging;

    /**
     * メッセージクラス
     */
    @Inject
    private SmsMessages beanMessages;

    //    @Inject
    //    SearchBean commonSearchUserBean;

    //検索条件の汎用検索からの選択結果
    //直接Condition(のリストの指定位置)への代入がうまくいかないのでBean経由で反映させる
    private String personId;
    private String personIdName;

    @Override
    public String init() {
        conversationStart();
        //        commonSearchUserBean.init();
        //        commonSearchUserBean.setInterface(this);
        return "meterUserTenantPerson";
    }

    /**
     * 担当建物検索初期表示
     *
     * @param selectPersonId
     * @param selectPersonName
     * @param selectPersonCorpId
     * @param targetCorpId 担当企業設定から遷移してきた場合
     * @return
     */
    public String initBuilding(String selectPersonId, String selectPersonName, String selectPersonCorpId,
            String targetCorpId) {

        // 初期化
        tenantPersonSearchBeanProperty = new TenantPersonSearchBeanProperty();

        tenantPersonSearchBeanProperty.setSelectCorpId(selectPersonCorpId);
        tenantPersonSearchBeanProperty.setSelectPersonName(selectPersonName);
        tenantPersonSearchBeanProperty.setSelectPersonId(selectPersonId);
        tenantPersonSearchBeanProperty.setTargetCorpId(targetCorpId);

        // 初期表示時は0件
        tenantPersonSearchBeanProperty.setTbuildingPersonCnt(0);

        // 初期遷移時は初期化する
        userTenantPersonPaging = new TenantPersonPaging();
        // ポップアップ表示を初期化
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.FALSE);
        // 検索フラグの初期化
        tenantPersonSearchBeanProperty.setSearchFlg(Boolean.FALSE);

        // 担当者情報を取得
        MPerson resultPerson = mPersonDao.find(targetCorpId, selectPersonId);
        tenantPersonSearchBeanProperty.setSelectMPerson(resultPerson);

        MCorp mCorp = new MCorp();
        // 対象の企業情報を取得
        if (!CheckUtility.isNullOrEmpty(targetCorpId)) {
            mCorp = mCorpDao.find(targetCorpId);
            tenantPersonSearchBeanProperty.setTargetCorpName(mCorp.getCorpName());
        }

        // 検索条件用リストの初期化
        List<TenantPersonCondition> conditionList = tenantPersonSearchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new LinkedList<>();

            initialConditionList(conditionList);
            tenantPersonSearchBeanProperty.setConditionList(conditionList);
        }

        // 複数ページの場合
        tenantPersonSearchBeanProperty.setAllSelectDisabled(Boolean.TRUE);
        tenantPersonSearchBeanProperty.setCurrentSelectDisabled(Boolean.TRUE);

        searchPersonInMeterTenant();

        return "meterUserTenantPerson";
    }

    /**
     * 担当メーターテナント検索
     */
    @Logged
    public void searchPersonInMeterTenant() {

        // 設定条件の取得
        List<TenantPersonCondition> conditionList = tenantPersonSearchBeanProperty.getConditionList(this);

        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        List<Object> targetBuildingNoOrNameList = new ArrayList<>();
        List<Object> targetPrefectureList = new ArrayList<>();
        List<Object> targetBuildingStateList = new ArrayList<>();
        List<Object> targetBuildingTenantList = new ArrayList<>();
        List<Object> targetBorrowCorpList = new ArrayList<>();
        List<Object> targetBorrowBuildingList = new ArrayList<>();
        List<Object> targetTenantIdList = new ArrayList<>();
        Date targetDate = buildingJoinBuildingInfoListDao.getSvDate();

        for (TenantPersonCondition condition : conditionList) {
            if (condition == null) {
                continue;
            }

            if (CheckUtility.isNullOrEmpty(condition.getSelectConditionCd())
                    || OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(condition.getSelectConditionCd())) {
                continue;
            }

            switch (condition.getSelectConditionCd()) {
                case OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY:
                    //テナント番号またはテナント名検索
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetBuildingNoOrNameList.add(condition.getConditionKeyword());
                    }
                    break;

                case OsolConstants.SEARCH_CONDITION_PREFECTURE:
                    // 都道府県検索
                    if (!CheckUtility.isNullOrEmpty(condition.getPrefectureCd())
                            && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getPrefectureCd())) {
                        targetPrefectureList.add(condition.getPrefectureCd());
                    }
                    break;

                case OsolConstants.SEARCH_CONDITION_BUILDING_STATE:
                    // 稼働状況検索
                    if (!CheckUtility.isNullOrEmpty(condition.getBuildingStatusCd())
                            && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getBuildingStatusCd())) {
                        targetBuildingStateList.add(condition.getBuildingStatusCd());
                    }
                    break;

                case SmsConstants.SEARCH_CONDITION_TENANT_ID:
                    //ユーザーコード検索
                    if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                        targetTenantIdList.add(condition.getConditionKeyword());
                    }
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
        List<TBuilding> resultList = buildingJoinBuildingInfoListDao.getSearchSmsList(
                targetPrefectureList, targetBuildingNoOrNameList, targetBuildingStateList, targetBuildingTenantList,
                targetBorrowCorpList, targetBorrowBuildingList, targetTenantIdList, targetDate);

        // SMSテナントフィルター
        resultList = tenantSmsDataFilterDao.applyDataFilter(resultList, new BuildingPersonDevDataParam(
                                topBean.getTopBeanProperty().getListInfo().getCorpId(), Long.valueOf(topBean.getTopBeanProperty().getListInfo().getBuildingId()),
                                this.getLoginCorpId(), this.getLoginPersonId()));

        // 対象担当者の担当建物リスト
        List<TBuildingPerson> tBuildingPersonList = tBuildingPersonJoinMPeronSmsDao.getTBuildingPersonList(
                tenantPersonSearchBeanProperty.getSelectCorpId(), tenantPersonSearchBeanProperty.getSelectPersonId());

        List<TenantPersonDispBean> tenantBeanList = new ArrayList<>();
        for (TBuilding tb : resultList) {
            // ユーザー指定検索でない場合
            // デフォルト検索/建物系指定検索の場合
            TenantPersonDispBean tenatPersonDispBean = new TenantPersonDispBean();

            tenatPersonDispBean.setCorpId(tb.getId().getCorpId());
            tenatPersonDispBean.setBuildingId(tb.getId().getBuildingId());
            tenatPersonDispBean.setBuildingNo(tb.getBuildingNo());
            tenatPersonDispBean.setBuildingName(tb.getBuildingName());
            //建物・テナント
            tenatPersonDispBean.setTenantFlg(BuildingUtility.isTenant(tb.getBuildingType()));
            tenatPersonDispBean.setAddress(tb.getAddress());
            tenatPersonDispBean.setPrefectureName(tb.getMPrefecture().getPrefectureName());
            tenatPersonDispBean.setAddressBuilding(tb.getAddressBuilding());

            // 建物状況
            tenatPersonDispBean.setBuildingStatus(checkBuildingStatus(tb.getTotalStartYm(), tb.getTotalEndYm(), tb.getBuildingDelDate()));

            // ユーザーコード（SMSテナント識別ID）取得（SMSテナント情報は登録されているはず）
            MTenantSm mTenantSm = buildingJoinBuildingInfoListDao.getSearchSmsTenant(tb.getId().getCorpId(), tb.getId().getBuildingId());
            if (mTenantSm != null) {
                tenatPersonDispBean.setTenantId(mTenantSm.getTenantId().toString());
            }

            // 担当情報のレコードが存在する
            for (TBuildingPerson tbp : tBuildingPersonList) {

                // 企業IDと建物IDが同じものだけ処理をする
                if (tb.getId().getCorpId().equals(tbp.getId().getCorpId())
                        && tb.getId().getBuildingId().equals(tbp.getId().getBuildingId())) {

                    // 担当設定
                    // 削除フラグがONの場合設定しない
                    if (OsolConstants.FLG_ON == tbp.getDelFlg()) {
                        tenatPersonDispBean.setBuildingPersonFlg(Boolean.FALSE);
                    } else {
                        tenatPersonDispBean.setBuildingPersonFlg(Boolean.TRUE);
                    }

                    // ある場合は、担当建物情報を設定
                    tenatPersonDispBean.settBuildingPerson(tbp);
                }
            }
            // 現在の建物情報を設定
            tenatPersonDispBean.settBuilding(tb);
            // セットする
            tenantBeanList.add(tenatPersonDispBean);
        }

        // ユーザーコードを昇順にソートする
        Collections.sort(tenantBeanList, new Comparator<TenantPersonDispBean>() {
            @Override
            public int compare(TenantPersonDispBean o1, TenantPersonDispBean o2) {
                return Long.valueOf(o1.getTenantId()).compareTo(Long.valueOf(o2.getTenantId())) < 0 ? -1 : 1;
            }
        });

        // 担当件数を集計
        int tbuildingPersonCount =  (int) tenantBeanList.stream()
                .filter(result -> result.isBuildingPersonFlg())
                .count();

        // 登録されている件数をセットする
        tenantPersonSearchBeanProperty.setTbuildingPersonCnt(tbuildingPersonCount);
        userTenantPersonPaging.init(tenantBeanList);

        // ボタン制御
        if (tenantBeanList.isEmpty()) {
            // 0件の場合
            tenantPersonSearchBeanProperty.setAllSelectDisabled(Boolean.TRUE);
            tenantPersonSearchBeanProperty.setCurrentSelectDisabled(Boolean.TRUE);
        } else {
            // 1件でもある場合
            tenantPersonSearchBeanProperty.setCurrentSelectDisabled(Boolean.FALSE);
        }

        if (userTenantPersonPaging.getMaxPage() > 1) {
            // 複数ページの場合
            tenantPersonSearchBeanProperty.setAllSelectDisabled(Boolean.FALSE);
        } else {
            tenantPersonSearchBeanProperty.setAllSelectDisabled(Boolean.TRUE);
        }

        tenantPersonSearchBeanProperty.setSearchFlg(Boolean.TRUE);
    }

    /**
     * 登録/削除処理
     */
    @Logged
    public void registPersonInMeterTenant() {

        // 全ての情報をチェック
        List<TenantPersonDispBean> targetList = getUserTenantPersonPaging().getAllPageInfoList();

        // 登録/更新
        tBuildingPersonJoinMPeronSmsDao.execTBuildingPerson(targetList, getLoginUserId(), tenantPersonSearchBeanProperty.getSelectCorpId(), tenantPersonSearchBeanProperty.getSelectPersonId());

        // 再検索
        searchPersonInMeterTenant();

        // 再検索後は、ポップアップフラグをOFF
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.FALSE);
        addMessage(beanMessages.getMessage("osol.info.UpdateSuccess"));
    }

    /**
     * 全選択
     */
    @Logged
    public void allSelected() {
        userTenantPersonPaging.allSelected();
        // 強制的に表示フラグを設定
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.TRUE);
    }

    /**
     * 全解除
     */
    @Logged
    public void allUnSelected() {
        userTenantPersonPaging.allUnSelected();
        // 強制的に表示フラグを設定
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.TRUE);
    }

    /**
     * 現在ページ全選択
     */
    @Logged
    public void currentAllSelected() {
        userTenantPersonPaging.currentAllSelected();
        // 強制的に表示フラグを設定
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.TRUE);
    }

    /**
     * 現在ページ全解除
     */
    @Logged
    public void currentAllUnSelected() {
        userTenantPersonPaging.currentAllUnSelected();
        // 強制的に表示フラグを設定
        tenantPersonSearchBeanProperty.setPopUpFlg(Boolean.TRUE);
    }

    //-------------------------ページング
    /**
     * 指定ページ遷移
     *
     * @return
     */
    @Logged
    public String pageJump() {
        userTenantPersonPaging.pageJump();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            userTenantPersonPaging.setCurrentPage(Integer.valueOf(param));
        }
        return "";
    }

    //-------------------------ページング
    // ------------------------------------動的検索

    public List<TenantPersonCondition> getConditionList() {
        //選択済み条件一覧
        return tenantPersonSearchBeanProperty.getConditionList(this);
    }

    @Logged
    public void deleteCondition(String orderNo) {
        //条件削除
        List<TenantPersonCondition> conditionList = tenantPersonSearchBeanProperty.getConditionList(this);

        //条件削除
        tenantPersonSearchBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if (tenantPersonSearchBeanProperty.getSelectConditionMap(this).size() == 2
                || tenantPersonSearchBeanProperty.getSelectConditionMap(this).size() == 3) {
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            TenantPersonCondition condition = conditionList.get(conditionList.size() - 1);
            if (condition instanceof BaseSearchCondition) {
                BaseSearchCondition base = (BaseSearchCondition) condition;
                if (!base.isDefaultCondition()) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }
        tenantPersonSearchBeanProperty.resetOrder();
        tenantPersonSearchBeanProperty.updateConditionMap(this, conditionList.size() - 1);

        int prefectureCnt = 0;
        Map<String, String> conditionListMap = conditionList.get(conditionList.size() - 1).getSelectConditionMap();
        for (TenantPersonCondition condition : conditionList) {
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
    @Logged
    public void changeConditionCd(String orderNo) {
        tenantPersonSearchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
        List<TenantPersonCondition> conditionList = tenantPersonSearchBeanProperty.getConditionList(this);
        Map<String, String> dropdown = conditionList.get(conditionList.size() - 1).getSelectConditionMap();

        //テナント所属建物・建物所属テナントのどちらかが選択された場合、もう一方も非表示にする
        int prefectureCnt = 0;
        boolean prefectureFlg = false;
        for (TenantPersonCondition condition : conditionList) {
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PREFECTURE)) {
                prefectureCnt++;
                if (prefectureCnt >= OsolConstants.SEARCH_CONDITION_MULTISELECT_LIMIT) {
                    dropdown.remove(OsolConstants.SEARCH_CONDITION_PREFECTURE);
                    prefectureFlg = true;
                }
            }
        }

        //ドロップダウンリストを手動で削除した影響で条件追加のが消えないため、削除制御をここで実施。
        if (prefectureFlg && tenantPersonSearchBeanProperty.getSelectConditionMap(this).size() == 2) {
            //ドロップダウンリストから項目が無くなったら、ドロップダウン自体を削除する
            conditionList.remove(conditionList.size() - 1);
        }
    }

    /**
     * 検索条件の候補の設定
     *
     * @param conditionList
     */
    @Override
    public void initialConditionList(List<TenantPersonCondition> conditionList) {
        //最初に選択済み状態で表示しておく条件の一覧
        TenantPersonCondition condition = new TenantPersonCondition();
        condition.setSelectEnable(false);
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY);
        condition.setPrefectureSelectEnable(false);
        condition.setBuildingStatusSelectEnable(false);
        condition.setCompanyIdButtonEnable(false);
        condition.setBuildingPersonButtonEnable(false);
        condition.setBuildingDeleteSelectEnable(true);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setPersonSelectEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setTenantIdSelectEnable(false);
        conditionList.add(condition);
    }

    @Override
    public TenantPersonCondition createDefaultCondition() {
        //未選択状態の条件（「条件を追加」）
        TenantPersonCondition condition = new TenantPersonCondition();
        condition.setSelectEnable(true);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        condition.setPrefectureSelectEnable(false);
        condition.setMultiSelectEnable(false);
        condition.setBuildingStatusSelectEnable(false);
        condition.setCompanyIdButtonEnable(false);
        condition.setBuildingPersonButtonEnable(false);
        condition.setBuildingDeleteSelectEnable(false);
        condition.setKeywordSelectEnable(false);
        condition.setPersonSelectEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setTenantIdSelectEnable(false);
        return condition;
    }

    @Override
    public void updateCondition(TenantPersonCondition condition) {
        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        case (OsolConstants.SEARCH_CONDITION_PREFECTURE):
            // 都道府県検索の場合
            // 都道府県
            condition.setPrefectureSelectEnable(true);
            // 建物状況
            condition.setBuildingStatusSelectEnable(false);
            // 企業ID
            condition.setCompanyIdButtonEnable(false);
            // 入力担当者
            condition.setBuildingPersonButtonEnable(false);
            // 削除済レコード検索
            condition.setBuildingDeleteSelectEnable(false);
            // キーワード
            condition.setKeywordSelectEnable(false);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
            //担当ユーザー
            condition.setPersonSelectEnable(false);
            condition.setMultiSelectEnable(true);// 複数選択可能
            // 削除ボタン
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            // ユーザーコード
            condition.setTenantIdSelectEnable(false);
            break;
        /*
        case (OsolConstants.SEARCH_CONDITION_PERSON):
            condition.setPrefectureSelectEnable(false);
            condition.setBuildingStatusSelectEnable(false);
            condition.setCompanyIdButtonEnable(false);
            condition.setBuildingPersonButtonEnable(false);
            condition.setBuildingDeleteSelectEnable(false);
            condition.setKeywordSelectEnable(false);
            condition.setMutchingTypeCd("の担当建物" + OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
            condition.setPersonSelectEnable(true);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            break;
            */
        case (OsolConstants.SEARCH_CONDITION_BUILDING_STATE):
            // 建物状況検索の場合
            condition.setPrefectureSelectEnable(false);
            condition.setBuildingStatusSelectEnable(true);
            condition.setCompanyIdButtonEnable(false);
            condition.setBuildingPersonButtonEnable(false);
            condition.setBuildingDeleteSelectEnable(false);
            condition.setKeywordSelectEnable(false);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);
            condition.setPersonSelectEnable(false);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setTenantIdSelectEnable(false);
            break;
        case (SmsConstants.SEARCH_CONDITION_TENANT_ID):
            // ユーザーコードの場合
            condition.setPrefectureSelectEnable(false);
            condition.setBuildingStatusSelectEnable(false);
            condition.setCompanyIdButtonEnable(false);
            condition.setBuildingPersonButtonEnable(false);
            condition.setBuildingDeleteSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setPersonSelectEnable(false);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setTenantIdSelectEnable(true);
            break;
        default: // キーワード検索
            condition.setPrefectureSelectEnable(false);
            condition.setBuildingStatusSelectEnable(false);
            condition.setCompanyIdButtonEnable(false);
            condition.setBuildingPersonButtonEnable(false);
            condition.setBuildingDeleteSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setPersonSelectEnable(false);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setTenantIdSelectEnable(false);
            break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        //選択肢の設定
        Map<String, String> selectConditionMap;
        selectConditionMap = new LinkedHashMap<>();
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY,
                OsolConstants.SEARCH_CONDITION_BUILDING_NO_OR_NAME_TENANT_ONLY);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PREFECTURE, OsolConstants.SEARCH_CONDITION_PREFECTURE);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_STATE,
                OsolConstants.SEARCH_CONDITION_BUILDING_STATE);
        selectConditionMap.put(SmsConstants.SEARCH_CONDITION_TENANT_ID, SmsConstants.SEARCH_CONDITION_TENANT_ID);
        // 汎用ユーザー検索
        //        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PERSON, OsolConstants.SEARCH_CONDITION_PERSON);
        return selectConditionMap;
    }

    public Map<String, String> getPrefectureMap() {
        prefectureMap = toolsPullDownList.getPrefecture(true, OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal());
        return prefectureMap;
    }

    public void setPrefectureMap(Map<String, String> prefectureMap) {
        this.prefectureMap = prefectureMap;
    }

    public Map<String, String> getBuildingSituationMap() {
        // Osaki権限グループの場合
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(getLoginCorpType())) {
            // Osaki権限の場合
            buildingSituationMap = toolsPullDownList.getBuildingSituation(true,
                    OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal());
        } else {
            // それ以外のグループの場合、削除済みを表示しない
            buildingSituationMap = toolsPullDownList.getBuildingSituation(true,
                    OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal());
            // 削除済みを削除
            buildingSituationMap.remove(OsolConstants.SEARCH_CONDITION_DEL_FLG);
        }

        return buildingSituationMap;
    }

    public void setBuildingSituationMap(Map<String, String> buildingSituationMap) {
        this.buildingSituationMap = buildingSituationMap;
    }

    public Map<String, String> getBuildingTenantMap() {
        return buildingTenantMap;
    }

    public void setBuildingTenantMap(Map<String, String> buildingTenantMap) {
        this.buildingTenantMap = buildingTenantMap;
    }

    public TenantPersonSearchBeanProperty getTenantPersonSearchBeanProperty() {
        return tenantPersonSearchBeanProperty;
    }

    public void setTenantPersonSearchBeanProperty(TenantPersonSearchBeanProperty tenantPersonSearchBeanProperty) {
        this.tenantPersonSearchBeanProperty = tenantPersonSearchBeanProperty;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonIdName() {
        return personIdName;
    }

    public void setPersonIdName(String personIdName) {
        this.personIdName = personIdName;
    }

    @Logged
    public TenantPersonPaging getUserTenantPersonPaging() {
        return userTenantPersonPaging;
    }

    public void setUserTenantPersonPaging(TenantPersonPaging userTenantPersonPaging) {
        this.userTenantPersonPaging = userTenantPersonPaging;
    }

    /**
     * 登録確認メッセージ
     *
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    /**
     * 建物状況判定メソッド
     *
     * @param totalStartYm　建物情報集計開始年月
     * @param TotalEndYm 建物情報集計終了年月
     * @param delDate 建物情報削除日時
     * @return 建物状況
     */
    private String checkBuildingStatus(Date totalStartYm, Date totalEndYm, Timestamp delDate) {

        // 建物削除日時がnullではない場合、削除済
        if (delDate != null) {
            return genericTypeList.getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.DELETED.getVal());
        }

        // 建物状況は、現在日が集計開始年月と終了年月の期間内だったら稼働中、期間外の場合稼動終了
        // 現在日時をDBから取得
        Timestamp svDate = buildingJoinBuildingInfoListDao.getSvDate();
        Date tempDate = new Date(svDate.getTime());
        String temp = DateUtility.changeDateFormat(tempDate, DateUtility.DATE_FORMAT_YYYYMM);

        // 開始日終了日をDate型に変換(年月)
        Date targetDate = DateUtility.conversionDate(temp, DateUtility.DATE_FORMAT_YYYYMM);

        // 終了日が入っていない場合も考慮
        Date endYmDate = null;
        int endDiff = 0;

        // チェック
        if (totalEndYm != null) {
            String endYmStr = DateUtility.changeDateFormat(totalEndYm, DateUtility.DATE_FORMAT_YYYYMM);
            endYmDate = DateUtility.conversionDate(endYmStr, DateUtility.DATE_FORMAT_YYYYMM);
            endDiff = endYmDate.compareTo(targetDate);
        }

        if(totalEndYm == null || endDiff >= 0){
            //集計終了が未設定か、今日以降なら稼働中と判断
            return genericTypeList.getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.NOW.getVal());
        }

        // 稼動終了
        return genericTypeList.getBuildingSituationByName(OsolConstants.BUILDING_SITUATION.END.getVal());
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

//    /**
//     * リスト情報の作成
//     *
//     * @param target
//     * @return
//     */
//    private List<Object> exChengeCondList(String target) {
//        List<Object> templist = new ArrayList<>();
//        templist.add(target);
//        return templist;
//    }

    /**
     * 汎用ユーザー検索呼び出し
     */
    public void searchPersonButton() {
        //        commonSearchUserBean.resetConditionList();
    }

}
