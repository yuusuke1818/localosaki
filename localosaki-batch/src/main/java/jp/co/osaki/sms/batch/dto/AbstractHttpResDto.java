package jp.co.osaki.sms.batch.dto;

/**
 * HTTPへのGET/POST処理メソッドの応答情報抽象クラス
 *
 * @author akr_iwamoto
 */
public class AbstractHttpResDto {

	/** 処理結果コード */
	private int result = 0;

	/** HTTPレスポンスコード */
	private int httpResponseCode = 0;

    /**
     * @param result セットする result
     */
	public int getResult() {
		return this.result;
	}

    /**
     * @return result
     */
	public void setResult(int result) {
		this.result = result;
	}

    /**
     * @param httpResponseCode セットする httpResponseCode
     */
	public int getHttpResponseCode() {
		return this.httpResponseCode;
	}

    /**
     * @return httpResponseCode
     */
	public void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
	}

    @Override
	public String toString() {
		return String.format("result=%d, httpResponse=%d", result, httpResponseCode);
	}
}

