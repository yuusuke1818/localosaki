package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_dev_prm database table.
 * 
 */
@Entity
@Table(name="m_dev_prm")
@NamedQuery(name="MDevPrm.findAll", query="SELECT m FROM MDevPrm m")
public class MDevPrm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="alert_disable_flg", length=1)
	private String alertDisableFlg;

	@Column(name="comm_interval", precision=2)
	private BigDecimal commInterval;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="demand_flg", length=1)
	private String demandFlg;

	@Column(name="dev_kind", nullable=false, length=1)
	private String devKind;

	@Column(name="dev_pw", length=12)
	private String devPw;

	@Column(name="dev_sta", precision=1)
	private BigDecimal devSta;

	@Column(name="exam_notice_month", precision=2)
	private BigDecimal examNoticeMonth;

	@Column(name="home_directory", length=40)
	private String homeDirectory;

	@Column(name="ip_addr", length=15)
	private String ipAddr;

	@Column(length=40)
	private String memo;

	@Column(length=40)
	private String name;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="rev_flg", length=1)
	private String revFlg;

	@Column(name="target_pwr", precision=4)
	private BigDecimal targetPwr;

	private Timestamp time;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MAlertMail
	@OneToMany(mappedBy="MDevPrm", cascade={CascadeType.ALL})
	private List<MAlertMail> MAlertMails;

	//bi-directional many-to-one association to MAutoInsp
	@OneToMany(mappedBy="MDevPrm")
	private List<MAutoInsp> MAutoInsps;

	//bi-directional many-to-one association to MConcentrator
	@OneToMany(mappedBy="MDevPrm")
	private List<MConcentrator> MConcentrators;

	//bi-directional many-to-one association to MDevRelation
	@OneToMany(mappedBy="MDevPrm")
	private List<MDevRelation> MDevRelations;

	//bi-directional many-to-one association to MMeter
	@OneToMany(mappedBy="MDevPrm")
	private List<MMeter> MMeters;

	//bi-directional many-to-one association to MRepeater
	@OneToMany(mappedBy="MDevPrm")
	private List<MRepeater> MRepeaters;

	public MDevPrm() {
	}

	public String getDevId() {
		return this.devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getAlertDisableFlg() {
		return this.alertDisableFlg;
	}

	public void setAlertDisableFlg(String alertDisableFlg) {
		this.alertDisableFlg = alertDisableFlg;
	}

	public BigDecimal getCommInterval() {
		return this.commInterval;
	}

	public void setCommInterval(BigDecimal commInterval) {
		this.commInterval = commInterval;
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

	public String getDemandFlg() {
		return this.demandFlg;
	}

	public void setDemandFlg(String demandFlg) {
		this.demandFlg = demandFlg;
	}

	public String getDevKind() {
		return this.devKind;
	}

	public void setDevKind(String devKind) {
		this.devKind = devKind;
	}

	public String getDevPw() {
		return this.devPw;
	}

	public void setDevPw(String devPw) {
		this.devPw = devPw;
	}

	public BigDecimal getDevSta() {
		return this.devSta;
	}

	public void setDevSta(BigDecimal devSta) {
		this.devSta = devSta;
	}

	public BigDecimal getExamNoticeMonth() {
		return this.examNoticeMonth;
	}

	public void setExamNoticeMonth(BigDecimal examNoticeMonth) {
		this.examNoticeMonth = examNoticeMonth;
	}

	public String getHomeDirectory() {
		return this.homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public String getIpAddr() {
		return this.ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getRevFlg() {
		return this.revFlg;
	}

	public void setRevFlg(String revFlg) {
		this.revFlg = revFlg;
	}

	public BigDecimal getTargetPwr() {
		return this.targetPwr;
	}

	public void setTargetPwr(BigDecimal targetPwr) {
		this.targetPwr = targetPwr;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
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

	public List<MAlertMail> getMAlertMails() {
		return this.MAlertMails;
	}

	public void setMAlertMails(List<MAlertMail> MAlertMails) {
		this.MAlertMails = MAlertMails;
	}

	public MAlertMail addMAlertMail(MAlertMail MAlertMail) {
		getMAlertMails().add(MAlertMail);
		MAlertMail.setMDevPrm(this);

		return MAlertMail;
	}

	public MAlertMail removeMAlertMail(MAlertMail MAlertMail) {
		getMAlertMails().remove(MAlertMail);
		MAlertMail.setMDevPrm(null);

		return MAlertMail;
	}

	public List<MAutoInsp> getMAutoInsps() {
		return this.MAutoInsps;
	}

	public void setMAutoInsps(List<MAutoInsp> MAutoInsps) {
		this.MAutoInsps = MAutoInsps;
	}

	public MAutoInsp addMAutoInsp(MAutoInsp MAutoInsp) {
		getMAutoInsps().add(MAutoInsp);
		MAutoInsp.setMDevPrm(this);

		return MAutoInsp;
	}

	public MAutoInsp removeMAutoInsp(MAutoInsp MAutoInsp) {
		getMAutoInsps().remove(MAutoInsp);
		MAutoInsp.setMDevPrm(null);

		return MAutoInsp;
	}

	public List<MConcentrator> getMConcentrators() {
		return this.MConcentrators;
	}

	public void setMConcentrators(List<MConcentrator> MConcentrators) {
		this.MConcentrators = MConcentrators;
	}

	public MConcentrator addMConcentrator(MConcentrator MConcentrator) {
		getMConcentrators().add(MConcentrator);
		MConcentrator.setMDevPrm(this);

		return MConcentrator;
	}

	public MConcentrator removeMConcentrator(MConcentrator MConcentrator) {
		getMConcentrators().remove(MConcentrator);
		MConcentrator.setMDevPrm(null);

		return MConcentrator;
	}

	public List<MDevRelation> getMDevRelations() {
		return this.MDevRelations;
	}

	public void setMDevRelations(List<MDevRelation> MDevRelations) {
		this.MDevRelations = MDevRelations;
	}

	public MDevRelation addMDevRelation(MDevRelation MDevRelation) {
		getMDevRelations().add(MDevRelation);
		MDevRelation.setMDevPrm(this);

		return MDevRelation;
	}

	public MDevRelation removeMDevRelation(MDevRelation MDevRelation) {
		getMDevRelations().remove(MDevRelation);
		MDevRelation.setMDevPrm(null);

		return MDevRelation;
	}

	public List<MMeter> getMMeters() {
		return this.MMeters;
	}

	public void setMMeters(List<MMeter> MMeters) {
		this.MMeters = MMeters;
	}

	public MMeter addMMeter(MMeter MMeter) {
		getMMeters().add(MMeter);
		MMeter.setMDevPrm(this);

		return MMeter;
	}

	public MMeter removeMMeter(MMeter MMeter) {
		getMMeters().remove(MMeter);
		MMeter.setMDevPrm(null);

		return MMeter;
	}

	public List<MRepeater> getMRepeaters() {
		return this.MRepeaters;
	}

	public void setMRepeaters(List<MRepeater> MRepeaters) {
		this.MRepeaters = MRepeaters;
	}

	public MRepeater addMRepeater(MRepeater MRepeater) {
		getMRepeaters().add(MRepeater);
		MRepeater.setMDevPrm(this);

		return MRepeater;
	}

	public MRepeater removeMRepeater(MRepeater MRepeater) {
		getMRepeaters().remove(MRepeater);
		MRepeater.setMDevPrm(null);

		return MRepeater;
	}

}