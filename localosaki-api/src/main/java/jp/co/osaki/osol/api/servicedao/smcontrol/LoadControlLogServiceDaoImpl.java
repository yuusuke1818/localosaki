package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.api.result.smcontrol.LoadControlLogResult;
import jp.co.osaki.osol.entity.TLoadControlLog;
import jp.co.osaki.osol.entity.TLoadControlLogPK;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class LoadControlLogServiceDaoImpl implements BaseServiceDao<LoadControlLogResult> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<LoadControlLogResult> getResultList(LoadControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<LoadControlLogResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<LoadControlLogResult> getResultList(List<LoadControlLogResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<LoadControlLogResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public LoadControlLogResult find(LoadControlLogResult target, EntityManager em) {
		// PK用
		TLoadControlLogPK entityPk = new TLoadControlLogPK();

		entityPk.setSmId(target.getSmId());
		entityPk.setRecordYmdhm(target.getRecordYMDHM());
		entityPk.setRestMs(target.getRestMS());
		entityPk.setControlStatus(target.getControlStatus());

		// entity作成
		TLoadControlLog entity = new TLoadControlLog();

		entity = em.find(TLoadControlLog.class, entityPk);

		if(entity==null){
			return null;
		}

		// レコード存在チェックのため、そのままtargetを返却する。
		return target;

	}

	@Override
	public void persist(LoadControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public LoadControlLogResult merge(LoadControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(LoadControlLogResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
