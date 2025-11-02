package jp.co.osaki.osol.api.dao.sms.meterreading;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import com.google.common.base.Objects;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.DeleteSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.request.sms.meterreading.DeleteSmsInspectionMeterBefRequestSet;
import jp.co.osaki.osol.api.result.sms.meterreading.DeleteSmsInspectionMeterBefResult;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterBefServiceDaoImpl;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK;

/**
 * 確定前検針データ 削除 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class DeleteSmsInspectionMeterBefDao extends OsolApiDao<DeleteSmsInspectionMeterBefParameter> {

    /** セッション. */
    @Resource
    private SessionContext sessionContext;

    // 確定前検針データ一覧 取得
    private final InspectionMeterBefServiceDaoImpl inspectionMeterBefServiceDaoImpl;

    public DeleteSmsInspectionMeterBefDao() {
        inspectionMeterBefServiceDaoImpl = new InspectionMeterBefServiceDaoImpl();
    }

    @Override
    public DeleteSmsInspectionMeterBefResult query(DeleteSmsInspectionMeterBefParameter parameter) throws Exception {

        // --- 確定前検針データ 削除 ---

        for (DeleteSmsInspectionMeterBefRequestSet deleteBean : parameter.getDeletes().getList()) {

            // 種別設定 検索条件設定
            TInspectionMeterBefPK targetPk = new TInspectionMeterBefPK();
            targetPk.setDevId(deleteBean.getDevId());                   // 装置ID
            targetPk.setMeterMngId(deleteBean.getMeterMngId());         // メーター管理番号
            targetPk.setLatestInspDate(deleteBean.getLatestInspDate()); // データ取得日時
            TInspectionMeterBef target = new TInspectionMeterBef();
            target.setId(targetPk);

            // 排他チェック
            TInspectionMeterBef entity = find(inspectionMeterBefServiceDaoImpl, target);
            if (entity == null) {
                // 更新対象なし(更新前に更新対象のレコードが削除されている) → エラー
                // ロールバック
                sessionContext.setRollbackOnly();
                // 排他エラー
                throw new OptimisticLockException();
            } else if (!Objects.equal(entity.getVersion(), deleteBean.getVersion())) {
                // 楽観ロック エラー
                // ロールバック
                sessionContext.setRollbackOnly();
                // 排他エラー
                throw new OptimisticLockException();
            }

            // 削除
            remove(inspectionMeterBefServiceDaoImpl, entity);
        }

        return new DeleteSmsInspectionMeterBefResult();
    }

}
