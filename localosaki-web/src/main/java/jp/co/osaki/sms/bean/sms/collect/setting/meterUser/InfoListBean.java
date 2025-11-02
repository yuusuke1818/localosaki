package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.ListSmsMeterUserParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterUser.ListSmsMeterUserConditionRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterUser.ListSmsMeterUserConditionRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.ListSmsMeterUserResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterUser.SearchMeterUserResultData;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

/**
 * メーターユーザー一覧画面
 * @author nishida.t
 *
 */
@Named(value = "smsCollectSettingMeterUserInfoListBean")
@ConversationScoped
public class InfoListBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    private static final long serialVersionUID = 2794812278692091417L;

    // 検索結果
    private List<MPerson> resultItems;

    private static final int MAX_NUM_PAGE_USERS = 10; // 1ページ内に表示する最大人数

    // 検索結果プロパティ
    @Inject
    private InfoListBeanProperty userInfoListBeanProperty;

    @Inject
    private EditBean meterUserEditBean;

    @Inject
    private TenantPersonBean tenantPersonBean;

    // ---------------------- ページング用 -----------------------------

    // ページング処理
    @Inject
    private InfoListPaging userInfoListPaging;

    @Inject
    private TopBean topBean;

    @Inject
    private OsolConfigs osolConfigs;

    @Override
    @Logged
    public String init() {
        conversationStart();

        //管理者操作権限がなければ自分の編集画面に遷移
        if (!this.isLoginOperationCorpAdmin()) {
            // 現在時点の担当者情報を取得する
            // ログイン時の情報だと未ログインユーザーの場合バージョンが古い可能性がある
            //            userEditBean.setEditPersonId(getLoginPersonId());
            //            userEditBean.setExpiryDate("day");  //Redmine#618 編集時は初期値、登録後は前回値
            //            return userEditBean.init();
        }

        // 初期表示のため検索実行
        search();

        return "meterUserList";
    }

    /**
     * 検索処理
     */
    @Logged
    public void search() {

        userInfoListBeanProperty.setSearchFlg(Boolean.TRUE);

        List<Condition> conditionList = userInfoListBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        String targetPersonIdOrName = null;
        String targetPersonKana = null;
        String targetDeptName = null;
        String targetPositionName = null;
        List<String> targetAccountStatus = new ArrayList<>();

        for (Condition condition : conditionList) {

            if (condition == null || condition.getSelectConditionCd() == null
                    || condition.getSelectConditionCd().equals(OsolConstants.DEFAULT_SELECT_BOX_VALUE)) {
                continue;
            }

            if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PERSON_ID_OR_NAME)) {
                    targetPersonIdOrName = condition.getConditionKeyword();

                } else if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_PERSON_KANA)) {
                    targetPersonKana = condition.getConditionKeyword();

                } else if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_DEPT_NAME)) {
                    targetDeptName = condition.getConditionKeyword();

                } else if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_POSITION_NAME)) {
                    targetPositionName = condition.getConditionKeyword();

                }
            }

            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_ACCOUNT_STATUS)) {
                //viewの1行目に表示するステータス
                for (String statusKind : condition.getAccountStatusKindValue()) {
                    if (!"".equals(statusKind)) {
                        targetAccountStatus.add(statusKind);
                    }
                }
                //viewの2行目に表示するステータス
                for (String statusKind2 : condition.getAccountStatusKindValue2()) {
                    if (!"".equals(statusKind2)) {
                        targetAccountStatus.add(statusKind2);
                    }
                }
            }
        }

        ListSmsMeterUserParameter parameter = new ListSmsMeterUserParameter();
        ListSmsMeterUserResponse response = new ListSmsMeterUserResponse();
        ListSmsMeterUserConditionRequest request = new ListSmsMeterUserConditionRequest();
        ListSmsMeterUserConditionRequestSet requestSet = new ListSmsMeterUserConditionRequestSet();
        requestSet.setPersonIdOrName(targetPersonIdOrName);
        requestSet.setPersonKana(targetPersonKana);
        requestSet.setDeptName(targetDeptName);
        requestSet.setPositionName(targetPositionName);
        requestSet.setAccountStatusList(targetAccountStatus);
        request.setCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
        request.setCondition(requestSet);

        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ListSmsMeterUserBean");
        parameter.setRequest(request);

        response = (ListSmsMeterUserResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (response == null) {
            //            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.noData"));
            setPersonInfoList(new ArrayList<>(), null);
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            //            addErrorMessages(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE + response.getResultCode()));
            setPersonInfoList(new ArrayList<>(), response.getServerDateTime());
        } else if (response.getResult() == null) {
            //            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.registered"));
            setPersonInfoList(new ArrayList<>(), response.getServerDateTime());
        } else if (response.getResult().getSearchResultList() == null) {
            setPersonInfoList(new ArrayList<>(), response.getServerDateTime());
        } else {
            // 取得結果を一覧表示リストに設定
            setPersonInfoList(response.getResult().getSearchResultList(), response.getServerDateTime());
        }

    }

    public String select(PersonInfo preson) {
        return "top";
    }

    /**
     * ユーザー編集ボタン(名前のリンクも含む)
     *
     * @param mPerson 担当者情報
     * @return
     */
    @Logged
    public String onclickMeterUserEdit(String corpId, String personId, boolean personDisabled) {

        if (CheckUtility.isNullOrEmpty(corpId) && CheckUtility.isNullOrEmpty(personId)) {
            meterUserEditBean.setPersonId(STR_EMPTY);
            meterUserEditBean.setCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
            meterUserEditBean.setPersonDisabled(personDisabled);
        } else {
            meterUserEditBean.setCorpId(corpId);
            meterUserEditBean.setPersonId(personId);
            meterUserEditBean.setPersonDisabled(personDisabled);
//            meterUserEditBean.getUserEditBeanProperty().setPerson(mPerson);
//            meterUserEditBean.setExpiryDate("day");  //Redmine#618 編集時は初期値、登録後は前回値
        }

        return meterUserEditBean.init();

    }

    /**
     * 担当企業設定ボタン
     *
     * @param selectPersonId
     * @param selectPersonName
     * @param selectPersonCorpId
     * @param transitionName
     * @return
     */
    public String onclickCorpPerson(String selectPersonId, String selectPersonName, String selectPersonCorpId,
            String transitionName) {

        //        userCorpPersonBean.init();

        //        return userCorpPersonBean.initCorpPerson(selectPersonId, selectPersonName, selectPersonCorpId, transitionName);
        return null;
    }

    /**
     * 担当建物設定ボタン
     *
     * @param selectPersonId
     * @param selectPersonName
     * @param selectPersonCorpId
     * @param targetCorpId
     * @param transitionName
     * @return
     */
    public String onclickBuildingPerson(String selectPersonId, String selectPersonName, String selectPersonCorpId,
            String targetCorpId, String transitionName) {
        /*
        userBuildingPersonBean.init();

        return userBuildingPersonBean.initBuilding(selectPersonId, selectPersonName, selectPersonCorpId, targetCorpId, transitionName);
        */
        return null;
    }

    /**
     * 担当テナント設定ボタン
     *
     * @param selectPersonId
     * @param selectPersonName
     * @param selectPersonCorpId
     * @param targetCorpId
     * @return
     */
    @Logged
    public String onclickTenantPerson(String selectPersonId, String selectPersonName, String selectPersonCorpId) {
        tenantPersonBean.init();
        // 一覧画面からの関係で企業IDを2個渡している
        return tenantPersonBean.initBuilding(selectPersonId, selectPersonName, selectPersonCorpId, selectPersonCorpId);
    }

    // ------------------------------------------------------Paging
    /**
     * ページング処理 前のページへ
     *
     * @param event
     * @return
     */
    @Logged
    public String prevPage(ActionEvent event) {
        userInfoListPaging.prevPage();
        return "";
    }

    /**
     * ページング処理　次のページへ
     *
     * @param event
     * @return
     */
    @Logged
    public String nextPage(ActionEvent event) {
        userInfoListPaging.nextPage();
        return "";
    }

    @Logged
    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            userInfoListPaging.setCurrentPage(Integer.valueOf(param));
        }
        return "";
    }
    // ------------------------------------------------------

    // ------------------------------------------------------検索処理
    public List<Condition> getConditionList() {
        //選択済み条件一覧
        return userInfoListBeanProperty.getConditionList(this);
    }

    @Logged
    public void deleteCondition(String orderNo) {
        //条件削除
        userInfoListBeanProperty.deleteCondition(this, orderNo);
    }

    /**
     * 検索条件が追加されたときに呼ばれる
     *
     * @param orderNo 選択された検索条件
     */
    @Logged
    public void changeConditionCd(String orderNo) {
        userInfoListBeanProperty.updateCondition(this, Integer.parseInt(orderNo));
    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        //最初に選択済み状態で表示しておく条件の一覧
        //ユーザーID
        Condition condition = new Condition();
        condition.setSelectEnable(false);
        // 2017/07/31 検索改善
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_PERSON_ID_OR_NAME);
        condition.setPrefectureSelectEnable(false);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setDeleteButtonEnable(false);
        conditionList.add(condition);
        //アカウント状態
        Condition condition2 = new Condition();
        condition2.setSelectEnable(false);
        condition2.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_ACCOUNT_STATUS);
        condition2.setPrefectureSelectEnable(false);
        condition2.setKeywordSelectEnable(false);
        condition2.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition2.setAccountStatusKindSelectEnable(true);
        condition2.setDeleteButtonEnable(false);
        conditionList.add(condition2);
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
        condition.setAccountStatusKindSelectEnable(false);
        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {
        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        // 2017/07/31 検索改善
        case (OsolConstants.SEARCH_CONDITION_PERSON_ID_OR_NAME):
            condition.setPrefectureSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setAccountStatusKindSelectEnable(false);
            break;
        case (OsolConstants.SEARCH_CONDITION_DEPT_NAME):
            condition.setPrefectureSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setAccountStatusKindSelectEnable(false);
            break;
        case (OsolConstants.SEARCH_CONDITION_POSITION_NAME):
            condition.setPrefectureSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setAccountStatusKindSelectEnable(false);
            break;
        case (OsolConstants.SEARCH_CONDITION_ACCOUNT_STATUS):
            condition.setPrefectureSelectEnable(false);
            condition.setKeywordSelectEnable(false);
            condition.setMutchingTypeCd("");
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setAccountStatusKindSelectEnable(true);
            break;
        default:
            condition.setPrefectureSelectEnable(false);
            condition.setKeywordSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            condition.setAccountStatusKindSelectEnable(false);
            break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        //選択肢の設定
        Map<String, String> selectConditionMap;
        selectConditionMap = new LinkedHashMap<>();
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PERSON_KANA, OsolConstants.SEARCH_CONDITION_PERSON_KANA);
        // 2017/07/31 検索改善
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_PERSON_ID_OR_NAME,
                OsolConstants.SEARCH_CONDITION_PERSON_ID_OR_NAME);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_DEPT_NAME, OsolConstants.SEARCH_CONDITION_DEPT_NAME);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_POSITION_NAME,
                OsolConstants.SEARCH_CONDITION_POSITION_NAME);
        selectConditionMap.put(OsolConstants.SEARCH_CONDITION_ACCOUNT_STATUS,
                OsolConstants.SEARCH_CONDITION_ACCOUNT_STATUS);

        return selectConditionMap;
    }
    // ------------------------------------------------------

    /**
     * 表示情報作成
     *
     * @param list
     * @param svDate
     */
    private void setPersonInfoList(List<SearchMeterUserResultData> list, Timestamp svDate) {
        List<PersonInfo> personList = new ArrayList<>();

        // ユーザー情報の表示設定
        for (SearchMeterUserResultData data : list) {
            PersonInfo info = new PersonInfo();
            info.setPersonId(data.getPersonId());
            info.setPersonKana(data.getPersonKana());
            info.setPersonName(data.getPersonName());
            info.setDeptName(data.getDeptName());
            info.setPositionName(data.getPositionName());
            info.setTelNo(data.getTelNo());
            info.setPersonType(data.getPersonType());
            // アカウントステータス
            info.setAccountState(getAcountStatusName(data, svDate));

            // ユーザー編集画面へ遷移するための担当者情報
//            info.setmPerson(data);

            info.setCorpId(data.getCorpId());
            checkPersonButton(info, data.getCorpType());

            personList.add(info);
        }
        userInfoListBeanProperty.setPersonList(personList);

        userInfoListPaging.init(personList, MAX_NUM_PAGE_USERS);
    }

    /**
     * アカウントステータスの取得
     *
     * @param target
     * @param svDate 現在日時
     * @return
     */
    public String getAcountStatusName(SearchMeterUserResultData target, Date svDate) {
        Date tempPassExpirationDate = null;
        if (target.getTempPassExpirationDate() != null) {
            String day = DateUtility
                    .changeDateFormat(target.getTempPassExpirationDate(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                    .concat(" 23:59:59");
            tempPassExpirationDate = DateUtility.conversionDate(day, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH);
        }
        if (target.getAccountStopFlg().equals(OsolConstants.FLG_ON)) {
            // "アカウント停止中"
            return OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getName();
        } else if (target.getPassMissCount() >= OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {
            // "ロック（入力ミス）"
            return OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName();
        } else if (tempPassExpirationDate != null
                && (svDate.compareTo(tempPassExpirationDate) > 0)) {
            // "ロック（期限切れ）"
            return OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName();
        } else if (tempPassExpirationDate != null
                && svDate.compareTo(tempPassExpirationDate) <= 0) {
            // "未ログイン"
            return OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getName();
        } else {
            // "正常"
            return OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getName();
        }
    }

    /**
     * 担当企業設定/担当建物設定ボタン<br>
     * 表示チェック
     *
     * @param info
     * @param corpType
     */
    private void checkPersonButton(PersonInfo info, String corpType) {
        info.setPersonDisabled(Boolean.FALSE);
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(corpType)) {
            // osakiの場合：担当企業設定
            info.setPersonFlg(Boolean.TRUE);
            info.setPersonAndBuildingFlg(Boolean.FALSE);
            info.setBuildingFlg(Boolean.FALSE);
            if (info.getPersonType().equals(OsolConstants.PERSON_TYPE.ADMIN.getVal())) {
                info.setPersonDisabled(Boolean.TRUE); //管理者は全企業担当してるので不要
            }
        } else if (OsolConstants.CORP_TYPE.MAINTENANCE.getVal().equals(corpType)) {
            // メンテナンスの場合：担当企業・建物設定
            info.setPersonFlg(Boolean.FALSE);
            info.setPersonAndBuildingFlg(Boolean.TRUE);
            info.setBuildingFlg(Boolean.FALSE);
        } else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(corpType)) {
            // パートナーの場合:担当企業・建物設定
            info.setPersonFlg(Boolean.FALSE);
            info.setPersonAndBuildingFlg(Boolean.TRUE);
            info.setBuildingFlg(Boolean.FALSE);
        } else if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(corpType)) {
            // 契約企業の場合:担当建物設定
            info.setPersonFlg(Boolean.FALSE);
            info.setPersonAndBuildingFlg(Boolean.FALSE);
            info.setBuildingFlg(Boolean.TRUE);
            if (info.getPersonType().equals(OsolConstants.PERSON_TYPE.ADMIN.getVal())) {
                info.setPersonDisabled(Boolean.TRUE); //管理者は全建物担当してるので不要
            }
        }

    }

    // -----------------getter/setter
    public List<MPerson> getResultItems() {
        return resultItems;
    }

    public InfoListPaging getUserInfoListPaging() {
        return userInfoListPaging;
    }

    public void setUserInfoListPaging(InfoListPaging userInfoListPaging) {
        this.userInfoListPaging = userInfoListPaging;
    }

    public InfoListBeanProperty getInfoListBeanProperty() {
        return userInfoListBeanProperty;
    }

    public void setInfoListBeanProperty(InfoListBeanProperty infoListBeanProperty) {
        this.userInfoListBeanProperty = infoListBeanProperty;
    }

    public InfoListBeanProperty getUserInfoListBeanProperty() {
        return userInfoListBeanProperty;
    }

    public void setUserInfoListBeanProperty(InfoListBeanProperty userInfoListBeanProperty) {
        this.userInfoListBeanProperty = userInfoListBeanProperty;
    }

}
