package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectControlSettingResultData;
import jp.co.osaki.osol.entity.TSmConnectControlProduct;
import jp.co.osaki.osol.entity.TSmConnectControlProductPK_;
import jp.co.osaki.osol.entity.TSmConnectControlProduct_;
import jp.co.osaki.osol.entity.TSmConnectControlSetting;
import jp.co.osaki.osol.entity.TSmConnectControlSetting_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器通信制御設定
 *
 * @author shimizu
 *
 */
public class SmConnectControlSettingDaoImpl implements BaseServiceDao<SmConnectControlSettingResultData>{

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmConnectControlSettingResultData> getResultList(SmConnectControlSettingResultData target,
			EntityManager em) {
		// BaseApiDaoにgetResultList(em)の実装がないため
		return this.getResultList(em);
	}

	@Override
	public List<SmConnectControlSettingResultData> getResultList(Map<String, List<Object>> parameterMap,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmConnectControlSettingResultData> getResultList(List<SmConnectControlSettingResultData> entityList,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<SmConnectControlSettingResultData> getResultList(EntityManager em) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SmConnectControlSettingResultData> query = builder.createQuery(SmConnectControlSettingResultData.class);

		Root<TSmConnectControlSetting> root = query.from(TSmConnectControlSetting.class);
		Join<TSmConnectControlSetting, TSmConnectControlProduct> join = root.join(TSmConnectControlSetting_.TSmConnectControlProducts);

		query.select(builder.construct(SmConnectControlSettingResultData.class,
				root.get(TSmConnectControlSetting_.smConnectControlSettingId),
				root.get(TSmConnectControlSetting_.parallelConnectControlMaxCount),
				root.get(TSmConnectControlSetting_.smConnectRetryCount),
				root.get(TSmConnectControlSetting_.smConnectWaitTime),
				root.get(TSmConnectControlSetting_.socketConnectRetryCount),
				root.get(TSmConnectControlSetting_.socketConnectWaitTime),
				join.get(TSmConnectControlProduct_.id).get(TSmConnectControlProductPK_.productCd)));

		return em.createQuery(query).getResultList();
	}

	@Override
	public SmConnectControlSettingResultData find(SmConnectControlSettingResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(SmConnectControlSettingResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public SmConnectControlSettingResultData merge(SmConnectControlSettingResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(SmConnectControlSettingResultData target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
