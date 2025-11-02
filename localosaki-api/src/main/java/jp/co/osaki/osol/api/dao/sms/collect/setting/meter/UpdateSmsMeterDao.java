package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;

import com.google.common.base.Objects;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.UpdateSmsMeterResult;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterLoadlimitServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildDevMeterRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.THopidHistoryServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MMeter;
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
import jp.co.osaki.sms.SmsConstants.METER_UPDATE_PATTERN;
import jp.co.osaki.sms.SmsConstants.MMETERLOADLIMIT_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETERLOADLIMIT_SRV_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_SRV_ENT;

/**
 * メーター登録内容・設定内容変更 DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class UpdateSmsMeterDao extends OsolApiDao<UpdateSmsMeterParameter> {

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

    public UpdateSmsMeterDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
        mMeterLoadlimitServiceDaoImpl = new MMeterLoadlimitServiceDaoImpl();
        tBuildDevMeterRelationServiceDaoImpl = new TBuildDevMeterRelationServiceDaoImpl();
        tHopidHistoryServiceDaoImpl = new THopidHistoryServiceDaoImpl();
        tBuildingServiceDaoImpl = new TBuildingServiceDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public UpdateSmsMeterResult query(UpdateSmsMeterParameter parameter) throws Exception {
        if (METER_UPDATE_PATTERN.SETTING.getVal().equals(parameter.getUpdatePattern())) {
            // 設定内容変更 の場合 (複数行更新あり)

            // 選択した行の メーター登録内容・設定内容を更新する
            UpdateSmsMeterResult rtn = null;
            for (UpdateSmsMeterRequestSet reqKey: parameter.getResult().getRequestSetList()) {
                parameter.getResult().setDevId(reqKey.getDevId());
                parameter.getResult().setMeterMngId(reqKey.getMeterMngId());
                parameter.getResult().setVersion(reqKey.getVersion());
                rtn = updateRow(parameter); // メーター登録内容・設定内容変更
            }
            return rtn;
        } else {
            // 全カラム更新 または 登録内容変更 の場合 (複数行更新なし)
            return updateRow(parameter); // メーター登録内容・設定内容変更
        }
    }

    /**
     * 選択行の１行を更新(全カラム更新 または 登録内容変更 または 設定内容変更)する
     *
     * @param parameter パラメータ
     * @return UpdateSmsMeterResult
     * @throws Exception
     */
    public UpdateSmsMeterResult updateRow(UpdateSmsMeterParameter parameter) throws Exception {
        final String meterKind = parameter.getMeterKind(); // メーターの種類 ※"A":スマートメーター, "P":パルスメーター, "MH":ハンディ検針用メーター
        final String updatePattern = parameter.getUpdatePattern(); // 更新パターン ※"0":全カラム更新, "1":登録内容変更, "2":設定内容変更

        final UpdateSmsMeterRequest result = parameter.getResult();
        final String devId = result.getDevId();
        final Long meterMngId = result.getMeterMngId();
        final String meterId = result.getMeterId();
        String commandFlg = null;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        MMeter exMeter;

        //ログインユーザーIDを取得
        final String loginPersonId = parameter.getLoginPersonId();
        final Long loginUserId = getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId();

        /** 機器制御からの呼び出し有無 */
        final Boolean fromDeviceCtrl = parameter.getFromDeviceCtrl();

        // メーター情報のNULL・排他チェックして既存レコード取得
        if (devId == null || meterMngId == null || meterId == null) {
            return new UpdateSmsMeterResult();
        } else {
            exMeter = meterExclusiveCheck(parameter);
        }

        // 計器ID重複チェック
        if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                && meterIdDuplicateCheck(devId, meterMngId, meterId)) {
            throw new Exception("meterIdDuplicate");
        }

        // ハンディのみ 無線ID重複チェック
        if (meterKind.equals(METER_KIND.HANDY.getVal())
                && wirelessIdDuplicateCheck(devId, meterMngId, result.getWirelessId())) {
            throw new Exception("wirelessIdDuplicate");
        }

        if (meterKind.equals(METER_KIND.SMART.getVal())) {
            // -------------------------------------------------
            // スマートメーターの処理
            // -------------------------------------------------

            // コマンドフラグ: 交換
            commandFlg = MMETER_COMMAND_FLG.CHANGE.getVal();

            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                // m_meterの更新
                updateMeterAll(exMeter, parameter, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);

            } else if (updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                // 共通の項目を設定
                updateMeterRegistCommon(exMeter, result);
                exMeter.setMemo(result.getMemo());

            } else if (updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {

                if ((result.getOpenMode() != null) && !(result.getOpenMode().equals(exMeter.getOpenMode()))) {
                    exMeter.setOpenMode(result.getOpenMode());
                }

                if (!(parameter.getSendFlg())) {
                    // 機器に送信しない場合は変更なしのためコメントアウト
//                    exMeter.setCommandFlg(null);
                } else {
                    //  コマンドフラグ: 開閉設定
                    exMeter.setCommandFlg(MMETER_COMMAND_FLG.SWITCHING.getVal());
                }

            } else {
                // エラー
                return new UpdateSmsMeterResult();
            }

            // 負荷制限テーブルの更新
            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                    || updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {
                MMeterLoadlimit mmlParam = new MMeterLoadlimit();
                MMeterLoadlimitPK mmlParamPK = new MMeterLoadlimitPK();
                mmlParamPK.setDevId(devId);
                mmlParamPK.setMeterMngId(meterMngId);
                mmlParam.setId(mmlParamPK);

                // 既存レコードを取得
                MMeterLoadlimit exmml = find(mMeterLoadlimitServiceDaoImpl, mmlParam);

                // 内容を更新
                exmml = updateMeterLoadlimit(exmml, result, fromDeviceCtrl, parameter);

                if (exmml.getCommandFlg() != null) {
                    exmml.setRecMan(loginPersonId);
                    exmml.setRecDate(serverDateTime);

                    exmml.setUpdateUserId(loginUserId);
                    exmml.setUpdateDate(serverDateTime);

                    // エンティティ更新
                    merge(mMeterLoadlimitServiceDaoImpl, exmml);
                }
            }

        } else if (meterKind.equals(METER_KIND.PULSE.getVal())) {
            // -------------------------------------------------
            // パルスメーターの処理
            // -------------------------------------------------

            // コマンドフラグ: パルス登録
            commandFlg = MMETER_COMMAND_FLG.PULSE_REGIST.getVal();
            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                // m_meterの更新
                updateMeterAll(exMeter, parameter, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);

            } else if (updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                updateMeterRegistCommon(exMeter, result);
                exMeter.setMemo(result.getMemo());
            } else if (updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {
                // コマンドフラグ: 交換
                updatePulseMeterCntr(exMeter, result, fromDeviceCtrl, parameter.getSendFlg());
            } else {
                // エラー
                return new UpdateSmsMeterResult();
            }

        } else if (meterKind.equals(METER_KIND.IOTR.getVal())) {
            // -------------------------------------------------
            // IoT-R連携用メーターの処理
            // -------------------------------------------------
            commandFlg = null;
            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                    || updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                // m_meterの更新
                updateMeterAll(exMeter, parameter, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
            } else {
                // IoT-R連携用メーターは設定内容変更はない
                return new UpdateSmsMeterResult();
            }

        } else if (meterKind.equals(METER_KIND.HANDY.getVal())) {
            // -------------------------------------------------
            // ハンディメーターの処理
            // -------------------------------------------------
            commandFlg = null;
            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                    || updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                // m_meterの更新
                updateMeterAll(exMeter, parameter, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
            } else {
                // ハンディ端末は設定内容変更はない
                return new UpdateSmsMeterResult();
            }

        } else if (meterKind.equals(METER_KIND.OCR.getVal())) {
            // -------------------------------------------------
            // AieLink用メーターの処理
            // -------------------------------------------------
            commandFlg = null;
            if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                    || updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                // m_meterの更新
                updateMeterAll(exMeter, parameter, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
            } else {
                // AieLink用メーターは設定内容変更はない
                return new UpdateSmsMeterResult();
            }

        } else {
            // メーター種類指定なし
            return new UpdateSmsMeterResult();
        }

        exMeter.setRecMan(parameter.getLoginPersonId());
        exMeter.setRecDate(serverDateTime);
        exMeter.setUpdateUserId(loginUserId);
        exMeter.setUpdateDate(serverDateTime);

        if (updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {
            updateMeterWithSendCommand(exMeter, exMeter.getCommandFlg(), fromDeviceCtrl);
        } else {
            // テナント変更を確認・DB反映
            tenantChangeCheck(exMeter, parameter, commandFlg, fromDeviceCtrl, loginPersonId, loginUserId,
                    serverDateTime);
        }
        return getNewMeterInfo(result);
    }

    /**
     * メーター情報を取得し排他チェックを行う
     * @param result APIのパラメーター
     * @return 取得した古いメーター情報
     * @throws Exception
     */
    private MMeter meterExclusiveCheck(final UpdateSmsMeterParameter parameter) throws Exception {
        UpdateSmsMeterRequest result = parameter.getResult();
        MMeter meterParam = new MMeter();
        MMeterPK meterPKParam = new MMeterPK();
        meterPKParam.setDevId(result.getDevId());
        meterPKParam.setMeterMngId(result.getMeterMngId());
        meterParam.setId(meterPKParam);

        MMeter exMeter = find(mMeterServiceDaoImpl, meterParam);

        if (exMeter == null) {
            throw new EntityNotFoundException();
        } // 機器制御から呼び出し時は排他チェック不要
        else if (parameter.getFromDeviceCtrl().booleanValue()) {
            return exMeter;
        } else if (!exMeter.getVersion().equals(result.getVersion()) && (exMeter.getDelFlg().intValue() != 1)) {
            // 既存レコードが論理削除されておらず、前に保持をしていたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exMeter;
        }
    }

    /**
     * 計器IDの重複を検出する
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param meterId 計器ID
     * @return 重複していればtrue
     */
    private boolean meterIdDuplicateCheck(final String devId, final Long meterMngId, final String meterId) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(devId);
        meterParam.setId(meterParamPK);
        meterParam.setMeterId(meterId.toUpperCase());
        List<MMeter> exList = getResultList(mMeterServiceDaoImpl, meterParam);

        // 一致するメーターがなければ重複なし
        if (exList.size() == 0) {
            return false;
        }

        // 複数のメーターが一致するか、今あるレコードが与えられたメーター管理番号と異なる場合はエラー
        return (exList.size() > 1 || !(exList.get(0).getId().getMeterMngId().equals(meterMngId)));
    }

    /**
     * 無線IDの重複を検出する
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param wirelessId 計器ID
     * @return 重複していればtrue
     */
    private boolean wirelessIdDuplicateCheck(final String devId, final Long meterMngId, final String wirelessId) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(devId);
        meterParam.setId(meterParamPK);
        meterParam.setWirelessId(wirelessId.toUpperCase());

        List<MMeter> exList = getResultList(mMeterServiceDaoImpl, meterParam);

        // 一致するメーターがなければ重複なし
        if (exList.size() == 0) {
            return false;
        }

        // 複数のメーターが一致するか、今あるレコードが与えられたメーター管理番号と異なる場合はエラー
        return (exList.size() > 1 || !(exList.get(0).getId().getMeterMngId().equals(meterMngId)));
    }

    /**
     * 更新後のエンティティを取得する
     * @param result APIのパラメーター
     * @return 更新後のエンティティ
     */
    private UpdateSmsMeterResult getNewMeterInfo(final UpdateSmsMeterRequest result) {
        MMeter meterParam = new MMeter();
        MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(result.getDevId());
        meterParamPK.setMeterMngId(result.getMeterMngId());
        meterParam.setId(meterParamPK);
        MMeter newMeter = find(mMeterServiceDaoImpl, meterParam);

        UpdateSmsMeterResult newResult = new UpdateSmsMeterResult(newMeter.getId().getMeterMngId(),
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
     * コマンド送信フラグを設定し、メーターを更新する。
     * コマンド送信フラグがnullの場合は元のエンティティで更新する。
     * @param mMeter 更新するメーターレコード
     * @param commandFlg コマンドフラグ
     * @param fromDeviceCtrl 機器制御からの呼び出し有無
     */
    private void updateMeterWithSendCommand(final MMeter mMeter, final String commandFlg,
            final Boolean fromDeviceCtrl) {
        // 機器制御からの呼び出し時はフラグセットしない
        if ((commandFlg != null) && ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue()))) {
            mMeter.setCommandFlg(commandFlg);

            // 処理待ちフラグ: 処理待ち
            mMeter.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
        }

        merge(mMeterServiceDaoImpl, mMeter);
    }

    /**
     * 登録内容変更のうち、メーター種別問わず共通の項目を設定する。
     * @param exMeter 更新前の値
     * @param result 更新値
     * @return
     */
    private MMeter updateMeterRegistCommon(MMeter exMeter, final UpdateSmsMeterRequest result) {
        // （共通の項目）検満年月(元号)
        if (!Objects.equal(result.getDispYearFlg(), exMeter.getDispYearFlg())) {
            exMeter.setDispYearFlg(result.getDispYearFlg());
        }
        // （共通の項目）検満年月
        if (!Objects.equal(result.getExamEndYm(), exMeter.getExamEndYm())) {
            exMeter.setExamEndYm(result.getExamEndYm());
        }
        // （共通の項目）検満通知
        if (!Objects.equal(result.getExamNotice(), exMeter.getExamNotice())) {
            exMeter.setExamNotice(result.getExamNotice());
        }
        // （共通の項目）メーター種別
        if (!Objects.equal(result.getMeterType(), exMeter.getMeterType())) {
            exMeter.setMeterType(result.getMeterType());
        }
        return exMeter;
    }

    /**
     * パルスメーターの設定内容変更の項目を設定する。
     * @param exMeter
     * @param result
     * @param fromDeviceCtrl
     * @return
     */
    private MMeter updatePulseMeterCntr(MMeter exMeter, final UpdateSmsMeterRequest result,
            final Boolean fromDeviceCtrl, Boolean isSend) {
        boolean isChanged = false;

        if (!Objects.equal(result.getCurrentData(), exMeter.getCurrentData())) {
            exMeter.setCurrentData(result.getCurrentData());
            isChanged = true;
        }
        if (!Objects.equal(result.getCurrentDataChg(), exMeter.getCurrentDataChg())) {
            exMeter.setCurrentDataChg(result.getCurrentDataChg());
            isChanged = true;
        }
        if (!Objects.equal(result.getPulseType(), exMeter.getPulseType())) {
            exMeter.setPulseType(result.getPulseType());
            isChanged = true;
        }
        if (!Objects.equal(result.getPulseTypeChg(), exMeter.getPulseTypeChg())) {
            exMeter.setPulseTypeChg(result.getPulseTypeChg());
            isChanged = true;
        }
        if (!Objects.equal(result.getPulseWeight(), exMeter.getPulseWeight())) {
            exMeter.setPulseWeight(result.getPulseWeight());
            isChanged = true;
        }
        if (!Objects.equal(result.getPulseWeightChg(), exMeter.getPulseWeightChg())) {
            exMeter.setPulseWeightChg(result.getPulseWeightChg());
            isChanged = true;
        }
        // 乗率
        if (!Objects.equal(result.getMulti(), exMeter.getMulti())) {
            exMeter.setMulti(result.getMulti());
            isChanged = true;
        }
        // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
        if (isChanged && ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue()))) {
            if (isSend) {
                exMeter.setCommandFlg(MMETER_COMMAND_FLG.PULSE_REGIST.getVal());
            }
        }
        return exMeter;
    }

    /**
    * 受け取ったパラメーターにあるすべての値を使ってメーターEntityを更新する
    * @param exMeter 元のレコード
    * @param parameter パラメーター
    * @param loginPersonId ログインパーソンID
    * @param loginUserId ログインユーザーID
    * @param serverDateTime 更新日時
    * @param commandFlg コマンドフラグ
     * @param fromDeviceCtrl
    * @return 更新を反映したエンティティ
    */
    private MMeter updateMeterAll(MMeter exMeter, final UpdateSmsMeterParameter parameter, final Long loginUserId,
            final Timestamp serverDateTime, final String commandFlg, final Boolean fromDeviceCtrl) {
        /*  テナントの建物IDは、中間テーブルの更新判定時に設定する
         *  更新日時・ユーザーは最後に共通で設定する
         *  機器制御と通信必要なものに関しても、各メーター種類で個別に判定して
         *  後からCOMMAND_FLGとSRV_ENTを設定
         */
        final UpdateSmsMeterRequest result = parameter.getResult();
        final String meterKind = parameter.getMeterKind();
        final String loginPersonId = parameter.getLoginPersonId();

        if (METER_KIND.SMART.getVal().equals(meterKind)) {
            // スマートメーターの項目

        // 共通の項目
        updateMeterRegistCommon(exMeter, result);

            // コメント
            if (!Objects.equal(result.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(result.getMemo());
            }

            if ((result.getOpenMode() != null) && !(result.getOpenMode().equals(exMeter.getOpenMode()))) {
                exMeter.setOpenMode(result.getOpenMode());

                if ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue())) {
                    //  コマンドフラグ: 開閉設定
                    exMeter.setCommandFlg(MMETER_COMMAND_FLG.SWITCHING.getVal());
                }
            }
        } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
            // パルスメーターの項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, result);

            // コメント
            if (!Objects.equal(result.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(result.getMemo());
            }

            // パルスメーターの設定内容変更の項目を設定
            exMeter = updatePulseMeterCntr(exMeter, result, fromDeviceCtrl, parameter.getSendFlg());

        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // IoT-R連携用メーター の項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, result);

            // コメント
            if (!Objects.equal(result.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(result.getMemo());
            }

        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            // ハンディメーター の項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, result);

            /** 無線ID履歴登録用 */
            List<String> hopIdList = new ArrayList<String>();

            if (!Objects.equal(result.getWirelessId(), exMeter.getWirelessId())) {
                exMeter.setWirelessId(result.getWirelessId() == null ? null : result.getWirelessId().toUpperCase());
            }
            if (!Objects.equal(result.getWirelessType(), exMeter.getWirelessType())) {
                exMeter.setWirelessType(result.getWirelessType());
            }
            if (!Objects.equal(result.getHop1Id(), exMeter.getHop1Id())) {
                exMeter.setHop1Id(result.getHop1Id() == null ? null : result.getHop1Id().toUpperCase());
                if (result.getHop1Id() != null) {
                    hopIdList.add(result.getHop1Id().toUpperCase());
                }
            }
            if (!Objects.equal(result.getHop2Id(), exMeter.getHop2Id())) {
                exMeter.setHop2Id(result.getHop2Id() == null ? null : result.getHop2Id().toUpperCase());
                if (result.getHop2Id() != null) {
                    hopIdList.add(result.getHop2Id().toUpperCase());
                }
            }
            if (!Objects.equal(result.getHop3Id(), exMeter.getHop3Id())) {
                exMeter.setHop3Id(result.getHop3Id() == null ? null : result.getHop3Id().toUpperCase());
                if (result.getHop3Id() != null) {
                    hopIdList.add(result.getHop3Id().toUpperCase());
                }
            }
            if (!Objects.equal(result.getPollingId(), exMeter.getPollingId())) {
                exMeter.setPollingId(result.getPollingId() == null ? null : result.getPollingId().toUpperCase());
            }

            // 無線ID履歴更新
            if (hopIdList.size() > 0) {
                updateHopidHistory(result.getDevId(), hopIdList, loginPersonId, loginUserId, serverDateTime);
            }

            // 乗率
            if (!Objects.equal(result.getMulti(), exMeter.getMulti())) {
                exMeter.setMulti(result.getMulti());
            }
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // AieLink用メーター の項目

            // （共通の項目）検満通知
            if (!Objects.equal(result.getExamNotice(), exMeter.getExamNotice())) {
                exMeter.setExamNotice(result.getExamNotice());
            }

            // （共通の項目）メーター種別
            if (!Objects.equal(result.getMeterType(), exMeter.getMeterType())) {
                exMeter.setMeterType(result.getMeterType());
            }

            // 乗率
            if (!Objects.equal(result.getMulti(), exMeter.getMulti())) {
                exMeter.setMulti(result.getMulti());
            }

        }

        // 機器制御のみ I/F種別
        if (((fromDeviceCtrl == null) || fromDeviceCtrl.booleanValue()) && (result.getIfType() != null)) {
            exMeter.setIfType(result.getIfType());
        }

        // 機器制御のみ multi
        if (((fromDeviceCtrl == null) || fromDeviceCtrl.booleanValue()) && (result.getMulti() != null)) {
            exMeter.setMulti(result.getMulti());
        }



        /* 論理削除済のメーターの場合、新規登録から呼ばれた再登録扱いのため、
         * 計器IDと、メーター・通信端末の異常状態を再設定する。
         */
        if (exMeter.getDelFlg().intValue() == 1) {
            exMeter.setMeterId(result.getMeterId());
            exMeter.setMeterSta(BigDecimal.valueOf(0));
            exMeter.setTermSta(BigDecimal.valueOf(0));
        }

        return exMeter;
    }

    /**
     *(スマートメーターのみ)負荷制限の更新後エンティティを返す
     * @param exmml 既存のエンティティ
     * @param result APIパラメーター
     * @param fromDeviceCtrl 機器制御からの呼び出しフラグ
     * @param parameter パラメーター
     * @return 負荷制限の更新後エンティティ
     */
    private MMeterLoadlimit updateMeterLoadlimit(MMeterLoadlimit exmml, final UpdateSmsMeterRequest result,
            final Boolean fromDeviceCtrl,UpdateSmsMeterParameter parameter) {
        boolean isChanged = false;
        if(parameter.getSendFlg() != null && parameter.getSendFlg()) {
            isChanged = true;
        }

        if (!Objects.equal(result.getAutoInjection(), exmml.getAutoInjection())) {
            exmml.setAutoInjection(result.getAutoInjection());
        }

        if (!Objects.equal(result.getBreakerActCount(), exmml.getBreakerActCount())) {
            exmml.setBreakerActCount(result.getBreakerActCount());
        }

        if (!Objects.equal(result.getCountClear(), exmml.getCountClear())) {
            exmml.setCountClear(result.getCountClear());
        }

        if (!Objects.equal(result.getLoadCurrent(), exmml.getLoadCurrent())) {
            exmml.setLoadCurrent(result.getLoadCurrent());
        }

        if (!Objects.equal(result.getLoadlimitMode(), exmml.getLoadlimitMode())) {
            exmml.setLoadlimitMode(result.getLoadlimitMode());
        }

        if (!Objects.equal(result.getTempAutoInjection(), exmml.getTempAutoInjection())) {
            exmml.setTempAutoInjection(result.getTempAutoInjection());
        }

        if (!Objects.equal(result.getTempBreakerActCount(), exmml.getTempBreakerActCount())) {
            exmml.setTempBreakerActCount(result.getTempBreakerActCount());
        }

        if (!Objects.equal(result.getTempCountClear(), exmml.getTempCountClear())) {
            exmml.setTempCountClear(result.getTempCountClear());
        }

        if (!Objects.equal(result.getTempLoadCurrent(), exmml.getTempLoadCurrent())) {
            exmml.setTempLoadCurrent(result.getTempLoadCurrent());
        }


        // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
        if (isChanged && ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue()))) {
            // コマンドフラグ: 負荷制限設定
            exmml.setCommandFlg(MMETERLOADLIMIT_COMMAND_FLG.SETTING.getVal());

            // 処理待ちフラグ: 処理待ち
            exmml.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());


            // 送信フラグがオフの場合は、コマンド・処理フラグをセットしない
            if(!(parameter.getSendFlg())) {

                exmml.setCommandFlg(MMETERLOADLIMIT_COMMAND_FLG.NONE.getVal());

                exmml.setSrvEnt(MMETERLOADLIMIT_SRV_FLG.NONE.getVal());


            }

        }

        return exmml;
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
     * テナント変更を確認し、変わっていれば建物メーター中間テーブルを更新する。
     * @param exMeter 現在のメーターレコード
     * @param parameter APIのパラメーター
     * @param commandFlg コマンドフラグ
     * @param fromDeviceCtrl 機器制御呼び出しフラグ
     * @param loginPersonId ログインパーソンID
     * @param loginUserId ログインユーザーID
     * @param serverDateTime サーバー時刻
     */
    private void tenantChangeCheck(MMeter exMeter, final UpdateSmsMeterParameter parameter, final String commandFlg,
            final Boolean fromDeviceCtrl, final String loginPersonId, final Long loginUserId,
            final Timestamp serverDateTime) {
        // テナント変更判定
        final TBuildDevMeterRelation exTenantRelation = getRelationWithTenantForMeter(exMeter);
        Long oldBuildingId = null;
        if (exTenantRelation != null) {
            oldBuildingId = exTenantRelation.getId().getBuildingId();
        }
        final UpdateSmsMeterRequest result = parameter.getResult();
        // ユーザーコードから建物IDを照会して取得
        final TBuilding newBuilding = getBuildingIdOfTenant(result.getDevId(), result.getTenantId());

        Long newBuildingId = null;
        // テナントがない場合はnullのまま
        if (newBuilding != null) {
            // 登録済みのテナントの場合はその建物IDを与える
            if (newBuilding.getId() != null) {
                newBuildingId = newBuilding.getId().getBuildingId();
            } else {
                // 未登録のテナントの場合は新たに登録
                TBuilding newTenantBuilding = newBuilding;
                newTenantBuilding = registTenant(result.getTenantId(), exMeter.getId().getDevId(), loginPersonId,
                        loginUserId, serverDateTime);
                newBuildingId = newTenantBuilding.getId().getBuildingId();
            }
        }

        if ((oldBuildingId == null) && (newBuildingId == null)) {
            if ((commandFlg != null) && commandFlg.equals(MMETER_COMMAND_FLG.SWITCHING.getVal())) {
                // 開閉切り替えはそのまま通す
                updateMeterWithSendCommand(exMeter, commandFlg, fromDeviceCtrl);
            } else {
                // テナント変更なければそのまま更新
                updateMeterWithSendCommand(exMeter, null, fromDeviceCtrl);
            }
        } else if ((oldBuildingId == null) && (newBuildingId != null)) {
            // コマンドフラグ・処理フラグ設定
            updateMeterWithSendCommand(exMeter, null, fromDeviceCtrl);

            // 変更前がNULLで変更後が非NULLなら建物メーター中間テーブルにテナントとの関係を登録
            TBuildDevMeterRelation tRelation = new TBuildDevMeterRelation();
            TBuildDevMeterRelationPK tRelationPK = new TBuildDevMeterRelationPK();

            tRelationPK.setBuildingId(newBuildingId);
            tRelationPK.setDevId(result.getDevId());
            tRelationPK.setMeterMngId(result.getMeterMngId());
            tRelation.setId(tRelationPK);
            tRelation.setVersion(0);
            tRelation.setCreateDate(serverDateTime);
            tRelation.setCreateUserId(loginUserId);
            tRelation.setUpdateDate(serverDateTime);
            tRelation.setUpdateUserId(loginUserId);

            persist(tBuildDevMeterRelationServiceDaoImpl, tRelation);

        } else if ((oldBuildingId != null) && (newBuildingId == null)) {

            // コマンドフラグ・処理フラグ設定
            updateMeterWithSendCommand(exMeter, null, fromDeviceCtrl);

            // 変更前が非NULLで変更後がNULLなら建物メーター中間テーブルからテナントとの関係を物理削除

            remove(tBuildDevMeterRelationServiceDaoImpl, exTenantRelation);

        } else if (!oldBuildingId.equals(newBuildingId)) {

            // コマンドフラグ・処理フラグ設定
            updateMeterWithSendCommand(exMeter, null, fromDeviceCtrl);

            // 変更前後ともNULLでなく内容が変わっていれば建物メーター中間テーブルに今あるテナントとの関係を削除
            remove(tBuildDevMeterRelationServiceDaoImpl, exTenantRelation);

            // その後新しいテナトンとの関係を建物メーター中間テーブルに登録
            TBuildDevMeterRelation tRelation = new TBuildDevMeterRelation();
            TBuildDevMeterRelationPK tRelationPK = new TBuildDevMeterRelationPK();

            tRelationPK.setBuildingId(newBuildingId);
            tRelationPK.setDevId(result.getDevId());
            tRelationPK.setMeterMngId(result.getMeterMngId());
            tRelation.setId(tRelationPK);
            tRelation.setVersion(0);
            tRelation.setCreateDate(serverDateTime);
            tRelation.setCreateUserId(loginUserId);
            tRelation.setUpdateDate(serverDateTime);
            tRelation.setUpdateUserId(loginUserId);

            persist(tBuildDevMeterRelationServiceDaoImpl, tRelation);
        } else {
            // ハンディはスルー
            if ((commandFlg != null) && commandFlg.equals(MMETER_COMMAND_FLG.SWITCHING.getVal())) {
                // 開閉切り替えはそのまま通す
                updateMeterWithSendCommand(exMeter, commandFlg, fromDeviceCtrl);
            } else {
                // テナント変更なければそのまま更新
                updateMeterWithSendCommand(exMeter, null, fromDeviceCtrl);
            }
        }
    }

    private TBuildDevMeterRelation getRelationWithTenantForMeter(final MMeter mMeter) {
        // パラメーター生成
        HashMap<String, List<Object>> searchParam = new HashMap<String, List<Object>>();
        List<Object> devIdList = new ArrayList<Object>();
        List<Object> meterMngIdList = new ArrayList<Object>();
        List<Object> tenantSearchFlgList = new ArrayList<Object>();
        devIdList.add(mMeter.getId().getDevId());
        meterMngIdList.add(mMeter.getId().getMeterMngId());
        tenantSearchFlgList.add(true);

        searchParam.put("devId", devIdList);
        searchParam.put("meterMngId", meterMngIdList);
        searchParam.put("tenantSearchFlg", tenantSearchFlgList);

        List<TBuildDevMeterRelation> exRelationList = getResultList(tBuildDevMeterRelationServiceDaoImpl,
                searchParam);
        TBuildDevMeterRelation result = null;
        if (exRelationList.size() > 0) {
            result = exRelationList.get(0);
        }
        return result;
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
