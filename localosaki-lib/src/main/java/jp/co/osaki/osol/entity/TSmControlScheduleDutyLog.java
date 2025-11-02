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
 * The persistent class for the t_sm_control_schedule_duty_log database table.
 *
 */
@Entity
@Table(name = "t_sm_control_schedule_duty_log")
@NamedQuery(name = "TSmControlScheduleDutyLog.findAll", query = "SELECT t FROM TSmControlScheduleDutyLog t")
public class TSmControlScheduleDutyLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TSmControlScheduleDutyLogPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "duty_control_time_slot", nullable = false, length = 6)
    private String dutyControlTimeSlot;

    @Column(name = "duty_off_time_minute", nullable = false)
    private Integer dutyOffTimeMinute;

    @Column(name = "duty_on_time_minute", nullable = false)
    private Integer dutyOnTimeMinute;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to TSmControlScheduleLog
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "sm_control_schedule_log_id", referencedColumnName = "sm_control_schedule_log_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "sm_id", referencedColumnName = "sm_id", nullable = false, insertable = false, updatable = false)
    })
    private TSmControlScheduleLog TSmControlScheduleLog;

    public TSmControlScheduleDutyLog() {
    }

    public TSmControlScheduleDutyLogPK getId() {
        return this.id;
    }

    public void setId(TSmControlScheduleDutyLogPK id) {
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

    public String getDutyControlTimeSlot() {
        return this.dutyControlTimeSlot;
    }

    public void setDutyControlTimeSlot(String dutyControlTimeSlot) {
        this.dutyControlTimeSlot = dutyControlTimeSlot;
    }

    public Integer getDutyOffTimeMinute() {
        return this.dutyOffTimeMinute;
    }

    public void setDutyOffTimeMinute(Integer dutyOffTimeMinute) {
        this.dutyOffTimeMinute = dutyOffTimeMinute;
    }

    public Integer getDutyOnTimeMinute() {
        return this.dutyOnTimeMinute;
    }

    public void setDutyOnTimeMinute(Integer dutyOnTimeMinute) {
        this.dutyOnTimeMinute = dutyOnTimeMinute;
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

    public TSmControlScheduleLog getTSmControlScheduleLog() {
        return this.TSmControlScheduleLog;
    }

    public void setTSmControlScheduleLog(TSmControlScheduleLog TSmControlScheduleLog) {
        this.TSmControlScheduleLog = TSmControlScheduleLog;
    }

}