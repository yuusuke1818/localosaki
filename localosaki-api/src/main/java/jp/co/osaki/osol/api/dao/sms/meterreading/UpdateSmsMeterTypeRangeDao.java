package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import com.google.common.base.Objects;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter.Ranges.RangeBean;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsMeterTypeRangeResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterRangeResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterTypeResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterRangeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;

/**
 * メータ種別設定情報 + メータ種別従量値情報 登録・更新・削除 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class UpdateSmsMeterTypeRangeDao extends OsolApiDao<UpdateSmsMeterTypeRangeParameter> {

    /** セッション. */
    @Resource
    private SessionContext sessionContext;

    // 種別設定情報 取得・登録 処理
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メータ種別従量値情報 取得・登録 処理
    private final MeterRangeServiceDaoImpl meterRangeServiceDaoImpl;

    public UpdateSmsMeterTypeRangeDao() {
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterRangeServiceDaoImpl = new MeterRangeServiceDaoImpl();
    }

    @Override
    public UpdateSmsMeterTypeRangeResult query(UpdateSmsMeterTypeRangeParameter parameter) throws Exception {
        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        // --- メーター種別設定 更新 ---

        // 種別設定 検索条件設定
        MMeterTypePK targetPk = new MMeterTypePK();
        targetPk.setMeterType(parameter.getMeterType());      // メーター種別(Key)
        targetPk.setCorpId(parameter.getCorpId());            // 企業ID(Key)
        targetPk.setBuildingId(parameter.getBuildingId());    // 建物ID(Key)
        targetPk.setMenuNo(parameter.getMenuNo());            // メニュー番号(Key)
        MMeterType target = new MMeterType();
        target.setId(targetPk);

        // 排他チェック
        MMeterType entity = find(meterTypeServiceDaoImpl, target);
        if (entity == null) {
            // 更新対象なし(更新前に更新対象のレコードが削除されている) → エラー
            // ロールバック
            sessionContext.setRollbackOnly();
            // 排他エラー
            throw new OptimisticLockException();
        } else if (!Objects.equal(entity.getVersion(), parameter.getVersion())) {
            // 楽観ロック エラー
            // ロールバック
            sessionContext.setRollbackOnly();
            // 排他エラー
            throw new OptimisticLockException();
        }

        // 更新データセット
        entity.setRecDate(serverDateTime);                                  // REC_DATE
        entity.setRecMan(parameter.getLoginPersonId());                     // REC_MAN
        entity.setMeterTypeName(parameter.getMeterTypeName());              // メーター種別名称
        entity.setUnitUsageBased(parameter.getUnitUsageBased());            // 従量単位
        entity.setCo2Coefficient(parameter.getCo2Coefficient());            // CO2排出係数
        entity.setUnitCo2Coefficient(parameter.getUnitCo2Coefficient());    // CO2排出係数単位
        entity.setVersion(parameter.getVersion());                          // 排他制御用カラム
        entity.setUpdateUserId(loginUserId);                                // 更新ユーザー識別ID
        entity.setUpdateDate(serverDateTime);                               // 更新日時

        // 更新 ※企業ID, 建物ID, メータ種別 を条件に更新 (注：更新条件に メニュー番号 は含めない)
        entity = merge(meterTypeServiceDaoImpl, entity);


        // --- メータ種別従量値情報 登録・更新・削除 ---

        List<MeterRangeResultData> meterRangeResultDataList = new ArrayList<MeterRangeResultData>();
        if (parameter.getRanges() != null) {
            for (RangeBean range : parameter.getRanges().getRangeList()) {

                // 種別従量値をセット（排他チェックと、変更がない場合に登録した）
                MMeterRangePK targetPkMeterRange = new MMeterRangePK();
                targetPkMeterRange.setRangeValue(range.getRangeValue());      // 従量値(編集前)
                targetPkMeterRange.setMeterType(parameter.getMeterType());    // メーター種別
                targetPkMeterRange.setCorpId(parameter.getCorpId());          // 企業ID
                targetPkMeterRange.setBuildingId(parameter.getBuildingId());  // 建物ID
                targetPkMeterRange.setMenuNo(parameter.getMenuNo());          // メニュー番号

                MMeterRange targetMeterRange = new MMeterRange();
                targetMeterRange.setId(targetPkMeterRange);
                targetMeterRange.setVersion(range.getVersion()); // 排他制御用カラム

                // 編集がないデータは更新対象外
                if (Objects.equal(range.getRangeValue(), range.getRangeValueEdit())) {
                    // 編集がない → 次のデータ
                    meterRangeResultDataList.add(new MeterRangeResultData(targetMeterRange));
                    continue;
                }

                // 更新または削除 か？
                MMeterRange entityMeterRange = null;
                if (range.getRangeValue() != null) {
                    // 更新または削除 ※更新：更新項目が主キーのため、更新対象のデータも一度削除する

                    // 排他チェック
                    entityMeterRange = find(meterRangeServiceDaoImpl, targetMeterRange);
                    if (entityMeterRange == null || !Objects.equal(entityMeterRange.getVersion(), range.getVersion())) {
                        // ロールバック
                        sessionContext.setRollbackOnly();
                        //排他エラー
                        throw new OptimisticLockException();
                    }
                    // 削除
                    remove(meterRangeServiceDaoImpl, entityMeterRange);
                }

                // 更新または新規登録か？
                if (range.getRangeValueEdit() != null) {
                    // 登録データをセット
                    targetPkMeterRange = new MMeterRangePK();
                    targetPkMeterRange.setRangeValue(range.getRangeValueEdit());  // 従量値(編集後)
                    targetPkMeterRange.setMeterType(parameter.getMeterType());    // メーター種別
                    targetPkMeterRange.setCorpId(parameter.getCorpId());          // 企業ID
                    targetPkMeterRange.setBuildingId(parameter.getBuildingId());  // 建物ID
                    targetPkMeterRange.setMenuNo(parameter.getMenuNo());          // メニュー番号

                    targetMeterRange = new MMeterRange(); // ※排他制御用カラム はセットしない(登録時に排他制御用カラムに値があるとエラーになる)
                    targetMeterRange.setId(targetPkMeterRange);
                    targetMeterRange.setRecDate(serverDateTime);                // REC_DATE
                    targetMeterRange.setRecMan(parameter.getLoginPersonId());   // REC_MAN
                    if (entityMeterRange == null) {
                        // 新規登録
                        targetMeterRange.setVersion(range.getVersion());        // 排他制御用カラム
                        targetMeterRange.setCreateUserId(loginUserId);          // 作成ユーザー識別ID
                        targetMeterRange.setCreateDate(serverDateTime);         // 作成日時
                    } else {
                        // 更新
                        targetMeterRange.setVersion(range.getVersion() + 1);    // 排他制御用カラム  ※更新時は+1する
                        targetMeterRange.setCreateUserId(entityMeterRange.getCreateUserId()); // 作成ユーザー識別ID
                        targetMeterRange.setCreateDate(entityMeterRange.getCreateDate());     // 作成日時
                    }
                    targetMeterRange.setUpdateUserId(loginUserId);              // 更新ユーザー識別ID
                    targetMeterRange.setUpdateDate(serverDateTime);             // 更新日時

                    // 新規登録
                    persist(meterRangeServiceDaoImpl, targetMeterRange);

                    meterRangeResultDataList.add(new MeterRangeResultData(targetMeterRange));
                }
            }
        }

        return new UpdateSmsMeterTypeRangeResult(new MeterTypeResultData(entity), meterRangeResultDataList);
    }

}
