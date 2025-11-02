package jp.co.osaki.osol.api.dao.smcontrol;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.result.smcontrol.LoadControlLogResult;
import jp.co.osaki.osol.api.servicedao.smcontrol.LoadControlLogServiceDaoImpl;

/**
 *
 *  負荷制御履歴(取得) Dao クラス.
 *
 * @author t_sakamoto
 *
 */
@Stateless
public class LoadCtrlHistSelectDao extends BaseSmControlDao {


	// ServiceDAO
	private final LoadControlLogServiceDaoImpl loadControlLogServiceDaoImpl;

	public LoadCtrlHistSelectDao() {
		loadControlLogServiceDaoImpl = new LoadControlLogServiceDaoImpl();
	}

	// 最新レコードチェック
	public LoadControlLogResult SelectLoadCntlLog(LoadControlLogResult param) throws Exception{

		// ServiceDAO内取得処理を呼ぶ
		return find(loadControlLogServiceDaoImpl,param);

	}
}
