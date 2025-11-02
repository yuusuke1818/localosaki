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
 * The persistent class for the t_api_use_setting database table.
 * 
 */
@Entity
@Table(name="t_api_use_setting")
@NamedQuery(name="TApiUseSetting.findAll", query="SELECT t FROM TApiUseSetting t")
public class TApiUseSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TApiUseSettingPK id;

	@Column(name="closing_day", nullable=false, length=2)
	private String closingDay;

	@Column(name="closing_day_setting_flg", nullable=false)
	private Integer closingDaySettingFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="max_val")
	private Integer maxVal;

	@Column(name="prediction_val")
	private Integer predictionVal;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	public TApiUseSetting() {
	}

	public TApiUseSettingPK getId() {
		return this.id;
	}

	public void setId(TApiUseSettingPK id) {
		this.id = id;
	}

	public String getClosingDay() {
		return this.closingDay;
	}

	public void setClosingDay(String closingDay) {
		this.closingDay = closingDay;
	}

	public Integer getClosingDaySettingFlg() {
		return this.closingDaySettingFlg;
	}

	public void setClosingDaySettingFlg(Integer closingDaySettingFlg) {
		this.closingDaySettingFlg = closingDaySettingFlg;
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

	public Integer getMaxVal() {
		return this.maxVal;
	}

	public void setMaxVal(Integer maxVal) {
		this.maxVal = maxVal;
	}

	public Integer getPredictionVal() {
		return this.predictionVal;
	}

	public void setPredictionVal(Integer predictionVal) {
		this.predictionVal = predictionVal;
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

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

}