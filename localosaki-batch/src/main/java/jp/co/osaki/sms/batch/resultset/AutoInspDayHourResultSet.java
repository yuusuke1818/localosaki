package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspDayHourResultSet {
    /* 自動検針日（0：なし　/日の指定：1から31） */
    private String day;
    /* 自動検針時（0から23） */
    private BigDecimal hour;
    public AutoInspDayHourResultSet(String day, BigDecimal hour) {
        super();
        this.day = day;
        this.hour = hour;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public BigDecimal getHour() {
        return hour;
    }
    public void setHour(BigDecimal hour) {
        this.hour = hour;
    }

}
