package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_concentrator database table.
 * 
 */
@Entity
@Table(name="m_concentrator")
@NamedQuery(name="MConcentrator.findAll", query="SELECT m FROM MConcentrator m")
public class MConcentrator implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MConcentratorPK id;

	@Column(name="command_flg", length=1)
	private String commandFlg;

	@Column(name="concent_sta", precision=1)
	private BigDecimal concentSta;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="if_type", precision=1)
	private BigDecimal ifType;

	@Column(name="ip_addr", length=15)
	private String ipAddr;

	@Column(length=40)
	private String memo;

	@Column(length=20)
	private String name;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

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

	public MConcentrator() {
	}

	public MConcentratorPK getId() {
		return this.id;
	}

	public void setId(MConcentratorPK id) {
		this.id = id;
	}

	public String getCommandFlg() {
		return this.commandFlg;
	}

	public void setCommandFlg(String commandFlg) {
		this.commandFlg = commandFlg;
	}

	public BigDecimal getConcentSta() {
		return this.concentSta;
	}

	public void setConcentSta(BigDecimal concentSta) {
		this.concentSta = concentSta;
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

	public BigDecimal getIfType() {
		return this.ifType;
	}

	public void setIfType(BigDecimal ifType) {
		this.ifType = ifType;
	}

	public String getIpAddr() {
		return this.ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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