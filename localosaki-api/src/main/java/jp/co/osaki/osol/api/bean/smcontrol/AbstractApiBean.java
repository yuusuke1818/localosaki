package jp.co.osaki.osol.api.bean.smcontrol;


import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 機器制御 API Bean 親 クラス.(非一括処理)
 *
 * @autho yasu_shimizu
 *
 * @param <R> リザルトクラス
 * @param <P> パラメータクラス
 *
 */
public abstract class AbstractApiBean<R extends BaseSmControlApiResult, P extends BaseSmControlApiParameter>
																				extends AbstractSmControlApiBean<R,P> {

	/**
	 * メイン処理
	 *
	 * @return レスポンス
	 */
	@Override
	public SmControlApiResponse<R> execute() throws Exception {

		/* 2 DAO設定 */
		super.dao = this.getSmCntrolDao();

		/* 3 機器制御用 パラメータの取得 */
		BaseParam param = this.initParam(super.apiParameter);

		/* 4 入力バリデーション */
		if(validator.validate(param).size()>0) {
			loggingError(Thread.currentThread().getStackTrace()[1],"validate",validator.validate(param).iterator().next().toString());
			return super.errResponse(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
		}

		try {
			/* 5 機器情報取得 */
			MPerson mPerson = getPerson(super.apiParameter);
			Long smId = super.apiParameter.getSmId();
			if(mPerson==null || smId==null) {
				return super.errResponse(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
			}

			super.loginUserId = mPerson.getUserId();
			SmPrmResultData smPrm = super.dao.findSmPrm(smId);

			/* 6 機種依存チェック */
			this.checkSmPrm(smPrm, param);

			/* 7 機器通信リクエスト生成 */
			FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm,
					super.apiParameter.getLoginCorpId(), super.apiParameter.getLoginPersonId(), super.loginUserId);
			req.setParam(param);

			/* 8 機器通信呼び出し */
			FvpCtrlMngResponse<BaseParam> res = super.fvpCtrlMngClient.excute(req);

			/* 9 DB登録処理 */
			if (OsolApiResultCode.API_OK.equals(res.getFvpResultCd())) {
				this.callDao(res);
			}

			/* 10 機器レスポンスから結果セットに値の詰め替え */
			BeanUtils.copyProperties(super.result, res.getParam());

			super.result.setSmId(smPrm.getSmId());

			/* 11 APIレスポンスに結果セットを */
			if (OsolApiResultCode.API_OK.equals(res.getFvpResultCd())) {
				super.response.setResult(super.result);
			}
			super.response.setResultCode(OsolApiResultCode.API_OK);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// BeanUtilsでのコピーエラー
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			return super.errResponse(OsolApiResultCode.API_ERROR_UNKNOWN);
		}catch (SmControlException e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			return super.errResponse(e.getErrorCode());
		}

		return super.response;
	}

	/**
	 * 機器制御 Dao取得
	 *
	 * @return dao
	 */
	protected abstract SmCntrolDao getSmCntrolDao();

	/**
	 * 機器制御用パラメータの初期化
	 *
	 * @param parameter
	 * @return
	 */
	protected abstract <T extends BaseParam>T initParam(P parameter);

	/**
	 * 機種依存チェック
	 *
	 * @param smPrm
	 * @param param
	 * @return
	 * @throws SmControlException
	 */
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		return true;
	}

	/**
	 * Dao処理
	 *
	 * @param res
	 * @throws
	 */
	protected void callDao(FvpCtrlMngResponse<?> res) throws Exception {
	}

}
