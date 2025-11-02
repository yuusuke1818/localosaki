package jp.co.osaki.sms.bean.building.info;

import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 *
 * @author h-shiba
 */
@Named("buildingInfoSearchCondition")
public class Condition extends BaseSearchCondition {

    private boolean prefectureSelectEnable;
    private String prefectureCd;

    private boolean nyukyoTypeSelectEnable;
    private String nyukyoTypeCd;

    private boolean buildingStatusSelectEnable;
    private String buildingStatusCd;

    private boolean buildingTenantSelectEnable;
    private String buildingTenantCd;

    private boolean subtypeSelectEnable;
    private String subtypeId;

    private boolean companyIdButtonEnable;
    private boolean buildingPersonButtonEnable;
    private boolean buildingDeleteSelectEnable;

    private boolean buildingGroupingSelectEnable;
    private String parentGroupId;
    private String childGroupId;

    private boolean searchBelongToBuildingSelectEnable;
    private String borrowingBuildingCd;
    private String borrowingBuildingIdName;

    private boolean searchBorrowingByTenantSelectEnable;
    private String belongingTenantCd;
    private String belongingTenantIdName;

    private boolean autoInspMonthSelectEnable;
    private String autoInspMonth;

    private boolean autoInspDaySelectEnable;
    private String autoInspDay;

    //所属企業ID
    private String divisionCorpId;
    //所属建物ID
    private long divisionBuildingId;

    // 建物グルーピング
    private boolean groupingEnable;

    //検索条件の接続詞
    private String searchSubjectConjunction;
    private boolean searchSubjectConjunctionEnable;

    /** 検索条件指定数上限到達フラグ. */
    private boolean limitCountFlg;

    public Condition() {
        //建物・テナントのラジオボタン初期選択
        this.buildingTenantCd = OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE;

        // 接続詞文言
        setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_EQUAL);
        // 接続詞
        setSearchSubjectConjunctionEnable(true);
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public boolean getPrefectureSelectEnable() {
        return prefectureSelectEnable;
    }

    public void setPrefectureSelectEnable(boolean prefectureSelectEnable) {
        this.prefectureSelectEnable = prefectureSelectEnable;
    }

    public String getPrefectureCd() {
        return prefectureCd;
    }

    public void setPrefectureCd(String prefectureCd) {
        this.prefectureCd = prefectureCd;
    }

    public boolean getNyukyoTypeSelectEnable() {
        return nyukyoTypeSelectEnable;
    }

    public void setNyukyoTypeSelectEnable(boolean nyukyoTypeSelectEnable) {
        this.nyukyoTypeSelectEnable = nyukyoTypeSelectEnable;
    }

    public String getNyukyoTypeCd() {
        return nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public boolean getBuildingStatusSelectEnable() {
        return buildingStatusSelectEnable;
    }

    public void setBuildingStatusSelectEnable(boolean buildingStatusSelectEnable) {
        this.buildingStatusSelectEnable = buildingStatusSelectEnable;
    }

    public String getBuildingStatusCd() {
        return buildingStatusCd;
    }

    public void setBuildingStatusCd(String buildingStatusCd) {
        this.buildingStatusCd = buildingStatusCd;
    }

    public boolean getBuildingTenantSelectEnable() {
        return buildingTenantSelectEnable;
    }

    public void setBuildingTenantSelectEnable(boolean buildingTenantSelectEnable) {
        this.buildingTenantSelectEnable = buildingTenantSelectEnable;
    }

    public String getBuildingTenantCd() {
        return buildingTenantCd;
    }

    public void setBuildingTenantCd(String buildingTenantCd) {
        this.buildingTenantCd = buildingTenantCd;
    }

    public boolean getSubtypeSelectEnable() {
        return subtypeSelectEnable;
    }

    public void setSubtypeSelectEnable(boolean subtypeSelectEnable) {
        this.subtypeSelectEnable = subtypeSelectEnable;
    }

    public String getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(String subtypeId) {
        this.subtypeId = subtypeId;
    }

    public boolean getCompanyIdButtonEnable() {
        return companyIdButtonEnable;
    }

    public void setCompanyIdButtonEnable(boolean companyIdButtonEnable) {
        this.companyIdButtonEnable = companyIdButtonEnable;
    }

    public boolean getBuildingPersonButtonEnable() {
        return buildingPersonButtonEnable;
    }

    public void setBuildingPersonButtonEnable(boolean buildingPersonButtonEnable) {
        this.buildingPersonButtonEnable = buildingPersonButtonEnable;
    }

    public boolean getBuildingDeleteSelectEnable() {
        return buildingDeleteSelectEnable;
    }

    public void setBuildingDeleteSelectEnable(boolean buildingDeleteSelectEnable) {
        this.buildingDeleteSelectEnable = buildingDeleteSelectEnable;
    }

    public boolean getBuildingGroupingSelectEnable() {
        return buildingGroupingSelectEnable;
    }

    public void setBuildingGroupingSelectEnable(boolean buildingGroupingSelectEnable) {
        this.buildingGroupingSelectEnable = buildingGroupingSelectEnable;
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

    public boolean isSearchBelongToBuildingSelectEnable() {
        return searchBelongToBuildingSelectEnable;
    }

    public void setSearchBelongToBuildingSelectEnable(boolean searchBelongToBuildingSelectEnable) {
        this.searchBelongToBuildingSelectEnable = searchBelongToBuildingSelectEnable;
    }

    public String getBorrowingBuildingCd() {
        return borrowingBuildingCd;
    }

    public void setBorrowingBuildingCd(String borrowingBuildingCd) {
        this.borrowingBuildingCd = borrowingBuildingCd;
    }

    public String getBorrowingBuildingIdName() {
        return borrowingBuildingIdName;
    }

    public void setBorrowingBuildingIdName(String borrowingBuildingIdName) {
        this.borrowingBuildingIdName = borrowingBuildingIdName;
    }

    public boolean isSearchBorrowingByTenantSelectEnable() {
        return searchBorrowingByTenantSelectEnable;
    }

    public void setSearchBorrowingByTenantSelectEnable(boolean searchBorrowingByTenantSelectEnable) {
        this.searchBorrowingByTenantSelectEnable = searchBorrowingByTenantSelectEnable;
    }

    public String getBelongingTenantCd() {
        return belongingTenantCd;
    }

    public void setBelongingTenantCd(String belongingTenantCd) {
        this.belongingTenantCd = belongingTenantCd;
    }

    public String getBelongingTenantIdName() {
        return belongingTenantIdName;
    }

    public void setBelongingTenantIdName(String belongingTenantIdName) {
        this.belongingTenantIdName = belongingTenantIdName;
    }

    public boolean isAutoInspMonthSelectEnable() {
        return autoInspMonthSelectEnable;
    }

    public void setAutoInspMonthSelectEnable(boolean autoInspMonthSelectEnable) {
        this.autoInspMonthSelectEnable = autoInspMonthSelectEnable;
    }

    public String getAutoInspMonth() {
        return autoInspMonth;
    }

    public void setAutoInspMonth(String autoInspMonth) {
        this.autoInspMonth = autoInspMonth;
    }

    public boolean isAutoInspDaySelectEnable() {
        return autoInspDaySelectEnable;
    }

    public void setAutoInspDaySelectEnable(boolean autoInspDaySelectEnable) {
        this.autoInspDaySelectEnable = autoInspDaySelectEnable;
    }

    public String getAutoInspDay() {
        return autoInspDay;
    }

    public void setAutoInspDay(String autoInspDay) {
        this.autoInspDay = autoInspDay;
    }

    public String getDivisionCorpId() {
        return divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public long getDivisionBuildingId() {
        return divisionBuildingId;
    }

    public void setDivisionBuildingId(long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

    public String getSearchSubjectConjunction() {
        return searchSubjectConjunction;
    }

    public void setSearchSubjectConjunction(String searchSubjectConjunction) {
        this.searchSubjectConjunction = searchSubjectConjunction;
    }

    public boolean isSearchSubjectConjunctionEnable() {
        return searchSubjectConjunctionEnable;
    }

    public void setSearchSubjectConjunctionEnable(boolean searchSubjectConjunctionEnable) {
        this.searchSubjectConjunctionEnable = searchSubjectConjunctionEnable;
    }

    public boolean isPrefectureSelectEnable() {
        return prefectureSelectEnable;
    }

    public boolean isGroupingEnable() {
        return groupingEnable;
    }

    public void setGroupingEnable(boolean groupingEnable) {
        this.groupingEnable = groupingEnable;
    }

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }

    public boolean isLimitCountFlg() {
        return limitCountFlg;
    }

    public void setLimitCountFlg(boolean limitCountFlg) {
        this.limitCountFlg = limitCountFlg;
    }

    public boolean isMultiSelectEnable() {
        return super.isMultiSelectEnable() && !isLimitCountFlg();
    }
}
