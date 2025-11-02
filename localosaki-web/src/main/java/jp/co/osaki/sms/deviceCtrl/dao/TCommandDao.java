package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.deviceCtrl.servicedao.TCommandDaoImpl;
/**
*
* @author hayashi_tak
*
*/
@Stateless
public class TCommandDao extends ConnectionSmsDao {
    TCommandDaoImpl daoImpl;

    public TCommandDao() {
        daoImpl = new TCommandDaoImpl();
    }

    public void remove(TCommand entity) {
        super.remove(daoImpl, entity);
    }

    public List<TCommand> getTCommandList(TCommand entity) {
        return super.getResultList(daoImpl, entity);
    }

    public TCommand find(TCommand entity) {
        return super.find(daoImpl, entity);
    }

    public TCommand merge(TCommand entity) {
        return super.merge(daoImpl, entity);
    }

    public void persist(TCommand entity) {
        super.persist(daoImpl, entity);
    }

    /**
     * 存在しなければtrueを返す
     * @param entity
     * @return
     */
    public boolean isNull(TCommand entity) {
        List<TCommand> retList = new ArrayList<>();
        retList.addAll(getTCommandList(entity));
        if(retList.size() == DeviceCtrlConstants.zero) {
            return true;
        }
        return false;
    }
}
