package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MDevPrmHandyListServiceDaoImpl;

/**
 * 装置情報一覧取得（ハンディ端末のみ）Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class MDevPrmHandyListDao extends SmsDao {

    private final MDevPrmHandyListServiceDaoImpl mDevPrmHandyListServiceDaoImpl;

    public MDevPrmHandyListDao() {
        mDevPrmHandyListServiceDaoImpl = new MDevPrmHandyListServiceDaoImpl();
    }

    /**
     * 装置情報一覧取得（ハンディ端末のみ）
     *
     * @return
     */
    public List<MDevPrm> getDevPrmHandyList() {
        MDevPrm mDevPrm = new MDevPrm();
        return getResultList(mDevPrmHandyListServiceDaoImpl, mDevPrm);
    }
}
