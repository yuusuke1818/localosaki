/**
 *
 */
package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean.FUNCTION_CD;
import jp.co.osaki.osol.access.function.dao.MCorpFunctionUseDao;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.dao.MCorpDao;

/**
 *
 *
 */
@Named(value = "smsAggregateTopBean")
@ConversationScoped
public class TopBean extends SmsConversationBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7516935874352427265L;

	@EJB
	MCorpDao mCorpDao;

	@EJB
	MCorpFunctionUseDao mCorpFunctionUseDao;

	// 当クラスパッケージ名
	private String packageName = this.getClass().getPackage().getName();

	@Override
	public String init() {
        // アクセスログ出力
        exportAccessLog("init", "ボタン「データダウンロード」押下");

		eventLogger.debug(packageName.concat(" smsAggregateTopBean:init():START"));

		eventLogger.debug(packageName.concat(" smsAggregateTopBean:init():END"));
		return "aggregateTop";
	}

	/***
	 * 検針データメニュー表示/非表示
	 *
	 * @return true:表示、false:非表示
	 */
	public boolean getMeterReadingMenuBulkDownloadVisble() {

		eventLogger.info("getMeterReadingMenuBulkDownloadVisble called.");

		eventLogger.info("corpType:" + this.getLoginCorpType());

		String loginCorpType = this.getLoginCorpType();

		try {

			// 企業種別で判別
			// 大崎の場合
			if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(loginCorpType)) {

				return true;
			}
			// パートナー企業 or 契約企業
			else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(loginCorpType) ||
					OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(loginCorpType)) {

				String loginCorpId = this.getLoginCorp().getCorpId();

				eventLogger.info("loginCorpId:" + loginCorpId);

				MCorp loginCorp = mCorpDao.find(loginCorpId);

				eventLogger.info("loginCorpName:" + loginCorp.getCorpName());

				List<MCorpFunctionUse> mCorpFunctionUses = mCorpFunctionUseDao.getResultList(loginCorpId);

				eventLogger.info("corpFunctionUse size:" + mCorpFunctionUses.size());

				for (MCorpFunctionUse functionUse : mCorpFunctionUses) {
					eventLogger.info("functionCd:" + functionUse.getMFunction().getFunctionCd() + " useFlg:" + functionUse.getUseFlg());
				}

				// 検針一括ダウンロード機能権限を保有しているか
				boolean isSmsBulkDownloadUse = mCorpFunctionUses.stream() //
						.filter(functionUse -> FUNCTION_CD.SMS_BULK_DOWNLOAD.getVal()
								.equals(functionUse.getMFunction().getFunctionCd())) //
						.filter(functionUse -> functionUse.getUseFlg() == OsolConstants.FLG_ON) //
						.findFirst().isPresent();

				eventLogger.info("isSmsBulkDownloadUse:" + isSmsBulkDownloadUse);

				// 検針一括ダウンロード機能権限を保有している場合
				if (isSmsBulkDownloadUse) {

					// SMS権限を保持しているか
					boolean isSmsAuthUse = this.getLoginCorpPersonAuthListAll().stream() //
							.filter(mCorpPersonAuth -> OsolConstants.USER_AUTHORITY.SMS.getVal()
									.equals(mCorpPersonAuth.getMAuth().getAuthorityCd())) //
							.filter(mCorpPersonAuth -> mCorpPersonAuth.getAuthorityFlg() == OsolConstants.FLG_ON) //
							.findFirst().isPresent();

					// 担当権限、かつSMS権限を保持していない場合
					if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(this.getLoginPerson().getPersonType()) &&
							!isSmsAuthUse) {
						return false;
					}

					return true;
				}
				else {
					return false;
				}

			}
			else {
				return false;
			}

		} catch (Throwable t) {

			errorLogger.fatal("一括ダウンロード時の権限チェック不正");
			errorLogger.fatal(t);

			return false;
		}
	}
}
