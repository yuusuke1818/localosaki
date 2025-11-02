package jp.co.osaki.sms.batch.resultset;

/**
 * アズビルサーバ再送信用情報 ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilResendInfoResultSet {
    /** 建物ID */
	private Long buildingId;

    /** 再送対象日時 */
	private String resendDatetime;

    /**
     * コンストラクタ
     *
     * @param buildngInfoId
     * @param username
     * @param password
     * @param corpId
     * @param buildingId
     */
    public AzbilResendInfoResultSet(Long buildingId, String resendDatetime) {
        this.buildingId = buildingId;
        this.resendDatetime = resendDatetime;
    }

    /**
     * @return buildingId
     */
	public Long getBuildingId() {
		return this.buildingId;
	}

    /**
     * @param buildingId セットする buildingId
     */
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

    /**
     * @return resendDatetime
     */
	public String getResendDatetime() {
		return this.resendDatetime;
	}

    /**
     * @param resendDatetime セットする resendDatetime
     */
	public void setResendDatetime(String resendDatetime) {
		this.resendDatetime = resendDatetime;
	}

}
