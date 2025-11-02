package jp.skygroup.enl.webap.base.batch;

import java.util.concurrent.Callable;

/**
 * バッチ処理用 スレッド実行 基底クラス
 *
 * @author take_suzuki
 * @param <T>
 *
 */
public abstract class BaseBatchRunner<T> extends BaseBatch implements Callable<T> {

    /**
     * バッチ処理名称
     */
    protected String batchName = this.getClass().getSimpleName();

}
