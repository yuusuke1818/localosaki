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
 * The persistent class for the m_tenant_sms database table.
 * 
 */
@Entity
@Table(name="m_tenant_sms")
@NamedQuery(name="MTenantSm.findAll", query="SELECT m FROM MTenantSm m")
public class MTenantSm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MTenantSmPK id;

	@Column(length=80)
	private String address2;

	@Column(name="contract_capacity", precision=2)
	private BigDecimal contractCapacity;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="div_rate1", precision=3)
	private BigDecimal divRate1;

	@Column(name="div_rate10", precision=3)
	private BigDecimal divRate10;

	@Column(name="div_rate2", precision=3)
	private BigDecimal divRate2;

	@Column(name="div_rate3", precision=3)
	private BigDecimal divRate3;

	@Column(name="div_rate4", precision=3)
	private BigDecimal divRate4;

	@Column(name="div_rate5", precision=3)
	private BigDecimal divRate5;

	@Column(name="div_rate6", precision=3)
	private BigDecimal divRate6;

	@Column(name="div_rate7", precision=3)
	private BigDecimal divRate7;

	@Column(name="div_rate8", precision=3)
	private BigDecimal divRate8;

	@Column(name="div_rate9", precision=3)
	private BigDecimal divRate9;

	@Column(name="elec_home_disc", precision=2)
	private BigDecimal elecHomeDisc;

	@Column(name="ener_equip_disc_capacity", precision=2)
	private BigDecimal enerEquipDiscCapacity;

	@Column(name="fixed1_name", length=40)
	private String fixed1Name;

	@Column(name="fixed1_price", precision=6)
	private BigDecimal fixed1Price;

	@Column(name="fixed2_name", length=40)
	private String fixed2Name;

	@Column(name="fixed2_price", precision=6)
	private BigDecimal fixed2Price;

	@Column(name="fixed3_name", length=40)
	private String fixed3Name;

	@Column(name="fixed3_price", precision=6)
	private BigDecimal fixed3Price;

	@Column(name="fixed4_name", length=40)
	private String fixed4Name;

	@Column(name="fixed4_price", precision=6)
	private BigDecimal fixed4Price;

	@Column(name="micom_disc_capacity", precision=2)
	private BigDecimal micomDiscCapacity;

	@Column(name="price_menu_no", precision=2)
	private BigDecimal priceMenuNo;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="tenant_id", nullable=false)
	private Long tenantId;

	@Column(name="type1_com_divrate", precision=6, scale=3)
	private BigDecimal type1ComDivrate;

	@Column(name="type2_com_divrate", precision=6, scale=3)
	private BigDecimal type2ComDivrate;

	@Column(name="type3_com_divrate", precision=6, scale=3)
	private BigDecimal type3ComDivrate;

	@Column(name="type4_com_divrate", precision=6, scale=3)
	private BigDecimal type4ComDivrate;

	@Column(name="type5_com_divrate", precision=6, scale=3)
	private BigDecimal type5ComDivrate;

	@Column(name="type6_com_divrate", precision=6, scale=3)
	private BigDecimal type6ComDivrate;

	@Column(name="type7_com_divrate", precision=6, scale=3)
	private BigDecimal type7ComDivrate;

	@Column(name="type8_com_divrate", precision=6, scale=3)
	private BigDecimal type8ComDivrate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MTenantSm() {
	}

	public MTenantSmPK getId() {
		return this.id;
	}

	public void setId(MTenantSmPK id) {
		this.id = id;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public BigDecimal getContractCapacity() {
		return this.contractCapacity;
	}

	public void setContractCapacity(BigDecimal contractCapacity) {
		this.contractCapacity = contractCapacity;
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

	public BigDecimal getDivRate1() {
		return this.divRate1;
	}

	public void setDivRate1(BigDecimal divRate1) {
		this.divRate1 = divRate1;
	}

	public BigDecimal getDivRate10() {
		return this.divRate10;
	}

	public void setDivRate10(BigDecimal divRate10) {
		this.divRate10 = divRate10;
	}

	public BigDecimal getDivRate2() {
		return this.divRate2;
	}

	public void setDivRate2(BigDecimal divRate2) {
		this.divRate2 = divRate2;
	}

	public BigDecimal getDivRate3() {
		return this.divRate3;
	}

	public void setDivRate3(BigDecimal divRate3) {
		this.divRate3 = divRate3;
	}

	public BigDecimal getDivRate4() {
		return this.divRate4;
	}

	public void setDivRate4(BigDecimal divRate4) {
		this.divRate4 = divRate4;
	}

	public BigDecimal getDivRate5() {
		return this.divRate5;
	}

	public void setDivRate5(BigDecimal divRate5) {
		this.divRate5 = divRate5;
	}

	public BigDecimal getDivRate6() {
		return this.divRate6;
	}

	public void setDivRate6(BigDecimal divRate6) {
		this.divRate6 = divRate6;
	}

	public BigDecimal getDivRate7() {
		return this.divRate7;
	}

	public void setDivRate7(BigDecimal divRate7) {
		this.divRate7 = divRate7;
	}

	public BigDecimal getDivRate8() {
		return this.divRate8;
	}

	public void setDivRate8(BigDecimal divRate8) {
		this.divRate8 = divRate8;
	}

	public BigDecimal getDivRate9() {
		return this.divRate9;
	}

	public void setDivRate9(BigDecimal divRate9) {
		this.divRate9 = divRate9;
	}

	public BigDecimal getElecHomeDisc() {
		return this.elecHomeDisc;
	}

	public void setElecHomeDisc(BigDecimal elecHomeDisc) {
		this.elecHomeDisc = elecHomeDisc;
	}

	public BigDecimal getEnerEquipDiscCapacity() {
		return this.enerEquipDiscCapacity;
	}

	public void setEnerEquipDiscCapacity(BigDecimal enerEquipDiscCapacity) {
		this.enerEquipDiscCapacity = enerEquipDiscCapacity;
	}

	public String getFixed1Name() {
		return this.fixed1Name;
	}

	public void setFixed1Name(String fixed1Name) {
		this.fixed1Name = fixed1Name;
	}

	public BigDecimal getFixed1Price() {
		return this.fixed1Price;
	}

	public void setFixed1Price(BigDecimal fixed1Price) {
		this.fixed1Price = fixed1Price;
	}

	public String getFixed2Name() {
		return this.fixed2Name;
	}

	public void setFixed2Name(String fixed2Name) {
		this.fixed2Name = fixed2Name;
	}

	public BigDecimal getFixed2Price() {
		return this.fixed2Price;
	}

	public void setFixed2Price(BigDecimal fixed2Price) {
		this.fixed2Price = fixed2Price;
	}

	public String getFixed3Name() {
		return this.fixed3Name;
	}

	public void setFixed3Name(String fixed3Name) {
		this.fixed3Name = fixed3Name;
	}

	public BigDecimal getFixed3Price() {
		return this.fixed3Price;
	}

	public void setFixed3Price(BigDecimal fixed3Price) {
		this.fixed3Price = fixed3Price;
	}

	public String getFixed4Name() {
		return this.fixed4Name;
	}

	public void setFixed4Name(String fixed4Name) {
		this.fixed4Name = fixed4Name;
	}

	public BigDecimal getFixed4Price() {
		return this.fixed4Price;
	}

	public void setFixed4Price(BigDecimal fixed4Price) {
		this.fixed4Price = fixed4Price;
	}

	public BigDecimal getMicomDiscCapacity() {
		return this.micomDiscCapacity;
	}

	public void setMicomDiscCapacity(BigDecimal micomDiscCapacity) {
		this.micomDiscCapacity = micomDiscCapacity;
	}

	public BigDecimal getPriceMenuNo() {
		return this.priceMenuNo;
	}

	public void setPriceMenuNo(BigDecimal priceMenuNo) {
		this.priceMenuNo = priceMenuNo;
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

	public Long getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public BigDecimal getType1ComDivrate() {
		return this.type1ComDivrate;
	}

	public void setType1ComDivrate(BigDecimal type1ComDivrate) {
		this.type1ComDivrate = type1ComDivrate;
	}

	public BigDecimal getType2ComDivrate() {
		return this.type2ComDivrate;
	}

	public void setType2ComDivrate(BigDecimal type2ComDivrate) {
		this.type2ComDivrate = type2ComDivrate;
	}

	public BigDecimal getType3ComDivrate() {
		return this.type3ComDivrate;
	}

	public void setType3ComDivrate(BigDecimal type3ComDivrate) {
		this.type3ComDivrate = type3ComDivrate;
	}

	public BigDecimal getType4ComDivrate() {
		return this.type4ComDivrate;
	}

	public void setType4ComDivrate(BigDecimal type4ComDivrate) {
		this.type4ComDivrate = type4ComDivrate;
	}

	public BigDecimal getType5ComDivrate() {
		return this.type5ComDivrate;
	}

	public void setType5ComDivrate(BigDecimal type5ComDivrate) {
		this.type5ComDivrate = type5ComDivrate;
	}

	public BigDecimal getType6ComDivrate() {
		return this.type6ComDivrate;
	}

	public void setType6ComDivrate(BigDecimal type6ComDivrate) {
		this.type6ComDivrate = type6ComDivrate;
	}

	public BigDecimal getType7ComDivrate() {
		return this.type7ComDivrate;
	}

	public void setType7ComDivrate(BigDecimal type7ComDivrate) {
		this.type7ComDivrate = type7ComDivrate;
	}

	public BigDecimal getType8ComDivrate() {
		return this.type8ComDivrate;
	}

	public void setType8ComDivrate(BigDecimal type8ComDivrate) {
		this.type8ComDivrate = type8ComDivrate;
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

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}