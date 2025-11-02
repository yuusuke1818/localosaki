package jp.co.osaki.osol.access.filter.datafilter;

import java.util.Map;

import javax.persistence.EntityManager;

/**
 *
 * フィルターデータ取得のインターフェース
 *
 * @param <T>   結果セットクラス
 * @param <E>   パラメータクラス
 * @author take_suzuki
 */
public interface GetFilterDataInterface<T, E> {

    public Map<String, T> getFilterData(EntityManager em, E param);
}
