package jp.co.osaki.sms.batch.resultset;

/**
 * アズビル建物毎のメーターリスト ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilExternalBuildingMeterInfoResultSet {
    /** 装置ID */
	private String devId;

	/** メーター管理番号 */
	private Long meterMngId;

    /** メーターID */
	private String meterId;

    /**
     * コンストラクタ
     *
     * @param buildngInfoId
     * @param username
     * @param password
     * @param corpId
     * @param buildingId
     */
    public AzbilExternalBuildingMeterInfoResultSet(String devId, Long meterMngId, String meterId) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.meterId = meterId;
    }

    /**
     * @return meterMngId
     */
	public Long getMeterMngId() {
		return this.meterMngId;
	}

    /**
     * @param meterMngId セットする meterMngId
     */
	public void setMeterMngId(Long meterMngId) {
		this.meterMngId = meterMngId;
	}

    /**
     * @return meterMngId
     */
	public String getDevId() {
		return this.devId;
	}

    /**
     * @param devId セットする devId
     */
	public void setDevId(String devId) {
		this.devId = devId;
	}

    /**
     * @return meterId
     */
	public String getMeterId() {
		return this.meterId;
	}

    /**
     * @param meterId セットする meterId
     */
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

}
