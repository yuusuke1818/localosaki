package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoResult;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.BuildDevMeterResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterGroupNameResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterRangeTenantPriceInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.PriceMenuLightaResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.PriceMenuLightbResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.RangeUnitPriceResultData;
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.common.municipality.Condition;
import jp.co.osaki.sms.bean.common.municipality.SearchBean;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MMunicipalityDao;
import jp.co.osaki.sms.dao.MeterTenantDao;

@Named("smsCollectSettingMeterTenantEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = -6609744494873952881L;

    // 詳細設定画面 基本料金最低値
    private static final String BASIC_PRICE_MIN = "0.00";

    // 詳細設定画面 基本料金最大値
    private static final String BASIC_PRICE_MAX = "99999.99";

    // 詳細設定画面 基本料金パターン（0.00 ～ 99999.99）
    private static final String BASIC_PRICE_PATTERN = "^[0-9]{1,5}(\\.[0-9]{1,2})?$";

    // 詳細設定画面 燃料調整費額最低値
  	private static final String FUEL_ADJUST_PRICE_MIN = "-999.99";

    // 詳細設定画面 燃料調整費額最大値
    private static final String FUEL_ADJUST_PRICE_MAX = "9999.99";

    // 詳細設定画面 燃料調整費額（-99.99 ～ 9999.99）
    private static final String FUEL_ADJUST_PRICE_PATTERN = "^[-]+[0-9]{1,3}(\\.[0-9]{1,2})?$|^[0-9]{1,4}(\\.[0-9]{1,2})?$";	// "^[-]+[0-9]{1,2}→"^[-]+[0-9]{1,3}

    // グループ設定 按分率最小値
    private static final String DIV_RATE_MIN = "0";

    // グループ設定 按分率最小値
    private static final String DIV_RATE_MAX = "100";

    // 登録計器（メーター）ID1行あたりの表示数
    private static final int METER_ID_COLUMN = 5;

    // メーターテナント管理画面プロパティ
    @Inject
    private EditBeanProperty editBeanProperty;

    @Inject
    private OsolAccessBean osolAccessBean;

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private PullDownList toolsPullDownList;

    /**
     * 自治体コードエンティティ保持
     */
    private MMunicipality mMunicipalityEntity;

    /**
     * 自治体汎用検索クラス
     */
    @Inject
    private SearchBean municipalitySearchBean;

    // メーターテナント一覧画面
    @Inject
    private jp.co.osaki.sms.bean.sms.collect.setting.meterTenant.SearchBean tenantSearchBean;

    // 自治体マスタ検索クラス
    @EJB
    private MMunicipalityDao mMunicipalityDao;

    // メーターテナント検索クラス
    @EJB
    private MeterTenantDao meterTenantDao;

    // 都道府県用セレクトボックス
    private Map<String, String> prefectureMap;

    // 推計種別用セレクトボックス
    private Map<String, String> suikeifuncMap;

    // 推計種別用セレクトボックス
    private Map<String, String> suikeitypeMap;

    // 電気料金メニュー
    private Map<String, String> priceMenuMap;

    // 契約容量
    private Map<String, String> contCapacityMap;

    // 確認ダイアログメッセージ
    private String dialogMessage;

    // 詳細設定画面
    private EditDetailPopupInfo editDetailPopupInfo;

    // 詳細設定画面OK/NGフラグ
    private boolean editDetailPopupCompFlg;

    private List<String> infoMessages;
    private List<String> errorMessages;
    private List<String> errorDetailMessages;
    private List<String> invalidComponent;

    private int popupIndex;

    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    @Override
    public String init() {
        conversationStart();
        municipalitySearchBean.init();
        initInfoMessages();
        initErrorMessages();
        initErrorDetailMessages();
        initInvalidComponent();
        editDetailPopupInfo = new EditDetailPopupInfo();
        popupIndex = -1;

        return null;
    }

    public String init(EditBeanProperty editBeanProperty) {
        init();
        this.editBeanProperty = editBeanProperty;


        // 複製フラグ（複製の場合true）
        boolean copyFlg = false;

        GetSmsMeterTenantBaseInfoParameter parameter = new GetSmsMeterTenantBaseInfoParameter();
        parameter.setBean("GetSmsMeterTenantBaseInfoBean");

        // 編集の場合
        if (this.editBeanProperty.getUpdateProcessFlg()) {
            // 企業ID（所属企業）
            parameter.setCorpId(this.editBeanProperty.getDivisionCorpId());
            // 建物ID（所属建物）
            parameter.setBuildingId(this.editBeanProperty.getDivisionBuildingId());

            // テナント企業ID
            parameter.setTenantCorpId(this.editBeanProperty.getCorpId());
            // テナント建物ID
            parameter.setTenantBuildingId(this.editBeanProperty.getBuildingId());

        }
        // 新規・複製の場合
        else {
            // 複製
            if (this.editBeanProperty.getBuildingId() != null) {
                copyFlg = true;

                // 企業ID（所属企業）
                parameter.setCorpId(this.editBeanProperty.getDivisionCorpId());
                // 建物ID（所属建物）
                parameter.setBuildingId(this.editBeanProperty.getDivisionBuildingId());

                // テナント企業ID
                parameter.setTenantCorpId(this.editBeanProperty.getCorpId());
                // テナント建物ID
                parameter.setTenantBuildingId(this.editBeanProperty.getBuildingId());

            }
            // 新規
            else {
                // 企業ID（所属企業）
                parameter.setCorpId(this.editBeanProperty.getDivisionCorpId());
                // 建物ID（所属建物）
                parameter.setBuildingId(this.editBeanProperty.getDivisionBuildingId());
            }
        }

        // 取得API実行
        GetSmsMeterTenantBaseInfoResult result = selectApi(parameter);

        if (result != null) {
            // 建物情報
            editBeanProperty.setCorpId(result.getCorpId());
            editBeanProperty.setBuildingId(result.getBuildingId());

            // 編集の場合のみ、建物番号はを設定
            if (editBeanProperty.getUpdateProcessFlg()) {
                editBeanProperty.setBuildingNo(result.getBuildingNo());
            }
            editBeanProperty.setBuildingName(result.getBuildingName());
            editBeanProperty.setBuildingTansyukuName(result.getBuildingTansyukuName());
            editBeanProperty.setBuildingNameKana(result.getBuildingNameKana());
            editBeanProperty.setZipCd(result.getZipCd());
            editBeanProperty.setPrefectureCd(result.getPrefectureCd());
            editBeanProperty.setMunicipalityCd(result.getMunicipalityCd());
            editBeanProperty.setMunicipalityName(result.getMunicipalityName());
            editBeanProperty.setAddressBuilding(result.getAddressBuilding());
            editBeanProperty.setTelNo(result.getTelNo());
            editBeanProperty.setFaxNo(result.getFaxNo());
            editBeanProperty.setBiko(result.getBiko());

            if (Objects.equals(result.getDelFlg(), OsolConstants.FLG_ON)
                    || result.getBuildingDelDate() != null) {
                // DelFlg設定または、削除日設定有の場合、画面表示用削除チェックON
                editBeanProperty.setbDelFlg(true);
            } else {
                editBeanProperty.setbDelFlg(false);
            }
            // 公開フラグ
            if (Objects.equals(result.getPublicFlg(), OsolConstants.FLG_ON)) {
                editBeanProperty.setbPublicFlg(true);
            } else {
                editBeanProperty.setbPublicFlg(false);
            }
            editBeanProperty.setBuildingDelDate(result.getBuildingDelDate());


            // 利用機能を設定
            editBeanProperty.setTotalStartYm(
                    DateUtility.changeDateFormat(result.getTotalStartYm(), DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            editBeanProperty.setTotalEndYm(
                    DateUtility.changeDateFormat(result.getTotalEndYm(), DateUtility.DATE_FORMAT_YYYYMM_SLASH));

            // 推計利用フラグ
            if (result.getEstimateUse() == null) {
                editBeanProperty.setEstimateUse(OsolConstants.ESTIMATE_USE.NOT_USE.getVal());
            }
            else {
                editBeanProperty.setEstimateUse(result.getEstimateUse());
            }

            editBeanProperty.setEstimateId(result.getEstimateId());


            // 編集の場合のみ、ユーザーコードを設定
            if (editBeanProperty.getUpdateProcessFlg()) {
                editBeanProperty.setTenantId(Objects.toString(result.getTenantId(), null));
            }

            // メーターテナント情報
            editBeanProperty.setFixedName1(result.getFixedName1());
            editBeanProperty.setFixedPrice1(Objects.toString(result.getFixedPrice1(), null));
            editBeanProperty.setFixedName2(result.getFixedName2());
            editBeanProperty.setFixedPrice2(Objects.toString(result.getFixedPrice2(), null));
            editBeanProperty.setFixedName3(result.getFixedName3());
            editBeanProperty.setFixedPrice3(Objects.toString(result.getFixedPrice3(), null));
            editBeanProperty.setFixedName4(result.getFixedName4());
            editBeanProperty.setFixedPrice4(Objects.toString(result.getFixedPrice4(), null));
            editBeanProperty.setPriceMenuNo(result.getPriceMenuNo());
            editBeanProperty.setContractCapacity(result.getContractCapacity());
            editBeanProperty.setDivRate1(Objects.toString(result.getDivRate1(), null));
            editBeanProperty.setDivRate2(Objects.toString(result.getDivRate2(), null));
            editBeanProperty.setDivRate3(Objects.toString(result.getDivRate3(), null));
            editBeanProperty.setDivRate4(Objects.toString(result.getDivRate4(), null));
            editBeanProperty.setDivRate5(Objects.toString(result.getDivRate5(), null));
            editBeanProperty.setDivRate6(Objects.toString(result.getDivRate6(), null));
            editBeanProperty.setDivRate7(Objects.toString(result.getDivRate7(), null));
            editBeanProperty.setDivRate8(Objects.toString(result.getDivRate8(), null));
            editBeanProperty.setDivRate9(Objects.toString(result.getDivRate9(), null));
            editBeanProperty.setDivRate10(Objects.toString(result.getDivRate10(), null));

            // メーター種別リスト詰め替え（詳細画面用）
            List<EditDetailPopupInfo> EditDetailPopupInfoList = new ArrayList<>();
            for (MeterRangeTenantPriceInfoResultData target : result.getMeterTypeList()) {
                EditDetailPopupInfo info = new EditDetailPopupInfo();
                info.setMeterType(target.getMeterType());
                info.setMenuNo(target.getMenuNo());
                info.setMeterTypeName(target.getMeterTypeName());
                info.setUnitUsageBased(target.getUnitUsageBased());
                info.setPricePlanId(target.getPricePlanId());
                info.setBasicPrice(Objects.toString(target.getBasicPrice(), null));
                info.setDiscountRate(Objects.toString(target.getDiscountRate(), null));

                List<RangeUnitPriceData> rangeUnitPriceDataList = new ArrayList<>();

                // メーター種別ごとの従量値と料金単価設定
                if (target.getRangeUnitPriceResultDataList() != null) {
                    for (RangeUnitPriceResultData rangeUnitPrice : target.getRangeUnitPriceResultDataList()) {
                        RangeUnitPriceData rangeUnitPriceData = new RangeUnitPriceData();
                        rangeUnitPriceData.setRangeValue(rangeUnitPrice.getRangeValue());
                        rangeUnitPriceData.setUnitPrice(Objects.toString(rangeUnitPrice.getUnitPrice(), null));
                        rangeUnitPriceDataList.add(rangeUnitPriceData);
                    }
                    info.setRangeUnitPriceDataList(rangeUnitPriceDataList);
                }

                // 料金メニュー（従量電灯A）
                if (target.getPriceMenuLightaResultData() != null) {
                    info.setLowestPrice(Objects.toString(target.getPriceMenuLightaResultData().getLowestPrice(), null));
                    info.setFuelAdjustPrice(Objects.toString(target.getPriceMenuLightaResultData().getFuelAdjustPrice(), null));
                    info.setAdjustPriceOver15(Objects.toString(target.getPriceMenuLightaResultData().getAdjustPriceOver15(), null));
                    info.setRenewEnerPrice(Objects.toString(target.getPriceMenuLightaResultData().getRenewEnerPrice(), null));
                    info.setRenewPriceOver15(Objects.toString(target.getPriceMenuLightaResultData().getRenewPriceOver15(), null));
                }

                // 料金メニュー（従量電灯B）
                if (target.getPriceMenuLightbResultData() != null) {
                    info.setBasicPrice(Objects.toString(target.getPriceMenuLightbResultData().getBasicPrice(), null));
                    info.setFuelAdjustPrice(Objects.toString(target.getPriceMenuLightbResultData().getFuelAdjustPrice(), null));
                    info.setRenewEnerPrice(Objects.toString(target.getPriceMenuLightbResultData().getRenewEnerPrice(), null));
                }

                EditDetailPopupInfoList.add(info);
            }
            editBeanProperty.setEditDetailPopupInfoList(EditDetailPopupInfoList);

            // グループ名称を作成および取得（デフォルト名：グループ1～グループ10）
            Map<Long, MeterGroupNameResultData> meterGroupNameMap = new LinkedHashMap<>();
            for (int i = 1; i <= 10; i++) {
                MeterGroupNameResultData meterGroupNameResultData = new MeterGroupNameResultData();
                meterGroupNameResultData.setMeterGroupId(new Long(i));
                meterGroupNameResultData.setMeterGroupName("グループ" + i);

                meterGroupNameMap.put(meterGroupNameResultData.getMeterGroupId(), meterGroupNameResultData);
            }
            // 対象グループが存在したら、取得データに置き換え
            result.getMeterGroupNameList().stream()
                    .filter(target -> meterGroupNameMap.containsKey(target.getMeterGroupId()))
                    .forEach(target -> meterGroupNameMap.put(target.getMeterGroupId(), target));

            editBeanProperty.setMeterGroupNameList(new ArrayList<>(meterGroupNameMap.values()));

            // 登録計器（メーター）IDを表示用テキストに置き換え
            if (result.getBuildDevMeterList() != null && !result.getBuildDevMeterList().isEmpty() && !copyFlg) {

                int valueCnt = 0;
                for (BuildDevMeterResultData buildDevMeter : result.getBuildDevMeterList()) {
                    if (!CheckUtility.isNullOrEmpty(buildDevMeter.getMeterId())) {
                        // １行あたりの表示数に到達したら改行コードを加える
                        if (valueCnt >= METER_ID_COLUMN) {
                            editBeanProperty.setMeterListStr(
                                    editBeanProperty.getMeterListStr().substring(0, editBeanProperty.getMeterListStr().length() - 1) + System.getProperty("line.separator"));
                            valueCnt = 0;
                        }
                        editBeanProperty
                        .setMeterListStr(editBeanProperty.getMeterListStr() + buildDevMeter.getMeterId() + "、");
                        valueCnt++;
                    }

                }

                if (!CheckUtility.isNullOrEmpty(editBeanProperty.getMeterListStr())) {
                    editBeanProperty.setMeterListStr(
                            editBeanProperty.getMeterListStr().substring(0, editBeanProperty.getMeterListStr().length() - 1));
                }
            }
        }

        // 新規登録の場合、所属建物の情報を一部設定しておく
        if (!this.editBeanProperty.getUpdateProcessFlg() && !copyFlg) {
            // 所属建物情報を取得しておく
            TBuilding divisionBuildingEntity = meterTenantDao.getBuilding(this.editBeanProperty.getDivisionCorpId(), this.editBeanProperty.getDivisionBuildingId());
            if (divisionBuildingEntity != null) {
                editBeanProperty.setZipCd(divisionBuildingEntity.getZipCd());
                editBeanProperty.setPrefectureCd(divisionBuildingEntity.getMPrefecture().getPrefectureCd());
                if (divisionBuildingEntity.getMMunicipality() != null) {
                    editBeanProperty.setMunicipalityCd(divisionBuildingEntity.getMMunicipality().getMunicipalityCd());
                }
                editBeanProperty.setMunicipalityName(divisionBuildingEntity.getAddress());
            }

            // 公開フラグのデフォルトを公開するに設定
            editBeanProperty.setbPublicFlg(true); // 公開する
        }

        return "meterTenantEdit";
    }

    /**
     * 建物基本情報登録・更新処理
     *
     * @return
     * @throws Exception
     */
    @Logged
    public String updateTenantInfo() throws Exception {
        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:updateTenantInfo():START"));

        // 画面メッセージ初期化
        initInfoMessages();
        initErrorMessages();
        initInvalidComponent();

        // 入力値チェック
        boolean ret = chkInput();
        if (ret) {
            // リクエスト生成
            UpdateSmsMeterTenantBaseInfoRequest req = createRequest();

            // API実行
            UpdateSmsMeterTenantBaseInfoResult result = updateApi(req);

            // 最新値を画面項目に設定
            if (result != null) {
                // 建物情報
                editBeanProperty.setCorpId(result.getCorpId());
                editBeanProperty.setBuildingId(result.getBuildingId());
                editBeanProperty.setBuildingNo(result.getBuildingNo());
                editBeanProperty.setBuildingName(result.getBuildingName());
                editBeanProperty.setBuildingTansyukuName(result.getBuildingTansyukuName());
                editBeanProperty.setBuildingNameKana(result.getBuildingNameKana());
                editBeanProperty.setZipCd(result.getZipCd());
                editBeanProperty.setPrefectureCd(result.getPrefectureCd());
                editBeanProperty.setMunicipalityCd(result.getMunicipalityCd());
                editBeanProperty.setMunicipalityName(result.getMunicipalityName());
                editBeanProperty.setAddressBuilding(result.getAddressBuilding());
                editBeanProperty.setTelNo(result.getTelNo());
                editBeanProperty.setFaxNo(result.getFaxNo());
                editBeanProperty.setBiko(result.getBiko());

                if (Objects.equals(result.getDelFlg(), OsolConstants.FLG_ON)
                        || result.getBuildingDelDate() != null) {
                    // DelFlg設定または、削除日設定有の場合、画面表示用削除チェックON
                    editBeanProperty.setbDelFlg(true);
                } else {
                    editBeanProperty.setbDelFlg(false);
                }
                // 公開フラグ
                if (Objects.equals(result.getPublicFlg(), OsolConstants.FLG_ON)) {
                    editBeanProperty.setbPublicFlg(true);
                } else {
                    editBeanProperty.setbPublicFlg(false);
                }
                editBeanProperty.setBuildingDelDate(result.getBuildingDelDate());


                // 利用機能を設定
                editBeanProperty.setTotalStartYm(
                        DateUtility.changeDateFormat(result.getTotalStartYm(), DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                editBeanProperty.setTotalEndYm(
                        DateUtility.changeDateFormat(result.getTotalEndYm(), DateUtility.DATE_FORMAT_YYYYMM_SLASH));

                // 推計利用フラグ
                if (result.getEstimateUse() == null) {
                    editBeanProperty.setEstimateUse(OsolConstants.ESTIMATE_USE.NOT_USE.getVal());
                }
                else {
                    editBeanProperty.setEstimateUse(result.getEstimateUse());
                }

                editBeanProperty.setEstimateId(result.getEstimateId());

                // メーターテナント情報
                editBeanProperty.setTenantId(Objects.toString(result.getTenantId(), null));
                editBeanProperty.setFixedName1(result.getFixedName1());
                editBeanProperty.setFixedPrice1(Objects.toString(result.getFixedPrice1(), null));
                editBeanProperty.setFixedName2(result.getFixedName2());
                editBeanProperty.setFixedPrice2(Objects.toString(result.getFixedPrice2(), null));
                editBeanProperty.setFixedName3(result.getFixedName3());
                editBeanProperty.setFixedPrice3(Objects.toString(result.getFixedPrice3(), null));
                editBeanProperty.setFixedName4(result.getFixedName4());
                editBeanProperty.setFixedPrice4(Objects.toString(result.getFixedPrice4(), null));
                editBeanProperty.setPriceMenuNo(result.getPriceMenuNo());
                editBeanProperty.setContractCapacity(result.getContractCapacity());
                editBeanProperty.setDivRate1(Objects.toString(result.getDivRate1(), null));
                editBeanProperty.setDivRate2(Objects.toString(result.getDivRate2(), null));
                editBeanProperty.setDivRate3(Objects.toString(result.getDivRate3(), null));
                editBeanProperty.setDivRate4(Objects.toString(result.getDivRate4(), null));
                editBeanProperty.setDivRate5(Objects.toString(result.getDivRate5(), null));
                editBeanProperty.setDivRate6(Objects.toString(result.getDivRate6(), null));
                editBeanProperty.setDivRate7(Objects.toString(result.getDivRate7(), null));
                editBeanProperty.setDivRate8(Objects.toString(result.getDivRate8(), null));
                editBeanProperty.setDivRate9(Objects.toString(result.getDivRate9(), null));
                editBeanProperty.setDivRate10(Objects.toString(result.getDivRate10(), null));


                // メーター種別リスト詰め替え（詳細画面用）
                List<EditDetailPopupInfo> EditDetailPopupInfoList = new ArrayList<>();
                for (MeterRangeTenantPriceInfoResultData target : result.getMeterTypeList()) {
                    EditDetailPopupInfo info = new EditDetailPopupInfo();
                    info.setMeterType(target.getMeterType());
                    info.setMenuNo(target.getMenuNo());
                    info.setMeterTypeName(target.getMeterTypeName());
                    info.setUnitUsageBased(target.getUnitUsageBased());
                    info.setPricePlanId(target.getPricePlanId());
                    info.setBasicPrice(Objects.toString(target.getBasicPrice(), null));
                    info.setDiscountRate(Objects.toString(target.getDiscountRate(), null));

                    List<RangeUnitPriceData> rangeUnitPriceDataList = new ArrayList<>();

                    // メーター種別ごとの従量値と料金単価設定
                    if (target.getRangeUnitPriceResultDataList() != null) {
                        for (RangeUnitPriceResultData rangeUnitPrice : target.getRangeUnitPriceResultDataList()) {
                            RangeUnitPriceData rangeUnitPriceData = new RangeUnitPriceData();
                            rangeUnitPriceData.setRangeValue(rangeUnitPrice.getRangeValue());
                            rangeUnitPriceData.setUnitPrice(Objects.toString(rangeUnitPrice.getUnitPrice(), null));
                            rangeUnitPriceDataList.add(rangeUnitPriceData);
                        }
                        info.setRangeUnitPriceDataList(rangeUnitPriceDataList);
                    }

                    // 料金メニュー（従量電灯A）
                    if (target.getPriceMenuLightaResultData() != null) {
                        info.setLowestPrice(Objects.toString(target.getPriceMenuLightaResultData().getLowestPrice(), null));
                        info.setFuelAdjustPrice(Objects.toString(target.getPriceMenuLightaResultData().getFuelAdjustPrice(), null));
                        info.setAdjustPriceOver15(Objects.toString(target.getPriceMenuLightaResultData().getAdjustPriceOver15(), null));
                        info.setRenewEnerPrice(Objects.toString(target.getPriceMenuLightaResultData().getRenewEnerPrice(), null));
                        info.setRenewPriceOver15(Objects.toString(target.getPriceMenuLightaResultData().getRenewPriceOver15(), null));
                    }

                    // 料金メニュー（従量電灯B）
                    if (target.getPriceMenuLightbResultData() != null) {
                        info.setBasicPrice(Objects.toString(target.getPriceMenuLightbResultData().getBasicPrice(), null));
                        info.setFuelAdjustPrice(Objects.toString(target.getPriceMenuLightbResultData().getFuelAdjustPrice(), null));
                        info.setRenewEnerPrice(Objects.toString(target.getPriceMenuLightbResultData().getRenewEnerPrice(), null));
                    }

                    EditDetailPopupInfoList.add(info);
                }
                editBeanProperty.setEditDetailPopupInfoList(EditDetailPopupInfoList);

                // グループ名称を作成および取得（デフォルト名：グループ1～グループ10）
                Map<Long, MeterGroupNameResultData> meterGroupNameMap = new LinkedHashMap<>();
                for (int i = 1; i <= 10; i++) {
                    MeterGroupNameResultData meterGroupNameResultData = new MeterGroupNameResultData();
                    meterGroupNameResultData.setMeterGroupId(new Long(i));
                    meterGroupNameResultData.setMeterGroupName("グループ" + i);

                    meterGroupNameMap.put(meterGroupNameResultData.getMeterGroupId(), meterGroupNameResultData);
                }
                // 対象グループが存在したら、取得データに置き換え
                result.getMeterGroupNameList().stream()
                        .filter(target -> meterGroupNameMap.containsKey(target.getMeterGroupId()))
                        .forEach(target -> meterGroupNameMap.put(target.getMeterGroupId(), target));

                editBeanProperty.setMeterGroupNameList(new ArrayList<>(meterGroupNameMap.values()));

                // 登録計器（メーター）ID表示用を初期化
                editBeanProperty.setMeterListStr(OsolConstants.STR_EMPTY);

                // 登録計器（メーター）IDを表示用テキストに置き換え
                if (result.getBuildDevMeterList() != null && !result.getBuildDevMeterList().isEmpty()) {
                    int valueCnt = 0;
                    for (BuildDevMeterResultData buildDevMeter : result.getBuildDevMeterList()) {
                        if (!CheckUtility.isNullOrEmpty(buildDevMeter.getMeterId())) {
                            // １行あたりの表示数に到達したら改行コードを加える
                            if (valueCnt >= METER_ID_COLUMN) {
                                editBeanProperty.setMeterListStr(
                                        editBeanProperty.getMeterListStr().substring(0, editBeanProperty.getMeterListStr().length() - 1) + System.getProperty("line.separator"));
                                valueCnt = 0;
                            }
                            editBeanProperty
                            .setMeterListStr(editBeanProperty.getMeterListStr() + buildDevMeter.getMeterId() + "、");
                            valueCnt++;
                        }
                    }
                    if (!CheckUtility.isNullOrEmpty(editBeanProperty.getMeterListStr())) {
                        editBeanProperty.setMeterListStr(
                                editBeanProperty.getMeterListStr().substring(0, editBeanProperty.getMeterListStr().length() - 1));
                    }
                }

                // 更新フラグをtrue
                editBeanProperty.setUpdateProcessFlg(true);
            }
        } else {
            return "";
        }
        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:updateTenantInfo():END"));
        return tenantSearchBean.init();

    }

    /**
     * GetAPI実行
     */
    private GetSmsMeterTenantBaseInfoResult selectApi (GetSmsMeterTenantBaseInfoParameter parameter) {
        GetSmsMeterTenantBaseInfoResponse response = new GetSmsMeterTenantBaseInfoResponse();
        SmsApiGateway gateway = new SmsApiGateway();

        response = (GetSmsMeterTenantBaseInfoResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (response == null) {
            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.noData"));
            return null;
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessages(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE + response.getResultCode()));
            return null;
        } else if (response.getResult() == null) {
            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.noData"));
            return null;
        }

        return response.getResult();
    }

    /**
     * UpdateAPI実行
     */
    private UpdateSmsMeterTenantBaseInfoResult updateApi (UpdateSmsMeterTenantBaseInfoRequest req) {
        UpdateSmsMeterTenantBaseInfoResponse response = new UpdateSmsMeterTenantBaseInfoResponse();
        UpdateSmsMeterTenantBaseInfoParameter parameter = new UpdateSmsMeterTenantBaseInfoParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSmsMeterTenantBaseInfoBean");
        parameter.setRequestSet(req);

        response = (UpdateSmsMeterTenantBaseInfoResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (response == null) {
            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.noData"));
            return null;
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
             addErrorMessages(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE + response.getResultCode()));
            return null;
        } else if (response.getResult() == null) {
            addErrorMessages(
                    beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.registered",
                            new String[] {OsolConstants.BUILDING_TENANT.TENANT.getName() + OsolConstants.LABEL_NUMBER}));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
            return null;
        }

        // 更新と新規/複製でメッセージを変更
        if (editBeanProperty.getUpdateProcessFlg()) {
            addInfoMessages(beanMessages.getMessage("osol.info.UpdateSuccess"));
        }
        else {
            addInfoMessages(beanMessages.getMessage("osol.info.RegisterSuccess"));
        }

        return response.getResult();
    }

    /**
     * Update用リクエスト生成
     */
    private UpdateSmsMeterTenantBaseInfoRequest createRequest () {

        // バリデーションチェックもこの辺で

        UpdateSmsMeterTenantBaseInfoRequest req= new UpdateSmsMeterTenantBaseInfoRequest();
        req.setUpdateProcessFlg(editBeanProperty.getUpdateProcessFlg());
        req.setCorpId(editBeanProperty.getCorpId());
        req.setBuildingId(editBeanProperty.getBuildingId());
        req.setBuildingNo(editBeanProperty.getBuildingNo());
//        req.setBuildingNo(String.format("%6s", editBeanProperty.getBuildingNo()).replace(OsolConstants.STR_HALF_SPACE, "0"));
        req.setBuildingName(editBeanProperty.getBuildingName());
        req.setBuildingNameKana(editBeanProperty.getBuildingNameKana());
        req.setBuildingTansyukuName(editBeanProperty.getBuildingTansyukuName());
        req.setMunicipalityCd(editBeanProperty.getMunicipalityCd());
        req.setPrefectureCd(editBeanProperty.getPrefectureCd());
        req.setMunicipalityName(editBeanProperty.getMunicipalityName());
        req.setAddressBuilding(editBeanProperty.getAddressBuilding());
        req.setZipCd(editBeanProperty.getZipCd());
        req.setTelNo(editBeanProperty.getTelNo());
        req.setFaxNo(editBeanProperty.getFaxNo());
        req.setBiko(editBeanProperty.getBiko());
        req.setTotalStartYm(editBeanProperty.getTotalStartYm());
        req.setTotalEndYm(editBeanProperty.getTotalEndYm());
        req.setEstimateUse(editBeanProperty.getEstimateUse());
        req.setEstimateId(editBeanProperty.getEstimateId());

        if (editBeanProperty.getbPublicFlg()) {
            req.setPublicFlg(OsolConstants.FLG_ON);
        }
        else {
            req.setPublicFlg(OsolConstants.FLG_OFF);
        }

        if (editBeanProperty.getbDelFlg()) {
            req.setDelFlg(OsolConstants.FLG_ON);
        }
        else {
            req.setDelFlg(OsolConstants.FLG_OFF);
        }

        req.setDivisionCorpId(editBeanProperty.getDivisionCorpId());
        req.setDivisionBuildingId(editBeanProperty.getDivisionBuildingId());

        // メーターテナント関連
        req.setTenantId(Long.valueOf(editBeanProperty.getTenantId()));
        req.setFixedName1(editBeanProperty.getFixedName1());
        req.setFixedPrice1(editBeanProperty.getFixedPrice1() != null ? new BigDecimal(editBeanProperty.getFixedPrice1()) : null);
        req.setFixedName2(editBeanProperty.getFixedName2());
        req.setFixedPrice2(editBeanProperty.getFixedPrice2() != null ? new BigDecimal(editBeanProperty.getFixedPrice2()) : null);
        req.setFixedName3(editBeanProperty.getFixedName3());
        req.setFixedPrice3(editBeanProperty.getFixedPrice3() != null ? new BigDecimal(editBeanProperty.getFixedPrice3()) : null);
        req.setFixedName4(editBeanProperty.getFixedName4());
        req.setFixedPrice4(editBeanProperty.getFixedPrice4() != null ? new BigDecimal(editBeanProperty.getFixedPrice4()) : null);
        req.setPriceMenuNo(editBeanProperty.getPriceMenuNo());
        req.setContractCapacity(editBeanProperty.getContractCapacity());
        req.setDivRate1(editBeanProperty.getDivRate1() != null ? new BigDecimal(editBeanProperty.getDivRate1()) : null);
        req.setDivRate2(editBeanProperty.getDivRate2() != null ? new BigDecimal(editBeanProperty.getDivRate2()) : null);
        req.setDivRate3(editBeanProperty.getDivRate3() != null ? new BigDecimal(editBeanProperty.getDivRate3()) : null);
        req.setDivRate4(editBeanProperty.getDivRate4() != null ? new BigDecimal(editBeanProperty.getDivRate4()) : null);
        req.setDivRate5(editBeanProperty.getDivRate5() != null ? new BigDecimal(editBeanProperty.getDivRate5()) : null);
        req.setDivRate6(editBeanProperty.getDivRate6() != null ? new BigDecimal(editBeanProperty.getDivRate6()) : null);
        req.setDivRate7(editBeanProperty.getDivRate7() != null ? new BigDecimal(editBeanProperty.getDivRate7()) : null);
        req.setDivRate8(editBeanProperty.getDivRate8() != null ? new BigDecimal(editBeanProperty.getDivRate8()) : null);
        req.setDivRate9(editBeanProperty.getDivRate9() != null ? new BigDecimal(editBeanProperty.getDivRate9()) : null);
        req.setDivRate10(editBeanProperty.getDivRate10() != null ? new BigDecimal(editBeanProperty.getDivRate10()) : null);

        List<UpdateSmsMeterTenantBaseInfoRequestSet> reqMeterTypeDetailList = new ArrayList<>();

        for (EditDetailPopupInfo info : editBeanProperty.getEditDetailPopupInfoList()) {
            UpdateSmsMeterTenantBaseInfoRequestSet reqSet = new UpdateSmsMeterTenantBaseInfoRequestSet();
            reqSet.setMeterType(info.getMeterType());
            reqSet.setMenuNo(info.getMenuNo());
            reqSet.setDiscountRate(info.getDiscountRate() != null ? new BigDecimal(info.getDiscountRate()) : null);

            reqSet.setBulkFlg(info.isBulkFlg());

            // 従量料金単価リストの詰め替え
            List<RangeUnitPriceResultData> reqRangeUnitPriceList = new ArrayList<>();
            if (info.getRangeUnitPriceDataList() != null) {
                for (RangeUnitPriceData target : info.getRangeUnitPriceDataList()) {
                    RangeUnitPriceResultData reqRangeUnitPrice = new RangeUnitPriceResultData();
                    reqRangeUnitPrice.setRangeValue(target.getRangeValue());
                    reqRangeUnitPrice.setUnitPrice(target.getUnitPrice() != null ? new BigDecimal(target.getUnitPrice()) : null);
                    reqRangeUnitPriceList.add(reqRangeUnitPrice);
                }
                reqSet.setRangeUnitPriceDataList(reqRangeUnitPriceList);
            }

            // 基本料金（その他）
            if (info.getMenuNo() == 0) {
                reqSet.setBasicPrice(info.getBasicPrice() != null ? new BigDecimal(info.getBasicPrice()) : null);
            }

            // 料金メニュー（従量電灯A）
            if (info.getMenuNo() == 1) {
                PriceMenuLightaResultData lighta = new PriceMenuLightaResultData();
                lighta.setLowestPrice(info.getLowestPrice() != null ? new BigDecimal(info.getLowestPrice()) : null);
                lighta.setFuelAdjustPrice(info.getFuelAdjustPrice() != null ? new BigDecimal(info.getFuelAdjustPrice()) : null);
                lighta.setAdjustPriceOver15(info.getAdjustPriceOver15() != null ? new BigDecimal(info.getAdjustPriceOver15()) : null);
                lighta.setRenewEnerPrice(info.getRenewEnerPrice() != null ? new BigDecimal(info.getRenewEnerPrice()) : null);
                lighta.setRenewPriceOver15(info.getRenewPriceOver15() != null ? new BigDecimal(info.getRenewPriceOver15()) : null);
                reqSet.setPriceMenuLightaData(lighta);
            }

            // 料金メニュー（従量電灯B）
            if (info.getMenuNo() == 2) {
                PriceMenuLightbResultData lightb = new PriceMenuLightbResultData();
                lightb.setBasicPrice(info.getBasicPrice() != null ? new BigDecimal(info.getBasicPrice()) : null);
                lightb.setFuelAdjustPrice(info.getFuelAdjustPrice() != null ? new BigDecimal(info.getFuelAdjustPrice()) : null);
                lightb.setRenewEnerPrice(info.getRenewEnerPrice() != null ? new BigDecimal(info.getRenewEnerPrice()) : null);
                reqSet.setPriceMenuLightbData(lightb);
            }
            reqMeterTypeDetailList.add(reqSet);
        }
        req.setMeterTypeDetailList(reqMeterTypeDetailList);

        return req;
    }

    /**
     * 詳細設定ボタン
     *
     * @param index
     * @throws java.lang.CloneNotSupportedException
     */
    @Logged
    public void detailRowBtnAction(int index) throws CloneNotSupportedException {
        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:detailRowBtnAction():START"));

        if (index < 0) {
            popupIndex = -1;
            return;
        }

        // 初期化
        editDetailPopupInfo = new EditDetailPopupInfo();
        EditDetailPopupInfo info = this.editBeanProperty.getEditDetailPopupInfoList().get(index);
        initErrorDetailMessages();
        invalidComponent.clear();
        editDetailPopupCompFlg = false;
        if (info != null) {

            setEditDetailPopupInfo(info.clone());

            if (info.getRangeUnitPriceDataList() != null) {
                List<RangeUnitPriceData> rangeUnitPriceDataList = new ArrayList<>();
                for (RangeUnitPriceData target : info.getRangeUnitPriceDataList()) {
                    rangeUnitPriceDataList.add(target.clone());
                }
                getEditDetailPopupInfo().setRangeUnitPriceDataList(rangeUnitPriceDataList);
            }

            popupIndex = index;
        }

        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:detailRowBtnAction():END"));
    }

    /**
     * 詳細設定ポップアップ確定ボタン
     *
     * @param index
     * @throws java.lang.CloneNotSupportedException
     */
    @Logged
    public void submitPopupBtnAction() throws CloneNotSupportedException {
        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:submitPopupBtnAction():START"));

        EditDetailPopupInfo info = editBeanProperty.getEditDetailPopupInfoList().get(popupIndex);
        if (info != null) {

            // 入力値チェック
            boolean ret = chkPopupInput(editDetailPopupInfo);
            if (ret) {
                // 詳細設定画面項目を設定
                editBeanProperty.getEditDetailPopupInfoList().set(popupIndex, editDetailPopupInfo);
                // 初期化
                editDetailPopupInfo = new EditDetailPopupInfo();
                // チェックOK
                editDetailPopupCompFlg = true;
            }
            else {
                // チェックNG
                editDetailPopupCompFlg = false;
            }


        }
        eventLogger.debug(
                EditBean.class.getPackage().getName().concat(" smsCollectSettingMeterTenantEditBean:submitPopupBtn()Action:END"));
    }

    // 入力チェック
    private boolean chkInput () {
        boolean ret = true;
        initInfoMessages();
        initErrorMessages();
        invalidComponent.clear();

        List<String[]> param = new ArrayList<>();

        // 新規登録の場合、ユーザーコードとテナント番号のチェックを行う（既に登録済メッセージ）
        if (!editBeanProperty.getUpdateProcessFlg()) {
            // テナント番号入力チェック
            if (CheckUtility.isNullOrEmpty(editBeanProperty.getBuildingNo())) {
                addErrorMessages(OsolConstants.BUILDING_TENANT.TENANT.getName()
                        + OsolConstants.LABEL_NUMBER
                        + beanMessages.getMessage("osol.error.required"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
                ret = false;
            }
            // テナント番号の重複チェック（1件以上）
            else if (1 <= meterTenantDao.getMeterTenatNoCount(editBeanProperty.getDivisionCorpId(), editBeanProperty.getBuildingNo())) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.registered",
                        new String[] {OsolConstants.BUILDING_TENANT.TENANT.getName() + OsolConstants.LABEL_NUMBER}));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
                ret = false;
            }

            // ユーザーコード入力チェック
            if (CheckUtility.isNullOrEmpty(editBeanProperty.getTenantId())) {
                addErrorMessages(SmsConstants.SEARCH_CONDITION_TENANT_ID + beanMessages.getMessage("osol.error.required"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:userCode");
                ret = false;
            }
            // ユーザーコードの範囲チェック
            else if (!CheckUtility.checkIntegerRange(editBeanProperty.getTenantId(), 1, 999999)) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                        new String[] { "ユーザーコード", "1", "999999" }));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:userCode");
                ret = false;
            }
            // ユーザーコードの重複チェック（1件以上)
            else if (1 <= meterTenantDao.getMeterTenatIdCount(editBeanProperty.getDivisionCorpId(), editBeanProperty.getDivisionBuildingId(),
                    Long.valueOf(editBeanProperty.getTenantId()))) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.registered",
                        new String[] {SmsConstants.SEARCH_CONDITION_TENANT_ID}));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:userCode");
                ret = false;
            }
        }
        // 更新で削除状態解除にする場合、ユーザーコードとテナント番号のチェックを行う（削除状態を解除できませんメッセージ）
        else if (editBeanProperty.getUpdateProcessFlg() && editBeanProperty.getBuildingDelDate() != null && editBeanProperty.getbDelFlg() == false) {
            // テナント番号入力チェック
            if (CheckUtility.isNullOrEmpty(editBeanProperty.getBuildingNo())) {
                addErrorMessages(OsolConstants.BUILDING_TENANT.TENANT.getName()
                        + OsolConstants.LABEL_NUMBER
                        + beanMessages.getMessage("osol.error.required"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
                ret = false;
            }
            // テナント番号の重複チェック（1件以上）
            else if (1 <= meterTenantDao.getMeterTenatNoCount(editBeanProperty.getDivisionCorpId(), editBeanProperty.getBuildingNo())) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.duplicateTenantNoDelete",
                        new String[] {OsolConstants.BUILDING_TENANT.TENANT.getName() + OsolConstants.LABEL_NUMBER}));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
                ret = false;
            }

            // ユーザーコード入力チェック
            if (CheckUtility.isNullOrEmpty(editBeanProperty.getTenantId())) {
                addErrorMessages(SmsConstants.SEARCH_CONDITION_TENANT_ID + beanMessages.getMessage("osol.error.required"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:userCode");
                ret = false;
            }
            // ユーザーコードの重複チェック（1件以上)
            else if (1 <= meterTenantDao.getMeterTenatIdCount(editBeanProperty.getDivisionCorpId(), editBeanProperty.getDivisionBuildingId(),
                    Long.valueOf(editBeanProperty.getTenantId()))) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.duplicateTenantNoDelete",
                        new String[] {SmsConstants.SEARCH_CONDITION_TENANT_ID}));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:userCode");
                ret = false;
            }
        }

        // テナント名の入力有無チェック
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getBuildingName())) {
            addErrorMessages(OsolConstants.BUILDING_TENANT.TENANT.getName()
                    + OsolConstants.LABEL_NAME
                    + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingName");
            ret = false;
        }

        // 建物短縮名称の入力有無チェック
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getBuildingTansyukuName())) {
            addErrorMessages(beanMessages.getMessage("osol.name.buildingTansyukuName")
                    + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingTansyukuName");
            ret = false;
        }

        // ふりがなの文字チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getBuildingNameKana())
                && !CheckUtility.checkFurigana(editBeanProperty.getBuildingNameKana())) {
            addErrorMessages(beanMessages.getMessage("osol.error.furiganaValidation"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNameKana");
            ret = false;
        }

        // 郵便番号の文字チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getZipCd())
                && !CheckUtility.checkZipCode(editBeanProperty.getZipCd())) {
            addErrorMessages(beanMessages.getMessage("osol.error.zipValueValidation"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:zipCd");
            ret = false;
        }

        // 自治体コードの有無チェック(推計機能：推計するの場合)
        if (OsolConstants.ESTIMATE_USE.USE.getVal() .equals(editBeanProperty.getEstimateUse())) {
            if (editBeanProperty.getMunicipalityCd() == null) {
                addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.notInputMunicipalityCdBySuikei"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:municipalityCd");
                ret = false;
            }
        }

        // 自治体コードの桁チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getMunicipalityCd())
                && !CheckUtility.checkMunicipalityCode(editBeanProperty.getMunicipalityCd())) {
            addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.municipalCodeLength"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:municipalityCd");
            ret = false;
        } else if (!CheckUtility.isNullOrEmpty(editBeanProperty.getMunicipalityCd())) {
            // 自治体コードの存在有無チェック
            MMunicipality mMunicipality = mMunicipalityDao.find(editBeanProperty.getMunicipalityCd());
            if (mMunicipality == null) {
                addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.undefinedMunicipalityCd"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:municipalityCd");
                ret = false;
            }
        }

        // 市区町村番地の入力有無チェック
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getMunicipalityName())) {
            addErrorMessages(beanMessages.getMessage("osol.name.municipalityName")
                    + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:municipalityName");
            ret = false;
        }

        // 都道府県コードの入力有無チェック
        if (editBeanProperty.getPrefectureCd() == null
                || editBeanProperty.getPrefectureCd().equals(OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE)) {
            addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.notSelectedPrefectureCd"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:prefectureCd");
            ret = false;
        }

        // 集計開始年月の入力有無チェック
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getTotalStartYm())) {
            addErrorMessages(
                    beanMessages.getMessage("osol.name.totalStartYm") + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:totalStartYm");
            ret = false;

            // 集計開始年月の日付チェック
        } else if (!DateUtility.checkRegDateYmSlash(editBeanProperty.getTotalStartYm())) {
            addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.totalStartYmValidation"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:totalStartYm");
            ret = false;
        }

        // 集計終了年月の日付チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getTotalEndYm())
                && !DateUtility.checkRegDateYmSlash(editBeanProperty.getTotalEndYm())) {
            addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.totalEndYmValidation"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:totalEndYm");
            ret = false;
        }

        // 集計(開始･終了)年月の関係チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getTotalStartYm())
                && !CheckUtility.isNullOrEmpty(editBeanProperty.getTotalEndYm())
                && DateUtility.checkRegDateYmSlash(editBeanProperty.getTotalStartYm())
                && DateUtility.checkRegDateYmSlash(editBeanProperty.getTotalEndYm())) {
            // 両項目入力時は集計開始年月 <= 集計終了年月であること
            Date strStartDate = DateUtility.conversionDate(editBeanProperty.getTotalStartYm(),
                    DateUtility.DATE_FORMAT_YYYYMM_SLASH);
            Date strEndDate = DateUtility.conversionDate(editBeanProperty.getTotalEndYm(),
                    DateUtility.DATE_FORMAT_YYYYMM_SLASH);
            eventLogger.debug(EditBean.class.getPackage().getName()
                    .concat(" strStartDate:" + strStartDate.toString() + " / strEndDate:" + strEndDate.toString()));

            int diff = strEndDate.compareTo(strStartDate);
            eventLogger.debug(EditBean.class.getPackage().getName().concat(" diff(" + diff + ")"));
            if (diff < 0) {
                eventLogger.debug(EditBean.class.getPackage().getName()
                        .concat(":: checkInputParam() error[totalStartYm and totalEndYm]"));
                addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.illegalRangeTotalEndYm"));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:totalStartYm");
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:totalEndYm");

                ret = false;
            }
        }

        // 固定費１金額の入力チェック
        if (editBeanProperty.getFixedPrice1() != null && !CheckUtility.checkIntegerRange(editBeanProperty.getFixedPrice1(), 1, 999999)) {
            addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                    new String[] { "固定費１(金額)", "1", "999999" }));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:price1");
            ret = false;
        }

        // 固定費２金額の入力チェック
        if (editBeanProperty.getFixedPrice2() != null && !CheckUtility.checkIntegerRange(editBeanProperty.getFixedPrice2(), 1, 999999)) {
            addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                    new String[] { "固定費２(金額)", "1", "999999" }));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:price2");
            ret = false;
        }

        // 固定費３金額の入力チェック
        if (editBeanProperty.getFixedPrice3() != null && !CheckUtility.checkIntegerRange(editBeanProperty.getFixedPrice3(), 1, 999999)) {
            addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                    new String[] { "固定費３(金額)", "1", "999999" }));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:price3");
            ret = false;
        }

        // 固定費４金額の入力チェック
        if (editBeanProperty.getFixedPrice4() != null && !CheckUtility.checkIntegerRange(editBeanProperty.getFixedPrice4(), 1, 999999)) {
            addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                    new String[] { "固定費４(金額)", "1", "999999" }));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:price4");
            ret = false;
        }

        // 按分率１
        if (editBeanProperty.getDivRate1() != null && editBeanProperty.getMeterGroupNameList().get(0) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(0).getMeterGroupName(), editBeanProperty.getDivRate1(),
                    "group_rate_0", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率２
        if (editBeanProperty.getDivRate2() != null && editBeanProperty.getMeterGroupNameList().get(1) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(1).getMeterGroupName(), editBeanProperty.getDivRate2(),
                    "group_rate_1", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率３
        if (editBeanProperty.getDivRate3() != null && editBeanProperty.getMeterGroupNameList().get(2) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(2).getMeterGroupName(), editBeanProperty.getDivRate3(),
                    "group_rate_2", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率４
        if (editBeanProperty.getDivRate4() != null && editBeanProperty.getMeterGroupNameList().get(3) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(3).getMeterGroupName(), editBeanProperty.getDivRate4(),
                    "group_rate_3", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率５
        if (editBeanProperty.getDivRate5() != null && editBeanProperty.getMeterGroupNameList().get(4) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(4).getMeterGroupName(), editBeanProperty.getDivRate5(),
                    "group_rate_4", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率６
        if (editBeanProperty.getDivRate6() != null && editBeanProperty.getMeterGroupNameList().get(5) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(5).getMeterGroupName(), editBeanProperty.getDivRate6(),
                    "group_rate_5", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率７
        if (editBeanProperty.getDivRate7() != null && editBeanProperty.getMeterGroupNameList().get(6) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(6).getMeterGroupName(), editBeanProperty.getDivRate7(),
                    "group_rate_6", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率８
        if (editBeanProperty.getDivRate8() != null && editBeanProperty.getMeterGroupNameList().get(7) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(7).getMeterGroupName(), editBeanProperty.getDivRate8(),
                    "group_rate_7", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率９
        if (editBeanProperty.getDivRate9() != null && editBeanProperty.getMeterGroupNameList().get(8) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(8).getMeterGroupName(), editBeanProperty.getDivRate9(),
                    "group_rate_8", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 按分率１０
        if (editBeanProperty.getDivRate10() != null && editBeanProperty.getMeterGroupNameList().get(9) != null) {
            String[] _arr = { "グループ設定(%)の" + editBeanProperty.getMeterGroupNameList().get(9).getMeterGroupName(), editBeanProperty.getDivRate10(),
                    "group_rate_9", DIV_RATE_MIN, DIV_RATE_MAX };
            param.add(_arr);
        }

        // 備考の文字長チェック
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getBiko())
                && editBeanProperty.getBiko().length() > 500) {
            addErrorMessages(beanMessages.getMessage("buildingInfoEditBean.error.bikoValidation"));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:biko");
            ret = false;
        }

        for (String[] _arr : param) {
            if (!CheckUtility.checkIntegerRange(_arr[1], Integer.parseInt(_arr[3]), Integer.parseInt(_arr[4]))) {
                addErrorMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                                                               new String[] { _arr[0], _arr[3], _arr[4] }));
                invalidComponent.add("smsCollectSettingMeterTenantEditBean:" + _arr[2]);
                ret = false;
            }
        }

        return ret;
    }

    // 詳細設定入力チェック
    private boolean chkPopupInput (EditDetailPopupInfo info) {
        boolean ret = true;
        initErrorDetailMessages();
        invalidComponent.clear();

        List<String[]> param = new ArrayList<>();

        // 基本料金
        if (info.getBasicPrice() != null) {
            String[] _arr = { "基本料金 ", info.getBasicPrice(),
                    "basicPriceId", BASIC_PRICE_MIN, BASIC_PRICE_MAX };
            param.add(_arr);
        }

        // 最低料金
        if (info.getLowestPrice() != null) {
            String[] _arr = { "最低料金 ", info.getLowestPrice(),
                    "lowestPriceId", BASIC_PRICE_MIN, BASIC_PRICE_MAX };
            param.add(_arr);
        }

        // 従量料金
        if (info.getRangeUnitPriceDataList() != null) {
            int index = 0;
            long rangeValue = 0L;
            long minValue = -1;
            for (RangeUnitPriceData infoData : info.getRangeUnitPriceDataList()) {
                // 最初の従量値を一時保存しておく
                if (index == 0) {
                    minValue = infoData.getRangeValue().longValue();
                }

                if (infoData.getUnitPrice() != null) {
                    String[] _arr = { "従量料金(" + rangeValue + "～" + infoData.getRangeValue() + ")", infoData.getUnitPrice(),
                            "range_val_" + String.valueOf(index), BASIC_PRICE_MIN, BASIC_PRICE_MAX };
                    param.add(_arr);
                    rangeValue = infoData.getRangeValue().longValue();
                }
                index++;
            }

            // 燃料費調整額(最初の15kWhまで)
            if (info.getFuelAdjustPrice() != null && info.getMenuNo().longValue() == 1L) {
                String[] _arr = { "燃料費調整額(最初の" + minValue + "kWhまで)", info.getFuelAdjustPrice(),
                        "fuelAdjustPriceId", FUEL_ADJUST_PRICE_MIN, FUEL_ADJUST_PRICE_MAX };
                param.add(_arr);
            } else if (info.getFuelAdjustPrice() != null && info.getMenuNo().longValue() == 2L) {
                String[] _arr = { "燃料費調整額", info.getFuelAdjustPrice(),
                        "fuelAdjustPriceId", FUEL_ADJUST_PRICE_MIN, FUEL_ADJUST_PRICE_MAX };
                param.add(_arr);
            }

            // 燃料費調整額(15kWh超過)
            if (info.getAdjustPriceOver15() != null) {
                String[] _arr = { "燃料費調整額(" + minValue + "kWh超過)", info.getAdjustPriceOver15(),
                        "adjustPriceOver15Id", FUEL_ADJUST_PRICE_MIN, FUEL_ADJUST_PRICE_MAX };
                param.add(_arr);
            }

            //再生可能エネルギー発電促進賦課金(最初の15kWhまで)
            if (info.getRenewEnerPrice() != null && info.getMenuNo().longValue() == 1L) {
                String[] _arr = { "再生可能エネルギー発電促進賦課金(最初の" + minValue + "kWhまで)", info.getRenewEnerPrice(),
                        "renewEnerPriceId", BASIC_PRICE_MIN, BASIC_PRICE_MAX };
                param.add(_arr);
            } else if (info.getRenewEnerPrice() != null && info.getMenuNo().longValue() == 2L) {
                String[] _arr = { "再生可能エネルギー発電促進賦課金", info.getRenewEnerPrice(),
                        "renewEnerPriceId", BASIC_PRICE_MIN, BASIC_PRICE_MAX };
                param.add(_arr);
            }

            // 再生可能エネルギー発電促進賦課金(15kWh超過)
            if (info.getRenewPriceOver15() != null) {
                String[] _arr = { "再生可能エネルギー発電促進賦課金(" + minValue + "kWh超過)", info.getRenewPriceOver15(),
                        "renewPriceOver15Id", BASIC_PRICE_MIN, BASIC_PRICE_MAX };
                param.add(_arr);
            }
        }

        for (String[] _arr : param) {
            // 燃料調整費額チェック
            if ("fuelAdjustPriceId".equals(_arr[2]) || "adjustPriceOver15Id".equals(_arr[2])) {
                if (!CheckUtility.checkRegex(_arr[1], FUEL_ADJUST_PRICE_PATTERN)) {
                    ret = false;
                    addErrorDetailMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                                                                   new String[] { _arr[0], _arr[3], _arr[4] }));
                    invalidComponent.add("smsCollectSettingMeterTenantEditBean:" + _arr[2]);
                }
            }
            // その他
            else {
                if (!CheckUtility.checkRegex(_arr[1], BASIC_PRICE_PATTERN)) {
                    ret = false;
                    addErrorDetailMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                                                                   new String[] { _arr[0], _arr[3], _arr[4] }));
                    invalidComponent.add("smsCollectSettingMeterTenantEditBean:" + _arr[2]);
                }
            }
        }

        // 割引率
        if (info.getDiscountRate() != null && !CheckUtility.checkIntegerRange(info.getDiscountRate(), 0, 100)) {
            ret = false;
            addErrorDetailMessages(beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                                                           new String[] { "割引率", "0", "100" }));
            invalidComponent.add("smsCollectSettingMeterTenantEditBean:discountRateId");
        }

        return ret;
    }

    public EditBeanProperty getEditBeanProperty() {
        return editBeanProperty;
    }

    public void setEditBeanProperty(EditBeanProperty editBeanProperty) {
        this.editBeanProperty = editBeanProperty;
    }

    /**
     * 都道府県リストboxの取得
     *
     * @return
     */
    public Map<String, String> getPrefectureMap() {

        if (prefectureMap != null) {
            return prefectureMap;
        }
        prefectureMap = toolsPullDownList.getPrefecture(false, null);
        return prefectureMap;
    }

    /**
     * 推計機能の取得
     *
     * @return
     */
    public Map<String, String> getSuikeifuncMap() {
        suikeifuncMap = toolsPullDownList.getEstimateUse(false, null);
        return suikeifuncMap;
    }

    /**
     * 推計種別の取得
     *
     * @return
     */
    public Map<String, String> getSuikeitypeMap() {
        String corpId = editBeanProperty.getDivisionCorpId();
        suikeitypeMap = toolsPullDownList.getEstimateKind(false, null, corpId);
        return suikeitypeMap;
    }

    /**
     * 料金メニューの取得
     * @return
     */
    public Map<String, String> getPriceMenuMap() {
        if (priceMenuMap != null) {
            return priceMenuMap;
        }
        priceMenuMap = toolsPullDownList.getPriceMenu(true, "-");
        priceMenuMap.replace("-", "0");
        return priceMenuMap;
    }

    /**
     * 契約容量の取得
     * @return
     */
    public Map<String, String> getContCapacityMap() {
        if (contCapacityMap != null) {
            return contCapacityMap;
        }
        LinkedHashMap<String, String> contCapacityMap = new LinkedHashMap<>();
        for (int i = 1; i < 50; i++) {
            contCapacityMap.put(i + "kVA", String.valueOf(i));
        }
        return contCapacityMap;
    }

    /**
     * 公開設定表示可否
     *
     * @return true:表示
     */
    public boolean getPublicSettingVisble() {
        AccessResultSet result = osolAccessBean.getAccessEnable(
                OsolAccessBean.FUNCTION_CD.NONE,
                "OsakiOrPartnerCorp",
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginOperationCorpId());
        return result.getOutput().isRoleGroupEnable() && result.getOutput().isFunctionEnable();
    }

    /**
     * 推計値利用する、しないをフラグで取得。<br>
     *
     * @return
     */
    public boolean isEstimateUse() {
        return OsolConstants.ESTIMATE_USE.USE.getVal().equals(editBeanProperty.getEstimateUse());
    }

    /**
     * 自治体コード取得
     */
    @Logged
    public void changeMunicipality() {
        // 汎用自治体検索の初期化
        municipalitySearchBean.resetConditionList();

        // 自治体コードが未設定の場合
        if (CheckUtility.isNullOrEmpty(editBeanProperty.getMunicipalityCd())) {
            mMunicipalityEntity = null;
            return;
        }

        // 自治体コードが変更された場合
        mMunicipalityEntity = mMunicipalityDao.find(editBeanProperty.getMunicipalityCd());
        if (mMunicipalityEntity != null) {
            //            buildingInfoEditBeanProperty.setMunicipalityName(mMunicipalityEntity.getMunicipalityName());
            //            buildingInfoEditBeanProperty.setPrefectureCd(mMunicipalityEntity.getMPrefecture().getPrefectureCd());
            //上のeditBeanPropertyに値を入れるだけでいけるはずなのだけど・・・
            //汎用検索で、UIOutput経由でsetValueすると、以降この代入では画面に反映されないという問題がある
            //汎用検索同様にsetValueすれば大丈夫なようだ
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elc = context.getELContext();
            UIComponent root = context.getViewRoot();//.getChildren().get(1);
            UIComponent ui;
            ui = findComponent(root, "prefectureCd");
            if (ui instanceof UIOutput) {
                String prefectureCd = mMunicipalityEntity.getMPrefecture().getPrefectureCd();
                UIOutput inp = (UIOutput) ui;
                inp.setValue(prefectureCd);

                ValueExpression ve = inp.getValueExpression("value");
                if (ve != null) {
                    ve.setValue(elc, prefectureCd);
                }
            }
            ui = findComponent(root, "municipalityName");
            if (ui instanceof UIOutput) {
                UIOutput inp = (UIOutput) ui;
                inp.setValue(mMunicipalityEntity.getMunicipalityName());

                ValueExpression ve = inp.getValueExpression("value");
                if (ve != null) {
                    ve.setValue(elc, mMunicipalityEntity.getMunicipalityName());
                }
            }

            changePrefectureCd();
        }
    }

    /**
    * 都道府県が変更されたとき、汎用自治体検索に値を放り込む
    */
    @Logged
    public void changePrefectureCd() {
        changeCommonMunicipalityPrefectureCd(editBeanProperty.getPrefectureCd());
    }

    /**
     *  汎用自治体検索に値を入れてみる
     * @param prefectureCd
     */
    @Logged
    public void changeCommonMunicipalityPrefectureCd(String prefectureCd) {
        municipalitySearchBean.clearMunicipalityForm();
        municipalitySearchBean.resetConditionList();

        municipalitySearchBean.getPrefectureMap();
        if (CheckUtility.isNullOrEmpty(prefectureCd)) {
            prefectureCd = "01";
        }

        for (Condition condition : municipalitySearchBean
                .getConditionList()) {
            if (condition.isPrefectureSelectEnable()) {
                condition.setPrefectureCd(prefectureCd);
            }
        }
        municipalitySearchBean.setSearchedFlg(false);
    }


    public void addInfoMessages(String infoMessages) {
        if (this.infoMessages == null) {
            this.infoMessages = new ArrayList<>();
        }
        this.infoMessages.add(infoMessages);
    }

    public void addErrorMessages(String errorMessages) {
        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        }
        this.errorMessages.add(errorMessages);
    }

    public void addErrorDetailMessages(String errorDetailMessages) {
        if (this.errorDetailMessages == null) {
            this.errorDetailMessages = new ArrayList<>();
        }
        this.errorDetailMessages.add(errorDetailMessages);
    }

    public void initInfoMessages() {
        if (this.infoMessages == null) {
            this.infoMessages = new ArrayList<>();
        } else {
            this.infoMessages.clear();
        }
    }

    public void initErrorMessages() {

        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        } else {
            this.errorMessages.clear();
        }
    }

    public void initErrorDetailMessages() {

        if (this.errorDetailMessages == null) {
            this.errorDetailMessages = new ArrayList<>();
        } else {
            this.errorDetailMessages.clear();
        }
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(List<String> infoMessages) {
        this.infoMessages = infoMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorDetailMessages() {
        return errorDetailMessages;
    }

    public void setErrorDetailMessages(List<String> errorDetailMessages) {
        this.errorDetailMessages = errorDetailMessages;
    }

    /**
     * 確認ダイアログ(登録・更新)表示メッセージの取得
     *
     * @return
     */
    public String getDialogMessage() {
        eventLogger.debug(EditBean.class
                .getPackage().getName().concat(" smsCollectSettingTenantEditBean:getDialogMessage():START"));
        dialogMessage = beanMessages.getMessage("osol.warn.beforeRegisterMessage");

        if (editBeanProperty.getUpdateProcessFlg()) {
            eventLogger.debug(EditBean.class
                    .getPackage().getName().concat(" getDialogMessage:updateMsg set"));
            dialogMessage = beanMessages.getMessage("osol.warn.beforeUpdateMessage");

        }
        eventLogger.debug(EditBean.class
                .getPackage().getName().concat(" smsCollectSettingTenantEditBean:getDialogMessage():END"));
        return dialogMessage;
    }

    /**
     * 確認ダイアログ(削除)表示メッセージの取得
     *
     * @return
     */
    public String getDeletePopupMsg() {

        if (editBeanProperty.getTenantFlg()) {
            return beanMessages.getMessageFormat("buildingInfoEditBean.warn.beforeDeleteMessage",
                    OsolConstants.BUILDING_TENANT.TENANT.getName());
        } else {
            return beanMessages.getMessageFormat("buildingInfoEditBean.warn.beforeDeleteMessage",
                    OsolConstants.BUILDING_TENANT.BUILDING.getName());
        }
    }


    public EditDetailPopupInfo getEditDetailPopupInfo() {
        return editDetailPopupInfo;
    }

    public void setEditDetailPopupInfo(EditDetailPopupInfo editDetailPopupInfo) {
        this.editDetailPopupInfo = editDetailPopupInfo;
    }

    public boolean isEditDetailPopupCompFlg() {
        return editDetailPopupCompFlg;
    }

    public void setEditDetailPopupCompFlg(boolean editDetailPopupCompFlg) {
        this.editDetailPopupCompFlg = editDetailPopupCompFlg;
    }

    public List<String> getInvalidComponent() {
        return invalidComponent;
    }

    public void setInvalidComponent(List<String> invalidComponent) {
        this.invalidComponent = invalidComponent;
    }

    public void initInvalidComponent() {

        if (this.invalidComponent == null) {
            this.invalidComponent = new ArrayList<>();
        } else {
            this.invalidComponent.clear();
        }
    }

    public int getPopupIndex() {
        return popupIndex;
    }

    public void setPopupIndex(int popupIndex) {
        this.popupIndex = popupIndex;
    }

}
