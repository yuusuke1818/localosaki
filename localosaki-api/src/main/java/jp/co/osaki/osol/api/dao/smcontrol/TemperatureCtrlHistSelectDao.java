package jp.co.osaki.osol.api.dao.smcontrol;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.result.smcontrol.TempHumidControlLogResult;
import jp.co.osaki.osol.api.servicedao.smcontrol.TempHumidControlLogServiceDaoImpl;


/**
 *
 * 温度制御履歴(取得) Dao クラス.
 *
 * @author da_yamano
 *
 */
@Stateless
public class TemperatureCtrlHistSelectDao extends BaseSmControlDao {

	// ServiceDAO
	private final TempHumidControlLogServiceDaoImpl tempHumidControlLogServiceDaoImpl;

	public TemperatureCtrlHistSelectDao() {
		tempHumidControlLogServiceDaoImpl = new TempHumidControlLogServiceDaoImpl();
	}

	// レコードチェック
	public TempHumidControlLogResult SelectTempHumidCntlLog(TempHumidControlLogResult param) throws Exception {

		// ServiceDAO内取得処理を呼ぶ
		return find(tempHumidControlLogServiceDaoImpl,param);
	}
}
