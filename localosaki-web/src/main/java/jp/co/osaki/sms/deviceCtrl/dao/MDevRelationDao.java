package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MDevRelationDaoImpl;

/**
*
* @author hayashi_tak
*/
@Stateless
public class MDevRelationDao extends ConnectionSmsDao {

    private final MDevRelationDaoImpl daoImpl;

    public MDevRelationDao() {
        daoImpl = new MDevRelationDaoImpl();
    }


    public List<MDevRelation> getMDevRelationList(MDevRelation entity){
        return getResultList(daoImpl, entity);
    }

    public MDevRelation find(MDevRelation entity) {
        return super.find(daoImpl, entity);
    }

    public void persist(MDevRelation entity) {		// 2024-01-19
    	super.persist(daoImpl, entity);
    	return;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MDevRelation entity) {
        List<MDevRelation> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }
        return false;
    }

}
