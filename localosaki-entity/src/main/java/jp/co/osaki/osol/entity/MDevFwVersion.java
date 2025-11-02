package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_dev_fw_version database table.
 *
 */
@Entity
@Table(name="m_dev_fw_version")
@NamedQuery(name="MDevFwVersion.findAll", query="SELECT m FROM MDevFwVersion m")
public class MDevFwVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="current_fw_ver", nullable=false)
	private Integer currentFwVer;

	@Column(name="latest_update_date")
	private Timestamp latestUpdateDate;

	@Column(name="prev_update_date")
	private Timestamp prevUpdateDate;

	@Column(name="target_fw_ver")
	private Integer targetFwVer;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_flg", nullable=false)
	private Boolean updateFlg;

	@Column(name="update_group", length=15)
	private String updateGroup;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public MDevFwVersion() {
	}

	public String getDevId() {
		return this.devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
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

	public Integer getCurrentFwVer() {
		return this.currentFwVer;
	}

	public void setCurrentFwVer(Integer currentFwVer) {
		this.currentFwVer = currentFwVer;
	}

	public Timestamp getLatestUpdateDate() {
		return this.latestUpdateDate;
	}

	public void setLatestUpdateDate(Timestamp latestUpdateDate) {
		this.latestUpdateDate = latestUpdateDate;
	}

	public Timestamp getPrevUpdateDate() {
		return this.prevUpdateDate;
	}

	public void setPrevUpdateDate(Timestamp prevUpdateDate) {
		this.prevUpdateDate = prevUpdateDate;
	}

	public Integer getTargetFwVer() {
		return this.targetFwVer;
	}

	public void setTargetFwVer(Integer targetFwVer) {
		this.targetFwVer = targetFwVer;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Boolean getUpdateFlg() {
		return this.updateFlg;
	}

	public void setUpdateFlg(Boolean updateFlg) {
		this.updateFlg = updateFlg;
	}

	public String getUpdateGroup() {
		return this.updateGroup;
	}

	public void setUpdateGroup(String updateGroup) {
		this.updateGroup = updateGroup;
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

}