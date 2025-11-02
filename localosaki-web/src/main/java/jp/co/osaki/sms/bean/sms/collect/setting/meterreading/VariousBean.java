package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsVariousParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsVariousParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.GetSmsVariousResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsVariousResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.GetSmsVariousResult;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsVariousResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MGenericTypeDao;

/**
 * 各種設定画面
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingVariousBean")
@ConversationScoped
public class VariousBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -820593880390287110L;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    /** 汎用区分マスタDao. */
    @EJB
    private MGenericTypeDao mGenericTypeDao;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // エラーリスト（背景色用）
    private List<String> invalidComponent;

    /** プルダウンリスト: 消費税扱いマップ(画面項目パラメータ). */
    private Map<String, String> saleTaxDealMap;
    /** プルダウンリスト: 小数部端数処理マップ(画面項目パラメータ). */
    private Map<String, String> decimalFractionMap;
    /** プルダウンリスト: 年報締め月マップ(画面項目パラメータ). */
    private Map<String, String> yearCloseMonthMap;

    /** 消費税率(画面項目パラメータ). */
    private String saleTaxRate;

    /** 消費税扱い(画面項目パラメータ) ※1:内税/2:外税. */
    private String saleTaxDeal;

    /** 小数部端数処理(画面項目パラメータ) ※1:四捨五入/2:切り捨て/3:切り上げ. */
    private String decimalFraction;

    /** 年報締め月(画面項目パラメータ)  ※1～12:1月末締～12月末締. */
    private String yearCloseMonth;

    /** 排他制御用カラム. */
    private Integer version;

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingVariousBean:init():START"));

        // エラーリスト（背景色用）初期化
        invalidComponent = new ArrayList<>();

        // プルダウンリスト: 消費税扱いマップ
        this.saleTaxDealMap = toolsPullDownList.getSaleTaxDeal(false, null);

        // プルダウンリスト: 小数部端数処理マップ
        this.decimalFractionMap = toolsPullDownList.getDecimalFraction(false, null);

        // プルダウンリスト: 年報締め月マップ
        this.yearCloseMonthMap = toolsPullDownList.getYearCloseMonth(false, null);

        GetSmsVariousResponse response = executeSearch(topBean.getTopBeanProperty().getListInfo());
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 該当データあり
            GetSmsVariousResult result = response.getResult();
            saleTaxRate = (result.getSaleTaxRate() == null ? "" : result.getSaleTaxRate().toString());
            saleTaxDeal = result.getSaleTaxDeal();
            decimalFraction = result.getDecimalFraction();
            yearCloseMonth = result.getYearCloseMonth().toString();
            version = result.getVersion();
        } else {
            // 該当データなし
            saleTaxRate = "";
            saleTaxDeal = "";
            decimalFraction = "";
            yearCloseMonth = null;
            version = 0;
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingVariousBean:init():END"));
        return "smsCollectSettingMeterreadingVarious";
    }

    /**
     * この内容で登録します。よろしいですか？
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    /**
     * 登録ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String regist() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingVariousBean:regist():START"));

        if (!validate()) {
            // 入力チェック・エラー
            eventLogger.debug(
                    packageName.concat(" smsCollectSettingMeterreadingVariousBean:regist():END (validate error)"));
            return "";
        }

        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        UpdateSmsVariousResponse response = new UpdateSmsVariousResponse();
        UpdateSmsVariousParameter parameter = new UpdateSmsVariousParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSmsVariousBean");
        parameter.setCorpId(listInfo.getCorpId()); // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID

        parameter.setSaleTaxRate(new BigDecimal(saleTaxRate)); // 消費税率
        parameter.setSaleTaxDeal(saleTaxDeal); // 消費税扱い
        parameter.setDecimalFraction(decimalFraction); // 小数部端数処理
        parameter.setYearCloseMonth(new BigDecimal(yearCloseMonth)); // 年報締め月
        parameter.setVersion(version); // VERSION

        response = (UpdateSmsVariousResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 登録成功
            UpdateSmsVariousResult result = response.getResult();
            saleTaxRate = (result.getSaleTaxRate() == null ? "" : result.getSaleTaxRate().toString());
            saleTaxDeal = result.getSaleTaxDeal();
            decimalFraction = result.getDecimalFraction();
            yearCloseMonth = result.getYearCloseMonth().toString();
            version = result.getVersion();

            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        } else {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingVariousBean:regist():END"));
        return "";
    }

    /**
     * 入力チェック.
     * @return true:入力OK false:入力NG
     */
    private boolean validate() {
        boolean isChkOk = true;
        invalidComponent = new ArrayList<>();

        // 消費税率
        if (CheckUtility.isNullOrEmpty(saleTaxRate) || !isWithinRange(saleTaxRate, 1, 100)) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingVariousBean.error.saleTaxRate"));
            invalidComponent.add("smsCollectSettingMeterreadingVariousBean:saleTaxRate");
            isChkOk = false;
        }
        // 消費税扱い
        if (CheckUtility.isNullOrEmpty(saleTaxDeal)) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingVariousBean.error.saleTaxDeal"));
            isChkOk = false;
        }
        // 小数部端数処理
        if (CheckUtility.isNullOrEmpty(decimalFraction)) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingVariousBean.error.decimalFraction"));
            isChkOk = false;
        }
        // 年報締め月
        if (CheckUtility.isNullOrEmpty(yearCloseMonth) || !StringUtility.isNumeric(yearCloseMonth)) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingVariousBean.error.yearCloseMonth"));
            isChkOk = false;
        }

        return isChkOk;
    }

    /**
     * 範囲チェック(整数)
     * @param numStr String型
     * @param min 下限
     * @param max 上限
     * @return true:範囲内  fa;se:範囲外
     */
    private boolean isWithinRange(String numStr, int min, int max) {
        // 数値チェック
        if (!StringUtility.isNumeric(numStr)) {
            return false;
        }

        // 範囲チェック
        int num = Integer.parseInt(numStr);
        if (num < min || max < num) {
            return false;
        }

        return true;
    }

    /**
     * 検索実処理
     *
     * @param listInfo 建物情報
     */
    private GetSmsVariousResponse executeSearch(ListInfo listInfo) {
        GetSmsVariousResponse response = new GetSmsVariousResponse();
        GetSmsVariousParameter parameter = new GetSmsVariousParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("GetSmsVariousBean");

        parameter.setCorpId(listInfo.getCorpId()); // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID

        response = (GetSmsVariousResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        return response;
    }

    /**
     * デザイン指定
     */
    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    public String getSaleTaxRate() {
        return saleTaxRate;
    }

    public void setSaleTaxRate(String saleTaxRate) {
        this.saleTaxRate = saleTaxRate;
    }

    public String getSaleTaxDeal() {
        return saleTaxDeal;
    }

    public void setSaleTaxDeal(String saleTaxDeal) {
        this.saleTaxDeal = saleTaxDeal;
    }

    public String getDecimalFraction() {
        return decimalFraction;
    }

    public void setDecimalFraction(String decimalFraction) {
        this.decimalFraction = decimalFraction;
    }

    public String getYearCloseMonth() {
        return yearCloseMonth;
    }

    public void setYearCloseMonth(String yearCloseMonth) {
        this.yearCloseMonth = yearCloseMonth;
    }

    public Map<String, String> getSaleTaxDealMap() {
        return saleTaxDealMap;
    }

    public void setSaleTaxDealMap(Map<String, String> saleTaxDealMap) {
        this.saleTaxDealMap = saleTaxDealMap;
    }

    public Map<String, String> getDecimalFractionMap() {
        return decimalFractionMap;
    }

    public void setDecimalFractionMap(Map<String, String> decimalFractionMap) {
        this.decimalFractionMap = decimalFractionMap;
    }

    public Map<String, String> getYearCloseMonthMap() {
        return yearCloseMonthMap;
    }

    public void setYearCloseMonthMap(Map<String, String> yearCloseMonthMap) {
        this.yearCloseMonthMap = yearCloseMonthMap;
    }

}
