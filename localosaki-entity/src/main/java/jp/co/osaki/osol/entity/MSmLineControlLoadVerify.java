package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_sm_line_control_load_verify database table.
 * 
 */
@Entity
@Table(name="m_sm_line_control_load_verify")
@NamedQuery(name="MSmLineControlLoadVerify.findAll", query="SELECT m FROM MSmLineControlLoadVerify m")
public class MSmLineControlLoadVerify implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmLineControlLoadVerifyPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="dm_load_shut_off_capacity", precision=5, scale=1)
	private BigDecimal dmLoadShutOffCapacity;

	@Column(name="event1_load_shut_off_capacity", precision=5, scale=1)
	private BigDecimal event1LoadShutOffCapacity;

	@Column(name="event2_load_shut_off_capacity", precision=5, scale=1)
	private BigDecimal event2LoadShutOffCapacity;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmControlLoad
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="control_load", referencedColumnName="control_load", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MSmControlLoad MSmControlLoad;

	//bi-directional many-to-one association to MSmLineVerify
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MSmLineVerify MSmLineVerify;

	public MSmLineControlLoadVerify() {
	}

	public MSmLineControlLoadVerifyPK getId() {
		return this.id;
	}

	public void setId(MSmLineControlLoadVerifyPK id) {
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

	public BigDecimal getDmLoadShutOffCapacity() {
		return this.dmLoadShutOffCapacity;
	}

	public void setDmLoadShutOffCapacity(BigDecimal dmLoadShutOffCapacity) {
		this.dmLoadShutOffCapacity = dmLoadShutOffCapacity;
	}

	public BigDecimal getEvent1LoadShutOffCapacity() {
		return this.event1LoadShutOffCapacity;
	}

	public void setEvent1LoadShutOffCapacity(BigDecimal event1LoadShutOffCapacity) {
		this.event1LoadShutOffCapacity = event1LoadShutOffCapacity;
	}

	public BigDecimal getEvent2LoadShutOffCapacity() {
		return this.event2LoadShutOffCapacity;
	}

	public void setEvent2LoadShutOffCapacity(BigDecimal event2LoadShutOffCapacity) {
		this.event2LoadShutOffCapacity = event2LoadShutOffCapacity;
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

	public MSmControlLoad getMSmControlLoad() {
		return this.MSmControlLoad;
	}

	public void setMSmControlLoad(MSmControlLoad MSmControlLoad) {
		this.MSmControlLoad = MSmControlLoad;
	}

	public MSmLineVerify getMSmLineVerify() {
		return this.MSmLineVerify;
	}

	public void setMSmLineVerify(MSmLineVerify MSmLineVerify) {
		this.MSmLineVerify = MSmLineVerify;
	}

}