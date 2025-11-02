package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.entity.MPerson;
import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 担当テナント検索用
 *
 * @author nishida.t
 */
@Named(value = "smsCollectSettingMeterUserTenantPersonSearchBeanProperty")
@Dependent
public class TenantPersonSearchBeanProperty extends BaseSearchBeanProperty<TenantPersonCondition> implements Serializable {

    private static final long serialVersionUID = -6852193538536075790L;

    // 対象ユーザーの担当情報
    private String selectPersonId;
    private String selectPersonName;
    private String selectCorpId;

    private MPerson selectMPerson;

    // 担当企業設定画面から遷移してきた場合の企業ID
    private String targetCorpId;
    private String targetCorpName;

    private List<TenantPersonCondition> conditionList;

    // 全選択/全解除ボタンフラグ
    private boolean allSelectDisabled;

    // 全選択/全解除ボタンフラグ
    private boolean currentSelectDisabled;

    // 登録中の建物の件数
    private int tbuildingPersonCnt;

    private String orderByCd;

    // 遷移元画面名
    private String transitionName;

    // ポップアップ表示フラグ
    private boolean popUpFlg;

    // 検索フラグ
    private boolean searchFlg;

    public String getSelectPersonId() {
        return selectPersonId;
    }

    public void setSelectPersonId(String selectPersonId) {
        this.selectPersonId = selectPersonId;
    }

    public String getSelectPersonName() {
        return selectPersonName;
    }

    public void setSelectPersonName(String selectPersonName) {
        this.selectPersonName = selectPersonName;
    }

    public String getSelectCorpId() {
        return selectCorpId;
    }

    public void setSelectCorpId(String selectCorpId) {
        this.selectCorpId = selectCorpId;
    }

    public List<TenantPersonCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<TenantPersonCondition> conditionList) {
        this.conditionList = conditionList;
    }

    public String getOrderByCd() {
        return orderByCd;
    }

    public void setOrderByCd(String orderByCd) {
        this.orderByCd = orderByCd;
    }

    public String getTargetCorpId() {
        return targetCorpId;
    }

    public void setTargetCorpId(String targetCorpId) {
        this.targetCorpId = targetCorpId;
    }

    public int getTbuildingPersonCnt() {
        return tbuildingPersonCnt;
    }

    public void setTbuildingPersonCnt(int tbuildingPersonCnt) {
        this.tbuildingPersonCnt = tbuildingPersonCnt;
    }

    public boolean isAllSelectDisabled() {
        return allSelectDisabled;
    }

    public void setAllSelectDisabled(boolean allSelectDisabled) {
        this.allSelectDisabled = allSelectDisabled;
    }

    public boolean isCurrentSelectDisabled() {
        return currentSelectDisabled;
    }

    public void setCurrentSelectDisabled(boolean CurrentSelectEnable) {
        this.currentSelectDisabled = CurrentSelectEnable;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    public boolean isPopUpFlg() {
        return popUpFlg;
    }

    public void setPopUpFlg(boolean popUpFlg) {
        this.popUpFlg = popUpFlg;
    }

    public MPerson getSelectMPerson() {
        return selectMPerson;
    }

    public void setSelectMPerson(MPerson selectMPerson) {
        this.selectMPerson = selectMPerson;
    }

    public boolean isSearchFlg() {
        return searchFlg;
    }

    public void setSearchFlg(boolean searchFlg) {
        this.searchFlg = searchFlg;
    }

    public String getTargetCorpName() {
        return targetCorpName;
    }

    public void setTargetCorpName(String targetCorpName) {
        this.targetCorpName = targetCorpName;
    }

}
