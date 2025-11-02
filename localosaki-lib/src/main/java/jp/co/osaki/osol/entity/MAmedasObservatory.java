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
 * The persistent class for the m_amedas_observatory database table.
 * 
 */
@Entity
@Table(name="m_amedas_observatory")
@NamedQuery(name="MAmedasObservatory.findAll", query="SELECT m FROM MAmedasObservatory m")
public class MAmedasObservatory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="amedas_observatory_no", unique=true, nullable=false, length=5)
	private String amedasObservatoryNo;

	@Column(name="amedas_observatory_name", nullable=false, length=32)
	private String amedasObservatoryName;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingDm
	@OneToMany(mappedBy="MAmedasObservatory")
	private List<MBuildingDm> MBuildingDms;

	//bi-directional many-to-one association to TAmedasWeather
	@OneToMany(mappedBy="MAmedasObservatory")
	private List<TAmedasWeather> TAmedasWeathers;

	public MAmedasObservatory() {
	}

	public String getAmedasObservatoryNo() {
		return this.amedasObservatoryNo;
	}

	public void setAmedasObservatoryNo(String amedasObservatoryNo) {
		this.amedasObservatoryNo = amedasObservatoryNo;
	}

	public String getAmedasObservatoryName() {
		return this.amedasObservatoryName;
	}

	public void setAmedasObservatoryName(String amedasObservatoryName) {
		this.amedasObservatoryName = amedasObservatoryName;
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

	public List<MBuildingDm> getMBuildingDms() {
		return this.MBuildingDms;
	}

	public void setMBuildingDms(List<MBuildingDm> MBuildingDms) {
		this.MBuildingDms = MBuildingDms;
	}

	public MBuildingDm addMBuildingDm(MBuildingDm MBuildingDm) {
		getMBuildingDms().add(MBuildingDm);
		MBuildingDm.setMAmedasObservatory(this);

		return MBuildingDm;
	}

	public MBuildingDm removeMBuildingDm(MBuildingDm MBuildingDm) {
		getMBuildingDms().remove(MBuildingDm);
		MBuildingDm.setMAmedasObservatory(null);

		return MBuildingDm;
	}

	public List<TAmedasWeather> getTAmedasWeathers() {
		return this.TAmedasWeathers;
	}

	public void setTAmedasWeathers(List<TAmedasWeather> TAmedasWeathers) {
		this.TAmedasWeathers = TAmedasWeathers;
	}

	public TAmedasWeather addTAmedasWeather(TAmedasWeather TAmedasWeather) {
		getTAmedasWeathers().add(TAmedasWeather);
		TAmedasWeather.setMAmedasObservatory(this);

		return TAmedasWeather;
	}

	public TAmedasWeather removeTAmedasWeather(TAmedasWeather TAmedasWeather) {
		getTAmedasWeathers().remove(TAmedasWeather);
		TAmedasWeather.setMAmedasObservatory(null);

		return TAmedasWeather;
	}

}