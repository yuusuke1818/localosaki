package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import jp.co.osaki.osol.utility.DateUtility;

/**
 * カレンダー表示クラス.
 *
 * @author ozaki.y
 */
public class CalendarBean {

    /** 選択日付. */
    private Date selectedDate;

    /** 選択月. */
    private int selectedMonth;

    /** 表示データList. */
    private List<CalendarData> dateList;

    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * 選択日付設定.
     *
     * @param selectedDate 選択日付
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        this.selectedMonth = Integer.parseInt(DateUtility.changeDateFormat(selectedDate, DateUtility.DATE_FORMAT_M));
        setDateList(createDateList(selectedDate));
    }

    /**
     * 選択年設定.
     *
     * @param selectedYearStr 選択年文字列
     */
    public void setSelectedYear(String selectedYearStr) {
        setSelectedYear(Integer.parseInt(selectedYearStr));
    }

    /**
     * 選択年設定.
     *
     * @param selectedYear 選択年
     */
    public void setSelectedYear(int selectedYear) {
        Date targetDate = getSelectedDate();

        targetDate = DateUtils.setYears(targetDate, selectedYear);

        this.selectedDate = targetDate;
    }

    /**
     * 選択月設定.
     *
     * @param selectedMonthStr 選択月文字列
     */
    public void setSelectedMonth(String selectedMonthStr) {
        setSelectedMonth(Integer.parseInt(selectedMonthStr));
    }

    /**
     * 選択月設定.
     *
     * @param selectedMonth 選択月
     */
    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
        Date targetDate = getSelectedDate();

        targetDate = DateUtils.setMonths(targetDate, (selectedMonth - 1));

        this.selectedDate = targetDate;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * 選択日設定.
     *
     * @param selectedDayStr 選択日文字列
     */
    public void setSelectedDay(String selectedDayStr) {
        setSelectedDay(Integer.parseInt(selectedDayStr));
    }

    /**
     * 選択日設定.
     *
     * @param selectedDay 選択日
     */
    public void setSelectedDay(int selectedDay) {
        Date targetDate = getSelectedDate();

        targetDate = DateUtils.setDays(targetDate, selectedDay);

        setSelectedDate(targetDate);
    }

    /**
     * 選択時刻設定.
     *
     * @param selectedHourStr 選択時文字列
     * @param selectedMinuteStr 選択分文字列
     */
    public void setSelectedTime(String selectedHourStr, String selectedMinuteStr) {
        setSelectedTime(Integer.parseInt(selectedHourStr), Integer.parseInt(selectedMinuteStr), 0);
    }

    /**
     * 選択時刻設定.
     *
     * @param selectedHour 選択時
     * @param selectedMinute 選択分
     */
    public void setSelectedTime(int selectedHour, int selectedMinute) {
        setSelectedTime(selectedHour, selectedMinute, 0);
    }

    /**
     * 選択時刻設定.
     *
     * @param selectedHour 選択時
     * @param selectedMinute 選択分
     * @param selectedSecond 選択秒
     */
    public void setSelectedTime(int selectedHour, int selectedMinute, int selectedSecond) {
        Date targetDate = getSelectedDate();

        targetDate = DateUtils.setHours(targetDate, selectedHour);
        targetDate = DateUtils.setMinutes(targetDate, selectedMinute);
        targetDate = DateUtils.setSeconds(targetDate, selectedSecond);

        setSelectedDate(targetDate);
    }

    /**
     * 選択日付変更.
     */
    public void changeSelectedDate() {
        this.selectedMonth = Integer.parseInt(DateUtility.changeDateFormat(selectedDate, DateUtility.DATE_FORMAT_M));
        setDateList(createDateList(selectedDate));
    }

    /**
     * 日付を翌日に変更.
     */
    public void changeToNextDay() {
        setSelectedDate(DateUtility.plusDay(getSelectedDate(), 1));
    }

    /**
     * 日付を前日に変更.
     */
    public void changeToLastDay() {
        setSelectedDate(DateUtility.plusDay(getSelectedDate(), -1));
    }

    /**
     * 日付を翌月に変更.
     */
    public void changeToNextMonth() {
        setSelectedDate(DateUtility.plusMonth(getSelectedDate(), 1));
    }

    /**
     * 日付を前月に変更.
     */
    public void changeToLastMonth() {
        setSelectedDate(DateUtility.plusMonth(getSelectedDate(), -1));
    }

    /**
     * 日付を翌年に変更.
     */
    public void changeToNextYear() {
        setSelectedDate(DateUtility.plusYear(getSelectedDate(), 1));
    }

    /**
     * 日付を前年に変更.
     */
    public void changeToLastYear() {
        setSelectedDate(DateUtility.plusYear(getSelectedDate(), -1));
    }

    public List<CalendarData> getDateList() {
        return dateList;
    }

    public void setDateList(List<CalendarData> dateList) {
        this.dateList = null;
        this.dateList = dateList;
    }

    /**
     * カレンダー表示データListを生成.
     *
     * @param selectedDate カレンダー選択日
     * @return カレンダー表示データList
     */
    private List<CalendarData> createDateList(Date selectedDate) {
        List<CalendarData> dateList = new ArrayList<>();

        int selectedDay = Integer.parseInt(DateUtility.changeDateFormat(selectedDate, DateUtility.DATE_FORMAT_D));
        int dayCount = Integer.parseInt(
                DateUtility.changeDateFormat(DateUtility.changeDateMonthLast(selectedDate), DateUtility.DATE_FORMAT_D));

        Date targetDate = DateUtility.changeDateMonthFirst(selectedDate);
        for (int day = 1; day <= dayCount; day++) {
            CalendarData data = new CalendarData();
            data.setDay(day);
            data.setDayKind(DateFormatUtils.format(targetDate, "EEEE", Locale.ENGLISH));
            data.setSelected(day == selectedDay);

            dateList.add(data);

            targetDate = DateUtility.plusDay(targetDate, 1);
        }

        int shortageCount = 31 - dateList.size();
        if (shortageCount > 0) {
            for (int i = 0; i < shortageCount; i++) {
                dateList.add(new CalendarData());
            }
        }

        return dateList;
    }
}
