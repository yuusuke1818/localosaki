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
 * The persistent class for the t_dm_day_rep_line database table.
 * 
 */
@Entity
@Table(name="t_dm_day_rep_line")
@NamedQuery(name="TDmDayRepLine.findAll", query="SELECT t FROM TDmDayRepLine t")
public class TDmDayRepLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmDayRepLinePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="line_limit_standard_kw", precision=10, scale=1)
	private BigDecimal lineLimitStandardKw;

	@Column(name="line_lower_standard_kw", precision=10, scale=1)
	private BigDecimal lineLowerStandardKw;

	@Column(name="line_value_kw", nullable=false, precision=10, scale=1)
	private BigDecimal lineValueKw;

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

	//bi-directional many-to-one association to TDmDayRep
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="jigen_no", referencedColumnName="jigen_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="measurement_date", referencedColumnName="measurement_date", nullable=false, insertable=false, updatable=false)
		})
	private TDmDayRep TDmDayRep;

	public TDmDayRepLine() {
	}

	public TDmDayRepLinePK getId() {
		return this.id;
	}

	public void setId(TDmDayRepLinePK id) {
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

	public BigDecimal getLineLimitStandardKw() {
		return this.lineLimitStandardKw;
	}

	public void setLineLimitStandardKw(BigDecimal lineLimitStandardKw) {
		this.lineLimitStandardKw = lineLimitStandardKw;
	}

	public BigDecimal getLineLowerStandardKw() {
		return this.lineLowerStandardKw;
	}

	public void setLineLowerStandardKw(BigDecimal lineLowerStandardKw) {
		this.lineLowerStandardKw = lineLowerStandardKw;
	}

	public BigDecimal getLineValueKw() {
		return this.lineValueKw;
	}

	public void setLineValueKw(BigDecimal lineValueKw) {
		this.lineValueKw = lineValueKw;
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

	public TDmDayRep getTDmDayRep() {
		return this.TDmDayRep;
	}

	public void setTDmDayRep(TDmDayRep TDmDayRep) {
		this.TDmDayRep = TDmDayRep;
	}

}