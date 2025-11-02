package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

@Named(value = "smsCollectSettingTenantSearchBeanProperty")
@Dependent
public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -7945477813231459986L;

    // 動的検索条件リスト
    private List<Condition> conditionList;

    // 検索結果表示用リスト
    private List<ListInfo> buildingList = new ArrayList<>();

    public SearchBeanProperty() {
    }

    // 企業ID
    private String corpId;

    // 建物番号
    private String buildingNo;

    // 建物名
    private String buildingName;

    // 親グループID
    private String parentGroupId;

    // 子グループID
    private String childGroupId;

    // 都道府県コード
    private String prefectureCd;

    // 入居形態
    private String nyukyoTypeCd;

    // 建物状況
    private String buildingStatus;

    // 細分分類（null able）
    private String subtypeId;

    // ユーザID
    private String searchUserId;

    // ユーザ名
    private String userName;

    // 一覧表示css制御用
    private String columnClassesStr;

    // 検索実行済みフラグ
    private boolean searchedFlg;

    public List<ListInfo> getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(List<ListInfo> buildingList) {
        this.buildingList = buildingList;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPrefectureCd() {
        return prefectureCd;
    }

    public void setPrefectureCd(String prefectureCd) {
        this.prefectureCd = prefectureCd;
    }

    public String getNyukyoTypeCd() {
        return nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

    public String getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(String subtypeId) {
        this.subtypeId = subtypeId;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getChildGroupId() {
        return childGroupId;
    }

    public void setChildGroupId(String childGroupId) {
        this.childGroupId = childGroupId;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public String getColumnClassesStr() {
        return columnClassesStr;
    }

    public void setColumnClassesStr(String columnClassesStr) {
        this.columnClassesStr = columnClassesStr;
    }

    public boolean getSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }
}
