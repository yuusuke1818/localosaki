package jp.skygroup.enl.webap.base;

import java.util.Map;

public abstract class BaseSearchCondition {
    private int orderNo;

    //条件選択有効
    private boolean selectEnable;
    //現在の選択値
    private String selectConditionCd;
    //選択可能項目
    private Map<String, String> selectConditionMap;

    //条件 キーワード指定
    private boolean keywordSelectEnable;
    private String conditionKeyword;

    // 同じ条件が複数選択可能
    private boolean multiSelectEnable;

    //一致条件表示
    private String mutchingTypeCd;
    
    //削除ボタン
    private boolean deleteButtonEnable;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    abstract public boolean isDefaultCondition();

    public boolean isSelectEnable() {
        return selectEnable;
    }

    public boolean isSelectDisable() {
        return !selectEnable;
    }

    public void setSelectEnable(boolean selectEnable) {
        this.selectEnable = selectEnable;
    }

    public String getSelectConditionCd() {
        return selectConditionCd;
    }

    public void setSelectConditionCd(String selectConditionCd) {
        this.selectConditionCd = selectConditionCd;
    }

    public Map<String, String> getSelectConditionMap() {
        return selectConditionMap;
    }

    public void setSelectConditionMap(Map<String, String> selectConditionMap) {
        this.selectConditionMap = selectConditionMap;
    }

    public boolean isKeywordSelectEnable() {
        return keywordSelectEnable;
    }

    public void setKeywordSelectEnable(boolean keywordSelectEnable) {
        this.keywordSelectEnable = keywordSelectEnable;
    }

    public String getConditionKeyword() {
        return conditionKeyword;
    }

    public void setConditionKeyword(String conditionKeyword) {
        this.conditionKeyword = conditionKeyword;
    }

    public boolean isMultiSelectEnable() {
        return multiSelectEnable;
    }

    public void setMultiSelectEnable(boolean multiSelectEnable) {
        this.multiSelectEnable = multiSelectEnable;
    }

    public String getMutchingTypeCd() {
        return mutchingTypeCd;
    }

    public void setMutchingTypeCd(String mutchingTypeCd) {
        this.mutchingTypeCd = mutchingTypeCd;
    }

    public boolean isDeleteButtonEnable() {
        return deleteButtonEnable;
    }

    public void setDeleteButtonEnable(boolean deleteButtonEnable) {
        this.deleteButtonEnable = deleteButtonEnable;
    }

}
