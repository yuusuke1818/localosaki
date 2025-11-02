package jp.co.osaki.sms.bean.common;

import java.math.BigDecimal;

/**
 * DB条件クラス
 *
 * @author ozaki.y
 */
public class DatabaseCondition {

    /** ORフラグ */
    private boolean isOr;

    /** 検索条件名 */
    private String conditionName;

    /** 検索対象値(文字列) */
    private String strValue;

    /** 検索対象値(数値) */
    private BigDecimal numValue;

    /**
     * コンストラクタ
     *
     * @param conditionName 検索条件名
     */
    public DatabaseCondition(String conditionName) {
        this.conditionName = conditionName;
    }

    /**
     * コンストラクタ
     *
     * @param conditionName 検索条件名
     * @param isOr ORフラグ
     * @param strValue 検索対象値(文字列)
     */
    public DatabaseCondition(String conditionName, boolean isOr, String strValue) {
        this.conditionName = conditionName;
        this.isOr = isOr;
        this.strValue = strValue;
    }

    /**
     * コンストラクタ
     *
     * @param conditionName 検索条件名
     * @param isOr ORフラグ
     * @param numValue 検索対象値(数値)
     */
    public DatabaseCondition(String conditionName, boolean isOr, BigDecimal numValue) {
        this.conditionName = conditionName;
        this.isOr = isOr;
        this.numValue = numValue;
    }

    public boolean isOr() {
        return isOr;
    }

    public String getConditionName() {
        return conditionName;
    }

    public String getStrValue() {
        return strValue;
    }

    public BigDecimal getNumValue() {
        return numValue;
    }
}
