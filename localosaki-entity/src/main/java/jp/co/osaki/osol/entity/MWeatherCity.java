package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_weather_city database table.
 * 
 */
@Entity
@Table(name="m_weather_city")
@NamedQuery(name="MWeatherCity.findAll", query="SELECT m FROM MWeatherCity m")
public class MWeatherCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="city_cd", unique=true, nullable=false, length=8)
	private String cityCd;

	@Column(name="city_name", nullable=false, length=50)
	private String cityName;

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

	//bi-directional many-to-one association to MPrefecture
	@ManyToOne
	@JoinColumn(name="prefecture_cd", nullable=false)
	private MPrefecture MPrefecture;

        //bi-directional many-to-one association to TBuilding
	@OneToMany(mappedBy="MWeatherCity")
	private List<TBuilding> TBuildings;

	//bi-directional many-to-one association to TTimeSlotWeather
	@OneToMany(mappedBy="MWeatherCity")
	private List<TTimeSlotWeather> TTimeSlotWeathers;

	//bi-directional many-to-one association to TTimelyWeather
	@OneToMany(mappedBy="MWeatherCity")
	private List<TTimelyWeather> TTimelyWeathers;

	//bi-directional many-to-one association to TWeekWeather
	@OneToMany(mappedBy="MWeatherCity")
	private List<TWeekWeather> TWeekWeathers;

	public MWeatherCity() {
	}

	public String getCityCd() {
		return this.cityCd;
	}

	public void setCityCd(String cityCd) {
		this.cityCd = cityCd;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

        public MPrefecture getMPrefecture() {
		return this.MPrefecture;
	}

	public void setMPrefecture(MPrefecture MPrefecture) {
		this.MPrefecture = MPrefecture;
	}

        public List<TBuilding> getTBuildings() {
		return this.TBuildings;
	}

	public void setTBuildings(List<TBuilding> TBuildings) {
		this.TBuildings = TBuildings;
	}

	public TBuilding addTBuilding(TBuilding TBuilding) {
		getTBuildings().add(TBuilding);
		TBuilding.setMWeatherCity(this);

		return TBuilding;
	}

	public TBuilding removeTBuilding(TBuilding TBuilding) {
		getTBuildings().remove(TBuilding);
		TBuilding.setMWeatherCity(null);

		return TBuilding;
	}

	public List<TTimeSlotWeather> getTTimeSlotWeathers() {
		return this.TTimeSlotWeathers;
	}

	public void setTTimeSlotWeathers(List<TTimeSlotWeather> TTimeSlotWeathers) {
		this.TTimeSlotWeathers = TTimeSlotWeathers;
	}

	public TTimeSlotWeather addTTimeSlotWeather(TTimeSlotWeather TTimeSlotWeather) {
		getTTimeSlotWeathers().add(TTimeSlotWeather);
		TTimeSlotWeather.setMWeatherCity(this);

		return TTimeSlotWeather;
	}

	public TTimeSlotWeather removeTTimeSlotWeather(TTimeSlotWeather TTimeSlotWeather) {
		getTTimeSlotWeathers().remove(TTimeSlotWeather);
		TTimeSlotWeather.setMWeatherCity(null);

		return TTimeSlotWeather;
	}

	public List<TTimelyWeather> getTTimelyWeathers() {
		return this.TTimelyWeathers;
	}

	public void setTTimelyWeathers(List<TTimelyWeather> TTimelyWeathers) {
		this.TTimelyWeathers = TTimelyWeathers;
	}

	public TTimelyWeather addTTimelyWeather(TTimelyWeather TTimelyWeather) {
		getTTimelyWeathers().add(TTimelyWeather);
		TTimelyWeather.setMWeatherCity(this);

		return TTimelyWeather;
	}

	public TTimelyWeather removeTTimelyWeather(TTimelyWeather TTimelyWeather) {
		getTTimelyWeathers().remove(TTimelyWeather);
		TTimelyWeather.setMWeatherCity(null);

		return TTimelyWeather;
	}

	public List<TWeekWeather> getTWeekWeathers() {
		return this.TWeekWeathers;
	}

	public void setTWeekWeathers(List<TWeekWeather> TWeekWeathers) {
		this.TWeekWeathers = TWeekWeathers;
	}

	public TWeekWeather addTWeekWeather(TWeekWeather TWeekWeather) {
		getTWeekWeathers().add(TWeekWeather);
		TWeekWeather.setMWeatherCity(this);

		return TWeekWeather;
	}

	public TWeekWeather removeTWeekWeather(TWeekWeather TWeekWeather) {
		getTWeekWeathers().remove(TWeekWeather);
		TWeekWeather.setMWeatherCity(null);

		return TWeekWeather;
	}

}