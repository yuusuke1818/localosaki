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
 * The persistent class for the m_mailaddress database table.
 * 
 */
@Entity
@Table(name="m_mailaddress")
@NamedQuery(name="MMailaddress.findAll", query="SELECT m FROM MMailaddress m")
public class MMailaddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Long no;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="disabled_flg", length=1)
	private String disabledFlg;

	@Column(length=100)
	private String email;

	@Column(length=100)
	private String memo;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public MMailaddress() {
	}

	public Long getNo() {
		return this.no;
	}

	public void setNo(Long no) {
		this.no = no;
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

	public String getDisabledFlg() {
		return this.disabledFlg;
	}

	public void setDisabledFlg(String disabledFlg) {
		this.disabledFlg = disabledFlg;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}