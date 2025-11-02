package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import jp.co.osaki.osol.api.resultdata.sms.meterUser.SearchMeterUserResultData;

/**
 *
 * @author nishida.t
 */
public class PersonInfo {

    private String personId;
    private String personKana;
    private String personName;
    private String deptName;
    private String positionName;
    private String corpId;
    private String telNo;
    private String personType;
    // アカウント状態
    private String accountState;
    // 担当企業フラグ
    private boolean personFlg;
    // 担当建物フラグ
    private boolean BuildingFlg;
    // 担当建物・担当企業フラグ
    private boolean personAndBuildingFlg;
    // 担当設定不要
    private boolean personDisabled;

    // ユーザー編集画面へ遷移するための担当者情報
//    private MPerson mPerson;
    private SearchMeterUserResultData mPerson;

    public PersonInfo() {
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

    public SearchMeterUserResultData getmPerson() {
        return mPerson;
    }

    public void setmPerson(SearchMeterUserResultData mPerson) {
        this.mPerson = mPerson;
    }

    public boolean isPersonFlg() {
        return personFlg;
    }

    public void setPersonFlg(boolean personFlg) {
        this.personFlg = personFlg;
    }

    public boolean isBuildingFlg() {
        return BuildingFlg;
    }

    public void setBuildingFlg(boolean BuildingFlg) {
        this.BuildingFlg = BuildingFlg;
    }

    public boolean isPersonAndBuildingFlg() {
        return personAndBuildingFlg;
    }

    public void setPersonAndBuildingFlg(boolean personAndBuildingFlg) {
        this.personAndBuildingFlg = personAndBuildingFlg;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public boolean isPersonDisabled() {
        return personDisabled;
    }

    public void setPersonDisabled(boolean personDisabled) {
        this.personDisabled = personDisabled;
    }

}
