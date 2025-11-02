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
 * The persistent class for the m_aggregate_dm database table.
 * 
 */
@Entity
@Table(name="m_aggregate_dm")
@NamedQuery(name="MAggregateDm.findAll", query="SELECT m FROM MAggregateDm m")
public class MAggregateDm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MAggregateDmPK id;

	@Column(name="aggregate_dm_name", nullable=false, length=40)
	private String aggregateDmName;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="sum_date", nullable=false, length=6)
	private String sumDate;

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

	//bi-directional many-to-one association to MAggregateDmLine
	@OneToMany(mappedBy="MAggregateDm")
	private List<MAggregateDmLine> MAggregateDmLines;

	public MAggregateDm() {
	}

	public MAggregateDmPK getId() {
		return this.id;
	}

	public void setId(MAggregateDmPK id) {
		this.id = id;
	}

	public String getAggregateDmName() {
		return this.aggregateDmName;
	}

	public void setAggregateDmName(String aggregateDmName) {
		this.aggregateDmName = aggregateDmName;
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

	public String getSumDate() {
		return this.sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
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

	public List<MAggregateDmLine> getMAggregateDmLines() {
		return this.MAggregateDmLines;
	}

	public void setMAggregateDmLines(List<MAggregateDmLine> MAggregateDmLines) {
		this.MAggregateDmLines = MAggregateDmLines;
	}

	public MAggregateDmLine addMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
		getMAggregateDmLines().add(MAggregateDmLine);
		MAggregateDmLine.setMAggregateDm(this);

		return MAggregateDmLine;
	}

	public MAggregateDmLine removeMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
		getMAggregateDmLines().remove(MAggregateDmLine);
		MAggregateDmLine.setMAggregateDm(null);

		return MAggregateDmLine;
	}

}