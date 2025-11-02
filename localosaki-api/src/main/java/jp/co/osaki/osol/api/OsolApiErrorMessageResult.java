package jp.co.osaki.osol.api;

/**
*
* OSOL API エラーメッセージ クラス.
*
* @author take_suzuki
*
*/
public final class OsolApiErrorMessageResult extends OsolApiResult {

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * @return エラーメッセージ
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage セットする エラーメッセージ
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
