package jp.co.osaki.osol.api;

import java.util.List;

/**
 *
 * OSOL API エラーメッセージリスト クラス.
 *
 * @author take_suzuki
 *
 */
public final class OsolApiErrorMessageListResult extends OsolApiResult {

    /**
     * エラーメッセージリスト
     */
    private List<String> errorMessageList;

    /**
     * @return エラーメッセージリスト
     */
    public List<String> getErrorMessageList() {
        return errorMessageList;
    }

    /**
     * @param errorMessageList セットする エラーメッセージリスト
     */
    public void setErrorMessageList(List<String> errorMessageList) {
        this.errorMessageList = errorMessageList;
    }

}
