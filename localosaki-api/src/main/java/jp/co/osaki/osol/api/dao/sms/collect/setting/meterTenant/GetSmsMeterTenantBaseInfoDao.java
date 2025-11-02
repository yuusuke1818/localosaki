package jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.BuildDevMeterResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterGroupNameResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterRangeTenantPriceInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTenantSmsInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTypeResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.RangeUnitPriceResultData;
import jp.co.osaki.osol.api.servicedao.entity.MMeterRangeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantPriceInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.BuildDevMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterGroupNameServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterRangeUnitPriceServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterTenantSmsInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK;
import jp.co.osaki.osol.entity.MTenantPriceInfo;
import jp.co.osaki.osol.entity.MTenantPriceInfoPK;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

@Stateless
public class GetSmsMeterTenantBaseInfoDao extends OsolApiDao<GetSmsMeterTenantBaseInfoParameter> {

    // メーター種別設定
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メーターグループ名称設定
    private final MeterGroupNameServiceDaoImpl meterGroupNameServiceDaoImpl;

    // 建物（テナント）
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;

    // 登録計器（メーター）
    private final BuildDevMeterServiceDaoImpl buildingDevMeterServiceDaoImpl;

    // メーター種別従量値情報
    private final MMeterRangeServiceDaoImpl mMeterRangeServiceDaoImpl;

    // テナント料金情報
    private final MTenantPriceInfoServiceDaoImpl mTenantPriceInfoServiceDaoImpl;

    // テナントユーザー
    private final MeterTenantSmsInfoServiceDaoImpl meterTenantSmsInfoServiceDaoImpl;

    // 料金単価情報（メーター種別従量値）
    private final MeterRangeUnitPriceServiceDaoImpl meterRangeUnitPriceServiceDaoImpl;

    public GetSmsMeterTenantBaseInfoDao() {
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterGroupNameServiceDaoImpl = new MeterGroupNameServiceDaoImpl();
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        buildingDevMeterServiceDaoImpl = new BuildDevMeterServiceDaoImpl();
        mMeterRangeServiceDaoImpl = new MMeterRangeServiceDaoImpl();
        mTenantPriceInfoServiceDaoImpl = new MTenantPriceInfoServiceDaoImpl();
        meterTenantSmsInfoServiceDaoImpl = new MeterTenantSmsInfoServiceDaoImpl();
        meterRangeUnitPriceServiceDaoImpl = new MeterRangeUnitPriceServiceDaoImpl();
    }

    @Override
    public GetSmsMeterTenantBaseInfoResult query(GetSmsMeterTenantBaseInfoParameter parameter) throws Exception {

        GetSmsMeterTenantBaseInfoResult result = new GetSmsMeterTenantBaseInfoResult();

        /**
         * 共通取得項目（建物に紐づく情報取得）
         * メーターグループ名称設定のリスト（複数）
         * メーター種別設定のリスト（複数）
         */

        // メーターグループ名称設定取得
        MeterGroupNameResultData meterGroupNameResultData = new MeterGroupNameResultData();
        meterGroupNameResultData.setCorpId(parameter.getCorpId());
        meterGroupNameResultData.setBuildingId(parameter.getBuildingId());
        List<MeterGroupNameResultData> meterGroupNameList = getResultList(meterGroupNameServiceDaoImpl,
                meterGroupNameResultData);

        MeterTypeResultData meterTypeAllResultData = new MeterTypeResultData();
        meterTypeAllResultData.setCorpId(parameter.getCorpId());
        meterTypeAllResultData.setBuildingId(parameter.getBuildingId());
        List<MeterTypeResultData> meterTypeAllList = getResultList(meterTypeServiceDaoImpl, meterTypeAllResultData);
        List<MeterRangeTenantPriceInfoResultData> MeterRangeTenantPriceInfoList = new ArrayList<>();

        for (MeterTypeResultData target : meterTypeAllList) {

            // 従量値範囲をリストで取得
            MMeterRangePK mMeterRangePK = new MMeterRangePK();
            MMeterRange mMeterRange = new MMeterRange();
            mMeterRangePK.setCorpId(target.getCorpId());
            mMeterRangePK.setBuildingId(target.getBuildingId());
            mMeterRangePK.setMeterType(target.getMeterType());
            mMeterRangePK.setMenuNo(target.getMenuNo());
            mMeterRange.setId(mMeterRangePK);
            List<MMeterRange> mMeterRangeList = getResultList(mMeterRangeServiceDaoImpl, mMeterRange);

            // 従量料金単価リストを作成
            // 従量値範囲をリストが空の場合nullを設定
            List<RangeUnitPriceResultData> rangeUnitPriceResultDataList = (mMeterRangeList.isEmpty() ? null
                    : new ArrayList<>());

            for (MMeterRange entity : mMeterRangeList) {
                RangeUnitPriceResultData rangeUnitPriceResultData = new RangeUnitPriceResultData();
                rangeUnitPriceResultData.setRangeValue(entity.getId().getRangeValue());
                rangeUnitPriceResultDataList.add(rangeUnitPriceResultData);
            }

            MeterRangeTenantPriceInfoResultData meterRangeTenantPriceInfo = new MeterRangeTenantPriceInfoResultData();
            meterRangeTenantPriceInfo.setMeterType(target.getMeterType());
            meterRangeTenantPriceInfo.setMenuNo(target.getMenuNo());
            meterRangeTenantPriceInfo.setMeterTypeName(target.getMeterTypeName());
            meterRangeTenantPriceInfo.setUnitUsageBased(target.getUnitUsageBased());
            meterRangeTenantPriceInfo.setRangeUnitPriceResultDataList(rangeUnitPriceResultDataList);
            MeterRangeTenantPriceInfoList.add(meterRangeTenantPriceInfo);
        }

        /**
         * 更新・複製時の取得項目
         * 建物情報(テナント建物)
         * テナントユーザー情報
         * 料金メニュー(従量電灯A)
         * 料金メニュー(従量電灯B)
         * メーター種別設定
         * メーター種別従量情報
         * テナント料金情報
         * 料金単価情報
         * 登録計器（メーター）ID取得
         */

        // テナントに紐づく情報取得
        if (parameter.getTenantCorpId() != null && parameter.getTenantBuildingId() != null) {

            // 建物情報取得
            TBuildingPK tBuildingPK = new TBuildingPK();
            TBuilding tBuilding = new TBuilding();
            tBuildingPK.setCorpId(parameter.getTenantCorpId());
            tBuildingPK.setBuildingId(parameter.getTenantBuildingId());
            tBuilding.setId(tBuildingPK);

            TBuilding tenantInfo = find(tBuildingApiServiceDaoImpl, tBuilding);
            // 建物情報を設定
            result.setCorpId(tenantInfo.getId().getCorpId());
            result.setBuildingId(tenantInfo.getId().getBuildingId());
            result.setBuildingNo(tenantInfo.getBuildingNo());
            result.setBuildingName(tenantInfo.getBuildingName());
            result.setBuildingTansyukuName(tenantInfo.getBuildingTansyukuName());
            result.setBuildingNameKana(tenantInfo.getBuildingNameKana());
            result.setZipCd(tenantInfo.getZipCd());
            result.setPrefectureCd(tenantInfo.getMPrefecture().getPrefectureCd());

            if (tenantInfo.getMMunicipality() != null) {
                result.setMunicipalityCd(tenantInfo.getMMunicipality().getMunicipalityCd());
            }
            result.setMunicipalityName(tenantInfo.getAddress());
            result.setAddressBuilding(tenantInfo.getAddressBuilding());
            result.setTelNo(tenantInfo.getTelNo());
            result.setFaxNo(tenantInfo.getFaxNo());
            result.setBiko(tenantInfo.getBiko());
            result.setBuildingDelDate(tenantInfo.getBuildingDelDate());
            result.setPublicFlg(tenantInfo.getPublicFlg());
            result.setDelFlg(tenantInfo.getDelFlg());
            result.setVersion(tenantInfo.getVersion());
            // 利用機能を設定
            result.setTotalStartYm(tenantInfo.getTotalStartYm());
            result.setTotalEndYm(tenantInfo.getTotalEndYm());
            result.setEstimateUse(tenantInfo.getEstimateUse());

            // 推計種別取得
            if (!tenantInfo.getTBuildingEstimateKinds().isEmpty()
                    && tenantInfo.getTBuildingEstimateKinds().get(0) != null) {
                result.setEstimateId(tenantInfo.getTBuildingEstimateKinds().get(0).getId().getEstimateId());
            }

            if (tenantInfo != null) {
                // 登録計器（メーター）ID取得
                BuildDevMeterResultData buildDevMeterResultData = new BuildDevMeterResultData();
                buildDevMeterResultData.setCorpId(tenantInfo.getId().getCorpId());
                buildDevMeterResultData.setBuildingId(tenantInfo.getId().getBuildingId());
                List<BuildDevMeterResultData> buildingDevMeterList = getResultList(buildingDevMeterServiceDaoImpl,
                        buildDevMeterResultData);

                // 登録計器（メーター）ID設定
                result.setBuildDevMeterList(buildingDevMeterList);


                // メーターテナント情報、料金メニュー（従量電灯A）、電気料金メニュー（従量電灯B）
                MeterTenantSmsInfoResultData meterTenantSmsInfoResultData = new MeterTenantSmsInfoResultData();
                meterTenantSmsInfoResultData.setCorpId(parameter.getTenantCorpId());
                meterTenantSmsInfoResultData.setBuildingId(parameter.getTenantBuildingId());
                MeterTenantSmsInfoResultData meterTenantSmsInfoEntity = find(meterTenantSmsInfoServiceDaoImpl, meterTenantSmsInfoResultData);

                // Entityが存在しない場合、テナントまだ登録されていない
                if (meterTenantSmsInfoEntity != null) {
                    result.setTenantId(meterTenantSmsInfoEntity.getTenantId());
                    result.setFixedName1(meterTenantSmsInfoEntity.getFixedName1());
                    result.setFixedPrice1(meterTenantSmsInfoEntity.getFixedPrice1());
                    result.setFixedName2(meterTenantSmsInfoEntity.getFixedName2());
                    result.setFixedPrice2(meterTenantSmsInfoEntity.getFixedPrice2());
                    result.setFixedName3(meterTenantSmsInfoEntity.getFixedName3());
                    result.setFixedPrice3(meterTenantSmsInfoEntity.getFixedPrice3());
                    result.setFixedName4(meterTenantSmsInfoEntity.getFixedName4());
                    result.setFixedPrice4(meterTenantSmsInfoEntity.getFixedPrice4());
                    result.setPriceMenuNo(meterTenantSmsInfoEntity.getPriceMenuNo());
                    result.setContractCapacity(meterTenantSmsInfoEntity.getContractCapacity());
                    result.setDivRate1(meterTenantSmsInfoEntity.getDivRate1());
                    result.setDivRate2(meterTenantSmsInfoEntity.getDivRate2());
                    result.setDivRate3(meterTenantSmsInfoEntity.getDivRate3());
                    result.setDivRate4(meterTenantSmsInfoEntity.getDivRate4());
                    result.setDivRate5(meterTenantSmsInfoEntity.getDivRate5());
                    result.setDivRate6(meterTenantSmsInfoEntity.getDivRate6());
                    result.setDivRate7(meterTenantSmsInfoEntity.getDivRate7());
                    result.setDivRate8(meterTenantSmsInfoEntity.getDivRate8());
                    result.setDivRate9(meterTenantSmsInfoEntity.getDivRate9());
                    result.setDivRate10(meterTenantSmsInfoEntity.getDivRate10());


                    // テナント料金情報取得
                    for (MeterRangeTenantPriceInfoResultData target : MeterRangeTenantPriceInfoList) {

                        // 料金メニュー（従量電灯A）設定
                        if (target.getMeterType().longValue() == 1L
                                && target.getMenuNo().longValue() == Long.valueOf(ApiGenericTypeConstants.SMS_ELECTRIC_MENU_312.LIGHT_A.getVal())) {
                            target.setPriceMenuLightaResultData(meterTenantSmsInfoEntity.getPriceMenuLightaResultData());
                        }

                        // 料金メニュー（従量電灯B）設定
                        if (target.getMeterType().longValue() == 1L
                                && target.getMenuNo().longValue() == Long.valueOf(ApiGenericTypeConstants.SMS_ELECTRIC_MENU_312.LIGHT_B.getVal())) {
                            target.setPriceMenuLightbResultData(meterTenantSmsInfoEntity.getPriceMenuLightbResultData());
                        }

                        MTenantPriceInfoPK mTenantPriceInfoPK = new MTenantPriceInfoPK();
                        MTenantPriceInfo mTenantPriceInfo = new MTenantPriceInfo();
                        mTenantPriceInfoPK.setMeterType(target.getMeterType());
                        mTenantPriceInfoPK.setCorpId(tenantInfo.getId().getCorpId());
                        mTenantPriceInfoPK.setBuildingId(tenantInfo.getId().getBuildingId());
                        mTenantPriceInfoPK.setPowerPlanId(target.getMenuNo());
                        mTenantPriceInfo.setId(mTenantPriceInfoPK);
                        MTenantPriceInfo mTenantPriceInfoEntity = find(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfo);

                        // テナント料金情報が存在する
                        if (mTenantPriceInfoEntity != null) {
                            target.setPricePlanId(mTenantPriceInfoEntity.getId().getPricePlanId());

                            // 料金メニュー（従量電灯A）以外は設定
                            if (mTenantPriceInfoEntity.getId().getMeterType().longValue() != 1L
                                    && mTenantPriceInfoEntity.getId().getPowerPlanId() != 1L) {
                                target.setBasicPrice(mTenantPriceInfoEntity.getBasicPrice());
                            }

                            target.setDiscountRate(mTenantPriceInfoEntity.getDiscountRate());

                            // 料金単価情報から取得
                            // メーター従量値がなければ料金単価情報は必要ない
                            if (target.getRangeUnitPriceResultDataList() != null && target.getPricePlanId() != null) {
                                MUnitPricePK mUnitPricePK = new MUnitPricePK();
                                MUnitPrice mUnitPrice = new MUnitPrice();
                                mUnitPricePK.setPricePlanId(target.getPricePlanId());
                                mUnitPrice.setId(mUnitPricePK);
                                List<MUnitPrice> mUnitPriceList = getResultList(meterRangeUnitPriceServiceDaoImpl, mUnitPrice);

                                if (!mUnitPriceList.isEmpty()) {
                                    // 従量範囲リストの従量値に一致する料金単価情報が存在
                                    for (RangeUnitPriceResultData rangeUnitPriceResultData : target.getRangeUnitPriceResultDataList()) {
                                        for (MUnitPrice mUnitPriceEntity : mUnitPriceList) {
                                            // 料金単価設定
                                            if (rangeUnitPriceResultData.getRangeValue().longValue() == mUnitPriceEntity.getId().getLimitUsageVal().longValue()) {
                                                rangeUnitPriceResultData.setUnitPrice(mUnitPriceEntity.getUnitPrice());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        result.setMeterGroupNameList(meterGroupNameList);
        result.setMeterTypeList(MeterRangeTenantPriceInfoList);
        return result;
    }

}
