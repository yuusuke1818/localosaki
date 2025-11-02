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
 * The persistent class for the t_dm_week_rep_line database table.
 * 
 */
@Entity
@Table(name="t_dm_week_rep_line")
@NamedQuery(name="TDmWeekRepLine.findAll", query="SELECT t FROM TDmWeekRepLine t")
public class TDmWeekRepLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmWeekRepLinePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="line_max_kw", nullable=false, precision=10, scale=1)
	private BigDecimal lineMaxKw;

	@Column(name="line_value_kwh", nullable=false, precision=10, scale=1)
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

	//bi-directional many-to-one association to TDmWeekRep
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="fiscal_year", referencedColumnName="fiscal_year", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="summary_unit", referencedColumnName="summary_unit", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="week_no", referencedColumnName="week_no", nullable=false, insertable=false, updatable=false)
		})
	private TDmWeekRep TDmWeekRep;

	public TDmWeekRepLine() {
	}

	public TDmWeekRepLinePK getId() {
		return this.id;
	}

	public void setId(TDmWeekRepLinePK id) {
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

	public TDmWeekRep getTDmWeekRep() {
		return this.TDmWeekRep;
	}

	public void setTDmWeekRep(TDmWeekRep TDmWeekRep) {
		this.TDmWeekRep = TDmWeekRep;
	}

}