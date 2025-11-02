package jp.co.osaki.sms.batch.resultset;

/**
 * アズビルサーバ外部起動設定情報 ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilBatchStartupInfoResultSet {
    /** ID */
	private Long batchId;

    /** 対象日時 */
	private String exeDate;

    /**
     * コンストラクタ
     *
     * @param buildngInfoId
     * @param username
     * @param password
     * @param corpId
     * @param buildingId
     */
    public AzbilBatchStartupInfoResultSet(Long batchId, String exeDate) {
        this.batchId = batchId;
        this.exeDate = exeDate;
    }

    /**
     * @return batchId
     */
	public Long getBatchId() {
		return this.batchId;
	}

    /**
     * @param batchId セットする batchId
     */
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

    /**
     * @return exeDate
     */
	public String getExeDate() {
		return this.exeDate;
	}

    /**
     * @param exeDate セットする exeDate
     */
	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
	}

}
