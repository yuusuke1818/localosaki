package jp.co.osaki.osol.mng;
/**
 * 機器制御API Excptionクラス
 *
 * @author shimizu
 *
 */
public class SmControlException extends Exception {

	/**
	 * エラーコード
	 */
	private String errorCode;

	public SmControlException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public SmControlException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
