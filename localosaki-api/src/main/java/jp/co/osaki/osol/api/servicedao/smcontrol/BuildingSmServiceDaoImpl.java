package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class BuildingSmServiceDaoImpl implements BaseServiceDao<BuildingSmResult> {

	@Override
	public BuildingSmResult find(BuildingSmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingSmResult> getResultList(BuildingSmResult target, EntityManager em) {

		// SQL文構築用
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BuildingSmResult> query = builder.createQuery(BuildingSmResult.class);
		Root<MBuildingSm> root = query.from(MBuildingSm.class);
		List<Predicate> whereList = new ArrayList<>();

		// where 機器ID='xxx'
		whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId), target.getSmId() ));

		// sql文構築
		query = query.select(builder.construct(BuildingSmResult.class,
				root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId),
				root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId),
				root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId))).
				where(builder.and(whereList.toArray(new Predicate[]{})));

		return em.createQuery(query).getResultList();
	}

	@Override
	public List<BuildingSmResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingSmResult> getResultList(List<BuildingSmResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingSmResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(BuildingSmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public BuildingSmResult merge(BuildingSmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(BuildingSmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
