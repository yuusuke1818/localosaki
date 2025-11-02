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
 * The persistent class for the m_meter_loadlimit database table.
 * 
 */
@Entity
@Table(name="m_meter_loadlimit")
@NamedQuery(name="MMeterLoadlimit.findAll", query="SELECT m FROM MMeterLoadlimit m")
public class MMeterLoadlimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMeterLoadlimitPK id;

	@Column(name="auto_injection", length=3)
	private String autoInjection;

	@Column(name="breaker_act_count", length=1)
	private String breakerActCount;

	@Column(name="command_flg", length=1)
	private String commandFlg;

	@Column(name="count_clear", length=2)
	private String countClear;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="load_current", length=2)
	private String loadCurrent;

	@Column(name="loadlimit_mode", length=1)
	private String loadlimitMode;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="srv_ent", length=1)
	private String srvEnt;

	@Column(name="temp_auto_injection", length=3)
	private String tempAutoInjection;

	@Column(name="temp_breaker_act_count", length=1)
	private String tempBreakerActCount;

	@Column(name="temp_count_clear", length=2)
	private String tempCountClear;

	@Column(name="temp_load_current", length=2)
	private String tempLoadCurrent;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MMeter
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dev_id", referencedColumnName="dev_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="meter_mng_id", referencedColumnName="meter_mng_id", nullable=false, insertable=false, updatable=false)
		})
	private MMeter MMeter;

	public MMeterLoadlimit() {
	}

	public MMeterLoadlimitPK getId() {
		return this.id;
	}

	public void setId(MMeterLoadlimitPK id) {
		this.id = id;
	}

	public String getAutoInjection() {
		return this.autoInjection;
	}

	public void setAutoInjection(String autoInjection) {
		this.autoInjection = autoInjection;
	}

	public String getBreakerActCount() {
		return this.breakerActCount;
	}

	public void setBreakerActCount(String breakerActCount) {
		this.breakerActCount = breakerActCount;
	}

	public String getCommandFlg() {
		return this.commandFlg;
	}

	public void setCommandFlg(String commandFlg) {
		this.commandFlg = commandFlg;
	}

	public String getCountClear() {
		return this.countClear;
	}

	public void setCountClear(String countClear) {
		this.countClear = countClear;
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

	public String getLoadCurrent() {
		return this.loadCurrent;
	}

	public void setLoadCurrent(String loadCurrent) {
		this.loadCurrent = loadCurrent;
	}

	public String getLoadlimitMode() {
		return this.loadlimitMode;
	}

	public void setLoadlimitMode(String loadlimitMode) {
		this.loadlimitMode = loadlimitMode;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
	}

	public String getSrvEnt() {
		return this.srvEnt;
	}

	public void setSrvEnt(String srvEnt) {
		this.srvEnt = srvEnt;
	}

	public String getTempAutoInjection() {
		return this.tempAutoInjection;
	}

	public void setTempAutoInjection(String tempAutoInjection) {
		this.tempAutoInjection = tempAutoInjection;
	}

	public String getTempBreakerActCount() {
		return this.tempBreakerActCount;
	}

	public void setTempBreakerActCount(String tempBreakerActCount) {
		this.tempBreakerActCount = tempBreakerActCount;
	}

	public String getTempCountClear() {
		return this.tempCountClear;
	}

	public void setTempCountClear(String tempCountClear) {
		this.tempCountClear = tempCountClear;
	}

	public String getTempLoadCurrent() {
		return this.tempLoadCurrent;
	}

	public void setTempLoadCurrent(String tempLoadCurrent) {
		this.tempLoadCurrent = tempLoadCurrent;
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

	public MMeter getMMeter() {
		return this.MMeter;
	}

	public void setMMeter(MMeter MMeter) {
		this.MMeter = MMeter;
	}

}