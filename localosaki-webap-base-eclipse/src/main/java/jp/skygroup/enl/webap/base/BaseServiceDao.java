package jp.skygroup.enl.webap.base;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 *
 * BaseServiceDaoインターフェース
 *
 * このクラスの実装にてDB更新処理を実行する。
 *
 * @author take_suzuki
 * @param <T> 対象となるエンティティクラスの型
 */
public interface BaseServiceDao<T> {

    /**
     *
     * @param parameterMap 検索条件
     * @param em エンティティマネージャ
     * @return 処理結果のObject
     */
    public abstract int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em);

    /**
     *
     * @param target 対象となるエンティティ
     * @param em エンティティマネージャ
     * @return 検索結果エンティティリスト
     */
    public abstract List<T> getResultList(T target, EntityManager em);

    /**
     *
     * @param parameterMap 検索用パラメータMap
     * @param em エンティティマネージャ
     * @return 検索結果エンティティリスト
     */
    public abstract List<T> getResultList(Map<String, List<Object>> parameterMap, EntityManager em);

    /**
     *
     * @param entityList 検索用エンティティリスト
     * @param em エンティティマネージャ
     * @return 検索結果エンティティリスト
     */
    public abstract List<T> getResultList(List<T> entityList, EntityManager em);

    /**
     * 全件数　検索用
     *
     * @param em エンティティマネージャ
     * @return 検索結果エンティティリスト
     */
    public abstract List<T> getResultList(EntityManager em);

    /**
     *
     * @param target 対象となるエンティティ
     * @param em エンティティマネージャ
     * @return 検索結果のエンティティ
     */
    public abstract T find(T target, EntityManager em);

    /**
     *
     * @param target 対象となるエンティティ
     * @param em エンティティマネージャ
     */
    public abstract void persist(T target, EntityManager em);

    /**
     *
     * @param <T> 対象となるエンティティの型
     * @param target 対象となるエンティティ
     * @param em エンティティマネージャ
     * @return 更新されたエンティティ
     */
    public abstract T merge(T target, EntityManager em);

    /**
     *
     * @param target 対象となるエンティティ
     * @param em エンティティマネージャ
     */
    public abstract void remove(T target, EntityManager em);

}
