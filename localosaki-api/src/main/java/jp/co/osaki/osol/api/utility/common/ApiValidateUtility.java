/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.common;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * Api共通入力チェッククラス
 *
 * @author ya-ishida
 */
public class ApiValidateUtility {

    /**
     * API共通パラメータの入力チェック（ログインユーザーID、ログインパーソンID、自企業）
     *
     * @param loginUserId
     * @param loginPersonId
     * @param loginUserCorpId
     * @return
     */
    public static boolean validateApiCommonParameter(Long loginUserId, String loginPersonId, String loginUserCorpId) {
        return validateLoginPersonId(loginPersonId) && validateLoginUserCorpId(loginUserCorpId);
    }

    /**
     * API共通パラメータの入力チェック（ログインユーザーID、ログインパーソンID、自企業 + 操作中企業）
     *
     * @param loginUserId
     * @param loginPersonId
     * @param loginUserCorpId
     * @param corpId
     * @return
     */
    public static boolean validateApiCommonParameter(Long loginUserId, String loginPersonId, String loginUserCorpId, String corpId) {
        return ApiValidateUtility.validateApiCommonParameter(loginUserId, loginPersonId, loginUserCorpId) && validateCorpId(corpId);
    }

    /**
     * API共通パラメータの入力チェック（ログインユーザーID、ログインパーソンID、自企業 + 操作中企業 + 建物ID）
     *
     * @param loginUserId
     * @param loginPersonId
     * @param loginUserCorpId
     * @param corpId
     * @param buildingId
     * @return
     */
    public static boolean validateApiCommonParameter(Long loginUserId, String loginPersonId, String loginUserCorpId, String corpId, Long buildingId) {
        return ApiValidateUtility.validateApiCommonParameter(loginUserId, loginPersonId, loginUserCorpId) && validateCorpId(corpId) && validateBuildingId(buildingId);
    }

    /**
     * デマンド情報（全体・サマリー）の共通パラメータの入力チェック
     *
     * @param reportTarget
     * @param analysisTarget
     * @param buildingTarget
     * @return
     */
    public static boolean validateDemandAllSummaryReportCommonParameter(String reportTarget, String analysisTarget, String buildingTarget) {
        return validateAllSummaryReportTarget(reportTarget) && validateAllSummaryAnalysisTarget(reportTarget, analysisTarget) && validateBuildingTarget(reportTarget, buildingTarget);
    }

    /**
     * デマンド情報（全体・サマリー・ＸＸＸＸのＸＸＸＸＸ評価）の共通パラメータの入力チェック（集計範囲指定方法・比較対象なし）
     *
     * @param buildingFilter
     * @param parentGroup
     * @param buildingNo
     * @param lineGroupId
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandAllSummaryPowerAnalysisCommonParameter(String buildingFilter, Long parentGroup, String buildingNo, Long lineGroupId, String type, String control) {
        return validateBuildingFilter(buildingFilter, parentGroup, buildingNo) && validateLineGroupId(lineGroupId) && validateType(type) && validateControl(control);
    }

    /**
     * デマンド情報（全体・サマリー・ＸＸＸＸのＸＸＸＸＸ評価）の共通パラメータの入力チェック（集計範囲指定方法なし・比較対象あり）
     *
     * @param buildingFilter
     * @param parentGroup
     * @param buildingNo
     * @param lineGroupId
     * @param type
     * @param compareTarget
     * @param control
     * @return
     */
    public static boolean validateDemandAllSummaryPowerAnalysisCommonParameter(String buildingFilter, Long parentGroup, String buildingNo, Long lineGroupId, String type, String compareTarget, String control) {
        return validateDemandAllSummaryPowerAnalysisCommonParameter(buildingFilter, parentGroup, buildingNo, lineGroupId, type, control) && validateCompareTarget(compareTarget);
    }

    /**
     * デマンド情報（全体・サマリー・ＸＸＸＸのＸＸＸＸＸ評価）の共通パラメータの入力チェック（集計範囲指定方法・比較対象あり）
     *
     * @param buildingFilter
     * @param parentGroup
     * @param buildingNo
     * @param lineGroupId
     * @param type
     * @param rangeUnit
     * @param compareTarget
     * @param control
     * @return
     */
    public static boolean validateDemandAllSummaryPowerAnalysisCommonParameter(String buildingFilter, Long parentGroup, String buildingNo, Long lineGroupId, String type, String rangeUnit, String compareTarget, String control) {
        return validateDemandAllSummaryPowerAnalysisCommonParameter(buildingFilter, parentGroup, buildingNo, lineGroupId, type, compareTarget, control) && validateRangeUnit(rangeUnit);
    }

    /**
     * デマンド情報（全体・サマリー・建物・テナント一覧）の共通パラメータの入力チェック
     *
     * @param buildingFilter
     * @param parentGroup
     * @param buildingNo
     * @param lineGroupId
     * @param summaryKind
     * @param sortKey
     * @param sortOrder
     * @param day
     * @param control
     * @return
     */
    public static boolean validateDemandAllSummaryBuildingDataCommonParameter(String buildingFilter, Long parentGroup, String buildingNo, Long lineGroupId, String summaryKind, String sortKey, String sortOrder, String day, String control) {
        if (CheckUtility.isNullOrEmpty(day)) {
            return validateBuildingFilter(buildingFilter, parentGroup, buildingNo) && validateLineGroupId(lineGroupId) && validateSummaryKind(summaryKind)
                    && validateSortKey(sortKey) && validateSortOrder(sortOrder) && validateControl(control);
        } else {
            return validateBuildingFilter(buildingFilter, parentGroup, buildingNo) && validateLineGroupId(lineGroupId) && validateSummaryKind(summaryKind)
                    && validateSortKey(sortKey) && validateSortOrder(sortOrder) && validateDay(day, DateUtility.DATE_FORMAT_YYYYMMDD) && validateControl(control);
        }
    }

    /**
     * デマンド情報（全体・帳票）の共通パラメータの入力チェック（集計日・集計期間指定方法なし）
     *
     * @param buildingFilter
     * @param parentGroupId
     * @param buildingNo
     * @param lineGroupId
     * @param lineNo
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandAllReportCommonParameter(String buildingFilter, Long parentGroupId, String buildingNo, Long lineGroupId, String lineNo, String type, String control) {
        return validateBuildingFilter(buildingFilter, parentGroupId, buildingNo) && validateLineGroupId(lineGroupId) && validateLineNo(lineNo) && validateType(type) && validateControl(control);
    }

    /**
     * デマンド情報（全体・帳票）の共通パラメータの入力チェック（集計日あり・集計期間指定方法なし）
     *
     * @param buildingFilter
     * @param parentGroupId roup
     * @param buildingNo
     * @param lineGroupId
     * @param lineNo ine
     * @param summaryKind
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandAllReportCommonParameter(String buildingFilter, Long parentGroupId, String buildingNo, Long lineGroupId, String lineNo, String summaryKind, String type, String control) {
        return validateDemandAllReportCommonParameter(buildingFilter, parentGroupId, buildingNo, lineGroupId, lineNo, type, control) && validateSummaryKind(summaryKind);
    }

    /**
     * デマンド情報（全体・帳票）の共通パラメータの入力チェック（集計日あり・集計期間指定方法あり）
     *
     * @param buildingFilter
     * @param parentGroupId
     * @param buildingNo
     * @param lineGroupId
     * @param lineNo
     * @param summaryKind
     * @param rangeUnit
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandAllReportCommonParameter(String buildingFilter, Long parentGroupId, String buildingNo, Long lineGroupId, String lineNo, String summaryKind, String rangeUnit, String type, String control) {
        return validateDemandAllReportCommonParameter(buildingFilter, parentGroupId, buildingNo, lineGroupId, lineNo, summaryKind, type, control) && validateRangeUnit(rangeUnit);
    }

    /**
     * エネルギー使用状況（個別）建物情報取得パラメータの入力チェック
     *
     * @param control
     * @return
     */
    public static boolean validateDemandOrgBuildingInfoParameter(String control) {
        return validateControl(control);
    }

    /**
     * エネルギー使用状況（個別・サマリー）パラメータの入力チェック
     *
     * @param lineGroupId
     * @param summaryKind
     * @param day
     * @param conrtol
     * @return
     */
    public static boolean validateDemandOrgSummaryParameter(Long lineGroupId, String summaryKind, Date day, String conrtol) {
        return validateLineGroupId(lineGroupId) && validateSummaryKind(summaryKind) && day != null && validateControl(conrtol);
    }

    /**
     * 電力使用量（目標対比）パラメータの入力チェック
     *
     * @param lineGroupId
     * @param summaryKind
     * @param day
     * @param year
     * @param month
     * @param control
     * @return
     */
    public static boolean validateDemandOrgSummaryUsedDataParameter(Long lineGroupId, String summaryKind, Date day, String year, String month, String control) {
        if (!validateLineGroupId(lineGroupId) || !validateSummaryKind(summaryKind) && !validateControl(control)) {
            return false;
        }

        if (day == null) {
            if (CheckUtility.isNullOrEmpty(year) || CheckUtility.isNullOrEmpty(month)) {
                return false;
            }
        }

        if (CheckUtility.isNullOrEmpty(year)) {
            if (day == null) {
                return false;
            }
        }

        if (!CheckUtility.isNullOrEmpty(year)) {
            if (CheckUtility.isNullOrEmpty(month)) {
                return false;
            }

            try {
                if (!validateYearMonth(year, new BigDecimal(month))) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }

        }

        return true;
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（系統グループID、集計期間計算方法、指定精度未満の制御）
     *
     * @param lineGroupId
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(Long lineGroupId, String type, String control) {
        return validateLineGroupId(lineGroupId) && validateType(type) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（系統グループID、指定精度未満の制御）
     *
     * @param lineGroupId
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(Long lineGroupId, String control) {
        return validateLineGroupId(lineGroupId) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（系統グループID、集計日、集計期間計算方法、集計範囲指定方法、指定精度未満の制御）
     *
     * @param lineGroupId
     * @param summaryKind
     * @param type
     * @param rangeUnit
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(Long lineGroupId, String summaryKind, String type, String rangeUnit, String control) {
        return validateLineGroupId(lineGroupId) && validateSummaryKind(summaryKind) && validateType(type) && validateRangeUnit(rangeUnit) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（集計日、集計期間計算方法、集計範囲指定方法、指定精度未満の制御）
     *
     * @param summaryKind
     * @param type
     * @param rangeUnit
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(String summaryKind, String type, String rangeUnit, String control) {
        return validateSummaryKind(summaryKind) && validateType(type) && validateRangeUnit(rangeUnit) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（集計日、集計期間計算方法、指定精度未満の制御）
     *
     * @param summaryKind
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(String summaryKind, String type, String control) {
        return validateSummaryKind(summaryKind) && validateType(type) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（集計期間計算方法、指定精度未満の制御）
     *
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(String type, String control) {
        return validateType(type) && validateControl(control);
    }

    /**
     * エネルギー使用状況（個別）の共通パラメータの入力チェック（系統グループID、集計日、集計期間計算方法、指定精度未満の制御）
     *
     * @param lineGroupId
     * @param summaryKind
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandOrgReportCommonParameter(Long lineGroupId, String summaryKind, String type, String control) {
        return validateLineGroupId(lineGroupId) && validateSummaryKind(summaryKind) && validateType(type) && validateControl(control);
    }

    /**
     * エネルギー使用状況（日報・日報集計表）のパラメータの入力チェック
     *
     * @param lineGroupId
     * @param lineNo
     * @param smId
     * @param pointNo
     * @param day
     * @param rangeUnit
     * @param type
     * @param control
     * @return
     */
    public static boolean validateDemandOrgDayReportSummaryParameter(Long lineGroupId, String lineNo, Long smId, String pointNo, Date day, String rangeUnit, String type, String control) {
        if (day == null || !validateRangeUnitMonthly(rangeUnit) || !validateType(type) || !validateControl(control)) {
            return false;
        }

        if (lineGroupId != null && !CheckUtility.isNullOrEmpty(lineNo) && smId == null && CheckUtility.isNullOrEmpty(pointNo)) {
            return true;
        }

        if (lineGroupId == null && CheckUtility.isNullOrEmpty(lineNo) && smId != null && !CheckUtility.isNullOrEmpty(pointNo)) {
            return true;
        }

        if (lineGroupId == null && CheckUtility.isNullOrEmpty(lineNo) && smId == null && CheckUtility.isNullOrEmpty(pointNo)) {
            return true;
        }

        return false;
    }

    /**
     * デマンド情報カレンダーの共通パラメータの入力チェック
     *
     * @param range
     * @return
     */
    public static boolean validateDemandCalendarCommonParameter(BigDecimal range) {
        return validateRange(range);
    }

    /**
     * デマンド週報カレンダ取得の共通パラメータの入力チェック
     *
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @param baseDate
     * @return
     */
    public static boolean validateDemandWeekCalListCommonParameter(String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom, BigDecimal weekNoTo, String baseDate) {
        //ToはFromが設定されていること
        if (CheckUtility.isNullOrEmpty(fiscalYearFrom) && !CheckUtility.isNullOrEmpty(fiscalYearTo)) {
            return false;
        }

        if (weekNoFrom == null && weekNoTo != null) {
            return false;
        }

        //週Noは年度番号が設定されていること
        if (CheckUtility.isNullOrEmpty(fiscalYearFrom) && weekNoFrom != null) {
            return false;
        }

        if (CheckUtility.isNullOrEmpty(fiscalYearTo) && weekNoTo != null) {
            return false;
        }

        //年度の数値チェック
        try {
            if (!CheckUtility.isNullOrEmpty(fiscalYearFrom)) {
                Integer.parseInt(fiscalYearFrom);
            }
            if (!CheckUtility.isNullOrEmpty(fiscalYearTo)) {
                Integer.parseInt(fiscalYearTo);
            }
        } catch (NumberFormatException ex) {
            return false;
        }

        //週Noの範囲チェック
        if (weekNoFrom != null) {
            if (weekNoFrom.compareTo(BigDecimal.ZERO) <= 0 || weekNoFrom.compareTo(new BigDecimal(54)) > 0) {
                return false;
            }
        }

        if (weekNoTo != null) {
            if (weekNoTo.compareTo(BigDecimal.ZERO) <= 0 || weekNoTo.compareTo(new BigDecimal(54)) > 0) {
                return false;
            }
        }

        //基点日の日付チェック
        if (!CheckUtility.isNullOrEmpty(baseDate)) {
            if (DateUtility.conversionDate(baseDate, DateUtility.DATE_FORMAT_YYYYMMDD) == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * デマンド年報カレンダ取得の共通パラメータの入力チェック
     *
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param calYmFrom
     * @param calYmTo
     * @param baseDate
     * @return
     */
    public static boolean validateDemandYearCalListCommonParameter(String fiscalYearFrom, String fiscalYearTo, String calYmFrom, String calYmTo, String baseDate) {
        //ToはFromが設定されていること
        if (CheckUtility.isNullOrEmpty(fiscalYearFrom) && !CheckUtility.isNullOrEmpty(fiscalYearTo)) {
            return false;
        }

        if (CheckUtility.isNullOrEmpty(calYmFrom) && !CheckUtility.isNullOrEmpty(calYmTo)) {
            return false;
        }

        //カレンダ年月、基点日の日付チェック
        if (!CheckUtility.isNullOrEmpty(calYmFrom)) {
            if (DateUtility.conversionDate(calYmFrom, DateUtility.DATE_FORMAT_YYYYMM) == null) {
                return false;
            }
        }

        if (!CheckUtility.isNullOrEmpty(calYmTo)) {
            if (DateUtility.conversionDate(calYmTo, DateUtility.DATE_FORMAT_YYYYMM) == null) {
                return false;
            }
        }

        if (!CheckUtility.isNullOrEmpty(baseDate)) {
            if (DateUtility.conversionDate(baseDate, DateUtility.DATE_FORMAT_YYYYMMDD) == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 日付のチェック
     *
     * @param day
     * @param format
     * @return
     */
    public static boolean validateDay(String day, String format) {

        if (CheckUtility.isNullOrEmpty(day)) {
            return false;
        }

        //日付のチェック
        if (DateUtility.conversionDate(day, format) == null) {
            return false;
        }

        return true;
    }

    /**
     * 年度・週Noの入力チェック
     *
     * @param fiscalYear
     * @param weekNo
     * @return
     */
    public static boolean validateFiscalYearWeekNo(String fiscalYear, BigDecimal weekNo) {
        //必須チェック
        if (CheckUtility.isNullOrEmpty(fiscalYear) || weekNo == null) {
            return false;
        }

        //年度の数値チェック
        try {
            Integer.parseInt(fiscalYear);
        } catch (NumberFormatException ex) {
            return false;
        }

        //週番号の範囲チェック
        if (weekNo.compareTo(BigDecimal.ZERO) <= 0 || weekNo.compareTo(new BigDecimal(54)) > 0) {
            return false;
        }

        return true;
    }

    /**
     * 年度・週Noの入力チェック（データ対比用）
     *
     * @param fiscalYearDestination
     * @param fiscalYearSource
     * @param weekNo
     * @return
     */
    public static boolean validateFiscalYearWeekNo(String fiscalYearDestination, String fiscalYearSource, BigDecimal weekNo) {
        //必須チェック
        if (CheckUtility.isNullOrEmpty(fiscalYearDestination) || CheckUtility.isNullOrEmpty(fiscalYearSource) || weekNo == null) {
            return false;
        }

        //年度の数値チェック
        try {
            Integer.parseInt(fiscalYearDestination);
            Integer.parseInt(fiscalYearSource);
        } catch (NumberFormatException ex) {
            return false;
        }

        //週番号の範囲チェック
        if (weekNo.compareTo(BigDecimal.ZERO) <= 0 || weekNo.compareTo(new BigDecimal(54)) > 0) {
            return false;
        }

        return true;
    }

    /**
     * 年と月の入力チェック
     *
     * @param year
     * @param month
     * @return
     */
    public static boolean validateYearMonth(String year, BigDecimal month) {
        //必須チェック
        if (CheckUtility.isNullOrEmpty(year) || month == null) {
            return false;
        }

        //年度の数値チェック
        try {
            Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            return false;
        }

        //月の範囲チェック
        if (month.compareTo(BigDecimal.ZERO) <= 0 || month.compareTo(new BigDecimal(12)) > 0) {
            return false;
        }

        return true;

    }

    /**
     * 年と月の入力チェック（データ対比用）
     *
     * @param yearDestination
     * @param yearSource
     * @param month
     * @return
     */
    public static boolean validateYearMonth(String yearDestination, String yearSource, BigDecimal month) {
        //必須チェック
        if (CheckUtility.isNullOrEmpty(yearDestination) || CheckUtility.isNullOrEmpty(yearSource) || month == null) {
            return false;
        }

        //年度の数値チェック
        try {
            Integer.parseInt(yearDestination);
            Integer.parseInt(yearSource);
        } catch (NumberFormatException ex) {
            return false;
        }

        if (yearDestination.equals(yearSource)) {
            return false;
        }

        //月の範囲チェック
        if (month.compareTo(BigDecimal.ZERO) <= 0 || month.compareTo(new BigDecimal(12)) > 0) {
            return false;
        }

        return true;

    }

    /**
     * 機器IDとポイント番号の相関チェック
     *
     * @param smId
     * @param pointNo
     * @return
     */
    public static boolean validateSmidAndPointNo(Long smId, String pointNo) {
        if (CheckUtility.isNullOrEmpty(pointNo)) {
            return true;
        } else if (smId == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 昼夜区分の入力チェック
     *
     * @param dayAndNightType
     * @param requiredFlg
     * @return
     */
    public static boolean validateDayAndNightType(String dayAndNightType, boolean requiredFlg) {
        if (requiredFlg && CheckUtility.isNullOrEmpty(dayAndNightType)) {
            return false;
        }

        if (CheckUtility.isNullOrEmpty(dayAndNightType)) {
            return true;
        } else if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.DAY_AND_NIGHT_TYPE.getName(dayAndNightType))) {
            //名称が取得できない
            return false;
        } else {
            return true;
        }
    }

    /**
     * 供給区分の入力チェック
     *
     * @param engSupplyType
     * @param requiredFlg
     * @return
     */
    public static boolean validateEngSupplyType(String engSupplyType, boolean requiredFlg) {
        if (requiredFlg && CheckUtility.isNullOrEmpty(engSupplyType)) {
            return false;
        }

        if (CheckUtility.isNullOrEmpty(engSupplyType)) {
            return true;
        } else if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ENG_SUPPLY_TYPE.getName(engSupplyType))) {
            //名称が取得できない
            return false;
        } else {
            return true;
        }
    }

    /**
     * カレンダ年月の入力チェック
     *
     * @param calYmFrom
     * @param calYmTo
     * @return
     */
    public static boolean validateCalYmFromTo(String calYmFrom, String calYmTo) {
        if (!CheckUtility.isNullOrEmpty(calYmTo) && CheckUtility.isNullOrEmpty(calYmFrom)) {
            return false;
        }

        if (!CheckUtility.isNullOrEmpty(calYmFrom) && DateUtility.conversionDate(calYmFrom, DateUtility.DATE_FORMAT_YYYYMM) == null) {
            return false;
        }

        if (!CheckUtility.isNullOrEmpty(calYmTo) && DateUtility.conversionDate(calYmTo, DateUtility.DATE_FORMAT_YYYYMM) == null) {
            return false;
        }

        return true;
    }

    /**
     * ログインパーソンIDの入力チェック
     *
     * @param loginPersonId
     * @return
     */
    private static boolean validateLoginPersonId(String loginPersonId) {
        return !CheckUtility.isNullOrEmpty(loginPersonId);
    }

    /**
     * 自企業の入力チェック
     *
     * @param loginUserCorpId
     * @return
     */
    private static boolean validateLoginUserCorpId(String loginUserCorpId) {
        return !CheckUtility.isNullOrEmpty(loginUserCorpId);
    }

    /**
     * 操作中企業の入力チェック
     *
     * @param corpId
     * @return
     */
    private static boolean validateCorpId(String corpId) {
        return !CheckUtility.isNullOrEmpty(corpId);
    }

    /**
     * 建物IDの入力チェック
     *
     * @param buildingId
     * @return
     */
    private static boolean validateBuildingId(Long buildingId) {
        return buildingId != null;
    }

    /**
     * 建物・テナント絞込みの入力チェック
     *
     * @param buildingFilter
     * @param parentGroupId
     * @param buildingNo
     * @return
     */
    private static boolean validateBuildingFilter(String buildingFilter, Long parentGroupId, String buildingNo) {

        if (CheckUtility.isNullOrEmpty(buildingFilter)) {
            return false;
        }

        //建物・テナント絞込みの入力チェック
        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.BUILDING_FILTER.getName(buildingFilter))) {
            //名称が取得できない
            return false;
        }

        //建物・テナント絞込みの内容による入力チェック
        if (ApiCodeValueConstants.BUILDING_FILTER.GROUP.getVal().equals(buildingFilter)) {
            if (parentGroupId == null || ApiSimpleConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(String.valueOf(parentGroupId))) {
                return false;
            }
        } else if (ApiCodeValueConstants.BUILDING_FILTER.NO.getVal().equals(buildingFilter)) {
            if (CheckUtility.isNullOrEmpty(buildingNo)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 系統グループIDの入力チェック
     *
     * @param lineGroupId
     * @return
     */
    public static boolean validateLineGroupId(Long lineGroupId) {
        return lineGroupId != null;
    }

    /**
     * 系統番号の入力チェック
     *
     * @param line
     * @return
     */
    private static boolean validateLineNo(String lineNo) {
        return !CheckUtility.isNullOrEmpty(lineNo);
    }

    /**
     * 集計日の入力チェック
     *
     * @param summaryKind
     * @return
     */
    private static boolean validateSummaryKind(String summaryKind) {

        if (CheckUtility.isNullOrEmpty(summaryKind)) {
            //設定されていない
            return false;
        }
        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.SUMMARY_KIND.getName(summaryKind))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * 集計期間計算方法の入力チェック
     *
     * @param type
     * @return
     */
    private static boolean validateType(String type) {

        if (CheckUtility.isNullOrEmpty(type)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(type))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * 指定精度未満の制御の入力チェック
     *
     * @param control
     * @return
     */
    private static boolean validateControl(String control) {

        if (CheckUtility.isNullOrEmpty(control)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(control))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * 集計範囲指定方法の入力チェック
     *
     * @param rangeUnit
     * @return
     */
    private static boolean validateRangeUnit(String rangeUnit) {

        if (CheckUtility.isNullOrEmpty(rangeUnit)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.RANGE_UNIT.getName(rangeUnit))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * 集計範囲指定方法（月報・日指定用）の入力チェック
     *
     * @param rangeUnit
     * @return
     */
    private static boolean validateRangeUnitMonthly(String rangeUnit) {

        if (CheckUtility.isNullOrEmpty(rangeUnit)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.RANGE_UNIT_MONTHLY.getName(rangeUnit))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * 集計期間の入力チェック
     *
     * @param range
     * @return
     */
    private static boolean validateRange(BigDecimal range) {
        return range != null;
    }

    /**
     * 比較対象の入力チェック
     *
     * @param compareTarget
     * @return
     */
    private static boolean validateCompareTarget(String compareTarget) {

        if (CheckUtility.isNullOrEmpty(compareTarget)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.COMPARE_TARGET.getName(compareTarget))) {
            //名称が取得できない
            return false;
        }

        return true;
    }

    /**
     * ソートキーの入力チェック
     *
     * @param sortKey
     * @return
     */
    private static boolean validateSortKey(String sortKey) {
        if (CheckUtility.isNullOrEmpty(sortKey)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.getName(sortKey))) {
            //名前が取得できない
            return false;
        }

        return true;
    }

    /**
     * ソート順の入力チェック
     *
     * @param sortOrder
     * @return
     */
    private static boolean validateSortOrder(String sortOrder) {

        if (CheckUtility.isNullOrEmpty(sortOrder)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.SORT_ORDER.getName(sortOrder))) {
            //名前が取得できない
            return false;
        }

        return true;
    }

    /**
     * 全体・サマリーの取得対象の入力チェック
     *
     * @param reportTarget
     * @return
     */
    private static boolean validateAllSummaryReportTarget(String reportTarget) {

        if (CheckUtility.isNullOrEmpty(reportTarget)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ALL_SUMMARY_REPORT_TARGET.getName(reportTarget))) {
            //名前が取得できない
            return false;
        }

        return true;
    }

    /**
     * 全体・サマリーのＸＸＸのＸＸＸＸ評価取得対象の入力チェック
     *
     * @param reportTarget
     * @param analysisTarget
     * @return
     */
    private static boolean validateAllSummaryAnalysisTarget(String reportTarget, String analysisTarget) {

        if (ApiCodeValueConstants.ALL_SUMMARY_REPORT_TARGET.BUILDING.getVal().equals(reportTarget)) {
            return true;
        }

        if (CheckUtility.isNullOrEmpty(analysisTarget)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ALL_SUMMARY_ANALYSIS_TARGET.getName(analysisTarget))) {
            //名前が取得できない
            return false;
        }

        return true;
    }

    /**
     * 全体・サマリーの建物・テナント一覧評価対象の入力チェック
     *
     * @param reportTarget
     * @param buildingTarget
     * @return
     */
    private static boolean validateBuildingTarget(String reportTarget, String buildingTarget) {
        if (ApiCodeValueConstants.ALL_SUMMARY_REPORT_TARGET.ANALYSIS.getVal().equals(reportTarget)) {
            return true;
        }

        if (CheckUtility.isNullOrEmpty(buildingTarget)) {
            //設定されていない
            return false;
        }

        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ALL_SUMMARY_DETAIL_KIND.getName(buildingTarget))) {
            //名前が取得できない
            return false;
        }

        return true;
    }

    /**
     * シーケンス系数値の入力チェック
     *
     * @param longIdString
     * @return
     */
    public static boolean validateLongId(String longIdString) {
        if (CheckUtility.isNullOrEmpty(longIdString)) {
            //設定されていない
            return false;
        }
        try {
            Long.parseLong(longIdString);
        } catch (NumberFormatException ex) {
            //Long型に変換できない
            return false;
        }

        return true;
    }
}
