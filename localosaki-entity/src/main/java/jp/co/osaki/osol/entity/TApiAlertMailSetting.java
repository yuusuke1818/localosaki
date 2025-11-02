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
 * The persistent class for the t_api_alert_mail_setting database table.
 * 
 */
@Entity
@Table(name="t_api_alert_mail_setting")
@NamedQuery(name="TApiAlertMailSetting.findAll", query="SELECT t FROM TApiAlertMailSetting t")
public class TApiAlertMailSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TApiAlertMailSettingPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="default_destination_flg")
	private Integer defaultDestinationFlg;

	@Column(name="delivery_stop_flg")
	private Integer deliveryStopFlg;

	@Column(name="destination_type", length=6)
	private String destinationType;

	@Column(name="mail_address", length=100)
	private String mailAddress;

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

	public TApiAlertMailSetting() {
	}

	public TApiAlertMailSettingPK getId() {
		return this.id;
	}

	public void setId(TApiAlertMailSettingPK id) {
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

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
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