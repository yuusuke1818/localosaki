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
 * The persistent class for the m_sm_control_load_verify database table.
 * 
 */
@Entity
@Table(name="m_sm_control_load_verify")
@NamedQuery(name="MSmControlLoadVerify.findAll", query="SELECT m FROM MSmControlLoadVerify m")
public class MSmControlLoadVerify implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmControlLoadVerifyPK id;

	@Column(name="control_load_running_hours", precision=2)
	private BigDecimal controlLoadRunningHours;

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

	//bi-directional many-to-one association to MSmControlLoad
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="control_load", referencedColumnName="control_load", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MSmControlLoad MSmControlLoad;

	public MSmControlLoadVerify() {
	}

	public MSmControlLoadVerifyPK getId() {
		return this.id;
	}

	public void setId(MSmControlLoadVerifyPK id) {
		this.id = id;
	}

	public BigDecimal getControlLoadRunningHours() {
		return this.controlLoadRunningHours;
	}

	public void setControlLoadRunningHours(BigDecimal controlLoadRunningHours) {
		this.controlLoadRunningHours = controlLoadRunningHours;
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

	public MSmControlLoad getMSmControlLoad() {
		return this.MSmControlLoad;
	}

	public void setMSmControlLoad(MSmControlLoad MSmControlLoad) {
		this.MSmControlLoad = MSmControlLoad;
	}

}