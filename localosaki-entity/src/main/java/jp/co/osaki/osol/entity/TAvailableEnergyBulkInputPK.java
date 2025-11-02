package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_available_energy_bulk_input database table.
 * 
 */
@Embeddable
public class TAvailableEnergyBulkInputPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(unique=true, nullable=false, length=4)
	private String year;

	@Column(name="eng_bulk_input_id", unique=true, nullable=false)
	private Long engBulkInputId;

	public TAvailableEnergyBulkInputPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getYear() {
		return this.year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Long getEngBulkInputId() {
		return this.engBulkInputId;
	}
	public void setEngBulkInputId(Long engBulkInputId) {
		this.engBulkInputId = engBulkInputId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TAvailableEnergyBulkInputPK)) {
			return false;
		}
		TAvailableEnergyBulkInputPK castOther = (TAvailableEnergyBulkInputPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.year.equals(castOther.year)
			&& this.engBulkInputId.equals(castOther.engBulkInputId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.year.hashCode();
		hash = hash * prime + this.engBulkInputId.hashCode();
		
		return hash;
	}
}