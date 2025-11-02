package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.math.BigDecimal;

import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.bean.common.DatabaseCondition;

/**
 * 建物情報検索Conditionクラス
 *
 */
public class BuildingInfoSearchCondition extends DatabaseCondition {

    /** 建物親グループID */
    private Long parentGroupId;

    /** 建物子グループID */
    private Long childGroupId;

    public BuildingInfoSearchCondition(String conditionName) {
        super(conditionName);
    }

    public BuildingInfoSearchCondition(String conditionName, boolean isOr, BigDecimal numValue) {
        super(conditionName, isOr, numValue);
    }

    public BuildingInfoSearchCondition(String conditionName, boolean isOr, String strValue) {
        super(conditionName, isOr, strValue);
    }

    public Long getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(Long parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public void setParentGroupId(String parentGroupIdStr) {
        if (CheckUtility.isNullOrEmpty(parentGroupIdStr)) {
            return;
        }

        setParentGroupId(Long.valueOf(parentGroupIdStr));
    }

    public Long getChildGroupId() {
        return childGroupId;
    }

    public void setChildGroupId(Long childGroupId) {
        this.childGroupId = childGroupId;
    }

    public void setChildGroupId(String childGroupIdStr) {
        if (CheckUtility.isNullOrEmpty(childGroupIdStr)) {
            return;
        }

        setChildGroupId(Long.valueOf(childGroupIdStr));
    }
}
