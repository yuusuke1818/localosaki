package jp.co.osaki.osol.api.bean.smcontrol;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.BulkApiResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;


/**
 * 機器制御 一括処理 API Bean 親 クラス.
 *
 * @author shimizu
 *
 * @param <R> 結果セットクラス（各レコードの単一クラス）
 * @param <P> パラメータクラス（一括用）
 *
 * @author yasu_shimizu
 * @author f_takemura
 */
public abstract class AbstructBulkApiBean<R extends BaseSmControlApiResult, P extends BulkSmControlApiParameter>
																				extends AbstractSmControlApiBean<R,P> {

	/**
	 * 一括処理の各レコードの結果リスト
	 */
	protected List<BulkResultData> resultDataList = new ArrayList<>();


	/**
	 * メイン処理
	 *
	 * @return レスポンス
	 * @throws Exception
	 */
	@Override
	public SmControlApiResponse<R> execute() throws Exception {

		/* 1 DAO設定 */
		this.dao = this.getSmCntrolDao();

		/* 2 機器制御用 パラメータの取得 */
		String jsonString = super.apiParameter.getSmIdList();
		if(jsonString==null || jsonString.isEmpty()) {
			// smIdList がない場合はエラー
			loggingError(Thread.currentThread().getStackTrace()[1]);
			return errResponse(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
		}

		/* 3 Gsonパース */
		// GSONで直接 Map<String,String>とするとJsonオブジェクト形式でエラーとなるため
		// JsonElementを直に toString でデシリアライズする
		Type t = new TypeToken<List<Map<String, JsonElement>>>(){}.getType();
		List<Map<String, JsonElement>> smIdList = new Gson().fromJson(jsonString, t);
		List<Map<String,String>> parameterList = new ArrayList<>();
		for(Map<String, JsonElement> map : smIdList) {
			HashMap<String,String> parameter = new HashMap<>();
			for(Entry<String, JsonElement> entry : map.entrySet()) {
				parameter.put(entry.getKey(), entry.getValue().toString());
			}
			parameterList.add(parameter);

			// 各レコードの処理結果保持インスタンス作成
			BulkResultData resultData = new BulkResultData();
			resultData.setRecordResult(OsolApiResultCode.API_OK);
			// 機器ID取得
			try {
				resultData.setSmId(Long.parseLong(parameter.get(ApiParamKeyConstants.SM_ID)));
			} catch (Exception e) {
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
				resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
			}
			resultData.setParameter(parameter);
			this.resultDataList.add(resultData);
		}

		// Parameterリスト から Paramリストを作成
		List<? extends BaseParam> paramList = initParamList(parameterList);

		/* 4 入力バリデーション */
		Iterator<BulkResultData> itr = this.resultDataList.iterator();
		for(BaseParam param : paramList) {
			BulkResultData resultData = itr.next();
			resultData.setParam(param);

			if(param==null) {
				loggingError(Thread.currentThread().getStackTrace()[1],
						String.format(" smId=%s:param=null",resultData.getSmId()));
				resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
				continue;
			}else {
				// Beanバリデーション
				Set<?> validateErrorSet = validator.validate(param);
				if(validateErrorSet.size()>0) {
					loggingError(Thread.currentThread().getStackTrace()[1],
						String.format(" smId=%s:validate=%s",resultData.getSmId(),validateErrorSet.toString()));
					resultData.setRecordResult(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
					continue;
				}
			}

			// 新旧設定情報チェック(設定系のみ)
			String oldResultSetJson = resultData.getParameter().get("oldResultSet");
			if (oldResultSetJson != null) {
				// 一部設定情報が等しい場合は通信しない
				if(param.partDataComparison(new Gson().fromJson(oldResultSetJson, param.getClass()))) {
					resultData.setRecordResult(SmControlConstants.RECORD_NO_CHANGE);
				}
			}
		}

		// userID 取得
		MPerson mPerson = getPerson(this.apiParameter);
		if(mPerson==null) {
			loggingError(Thread.currentThread().getStackTrace()[1],
					String.format(" corpId=%s,personId=%s",apiParameter.getLoginCorpId(),apiParameter.getLoginPersonId()));
			return errResponse(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
		}
		super.loginUserId = mPerson.getUserId();

		// 機器情報取得用リスト
		List<SmPrmResultData> smPrmList = new ArrayList<>();

		// 機器通信リクエストリスト
		List<FvpCtrlMngRequest<BaseParam>> fvpReqList = new ArrayList<>();

		for (BulkResultData resultData : this.resultDataList) {
			/* 5 機器情報取得 */
			Long smId = resultData.getSmId();
			SmPrmResultData smPrm = null;
			try {
				// 機器IDが正しく取得できていれば、機器情報取得
				if(smId != null ) {
					smPrm = this.dao.findSmPrm(smId);
				}
			}catch (SmControlException e) {
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
				resultData.setRecordResult(e.getErrorCode());
			} catch(Exception e) {
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
				throw e;
			}finally {
				// エラーの場合でも空の機器情報をセットする。
				if(smPrm == null) {
					smPrm = new SmPrmResultData();
				}
				smPrm.setSmId(smId);
				resultData.setSmPrm(smPrm);
			}

			/* 6 機種依存チェック */
			// レコード処理結果にエラーがある場合はチェックはしない
			if(OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
				try {
					this.checkSmPrm(smPrm, resultData.getParam());
				} catch (SmControlException e) {
					errorLogger.error(BaseUtility.getStackTraceMessage(e));
					resultData.setRecordResult(e.getErrorCode());
				}
			}


			/* 7 機器通信リクエスト生成 */
			FvpCtrlMngRequest<BaseParam> fvpReq = null;

			// FvpCtrlMngRequest作成(機器情報)
			fvpReq = new FvpCtrlMngRequest<>(	smPrm,
					super.apiParameter.getLoginCorpId(),
					super.apiParameter.getLoginPersonId(),
					super.loginUserId);

			if(OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
				// FvpCtrlMngRequest作成(リクエスト)
				fvpReq.setParam(resultData.getParam());
			}else {
				// エラーレコードはエラー内容をセット
				fvpReq.setCommandCd(resultData.getRecordResult());
			}
			// エラーレコードの場合はnullを設定
			fvpReqList.add(fvpReq);
		}

		/* 8 機器通信呼び出し */
		List<FvpCtrlMngResponse<BaseParam>> fvpResList;
		try {
			fvpResList = fvpCtrlMngClient.excute(fvpReqList);
		} catch (SmControlException e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			loggingError(Thread.currentThread().getStackTrace()[1], e);
			return errResponse(e.getErrorCode());
		}

		/* 9 DB登録処理 */
		this.callDao(fvpResList);

		// (設定系の場合)メールAPI
		// this.callMailApi(fvpResList,old_targetList,loginPersonId,loginCorpId);


		// エラーレコードの個数をカウント
		int errCount = 0;

//		/* 10 機器レスポンスから結果セットに値の詰め替え */
		itr = this.resultDataList.iterator();
		for(FvpCtrlMngResponse<BaseParam> res: fvpResList) {
			BulkResultData resultData = itr.next();

			if(SmControlConstants.RECORD_NO_CHANGE.equals(resultData.getRecordResult())) {
				// 新旧で設定変更がない場合はAPI_OK  未送信コードをAPI_OKに変更
				resultData.setRecordResult(OsolApiResultCode.API_OK);
				continue;
			}else if(!OsolApiResultCode.API_OK.equals(resultData.getRecordResult())) {
				// 通信前にエラーとなっているレコード
				errCount++;
				continue;
			}else if(!OsolApiResultCode.API_OK.equals(res.getFvpResultCd())) {
				// 通信後にエラーとなっているレコードの場合は、エラーコードを詰め替える
				resultData.setRecordResult(res.getFvpResultCd());
				errCount++;
				continue;
			}

			// レスポンス変換
			try {
				// resultクラスを生成 Paramクラスから値のコピー
				@SuppressWarnings("unchecked")
				R resultSet = (R)super.result.getClass().newInstance();
				BeanUtils.copyProperties(resultSet, res.getParam());
				resultSet.setSmId(resultData.getSmId());
				resultData.setResult(resultSet);
			} catch (Exception e) {
				// resultクラス生成失敗、プロパティマッピング失敗
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
				resultData.setRecordResult(OsolApiResultCode.API_ERROR_UNKNOWN);
			}
		}

		/* 11 処理結果コード判定 */
		if(errCount == 0) {
			// 全て正常レコード
			super.response.setResultCode(OsolApiResultCode.API_OK);
		}else if(errCount == this.resultDataList.size()) {
			// 全て異常
			super.response.setResultCode(OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTFAILED);
		}else {
			// 正常/異常レコード混在
			super.response.setResultCode(OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTWARN);
		}

		/* 12 APIレスポンスに結果セットを */
		List<BulkApiResultData<R>> resultList = new ArrayList<>();
		for( BulkResultData resultData : this.resultDataList) {
			// アップキャストして、レスポンスに不必要なフィールドを取り除く
			BulkApiResultData<R> result = (BulkApiResultData<R>)resultData;
			resultList.add(result);
		}
		super.response.setResultList(resultList);
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
	 * @param parameterList
	 * @return
	 */
	protected abstract List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList);

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
	 * @param fvpResList
	 */
	protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {
	}


	protected class BulkResultData extends BulkApiResultData<R>{

		private Long smId;

		public Long getSmId() {
			return smId;
		}

		public void setSmId(Long smId) {
			this.smId = smId;
		}

		private Map<String,String> parameter;

		private BaseParam param;

		private SmPrmResultData smPrm;

		public Map<String, String> getParameter() {
			return parameter;
		}

		public void setParameter(Map<String, String> parameter) {
			this.parameter = parameter;
		}

		public BaseParam getParam() {
			return param;
		}

		public void setParam(BaseParam param) {
			this.param = param;
		}

		public SmPrmResultData getSmPrm() {
			return smPrm;
		}

		public void setSmPrm(SmPrmResultData smPrm) {
			this.smPrm = smPrm;
		}
	}
}

