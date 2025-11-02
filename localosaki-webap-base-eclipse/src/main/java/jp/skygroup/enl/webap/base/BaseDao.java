package jp.skygroup.enl.webap.base;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.jboss.logging.Logger;

/**
 *
 * BaseDaoクラス
 *
 * BaseServiceDaoインターフェース実装を呼び出すメソッドを保有するクラス
 *
 * @author take_suzuki
 */
@SuppressWarnings("unchecked")
@Interceptors(BaseDaoInterceptor.class)
public abstract class BaseDao extends BaseSession {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;

    /**
     * エラーログ
     */
    protected static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    private Exception exception;

    private boolean exceptionFlg;

//<editor-fold defaultstate="collapsed" desc="getter, setter">
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isExceptionFlg() {
        return exceptionFlg;
    }

    public void setExceptionFlg(boolean exceptionFlg) {
        this.exceptionFlg = exceptionFlg;
    }
    //</editor-fold>

    /**
     * Daoログ
     */
    private static final Logger daoLogger = Logger.getLogger(LOGGER_NAME.DAO.getVal());

    /**
     *
     * DBサーバ時刻取得メソッド
     *
     * @return DBサーバ時刻
     */
    public Timestamp getSvDate() {
        if (isExceptionFlg()) {
            return new Timestamp((new Date()).getTime());
        }

        Date now_date = new Timestamp((new Date()).getTime());
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getSvDate Start"));
            //セッション取得
            StatelessSession statelessSession = ((Session) em.getDelegate()).getSessionFactory().openStatelessSession();
            // 実行
            List<Date> subTypeIdList = (List<Date>) statelessSession.createSQLQuery("SELECT now() _now").addScalar("_now").list();
            if (!subTypeIdList.isEmpty()) {
                now_date = subTypeIdList.get(0);
            }
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getSvDate End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return (Timestamp) now_date;
    }

    /**
     *
     * ID採番メソッド
     *
     * @param SequenceObjectName シーケンスオブジェクト名
     * @return 採番されたID
     */
    protected Long createId(String SequenceObjectName) {
        if (isExceptionFlg()) {
            return BigInteger.ZERO.longValue();
        }

        BigInteger id;
        id = BigInteger.ZERO;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".createId Start"));
            //セッション取得
            StatelessSession statelessSession = ((Session) em.getDelegate()).getSessionFactory().openStatelessSession();
            // 実行
            List<BigInteger> subTypeIdList = (List<BigInteger>) statelessSession.createSQLQuery("SELECT nextval('".concat(SequenceObjectName).concat("') nextval_id")).addScalar("nextval_id").list();
            if (!subTypeIdList.isEmpty()) {
                id = subTypeIdList.get(0);
            }
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".createId End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return id.longValue();
    }

    /**
     *
     * 複数レコード更新削除実行処理
     *
     * @param impl 実行対象のインターフェース
     * @param parameterMap バインドするパラメータ
     * @return 更新対象となった件数
     */
    protected <T> int executeUpdate(BaseServiceDao<T> impl, Map<String, List<Object>> parameterMap) {
        if (isExceptionFlg()) {
            return 0;
        }

        int result = 0;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".executeUpdate Start"));
            // 実行
            result = impl.executeUpdate(parameterMap, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".executeUpdate End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return result;
    }

    /**
     * 複数件数取得DBアクセス処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 検索対象のEntity
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, T target) {
        if (isExceptionFlg()) {
            return null;
        }

        List<T> resultList = null;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(target, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return resultList;
    }

    /**
     * 複数件数取得DBアクセス処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param parameterMap 検索値のMap
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, Map<String, List<Object>> parameterMap) {
        if (isExceptionFlg()) {
            return null;
        }

        List<T> resultList = null;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(parameterMap, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return resultList;
    }

    /**
     * 複数件数取得DBアクセス処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param entityList 検索値のエンティティリスト
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl, List<T> entityList) {
        if (isExceptionFlg()) {
            return null;
        }

        List<T> resultList = null;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(entityList, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return resultList;
    }

    /**
     * 全件取得DBアクセス処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @return 複数件数の検索結果
     */
    protected <T> List<T> getResultList(BaseServiceDao<T> impl) {
        if (isExceptionFlg()) {
            return null;
        }

        List<T> resultList = null;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList Start"));
            // 実行
            resultList = impl.getResultList(em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".getResultList End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return resultList;
    }

    /**
     * 1件のみレコード取得するDBアクセス処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 検索対象のEntity
     * @return 検索結果のEntity
     */
    protected <T> T find(BaseServiceDao<T> impl, T target) {
        if (isExceptionFlg()) {
            return null;
        }

        T resultobject = null;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".find Start"));
            // 実行
            resultobject = impl.find(target, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".find End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return resultobject;
    }

    /**
     * 新規登録処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 登録対象のEntity
     */
    protected <T> void persist(BaseServiceDao<T> impl, T target) {
        if (isExceptionFlg()) {
            return;
        }

        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".persist End"));
            // 実行
            impl.persist(target, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".persist End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
    }

    /**
     * 更新処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 更新対象のEntity
     * @return 更新後のEntity
     */
    protected <T> T merge(BaseServiceDao<T> impl, T target) {
        if (isExceptionFlg()) {
            return null;
        }

        T mergeobject;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".merge End"));
            // 実行
            mergeobject = impl.merge(target, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".merge End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
            mergeobject = null;
        }
        return mergeobject;
    }

    /**
     * 削除処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 削除対象のEntity
     */
    protected <T> void remove(BaseServiceDao<T> impl, T target) {
        if (isExceptionFlg()) {
            return;
        }

        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".remove End"));
            // 実行
            impl.remove(target, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".remove End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
    }

    /**
     *
     * 複数レコード更新削除実行処理
     *
     * @param impl 実行対象のインターフェース
     * @param parameterMap バインドするパラメータ
     * @return 更新対象となった件数
     */
    protected boolean dbExecute(BaseDbExecute impl, Map<String, List<Object>> parameterMap) {
        if (isExceptionFlg()) {
            return false;
        }

        boolean result = false;
        setExceptionFlg(false);
        try {
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".dbExecute Start"));
            // 実行
            result = impl.dbExecute(parameterMap, em);
            //ログ出力
            daoLogger.trace(this.getClass().getName().concat(".dbExecute End"));
        } catch (Exception ex) {
            ctx.setRollbackOnly();
            setExceptionFlg(true);
            exception = ex;
        }
        return result;
    }

    /**
     * 強制的にRollbackを行う
     */
    public void setRollBack() {
        ctx.setRollbackOnly();
    }
}
