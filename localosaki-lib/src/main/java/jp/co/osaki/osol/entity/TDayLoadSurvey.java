package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_day_load_survey database table.
 * 
 */
@Entity
@Table(name="t_day_load_survey")
@NamedQuery(name="TDayLoadSurvey.findAll", query="SELECT t FROM TDayLoadSurvey t")
public class TDayLoadSurvey implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDayLoadSurveyPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="dmv_kwh", precision=6, scale=1)
	private BigDecimal dmvKwh;

	@Column(precision=8, scale=2)
	private BigDecimal kwh30;

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

	public TDayLoadSurvey() {
	}

	public TDayLoadSurveyPK getId() {
		return this.id;
	}

	public void setId(TDayLoadSurveyPK id) {
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

	public BigDecimal getDmvKwh() {
		return this.dmvKwh;
	}

	public void setDmvKwh(BigDecimal dmvKwh) {
		this.dmvKwh = dmvKwh;
	}

	public BigDecimal getKwh30() {
		return this.kwh30;
	}

	public void setKwh30(BigDecimal kwh30) {
		this.kwh30 = kwh30;
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