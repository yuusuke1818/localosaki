package jp.co.osaki.sms.bean.sms.collect.dataview;

/**
 * カレンダー表示データ.
 *
 * @author ozaki.y
 */
public class CalendarData {

    /** 日. */
    private int day;

    /** 日の種別. */
    private String dayKind;

    /** 選択日フラグ. */
    private boolean isSelected;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDayKind() {
        return dayKind;
    }

    public void setDayKind(String dayKind) {
        this.dayKind = dayKind;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
