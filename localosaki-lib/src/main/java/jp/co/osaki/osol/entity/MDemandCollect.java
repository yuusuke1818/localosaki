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
 * The persistent class for the m_demand_collect database table.
 * 
 */
@Entity
@Table(name="m_demand_collect")
@NamedQuery(name="MDemandCollect.findAll", query="SELECT m FROM MDemandCollect m")
public class MDemandCollect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="demand_collect_cd", unique=true, nullable=false, length=3)
	private String demandCollectCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="demand_collect_name", length=100)
	private String demandCollectName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MDemandCollectProduct
	@OneToMany(mappedBy="MDemandCollect")
	private List<MDemandCollectProduct> MDemandCollectProducts;

	//bi-directional many-to-one association to MDemandCollectSchedule
	@OneToMany(mappedBy="MDemandCollect")
	private List<MDemandCollectSchedule> MDemandCollectSchedules;

	public MDemandCollect() {
	}

	public String getDemandCollectCd() {
		return this.demandCollectCd;
	}

	public void setDemandCollectCd(String demandCollectCd) {
		this.demandCollectCd = demandCollectCd;
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

	public String getDemandCollectName() {
		return this.demandCollectName;
	}

	public void setDemandCollectName(String demandCollectName) {
		this.demandCollectName = demandCollectName;
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

	public List<MDemandCollectProduct> getMDemandCollectProducts() {
		return this.MDemandCollectProducts;
	}

	public void setMDemandCollectProducts(List<MDemandCollectProduct> MDemandCollectProducts) {
		this.MDemandCollectProducts = MDemandCollectProducts;
	}

	public MDemandCollectProduct addMDemandCollectProduct(MDemandCollectProduct MDemandCollectProduct) {
		getMDemandCollectProducts().add(MDemandCollectProduct);
		MDemandCollectProduct.setMDemandCollect(this);

		return MDemandCollectProduct;
	}

	public MDemandCollectProduct removeMDemandCollectProduct(MDemandCollectProduct MDemandCollectProduct) {
		getMDemandCollectProducts().remove(MDemandCollectProduct);
		MDemandCollectProduct.setMDemandCollect(null);

		return MDemandCollectProduct;
	}

	public List<MDemandCollectSchedule> getMDemandCollectSchedules() {
		return this.MDemandCollectSchedules;
	}

	public void setMDemandCollectSchedules(List<MDemandCollectSchedule> MDemandCollectSchedules) {
		this.MDemandCollectSchedules = MDemandCollectSchedules;
	}

	public MDemandCollectSchedule addMDemandCollectSchedule(MDemandCollectSchedule MDemandCollectSchedule) {
		getMDemandCollectSchedules().add(MDemandCollectSchedule);
		MDemandCollectSchedule.setMDemandCollect(this);

		return MDemandCollectSchedule;
	}

	public MDemandCollectSchedule removeMDemandCollectSchedule(MDemandCollectSchedule MDemandCollectSchedule) {
		getMDemandCollectSchedules().remove(MDemandCollectSchedule);
		MDemandCollectSchedule.setMDemandCollect(null);

		return MDemandCollectSchedule;
	}

}