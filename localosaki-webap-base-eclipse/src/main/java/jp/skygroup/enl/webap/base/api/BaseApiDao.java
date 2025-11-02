package jp.skygroup.enl.webap.base.api;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * BaseApiDaoクラス.
 *
 * API用のBaseServiceDaoインターフェース実装を呼び出すメソッドを保有するクラス
 *
 * @author take_suzuki
 */
public abstract class BaseApiDao {

    /**
     * Daoログ.
     */
    protected static final Logger daoLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.DAO.getVal());

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private SessionContext sessionContext;

    /**
     *
     * DBサーバ時刻取得メソッド.
     *
     * @return DBサーバ時刻
     */
    public Timestamp getServerDateTime() {

        Date now_date = new Timestamp((new Date()).getTime());
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getServerDateTime Start"));
            //セッション取得
            StatelessSession statelessSession = ((Session) entityManager.getDelegate()).getSessionFactory()
                    .openStatelessSession();
            // 実行
            @SuppressWarnings("unchecked")
            List<Date> subTypeIdList = (List<Date>) statelessSession.createSQLQuery("SELECT now() _now")
                    .addScalar("_now").list();
            if (!subTypeIdList.isEmpty()) {
                now_date = subTypeIdList.get(0);
            }
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getServerDateTime End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return (Timestamp) now_date;
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
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".createId Start"));
            //セッション取得
            StatelessSession statelessSession = ((Session) entityManager.getDelegate()).getSessionFactory()
                    .openStatelessSession();
            // 実行
            @SuppressWarnings("unchecked")
            List<BigInteger> subTypeIdList = (List<BigInteger>) statelessSession
                    .createSQLQuery("SELECT nextval('".concat(sequenceObjectName).concat("') nextval_id"))
                    .addScalar("nextval_id").list();
            if (!subTypeIdList.isEmpty()) {
                id = subTypeIdList.get(0);
            }
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".createId End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return id.longValue();
    }

    /**
     * 複数件数取得DBアクセス処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 検索対象のEntity
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, T target) {

        List<T> resultList = null;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(target, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return resultList;
    }

    /**
     * 複数件数取得DBアクセス処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param entityList 検索値のエンティティリスト
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, List<T> entityList) {

        List<T> resultList = null;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(entityList, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return resultList;
    }

    /**
     * 複数件数取得DBアクセス処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param parameterMap 検索値のMap
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, Map<String, List<Object>> parameterMap) {

        List<T> resultList = null;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(parameterMap, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return resultList;
    }

    /**
     * 複数レコード更新削除実行処理.
     *
     * @param impl 実行対象のインターフェース
     * @param parameterMap バインドするパラメータ
     * @return 更新対象となった件数
     */
    protected <T> int executeUpdate(BaseServiceDao<T> impl, Map<String, List<Object>> parameterMap) {

        int result = 0;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".executeUpdate Start"));
            // 実行
            result = impl.executeUpdate(parameterMap, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".executeUpdate End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return result;
    }

    /**
     * 1件のみレコード取得するDBアクセス処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 検索対象のEntity
     * @return 検索結果のEntity
     */
    protected <T> T find(BaseServiceDao<T> impl, T target) {

        T resultobject = null;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".find Start"));
            // 実行
            resultobject = impl.find(target, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".find End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
        return resultobject;
    }

    /**
     * 新規登録処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 登録対象のEntity
     */
    protected <T> void persist(BaseServiceDao<T> impl, T target) {

        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".persist End"));
            // 実行
            impl.persist(target, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".persist End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
    }

    /**
     * 更新処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 更新対象のEntity
     * @return 更新後のEntity
     */
    protected <T> T merge(BaseServiceDao<T> impl, T target) {

        T mergeobject;
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".merge End"));
            // 実行
            mergeobject = impl.merge(target, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".merge End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            mergeobject = null;
            throw ex;
        }
        return mergeobject;
    }

    /**
     * 削除処理.
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 削除対象のEntity
     */
    protected <T> void remove(BaseServiceDao<T> impl, T target) {

        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".remove End"));
            // 実行
            impl.remove(target, entityManager);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".remove End"));
        } catch (Exception ex) {
            sessionContext.setRollbackOnly();
            throw ex;
        }
    }

}
