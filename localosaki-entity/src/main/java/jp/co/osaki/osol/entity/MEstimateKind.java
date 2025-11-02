package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_estimate_kind database table.
 * 
 */
@Entity
@Table(name = "m_estimate_kind")
@NamedQueries({
    @NamedQuery(name = "MEstimateKind.findAll", query = "SELECT m FROM MEstimateKind m"),
    @NamedQuery(name = "MEstimateKind.findValidAllByCorpId",
            query = "SELECT m FROM MEstimateKind m "
            + " WHERE m.id.corpId =:corpId "
            + " AND m.delFlg <>'1' "),
    @NamedQuery(name = "MEstimateKind.findByEstimateId",
            query = "SELECT m FROM MEstimateKind m "
            + " WHERE m.id.estimateId =:estimateId "
            + " AND m.delFlg <>'1' "),
    @NamedQuery(name = "MEstimateKind.findValidAll",
            query = "SELECT DISTINCT m FROM MEstimateKind m "
            + "LEFT JOIN FETCH m.MCorp mc "
            + " WHERE ( mc.corpType ='0' AND m.delFlg <>'1') "
            + " OR ( m.id.corpId =:corpId AND m.delFlg <>'1') "),})
public class MEstimateKind implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MEstimateKindPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="estimate_name", nullable=false, length=100)
	private String estimateName;

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

	//bi-directional many-to-one association to MEstimateUnit
	@OneToMany(mappedBy="MEstimateKind")
	private List<MEstimateUnit> MEstimateUnits;

	//bi-directional many-to-one association to TBuildingEstimateKind
	@OneToMany(mappedBy="MEstimateKind")
	private List<TBuildingEstimateKind> TBuildingEstimateKinds;

	public MEstimateKind() {
	}

	public MEstimateKindPK getId() {
		return this.id;
	}

	public void setId(MEstimateKindPK id) {
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

	public String getEstimateName() {
		return this.estimateName;
	}

	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
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

	public List<MEstimateUnit> getMEstimateUnits() {
		return this.MEstimateUnits;
	}

	public void setMEstimateUnits(List<MEstimateUnit> MEstimateUnits) {
		this.MEstimateUnits = MEstimateUnits;
	}

	public MEstimateUnit addMEstimateUnit(MEstimateUnit MEstimateUnit) {
		getMEstimateUnits().add(MEstimateUnit);
		MEstimateUnit.setMEstimateKind(this);

		return MEstimateUnit;
	}

	public MEstimateUnit removeMEstimateUnit(MEstimateUnit MEstimateUnit) {
		getMEstimateUnits().remove(MEstimateUnit);
		MEstimateUnit.setMEstimateKind(null);

		return MEstimateUnit;
	}

	public List<TBuildingEstimateKind> getTBuildingEstimateKinds() {
		return this.TBuildingEstimateKinds;
	}

	public void setTBuildingEstimateKinds(List<TBuildingEstimateKind> TBuildingEstimateKinds) {
		this.TBuildingEstimateKinds = TBuildingEstimateKinds;
	}

	public TBuildingEstimateKind addTBuildingEstimateKind(TBuildingEstimateKind TBuildingEstimateKind) {
		getTBuildingEstimateKinds().add(TBuildingEstimateKind);
		TBuildingEstimateKind.setMEstimateKind(this);

		return TBuildingEstimateKind;
	}

	public TBuildingEstimateKind removeTBuildingEstimateKind(TBuildingEstimateKind TBuildingEstimateKind) {
		getTBuildingEstimateKinds().remove(TBuildingEstimateKind);
		TBuildingEstimateKind.setMEstimateKind(null);

		return TBuildingEstimateKind;
	}

}