package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

public class A200069Param extends BaseParam {

    /**
     * 設定変更履歴
     */
    @Pattern(regexp="[0-9]")
    private String settingChangeHist;

    /**
     * 設定変更日時
     */
    private String settingDate;

    /**
     * 負荷EXリスト
     */
    private List<Map<String, String>> loadExList;

    /**
     * 負荷A～H端末アドレス
     */
    private String load1TerminalAddress;

    /**
     * 負荷I～P端末アドレス
     */
    private String load2TerminalAddress;

    /**
     * 負荷リスト
     */
   private List<Map<String, String>> loadList;


    public String getSettingChangeHist() {
        return settingChangeHist;
    }

    public void setSettingChangeHist(String settingChangeHist) {
        this.settingChangeHist = settingChangeHist;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public List<Map<String, String>> getLoadExList() {
        return loadExList;
    }

    public void setLoadExList(List<Map<String, String>> loadExList) {
        this.loadExList = loadExList;
    }

    public String getLoad1TerminalAddress() {
        return load1TerminalAddress;
    }

    public void setLoad1TerminalAddress(String load1TerminalAddress) {
        this.load1TerminalAddress = load1TerminalAddress;
    }

    public String getLoad2TerminalAddress() {
        return load2TerminalAddress;
    }

    public void setLoad2TerminalAddress(String load2TerminalAddress) {
        this.load2TerminalAddress = load2TerminalAddress;
    }

    public List<Map<String, String>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, String>> loadList) {
        this.loadList = loadList;
    }

}
