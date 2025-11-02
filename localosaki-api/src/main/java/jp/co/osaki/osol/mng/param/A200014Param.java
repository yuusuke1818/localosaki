package jp.co.osaki.osol.mng.param;

import java.util.Map;

/**
 *
 * FVx間管理情報(取得) Param クラス
 *
 * @autho da_yamano
 *
 */
public class A200014Param extends BaseParam {

	/**
	 * 応答コード
	 */
	private String answerCd;

	/**
	 * FVPx管理情報設定
	 */
	private Map<String,String> setManageInfoFVPx;

	/**
	 * FVPxナンバー
	 */
	private String numFVPx;
	/**
	 * FVPx警報状態
	 */
	private Map<String,String> alertStateFVPx;


	public String getAnswerCd() {
		return answerCd;
	}

	public void setAnswerCd(String answerCd) {
		this.answerCd = answerCd;
	}

	public Map<String, String> getSetManageInfoFVPx() {
		return setManageInfoFVPx;
	}

	public void setSetManageInfoFVPx(Map<String, String> setManageInfoFVPx) {

		super.setSmAddress(setManageInfoFVPx.get("addressFVPx"));

		this.setManageInfoFVPx = setManageInfoFVPx;
	}

	public String getNumFVPx() {
		return numFVPx;
	}

	public void setNumFVPx(String numFVPx) {
		this.numFVPx = numFVPx;
	}


	public Map<String, String> getAlertStateFVPx() {
		return alertStateFVPx;
	}

	public void setAlertStateFVPx(Map<String, String> alertStateFVPx) {
		this.alertStateFVPx = alertStateFVPx;
	}

}
