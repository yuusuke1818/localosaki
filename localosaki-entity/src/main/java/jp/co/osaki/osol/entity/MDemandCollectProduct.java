package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_demand_collect_product database table.
 * 
 */
@Entity
@Table(name="m_demand_collect_product")
@NamedQuery(name="MDemandCollectProduct.findAll", query="SELECT m FROM MDemandCollectProduct m")
public class MDemandCollectProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MDemandCollectProductPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MDemandCollect
	@ManyToOne
	@JoinColumn(name="demand_collect_cd", nullable=false, insertable=false, updatable=false)
	private MDemandCollect MDemandCollect;

	//bi-directional many-to-one association to MProductSpec
	@ManyToOne
	@JoinColumn(name="product_cd", nullable=false, insertable=false, updatable=false)
	private MProductSpec MProductSpec;

	public MDemandCollectProduct() {
	}

	public MDemandCollectProductPK getId() {
		return this.id;
	}

	public void setId(MDemandCollectProductPK id) {
		this.id = id;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public MDemandCollect getMDemandCollect() {
		return this.MDemandCollect;
	}

	public void setMDemandCollect(MDemandCollect MDemandCollect) {
		this.MDemandCollect = MDemandCollect;
	}

	public MProductSpec getMProductSpec() {
		return this.MProductSpec;
	}

	public void setMProductSpec(MProductSpec MProductSpec) {
		this.MProductSpec = MProductSpec;
	}

}