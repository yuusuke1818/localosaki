package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.api.result.smcontrol.TempHumidControlLogResult;
import jp.co.osaki.osol.entity.TTempHumidControlLog;
import jp.co.osaki.osol.entity.TTempHumidControlLogPK;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TempHumidControlLogServiceDaoImpl implements BaseServiceDao<TempHumidControlLogResult> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TempHumidControlLogResult> getResultList(TempHumidControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TempHumidControlLogResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TempHumidControlLogResult> getResultList(List<TempHumidControlLogResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TempHumidControlLogResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public TempHumidControlLogResult find(TempHumidControlLogResult target, EntityManager em) {
		// PK用
		TTempHumidControlLogPK entityPk = new TTempHumidControlLogPK();

		entityPk.setSmId(target.getSmId());
		entityPk.setRecordYmdhms(target.getRecordYMDHM());
		entityPk.setPortOutStatus(target.getPortOutStatus());

		// entity作成
		TTempHumidControlLog entity = new TTempHumidControlLog();

		entity = em.find(TTempHumidControlLog.class, entityPk);
		if(entity==null){
			return null;
		}

		// レコード存在チェックのため、そのままtargetを返却する。
		return target;	}

	@Override
	public void persist(TempHumidControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TempHumidControlLogResult merge(TempHumidControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(TempHumidControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
