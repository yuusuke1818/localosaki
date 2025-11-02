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
 * The persistent class for the m_repeater database table.
 * 
 */
@Entity
@Table(name="m_repeater")
@NamedQuery(name="MRepeater.findAll", query="SELECT m FROM MRepeater m")
public class MRepeater implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MRepeaterPK id;

	@Column(name="command_flg", length=1)
	private String commandFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(length=40)
	private String memo;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="repeater_id", length=10)
	private String repeaterId;

	@Column(name="srv_ent", length=1)
	private String srvEnt;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MDevPrm
	@ManyToOne
	@JoinColumn(name="dev_id", nullable=false, insertable=false, updatable=false)
	private MDevPrm MDevPrm;

	public MRepeater() {
	}

	public MRepeaterPK getId() {
		return this.id;
	}

	public void setId(MRepeaterPK id) {
		this.id = id;
	}

	public String getCommandFlg() {
		return this.commandFlg;
	}

	public void setCommandFlg(String commandFlg) {
		this.commandFlg = commandFlg;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getRepeaterId() {
		return this.repeaterId;
	}

	public void setRepeaterId(String repeaterId) {
		this.repeaterId = repeaterId;
	}

	public String getSrvEnt() {
		return this.srvEnt;
	}

	public void setSrvEnt(String srvEnt) {
		this.srvEnt = srvEnt;
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

	public MDevPrm getMDevPrm() {
		return this.MDevPrm;
	}

	public void setMDevPrm(MDevPrm MDevPrm) {
		this.MDevPrm = MDevPrm;
	}

}