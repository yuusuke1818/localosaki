package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterAlertParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterAlertRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterAlertRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.UpdateSmsMeteAlertrResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.UpdateSmsMeteAlertrResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;

/**
 * メーター登録内容・アラート設定内容変更 DAOクラス
 * @author kobayashi.sho
 */
@Stateless
public class UpdateSmsMeterAlertDao extends OsolApiDao<UpdateSmsMeterAlertParameter> {

    /** メータ登録用. */
    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;

    public UpdateSmsMeterAlertDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    /**
     * メータ登録用のアラート設定を更新.
     * @param parameter パラメータ
     * @return 更新結果
     * @throws Exception
     */
    @Override
    public UpdateSmsMeteAlertrResult query(UpdateSmsMeterAlertParameter parameter) throws Exception {

        UpdateSmsMeterAlertRequest request = parameter.getResult();
        List<UpdateSmsMeteAlertrResultData> resultDataList = new ArrayList<UpdateSmsMeteAlertrResultData>();

        for (UpdateSmsMeterAlertRequestSet requestSet : request.getRequestList()) {
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
            exMeter.setMeterPresSitu(request.getMeterPresSitu());     // メーター状況 (0:通常 1:工事中 2:予備 3:その他)
            exMeter.setAlertPauseStart(request.getAlertPauseStart()); // アラート停止期間開始日
            exMeter.setAlertPauseEnd(request.getAlertPauseEnd());     // アラート停止期間終了日
            exMeter.setAlertPauseFlg(request.getAlertPauseFlg());     // アラート停止フラグ (0:停止しない 1:停止する)
            exMeter.setMeterStaMemo(request.getMeterStaMemo());       // メーター状況備考
            exMeter.setVersion(requestSet.getVersion() + 1);             // 排他制御用カラム + 1
            exMeter.setUpdateUserId(loginUserId);
            exMeter.setUpdateDate(serverDateTime);

            // 更新
            merge(mMeterServiceDaoImpl, exMeter);

            // 更新結果を返す
            resultDataList.add(new UpdateSmsMeteAlertrResultData(exMeter));
        }

        UpdateSmsMeteAlertrResult result = new UpdateSmsMeteAlertrResult(resultDataList);
        return result;
    }

}
