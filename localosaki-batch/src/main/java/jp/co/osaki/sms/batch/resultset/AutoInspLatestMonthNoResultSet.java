package jp.co.osaki.sms.batch.resultset;

public class AutoInspLatestMonthNoResultSet {

    /** 月検針連番 1から999. */
    private Long inspMonthNo;

    /** データ有無フラグ. true:過去に登録あり false:新規自動検針 . */
    private boolean isFind;

    public AutoInspLatestMonthNoResultSet(Long inspMonthNo, boolean isFind) {
        super();
        this.inspMonthNo = inspMonthNo;
        this.isFind = isFind;
    }

    public Long getInspMonthNo() {
        return inspMonthNo;
    }

    public void setInspMonthNo(Long inspMonthNo) {
        this.inspMonthNo = inspMonthNo;
    }

    public boolean getIsFind() {
        return isFind;
    }

    public void setIsFind(boolean isFind) {
        this.isFind = isFind;
    }

}
