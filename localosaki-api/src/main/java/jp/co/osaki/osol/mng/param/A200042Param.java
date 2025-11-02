package jp.co.osaki.osol.mng.param;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * 複数建物・テナント一括 温度制御(設定) Param クラス
 *
 * @author f_takemura
 */

public class A200042Param extends BaseParam {

    /*
     ************************************
     * 温度制御で利用するパラメータ     *
     ************************************
     */
    /**
     * 取得日時
     */
    private String settingDate;

    /**
     * 温湿度制御
     */
    @Pattern(regexp = "[0-9]")
    private String ctrlTH;

    /**
     * 温湿度制御条件
     */
    @Pattern(regexp = "[0-9]")
    private String ctrlTermsTH;

    /**
     * 冷暖房切替用4点端末器アドレス
     */
    @Pattern(regexp = "[0-9]{1,2}")
    private String terminalEquipaddressCW;

    /**
     *デマンド連動制御条件(超過)
     */
    @Pattern(regexp = "[0-9]")
    private String demandGangCtrlTermsEx;

    /**
     * デマンド連動制御条件(注意)
     */
    @Pattern(regexp = "[0-9]")
    private String demandGangCtrlTermsCa;

    /**
     * デマンド連動制御条件(遮断)
     */
    @Pattern(regexp = "[0-9]")
    private String demandGangCtrlTermsBl;

    /**
     * デマンド連動制御条件(限界)
     */
    @Pattern(regexp = "[0-9]")
    private String demandGangCtrlTermsLi;

    /**
     *デマンド連動制御条件(高負荷)
     */
    @Pattern(regexp = "[0-9]")
    private String demandGangCtrlTermsHi;

    /**
     * 温湿度制御時間帯No.X
     */
    private List<Map<String,String>> ctrlTimeZoneTHList;

    /**
     * 制御ポートNo.Y設定
     */
    private List<Map<String,String>> settingCtrlPortList;

    @AssertTrue
    public boolean isValidateCtrlTimeZoneTHList() {
        if(loadList==null && ctrlTimeZoneTHList==null ) {
            return false;
        }

        // イベント制御固有のloadListがnullでない場合実行しない
        if(loadList!=null) {
            return true;
        }

        return ctrlTimeZoneTHList!=null && validateList(ctrlTimeZoneTHList,  new HashMap<String, String>(){{
            put("startHour",  "[0-9]{1,2}");
            put("startMinute", "[0-9]{1,2}");
            put("endHour",     "[0-9]{1,2}");
            put("endMinute",   "[0-9]{1,2}");
        }});
    }//List内のバリデーションチェックを行うメソッド

    @AssertTrue
    public boolean isValidateSettingCtrlPortList() {

        if(loadList==null && settingCtrlPortList==null ) {
            return false;
        }

        // 温度制御時のみ必須のため、イベント制御は実行しない
        if(loadList!=null) {
            return true;
        }

        return  settingCtrlPortList!=null && validateList(settingCtrlPortList,  new HashMap<String, String>(){{

            put("ctrlPermissionTH",  "[0-9]");
//			put("demandGangCtrlPermission", "[0-9]");
//			put("switchChoiceCW",     "[0-9]");
            put("sensoraddressTH",   "[0-9]{1,2}");
            put("temperatureMax",   "[0-9]{1,2}");
            put("temperatureMin",   "[0-9]{1,2}");
            put("humidityMax",   "[0-9]{1,2}");
            put("humidityMin",   "[0-9]{1,2}");
            put("temperatureReturnWidth",   "[0-9]{1,3}");
            put("humidityReturnWidth",   "[0-9]{1,3}");
            put("loadBlockHoldTime",   "[0-9]{1,2}");
            put("loadReturnHoldTime",   "[0-9]{1,2}");

        }});
    }


    /*
     ************************************
     * イベント制御で利用するパラメータ *
     ************************************
     */

    /**
     * 負荷??
     */
    private List<Map<String, Object>> loadList;

    /**
     * イベント??制御設定
     */
    private List<Map<String, String>> settingEventCtrlList;

    @AssertTrue
    public boolean isValidateLoadList() {

        if(loadList==null && ctrlTimeZoneTHList==null ) {
            return false;
        }

        if(ctrlTimeZoneTHList!=null) {
            return true;
        }


        return loadList != null && isValidateSettingEventCtrlList(loadList, new HashMap<String, String>() {
            {
                put("eventCtrlFlg", "[0-9]");				// イベント制御フラグ
                put("settingEventCtrlList", "");            // イベント??制御設定
                put("blockHoldTime", "[0-9]{1,2}");         // 遮断保持時間
                put("returnHoldTime", "[0-9]{1,2}");        // 復帰保持時間
            }
        });
    }

    private boolean isValidateSettingEventCtrlList(List<Map<String, Object>> list, HashMap<String, String> validationPattern) {
        for(Map<String, Object> obj : list) {
            for(Entry<String, String> ent : validationPattern.entrySet()) {
                Object value = obj.get(ent.getKey());
                if(value == null) {
                    return false;

                }

                if(value instanceof String) {
                    if(!((String)value).matches(ent.getValue())) {
                        return false;
                    }
                }else if(value instanceof List) {
                    // リスト内のリストをバリデーションチェック
                    @SuppressWarnings("unchecked")
                    List<Map<String,String>> valueList = (List<Map<String,String>>)value;
                    if(!this.validateList(valueList, new HashMap<String, String>(){{
                        put("measuredValue",  "[0-9]{1,3}");			// 計測値
                        put("ctrlThreshold", "[\\s\\S0-9]{1,6}");       // 制御閾値
                        put("ctrlTerms",     "[0-9]");                  // 制御条件
                        put("eventTerms",   "[0-9]");                   // イベント条件
                        put("returnWidth",   "[0-9]{1,5}");				// 復帰幅
                    }})){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     ************************************
     * 共通で利用するパラメータ *
     ************************************
     */

    /**
     * 温度制御設定フラグ
     */
    private String temperatureCtrlFlg;

    /**
     * 温度制御種別
     */
    private String tempControlType;

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getCtrlTH() {
        return ctrlTH;
    }

    public void setCtrlTH(String ctrlTH) {
        this.ctrlTH = ctrlTH;
    }

    public String getCtrlTermsTH() {
        return ctrlTermsTH;
    }

    public void setCtrlTermsTH(String ctrlTermsTH) {
        this.ctrlTermsTH = ctrlTermsTH;
    }

    public String getTerminalEquipaddressCW() {
        return terminalEquipaddressCW;
    }

    public void setTerminalEquipaddressCW(String terminalEquipaddressCW) {
        this.terminalEquipaddressCW = terminalEquipaddressCW;
    }

    public String getDemandGangCtrlTermsEx() {
        return demandGangCtrlTermsEx;
    }

    public void setDemandGangCtrlTermsEx(String demandGangCtrlTermsEx) {
        this.demandGangCtrlTermsEx = demandGangCtrlTermsEx;
    }

    public String getDemandGangCtrlTermsCa() {
        return demandGangCtrlTermsCa;
    }

    public void setDemandGangCtrlTermsCa(String demandGangCtrlTermsCa) {
        this.demandGangCtrlTermsCa = demandGangCtrlTermsCa;
    }

    public String getDemandGangCtrlTermsBl() {
        return demandGangCtrlTermsBl;
    }

    public void setDemandGangCtrlTermsBl(String demandGangCtrlTermsBl) {
        this.demandGangCtrlTermsBl = demandGangCtrlTermsBl;
    }

    public String getDemandGangCtrlTermsLi() {
        return demandGangCtrlTermsLi;
    }

    public void setDemandGangCtrlTermsLi(String demandGangCtrlTermsLi) {
        this.demandGangCtrlTermsLi = demandGangCtrlTermsLi;
    }

    public String getDemandGangCtrlTermsHi() {
        return demandGangCtrlTermsHi;
    }

    public void setDemandGangCtrlTermsHi(String demandGangCtrlTermsHi) {
        this.demandGangCtrlTermsHi = demandGangCtrlTermsHi;
    }

    public List<Map<String, String>> getCtrlTimeZoneTHList() {
        return ctrlTimeZoneTHList;
    }

    public void setCtrlTimeZoneTHList(List<Map<String, String>> ctrlTimeZoneTHList) {
        this.ctrlTimeZoneTHList = ctrlTimeZoneTHList;
    }

    public List<Map<String, String>> getSettingCtrlPortList() {
        return settingCtrlPortList;
    }

    public void setSettingCtrlPortList(List<Map<String, String>> settingCtrlPortList) {
        this.settingCtrlPortList = settingCtrlPortList;
    }

    public List<Map<String, Object>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, Object>> loadList) {
        this.loadList = loadList;
    }

    public List<Map<String, String>> getSettingEventCtrlList() {
        return settingEventCtrlList;
    }

    public void setSettingEventCtrlList(List<Map<String, String>> settingEventCtrlList) {
        this.settingEventCtrlList = settingEventCtrlList;
    }

    public String getTemperatureCtrlFlg() {
        return temperatureCtrlFlg;
    }

    public void setTemperatureCtrlFlg(String temperatureCtrlFlg) {
        this.temperatureCtrlFlg = temperatureCtrlFlg;
    }

    public String getTempControlType() {
        return tempControlType;
    }

    public void setTempControlType(String tempControlType) {
        this.tempControlType = tempControlType;
    }

    @Override
    public boolean partDataComparison(Object oldData) {
        // 一括機器制御系：比較処理
        if (temperatureCtrlFlg != null) {
            // 温度制御設定、設定なしの場合
            if (SmControlConstants.TEMP_CONTROL_NONE.equals(temperatureCtrlFlg)) {
                return true;
            }
        }

        // 1つでも有効ポートが存在すれば、falseを返却する。
        // 温度制御の場合、各制御ポートの値をチェック
        // 温湿度制御許可(ctrlPermissionTH) : 0 -> 無効, 1 -> 有効
        if (settingCtrlPortList != null) {
            for (Map<String, String> settingCtrlPort : ((A200042Param)oldData).getSettingCtrlPortList()) {
                if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
                    return false;
                }
            }
        }

        // イベント制御の場合、各イベント負荷の値をチェック
        // イベント制御フラグ(eventCtrlFlg) : 0 -> 無効, 1 -> 有効
        else {
            for (Map<String, Object> load : ((A200042Param)oldData).getLoadList()) {
                String strValue = String.valueOf(load.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);

                // FVPα(G2)の場合
                if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(getProductCd())) {
                    Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                    Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);

                    // イベント制御設定リスト
                    List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(
                            String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>() {
                            }.getType());
                    // イベント条件
                    String eventTerms = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);

                    // イベント制御フラグ = 有効 且つ イベント条件 = 温度制御
                    if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)
                            && (!CheckUtility.isNullOrEmpty(eventTerms)
                                    && eventTerms.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE))) {
                        return false;
                    }

                // Eα / Eα2の場合
                } else if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(getProductCd())
                        || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(getProductCd())) {
                    // イベント制御フラグ = 有効
                    if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean partDataComparison(Object oldData, Object newData) {

        A200042Param newParam = (A200042Param) newData;

        // 一括機器制御系：比較処理
        if (temperatureCtrlFlg != null) {
            // 温度制御設定、設定なしの場合
            if (SmControlConstants.TEMP_CONTROL_NONE.equals(temperatureCtrlFlg)) {
                return true;
            }
        }

        // 新旧比較して変更があれば、false
        if (settingCtrlPortList != null) {
            int index = 0;
            for (Map<String, String> settingCtrlPort : ((A200042Param)oldData).getSettingCtrlPortList()) {
                List<Map<String, String>> tempSettingCtrlPortNewList = ((A200042Param)newData).getSettingCtrlPortList();
                if (index < tempSettingCtrlPortNewList.size()) {
                    Map<String, String> settingCtrlPortNew = tempSettingCtrlPortNewList.get(index);
                    // 温度制御設定
                    if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.TEMP_CONTROL.equals(newParam.getTempControlType())) {
                        // 新旧比較して一致しない場合、変更ありと判断
                        if (!settingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(settingCtrlPortNew.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH))) {
                            return false;
                        }
                    } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.COOLER.equals(newParam.getTempControlType())) {
                        // 冷房設定
                        // 温度制御可否判定
                        if (settingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)
                                && settingCtrlPortNew.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                            // 新旧比較して一致しない場合、変更ありと判断
                            if (!settingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX).equals(
                                    settingCtrlPortNew.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MAX))) {
                                return false;
                            }
                        }
                    } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.HEATER.equals(newParam.getTempControlType())) {
                        // 暖房設定
                        // 温度制御可否判定
                        if (settingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)
                                && settingCtrlPortNew.get(SmControlConstants.BULK_CTRL_PARAM_CTRL_PERMISSION_TH).equals(SmControlConstants.ABLE_PORT)) {
                            // 新旧比較して一致しない場合、変更ありと判断
                            if (!settingCtrlPort.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN).equals(
                                    settingCtrlPortNew.get(SmControlConstants.BULK_CTRL_PARAM_TEMPERATURE_MIN))) {
                                return false;
                            }
                        }
                    }
                }
                index++;
            }
        }

        // イベント制御の場合、各イベント負荷の値を新旧チェック
        else {
            int index = 0;
            for (Map<String, Object> load : ((A200042Param)oldData).getLoadList()) {
                String strValue = String.valueOf(load.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                int intEventCtrlFlg = (int)Double.parseDouble(strValue);
                Map<String, Object> tempLoad = new HashMap<String, Object>(load);
                Object strSettingEventList = tempLoad.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                // イベント制御設定リスト
                List<Map<String, String>> settingEventCtrlList = new Gson().fromJson(
                        String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String, String>>>() {
                        }.getType());

                List<Map<String, Object>> tempLoadNewList = ((A200042Param)newData).getLoadList();
                if (index < tempLoadNewList.size()) {
                    Map<String, Object> tempLoadNew = tempLoadNewList.get(index);
                    String strValueNew = String.valueOf(tempLoadNew.get(SmControlConstants.BULK_CTRL_PARAM_EVENT_CTRL_FLG));
                    int intEventCtrlFlgNew = (int)Double.parseDouble(strValueNew);
                    Object strSettingEventNewList = tempLoadNew.get(SmControlConstants.BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST);
                    // イベント制御設定リスト
                    List<Map<String, String>> settingEventCtrlNewList = new Gson().fromJson(
                            String.valueOf(strSettingEventNewList), new TypeToken<Collection<Map<String, String>>>() {
                            }.getType());

                    // FVPα(G2)の場合
                    if (SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2.equals(getProductCd())) {
                        // イベント条件
                        String eventTerms = settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);
                        // イベント条件（New）
                        String eventTermsNew = settingEventCtrlNewList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_EVENT_TERMS);

                        // 新旧のイベント条件が温度制御の場合
                        if (!CheckUtility.isNullOrEmpty(eventTerms) && eventTerms.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)
                                && !CheckUtility.isNullOrEmpty(eventTermsNew) && eventTermsNew.equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                            // 温度制御設定
                            if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.TEMP_CONTROL.equals(newParam.getTempControlType())) {
                                // 新旧比較して一致しない場合、変更ありと判断
                                if (!String.valueOf(intEventCtrlFlg).equals(String.valueOf(intEventCtrlFlgNew))) {
                                    return false;
                                }
                            } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.COOLER.equals(newParam.getTempControlType())) {
                                // 冷房設定
                                // 温度制御可否判定
                                if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)
                                        && String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.ABLE_PORT)) {
                                    // 新旧比較して一致しない場合、変更ありと判断
                                    if (!settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD).equals(
                                            settingEventCtrlNewList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD))) {
                                        return false;
                                    }
                                }

                            } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.HEATER.equals(newParam.getTempControlType())) {
                                // 暖房設定
                                // 温度制御可否判定
                                if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)
                                        && String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.ABLE_PORT)) {
                                    // 新旧比較して一致しない場合、変更ありと判断
                                    if (!settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD).equals(
                                            settingEventCtrlNewList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD))) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    // Eα / Eα2の場合
                    else if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(getProductCd())
                        || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(getProductCd())) {
                        // 温度制御設定
                        if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.TEMP_CONTROL.equals(newParam.getTempControlType())) {
                            boolean isTempCtrl = false;
                            boolean isTempCtrlNew = false;
                            // 最新値のイベント制御が温度制御有効/無効の場合
                            if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE)
                                    || String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                                isTempCtrl = true;
                            }

                            // 履歴値のイベント制御が温度制御有効/無効の場合
                            if (String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_DISABLE)
                                    || String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                                isTempCtrlNew = true;
                            }

                            // 温度制御の負荷
                            if (isTempCtrl && isTempCtrlNew) {
                                // 新旧比較して一致しない場合、変更ありと判断
                                if (!String.valueOf(intEventCtrlFlg).equals(String.valueOf(intEventCtrlFlgNew))) {
                                    return false;
                                }
                            }
                        } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.COOLER.equals(newParam.getTempControlType())) {
                            // 冷房設定
                            // 温度制御可否判定
                            if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)
                                    && String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                                // 新旧比較して一致しない場合、変更ありと判断
                                if (!settingEventCtrlList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD).equals(
                                        settingEventCtrlNewList.get(0).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD))) {
                                    return false;
                                }
                            }
                        } else if (!CheckUtility.isNullOrEmpty(newParam.getTempControlType()) && SmControlConstants.HEATER.equals(newParam.getTempControlType())) {
                            // 暖房設定
                            // 温度制御可否判定
                            if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)
                                    && String.valueOf(intEventCtrlFlgNew).equals(SmControlConstants.BULK_CTRL_EVENT_CONTROL_TEMPERATURE)) {
                                // 新旧比較して一致しない場合、変更ありと判断
                                if (!settingEventCtrlList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD).equals(
                                        settingEventCtrlNewList.get(1).get(SmControlConstants.BULK_CTRL_PARAM_CTRL_THRESHOLD))) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                index++;
            }
        }
        return true;
    }
}
