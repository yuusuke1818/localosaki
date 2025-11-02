package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Time;
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
 * The persistent class for the t_signage_content database table.
 * 
 */
@Entity
@Table(name="t_signage_content")
@NamedQuery(name="TSignageContent.findAll", query="SELECT t FROM TSignageContent t")
public class TSignageContent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSignageContentPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_end_time", nullable=false)
	private Time displayEndTime;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="display_start_time", nullable=false)
	private Time displayStartTime;

	@Column(name="font_color_code", nullable=false, length=7)
	private String fontColorCode;

	@Column(name="font_size", nullable=false)
	private Integer fontSize;

	@Column(name="image_file_name", nullable=false, length=100)
	private String imageFileName;

	@Column(name="image_file_path", nullable=false, length=300)
	private String imageFilePath;

	@Column(length=300)
	private String message;

	@Column(name="signage_contents_type", nullable=false, length=6)
	private String signageContentsType;

	@Column(name="specify_friday", nullable=false)
	private Integer specifyFriday;

	@Column(name="specify_monday", nullable=false)
	private Integer specifyMonday;

	@Column(name="specify_saturday", nullable=false)
	private Integer specifySaturday;

	@Column(name="specify_sunday", nullable=false)
	private Integer specifySunday;

	@Column(name="specify_thursday", nullable=false)
	private Integer specifyThursday;

	@Column(name="specify_tuesday", nullable=false)
	private Integer specifyTuesday;

	@Column(name="specify_wednesday", nullable=false)
	private Integer specifyWednesday;

	@Column(nullable=false, length=50)
	private String title;

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

	public TSignageContent() {
	}

	public TSignageContentPK getId() {
		return this.id;
	}

	public void setId(TSignageContentPK id) {
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

	public Time getDisplayEndTime() {
		return this.displayEndTime;
	}

	public void setDisplayEndTime(Time displayEndTime) {
		this.displayEndTime = displayEndTime;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Time getDisplayStartTime() {
		return this.displayStartTime;
	}

	public void setDisplayStartTime(Time displayStartTime) {
		this.displayStartTime = displayStartTime;
	}

	public String getFontColorCode() {
		return this.fontColorCode;
	}

	public void setFontColorCode(String fontColorCode) {
		this.fontColorCode = fontColorCode;
	}

	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getImageFileName() {
		return this.imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageFilePath() {
		return this.imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSignageContentsType() {
		return this.signageContentsType;
	}

	public void setSignageContentsType(String signageContentsType) {
		this.signageContentsType = signageContentsType;
	}

	public Integer getSpecifyFriday() {
		return this.specifyFriday;
	}

	public void setSpecifyFriday(Integer specifyFriday) {
		this.specifyFriday = specifyFriday;
	}

	public Integer getSpecifyMonday() {
		return this.specifyMonday;
	}

	public void setSpecifyMonday(Integer specifyMonday) {
		this.specifyMonday = specifyMonday;
	}

	public Integer getSpecifySaturday() {
		return this.specifySaturday;
	}

	public void setSpecifySaturday(Integer specifySaturday) {
		this.specifySaturday = specifySaturday;
	}

	public Integer getSpecifySunday() {
		return this.specifySunday;
	}

	public void setSpecifySunday(Integer specifySunday) {
		this.specifySunday = specifySunday;
	}

	public Integer getSpecifyThursday() {
		return this.specifyThursday;
	}

	public void setSpecifyThursday(Integer specifyThursday) {
		this.specifyThursday = specifyThursday;
	}

	public Integer getSpecifyTuesday() {
		return this.specifyTuesday;
	}

	public void setSpecifyTuesday(Integer specifyTuesday) {
		this.specifyTuesday = specifyTuesday;
	}

	public Integer getSpecifyWednesday() {
		return this.specifyWednesday;
	}

	public void setSpecifyWednesday(Integer specifyWednesday) {
		this.specifyWednesday = specifyWednesday;
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