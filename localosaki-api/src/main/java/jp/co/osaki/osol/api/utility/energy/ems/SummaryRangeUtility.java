/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.energy.ems;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForWeekResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * 集計範囲取得ユーティリティクラス
 *
 * @author ya-ishida
 */
public class SummaryRangeUtility {

    /**
     * デマンド情報日報用集計範囲取得
     *
     * @param date 年月日（yyyyMMdd）
     * @param time 基点となる時刻（HH00）
     * @param type 集計期間計算方法　0：から　1：を中心に 2：まで
     * @param range 集計期間（単位：時間）
     * @return 集計範囲のResultSet
     * @throws java.text.ParseException
     */
    public static SummaryRangeForDayResult getSummaryRangeForDay(String date, String time, String type,
            BigDecimal range) {

        SummaryRangeForDayResult result;
        String rangeFrom;
        String rangeTo;
        Date measurementDateFrom;
        Date measurementDateTo;
        BigDecimal jigenNoFrom;
        BigDecimal jigenNoTo;
        Date baseDate;
        Date startDate;
        Date endDate;

        //集計時刻と計測日時行のMap作成
        Map<String, BigDecimal> rowMap;
        String keyTime;
        int count;
        keyTime = "0000";
        rowMap = new HashMap<>();

        for (count = 1; count <= 48; count++) {
            if (count == 1) {
                //1件目の場合keyTimeの検索が不要
                rowMap.put(keyTime, new BigDecimal(count));
            } else if (count % 2 == 1) {
                //奇数行の場合、keyTimeの1文字目、2文字目を1加算、3文字目、4文字目を"00"に変更
                keyTime = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        Integer.parseInt(keyTime.substring(0, 2)) + 1).concat("00");
                rowMap.put(keyTime, new BigDecimal(count));
            } else {
                //偶数行の場合、keyTimeの3文字目、4文字目を"30"に変更
                keyTime = keyTime.substring(0, 2).concat("30");
                rowMap.put(keyTime, new BigDecimal(count));
            }
        }

        //集計期間計算方法により、集計期間を算出する
        if (CheckUtility.isNullOrEmpty(type) || CheckUtility.isNullOrEmpty(date) || CheckUtility.isNullOrEmpty(time)) {
            rangeFrom = null;
            rangeTo = null;
            measurementDateFrom = null;
            jigenNoFrom = null;
            measurementDateTo = null;
            jigenNoTo = null;
        } else {
            baseDate = DateUtility.conversionDate(date.concat(time), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

            //開始日時を算出する
            if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(type)) {
                startDate = baseDate;
            } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.CENTER.getVal().equals(type)) {
                //rangeの半分の時間を引く（割り切れない場合は切り捨て）
                startDate = DateUtility.plusHour(baseDate, range.divide(new BigDecimal(2))
                        .setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(-1)).intValue());
            } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal().equals(type)) {
                //range時間引く
                startDate = DateUtility.plusHour(baseDate, range.intValue() * -1);
            } else {
                startDate = null;
            }

            if (null == startDate) {
                rangeFrom = null;
                rangeTo = null;
                measurementDateFrom = null;
                jigenNoFrom = null;
                measurementDateTo = null;
                jigenNoTo = null;
            } else {
                //endDateはstartDateの集計範囲-1時間と30分後
                endDate = DateUtility.plusHour(startDate, range.intValue() - 1);
                endDate = DateUtility.plusMinute(endDate, 30);
                rangeFrom = DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                rangeTo = DateUtility.changeDateFormat(endDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                measurementDateFrom = DateUtility.conversionDate(
                        DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD);
                jigenNoFrom = rowMap.get(rangeFrom.substring(8));
                measurementDateTo = DateUtility.conversionDate(
                        DateUtility.changeDateFormat(endDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD);
                jigenNoTo = rowMap.get(rangeTo.substring(8));

            }
        }

        result = new SummaryRangeForDayResult(rangeFrom, rangeTo, measurementDateFrom, measurementDateTo, jigenNoFrom,
                jigenNoTo);
        return result;
    }

    /**
     * デマンド情報週報用集計範囲取得
     *
     * @param fiscalYear 年度
     * @param weekNo 週番号
     * @param week54ExistList 54週を持っている年度のリスト
     * @param type　集計期間計算方法　0：から　1：を中心に 2：まで
     * @param rangeUnit　集計範囲の単位　0：年単位　1：週単位
     * @param range　集計期間
     * @return 集計範囲のResultSet
     */
    public static SummaryRangeForWeekResult getSummaryRangeForWeek(String fiscalYear, BigDecimal weekNo,
            List<String> week54ExistList,
            String type, String rangeUnit, BigDecimal range) {

        SummaryRangeForWeekResult result;
        BigDecimal fiscalYearFrom;
        BigDecimal fiscalYearTo;
        BigDecimal weekNoFrom;
        BigDecimal weekNoTo;
        BigDecimal backRangeByWeek;
        BigDecimal moveRangeByWeek;
        BigDecimal rangeByWeek;

        if (CheckUtility.isNullOrEmpty(type) || CheckUtility.isNullOrEmpty(rangeUnit)) {
            result = new SummaryRangeForWeekResult(null, null, null, null);
            return result;
        }

        if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(rangeUnit) && BigDecimal.ONE.compareTo(range) == 0) {
            result = new SummaryRangeForWeekResult(fiscalYear, fiscalYear, weekNo, weekNo);
            return result;
        }

        //集計単位を週単位に変換する
        if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(rangeUnit)) {
            //週単位の場合はそのまま
            rangeByWeek = range;
        } else {
            //それ以外は53週をかける
            rangeByWeek = new BigDecimal(53).multiply(range);
        }

        //基点を設定する
        fiscalYearFrom = new BigDecimal(fiscalYear);
        fiscalYearTo = new BigDecimal(fiscalYear);
        weekNoFrom = weekNo;
        weekNoTo = weekNo;

        //さかのぼる週、進む週を設定する
        if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(type)) {
            //からの場合
            backRangeByWeek = BigDecimal.ZERO;
            moveRangeByWeek = rangeByWeek.subtract(BigDecimal.ONE);
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.CENTER.getVal().equals(type)) {
            //を中心にの場合
            if (rangeByWeek.intValue() % 2 == 1) {
                //奇数の場合
                backRangeByWeek = rangeByWeek.divide(new BigDecimal(2), 0, BigDecimal.ROUND_DOWN);
                moveRangeByWeek = rangeByWeek.divide(new BigDecimal(2), 0, BigDecimal.ROUND_DOWN);
            } else {
                //偶数の場合
                backRangeByWeek = rangeByWeek.divide(new BigDecimal(2));
                moveRangeByWeek = rangeByWeek.divide(new BigDecimal(2)).subtract(BigDecimal.ONE);
            }
        } else {
            //までの場合
            backRangeByWeek = rangeByWeek.subtract(BigDecimal.ONE);
            moveRangeByWeek = BigDecimal.ZERO;
        }

        //指定の数だけさかのぼる
        while (backRangeByWeek.compareTo(BigDecimal.ZERO) > 0) {

            if (weekNoFrom.compareTo(BigDecimal.ONE) == 0) {
                //1週の場合
                fiscalYearFrom = fiscalYearFrom.subtract(BigDecimal.ONE);
                Boolean existFlg = Boolean.FALSE;
                for (String year : week54ExistList) {
                    if (year.equals(fiscalYearFrom.toString())) {
                        weekNoFrom = new BigDecimal(54);
                        existFlg = Boolean.TRUE;
                    }
                }
                if (!existFlg) {
                    weekNoFrom = new BigDecimal(53);
                }
                backRangeByWeek = backRangeByWeek.subtract(BigDecimal.ONE);
            } else if (weekNoFrom.compareTo(new BigDecimal(54)) == 0) {
                weekNoFrom = weekNoFrom.subtract(BigDecimal.ONE);
                if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(rangeUnit)) {
                    backRangeByWeek = backRangeByWeek.subtract(BigDecimal.ONE);
                }
            } else {
                //それ以外の場合
                weekNoFrom = weekNoFrom.subtract(BigDecimal.ONE);
                backRangeByWeek = backRangeByWeek.subtract(BigDecimal.ONE);
            }

        }

        //指定の数だけ前へ進む
        while (moveRangeByWeek.compareTo(BigDecimal.ZERO) > 0) {

            if (weekNoTo.compareTo(new BigDecimal(54)) == 0) {
                //54週の場合
                fiscalYearTo = fiscalYearTo.add(BigDecimal.ONE);
                weekNoTo = BigDecimal.ONE;
                if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(rangeUnit)) {
                    moveRangeByWeek = moveRangeByWeek.subtract(BigDecimal.ONE);
                }
            } else if (weekNoTo.compareTo(new BigDecimal(53)) == 0) {
                //53週の場合
                Boolean existFlg = Boolean.FALSE;
                for (String year : week54ExistList) {
                    if (year.equals(fiscalYearTo.toString())) {
                        existFlg = Boolean.TRUE;
                        //54週が存在する場合
                        weekNoTo = weekNoTo.add(BigDecimal.ONE);
                        moveRangeByWeek = moveRangeByWeek.subtract(BigDecimal.ONE);
                        if (moveRangeByWeek.compareTo(BigDecimal.ZERO) == 0
                                && ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal().equals(rangeUnit)) {
                            //最終週かつ年単位の場合、もう1週進める
                            weekNoTo = BigDecimal.ONE;
                            fiscalYearTo = fiscalYearTo.add(BigDecimal.ONE);
                        }
                    }
                }
                if (!existFlg) {
                    //54週がない場合
                    fiscalYearTo = fiscalYearTo.add(BigDecimal.ONE);
                    weekNoTo = BigDecimal.ONE;
                    moveRangeByWeek = moveRangeByWeek.subtract(BigDecimal.ONE);
                }
            } else if (weekNoTo.compareTo(new BigDecimal(52)) == 0) {
                //52週の場合
                weekNoTo = weekNoTo.add(BigDecimal.ONE);
                if (moveRangeByWeek.compareTo(BigDecimal.ONE) == 0
                        && ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal().equals(rangeUnit)) {
                    //最後の1週かつ年単位の場合
                    for (String year : week54ExistList) {
                        if (year.equals(fiscalYearTo.toString())) {
                            weekNoTo = weekNoTo.add(BigDecimal.ONE);
                        }
                    }
                }
                moveRangeByWeek = moveRangeByWeek.subtract(BigDecimal.ONE);
            } else {
                //それ以外の場合
                weekNoTo = weekNoTo.add(BigDecimal.ONE);
                moveRangeByWeek = moveRangeByWeek.subtract(BigDecimal.ONE);
            }
        }

        result = new SummaryRangeForWeekResult(fiscalYearFrom.toString(), fiscalYearTo.toString(), weekNoFrom,
                weekNoTo);
        return result;
    }

    /**
     * デマンド情報月報用集計範囲取得
     *
     * @param date　年月日（yyyyMMdd）
     * @param type　集計期間計算方法　0：から　1：を中心に 2：まで
     * @return 集計範囲のResultSet
     */
    public static SummaryRangeForMonthResult getSummaryRangeForMonth(String date, String type) {
        return getSummaryRangeForMonth(date, type, BigDecimal.ONE);
    }

    /**
     * デマンド情報月報用集計範囲取得
     *
     * @param date　年月日（yyyyMMdd）
     * @param type　集計期間計算方法　0：から　1：を中心に 2：まで
     * @param range　集計期間（月単位）
     * @return 集計範囲のResultSet
     */
    public static SummaryRangeForMonthResult getSummaryRangeForMonth(String date, String type, BigDecimal range) {
        SummaryRangeForMonthResult result;

        if (CheckUtility.isNullOrEmpty(date) || CheckUtility.isNullOrEmpty(type)) {
            result = new SummaryRangeForMonthResult(null, null);
            return result;
        }

        Date startDate = DateUtility.conversionDate(date, DateUtility.DATE_FORMAT_YYYYMMDD);
        Date endDate;

        if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(type)) {
            //からの場合
            endDate = DateUtility.plusMonth(startDate, range.intValue());
            if (DateUtility.getNumberOfDays(startDate) != Integer
                    .parseInt(DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_DD))
                    || DateUtility.getNumberOfDays(startDate) <= DateUtility.getNumberOfDays(endDate)) {
                //月末日以外または開始日の月の日数<=終了日の月の日数の場合、1日減らす
                endDate = DateUtility.plusDay(endDate, -1);
            }

        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.CENTER.getVal().equals(type)) {
            //を中心にの場合
            BigDecimal tempRange = range.divide(new BigDecimal(2), 0, BigDecimal.ROUND_DOWN);
            BigDecimal halfOfMonth;
            if (range.intValue() % 2 == 0) {
                //偶数の場合
                startDate = DateUtility.plusMonth(startDate, tempRange.intValue() * -1);
                endDate = DateUtility.plusMonth(startDate, range.intValue());
                if (DateUtility.getNumberOfDays(startDate) != Integer
                        .parseInt(DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_DD))
                        || DateUtility.getNumberOfDays(startDate) <= DateUtility.getNumberOfDays(endDate)) {
                    //月末日以外または開始日の月の日数<=終了日の月の日数の場合、1日減らす
                    endDate = DateUtility.plusDay(endDate, -1);
                }
            } else {
                //奇数の場合
                startDate = DateUtility.plusMonth(startDate, tempRange.intValue() * -1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                halfOfMonth = new BigDecimal(cal.getActualMaximum(Calendar.DATE)).divide(new BigDecimal(2), 0,
                        BigDecimal.ROUND_DOWN);
                if (new BigDecimal(cal.get(Calendar.DATE)).compareTo(halfOfMonth) < 0) {
                    cal.setTime(DateUtility.plusMonth(startDate, -1));
                    halfOfMonth = new BigDecimal(cal.getActualMaximum(Calendar.DATE)).divide(new BigDecimal(2), 0,
                            BigDecimal.ROUND_DOWN);
                }
                startDate = DateUtility.plusDay(startDate, halfOfMonth.intValue() * -1);
                endDate = DateUtility.plusMonth(startDate, range.intValue());
                if (DateUtility.getNumberOfDays(startDate) != Integer
                        .parseInt(DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_DD))
                        || DateUtility.getNumberOfDays(startDate) <= DateUtility.getNumberOfDays(endDate)) {
                    //月末日以外または開始日の月の日数<=終了日の月の日数の場合、1日減らす
                    endDate = DateUtility.plusDay(endDate, -1);
                }
            }
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal().equals(type)) {
            //までの場合
            endDate = startDate;
            if (DateUtility.getNumberOfDays(startDate) == Integer
                    .parseInt(DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_DD))) {
                //月末日の場合
                startDate = DateUtility.plusMonth(startDate, (range.intValue() - 1) * -1);
                startDate = DateUtility.conversionDate(
                        DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_YYYYMM).concat("01"),
                        DateUtility.DATE_FORMAT_YYYYMMDD);
            } else {
                startDate = DateUtility.plusDay(DateUtility.plusMonth(startDate, range.intValue() * -1), 1);
            }

        } else {
            result = new SummaryRangeForMonthResult(null, null);
            return result;
        }

        return new SummaryRangeForMonthResult(startDate, endDate);
    }

    /**
     * デマンド情報年報用集計範囲取得
     *
     * @param year 年（yyyy）
     * @param month　月（MM）
     * @param type　集計期間計算方法　0：から　1：を中心に 2：まで
     * @param range　集計期間（年単位）
     * @return
     */
    public static SummaryRangeForYearResult getSummaryRangeForYear(String year, BigDecimal month, String type,
            BigDecimal range) {

        SummaryRangeForYearResult result;

        if (CheckUtility.isNullOrEmpty(year) || month == null || CheckUtility.isNullOrEmpty(type)) {
            result = new SummaryRangeForYearResult(null, null, null, null);
            return result;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.set(Integer.parseInt(year), month.intValue() - 1, 1);

        //月単位の集計範囲に換算する
        BigDecimal rangeByMonth = range.multiply(new BigDecimal(12));

        //開始年月を取得する
        if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(type)) {
            //からの場合は処理なし
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.CENTER.getVal().equals(type)) {
            //を中心に
            startCal.add(Calendar.MONTH,
                    rangeByMonth.divide(new BigDecimal(2)).multiply(new BigDecimal(-1)).intValue());
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal().equals(type)) {
            //まで
            startCal.add(Calendar.MONTH, (rangeByMonth.intValue() - 1) * -1);
        } else {
            result = new SummaryRangeForYearResult(null, null, null, null);
            return result;
        }

        //終了年月を取得する
        Calendar endCal = (Calendar) startCal.clone();
        endCal.add(Calendar.MONTH, rangeByMonth.intValue() - 1);

        result = new SummaryRangeForYearResult(
                DateUtility.changeDateFormat(new Date(startCal.getTimeInMillis()), DateUtility.DATE_FORMAT_YYYY),
                DateUtility.changeDateFormat(new Date(endCal.getTimeInMillis()), DateUtility.DATE_FORMAT_YYYY),
                new BigDecimal(
                        DateUtility.changeDateFormat(new Date(startCal.getTimeInMillis()), DateUtility.DATE_FORMAT_MM)),
                new BigDecimal(
                        DateUtility.changeDateFormat(new Date(endCal.getTimeInMillis()), DateUtility.DATE_FORMAT_MM)));
        return result;
    }

    /**
     * デマンド情報月報用集計範囲取得（日指定）
     *
     * @param date
     * @param type
     * @param range 日単位
     * @return
     */
    public static SummaryRangeForMonthResult getSummaryRangeForMonthRangeDaily(String date, String type,
            BigDecimal range) {

        SummaryRangeForMonthResult result;

        if (CheckUtility.isNullOrEmpty(date) || CheckUtility.isNullOrEmpty(type)) {
            result = new SummaryRangeForMonthResult(null, null);
            return result;
        }

        Date startDate = DateUtility.conversionDate(date, DateUtility.DATE_FORMAT_YYYYMMDD);
        Date endDate;

        if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(type)) {
            //からの場合
            endDate = DateUtility.plusDay(startDate, range.intValue() - 1);
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.CENTER.getVal().equals(type)) {
            //を中心にの場合
            startDate = DateUtility.plusDay(startDate, (range.divide(new BigDecimal("2"), 0, BigDecimal.ROUND_DOWN))
                    .multiply(new BigDecimal(-1)).intValue());
            endDate = DateUtility.plusDay(startDate, range.intValue() - 1);
        } else if (ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal().equals(type)) {
            //までの場合
            endDate = startDate;
            startDate = DateUtility.plusDay(endDate,
                    (range.subtract(BigDecimal.ONE)).multiply(new BigDecimal(-1)).intValue());
        } else {
            result = new SummaryRangeForMonthResult(null, null);
            return result;
        }

        return new SummaryRangeForMonthResult(startDate, endDate);
    }

}
