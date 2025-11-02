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
 * The persistent class for the m_line_type database table.
 * 
 */
@Entity
@Table(name="m_line_type")
@NamedQuery(name="MLineType.findAll", query="SELECT m FROM MLineType m")
public class MLineType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="line_type", unique=true, nullable=false, length=10)
	private String lineType;

	@Column(name="air_valid_flg", nullable=false)
	private Integer airValidFlg;

        @Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="dm_valid_flg", nullable=false)
	private Integer dmValidFlg;

	@Column(name="event_valid_flg", nullable=false)
	private Integer eventValidFlg;

        @Column(name="line_type_name", nullable=false, length=40)
	private String lineTypeName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MLine
	@OneToMany(mappedBy="MLineType")
	private List<MLine> MLines;

	public MLineType() {
	}

	public String getLineType() {
		return this.lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Integer getAirValidFlg() {
		return this.airValidFlg;
	}

	public void setAirValidFlg(Integer airValidFlg) {
		this.airValidFlg = airValidFlg;
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

	public Integer getDmValidFlg() {
		return this.dmValidFlg;
	}

	public void setDmValidFlg(Integer dmValidFlg) {
		this.dmValidFlg = dmValidFlg;
	}

	public Integer getEventValidFlg() {
		return this.eventValidFlg;
	}

	public void setEventValidFlg(Integer eventValidFlg) {
		this.eventValidFlg = eventValidFlg;
	}

        public String getLineTypeName() {
		return this.lineTypeName;
	}

	public void setLineTypeName(String lineTypeName) {
		this.lineTypeName = lineTypeName;
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

	public List<MLine> getMLines() {
		return this.MLines;
	}

	public void setMLines(List<MLine> MLines) {
		this.MLines = MLines;
	}

	public MLine addMLine(MLine MLine) {
		getMLines().add(MLine);
		MLine.setMLineType(this);

		return MLine;
	}

	public MLine removeMLine(MLine MLine) {
		getMLines().remove(MLine);
		MLine.setMLineType(null);

		return MLine;
	}

}