package jp.co.osaki.sms.batch.resultset;

/**
 * アラートメール設定 ResultSetクラス
 *
 * @author sagi_h
 *
 */
public class MAlertMailListResultSet {
    /** メールアドレス */
    private String email;

    /**
     * @param email
     */
    public MAlertMailListResultSet(String email) {
        this.email = email;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email セットする email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
