package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.google.common.base.Objects;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateBulkSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateBulkSmsMeterRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.UpdateSmsMeterResult;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MManualInspServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterLoadlimitServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MeterMngIdResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.MeterMngIdServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildDevMeterRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.THopidHistoryServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
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
import jp.co.osaki.sms.SmsConstants.METER_UPDATE_PATTERN;
import jp.co.osaki.sms.SmsConstants.MMETERLOADLIMIT_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_SRV_ENT;

/**
 * メーター登録 DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class UpdateBulkSmsMeterDao extends OsolApiDao<UpdateBulkSmsMeterParameter> {

    // meterMngIdDuplicateCheck(～) 処理 の返値
    private static final int MMETER_NO_DUPLICATION = 0;       // 重複なし
    private static final int MMETER_THERE_IS_DUPLICATION = 1; // 重複あり(エラー)
    private static final int MMETER_LOGICAL_DELETION = 2;     // 削除データあり(物理削除済み)

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

    /** 任意検針設定(再登録時の削除用)*/
    private final MManualInspServiceDaoImpl mManualInspServiceDaoImpl;

    /** メーターグループ設定 */
    private final MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    /** (AieLink用メーター用)メーター管理番号取得  ※取得できない場合は自動採番. */
    private final MeterMngIdServiceDaoImpl meterMngIdServiceDaoImpl;

    /** 無線ID履歴(ハンディ検針用メーターのみ使用) */
    private final THopidHistoryServiceDaoImpl tHopidHistoryServiceDaoImpl;

    @Inject
    GenericTypeUtility genericTypeUtility;

    public UpdateBulkSmsMeterDao() {
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
        mMeterLoadlimitServiceDaoImpl = new MMeterLoadlimitServiceDaoImpl();
        tBuildDevMeterRelationServiceDaoImpl = new TBuildDevMeterRelationServiceDaoImpl();
        tBuildingServiceDaoImpl = new TBuildingServiceDaoImpl();
        mManualInspServiceDaoImpl = new MManualInspServiceDaoImpl();
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        meterMngIdServiceDaoImpl = new MeterMngIdServiceDaoImpl();
        tHopidHistoryServiceDaoImpl = new THopidHistoryServiceDaoImpl();
    }

    private final String ERROR_BASE = "管理番号:%s 計器ID:%s";

    @Override
    public List<String> query(UpdateBulkSmsMeterParameter parameter) {

        //レスポンス用List作成
        List<String> resultList = new ArrayList<>();

        //既存メーターリストを取得
        MMeter meterParam = new MMeter();
        MMeterPK meterPK = new MMeterPK();
        meterPK.setDevId(parameter.getRequest().getDevId());
        meterParam.setId(meterPK);
        List<MMeter> meterList = getResultList(mMeterServiceDaoImpl, meterParam);
        if (meterList == null) {
            return resultList;
        }
        //管理番号のみのリストを作成
        List<Long> meterMngIdList = new ArrayList<>();

        // メーター別にフィルタリング
        if(parameter.getRequest().getDevId().startsWith(METER_KIND.IOTR.getVal())
                || parameter.getRequest().getDevId().startsWith(METER_KIND.HANDY.getVal())
                || parameter.getRequest().getDevId().startsWith(METER_KIND.OCR.getVal())) {
            meterMngIdList = meterList.stream()
            .map(x -> x.getId().getMeterMngId()).collect(Collectors.toList());
        } else {
            meterMngIdList = meterList.stream()
            .filter(x -> x.getMeterId().startsWith(parameter.getMeterKind()))
            .map(x -> x.getId().getMeterMngId()).collect(Collectors.toList());
        }

        Integer index = 0;
        for (UpdateBulkSmsMeterRequestSet request: parameter.getRequest().getRequestSetList()) {
            index++;
            List<String> errorList = new ArrayList<>();
            final String updatePattern = parameter.getUpdatePattern();

            final String devId = parameter.getRequest().getDevId();
            final String meterId = request.getMeterId();

            // 「OCR検針」→「AieLink」へ変更
            // AieLinkフラグ  true:[AieLink用メーター]タブ  false:[AieLink用メーター]タブ以外 ([スマートメーター]タブの場合)
            final boolean isOcr = METER_KIND.OCR.getVal().equals(parameter.getMeterKind());

            if (isOcr) {
                // AieLink用メーターの場合 → メーター管理番号は、1～自動採番 する

                // 装置ID と メータID で m_meter を検索して メーター管理番号 が取得でき場合は 既存レコードを更新。
                // 取得できない場合は、装置ID毎 に1から連番を採番する。
                MeterMngIdResultData target = new MeterMngIdResultData(devId, meterId);
                MeterMngIdResultData entity = find(meterMngIdServiceDaoImpl, target);

                request.setMeterMngId(entity.getMeterMngId());
            }

            final Long meterMngId = request.getMeterMngId();

            //登録時刻、更新時刻にセットする時刻を設定する
            Timestamp serverDateTime = getServerDateTime();

            //ログインユーザーIDを取得
            final String loginPersonId = parameter.getLoginPersonId();
            final Long loginUserId = getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId();

            /** 機器制御からの呼び出し有無 */
            final Boolean fromDeviceCtrl = parameter.getFromDeviceCtrl();

            // 更新区分より判定 (更新なし:0 / 更新あり:1 / 新規登録:2)
            // 更新なしの場合は登録処理は実施しない
            String noUpdate = genericTypeUtility.getKbnName(ApiGenericTypeConstants.GROUP_CODE.UPDATE_KBN_137,
                    ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal());
            if (request.getUpdateKbn().equals(noUpdate)) {
                continue;
            }

            if (!meterMngIdList.contains(request.getMeterMngId())) {
                // 未登録の管理番号 → 新規登録

                // メーター管理番号重複チェック
                // 論理削除済みの場合は、当該レコードを削除
                int chkCd = meterMngIdDuplicateCheck(devId, request.getMeterMngId());
                if (chkCd == MMETER_THERE_IS_DUPLICATION) {
                    errorList.add("管理番号:" + meterMngId + " 重複あり(データベースに既に登録済みの値です)");
                }

                // 計器ID重複チェック
                if (meterIdDuplicateCheck(devId, meterId)) {
                    errorList.add(String.format(ERROR_BASE, meterMngId, meterId) + " 重複あり(データベースに既に登録済みの値です)");
                }

                //エラーがある場合はリザルトに登録
                if (!errorList.isEmpty()) {
                    resultList.addAll(errorList);
                }

                //エラーがある場合は登録処理は実施しない
                if (!resultList.isEmpty()) {
                    continue;
                }

                // ユーザーコードから建物IDを照会して取得
                final TBuilding tenantBuilding = getBuildingIdOfTenant(devId, request.getTenantId());

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

                // メータ登録用 登録処理

                MMeter entity = new MMeter();
                MMeterPK entityPK = new MMeterPK();

                entityPK.setDevId(devId);
                entityPK.setMeterMngId(request.getMeterMngId());
                entity.setId(entityPK);
                entity.setMeterId(meterId.toUpperCase());

                // -------------------------------------------------
                // スマートメーターの項目
                // -------------------------------------------------
                if(METER_KIND.SMART.getVal().equals(parameter.getMeterKind())) {
                    if (request.getOpenMode() != null) {
                        entity.setOpenMode(request.getOpenMode());
                    }

                    // 機器制御からの登録ではない場合　かつ　CSVファイル内で「機器への送信する」とした場合、コマンド・処理フラグをセットする
                    if ((fromDeviceCtrl == null || !(fromDeviceCtrl.booleanValue())) && request.isSendDeviceFlg()) {
                        // コマンドフラグ: 登録
                        entity.setCommandFlg(MMETER_COMMAND_FLG.REGIST.getVal());
                        //処理フラグ: 処理待ち
                        entity.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
                    }
                } else if (METER_KIND.PULSE.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // パルスメーターの項目
                    // -------------------------------------------------
                    // 乗率
                    if (request.getMulti() != null) {
                        entity.setMulti(request.getMulti());
                    } else {
                        entity.setMulti(BigDecimal.ONE);
                    }
                    // パルス種別変更フラグ
                    if (request.getPulseTypeChg() != null) {
                        entity.setPulseTypeChg(request.getPulseTypeChg());
                    }
                    // パルス種別
                    if (request.getPulseType() != null) {
                        entity.setPulseType(request.getPulseType());
                    }
                    // パルス重み変更フラグ
                    if (request.getPulseWeightChg() != null) {
                        entity.setPulseWeightChg(request.getPulseWeightChg());
                    }
                    // パルス重み
                    if (request.getPulseWeight() != null) {
                        entity.setPulseWeight(request.getPulseWeight());
                    }
                    // 指針値変更フラグ
                    if (request.getCurrentDataChg() != null) {
                        entity.setCurrentDataChg(request.getCurrentDataChg());
                    }
                    // 指針値
                    if (request.getCurrentData() != null) {
                        entity.setCurrentData(request.getCurrentData());
                    }
                    // 機器への設定送信 メーター管理画面とCSVファイル内で両方とも「送信する」とした場合
                    if(request.isSendLumpFlg() && request.isSendDeviceFlg()) {
                        // コマンドフラグ: パルス登録
                        entity.setCommandFlg(MMETER_COMMAND_FLG.PULSE_REGIST.getVal());
                        //処理フラグ: 処理待ち
                        entity.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
                    }
                } else if (METER_KIND.IOTR.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // IoT-R連携用メーターの項目
                    // -------------------------------------------------
                    // IoT-R連携用メーターとして設定する項目は無し

                } else if (METER_KIND.HANDY.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // ハンディメーターの項目
                    // -------------------------------------------------
                    /** 無線ID履歴登録用 */
                    List<String> hopIdList = new ArrayList<String>();

                    // 乗率
                    if (request.getMulti() != null) {
                        entity.setMulti(request.getMulti());
                    }
                    if (request.getWirelessId() != null) {
                        entity.setWirelessId(request.getWirelessId().toUpperCase());
                    }
                    if (request.getWirelessType() != null) {
                        entity.setWirelessType(request.getWirelessType());
                    }
                    if (request.getHop1Id() != null) {
                        entity.setHop1Id(request.getHop1Id().toUpperCase());
                        hopIdList.add(request.getHop1Id().toUpperCase());
                    }
                    if (request.getHop2Id() != null) {
                        entity.setHop2Id(request.getHop2Id().toUpperCase());
                        hopIdList.add(request.getHop2Id().toUpperCase());
                    }
                    if (request.getHop3Id() != null) {
                        entity.setHop3Id(request.getHop3Id().toUpperCase());
                        hopIdList.add(request.getHop3Id().toUpperCase());
                    }
                    if (request.getPollingId() != null) {
                        entity.setPollingId(request.getPollingId().toUpperCase());
                    }
                    updateHopidHistory(devId, hopIdList, loginPersonId, loginUserId, serverDateTime);
                }
                // -------------------------------------------------
                // 共通の項目
                // -------------------------------------------------
                // コメント
                if (request.getMemo() != null) {
                    entity.setMemo(request.getMemo());
                }
                // 検満西暦・和暦
                if (request.getDispYearFlg() != null) {
                    entity.setDispYearFlg(request.getDispYearFlg());
                } else {
                    entity.setDispYearFlg("0");
                }
                // 検満年月
                if (request.getExamEndYm() != null) {
                    entity.setExamEndYm(request.getExamEndYm());
                }
                // 検満通知
                if (request.getExamNotice() != null) {
                    entity.setExamNotice(request.getExamNotice());
                } else {
                    entity.setExamNotice("1");
                }
                // メーター種別
                if (request.getMeterType() != null) {
                    entity.setMeterType(request.getMeterType());
                } else {
                    entity.setMeterType(1L);
                }

                entity.setMeterPresSitu(new BigDecimal("0"));
                entity.setAlertPauseFlg(BigDecimal.valueOf(OsolConstants.FLG_OFF));
                entity.setMeterSta(BigDecimal.valueOf(OsolConstants.FLG_OFF));
                entity.setTermSta(BigDecimal.valueOf(OsolConstants.FLG_OFF));

                entity.setRecMan(loginPersonId);
                entity.setRecDate(serverDateTime);

                if (isOcr && chkCd == MMETER_LOGICAL_DELETION) { // 削除データあり ※再登録のため物理削除済み
                    entity.setDelFlg(OsolConstants.FLG_ON);
                } else { // if (!isOcr || chkCd == MMETER_NO_DUPLICATION) { // 重複なし(通所)
                entity.setDelFlg(OsolConstants.FLG_OFF);
                }
                entity.setCreateUserId(loginUserId);
                entity.setCreateDate(serverDateTime);
                entity.setUpdateUserId(loginUserId);
                entity.setUpdateDate(serverDateTime);

                persist(mMeterServiceDaoImpl, entity);
                //登録メーターリストに追加
                meterMngIdList.add(request.getMeterMngId());

                // スマートメーターの場合、負荷制限を追加
                if (METER_KIND.SMART.getVal().equals(parameter.getMeterKind())) {
                    //負荷制限を追加
                    MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
                    MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
                    mMeterLoadlimitPK.setDevId(devId);
                    mMeterLoadlimitPK.setMeterMngId(meterMngId);
                    mMeterLoadlimit.setId(mMeterLoadlimitPK);

                    // NULL以外 または LTE-M一括登録の場合
                    if (request.getAutoInjection() != null || parameter.getLteMLumpRegistExecFlg()) {
                        mMeterLoadlimit.setAutoInjection(request.getAutoInjection());
                    } else {
                        mMeterLoadlimit.setAutoInjection(SmsConstants.AUTO_INJECTION_DEFAULT);
                    }

                    if (request.getBreakerActCount() != null) {
                        mMeterLoadlimit.setBreakerActCount(request.getBreakerActCount());
                    }

                    if (request.getCountClear() != null) {
                        mMeterLoadlimit.setCountClear(request.getCountClear());
                    }

                    if (request.getLoadCurrent() != null) {
                        // 1桁の場合は先頭に「0」を付与する
                        mMeterLoadlimit.setLoadCurrent(request.getLoadCurrent().length() == 1 ? "0" + request.getLoadCurrent() : request.getLoadCurrent());
                    }

                    if (request.getLoadlimitMode() != null) {
                        mMeterLoadlimit.setLoadlimitMode(request.getLoadlimitMode());
                    }

                    // NULL以外 または LTE-M一括登録の場合
                    if (request.getTempAutoInjection() != null || parameter.getLteMLumpRegistExecFlg()) {
                        mMeterLoadlimit.setTempAutoInjection(request.getTempAutoInjection());
                    } else {
                        mMeterLoadlimit.setTempAutoInjection(SmsConstants.TEMP_AUTO_INJECTION_DEFAULT);
                    }

                    if (request.getTempBreakerActCount() != null) {
                        mMeterLoadlimit.setTempBreakerActCount(request.getTempBreakerActCount());
                    }

                    if (request.getTempCountClear() != null) {
                        mMeterLoadlimit.setTempCountClear(request.getTempCountClear());
                    }

                    if (request.getTempLoadCurrent() != null) {
                        // 1桁の場合は先頭に「0」を付与する
                        mMeterLoadlimit.setTempLoadCurrent(request.getTempLoadCurrent().length() == 1 ? "0" + request.getTempLoadCurrent() : request.getTempLoadCurrent());
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
                        newTenantBuilding = registTenant(request.getTenantId(), devId, loginPersonId, loginUserId,
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

            } else {
                // 指定された管理番号は登録済みの管理番号 → 更新

                String commandFlg = null;

                //既存メーターリストを取得
                meterParam = new MMeter();
                meterPK = new MMeterPK();
                meterPK.setDevId(parameter.getRequest().getDevId());
                meterPK.setMeterMngId(request.getMeterMngId());
                meterParam.setId(meterPK);
                MMeter exMeter = find(mMeterServiceDaoImpl, meterParam);

                // 計器ID重複チェック
                if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
                        && meterIdDuplicateCheck(devId, meterMngId, meterId)) {
                    errorList.add(String.format(ERROR_BASE, meterMngId, meterId) + " 重複あり(データベースに既に登録済みの値です)");
                }

                //エラーがある場合はリザルトに登録
                if (!errorList.isEmpty()) {
                    resultList.addAll(errorList);
                }

                //エラーがある場合は登録処理は実施しない
                if (!resultList.isEmpty()) {
                    continue;
                }
                // 更新時と一括登録時で同様の処理にするためコメントアウト
//                // AieLink 且つ 検満年月 変更なし → スキップ
//                if (isOcr && Objects.equal(exMeter.getExamEndYm(), request.getExamEndYm())) {
//                    continue;
//                }

                if(METER_KIND.SMART.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // スマートメーターの処理
                    // -------------------------------------------------
                    // コマンドフラグ: 交換
                    commandFlg = MMETER_COMMAND_FLG.CHANGE.getVal();

                    // m_meterの更新
                    updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl, parameter.getLteMLumpRegistExecFlg());

                    // LTE-M一括登録以外の場合
                    if (!parameter.getLteMLumpRegistExecFlg()) {
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
                            exmml = updateMeterLoadlimit(exmml, request, fromDeviceCtrl);

                            if (exmml.getCommandFlg() != null) {
                                exmml.setRecMan(loginPersonId);
                                exmml.setRecDate(serverDateTime);
                                exmml.setUpdateUserId(loginUserId);
                                exmml.setUpdateDate(serverDateTime);

                                // エンティティ更新
                                merge(mMeterLoadlimitServiceDaoImpl, exmml);
                            }
                        }
                    }
                } else if (METER_KIND.PULSE.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // パルスメーターの処理
                    // -------------------------------------------------
                    // コマンドフラグ: パルス登録
                    commandFlg = MMETER_COMMAND_FLG.PULSE_REGIST.getVal();
                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                        // m_meterの更新
                        updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
                    } else if (updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                        updateMeterRegistCommon(exMeter, request);
                        exMeter.setMemo(request.getMemo());
                    } else if (updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {
                        // コマンドフラグ: 交換
                        updatePulseMeterCntr(exMeter, request, fromDeviceCtrl);
                    } else {
                        // エラー
                        errorList.add(String.format(ERROR_BASE, meterMngId, meterId) + "updatePatternが不正です。");

                        //エラーがある場合はリザルトに登録
                        if (!errorList.isEmpty()) {
                            resultList.addAll(errorList);
                        }

                        //エラーがある場合は登録処理は実施しない
                        if (!resultList.isEmpty()) {
                            continue;
                        }
                    }
                } else if (METER_KIND.IOTR.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // IoT-R連携用メーターの処理
                    // -------------------------------------------------
                    commandFlg = null;
                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                        // m_meterの更新
                        updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
                    }
                } else if (METER_KIND.HANDY.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // ハンディメーターの処理
                    // -------------------------------------------------
                    commandFlg = null;
                    // 一括登録のupdatePatternは全てALL（全カラム更新）としているためコメントアウト
//                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
//                            || updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                        // m_meterの更新
                        updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
//                    } else {
//                        // ハンディ端末は設定内容変更はない
//                        return new UpdateSmsMeterResult();
                    }
                } else if (METER_KIND.OCR.getVal().equals(parameter.getMeterKind())) {
                    // -------------------------------------------------
                    // AieLink用メーターの処理
                    // -------------------------------------------------
                    commandFlg = null;
                    // 一括登録のupdatePatternは全てALL（全カラム更新）としているためコメントアウト
//                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())
//                            || updatePattern.equals(METER_UPDATE_PATTERN.REGIST.getVal())) {
                    if (updatePattern.equals(METER_UPDATE_PATTERN.ALL.getVal())) {
                        // m_meterの更新
                        updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl);
//                    } else {
//                        // ハンディ端末は設定内容変更はない
//                        return new UpdateSmsMeterResult();
                    }
                }

                exMeter.setRecMan(parameter.getLoginPersonId());
                exMeter.setRecDate(serverDateTime);
                exMeter.setUpdateUserId(loginUserId);
                exMeter.setUpdateDate(serverDateTime);

                if (updatePattern.equals(METER_UPDATE_PATTERN.SETTING.getVal())) {
                    updateMeterWithSendCommand(exMeter, exMeter.getCommandFlg(), fromDeviceCtrl);
                  // 更新時と一括登録時で同様の処理にするためコメントアウト
//                } else if (!isOcr) { // [AieLink]タブではないこと
                } else {
                    // テナント変更を確認・DB反映
                    tenantChangeCheck(exMeter, devId, request, commandFlg, fromDeviceCtrl, loginPersonId, loginUserId,
                            serverDateTime);
                }

            }
        }

        return resultList;

    }

    /**
     * 登録内容変更のうち、メーター種別問わず共通の項目を設定する。
     * @param exMeter 更新前の値
     * @param result 更新値
     * @return
     */
    private MMeter updateMeterRegistCommon(MMeter exMeter, final UpdateBulkSmsMeterRequestSet result) {
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
    private MMeter updatePulseMeterCntr(MMeter exMeter, final UpdateBulkSmsMeterRequestSet result,
            final Boolean fromDeviceCtrl) {
        boolean isChanged = false;

        // 指針値
        if (!Objects.equal(result.getCurrentData(), exMeter.getCurrentData())) {
            exMeter.setCurrentData(result.getCurrentData());
            isChanged = true;
        }

        // 指針値変更フラグ
        if (!Objects.equal(result.getCurrentDataChg(), exMeter.getCurrentDataChg())) {
            exMeter.setCurrentDataChg(result.getCurrentDataChg());
            isChanged = true;
        }

        // パルス種別
        if (!Objects.equal(result.getPulseType(), exMeter.getPulseType())) {
            exMeter.setPulseType(result.getPulseType());
            isChanged = true;
        }

        // パルス種別変更フラグ
        if (!Objects.equal(result.getPulseTypeChg(), exMeter.getPulseTypeChg())) {
            exMeter.setPulseTypeChg(result.getPulseTypeChg());
            isChanged = true;
        }

        // パルス重み
        if (!Objects.equal(result.getPulseWeight(), exMeter.getPulseWeight())) {
            exMeter.setPulseWeight(result.getPulseWeight());
            isChanged = true;
        }

        // パルス重み変更フラグ
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
            // fromDeviceCtrlで判定するためコメントアウト
//            if (isSend) {
                // コマンドフラグ: パルス登録
                exMeter.setCommandFlg(MMETER_COMMAND_FLG.PULSE_REGIST.getVal());
                // 処理フラグ: 処理待ち
                exMeter.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
//            }
        }
        return exMeter;
    }

    /**
     * 計器IDの重複を検出する(新規登録処理用)
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
     * メーター管理番号の重複を検出する。
     * 論理削除済みであれば、当該レコードを物理削除する。
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @return 重複フラグ  0:重複なし  1:重複あり(エラー)  2:削除データあり(物理削除済み)
     */
    private int meterMngIdDuplicateCheck(final String devId, final Long meterMngId) {
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

            return MMETER_LOGICAL_DELETION; // 論理削除データあり(論理削除データは削除済み→再登録)
        }
        return (foundMeter != null ? MMETER_THERE_IS_DUPLICATION : MMETER_NO_DUPLICATION);

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

    /**
     * 計器IDの重複を検出する(更新処理用)
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
     * コマンド送信フラグを設定し、メーターを更新する。
     * コマンド送信フラグがnullの場合は元のエンティティで更新する。
     * @param mMeter 更新するメーターレコード
     * @param commandFlg コマンドフラグ
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

    private MMeter updateMeterAll(MMeter exMeter, final UpdateBulkSmsMeterParameter parameter, UpdateBulkSmsMeterRequestSet request, final Long loginUserId,
            final Timestamp serverDateTime, final String commandFlg, final Boolean fromDeviceCtrl) {
        return updateMeterAll(exMeter, parameter, request, loginUserId, serverDateTime, commandFlg, fromDeviceCtrl, false);
    }
    /**
    * 受け取ったパラメーターにあるすべての値を使ってメーターEntityを更新する
    * @param exMeter 元のレコード
    * @param request パラメーター
    * @param loginPersonId ログインパーソンID
    * @param loginUserId ログインユーザーID
    * @param serverDateTime 更新日時
    * @param commandFlg コマンドフラグ
    * @param fromDeviceCtrl 機器制御からの呼び出し有無
    * @param LteMLumpRegistExecFlg LTE-M一括登録フラグ
    * @return 更新を反映したエンティティ
    */
    private MMeter updateMeterAll(MMeter exMeter, final UpdateBulkSmsMeterParameter parameter, UpdateBulkSmsMeterRequestSet request, final Long loginUserId,
            final Timestamp serverDateTime, final String commandFlg, final Boolean fromDeviceCtrl, final Boolean LteMLumpRegistExecFlg) {
        /*  テナントの建物IDは、中間テーブルの更新判定時に設定する
         *  更新日時・ユーザーは最後に共通で設定する
         *  機器制御と通信必要なものに関しても、各メーター種類で個別に判定して
         *  後からCOMMAND_FLGとSRV_ENTを設定
         */
        final String loginPersonId = parameter.getLoginPersonId();

        if (METER_KIND.SMART.getVal().equals(parameter.getMeterKind())) {
            // スマートメーターの項目

            // 共通の項目に集約のためコメントアウト
//            // メーター種別
//            if (request.getMeterType() != null) {
//                exMeter.setMeterType(request.getMeterType());
//            }
//
//            // 検満年月(元号)
//            if (!Objects.equal(request.getDispYearFlg(), exMeter.getDispYearFlg())) {
//                exMeter.setDispYearFlg(request.getDispYearFlg());
//            }
//
//            // 検満年月
//            if (request.getExamEndYm() != null && !request.getExamEndYm().isEmpty()) {
//                exMeter.setExamEndYm(request.getExamEndYm());
//            }
//
//            // 検満通知
//            if (!Objects.equal(request.getExamNotice(), exMeter.getExamNotice())) {
//                exMeter.setExamNotice(request.getExamNotice());
//            }

            // 共通の項目
            updateMeterRegistCommon(exMeter, request);

            // 開閉区分に関わらずコマンドフラグと処理フラグを設定するためコメントアウト
            // 開閉区分
//            if ((request.getOpenMode() != null) && !(request.getOpenMode().equals(exMeter.getOpenMode()))) {
            if (request.getOpenMode() != null && !LteMLumpRegistExecFlg) { // LTE-M一括登録ではない場合
                exMeter.setOpenMode(request.getOpenMode());
            }

            if ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue())) {
                // コマンドフラグ: 開閉設定
                exMeter.setCommandFlg(MMETER_COMMAND_FLG.SWITCHING.getVal());
                // 処理フラグ: 処理待ち
                exMeter.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
            }
//            }

            // コメント
            if (!Objects.equal(request.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(request.getMemo());
            }

        } else if (METER_KIND.PULSE.getVal().equals(parameter.getMeterKind())) {
            // パルスメーターの項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, request);

            // コメント
            if (!Objects.equal(request.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(request.getMemo());
            }

            // パルスメーターの設定内容変更の項目を設定
            exMeter = updatePulseMeterCntr(exMeter, request, fromDeviceCtrl);

        } else if (METER_KIND.IOTR.getVal().equals(parameter.getMeterKind())) {
            // IoT-R連携用メーター の項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, request);

            // コメント
            if (!Objects.equal(request.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(request.getMemo());
            }

        } else if (METER_KIND.HANDY.getVal().equals(parameter.getMeterKind())) {
            // ハンディメーター の項目

            // 共通の項目
            updateMeterRegistCommon(exMeter, request);

            /** 無線ID履歴登録用 */
            List<String> hopIdList = new ArrayList<String>();

            // 無線ID
            if (!Objects.equal(request.getWirelessId(), exMeter.getWirelessId())) {
                exMeter.setWirelessId(request.getWirelessId() == null ? null : request.getWirelessId().toUpperCase());
            }

            // 無線種別
            if (!Objects.equal(request.getWirelessType(), exMeter.getWirelessType())) {
                exMeter.setWirelessType(request.getWirelessType());
            }

            // リレー無線ID1
            if (!Objects.equal(request.getHop1Id(), exMeter.getHop1Id())) {
                exMeter.setHop1Id(request.getHop1Id() == null ? null : request.getHop1Id().toUpperCase());
                if (request.getHop1Id() != null) {
                    hopIdList.add(request.getHop1Id().toUpperCase());
                }
            }

            // リレー無線ID2
            if (!Objects.equal(request.getHop2Id(), exMeter.getHop2Id())) {
                exMeter.setHop2Id(request.getHop2Id() == null ? null : request.getHop2Id().toUpperCase());
                if (request.getHop2Id() != null) {
                    hopIdList.add(request.getHop2Id().toUpperCase());
                }
            }

            // リレー無線ID3
            if (!Objects.equal(request.getHop3Id(), exMeter.getHop3Id())) {
                exMeter.setHop3Id(request.getHop3Id() == null ? null : request.getHop3Id().toUpperCase());
                if (request.getHop3Id() != null) {
                    hopIdList.add(request.getHop3Id().toUpperCase());
                }
            }

            // ポーリンググループNo
            if (!Objects.equal(request.getPollingId(), exMeter.getPollingId())) {
                exMeter.setPollingId(request.getPollingId() == null ? null : request.getPollingId().toUpperCase());
            }

            // 無線ID履歴更新
            if (hopIdList.size() > 0) {
                updateHopidHistory(parameter.getRequest().getDevId(), hopIdList, loginPersonId, loginUserId, serverDateTime);
            }

            // 乗率
            if (!Objects.equal(request.getMulti(), exMeter.getMulti())) {
                exMeter.setMulti(request.getMulti());
            }

            // コメント
            if (!Objects.equal(request.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(request.getMemo());
            }

        } else if (METER_KIND.OCR.getVal().equals(parameter.getMeterKind())) {
            // AieLink用メーター の項目

            // （共通の項目）検満年月(元号)
            if (!Objects.equal(request.getDispYearFlg(), exMeter.getDispYearFlg())) {
                exMeter.setDispYearFlg(request.getDispYearFlg());
            }

            // （共通の項目）検満通知
            if (!Objects.equal(request.getExamNotice(), exMeter.getExamNotice())) {
                exMeter.setExamNotice(request.getExamNotice());
            }

            // （共通の項目）メーター種別
            if (!Objects.equal(request.getMeterType(), exMeter.getMeterType())) {
                exMeter.setMeterType(request.getMeterType());
            }

            // 乗率
            if (!Objects.equal(request.getMulti(), exMeter.getMulti())) {
                exMeter.setMulti(request.getMulti());
            }

            // コメント
            if (!Objects.equal(request.getMemo(), exMeter.getMemo())) {
                exMeter.setMemo(request.getMemo());
            }
        }

        // 機器制御のみ I/F種別
        if (((fromDeviceCtrl == null) || fromDeviceCtrl.booleanValue()) && (request.getIfType() != null)) {
            exMeter.setIfType(request.getIfType());
        }

        // 機器制御のみ 乗率
        if (((fromDeviceCtrl == null) || fromDeviceCtrl.booleanValue()) && (request.getMulti() != null)) {
            exMeter.setMulti(request.getMulti());
        }

        /* 論理削除済のメーターの場合、新規登録から呼ばれた再登録扱いのため、
         * 計器IDと、メーター・通信端末の異常状態を再設定する。
         */
        if (exMeter.getDelFlg().intValue() == 1) {
            exMeter.setMeterId(request.getMeterId());
            exMeter.setMeterSta(BigDecimal.valueOf(0));
            exMeter.setTermSta(BigDecimal.valueOf(0));
        }

        return exMeter;
    }

    /**
     *(スマートメーターのみ)負荷制限の更新後エンティティを返す
     * @param exmml 既存のエンティティ
     * @param request APIパラメーター
     * @param fromDeviceCtrl 機器制御からの呼び出しフラグ
     * @return 負荷制限の更新後エンティティ
     */
    private MMeterLoadlimit updateMeterLoadlimit(MMeterLoadlimit exmml, final UpdateBulkSmsMeterRequestSet request,
            final Boolean fromDeviceCtrl) {
        boolean isChanged = false;

        // 自動投入
        if ((request.getAutoInjection() != null) && !(request.getAutoInjection().equals(exmml.getAutoInjection()))) {
            exmml.setAutoInjection(request.getAutoInjection());
            isChanged = true;
        }

        // 開閉器動作カウント
        if ((request.getBreakerActCount() != null)
                && !(request.getBreakerActCount().equals(exmml.getBreakerActCount()))) {
            exmml.setBreakerActCount(request.getBreakerActCount());
            isChanged = true;
        }

        // 開閉器カウントクリア
        if ((request.getCountClear() != null) && !(request.getCountClear().equals(exmml.getCountClear()))) {
            exmml.setCountClear(request.getCountClear());
            isChanged = true;
        }

        // 負荷電流 1桁の場合は先頭に「0」を付与する
        if ((request.getLoadCurrent() != null) && !(request.getLoadCurrent().equals(exmml.getLoadCurrent()))) {
            exmml.setLoadCurrent(request.getLoadCurrent().length() == 1 ? "0" + request.getLoadCurrent() : request.getLoadCurrent());
            isChanged = true;
        }

        // 負荷制限
        if ((request.getLoadlimitMode() != null) && !(request.getLoadlimitMode().equals(exmml.getLoadlimitMode()))) {
            exmml.setLoadlimitMode(request.getLoadlimitMode());
            isChanged = true;
        }

        // (臨時)自動投入
        if ((request.getTempAutoInjection() != null)
                && !(request.getTempAutoInjection().equals(exmml.getTempAutoInjection()))) {
            exmml.setTempAutoInjection(request.getTempAutoInjection());
            isChanged = true;
        }

        // (臨時)開閉器動作カウント
        if ((request.getTempBreakerActCount() != null)
                && !(request.getTempBreakerActCount().equals(exmml.getTempBreakerActCount()))) {
            exmml.setTempBreakerActCount(request.getTempBreakerActCount());
            isChanged = true;
        }

        // (臨時)開閉器カウントクリア
        if ((request.getTempCountClear() != null) && !(request.getTempCountClear().equals(exmml.getTempCountClear()))) {
            exmml.setTempCountClear(request.getTempCountClear());
            isChanged = true;
        }

        // (臨時)負荷電流 1桁の場合は先頭に「0」を付与する
        if ((request.getTempLoadCurrent() != null)
                && !(request.getTempLoadCurrent().equals(exmml.getTempLoadCurrent()))) {
            exmml.setTempLoadCurrent(request.getTempLoadCurrent().length() == 1 ? "0" + request.getTempLoadCurrent() : request.getTempLoadCurrent());
            isChanged = true;
        }

        // 設定変更の有無に関わらずコマンドフラグと処理フラグを設定するためコメントアウト
//        // 機器制御からの登録の場合は、コマンド・処理フラグをセットしない
//        if (isChanged && ((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue()))) {
//            // コマンドフラグ: 負荷制限設定
//            exmml.setCommandFlg(MMETERLOADLIMIT_COMMAND_FLG.SETTING.getVal());
//
//            // 処理待ちフラグ: 処理待ち
//            exmml.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
//        }
        if (((fromDeviceCtrl == null) || !(fromDeviceCtrl.booleanValue()))) {
            // コマンドフラグ: 負荷制限設定
            exmml.setCommandFlg(MMETERLOADLIMIT_COMMAND_FLG.SETTING.getVal());

            // 処理待ちフラグ: 処理待ち
            exmml.setSrvEnt(MMETER_SRV_ENT.WAIT.getVal());
        }

        return exmml;
    }

    /**
     * テナント変更を確認し、変わっていれば建物メーター中間テーブルを更新する。
     * @param exMeter 現在のメーターレコード
     * @param devId 装置ID
     * @param request APIのリクエスト
     * @param commandFlg コマンドフラグ
     * @param fromDeviceCtrl 機器制御呼び出しフラグ
     * @param loginPersonId ログインパーソンID
     * @param loginUserId ログインユーザーID
     * @param serverDateTime サーバー時刻
     */
    private void tenantChangeCheck(MMeter exMeter, final String devId, final UpdateBulkSmsMeterRequestSet request, final String commandFlg,
            final Boolean fromDeviceCtrl, final String loginPersonId, final Long loginUserId,
            final Timestamp serverDateTime) {
        // テナント変更判定
        final TBuildDevMeterRelation exTenantRelation = getRelationWithTenantForMeter(exMeter);
        Long oldBuildingId = null;
        if (exTenantRelation != null) {
            oldBuildingId = exTenantRelation.getId().getBuildingId();
        }
        // ユーザーコードから建物IDを照会して取得
        final TBuilding newBuilding = getBuildingIdOfTenant(devId, request.getTenantId());

        Long newBuildingId = null;
        // テナントがない場合はnullのまま
        if (newBuilding != null) {
            // 登録済みのテナントの場合はその建物IDを与える
            if (newBuilding.getId() != null) {
                newBuildingId = newBuilding.getId().getBuildingId();
            } else {
                // 未登録のテナントの場合は新たに登録
                TBuilding newTenantBuilding = newBuilding;
                newTenantBuilding = registTenant(request.getTenantId(), exMeter.getId().getDevId(), loginPersonId,
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
            updateMeterWithSendCommand(exMeter, commandFlg, fromDeviceCtrl);

            // 変更前がNULLで変更後が非NULLなら建物メーター中間テーブルにテナントとの関係を登録
            TBuildDevMeterRelation tRelation = new TBuildDevMeterRelation();
            TBuildDevMeterRelationPK tRelationPK = new TBuildDevMeterRelationPK();

            tRelationPK.setBuildingId(newBuildingId);
            tRelationPK.setDevId(devId);
            tRelationPK.setMeterMngId(request.getMeterMngId());
            tRelation.setId(tRelationPK);
            tRelation.setVersion(0);
            tRelation.setCreateDate(serverDateTime);
            tRelation.setCreateUserId(loginUserId);
            tRelation.setUpdateDate(serverDateTime);
            tRelation.setUpdateUserId(loginUserId);

            persist(tBuildDevMeterRelationServiceDaoImpl, tRelation);

        } else if ((oldBuildingId != null) && (newBuildingId == null)) {

            // コマンドフラグ・処理フラグ設定
            updateMeterWithSendCommand(exMeter, commandFlg, fromDeviceCtrl);

            // 変更前が非NULLで変更後がNULLなら建物メーター中間テーブルからテナントとの関係を物理削除

            remove(tBuildDevMeterRelationServiceDaoImpl, exTenantRelation);

        } else if (!oldBuildingId.equals(newBuildingId)) {

            // コマンドフラグ・処理フラグ設定
            updateMeterWithSendCommand(exMeter, commandFlg, fromDeviceCtrl);

            // 変更前後ともNULLでなく内容が変わっていれば建物メーター中間テーブルに今あるテナントとの関係を削除
            remove(tBuildDevMeterRelationServiceDaoImpl, exTenantRelation);

            // その後新しいテナトンとの関係を建物メーター中間テーブルに登録
            TBuildDevMeterRelation tRelation = new TBuildDevMeterRelation();
            TBuildDevMeterRelationPK tRelationPK = new TBuildDevMeterRelationPK();

            tRelationPK.setBuildingId(newBuildingId);
            tRelationPK.setDevId(devId);
            tRelationPK.setMeterMngId(request.getMeterMngId());
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

}
