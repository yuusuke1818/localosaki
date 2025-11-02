package jp.co.osaki.osol.api.dao.sms.manualinsp.reserveinsp;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspParameter;
import jp.co.osaki.osol.api.request.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspRequest;
import jp.co.osaki.osol.api.request.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspRequestSet;
import jp.co.osaki.osol.api.result.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspResult;
import jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe.MMeterServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;

/**
 * 任意検針 予約検針日時更新 Daoクラス.
 * @author kobayashi.sho
 */
@Stateless
public class UpdateManualInspectionReserveInspDao extends OsolApiDao<UpdateManualInspectionReserveInspParameter> {

    /** メータ登録用. */
    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;

    public UpdateManualInspectionReserveInspDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    /**
     * メータ登録用の予約検針日時を更新.
     * @param parameter パラメータ
     * @return 更新結果
     * @throws Exception
     */
    @Override
    public UpdateManualInspectionReserveInspResult query(UpdateManualInspectionReserveInspParameter parameter) throws Exception {

        UpdateManualInspectionReserveInspRequest request = parameter.getRequest();

        for (UpdateManualInspectionReserveInspRequestSet requestSet : request.getRequestList()) {
            // 検索条件セット
            MMeter target = new MMeter();
            MMeterPK targetPK = new MMeterPK();
            targetPK.setDevId(requestSet.getDevId());
            targetPK.setMeterMngId(requestSet.getMeterMngId());
            target.setId(targetPK);

            // m_meter 現行レコード取得
            MMeter exMeter = find(mMeterServiceDaoImpl, target);

            if (exMeter == null || !requestSet.getVersion().equals(exMeter.getVersion())) {
                // 排他エラー
                throw new OptimisticLockException();
            }

            //登録時刻、更新時刻にセットする時刻を設定する
            Timestamp serverDateTime = getServerDateTime();

            //ログインユーザーIDを取得
            final String loginPersonId = parameter.getLoginPersonId();
            final Long loginUserId = getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId();

            // 更新値セット
            exMeter.setRecMan(parameter.getLoginPersonId());
            exMeter.setRecDate(serverDateTime);

            exMeter.setReserveInspDate(convTimestamp(request.getReserveInspDate()));       // 予約検針日時

            exMeter.setVersion(requestSet.getVersion() + 1);             // 排他制御用カラム + 1
            exMeter.setUpdateUserId(loginUserId);
            exMeter.setUpdateDate(serverDateTime);

            // 更新
            try {
                merge(mMeterServiceDaoImpl, exMeter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        UpdateManualInspectionReserveInspResult result = new UpdateManualInspectionReserveInspResult();
        return result;
    }

    /**
     * Date型をTimestamp型に変換
     * @param date Date型の日時
     * @return Timestamp型の日時
     */
    private Timestamp convTimestamp(Date date) {
        return (date == null ? null : new Timestamp(date.getTime()));
    }
}
