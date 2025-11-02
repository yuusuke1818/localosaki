package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmAlarmCallResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmAlarmCallDaoImpl;


/**
 *
 * 機器警報発呼 Dao クラス.
 *
 * @author akr_iwamoto
 *
 */
@Stateless
public class SmAlarmCallDao extends BaseSmControlDao {


	/**
	 *  機器通信ステータス
	 */
	private final SmAlarmCallDaoImpl smAlarmCallDaoImpl;


	/**
	 * コンストラクタ
	 */
	public SmAlarmCallDao() {
		smAlarmCallDaoImpl = new SmAlarmCallDaoImpl();
	}

	/**
	 * セッション
	 */
	@Resource
	private SessionContext sessionContext;

	/**
	 * 主キー検索
	 *
	 * @param smId
	 * @return ステータス情報 見つからない場合 null
	 */
	public SmAlarmCallResultData findSmAlarmCall(Long smId) {
		SmAlarmCallResultData target = new SmAlarmCallResultData();
		target.setSmId(smId);
		return find(smAlarmCallDaoImpl, target);
	}

	/**
	 * レコード追加
	 *
	 * @param smId
	 */
	public void insertSmAlarmCall(SmAlarmCallResultData target) {
		Timestamp timestamp = super.getServerDateTime();
		target.setUpdateDate(timestamp);
		if (target.getSmAlarmCallDate() == null) {
			target.setSmAlarmCallDate(timestamp);
		}
		persist(smAlarmCallDaoImpl, target);
	}

	/**
	 * レコード更新
	 *
	 * @param status
	 */
	public void updateSmAlarmCall(SmAlarmCallResultData target) {
		Timestamp timestamp = super.getServerDateTime();
		target.setUpdateDate(timestamp);
		if (target.getSmAlarmCallDate() == null) {
			target.setSmAlarmCallDate(timestamp);
		}
		merge(smAlarmCallDaoImpl, target);
	}


}
