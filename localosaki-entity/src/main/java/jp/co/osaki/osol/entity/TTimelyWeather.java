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
 * The persistent class for the t_timely_weather database table.
 * 
 */
@Entity
@Table(name="t_timely_weather")
@NamedQuery(name="TTimelyWeather.findAll", query="SELECT t FROM TTimelyWeather t")
public class TTimelyWeather implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TTimelyWeatherPK id;

	@Column(name="barometric_pressure", nullable=false, precision=5, scale=1)
	private BigDecimal barometricPressure;

	@Column(name="cloud_amount", nullable=false, length=6)
	private String cloudAmount;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(nullable=false, precision=3)
	private BigDecimal humidity;

	@Column(name="rain_amount", nullable=false, precision=4, scale=1)
	private BigDecimal rainAmount;

	@Column(nullable=false, precision=4, scale=1)
	private BigDecimal temperature;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(nullable=false, length=6)
	private String weather;

	@Column(name="wind_direction", nullable=false, length=6)
	private String windDirection;

	@Column(name="wind_speed", nullable=false, precision=3, scale=1)
	private BigDecimal windSpeed;

	//bi-directional many-to-one association to MWeatherCity
	@ManyToOne
	@JoinColumn(name="city_cd", nullable=false, insertable=false, updatable=false)
	private MWeatherCity MWeatherCity;

	public TTimelyWeather() {
	}

	public TTimelyWeatherPK getId() {
		return this.id;
	}

	public void setId(TTimelyWeatherPK id) {
		this.id = id;
	}

	public BigDecimal getBarometricPressure() {
		return this.barometricPressure;
	}

	public void setBarometricPressure(BigDecimal barometricPressure) {
		this.barometricPressure = barometricPressure;
	}

	public String getCloudAmount() {
		return this.cloudAmount;
	}

	public void setCloudAmount(String cloudAmount) {
		this.cloudAmount = cloudAmount;
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

	public BigDecimal getHumidity() {
		return this.humidity;
	}

	public void setHumidity(BigDecimal humidity) {
		this.humidity = humidity;
	}

	public BigDecimal getRainAmount() {
		return this.rainAmount;
	}

	public void setRainAmount(BigDecimal rainAmount) {
		this.rainAmount = rainAmount;
	}

	public BigDecimal getTemperature() {
		return this.temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
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

	public String getWeather() {
		return this.weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public BigDecimal getWindSpeed() {
		return this.windSpeed;
	}

	public void setWindSpeed(BigDecimal windSpeed) {
		this.windSpeed = windSpeed;
	}

	public MWeatherCity getMWeatherCity() {
		return this.MWeatherCity;
	}

	public void setMWeatherCity(MWeatherCity MWeatherCity) {
		this.MWeatherCity = MWeatherCity;
	}

}