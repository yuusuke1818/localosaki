package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MBuildingDmPK_;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class BuildingDmServiceDaoImpl implements BaseServiceDao<BuildingDmResult> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingDmResult> getResultList(BuildingDmResult target, EntityManager em) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BuildingDmResult> query = builder.createQuery(BuildingDmResult.class);
		Root<MBuildingDm> root = query.from(MBuildingDm.class);

		query.select(builder.construct( BuildingDmResult.class,
						root.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId),
						root.join(MBuildingDm_.TBuilding).get(TBuilding_.buildingName),
						root.get(MBuildingDm_.facilitiesMailAddress1),
						root.get(MBuildingDm_.facilitiesMailAddress2),
						root.get(MBuildingDm_.facilitiesMailAddress3),
						root.get(MBuildingDm_.facilitiesMailAddress4),
						root.get(MBuildingDm_.facilitiesMailAddress5)
					)
				)
		.where(builder.equal(
				root.join(MBuildingDm_.MBuildingSms).get(MBuildingSm_.id).get(MBuildingSmPK_.smId), target.getSmId()));

		return em.createQuery(query).getResultList();

	}

	@Override
	public List<BuildingDmResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingDmResult> getResultList(List<BuildingDmResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<BuildingDmResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public BuildingDmResult find(BuildingDmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(BuildingDmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public BuildingDmResult merge(BuildingDmResult target, EntityManager em) {
		// PK用のエンティティ
		MBuildingDmPK entityPk = new MBuildingDmPK();

		// 要企業TBLのServiceDAO作成+Resultsetの設定
		entityPk.setCorpId(target.getCorpId());
		entityPk.setBuildingId(target.getBuildingId());

		// 更新対象テーブルのエンティティ
		MBuildingDm entity = new MBuildingDm();

		// 更新対象
		entity = em.find(MBuildingDm.class, entityPk);

		// 値比較
		BigDecimal targetKw = entity.getTargetKw();
		if (targetKw != null) {
			if(target.getTargetPower().compareTo(targetKw) == 0) {
			// 値に変更がない場合、更新しない
			return null;
			}
		}

		// 更新対象カラム設定
		entity.setTargetKw(target.getTargetPower());
		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());

		// 更新処理
		em.merge(entity);

		return target;
	}

	@Override
	public void remove(BuildingDmResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
