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
 * The persistent class for the m_maintenance_mail_setting database table.
 * 
 */
@Entity
@Table(name="m_maintenance_mail_setting")
@NamedQuery(name="MMaintenanceMailSetting.findAll", query="SELECT m FROM MMaintenanceMailSetting m")
public class MMaintenanceMailSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMaintenanceMailSettingPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="default_destination_flg", nullable=false)
	private Integer defaultDestinationFlg;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="delivery_stop_flg", nullable=false)
	private Integer deliveryStopFlg;

	@Column(name="destination_type", nullable=false, length=6)
	private String destinationType;

	@Column(name="maintenance_mail_address", length=100)
	private String maintenanceMailAddress;

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

	public MMaintenanceMailSetting() {
	}

	public MMaintenanceMailSettingPK getId() {
		return this.id;
	}

	public void setId(MMaintenanceMailSettingPK id) {
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

	public Integer getDefaultDestinationFlg() {
		return this.defaultDestinationFlg;
	}

	public void setDefaultDestinationFlg(Integer defaultDestinationFlg) {
		this.defaultDestinationFlg = defaultDestinationFlg;
	}

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public Integer getDeliveryStopFlg() {
		return this.deliveryStopFlg;
	}

	public void setDeliveryStopFlg(Integer deliveryStopFlg) {
		this.deliveryStopFlg = deliveryStopFlg;
	}

	public String getDestinationType() {
		return this.destinationType;
	}

	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}

	public String getMaintenanceMailAddress() {
		return this.maintenanceMailAddress;
	}

	public void setMaintenanceMailAddress(String maintenanceMailAddress) {
		this.maintenanceMailAddress = maintenanceMailAddress;
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