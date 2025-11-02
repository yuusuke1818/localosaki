package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.smcontrol.BuildingSmPointResult;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.entity.MBuildingSmPointPK_;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MSmPointPK_;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class BuildingSmPointServiceDaoImpl implements BaseServiceDao<BuildingSmPointResult> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public List<BuildingSmPointResult> getResultList(BuildingSmPointResult target, EntityManager em) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BuildingSmPointResult> query = builder.createQuery(BuildingSmPointResult.class);
		Root<MBuildingSmPoint> root = query.from(MBuildingSmPoint.class);

		query.select(builder.construct( BuildingSmPointResult.class,
						root.join(MBuildingSmPoint_.MSmPoint).get(MSmPoint_.id).get(MSmPointPK_.pointNo),
						root.get(MBuildingSmPoint_.pointName)
					)
				)
		.where(builder.equal(root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.smId), target.getSmId()));

		return em.createQuery(query).getResultList();
	}

	@Override
	public List<BuildingSmPointResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public List<BuildingSmPointResult> getResultList(List<BuildingSmPointResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public List<BuildingSmPointResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public BuildingSmPointResult find(BuildingSmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public void persist(BuildingSmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public BuildingSmPointResult merge(BuildingSmPointResult target, EntityManager em) {
		MBuildingSmPointPK entityPk = new MBuildingSmPointPK();

		entityPk.setCorpId(target.getCorpId());
		entityPk.setBuildingId(target.getBuildingId());
		entityPk.setSmId(target.getSmId());
		entityPk.setPointNo(target.getPointNo());
		//探したい主キーをentityPkにset

		MBuildingSmPoint entity = new MBuildingSmPoint();
		entity = em.find(MBuildingSmPoint.class, entityPk);//emのfindメソッドでSetした主キーのレコードを取得
		if(entity == null) {
			return null;
		}
		entity.setPointSumFlg(target.getPointSumFlg());
		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());

		return null;
	}

	@Override
	public void remove(BuildingSmPointResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}
}
