package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
*
* PING(取得) Param クラス.
*
* @author Takemura
*
*/
public class A200001Param extends BaseParam {

	/**
	 *  ipアドレス
	 */
	@NotNull
	@Pattern(regexp = "[.0-9]{1,15}")
	private String ipAddress;

	/**
	 *  回数
	 */
	@Pattern(regexp = "[1-9]")
	private String count;

	/**
	 *  疎通結果コード
	 */
	private String communicationResultCd;

	/**
	 * 疎通結果
	 */
	private String communicationResult;

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}

	public String getCommunicationResultCd() {
		return communicationResultCd;
	}
	public void setCommunicationResultCd(String communicationResultCd) {
		this.communicationResultCd = communicationResultCd;
	}
	public String getCommunicationResult() {
		return communicationResult;
	}
	public void setCommunicationResult(String communicationResult) {
		this.communicationResult = communicationResult;
	}

}
