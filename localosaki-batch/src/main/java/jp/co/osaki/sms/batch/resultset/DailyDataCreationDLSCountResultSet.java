package jp.co.osaki.sms.batch.resultset;

/**
 * 日報データ作成用ロードサーベイ日データ件数 ResultSetクラス
 * @author sagi_h
 *
 */
public class DailyDataCreationDLSCountResultSet {
    /** 指定条件に合致するロードサーベイデータの件数 */
    private Long count;

    /**
     * @param count
     */
    public DailyDataCreationDLSCountResultSet(Long count) {
        this.count = count;
    }

    /**
     * @return count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count セットする count
     */
    public void setCount(Long count) {
        this.count = count;
    }


}
