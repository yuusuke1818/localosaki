package jp.co.osaki.osol.api.bean.smcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.lang.SystemUtils;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.PingSelectParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.PingSelectResult;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200001Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * PING(取得) Bean クラス
 *
 * @author Takemura
 *
 */
@Named(value = SmControlConstants.PING_SELECT)
@RequestScoped
public class PingSelectBean extends AbstractApiBean<PingSelectResult,PingSelectParameter>{

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	/*  ping通信は他機能と処理仕様が異なるため、executeメソッドからOverride */
	@Override
	public SmControlApiResponse<PingSelectResult> execute() {

		/*  パラメータの取得 */
		BaseParam param = this.initParam(this.apiParameter);

		/*  入力バリデーション */
		if(validator.validate(param).size()>0) {
			return this.errResponse(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
		}

		Map<String, String> pingResult = new HashMap<String, String>();

		/*  ping実行 */
		try {
			pingResult = pingExecute(((A200001Param) param).getCount(),((A200001Param) param).getIpAddress());
		} catch (SmControlException e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			return this.errResponse(e.getErrorCode());
		}

		/* 結果をセット */
		this.result.setCommunicationResult(pingResult.get(SmControlConstants.PING_RESULT));
		this.result.setCommunicationResultCd(pingResult.get(SmControlConstants.PING_CHECK));


		/* 11 APIレスポンスに結果セットを格納 */
		this.response.setResult(this.result);
		this.response.setResultCode(OsolApiResultCode.API_OK);

		return response;
	}


	@Override
	protected <T extends BaseParam> T initParam(PingSelectParameter parameter) {
		A200001Param param = new A200001Param();

		//リクエスト仕様内、固有の値を設定
		if(param!=null) {
			param.setIpAddress(parameter.getIpAddress());
			param.setCount(parameter.getCount());
		}

		// カウント値が設定されていなければ、デフォルト値設定
		if(param.getCount() == null || param.getCount() == "") {
			param.setCount(SmControlConstants.DEFAULT_COUNT);
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	private Map<String , String>  pingExecute(String count, String ipAddress) throws SmControlException {

		// アドレスの形になっていない場合、例外を返却する。
		if (ipAddress.split("\\.").length != 4) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "ipAddress", ipAddress);
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		// ipアドレス整形 [000.000.000.000 -> 0.0.0.0]
		List<String> addressList = new ArrayList<>();

		for (String address : ipAddress.split("\\.")) {
			String str = String.valueOf(Integer.parseInt(address));
			addressList.add(str);
		}

		String ipStr = addressList.get(0) + "." + addressList.get(1) + "."
				+ addressList.get(2) + "." + addressList.get(3);

		String cmd;
		// windows pingコマンド
		if (SystemUtils.IS_OS_WINDOWS) {
			cmd = "cmd.exe /c ping -n "+ count + " " + ipStr;
		}else {
			cmd = "ping -c "+ count + " " + ipStr;
		}

		String pingStr = "";
		Runtime runtime = Runtime.getRuntime();

		// 例外ログ格納用
		Process p;
		InputStream is = null;
		BufferedReader reader = null;
		// コマンド実行
		try {
			p = runtime.exec(cmd);

			// プロセスの正常終了まで待機させる
			p.waitFor();


			// ping結果を格納
			is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			reader = new BufferedReader(isr);

			for (String line; (line = reader.readLine()) != null;) {
				pingStr = pingStr + line + "\r\n";

			}
		}catch (UnsupportedEncodingException e) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, e);
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			throw new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN,"API_ERROR_UNKNOWN");
		}catch (IOException e) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, e);
			super.loggingError(st, "EXEC_COMMAND",cmd);
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			throw new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN,"API_ERROR_UNKNOWN");
		} catch (InterruptedException e) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, e);
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			throw new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN,"API_ERROR_UNKNOWN");
		}

		// 返却値設定
		Map<String , String> pingResult = new HashMap<String, String>();

		// ping結果設定
		pingResult.put(SmControlConstants.PING_RESULT, pingStr);
		// 実行結果設定
		pingResult.put(SmControlConstants.PING_CHECK, String.valueOf(p.exitValue()));

		return pingResult;

	}
}
