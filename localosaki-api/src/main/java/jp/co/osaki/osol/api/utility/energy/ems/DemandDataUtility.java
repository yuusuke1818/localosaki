/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgMonthlyKensyoDataTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandYearTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventMonthTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventYearTimeDetailResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンドデータ取得ユーティリティクラス
 *
 * @author ya-ishida
 */
public class DemandDataUtility {

    /**
     * デマンド週報企業カレンダ情報から54週がある年度のリストを取得する
     *
     * @param calList
     * @return
     */
    public static List<String> getWeek54ListForCompany(List<DemandWeekCorpCalListDetailResultData> calList) {
        return getWeek54List(
                calList.stream().collect(Collectors.groupingBy(DemandWeekCorpCalListDetailResultData::getFiscalYear,
                        Collectors.mapping(DemandWeekCorpCalListDetailResultData::getWeekNo, Collectors.toList()))));
    }

    /**
     * デマンド週報建物カレンダ情報から54週がある年度のリストを取得する
     *
     * @param calList
     * @return
     */
    public static List<String> getWeek54ListForPower(List<DemandWeekBuildingCalListDetailResultData> calList) {
        return getWeek54List(calList.stream().collect(Collectors.groupingBy(
                DemandWeekBuildingCalListDetailResultData::getFiscalYear,
                Collectors.mapping(DemandWeekBuildingCalListDetailResultData::getWeekNo, Collectors.toList()))));
    }

    /**
     * 54週がある年度のリストを取得する（デマンド週報カレンダをグルーピング後呼び出し）
     *
     * @param calMap
     * @return
     */
    private static List<String> getWeek54List(Map<String, List<BigDecimal>> calMap) {
        List<String> week54ExistList = new ArrayList<>();

        for (Map.Entry<String, List<BigDecimal>> entry : calMap.entrySet()) {
            if (entry.getValue().contains(new BigDecimal(54))) {
                //54週が含まれている場合
                week54ExistList.add(entry.getKey());
            }
        }

        return week54ExistList;
    }

    /**
     * 時間を時限Noに変換する
     *
     * @param hh 00～23の文字列
     * @return 時限No
     */
    public static BigDecimal changeHHToJigenNo(String hh) {
        if (hh == null || "".equals(hh)) {
            return null;
        }

        switch (hh) {
        case "00":
            return new BigDecimal(1);
        case "01":
            return new BigDecimal(3);
        case "02":
            return new BigDecimal(5);
        case "03":
            return new BigDecimal(7);
        case "04":
            return new BigDecimal(9);
        case "05":
            return new BigDecimal(11);
        case "06":
            return new BigDecimal(13);
        case "07":
            return new BigDecimal(15);
        case "08":
            return new BigDecimal(17);
        case "09":
            return new BigDecimal(19);
        case "10":
            return new BigDecimal(21);
        case "11":
            return new BigDecimal(23);
        case "12":
            return new BigDecimal(25);
        case "13":
            return new BigDecimal(27);
        case "14":
            return new BigDecimal(29);
        case "15":
            return new BigDecimal(31);
        case "16":
            return new BigDecimal(33);
        case "17":
            return new BigDecimal(35);
        case "18":
            return new BigDecimal(37);
        case "19":
            return new BigDecimal(39);
        case "20":
            return new BigDecimal(41);
        case "21":
            return new BigDecimal(43);
        case "22":
            return new BigDecimal(45);
        case "23":
            return new BigDecimal(47);
        default:
            return null;
        }
    }

    /**
     * 時限NoをHHMMに変換する
     *
     * @param jigenNo 1～48の時限番号
     * @param hasColonFlg
     * @return 時限時間文字列
     */
    public static String changeJigenNoToHHMM(BigDecimal jigenNo, boolean hasColonFlg) {
        if (jigenNo == null) {
            return null;
        }
        jigenNo = jigenNo.setScale(0);
        int jigenInt = jigenNo.intValue();
        int minute = (jigenInt - 1) * 30;
        String tempDateString = DateUtility.changeDateFormat(new Date(), DateUtility.DATE_FORMAT_YYYYMMDD);
        Date tempDate = DateUtility.conversionDate(tempDateString, DateUtility.DATE_FORMAT_YYYYMMDD);
        Date retDate = DateUtility.plusMinute(tempDate, minute);
        if (hasColonFlg) {
            return DateUtility.changeDateFormat(retDate, DateUtility.DATE_FORMAT_HHMM_COLON);
        }
        return DateUtility.changeDateFormat(retDate, DateUtility.DATE_FORMAT_HHMM);
    }

    /**
     * 時限番号の足し算を行う
     *
     * @param jigenNo 元になる時限番号
     * @param num マイナス可
     * @return
     */
    public static BigDecimal plusJigenNo(BigDecimal jigenNo, int num) {
        BigDecimal maxDecimal = new BigDecimal(48);
        BigDecimal numBigDecimal = new BigDecimal(num);
        BigDecimal plus = jigenNo.add(numBigDecimal);
        while (plus.compareTo(BigDecimal.ZERO) <= 0 || plus.compareTo(maxDecimal) > 0) {
            if (plus.compareTo(BigDecimal.ZERO) <= 0) {
                plus = plus.add(maxDecimal);
            }
            if (plus.compareTo(maxDecimal) > 0) {
                plus = plus.subtract(maxDecimal);
            }
        }
        return plus;
    }

    /**
     * 開始hhmmより24時間分のヘッダーを作成する
     *
     * @param hhmm コロン無しのhhmm
     * @return
     */
    public static List<String> createKensyoDayHeaderList(String hhmm) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            Date startHHMM = DateUtility.conversionDate(hhmm, DateUtility.DATE_FORMAT_HHMM);
            startHHMM = DateUtility.plusMinute(startHHMM, i * 30);
            String header = DateUtility.changeDateFormat(startHHMM, DateUtility.DATE_FORMAT_HHMM_COLON);
            StringBuilder sb = new StringBuilder(header);
            sb.append("～");
            list.add(sb.toString());

        }
        return list;
    }

    /**
     * 検証日報の時間帯別ヘッダ情報を作成する
     *
     * @param summaryRange
     * @return
     */
    public static List<DemandOrgDailyKensyoDataTimeDetailResultData> createDailyKensyoDetailHeader(
            SummaryRangeForDayResult summaryRange) {
        List<DemandOrgDailyKensyoDataTimeDetailResultData> resultList = new ArrayList<>();
        Date curreDate = DateUtility.conversionDate(summaryRange.getRangeFrom(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        BigDecimal currentJigenNo = summaryRange.getJigenNoFrom();
        while (!curreDate
                .after(DateUtility.conversionDate(summaryRange.getRangeTo(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM))) {
            DemandOrgDailyKensyoDataTimeDetailResultData result = new DemandOrgDailyKensyoDataTimeDetailResultData();
            result.setTimeTitle(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_HHMM_COLON)
                    .concat(ApiSimpleConstants.FROM));
            result.setMeasurementDate(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD));
            result.setJigenNo(currentJigenNo);
            resultList.add(result);

            //時間を30分進める
            curreDate = DateUtility.plusMinute(curreDate, 30);
            //時限を変更する
            if (currentJigenNo.compareTo(new BigDecimal("48")) == 0) {
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }
        }

        return resultList;
    }

    /**
     * 検証月報の時間帯別ヘッダ情報を作成する
     *
     * @param summaryRange
     * @return
     */
    public static List<DemandOrgMonthlyKensyoDataTimeDetailResultData> createMonthlyKensyoDetailHeader(
            SummaryRangeForMonthResult summaryRange) {
        List<DemandOrgMonthlyKensyoDataTimeDetailResultData> resultList = new ArrayList<>();
        Date currentDate = summaryRange.getMeasurementDateFrom();
        while (!currentDate.after(summaryRange.getMeasurementDateTo())) {
            DemandOrgMonthlyKensyoDataTimeDetailResultData result = new DemandOrgMonthlyKensyoDataTimeDetailResultData();
            result.setTimeTitle(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
            result.setMeasurementDate(currentDate);
            result.setResultValue(null);
            resultList.add(result);

            //1日進める
            currentDate = DateUtility.plusDay(currentDate, 1);
        }

        return resultList;
    }

    /**
     * イベント月報検証の時間帯別ヘッダ情報を作成する
     *
     * @param summaryRange
     * @return
     */
    public static List<KensyoEventMonthTimeDetailResultData> createEventMonthlyKensyoDetailHeader(
            SummaryRangeForMonthResult summaryRange) {
        List<KensyoEventMonthTimeDetailResultData> resultList = new ArrayList<>();
        Date currentDate = summaryRange.getMeasurementDateFrom();
        while (!currentDate.after(summaryRange.getMeasurementDateTo())) {
            KensyoEventMonthTimeDetailResultData result = new KensyoEventMonthTimeDetailResultData();
            result.setTimeTitle(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
            result.setMeasurementDate(currentDate);
            result.setValue(null);
            resultList.add(result);

            //1日進める
            currentDate = DateUtility.plusDay(currentDate, 1);
        }
        return resultList;
    }

    /**
     * 検証年報の時間帯別ヘッダ情報を作成する
     *
     * @param summaryRange
     * @return
     */
    public static List<KensyoDemandYearTimeDetailResultData> createYearlyKensyoDetailHeader(
            SummaryRangeForYearResult summaryRange) {
        List<KensyoDemandYearTimeDetailResultData> resultList = new ArrayList<>();
        Date curreDate = DateUtility.conversionDate(
                summaryRange.getYearFrom()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getMonthFrom().intValue()))
                        .concat("01"),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        Date lastDate = DateUtility
                .conversionDate(
                        summaryRange.getYearTo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        summaryRange.getMonthTo().intValue()))
                                .concat("01"),
                        DateUtility.DATE_FORMAT_YYYYMMDD);
        while (!curreDate.after(lastDate)) {
            KensyoDemandYearTimeDetailResultData result = new KensyoDemandYearTimeDetailResultData();
            result.setTimeTitle(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            result.setCalY(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_YYYY));
            result.setCalM(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_MM));
            result.setResultValue(null);
            resultList.add(result);

            //1ヶ月進める
            curreDate = DateUtility.plusMonth(curreDate, 1);
        }
        return resultList;
    }

    /**
     * イベント年報検証の時間帯別ヘッダ情報を作成する
     *
     * @param summaryRange
     * @return
     */
    public static List<KensyoEventYearTimeDetailResultData> createEventYearlyKensyoDetailHeader(
            SummaryRangeForYearResult summaryRange) {
        List<KensyoEventYearTimeDetailResultData> resultList = new ArrayList<>();
        Date curreDate = DateUtility.conversionDate(
                summaryRange.getYearFrom()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getMonthFrom().intValue()))
                        .concat("01"),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        Date lastDate = DateUtility
                .conversionDate(
                        summaryRange.getYearTo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        summaryRange.getMonthTo().intValue()))
                                .concat("01"),
                        DateUtility.DATE_FORMAT_YYYYMMDD);
        while (!curreDate.after(lastDate)) {
            KensyoEventYearTimeDetailResultData result = new KensyoEventYearTimeDetailResultData();
            result.setTimeTitle(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            result.setCalYm(DateUtility.changeDateFormat(curreDate, DateUtility.DATE_FORMAT_YYYYMM));
            result.setValue(null);
            result.setJudge(null);
            resultList.add(result);

            //1ヶ月進める
            curreDate = DateUtility.plusMonth(curreDate, 1);
        }
        return resultList;
    }

    /**
     *
     * @param measurementDateFrom
     * @param measurementDateTo
     * @return
     */
    public static List<String> createMonthHeader(Date measurementDateFrom, Date measurementDateTo) {
        List<String> list = new ArrayList<>();
        while (measurementDateTo.after(measurementDateFrom)) {
            list.add(DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_MMDD_SLASH));
            measurementDateFrom = DateUtility.plusDay(measurementDateFrom, 1);
        }
        list.add(DateUtility.changeDateFormat(measurementDateTo, DateUtility.DATE_FORMAT_MMDD_SLASH));
        return list;
    }

    /**
     * 制御状態を2進数で構成される文字列に変換する
     *
     * @param controlStatus
     * @return
     */
    public static String createBinaryStringFromControlStatus(String controlStatus) {

        if (CheckUtility.isNullOrEmpty(controlStatus)) {
            return null;
        }

        int charCount = controlStatus.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            String s = String.valueOf(controlStatus.charAt(i));
            int num = Integer.parseInt(s, 16);//10進数化
            String binaryString = Integer.toBinaryString(num);
            while (binaryString.length() < 4) {
                binaryString = "0" + binaryString;
            }
            sb.append(binaryString);
        }

        return sb.toString();
    }
}
