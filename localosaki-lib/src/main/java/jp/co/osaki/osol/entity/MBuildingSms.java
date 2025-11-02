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
 * The persistent class for the m_building_sms database table.
 * 
 */
@Entity
@Table(name="m_building_sms")
@NamedQuery(name="MBuildingSms.findAll", query="SELECT m FROM MBuildingSms m")
public class MBuildingSms implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MBuildingSmsPK id;

	@Column(name="chk_int", precision=1)
	private BigDecimal chkInt;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(length=255)
	private String mail1;

	@Column(length=255)
	private String mail10;

	@Column(length=255)
	private String mail2;

	@Column(length=255)
	private String mail3;

	@Column(length=255)
	private String mail4;

	@Column(length=255)
	private String mail5;

	@Column(length=255)
	private String mail6;

	@Column(length=255)
	private String mail7;

	@Column(length=255)
	private String mail8;

	@Column(length=255)
	private String mail9;

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

	public MBuildingSms() {
	}

	public MBuildingSmsPK getId() {
		return this.id;
	}

	public void setId(MBuildingSmsPK id) {
		this.id = id;
	}

	public BigDecimal getChkInt() {
		return this.chkInt;
	}

	public void setChkInt(BigDecimal chkInt) {
		this.chkInt = chkInt;
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

	public String getMail1() {
		return this.mail1;
	}

	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

	public String getMail10() {
		return this.mail10;
	}

	public void setMail10(String mail10) {
		this.mail10 = mail10;
	}

	public String getMail2() {
		return this.mail2;
	}

	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

	public String getMail3() {
		return this.mail3;
	}

	public void setMail3(String mail3) {
		this.mail3 = mail3;
	}

	public String getMail4() {
		return this.mail4;
	}

	public void setMail4(String mail4) {
		this.mail4 = mail4;
	}

	public String getMail5() {
		return this.mail5;
	}

	public void setMail5(String mail5) {
		this.mail5 = mail5;
	}

	public String getMail6() {
		return this.mail6;
	}

	public void setMail6(String mail6) {
		this.mail6 = mail6;
	}

	public String getMail7() {
		return this.mail7;
	}

	public void setMail7(String mail7) {
		this.mail7 = mail7;
	}

	public String getMail8() {
		return this.mail8;
	}

	public void setMail8(String mail8) {
		this.mail8 = mail8;
	}

	public String getMail9() {
		return this.mail9;
	}

	public void setMail9(String mail9) {
		this.mail9 = mail9;
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