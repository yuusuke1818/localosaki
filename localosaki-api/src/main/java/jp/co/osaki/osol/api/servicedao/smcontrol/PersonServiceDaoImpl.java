package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.api.result.smcontrol.PersonResult;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class PersonServiceDaoImpl implements BaseServiceDao<PersonResult> {

	@Override
	public PersonResult find(PersonResult target, EntityManager em) {
		// 主キーセット
		MPersonPK entityPK = new MPersonPK();
		entityPK.setCorpId(target.getCorpId());
		entityPK.setPersonId(target.getPersonId());

		// 検索実行
		MPerson entity = em.find(MPerson.class, entityPK);

		// ヒットしない場合Nullを返却する。
		if(entity==null){
			return null;
		}

		// ヒットした場合はemailをセットし返却
		target.setMailAddress(entity.getMailAddress());
		return target;

	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<PersonResult> getResultList(PersonResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<PersonResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<PersonResult> getResultList(List<PersonResult> entityList, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<PersonResult> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(PersonResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public PersonResult merge(PersonResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(PersonResult target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
