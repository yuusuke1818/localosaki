package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MPerson;

@Named(value = "meterUserEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    private static final long serialVersionUID = -1879620820185410894L;

    private MPerson person;
    private String personName;
    private String personId;
    private String personCorpId;
    private boolean personTypeFlg;

    // ユーザー権限
    private boolean personAuthManager;
    private boolean personAuthBuilding;
    private boolean personAuthMaintenance;
    private boolean personAuthEnergyTarget;
    private boolean personAuthEnergyUsed;
    private boolean personAuthAnalysis;
    private boolean personAuthPlan;
    private boolean personAuthSinage;
    private boolean personAuthEnergyConf;
    private boolean personAuthEnergyPointInput;
    private boolean personAuthSmControl;

    private boolean personTypeCorpFlg;

    private boolean buildingManageFlg;

    private String corpType;

    // 権限グループ制御
    // 担当企業フラグ
    private boolean personFlg;
    // 担当建物フラグ
    private boolean buildingFlg;

    // ユーザー権限表示制御
    private boolean authorityFlg;
    private boolean manageFlg;

    // 自分自身を編集しているか
    private boolean selfFlg;

    // 管理者権限フラグ
    private boolean corpManageFlg;

    // メンテ依頼発行フラグ
    private boolean mainteFlg;

    // 手入力ポイント権限フラグ
    private boolean energyPointInputFlg;

    List<MCorpPersonAuth> mCorpPersonAuthList;
    MCorpPerson mCorpPerson;
    Map<MCorpPerson, List<MCorpPersonAuth>> anotherMCorpPersonList;

    // アカウントロック時(期限切れ/入力ミス)
    private boolean unlockMsgFlg;

    public EditBeanProperty() {

    }

    public void initPersonAuthCheck() {
        setCorpManageFlg(Boolean.FALSE);
        personAuthManager = false;
        personAuthBuilding = false;
        personAuthMaintenance = false;
        personAuthEnergyTarget = false;
        personAuthEnergyUsed = false;
        personAuthAnalysis = false;
        personAuthPlan = false;
        personAuthSinage = false;
        personAuthEnergyConf = false;
        personAuthSmControl = false;
    }

    public MPerson getPerson() {
        return person;
    }

    public void setPerson(MPerson person) {
        this.person = person;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonCorpId() {
        return personCorpId;
    }

    public void setPersonCorpId(String personCorpId) {
        this.personCorpId = personCorpId;
    }

    public String getPersonType() {
        String person_type = "";
        if (personTypeFlg == true) {
            person_type = "0";
        } else {
            person_type = "1";
        }
        return person_type;
    }

    public void setPersonType(String person_type) {

        if ("0".equals(person_type)) {
            this.personTypeFlg = true;
            this.personTypeCorpFlg = true;
        } else {
            this.personTypeFlg = false;
            this.personTypeCorpFlg = false;
        }
    }

    public boolean getPersonTypeFlg() {
        return personTypeFlg;
    }

    public void setPersonTypeFlg(boolean person_type_flg) {
        this.personTypeFlg = person_type_flg;
    }

    public boolean getPersonTypeCorpFlg() {
        return personTypeCorpFlg;
    }

    public void setPersonTypeCorpFlg(boolean corp_flg) {
        this.personTypeCorpFlg = corp_flg;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public boolean isPersonFlg() {
        return personFlg;
    }

    public void setPersonFlg(boolean personFlg) {
        this.personFlg = personFlg;
    }

    public boolean isBuildingFlg() {
        return buildingFlg;
    }

    public void setBuildingFlg(boolean BuildingFlg) {
        this.buildingFlg = BuildingFlg;
    }

    public boolean getBuildingManageFlg() {
        return buildingManageFlg;
    }

    public void setBuildingManageFlg(boolean building_manage_flg) {
        this.buildingManageFlg = building_manage_flg;
    }

    public boolean isPersonAuthManager() {
        return personAuthManager;
    }

    public void setPersonAuthManager(boolean personAuthManager) {
        this.personAuthManager = personAuthManager;
    }

    public boolean isPersonAuthBuilding() {
        return personAuthBuilding;
    }

    public void setPersonAuthBuilding(boolean personAuthBuilding) {
        this.personAuthBuilding = personAuthBuilding;
    }

    public boolean isPersonAuthMaintenance() {
        return personAuthMaintenance;
    }

    public void setPersonAuthMaintenance(boolean personAuthMaintenance) {
        this.personAuthMaintenance = personAuthMaintenance;
    }

    public boolean isPersonAuthEnergyTarget() {
        return personAuthEnergyTarget;
    }

    public void setPersonAuthEnergyTarget(boolean personAuthEnergyTarget) {
        this.personAuthEnergyTarget = personAuthEnergyTarget;
    }

    public boolean isPersonAuthEnergyUsed() {
        return personAuthEnergyUsed;
    }

    public void setPersonAuthEnergyUsed(boolean personAuthEnergyUsed) {
        this.personAuthEnergyUsed = personAuthEnergyUsed;
    }

    public boolean isPersonAuthAnalysis() {
        return personAuthAnalysis;
    }

    public void setPersonAuthAnalysis(boolean personAuthAnalysis) {
        this.personAuthAnalysis = personAuthAnalysis;
    }

    public boolean isPersonAuthPlan() {
        return personAuthPlan;
    }

    public void setPersonAuthPlan(boolean personAuthPlan) {
        this.personAuthPlan = personAuthPlan;
    }

    public boolean isPersonAuthSinage() {
        return personAuthSinage;
    }

    public void setPersonAuthSinage(boolean personAuthSinage) {
        this.personAuthSinage = personAuthSinage;
    }

    public boolean isPersonAuthEnergyConf() {
        return personAuthEnergyConf;
    }

    public void setPersonAuthEnergyConf(boolean personAuthEnergyConf) {
        this.personAuthEnergyConf = personAuthEnergyConf;
    }

    public boolean isPersonAuthEnergyPointInput() {
        return personAuthEnergyPointInput;
    }

    public void setPersonAuthEnergyPointInput(boolean personAuthEnergyPointInput) {
        this.personAuthEnergyPointInput = personAuthEnergyPointInput;
    }

    public boolean isPersonAuthSmControl() {
        return personAuthSmControl;
    }

    public void setPersonAuthSmControl(boolean personAuthSmControl) {
        this.personAuthSmControl = personAuthSmControl;
    }

    public void setPersonAuth(String authorityCd) {

        if (OsolConstants.USER_AUTHORITY.MANAGE.getVal().equals(authorityCd)) {
            //管理者権限
            personAuthManager = true;
        }
        else if (OsolConstants.USER_AUTHORITY.MANAGE.getVal().equals(authorityCd)) {
            //管理者権限
            personAuthManager = true;
        }
    }



    public boolean isSelfFlg() {
        return selfFlg;
    }

    public void setSelfFlg(boolean selfFlg) {
        this.selfFlg = selfFlg;
    }

    public List<MCorpPersonAuth> getmCorpPersonAuthList() {
        return mCorpPersonAuthList;
    }

    public void setmCorpPersonAuthList(List<MCorpPersonAuth> mCorpPersonAuthList) {
        this.mCorpPersonAuthList = mCorpPersonAuthList;
    }

    public MCorpPerson getmCorpPerson() {
        return mCorpPerson;
    }

    public void setmCorpPerson(MCorpPerson mCorpPerson) {
        this.mCorpPerson = mCorpPerson;
    }

    public Map<MCorpPerson, List<MCorpPersonAuth>> getAnotherMCorpPersonList() {
        return anotherMCorpPersonList;
    }

    public void setAnotherMCorpPersonList(Map<MCorpPerson, List<MCorpPersonAuth>> anotherMCorpPersonList) {
        this.anotherMCorpPersonList = anotherMCorpPersonList;
    }

    public boolean isCorpManageFlg() {
        return corpManageFlg;
    }

    public void setCorpManageFlg(boolean corpManageFlg) {
        this.corpManageFlg = corpManageFlg;
    }

    public boolean isAuthorityFlg() {
        return authorityFlg;
    }

    public void setAuthorityFlg(boolean authorityFlg) {
        this.authorityFlg = authorityFlg;
    }

    public boolean isMainteFlg() {
        return mainteFlg;
    }

    public void setMainteFlg(boolean mainteFlg) {
        this.mainteFlg = mainteFlg;
    }

    public boolean isEnergyPointInputFlg() {
        return energyPointInputFlg;
    }

    public void setEnergyPointInputFlg(boolean energyPointInputFlg) {
        this.energyPointInputFlg = energyPointInputFlg;
    }

    public boolean isUnlockMsgFlg() {
        return unlockMsgFlg;
    }

    public void setUnlockMsgFlg(boolean unlockMsgFlg) {
        this.unlockMsgFlg = unlockMsgFlg;
    }

    public boolean isManageFlg() {
        return manageFlg;
    }

    public void setManageFlg(boolean manageFlg) {
        this.manageFlg = manageFlg;
    }

}
