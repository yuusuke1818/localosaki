package jp.co.osaki.sms.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.entity.TMeterReadingDownloadReservationInfo;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MeterReadingDownloadReservationDaoImpl;

@Stateless
public class MeterReadingDownloadReservationDao extends SmsDao {

    private final MeterReadingDownloadReservationDaoImpl daoImpl;

    public MeterReadingDownloadReservationDao() {
        daoImpl = new MeterReadingDownloadReservationDaoImpl();
    }

    /**
     * 参照可能な予約情報を抽出。
     */
    public List<TMeterReadingDownloadReservationInfo> getVisibleReservationInfo(Map<String, List<Object>> parameterMap) {
        return getResultList(daoImpl, parameterMap);
    }

//    /**
//     * process_statusが"処理待ち"または"処理中"の予約情報数をカウント
//     * ↑と同じメソッドだが、渡すパラメータを変えることでSQLを分岐させている
//     */
//    public List<TMeterReadingDownloadReservationInfo> getReservationCount(Map<String, List<Object>> parameterMap) {
//        return getResultList(daoImpl, parameterMap);
//    }


    /**
     * 予約情報を登録。
     */
    public void persist(TMeterReadingDownloadReservationInfo entity) {
    	entity.setReservationId(this.createReservationId());
        super.persist(daoImpl, entity);
        return ;
    }

    /**
     * 予約情報を更新。
     */
    public TMeterReadingDownloadReservationInfo merge(TMeterReadingDownloadReservationInfo entity) {
        return super.merge(daoImpl, entity);
    }


    /**
     * ダウンロードファイル取得
     */
    public TMeterReadingDownloadReservationInfo find(TMeterReadingDownloadReservationInfo entity) {
        return super.find(daoImpl, entity);
    }


    /**
     * 予約ID採番
     */
    public Long createReservationId() {
    	return super.createId(ID_SEQUENCE_NAME.RESERVATION_ID.getVal());
    }
}
