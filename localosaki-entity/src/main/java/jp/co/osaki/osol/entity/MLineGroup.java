package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_line_group database table.
 * 
 */
@Entity
@Table(name="m_line_group")
@NamedQuery(name="MLineGroup.findAll", query="SELECT m FROM MLineGroup m")
public class MLineGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MLineGroupPK id;

	@Column(name="building_id")
	private Long buildingId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="initial_view_flg")
	private Integer initialViewFlg;

	@Column(name="line_group_name", length=40)
	private String lineGroupName;

	@Column(name="line_group_type", nullable=false, length=6)
	private String lineGroupType;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MGraph
	@OneToMany(mappedBy="MLineGroup")
	private List<MGraph> MGraphs;

	//bi-directional many-to-one association to MLine
	@OneToMany(mappedBy="MLineGroup")
	private List<MLine> MLines;

	//bi-directional many-to-one association to MCorpDm
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorpDm MCorpDm;

	//bi-directional one-to-one association to MLineGroupEx
	@OneToOne(mappedBy="MLineGroup")
	private MLineGroupEx MLineGroupEx;

	public MLineGroup() {
	}

	public MLineGroupPK getId() {
		return this.id;
	}

	public void setId(MLineGroupPK id) {
		this.id = id;
	}

	public Long getBuildingId() {
		return this.buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
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

	public Integer getInitialViewFlg() {
		return this.initialViewFlg;
	}

	public void setInitialViewFlg(Integer initialViewFlg) {
		this.initialViewFlg = initialViewFlg;
	}

	public String getLineGroupName() {
		return this.lineGroupName;
	}

	public void setLineGroupName(String lineGroupName) {
		this.lineGroupName = lineGroupName;
	}

	public String getLineGroupType() {
		return this.lineGroupType;
	}

	public void setLineGroupType(String lineGroupType) {
		this.lineGroupType = lineGroupType;
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

	public List<MGraph> getMGraphs() {
		return this.MGraphs;
	}

	public void setMGraphs(List<MGraph> MGraphs) {
		this.MGraphs = MGraphs;
	}

	public MGraph addMGraph(MGraph MGraph) {
		getMGraphs().add(MGraph);
		MGraph.setMLineGroup(this);

		return MGraph;
	}

	public MGraph removeMGraph(MGraph MGraph) {
		getMGraphs().remove(MGraph);
		MGraph.setMLineGroup(null);

		return MGraph;
	}

	public List<MLine> getMLines() {
		return this.MLines;
	}

	public void setMLines(List<MLine> MLines) {
		this.MLines = MLines;
	}

	public MLine addMLine(MLine MLine) {
		getMLines().add(MLine);
		MLine.setMLineGroup(this);

		return MLine;
	}

	public MLine removeMLine(MLine MLine) {
		getMLines().remove(MLine);
		MLine.setMLineGroup(null);

		return MLine;
	}

	public MCorpDm getMCorpDm() {
		return this.MCorpDm;
	}

	public void setMCorpDm(MCorpDm MCorpDm) {
		this.MCorpDm = MCorpDm;
	}

	public MLineGroupEx getMLineGroupEx() {
		return this.MLineGroupEx;
	}

	public void setMLineGroupEx(MLineGroupEx MLineGroupEx) {
		this.MLineGroupEx = MLineGroupEx;
	}

}