package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_building_energy_type database table.
 * 
 */
@Entity
@Table(name="t_building_energy_type")
@NamedQuery(name="TBuildingEnergyType.findAll", query="SELECT t FROM TBuildingEnergyType t")
public class TBuildingEnergyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildingEnergyTypePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TAvailableEnergy
	@OneToMany(mappedBy="TBuildingEnergyType")
	private List<TAvailableEnergy> TAvailableEnergies;

	public TBuildingEnergyType() {
	}

	public TBuildingEnergyTypePK getId() {
		return this.id;
	}

	public void setId(TBuildingEnergyTypePK id) {
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

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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

	public List<TAvailableEnergy> getTAvailableEnergies() {
		return this.TAvailableEnergies;
	}

	public void setTAvailableEnergies(List<TAvailableEnergy> TAvailableEnergies) {
		this.TAvailableEnergies = TAvailableEnergies;
	}

	public TAvailableEnergy addTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		getTAvailableEnergies().add(TAvailableEnergy);
		TAvailableEnergy.setTBuildingEnergyType(this);

		return TAvailableEnergy;
	}

	public TAvailableEnergy removeTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		getTAvailableEnergies().remove(TAvailableEnergy);
		TAvailableEnergy.setTBuildingEnergyType(null);

		return TAvailableEnergy;
	}

}