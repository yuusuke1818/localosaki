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
 * The persistent class for the m_sm_collect_manage database table.
 * 
 */
@Entity
@Table(name="m_sm_collect_manage")
@NamedQuery(name="MSmCollectManage.findAll", query="SELECT m FROM MSmCollectManage m")
public class MSmCollectManage implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmCollectManagePK id;

	@Column(name="collect_flg", nullable=false)
	private Integer collectFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(length=200)
	private String note;

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

	//bi-directional many-to-one association to TBatchStartupSetting
	@ManyToOne
	@JoinColumn(name="batch_process_cd", nullable=false, insertable=false, updatable=false)
	private TBatchStartupSetting TBatchStartupSetting;

	public MSmCollectManage() {
	}

	public MSmCollectManagePK getId() {
		return this.id;
	}

	public void setId(MSmCollectManagePK id) {
		this.id = id;
	}

	public Integer getCollectFlg() {
		return this.collectFlg;
	}

	public void setCollectFlg(Integer collectFlg) {
		this.collectFlg = collectFlg;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public TBatchStartupSetting getTBatchStartupSetting() {
		return this.TBatchStartupSetting;
	}

	public void setTBatchStartupSetting(TBatchStartupSetting TBatchStartupSetting) {
		this.TBatchStartupSetting = TBatchStartupSetting;
	}

}