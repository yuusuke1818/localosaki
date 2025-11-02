package jp.co.osaki.osol.mng.param;

/**
 * Aiel Master 動作モード（取得） Paramクラス
 * @author ya-ishida
 *
 */
public class A210012Param extends BaseParam {

    /**
     * データ取得日時（YYMMDDhhmm）
     */
    private String selectDate;

    /**
     * 動作モード
     */
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
