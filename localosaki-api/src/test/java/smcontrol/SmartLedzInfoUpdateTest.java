package smcontrol;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.extract.SmartLedzInfoSelectExtractResult;
import util.SmConstants;
import util.SmControlTestUtil;
import util.SmStub;

public class SmartLedzInfoUpdateTest {

	private static SmControlTestUtil util = new SmControlTestUtil();

	private static SmStub stub;

	private static String API_ID = SmConstants.A200058_TEST;

	private String METHOD_NAME = "";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		System.out.println("setUpBeforeClass");
		if (SmConstants.IS_STUB) {

			// 機能用のスタブ準備
			stub = new SmStub(API_ID);

			// ダミー機器起動
			Thread thread = new Thread(stub);
			thread.start();
		}


	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass");
		if (SmConstants.IS_STUB) {
			// ダミー機器停止
			stub.stop();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		System.out.println("setUp ----->>");

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		stub.initCase();
		System.out.println("tearDown << -----");
	}

	/**
	 * test_001
	 * 正常系(Eα)
	 */
	@Test
	public void test_001() {
		// メソッド名取得
		this.METHOD_NAME = new Object(){}.getClass().getEnclosingMethod().getName();

		try {
			// 初期処理
			ResourceBundle inRb = commonInput();

			// ------------------------- リクエストデータ作成 -------------------------

			// プロパティファイルから値取得
			String strBean = inRb.getString(SmConstants.PARAM_BEAN);
			String strSmId = inRb.getString(SmConstants.PARAM_SMID);
			String strResultSet = inRb.getString(SmConstants.PARAM_RESULT_SET);

			// リクエスト生成
			String strParam = SmConstants.PARAM_BEAN + "=" + strBean + "&" +
					SmConstants.PARAM_SMID + "=" + strSmId + "&" +
					SmConstants.PARAM_RESULT_SET + "=" + strResultSet;


			// ------------------------- リクエストデータ作成 -------------------------

			// API呼出(機器通信/結果取得/期待値読取/結果:期待値比較)
			commonExecute(strParam);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * test_002
	 * 正常系(Eα2)
	 */
	@Test
	public void test_002() {
		// メソッド名取得
		this.METHOD_NAME = new Object(){}.getClass().getEnclosingMethod().getName();


		try {
			// 初期処理
			ResourceBundle inRb = commonInput();

			// ------------------------- リクエストデータ作成 -------------------------

			// プロパティファイルから値取得
			String strBean = inRb.getString(SmConstants.PARAM_BEAN);
			String strSmId = inRb.getString(SmConstants.PARAM_SMID);
			String strResultSet = inRb.getString(SmConstants.PARAM_RESULT_SET);

			// リクエスト生成
			String strParam = SmConstants.PARAM_BEAN + "=" + strBean + "&" +
					SmConstants.PARAM_SMID + "=" + strSmId + "&" +
					SmConstants.PARAM_RESULT_SET + "=" + strResultSet;


			// ------------------------- リクエストデータ作成 -------------------------

			// API呼出(機器通信/結果取得/期待値読取/結果:期待値比較)
			commonExecute(strParam);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 初期設定処理(ログファイル作成/定数設定/インプットファイル読込)
	 * 【機能毎に修正の必要無】
	 * @return ResourceBundle
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private ResourceBundle commonInput() {
		// LOGファイル作成
		util.createLogFile(this.API_ID, this.METHOD_NAME);

		// スタブ機器用のステータス指定
		stub.setUtil(util);
		stub.setMethodName(this.METHOD_NAME);

		// InputDataパス取得
		String homeDir = System.getProperty("user.dir");
		String testInputDir = homeDir + SmConstants.TEST_DATA_DIRECTORY
				+ this.API_ID + "\\" + this.METHOD_NAME;

		URLClassLoader inputUrlLoader;
		ResourceBundle inRb = null;
		try {
			inputUrlLoader = new URLClassLoader(new URL[]{new File(testInputDir).toURI().toURL()});

			// inputプロパティファイル名作成
			String inputFile = this.METHOD_NAME + SmConstants.INPUT_FILE;

			// inputDataデータを読み込み
			inRb = ResourceBundle.getBundle(inputFile ,Locale.getDefault(), inputUrlLoader);
		} catch (Exception e) {
			util.writeLogFile(this.METHOD_NAME + "のリクエスト設定情報が読み込めません。");
			util.writeLogFile(e.getMessage());
		}

		return inRb;
	}

	/**
	 * テスト実行(機器通信/結果取得/期待値読取/結果:期待値比較)
	 * 【機能毎にResultクラスの修正が必要】
	 * @param String strParam
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private void commonExecute(String strParam){

		// APIリクエスト
		String output = util.callPost(SmConstants.SERVER_URL, strParam);
		util.writeLogFile("レスポンス : " + output);

		// レスポンスからGsonパースで値セット
		SmControlApiResponse<SmartLedzInfoSelectExtractResult> param = new Gson().fromJson(output, new TypeToken<SmControlApiResponse<SmartLedzInfoSelectExtractResult>>(){}.getType());
		SmartLedzInfoSelectExtractResult paramResult = param.getResult();

		// OutputDataパス取得
		String homeDir = System.getProperty("user.dir");
		String testOutputDir = homeDir + SmConstants.TEST_DATA_DIRECTORY
				+ this.API_ID + "\\" + this.METHOD_NAME;
		URLClassLoader outputUrlLoader;
		SmartLedzInfoSelectExtractResult expected = null;
		ResourceBundle outRb = null;
		try {
			outputUrlLoader = new URLClassLoader(new URL[]{new File(testOutputDir).toURI().toURL()});

			// outputプロパティファイル名作成
			String outputFile = this.METHOD_NAME + SmConstants.OUTPUT_FILE;

			// inputDataデータを読み込み
			outRb = ResourceBundle.getBundle(outputFile ,Locale.getDefault(), outputUrlLoader);

			// 期待値生成
			if(!(outRb.getString(SmConstants.JSON_RESULT) == null) ) {
				expected =
						new Gson().fromJson(outRb.getString(SmConstants.JSON_RESULT), new TypeToken<SmartLedzInfoSelectExtractResult>(){}.getType());

			}
		} catch (Exception e) {
			util.writeLogFile(this.METHOD_NAME + "の期待値設定情報が読み込めません。");
			util.writeLogFile(e.getMessage());
		}

		// Response内容をチェック
		assertThat(param.getResultCode(), is(outRb.getString(SmConstants.RESULT_CODE)));
		if (!(expected == null)) {
			assertReflectionEquals(expected , paramResult);
		}else {
			assertThat(paramResult, is(nullValue()));
		}

		util.writeLogFile("***** " + this.API_ID + ":" + this.METHOD_NAME + " を終了 *****");

	}
}
