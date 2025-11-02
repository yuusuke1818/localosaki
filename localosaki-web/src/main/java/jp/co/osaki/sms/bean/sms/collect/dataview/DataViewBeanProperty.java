package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

/**
 * データ収集装置 データ表示機能 Propertyクラス.
 *
 * @author ozaki.y
 */
@Dependent
public class DataViewBeanProperty implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -3881885446518692488L;

    /** 対象日時フォーマット. */
    private String targetDateTimeFormat;

    /** 対象日時取得間隔. */
    private String targetDateTimeDuration;

    /** 対象日時取得時間数. */
    private String targetDateTimeAddtion;

    /** 画面表示用 データList日時フォーマット. */
    private String listDateTimeFormat;

    /** 帳票出力用 表示日時フォーマット. */
    private String dispDateTimeFormat;

    /** PDFテンプレートファイル名. */
    private String pdfTemplateFileName;

    /** PDFテンプレートファイル名(全装置用). */
    private String pdfTemplateFileNameAllDevice;

    /** タイトル 帳票名. */
    private String titleReport;

    /** タイトル 表示日時. */
    private String titleDispDate;

    /** タイトル 開始日時. */
    private String titleStartDate;

    public String getTargetDateTimeFormat() {
        return targetDateTimeFormat;
    }

    public void setTargetDateTimeFormat(String targetDateTimeFormat) {
        this.targetDateTimeFormat = targetDateTimeFormat;
    }

    public String getTargetDateTimeDuration() {
        return targetDateTimeDuration;
    }

    public void setTargetDateTimeDuration(String targetDateTimeDuration) {
        this.targetDateTimeDuration = targetDateTimeDuration;
    }

    public String getTargetDateTimeAddtion() {
        return targetDateTimeAddtion;
    }

    public void setTargetDateTimeAddtion(String targetDateTimeAddtion) {
        this.targetDateTimeAddtion = targetDateTimeAddtion;
    }

    public String getListDateTimeFormat() {
        return listDateTimeFormat;
    }

    public void setListDateTimeFormat(String listDateTimeFormat) {
        this.listDateTimeFormat = listDateTimeFormat;
    }

    public String getDispDateTimeFormat() {
        return dispDateTimeFormat;
    }

    public void setDispDateTimeFormat(String dispDateTimeFormat) {
        this.dispDateTimeFormat = dispDateTimeFormat;
    }

    public String getPdfTemplateFileName() {
        return pdfTemplateFileName;
    }

    public void setPdfTemplateFileName(String pdfTemplateFileName) {
        this.pdfTemplateFileName = pdfTemplateFileName;
    }

    public String getTitleReport() {
        return titleReport;
    }

    public void setTitleReport(String titleReport) {
        this.titleReport = titleReport;
    }

    public String getTitleDispDate() {
        return titleDispDate;
    }

    public void setTitleDispDate(String titleDispDate) {
        this.titleDispDate = titleDispDate;
    }

    public String getTitleStartDate() {
        return titleStartDate;
    }

    public void setTitleStartDate(String titleStartDate) {
        this.titleStartDate = titleStartDate;
    }

    public String getPdfTemplateFileNameAllDevice() {
        return pdfTemplateFileNameAllDevice;
    }

    public void setPdfTemplateFileNameAllDevice(String pdfTemplateFileNameAllDevice) {
        this.pdfTemplateFileNameAllDevice = pdfTemplateFileNameAllDevice;
    }
}
