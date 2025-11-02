package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_prefecture database table.
 * 
 */
@Entity
@Table(name="m_prefecture")
@NamedQueries({
    @NamedQuery(name="MPrefecture.findAll", query="SELECT m FROM MPrefecture m"),
    @NamedQuery(name="MPrefecture.findAllOrderByPrefectureCd", query="SELECT c FROM MPrefecture c ORDER BY c.prefectureCd"),
})
public class MPrefecture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="prefecture_cd", unique=true, nullable=false, length=2)
	private String prefectureCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="prefecture_name", nullable=false, length=100)
	private String prefectureName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@OneToMany(mappedBy="MPrefecture")
	private List<MCorp> MCorps;

	//bi-directional many-to-one association to MMunicipality
	@OneToMany(mappedBy="MPrefecture")
	private List<MMunicipality> MMunicipalities;

	//bi-directional many-to-one association to TBuilding
	@OneToMany(mappedBy="MPrefecture")
	private List<TBuilding> TBuildings;

	//bi-directional many-to-one association to MWeatherCity
	@OneToMany(mappedBy="MPrefecture")
	private List<MWeatherCity> MWeatherCities;

        public MPrefecture() {
	}

	public String getPrefectureCd() {
		return this.prefectureCd;
	}

	public void setPrefectureCd(String prefectureCd) {
		this.prefectureCd = prefectureCd;
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

	public String getPrefectureName() {
		return this.prefectureName;
	}

	public void setPrefectureName(String prefectureName) {
		this.prefectureName = prefectureName;
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

	public List<MCorp> getMCorps() {
		return this.MCorps;
	}

	public void setMCorps(List<MCorp> MCorps) {
		this.MCorps = MCorps;
	}

	public MCorp addMCorp(MCorp MCorp) {
		getMCorps().add(MCorp);
		MCorp.setMPrefecture(this);

		return MCorp;
	}

	public MCorp removeMCorp(MCorp MCorp) {
		getMCorps().remove(MCorp);
		MCorp.setMPrefecture(null);

		return MCorp;
	}

	public List<MMunicipality> getMMunicipalities() {
		return this.MMunicipalities;
	}

	public void setMMunicipalities(List<MMunicipality> MMunicipalities) {
		this.MMunicipalities = MMunicipalities;
	}

	public MMunicipality addMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().add(MMunicipality);
		MMunicipality.setMPrefecture(this);

		return MMunicipality;
	}

	public MMunicipality removeMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().remove(MMunicipality);
		MMunicipality.setMPrefecture(null);

		return MMunicipality;
	}

	public List<TBuilding> getTBuildings() {
		return this.TBuildings;
	}

	public void setTBuildings(List<TBuilding> TBuildings) {
		this.TBuildings = TBuildings;
	}

	public TBuilding addTBuilding(TBuilding TBuilding) {
		getTBuildings().add(TBuilding);
		TBuilding.setMPrefecture(this);

		return TBuilding;
	}

	public TBuilding removeTBuilding(TBuilding TBuilding) {
		getTBuildings().remove(TBuilding);
		TBuilding.setMPrefecture(null);

		return TBuilding;
	}

	public List<MWeatherCity> getMWeatherCities() {
		return this.MWeatherCities;
	}

	public void setMWeatherCities(List<MWeatherCity> MWeatherCities) {
		this.MWeatherCities = MWeatherCities;
	}

	public MWeatherCity addMWeatherCity(MWeatherCity MWeatherCity) {
		getMWeatherCities().add(MWeatherCity);
		MWeatherCity.setMPrefecture(this);

		return MWeatherCity;
	}

	public MWeatherCity removeMWeatherCity(MWeatherCity MWeatherCity) {
		getMWeatherCities().remove(MWeatherCity);
		MWeatherCity.setMPrefecture(null);

		return MWeatherCity;
	}

}