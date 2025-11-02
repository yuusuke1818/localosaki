package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_freon_filling_report database table.
 * 
 */
@Embeddable
public class TFreonFillingReportPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="maintenance_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long maintenanceId;

	@Column(name="maintenance_request_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long maintenanceRequestId;

	@Column(name="facility_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilityId;

	@Column(name="freon_filling_report_id", unique=true, nullable=false)
	private Long freonFillingReportId;

	public TFreonFillingReportPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getBuildingId() {
		return this.buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Long getMaintenanceId() {
		return this.maintenanceId;
	}
	public void setMaintenanceId(Long maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	public Long getMaintenanceRequestId() {
		return this.maintenanceRequestId;
	}
	public void setMaintenanceRequestId(Long maintenanceRequestId) {
		this.maintenanceRequestId = maintenanceRequestId;
	}
	public Long getFacilityId() {
		return this.facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public Long getFreonFillingReportId() {
		return this.freonFillingReportId;
	}
	public void setFreonFillingReportId(Long freonFillingReportId) {
		this.freonFillingReportId = freonFillingReportId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TFreonFillingReportPK)) {
			return false;
		}
		TFreonFillingReportPK castOther = (TFreonFillingReportPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.maintenanceId.equals(castOther.maintenanceId)
			&& this.maintenanceRequestId.equals(castOther.maintenanceRequestId)
			&& this.facilityId.equals(castOther.facilityId)
			&& this.freonFillingReportId.equals(castOther.freonFillingReportId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.maintenanceId.hashCode();
		hash = hash * prime + this.maintenanceRequestId.hashCode();
		hash = hash * prime + this.facilityId.hashCode();
		hash = hash * prime + this.freonFillingReportId.hashCode();
		
		return hash;
	}
}