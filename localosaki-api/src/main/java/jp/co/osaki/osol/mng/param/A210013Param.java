package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Aiel Master 動作モード（設定） Paramクラス
 * @author ya-ishida
 *
 */
public class A210013Param extends BaseParam {

    /**
     * データ取得日時（YYMMDDhhmm）
     */
    private String selectDate;

    /**
     * 動作モード
     */
    @NotNull
    @Pattern(regexp="[0-9]")
    private String actionMode;

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

    public String getActionMode() {
        return actionMode;
    }

    public void setActionMode(String actionMode) {
        this.actionMode = actionMode;
    }

}
