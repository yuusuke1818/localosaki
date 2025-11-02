package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_graph_element database table.
 * 
 */
@Embeddable
public class MGraphElementPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="line_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long lineGroupId;

	@Column(name="graph_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long graphId;

	@Column(name="graph_element_id", unique=true, nullable=false)
	private Long graphElementId;

	public MGraphElementPK() {
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
	public Long getLineGroupId() {
		return this.lineGroupId;
	}
	public void setLineGroupId(Long lineGroupId) {
		this.lineGroupId = lineGroupId;
	}
	public Long getGraphId() {
		return this.graphId;
	}
	public void setGraphId(Long graphId) {
		this.graphId = graphId;
	}
	public Long getGraphElementId() {
		return this.graphElementId;
	}
	public void setGraphElementId(Long graphElementId) {
		this.graphElementId = graphElementId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MGraphElementPK)) {
			return false;
		}
		MGraphElementPK castOther = (MGraphElementPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.lineGroupId.equals(castOther.lineGroupId)
			&& this.graphId.equals(castOther.graphId)
			&& this.graphElementId.equals(castOther.graphElementId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.lineGroupId.hashCode();
		hash = hash * prime + this.graphId.hashCode();
		hash = hash * prime + this.graphElementId.hashCode();
		
		return hash;
	}
}