package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AlarmClearDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AlarmClearParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.FactoryLoaderUtility;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200202Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 機器警報解除 Bean クラス.
 *
 * @author akr_iwamoto
 *
 */
@Named(value = SmControlConstants.ALARM_CLEAR)
@RequestScoped
public class AlarmClearBean extends OsolApiBean<AlarmClearParameter>
			implements BaseApiBean<AlarmClearParameter,SmControlApiResponse<BaseSmControlApiResult>> {


	@EJB
	private AlarmClearDao dao;

	/**
	 * 機器通信
	 */
	@Inject
	FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

	/**
	 * BeanIO マッピング定義Loader
	 */
	@Inject
	FactoryLoaderUtility factoryLoaderUtility;

	/**
	 * Parameter
	 */
	private AlarmClearParameter parameter = new AlarmClearParameter();

	/**
	 * response
	 */
	private SmControlApiResponse<BaseSmControlApiResult> response = new SmControlApiResponse<>();

	@Override
	public AlarmClearParameter getParameter() {
		return this.parameter;
	}

	@Override
	public void setParameter(AlarmClearParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public SmControlApiResponse<BaseSmControlApiResult> execute() throws Exception {

		String corpId = this.parameter.getLoginCorpId();
		String personId = this.parameter.getLoginPersonId();
//		String alermMsg = this.parameter.getAlermMsg();

		// SMアドレスから機器情報取得
		Long smId = parameter.getSmId();
		SmPrmResultData smPrm = null;

		try {
			// 機器IDが正しく取得できていれば、機器情報取得
			if (smId != null) {
				smPrm = this.dao.findSmPrm(smId);
			}
		} catch (Exception e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
		} finally {
			// エラーの場合でも空の機器情報をセットする。
			if (smPrm == null) {
				this.response.setResultCode(OsolApiResultCode.API_ERROR_DATABASE_ENTITY_NOT_FOUND);
				return this.response;
			}
			smPrm.setSmId(smId);
		}

		A200202Param param = createA200202Param();

		/* 解除電文作成 */
		StringBuilder msgTB = buildMessage(param);

		// ユーザーID取得
		Long userId = super.getPerson(this.parameter).getUserId();

		try {
			// 警報解除電文送信
			sendTelegram(smPrm, String.format("TB00%s%s", smPrm.getSmAddress(), msgTB),
					corpId, personId, userId, param.getCommandCd());
		} catch (SmControlException e) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			errorLogger.errorf("[%s][%s](%s) TB ErrorCode=%s",
					st.getClassName(), st.getMethodName(), st.getLineNumber(), e.getErrorCode());
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			this.response.setResultCode(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
			return this.response;
		}

		this.response.setResultCode(OsolApiResultCode.API_OK);
		return this.response;
	}

	private A200202Param createA200202Param() {
		A200202Param param = new A200202Param();
		param.setStateFVPx(SmControlConstants.NO_ALERT);
		param.setDemandAlert(SmControlConstants.ALERT_OFF);
		param.setErrorCircuitLON(SmControlConstants.NO_ALERT);
		param.setSettingDemand(SmControlConstants.NO_ALERT);
		param.setSettingUnit(SmControlConstants.NO_ALERT);
		param.setTransferAlert(SmControlConstants.ALERT_OFF);
		param.setBatteryErrorAlertFVPx(SmControlConstants.NO_ALERT);

		// ポイント警報 20ポイント固定
		List<String> pList = new ArrayList<String>();
		for(int i=0; i < 20; i++) {
			pList.add("110011");
		}
		param.setPointList(pList);

		return param;
	}


	/**
	 * 警報解除電文作成
	 *
	 * @param param
	 * @return
	 */
	private StringBuilder buildMessage(A200202Param param) {
		StringBuilder sb = new StringBuilder()
			.append(param.getStateFVPx())
			.append(param.getDemandAlert())
			.append(param.getErrorCircuitLON())
			.append(param.getSettingDemand())
			.append(param.getSettingUnit())
			.append(param.getTransferAlert())
			.append(param.getBatteryErrorAlertFVPx()).append(' ');//予備部は空白

		// 各ポイント警報
		for(String point : param.getPointList()) {
			sb.append(point);
		}

		return sb;
	}

	/**
	 * 機器通信
	 *
	 * @param smPrm
	 * @param telegram
	 * @param corpId
	 * @param personId
	 * @param userId
	 * @return 受信電文
	 * @throws Exception
	 */
	private String sendTelegram(SmPrmResultData smPrm, String telegram,  String corpId,
			String personId, Long userId, String commandCd) throws Exception {
		return fvpCtrlMngClient.excute(smPrm.getSmId(), smPrm.getProductCd(), smPrm.getIpAddress(),
				telegram, corpId, personId, userId, commandCd);
	}
}
