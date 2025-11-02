package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_sm_connect_control_product database table.
 * 
 */
@Entity
@Table(name="t_sm_connect_control_product")
@NamedQuery(name="TSmConnectControlProduct.findAll", query="SELECT t FROM TSmConnectControlProduct t")
public class TSmConnectControlProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmConnectControlProductPK id;

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

	//bi-directional many-to-one association to TSmConnectControlSetting
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="sm_connect_control_setting_id", nullable=false, insertable=false, updatable=false)
	private TSmConnectControlSetting TSmConnectControlSetting;

	//bi-directional many-to-one association to MProductSpec
	@ManyToOne
	@JoinColumn(name="product_cd", nullable=false, insertable=false, updatable=false)
	private MProductSpec MProductSpec;

	public TSmConnectControlProduct() {
	}

	public TSmConnectControlProductPK getId() {
		return this.id;
	}

	public void setId(TSmConnectControlProductPK id) {
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

	public TSmConnectControlSetting getTSmConnectControlSetting() {
		return this.TSmConnectControlSetting;
	}

	public void setTSmConnectControlSetting(TSmConnectControlSetting TSmConnectControlSetting) {
		this.TSmConnectControlSetting = TSmConnectControlSetting;
	}

	public MProductSpec getMProductSpec() {
		return this.MProductSpec;
	}

	public void setMProductSpec(MProductSpec MProductSpec) {
		this.MProductSpec = MProductSpec;
	}

}