package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_sm_air_setting database table.
 * 
 */
@Entity
@Table(name="m_sm_air_setting")
@NamedQuery(name="MSmAirSetting.findAll", query="SELECT m FROM MSmAirSetting m")
public class MSmAirSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmAirSettingPK id;

	@Column(name="cooling_differential", precision=3, scale=1)
	private BigDecimal coolingDifferential;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="heating_differential", precision=3, scale=1)
	private BigDecimal heatingDifferential;

	@Column(name="link_output_port_no", precision=3)
	private BigDecimal linkOutputPortNo;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public MSmAirSetting() {
	}

	public MSmAirSettingPK getId() {
		return this.id;
	}

	public void setId(MSmAirSettingPK id) {
		this.id = id;
	}

	public BigDecimal getCoolingDifferential() {
		return this.coolingDifferential;
	}

	public void setCoolingDifferential(BigDecimal coolingDifferential) {
		this.coolingDifferential = coolingDifferential;
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

	public BigDecimal getHeatingDifferential() {
		return this.heatingDifferential;
	}

	public void setHeatingDifferential(BigDecimal heatingDifferential) {
		this.heatingDifferential = heatingDifferential;
	}

	public BigDecimal getLinkOutputPortNo() {
		return this.linkOutputPortNo;
	}

	public void setLinkOutputPortNo(BigDecimal linkOutputPortNo) {
		this.linkOutputPortNo = linkOutputPortNo;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

}