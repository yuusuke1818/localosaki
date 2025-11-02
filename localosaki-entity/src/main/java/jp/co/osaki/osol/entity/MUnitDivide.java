package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_unit_divide database table.
 * 
 */
@Entity
@Table(name="m_unit_divide")
@NamedQuery(name="MUnitDivide.findAll", query="SELECT m FROM MUnitDivide m")
public class MUnitDivide implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MUnitDividePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="total_avg_type", nullable=false, length=1)
	private String totalAvgType;

	@Column(name="unit_divide_formula", length=1000)
	private String unitDivideFormula;

	@Column(name="unit_divide_name", nullable=false, length=100)
	private String unitDivideName;

	@Column(name="unit_divide_power_root", length=6)
	private String unitDividePowerRoot;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildUnitDenominator
	@OneToMany(mappedBy="MUnitDivide", cascade={CascadeType.ALL})
	private List<MBuildUnitDenominator> MBuildUnitDenominators;

	//bi-directional many-to-one association to MSubtype
	@OneToMany(mappedBy="MUnitDivide")
	private List<MSubtype> MSubtypes;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to MUnitFactor
	@ManyToOne
	@JoinColumn(name="unit_factor_cd")
	private MUnitFactor MUnitFactor;

	public MUnitDivide() {
	}

	public MUnitDividePK getId() {
		return this.id;
	}

	public void setId(MUnitDividePK id) {
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

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public String getTotalAvgType() {
		return this.totalAvgType;
	}

	public void setTotalAvgType(String totalAvgType) {
		this.totalAvgType = totalAvgType;
	}

	public String getUnitDivideFormula() {
		return this.unitDivideFormula;
	}

	public void setUnitDivideFormula(String unitDivideFormula) {
		this.unitDivideFormula = unitDivideFormula;
	}

	public String getUnitDivideName() {
		return this.unitDivideName;
	}

	public void setUnitDivideName(String unitDivideName) {
		this.unitDivideName = unitDivideName;
	}

	public String getUnitDividePowerRoot() {
		return this.unitDividePowerRoot;
	}

	public void setUnitDividePowerRoot(String unitDividePowerRoot) {
		this.unitDividePowerRoot = unitDividePowerRoot;
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

	public List<MBuildUnitDenominator> getMBuildUnitDenominators() {
		return this.MBuildUnitDenominators;
	}

	public void setMBuildUnitDenominators(List<MBuildUnitDenominator> MBuildUnitDenominators) {
		this.MBuildUnitDenominators = MBuildUnitDenominators;
	}

	public MBuildUnitDenominator addMBuildUnitDenominator(MBuildUnitDenominator MBuildUnitDenominator) {
		getMBuildUnitDenominators().add(MBuildUnitDenominator);
		MBuildUnitDenominator.setMUnitDivide(this);

		return MBuildUnitDenominator;
	}

	public MBuildUnitDenominator removeMBuildUnitDenominator(MBuildUnitDenominator MBuildUnitDenominator) {
		getMBuildUnitDenominators().remove(MBuildUnitDenominator);
		MBuildUnitDenominator.setMUnitDivide(null);

		return MBuildUnitDenominator;
	}

	public List<MSubtype> getMSubtypes() {
		return this.MSubtypes;
	}

	public void setMSubtypes(List<MSubtype> MSubtypes) {
		this.MSubtypes = MSubtypes;
	}

	public MSubtype addMSubtype(MSubtype MSubtype) {
		getMSubtypes().add(MSubtype);
		MSubtype.setMUnitDivide(this);

		return MSubtype;
	}

	public MSubtype removeMSubtype(MSubtype MSubtype) {
		getMSubtypes().remove(MSubtype);
		MSubtype.setMUnitDivide(null);

		return MSubtype;
	}

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

	public MUnitFactor getMUnitFactor() {
		return this.MUnitFactor;
	}

	public void setMUnitFactor(MUnitFactor MUnitFactor) {
		this.MUnitFactor = MUnitFactor;
	}

}