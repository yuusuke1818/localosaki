package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_generic_type database table.
 * 
 */
@Embeddable
public class MGenericTypePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="group_code", unique=true, nullable=false, length=3)
	private String groupCode;

	@Column(name="kbn_code", unique=true, nullable=false, length=6)
	private String kbnCode;

	public MGenericTypePK() {
	}
	public String getGroupCode() {
		return this.groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getKbnCode() {
		return this.kbnCode;
	}
	public void setKbnCode(String kbnCode) {
		this.kbnCode = kbnCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MGenericTypePK)) {
			return false;
		}
		MGenericTypePK castOther = (MGenericTypePK)other;
		return 
			this.groupCode.equals(castOther.groupCode)
			&& this.kbnCode.equals(castOther.kbnCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.groupCode.hashCode();
		hash = hash * prime + this.kbnCode.hashCode();
		
		return hash;
	}
}