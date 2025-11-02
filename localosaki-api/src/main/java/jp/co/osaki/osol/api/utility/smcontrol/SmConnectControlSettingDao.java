package jp.co.osaki.osol.api.utility.smcontrol;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectControlSettingResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmConnectControlSettingDaoImpl;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

/**
 * 機器通信制御設定 Dao クラス
 *
 * @author shimizu
 */
@Stateless
public class SmConnectControlSettingDao extends BaseApiDao {

	private final SmConnectControlSettingDaoImpl smConnectControlSettingDaoImpl;

	public SmConnectControlSettingDao() {
		smConnectControlSettingDaoImpl = new SmConnectControlSettingDaoImpl();
	}

	/**
	 * 機器通信制御設定 全件取得
	 *
	 * @return 機器通信制御設定リスト
	 */
	public List<SmConnectControlSettingResultData> selectAll() throws Exception {
		return getResultList(smConnectControlSettingDaoImpl,new SmConnectControlSettingResultData());
	}
}
