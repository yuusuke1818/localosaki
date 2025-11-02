package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * FVx間管理情報(設定) Param クラス
 *
 * @autho da_yamano
 *
 */
public class A200015Param extends BaseParam {

	/**
	 * 応答コード
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String answerCd;

	/**
	 * FVPx管理情報設定
	 */
	private Map<String,Object> setManageInfoFVPx;

	@AssertTrue
	public boolean isValidateSetManageInfoFVPx() {
		return setManageInfoFVPx!=null && validateObj(setManageInfoFVPx,  new HashMap<String, String>(){{
			put("numFVPx",  "[0-9]");						// FVPxナンバー
			put("addressFVPx", "XXXX|[0-9]{1,4}");          // FVPxアドレス
			put("executePolling",     "[0-9]");             // ポーリング実行
			put("stateFVPx",   "[0-9]");                    // FVPx運転状態
			put("demandAlert",   "[0-9]");              // デマンド警報発呼
			put("errorCircuitLON",   "[0-9]");          // LON回線異常警報発呼
			put("settingDemand",   "[0-9]");            // デマンド設定変更警報発呼
			put("settingUnit",   "[0-9]");              // ユニット設定変更警報発呼
			put("transferAlert",   "[0-9]");            // 移報警報発呼
			put("batteryErrorAlertFVPx",   "[0-9]");    // FVPx電池異常警報発呼
			put("reserve",   " ");                       // 予備
			put("errorAlertMeasurePointXList","");   // 異常警報発呼計測ポイントX

		}});
	}

	private boolean validateObj(Map<String, Object> obj, Map<String,String> validationPattern) {
		for(Entry<String, String> ent : validationPattern.entrySet()) {
			String key = ent.getKey();

			Object value = obj.get(ent.getKey());
			if(value == null) {
				return false;
			}

			if(value instanceof String) {
				if(!((String)value).matches(ent.getValue())) {
					return false;
				}
			}else if(value instanceof List) {
				// オブジェクト内のリストをバリデーションチェック
				@SuppressWarnings("unchecked")
				List<Map<String,String>> valueList = (List<Map<String,String>>)value;
				if(!this.validateList(valueList, new HashMap<String, String>(){{
					put("inputON",  "[0-9]");				// 入力状態ON
					put("inputOFF", "[0-9]");               // 入力状態OFF
					put("initialState",     "[0-9]");       // 初期状態
					put("noCommunication",   "[0-9]");      // 通信不能
					put("analogMax",   "[0-9]");            // アナログ上限値
					put("analogMin",   "[0-9]");            // アナログ下限値

				}})){
					return false;
				}
			}
		}
		return true;
	}

	public String getAnswerCd() {
		return answerCd;
	}

	public void setAnswerCd(String answerCd) {
		this.answerCd = answerCd;
	}


	public Map<String, Object> getSetManageInfoFVPx() {
		return setManageInfoFVPx;
	}

	public void setSetManageInfoFVPx(Map<String, Object> setManageInfoFVPx) {
		super.setSmAddress((String) setManageInfoFVPx.get("addressFVPx"));
		this.setManageInfoFVPx = setManageInfoFVPx;
	}


}
