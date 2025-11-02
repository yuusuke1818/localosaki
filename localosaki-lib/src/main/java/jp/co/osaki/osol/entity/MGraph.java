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
 * The persistent class for the m_graph database table.
 * 
 */
@Entity
@Table(name="m_graph")
@NamedQuery(name="MGraph.findAll", query="SELECT m FROM MGraph m")
public class MGraph implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MGraphPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="graph_name", nullable=false, length=60)
	private String graphName;

	@Column(name="initial_view_flg", nullable=false)
	private Integer initialViewFlg;

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

	//bi-directional many-to-one association to MLineGroup
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MLineGroup MLineGroup;

	//bi-directional many-to-one association to MGraphElement
	@OneToMany(mappedBy="MGraph")
	private List<MGraphElement> MGraphElements;

	public MGraph() {
	}

	public MGraphPK getId() {
		return this.id;
	}

	public void setId(MGraphPK id) {
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

	public String getGraphName() {
		return this.graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public Integer getInitialViewFlg() {
		return this.initialViewFlg;
	}

	public void setInitialViewFlg(Integer initialViewFlg) {
		this.initialViewFlg = initialViewFlg;
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

	public MLineGroup getMLineGroup() {
		return this.MLineGroup;
	}

	public void setMLineGroup(MLineGroup MLineGroup) {
		this.MLineGroup = MLineGroup;
	}

	public List<MGraphElement> getMGraphElements() {
		return this.MGraphElements;
	}

	public void setMGraphElements(List<MGraphElement> MGraphElements) {
		this.MGraphElements = MGraphElements;
	}

	public MGraphElement addMGraphElement(MGraphElement MGraphElement) {
		getMGraphElements().add(MGraphElement);
		MGraphElement.setMGraph(this);

		return MGraphElement;
	}

	public MGraphElement removeMGraphElement(MGraphElement MGraphElement) {
		getMGraphElements().remove(MGraphElement);
		MGraphElement.setMGraph(null);

		return MGraphElement;
	}

}