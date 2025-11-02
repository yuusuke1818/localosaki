package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the m_system database table.
 * 
 */
@Entity
@Table(name="m_system")
@NamedQuery(name="MSystem.findAll", query="SELECT m FROM MSystem m")
public class MSystem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="system_key", unique=true, nullable=false)
	private Integer systemKey;

	@Column(name="coefficient_note", length=1000)
	private String coefficientNote;

        @Temporal(TemporalType.DATE)
	@Column(name="coefficient_update", nullable=false)
	private Date coefficientUpdate;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public MSystem() {
	}

	public Integer getSystemKey() {
		return this.systemKey;
	}

	public void setSystemKey(Integer systemKey) {
		this.systemKey = systemKey;
	}

	public String getCoefficientNote() {
		return this.coefficientNote;
	}

	public void setCoefficientNote(String coefficientNote) {
		this.coefficientNote = coefficientNote;
	}

        public Date getCoefficientUpdate() {
		return this.coefficientUpdate;
	}

	public void setCoefficientUpdate(Date coefficientUpdate) {
		this.coefficientUpdate = coefficientUpdate;
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