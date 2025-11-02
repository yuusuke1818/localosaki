package jp.co.osaki.osol.mng;

import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 機器制御用 レスポンス
 *
 * @author shimizu
 *
 * @param <T>
 */
public class FvpCtrlMngResponse<T extends BaseParam> {

	/**
	 * 結果コード
	 */
	private String fvpResultCd;

	/**
	 * 伝送コード
	 */
	private String commandCd;

	/**
	 * 機器ID
	 */
	private Long smId;

	/**
	 * 製品コード
	 */
	private String productCd;

	/**
	 * SMアドレス
	 */
	private String smAddress;

	/**
	 * IPアドレス
	 */
	private String ipAddress;

	/**
	 * ユーザーID
	 */
	private Long userId;

	/**
	 * パーソンID
	 */
	private String personId;

	/**
	 * 企業ID
	 */
	private String corpId;

	/**
	 * 伝送パラメータ
	 */
	private T param;


	public String getFvpResultCd() {
		return fvpResultCd;
	}

	public void setFvpResultCd(String fvpResultCd) {
		this.fvpResultCd = fvpResultCd;
	}

	public String getCommandCd() {
		return commandCd;
	}

	public void setCommandCd(String commandCd) {
		this.commandCd = commandCd;
	}

	public Long getSmId() {
		return smId;
	}

	public void setSmId(Long smId) {
		this.smId = smId;
	}

	public String getProductCd() {
		return productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public String getSmAddress() {
		return smAddress;
	}

	public void setSmAddress(String smAddress) {
		this.smAddress = smAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

}
