package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_type_power database table.
 * 
 */
@Entity
@Table(name="m_type_power")
@NamedQuery(name="MTypePower.findAll", query="SELECT m FROM MTypePower m")
public class MTypePower implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MTypePowerPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="day_other_price", precision=6, scale=2)
	private BigDecimal dayOtherPrice;

	@Column(name="day_summer_price", precision=6, scale=2)
	private BigDecimal daySummerPrice;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MTypeStage
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="basic_date", referencedColumnName="basic_date", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="meter_type", referencedColumnName="meter_type", nullable=false, insertable=false, updatable=false)
		})
	private MTypeStage MTypeStage;

	public MTypePower() {
	}

	public MTypePowerPK getId() {
		return this.id;
	}

	public void setId(MTypePowerPK id) {
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

	public BigDecimal getDayOtherPrice() {
		return this.dayOtherPrice;
	}

	public void setDayOtherPrice(BigDecimal dayOtherPrice) {
		this.dayOtherPrice = dayOtherPrice;
	}

	public BigDecimal getDaySummerPrice() {
		return this.daySummerPrice;
	}

	public void setDaySummerPrice(BigDecimal daySummerPrice) {
		this.daySummerPrice = daySummerPrice;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
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

	public MTypeStage getMTypeStage() {
		return this.MTypeStage;
	}

	public void setMTypeStage(MTypeStage MTypeStage) {
		this.MTypeStage = MTypeStage;
	}

}