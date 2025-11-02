package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsMeterTypeParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsMeterTypeResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterTypeResultData;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.dao.MGenericTypeDao;

/**
 * 種別設定表示画面
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingTypeBean")
@ConversationScoped
public class TypeBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 9167495115470961829L;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    @Inject
    private TypeRangeBean typeRangeBean;

    /** 汎用区分 */
    @Inject
    private GenericTypeList genericTypeList;

    /** 汎用区分マスタDao. */
    @EJB
    private MGenericTypeDao mGenericTypeDao;

    /** CO2排出係数単位名マップ. */
    private Map<String, String> unitCo2CoefficientByIdMap;
    /** 電気メニュー名マップ. */
    private Map<Long, String> menuNameByIdMap;

    /** メーター種別設定 + 従量値 検索結果. */
    private List<MeterType> meterTypeList;

    /**
     * この内容で登録します。よろしいですか？
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:init():START"));

        // --- 汎用区分マスタ取得 ---

        // CO2排出係数単位名マップ
        this.unitCo2CoefficientByIdMap = swapKeyAndValue(genericTypeList.getSmsUnitCo2Coefficient());

        // 電気メニュー名マップ
        this.menuNameByIdMap = convLongSwapKeyAndValue(genericTypeList.getSmsElectricMenu());

        // --- 検索実処理 ---
        executeSearch(topBean.getTopBeanProperty().getListInfo());

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:init():END"));
        return "smsCollectSettingMeterreadingType";
    }

    /**
     * 一覧画面再表示（種別設定編集画面 で 登録 を行い正常終了したときの処理）
     * @return 画面Beanページ
     */
    public String initReturn() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:initReturn():START"));

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:initReturn():END"));
        return init(); // 再検索実
    }

    /**
     * 画面遷移処理
     * @return
     */
    @Logged
    public String execNextScreen(MeterType item) {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:execNextScreen():START"));

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeBean:execNextScreen():END"));
        return typeRangeBean.initType(item);
    }

    /**
     * 検索実処理
     *
     * @param listInfo 建物情報
     */
    private void executeSearch(ListInfo listInfo) {
        ListSmsMeterTypeResponse response = new ListSmsMeterTypeResponse();
        ListSmsMeterTypeParameter parameter = new ListSmsMeterTypeParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ListSmsMeterTypeBean");

        parameter.setCorpId(listInfo.getCorpId());    // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID

        response = (ListSmsMeterTypeResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 該当データあり

            List<MeterTypeResultData> meterTypeResultList = response.getResult().getMeterTypeResultDataList();
            meterTypeList = new ArrayList<MeterType>();
            meterTypeResultList.stream().forEach(result -> {
                MeterType meterType = new MeterType();
                meterType.setMeterType(result.getMeterType());          // メーター種別
                meterType.setCorpId(result.getCorpId());                // 企業ID
                meterType.setBuildingId(result.getBuildingId());        // 建物ID
                meterType.setMenuNo(result.getMenuNo());                // メニュー番号(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
                meterType.setMenuName(menuNameByIdMap.get(result.getMenuNo())); // 電気メニュー の名称をセット
                meterType.setMeterTypeName(result.getMeterTypeName());  // メーター種別名称
                meterType.setCalcType(result.getCalcType());            // 計算方法（NULL：--  1：単一制  2：段階制  3：業務用季節別時間帯別電力  4：業務用電力）
                meterType.setUnitPrice(result.getUnitPrice());          // 単一制単価
                meterType.setAllComDiv(result.getAllComDiv());          // 全体共用按分（NULL：--  1：固定  2：均等  3：使用量率  4：最大デマンド  5：料金メニュー）
                meterType.setUnitUsageBased(result.getUnitUsageBased()); // 従量単位
                meterType.setAutoInspDay(result.getAutoInspDay());      // 自動検針日時（日）（0：日の指定なし  1～31：日の指定）
                meterType.setCo2Coefficient(convStr(result.getCo2Coefficient())); // CO2排出係数
                meterType.setUnitCo2Coefficient(result.getUnitCo2Coefficient()); // CO2排出係数単位
                meterType.setUnitCo2CoefficientName(unitCo2CoefficientByIdMap.get(result.getUnitCo2Coefficient())); // CO2排出係数単位 の名称をセット
                meterType.setAutoInspHour(result.getAutoInspHour());    // 自動検針日時（時）（0～23：時の指定）
                meterType.setRangeValue(result.getRangeValue());        // 従量値(","区切り)
                meterTypeList.add(meterType);
            });
        } else {
            // 該当データなし
            meterTypeList = null;
        }
    }

    /**
     * Mapのキーと値を入れ替える
     * @param baseMap 変換元Map
     * @return 返還後のMap
     */
    private HashMap<String, String> swapKeyAndValue(Map<String, String> baseMap) {
        HashMap<String, String> map = new LinkedHashMap<>();
        baseMap.keySet().stream().forEach(key -> { map.put(baseMap.get(key), key); } );
        return map;
    }

    /**
     * Mapのキーと値を入れ替えて Map<String, String> 型を Map<Long, String> 型に変換
     * @param baseMap 変換元Map
     * @return 返還後のMap
     */
    private Map<Long, String> convLongSwapKeyAndValue(Map<String, String> baseMap) {
        HashMap<Long, String> map = new LinkedHashMap<>();
        baseMap.keySet().stream().forEach(key -> { map.put(Long.valueOf(baseMap.get(key)), key); } );
        return map;
    }

    /**
     * 文字型に変換
     * @param val 数値
     * @return 変換後の文字
     */
    private String convStr(BigDecimal val) {
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    public List<MeterType> getMeterTypeList() {
        return meterTypeList;
    }

}
