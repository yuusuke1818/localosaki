package jp.co.osaki.sms.deviceCtrl.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.deviceCtrl.servicedao.MDevPrmDaoImpl;

@Stateless
public class MDevPrmDao extends ConnectionSmsDao  {
    MDevPrmDaoImpl daoImpl;

    public MDevPrmDao() {
        daoImpl = new MDevPrmDaoImpl();
    }

    public MDevPrm find(MDevPrm entity) {
        return super.find(daoImpl, entity);
    }

    public void persist(MDevPrm entity) {
        super.persist(daoImpl, entity);
        return;
    }

    public void merge(MDevPrm entity) {
        super.merge(daoImpl, entity);
        return;
    }

    /**
     * 存在しなければtrueを返す
     * @param entity
     * @return
     */
    public boolean isNull(MDevPrm entity) {
        List<MDevPrm> retList = new ArrayList<>();
        retList.addAll(getMDevPrmList(entity));
        if(retList.size() == DeviceCtrlConstants.zero) {
            return true;
        }
        return false;
    }

    public List<MDevPrm> getMDevPrmList(MDevPrm entity){
        return  super.getResultList(daoImpl, entity);
      }


}
