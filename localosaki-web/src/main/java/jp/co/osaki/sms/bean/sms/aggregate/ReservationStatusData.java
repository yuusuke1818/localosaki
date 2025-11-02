package jp.co.osaki.sms.bean.sms.aggregate;

/**
 * 検針データ一括ダウンロード
 * 予約状況画面
 * 表示項目管理クラス
 *
 * @author 22702kansaku
 */

public class ReservationStatusData {

    private String reservationDate;

    private String startDate;

    private String endDate;

    private String reservationHolder;

    private String creationCondition;

    private String status;

    private String result;

    private Boolean flg;

	private Long reservationId;

    private String searchDate;

    private String inspTypeName;

    private String corpParam;

    private String buildingParam;

    private String maxDemand;

    private String prevYearData;

	public Boolean getFlg() {
		return flg;
	}

	public void setFlg(Boolean flg) {
		this.flg = flg;
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getReservationHolder() {
		return reservationHolder;
	}

	public void setReservationHolder(String reservationHolder) {
		this.reservationHolder = reservationHolder;
	}

	public String getCreationCondition() {
		return creationCondition;
	}

	public void setCreationCondition(String creationCondition) {
		this.creationCondition = creationCondition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public String getInspTypeName() {
		return inspTypeName;
	}

	public void setInspTypeName(String inspTypeName) {
		this.inspTypeName = inspTypeName;
	}

	public String getCorpParam() {
		return corpParam;
	}

	public void setCorpParam(String corpParam) {
		this.corpParam = corpParam;
	}

	public String getBuildingParam() {
		return buildingParam;
	}

	public void setBuildingParam(String buildingParam) {
		this.buildingParam = buildingParam;
	}

	public String getMaxDemand() {
		return maxDemand;
	}

	public void setMaxDemand(String maxDemand) {
		this.maxDemand = maxDemand;
	}

	public String getPrevYearData() {
		return prevYearData;
	}

	public void setPrevYearData(String prevYearData) {
		this.prevYearData = prevYearData;
	}
}
