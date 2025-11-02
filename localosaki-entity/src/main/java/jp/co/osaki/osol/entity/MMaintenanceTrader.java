package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_maintenance_trader database table.
 * 
 */
@Entity
@Table(name="m_maintenance_trader")
@NamedQueries({
    @NamedQuery(name="MMaintenanceTrader.findAll", query="SELECT m FROM MMaintenanceTrader m"),
    @NamedQuery(name="MMaintenanceTrader.findTraderList",
            query = "SELECT m FROM MMaintenanceTrader m "
            + " WHERE (:maintenanceTraderName IS NULL OR m.maintenanceTraderName LIKE :maintenanceTraderName)"
            + " AND (:maintenanceTraderPersonName IS NULL OR m.maintenanceTraderPersonName LIKE :maintenanceTraderPersonName)"
            + " AND (:maintenanceMailAddress IS NULL OR m.maintenanceMailAddress LIKE :maintenanceMailAddress)"
            + " AND (:maintenanceTelNo IS NULL OR m.maintenanceTelNo LIKE :maintenanceTelNo)"
            + " AND (:maintenanceFaxNo IS NULL OR m.maintenanceFaxNo LIKE :maintenanceFaxNo)"
            + " AND (:corpId IS NULL OR m.id.corpId = :corpId)"
            + " AND m.delFlg = 0"
            + " ORDER BY m.id.corpId ASC, m.id.maintenanceTraderId ASC")
})
public class MMaintenanceTrader implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMaintenanceTraderPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="maintenance_fax_no", length=100)
	private String maintenanceFaxNo;

	@Column(name="maintenance_mail_address", length=100)
	private String maintenanceMailAddress;

	@Column(name="maintenance_tel_no", length=100)
	private String maintenanceTelNo;

	@Column(name="maintenance_trader_name", nullable=false, length=100)
	private String maintenanceTraderName;

	@Column(name="maintenance_trader_person_name", nullable=false, length=50)
	private String maintenanceTraderPersonName;

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

	public MMaintenanceTrader() {
	}

	public MMaintenanceTraderPK getId() {
		return this.id;
	}

	public void setId(MMaintenanceTraderPK id) {
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

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public String getMaintenanceFaxNo() {
		return this.maintenanceFaxNo;
	}

	public void setMaintenanceFaxNo(String maintenanceFaxNo) {
		this.maintenanceFaxNo = maintenanceFaxNo;
	}

	public String getMaintenanceMailAddress() {
		return this.maintenanceMailAddress;
	}

	public void setMaintenanceMailAddress(String maintenanceMailAddress) {
		this.maintenanceMailAddress = maintenanceMailAddress;
	}

	public String getMaintenanceTelNo() {
		return this.maintenanceTelNo;
	}

	public void setMaintenanceTelNo(String maintenanceTelNo) {
		this.maintenanceTelNo = maintenanceTelNo;
	}

	public String getMaintenanceTraderName() {
		return this.maintenanceTraderName;
	}

	public void setMaintenanceTraderName(String maintenanceTraderName) {
		this.maintenanceTraderName = maintenanceTraderName;
	}

	public String getMaintenanceTraderPersonName() {
		return this.maintenanceTraderPersonName;
	}

	public void setMaintenanceTraderPersonName(String maintenanceTraderPersonName) {
		this.maintenanceTraderPersonName = maintenanceTraderPersonName;
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