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
 * The persistent class for the t_day_load_survey_max_demand_rev database table.
 * 
 */
@Entity
@Table(name="t_day_load_survey_max_demand_rev")
@NamedQuery(name="TDayLoadSurveyMaxDemandRev.findAll", query="SELECT t FROM TDayLoadSurveyMaxDemandRev t")
public class TDayLoadSurveyMaxDemandRev implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDayLoadSurveyMaxDemandRevPK id;

	@Column(name="create_date")
	private Timestamp createDate;

	@Column(name="create_user_id")
	private Long createUserId;

	@Column(name="max_demand")
	private BigDecimal maxDemand;

	@Column(name="rec_date")
	private Timestamp recDate;

	@Column(name="rec_man")
	private String recMan;

	@Column(name="update_date")
	private Timestamp updateDate;

	@Column(name="update_user_id")
	private Long updateUserId;

	@Version
	private Integer version;

	public TDayLoadSurveyMaxDemandRev() {
	}

	public TDayLoadSurveyMaxDemandRevPK getId() {
		return this.id;
	}

	public void setId(TDayLoadSurveyMaxDemandRevPK id) {
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

	public BigDecimal getMaxDemand() {
		return this.maxDemand;
	}

	public void setMaxDemand(BigDecimal maxDemand) {
		this.maxDemand = maxDemand;
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