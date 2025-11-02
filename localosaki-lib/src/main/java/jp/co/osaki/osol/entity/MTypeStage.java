package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_type_stage database table.
 * 
 */
@Entity
@Table(name="m_type_stage")
@NamedQuery(name="MTypeStage.findAll", query="SELECT m FROM MTypeStage m")
public class MTypeStage implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MTypeStagePK id;

	@Column(name="a_price", precision=6, scale=2)
	private BigDecimal aPrice;

	@Column(name="a_range", precision=4)
	private BigDecimal aRange;

	@Column(name="b_price", precision=6, scale=2)
	private BigDecimal bPrice;

	@Column(name="b_range", precision=4)
	private BigDecimal bRange;

	@Column(name="c_price", precision=6, scale=2)
	private BigDecimal cPrice;

	@Column(name="c_range", precision=4)
	private BigDecimal cRange;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="d_price", precision=6, scale=2)
	private BigDecimal dPrice;

	@Column(name="d_range", precision=4)
	private BigDecimal dRange;

	@Column(name="e_price", precision=6, scale=2)
	private BigDecimal ePrice;

	@Column(name="e_range", precision=4)
	private BigDecimal eRange;

	@Column(name="f_price", precision=6, scale=2)
	private BigDecimal fPrice;

	@Column(name="f_range", precision=4)
	private BigDecimal fRange;

	@Column(name="g_price", precision=6, scale=2)
	private BigDecimal gPrice;

	@Column(name="g_range", precision=4)
	private BigDecimal gRange;

	@Column(name="h_price", precision=6, scale=2)
	private BigDecimal hPrice;

	@Column(name="h_range", precision=4)
	private BigDecimal hRange;

	@Column(name="i_price", precision=6, scale=2)
	private BigDecimal iPrice;

	@Column(name="i_range", precision=4)
	private BigDecimal iRange;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MTypePower
	@OneToMany(mappedBy="MTypeStage")
	private List<MTypePower> MTypePowers;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MTypeStage() {
	}

	public MTypeStagePK getId() {
		return this.id;
	}

	public void setId(MTypeStagePK id) {
		this.id = id;
	}

	public BigDecimal getAPrice() {
		return this.aPrice;
	}

	public void setAPrice(BigDecimal aPrice) {
		this.aPrice = aPrice;
	}

	public BigDecimal getARange() {
		return this.aRange;
	}

	public void setARange(BigDecimal aRange) {
		this.aRange = aRange;
	}

	public BigDecimal getBPrice() {
		return this.bPrice;
	}

	public void setBPrice(BigDecimal bPrice) {
		this.bPrice = bPrice;
	}

	public BigDecimal getBRange() {
		return this.bRange;
	}

	public void setBRange(BigDecimal bRange) {
		this.bRange = bRange;
	}

	public BigDecimal getCPrice() {
		return this.cPrice;
	}

	public void setCPrice(BigDecimal cPrice) {
		this.cPrice = cPrice;
	}

	public BigDecimal getCRange() {
		return this.cRange;
	}

	public void setCRange(BigDecimal cRange) {
		this.cRange = cRange;
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

	public BigDecimal getDPrice() {
		return this.dPrice;
	}

	public void setDPrice(BigDecimal dPrice) {
		this.dPrice = dPrice;
	}

	public BigDecimal getDRange() {
		return this.dRange;
	}

	public void setDRange(BigDecimal dRange) {
		this.dRange = dRange;
	}

	public BigDecimal getEPrice() {
		return this.ePrice;
	}

	public void setEPrice(BigDecimal ePrice) {
		this.ePrice = ePrice;
	}

	public BigDecimal getERange() {
		return this.eRange;
	}

	public void setERange(BigDecimal eRange) {
		this.eRange = eRange;
	}

	public BigDecimal getFPrice() {
		return this.fPrice;
	}

	public void setFPrice(BigDecimal fPrice) {
		this.fPrice = fPrice;
	}

	public BigDecimal getFRange() {
		return this.fRange;
	}

	public void setFRange(BigDecimal fRange) {
		this.fRange = fRange;
	}

	public BigDecimal getGPrice() {
		return this.gPrice;
	}

	public void setGPrice(BigDecimal gPrice) {
		this.gPrice = gPrice;
	}

	public BigDecimal getGRange() {
		return this.gRange;
	}

	public void setGRange(BigDecimal gRange) {
		this.gRange = gRange;
	}

	public BigDecimal getHPrice() {
		return this.hPrice;
	}

	public void setHPrice(BigDecimal hPrice) {
		this.hPrice = hPrice;
	}

	public BigDecimal getHRange() {
		return this.hRange;
	}

	public void setHRange(BigDecimal hRange) {
		this.hRange = hRange;
	}

	public BigDecimal getIPrice() {
		return this.iPrice;
	}

	public void setIPrice(BigDecimal iPrice) {
		this.iPrice = iPrice;
	}

	public BigDecimal getIRange() {
		return this.iRange;
	}

	public void setIRange(BigDecimal iRange) {
		this.iRange = iRange;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
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

	public List<MTypePower> getMTypePowers() {
		return this.MTypePowers;
	}

	public void setMTypePowers(List<MTypePower> MTypePowers) {
		this.MTypePowers = MTypePowers;
	}

	public MTypePower addMTypePower(MTypePower MTypePower) {
		getMTypePowers().add(MTypePower);
		MTypePower.setMTypeStage(this);

		return MTypePower;
	}

	public MTypePower removeMTypePower(MTypePower MTypePower) {
		getMTypePowers().remove(MTypePower);
		MTypePower.setMTypeStage(null);

		return MTypePower;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}