package jp.co.osaki.sms.batch.resultset;

/***
 * メール停止
 * @author hayashi_tak
 *
 */
public class MPauseMailResultSet {
    private String sendFlg;

    public MPauseMailResultSet(String sendFlg) {
        super();
        this.sendFlg = sendFlg;
    }

    public String getSendFlg() {
        return sendFlg;
    }

    public void setSendFlg(String sendFlg) {
        this.sendFlg = sendFlg;
    }
}
