package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the t_dm_day_rep database table.
 *
 */
@Entity
@Table(name="t_dm_day_rep")
@NamedQuery(name="TDmDayRep.findAll", query="SELECT t FROM TDmDayRep t")
public class TDmDayRep implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmDayRepPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="out_air_temp", precision=4, scale=1)
	private BigDecimal outAirTemp;

	@Temporal(TemporalType.DATE)
	@Column(name="record_date", nullable=false)
	private Date recordDate;

	@Column(name="sum_date", nullable=false, length=6)
	private String sumDate;

	@Column(name="target_kw", nullable=false, precision=10, scale=1)
	private BigDecimal targetKw;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingDm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingDm MBuildingDm;

	//bi-directional many-to-one association to TDmDayRepLine
	@OneToMany(mappedBy="TDmDayRep")
	private List<TDmDayRepLine> TDmDayRepLines;

	//bi-directional many-to-one association to TDmDayRepPoint
	@OneToMany(mappedBy="TDmDayRep")
	private List<TDmDayRepPoint> TDmDayRepPoints;

    //bi-directional many-to-one association to TDmDayRepPointInput
    @OneToMany(mappedBy="TDmDayRep")
    private List<TDmDayRepPointInput> TDmDayRepPointInputs;

    public TDmDayRep() {
	}

	public TDmDayRepPK getId() {
		return this.id;
	}

	public void setId(TDmDayRepPK id) {
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

	public BigDecimal getOutAirTemp() {
		return this.outAirTemp;
	}

	public void setOutAirTemp(BigDecimal outAirTemp) {
		this.outAirTemp = outAirTemp;
	}

	public Date getRecordDate() {
		return this.recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getSumDate() {
		return this.sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
	}

	public BigDecimal getTargetKw() {
		return this.targetKw;
	}

	public void setTargetKw(BigDecimal targetKw) {
		this.targetKw = targetKw;
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

	public MBuildingDm getMBuildingDm() {
		return this.MBuildingDm;
	}

	public void setMBuildingDm(MBuildingDm MBuildingDm) {
		this.MBuildingDm = MBuildingDm;
	}

	public List<TDmDayRepLine> getTDmDayRepLines() {
		return this.TDmDayRepLines;
	}

	public void setTDmDayRepLines(List<TDmDayRepLine> TDmDayRepLines) {
		this.TDmDayRepLines = TDmDayRepLines;
	}

	public TDmDayRepLine addTDmDayRepLine(TDmDayRepLine TDmDayRepLine) {
		getTDmDayRepLines().add(TDmDayRepLine);
		TDmDayRepLine.setTDmDayRep(this);

		return TDmDayRepLine;
	}

	public TDmDayRepLine removeTDmDayRepLine(TDmDayRepLine TDmDayRepLine) {
		getTDmDayRepLines().remove(TDmDayRepLine);
		TDmDayRepLine.setTDmDayRep(null);

		return TDmDayRepLine;
	}

	public List<TDmDayRepPoint> getTDmDayRepPoints() {
		return this.TDmDayRepPoints;
	}

	public void setTDmDayRepPoints(List<TDmDayRepPoint> TDmDayRepPoints) {
		this.TDmDayRepPoints = TDmDayRepPoints;
	}

	public TDmDayRepPoint addTDmDayRepPoint(TDmDayRepPoint TDmDayRepPoint) {
		getTDmDayRepPoints().add(TDmDayRepPoint);
		TDmDayRepPoint.setTDmDayRep(this);

		return TDmDayRepPoint;
	}

	public TDmDayRepPoint removeTDmDayRepPoint(TDmDayRepPoint TDmDayRepPoint) {
		getTDmDayRepPoints().remove(TDmDayRepPoint);
		TDmDayRepPoint.setTDmDayRep(null);

		return TDmDayRepPoint;
	}

    public List<TDmDayRepPointInput> getTDmDayRepPointInputs() {
        return this.TDmDayRepPointInputs;
    }

    public void setTDmDayRepPointInputs(List<TDmDayRepPointInput> TDmDayRepPointInputs) {
        this.TDmDayRepPointInputs = TDmDayRepPointInputs;
    }

    public TDmDayRepPointInput addTDmDayRepPointInput(TDmDayRepPointInput TDmDayRepPointInput) {
        getTDmDayRepPointInputs().add(TDmDayRepPointInput);
        TDmDayRepPointInput.setTDmDayRep(this);

        return TDmDayRepPointInput;
    }

    public TDmDayRepPointInput removeTDmDayRepPointInput(TDmDayRepPointInput TDmDayRepPointInput) {
        getTDmDayRepPointInputs().remove(TDmDayRepPointInput);
        TDmDayRepPointInput.setTDmDayRep(null);

        return TDmDayRepPointInput;
    }

}