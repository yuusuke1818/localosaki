package jp.co.osaki.osol.access.filter.dao;

import java.util.List;

import jp.skygroup.enl.webap.base.BaseDao;

/**
 *
 * データフィルターDao 抽象クラス
 *
 * @author take_suzuki
 * @param <E> データフィルターリストオブジェクトクラス
 * @param <P> データフィルターパラメータオブジェクトクラス
 */
public abstract class AbstractDataFilterDao<E, P> extends BaseDao {

    /**
     *
     * データフィルター取得メソッド
     *
     * @param paramObject データフィルターパラメータオブジェクト
     * @return データフィルターリスト
     */
    protected abstract List<E> getDataFilter(P paramObject);

    /**
     *
     * データフィルター適用メソッド
     *
     * @param <T> データリストオブジェクトクラス
     * @param inputList フィルター適用前のデータリスト
     * @param paramObject フィルター取得パラメータオブジェクト
     * @return フィルター適用後のデータリスト
     */
    public abstract <T> List<T> applyDataFilter(List<T> inputList, P paramObject);

}
