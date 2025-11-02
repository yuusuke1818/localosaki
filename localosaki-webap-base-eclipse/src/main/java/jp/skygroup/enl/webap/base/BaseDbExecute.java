package jp.skygroup.enl.webap.base;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * BaseDbExecuteインターフェース
 *
 * このクラスの実装にてDB更新処理を一括で実行する。
 * 
 * @author take_suzuki
 */
public interface BaseDbExecute {
    
    /**
     *
     * DB一括更新処理
     * 
     * @param parameterMap 検索条件
     * @param em エンティティマネージャ
     * @return 処理結果 true:成功 false:失敗
     */
    public abstract boolean dbExecute(Map<String, List<Object>> parameterMap, EntityManager em);

}
