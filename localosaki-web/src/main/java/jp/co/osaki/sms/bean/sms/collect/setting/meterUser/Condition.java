package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 *
 * @author nishida.t
 */
public class Condition extends BaseSearchCondition {

    //文字列指定以外の条件入力
    private boolean prefectureSelectEnable;
    private String prefectureCd;

    private String corpId;
    private String personId;
    private String personKana;
    private String personName;
    private String deptName;
    private String positionName;
    private String userId;

    //検索条件の接続詞
    private String searchSubjectConjunction;

    // アカウント状態
    private boolean accountStatusKindSelectEnable;
    private String[] accountStatusKindValue = {"1", "2"};
    private String[] accountStatusKindValue2 = {"", "", ""};

    public Condition() {
        // 接続詞文言
        setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_EQUAL);
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonKana() {
        return personKana;
    }

    public void setPersonKana(String personKana) {
        this.personKana = personKana;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAccountStatusKindSelectEnable() {
        return accountStatusKindSelectEnable;
    }

    public void setAccountStatusKindSelectEnable(boolean accountStatusKindSelectEnable) {
        this.accountStatusKindSelectEnable = accountStatusKindSelectEnable;
    }

    public String[] getAccountStatusKindValue() {
        return accountStatusKindValue;
    }

    public void setAccountStatusKindValue(String[] accountStatusKindValue) {
        this.accountStatusKindValue = accountStatusKindValue;
    }

    public String[] getAccountStatusKindValue2() {
        return accountStatusKindValue2;
    }

    public void setAccountStatusKindValue2(String[] accountStatusKindValue2) {
        this.accountStatusKindValue2 = accountStatusKindValue2;
    }

    public String getSearchSubjectConjunction() {
        return searchSubjectConjunction;
    }

    public void setSearchSubjectConjunction(String searchSubjectConjunction) {
        this.searchSubjectConjunction = searchSubjectConjunction;
    }

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }
}
