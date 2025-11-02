package jp.skygroup.enl.webap.base.batch;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * バッチ処理用 基底クラス
 *
 * @author take_suzuki
 */
public abstract class BaseBatch {

    /**
     * バッチログ
     */
    protected static Logger batchLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.BATCH.getVal());

    /**
     * エラーログ
     */
    protected static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

}
