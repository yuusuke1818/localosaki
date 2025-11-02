package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MGenericType;
import jp.co.osaki.osol.entity.MGenericTypePK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MGenericTypeServiceDaoImpl;

/**
 *
 * 汎用区分Daoクラス
 *
 * @author take_suzuki
 */
@Stateless
public class MGenericTypeDao extends SmsDao {

    private final MGenericTypeServiceDaoImpl impl;

    public MGenericTypeDao() {
        impl = new MGenericTypeServiceDaoImpl();
    }
    public List<MGenericType> getResultList(String GroupCode) {
        MGenericType mGenericType = new MGenericType();
        MGenericTypePK mGenericTypePK = new MGenericTypePK();
        mGenericTypePK.setGroupCode(GroupCode);
        mGenericType.setId(mGenericTypePK);
        return getResultList(impl, mGenericType);
    }
}
