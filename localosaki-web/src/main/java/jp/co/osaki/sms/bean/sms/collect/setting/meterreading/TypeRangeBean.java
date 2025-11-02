package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.base.Objects;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter.Ranges;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter.Ranges.RangeBean;
import jp.co.osaki.osol.api.response.sms.meterreading.GetSmsMeterTypeRangeResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsMeterTypeRangeResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterRangeResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterTypeResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MGenericTypeDao;

/**
 * 種別設定編集画面
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingTypeRangeBean")
@ConversationScoped
public class TypeRangeBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 9167495115470961829L;

    // 従量範囲最大値設定
    private static final int MEASURED_RATE_SYSTEM_MIN = 1;

    // 従量範囲最大値設定
    private static final int MEASURED_RATE_SYSTEM_MAX = 999999;

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

    /** 汎用区分 */
    @Inject
    private GenericTypeList genericTypeList;

    /** プルダウンリストクラス. */
    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private TypeBean typeBean;

    // エラーリスト（背景色用）
    private List<String> invalidComponent;

    /** CO2排出係数単位名マップ. */
    private Map<String, String> unitCo2CoefficientByIdMap;
    /** 電気メニュー名マップ. */
    private Map<Long, String> menuNameByIdMap;

    /** プルダウンリスト：CO2排出係数単位名マップ. */
    private Map<String, String> unitCo2CoefficientMap;

    /** メーター種別設定. */
    private MeterType meterType = new MeterType();

    /** メーター種別従量値 一覧. */
    private List<MeterRange> meterRangeList;

    /** メーター種別従量値 削除対象一覧. */
    private List<MeterRange> deleteMeterRangeList;

    /** 従量値(追加行) */
    private String addRangeValue;

    /**
     * この内容で登録します。よろしいですか？
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    @Override
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:init():START"));

        invalidComponent = new ArrayList<>();

        // --- プルダウンリストのMap生成 ---

        // CO2排出係数単位名マップ
        this.unitCo2CoefficientMap = toolsPullDownList.getUnitCo2Coefficient(false, null);

        // --- 汎用区分マスタ取得 ---

        // CO2排出係数単位名マップ
        this.unitCo2CoefficientByIdMap = swapKeyAndValue(this.unitCo2CoefficientMap);

        // 電気メニュー名マップ
        this.menuNameByIdMap = convLongSwapKeyAndValue(genericTypeList.getSmsElectricMenu());

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:init():END"));
        return "smsCollectSettingMeterreadingTypeRange";
    }

    /**
     * 種別設定画面(一覧)からの画面遷移時の処理
     * @param item 種別設定画面で選択した行の情報
     * @return 画面Beanページ
     */
    public String initType(MeterType item) {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:initType():START"));

        // 画面初期化
        init();

        // 検索実処理
        if (!executeSearch(topBean.getTopBeanProperty().getListInfo(), item)) {
            // データ異常(不正操作など)
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.parameter"));
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:initType():END (error)"));
            return "";
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:initType():END"));
        return "smsCollectSettingMeterreadingTypeRange";
    }

    /**
     * (従量値)行追加ボタン押下時
     */
    @Logged
    public void addRowButton() {
        if (!validate(false)) {
            // 入力チェック・エラー
            eventLogger.debug(
                    packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():END (validate error)"));
            return;
        }

        addRangeValueList(); // 従量値 追加
    }

    /**
     * 従量値 追加
     */
    private void addRangeValueList() {
        MeterRange meterRange = new MeterRange(
                null,
                meterType.getMeterType(),
                meterType.getCorpId(),
                meterType.getBuildingId(),
                meterType.getMenuNo(),
                0,
                this.addRangeValue);
        meterRangeList.add(meterRange);
        this.addRangeValue = "";
    }

    /**
     * 行削除ボタン押下時
     * 選択行を削除する（「登録」ボタン押下時にＤＢから削除される）.
     * @param meterRange メーター種別従量値リスト選択行の値
     */
    @Logged
    public void deleteRowButton(MeterRange meterRange) {
        // 新規行 → 行削除
        meterRangeList.remove(meterRange);
        if (meterRange.getRangeValue() != null) {
            // 新規ではない行 → DBの削除対象一覧に追加
            if (deleteMeterRangeList == null) {
                deleteMeterRangeList = new ArrayList<MeterRange>();
            }
            deleteMeterRangeList.add(meterRange);
        }
    }

    /**
     * 登録ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String regist() throws ParseException {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():START"));

        // 企業ID・建物IDチェック
        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();
        if (!listInfo.getCorpId().equals(meterType.getCorpId())
                || !listInfo.getCorpId().equals(meterType.getCorpId())) {
            // 建物選択画面で選択した条件と 企業ID または 建物ID が一致しない (画面操作外の不正操作が行われた)
            // データ異常(不正操作など)
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.parameter"));
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():END (error)"));
            return "";
        }

        // 追加行チェック
        if (!CheckUtility.isNullOrEmpty(addRangeValue)) {
            // 追加行に従量値が入力されている → 行追加ボタン押下時の処理を実施
            addRangeValueList(); // 従量値 追加
        }

        if (!validate(true)) {
            // 入力チェック・エラー
            eventLogger.debug(
                    packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():END (validate error)"));
            return "";
        }

        UpdateSmsMeterTypeRangeResponse response = new UpdateSmsMeterTypeRangeResponse();
        UpdateSmsMeterTypeRangeParameter parameter = new UpdateSmsMeterTypeRangeParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSmsMeterTypeRangeBean");
        parameter.setMeterType(meterType.getMeterType()); // メーター種別(Key)
        parameter.setCorpId(meterType.getCorpId()); // 企業ID(Key)
        parameter.setBuildingId(meterType.getBuildingId()); // 建物ID(Key)
        parameter.setMenuNo(meterType.getMenuNo()); // メニュー番号(Key)(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
        parameter.setMeterTypeName(meterType.getMeterTypeName()); // メーター種別名称
        parameter.setUnitUsageBased(meterType.getUnitUsageBased()); // 従量単位
        parameter.setCo2Coefficient(StringUtility.toBigDecimal(meterType.getCo2Coefficient())); // CO2排出係数
        parameter.setUnitCo2Coefficient(meterType.getUnitCo2Coefficient()); // CO2排出係数単位
        parameter.setVersion(meterType.getVersion()); // 排他制御用カラム

        // 削除分
        Ranges range = parameter.new Ranges();
        List<RangeBean> rList = new ArrayList<RangeBean>();
        if (deleteMeterRangeList != null) {
            for (MeterRange meterRange : deleteMeterRangeList) {
                rList.add(range.new RangeBean(
                        meterRange.getRangeValue(), // 従量値(Key)
                        null, // 従量値(編集値) ※null:削除
                        meterRange.getVersion())); // 排他制御用カラム
            }
        }
        // 更新・新規追加 分
        for (MeterRange meterRange : meterRangeList) {
            rList.add(range.new RangeBean(
                    meterRange.getRangeValue(), // 従量値(Key) ※null:新規登録
                    Long.valueOf(meterRange.getRangeValueEdit()), // 従量値(編集値)
                    meterRange.getVersion())); // 排他制御用カラム  ※新規登録時は"0"をセットする
        }

        // 従量範囲設定値が「MEASURED_RATE_SYSTEM_MAX=999999」の設定を探す
        if (!rList.stream().filter(r -> new Long(MEASURED_RATE_SYSTEM_MAX).equals(r.getRangeValueEdit())).findFirst().isPresent()) {

        	// 存在しない場合は「MEASURED_RATE_SYSTEM_MAX=999999」の設定を追加する
            rList.add(range.new RangeBean(
                    null, // 従量値(Key) ※null:新規登録
                    Long.valueOf(MEASURED_RATE_SYSTEM_MAX), // 従量値(編集値)
                    0)); // 排他制御用カラム  ※新規登録時は"0"をセットする
        }

        range.setRangeList(rList);
        parameter.setRanges(range);

        response = (UpdateSmsMeterTypeRangeResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 登録成功
            // 登録後のデータを反映
            dispDbData(
                    response.getResult().getMeterTypeResultData(),
                    response.getResult().getMeterRangeResultDataList());
            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));

            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():END (success)"));
            return typeBean.initReturn(); // 一覧画面に戻る
        } else {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingTypeRangeBean:regist():END"));
        return "";
    }

    /**
     * 入力チェック.
     * @param isRec true:登録ボタン押下時  false:行追加ボタン押下時
     * @return true:入力OK false:入力NG
     */
    private boolean validate(boolean isRec) {
        boolean isChkOk = true;
        invalidComponent = new ArrayList<>();

        if (isRec) {
            // 登録ボタン押下時

            // --- メーター種別設 ---

            // メーター種別名称
            if (CheckUtility.isNullOrEmpty(meterType.getMeterTypeName())) {
                addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.meterTypeName"));
                invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:meterTypeName");
                isChkOk = false;
            }

            // CO2排出係数 ※非必須
            if (!CheckUtility.isNullOrEmpty(meterType.getCo2Coefficient())
                    && !isWithinRange(meterType.getCo2Coefficient(), new BigDecimal("0.000"), new BigDecimal("9.9999"))) {
                addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.co2Coefficient"));
                invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:co2Coefficient");
                isChkOk = false;
            }

            // CO2排出係数単位
            if (CheckUtility.isNullOrEmpty(meterType.getUnitCo2Coefficient())) {
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.unitCo2Coefficient"));
                isChkOk = false;
            }

            // 従量単位 ※非必須のためチェック対象外

            // --- メーター種別従量値 一覧 ---

            // 範囲チェック(1～999999)
            int count = 0;
            for (MeterRange meterRange : meterRangeList) {
                if (CheckUtility.isNullOrEmpty(meterRange.getRangeValueEdit())
                        || !isWithinRange(meterRange.getRangeValueEdit(), MEASURED_RATE_SYSTEM_MIN, MEASURED_RATE_SYSTEM_MAX)) {
                    addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.rangeValue"));
                    invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:rangeValue_" + count);
                    isChkOk = false;
                }
                count++;
            }

            // 重複チェック
            boolean isDuplication = false; // 重複フラグ
            for (int idx = 0; idx < meterRangeList.size() - 1 && !isDuplication; idx++) {
                for (int idx2 = idx + 1; idx2 < meterRangeList.size(); idx2++) {
                    if (Objects.equal(meterRangeList.get(idx).getRangeValueEdit(),
                            meterRangeList.get(idx2).getRangeValueEdit())) {
                        // 重複あり
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.duplicationRangeValue"));
                        invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:rangeValue_" + idx);
                        isChkOk = false;
                        isDuplication = true; // 重複チェックを抜ける
                        break;
                    }
                }
            }

        } else {
            // 行追加ボタン押下時

            // --- 従量範囲設定 追加行 ---

            // (追加行)従量値 必須チェック
            if (CheckUtility.isNullOrEmpty(addRangeValue)) {
                addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.rangeValue"));
                invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:addRangeValue");
                isChkOk = false;
            } else {
                // 範囲チェック(1～999999)
                if (!isWithinRange(addRangeValue, MEASURED_RATE_SYSTEM_MIN, MEASURED_RATE_SYSTEM_MAX)) {
                    addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.rangeValue"));
                    invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:addRangeValue");
                    isChkOk = false;
                }

                // 重複チェック
                for (MeterRange meterRange : meterRangeList) {
                    if (Objects.equal(meterRange.getRangeValueEdit(), addRangeValue)) {
                        // 重複あり
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterreadingTypeRangeBean.error.duplicationRangeValue"));
                        invalidComponent.add("smsCollectSettingMeterreadingTypeRangeBean:addRangeValue");
                        isChkOk = false;
                        break;
                    }
                }
            }
        }

        return isChkOk;
    }

    /**
     * 検索実処理
     *
     * @param listInfo 建物情報
     * @param item 種別設定画面(一覧)で選択した情報
     * @return true:正常  false:データ異常(不正操作など)
     */
    private boolean executeSearch(ListInfo listInfo, MeterType item) {
        GetSmsMeterTypeRangeResponse response = new GetSmsMeterTypeRangeResponse();
        GetSmsMeterTypeRangeParameter parameter = new GetSmsMeterTypeRangeParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("GetSmsMeterTypeRangeBean");

        if (!listInfo.getCorpId().equals(item.getCorpId())
                || !Long.valueOf(listInfo.getBuildingId()).equals(item.getBuildingId())) {
            // 建物選択画面で選択した条件と 企業ID または 建物ID が一致しない (画面操作外の不正操作が行われた)
            meterRangeList = null;
            return false;
        }

        parameter.setMeterType(item.getMeterType()); // メーター種別
        parameter.setCorpId(item.getCorpId()); // 企業ID
        parameter.setBuildingId(new Long(item.getBuildingId())); // 建物ID
        parameter.setMenuNo(item.getMenuNo()); // メニュー番号

        response = (GetSmsMeterTypeRangeResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 該当データあり
            // 検索結果を表示
            dispDbData(
                    response.getResult().getMeterTypeResultData(),
                    response.getResult().getMeterRangeResultDataList());
        } else {
            // 該当データなし
            meterType = new MeterType();
            meterRangeList = null;
        }

        return true;
    }

    /**
     * 検索結果を表示
     * @param meterTypeResult メーター種別設定
     * @param meterRangeResultList メータ種別従量値情報
     */
    private void dispDbData(MeterTypeResultData meterTypeResult, List<MeterRangeResultData> meterRangeResultList) {
        meterType = new MeterType();
        meterType.setMeterType(meterTypeResult.getMeterType()); // メーター種別
        meterType.setCorpId(meterTypeResult.getCorpId()); // 企業ID
        meterType.setBuildingId(meterTypeResult.getBuildingId()); // 建物ID
        meterType.setMenuNo(meterTypeResult.getMenuNo()); // メニュー番号(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
        meterType.setMenuName(menuNameByIdMap.get(meterTypeResult.getMenuNo())); // 電気メニュー の名称をセット
        meterType.setMeterTypeName(meterTypeResult.getMeterTypeName()); // メーター種別名称
        meterType.setCalcType(meterTypeResult.getCalcType()); // 計算方法（NULL：--  1：単一制  2：段階制  3：業務用季節別時間帯別電力  4：業務用電力）
        meterType.setUnitPrice(meterTypeResult.getUnitPrice()); // 単一制単価
        meterType.setAllComDiv(meterTypeResult.getAllComDiv()); // 全体共用按分（NULL：--  1：固定  2：均等  3：使用量率  4：最大デマンド  5：料金メニュー）
        meterType.setUnitUsageBased(meterTypeResult.getUnitUsageBased()); // 従量単位
        meterType.setAutoInspDay(meterTypeResult.getAutoInspDay()); // 自動検針日時（日）（0：日の指定なし  1～31：日の指定）※編集画面に項目なし（未使用）
        meterType.setCo2Coefficient(convStr(meterTypeResult.getCo2Coefficient())); // CO2排出係数
        meterType.setUnitCo2Coefficient(meterTypeResult.getUnitCo2Coefficient()); // CO2排出係数単位
        meterType.setUnitCo2CoefficientName(unitCo2CoefficientByIdMap.get(meterTypeResult.getUnitCo2Coefficient())); // CO2排出係数単位 の名称をセット
        meterType.setAutoInspHour(meterTypeResult.getAutoInspHour()); // 自動検針日時（時）（0～23：時の指定）※編集画面に項目なし（未使用）
        meterType.setVersion(meterTypeResult.getVersion()); // 排他制御用カラム

        meterRangeList = new ArrayList<MeterRange>();
        if (meterRangeResultList != null) {
            meterRangeResultList.stream().forEach(result -> {
                MeterRange meterRange = new MeterRange(
                        result.getRangeValue(), // 従量値
                        result.getMeterType(), // メーター種別
                        result.getCorpId(), // 企業ID
                        result.getBuildingId(), // 建物ID
                        result.getMenuNo(), // メニュー番号(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
                        result.getVersion(), // 排他制御用カラム
                        result.getRangeValue().toString()); // 従量値(編集用)
                meterRangeList.add(meterRange);
            });
        }

        deleteMeterRangeList = null; // メーター種別従量値 削除対象一覧クリア
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
     * 範囲チェック(BigDecimal)
     * @param numStr String型
     * @param min 下限
     * @param max 上限
     * @return true:範囲内  fa;se:範囲外
     */
    private boolean isWithinRange(String numStr, BigDecimal min, BigDecimal max) {
        // 数値チェック
        if (!StringUtility.isNumeric(numStr)) {
            return false;
        }

        // 範囲チェック
        BigDecimal num = new BigDecimal(numStr);
        if (num.compareTo(min) < 0 || num.compareTo(max) > 0) {
            return false;
        }

        return true;
    }

    /**
     * Mapのキーと値を入れ替える
     * @param baseMap 変換元Map
     * @return 返還後のMap
     */
    private HashMap<String, String> swapKeyAndValue(Map<String, String> baseMap) {
        HashMap<String, String> map = new LinkedHashMap<>();
        baseMap.keySet().stream().forEach(key -> {
            map.put(baseMap.get(key), key);
        });
        return map;
    }

    /**
     * Mapのキーと値を入れ替えて Map<String, String> 型を Map<Long, String> 型に変換
     * @param baseMap 変換元Map
     * @return 返還後のMap
     */
    private Map<Long, String> convLongSwapKeyAndValue(Map<String, String> baseMap) {
        HashMap<Long, String> map = new LinkedHashMap<>();
        baseMap.keySet().stream().forEach(key -> {
            map.put(Long.valueOf(baseMap.get(key)), key);
        });
        return map;
    }

    public List<MeterRange> getMeterRangeList() {
        return meterRangeList;
    }

    public Map<String, String> getUnitCo2CoefficientMap() {
        return unitCo2CoefficientMap;
    }

    public String getAddRangeValue() {
        return addRangeValue;
    }

    public void setAddRangeValue(String addRangeValue) {
        this.addRangeValue = zeroSuppression(addRangeValue); // 先頭が0の場合は0を削除する(重複チェックで 01 と 1 を同じ値と判断させるため)
    }

    /**
     * 先頭が0の場合は0を削除する
     * @return ゼロサプレスした値
     */
    private String zeroSuppression(String val) {
        return (val == null ? null : val.replaceAll("^0+([1-9])", "$1"));
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

    public MeterType getMeterType() {
        return meterType;
    }
}
