package jp.co.osaki.osol.api.bean.smcontrol;

import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 機器制御 API Bean 親 クラス.（フレームワーク差異吸収）
 *
 * @autho yasu_shimizu
 *
 * @param <R> リザルトクラス
 * @param <P> パラメータクラス
 */
public abstract class AbstractSmControlApiBean<R extends BaseSmControlApiResult, P extends OsolApiParameter>
								extends OsolApiBean<P> implements BaseApiBean<P, SmControlApiResponse<R>> {

	/**
	 * 機器制御マネージャークライアント
	 */
	@Inject
	protected FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

	/**
	 * 機器制御 Dao
	 */
	protected SmCntrolDao dao;

	/**
	 * リクエスト Parameter
	 */
	protected P apiParameter;

	/**
	 * レスポンス<結果セット>
	 */
	protected SmControlApiResponse<R> response = new SmControlApiResponse<>();

	/**
	 * 結果セット Resultクラス
	 */
	protected R result;

	/**
	 * ログインユーザID
	 */
	protected Long loginUserId;


	/**
	 * レスポンス・結果セットのインスタンス設定
	 *
	 */
	@PostConstruct
	public void postConstruct() {
		// リクエストの生成
		Class<P> clazzP = this.getActualParameterClass();
		this.apiParameter = this.createActualInstance(clazzP);
		// 結果セットの生成
		Class<R> clazzR = this.getActualResultClass();
		this.result = this.createActualInstance(clazzR);
	}

	@Override
	public P getParameter() {
		return apiParameter;
	}

	@Override
	public void setParameter(P parameter) {
		this.apiParameter = parameter;
	}


	/**
	 * メイン処理
	 *
	 * @return レスポンス
	 * @throws Exception
	 */
	@Override
	public abstract SmControlApiResponse<R> execute() throws Exception;

	/**
	 * レスポンスにエラーコードを設定して返却
	 *
	 * @param code
	 * @return
	 */
	protected SmControlApiResponse<R> errResponse(String code){
		this.response.setResultCode(code);
		return this.response;
	}

	/**
	 * 製品：SM-FV2 であるかの確認
	 *
	 * @param smPrm
	 * @return
	 */
	protected boolean isFV2(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd());
	}

	/**
	 * 製品：FVP(D) であるかの確認
	 * @param smPrm
	 * @return
	 */
	protected boolean isFVPD(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd());
	}

	/**
	 * 製品：FVPα(D) であるかの確認
	 * @param smPrm
	 * @return
	 */
	protected boolean isFVPaD(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd());
	}

	/**
	 * 製品：FVPα(G2) であるかの確認
	 * @param smPrm
	 * @return
	 */
	protected boolean isFVPaG2(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd());
	}

	/**
	 * 製品：Eα であるかの確認
	 * @param smPrm
	 * @return
	 */
	protected boolean isEa(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd());
	}

	/**
	 * 製品：Eα2 であるかの確認
	 * @param smPrm
	 * @return
	 */
	protected boolean isEa2(SmPrmResultData smPrm) {
		return SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd());
	}

	/**
	 * ログ出力 Excption
	 * @param st
	 * @param e
	 */
	protected void loggingError(StackTraceElement st, Exception e) {
		loggingError(st,"Exception", e.toString());
	}

	/**
	 * ログ出力 key=value
	 *
	 * @param st
	 * @param key
	 * @param value
	 */
	protected void loggingError(StackTraceElement st, String key , String value) {
		loggingError(st,String.format("%s=%s", key, value));
	}

	/**
	 * ログ出力
	 * @param st
	 * @param etc
	 */
	protected void loggingError(StackTraceElement st, String etc) {
		errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(),st.getMethodName(),st.getLineNumber(),etc);
	}

	/**
	 * ログ出力
	 * @param st
	 */
	protected void loggingError(StackTraceElement st) {
		errorLogger.errorf("[%s][%s](%s)", st.getClassName(),st.getMethodName(),st.getLineNumber());
	}



	/**
	 * ジェネリクスResultの実クラスを取得する
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<R> getActualResultClass(){
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<R>)pt.getActualTypeArguments()[0];
	}

	/**
	 * ジェネリクスParameterの実クラスを取得する
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<P> getActualParameterClass() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<P>)pt.getActualTypeArguments()[1];
	}

	/**
	 * クラスからインスタンスを作成
	 *
	 * @param clazz
	 * @return
	 */
	private <T> T createActualInstance(Class<T> clazz) {
		T instance;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			return null;
		}
		return instance;
	}


}
