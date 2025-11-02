package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MRepeaterDaoImpl;
/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class MRepeaterDao extends ConnectionSmsDao {

    private final MRepeaterDaoImpl daoImpl;

    public MRepeaterDao() {
        daoImpl = new MRepeaterDaoImpl();
    }

    public void persist(MRepeater entity){
        super.persist(daoImpl, entity);
        return;
    }

    public MRepeater find(MRepeater entity){
        return super.find(daoImpl, entity);
    }

    public List<MRepeater> getMRepeaterList(MRepeater entity){
        return super.getResultList(daoImpl, entity);
    }

    public void merge(MRepeater entity) {
        super.merge(daoImpl, entity);
        return;
    }


    public void remove(MRepeater entity){
        entity = find(entity);
        super.remove(daoImpl, entity);
        return;
    }

    /**
     * nullだったらtrue返す
     * @param entity
     * @return
     */
    public boolean isNull(MRepeater entity) {
        List<MRepeater> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }
}
