package jp.co.osaki.sms.bean.menu;

/**
 * お知らせ/トピックス表示用クラス
 *
 * @author h-shiba
 */
public class OshiraseDisp {

    //レコード無しフラグ
    private boolean nonRecordFlg;

    //マークコード
    private String markCd;

    //アイコンフラグ
    private boolean dispIconFlg;

    //アイコンファイルパス
    private String dispIconFilePath;

    //外部サイトフラグ
    private Integer externalSiteFlg;

    //掲載日
    private String PublishedDay;

    //タイトル
    private String title;

    //URL
    private String url;

    //ファイルパス
    private String fileName;

    //保存ファイル名
    private String saveFileName;

    public Integer getExternalSiteFlg() {
        return externalSiteFlg;
    }

    public void setExternalSiteFlg(Integer externSiteFlg) {
        this.externalSiteFlg = externSiteFlg;
    }

    public boolean getNonRecordFlg() {
        return nonRecordFlg;
    }

    public void setNonRecordFlg(boolean nonRecordFlg) {
        this.nonRecordFlg = nonRecordFlg;
    }

    public String getPublishedDay() {
        return PublishedDay;
    }

    public void setPublishedDay(String PublishedDay) {
        this.PublishedDay = PublishedDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public String getMarkCd() {
        return markCd;
    }

    public void setMarkCd(String markCd) {
        this.markCd = markCd;
    }

    public boolean getDispIconFlg() {
        return dispIconFlg;
    }

    public void setDispIconFlg(boolean dispIconFlg) {
        this.dispIconFlg = dispIconFlg;
    }

    public String getDispIconFilePath() {
        return dispIconFilePath;
    }

    public void setDispIconFilePath(String dispIconFilePath) {
        this.dispIconFilePath = dispIconFilePath;
    }

}
