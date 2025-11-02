package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_sm_connect_control_setting database table.
 * 
 */
@Entity
@Table(name="t_sm_connect_control_setting")
@NamedQuery(name="TSmConnectControlSetting.findAll", query="SELECT t FROM TSmConnectControlSetting t")
public class TSmConnectControlSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sm_connect_control_setting_id", unique=true, nullable=false)
	private Long smConnectControlSettingId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="parallel_connect_control_max_count", nullable=false)
	private Integer parallelConnectControlMaxCount;

	@Column(name="sm_connect_control_group_name", length=100)
	private String smConnectControlGroupName;

	@Column(name="sm_connect_retry_count", nullable=false)
	private Integer smConnectRetryCount;

	@Column(name="sm_connect_wait_time", nullable=false)
	private Integer smConnectWaitTime;

	@Column(name="socket_connect_retry_count", nullable=false)
	private Integer socketConnectRetryCount;

	@Column(name="socket_connect_wait_time", nullable=false)
	private Integer socketConnectWaitTime;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TSmConnectControlProduct
	@OneToMany(mappedBy="TSmConnectControlSetting", cascade={CascadeType.ALL})
	private List<TSmConnectControlProduct> TSmConnectControlProducts;

	public TSmConnectControlSetting() {
	}

	public Long getSmConnectControlSettingId() {
		return this.smConnectControlSettingId;
	}

	public void setSmConnectControlSettingId(Long smConnectControlSettingId) {
		this.smConnectControlSettingId = smConnectControlSettingId;
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

	public Integer getParallelConnectControlMaxCount() {
		return this.parallelConnectControlMaxCount;
	}

	public void setParallelConnectControlMaxCount(Integer parallelConnectControlMaxCount) {
		this.parallelConnectControlMaxCount = parallelConnectControlMaxCount;
	}

	public String getSmConnectControlGroupName() {
		return this.smConnectControlGroupName;
	}

	public void setSmConnectControlGroupName(String smConnectControlGroupName) {
		this.smConnectControlGroupName = smConnectControlGroupName;
	}

	public Integer getSmConnectRetryCount() {
		return this.smConnectRetryCount;
	}

	public void setSmConnectRetryCount(Integer smConnectRetryCount) {
		this.smConnectRetryCount = smConnectRetryCount;
	}

	public Integer getSmConnectWaitTime() {
		return this.smConnectWaitTime;
	}

	public void setSmConnectWaitTime(Integer smConnectWaitTime) {
		this.smConnectWaitTime = smConnectWaitTime;
	}

	public Integer getSocketConnectRetryCount() {
		return this.socketConnectRetryCount;
	}

	public void setSocketConnectRetryCount(Integer socketConnectRetryCount) {
		this.socketConnectRetryCount = socketConnectRetryCount;
	}

	public Integer getSocketConnectWaitTime() {
		return this.socketConnectWaitTime;
	}

	public void setSocketConnectWaitTime(Integer socketConnectWaitTime) {
		this.socketConnectWaitTime = socketConnectWaitTime;
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

	public List<TSmConnectControlProduct> getTSmConnectControlProducts() {
		return this.TSmConnectControlProducts;
	}

	public void setTSmConnectControlProducts(List<TSmConnectControlProduct> TSmConnectControlProducts) {
		this.TSmConnectControlProducts = TSmConnectControlProducts;
	}

	public TSmConnectControlProduct addTSmConnectControlProduct(TSmConnectControlProduct TSmConnectControlProduct) {
		getTSmConnectControlProducts().add(TSmConnectControlProduct);
		TSmConnectControlProduct.setTSmConnectControlSetting(this);

		return TSmConnectControlProduct;
	}

	public TSmConnectControlProduct removeTSmConnectControlProduct(TSmConnectControlProduct TSmConnectControlProduct) {
		getTSmConnectControlProducts().remove(TSmConnectControlProduct);
		TSmConnectControlProduct.setTSmConnectControlSetting(null);

		return TSmConnectControlProduct;
	}

}