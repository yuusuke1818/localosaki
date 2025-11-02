package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectStatusResultData;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.co.osaki.osol.entity.TSmConnectControlProduct_;
import jp.co.osaki.osol.entity.TSmConnectControlSetting_;
import jp.co.osaki.osol.entity.TSmConnectStatus;
import jp.co.osaki.osol.entity.TSmConnectStatus_;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 機器通信ステータス
 *
 * @author shimizu
 *
 */
public class SmConnectStatusDaoImpl implements BaseServiceDao<SmConnectStatusResultData> {
	/**
	 * エラー用ログ
	 */
	private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

	@Override
	public SmConnectStatusResultData find(SmConnectStatusResultData target, EntityManager em){
		TSmConnectStatus entity = em.find(TSmConnectStatus.class, target.getSmId());
		if(entity==null){
			return null;
		}
		// リザルトクラスに詰め替え
		SmConnectStatusResultData result = new SmConnectStatusResultData();
		try {
			BeanUtils.copyProperties(result, entity);
		} catch (Exception e) {
			errorLogger.errorf(BaseUtility.getStackTraceMessage(e));
		}

		return result;
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em){

		int count = 0;

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaUpdate<TSmConnectStatus> update = builder.createCriteriaUpdate(TSmConnectStatus.class);
		Root<TSmConnectStatus> root = update.from(TSmConnectStatus.class);

		List<Object> paramList = parameterMap.get(SmControlConstants.EXECUTEUPDATE_MAP_KEY_UPDATE);
		if(paramList != null) {
			for (Object data : paramList) {
				SmConnectStatusResultData target = (SmConnectStatusResultData)data;

				// ステータスの確認時とDB側の更新時間が違う場合は、更新しない
				update
					.set(root.get(TSmConnectStatus_.connectActiveFlg), target.getConnectActiveFlg())
					.set(root.get(TSmConnectStatus_.updateDate), target.getUpdateDate())
					.set(root.get(TSmConnectStatus_.updateUserId), target.getUpdateUserId())
					.where(
						builder.and(
							builder.equal(root.get(TSmConnectStatus_.smId),target.getSmId()),
							builder.equal(root.get(TSmConnectStatus_.updateDate),target.getOldUpdateDate())
						)
					);
				count += em.createQuery(update).executeUpdate();
			}
		}

		// アプリケーション起動時 初期化処理
		List<Object> initlist = parameterMap.get(SmControlConstants.EXECUTEUPDATE_MAP_KEY_INIT);
		if(initlist != null) {
			// 通信中フラグをすべてオフにする
			update.set(root.get(TSmConnectStatus_.connectActiveFlg), OsolConstants.FLG_OFF);
			em.createQuery(update).executeUpdate();
		}

		return count;
	}

	@Override
	public List<SmConnectStatusResultData> getResultList(SmConnectStatusResultData target, EntityManager em) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SmConnectStatusResultData> query = builder.createQuery(SmConnectStatusResultData.class);
		Root<TSmConnectStatus> root = query.from(TSmConnectStatus.class);

		// 絞り込み条件
		List<Predicate> whereList = new ArrayList<>();

		// 通信中フラグ
		Integer connectActiveFlg = target.getConnectActiveFlg();
		if(connectActiveFlg==null) {
			connectActiveFlg = OsolConstants.FLG_OFF;
		}
		whereList.add(builder.equal(root.get(TSmConnectStatus_.connectActiveFlg), connectActiveFlg));

		// 同一IPアドレス
		String ipAddress = target.getIpAddress();

		if(ipAddress!=null) {

			// ipアドレス整形
			List<String> addressList = new ArrayList<>();

			for (String address : ipAddress.split("\\.")) {
				String str = String.valueOf(Integer.parseInt(address));
				addressList.add(str);
			}


			// 0パディング
			String paddingAddress = String.format("%3s", addressList.get(0)).replace(" ", "0")  + "."
					+ String.format("%3s", addressList.get(1)).replace(" ", "0") + "."
					+ String.format("%3s", addressList.get(2)).replace(" ", "0") + "."
					+ String.format("%3s", addressList.get(3)).replace(" ", "0");

			// 0サプレス
			String suppressAddress = addressList.get(0) + "." + addressList.get(1) + "."
					+ addressList.get(2) + "." + addressList.get(3);

			// 0パディングIP or 0サプレスIP
			whereList.add(builder.or(
							builder.equal(root.join(TSmConnectStatus_.MSmPrm).get(MSmPrm_.ipAddress), paddingAddress),
							builder.equal(root.join(TSmConnectStatus_.MSmPrm).get(MSmPrm_.ipAddress), suppressAddress)
						));
		}

		// 同じ機器制御グループID
		Long settingId = target.getSmConnectControlSettingId();
		if(settingId!=null){
			whereList.add(
				builder.equal(root.join(TSmConnectStatus_.MSmPrm)
									.join(MSmPrm_.MProductSpec)
									.join(MProductSpec_.TSmConnectControlProducts)
									.join(TSmConnectControlProduct_.TSmConnectControlSetting)
									.get(TSmConnectControlSetting_.smConnectControlSettingId), settingId)
			);
		}

		// 組み立て
		query.select(builder.construct(SmConnectStatusResultData.class,
										root.get(TSmConnectStatus_.smId),
										root.get(TSmConnectStatus_.updateDate)))
			.where(builder.and(whereList.toArray(new Predicate[]{})));

		return em.createQuery(query).getResultList();
	}

	@Override
	public List<SmConnectStatusResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmConnectStatusResultData> getResultList(List<SmConnectStatusResultData> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmConnectStatusResultData> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(SmConnectStatusResultData target, EntityManager em) {
		Timestamp timestamp = target.getUpdateDate();
		Long userId = target.getUpdateUserId();

		TSmConnectStatus entity = new TSmConnectStatus();
		entity.setSmId(target.getSmId());
		entity.setConnectActiveFlg(0);
		entity.setVersion(0);
		entity.setCreateUserId(userId);
		entity.setCreateDate(timestamp);
		entity.setUpdateUserId(userId);
		entity.setUpdateDate(timestamp);
		em.persist(entity);
	}

	@Override
	public SmConnectStatusResultData merge(SmConnectStatusResultData target, EntityManager em) {
		TSmConnectStatus entity = em.find(TSmConnectStatus.class, target.getSmId());
		if(entity==null) {
			return null;
		}
		entity.setConnectActiveFlg(target.getConnectActiveFlg());
		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());
		em.merge(entity);

		return target;
	}

	@Override
	public void remove(SmConnectStatusResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
