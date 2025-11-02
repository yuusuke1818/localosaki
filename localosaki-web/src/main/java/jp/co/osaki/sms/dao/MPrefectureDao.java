package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MPrefectureServiceDaoImpl;

/**
 *
 * @author n-takada
 */
@Stateless
public class MPrefectureDao extends SmsDao {

    private final MPrefectureServiceDaoImpl impl;

    public MPrefectureDao() {
        impl = new MPrefectureServiceDaoImpl();
    }

    public List<MPrefecture> getResultList(MPrefecture entity) {
        return getResultList(impl, entity);
    }

    public MPrefecture find(MPrefecture entity) {
        return find(impl, entity);
    }
}
