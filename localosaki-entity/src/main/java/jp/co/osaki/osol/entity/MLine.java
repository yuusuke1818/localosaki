package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the m_line database table.
 *
 */
@Entity
@Table(name="m_line")
@NamedQuery(name="MLine.findAll", query="SELECT m FROM MLine m")
public class MLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MLinePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

    @Column(name="input_enable_flg", nullable=false)
    private Integer inputEnableFlg;

    @Column(name="line_enable_flg", nullable=false)
	private Integer lineEnableFlg;

	@Column(name="line_name", length=40)
	private String lineName;

	@Column(name="line_target", nullable=false, length=6)
	private String lineTarget;

	@Column(name="line_unit", length=10)
	private String lineUnit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MLineGroup
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MLineGroup MLineGroup;

	//bi-directional many-to-one association to MLineType
	@ManyToOne
	@JoinColumn(name="line_type")
	private MLineType MLineType;

	//bi-directional many-to-one association to MLineTargetAlarm
	@OneToMany(mappedBy="MLine")
	private List<MLineTargetAlarm> MLineTargetAlarms;

	//bi-directional many-to-one association to MLineTimeStandard
	@OneToMany(mappedBy="MLine")
	private List<MLineTimeStandard> MLineTimeStandards;

	//bi-directional many-to-one association to MSmLinePoint
	@OneToMany(mappedBy="MLine")
	private List<MSmLinePoint> MSmLinePoints;

	//bi-directional many-to-one association to MSmLineVerify
	@OneToMany(mappedBy="MLine")
	private List<MSmLineVerify> MSmLineVerifies;

        //bi-directional many-to-one association to TDmDayRepLine
	@OneToMany(mappedBy="MLine")
	private List<TDmDayRepLine> TDmDayRepLines;

	//bi-directional many-to-one association to TDmMonthRepLine
	@OneToMany(mappedBy="MLine")
	private List<TDmMonthRepLine> TDmMonthRepLines;

	//bi-directional many-to-one association to TDmWeekRepLine
	@OneToMany(mappedBy="MLine")
	private List<TDmWeekRepLine> TDmWeekRepLines;

	//bi-directional many-to-one association to TDmYearRepLine
	@OneToMany(mappedBy="MLine")
	private List<TDmYearRepLine> TDmYearRepLines;

    //bi-directional many-to-one association to TAvailableEnergyLine
    @OneToMany(mappedBy="MLine")
    private List<TAvailableEnergyLine> TAvailableEnergyLines;

    //bi-directional many-to-one association to MAggregateDmLine
    @OneToMany(mappedBy="MLine")
    private List<MAggregateDmLine> MAggregateDmLines;

    public MLine() {
	}

	public MLinePK getId() {
		return this.id;
	}

	public void setId(MLinePK id) {
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

    public Integer getInputEnableFlg() {
        return this.inputEnableFlg;
    }

    public void setInputEnableFlg(Integer inputEnableFlg) {
        this.inputEnableFlg = inputEnableFlg;
    }

    public Integer getLineEnableFlg() {
		return this.lineEnableFlg;
	}

	public void setLineEnableFlg(Integer lineEnableFlg) {
		this.lineEnableFlg = lineEnableFlg;
	}

	public String getLineName() {
		return this.lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineTarget() {
		return this.lineTarget;
	}

	public void setLineTarget(String lineTarget) {
		this.lineTarget = lineTarget;
	}

	public String getLineUnit() {
		return this.lineUnit;
	}

	public void setLineUnit(String lineUnit) {
		this.lineUnit = lineUnit;
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

	public MLineGroup getMLineGroup() {
		return this.MLineGroup;
	}

	public void setMLineGroup(MLineGroup MLineGroup) {
		this.MLineGroup = MLineGroup;
	}

	public MLineType getMLineType() {
		return this.MLineType;
	}

	public void setMLineType(MLineType MLineType) {
		this.MLineType = MLineType;
	}

	public List<MLineTargetAlarm> getMLineTargetAlarms() {
		return this.MLineTargetAlarms;
	}

	public void setMLineTargetAlarms(List<MLineTargetAlarm> MLineTargetAlarms) {
		this.MLineTargetAlarms = MLineTargetAlarms;
	}

	public MLineTargetAlarm addMLineTargetAlarm(MLineTargetAlarm MLineTargetAlarm) {
		getMLineTargetAlarms().add(MLineTargetAlarm);
		MLineTargetAlarm.setMLine(this);

		return MLineTargetAlarm;
	}

	public MLineTargetAlarm removeMLineTargetAlarm(MLineTargetAlarm MLineTargetAlarm) {
		getMLineTargetAlarms().remove(MLineTargetAlarm);
		MLineTargetAlarm.setMLine(null);

		return MLineTargetAlarm;
	}

	public List<MLineTimeStandard> getMLineTimeStandards() {
		return this.MLineTimeStandards;
	}

	public void setMLineTimeStandards(List<MLineTimeStandard> MLineTimeStandards) {
		this.MLineTimeStandards = MLineTimeStandards;
	}

	public MLineTimeStandard addMLineTimeStandard(MLineTimeStandard MLineTimeStandard) {
		getMLineTimeStandards().add(MLineTimeStandard);
		MLineTimeStandard.setMLine(this);

		return MLineTimeStandard;
	}

	public MLineTimeStandard removeMLineTimeStandard(MLineTimeStandard MLineTimeStandard) {
		getMLineTimeStandards().remove(MLineTimeStandard);
		MLineTimeStandard.setMLine(null);

		return MLineTimeStandard;
	}

	public List<MSmLinePoint> getMSmLinePoints() {
		return this.MSmLinePoints;
	}

	public void setMSmLinePoints(List<MSmLinePoint> MSmLinePoints) {
		this.MSmLinePoints = MSmLinePoints;
	}

	public MSmLinePoint addMSmLinePoint(MSmLinePoint MSmLinePoint) {
		getMSmLinePoints().add(MSmLinePoint);
		MSmLinePoint.setMLine(this);

		return MSmLinePoint;
	}

	public MSmLinePoint removeMSmLinePoint(MSmLinePoint MSmLinePoint) {
		getMSmLinePoints().remove(MSmLinePoint);
		MSmLinePoint.setMLine(null);

		return MSmLinePoint;
	}

	public List<MSmLineVerify> getMSmLineVerifies() {
		return this.MSmLineVerifies;
	}

	public void setMSmLineVerifies(List<MSmLineVerify> MSmLineVerifies) {
		this.MSmLineVerifies = MSmLineVerifies;
	}

	public MSmLineVerify addMSmLineVerify(MSmLineVerify MSmLineVerify) {
		getMSmLineVerifies().add(MSmLineVerify);
		MSmLineVerify.setMLine(this);

		return MSmLineVerify;
	}

	public MSmLineVerify removeMSmLineVerify(MSmLineVerify MSmLineVerify) {
		getMSmLineVerifies().remove(MSmLineVerify);
		MSmLineVerify.setMLine(null);

		return MSmLineVerify;
	}

        public List<TDmDayRepLine> getTDmDayRepLines() {
		return this.TDmDayRepLines;
	}

	public void setTDmDayRepLines(List<TDmDayRepLine> TDmDayRepLines) {
		this.TDmDayRepLines = TDmDayRepLines;
	}

	public TDmDayRepLine addTDmDayRepLine(TDmDayRepLine TDmDayRepLine) {
		getTDmDayRepLines().add(TDmDayRepLine);
		TDmDayRepLine.setMLine(this);

		return TDmDayRepLine;
	}

	public TDmDayRepLine removeTDmDayRepLine(TDmDayRepLine TDmDayRepLine) {
		getTDmDayRepLines().remove(TDmDayRepLine);
		TDmDayRepLine.setMLine(null);

		return TDmDayRepLine;
	}

	public List<TDmMonthRepLine> getTDmMonthRepLines() {
		return this.TDmMonthRepLines;
	}

	public void setTDmMonthRepLines(List<TDmMonthRepLine> TDmMonthRepLines) {
		this.TDmMonthRepLines = TDmMonthRepLines;
	}

	public TDmMonthRepLine addTDmMonthRepLine(TDmMonthRepLine TDmMonthRepLine) {
		getTDmMonthRepLines().add(TDmMonthRepLine);
		TDmMonthRepLine.setMLine(this);

		return TDmMonthRepLine;
	}

	public TDmMonthRepLine removeTDmMonthRepLine(TDmMonthRepLine TDmMonthRepLine) {
		getTDmMonthRepLines().remove(TDmMonthRepLine);
		TDmMonthRepLine.setMLine(null);

		return TDmMonthRepLine;
	}

	public List<TDmWeekRepLine> getTDmWeekRepLines() {
		return this.TDmWeekRepLines;
	}

	public void setTDmWeekRepLines(List<TDmWeekRepLine> TDmWeekRepLines) {
		this.TDmWeekRepLines = TDmWeekRepLines;
	}

	public TDmWeekRepLine addTDmWeekRepLine(TDmWeekRepLine TDmWeekRepLine) {
		getTDmWeekRepLines().add(TDmWeekRepLine);
		TDmWeekRepLine.setMLine(this);

		return TDmWeekRepLine;
	}

	public TDmWeekRepLine removeTDmWeekRepLine(TDmWeekRepLine TDmWeekRepLine) {
		getTDmWeekRepLines().remove(TDmWeekRepLine);
		TDmWeekRepLine.setMLine(null);

		return TDmWeekRepLine;
	}

	public List<TDmYearRepLine> getTDmYearRepLines() {
		return this.TDmYearRepLines;
	}

	public void setTDmYearRepLines(List<TDmYearRepLine> TDmYearRepLines) {
		this.TDmYearRepLines = TDmYearRepLines;
	}

	public TDmYearRepLine addTDmYearRepLine(TDmYearRepLine TDmYearRepLine) {
		getTDmYearRepLines().add(TDmYearRepLine);
		TDmYearRepLine.setMLine(this);

		return TDmYearRepLine;
	}

	public TDmYearRepLine removeTDmYearRepLine(TDmYearRepLine TDmYearRepLine) {
		getTDmYearRepLines().remove(TDmYearRepLine);
		TDmYearRepLine.setMLine(null);

		return TDmYearRepLine;
	}

    public List<TAvailableEnergyLine> getTAvailableEnergyLines() {
        return this.TAvailableEnergyLines;
    }

    public void setTAvailableEnergyLines(List<TAvailableEnergyLine> TAvailableEnergyLines) {
        this.TAvailableEnergyLines = TAvailableEnergyLines;
    }

    public TAvailableEnergyLine addTAvailableEnergyLine(TAvailableEnergyLine TAvailableEnergyLine) {
        getTAvailableEnergyLines().add(TAvailableEnergyLine);
        TAvailableEnergyLine.setMLine(this);

        return TAvailableEnergyLine;
    }

    public TAvailableEnergyLine removeTAvailableEnergyLine(TAvailableEnergyLine TAvailableEnergyLine) {
        getTAvailableEnergyLines().remove(TAvailableEnergyLine);
        TAvailableEnergyLine.setMLine(null);

        return TAvailableEnergyLine;
    }

    public List<MAggregateDmLine> getMAggregateDmLines() {
        return this.MAggregateDmLines;
    }

    public void setMAggregateDmLines(List<MAggregateDmLine> MAggregateDmLines) {
        this.MAggregateDmLines = MAggregateDmLines;
    }

    public MAggregateDmLine addMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
        getMAggregateDmLines().add(MAggregateDmLine);
        MAggregateDmLine.setMLine(this);

        return MAggregateDmLine;
    }

    public MAggregateDmLine removeMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
        getMAggregateDmLines().remove(MAggregateDmLine);
        MAggregateDmLine.setMLine(null);

        return MAggregateDmLine;
    }
}