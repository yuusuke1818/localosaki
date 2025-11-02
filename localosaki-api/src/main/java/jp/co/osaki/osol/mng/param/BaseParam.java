package jp.co.osaki.osol.mng.param;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseParam {

    /**
     * 製品コード
     */
    private String productCd;

    /**
     * アドレス
     */
    private String address;

    /**
     * SMアドレス(製品コード ＋ アドレス)
     */
    private String smAddress;

    /**
     * コマンド
     */
    private String command;

    /**
     * 圧縮データ長
     */
    private String compressionDataLength;


    /**
     * DB登録フラグ
     */
    private boolean updateDBflg;

    /**
     * ダミーGetter BeanIO定義無効化に利用
     * @return
     */
    public String blank() {
        return null;
    }

    /**
     * ダミーSetter BeanIO定義無効化に利用
     * @param str
     */
    public void blank(String str) {
    }

    /**
     * Paramクラス 比較
     * @param obj
     * @return
     */
    public boolean partDataComparison(Object obj) {
        return false;
    }

       /**
     * Paramクラス 比較（履歴用）
     * @param obj
     * @return
     */
    public boolean partDataComparison(Object obj, Object obj2) {
        return false;
    }

    /**
     * クラス名からコマンドコードを取得
     *
     * @return
     */
    public String getCommandCd() {
        String commandCd = "";
        // クラス名から「A*****」を取得
        Matcher m = Pattern.compile("A\\d+").matcher(this.getClass().getSimpleName());
        if(m.find()) {
            commandCd = m.group();
        }
        // 日報対応 commandに値があれば文字列を追加
        if (commandCd.equals("A200039")) {
            if(this.command != null) {
                commandCd += this.command;
            }
        }

        return commandCd;
    }

    /**
     * BeanValidation Map<String,String>バリデーション対応
     *
     * @param list
     * @param validationPattern
     * @return
     */
    protected boolean validateList(List<Map<String, String>> list, Map<String,String> validationPattern) {
        Iterator<Map<String, String>> itr = list.iterator();
        while(itr.hasNext()) {
            Map<String, String> map = itr.next();
            for(Entry<String, String> ent : validationPattern.entrySet()) {
                String value = String.valueOf(map.get(ent.getKey()));
                if(value==null || !value.matches(ent.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getProductCd() {
        return (productCd!=null && !productCd.isEmpty()) ? productCd : smAddress.substring(0,2);
    }

    public void setProductCd(String productCd) {
        this.productCd = productCd;
    }

    public String getAddress() {
        return (address!=null && !address.isEmpty()) ? address : smAddress.substring(2);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSmAddress() {
        return smAddress;
    }

    public void setSmAddress(String smAddress) {
        this.smAddress = smAddress;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCompressionDataLength() {
        return compressionDataLength;
    }

    public void setCompressionDataLength(String compressionDataLength) {
        this.compressionDataLength = compressionDataLength;
    }

    public boolean isUpdateDBflg() {
        return updateDBflg;
    }

    public void setUpdateDBflg(boolean updateDBflg) {
        this.updateDBflg = updateDBflg;
    }

}
