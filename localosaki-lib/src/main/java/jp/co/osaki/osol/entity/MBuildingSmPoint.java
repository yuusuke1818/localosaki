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
 * The persistent class for the m_building_sm_point database table.
 *
 */
@Entity
@Table(name="m_building_sm_point")
@NamedQuery(name="MBuildingSmPoint.findAll", query="SELECT m FROM MBuildingSmPoint m")
public class MBuildingSmPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MBuildingSmPointPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="point_name", length=60)
	private String pointName;

	@Column(name="point_sum_flg", nullable=false)
	private Integer pointSumFlg;

	@Column(name="point_unit", length=10)
	private String pointUnit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingSm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingSm MBuildingSm;

	//bi-directional many-to-one association to MSmPoint
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="point_no", referencedColumnName="point_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MSmPoint MSmPoint;

	//bi-directional many-to-one association to MSmLinePoint
	@OneToMany(mappedBy="MBuildingSmPoint")
	private List<MSmLinePoint> MSmLinePoints;

	//bi-directional many-to-one association to TDmDayRepPoint
	@OneToMany(mappedBy="MBuildingSmPoint")
	private List<TDmDayRepPoint> TDmDayRepPoints;

	//bi-directional many-to-one association to TDmMonthRepPoint
	@OneToMany(mappedBy="MBuildingSmPoint")
	private List<TDmMonthRepPoint> TDmMonthRepPoints;

	//bi-directional many-to-one association to TDmWeekRepPoint
	@OneToMany(mappedBy="MBuildingSmPoint")
	private List<TDmWeekRepPoint> TDmWeekRepPoints;

	//bi-directional many-to-one association to TDmYearRepPoint
	@OneToMany(mappedBy="MBuildingSmPoint")
	private List<TDmYearRepPoint> TDmYearRepPoints;

    //bi-directional many-to-one association to TDmDayRepPointInput
    @OneToMany(mappedBy="MBuildingSmPoint")
    private List<TDmDayRepPointInput> TDmDayRepPointInputs;

    public MBuildingSmPoint() {
	}

	public MBuildingSmPointPK getId() {
		return this.id;
	}

	public void setId(MBuildingSmPointPK id) {
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

	public String getPointName() {
		return this.pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public Integer getPointSumFlg() {
		return this.pointSumFlg;
	}

	public void setPointSumFlg(Integer pointSumFlg) {
		this.pointSumFlg = pointSumFlg;
	}

	public String getPointUnit() {
		return this.pointUnit;
	}

	public void setPointUnit(String pointUnit) {
		this.pointUnit = pointUnit;
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

	public MBuildingSm getMBuildingSm() {
		return this.MBuildingSm;
	}

	public void setMBuildingSm(MBuildingSm MBuildingSm) {
		this.MBuildingSm = MBuildingSm;
	}

	public MSmPoint getMSmPoint() {
		return this.MSmPoint;
	}

	public void setMSmPoint(MSmPoint MSmPoint) {
		this.MSmPoint = MSmPoint;
	}

	public List<MSmLinePoint> getMSmLinePoints() {
		return this.MSmLinePoints;
	}

	public void setMSmLinePoints(List<MSmLinePoint> MSmLinePoints) {
		this.MSmLinePoints = MSmLinePoints;
	}

	public MSmLinePoint addMSmLinePoint(MSmLinePoint MSmLinePoint) {
		getMSmLinePoints().add(MSmLinePoint);
		MSmLinePoint.setMBuildingSmPoint(this);

		return MSmLinePoint;
	}

	public MSmLinePoint removeMSmLinePoint(MSmLinePoint MSmLinePoint) {
		getMSmLinePoints().remove(MSmLinePoint);
		MSmLinePoint.setMBuildingSmPoint(null);

		return MSmLinePoint;
	}

	public List<TDmDayRepPoint> getTDmDayRepPoints() {
		return this.TDmDayRepPoints;
	}

	public void setTDmDayRepPoints(List<TDmDayRepPoint> TDmDayRepPoints) {
		this.TDmDayRepPoints = TDmDayRepPoints;
	}

	public TDmDayRepPoint addTDmDayRepPoint(TDmDayRepPoint TDmDayRepPoint) {
		getTDmDayRepPoints().add(TDmDayRepPoint);
		TDmDayRepPoint.setMBuildingSmPoint(this);

		return TDmDayRepPoint;
	}

	public TDmDayRepPoint removeTDmDayRepPoint(TDmDayRepPoint TDmDayRepPoint) {
		getTDmDayRepPoints().remove(TDmDayRepPoint);
		TDmDayRepPoint.setMBuildingSmPoint(null);

		return TDmDayRepPoint;
	}

	public List<TDmMonthRepPoint> getTDmMonthRepPoints() {
		return this.TDmMonthRepPoints;
	}

	public void setTDmMonthRepPoints(List<TDmMonthRepPoint> TDmMonthRepPoints) {
		this.TDmMonthRepPoints = TDmMonthRepPoints;
	}

	public TDmMonthRepPoint addTDmMonthRepPoint(TDmMonthRepPoint TDmMonthRepPoint) {
		getTDmMonthRepPoints().add(TDmMonthRepPoint);
		TDmMonthRepPoint.setMBuildingSmPoint(this);

		return TDmMonthRepPoint;
	}

	public TDmMonthRepPoint removeTDmMonthRepPoint(TDmMonthRepPoint TDmMonthRepPoint) {
		getTDmMonthRepPoints().remove(TDmMonthRepPoint);
		TDmMonthRepPoint.setMBuildingSmPoint(null);

		return TDmMonthRepPoint;
	}

	public List<TDmWeekRepPoint> getTDmWeekRepPoints() {
		return this.TDmWeekRepPoints;
	}

	public void setTDmWeekRepPoints(List<TDmWeekRepPoint> TDmWeekRepPoints) {
		this.TDmWeekRepPoints = TDmWeekRepPoints;
	}

	public TDmWeekRepPoint addTDmWeekRepPoint(TDmWeekRepPoint TDmWeekRepPoint) {
		getTDmWeekRepPoints().add(TDmWeekRepPoint);
		TDmWeekRepPoint.setMBuildingSmPoint(this);

		return TDmWeekRepPoint;
	}

	public TDmWeekRepPoint removeTDmWeekRepPoint(TDmWeekRepPoint TDmWeekRepPoint) {
		getTDmWeekRepPoints().remove(TDmWeekRepPoint);
		TDmWeekRepPoint.setMBuildingSmPoint(null);

		return TDmWeekRepPoint;
	}

	public List<TDmYearRepPoint> getTDmYearRepPoints() {
		return this.TDmYearRepPoints;
	}

	public void setTDmYearRepPoints(List<TDmYearRepPoint> TDmYearRepPoints) {
		this.TDmYearRepPoints = TDmYearRepPoints;
	}

	public TDmYearRepPoint addTDmYearRepPoint(TDmYearRepPoint TDmYearRepPoint) {
		getTDmYearRepPoints().add(TDmYearRepPoint);
		TDmYearRepPoint.setMBuildingSmPoint(this);

		return TDmYearRepPoint;
	}

	public TDmYearRepPoint removeTDmYearRepPoint(TDmYearRepPoint TDmYearRepPoint) {
		getTDmYearRepPoints().remove(TDmYearRepPoint);
		TDmYearRepPoint.setMBuildingSmPoint(null);

		return TDmYearRepPoint;
	}

    public List<TDmDayRepPointInput> getTDmDayRepPointInputs() {
        return this.TDmDayRepPointInputs;
    }

    public void setTDmDayRepPointInputs(List<TDmDayRepPointInput> TDmDayRepPointInputs) {
        this.TDmDayRepPointInputs = TDmDayRepPointInputs;
    }

    public TDmDayRepPointInput addTDmDayRepPointInput(TDmDayRepPointInput TDmDayRepPointInput) {
        getTDmDayRepPointInputs().add(TDmDayRepPointInput);
        TDmDayRepPointInput.setMBuildingSmPoint(this);

        return TDmDayRepPointInput;
    }

    public TDmDayRepPointInput removeTDmDayRepPointInput(TDmDayRepPointInput TDmDayRepPointInput) {
        getTDmDayRepPointInputs().remove(TDmDayRepPointInput);
        TDmDayRepPointInput.setMBuildingSmPoint(null);

        return TDmDayRepPointInput;
    }

}