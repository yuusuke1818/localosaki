package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MDevPrmCollectListServiceDaoImpl;

/**
 * 装置情報一覧取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class MDevPrmCollectListDao extends SmsDao {

    private final MDevPrmCollectListServiceDaoImpl mDevPrmCollectListServiceDaoImpl;

    public MDevPrmCollectListDao() {
        mDevPrmCollectListServiceDaoImpl = new MDevPrmCollectListServiceDaoImpl();
    }

    /**
     * 装置情報一覧取得
     *
     * @return
     */
    public List<MDevPrm> getDevPrmCollectList() {
        MDevPrm mDevPrm = new MDevPrm();
        return getResultList(mDevPrmCollectListServiceDaoImpl, mDevPrm);
    }
}
