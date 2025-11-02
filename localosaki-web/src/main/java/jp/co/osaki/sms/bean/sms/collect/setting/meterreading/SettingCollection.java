package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

/**
 * 設定一括収集画面 検索結果
 * @author kobayashi.sho
 */
public class SettingCollection {

    /** 選択. */
    private boolean checkbox;

    /** 選択欄 非活性フラグ. */
    private boolean checkboxDisabled;

    /** 収集コマンド. */
    private String command;

    /** 収集コマンド名. */
    private String commandName;

    /** 操作状態. */
    private String commandStatus;

    /** 操作状態欄文字色(true:赤 false:青). */
    private boolean commandError;

    public SettingCollection(boolean checkbox, boolean checkboxDisabled, String command, String commandName, String commandStatus, boolean commandError) {
        this.checkbox = checkbox;                   // 選択
        this.checkboxDisabled = checkboxDisabled;   // 選択欄 非活性フラグ
        this.command = command;                     // 収集コマンド
        this.commandName = commandName;             // 収集コマンド名
        this.commandStatus = commandStatus;         // 操作状態
        this.commandError = commandError;           // 操作状態欄文字色(true:赤  false:青)
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(String commandStatus) {
        this.commandStatus = commandStatus;
    }

    public boolean getCheckboxDisabled() {
        return checkboxDisabled;
    }

    public void setCheckboxDisabled(boolean checkboxDisabled) {
        this.checkboxDisabled = checkboxDisabled;
    }

    public boolean getCommandError() {
        return commandError;
    }

    public void setCommandError(boolean commandError) {
        this.commandError = commandError;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
