package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the t_aggregate_reservation_info database table.
 *
 */
@Entity
@Table(name="t_aggregate_reservation_info")
@NamedQuery(name="TAggregateReservationInfo.findAll", query="SELECT t FROM TAggregateReservationInfo t")
public class TAggregateReservationInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TAggregateReservationInfoPK id;

    @Column(name="aggregate_condition", nullable=false, length=2147483647)
    private String aggregateCondition;

    @Column(name="aggregate_process_result", nullable=false, length=6)
    private String aggregateProcessResult;

    @Column(name="aggregate_process_status", nullable=false, length=6)
    private String aggregateProcessStatus;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="del_flg", nullable=false)
    private Integer delFlg;

    @Column(name="end_date")
    private Timestamp endDate;

    @Column(name="output_file_name", length=200)
    private String outputFileName;

    @Column(name="output_file_path", length=300)
    private String outputFilePath;

    @Column(name="reservation_date", nullable=false)
    private Timestamp reservationDate;

    @Column(name="start_date")
    private Timestamp startDate;

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

    //bi-directional many-to-one association to MPerson
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="person_corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="person_id", referencedColumnName="person_id", nullable=false, insertable=false, updatable=false)
        })
    private MPerson MPerson;

    public TAggregateReservationInfo() {
    }

    public TAggregateReservationInfoPK getId() {
        return this.id;
    }

    public void setId(TAggregateReservationInfoPK id) {
        this.id = id;
    }

    public String getAggregateCondition() {
        return this.aggregateCondition;
    }

    public void setAggregateCondition(String aggregateCondition) {
        this.aggregateCondition = aggregateCondition;
    }

    public String getAggregateProcessResult() {
        return this.aggregateProcessResult;
    }

    public void setAggregateProcessResult(String aggregateProcessResult) {
        this.aggregateProcessResult = aggregateProcessResult;
    }

    public String getAggregateProcessStatus() {
        return this.aggregateProcessStatus;
    }

    public void setAggregateProcessStatus(String aggregateProcessStatus) {
        this.aggregateProcessStatus = aggregateProcessStatus;
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

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getOutputFileName() {
        return this.outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFilePath() {
        return this.outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public Timestamp getReservationDate() {
        return this.reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
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

    public MPerson getMPerson() {
        return this.MPerson;
    }

    public void setMPerson(MPerson MPerson) {
        this.MPerson = MPerson;
    }

}