package jp.co.osaki.sms.resultset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class InspectionMeterSrvBulkResultSet {

	public InspectionMeterSrvBulkResultSet(Object[] columns) {

		super();
		this.corpId = (String)columns[0];
		this.corpName = (String)columns[1];
		this.buildingId = (BigInteger)columns[2];
		this.buildingNo = (String)columns[3];
		this.buildingName = (String)columns[4];
		this.buildingAdress = (String)columns[5];
		this.prefectureCd = (String)columns[6];
		this.prefectureName = (String)columns[7];
		this.tenantId = (BigInteger)columns[8];
		this.tenantBuildingNo = (String)columns[9];
		this.tenantName = (String)columns[10];
		this.userCd = (BigInteger)columns[11];
		this.buildingType = (String)columns[12];
		this.devId = (String)columns[13];
		this.devName = (String)columns[14];
		this.meterMngId = (BigInteger)columns[15];
		this.meterType = (BigInteger)columns[16];
		this.meterTypeName = (String)columns[17];
		this.inspYear = (String)columns[18];
		this.inspMonth = (String)columns[19];
		this.inspMonthNo = (BigInteger)columns[20];
		this.inspType = (String)columns[21];
		this.latestInspVal = (BigDecimal)columns[22];
		this.prevInspVal = (BigDecimal)columns[23];
		this.multipleRate = (BigDecimal)columns[24];
		this.latestUseVal = (BigDecimal)columns[25];
		this.prevUseVal = (BigDecimal)columns[26];
		this.usePerRate = (BigDecimal)columns[27];
		this.latestInspDate = (Timestamp)columns[28];
		this.prevInspDate = (Timestamp)columns[29];
		this.endFlg = (BigDecimal)columns[30];
	}

	private String corpId;               // 企業ID
	private String corpName;             // 企業名
	private BigInteger buildingId;       // 建物ID
	private String buildingNo;           // 建物番号
	private String buildingName;         // 建物名
	private String buildingAdress;       // 建物住所
	private String prefectureCd;         // 都道府県コード
	private String prefectureName;       // 都道府県名
	private BigInteger tenantId;         // テナントID
	private String tenantBuildingNo;     // テナント建物番号
	private String tenantName;           // テナント名
	private BigInteger userCd;           // ユーザーコード
	private String buildingType;         // 建物種別
	private String devId;                // 機器ID
	private String devName;              // 機器名
	private BigInteger meterMngId;       // メーター管理番号
	private BigInteger meterType;        // メーター種別
	private String meterTypeName;        // メーター種別名
	private String inspYear;             // 検針年
	private String inspMonth;            // 検針月
	private BigInteger inspMonthNo;      // 月検針連番
	private String inspType;             // 検針種別
	private BigDecimal latestInspVal;    // 最新検針値
	private BigDecimal prevInspVal;      // 前回検針値
	private BigDecimal multipleRate;     // 乗率
	private BigDecimal latestUseVal;     // 今回使用量
	private BigDecimal prevUseVal;       // 前回使用量
	private BigDecimal usePerRate;       // 使用量率
	private Timestamp latestInspDate;    // 最新検針日時
	private Timestamp prevInspDate;      // 前回検針日時
	private BigDecimal endFlg;           // 処理終了フラグ

	// 前年同月の情報は別SQLで取得
	private Timestamp prevYearInspDate; // 前年同月検針日時
	private BigDecimal prevYearUseVal;  // 前年同月使用量

	// 最大デマンド値は別SQLで取得
	private BigDecimal maxDemand;       // 最大デマンド値

	public String getCorpId() {
		return corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public Long getBuildingId() {
		return buildingId != null ? buildingId.longValue() : null;
	}
	public String getBuildingNo() {
		return buildingNo;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public String getBuildingAdress() {
		return buildingAdress;
	}
	public String getPrefectureCd() {
		return prefectureCd;
	}
	public String getPrefectureName() {
		return prefectureName;
	}
	public Long getTenantId() {
		return tenantId != null ? tenantId.longValue() : null;
	}
	public String getTenantBuildingNo() {
		return tenantBuildingNo;
	}
	public String getTenantName() {
		return tenantName;
	}
	public Long getUserCd() {
		return userCd != null ? userCd.longValue() : null;
	}
	public String getBuildingType() {
		return buildingType;
	}
	public String getDevId() {
		return devId;
	}
	public String getDevName() {
		return devName;
	}
	public Long getMeterMngId() {
		return meterMngId != null ? meterMngId.longValue() : null;
	}
	public Long getMeterType() {
		return meterType != null ? meterType.longValue() : null;
	}
	public String getMeterTypeName() {
		return meterTypeName;
	}
	public String getInspYear() {
		return inspYear;
	}
	public String getInspMonth() {
		return inspMonth;
	}
	public Long getInspMonthNo() {
		return inspMonthNo != null ? inspMonthNo.longValue() : null;
	}
	public String getInspType() {
		return inspType;
	}
	public BigDecimal getLatestInspVal() {
		return latestInspVal;
	}
	public BigDecimal getPrevInspVal() {
		return prevInspVal;
	}
	public BigDecimal getMultipleRate() {
		return multipleRate;
	}
	public BigDecimal getLatestUseVal() {
		return latestUseVal;
	}
	public BigDecimal getPrevUseVal() {
		return prevUseVal;
	}
	public BigDecimal getUsePerRate() {
		return usePerRate;
	}
	public Date getLatestInspDate() {
		return latestInspDate != null ? new Date(latestInspDate.getTime()) : null;
	}
	public Date getPrevInspDate() {
		return prevInspDate != null ? new Date(prevInspDate.getTime()) : null;
	}
	public BigDecimal getEndFlg() {
		return endFlg;
	}
	public String getStatus() {

		if (endFlg != null) {

			if (endFlg.intValue() == 0) {
				return "未完了";
			}
			else if(endFlg.intValue() == 1) {
				return "完了";
			}
		}
		return "";
	}
	public Timestamp getPrevYearInspDate() {
		return prevYearInspDate;
	}
	public void setPrevYearInspDate(Timestamp prevYearInspDate) {
		this.prevYearInspDate = prevYearInspDate;
	}
	public BigDecimal getPrevYearUseVal() {
		return prevYearUseVal;
	}
	public void setPrevYearUseVal(BigDecimal prevYearUseVal) {
		this.prevYearUseVal = prevYearUseVal;
	}
	public BigDecimal getMaxDemand() {
		return maxDemand;
	}
	public void setMaxDemand(BigDecimal maxDemand) {
		this.maxDemand = maxDemand;
	}
}
