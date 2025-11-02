package jp.co.osaki.osol.access.function.parser;

import java.io.File;
import java.util.LinkedHashMap;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * XMLファイルパーサー インターフェース
 *
 * @author take_suzuki
 * @param <M> 使用するモデルクラス
 */
public interface XmlParserInterface<M> {

    /**
     * エラーロガー
     */
    public static final Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    /**
     * イベントロガー
     */
    public static final Logger eventLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.EVENT.getVal());

    /**
     *  文字列要素タグ
     */
    public static final String S_TEXT = "#text";

    /**
     *  キー 属性タグ
     */
    public static final String KEY = "Key";

    /**
     *  名 属性タグ
     */
    public static final String NAME = "Name";

    /**
     *
     * XMLファイルパース
     *
     * @param xmlFile パースするXMLファイル
     * @return パースしたXMLの内容を保持するLinkedHashMap
     */
    public LinkedHashMap<String, M> parse(File xmlFile);

}
