package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.InsertSmsMeterRequest;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.InsertSmsMeterResult;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MManualInspServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterLoadlimitServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildDevMeterRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.THopidHistoryServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MManualInsp;
import jp.co.osaki.osol.entity.MManualInspPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK;
import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MMeterLoadlimitPK;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.THopidHistory;
import jp.co.osaki.osol.entity.THopidHistoryPK;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.HOPID_HISTORY_EXECUPD;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.co.osaki.sms.SmsConstants.MMETERLOADLIMIT_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_SRV_ENT;

/**
 * メーター登録 DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class InsertSmsMeterDao extends OsolApiDao<InsertSmsMeterParameter> {

    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;

    /** 負荷制限(スマートメーターのみ使用) */
    private final MMeterLoadlimitServiceDaoImpl mMeterLoadlimitServiceDaoImpl;

    /** 建物テーブル(テナントの建物ID取得、テナント仮登録に使用) */
    private final TBuildingServiceDaoImpl tBuildingServiceDaoImpl;

    /** m_tenant_sms(テナント仮登録に使用) */
    private final MTenantSmsServiceDaoImpl mTenantSmsServiceDaoImpl;

    /** 企業(テナント仮登録時の排他制御用) */
    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;

    /** 建物メーター中間テーブル(テナントとの関係を反映) */
    private final TBuildDevMeterRelationServiceDaoImpl tBuildDevMeterRelationServiceDaoImpl;

    /** 無線ID履歴(ハンディ検針用メーターのみ使用) */
    private final THopidHistoryServiceDaoImpl tHopidHistoryServiceDaoImpl;

    /** 任意検針設定(再登録時の削除用)*/
    private final MManualInspServiceDaoImpl mManualInspServiceDaoImpl;

    /** メーターグループ設定 */
    private final MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    public InsertSmsMeterDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
        mMeterLoadlimitServiceDaoImpl = new MMeterLoadlimitServiceDaoImpl();
        tBuildDevMeterRelationServiceDaoImpl = new TBuildDevMeterRelationServiceDaoImpl();
        tHopidHistoryServiceDaoImpl = new THopidHistoryServiceDaoImpl();
        tBuildingServiceDaoImpl = new TBuildingServiceDaoImpl();
        mManualInspServiceDaoImpl = new MManualInspServiceDaoImpl();
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
    }

    @Override
    public InsertSmsMeterResult query(InsertSmsMeterParameter parameter) throws Exception {
        final InsertSmsMeterRequest result = parameter.getResult();
        /** 装置ID */
        final String devId = result.getDevId();

        /** メーター管理番号 */
        final Long meterMngId = result.getMeterMngId();

        /** メーター種別 */
        final String meterKind = parameter.getMeterKind();

        /** 機器制御からの呼び出し有無 */
        final Boolean fromDeviceCtrl = parameter.getFromDeviceCtrl();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //ログインユーザーIDを取得
        final String loginPersonId = parameter.getLoginPersonId();
        final Long loginUserId = getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId();

        // メーター情報のNULLチェック
        if (result.getDevId() == null || result.getMeterMngId() == null || result.getMeterId() == null) {
            return new InsertSmsMeterResult();
        }

        // メーター管理番号重複チェック
        // 論理削除済みの場合は、当該レコードを削除
        if (meterMngIdDuplicateCheck(result.getDevId(), result.getMeterMngId())) {
            throw new Exception("meterMngIdDuplicate");
        }

        // 計器ID重複チェック
        if (meterIdDuplicateCheck(devId, result.getMeterId())) {
            throw new Exception("meterIdDuplicate");
        }
        // ハンディのみ 無線ID重複チェック
        if (meterKind.equals(METER_KIND.HANDY.getVal()) && wirelessIdDuplicateCheck(devId, result.getWirelessId())) {
            throw new Exception("wirelessIdDuplicate");
        }

        // ユーザーコードから建物IDを照会して取得
        final TBuilding tenantBuilding = getBuildingIdOfTenant(devId, result.getTenantId());

        /** テナントの建物ID */
        Long buildingId = null;

        // テナントがない場合はnullのまま
        if (tenantBuilding != null) {
            // 登録済みのテナントの場合はその建物IDを与える
            if (tenantBuilding.getId() != null) {
                buildingId = tenantBuilding.getId().getBuildingId();
            } else {
                // テナント未登録の場合は仮の値を与える
                buildingId = SmsConstants.UNREGISTERED_TENANT_BUILDING_ID;
            }
        }

        MMeter entity = new MMeter();
        MMeterPK entityPK = new MMeterPK();

        entityPK.setDevId(result.getDevId());
        entityPK.setMeterMngId(result.getMeterMngId());
        entity.setId(entityPK);
        entity.setMeterId(result.getMeterId().toUpperCase());

        if (meterKind.equals(METER_KIND.SMART.getVal())) {
            // スマートメーターの項目
            if (result.getOpenMode() != null) {
                entity.setOpenMode(result.getOpenMode());
            }
            // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
            if ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue())) {
                // コマンドフラグ: 登録
                entity.setCommandFlg(MMETER_COMMAND_FLG.REGIST.getVal());
                // 処理待ちフラグ: 処理待ち
                entity.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
            }
            // コメント
            if (result.getMemo() != null) {
                entity.setMemo(result.getMemo());
            }

        } else if (meterKind.equals(METER_KIND.PULSE.getVal())) {
            // パルスの項目
            // 乗率
            if (result.getMulti() != null) {
                entity.setMulti(result.getMulti());
            }
            if (result.getCurrentData() != null) {
                entity.setCurrentData(result.getCurrentData());
            }
            if (result.getCurrentDataChg() != null) {
                entity.setCurrentDataChg(result.getCurrentDataChg());
            }
            if (result.getPulseType() != null) {
                entity.setPulseType(result.getPulseType());
            }
            if (result.getPulseTypeChg() != null) {
                entity.setPulseTypeChg(result.getPulseTypeChg());
            }
            if (result.getPulseWeight() != null) {
                entity.setPulseWeight(result.getPulseWeight());
            }
            if (result.getPulseWeightChg() != null) {
                entity.setPulseWeightChg(result.getPulseWeightChg());
            }
            // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
            if ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue())) {
                // コマンドフラグ: パルスメーター登録
                entity.setCommandFlg(MMETER_COMMAND_FLG.PULSE_REGIST.getVal());
                // 処理待ちフラグ: 処理待ち
                entity.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
            }
            // コメント
            if (result.getMemo() != null) {
                entity.setMemo(result.getMemo());
            }

        } else if (meterKind.equals(METER_KIND.IOTR.getVal())) {
            // IoT-R連携用メーターの項目
            // コメント
            if (result.getMemo() != null) {
                entity.setMemo(result.getMemo());
            }

        } else if (meterKind.equals(METER_KIND.HANDY.getVal())) {
            // ハンディメーターの項目
            /** 無線ID履歴登録用 */
            List<String> hopIdList = new ArrayList<String>();

            // 乗率
            if (result.getMulti() != null) {
                entity.setMulti(result.getMulti());
            }
            if (result.getWirelessId() != null) {
                entity.setWirelessId(result.getWirelessId().toUpperCase());
            }
            if (result.getWirelessType() != null) {
                entity.setWirelessType(result.getWirelessType());
            }
            if (result.getHop1Id() != null) {
                entity.setHop1Id(result.getHop1Id().toUpperCase());
                hopIdList.add(result.getHop1Id().toUpperCase());
            }
            if (result.getHop2Id() != null) {
                entity.setHop2Id(result.getHop2Id().toUpperCase());
                hopIdList.add(result.getHop2Id().toUpperCase());
            }
            if (result.getHop3Id() != null) {
                entity.setHop3Id(result.getHop3Id().toUpperCase());
                hopIdList.add(result.getHop3Id().toUpperCase());
            }
            if (result.getPollingId() != null) {
                entity.setPollingId(result.getPollingId().toUpperCase());
            }
            updateHopidHistory(devId, hopIdList, loginPersonId, loginUserId, serverDateTime);

        } else if (meterKind.equals(METER_KIND.OCR.getVal())) {
            // AieLink用メーターの項目
        }

        // 共通の項目
        if (result.getDispYearFlg() != null) {
            entity.setDispYearFlg(result.getDispYearFlg());
        }
        if (result.getExamEndYm() != null) {
            entity.setExamEndYm(result.getExamEndYm());
        }
        if (result.getExamNotice() != null) {
            entity.setExamNotice(result.getExamNotice());
        }
        if (result.getMeterType() != null) {
            entity.setMeterType(result.getMeterType());
        }


        //メーター状況、アラート停止フラグのデフォルト設定
        if (meterKind.equals(METER_KIND.SMART.getVal()) || meterKind.equals(METER_KIND.PULSE.getVal())) {
            entity.setAlertPauseFlg(BigDecimal.valueOf(OsolConstants.FLG_OFF));
            entity.setMeterPresSitu(BigDecimal.valueOf(OsolConstants.FLG_OFF));
        }

        // 機器制御のみ I/F種別
        if (((fromDeviceCtrl == null) || fromDeviceCtrl.booleanValue()) && (result.getIfType() != null)) {
            entity.setIfType(result.getIfType());
        }

        entity.setMeterSta(BigDecimal.valueOf(OsolConstants.FLG_OFF));
        entity.setTermSta(BigDecimal.valueOf(OsolConstants.FLG_OFF));

        entity.setRecMan(loginPersonId);
        entity.setRecDate(serverDateTime);

        entity.setDelFlg(OsolConstants.FLG_OFF);
        entity.setCreateUserId(loginUserId);
        entity.setCreateDate(serverDateTime);
        entity.setUpdateUserId(loginUserId);
        entity.setUpdateDate(serverDateTime);

        persist(mMeterServiceDaoImpl, entity);

        // スマートメーターの場合、負荷制限を追加
        if (meterKind.equals(METER_KIND.SMART.getVal())) {
            MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
            MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
            mMeterLoadlimitPK.setDevId(devId);
            mMeterLoadlimitPK.setMeterMngId(meterMngId);
            mMeterLoadlimit.setId(mMeterLoadlimitPK);

            if (result.getAutoInjection() != null) {
                mMeterLoadlimit.setAutoInjection(result.getAutoInjection());
            } else {
                mMeterLoadlimit.setAutoInjection(SmsConstants.AUTO_INJECTION_DEFAULT);
            }

            if (result.getBreakerActCount() != null) {
                mMeterLoadlimit.setBreakerActCount(result.getBreakerActCount());
            }

            if (result.getCountClear() != null) {
                mMeterLoadlimit.setCountClear(result.getCountClear());
            }

            if (result.getLoadCurrent() != null) {
                mMeterLoadlimit.setLoadCurrent(result.getLoadCurrent());
            }

            if (result.getLoadlimitMode() != null) {
                mMeterLoadlimit.setLoadlimitMode(result.getLoadlimitMode());
            }

            if (result.getTempAutoInjection() != null) {
                mMeterLoadlimit.setTempAutoInjection(result.getTempAutoInjection());
            } else {
                mMeterLoadlimit.setTempAutoInjection(SmsConstants.TEMP_AUTO_INJECTION_DEFAULT);
            }

            if (result.getTempBreakerActCount() != null) {
                mMeterLoadlimit.setTempBreakerActCount(result.getTempBreakerActCount());
            }

            if (result.getTempCountClear() != null) {
                mMeterLoadlimit.setTempCountClear(result.getTempCountClear());
            }

            if (result.getTempLoadCurrent() != null) {
                mMeterLoadlimit.setTempLoadCurrent(result.getTempLoadCurrent());
            }

            // LTE-Mの場合は値がnullのため、その場合は自動投入もnull(=渡ってきた値をそのまま設定)にする。
            if (checkLteMDevice(result)) {
                mMeterLoadlimit.setAutoInjection(result.getAutoInjection());
                mMeterLoadlimit.setTempAutoInjection(result.getTempAutoInjection());
            }

            // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
            if ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue())) {
                // コマンドフラグ: 負荷制限設定
                mMeterLoadlimit.setCommandFlg(MMETERLOADLIMIT_COMMAND_FLG.SETTING.getVal());

                // 処理待ちフラグ: 処理待ち
                mMeterLoadlimit.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
            }

            mMeterLoadlimit.setRecMan(loginPersonId);
            mMeterLoadlimit.setRecDate(serverDateTime);

            mMeterLoadlimit.setCreateUserId(loginUserId);
            mMeterLoadlimit.setCreateDate(serverDateTime);
            mMeterLoadlimit.setUpdateUserId(loginUserId);
            mMeterLoadlimit.setUpdateDate(serverDateTime);

            persist(mMeterLoadlimitServiceDaoImpl, mMeterLoadlimit);
        }

        // 建物メーター中間テーブルに建物との関係を登録
        TBuildDevMeterRelation tRelation = new TBuildDevMeterRelation();
        TBuildDevMeterRelationPK tRelationPK = new TBuildDevMeterRelationPK();
        tRelationPK.setDevId(devId);
        tRelationPK.setMeterMngId(meterMngId);
        tRelation.setId(tRelationPK);

        tRelation.setVersion(0);
        tRelation.setCreateUserId(loginUserId);
        tRelation.setCreateDate(serverDateTime);
        tRelation.setUpdateUserId(loginUserId);
        tRelation.setUpdateDate(serverDateTime);

        persist(tBuildDevMeterRelationServiceDaoImpl, tRelation);

        // 建物メーター中間テーブルにテナントとの関係を登録
        if (buildingId != null) {
            TBuilding newTenantBuilding = tenantBuilding;
            // 建物IDが-1の場合は、テナント自体の登録を行い、登録後の建物ID取得
            if (buildingId.equals(SmsConstants.UNREGISTERED_TENANT_BUILDING_ID)) {
                newTenantBuilding = registTenant(result.getTenantId(), devId, loginPersonId, loginUserId,
                        serverDateTime);
                buildingId = newTenantBuilding.getId().getBuildingId();
            }
            TBuildDevMeterRelation tTenantRelation = new TBuildDevMeterRelation();
            TBuildDevMeterRelationPK tTenantRelationPK = new TBuildDevMeterRelationPK();

            tTenantRelationPK.setCorpId(newTenantBuilding.getId().getCorpId());
            tTenantRelationPK.setBuildingId(buildingId);
            tTenantRelationPK.setDevId(devId);
            tTenantRelationPK.setMeterMngId(meterMngId);
            tTenantRelation.setId(tTenantRelationPK);

            tTenantRelation.setVersion(0);
            tTenantRelation.setCreateUserId(loginUserId);
            tTenantRelation.setCreateDate(serverDateTime);
            tTenantRelation.setUpdateUserId(loginUserId);
            tTenantRelation.setUpdateDate(serverDateTime);

            persist(tBuildDevMeterRelationServiceDaoImpl, tTenantRelation);

        }

        return getNewMeterInfo(devId, meterMngId);

    }

    /**
     * 装置がLTE-Mか判定する
     *
     * @param result 登録予定値
     * @return true:LTE-Mである false:LTE-Mではない
     */
    private boolean checkLteMDevice(InsertSmsMeterRequest result) {

        return result.getLoadlimitMode() == null
                && result.getLoadCurrent() == null
                && result.getTempLoadCurrent() == null
                && result.getBreakerActCount() == null
                && result.getTempBreakerActCount() == null
                && result.getCountClear() == null
                && result.getTempCountClear() == null
                && result.getOpenMode() == null;
    }

    /**
     * 計器IDの重複を検出する
     * @param devId 装置ID
     * @param meterId 計器ID
     * @return 重複していればtrue
     */
    private boolean meterIdDuplicateCheck(final String devId, final String meterId) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(devId);
        meterParam.setId(meterParamPK);
        meterParam.setMeterId(meterId.toUpperCase());
        return (getResultList(mMeterServiceDaoImpl, meterParam).size() > 0);
    }

    /**
     * 無線IDの重複を検出する
     * @param devId 装置ID
     * @param wirelessId 計器ID
     * @return 重複していればtrue
     */
    private boolean wirelessIdDuplicateCheck(final String devId, final String wirelessId) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(devId);
        meterParam.setId(meterParamPK);
        meterParam.setWirelessId(wirelessId.toUpperCase());
        return (getResultList(mMeterServiceDaoImpl, meterParam).size() > 0);
    }

    /**
     * メーター管理番号の重複を検出する。
     * 論理削除済みであれば、当該レコードを物理削除する。
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @return 重複していればtrue
     */
    private boolean meterMngIdDuplicateCheck(final String devId, final Long meterMngId) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(devId);
        meterParamPK.setMeterMngId(meterMngId);
        meterParam.setId(meterParamPK);
        MMeter foundMeter = find(mMeterServiceDaoImpl, meterParam);
        if ((foundMeter != null)
                && ((foundMeter.getDelFlg() == null) || (foundMeter.getDelFlg().equals(OsolConstants.FLG_ON)))) {
            // 既存のレコードを削除する

            // 負荷制限を削除
            MMeterLoadlimit mmlParam = new MMeterLoadlimit();
            MMeterLoadlimitPK mmlParamPK = new MMeterLoadlimitPK();
            mmlParamPK.setDevId(devId);
            mmlParamPK.setMeterMngId(meterMngId);
            mmlParam.setId(mmlParamPK);
            MMeterLoadlimit foundmml = find(mMeterLoadlimitServiceDaoImpl, mmlParam);
            if (foundmml != null) {
                remove(mMeterLoadlimitServiceDaoImpl, foundmml);
            }

            // 建物メーター中間テーブルを削除
            // パラメーター生成
            HashMap<String, List<Object>> searchParam = new HashMap<String, List<Object>>();
            List<Object> devIdList = new ArrayList<Object>();
            List<Object> meterMngIdList = new ArrayList<Object>();
            devIdList.add(devId);
            meterMngIdList.add(meterMngId);

            searchParam.put("devId", devIdList);
            searchParam.put("meterMngId", meterMngIdList);

            List<TBuildDevMeterRelation> foundtbdmr = getResultList(tBuildDevMeterRelationServiceDaoImpl, searchParam);
            for (TBuildDevMeterRelation item : foundtbdmr) {
                remove(tBuildDevMeterRelationServiceDaoImpl, item);
            }

            // 任意検針設定を削除
            MManualInsp mManualInsp = new MManualInsp();
            MManualInspPK mManualInspPK = new MManualInspPK();
            mManualInspPK.setDevId(devId);
            mManualInspPK.setMeterMngId(meterMngId);
            mManualInsp.setId(mManualInspPK);
            MManualInsp foundmmi = find(mManualInspServiceDaoImpl, mManualInsp);
            if (foundmmi != null) {
                remove(mManualInspServiceDaoImpl, foundmmi);
            }

            // メーターグループ設定を削除
            MMeterGroup mMeterGroup = new MMeterGroup();
            MMeterGroupPK mMeterGroupPK = new MMeterGroupPK();
            mMeterGroupPK.setDevId(devId);
            mMeterGroupPK.setMeterMngId(meterMngId);
            mMeterGroup.setId(mMeterGroupPK);
            List<MMeterGroup> foundmmg = getResultList(mMeterGroupServiceDaoImpl, mMeterGroup);
            for (MMeterGroup item : foundmmg) {
                remove(mMeterGroupServiceDaoImpl, item);
            }

            // メーターを削除
            remove(mMeterServiceDaoImpl, foundMeter);

            return false;
        }
        return (foundMeter != null);

    }

    /**
     * 新規登録後のエンティティを取得する
     * @param result
     * @return
     */
    private InsertSmsMeterResult getNewMeterInfo(final String devId, final Long meterMngId) {
        MMeter param = new MMeter();
        MMeterPK paramPK = new MMeterPK();
        paramPK.setDevId(devId);
        paramPK.setMeterMngId(meterMngId);
        param.setId(paramPK);
        MMeter newMeter = find(mMeterServiceDaoImpl, param);

        InsertSmsMeterResult newResult = new InsertSmsMeterResult(newMeter.getId().getMeterMngId(),
                newMeter.getId().getDevId(), newMeter.getAlarm(), newMeter.getBasicPrice(),
                newMeter.getComMeter(), newMeter.getCommandFlg(), newMeter.getConcentId(), newMeter.getCreateDate(),
                newMeter.getCreateUserId(),
                newMeter.getCurrentData(), newMeter.getCurrentDataChg(), newMeter.getDelFlg(),
                newMeter.getDispYearFlg(), newMeter.getExamEndYm(),
                newMeter.getExamNotice(), newMeter.getHop1Id(), newMeter.getHop2Id(), newMeter.getHop3Id(),
                newMeter.getIfType(), newMeter.getMemo(),
                newMeter.getMeterId(), newMeter.getMeterIdOld(), newMeter.getMeterSta(), newMeter.getMeterType(),
                newMeter.getMulti(), newMeter.getName(),
                newMeter.getOpenMode(), newMeter.getPollingId(), newMeter.getPulseType(), newMeter.getPulseTypeChg(),
                newMeter.getPulseWeight(),
                newMeter.getPulseWeightChg(), newMeter.getRecDate(), newMeter.getRecMan(), newMeter.getSrvEnt(),
                newMeter.getTermAddr(), newMeter.getTermSta(),
                newMeter.getUpdateDate(), newMeter.getUpdateUserId(), newMeter.getVersion(), newMeter.getWirelessId(),
                newMeter.getWirelessType());

        return newResult;
    }

    /**
     * 無線ID履歴に追加対象の無線IDがあるか確認し追加する。
     * 同時に、無線ID履歴登録数上限に達している場合は古い履歴を削除する。
     * @param devId 装置ID
     * @param hopIdList 今回入力された無線IDリスト
     * @param loginPersonId ログインパーソンID
     * @param loginUserId ログインユーザーID
     * @param serverDateTime 更新日時に入れる現在日時
     */
    private void updateHopidHistory(final String devId, final List<String> hopIdList, final String loginPersonId,
            final Long loginUserId, final Timestamp serverDateTime) {
        THopidHistory tHopidHistory = new THopidHistory();
        THopidHistoryPK tHopidHistoryPK = new THopidHistoryPK();
        tHopidHistoryPK.setDevId(devId);
        tHopidHistory.setId(tHopidHistoryPK);
        List<THopidHistory> currentHopidList = getResultList(tHopidHistoryServiceDaoImpl, tHopidHistory);

        // 各無線IDについて既存かどうか確認し、履歴を更新
        boolean found = false;
        for (String hopId : hopIdList) {
            for (THopidHistory record : currentHopidList) {
                // 既存のリストにあればそのエンティティを使って更新
                if (record.getId().getWirelessId().equals(hopId)) {
                    record.setRecMan(loginPersonId);
                    record.setRecDate(serverDateTime);

                    record.setUpdateUserId(loginUserId);
                    record.setUpdateDate(serverDateTime);

                    merge(tHopidHistoryServiceDaoImpl, record);

                    found = true;
                    break;
                }
            }

            // 更新されていなければまだないので新規追加する
            if (!found) {
                tHopidHistoryPK = new THopidHistoryPK();
                tHopidHistory = new THopidHistory();

                tHopidHistoryPK.setDevId(devId);
                tHopidHistoryPK.setWirelessId(hopId);

                tHopidHistory.setId(tHopidHistoryPK);

                tHopidHistory.setRecMan(loginPersonId);
                tHopidHistory.setRecDate(serverDateTime);

                tHopidHistory.setCreateUserId(loginUserId);
                tHopidHistory.setCreateDate(serverDateTime);

                tHopidHistory.setUpdateUserId(loginUserId);
                tHopidHistory.setUpdateDate(serverDateTime);

                persist(tHopidHistoryServiceDaoImpl, tHopidHistory);
            }
            found = false;
        }

        if ((hopIdList.size() + currentHopidList.size()) > SmsConstants.HOPID_HISTORY_MAX) {
            // 履歴の最大件数を超えている分について、古いものから削除
            HashMap<String, List<Object>> deleteParam = new HashMap<String, List<Object>>();
            List<Object> newParamList = new ArrayList<Object>();
            List<Object> deleteAmountList = new ArrayList<Object>();
            newParamList.add(devId);
            deleteAmountList.add(SmsConstants.HOPID_HISTORY_MAX);

            deleteParam.put(HOPID_HISTORY_EXECUPD.DELETE_HISTORY.getVal(), newParamList);
            deleteParam.put(HOPID_HISTORY_EXECUPD.MAXAMOUNT.getVal(), deleteAmountList);

            executeUpdate(tHopidHistoryServiceDaoImpl, deleteParam);
        }
    }

    /**
     * テナントのユーザーコードに対応する建物を返す
     * @param devId 装置ID
     * @param tenantId ユーザーコード
     * @return 当該建物番号のテナントの建物エンティティ
     */
    private TBuilding getBuildingIdOfTenant(final String devId, final Long tenantId) {
        // 建物番号か装置IDがnullならnullを返す
        if (tenantId == null || devId == null) {
            return null;
        }

        // パラメーター生成
        HashMap<String, List<Object>> searchParam = new HashMap<String, List<Object>>();
        List<Object> devIdList = new ArrayList<Object>();
        List<Object> tenantIdList = new ArrayList<Object>();
        devIdList.add(devId);
        tenantIdList.add(tenantId);

        searchParam.put("tenantId", tenantIdList);
        searchParam.put("devId", devIdList);
        List<TBuilding> tenant = getResultList(tBuildingServiceDaoImpl, searchParam);

        // 該当するテナントが返ってこなければ例外
        // TODO: sagi_h sizeが1より大きい(ユーザーコード被り)の場合はどうするか
        if (tenant.size() == 0) {
            return new TBuilding();
        }
        return tenant.get(0);
    }

    /**
     * 未登録ユーザーコードのテナントを新たに登録する。
     * @param tenantId ユーザーコード
     * @param devId 建物ID
     * @param loginPersonId ログインパーソンID
     * @param loginUserId ログインユーザーID
     * @param serverDateTime 更新日時に入れる現在日時
     * @return 登録されたテナントの建物エンティティ
     */
    private TBuilding registTenant(final Long tenantId, final String devId, final String loginPersonId,
            final Long loginUserId, final Timestamp serverDateTime) {
        /** 追加するテナント建物 */
        TBuilding newTenant = new TBuilding();

        /** 追加するテナント建物の主キー */
        TBuildingPK newTenantPK = new TBuildingPK();

        /** 追加するテナントのSMS固有情報 */
        MTenantSm newTenantSms = new MTenantSm();

        /** 追加するテナントのSMS固有情報の主キー */
        MTenantSmPK newTenantSmsPK = new MTenantSmPK();

        // パラメーター生成
        HashMap<String, List<Object>> searchParam = new HashMap<String, List<Object>>();
        List<Object> devIdList = new ArrayList<Object>();
        devIdList.add(devId);

        searchParam.put("devId", devIdList);
        List<TBuilding> exBuildings = getResultList(tBuildingServiceDaoImpl, searchParam);
        if (exBuildings.size() == 0) {
            return null;
        }

        /** 親建物のエンティティ */
        final TBuilding exBuilding = exBuildings.get(0);

        // テナントの建物情報を登録
        newTenantPK.setCorpId(exBuilding.getId().getCorpId());
        newTenantPK.setBuildingId(super.createId(ID_SEQUENCE_NAME.BUILDING_ID.getVal()));
        newTenant.setId(newTenantPK);

        newTenant.setBuildingNo(
                String.format(SmsConstants.FORMAT_TENANT_BUILDING_NO, exBuilding.getBuildingNo(), tenantId));
        newTenant.setBuildingName(String.format(SmsConstants.FORMAT_TENANT_BUILDING_NAME, tenantId));
        newTenant.setMPrefecture(exBuilding.getMPrefecture());
        newTenant.setAddress(exBuilding.getAddress());
        newTenant.setNyukyoTypeCd(exBuilding.getNyukyoTypeCd());
        newTenant.setEstimateUse(OsolConstants.ESTIMATE_USE.NOT_USE.getVal());
        newTenant.setFreonDischargeOffice(exBuilding.getFreonDischargeOffice());
        newTenant.setPublicFlg(exBuilding.getPublicFlg());
        newTenant.setBuildingType(OsolConstants.BUILDING_TYPE.TENANT.getVal());
        newTenant.setDivisionBuildingId(exBuilding.getId().getBuildingId());
        newTenant.setDivisionCorpId(exBuilding.getId().getCorpId());


        newTenant.setDelFlg(OsolConstants.FLG_OFF);
        newTenant.setCreateUserId(loginUserId);
        newTenant.setCreateDate(serverDateTime);

        newTenant.setUpdateUserId(loginUserId);
        newTenant.setUpdateDate(serverDateTime);

        persist(tBuildingServiceDaoImpl, newTenant);

        // 企業情報を更新(企業の排他)
        MCorp mCorp = new MCorp();
        mCorp.setCorpId(exBuilding.getId().getCorpId());
        MCorp mCorpEntity = find(mCorpApiServiceDaoImpl, mCorp);
        mCorpEntity.setUpdateDate(serverDateTime);
        mCorpEntity.setUpdateUserId(loginUserId);
        merge(mCorpApiServiceDaoImpl, mCorpEntity);

        // テナントのSMS固有情報を登録
        newTenantSmsPK.setCorpId(newTenantPK.getCorpId());
        newTenantSmsPK.setBuildingId(newTenantPK.getBuildingId());
        newTenantSms.setId(newTenantSmsPK);

        newTenantSms.setTenantId(tenantId);

        newTenantSms.setDelFlg(OsolConstants.FLG_OFF);

        newTenantSms.setRecMan(loginPersonId);
        newTenantSms.setRecDate(serverDateTime);

        newTenantSms.setCreateUserId(loginUserId);
        newTenantSms.setCreateDate(serverDateTime);

        newTenantSms.setUpdateUserId(loginUserId);
        newTenantSms.setUpdateDate(serverDateTime);

        persist(mTenantSmsServiceDaoImpl, newTenantSms);

        return find(tBuildingServiceDaoImpl, newTenant);
    }

}
