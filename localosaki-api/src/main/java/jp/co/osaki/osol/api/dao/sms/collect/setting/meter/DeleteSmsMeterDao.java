package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.DeleteSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.DeleteSmsMeterRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.DeleteSmsMeterResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConstants.MMETER_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_SRV_ENT;

/**
 * メーター削除要求 DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class DeleteSmsMeterDao extends OsolApiDao<DeleteSmsMeterParameter> {

    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;

    public DeleteSmsMeterDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public DeleteSmsMeterResult query(DeleteSmsMeterParameter parameter) throws Exception {
        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();
        DeleteSmsMeterResult result = new DeleteSmsMeterResult();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();
        for (DeleteSmsMeterRequestSet request : parameter.getResult().getRequestSetList()) {
            MMeter exMeter;
            String devId = request.getDevId();

            // メーター情報のNULL・排他チェック
            if (devId == null || request.getMeterMngId() == null) {
                return new DeleteSmsMeterResult();
            } else {
                exMeter = meterExclusiveCheck(request);
            }

            exMeter.setUpdateUserId(loginUserId);
            exMeter.setUpdateDate(serverDateTime);
            exMeter.setRecDate(serverDateTime);
            exMeter.setRecMan(parameter.getLoginPersonId());

            // ハンディまたはAieLinkの場合は、レコードを論理削除し、他のレコードから無線IDを削除
            if (devId.startsWith(DEVICE_KIND.HANDY.getVal()) || devId.startsWith(DEVICE_KIND.OCR.getVal())) {
                // 論理削除
                exMeter.setDelFlg(1);
                merge(mMeterServiceDaoImpl, exMeter);

                // 削除したメーターの無線IDをリレー無線IDに持つメーターで
                // 当該リレー無線IDを削除
                final String wirelessId = exMeter.getWirelessId();

                // パラメーター生成
                HashMap<String, List<Object>> searchParam = new HashMap<String, List<Object>>();
                List<Object> devIdList = new ArrayList<Object>();
                List<Object> wirelessIdList = new ArrayList<Object>();
                devIdList.add(devId);
                wirelessIdList.add(wirelessId);

                searchParam.put("devId", devIdList);
                searchParam.put("wirelessId", wirelessIdList);

                executeUpdate(mMeterServiceDaoImpl, searchParam);
            } else {
                // コマンドフラグ更新
//                updateMeterWithSendCommand(exMeter, MMETER_COMMAND_FLG.DELETE.getVal(), MMETER_SRV_ENT.WAIT.getVal());
                String cmdFlg = parameter.getSendFlg() ? MMETER_COMMAND_FLG.DELETE.getVal() : null;
                String srvEnt = parameter.getSendFlg() ? MMETER_SRV_ENT.WAIT.getVal() : null;
                if (!parameter.getSendFlg()) {
                    // 論理削除
                    exMeter.setDelFlg(1);
                }
                updateMeterWithSendCommand(exMeter, cmdFlg, srvEnt);
            }
        }
        return result;
    }

    /**
     * メーター情報を取得し排他チェックを行う
     * @param result APIのパラメーター
     * @return 取得した古いメーター情報
     * @throws Exception (OptimisticLockException: 排他エラー)
     */
    private MMeter meterExclusiveCheck(DeleteSmsMeterRequestSet request) throws Exception {
        MMeter meterParam = new MMeter();
        MMeterPK meterPKParam = new MMeterPK();
        meterPKParam.setDevId(request.getDevId());
        meterPKParam.setMeterMngId(request.getMeterMngId());
        meterParam.setId(meterPKParam);

        MMeter exMeter = find(mMeterServiceDaoImpl, meterParam);
        if (exMeter == null || !exMeter.getVersion().equals(request.getVersion())) {
            //排他制御のデータがない場合または前に保持をしていたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exMeter;
        }
    }

    /**
     * コマンド送信フラグを設定し、メーターを更新する。
     * @param mMeter 更新するメーターレコード
     * @param cmdFlg コマンドフラグ
     * @param srvEnt 処理フラグ
     */
    private void updateMeterWithSendCommand(MMeter mMeter, String cmdFlg, String srvEnt) {
        mMeter.setCommandFlg(cmdFlg);
        mMeter.setSrvEnt(srvEnt);

        merge(mMeterServiceDaoImpl, mMeter);
    }
}
