package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_line_group_ex database table.
 * 
 */
@Entity
@Table(name="m_line_group_ex")
@NamedQuery(name="MLineGroupEx.findAll", query="SELECT m FROM MLineGroupEx m")
public class MLineGroupEx implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MLineGroupExPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="etc_line_error_flg", nullable=false)
	private Integer etcLineErrorFlg;

	@Column(name="etc_line_error_threshold", nullable=false, precision=6, scale=3)
	private BigDecimal etcLineErrorThreshold;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional one-to-one association to MLineGroup
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MLineGroup MLineGroup;

	public MLineGroupEx() {
	}

	public MLineGroupExPK getId() {
		return this.id;
	}

	public void setId(MLineGroupExPK id) {
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

	public Integer getEtcLineErrorFlg() {
		return this.etcLineErrorFlg;
	}

	public void setEtcLineErrorFlg(Integer etcLineErrorFlg) {
		this.etcLineErrorFlg = etcLineErrorFlg;
	}

	public BigDecimal getEtcLineErrorThreshold() {
		return this.etcLineErrorThreshold;
	}

	public void setEtcLineErrorThreshold(BigDecimal etcLineErrorThreshold) {
		this.etcLineErrorThreshold = etcLineErrorThreshold;
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

	public MLineGroup getMLineGroup() {
		return this.MLineGroup;
	}

	public void setMLineGroup(MLineGroup MLineGroup) {
		this.MLineGroup = MLineGroup;
	}

}