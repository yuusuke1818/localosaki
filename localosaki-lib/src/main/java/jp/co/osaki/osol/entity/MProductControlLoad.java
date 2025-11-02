package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_product_control_load database table.
 * 
 */
@Entity
@Table(name="m_product_control_load")
@NamedQuery(name="MProductControlLoad.findAll", query="SELECT m FROM MProductControlLoad m")
public class MProductControlLoad implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MProductControlLoadPK id;

	@Column(name="control_load_circuit", length=10)
	private String controlLoadCircuit;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="demand_control_flg", nullable=false)
	private Integer demandControlFlg;

	@Column(name="event_control_flg", nullable=false)
	private Integer eventControlFlg;

	@Column(name="manual_load_control_flg", nullable=false)
	private Integer manualLoadControlFlg;

	@Column(name="schedule_control_flg", nullable=false)
	private Integer scheduleControlFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MProductSpec
	@ManyToOne
	@JoinColumn(name="product_cd", nullable=false, insertable=false, updatable=false)
	private MProductSpec MProductSpec;

	public MProductControlLoad() {
	}

	public MProductControlLoadPK getId() {
		return this.id;
	}

	public void setId(MProductControlLoadPK id) {
		this.id = id;
	}

	public String getControlLoadCircuit() {
		return this.controlLoadCircuit;
	}

	public void setControlLoadCircuit(String controlLoadCircuit) {
		this.controlLoadCircuit = controlLoadCircuit;
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

	public Integer getDemandControlFlg() {
		return this.demandControlFlg;
	}

	public void setDemandControlFlg(Integer demandControlFlg) {
		this.demandControlFlg = demandControlFlg;
	}

	public Integer getEventControlFlg() {
		return this.eventControlFlg;
	}

	public void setEventControlFlg(Integer eventControlFlg) {
		this.eventControlFlg = eventControlFlg;
	}

	public Integer getManualLoadControlFlg() {
		return this.manualLoadControlFlg;
	}

	public void setManualLoadControlFlg(Integer manualLoadControlFlg) {
		this.manualLoadControlFlg = manualLoadControlFlg;
	}

	public Integer getScheduleControlFlg() {
		return this.scheduleControlFlg;
	}

	public void setScheduleControlFlg(Integer scheduleControlFlg) {
		this.scheduleControlFlg = scheduleControlFlg;
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

	public MProductSpec getMProductSpec() {
		return this.MProductSpec;
	}

	public void setMProductSpec(MProductSpec MProductSpec) {
		this.MProductSpec = MProductSpec;
	}

}