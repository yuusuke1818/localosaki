package jp.co.osaki.osol.api.bean.smcontrol;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.beanio.StreamFactory;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiMailService;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AlermMailSendDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmAlarmCallDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AlermMailSendParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmAlarmCallResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.FactoryLoaderUtility;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200030Param;
import jp.co.osaki.osol.mng.param.A200202Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 警報メール送信 Bean クラス.
 *
 * @author yasu_shimizu
 *
 */
@Named(value = SmControlConstants.ALERM_MAIL_SEND)
@RequestScoped
public class AlermMailSendBean extends OsolApiBean<AlermMailSendParameter>
			implements BaseApiBean<AlermMailSendParameter,SmControlApiResponse<BaseSmControlApiResult>> {

	private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Inject
    private OsolConfigs osolConfigs;

	@EJB
	private AlermMailSendDao dao;

	@EJB
	private SmAlarmCallDao alarmCallDao;

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
	private AlermMailSendParameter parameter = new AlermMailSendParameter();

	/**
	 * response
	 */
	private SmControlApiResponse<BaseSmControlApiResult> response = new SmControlApiResponse<>();

	@Override
	public AlermMailSendParameter getParameter() {
		return this.parameter;
	}

	@Override
	public void setParameter(AlermMailSendParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public SmControlApiResponse<BaseSmControlApiResult> execute() throws Exception {

		String corpId = this.parameter.getLoginCorpId();
		String personId = this.parameter.getLoginPersonId();
		String alermMsg = this.parameter.getAlermMsg();

		// IPアドレス、SMアドレスから機器情報取得
		SmPrmResultData smPrm = (SmPrmResultData)dao.query(parameter);
		if(smPrm==null) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			errorLogger.errorf("[%s][%s](%s) API_ERROR_DATABASE_ENTITY_NOT_FOUND ip=",
					st.getClassName(), st.getMethodName(), st.getLineNumber(), parameter.getIpAddress());
			this.response.setResultCode(OsolApiResultCode.API_ERROR_DATABASE_ENTITY_NOT_FOUND);
			return this.response;
		}

		// 固定長->Bean変換
		A200202Param param = fromFixedString(alermMsg, smPrm.getProductCd(), new A200202Param());

		// メール送信要因リスト
		List<String> mailSendFactorList = new ArrayList<>();

		// G2からの発呼の場合
		if(SmControlConstants.COMMAND_HA.equals(param.getCommand())) {
			/* メール送信要因の確認 */
			// デマンド警報
			String demandAlertMsg = SmControlConstants.DEMAND_ALERT_HA.getName(param.getAlertState());
			if(demandAlertMsg != null) {
				mailSendFactorList.add(demandAlertMsg);
			}

		// G2以外の機器からの発呼
		}else if(SmControlConstants.COMMAND_T9.equals(param.getCommand())) {
			/* メール送信要因の確認 */
			// デマンド警報
			String demandAlertMsg = SmControlConstants.DEMAND_ALERT_T9.getName(param.getDemandAlert());
			if(demandAlertMsg != null) {
				mailSendFactorList.add(demandAlertMsg);
			}
			// 移報警報
			String transferAlertMsg = SmControlConstants.TRANSFER_ALERT_T9.getName(param.getTransferAlert());
			if(transferAlertMsg != null) {
				mailSendFactorList.add(transferAlertMsg);
			}
			// 計測ポイント異常
			Map<String,String> pointNameMap = null;
			int index = 0;
			for(String val:param.getPointList()) {
				index++;
				String pointAlertMsg = SmControlConstants.POINT_ALERT_T9.getName(val);
				if(pointAlertMsg != null) {
					if(pointNameMap==null) {
						// 全ポイント名称 取得
						pointNameMap = dao.getPointNameMap(smPrm);
					}
					String pointName = pointNameMap.get(String.valueOf(index));
					if(pointName==null) {
						// ポイント名称がDBに設定されていなければ生成
						pointName = "ポイント"+ String.valueOf(index);
					}
					mailSendFactorList.add(String.join(":", pointAlertMsg, pointName));
				}
			}

			// T9系機器警報発呼は次時限で警報解除する必要があるため、一旦DBへ記録する

			// ユーザーID取得
			Long userId = super.getPerson(this.parameter).getUserId();

			// 機器ステータスを主キー(smId)検索
			SmAlarmCallResultData smAlarmCallResultData = alarmCallDao.findSmAlarmCall(smPrm.getSmId());
			if(smAlarmCallResultData == null) {
				// 新規追加
				smAlarmCallResultData = new SmAlarmCallResultData();
				smAlarmCallResultData.setSmId(smPrm.getSmId());
				smAlarmCallResultData.setUpdateUserId(userId);
				alarmCallDao.insertSmAlarmCall(smAlarmCallResultData);
			} else {
				// 更新
				smAlarmCallResultData.setUpdateUserId(userId);
				alarmCallDao.updateSmAlarmCall(smAlarmCallResultData);
			}

			// メール送信の場合は、目標電力＆現在電力を取得
			if(!mailSendFactorList.isEmpty()) {

				try {
					// B00電文送受信
					String res = sendTelegram(smPrm, String.format("%sB0000", smPrm.getSmAddress()),
							corpId, personId, userId, param.getCommandCd());

					// 固定長->Bean変換
					A200030Param paramB0 = fromFixedString(res, smPrm.getProductCd(), new A200030Param());

					// 電文からメール用に情報を取得
					param.setRecordDayTime(paramB0.getMeasureDayTime());
					param.setTargetPowerOrSettingNo(paramB0.getTargetPower());
					param.setNowTargetPower(paramB0.getNowTargetPower());
					param.setLeftMinute(paramB0.getLeftMinute());
					param.setLeftSeconds(paramB0.getLeftSeconds());

				} catch (SmControlException e) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					errorLogger.errorf("[%s][%s](%s) B00 ErrorCode=%s",
							st.getClassName(), st.getMethodName(), st.getLineNumber(), e.getErrorCode());
					errorLogger.error(BaseUtility.getStackTraceMessage(e));
					String errMsg = "電文未取得";
					param.setRecordDayTime(errMsg);
					param.setTargetPowerOrSettingNo(errMsg);
					param.setNowTargetPower(errMsg);
					param.setLeftMinute(errMsg);
					param.setLeftSeconds(errMsg);
				}
			}
		// HM機器からの発呼
		}else if(SmControlConstants.COMMAND_HM.equals(param.getCommand())) {

			if(SmControlConstants.ALERM_STATE_HM_SETTINGREQ.equals(param.getAlertState())) {
				// 1：Aiel Master設定要求

				/* メール送信要因の確認 */
				// デマンド警報 HM Aiel Master設定要求
				String demandAlertMsg = SmControlConstants.DEMAND_ALERT_HM_SETTINGREG.getName(param.getStateFlg1());
				if(demandAlertMsg != null) {
					mailSendFactorList.add(demandAlertMsg);
				}

			} else if (SmControlConstants.ALERM_STATE_HM_FAIL.equals(param.getAlertState())) {
				// 2：Aiel Master異常

				/* メール送信要因の確認 */
				// デマンド警報 HM Aiel Master異常
				String demandAlertMsg = SmControlConstants.DEMAND_ALERT_HM_FAIL.getName(param.getStateFlg1());
				if(demandAlertMsg != null) {
					mailSendFactorList.add(demandAlertMsg);
				}
			} else {
				// 未対応コマンド
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				errorLogger.errorf("[%s][%s](%s) AlermMsg=%s",
						st.getClassName(), st.getMethodName(), st.getLineNumber(), parameter.getAlermMsg());
				this.response.setResultCode(OsolApiResultCode.API_ERROR_UNKNOWN);
				return this.response;
			}

		}else {
			// 未対応コマンド
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			errorLogger.errorf("[%s][%s](%s) AlermMsg=%s",
					st.getClassName(), st.getMethodName(), st.getLineNumber(), parameter.getAlermMsg());
			this.response.setResultCode(OsolApiResultCode.API_ERROR_UNKNOWN);
			return this.response;
		}

		eventLogger.debug("メール送信 = " + !mailSendFactorList.isEmpty());

		// メール送信
		if(!mailSendFactorList.isEmpty()) {

			// DBから情報取得
			BuildingDmResult result =  dao.getAlertMaiInfo(smPrm);
			if(result==null) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				errorLogger.errorf("[%s][%s](%s) API_ERROR_DATABASE_ENTITY_NOT_FOUND smId=%s",
						st.getClassName(), st.getMethodName(), st.getLineNumber(), smPrm.getSmId());
				this.response.setResultCode(OsolApiResultCode.API_ERROR_DATABASE_ENTITY_NOT_FOUND);
				return this.response;
			}

			// 設定ファイル読み込み
			Properties prop = new Properties();
			try(FileReader mailSettingFile = new FileReader(SmControlConstants.ALERT_MAIL_SETTING)){
				prop.load(mailSettingFile);
			}catch (Exception e) {
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
			}


			String subject;
			String content;

			// HMの場合はメールフォーマットが異なる
			if(SmControlConstants.COMMAND_HM.equals(param.getCommand())) {
				subject = prop.getProperty("alertMail.subjectHM");
				content = buildMailBodyHM(result, param, mailSendFactorList);
			} else {
				subject = prop.getProperty("alertMail.subject");
				content = buildMailBody(result, param, mailSendFactorList);
			}


			String mailFrom = prop.getProperty("alertMail.mailFrom");

			List<String> toList =buildMailTo(result);
			List<String> ccList = buildCsvDataToList(prop.getProperty("alertMail.cc"));
			List<String> bccList = buildCsvDataToList(prop.getProperty("alertMail.bcc"));

			// debugログ
			eventLogger.debug("件名 = " + subject);
			eventLogger.debug("メール本文 = " + content);
			eventLogger.debug("差出人 = " + mailFrom);
			eventLogger.debug("宛先 = " + toList);
			eventLogger.debug("cc = " + ccList);
			eventLogger.debug("bcc = " + bccList);

			// メールの送信
			if(!sendAlertMail(subject, content, mailFrom, toList, ccList, bccList)) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				errorLogger.errorf("[%s][%s](%s) ALERM_MAIL_FAILD smId=%s",
						st.getClassName(), st.getMethodName(), st.getLineNumber(), smPrm.getSmId());
				this.response.setResultCode(OsolApiResultCode.API_ERROR_UNKNOWN);
				return this.response;
			}
		}
		this.response.setResultCode(OsolApiResultCode.API_OK);
		return this.response;
	}

	/**
	 * 固定長電文をBeanに変換する
	 *
	 * @param msg
	 * @param productCd
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <P extends BaseParam>P fromFixedString(String msg, String productCd, P bean) {

		String mProductCd = productCd;

		// HMからの発呼の場合
		// HM用に23に変換する
		if(SmControlConstants.COMMAND_HM.equals(parameter.getAlermMsg().substring(2, 4))) {
			if(SmControlConstants.COMMAND_HM_ALERM_STATE_3.equals(parameter.getAlermMsg().substring(22, 23))) {
				mProductCd = SmControlConstants.PRODUCT_CODE_HM_STATE3;
			} else if(SmControlConstants.COMMAND_HM_ALERM_STATE_5.equals(parameter.getAlermMsg().substring(22, 23))) {
				mProductCd = SmControlConstants.PRODUCT_CODE_HM_STATE5;
			} else {
				mProductCd = SmControlConstants.PRODUCT_CODE_HM;
			}
		}

		// BeanIO Facroty取得
		StreamFactory factory = factoryLoaderUtility.getFacrory(bean.getCommandCd());
		String resStreamName = mProductCd.concat(SmControlConstants.FROM_FIXEDSTRING_STREAM_NAME_POSTFIX);
		// 固定長->Bean変換
		return (P)factory.createUnmarshaller(resStreamName).unmarshal(msg);
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

	/**
	 * 警報メール 本文作成
	 *
	 * @param result
	 * @param param
	 * @param mailSendFactorList
	 * @return
	 */
	private String buildMailBody(BuildingDmResult result, A200202Param param, List<String> mailSendFactorList) {
		// メール本文用テンプレート読み込み
		BaseVelocity mailTemplate = new BaseVelocity(SmControlConstants.ALERT_MAIL_BODY, SmControlConstants.TEMPLATE_DIR);

		mailTemplate.put("fromDB", result);
		mailTemplate.put("fromMsg" ,param);
		mailTemplate.put("alertList", mailSendFactorList);

		return mailTemplate.merge();
	}

	/**
	 * Aiel Master警報発生通知メール 本文作成
	 *
	 * @param result
	 * @param param
	 * @param mailSendFactorList
	 * @return
	 */
	private String buildMailBodyHM(BuildingDmResult result, A200202Param param, List<String> mailSendFactorList) {
		// メール本文用テンプレート読み込み
		BaseVelocity mailTemplate = new BaseVelocity(SmControlConstants.ALERT_HM_MAIL_BODY, SmControlConstants.TEMPLATE_DIR);

		mailTemplate.put("fromDB", result);
		mailTemplate.put("fromMsg" ,param);
		mailTemplate.put("alertList", mailSendFactorList);

		return mailTemplate.merge();
	}

	/**
	 * 警報メール メールアドレスList化
	 *
	 * @param result
	 * @return
	 */
	private List<String> buildMailTo(BuildingDmResult result){
		List<String> list = new ArrayList<>(Arrays.asList(result.getFacilitiesMailAddress1(),
							result.getFacilitiesMailAddress2(),result.getFacilitiesMailAddress3(),
							result.getFacilitiesMailAddress4(),result.getFacilitiesMailAddress5()));
		// null要素の削除
		list.removeAll(Collections.singleton(null));

		return list;
	}

	/**
	 * カンマ区切りの設定値をList化
	 *
	 * @param csv
	 * @return
	 */
	private List<String> buildCsvDataToList(String csv){
		return csv==null? null : Arrays.asList(csv.split(","));
	}

	/**
	 * 警報メール メールAPI呼び出し
	 *
	 * @param subject
	 * @param content
	 * @param mailFrom
	 * @param toList
	 * @param ccList
	 * @param bccList
	 */
	private boolean sendAlertMail(String subject, String content, String mailFrom,
			List<String> toList, List<String> ccList,List<String> bccList) {

        // メール送信フラグ
        String useSendStr = osolConfigs.getConfig("mail.send.available");
        boolean useSendMailFlg = false;
        if (useSendStr != null && OsolConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }
        // メールAPI用パラメータ作成
		OsolMailParameter mailParameter = new OsolMailParameter(subject, content, mailFrom, toList, ccList, bccList, useSendMailFlg);
		OsolApiMailService osolMailService = new OsolApiMailService();

		// メール送信
		try {
			if(!osolMailService.mailSend(mailParameter)) {
				return false;
			}
		} catch (Exception e) {
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
			return false;
		}
		return true;
	}
}
