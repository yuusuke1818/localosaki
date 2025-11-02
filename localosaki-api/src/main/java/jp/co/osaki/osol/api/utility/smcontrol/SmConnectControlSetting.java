package jp.co.osaki.osol.api.utility.smcontrol;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import jp.co.osaki.osol.api.dao.smcontrol.SmConnectStatusDao;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectControlSettingResultData;

/**
 * 機器通信制御設定 Utiltiyクラス
 *
 * @author shimizu
 */
@Named(value = "smConnectControlSetting")
@ApplicationScoped
public class SmConnectControlSetting {

	/**
	 * 機器通信制御設定 Dao クラス
	 */
	@EJB
	SmConnectControlSettingDao dao;

	/**
	 * 機器通信ステータス Dao クラス
	 */
	@EJB
	SmConnectStatusDao smConnectStatusDao;

	/**
	 * key:プロダクトコード value:機器通信制御設定
	 */
	private Map<String, SmConnectControlSettingResultData> settingMap = new HashMap<>();

	/**
	 * サーバー起動時 機器通信制御設定の全件取得、機器通信ステータスの初期化
	 *
	 * @param event
	 * @throws Exception
	 */
	public void handle(@Observes @Initialized(ApplicationScoped.class) Object event) throws Exception {
		// 機器通信制御設定を取得してMapに
		for(SmConnectControlSettingResultData setting : dao.selectAll()) {
			settingMap.put(setting.getProductCd(), setting);
		}

		// 機器通信ステータスの初期化
		smConnectStatusDao.initConnectActiveFlg();
	}

	/**
	 * プロダクトコードから設定を取得する
	 *
	 * @param productCd
	 * @return 機器通信制御設定 ResultData
	 */
	public SmConnectControlSettingResultData getSmConnectControlSettingSetting(String productCd) {
		return settingMap.get(productCd);
	}


}
