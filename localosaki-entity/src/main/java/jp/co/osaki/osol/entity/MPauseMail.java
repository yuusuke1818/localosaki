package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_pause_mail database table.
 *
 */
@Entity
@Table(name="m_pause_mail")
@NamedQuery(name="MPauseMail.findAll", query="SELECT m FROM MPauseMail m")
public class MPauseMail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="dev_id", unique=true, nullable=false, length=8)
    private String devId;

    @Column(name="autoinsp_comp", length=1)
    private String autoinspComp;

    @Column(name="autoinsp_date_setting", length=1)
    private String autoinspDateSetting;

    @Column(name="autoinsp_missing", length=1)
    private String autoinspMissing;

    @Column(name="command_err", length=1)
    private String commandErr;

    @Column(name="concent_err", length=1)
    private String concentErr;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="dmv_err", length=1)
    private String dmvErr;

    @Column(length=1)
    private String exam;

    @Column(name="loadsurvey_csv_err", length=1)
    private String loadsurveyCsvErr;

    @Column(name="loadsurvey_err", length=1)
    private String loadsurveyErr;

    @Column(name="manual_insp", length=1)
    private String manualInsp;

    @Column(name="meter_err", length=1)
    private String meterErr;

    @Column(name="rec_date", nullable=false)
    private Timestamp recDate;

    @Column(name="rec_man", nullable=false, length=50)
    private String recMan;

    @Column(name="term_err", length=1)
    private String termErr;

    @Column(name="unsettled_csv_err", length=1)
    private String unsettledCsvErr;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    @Column(name="work_hst_err", length=1)
    private String workHstErr;

    //bi-directional one-to-one association to MDevPrm
    @OneToOne
    @JoinColumn(name="dev_id", nullable=false, insertable=false, updatable=false)
    private MDevPrm MDevPrm;

    public MPauseMail() {
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getAutoinspComp() {
        return this.autoinspComp;
    }

    public void setAutoinspComp(String autoinspComp) {
        this.autoinspComp = autoinspComp;
    }

    public String getAutoinspDateSetting() {
        return this.autoinspDateSetting;
    }

    public void setAutoinspDateSetting(String autoinspDateSetting) {
        this.autoinspDateSetting = autoinspDateSetting;
    }

    public String getAutoinspMissing() {
        return this.autoinspMissing;
    }

    public void setAutoinspMissing(String autoinspMissing) {
        this.autoinspMissing = autoinspMissing;
    }

    public String getCommandErr() {
        return this.commandErr;
    }

    public void setCommandErr(String commandErr) {
        this.commandErr = commandErr;
    }

    public String getConcentErr() {
        return this.concentErr;
    }

    public void setConcentErr(String concentErr) {
        this.concentErr = concentErr;
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

    public String getDmvErr() {
        return this.dmvErr;
    }

    public void setDmvErr(String dmvErr) {
        this.dmvErr = dmvErr;
    }

    public String getExam() {
        return this.exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getLoadsurveyCsvErr() {
        return this.loadsurveyCsvErr;
    }

    public void setLoadsurveyCsvErr(String loadsurveyCsvErr) {
        this.loadsurveyCsvErr = loadsurveyCsvErr;
    }

    public String getLoadsurveyErr() {
        return this.loadsurveyErr;
    }

    public void setLoadsurveyErr(String loadsurveyErr) {
        this.loadsurveyErr = loadsurveyErr;
    }

    public String getManualInsp() {
        return this.manualInsp;
    }

    public void setManualInsp(String manualInsp) {
        this.manualInsp = manualInsp;
    }

    public String getMeterErr() {
        return this.meterErr;
    }

    public void setMeterErr(String meterErr) {
        this.meterErr = meterErr;
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

    public String getTermErr() {
        return this.termErr;
    }

    public void setTermErr(String termErr) {
        this.termErr = termErr;
    }

    public String getUnsettledCsvErr() {
        return this.unsettledCsvErr;
    }

    public void setUnsettledCsvErr(String unsettledCsvErr) {
        this.unsettledCsvErr = unsettledCsvErr;
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

    public String getWorkHstErr() {
        return this.workHstErr;
    }

    public void setWorkHstErr(String workHstErr) {
        this.workHstErr = workHstErr;
    }

    public MDevPrm getMDevPrm() {
        return this.MDevPrm;
    }

    public void setMDevPrm(MDevPrm MDevPrm) {
        this.MDevPrm = MDevPrm;
    }

}