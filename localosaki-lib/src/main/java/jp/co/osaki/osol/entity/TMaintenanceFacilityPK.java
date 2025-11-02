package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_maintenance_facility database table.
 * 
 */
@Embeddable
public class TMaintenanceFacilityPK implements Serializable {
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

	@Column(name="maintenance_history_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long maintenanceHistoryId;

	@Column(name="facility_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilityId;

	@Column(name="maintenance_facility_id", unique=true, nullable=false)
	private Long maintenanceFacilityId;

	public TMaintenanceFacilityPK() {
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
	public Long getMaintenanceHistoryId() {
		return this.maintenanceHistoryId;
	}
	public void setMaintenanceHistoryId(Long maintenanceHistoryId) {
		this.maintenanceHistoryId = maintenanceHistoryId;
	}
	public Long getFacilityId() {
		return this.facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public Long getMaintenanceFacilityId() {
		return this.maintenanceFacilityId;
	}
	public void setMaintenanceFacilityId(Long maintenanceFacilityId) {
		this.maintenanceFacilityId = maintenanceFacilityId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TMaintenanceFacilityPK)) {
			return false;
		}
		TMaintenanceFacilityPK castOther = (TMaintenanceFacilityPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.maintenanceId.equals(castOther.maintenanceId)
			&& this.maintenanceRequestId.equals(castOther.maintenanceRequestId)
			&& this.maintenanceHistoryId.equals(castOther.maintenanceHistoryId)
			&& this.facilityId.equals(castOther.facilityId)
			&& this.maintenanceFacilityId.equals(castOther.maintenanceFacilityId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.maintenanceId.hashCode();
		hash = hash * prime + this.maintenanceRequestId.hashCode();
		hash = hash * prime + this.maintenanceHistoryId.hashCode();
		hash = hash * prime + this.facilityId.hashCode();
		hash = hash * prime + this.maintenanceFacilityId.hashCode();
		
		return hash;
	}
}