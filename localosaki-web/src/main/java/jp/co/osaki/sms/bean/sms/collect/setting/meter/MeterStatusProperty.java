package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.Dependent;

import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * メーター管理画面 メーター状態登録情報を持つプロパティ
 * @author iwasaki_y
 *
 */
@Dependent
public class MeterStatusProperty implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 4947924855174942809L;

    /** メーター状況  */
    private BigDecimal meterPresentSituation;
    /** メーター状況（表示用）  */
    private String meterPresentSituationDisp;
    /** アラート停止期間 開始日 */
    private String alertPauseStart;
    /** アラート停止期間 開始日（カレンダー用） */
    private Date calAlertPauseStart;
    /** アラート停止期間 終了日 */
    private String alertPauseEnd;
    /** アラート停止期間 終了日（カレンダー用） */
    private Date calAlertPauseEnd;
    /** アラート停止フラグ */
    private BigDecimal alertPauseFlg;
    /** アラート停止フラグ（boolean型） */
    private boolean alertPauseFlgBool;
    /** メーター状態備考 */
    private String meterStatusMemo;
    /** 計器ID（バリデーションチェック時の判定用） */
    private String meterId;

   /**
    * @return meterPresentSituation
    */
   public BigDecimal getMeterPresentSituation() {
       return meterPresentSituation;
   }
   /**
    * @param meterPresentSituation セットする meterPresentSituation
    */
   public void setMeterPresentSituation(BigDecimal meterPresentSituation) {
       this.meterPresentSituation = meterPresentSituation;
   }
   /**
    * @return meterPresentSituationDisp
    */
   public String getMeterPresentSituationDisp() {
       return meterPresentSituationDisp;
   }
   /**
    * @param meterPresentSituationDisp セットする meterPresentSituationDisp
    */
   public void setMeterPresentSituationDisp(String meterPresentSituationDisp) {
       this.meterPresentSituationDisp = meterPresentSituationDisp;
   }
   /**
    * @return alertPauseStart
    */
   public String getAlertPauseStart() {
       if (calAlertPauseStart == null) {
           if (CheckUtility.isNullOrEmpty(alertPauseStart)) {
               return null;
           } else {
               return alertPauseStart;
           }
       }
       return DateUtility.changeDateFormat(this.calAlertPauseStart, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
   }
   /**
    * @param alertPauseStart セットする alertPauseStart
    */
   public void setAlertPauseStart(String alertPauseStart) {
       this.alertPauseStart = alertPauseStart;
   }
   /**
    * @return calAlertPauseStart
    */
   public Date getCalAlertPauseStart() {
       if (this.alertPauseStart == null) {
           return null;
       }
       try {
           SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
           sdf.setLenient(false);
           calAlertPauseStart = sdf.parse(this.alertPauseStart);
       } catch (ParseException e) {
           return null;
       }
       return calAlertPauseStart;
   }
   /**
    * @param calAlertPauseStart セットする calAlertPauseStart
    */
   public void setCalAlertPauseStart(Date calAlertPauseStart) {
       this.calAlertPauseStart = calAlertPauseStart;
   }
   /**
    * @return alertPauseEnd
    */
   public String getAlertPauseEnd() {
       if (calAlertPauseEnd == null) {
           if (CheckUtility.isNullOrEmpty(alertPauseEnd)) {
               return null;
           } else {
               return alertPauseEnd;
           }
       }
       return DateUtility.changeDateFormat(this.calAlertPauseEnd,DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
   }
   /**
    * @param alertPauseEnd セットする alertPauseEnd
    */
   public void setAlertPauseEnd(String alertPauseEnd) {
       this.alertPauseEnd = alertPauseEnd;
   }
   /**
    * @return calAlertPauseEnd
    */
   public Date getCalAlertPauseEnd() {
       if (this.alertPauseEnd == null) {
           return null;
       }
       try {
           SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
           sdf.setLenient(false);
           calAlertPauseEnd = sdf.parse(this.alertPauseEnd);
       } catch (ParseException e) {
           return null;
       }
       return calAlertPauseEnd;
   }
   /**
    * @param calAlertPauseEnd セットする calAlertPauseEnd
    */
   public void setCalAlertPauseEnd(Date calAlertPauseEnd) {
       this.calAlertPauseEnd = calAlertPauseEnd;
   }
   /**
    * @return alertPauseFlg
    */
   public BigDecimal getAlertPauseFlg() {
       return alertPauseFlg;
   }
   /**
    * @param alertPauseFlg セットする alertPauseFlg
    */
   public void setAlertPauseFlg(BigDecimal alertPauseFlg) {
       this.alertPauseFlg = alertPauseFlg;
   }
   /**
    * @return alertPauseFlgBool
    */
   public boolean getAlertPauseFlgBool() {
       return alertPauseFlgBool;
   }
   /**
    * @param alertPauseFlgBool セットする alertPauseFlgBool
    */
   public void setAlertPauseFlgBool(boolean alertPauseFlgBool) {
       this.alertPauseFlgBool = alertPauseFlgBool;
   }
   /**
    * @return meterStatusMemo
    */
   public String getMeterStatusMemo() {
       return meterStatusMemo;
   }
   /**
    * @param meterStatusMemo セットする meterStatusMemo
    */
   public void setMeterStatusMemo(String meterStatusMemo) {
       this.meterStatusMemo = meterStatusMemo;
   }
   /**
    * @return meterId
    */
   public String getMeterId() {
       return meterId;
   }
   /**
    * @param meterId セットする meterId
    */
   public void setMeterId(String meterId) {
       this.meterId = meterId;
   }
}
