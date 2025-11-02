package jp.co.osaki.sms.batch.resultset;

/**
 * 日報データ前日分 再収集予約処理実行用既存コマンド件数ResultSetクラス
 * @author sagi_h
 *
 */
public class PrevSurveyRetrialCommandCountResultSet {
    /** 指定条件に合致するコマンド件数 */
    private Long count;

    /**
     * @param count
     */
    public PrevSurveyRetrialCommandCountResultSet(Long count) {
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
