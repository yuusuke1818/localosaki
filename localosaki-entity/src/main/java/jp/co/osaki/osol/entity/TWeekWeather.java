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
 * The persistent class for the t_week_weather database table.
 * 
 */
@Entity
@Table(name="t_week_weather")
@NamedQuery(name="TWeekWeather.findAll", query="SELECT t FROM TWeekWeather t")
public class TWeekWeather implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TWeekWeatherPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="high_temp", nullable=false, precision=4, scale=1)
	private BigDecimal highTemp;

	@Column(name="low_temp", nullable=false, precision=4, scale=1)
	private BigDecimal lowTemp;

	@Column(name="rain_probability", nullable=false)
	private Integer rainProbability;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="weather_state", nullable=false, length=6)
	private String weatherState;

	//bi-directional many-to-one association to MWeatherCity
	@ManyToOne
	@JoinColumn(name="city_cd", nullable=false, insertable=false, updatable=false)
	private MWeatherCity MWeatherCity;

	public TWeekWeather() {
	}

	public TWeekWeatherPK getId() {
		return this.id;
	}

	public void setId(TWeekWeatherPK id) {
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

	public BigDecimal getHighTemp() {
		return this.highTemp;
	}

	public void setHighTemp(BigDecimal highTemp) {
		this.highTemp = highTemp;
	}

	public BigDecimal getLowTemp() {
		return this.lowTemp;
	}

	public void setLowTemp(BigDecimal lowTemp) {
		this.lowTemp = lowTemp;
	}

	public Integer getRainProbability() {
		return this.rainProbability;
	}

	public void setRainProbability(Integer rainProbability) {
		this.rainProbability = rainProbability;
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

	public String getWeatherState() {
		return this.weatherState;
	}

	public void setWeatherState(String weatherState) {
		this.weatherState = weatherState;
	}

	public MWeatherCity getMWeatherCity() {
		return this.MWeatherCity;
	}

	public void setMWeatherCity(MWeatherCity MWeatherCity) {
		this.MWeatherCity = MWeatherCity;
	}

}