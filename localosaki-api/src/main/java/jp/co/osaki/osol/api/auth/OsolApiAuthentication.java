package jp.co.osaki.osol.api.auth;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiOperationParameter;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.bean.apikeyissue.ApiKeyCreateBean;
import jp.co.osaki.osol.api.bean.apikeyissue.ApiKeyReCreateBean;
import jp.co.osaki.osol.api.bean.building.AllBuildingListBean;
import jp.co.osaki.osol.api.bean.building.GroupBuildingListBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllDayReportListBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllMonthReportListBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryBuildingMaxValueDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryBuildingReductionInnovateDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryBuildingReductionLastDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryBuildingUsedDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerDayReductionAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerDayUsedAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerMonthReductionAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerMonthUsedAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllSummaryPowerWeekUsedAnalysisBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllWeekReportListBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandAllYearReportListBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgAiPredictionTodaySelectBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgAiPredictionWeekSelectBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgBuildingTenantInfoBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgDayReportListLineMaxValRankingBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgDayReportListLineUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgDayReportListLineUsedCompareBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgDayReportListLineUsedSummaryBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgDayReportListMaxDemandBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgMonthReportListLineMaxValBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgMonthReportListLineUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgMonthReportListLineUsedCompareBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgMonthReportListMaxDemandBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgMonthReportListTimeZoneUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgSummaryMeasurementDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgSummaryThisMonthResultBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgSummaryUsedDataBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgWeekReportListLineMaxValBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgWeekReportListLineUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgWeekReportListLineUsedCompareBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgWeekReportListTimeZoneUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgYearReportListLineMaxValBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgYearReportListLineUsedBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgYearReportListLineUsedCompareBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgYearReportListMaxDemandBean;
import jp.co.osaki.osol.api.bean.energy.ems.DemandOrgYearReportListTimeZoneUsedBean;
import jp.co.osaki.osol.api.bean.energy.setting.DemandGraphElementListBean;
import jp.co.osaki.osol.api.bean.energy.setting.DemandGraphSettingListBean;
import jp.co.osaki.osol.api.bean.energy.setting.LineGroupSearchBean;
import jp.co.osaki.osol.api.bean.energy.setting.LineListBean;
import jp.co.osaki.osol.api.bean.energy.verify.DemandOrgDailyControlLogBean;
import jp.co.osaki.osol.api.bean.energy.verify.DemandOrgDailyKensyoDataBean;
import jp.co.osaki.osol.api.bean.energy.verify.DemandOrgMonthlyKensyoDataBean;
import jp.co.osaki.osol.api.bean.energy.verify.KensyoDemandYearBean;
import jp.co.osaki.osol.api.bean.energy.verify.KensyoEventMonthBean;
import jp.co.osaki.osol.api.bean.energy.verify.KensyoEventYearBean;
import jp.co.osaki.osol.api.bean.grouping.ChildGroupListBean;
import jp.co.osaki.osol.api.bean.grouping.ParentGroupListBean;
import jp.co.osaki.osol.api.bean.osolapi.OsolApiServerDateTimeBean;
import jp.co.osaki.osol.api.bean.signage.SignageApkLoginBean;
import jp.co.osaki.osol.api.bean.smcontrol.AddBasicFVPaSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AddBasicFVPaUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AiSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AiUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterAreaConfigSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterAreaConfigUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterDiscomfortIndexStandardSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterDiscomfortIndexStandardUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterScheduleSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterScheduleUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterStoreConfigSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AielMasterStoreConfigUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.AnswerBackSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.AnswerBackUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.CircuitErrorHistRS485SelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.CommunicationOpModeSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.CommunicationOpModeUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.ContinuousMonitorringSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DayReportFeederSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DayReportSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DayReportSrcSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandOptionSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandReportSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandSelectEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.DemandUpdateEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.DeviceInfoSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.EventCtrlHistFVPSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.FVxManageInfoSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.FVxManageInfoUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.HalfHourReportFeederSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.HalfHourReportSrcSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.LightingOptionSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.LoadCtrlHistSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.LoadCtrlResultSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.ManualLoadCtrlSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.ManualLoadCtrlUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.MaxDemandSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.MeasurePointSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.MeasurePointUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.NationalHolidaySelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.NationalHolidayUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.OutputPointSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.OutputPointUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.OutputTerminalCtrlSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.OutputTerminalCtrlUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.RadioWaveStateFOMASelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.SchedulePatternSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.SchedulePatternUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.ScheduleSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.ScheduleSelectEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.ScheduleUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.ScheduleUpdateEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.SettingEventCtrlSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.SettingEventCtrlUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.SmartLedzInfoSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.SmartLedzInfoUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.TemperatureCtrlHistSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.TemperatureCtrlSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.TemperatureCtrlUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.TemperatureLoggerHistSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.UnitSelectBean;
import jp.co.osaki.osol.api.bean.smcontrol.UnitSelectEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.UnitUpdateBean;
import jp.co.osaki.osol.api.bean.smcontrol.UnitUpdateEaBean;
import jp.co.osaki.osol.api.bean.smcontrol.WirelessDeviceInfoSelectBean;
import jp.co.osaki.osol.api.bean.smoperation.SmOperationBuildingSearchBean;
import jp.co.osaki.osol.api.bean.sms.collect.dataview.ListSmsDailyDataBean;
import jp.co.osaki.osol.api.bean.sms.collect.dataview.ListSmsDataViewBean;
import jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataBean;
import jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataSearchBean;
import jp.co.osaki.osol.api.bean.sms.collect.setting.meter.ListSmsMeterDataBean;
import jp.co.osaki.osol.api.bean.sms.collect.setting.meter.ListSmsMeterSearchBean;
import jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice.ListSmsBuildingBean;
import jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice.ListSmsDevRelationBean;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * API 認証クラス
 *
 * @author take_suzuki
 */
public final class OsolApiAuthentication {

    /**
     * OSOL内部のAPIキー管理（キー値はAPIキー）
     */
    private static final Map<String, OsolApiPrivateAuthInfo> privateApiAuthMap = new HashMap<>();

    /**
     * APIキーチェックなしリスト
     */
    static final String[] apiKeyNonCheckApiBeanList = {
            OsolApiServerDateTimeBean.class.getSimpleName(),
            ApiKeyReCreateBean.class.getSimpleName()
    };

    /**
     * 外部アクセス不可APIリスト
     */
    static final String[] publicBlockApiBeanList = {
            ApiKeyCreateBean.class.getSimpleName()
    };

    /**
     * 外部アクセス可能APIリスト
     */
    static final String[] publicAccessApiBeanList = {
            AllBuildingListBean.class.getSimpleName(),
            GroupBuildingListBean.class.getSimpleName(),
            ParentGroupListBean.class.getSimpleName(),
            ChildGroupListBean.class.getSimpleName(),
            DemandAllSummaryPowerDayUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerDayReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerWeekUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerWeekReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerMonthUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerMonthReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryBuildingUsedDataBean.class.getSimpleName(),
            DemandAllSummaryBuildingMaxValueDataBean.class.getSimpleName(),
            DemandAllSummaryBuildingReductionLastDataBean.class.getSimpleName(),
            DemandAllWeekReportListBean.class.getSimpleName(),
            DemandAllMonthReportListBean.class.getSimpleName(),
            DemandOrgBuildingTenantInfoBean.class.getSimpleName(),
            DemandOrgSummaryThisMonthResultBean.class.getSimpleName(),
            DemandOrgSummaryMeasurementDataBean.class.getSimpleName(),
            DemandOrgSummaryUsedDataBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedSummaryBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgDayReportListMaxDemandBean.class.getSimpleName(),
            DemandOrgYearReportListLineUsedBean.class.getSimpleName(),
            DemandOrgYearReportListTimeZoneUsedBean.class.getSimpleName(),
            DemandOrgYearReportListLineMaxValBean.class.getSimpleName(),
            DemandOrgYearReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgYearReportListMaxDemandBean.class.getSimpleName(),
            LineGroupSearchBean.class.getSimpleName(),
            LineListBean.class.getSimpleName(),
            DemandGraphSettingListBean.class.getSimpleName(),
            DemandGraphElementListBean.class.getSimpleName(),
            DemandOrgDailyControlLogBean.class.getSimpleName(),
            SignageApkLoginBean.class.getSimpleName(),
            ListSmsBuildingBean.class.getSimpleName(),               //SMS_API外部公開
            ListSmsDevRelationBean.class.getSimpleName(),            //SMS_API外部公開
            ListSmsMeterSearchBean.class.getSimpleName(),            //SMS_API外部公開
            ListSmsMeterDataBean.class.getSimpleName(),              //SMS_API外部公開
            ListSmsDataViewBean.class.getSimpleName(),               //SMS_API外部公開
            ListSmsDailyDataBean.class.getSimpleName(),              //SMS_API外部公開
            ListSmsMeterReadingDataSearchBean.class.getSimpleName(), //SMS_API外部公開
            ListSmsMeterInspectionDataBean.class.getSimpleName()  //SMS_API外部公開
    };

    /**
     * パートナー公開APIリスト
     * 外部アクセス可能APIリスト
     */
    static final String[] partnerAccessApiBeanList = {
            AllBuildingListBean.class.getSimpleName(),
            GroupBuildingListBean.class.getSimpleName(),
            ParentGroupListBean.class.getSimpleName(),
            ChildGroupListBean.class.getSimpleName(),
            DemandAllSummaryPowerDayUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerDayReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerWeekUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerWeekReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerMonthUsedAnalysisBean.class.getSimpleName(),
            DemandAllSummaryPowerMonthReductionAnalysisBean.class.getSimpleName(),
            DemandAllSummaryBuildingUsedDataBean.class.getSimpleName(),
            DemandAllSummaryBuildingMaxValueDataBean.class.getSimpleName(),
            DemandAllSummaryBuildingReductionLastDataBean.class.getSimpleName(),
            DemandAllSummaryBuildingReductionInnovateDataBean.class.getSimpleName(),
            DemandAllDayReportListBean.class.getSimpleName(),
            DemandAllWeekReportListBean.class.getSimpleName(),
            DemandAllMonthReportListBean.class.getSimpleName(),
            DemandAllYearReportListBean.class.getSimpleName(),
            DemandOrgBuildingTenantInfoBean.class.getSimpleName(),
            DemandOrgSummaryThisMonthResultBean.class.getSimpleName(),
            DemandOrgSummaryMeasurementDataBean.class.getSimpleName(),
            DemandOrgSummaryUsedDataBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedSummaryBean.class.getSimpleName(),
            DemandOrgDayReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgDayReportListMaxDemandBean.class.getSimpleName(),
            DemandOrgWeekReportListLineUsedBean.class.getSimpleName(),
            DemandOrgWeekReportListTimeZoneUsedBean.class.getSimpleName(),
            DemandOrgWeekReportListLineMaxValBean.class.getSimpleName(),
            DemandOrgWeekReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgMonthReportListLineUsedBean.class.getSimpleName(),
            DemandOrgMonthReportListTimeZoneUsedBean.class.getSimpleName(),
            DemandOrgMonthReportListLineMaxValBean.class.getSimpleName(),
            DemandOrgMonthReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgMonthReportListMaxDemandBean.class.getSimpleName(),
            DemandOrgYearReportListLineUsedBean.class.getSimpleName(),
            DemandOrgYearReportListTimeZoneUsedBean.class.getSimpleName(),
            DemandOrgYearReportListLineMaxValBean.class.getSimpleName(),
            DemandOrgYearReportListLineUsedCompareBean.class.getSimpleName(),
            DemandOrgYearReportListMaxDemandBean.class.getSimpleName(),
            DemandOrgDayReportListLineMaxValRankingBean.class.getSimpleName(),
            DemandOrgAiPredictionTodaySelectBean.class.getSimpleName(),
            DemandOrgAiPredictionWeekSelectBean.class.getSimpleName(),
            LineGroupSearchBean.class.getSimpleName(),
            LineListBean.class.getSimpleName(),
            DemandOrgDailyKensyoDataBean.class.getSimpleName(),
            DemandOrgMonthlyKensyoDataBean.class.getSimpleName(),
            KensyoEventMonthBean.class.getSimpleName(),
            KensyoDemandYearBean.class.getSimpleName(),
            KensyoEventYearBean.class.getSimpleName(),
            DemandOrgDailyControlLogBean.class.getSimpleName(),
            SmOperationBuildingSearchBean.class.getSimpleName(),
            TemperatureCtrlSelectBean.class.getSimpleName(),
            TemperatureCtrlUpdateBean.class.getSimpleName(),
            ScheduleSelectBean.class.getSimpleName(),
            ScheduleUpdateBean.class.getSimpleName(),
            DemandSelectBean.class.getSimpleName(),
            DemandUpdateBean.class.getSimpleName(),
            ManualLoadCtrlSelectBean.class.getSimpleName(),
            ManualLoadCtrlUpdateBean.class.getSimpleName(),
            OutputTerminalCtrlSelectBean.class.getSimpleName(),
            OutputTerminalCtrlUpdateBean.class.getSimpleName(),
            UnitSelectBean.class.getSimpleName(),
            UnitUpdateBean.class.getSimpleName(),
            FVxManageInfoSelectBean.class.getSimpleName(),
            FVxManageInfoUpdateBean.class.getSimpleName(),
            CommunicationOpModeSelectBean.class.getSimpleName(),
            CommunicationOpModeUpdateBean.class.getSimpleName(),
            AddBasicFVPaSelectBean.class.getSimpleName(),
            AddBasicFVPaUpdateBean.class.getSimpleName(),
            SettingEventCtrlSelectBean.class.getSimpleName(),
            SettingEventCtrlUpdateBean.class.getSimpleName(),
            AnswerBackSelectBean.class.getSimpleName(),
            AnswerBackUpdateBean.class.getSimpleName(),
            LoadCtrlHistSelectBean.class.getSimpleName(),
            TemperatureCtrlHistSelectBean.class.getSimpleName(),
            DemandReportSelectBean.class.getSimpleName(),
            DemandOptionSelectBean.class.getSimpleName(),
            ContinuousMonitorringSelectBean.class.getSimpleName(),
            WirelessDeviceInfoSelectBean.class.getSimpleName(),
            RadioWaveStateFOMASelectBean.class.getSimpleName(),
            DeviceInfoSelectBean.class.getSimpleName(),
            EventCtrlHistFVPSelectBean.class.getSimpleName(),
            CircuitErrorHistRS485SelectBean.class.getSimpleName(),
            TemperatureLoggerHistSelectBean.class.getSimpleName(),
            LoadCtrlResultSelectBean.class.getSimpleName(),
            DayReportSelectBean.class.getSimpleName(),
            MaxDemandSelectBean.class.getSimpleName(),
            LightingOptionSelectBean.class.getSimpleName(),
            DemandSelectEaBean.class.getSimpleName(),
            DemandUpdateEaBean.class.getSimpleName(),
            NationalHolidaySelectBean.class.getSimpleName(),
            NationalHolidayUpdateBean.class.getSimpleName(),
            UnitSelectEaBean.class.getSimpleName(),
            UnitUpdateEaBean.class.getSimpleName(),
            MeasurePointSelectBean.class.getSimpleName(),
            MeasurePointUpdateBean.class.getSimpleName(),
            SmartLedzInfoSelectBean.class.getSimpleName(),
            SmartLedzInfoUpdateBean.class.getSimpleName(),
            ScheduleSelectEaBean.class.getSimpleName(),
            ScheduleUpdateEaBean.class.getSimpleName(),
            SchedulePatternSelectBean.class.getSimpleName(),
            SchedulePatternUpdateBean.class.getSimpleName(),
            DayReportSrcSelectBean.class.getSimpleName(),
            DayReportFeederSelectBean.class.getSimpleName(),
            HalfHourReportSrcSelectBean.class.getSimpleName(),
            HalfHourReportFeederSelectBean.class.getSimpleName(),
            AiSelectBean.class.getSimpleName(),
            AiUpdateBean.class.getSimpleName(),
            AielMasterScheduleSelectBean.class.getSimpleName(),
            AielMasterScheduleUpdateBean.class.getSimpleName(),
            AielMasterStoreConfigSelectBean.class.getSimpleName(),
            AielMasterStoreConfigUpdateBean.class.getSimpleName(),
            AielMasterAreaConfigSelectBean.class.getSimpleName(),
            AielMasterAreaConfigUpdateBean.class.getSimpleName(),
            OutputPointSelectBean.class.getSimpleName(),
            OutputPointUpdateBean.class.getSimpleName(),
            AielMasterDiscomfortIndexStandardSelectBean.class.getSimpleName(),
            AielMasterDiscomfortIndexStandardUpdateBean.class.getSimpleName(),
            ListSmsBuildingBean.class.getSimpleName(),               //SMS_API外部公開
            ListSmsDevRelationBean.class.getSimpleName(),            //SMS_API外部公開
            ListSmsMeterSearchBean.class.getSimpleName(),            //SMS_API外部公開
            ListSmsMeterDataBean.class.getSimpleName(),              //SMS_API外部公開
            ListSmsDataViewBean.class.getSimpleName(),               //SMS_API外部公開
            ListSmsDailyDataBean.class.getSimpleName(),              //SMS_API外部公開
            ListSmsMeterReadingDataSearchBean.class.getSimpleName(),  //SMS_API外部公開
            ListSmsMeterInspectionDataBean.class.getSimpleName()  //SMS_API外部公開
    };

    /**
     * OSOL内部用APIキーをMapにセットする
     *
     * @param apiKey
     * @param svDate
     */
    public static void setPrivateApiKeyMap(String apiKey, Timestamp svDate) {

        OsolApiPrivateAuthInfo osolApiPrivateAuthInfo = new OsolApiPrivateAuthInfo(apiKey, svDate,
                new Timestamp(DateUtility.plusMinute(new Date(svDate.getTime()),
                        OsolConstants.API_PRIVATE_KEY_LIFE_TIME).getTime()));

        privateApiAuthMap.put(apiKey, osolApiPrivateAuthInfo);
    }

    /**
     * OSOL内部APIの権限チェックを行う
     *
     * @param parameter
     * @param svDate
     * @param testFlg
     * @return
     */
    public static String checkApiAuthForPrivate(OsolApiParameter parameter, Timestamp svDate, boolean testFlg) {

        //ハッシュ値のチェックを行う
        if (!testFlg) {
            if (!checkAuthHash(parameter, svDate)) {
                return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_HASH_ILLEGAL;
            }
        }

        //APIキーのチェックを行う
        return checkApiKeyForPrivate(parameter, svDate);
    }

    /**
     * OSOL外部APIの権限チェックを行う
     *
     * @param parameter
     * @param svDate
     * @param tApiKey
     * @return
     */
    public static String checkApiAuthForPublic(OsolApiParameter parameter, Timestamp svDate, TApiKey tApiKey, MPerson mPerson) {

        String strBean = "Bean";

        // 企業種別
        String corpType = mPerson.getMCorp().getCorpType();

        // 大崎電気工業
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(corpType)) {
            //APIの権限チェックを行う
            if (Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean() + strBean)) {
                // 外部アクセス可ではない
                return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
            }
        }
        // パートナー
        else if(OsolConstants.CORP_TYPE.PARTNER.getVal().equals(corpType)) {
            //APIの権限チェックを行う（APIキーチェックリストに含まれていない、かつパートナー公開APIリストに含まれていない）
            if(!(Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean() + strBean))
                    && !(Arrays.asList(partnerAccessApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(partnerAccessApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(partnerAccessApiBeanList).contains(parameter.getBean() + strBean))
                    ) {
                //外部アクセス可ではない
                return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
            } else if (Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(publicBlockApiBeanList).contains(parameter.getBean() + strBean)) {
                // 外部アクセス不可APIリストに含まれている場合、外部アクセス可ではない
                return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
            }
        }

        //APIキーのチェックを行う
        return checkApiKeyForPublic(parameter, svDate, tApiKey);

    }

    /**
     * OSOL内部アクセスAPIキーのチェックを行う
     *
     * @param parameter
     * @param svDate
     * @return
     */
    private static String checkApiKeyForPrivate(OsolApiParameter parameter, Timestamp svDate) {

        String strBean = "Bean";
        if ((Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean() + strBean))) {
            //APIキーチェック不要
            return OsolApiResultCode.API_OK;
        }

        if (CheckUtility.isNullOrEmpty(parameter.getApiKey())) {
            //APIキーが設定されていない
            return OsolApiResultCode.API_ERROR_PARAMETER_GET;
        }

        OsolApiPrivateAuthInfo authInfo = privateApiAuthMap.get(parameter.getApiKey());
        if (authInfo == null) {
            //Mapに存在しない
            return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
        }

        //APIキーの期限切れをチェックする
        if (svDate.after(authInfo.getApiKeyLimitTime())) {
            //期限切れのキーをMapから削除する
            privateApiAuthMap.remove(parameter.getApiKey());
            return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_EXPIRED;
        }

        //チェックOK
        return OsolApiResultCode.API_OK;
    }

    /**
     * OSOL外部アクセスAPIキーのチェック
     *
     * @param parameter
     * @param svDate
     * @param tApiKey
     * @return
     */
    private static String checkApiKeyForPublic(OsolApiParameter parameter, Timestamp svDate, TApiKey tApiKey) {

        String strBean = "Bean";
        if ((Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean()) ||  Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean().replace(strBean, "")) || Arrays.asList(apiKeyNonCheckApiBeanList).contains(parameter.getBean() + strBean))) {
            //APIキーチェック不要
            return OsolApiResultCode.API_OK;
        }

        if (CheckUtility.isNullOrEmpty(parameter.getApiKey())) {
            //APIキーが設定されていない
            return OsolApiResultCode.API_ERROR_PARAMETER_GET;
        }

        if (tApiKey == null) {
            //APIキー情報が存在しない
            return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
        }

        if (!tApiKey.getApiKey().equals(parameter.getApiKey())) {
            //APIキーが一致しない
            return OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_ILLEGAL;
        }

        if (svDate.after(tApiKey.getExpirationDate())) {
            //期限切れ
            return (OsolApiResultCode.API_ERROR_AUTHENTICATION_API_KEY_EXPIRED);
        }

        //チェックOK
        return OsolApiResultCode.API_OK;

    }

    /**
     * パラメータの入力チェック
     *
     * @param parameter
     * @return
     */
    public static boolean checkParameter(OsolApiOperationParameter parameter) {

        if (CheckUtility.isNullOrEmpty(parameter.getBean())
                || CheckUtility.isNullOrEmpty(parameter.getLoginCorpId())
                || CheckUtility.isNullOrEmpty(parameter.getLoginPersonId())
                || CheckUtility.isNullOrEmpty(parameter.getOperationCorpId())) {
            return false;
        }

        return true;
    }

    /**
     * ハッシュ値のチェックを行う
     *
     * @param parameter
     * @param svDate
     * @return
     */
    private static boolean checkAuthHash(OsolApiOperationParameter parameter, Timestamp svDate) {

        //サーバー時刻取得APIの場合はハッシュのチェックは行わない
        if (OsolApiServerDateTimeBean.class.getSimpleName().equals(parameter.getBean())) {
            //ハッシュ値チェックOK
            return true;
        }

        //ハッシュ値チェック
        return OsolApiAuthUtil.checkAuthHash(
                parameter.getLoginCorpId(), parameter.getLoginPersonId(),
                parameter.getMillisec().toString(), parameter.getAuthHash());
    }

    /**
     * パートナー公開APIリストを返却
     *
     * @return パートナー公開APIリスト（パートナー企業向け外部公開用）
     */
    public static String[] getPartnerAccessApiBeanList() {
        return partnerAccessApiBeanList;
    }

}
