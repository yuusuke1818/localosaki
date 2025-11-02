package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the t_oshirase database table.
 *
 */
@Entity
@Table(name="t_oshirase")
@NamedQuery(name="TOshirase.findAll", query="SELECT t FROM TOshirase t")
public class TOshirase implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TOshirasePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="external_site_flg", nullable=false)
	private Integer externalSiteFlg;

	@Column(name="file_name", length=510)
	private String fileName;

	@Column(name="mark_code", nullable=false, length=6)
	private String markCode;

	@Temporal(TemporalType.DATE)
	@Column(name="published_end_day")
	private Date publishedEndDay;

	@Temporal(TemporalType.DATE)
	@Column(name="published_start_day", nullable=false)
	private Date publishedStartDay;

	@Column(name="save_file_path", length=510)
	private String saveFilePath;

	@Column(name="target_code", nullable=false, length=6)
	private String targetCode;

	@Column(nullable=false, length=200)
	private String title;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(length=300)
	private String url;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to MOshiraseDelivery
    @OneToMany(mappedBy="TOshirase", cascade={CascadeType.ALL})
    private List<MOshiraseDelivery> MOshiraseDeliveries;

	public TOshirase() {
	}

	public TOshirasePK getId() {
		return this.id;
	}

	public void setId(TOshirasePK id) {
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

	public Integer getExternalSiteFlg() {
		return this.externalSiteFlg;
	}

	public void setExternalSiteFlg(Integer externalSiteFlg) {
		this.externalSiteFlg = externalSiteFlg;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMarkCode() {
		return this.markCode;
	}

	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}

	public Date getPublishedEndDay() {
		return this.publishedEndDay;
	}

	public void setPublishedEndDay(Date publishedEndDay) {
		this.publishedEndDay = publishedEndDay;
	}

	public Date getPublishedStartDay() {
		return this.publishedStartDay;
	}

	public void setPublishedStartDay(Date publishedStartDay) {
		this.publishedStartDay = publishedStartDay;
	}

	public String getSaveFilePath() {
		return this.saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getTargetCode() {
		return this.targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public List<MOshiraseDelivery> getMOshiraseDeliveries() {
	    return this.MOshiraseDeliveries;
	}

	public void setMOshiraseDeliveries(List<MOshiraseDelivery> MOshiraseDeliveries) {
	    this.MOshiraseDeliveries = MOshiraseDeliveries;
	}

}