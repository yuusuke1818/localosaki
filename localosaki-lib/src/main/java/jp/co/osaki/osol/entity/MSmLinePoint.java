package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the m_sm_line_point database table.
 * 
 */
@Entity
@Table(name="m_sm_line_point")
@NamedQuery(name="MSmLinePoint.findAll", query="SELECT m FROM MSmLinePoint m")
public class MSmLinePoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmLinePointPK id;

	@Column(length=100)
	private String comment;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="point_calc_type", nullable=false, length=6)
	private String pointCalcType;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingSmPoint
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="point_no", referencedColumnName="point_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingSmPoint MBuildingSmPoint;

	//bi-directional many-to-one association to MLine
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false, insertable=false, updatable=false)
		})
	private MLine MLine;

	public MSmLinePoint() {
	}

	public MSmLinePointPK getId() {
		return this.id;
	}

	public void setId(MSmLinePointPK id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getPointCalcType() {
		return this.pointCalcType;
	}

	public void setPointCalcType(String pointCalcType) {
		this.pointCalcType = pointCalcType;
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

	public MBuildingSmPoint getMBuildingSmPoint() {
		return this.MBuildingSmPoint;
	}

	public void setMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
		this.MBuildingSmPoint = MBuildingSmPoint;
	}

	public MLine getMLine() {
		return this.MLine;
	}

	public void setMLine(MLine MLine) {
		this.MLine = MLine;
	}

}