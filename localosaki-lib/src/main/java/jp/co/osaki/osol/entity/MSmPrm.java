package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the m_sm_prm database table.
 *
 */
@Entity
@Table(name = "m_sm_prm")
@NamedQuery(name = "MSmPrm.findAll", query = "SELECT m FROM MSmPrm m")
public class MSmPrm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sm_id", unique = true, nullable = false)
    private Long smId;

	@Column(name="aiel_master_connect_flg", nullable=false)
	private Integer aielMasterConnectFlg;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "ip_address", length = 15)
    private String ipAddress;

    @Column(length = 200)
    private String note;

    @Column(name = "plot_analog_point_no_1", length = 4)
    private String plotAnalogPointNo1;

    @Column(name = "plot_analog_point_no_2", length = 4)
    private String plotAnalogPointNo2;

    @Column(name = "sm_address", nullable = false, length = 4)
    private String smAddress;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MBuildingSm
    @OneToMany(mappedBy = "MSmPrm")
    private List<MBuildingSm> MBuildingSms;

    //bi-directional many-to-one association to MSmAirSetting
    @OneToMany(mappedBy = "MSmPrm")
    private List<MSmAirSetting> MSmAirSettings;

	//bi-directional many-to-one association to MSmCollectManage
	@OneToMany(mappedBy="MSmPrm")
	private List<MSmCollectManage> MSmCollectManages;

    //bi-directional many-to-one association to MSmControlLoad
    @OneToMany(mappedBy = "MSmPrm")
    private List<MSmControlLoad> MSmControlLoads;

    //bi-directional many-to-one association to MSmPoint
    @OneToMany(mappedBy = "MSmPrm")
    private List<MSmPoint> MSmPoints;

    //bi-directional one-to-one association to MSmPrmEx
    @OneToOne(mappedBy = "MSmPrm")
    private MSmPrmEx MSmPrmEx;

    //bi-directional many-to-one association to MProductSpec
    @ManyToOne
    @JoinColumn(name = "product_cd", nullable = false)
    private MProductSpec MProductSpec;

	//bi-directional many-to-one association to TDemandPowerForecastDay
	@OneToMany(mappedBy="MSmPrm")
	private List<TDemandPowerForecastDay> TDemandPowerForecastDays;

	//bi-directional many-to-one association to TDemandPowerForecastTime
	@OneToMany(mappedBy="MSmPrm")
	private List<TDemandPowerForecastTime> TDemandPowerForecastTimes;

    //bi-directional many-to-one association to TEventControlLog
    @OneToMany(mappedBy = "MSmPrm")
    private List<TEventControlLog> TEventControlLogs;

    //bi-directional many-to-one association to TLoadControlLog
    @OneToMany(mappedBy = "MSmPrm")
    private List<TLoadControlLog> TLoadControlLogs;

    //bi-directional many-to-one association to TTempHumidControlLog
    @OneToMany(mappedBy = "MSmPrm")
    private List<TTempHumidControlLog> TTempHumidControlLogs;

    //bi-directional one-to-one association to TSmConnectStatus
    @OneToOne(mappedBy = "MSmPrm")
    private TSmConnectStatus TSmConnectStatus;

    //bi-directional many-to-one association to TSmControlScheduleLog
    @OneToMany(mappedBy = "MSmPrm")
    private List<TSmControlScheduleLog> TSmControlScheduleLogs;

    //bi-directional many-to-one association to TLoadControlResult
    @OneToMany(mappedBy = "MSmPrm")
    private List<TLoadControlResult> TLoadControlResults;
    
    //bi-directional one-to-one association to TSmAlarmCall
	@OneToOne(mappedBy="MSmPrm")
	private TSmAlarmCall TSmAlarmCall;

    //bi-directional many-to-one association to TSmControlHolidayLog
    @OneToMany(mappedBy = "MSmPrm")
    private List<TSmControlHolidayLog> TSmControlHolidayLogs;

    public MSmPrm() {
    }

    public Long getSmId() {
        return this.smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
    }

	public Integer getAielMasterConnectFlg() {
		return this.aielMasterConnectFlg;
	}

	public void setAielMasterConnectFlg(Integer aielMasterConnectFlg) {
		this.aielMasterConnectFlg = aielMasterConnectFlg;
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

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPlotAnalogPointNo1() {
        return this.plotAnalogPointNo1;
    }

    public void setPlotAnalogPointNo1(String plotAnalogPointNo1) {
        this.plotAnalogPointNo1 = plotAnalogPointNo1;
    }

    public String getPlotAnalogPointNo2() {
        return this.plotAnalogPointNo2;
    }

    public void setPlotAnalogPointNo2(String plotAnalogPointNo2) {
        this.plotAnalogPointNo2 = plotAnalogPointNo2;
    }

    public String getSmAddress() {
        return this.smAddress;
    }

    public void setSmAddress(String smAddress) {
        this.smAddress = smAddress;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
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

    public List<MBuildingSm> getMBuildingSms() {
        return this.MBuildingSms;
    }

    public void setMBuildingSms(List<MBuildingSm> MBuildingSms) {
        this.MBuildingSms = MBuildingSms;
    }

    public MBuildingSm addMBuildingSm(MBuildingSm MBuildingSm) {
        getMBuildingSms().add(MBuildingSm);
        MBuildingSm.setMSmPrm(this);

        return MBuildingSm;
    }

    public MBuildingSm removeMBuildingSm(MBuildingSm MBuildingSm) {
        getMBuildingSms().remove(MBuildingSm);
        MBuildingSm.setMSmPrm(null);

        return MBuildingSm;
    }

    public List<MSmAirSetting> getMSmAirSettings() {
        return this.MSmAirSettings;
    }

    public void setMSmAirSettings(List<MSmAirSetting> MSmAirSettings) {
        this.MSmAirSettings = MSmAirSettings;
    }

    public MSmAirSetting addMSmAirSetting(MSmAirSetting MSmAirSetting) {
        getMSmAirSettings().add(MSmAirSetting);
        MSmAirSetting.setMSmPrm(this);

        return MSmAirSetting;
    }

    public MSmAirSetting removeMSmAirSetting(MSmAirSetting MSmAirSetting) {
        getMSmAirSettings().remove(MSmAirSetting);
        MSmAirSetting.setMSmPrm(null);

        return MSmAirSetting;
    }

	public List<MSmCollectManage> getMSmCollectManages() {
		return this.MSmCollectManages;
	}

	public void setMSmCollectManages(List<MSmCollectManage> MSmCollectManages) {
		this.MSmCollectManages = MSmCollectManages;
	}

	public MSmCollectManage addMSmCollectManage(MSmCollectManage MSmCollectManage) {
		getMSmCollectManages().add(MSmCollectManage);
		MSmCollectManage.setMSmPrm(this);

		return MSmCollectManage;
	}

	public MSmCollectManage removeMSmCollectManage(MSmCollectManage MSmCollectManage) {
		getMSmCollectManages().remove(MSmCollectManage);
		MSmCollectManage.setMSmPrm(null);

		return MSmCollectManage;
	}

    public List<MSmControlLoad> getMSmControlLoads() {
        return this.MSmControlLoads;
    }

    public void setMSmControlLoads(List<MSmControlLoad> MSmControlLoads) {
        this.MSmControlLoads = MSmControlLoads;
    }

    public MSmControlLoad addMSmControlLoad(MSmControlLoad MSmControlLoad) {
        getMSmControlLoads().add(MSmControlLoad);
        MSmControlLoad.setMSmPrm(this);

        return MSmControlLoad;
    }

    public MSmControlLoad removeMSmControlLoad(MSmControlLoad MSmControlLoad) {
        getMSmControlLoads().remove(MSmControlLoad);
        MSmControlLoad.setMSmPrm(null);

        return MSmControlLoad;
    }

    public List<MSmPoint> getMSmPoints() {
        return this.MSmPoints;
    }

    public void setMSmPoints(List<MSmPoint> MSmPoints) {
        this.MSmPoints = MSmPoints;
    }

    public MSmPoint addMSmPoint(MSmPoint MSmPoint) {
        getMSmPoints().add(MSmPoint);
        MSmPoint.setMSmPrm(this);

        return MSmPoint;
    }

    public MSmPoint removeMSmPoint(MSmPoint MSmPoint) {
        getMSmPoints().remove(MSmPoint);
        MSmPoint.setMSmPrm(null);

        return MSmPoint;
    }

    public MSmPrmEx getMSmPrmEx() {
        return this.MSmPrmEx;
    }

    public void setMSmPrmEx(MSmPrmEx MSmPrmEx) {
        this.MSmPrmEx = MSmPrmEx;
    }

    public MProductSpec getMProductSpec() {
        return this.MProductSpec;
    }

    public void setMProductSpec(MProductSpec MProductSpec) {
        this.MProductSpec = MProductSpec;
    }

	public List<TDemandPowerForecastDay> getTDemandPowerForecastDays() {
		return this.TDemandPowerForecastDays;
	}

	public void setTDemandPowerForecastDays(List<TDemandPowerForecastDay> TDemandPowerForecastDays) {
		this.TDemandPowerForecastDays = TDemandPowerForecastDays;
	}

	public TDemandPowerForecastDay addTDemandPowerForecastDay(TDemandPowerForecastDay TDemandPowerForecastDay) {
		getTDemandPowerForecastDays().add(TDemandPowerForecastDay);
		TDemandPowerForecastDay.setMSmPrm(this);

		return TDemandPowerForecastDay;
	}

	public TDemandPowerForecastDay removeTDemandPowerForecastDay(TDemandPowerForecastDay TDemandPowerForecastDay) {
		getTDemandPowerForecastDays().remove(TDemandPowerForecastDay);
		TDemandPowerForecastDay.setMSmPrm(null);

		return TDemandPowerForecastDay;
	}

	public List<TDemandPowerForecastTime> getTDemandPowerForecastTimes() {
		return this.TDemandPowerForecastTimes;
	}

	public void setTDemandPowerForecastTimes(List<TDemandPowerForecastTime> TDemandPowerForecastTimes) {
		this.TDemandPowerForecastTimes = TDemandPowerForecastTimes;
	}

	public TDemandPowerForecastTime addTDemandPowerForecastTime(TDemandPowerForecastTime TDemandPowerForecastTime) {
		getTDemandPowerForecastTimes().add(TDemandPowerForecastTime);
		TDemandPowerForecastTime.setMSmPrm(this);

		return TDemandPowerForecastTime;
	}

	public TDemandPowerForecastTime removeTDemandPowerForecastTime(TDemandPowerForecastTime TDemandPowerForecastTime) {
		getTDemandPowerForecastTimes().remove(TDemandPowerForecastTime);
		TDemandPowerForecastTime.setMSmPrm(null);

		return TDemandPowerForecastTime;
	}

    public List<TEventControlLog> getTEventControlLogs() {
        return this.TEventControlLogs;
    }

    public void setTEventControlLogs(List<TEventControlLog> TEventControlLogs) {
        this.TEventControlLogs = TEventControlLogs;
    }

    public TEventControlLog addTEventControlLog(TEventControlLog TEventControlLog) {
        getTEventControlLogs().add(TEventControlLog);
        TEventControlLog.setMSmPrm(this);

        return TEventControlLog;
    }

    public TEventControlLog removeTEventControlLog(TEventControlLog TEventControlLog) {
        getTEventControlLogs().remove(TEventControlLog);
        TEventControlLog.setMSmPrm(null);

        return TEventControlLog;
    }

    public List<TLoadControlLog> getTLoadControlLogs() {
        return this.TLoadControlLogs;
    }

    public void setTLoadControlLogs(List<TLoadControlLog> TLoadControlLogs) {
        this.TLoadControlLogs = TLoadControlLogs;
    }

    public TLoadControlLog addTLoadControlLog(TLoadControlLog TLoadControlLog) {
        getTLoadControlLogs().add(TLoadControlLog);
        TLoadControlLog.setMSmPrm(this);

        return TLoadControlLog;
    }

    public TLoadControlLog removeTLoadControlLog(TLoadControlLog TLoadControlLog) {
        getTLoadControlLogs().remove(TLoadControlLog);
        TLoadControlLog.setMSmPrm(null);

        return TLoadControlLog;
    }

    public List<TTempHumidControlLog> getTTempHumidControlLogs() {
        return this.TTempHumidControlLogs;
    }

    public void setTTempHumidControlLogs(List<TTempHumidControlLog> TTempHumidControlLogs) {
        this.TTempHumidControlLogs = TTempHumidControlLogs;
    }

    public TTempHumidControlLog addTTempHumidControlLog(TTempHumidControlLog TTempHumidControlLog) {
        getTTempHumidControlLogs().add(TTempHumidControlLog);
        TTempHumidControlLog.setMSmPrm(this);

        return TTempHumidControlLog;
    }

    public TTempHumidControlLog removeTTempHumidControlLog(TTempHumidControlLog TTempHumidControlLog) {
        getTTempHumidControlLogs().remove(TTempHumidControlLog);
        TTempHumidControlLog.setMSmPrm(null);

        return TTempHumidControlLog;
    }

    public TSmConnectStatus getTSmConnectStatus() {
        return this.TSmConnectStatus;
    }

    public void setTSmConnectStatus(TSmConnectStatus TSmConnectStatus) {
        this.TSmConnectStatus = TSmConnectStatus;
    }

    public List<TSmControlScheduleLog> getTSmControlScheduleLogs() {
        return this.TSmControlScheduleLogs;
    }

    public void setTSmControlScheduleLogs(List<TSmControlScheduleLog> TSmControlScheduleLogs) {
        this.TSmControlScheduleLogs = TSmControlScheduleLogs;
    }

    public TSmControlScheduleLog addTSmControlScheduleLog(TSmControlScheduleLog TSmControlScheduleLog) {
        getTSmControlScheduleLogs().add(TSmControlScheduleLog);
        TSmControlScheduleLog.setMSmPrm(this);

        return TSmControlScheduleLog;
    }

    public TSmControlScheduleLog removeTSmControlScheduleLog(TSmControlScheduleLog TSmControlScheduleLog) {
        getTSmControlScheduleLogs().remove(TSmControlScheduleLog);
        TSmControlScheduleLog.setMSmPrm(null);

        return TSmControlScheduleLog;
    }

    public List<TLoadControlResult> getTLoadControlResults() {
        return this.TLoadControlResults;
    }

    public void setTLoadControlResults(List<TLoadControlResult> TLoadControlResults) {
        this.TLoadControlResults = TLoadControlResults;
    }

    public TLoadControlResult addTLoadControlResult(TLoadControlResult TLoadControlResult) {
        getTLoadControlResults().add(TLoadControlResult);
        TLoadControlResult.setMSmPrm(this);

        return TLoadControlResult;
    }

    public TLoadControlResult removeTLoadControlResult(TLoadControlResult TLoadControlResult) {
        getTLoadControlResults().remove(TLoadControlResult);
        TLoadControlResult.setMSmPrm(null);

        return TLoadControlResult;
    }
    
    public TSmAlarmCall getTSmAlarmCall() {
		return this.TSmAlarmCall;
	}

	public void setTSmAlarmCall(TSmAlarmCall TSmAlarmCall) {
		this.TSmAlarmCall = TSmAlarmCall;
	}

    public List<TSmControlHolidayLog> getTSmControlHolidayLogs() {
        return this.TSmControlHolidayLogs;
    }

    public void setTSmControlHolidayLogs(List<TSmControlHolidayLog> TSmControlHolidayLogs) {
        this.TSmControlHolidayLogs = TSmControlHolidayLogs;
    }

    public TSmControlHolidayLog addTSmControlHolidayLog(TSmControlHolidayLog TSmControlHolidayLog) {
        getTSmControlHolidayLogs().add(TSmControlHolidayLog);
        TSmControlHolidayLog.setMSmPrm(this);

        return TSmControlHolidayLog;
    }

    public TSmControlHolidayLog removeTSmControlHolidayLog(TSmControlHolidayLog TSmControlHolidayLog) {
        getTSmControlHolidayLogs().remove(TSmControlHolidayLog);
        TSmControlHolidayLog.setMSmPrm(null);

        return TSmControlHolidayLog;
    }

}