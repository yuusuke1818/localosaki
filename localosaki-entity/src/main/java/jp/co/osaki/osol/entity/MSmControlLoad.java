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
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_sm_control_load database table.
 * 
 */
@Entity
@Table(name="m_sm_control_load")
@NamedQuery(name="MSmControlLoad.findAll", query="SELECT m FROM MSmControlLoad m")
public class MSmControlLoad implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmControlLoadPK id;

	@Column(name="control_load_memo", length=100)
	private String controlLoadMemo;

	@Column(name="control_load_name", length=60)
	private String controlLoadName;

	@Column(name="control_load_shut_off_capacity", length=4)
	private String controlLoadShutOffCapacity;

	@Column(name="control_load_shut_off_rank", length=3)
	private String controlLoadShutOffRank;

	@Column(name="control_load_shut_off_time", length=2)
	private String controlLoadShutOffTime;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	//bi-directional many-to-one association to MSmControlLoadVerify
	@OneToMany(mappedBy="MSmControlLoad")
	private List<MSmControlLoadVerify> MSmControlLoadVerifies;

	//bi-directional many-to-one association to MSmLineControlLoadVerify
	@OneToMany(mappedBy="MSmControlLoad")
	private List<MSmLineControlLoadVerify> MSmLineControlLoadVerifies;

        public MSmControlLoad() {
	}

	public MSmControlLoadPK getId() {
		return this.id;
	}

	public void setId(MSmControlLoadPK id) {
		this.id = id;
	}

	public String getControlLoadMemo() {
		return this.controlLoadMemo;
	}

	public void setControlLoadMemo(String controlLoadMemo) {
		this.controlLoadMemo = controlLoadMemo;
	}

	public String getControlLoadName() {
		return this.controlLoadName;
	}

	public void setControlLoadName(String controlLoadName) {
		this.controlLoadName = controlLoadName;
	}

	public String getControlLoadShutOffCapacity() {
		return this.controlLoadShutOffCapacity;
	}

	public void setControlLoadShutOffCapacity(String controlLoadShutOffCapacity) {
		this.controlLoadShutOffCapacity = controlLoadShutOffCapacity;
	}

	public String getControlLoadShutOffRank() {
		return this.controlLoadShutOffRank;
	}

	public void setControlLoadShutOffRank(String controlLoadShutOffRank) {
		this.controlLoadShutOffRank = controlLoadShutOffRank;
	}

	public String getControlLoadShutOffTime() {
		return this.controlLoadShutOffTime;
	}

	public void setControlLoadShutOffTime(String controlLoadShutOffTime) {
		this.controlLoadShutOffTime = controlLoadShutOffTime;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

	public List<MSmControlLoadVerify> getMSmControlLoadVerifies() {
		return this.MSmControlLoadVerifies;
	}

	public void setMSmControlLoadVerifies(List<MSmControlLoadVerify> MSmControlLoadVerifies) {
		this.MSmControlLoadVerifies = MSmControlLoadVerifies;
	}

	public MSmControlLoadVerify addMSmControlLoadVerify(MSmControlLoadVerify MSmControlLoadVerify) {
		getMSmControlLoadVerifies().add(MSmControlLoadVerify);
		MSmControlLoadVerify.setMSmControlLoad(this);

		return MSmControlLoadVerify;
	}

	public MSmControlLoadVerify removeMSmControlLoadVerify(MSmControlLoadVerify MSmControlLoadVerify) {
		getMSmControlLoadVerifies().remove(MSmControlLoadVerify);
		MSmControlLoadVerify.setMSmControlLoad(null);

		return MSmControlLoadVerify;
	}

	public List<MSmLineControlLoadVerify> getMSmLineControlLoadVerifies() {
		return this.MSmLineControlLoadVerifies;
	}

	public void setMSmLineControlLoadVerifies(List<MSmLineControlLoadVerify> MSmLineControlLoadVerifies) {
		this.MSmLineControlLoadVerifies = MSmLineControlLoadVerifies;
	}

	public MSmLineControlLoadVerify addMSmLineControlLoadVerify(MSmLineControlLoadVerify MSmLineControlLoadVerify) {
		getMSmLineControlLoadVerifies().add(MSmLineControlLoadVerify);
		MSmLineControlLoadVerify.setMSmControlLoad(this);

		return MSmLineControlLoadVerify;
	}

	public MSmLineControlLoadVerify removeMSmLineControlLoadVerify(MSmLineControlLoadVerify MSmLineControlLoadVerify) {
		getMSmLineControlLoadVerifies().remove(MSmLineControlLoadVerify);
		MSmLineControlLoadVerify.setMSmControlLoad(null);

		return MSmLineControlLoadVerify;
	}

}