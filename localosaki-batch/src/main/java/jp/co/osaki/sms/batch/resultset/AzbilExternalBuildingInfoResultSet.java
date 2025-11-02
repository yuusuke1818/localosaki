package jp.co.osaki.sms.batch.resultset;

/**
 * アズビルサーバ送信用建物リスト ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilExternalBuildingInfoResultSet {
    /** 外部建物ID */
	private String buildngInfoId;

    /** 認証ユーザID */
	private String username;

    /** 認証ユーザパスワード */
	private String password;

    /** 企業ID */
	private String corpId;

    /** 建物ID */
	private Long buildingId;

    /**
     * コンストラクタ
     *
     * @param buildngInfoId
     * @param username
     * @param password
     * @param corpId
     * @param buildingId
     */
    public AzbilExternalBuildingInfoResultSet(String buildngInfoId, String username, String password,
    		String corpId, Long buildingId) {
        this.buildngInfoId = buildngInfoId;
        this.username = username;
        this.password = password;
        this.corpId = corpId;
        this.buildingId = buildingId;
    }

    /**
     * @return buildngInfoId
     */
	public String getBuildngInfoId() {
		return this.buildngInfoId;
	}

    /**
     * @param buildngInfoId セットする buildngInfoId
     */
	public void setBuildngInfoId(String buildngInfoId) {
		this.buildngInfoId = buildngInfoId;
	}

    /**
     * @return username
     */
	public String getUsername() {
		return this.username;
	}

    /**
     * @param username セットする username
     */
	public void setUsername(String username) {
		this.username = username;
	}

    /**
     * @return password
     */
	public String getPassword() {
		return this.password;
	}

    /**
     * @param password セットする password
     */
	public void setPassword(String password) {
		this.password = password;
	}

    /**
     * @return corpId
     */
	public String getCorpId() {
		return this.corpId;
	}

    /**
     * @param corpId セットする corpId
     */
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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

}
