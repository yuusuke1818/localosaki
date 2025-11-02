package jp.co.osaki.sms;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseProperties;

/**
 *
 * メッセージプロパティ取得
 *
 * 各画面で使用するメッセージ(SmsMessages.properties)を取得する。
 *
 * @author take_suzuki
 */
@Named(value = "smsMessages")
@ApplicationScoped
public class SmsMessages extends BaseProperties implements Serializable {

    private static final long serialVersionUID = 6164080018317195267L;

    /**
     *  初期化処理
     *
     *  プロパティファイルから読込みプロパティリストに設定する。
     */
    @PostConstruct
    public void initMessages() {

        this.propertyFileName = this.getClass().getSimpleName().concat(".properties");
        super.initProperties();

    }

    /**
     *  メッセージ取得処理
     *
     * @param propertyName メッセージプロパティ名
     * @return 取得メッセージ
     */
    public String getMessage(final String propertyName) {

        return super.getProperty(propertyName);

    }

    /**
     *  メッセージ取得処理(文字列置き換え)
     *
     * @param propertyName メッセージプロパティ名
     * @param arg 置き換え文字列
     * @return 取得メッセージ
     */
    public String getMessageFormat(final String propertyName, String arg[]) {

        if (propertyName == null || propertyName.isEmpty()) {
            return "";
        }

        MessageFormat messageFormat = new MessageFormat(super.getProperty(propertyName));
        return messageFormat.format(arg);
    }

    /**
     *  メッセージ取得処理(文字列置き換え)
     *
     * @param propertyName メッセージプロパティ名
     * @param parameters 置き換え文字列
     * @return 取得メッセージ
     */
    public String getMessageFormat(final String propertyName, String parameters) {

        if (propertyName == null || propertyName.isEmpty()) {
            return "";
        }
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }

        return this.getMessageFormat(propertyName, parameters.split(",", 0));
    }
}
