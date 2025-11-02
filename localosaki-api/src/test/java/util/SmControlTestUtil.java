package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class SmControlTestUtil {
	private File CURRENT_LOG_FILE;

	public String callPost(String strPostUrl, String strParam) {
		// Corp ID, Person IDを付加
		strParam = strParam + "&" + SmConstants.PARAM_CORPID + "&" + SmConstants.PARAM_PERSONID + "&" + SmConstants.PARAM_OPECORPID;

		writeLogFile("リクエスト : " + strPostUrl + "?" + strParam);

		HttpURLConnection con = null;
		StringBuffer result = new StringBuffer();
		try {
			URL url = new URL(strPostUrl);
			con = (HttpURLConnection)url.openConnection();

			// HTTPリクエストコード
			con.setDoOutput(true);
			con.setRequestMethod("POST");

			// リクエストのbodyにParamを書き込む
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(strParam);
			out.flush();
			con.connect();

			// HTTPレスポンスコード
			final int status = con.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				// 通信成功
				final InputStream in = con.getInputStream();
				String encoding = con.getContentEncoding();
				if (null == encoding) {
					encoding = "UTF-8";
				}
				final InputStreamReader inReader = new InputStreamReader(in, encoding);
				final BufferedReader bufReader = new BufferedReader(inReader);
				String line = null;

				while ((line = bufReader.readLine()) != null) {
					result.append(line);
				}
				bufReader.close();
				inReader.close();
				in.close();
			} else {
				// 通信失敗
				System.out.println(status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				// コネクション切断
				con.disconnect();
			}
		}
		return result.toString();
	}


	/**
	 * ログファイル作成
	 * @param String apiId
	 * @param String methodName
	 */
	public void createLogFile(String apiId, String methodName) {

		// ログ出力場所指定
		String homePath = System.getProperty("user.dir");
		String logDir = homePath + SmConstants.TEST_DATA_DIRECTORY
				+ apiId + "\\" + methodName + SmConstants.TEST_LOG_DIR;
		// ログファイル名
		String logName = methodName + SmConstants.LOG_EXTENSION;

		// 絶対パス
		String logFile = logDir + "\\" + logName;

		File newfile = new File(logFile);
		try {
			// 過去ログが存在する場合、上書き（削除/新規作成）する
			if(newfile.exists()) {
				newfile.delete();
			}
			// ファイル作成
			newfile.createNewFile();

			// ファイル情報を保持
			this.CURRENT_LOG_FILE = newfile;

			// 開始ログ書込
			writeLogFile("***** " + apiId + ":" + methodName + " を開始 *****");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * ログファイル書込
	 * @param String apiId
	 * @param String methodName
	 */
	public void writeLogFile(String log) {
		// 実行時間取得
		Calendar cTime = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(SmConstants.LOG_TIME_FORMAT);

		// ファイル書き込み
		try {
			FileWriter filewriter = new FileWriter(this.CURRENT_LOG_FILE, true);
			filewriter.write(sdf.format(cTime.getTime()) + log + "\n");
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 機器通信-送信ログ作成
	 * @param String apiId
	 * @param String methodName
	 */
	public void smSendLog(String apiId, String methodName, List<String> logList) {

		// ログ出力場所指定
		String homePath = System.getProperty("user.dir");
		String logDir = homePath + SmConstants.TEST_DATA_DIRECTORY
				+ apiId + "\\" + methodName + SmConstants.TEST_LOG_DIR;
		// ログファイル名
		String logName = methodName+ SmConstants.SM_SEND_FILE + SmConstants.LOG_EXTENSION;

		// 絶対パス
		String logFile = logDir + "\\" + logName;

		File newfile = new File(logFile);
		try {
			// 過去ログが存在する場合、上書き（削除/新規作成）する
			if(newfile.exists()) {
				newfile.delete();
			}
			// ファイル作成
			newfile.createNewFile();

			// ログ書込
			FileWriter filewriter = new FileWriter(newfile, true);

			for (String log : logList) {
				filewriter.write(log + "\n");
			}
			filewriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *
	 * 機器通信-受信ログ作成
	 * @param String apiId
	 * @param String methodName
	 */
	public void smRecvLog(String apiId, String methodName, List<String> logList) {

		// ログ出力場所指定
		String homePath = System.getProperty("user.dir");
		String logDir = homePath + SmConstants.TEST_DATA_DIRECTORY
				+ apiId + "\\" + methodName + SmConstants.TEST_LOG_DIR;
		// ログファイル名
		String logName = methodName+ SmConstants.SM_RECV_FILE + SmConstants.LOG_EXTENSION;

		// 絶対パス
		String logFile = logDir + "\\" + logName;

		File newfile = new File(logFile);
		try {
			// 過去ログが存在する場合、上書き（削除/新規作成）する
			if(newfile.exists()) {
				newfile.delete();
			}
			// ファイル作成
			newfile.createNewFile();

			// ログ書込
			FileWriter filewriter = new FileWriter(newfile, true);

			for (String log : logList) {
				filewriter.write(log + "\n");
			}
			filewriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//
//	/**
//	 *
//	 * プロパティ比較
//	 * @param T left
//	 * @param T right
//	 */
//	public static <T> List<Difference>  diffProperty(T a, T b) {
//		BeanWrapper aWrap = new BeanWrapperImpl(a);
//
//		return null;
//
//	}
//
//	public static class Difference{
//		private String propertyName;
//		private Object left;
//		private Object right;
//
//	}

}
