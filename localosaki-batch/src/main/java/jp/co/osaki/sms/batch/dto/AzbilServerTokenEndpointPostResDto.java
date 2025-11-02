package jp.co.osaki.sms.batch.dto;

/**
 * アズビルサーバトークン取得 Dtoクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilServerTokenEndpointPostResDto extends AbstractHttpResDto {

    /** アクセストークン */
    private String access_token;

    /** トークン種別 */
    private String token_type;

    /** 有効秒数 */
    private String expires_in;

    /** リフレッシュトークン */
    private String refresh_token;

    /** トークンID */
    private String id_token;

    /** エラー情報 */
    private String errorMessage;

    /**
     * @return access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token セットする access_token
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * @return token_type
     */
    public String getToken_type() {
        return token_type;
    }

    /**
     * @param token_type セットする token_type
     */
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    /**
     * @return expires_in
     */
    public String getExpires_in() {
        return expires_in;
    }

    /**
     * @param expires_in セットする expires_in
     */
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * @return refresh_token
     */
    public String getRefresh_token() {
        return refresh_token;
    }

    /**
     * @param refresh_token セットする refresh_token
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    /**
     * @return id_token
     */
    public String getId_token() {
        return id_token;
    }

    /**
     * @param id_token セットする id_token
     */
    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

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
