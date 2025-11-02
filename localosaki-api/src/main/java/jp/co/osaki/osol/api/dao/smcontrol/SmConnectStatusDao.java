package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectStatusResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmConnectStatusDaoImpl;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;


/**
 *
 * 機器通信ステータス Dao クラス.
 *
 * @author yasu_shimizu
 *
 */
@Stateless
public class SmConnectStatusDao extends BaseSmControlDao {


	/**
	 *  機器通信ステータス
	 */
	private final SmConnectStatusDaoImpl smConnectStatusDaoImpl;


	/**
	 * コンストラクタ
	 */
	public SmConnectStatusDao() {
		smConnectStatusDaoImpl = new SmConnectStatusDaoImpl();
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
	public SmConnectStatusResultData findSmConnectStatus(Long smId) {
		SmConnectStatusResultData target = new SmConnectStatusResultData();
		target.setSmId(smId);
		return find(smConnectStatusDaoImpl, target);
	}

	/**
	 * レコード追加
	 *
	 * @param smId
	 */
	public void insertSmConnectStatus(SmConnectStatusResultData target) {
		Timestamp timestamp = super.getServerDateTime();
		target.setUpdateDate(timestamp);
		persist(smConnectStatusDaoImpl, target);
	}

	/**
	 * レコード更新
	 *
	 * @param status
	 */
	public void updateSmConnectStatus(SmConnectStatusResultData target) {
		Timestamp timestamp = super.getServerDateTime();
		target.setUpdateDate(timestamp);
		merge(smConnectStatusDaoImpl, target);
	}

	/**
	 * 同一IP通信/同時通信上限確認
	 *
	 * @param target
	 * @param connectMax
	 * @return
	 */
	public boolean checkParallelConnect(SmConnectStatusResultData status, int connectMax, boolean updateDBFlag, Long userId) throws Exception {

		// DB登録フラグが立っている場合もチェック対象とする
		// if (!updateDBFlag) {
		// 同一グループかつ通信中の件数を取得
		SmConnectStatusResultData sameSettingIdTarget = new SmConnectStatusResultData();
		sameSettingIdTarget.setSmConnectControlSettingId(status.getSmConnectControlSettingId());
		sameSettingIdTarget.setConnectActiveFlg(OsolConstants.FLG_ON);
		List<SmConnectStatusResultData> sameSettingIdList = getResultList(smConnectStatusDaoImpl, sameSettingIdTarget);

		if(sameSettingIdList.size() >= connectMax) {
			daoLogger.trace(this.getClass().getName().concat("sameSetting:"+sameSettingIdList.size()+">="+connectMax));
			return false;
		}
		//}

		// 同一IPかつ通信中のレコードを取得
		SmConnectStatusResultData sameIPTarget = new SmConnectStatusResultData();
		sameIPTarget.setIpAddress(status.getIpAddress());
		sameIPTarget.setConnectActiveFlg(OsolConstants.FLG_ON);
		List<SmConnectStatusResultData> sameIPList = getResultList(smConnectStatusDaoImpl, sameIPTarget);

		Map<Long, SmConnectStatusResultData> targetMap = new HashMap<>();

		// 件数が0件でなければ、同一IP機器の更新日時をチェック
		if (!sameIPList.isEmpty()) {
			int count = 0;

			for (SmConnectStatusResultData data : sameIPList) {
				// 一定時間ロックのかかった機器の強制ロック解除
				if(super.getServerDateTime().after(new Date(data.getUpdateDate().getTime() + SmControlConstants.SM_CONTROL_ACTIVE_RESET_TIME))) {
					data = find(smConnectStatusDaoImpl, data);
					data.setConnectActiveFlg(OsolConstants.FLG_OFF);
					targetMap.put(data.getSmId(), data);

					continue;
				}
				count++;
			}

			// ロック中の同一IPがあれば、通信NG
			if (count > 0) {
				daoLogger.trace(this.getClass().getName().concat("ip="+status.getIpAddress()+" sameIP:smId="+sameIPList.get(0).getSmId()));
				return false;
			}
		}

		// ステータス更新
		Timestamp timestamp = super.getServerDateTime();
		for (Entry<Long, SmConnectStatusResultData> entry : targetMap.entrySet()) {
			// ロックの強制解除が機器自身なら
			if (entry.getKey().equals(status.getSmId())) {
				entry.getValue().setConnectActiveFlg(OsolConstants.FLG_ON);
			}
			entry.getValue().setOldUpdateDate(entry.getValue().getUpdateDate());
			entry.getValue().setUpdateDate(timestamp);
			entry.getValue().setUpdateUserId(userId);
		}

		if (!targetMap.containsKey(status.getSmId())) {
			SmConnectStatusResultData target = new SmConnectStatusResultData();
			target.setSmId(status.getSmId());
			target.setConnectActiveFlg(OsolConstants.FLG_ON);
			target.setOldUpdateDate(status.getUpdateDate());
			target.setUpdateDate(timestamp);
			target.setUpdateUserId(userId);
			targetMap.put(status.getSmId(), target);
		}

		List<Object> targetList = new ArrayList<>(targetMap.values());
		Map<String, List<Object>> param = new HashMap<>();
		param.put(SmControlConstants.EXECUTEUPDATE_MAP_KEY_UPDATE, targetList);

		// 更新処理
		int res = executeUpdate(smConnectStatusDaoImpl, param);

		// 更新予定と更新結果の件数がことなれば、排他制御エラー
		if (res != targetList.size()) {
			for(Entry<Long, SmConnectStatusResultData> entry:targetMap.entrySet()) {
				daoLogger.trace(this.getClass().getName().concat("update smId" + entry.getKey() +
						":oldUpdate=" + entry.getValue().getOldUpdateDate() +":flag="+ entry.getValue().getConnectActiveFlg()));
			}
			// ロールバック
			sessionContext.setRollbackOnly();
			// throw new SmControlException(null,null);
			throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_THREADTIMEOUT, "API_ERROR_SMCONTROL_THREADTIMEOUT");
		}

		return true;
	}

	/**
	 * アプリケーション起動時 初期化処理
	 *
	 */
	public void initConnectActiveFlg() {
		// Updateの識別のために空のListをvalueに設定したMapを渡す
		Map<String, List<Object>> param = new HashMap<>();
		param.put(SmControlConstants.EXECUTEUPDATE_MAP_KEY_INIT, new ArrayList<>());
		executeUpdate(smConnectStatusDaoImpl, param);
	}
}
