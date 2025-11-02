package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MDevPrmOcrListServiceDaoImpl;

/**
 * 装置情報一覧取得（AieLinkのみ）Daoクラス
 * 「OCR検針」→「AieLink」へ変更
 *
 * @author iwasaki_y
 */
@Stateless
public class MDevPrmOcrListDao extends SmsDao {

    private final MDevPrmOcrListServiceDaoImpl mDevPrmOcrListServiceDaoImpl;

    public MDevPrmOcrListDao() {
        mDevPrmOcrListServiceDaoImpl = new MDevPrmOcrListServiceDaoImpl();
    }

    /**
     * 装置情報一覧取得（AieLinkのみ）
     * 「OCR検針」→「AieLink」へ変更
     *
     * @return
     */
    public List<MDevPrm> getDevPrmOcrList() {
        MDevPrm mDevPrm = new MDevPrm();
        return getResultList(mDevPrmOcrListServiceDaoImpl, mDevPrm);
    }
}
