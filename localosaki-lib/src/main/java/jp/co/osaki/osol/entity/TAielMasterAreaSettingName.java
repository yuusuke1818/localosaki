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
 * The persistent class for the t_aiel_master_area_setting_name database table.
 * 
 */
@Entity
@Table(name="t_aiel_master_area_setting_name")
@NamedQuery(name="TAielMasterAreaSettingName.findAll", query="SELECT t FROM TAielMasterAreaSettingName t")
public class TAielMasterAreaSettingName implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TAielMasterAreaSettingNamePK id;

	@Column(name="area_name", length=60)
	private String areaName;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="sensor_name_1", length=60)
	private String sensorName1;

	@Column(name="sensor_name_2", length=60)
	private String sensorName2;

	@Column(name="sensor_name_3", length=60)
	private String sensorName3;

	@Column(name="sensor_name_4", length=60)
	private String sensorName4;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public TAielMasterAreaSettingName() {
	}

	public TAielMasterAreaSettingNamePK getId() {
		return this.id;
	}

	public void setId(TAielMasterAreaSettingNamePK id) {
		this.id = id;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	public String getSensorName1() {
		return this.sensorName1;
	}

	public void setSensorName1(String sensorName1) {
		this.sensorName1 = sensorName1;
	}

	public String getSensorName2() {
		return this.sensorName2;
	}

	public void setSensorName2(String sensorName2) {
		this.sensorName2 = sensorName2;
	}

	public String getSensorName3() {
		return this.sensorName3;
	}

	public void setSensorName3(String sensorName3) {
		this.sensorName3 = sensorName3;
	}

	public String getSensorName4() {
		return this.sensorName4;
	}

	public void setSensorName4(String sensorName4) {
		this.sensorName4 = sensorName4;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

}