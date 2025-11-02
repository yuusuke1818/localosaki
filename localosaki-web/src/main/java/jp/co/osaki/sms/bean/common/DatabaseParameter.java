package jp.co.osaki.sms.bean.common;

import java.sql.Timestamp;
import java.util.List;

/**
 * DBパラメータクラス
 *
 * @author ozaki.y
 */
public class DatabaseParameter<T extends DatabaseCondition> {
    /** 担当企業ID */
    private String personCorpId;

    /** 担当者ID */
    private String personId;

    /** 現在日時 */
    private Timestamp targetDateTime;

    /** 使用機能 */
    private String funcType;

    /** 条件リスト */
    private List<T> conditionList;

    public String getPersonCorpId() {
        return personCorpId;
    }

    public void setPersonCorpId(String personCorpId) {
        this.personCorpId = personCorpId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Timestamp getTargetDateTime() {
        return targetDateTime;
    }

    public void setTargetDateTime(Timestamp targetDateTime) {
        this.targetDateTime = targetDateTime;
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public List<T> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<T> conditionList) {
        this.conditionList = conditionList;
    }
}
