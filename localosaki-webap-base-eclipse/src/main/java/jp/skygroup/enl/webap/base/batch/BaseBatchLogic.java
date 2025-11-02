package jp.skygroup.enl.webap.base.batch;

/**
 * バッチ処理用 Logic基底クラス
 *
 * @author take_suzuki
 *
 */
public abstract class BaseBatchLogic extends BaseBatch {

    /**
     * バッチ処理名称
     */
    protected String batchName = this.getClass().getSimpleName();

}
