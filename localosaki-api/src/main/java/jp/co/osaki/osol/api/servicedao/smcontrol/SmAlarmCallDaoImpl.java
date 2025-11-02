package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmAlarmCallResultData;
import jp.co.osaki.osol.entity.TSmAlarmCall;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 機器警報発呼
 *
 * @author akr_iwamoto
 *
 */
public class SmAlarmCallDaoImpl implements BaseServiceDao<SmAlarmCallResultData> {
	/**
	 * エラー用ログ
	 */
	private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

	@Override
	public SmAlarmCallResultData find(SmAlarmCallResultData target, EntityManager em){
		TSmAlarmCall entity = em.find(TSmAlarmCall.class, target.getSmId());
		if(entity==null){
			return null;
		}
		// リザルトクラスに詰め替え
		SmAlarmCallResultData result = new SmAlarmCallResultData();
		try {
			BeanUtils.copyProperties(result, entity);
		} catch (Exception e) {
			errorLogger.errorf(BaseUtility.getStackTraceMessage(e));
		}

		return result;
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmAlarmCallResultData> getResultList(SmAlarmCallResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmAlarmCallResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmAlarmCallResultData> getResultList(List<SmAlarmCallResultData> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmAlarmCallResultData> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(SmAlarmCallResultData target, EntityManager em) {
		Timestamp timestamp = target.getUpdateDate();
		Long userId = target.getUpdateUserId();

		TSmAlarmCall entity = new TSmAlarmCall();
		entity.setSmId(target.getSmId());
		entity.setSmAlarmCallDate(target.getSmAlarmCallDate());
		entity.setVersion(0);
		entity.setCreateUserId(userId);
		entity.setCreateDate(timestamp);
		entity.setUpdateUserId(userId);
		entity.setUpdateDate(timestamp);
		em.persist(entity);
	}

	@Override
	public SmAlarmCallResultData merge(SmAlarmCallResultData target, EntityManager em) {
		TSmAlarmCall entity = em.find(TSmAlarmCall.class, target.getSmId());
		if(entity==null) {
			return null;
		}
		entity.setSmAlarmCallDate(target.getSmAlarmCallDate());
		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());
		em.merge(entity);

		return target;
	}

	@Override
	public void remove(SmAlarmCallResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
