package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_function database table.
 * 
 */
@Entity
@Table(name="m_function")
@NamedQuery(name="MFunction.findAll", query="SELECT m FROM MFunction m")
public class MFunction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="function_cd", unique=true, nullable=false, length=3)
	private String functionCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="function_name", nullable=false, length=100)
	private String functionName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorpFunctionUse
	@OneToMany(mappedBy="MFunction")
	private List<MCorpFunctionUse> MCorpFunctionUses;

	public MFunction() {
	}

	public String getFunctionCd() {
		return this.functionCd;
	}

	public void setFunctionCd(String functionCd) {
		this.functionCd = functionCd;
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

	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
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

	public List<MCorpFunctionUse> getMCorpFunctionUses() {
		return this.MCorpFunctionUses;
	}

	public void setMCorpFunctionUses(List<MCorpFunctionUse> MCorpFunctionUses) {
		this.MCorpFunctionUses = MCorpFunctionUses;
	}

	public MCorpFunctionUse addMCorpFunctionUs(MCorpFunctionUse MCorpFunctionUs) {
		getMCorpFunctionUses().add(MCorpFunctionUs);
		MCorpFunctionUs.setMFunction(this);

		return MCorpFunctionUs;
	}

	public MCorpFunctionUse removeMCorpFunctionUs(MCorpFunctionUse MCorpFunctionUs) {
		getMCorpFunctionUses().remove(MCorpFunctionUs);
		MCorpFunctionUs.setMFunction(null);

		return MCorpFunctionUs;
	}

}