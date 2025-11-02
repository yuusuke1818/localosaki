package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MMeterServiceDaoImpl;

/**
 * メータ登録用 Daoクラス
 *
 * @author y.nakamura
 */
@Stateless
public class MMeterListDao extends SmsDao {

    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;

    public MMeterListDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
    }

    /**
     * テーブル「メータ登録用」レコード取得
     *
     * @return テーブル「メータ登録用」レコードリスト
     */
    public List<MMeter> getMeterIdList(String meterId) {
        MMeter mMeter = new MMeter();
        mMeter.setMeterId(meterId);

        return getResultList(mMeterServiceDaoImpl, mMeter);
    }
}
