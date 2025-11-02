package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class A200070Param extends BaseParam {

    /**
     * 負荷EXリスト
     */
    @NotNull
    private List<Map<String, String>> loadExList;

    /**
     * 負荷A～H端末アドレス
     */
    @NotNull
    @Pattern(regexp = "[0-9]{1,2}")
    private String load1TerminalAddress;

    /**
     * 負荷I～P端末アドレス
     */
    @NotNull
    @Pattern(regexp = "[0-9]{1,2}")
    private String load2TerminalAddress;

    /**
     * 負荷リスト
     */
    @NotNull
    private List<Map<String, String>> loadList;

   @AssertTrue
   public boolean isValidateLoadExList() {
       @SuppressWarnings("serial")
       HashMap<String, String> loadExCheckMap = new HashMap<String, String>() {
           {
               put("terminalAddress", "[0-9]{1,2}"); // 端末アドレス
               put("portNo", "[0-8]{1}|0[0-8]{1}"); // ポート番号
//               put("answerBackPoint", "[0-9]{1,3}"); // アンサーバックポイント 詳細チェックはBean
           }
       };

       @SuppressWarnings("serial")
       HashMap<String, String> loadCheckMap = new HashMap<String, String>() {
           {
               put("controlState", "[0-1]{1}"); // 負荷XX制御
           }
       };
       return loadExList != null && validateList(loadExList, loadExCheckMap)
               && loadList != null && validateList(loadList, loadCheckMap);
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
