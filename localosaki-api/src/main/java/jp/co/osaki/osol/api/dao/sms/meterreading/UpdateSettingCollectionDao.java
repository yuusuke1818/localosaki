package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSettingCollectionParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSettingCollectionResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.CommandResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.CommandServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.DevRelationServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.sms.SmsConstants;

/**
 * 設定一括収集 Daoクラス
 * 参照コード
 *  SettingAllCollect.js
 *  MeterListCheck.php  メーター一覧要求-正常終了チェック:getMeterCheck
 *  ConListDem.php      コンセントレーター一覧要求           存在チェック:getCountRegConListDem    登録:insertRegConListDem    更新:updateRegConListDem
 *  MeterListDem.php    メーター一覧要求                     存在チェック:getCountRegMeterListDem  登録:iinsertRegMeterListDem 更新:updateRegMeterListDem
 *  MeterInfoDem.php    メーター情報個別要求                 存在チェック:getCountRegMeterInfoDem  登録:iinsertRegMeterInfoDem 更新:updateRegMeterInfoDem
 *  MeterLoadDem.php    メーター負荷制限状態確認             存在チェック:getCountRegMeterLoadDem  登録:iinsertRegMeterLoadDem 更新:updateRegMeterLoadDem
 *  AutoInspDem.php     自動検針月日時要求                 存在チェック:getCountRegAutoInspDem   登録:iinsertRegAutoInspDem  更新:updateRegAutoInspDem
 *  PulseMeterDem.php   スマートパルス入力端末設定内容要求 存在チェック:getCountRegPulseMeterInfoDemCheck  登録:iinsertRegPulseMeterInfoDem  更新:updateRegPulseMeterInfoDem
 *  RepeaterListDem.php 中継装置（無線）一覧要求           存在チェック:getCountRegDemRepeaterListCheck  登録:iinsertRegRepeaterListDem  更新:updateRegRepeaterListDem
 * @author kobayashi.sho
 */
@Stateless
public class UpdateSettingCollectionDao extends OsolApiDao<UpdateSettingCollectionParameter> {

    // 処理フラグ値
    private static final String SRV_ENT_REQUESTING = "1"; // 1：処理待ち
    private static final String SRV_ENT_ALL_REQUESTING = "5"; // 5:一括処理待ち

    // 建物、装置関連テーブル ServiceDao
    private final DevRelationServiceDaoImpl devRelationServiceDaoImpl;

    // コマンド送信 ServiceDao
    private final CommandServiceDaoImpl commandServiceDaoImpl;

    public UpdateSettingCollectionDao() {
        devRelationServiceDaoImpl = new DevRelationServiceDaoImpl();
        commandServiceDaoImpl = new CommandServiceDaoImpl();
    }

    @Override
    public UpdateSettingCollectionResult query(UpdateSettingCollectionParameter parameter) throws Exception {
        // 装置IDを取得
        List<String> devIdList = getDevId(parameter.getCorpId(), parameter.getBuildingId());
        if (devIdList == null) {
            return null; // データエラー終了
        }

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        // ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        // 変数初期化
        List<String> warningCommandList = new ArrayList<String>(); // 警告ありコマンド一覧 初期化
        List<String> meterDevIdList = new ArrayList<String>(); // メーター一覧要求正常リスト＜装置ID＞ ※DEV_ID 毎に メーター一覧要求 をチェックして 処理フラグ(SRV_ENT) が 正常(値が空) になっている 装置IDリスト

        // (処理対象の)通信コマンドに「メーター一覧要求」が含まれているか？
        if (parameter.getCommands().getList().contains(CommandServiceDaoImpl.COMMMAND_METER_LIST)) {
            // 「メーター一覧要求」を先に処理する
            requestCollectiveSettings(CommandServiceDaoImpl.COMMMAND_METER_LIST, devIdList, parameter.getLoginPersonId(), loginUserId, serverDateTime, meterDevIdList);
        }

        // メーター一覧要求 の メーター一覧状態.処理フラグ を確認
        // ※参考処理: MeterListCheck.php
        for (String devId : devIdList) {
            TCommand entity = getTCommand(devId, CommandServiceDaoImpl.COMMMAND_METER_LIST);
            if (entity == null || entity.getSrvEnt() == null || "".equals(entity.getSrvEnt())) {
                // メーター一覧要求行が無い or 処理フラグが"正常終了" → 正常リストに追加
                meterDevIdList.add(devId);
            }
        }

        for (String command : parameter.getCommands().getList()) {
            if (CommandServiceDaoImpl.COMMMAND_METER_LIST.equals(command)) {
                continue; // 「メーター一覧要求」は先処理して処理済みのため、ここでは処理不要。
            }

            // 設定一括収集(処理フラグ を "要求中" に 登録・更新)
            boolean isSuccess = requestCollectiveSettings(command, devIdList, parameter.getLoginPersonId(), loginUserId, serverDateTime, meterDevIdList);

            if (!isSuccess) {
                // メーター一覧要求が正常終了しないと処理できないコマンド(「メーター情報個別要求、メーター負荷制限状態確認、スマートパルス入力端末設定内容要求」)が含まれていてスキップされた処理がある
                warningCommandList.add(command);
            }
        }

        // (登録後)再検索
        List<CommandResultData> resultList = getSettingCheck(devIdList);

        return new UpdateSettingCollectionResult(warningCommandList, devIdList, resultList);
    }

    /**
     * 設定一括収集(処理フラグ を "要求中" に 登録・更新).
     * 収集コマンド名「メーター情報個別要求、メーター負荷制限状態確認、スマートパルス入力端末設定内容要求」の場合は、メーター一覧要求 が正常ではない場合は処理をスキップする(返値にfalseを返す)
     * @param command 通信コマンド
     * @param devIdList (処理対象)装置IDリスト
     * @param recMan ユーザー
     * @param loginUserId ユーザーID
     * @param serverDateTime システム日時
     * @param meterDevIdList メーター一覧要求正常の 装置IDリスト  ※
     * @return 処理結果  true:全て正常終了  false:収集コマンド名「メーター情報個別要求、メーター負荷制限状態確認、スマートパルス入力端末設定内容要求」で、メーター一覧要求 が 正常になっていないため スキップ された処理がある
     */
    private boolean requestCollectiveSettings(String command, List<String> devIdList, String recMan, Long loginUserId, Timestamp serverDateTime, List<String> meterDevIdList) {

        boolean isSuccess = true; // 正常フラグ  false:メーター一覧要求正常終了しないと処理できない項目をスキップした

        for (String devId : devIdList) {

            // 処理対象外チェック → 対象外の場合は次のデータを処理する
            if (isExclude(devId, command)) {
                continue;
            }

            // メーター関連項目か？ 且つ メーター一覧要求正常終了していないか？
            if (CommandServiceDaoImpl.COMMAND_TAG_IS_NULL_LIST.contains(command) && !meterDevIdList.contains(devId)) {
                // メーター一覧要求正常終了しないと処理できない項目のためスキップする
                isSuccess = false; // false:メーター一覧要求正常終了しないと処理できない項目をスキップした
                continue;
            }

            TCommand target = new TCommand();
            target.setId(new TCommandPK());
            target.getId().setDevId(devId);             // 装置ID
            target.getId().setCommand(command);         // 通信コマンド
            target.getId().setRecDate(serverDateTime);  // REC_DATE
            target.setSrvEnt(initSrvEnt(command));      // 処理フラグ
            target.setRecMan(recMan);                   // REC_MAN
            target.setTag(null);                        // TAG
            target.setUpdateUserId(loginUserId);        // UPDATE_USER_ID
            target.setUpdateDate(serverDateTime);       // UPDATE_DATE

            // データ有無チェック
            TCommand entity = getTCommand(devId, command);
            if (entity != null) {
                // 更新 ※更新項目に 主キー が含まれるため 削除・再登録 を行う

                // 削除
                deleteTCommand(entity);

                // 再登録
                target.setLinking(entity.getLinking());         // 一括設定用紐付フィールド
                target.setRetryCount(entity.getRetryCount());   // 再要求カウント
                target.setVersion(entity.getVersion() + 1);     // 排他制御用カラム
                target.setCreateUserId(entity.getCreateUserId()); // CREATE_USER_ID
                target.setCreateDate(entity.getCreateDate());   // CREATE_DATE
            } else {
                // 新規登録
                target.setLinking(null);                // 一括設定用紐付フィールド
                target.setRetryCount(null);             // 再要求カウント
                target.setVersion(0);                   // 排他制御用カラム
                target.setCreateUserId(loginUserId);    // CREATE_USER_ID
                target.setCreateDate(serverDateTime);   // CREATE_DATE
            }

            // 登録
            insertTCommand(target);
        }

        return isSuccess;
    }

    /**
     * 処理対象外チェック.
     * コンセントレーター の場合、対象外になる通信コマンドかどうかをチェックする.
     * @param devId 装置ID
     * @param command 通信コマンド
     * @return コンセントレーターフラグ  true:コンセントレーター
     */
    private boolean isExclude(String devId, String command) {
        // コンセントレーターか 且つ 対象外になる通信コマンドか
        if ((devId.startsWith(SmsConstants.DEVICE_KIND.CONCENTRATER_XR.getVal())  // コンセントレーター("XR") か？
          || devId.startsWith(SmsConstants.DEVICE_KIND.CONCENTRATER_XS.getVal())) // コンセントレーター("XS") か？
         && (CommandServiceDaoImpl.COMMMAND_CONCENT_LIST.equals(command)     // 対象外になる通信コマンド("concent-list")か？
          || CommandServiceDaoImpl.COMMMAND_GET_AUTOINSP.equals(command))) { // 対象外になる通信コマンド("get-autoinsp")か？
            return true;
        }
        return false;
    }

    /**
     * 処理フラグ(SRV_ENT)の初期値取得
     * @param command 通信コマンド
     * @return "1"：処理待ち または "5":一括処理待ち を返す
     */
    private static String initSrvEnt(String command) {
        return CommandServiceDaoImpl.COMMAND_TAG_IS_NULL_LIST.contains(command) ? SRV_ENT_ALL_REQUESTING : SRV_ENT_REQUESTING;
    }

    /**
     * 接続先の装置IDを取得
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 装置ID
     */
    private List<String> getDevId(String corpId, Long buildingId) {

        MDevRelation target = new MDevRelation();
        target.setId(new MDevRelationPK());
        target.getId().setCorpId(corpId);
        target.getId().setBuildingId(buildingId);

        List<MDevRelation> entity = getResultList(devRelationServiceDaoImpl, target);

        if (entity == null || entity.isEmpty()) {
            return null;
        }

        return entity.stream().map(row -> row.getId().getDevId()).collect(Collectors.toList());
    }

    /**
     * コマンド送信(T_COMMAND) の最新の1件を取得
     * @param devId 装置ID
     * @param command 収集コマンド
     * @return 取得データ
     */
    private TCommand getTCommand(String devId, String command) {
        TCommand target = new TCommand();
        target.setId(new TCommandPK());
        target.getId().setDevId(devId);
        target.getId().setCommand(command);

        return find(commandServiceDaoImpl, target);
    }

    /**
     * 一括収集の状態取得.
     * @param devIdList 装置ID
     * @return 状態確認
     */
    private List<CommandResultData> getSettingCheck(List<String> devIdList) {
        // 検索条件セット
        Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
        targetMap.put(CommandServiceDaoImpl.DEV_ID,
                devIdList.stream()
                        .map(devId -> (Object) devId)
                        .collect(Collectors.toList()));

        // 検索
        List<TCommand> entityList = getResultList(commandServiceDaoImpl, targetMap);
        if (entityList == null || entityList.isEmpty()) {
            return null; // 該当データなし
        }

        return entityList.stream().map(row -> new CommandResultData(row)).collect(Collectors.toList());
    }

    /**
     * 削除
     * @param target 削除データ
     */
    private void deleteTCommand(TCommand target) {
        remove(commandServiceDaoImpl, target);
    }

    /**
     * 新規登録
     * @param target 登録データ
     */
    private void insertTCommand(TCommand target) {
        persist(commandServiceDaoImpl, target);
    }
}
