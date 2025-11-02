package jp.co.osaki.osol.mng;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 機器制御用 リクエスト
 *
 * @author shimizu
 *
 * @param <T>
 */
public class FvpCtrlMngRequest<T extends BaseParam> {

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

	/**
	 * DB登録フラグ
	 */
	private boolean updateDBflg = false;

	public FvpCtrlMngRequest(SmPrmResultData sm, String corpId, String personId, Long userId) {
		this.smId = sm.getSmId();
		this.productCd = sm.getProductCd();
		this.smAddress = sm.getSmAddress();
		this.ipAddress = sm.getIpAddress();
		this.corpId = corpId;
		this.personId = personId;
		this.userId = userId;
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
		((BaseParam)param).setSmAddress(smAddress);
		this.param = param;
		this.commandCd = param.getCommandCd();
		this.updateDBflg = param.isUpdateDBflg();
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

	public boolean isUpdateDBflg() {
		return updateDBflg;
	}

	public void setUpdateDBflg(boolean updateDBflg) {
		this.updateDBflg = updateDBflg;
	}

}
