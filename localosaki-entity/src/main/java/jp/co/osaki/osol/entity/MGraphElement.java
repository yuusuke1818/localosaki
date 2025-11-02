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
 * The persistent class for the m_graph_element database table.
 * 
 */
@Entity
@Table(name="m_graph_element")
@NamedQuery(name="MGraphElement.findAll", query="SELECT m FROM MGraphElement m")
public class MGraphElement implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MGraphElementPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="graph_color_code", nullable=false, length=7)
	private String graphColorCode;

	@Column(name="graph_element_type", nullable=false, length=6)
	private String graphElementType;

	@Column(name="graph_line_group_id")
	private Long graphLineGroupId;

	@Column(name="graph_line_no", length=4)
	private String graphLineNo;

	@Column(name="graph_point_no", length=4)
	private String graphPointNo;

	@Column(name="graph_sm_id")
	private Long graphSmId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MGraph
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="graph_id", referencedColumnName="graph_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MGraph MGraph;

	public MGraphElement() {
	}

	public MGraphElementPK getId() {
		return this.id;
	}

	public void setId(MGraphElementPK id) {
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

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getGraphColorCode() {
		return this.graphColorCode;
	}

	public void setGraphColorCode(String graphColorCode) {
		this.graphColorCode = graphColorCode;
	}

	public String getGraphElementType() {
		return this.graphElementType;
	}

	public void setGraphElementType(String graphElementType) {
		this.graphElementType = graphElementType;
	}

	public Long getGraphLineGroupId() {
		return this.graphLineGroupId;
	}

	public void setGraphLineGroupId(Long graphLineGroupId) {
		this.graphLineGroupId = graphLineGroupId;
	}

	public String getGraphLineNo() {
		return this.graphLineNo;
	}

	public void setGraphLineNo(String graphLineNo) {
		this.graphLineNo = graphLineNo;
	}

	public String getGraphPointNo() {
		return this.graphPointNo;
	}

	public void setGraphPointNo(String graphPointNo) {
		this.graphPointNo = graphPointNo;
	}

	public Long getGraphSmId() {
		return this.graphSmId;
	}

	public void setGraphSmId(Long graphSmId) {
		this.graphSmId = graphSmId;
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

	public MGraph getMGraph() {
		return this.MGraph;
	}

	public void setMGraph(MGraph MGraph) {
		this.MGraph = MGraph;
	}

}