package jp.co.osaki.sms.batch.dto;

/**
 * アズビルサーバ計測値送信 Dtoクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilServerSendDayLoadServeyPostResDto extends AbstractHttpResDto {

    /** エラー情報 */
    private String errorMessage;

    /**
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage セットする errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
