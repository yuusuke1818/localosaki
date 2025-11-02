package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
*
* @author nishida.t
*/
@Named("smsCollectSettingMeterUserInfoListBeanProperty")
@Dependent
public class InfoListBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    private static final long serialVersionUID = 3267692027854017571L;

    private List<Condition> conditionList;
    private List<PersonInfo> personList = new ArrayList<>();

    private boolean searchFlg;

    public InfoListBeanProperty() {}

    // ---------検索条件----------
    /**
     * 企業ID
     */
    private String corpId;
    /**
     * 担当者ID
     */
    private String personId;
    /**
     * 担当者ふりがな
     */
    private String personKana;
    /**
     * 担当者名
     */
    private String personName;
    /**
     * 部署名
     */
    private String deptName;
    /**
     * 役職名
     */
    private String positionName;
    /**
     * ユーザID
     */
    private String userId;

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

    public List<PersonInfo> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonInfo> personList) {
        this.personList = personList;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public boolean isSearchFlg() {
        return searchFlg;
    }

    public void setSearchFlg(boolean searchFlg) {
        this.searchFlg = searchFlg;
    }

    public LinkedHashMap<String, String> getCheckAccountStatusTypeMap() {
        LinkedHashMap<String, String> retMap = new LinkedHashMap<>();
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getName(), OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getName(), OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName(), OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName(), OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getName(), OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getVal());
        return retMap;
    }

    @Override
    public String toString() {
        return "corpId:" + corpId + " personId:" + personId + " personKana:" + personKana + " personName:" + personName + " deptName:" + deptName + " positionName:" + positionName + " userId:" + userId;
    }

}
