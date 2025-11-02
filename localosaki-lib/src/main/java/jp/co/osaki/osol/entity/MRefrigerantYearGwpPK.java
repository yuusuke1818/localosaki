package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_refrigerant_year_gwp database table.
 * 
 */
@Embeddable
public class MRefrigerantYearGwpPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="refrigerant_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long refrigerantId;

	@Column(unique=true, nullable=false, length=4)
	private String year;

	public MRefrigerantYearGwpPK() {
	}
	public Long getRefrigerantId() {
		return this.refrigerantId;
	}
	public void setRefrigerantId(Long refrigerantId) {
		this.refrigerantId = refrigerantId;
	}
	public String getYear() {
		return this.year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MRefrigerantYearGwpPK)) {
			return false;
		}
		MRefrigerantYearGwpPK castOther = (MRefrigerantYearGwpPK)other;
		return 
			this.refrigerantId.equals(castOther.refrigerantId)
			&& this.year.equals(castOther.year);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.refrigerantId.hashCode();
		hash = hash * prime + this.year.hashCode();
		
		return hash;
	}
}