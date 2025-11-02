package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器情報
 *
 * @author shimizu
 *
 */
public class SmCntrolServiceDaoImpl implements BaseServiceDao<SmPrmResultData> {

	@Override
	public SmPrmResultData find(SmPrmResultData target, EntityManager em) {
		MSmPrm entity = em.find(MSmPrm.class, target.getSmId());
		if(entity==null || entity.getDelFlg()==1){
			return null;
		}
		target.setProductCd(entity.getMProductSpec().getProductCd());
		target.setSmAddress(entity.getSmAddress());
		target.setIpAddress(entity.getIpAddress());
		return target;
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPrmResultData> getResultList(SmPrmResultData target, EntityManager em) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SmPrmResultData> query = builder.createQuery(SmPrmResultData.class);
		Root<MSmPrm> root = query.from(MSmPrm.class);


		String ipZeroPadding =
				String.format("%3s.%3s.%3s.%3s", (Object[]) target.getIpAddress().split("\\.")).replace(' ', '0');

		// Query組み立て
		query.select(builder.construct(SmPrmResultData.class, root.get(MSmPrm_.smId)))
			.where(
				builder.and(
					root.get(MSmPrm_.ipAddress).in(target.getIpAddress(), ipZeroPadding),
					builder.equal(root.get(MSmPrm_.smAddress), target.getSmAddress()),
					builder.equal(root.get(MSmPrm_.delFlg), OsolConstants.FLG_OFF)
				)
			);

		return em.createQuery(query).getResultList();
	}

	@Override
	public List<SmPrmResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPrmResultData> getResultList(List<SmPrmResultData> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmPrmResultData> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(SmPrmResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public SmPrmResultData merge(SmPrmResultData target, EntityManager em) {

		MSmPrm entity = em.find(MSmPrm.class, target.getSmId());
		if(entity == null) {
			return null;
		}

		entity.setUpdateUserId(target.getUpdateUserId());
		entity.setUpdateDate(target.getUpdateDate());

		em.merge(entity);

		return target;
	}

	@Override
	public void remove(SmPrmResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
