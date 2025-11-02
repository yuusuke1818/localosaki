package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_dm_year_rep_line database table.
 * 
 */
@Entity
@Table(name="t_dm_year_rep_line")
@NamedQuery(name="TDmYearRepLine.findAll", query="SELECT t FROM TDmYearRepLine t")
public class TDmYearRepLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmYearRepLinePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="line_max_kw", precision=10, scale=1)
	private BigDecimal lineMaxKw;

	@Column(name="line_value_kwh", precision=10, scale=1)
	private BigDecimal lineValueKwh;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MLine
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false, insertable=false, updatable=false)
		})
	private MLine MLine;

	//bi-directional many-to-one association to TDmYearRep
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="month_no", referencedColumnName="month_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="summary_unit", referencedColumnName="summary_unit", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="year_no", referencedColumnName="year_no", nullable=false, insertable=false, updatable=false)
		})
	private TDmYearRep TDmYearRep;

	public TDmYearRepLine() {
	}

	public TDmYearRepLinePK getId() {
		return this.id;
	}

	public void setId(TDmYearRepLinePK id) {
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

	public BigDecimal getLineMaxKw() {
		return this.lineMaxKw;
	}

	public void setLineMaxKw(BigDecimal lineMaxKw) {
		this.lineMaxKw = lineMaxKw;
	}

	public BigDecimal getLineValueKwh() {
		return this.lineValueKwh;
	}

	public void setLineValueKwh(BigDecimal lineValueKwh) {
		this.lineValueKwh = lineValueKwh;
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

	public MLine getMLine() {
		return this.MLine;
	}

	public void setMLine(MLine MLine) {
		this.MLine = MLine;
	}

	public TDmYearRep getTDmYearRep() {
		return this.TDmYearRep;
	}

	public void setTDmYearRep(TDmYearRep TDmYearRep) {
		this.TDmYearRep = TDmYearRep;
	}

}