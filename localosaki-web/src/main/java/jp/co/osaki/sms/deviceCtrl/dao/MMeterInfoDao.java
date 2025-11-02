package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeterInfo;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.deviceCtrl.servicedao.MMeterInfoDaoImpl;

/**
 * メーターの詳細情報 Dao
 *
 * @author y.nakamura
 */
@Stateless
public class MMeterInfoDao extends ConnectionSmsDao {
    MMeterInfoDaoImpl daoImpl;

    public MMeterInfoDao() {
        daoImpl = new MMeterInfoDaoImpl();
    }

    public void persist(MMeterInfo entity){
        super.persist(daoImpl, entity);
        return;
    }

    public void remove(MMeterInfo entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public MMeterInfo find(MMeterInfo entity) {
        return super.find(daoImpl, entity);
    }

    public void merge(MMeterInfo entity) {
        super.merge(daoImpl, entity);
        return ;
    }

    public List<MMeterInfo> getMMeterInfoList(MMeterInfo entity){
        return  super.getResultList(daoImpl, entity);
    }

    /**
     * nullの場合trueを返却
     * @param entity
     * @return 判定結果
     */
    public boolean isNull(MMeterInfo entity) {
        List<MMeterInfo> retList = new ArrayList<>();
        retList.addAll(super.getResultList(daoImpl, entity));

        if(retList.size() == 0) {
            return true;
        }

        return false;
    }

}
