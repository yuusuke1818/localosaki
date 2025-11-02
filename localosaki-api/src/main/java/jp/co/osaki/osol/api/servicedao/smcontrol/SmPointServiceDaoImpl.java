package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.api.result.smcontrol.SmPointResult;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPointPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class SmPointServiceDaoImpl implements BaseServiceDao<SmPointResult> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPointResult> getResultList(SmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public List<SmPointResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPointResult> getResultList(List<SmPointResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPointResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public SmPointResult find(SmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(SmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public SmPointResult merge(SmPointResult target, EntityManager em) {
		MSmPointPK entityPk = new MSmPointPK();

		entityPk.setSmId(target.getSmId());
		entityPk.setPointNo(target.getPointNo());
		//探したい主キーをentityPkにset

		MSmPoint entity = new MSmPoint();
		entity = em.find(MSmPoint.class, entityPk);//emのfindメソッドでSetした主キーのレコードを取得
		if(entity == null) {
			return null;
		}

		// ポイント種別 比較
		String curPointType = entity.getPointType();
		if (!CheckUtility.isNullOrEmpty(curPointType) && curPointType.equals(target.getPointType())) {
			// ポイント種別に変更が無い場合はDB更新せずに終了
			return null;
		}

		entity.setPointType(target.getPointType());
		entity.setDmCorrectionFactor(target.getDmCorrectionFactor());
		entity.setAnalogOffSetValue(target.getAnalogOffSetValue());
		entity.setAnalogConversionFactor(target.getAnalogConversionFactor());
		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());

		em.merge(entity);

		return target;
	}

	@Override
	public void remove(SmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
