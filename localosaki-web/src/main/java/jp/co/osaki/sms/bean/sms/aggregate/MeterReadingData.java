package jp.co.osaki.sms.bean.sms.aggregate;

public class MeterReadingData {

    private String corpId;

    private String corpName;

    private boolean tenantFlg;

    private String buildingNo;

    private String buildingName;

    private String meterReadingSerialNumber;

    private String meterReadingDate;

    private String inspTypeName;

    private Long buildingId;

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public boolean isTenantFlg() {
		return tenantFlg;
	}

	public void setTenantFlg(boolean tenantFlg) {
		this.tenantFlg = tenantFlg;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getMeterReadingSerialNumber() {
		return meterReadingSerialNumber;
	}

	public void setMeterReadingSerialNumber(String meterReadingSerialNumber) {
		this.meterReadingSerialNumber = meterReadingSerialNumber;
	}

	public String getMeterReadingDate() {
		return meterReadingDate;
	}

	public void setMeterReadingDate(String meterReadingDate) {
		this.meterReadingDate = meterReadingDate;
	}

	public String getInspTypeName() {
		return inspTypeName;
	}

	public void setInspTypeName(String inspTypeName) {
		this.inspTypeName = inspTypeName;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;;
	}


	@Override
	public String toString() {
		return "{企業ID=" + this.corpId + "  /企業名=" + this.corpName + "  /建物番号=" + this.buildingNo + "  /建物名=" + this.buildingName
				+ "  /検針連番="+ this.meterReadingSerialNumber + "  /検針日時=" + this.meterReadingDate +  "  /検針種別=" + this.inspTypeName+ "  /建物ID="+ this.buildingId +"}\n";
	}

}
