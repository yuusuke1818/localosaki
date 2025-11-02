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
 * The persistent class for the t_amedas_weather database table.
 * 
 */
@Entity
@Table(name="t_amedas_weather")
@NamedQuery(name="TAmedasWeather.findAll", query="SELECT t FROM TAmedasWeather t")
public class TAmedasWeather implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TAmedasWeatherPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="out_air_temp", nullable=false, precision=4, scale=1)
	private BigDecimal outAirTemp;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MAmedasObservatory
	@ManyToOne
	@JoinColumn(name="amedas_observatory_no", nullable=false, insertable=false, updatable=false)
	private MAmedasObservatory MAmedasObservatory;

	public TAmedasWeather() {
	}

	public TAmedasWeatherPK getId() {
		return this.id;
	}

	public void setId(TAmedasWeatherPK id) {
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

	public BigDecimal getOutAirTemp() {
		return this.outAirTemp;
	}

	public void setOutAirTemp(BigDecimal outAirTemp) {
		this.outAirTemp = outAirTemp;
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

	public MAmedasObservatory getMAmedasObservatory() {
		return this.MAmedasObservatory;
	}

	public void setMAmedasObservatory(MAmedasObservatory MAmedasObservatory) {
		this.MAmedasObservatory = MAmedasObservatory;
	}

}