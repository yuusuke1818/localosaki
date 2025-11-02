package jp.skygroup.enl.webap.base.batch;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * バッチ処理用 DBアクセス 基底クラス
 *
 * @author take_suzuki
 */
public abstract class BaseBatchDao extends BaseBatch {

    /**
     * EntityManager
     */
    protected EntityManager entityManager;

    /**
     * デフォルトコンストラクタ
     */
    @SuppressWarnings("unused")
    private BaseBatchDao() {}

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public BaseBatchDao(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    /**
     *
     * DBサーバ時刻取得メソッド
     *
     * @return DBサーバ時刻
     */
    protected Timestamp getServerDateTime() {

        Timestamp nowTimestamp = new Timestamp((new Date()).getTime());
        try {
            if (this.entityManager != null && this.entityManager.isOpen()) {
                //セッション取得
                Session session = ((Session) this.entityManager.getDelegate());
                // 実行
                List<?> subTypeIdList = session.createSQLQuery("SELECT now() _now")
                        .addScalar("_now").list();
                if (!subTypeIdList.isEmpty()) {
                    nowTimestamp = new Timestamp(((Date) subTypeIdList.get(0)).getTime());
                }
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            throw ex;
        }
        return nowTimestamp;
    }

    /**
     * ID採番メソッド.
     *
     * @param SequenceObjectName シーケンスオブジェクト名
     * @return 採番されたID
     */
    protected Long createId(String sequenceObjectName) {

        BigInteger id = BigInteger.ZERO;
        try {
            //セッション取得
            Session session = ((Session) entityManager.getDelegate());
            // 実行
            List<?> subTypeIdList = session.createSQLQuery(
                    "SELECT nextval('"
                            .concat(sequenceObjectName)
                            .concat("') nextval_id"))
                    .addScalar("nextval_id")
                    .list();
            if (!subTypeIdList.isEmpty()) {
                id = (BigInteger) subTypeIdList.get(0);
            }
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            throw ex;
        }
        return id.longValue();
    }
}
