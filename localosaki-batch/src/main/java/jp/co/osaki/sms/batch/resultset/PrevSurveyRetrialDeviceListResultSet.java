package jp.co.osaki.sms.batch.resultset;

/**
 * 日報データ前日分 再収集予約処理実行用装置リストResultSetクラス
 * @author sagi_h
 *
 */
public class PrevSurveyRetrialDeviceListResultSet {
    /** 装置ID */
    private String devId;
    /** 逆潮対応フラグ */
    private String revFlg;
    /**
     * @param devId
     * @param revFlg
     */
    public PrevSurveyRetrialDeviceListResultSet(String devId, String revFlg) {
        this.devId = devId;
        this.revFlg = revFlg;
    }
    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }
    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }
    /**
     * @return revFlg
     */
    public String getRevFlg() {
        return revFlg;
    }
    /**
     * @param revFlg セットする revFlg
     */
    public void setRevFlg(String revFlg) {
        this.revFlg = revFlg;
    }

}
