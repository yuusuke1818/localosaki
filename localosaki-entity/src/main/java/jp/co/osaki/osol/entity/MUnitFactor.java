package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_unit_factor database table.
 * 
 */
@Entity
@Table(name="m_unit_factor")
@NamedQuery(name="MUnitFactor.findAll", query="SELECT m FROM MUnitFactor m")
public class MUnitFactor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="unit_factor_cd", unique=true, nullable=false, length=50)
	private String unitFactorCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="unit_factor_name", nullable=false, length=100)
	private String unitFactorName;

	@Column(name="unit_factor_unit", nullable=false, length=100)
	private String unitFactorUnit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MUnitDivide
	@OneToMany(mappedBy="MUnitFactor")
	private List<MUnitDivide> MUnitDivides;

	public MUnitFactor() {
	}

	public String getUnitFactorCd() {
		return this.unitFactorCd;
	}

	public void setUnitFactorCd(String unitFactorCd) {
		this.unitFactorCd = unitFactorCd;
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

	public String getUnitFactorName() {
		return this.unitFactorName;
	}

	public void setUnitFactorName(String unitFactorName) {
		this.unitFactorName = unitFactorName;
	}

	public String getUnitFactorUnit() {
		return this.unitFactorUnit;
	}

	public void setUnitFactorUnit(String unitFactorUnit) {
		this.unitFactorUnit = unitFactorUnit;
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

	public List<MUnitDivide> getMUnitDivides() {
		return this.MUnitDivides;
	}

	public void setMUnitDivides(List<MUnitDivide> MUnitDivides) {
		this.MUnitDivides = MUnitDivides;
	}

	public MUnitDivide addMUnitDivide(MUnitDivide MUnitDivide) {
		getMUnitDivides().add(MUnitDivide);
		MUnitDivide.setMUnitFactor(this);

		return MUnitDivide;
	}

	public MUnitDivide removeMUnitDivide(MUnitDivide MUnitDivide) {
		getMUnitDivides().remove(MUnitDivide);
		MUnitDivide.setMUnitFactor(null);

		return MUnitDivide;
	}

}