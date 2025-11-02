package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_aggregate_reservation_info database table.
 * 
 */
@Embeddable
public class TAggregateReservationInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="person_corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personCorpId;

	@Column(name="person_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personId;

	@Column(name="aggregate_id", unique=true, nullable=false)
	private Long aggregateId;

	public TAggregateReservationInfoPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getPersonCorpId() {
		return this.personCorpId;
	}
	public void setPersonCorpId(String personCorpId) {
		this.personCorpId = personCorpId;
	}
	public String getPersonId() {
		return this.personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public Long getAggregateId() {
		return this.aggregateId;
	}
	public void setAggregateId(Long aggregateId) {
		this.aggregateId = aggregateId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TAggregateReservationInfoPK)) {
			return false;
		}
		TAggregateReservationInfoPK castOther = (TAggregateReservationInfoPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.personCorpId.equals(castOther.personCorpId)
			&& this.personId.equals(castOther.personId)
			&& this.aggregateId.equals(castOther.aggregateId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.personCorpId.hashCode();
		hash = hash * prime + this.personId.hashCode();
		hash = hash * prime + this.aggregateId.hashCode();
		
		return hash;
	}
}