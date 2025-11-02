package jp.co.osaki.sms.batch.resultset;

/**
 * コマンド異常通知用 建物メールアドレス情報 ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class DevCommandBuildingMailAddressResultSet {
    /** 建物ID */
    private Long buildingId;

    /** 建物名 */
    private String buildingName;

    /** SMS建物メールアドレス1 */
    private String mail1;

    /** SMS建物メールアドレス2 */
    private String mail2;

    /** SMS建物メールアドレス3 */
    private String mail3;

    /** SMS建物メールアドレス4 */
    private String mail4;

    /** SMS建物メールアドレス5 */
    private String mail5;

    /** SMS建物メールアドレス6 */
    private String mail6;

    /** SMS建物メールアドレス7 */
    private String mail7;

    /** SMS建物メールアドレス8 */
    private String mail8;

    /** SMS建物メールアドレス9 */
    private String mail9;

    /** SMS建物メールアドレス10 */
    private String mail10;


    /**
     * コンストラクタ
     *
     * @param buildingId
     * @param buildingName
     * @param String mail1～10
     */
    public DevCommandBuildingMailAddressResultSet(Long buildingId, String buildingName,
    		String mail1, String mail2, String mail3, String mail4, String mail5,
    		String mail6, String mail7, String mail8, String mail9, String mail10) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.mail1 = mail1;
        this.mail2 = mail2;
        this.mail3 = mail3;
        this.mail4 = mail4;
        this.mail5 = mail5;
        this.mail6 = mail6;
        this.mail7 = mail7;
        this.mail8 = mail8;
        this.mail9 = mail9;
        this.mail10 = mail10;

    }

    /**
     * @return buildingId
     */
    public Long getBuildingId() {
        return buildingId;
    }

    /**
     * @param buildingId セットする buildingId
     */
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    /**
     * @return buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName セットする buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * @return mail1
     */
	public String getMail1() {
		return this.mail1;
	}

    /**
     * @param mail1 セットする mail1
     */
	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

    /**
     * @return mail2
     */
	public String getMail2() {
		return this.mail2;
	}

    /**
     * @param mail2 セットする mail2
     */
	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

    /**
     * @return mail3
     */
	public String getMail3() {
		return this.mail3;
	}

    /**
     * @param mail3 セットする mail3
     */
	public void setMail3(String mail3) {
		this.mail3 = mail3;
	}

    /**
     * @return mail4
     */
	public String getMail4() {
		return this.mail4;
	}

    /**
     * @param mail4 セットする mail4
     */
	public void setMail4(String mail4) {
		this.mail4 = mail4;
	}

    /**
     * @return mail5
     */
	public String getMail5() {
		return this.mail5;
	}

    /**
     * @param mail5 セットする mail5
     */
	public void setMail5(String mail5) {
		this.mail5 = mail5;
	}

    /**
     * @return mail6
     */
	public String getMail6() {
		return this.mail6;
	}

    /**
     * @param mail6 セットする mail6
     */
	public void setMail6(String mail6) {
		this.mail6 = mail6;
	}

    /**
     * @return mail7
     */
	public String getMail7() {
		return this.mail7;
	}

    /**
     * @param mail7 セットする mail7
     */
	public void setMail7(String mail7) {
		this.mail7 = mail7;
	}

    /**
     * @return mail8
     */
	public String getMail8() {
		return this.mail8;
	}

    /**
     * @param mail8 セットする mail8
     */
	public void setMail8(String mail8) {
		this.mail8 = mail8;
	}

    /**
     * @return mail9
     */
	public String getMail9() {
		return this.mail9;
	}

    /**
     * @param mail9 セットする mail9
     */
	public void setMail9(String mail9) {
		this.mail9 = mail9;
	}

    /**
     * @return mail10
     */
	public String getMail10() {
		return this.mail10;
	}

    /**
     * @param mail10 セットする mail10
     */
	public void setMail10(String mail10) {
		this.mail1 = mail10;
	}

}
