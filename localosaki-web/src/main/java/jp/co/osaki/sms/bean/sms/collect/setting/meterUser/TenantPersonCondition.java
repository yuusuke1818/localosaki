package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 * 担当テナント検索用
 *
 * @author nishida.t
 */
public class TenantPersonCondition extends BaseSearchCondition {

    private boolean prefectureSelectEnable;
    private String prefectureCd;

    private boolean searchBorrowingByTenantSelectEnable;
//    private String belongingTenantCd;
//    private String belongingTenantIdName;

    //検索条件の接続詞
    private String searchSubjectConjunction;
    private boolean searchSubjectConjunctionEnable;

    private boolean buildingStatusSelectEnable;
    private String buildingStatusCd;


    private boolean companyIdButtonEnable;
    private boolean buildingPersonButtonEnable;
    private boolean buildingDeleteSelectEnable;

    private boolean personSelectEnable;
    private String personId;
    private String personDisp;

    private boolean tenantIdSelectEnable;
    private String tenantId;

    public TenantPersonCondition() {
        // 接続詞文言
        setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_EQUAL);
    }

    public boolean isPrefectureSelectEnable() {
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

    public boolean isBuildingStatusSelectEnable() {
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

    public boolean isCompanyIdButtonEnable() {
        return companyIdButtonEnable;
    }

    public void setCompanyIdButtonEnable(boolean companyIdButtonEnable) {
        this.companyIdButtonEnable = companyIdButtonEnable;
    }

    public boolean isBuildingPersonButtonEnable() {
        return buildingPersonButtonEnable;
    }

    public void setBuildingPersonButtonEnable(boolean buildingPersonButtonEnable) {
        this.buildingPersonButtonEnable = buildingPersonButtonEnable;
    }

    public boolean isBuildingDeleteSelectEnable() {
        return buildingDeleteSelectEnable;
    }

    public void setBuildingDeleteSelectEnable(boolean buildingDeleteSelectEnable) {
        this.buildingDeleteSelectEnable = buildingDeleteSelectEnable;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonDisp() {
        return personDisp;
    }

    public void setPersonDisp(String personDisp) {
        this.personDisp = personDisp;
    }

    public boolean isTenantIdSelectEnable() {
        return tenantIdSelectEnable;
    }

    public void setTenantIdSelectEnable(boolean tenantIdSelectEnable) {
        this.tenantIdSelectEnable = tenantIdSelectEnable;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isPersonSelectEnable() {
        return personSelectEnable;
    }

    public void setPersonSelectEnable(boolean personSelectEnable) {
        this.personSelectEnable = personSelectEnable;
    }
    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public boolean isSearchBorrowingByTenantSelectEnable() {
        return searchBorrowingByTenantSelectEnable;
    }

    public void setSearchBorrowingByTenantSelectEnable(boolean searchBorrowingByTenantSelectEnable) {
        this.searchBorrowingByTenantSelectEnable = searchBorrowingByTenantSelectEnable;
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

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }
}
