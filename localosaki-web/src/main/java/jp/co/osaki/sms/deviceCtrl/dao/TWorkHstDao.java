package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.deviceCtrl.servicedao.TWorkHstDaoImpl;

/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class TWorkHstDao extends ConnectionSmsDao  {
    TWorkHstDaoImpl daoImpl;

    public TWorkHstDao() {
        daoImpl = new TWorkHstDaoImpl();
    }

    public void remove(TWorkHst entity) {
        super.remove(daoImpl, entity);
        return;
    }

    public List<TWorkHst> getTWorkHstList(TWorkHst entity) {
        return super.getResultList(daoImpl, entity);
    }

    public TWorkHst find(TWorkHst entity) {
        return super.find(daoImpl, entity);
    }

    public TWorkHst merge(TWorkHst entity) {
        return super.merge(daoImpl, entity);
    }

    public void persist(TWorkHst entity) {
        super.persist(daoImpl, entity);
        return ;
    }

    /**
     * 存在しなければtrueを返す
     * @param entity
     * @return
     */
    public boolean isNull(TWorkHst entity) {
        List<TWorkHst> retList = new ArrayList<>();
        retList.addAll(getTWorkHstList(entity));
        if(retList.size() == DeviceCtrlConstants.zero) {
            return true;
        }
        return false;
    }


}
