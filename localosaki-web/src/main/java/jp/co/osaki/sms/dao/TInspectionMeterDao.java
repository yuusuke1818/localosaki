package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.sms.ConnectionSmsDao;
import jp.co.osaki.sms.servicedao.TInspectionMeterDaoImpl;
/**
 *
 * @author hayashi_tak
 *
 */
@Stateless
public class TInspectionMeterDao extends ConnectionSmsDao  {
    private TInspectionMeterDaoImpl daoImpl;

    public TInspectionMeterDao() {
        daoImpl = new TInspectionMeterDaoImpl();
    }

    public List<TInspectionMeter> getTInspectionMeterList(TInspectionMeter entity){
        List<TInspectionMeter> tInspectionMeterList = super.getResultList(daoImpl, entity);
        return tInspectionMeterList;
    }

    public void persist(TInspectionMeter entity) {
        super.persist(daoImpl, entity);
        return ;
    }

    public TInspectionMeter merge(TInspectionMeter entity) {
        return super.merge(daoImpl, entity);
    }

}
