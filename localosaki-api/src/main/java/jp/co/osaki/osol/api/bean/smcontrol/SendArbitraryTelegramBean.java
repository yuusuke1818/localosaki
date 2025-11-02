package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SendArbitraryTelegramDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SendArbitraryTelegramParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.SendArbitraryTelegramResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 任意電文送信 Bean クラス.
 *
 * @author yasu_shimizu
 *
 */
@Named(value = SmControlConstants.SEND_ARBITRARY_TELEGRAM)
@RequestScoped
public class SendArbitraryTelegramBean extends OsolApiBean<SendArbitraryTelegramParameter>
			implements BaseApiBean<SendArbitraryTelegramParameter,SmControlApiResponse<SendArbitraryTelegramResult>> {

	@EJB
	private SendArbitraryTelegramDao dao;

	@Inject
	FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

	/**
	 * Parameter
	 */
	private SendArbitraryTelegramParameter parameter = new SendArbitraryTelegramParameter();

	/**
	 * response
	 */
	private SmControlApiResponse<SendArbitraryTelegramResult> response = new SmControlApiResponse<>();

	/**
	 * result
	 */
	private SendArbitraryTelegramResult result = new SendArbitraryTelegramResult();

	@Override
	public SendArbitraryTelegramParameter getParameter() {
		return this.parameter;
	}

	@Override
	public void setParameter(SendArbitraryTelegramParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public SmControlApiResponse<SendArbitraryTelegramResult> execute() throws Exception {

		Long smId = this.parameter.getSmId();
		String corpId = this.parameter.getLoginCorpId();
		String personId = this.parameter.getLoginPersonId();
		String anyMessage = this.parameter.getAnyMessage();


		if(anyMessage == null 															// Nullチェック
				|| anyMessage.length() > SmControlConstants.TELEGRAM_MAX_LENGTH		// 任意電文長チェック
				|| !anyMessage.matches("[-+0-9a-zA-Z\\s　]+")) { 						//型チェック
			this.response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
			return this.response;
		}
			try {
			//機器情報取得
			SmPrmResultData smPrm = dao.findSmPrm(smId);

			// 対象機器チェック
			if(!SmControlConstants.PRODUCT_CD_FV2.equals(smPrm.getProductCd())
					&& !SmControlConstants.PRODUCT_CD_FVP_D.equals(smPrm.getProductCd())
					&& !SmControlConstants.PRODUCT_CD_FVP_ALPHA_D.equals(smPrm.getProductCd())
					&& !SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(smPrm.getProductCd())
					&& !SmControlConstants.PRODUCT_CD_E_ALPHA.equals(smPrm.getProductCd())
					&& !SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(smPrm.getProductCd())) {
				this.response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
				return this.response;
			}


			// 機器通信
			String returnMessage = fvpCtrlMngClient.excute(smId, smPrm.getProductCd(), smPrm.getIpAddress(),
													anyMessage, corpId, personId, getPerson(this.parameter).getUserId(),
													null);
			this.result.setReturnMessage(returnMessage);
		} catch (SmControlException e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			this.response.setResultCode(e.getErrorCode());
			return this.response;
		}

		this.result.setSmId(smId);
		this.response.setResult(result);
		this.response.setResultCode(OsolApiResultCode.API_OK);
		return this.response;
	}

}
