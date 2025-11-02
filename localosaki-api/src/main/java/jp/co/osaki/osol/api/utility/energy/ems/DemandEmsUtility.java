package jp.co.osaki.osol.api.utility.energy.ems;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayLoadListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayMaxListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CurrentWeekReportResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.DailyUsedTargetResult;
import jp.co.osaki.osol.api.result.utility.MonthlyTargetUsedPowerResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearMonthNoResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearSummaryRangeResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearWeekNoResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage;
import jp.co.osaki.osol.entity.MCoefficientHistoryManagePK;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * EMS全体のUtilityクラス
 * @author ya-ishida
 *
 */
public class DemandEmsUtility {

    /**
     * 企業デマンド取得のパラメータを設定する
     * @param corpId
     * @return
     */
    public static CorpDemandSelectResult getCorpDemandParam(String corpId) {
        CorpDemandSelectResult param = new CorpDemandSelectResult();
        param.setCorpId(corpId);
        return param;
    }

    /**
     * 建物デマンド一覧取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @return
     */
    public static BuildingDemandListDetailResultData getBuildingDemandListParam(String corpId, Long buildingId) {
        BuildingDemandListDetailResultData param = new BuildingDemandListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        return param;
    }

    /**
     * 系統一覧取得のパラメータを設定する
     * @param corpId
     * @param lineGroupId
     * @param lineNo
     * @param lineEnableFlg
     * @return
     */
    public static LineListDetailResultData getLineListParam(String corpId, Long lineGroupId, String lineNo,
            Integer lineEnableFlg) {
        LineListDetailResultData param = new LineListDetailResultData();
        param.setCorpId(corpId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setLineEnableFlg(lineEnableFlg);
        return param;
    }

    /**
     * 建物系統時限標準値取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @return
     */
    public static BuildingLineTimeStandardListTimeResultData getBuildingLineTimeStandardListParam(String corpId,
            Long buildingId, Long lineGroupId, String lineNo) {
        BuildingLineTimeStandardListTimeResultData param = new BuildingLineTimeStandardListTimeResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        return param;
    }

    /**
     * 機器情報取得のパラメータを設定する
     * @param smId
     * @return
     */
    public static SmSelectResult getSmSelectParam(Long smId) {
        SmSelectResult param = new SmSelectResult();
        param.setSmId(smId);
        return param;
    }

    /**
     * 機器ポイント情報取得のパラメータを設定する
     * @param smId
     * @param pointNo
     * @return
     */
    public static SmPointListDetailResultData getSmPointListParam(Long smId, String pointNo) {
        SmPointListDetailResultData param = new SmPointListDetailResultData();
        param.setSmId(smId);
        param.setPointNo(pointNo);
        return param;
    }

    /**
     * 建物機器一覧取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @return
     */
    public static DemandBuildingSmListDetailResultData getBuildingSmListParam(String corpId, Long buildingId,
            Long smId) {
        DemandBuildingSmListDetailResultData param = new DemandBuildingSmListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        return param;
    }

    /**
     * 建物機器ポイント一覧取得のパラメータを設定する
     * @param corpId
     * @param buidingId
     * @param smId
     * @param pointNo
     * @return
     */
    public static DemandBuildingSmPointListDetailResultData getBuildingSmPointListParam(String corpId, Long buildingId,
            Long smId, String pointNo) {
        DemandBuildingSmPointListDetailResultData param = new DemandBuildingSmPointListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setPointNo(pointNo);
        return param;
    }

    /**
     * グラフ要素情報設定取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param graphId
     * @param graphElementId
     * @return
     */
    public static DemandGraphElementListDetailResultData getGraphElementListParam(String corpId, Long buildingId,
            Long lineGroupId, Long graphId, Long graphElementId) {
        DemandGraphElementListDetailResultData param = new DemandGraphElementListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setGraphId(graphId);
        param.setGraphElementId(graphElementId);
        return param;
    }

    /**
     * デマンド日報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @return
     */
    public static CommonDemandDayReportListResult getDayReportListParam(String corpId, Long buildingId,
            Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo, BigDecimal jigenNoTo) {
        CommonDemandDayReportListResult param = new CommonDemandDayReportListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setJigenNoFrom(jigenNoFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setJigenNoTo(jigenNoTo);
        return param;
    }

    /**
     * デマンド日報系統取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @return
     */
    public static CommonDemandDayReportLineListResult getDayReportLineListParam(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo,
            BigDecimal jigenNoTo) {
        CommonDemandDayReportLineListResult param = new CommonDemandDayReportLineListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setJigenNoFrom(jigenNoFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setJigenNoTo(jigenNoTo);
        return param;
    }

    /**
     * デマンド日報ポイント取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @param pointNoFrom
     * @param pointNoTo
     * @return
     */
    public static CommonDemandDayReportPointListResult getDayReportPointListParam(String corpId, Long buildingId,
            Long smId, Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo, BigDecimal jigenNoTo,
            String pointNoFrom, String pointNoTo) {
        CommonDemandDayReportPointListResult param = new CommonDemandDayReportPointListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setJigenNoFrom(jigenNoFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setJigenNoTo(jigenNoTo);
        param.setPointNoFrom(pointNoFrom);
        param.setPointNoTo(pointNoTo);
        return param;
    }

    /**
     * デマンド週報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param summaryUnit
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @return
     */
    public static CommonDemandWeekReportListResult getWeekReportListParam(String corpId, Long buildingId,
            String summaryUnit, String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom,
            BigDecimal weekNoTo) {
        CommonDemandWeekReportListResult param = new CommonDemandWeekReportListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSummaryUnit(summaryUnit);
        param.setFiscalYearFrom(fiscalYearFrom);
        param.setFiscalYearTo(fiscalYearTo);
        param.setWeekNoFrom(weekNoFrom);
        param.setWeekNoTo(weekNoTo);
        return param;
    }

    /**
     * デマンド週報系統取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param summaryUnit
     * @param lineGroupId
     * @param lineNo
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @return
     */
    public static CommonDemandWeekReportLineListResult getWeekReportLineListParam(String corpId, Long buildingId,
            String summaryUnit, Long lineGroupId, String lineNo, String fiscalYearFrom, String fiscalYearTo,
            BigDecimal weekNoFrom, BigDecimal weekNoTo) {

        CommonDemandWeekReportLineListResult param = new CommonDemandWeekReportLineListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSummaryUnit(summaryUnit);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setFiscalYearFrom(fiscalYearFrom);
        param.setFiscalYearTo(fiscalYearTo);
        param.setWeekNoFrom(weekNoFrom);
        param.setWeekNoTo(weekNoTo);
        return param;
    }

    /**
     * デマンド週報ポイント取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param summaryUnit
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @param pointNoFrom
     * @param pointNoTo
     * @return
     */
    public static CommonDemandWeekReportPointListResult getWeekReportPointListParam(String corpId, Long buildingId,
            Long smId, String summaryUnit, String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom,
            BigDecimal weekNoTo, String pointNoFrom, String pointNoTo) {
        CommonDemandWeekReportPointListResult param = new CommonDemandWeekReportPointListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setSummaryUnit(summaryUnit);
        param.setFiscalYearFrom(fiscalYearFrom);
        param.setFiscalYearTo(fiscalYearTo);
        param.setWeekNoFrom(weekNoFrom);
        param.setWeekNoTo(weekNoTo);
        param.setPointNoFrom(pointNoFrom);
        param.setPointNoTo(pointNoTo);
        return param;
    }

    /**
     * デマンド月報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param measurementDateFrom
     * @param measurementDateTo
     * @return
     */
    public static CommonDemandMonthReportListResult getMonthReportListParam(String corpId, Long buildingId,
            Date measurementDateFrom, Date measurementDateTo) {
        CommonDemandMonthReportListResult param = new CommonDemandMonthReportListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setMeasurementDateTo(measurementDateTo);
        return param;
    }

    /**
     * デマンド月報系統取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param measurementDateFrom
     * @param measurementDateTo
     * @return
     */
    public static CommonDemandMonthReportLineListResult getMonthReportLineListParam(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, Date measurementDateFrom, Date measurementDateTo) {
        CommonDemandMonthReportLineListResult param = new CommonDemandMonthReportLineListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setMeasurementDateTo(measurementDateTo);
        return param;
    }

    /**
     * デマンド月報ポイント取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param pointNoFrom
     * @param pointNoTo
     * @return
     */
    public static CommonDemandMonthReportPointListResult getMonthReportPointListParam(String corpId, Long buildingId,
            Long smId, Date measurementDateFrom, Date measurementDateTo, String pointNoFrom, String pointNoTo) {
        CommonDemandMonthReportPointListResult param = new CommonDemandMonthReportPointListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setPointNoFrom(pointNoFrom);
        param.setPointNoTo(pointNoTo);
        return param;
    }

    /**
     * デマンド年報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param summaryUnit
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @return
     */
    public static CommonDemandYearReportListResult getYearReportListParam(String corpId, Long buildingId,
            String summaryUnit, String yearNoFrom, String yearNoTo, BigDecimal monthNoFrom, BigDecimal monthNoTo) {
        CommonDemandYearReportListResult param = new CommonDemandYearReportListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSummaryUnit(summaryUnit);
        param.setYearNoFrom(yearNoFrom);
        param.setYearNoTo(yearNoTo);
        param.setMonthNoFrom(monthNoFrom);
        param.setMonthNoTo(monthNoTo);
        return param;
    }

    /**
     * デマンド年報系統取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param summaryUnit
     * @param lineGroupId
     * @param lineNo
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @return
     */
    public static CommonDemandYearReportLineListResult getYearReportLineListParam(String corpId, Long buildingId,
            String summaryUnit, Long lineGroupId, String lineNo, String yearNoFrom, String yearNoTo,
            BigDecimal monthNoFrom, BigDecimal monthNoTo) {
        CommonDemandYearReportLineListResult param = new CommonDemandYearReportLineListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSummaryUnit(summaryUnit);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setYearNoFrom(yearNoFrom);
        param.setYearNoTo(yearNoTo);
        param.setMonthNoFrom(monthNoFrom);
        param.setMonthNoTo(monthNoTo);
        return param;
    }

    /**
     * デマンド年報ポイント取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param summaryUnit
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @param pointNoFrom
     * @param pointNoTo
     * @return
     */
    public static CommonDemandYearReportPointListResult getYearReportPointListParam(String corpId, Long buildingId,
            Long smId, String summaryUnit, String yearNoFrom, String yearNoTo, BigDecimal monthNoFrom,
            BigDecimal monthNoTo, String pointNoFrom, String pointNoTo) {
        CommonDemandYearReportPointListResult param = new CommonDemandYearReportPointListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setSummaryUnit(summaryUnit);
        param.setYearNoFrom(yearNoFrom);
        param.setYearNoTo(yearNoTo);
        param.setMonthNoFrom(monthNoFrom);
        param.setMonthNoTo(monthNoTo);
        param.setPointNoFrom(pointNoFrom);
        param.setPointNoTo(pointNoTo);
        return param;
    }

    /**
     * デマンド日最大取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param crntMinFrom
     * @param crntMinTo
     * @return
     */
    public static CommonDemandDayMaxListResult getDemandDayMaxListParam(String corpId, Long buildingId, Long smId,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal crntMinFrom, BigDecimal crntMinTo) {
        CommonDemandDayMaxListResult param = new CommonDemandDayMaxListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setCrntMinFrom(crntMinFrom);
        param.setCrntMinTo(crntMinTo);
        return param;
    }

    /**
     * デマンド日負荷取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param smId
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param crntMinFrom
     * @param crntMinTo
     * @param controlLoadFrom
     * @param controlLoadTo
     * @return
     */
    public static CommonDemandDayLoadListResult getDemandDayLoadListParam(String corpId, Long buildingId, Long smId,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal crntMinFrom, BigDecimal crntMinTo,
            BigDecimal controlLoadFrom, BigDecimal controlLoadTo) {
        CommonDemandDayLoadListResult param = new CommonDemandDayLoadListResult();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        param.setMeasurementDateFrom(measurementDateFrom);
        param.setMeasurementDateTo(measurementDateTo);
        param.setCrntMinFrom(crntMinFrom);
        param.setCrntMinTo(crntMinTo);
        param.setControlLoadFrom(controlLoadFrom);
        param.setControlLoadTo(controlLoadTo);
        return param;
    }

    /**
     * 製品制御負荷情報取得のパラメータを取得する
     * @param productCd
     * @param controlLoadFrom
     * @param controlLoadTo
     * @return
     */
    public static ProductControlLoadListDetailResultData getProductControlLoadListParam(String productCd,
            BigDecimal controlLoadFrom, BigDecimal controlLoadTo) {
        ProductControlLoadListDetailResultData param = new ProductControlLoadListDetailResultData();
        param.setProductCd(productCd);
        param.setControlLoadFrom(controlLoadFrom);
        param.setControlLoadTo(controlLoadTo);
        return param;
    }

    /**
     * 機器制御負荷情報取得のパラメータを取得する
     * @param smId
     * @param controlLoadFrom
     * @param controlLoadTo
     * @return
     */
    public static SmControlLoadListDetailResultData getSmControlLoadListParam(Long smId, BigDecimal controlLoadFrom,
            BigDecimal controlLoadTo) {
        SmControlLoadListDetailResultData param = new SmControlLoadListDetailResultData();
        param.setSmId(smId);
        param.setControlLoadFrom(controlLoadFrom);
        param.setControlLoadTo(controlLoadTo);
        return param;
    }

    /**
     * 使用エネルギー取得のパラメータを取得する
     * @param corpId
     * @param buildingId
     * @param engTypeCd
     * @param engId
     * @param contractId
     * @return
     */
    public static TAvailableEnergy getTAvailableEnergyListParam(String corpId, Long buildingId, String engTypeCd,
            Long engId, Long contractId) {
        TAvailableEnergy param = new TAvailableEnergy();
        TAvailableEnergyPK id = new TAvailableEnergyPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        id.setEngTypeCd(engTypeCd);
        id.setEngId(engId);
        id.setContractId(contractId);
        param.setId(id);
        return param;
    }

    /**
     * 係数履歴管理マスタ情報取得のパラメータを取得する
     * @param engTypeCd
     * @param engId
     * @param dayAndNightType
     * @return
     */
    public static MCoefficientHistoryManage getCoefficientHistroryListParam(String engTypeCd, Long engId,
            String dayAndNightType) {
        MCoefficientHistoryManage param = new MCoefficientHistoryManage();
        MCoefficientHistoryManagePK id = new MCoefficientHistoryManagePK();
        id.setEngTypeCd(engTypeCd);
        id.setEngId(engId);
        id.setDayAndNightType(dayAndNightType);
        param.setId(id);
        return param;
    }



    /**
     * 企業集計週報カレンダを作成する
     * @param corpInfo
     * @param serverDateTime
     * @return
     */
    public static List<DemandWeekCorpCalListDetailResultData> getWeekCorpCalList(CorpDemandSelectResult corpInfo,
            Timestamp serverDateTime) {

        List<DemandWeekCorpCalListDetailResultData> calList = new ArrayList<>();
        Integer fiscalYearFrom;
        Integer fiscalYearTo;
        Integer currentYear;

        if (corpInfo == null || serverDateTime == null) {
            return null;
        }

        //年度の範囲を取得する
        fiscalYearFrom = Integer
                .parseInt(DateUtility.changeDateFormat(DateUtility.plusYear(serverDateTime, -8),
                        DateUtility.DATE_FORMAT_YYYY));
        fiscalYearTo = Integer
                .parseInt(DateUtility.changeDateFormat(DateUtility.plusYear(serverDateTime, 1),
                        DateUtility.DATE_FORMAT_YYYY));
        currentYear = fiscalYearFrom;

        while (currentYear <= fiscalYearTo) {

            //対象年度の開始日、終了日を設定する
            Date yearStartDate = DateUtility.conversionDate(
                    String.valueOf(currentYear).concat(corpInfo.getWeekStartDay()), DateUtility.DATE_FORMAT_YYYYMMDD);
            Date yearEndDate = DateUtility.plusDay(DateUtility.plusYear(yearStartDate, 1), -1);
            Date currentDate = yearStartDate;
            Date weekStartDate;

            //第1週の締め曜日にあたる日付を探す
            while (true) {
                if (DateUtility.getDayOfWeek(currentDate) - 1 == corpInfo.getWeekClosingDayOfWeek()) {
                    //週締め曜日と一致する場合は、ループを抜ける
                    break;
                } else {
                    //それ以外は1日加算
                    currentDate = DateUtility.plusDay(currentDate, 1);
                }
            }

            //第1週の設定
            calList.add(
                    new DemandWeekCorpCalListDetailResultData(corpInfo.getCorpId(), String.valueOf(currentYear),
                            BigDecimal.ONE,
                            yearStartDate, currentDate));

            //第2週～第52週まで
            for (int i = 2; i <= 52; i++) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                weekStartDate = currentDate;
                currentDate = DateUtility.plusDay(currentDate, 6);
                calList.add(new DemandWeekCorpCalListDetailResultData(corpInfo.getCorpId(), String.valueOf(currentYear),
                        new BigDecimal(i),
                        weekStartDate, currentDate));
            }

            //第53週
            currentDate = DateUtility.plusDay(currentDate, 1);
            weekStartDate = currentDate;
            currentDate = DateUtility.plusDay(currentDate, 6);
            if (currentDate.after(yearEndDate)) {
                //年度の終了日を超えている場合
                calList.add(new DemandWeekCorpCalListDetailResultData(corpInfo.getCorpId(), String.valueOf(currentYear),
                        new BigDecimal(53),
                        weekStartDate, yearEndDate));
            } else {
                calList.add(new DemandWeekCorpCalListDetailResultData(corpInfo.getCorpId(), String.valueOf(currentYear),
                        new BigDecimal(53),
                        weekStartDate, currentDate));
            }

            //第54週
            if (currentDate.before(yearEndDate)) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                weekStartDate = currentDate;
                calList.add(new DemandWeekCorpCalListDetailResultData(corpInfo.getCorpId(), String.valueOf(currentYear),
                        new BigDecimal(54),
                        weekStartDate, yearEndDate));
            }

            currentYear = currentYear + 1;

        }

        return calList;
    }

    /**
     * 電力集計週報カレンダを作成する
     * @param buildingInfo
     * @param serverDateTime
     * @return
     */
    public final static List<DemandWeekBuildingCalListDetailResultData> getWeekBuildingCalList(
            BuildingDemandListDetailResultData buildingInfo, Timestamp serverDateTime) {

        List<DemandWeekBuildingCalListDetailResultData> calList = new ArrayList<>();

        Integer fiscalYearFrom;
        Integer fiscalYearTo;
        Integer currentYear;

        if (buildingInfo == null || serverDateTime == null) {
            return null;
        }

        //年度の範囲を取得する
        fiscalYearFrom = Integer
                .parseInt(DateUtility.changeDateFormat(DateUtility.plusYear(serverDateTime, -8),
                        DateUtility.DATE_FORMAT_YYYY));
        fiscalYearTo = Integer
                .parseInt(DateUtility.changeDateFormat(DateUtility.plusYear(serverDateTime, 1),
                        DateUtility.DATE_FORMAT_YYYY));
        currentYear = fiscalYearFrom;

        while (currentYear <= fiscalYearTo) {

            //対象年度の開始日、終了日を設定する
            Date yearStartDate = DateUtility.conversionDate(
                    String.valueOf(currentYear).concat(buildingInfo.getWeekStartDay()),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
            Date yearEndDate = DateUtility.plusDay(DateUtility.plusYear(yearStartDate, 1), -1);
            Date currentDate = yearStartDate;
            Date weekStartDate;

            //第1週の締め曜日にあたる日付を探す
            while (true) {
                if (DateUtility.getDayOfWeek(currentDate) - 1 == buildingInfo.getWeekClosingDayOfWeek()) {
                    //週締め曜日と一致する場合は、ループを抜ける
                    break;
                } else {
                    //それ以外は1日加算
                    currentDate = DateUtility.plusDay(currentDate, 1);
                }
            }

            //第1週の設定
            calList.add(new DemandWeekBuildingCalListDetailResultData(buildingInfo.getCorpId(),
                    buildingInfo.getBuildingId(),
                    String.valueOf(currentYear),
                    BigDecimal.ONE, yearStartDate, currentDate));

            //第2週～第52週まで
            for (int i = 2; i <= 52; i++) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                weekStartDate = currentDate;
                currentDate = DateUtility.plusDay(currentDate, 6);
                calList.add(new DemandWeekBuildingCalListDetailResultData(buildingInfo.getCorpId(),
                        buildingInfo.getBuildingId(),
                        String.valueOf(currentYear),
                        new BigDecimal(i), weekStartDate, currentDate));
            }

            //第53週
            currentDate = DateUtility.plusDay(currentDate, 1);
            weekStartDate = currentDate;
            currentDate = DateUtility.plusDay(currentDate, 6);
            if (currentDate.after(yearEndDate)) {
                //年度の終了日を超えている場合
                calList.add(new DemandWeekBuildingCalListDetailResultData(buildingInfo.getCorpId(),
                        buildingInfo.getBuildingId(),
                        String.valueOf(currentYear),
                        new BigDecimal(53), weekStartDate, yearEndDate));
            } else {
                calList.add(new DemandWeekBuildingCalListDetailResultData(buildingInfo.getCorpId(),
                        buildingInfo.getBuildingId(),
                        String.valueOf(currentYear),
                        new BigDecimal(53), weekStartDate, currentDate));
            }

            //第54週
            if (currentDate.before(yearEndDate)) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                weekStartDate = currentDate;
                calList.add(new DemandWeekBuildingCalListDetailResultData(buildingInfo.getCorpId(),
                        buildingInfo.getBuildingId(),
                        String.valueOf(currentYear),
                        new BigDecimal(54), weekStartDate, yearEndDate));
            }

            currentYear = currentYear + 1;

        }

        return calList;
    }

    /**
     * 実績取得年度・週No取得処理（企業集計）
     *
     * @param calList
     * @param baseDate
     * @param range
     * @return
     */
    public final static TargetFiscalYearWeekNoResult getTargetCorpFiscalYearWeekNo(
            List<DemandWeekCorpCalListDetailResultData> calList,
            Date baseDate, BigDecimal range) {

        String fiscalYearFrom;
        String fiscalYearTo;
        Integer index;

        //現在の年度、週Noを取得する
        CurrentWeekReportResult currentWeek = getCurrentCorpWeekReport(calList, baseDate);

        if (range.compareTo(BigDecimal.ZERO) == 0) {
            //集計期間が0の場合は、現在
            return new TargetFiscalYearWeekNoResult(currentWeek.getFiscalYear(), currentWeek.getWeekNo());
        }

        //年度の範囲を設定する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合
            fiscalYearFrom = new BigDecimal(currentWeek.getFiscalYear())
                    .add(range.divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
            fiscalYearTo = currentWeek.getFiscalYear();
        } else {
            //プラスの場合
            fiscalYearFrom = currentWeek.getFiscalYear();
            fiscalYearTo = new BigDecimal(currentWeek.getFiscalYear())
                    .add(range.divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
        }

        //年度の範囲でフィルタリングする
        List<DemandWeekCorpCalListDetailResultData> filterCalList = calList.stream()
                .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                        && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                .collect(Collectors.toList());

        //さらにフィルタリングする
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、現在の週、年度より前
            filterCalList = filterCalList.stream()
                    .filter(i -> (Integer.parseInt(i.getFiscalYear()) < Integer.parseInt(currentWeek.getFiscalYear())
                            || (i.getFiscalYear().equals(currentWeek.getFiscalYear())
                                    && i.getWeekNo().compareTo(currentWeek.getWeekNo()) < 0)))
                    .collect(Collectors.toList());
        } else {
            //プラスの場合、現在の週、年度より後
            filterCalList = filterCalList.stream()
                    .filter(i -> (Integer.parseInt(i.getFiscalYear()) > Integer.parseInt(currentWeek.getFiscalYear())
                            || (i.getFiscalYear().equals(currentWeek.getFiscalYear())
                                    && i.getWeekNo().compareTo(currentWeek.getWeekNo()) > 0)))
                    .collect(Collectors.toList());
        }

        if (filterCalList == null || filterCalList.isEmpty()) {
            return null;
        }

        //指定のインデックスを取得する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、フィルタリング後のサイズ + 集計期間
            index = new BigDecimal(filterCalList.size()).add(range).intValue();
        } else {
            //プラスの場合、集計期間 - 1
            index = range.subtract(BigDecimal.ONE).intValue();
        }

        if (filterCalList.size() - 1 < index) {
            //対象の箇所にリストの要素がない場合
            return null;
        } else {
            //年度、週Noでソート
            filterCalList = filterCalList.stream()
                    .sorted(Comparator
                            .comparing(DemandWeekCorpCalListDetailResultData::getFiscalYear,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(DemandWeekCorpCalListDetailResultData::getWeekNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        return new TargetFiscalYearWeekNoResult(filterCalList.get(index).getFiscalYear(),
                filterCalList.get(index).getWeekNo());
    }

    /**
     * 実績取得年度・週No取得処理（電力集計）
     *
     * @param calList
     * @param baseDate
     * @param range
     * @return
     */
    public final static TargetFiscalYearWeekNoResult getTargetBuildingFiscalYearWeekNo(
            List<DemandWeekBuildingCalListDetailResultData> calList, Date baseDate, BigDecimal range) {
        String fiscalYearFrom;
        String fiscalYearTo;
        Integer index;

        //現在の年度、週Noを取得する
        CurrentWeekReportResult currentWeek = getCurrentBuildingWeekReport(calList, baseDate);

        if (range.compareTo(BigDecimal.ZERO) == 0) {
            //集計期間が0の場合は、現在
            return new TargetFiscalYearWeekNoResult(currentWeek.getFiscalYear(), currentWeek.getWeekNo());
        }

        //年度の範囲を設定する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合
            fiscalYearFrom = new BigDecimal(currentWeek.getFiscalYear())
                    .add(range.divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
            fiscalYearTo = currentWeek.getFiscalYear();
        } else {
            //プラスの場合
            fiscalYearFrom = currentWeek.getFiscalYear();
            fiscalYearTo = new BigDecimal(currentWeek.getFiscalYear())
                    .add(range.divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
        }

        //年度の範囲でフィルタリングする
        List<DemandWeekBuildingCalListDetailResultData> filterCalList = calList.stream()
                .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                        && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                .collect(Collectors.toList());

        //さらにフィルタリングする
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、現在の年度、週Noより前
            filterCalList = filterCalList.stream()
                    .filter(i -> (Integer.parseInt(i.getFiscalYear()) < Integer.parseInt(currentWeek.getFiscalYear())
                            || (i.getFiscalYear().equals(currentWeek.getFiscalYear())
                                    && i.getWeekNo().compareTo(currentWeek.getWeekNo()) < 0)))
                    .collect(Collectors.toList());
        } else {
            //プラスの場合、現在の年度、週Noより後
            filterCalList = filterCalList.stream()
                    .filter(i -> (Integer.parseInt(i.getFiscalYear()) > Integer.parseInt(currentWeek.getFiscalYear())
                            || (i.getFiscalYear().equals(currentWeek.getFiscalYear())
                                    && i.getWeekNo().compareTo(currentWeek.getWeekNo()) > 0)))
                    .collect(Collectors.toList());
        }

        if (filterCalList == null || filterCalList.isEmpty()) {
            return null;
        }

        //指定のインデックスを取得する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、フィルタリング後のサイズ + 集計期間
            index = new BigDecimal(filterCalList.size()).add(range).intValue();
        } else {
            //プラスの場合、集計期間 - 1
            index = range.subtract(BigDecimal.ONE).intValue();
        }

        if (filterCalList.size() - 1 < index) {
            //対象の箇所にリストの要素がない場合
            return null;
        } else {
            //年度、週Noでソート
            filterCalList = filterCalList.stream()
                    .sorted(Comparator
                            .comparing(DemandWeekBuildingCalListDetailResultData::getFiscalYear,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(DemandWeekBuildingCalListDetailResultData::getWeekNo,
                                    Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        return new TargetFiscalYearWeekNoResult(filterCalList.get(index).getFiscalYear(),
                filterCalList.get(index).getWeekNo());
    }

    /**
     * 週報積算中年度・週No取得処理（企業集計）
     *
     * @param calList
     * @param baseDate
     * @return
     */
    public final static CurrentWeekReportResult getCurrentCorpWeekReport(
            List<DemandWeekCorpCalListDetailResultData> calList,
            Date baseDate) {
        //baseDateをyyyyMMddでフォーマットする
        Date formatBaseDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        List<DemandWeekCorpCalListDetailResultData> filterCalList = calList.stream()
                .filter(i -> i.getWeekStartDate().compareTo(formatBaseDate) <= 0
                        && i.getWeekEndDate().compareTo(formatBaseDate) >= 0)
                .collect(Collectors.toList());

        if (filterCalList == null || filterCalList.size() != 1) {
            return null;
        }

        return new CurrentWeekReportResult(filterCalList.get(0).getFiscalYear(), filterCalList.get(0).getWeekNo());
    }

    /**
     * 週報積算中年度・週No取得処理（電力集計）
     *
     * @param calList
     * @param baseDate
     * @return
     */
    public final static CurrentWeekReportResult getCurrentBuildingWeekReport(
            List<DemandWeekBuildingCalListDetailResultData> calList,
            Date baseDate) {
        //baseDateをyyyyMMddでフォーマットする
        Date formatBaseDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        List<DemandWeekBuildingCalListDetailResultData> filterCalList = calList.stream()
                .filter(i -> i.getWeekStartDate().compareTo(formatBaseDate) <= 0
                        && i.getWeekEndDate().compareTo(formatBaseDate) >= 0)
                .collect(Collectors.toList());

        if (filterCalList == null || filterCalList.size() != 1) {
            return null;
        }

        return new CurrentWeekReportResult(filterCalList.get(0).getFiscalYear(), filterCalList.get(0).getWeekNo());
    }

    /**
     * 実績取得年度・月No取得処理
     * @param yearCalList
     * @param baseDate
     * @param range
     * @param sumDate
     * @return
     */
    public final static TargetFiscalYearMonthNoResult getTargetFiscalYearMonthNo(
            List<DemandCalendarYearData> yearCalList, Date baseDate, BigDecimal range, String sumDate) {

        BigDecimal fiscalYearFrom;
        BigDecimal fiscalYearTo;
        Integer index;
        List<DemandCalendarYearData> tempCalList = new ArrayList<>();

        //現在の年度、月Noを取得する
        CurrentYearReportResult currentMonth = getCurrentYearReport(yearCalList, baseDate, sumDate);
        if (range.compareTo(BigDecimal.ZERO) == 0) {
            //集計期間が0の場合は、現在
            return new TargetFiscalYearMonthNoResult(currentMonth.getFiscalYear(), currentMonth.getMonthNo(),
                    currentMonth.getCalYear());
        }

        //年度の範囲を設定する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合
            fiscalYearFrom = new BigDecimal(currentMonth.getFiscalYear()).add(range);
            fiscalYearTo = new BigDecimal(currentMonth.getFiscalYear());
        } else {
            //プラスの場合
            fiscalYearFrom = new BigDecimal(currentMonth.getFiscalYear());
            fiscalYearTo = new BigDecimal(currentMonth.getFiscalYear()).add(range);
        }

        //年度の範囲でフィルタリングする
        for (DemandCalendarYearData yearCal : yearCalList) {
            if (fiscalYearFrom
                    .compareTo(new BigDecimal(DemandCalendarYearUtility.getFiscalYearNo(yearCal, sumDate))) <= 0
                    && fiscalYearTo.compareTo(
                            new BigDecimal(DemandCalendarYearUtility.getFiscalYearNo(yearCal, sumDate))) >= 0) {
                tempCalList.add(yearCal);
            }
        }

        if (tempCalList.isEmpty()) {
            return null;
        }

        //さらにフィルタリングする
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、現在のカレンダ年、月Noより前
            tempCalList = tempCalList
                    .stream().filter(
                            i -> Integer.parseInt(i.getYearNo().concat(String.format(
                                    StringUtility.STRING_FORMAT_ZERO_PADDING_2, i.getMonthNo().intValue()))) < Integer
                                            .parseInt(currentMonth.getCalYear()
                                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            currentMonth.getMonthNo().intValue()))))
                    .collect(Collectors.toList());
        } else {
            //プラスの場合、現在のカレンダ年、月Noより後
            tempCalList = tempCalList
                    .stream().filter(
                            i -> Integer.parseInt(i.getYearNo().concat(String.format(
                                    StringUtility.STRING_FORMAT_ZERO_PADDING_2, i.getMonthNo().intValue()))) > Integer
                                            .parseInt(currentMonth.getCalYear()
                                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            currentMonth.getMonthNo().intValue()))))
                    .collect(Collectors.toList());
        }

        if (tempCalList == null || tempCalList.isEmpty()) {
            return null;
        }

        //指定のインデックスを取得する
        if (range.compareTo(BigDecimal.ZERO) < 0) {
            //マイナスの場合、フィルタリング後のサイズ + 集計期間
            index = new BigDecimal(tempCalList.size()).add(range).intValue();
        } else {
            //プラスの場合、集計期間 - 1
            index = range.subtract(BigDecimal.ONE).intValue();
        }

        if (tempCalList.size() - 1 < index) {
            //対象の箇所にリストの要素がない場合
            return null;
        } else {
            //年No、月Noでソート
            tempCalList = tempCalList.stream().sorted(Comparator.comparing(DemandCalendarYearData::getYearNo,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(DemandCalendarYearData::getMonthNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        return new TargetFiscalYearMonthNoResult(
                DemandCalendarYearUtility.getFiscalYearNo(tempCalList.get(index), sumDate),
                tempCalList.get(0).getMonthNo(), tempCalList.get(index).getYearNo());
    }

    /**
     * 実績取得年度・集計範囲取得処理
     * @param yearCalList
     * @param baseDate
     * @param range
     * @param sumDate
     * @return
     */
    public final static TargetFiscalYearSummaryRangeResult getTargetFiscalYearSummaryRange(
            List<DemandCalendarYearData> yearCalList, Date baseDate, BigDecimal range, String sumDate) {

        BigDecimal fiscalYearFrom;
        BigDecimal fiscalYearTo;
        List<DemandCalendarYearData> tempCalList = new ArrayList<>();

        //現在の年度、月Noを取得する
        CurrentYearReportResult currentMonth = getCurrentYearReport(yearCalList, baseDate, sumDate);

        //年度の範囲を取得する
        fiscalYearFrom = new BigDecimal(currentMonth.getFiscalYear()).subtract(range);
        fiscalYearTo = new BigDecimal(currentMonth.getFiscalYear()).add(range);

        //年度の範囲でフィルタリングする
        for (DemandCalendarYearData yearCal : yearCalList) {
            if (fiscalYearFrom
                    .compareTo(new BigDecimal(DemandCalendarYearUtility.getFiscalYearNo(yearCal, sumDate))) <= 0
                    && fiscalYearTo.compareTo(
                            new BigDecimal(DemandCalendarYearUtility.getFiscalYearNo(yearCal, sumDate))) >= 0) {
                tempCalList.add(yearCal);
            }
        }

        if (tempCalList.isEmpty()) {
            return null;
        }

        //年No、月Noでソート
        tempCalList = tempCalList.stream().sorted(Comparator.comparing(DemandCalendarYearData::getYearNo,
                Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(DemandCalendarYearData::getMonthNo, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        return new TargetFiscalYearSummaryRangeResult(tempCalList.get(0).getYearNo(), tempCalList.get(0).getMonthNo(),
                tempCalList.get(tempCalList.size() - 1).getYearNo(),
                tempCalList.get(tempCalList.size() - 1).getMonthNo());
    }

    /**
     * 年報積算中の年月を取得する
     * @param yearCalList
     * @param baseDate
     * @param sumDate
     * @return
     */
    public static CurrentYearReportResult getCurrentYearReport(List<DemandCalendarYearData> yearCalList, Date baseDate,
            String sumDate) {

        CurrentYearReportResult result = new CurrentYearReportResult();

        //baseDateをyyyyMMddでフォーマットする
        Date formatBaseDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        //基準日でフィルタリングを行う
        List<DemandCalendarYearData> filterCalList = yearCalList.stream()
                .filter(i -> i.getMonthStartDate().compareTo(formatBaseDate) <= 0
                        && i.getMonthEndDate().compareTo(formatBaseDate) >= 0)
                .collect(Collectors.toList());

        if (filterCalList == null || filterCalList.size() != 1) {
            return null;
        }

        result.setFiscalYear(DemandCalendarYearUtility.getFiscalYearNo(filterCalList.get(0), sumDate));
        result.setMonthNo(filterCalList.get(0).getMonthNo());
        result.setCalYear(filterCalList.get(0).getYearNo());
        result.setMonthEndDate(filterCalList.get(0).getMonthEndDate());

        return result;
    }

    /**
     * 指定された年と指定された過去の年数分のリストを返す
     *
     * @param baseYear
     * @param beforeYearCount
     * @return
     */
    public final static List<String> getYearList(String baseYear, BigDecimal beforeYearCount) {
        List<String> yearList = new ArrayList<>();
        for (int i = beforeYearCount.intValue(); i >= 0; i--) {
            yearList.add(String.valueOf(Integer.parseInt(baseYear) - i));
        }
        return yearList;
    }

    /**
     * 各月目標電力量を取得する
     *
     * @param corpId
     * @param buildingId
     * @param calYmFrom
     * @param calYmTo
     * @param buildingDemand
     * @return
     */
    public static final List<MonthlyTargetUsedPowerResult> getMonthlyTargetUsedPower(String corpId, Long buildingId,
            String calYmFrom, String calYmTo, BuildingDemandListDetailResultData buildingDemand) {

        String currentYm;
        String mm;
        List<MonthlyTargetUsedPowerResult> list = new ArrayList<>();

        if (CheckUtility.isNullOrEmpty(corpId) || buildingId == null || CheckUtility.isNullOrEmpty(calYmFrom)
                || CheckUtility.isNullOrEmpty(calYmTo)) {
            return null;
        }

        if (DateUtility.conversionDate(calYmFrom, DateUtility.DATE_FORMAT_YYYYMM) == null
                || DateUtility.conversionDate(calYmTo, DateUtility.DATE_FORMAT_YYYYMM) == null) {
            return null;
        }

        if (DateUtility.conversionDate(calYmFrom, DateUtility.DATE_FORMAT_YYYYMM)
                .after(DateUtility.conversionDate(calYmTo, DateUtility.DATE_FORMAT_YYYYMM))) {
            //FromとToが逆転している場合
            return null;
        }

        currentYm = "";

        //年月を設定する
        do {

            if (CheckUtility.isNullOrEmpty(currentYm)) {
                currentYm = calYmFrom;
            } else if (currentYm.substring(4).equals("12")) {
                //12月の場合年を1年足して1月に
                currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
            } else {
                //1ヶ月足す
                currentYm = currentYm.substring(0, 4).concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        Integer.parseInt(currentYm.substring(4)) + 1));
            }

            //年月をセットする
            list.add(new MonthlyTargetUsedPowerResult(currentYm, BigDecimal.ZERO));

        } while (!currentYm.equals(calYmTo));

        //各月の目標電力を設定する
        for (MonthlyTargetUsedPowerResult result : list) {
            //建物デマンドから取得する
            mm = DateUtility.changeDateFormat(
                    DateUtility.conversionDate(result.getYm(), DateUtility.DATE_FORMAT_YYYYMM),
                    DateUtility.DATE_FORMAT_MM);
            result.setTargetUsedPower(getTargetKwhMonth(mm, buildingDemand));
        }

        return list;

    }

    /**
     * 指定月の使用エネルギー各月目標を取得する
     *
     * @param mm
     * @return
     */
    private static BigDecimal getTargetKwhMonth(String mm, BuildingDemandListDetailResultData buildingDemand) {
        switch (mm) {
        case "01":
            return buildingDemand.getTargetKwhMonth1();
        case "02":
            return buildingDemand.getTargetKwhMonth2();
        case "03":
            return buildingDemand.getTargetKwhMonth3();
        case "04":
            return buildingDemand.getTargetKwhMonth4();
        case "05":
            return buildingDemand.getTargetKwhMonth5();
        case "06":
            return buildingDemand.getTargetKwhMonth6();
        case "07":
            return buildingDemand.getTargetKwhMonth7();
        case "08":
            return buildingDemand.getTargetKwhMonth8();
        case "09":
            return buildingDemand.getTargetKwhMonth9();
        case "10":
            return buildingDemand.getTargetKwhMonth10();
        case "11":
            return buildingDemand.getTargetKwhMonth11();
        case "12":
            return buildingDemand.getTargetKwhMonth12();
        default:
            return null;
        }
    }

    /**
     * 日別目標値を取得する
     *
     * @param monthStartDate
     * @param monthEndDate
     * @param monthlyUsedTarget
     * @param decimal
     * @param control
     * @return
     */
    public static final List<DailyUsedTargetResult> getDailyUsedTarget(Date monthStartDate, Date monthEndDate,
            BigDecimal monthlyUsedTarget, Integer decimal, String control) {

        List<DailyUsedTargetResult> list = new ArrayList<>();
        Date currentDate = null;
        BigDecimal currentTargetUse = null;
        BigDecimal usePerDay;

        if (monthStartDate == null || monthEndDate == null || monthStartDate.compareTo(monthEndDate) >= 0) {
            //上記いずれかに該当する場合、nullを返却
            return null;
        }

        do {

            if (currentDate == null) {
                currentDate = monthStartDate;
            } else {
                currentDate = DateUtility.plusDay(currentDate, 1);
            }
            list.add(new DailyUsedTargetResult(currentDate, null));

        } while (currentDate.compareTo(monthEndDate) != 0);

        //対象月の日数を取得する
        int range = (int) ((monthEndDate.getTime() - monthStartDate.getTime()) / 1000 / 60 / 60 / 24);
        range = range + 1;

        //1日当たりの使用量を算出する
        if (monthlyUsedTarget == null) {
            usePerDay = null;
        } else {
            usePerDay = monthlyUsedTarget.divide(new BigDecimal(range), 10, BigDecimal.ROUND_HALF_UP);
        }

        //日別目標値を設定する
        for (DailyUsedTargetResult result : list) {
            if (currentTargetUse == null) {
                currentTargetUse = usePerDay;
            } else if (result.getDay().compareTo(monthEndDate) == 0) {
                //最終日の場合は、月間目標電力量
                if (monthlyUsedTarget == null) {
                    currentTargetUse = null;
                } else {
                    currentTargetUse = monthlyUsedTarget;
                }

            } else {
                if (usePerDay != null)
                    currentTargetUse = currentTargetUse.add(usePerDay);
            }
            if (currentTargetUse != null)
                result.setUsedTarget(currentTargetUse.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return list;

    }

    /**
     * 月間推測値を取得する
     *
     * @param monthStartDate
     * @param monthEndDate
     * @param baseDate
     * @param monthlyUsed
     * @param recent6DaysUsed
     * @param recent6DaysSettingCount
     * @param decimal
     * @param control
     * @return
     */
    public static final BigDecimal getSummaryUsedForecast(Date monthStartDate, Date monthEndDate, Date baseDate,
            BigDecimal monthlyUsed, BigDecimal recent6DaysUsed, BigDecimal recent6DaysSettingCount, Integer decimal,
            String control) {

        if (monthStartDate == null || monthEndDate == null || baseDate == null || monthlyUsed == null
                || BigDecimal.ZERO.compareTo(monthlyUsed) == 0 || recent6DaysUsed == null
                || recent6DaysSettingCount == null || monthStartDate.compareTo(monthEndDate) >= 0) {
            //上記条件のいずれかに該当する場合は、0を返却
            return BigDecimal.ZERO;
        }

        //過去6日分の平均値を算出する
        BigDecimal recent6DaysAverage;
        if (BigDecimal.ZERO.compareTo(recent6DaysUsed) == 0
                || BigDecimal.ZERO.compareTo(recent6DaysSettingCount) == 0) {
            recent6DaysAverage = BigDecimal.ZERO;
        } else {
            recent6DaysAverage = recent6DaysUsed.divide(recent6DaysSettingCount, 10, BigDecimal.ROUND_HALF_UP);
        }

        //対象月の残日数を算出する
        int rest = (int) ((monthEndDate.getTime() - baseDate.getTime()) / 1000 / 60 / 60 / 24);

        //月間推測値を算出する
        return monthlyUsed.add(recent6DaysAverage.multiply(new BigDecimal(rest))).setScale(decimal,
                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control));
    }

    /**
     * 電力使用量目標対比を取得する
     *
     * @param monthStartDate
     * @param monthEndDate
     * @param baseDate
     * @param monthlyUsedTarget
     * @param monthlyUsed
     * @param decimal
     * @param control
     * @return
     */
    public static final BigDecimal getCompareSummaryUsedAndTargetUsed(Date monthStartDate, Date monthEndDate,
            Date baseDate, BigDecimal monthlyUsedTarget, BigDecimal monthlyUsed, Integer decimal, String control) {
        if (monthStartDate == null || monthEndDate == null || baseDate == null || monthlyUsedTarget == null
                || BigDecimal.ZERO.compareTo(monthlyUsedTarget) == 0
                || monthlyUsed == null || BigDecimal.ZERO.compareTo(monthlyUsed) == 0
                || monthStartDate.compareTo(monthEndDate) >= 0) {
            //上記条件のいずれかに該当する場合は、0を返却
            return BigDecimal.ZERO;
        }

        //対象月の日数を取得する
        int range = (int) ((monthEndDate.getTime() - monthStartDate.getTime()) / 1000 / 60 / 60 / 24);
        range = range + 1;

        //基準日の経過日数を取得する
        int progress = (int) ((baseDate.getTime() - monthStartDate.getTime()) / 1000 / 60 / 60 / 24);
        progress = progress + 1;

        //目標対比値を取得する
        return monthlyUsed.multiply(new BigDecimal(100))
                .divide((monthlyUsedTarget.divide(new BigDecimal(range), decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)))
                                .multiply(new BigDecimal(progress)),
                        decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control))
                .setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control));

    }

    /**
     * 対象となる使用エネルギーデータを取得する
     * @param energyList
     * @param baseDate
     * @return
     */
    public static TAvailableEnergy getAvailableEnergyData(List<TAvailableEnergy> energyList, Date baseDate) {
        TAvailableEnergy result = null;
        if (energyList == null || energyList.isEmpty()) {
            return null;
        }
        for (TAvailableEnergy energy : energyList) {
            // 利用開始日
            if (energy.getEnergyStartYm() != null && energy.getEnergyStartYm().compareTo(baseDate) > 0) {
                if (result == null)
                    result = energy;
                continue;
            }
            // 利用終了日
            if (energy.getEnergyEndYm() != null && energy.getEnergyEndYm().compareTo(baseDate) < 0) {
                if (result == null)
                    result = energy;
                continue;
            }
            // 範囲内
            return energy;
        }
        return result;
    }

    /**
     * 対象となる係数履歴管理データを取得する
     * @param coefficientList
     * @param baseDate
     * @return
     */
    public static MCoefficientHistoryManage getCoefficientHistoryData(List<MCoefficientHistoryManage> coefficientList,
            Date baseDate) {

        if (coefficientList == null || coefficientList.isEmpty()) {
            return null;
        }

        for (MCoefficientHistoryManage entity : coefficientList) {
            // 開始年月
            if (entity.getStartYm() != null && entity.getStartYm()
                    .compareTo(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYYMM)) > 0) {
                continue;
            }
            // 終了年月
            if (entity.getStartYm() != null && entity.getStartYm()
                    .compareTo(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYYMM)) > 0) {
                continue;
            }
            // 範囲内
            return entity;
        }

        return null;
    }

}
