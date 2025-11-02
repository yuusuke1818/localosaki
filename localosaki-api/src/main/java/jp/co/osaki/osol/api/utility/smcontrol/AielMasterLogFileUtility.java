package jp.co.osaki.osol.api.utility.smcontrol;

/**
 * Aiel Master ログファイル Utiltiyクラス
 *
 * @author t_hayama
 */
public class AielMasterLogFileUtility {

    /**
     * 拡張子
     */
    private String extension;

    /**
     * 機器ID
     */
    private Long smId;

    /**
     * 要求日付 (YYMMDDhhmm)
     */
    private String reqDateTime;

    /**
     * 応答日付 (YYMMDDhhmm)
     */
    private String resDateTime;

    /**
     * コマンド
     */
    private String command;


    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSmId() {
        return smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
    }

    public String getReqDayTime() {
        return reqDateTime;
    }

    public void setReqDayTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getResDayTime() {
        return resDateTime;
    }

    public void setResDayTime(String resDateTime) {
        this.resDateTime = resDateTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
