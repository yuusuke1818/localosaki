package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataFixedCostResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataGroupTotalChargeResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResultData;
import jp.co.osaki.osol.api.servicedao.entity.MUnitPriceServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TInspectionMeterSvrServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterGroup;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.TenantSmsServiceDaoImpl;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.sms.SmsConstants;

/**
 * 請求金額データ取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class ListSmsBillingAmountDataSearchDao extends OsolApiDao<ListSmsBillingAmountDataParameter> {

    private final ListSmsBillingAmountDataServiceDaoImpl listSmsBillingAmountDataServiceDaoImpl;
    private final ListSmsMeterGroupServiceDaoImpl listSmsMeterGroupServiceDaoImpl;
    private final MUnitPriceServiceDaoImpl mUnitPriceServiceDaoImpl;
    private final TInspectionMeterSvrServiceDaoImpl tInspectionMeterSvrServiceDaoImpl;
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;
    private final TenantSmsServiceDaoImpl tenantSmsServiceDaoImpl;

    public ListSmsBillingAmountDataSearchDao() {
        listSmsBillingAmountDataServiceDaoImpl = new ListSmsBillingAmountDataServiceDaoImpl();
        listSmsMeterGroupServiceDaoImpl = new ListSmsMeterGroupServiceDaoImpl();
        mUnitPriceServiceDaoImpl = new MUnitPriceServiceDaoImpl();
        tInspectionMeterSvrServiceDaoImpl = new TInspectionMeterSvrServiceDaoImpl();
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
        tenantSmsServiceDaoImpl = new TenantSmsServiceDaoImpl();

    }

    @Override
    public ListSmsBillingAmountDataResult query(ListSmsBillingAmountDataParameter parameter) throws Exception {

        ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData = new ListSmsBillingAmountDataInfoResultData();

        buildingBillingInfoResultData.setCorpId(parameter.getCorpId());
        buildingBillingInfoResultData.setBuildingId(parameter.getBuildingId());

        //建物請求情報取得処理
        List<ListSmsBillingAmountDataInfoResultData> resultBuildingBillingInfoList = getResultList(
                listSmsBillingAmountDataServiceDaoImpl, buildingBillingInfoResultData);

        // メーターフィルター
        resultBuildingBillingInfoList = meterDataFilter(resultBuildingBillingInfoList, parameter);

        Long prevBuildingId = null;
        BigDecimal usageFees = BigDecimal.valueOf(0);
        Boolean billingAmountStatusFlg = Boolean.TRUE;
        List<ListSmsBillingAmountDataResultData> billingAmountDataResultDataList = new ArrayList<>();
        ListSmsBillingAmountDataResultData billingAmountDataResultData = null;
        List<ListSmsBillingAmountDataDetailResultData> billingAmountResultDataList = null;

        ListSmsMeterGroup meterGroupTotalChargeResultData = new ListSmsMeterGroup();
        meterGroupTotalChargeResultData.setCorpId(parameter.getCorpId());
        meterGroupTotalChargeResultData.setBuildingId(parameter.getOperationBuildingId());
        meterGroupTotalChargeResultData.setYear(parameter.getYear());
        meterGroupTotalChargeResultData.setMonth(parameter.getMonth());

        //メーターグループ情報取得処理
        List<ListSmsMeterGroup> listSmsMeterGroupList = getResultList(
                listSmsMeterGroupServiceDaoImpl, meterGroupTotalChargeResultData);

        for (ListSmsBillingAmountDataInfoResultData buildingBillingInfo : resultBuildingBillingInfoList) {
            MUnitPrice mUnitPrice = new MUnitPrice();
            MUnitPricePK mUnitPricePK = new MUnitPricePK();

            mUnitPricePK.setPricePlanId(buildingBillingInfo.getPricePlanId());
            mUnitPrice.setId(mUnitPricePK);
            //料金単価情報取得処理
            List<MUnitPrice> resultMUnitPriceList = getResultList(mUnitPriceServiceDaoImpl, mUnitPrice);

            TInspectionMeterSvr tInspectionMeterSvr = new TInspectionMeterSvr();
            TInspectionMeterSvrPK tInspectionMeterSvrPK = new TInspectionMeterSvrPK();

            tInspectionMeterSvrPK.setDevId(buildingBillingInfo.getDevId());
            tInspectionMeterSvrPK.setMeterMngId(buildingBillingInfo.getMeterMngId());
            tInspectionMeterSvrPK.setInspYear(parameter.getYear());
            String inspMonth = parameter.getMonth();
            tInspectionMeterSvrPK.setInspMonth(inspMonth);
            tInspectionMeterSvrPK.setInspMonthNo(parameter.getMonthMeterReadingNo());
            tInspectionMeterSvr.setId(tInspectionMeterSvrPK);
            tInspectionMeterSvr.setInspType(parameter.getMeterReadingType());
            //検針請求データ取得処理
            List<TInspectionMeterSvr> resultTInspectionMeterSvrList = getResultList(tInspectionMeterSvrServiceDaoImpl,
                    tInspectionMeterSvr);

            if (Objects.nonNull(resultTInspectionMeterSvrList) && resultTInspectionMeterSvrList.size() != 0) {

                ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData = new ListSmsBillingAmountDataDetailResultData();

                createBillingAmountDataDetailResultData(
                        buildingBillingInfo, resultTInspectionMeterSvrList.get(0), resultMUnitPriceList,
                        resultBillingAmountResultData);

                if (prevBuildingId != buildingBillingInfo.getBuildingId()) {

                    usageFees = BigDecimal.valueOf(0);
                    billingAmountStatusFlg = Boolean.TRUE;
                    billingAmountDataResultData = new ListSmsBillingAmountDataResultData();
                    billingAmountResultDataList = new ArrayList<>();

                    billingAmountDataResultData.setBuildingNo(buildingBillingInfo.getBuildingNo());
                    billingAmountDataResultData.setBuildingName(buildingBillingInfo.getBuildingName());
                    billingAmountDataResultData.setBuildingId(buildingBillingInfo.getBuildingId().toString());
                    billingAmountDataResultData.setTenantId(buildingBillingInfo.getTenantId());

                    // グループ1～10按分率 取得
                    MTenantSm mTenantSmsTarget = new MTenantSm();
                    mTenantSmsTarget.setId(new MTenantSmPK());
                    mTenantSmsTarget.getId().setCorpId(buildingBillingInfo.getCorpId());
                    mTenantSmsTarget.getId().setBuildingId(buildingBillingInfo.getBuildingId()); // 建物ID(テナント)
                    MTenantSm mTenantSmsEntity = find(tenantSmsServiceDaoImpl, mTenantSmsTarget);

                    // グループ1～10按分率 をセットして、グループ合計料金(groupTotalCharge)を算出する
                    List<ListSmsBillingAmountDataGroupTotalChargeResultData> resultMeterGroupTotalChargeResultDataList = new ArrayList<>();

                    for(int i = 0; i < 10; i++) {
                        if(i < listSmsMeterGroupList.size()) {
                            resultMeterGroupTotalChargeResultDataList.add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                    listSmsMeterGroupList.get(i).getMeterGroupName(), listSmsMeterGroupList.get(i).getMeterGroupId(), listSmsMeterGroupList.get(i).getGroupPrice(), mTenantSmsEntity));
                        }else {
                            resultMeterGroupTotalChargeResultDataList.add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                    null, null, null, null));
                        }
                    }

                    billingAmountDataResultData.setMeterGroupTotalChargeList(resultMeterGroupTotalChargeResultDataList);

                    List<ListSmsBillingAmountDataFixedCostResultData> fixedCostResultDataList = new ArrayList<>();

                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData1 = new ListSmsBillingAmountDataFixedCostResultData(
                            buildingBillingInfo.getFixedCostName1(),
                            buildingBillingInfo.getFixedCost1(),
                            String.format("%,d", Integer.parseInt(buildingBillingInfo.getFixedCost1())));
                    fixedCostResultDataList.add(fixedCostResultData1);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData2 = new ListSmsBillingAmountDataFixedCostResultData(
                            buildingBillingInfo.getFixedCostName2(),
                            buildingBillingInfo.getFixedCost2(),
                            String.format("%,d", Integer.parseInt(buildingBillingInfo.getFixedCost2())));
                    fixedCostResultDataList.add(fixedCostResultData2);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData3 = new ListSmsBillingAmountDataFixedCostResultData(
                            buildingBillingInfo.getFixedCostName3(),
                            buildingBillingInfo.getFixedCost3(),
                            String.format("%,d", Integer.parseInt(buildingBillingInfo.getFixedCost3())));
                    fixedCostResultDataList.add(fixedCostResultData3);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData4 = new ListSmsBillingAmountDataFixedCostResultData(
                            buildingBillingInfo.getFixedCostName4(),
                            buildingBillingInfo.getFixedCost4(),
                            String.format("%,d", Integer.parseInt(buildingBillingInfo.getFixedCost4())));
                    fixedCostResultDataList.add(fixedCostResultData4);
                    billingAmountDataResultData.setFixedCostList(fixedCostResultDataList);

                    // 各種設定テーブルがなければ固定値
                    if (Objects.isNull(buildingBillingInfo.getMVariousPK())) {
                        billingAmountDataResultData.setSalesTaxRate(SmsConstants.SALE_TAX_RATE);
                        billingAmountDataResultData.setSalesTaxTreatment(SmsConstants.SALE_TAX_DEAL);
                        billingAmountDataResultData
                                .setFractionalProcessing(SmsConstants.DECIMAL_FRACTION);
                    } else {
                        String salesTaxRate = Objects.isNull(buildingBillingInfo.getSalesTaxRate())
                                ? SmsConstants.SALE_TAX_RATE
                                : buildingBillingInfo.getSalesTaxRate().toString();
                        billingAmountDataResultData.setSalesTaxRate(salesTaxRate);

                        String salesTaxTreatment = StringUtils.isEmpty(buildingBillingInfo.getSalesTaxTreatment())
                                ? SmsConstants.SALE_TAX_DEAL
                                : buildingBillingInfo.getSalesTaxTreatment();
                        billingAmountDataResultData.setSalesTaxTreatment(salesTaxTreatment);

                        String fractionalProcessing = StringUtils.isEmpty(buildingBillingInfo.getFractionalProcessing())
                                ? SmsConstants.DECIMAL_FRACTION
                                : buildingBillingInfo.getFractionalProcessing();
                        billingAmountDataResultData
                                .setFractionalProcessing(fractionalProcessing);
                    }

                    billingAmountDataResultData.setBillingAmountDataList(billingAmountResultDataList);
                    billingAmountDataResultDataList.add(billingAmountDataResultData);

                }

                usageFees = usageFees.add(new BigDecimal(resultBillingAmountResultData.getUsageFee()));
                billingAmountResultDataList.add(resultBillingAmountResultData);

                prevBuildingId = buildingBillingInfo.getBuildingId();

                if (!resultBillingAmountResultData.getBillingAmountStatus().equals("完了")) {
                    billingAmountStatusFlg = Boolean.FALSE;
                }
                if (null != prevBuildingId) {
                    //請求金額・消費税
//                    createBillingAmountCalculation(usageFees, billingAmountDataResultData);
                    //ステータス
                    billingAmountDataResultData.setStatus(resultBillingAmountResultData.getBillingAmountStatus());
                }

            }

        }

        ListSmsBillingAmountDataResult result = new ListSmsBillingAmountDataResult();
        result.setBillingAmountDataResultDataList(billingAmountDataResultDataList);

        return result;
    }

    public void createBillingAmountDataDetailResultData(
            ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData,
            TInspectionMeterSvr tInspectionMeterSvr, List<MUnitPrice> mUnitPriceList,
            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {

        String menuName = ListSmsBillingAmountDataInfoResultData.NO_VALUE_STR;
        String menu = Objects.isNull(buildingBillingInfoResultData.getPriceMenuNo()) ? ""
                : buildingBillingInfoResultData.getPriceMenuNo();
        switch (menu) {
        case "1":
            menuName = ListSmsBillingAmountDataInfoResultData.A_VALUE_STR;
            break;
        case "2":
            menuName = ListSmsBillingAmountDataInfoResultData.B_VALUE_STR;
            break;
        default:
            menuName = ListSmsBillingAmountDataInfoResultData.NO_VALUE_STR;
            break;
        }
        resultBillingAmountResultData.setPriceMenuName(menuName);
        resultBillingAmountResultData.setDevId(buildingBillingInfoResultData.getDevId());
        resultBillingAmountResultData.setMeterMngId(buildingBillingInfoResultData.getMeterMngId());
        resultBillingAmountResultData.setMemo(buildingBillingInfoResultData.getMemo());
        resultBillingAmountResultData.setMeterId(buildingBillingInfoResultData.getMeterId());
        resultBillingAmountResultData.setMeterTypeName(buildingBillingInfoResultData.getMeterTypeName());
        resultBillingAmountResultData.setUnitUsageBased(buildingBillingInfoResultData.getUnitUsageBased());
        resultBillingAmountResultData.setMeterGroupId(buildingBillingInfoResultData.getMeterGroupId());
        resultBillingAmountResultData.setLatestInspVal(Objects.isNull(tInspectionMeterSvr.getLatestInspVal()) ? "-" : tInspectionMeterSvr.getLatestInspVal().toString());
        resultBillingAmountResultData.setPrevInspVal(Objects.isNull(tInspectionMeterSvr.getPrevInspVal()) ? "-" : tInspectionMeterSvr.getPrevInspVal().toString());
        resultBillingAmountResultData.setMultipleRate(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? null : tInspectionMeterSvr.getMultipleRate().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        resultBillingAmountResultData.setLatestInspDate(Objects.isNull(tInspectionMeterSvr.getLatestInspDate()) ? null : sdf.format(tInspectionMeterSvr.getLatestInspDate()));

        BigDecimal usageVal = BigDecimal.ZERO;

        if (!Objects.isNull(tInspectionMeterSvr.getLatestInspVal()) && !Objects.isNull(tInspectionMeterSvr.getPrevInspVal())) {
            if (1 == tInspectionMeterSvr.getPrevInspVal().compareTo(tInspectionMeterSvr.getLatestInspVal())) {
                usageVal = tInspectionMeterSvr.getLatestInspVal().add(BigDecimal.valueOf(100000))
                        .subtract(tInspectionMeterSvr.getPrevInspVal()).multiply(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? BigDecimal.ZERO : tInspectionMeterSvr.getMultipleRate());
            } else {
                usageVal = tInspectionMeterSvr.getLatestInspVal().subtract(tInspectionMeterSvr.getPrevInspVal())
                        .multiply(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? BigDecimal.ZERO : tInspectionMeterSvr.getMultipleRate());
            }
        }
        resultBillingAmountResultData.setUsageVal(usageVal.toString());

        if (BigDecimal.ZERO.equals(tInspectionMeterSvr.getEndFlg())) {
            resultBillingAmountResultData.setBillingAmountStatus("未完了");
        } else if (BigDecimal.ONE.equals(tInspectionMeterSvr.getEndFlg())) {
            resultBillingAmountResultData.setBillingAmountStatus("完了");
        } else {
            resultBillingAmountResultData.setBillingAmountStatus("エラー");
        }

        if (1 == buildingBillingInfoResultData.getMeterType()
                && "1".equals(buildingBillingInfoResultData.getPriceMenuNo())) {
            //従量電灯Aの請求金額算出
            usageVolumeFee_A(buildingBillingInfoResultData, mUnitPriceList, usageVal, resultBillingAmountResultData);
        } else if (1 == buildingBillingInfoResultData.getMeterType()
                && "2".equals(buildingBillingInfoResultData.getPriceMenuNo())) {
            //従量電灯Bの請求金額算出
            usageVolumeFee_B(buildingBillingInfoResultData, mUnitPriceList, usageVal, resultBillingAmountResultData);
        } else {
            //その他の請求金額算出
            usageVolumeFee_Other(buildingBillingInfoResultData, mUnitPriceList, usageVal,
                    resultBillingAmountResultData);
        }

    }

    /**
     * 従量電灯Aの請求金額算出.
     *
     */
    private void usageVolumeFee_A(ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData,
            List<MUnitPrice> mUnitPriceList, BigDecimal usageVal,
            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {

        //各種テーブル、テナント料金情報テーブル、料金単価情報テーブル、料金メニュー(従量電灯A)テーブルのレコード存在しない場合
        if (Objects.isNull(buildingBillingInfoResultData.getMVariousPK())
                || Objects.isNull(buildingBillingInfoResultData.getMTenantPriceInfoPK())
                || CollectionUtils.isEmpty(mUnitPriceList)
                || Objects.isNull(buildingBillingInfoResultData.getMPriceMenuLightaPK())) {
            resultBillingAmountResultData.setUsageVolumeFee("0");
            resultBillingAmountResultData.setFuelAdjustmentFee("0");
            resultBillingAmountResultData.setRenewableEnergyLevy("0");
            resultBillingAmountResultData.setHighVoltageBulkDiscount("0");
            resultBillingAmountResultData.setDiscountAmount("0");
            resultBillingAmountResultData.setUsageFee("0");
            return;
        }

        //従量料金算出
        Long prevUsageVal = 0L;
        BigDecimal usageVolumeFee = BigDecimal.ZERO;

        for (MUnitPrice mUnitPrice : mUnitPriceList) {
            BigDecimal unitPrice = Objects.isNull(mUnitPrice.getUnitPrice()) ? BigDecimal.ZERO
                    : mUnitPrice.getUnitPrice();

            if (1 == usageVal.compareTo(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal()))) {
                usageVolumeFee = usageVolumeFee
                        .add(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal() - prevUsageVal)
                                .multiply(unitPrice));
                prevUsageVal = mUnitPrice.getId().getLimitUsageVal();
            } else {
                usageVolumeFee = usageVolumeFee
                        .add(usageVal.subtract(BigDecimal.valueOf(prevUsageVal)).multiply(unitPrice));
                break;
            }
        }
        BigDecimal lowestPrice = Objects.isNull(buildingBillingInfoResultData.getLowestPrice_A()) ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getLowestPrice_A();
        usageVolumeFee = usageVolumeFee.add(lowestPrice);

        // 小数第2位で指定
        usageVolumeFee = usageVolumeFee.setScale(2, RoundingMode.DOWN);
        resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());

        //燃料調整額算出
        BigDecimal fuelAdjustmentFee = BigDecimal.ZERO;
        BigDecimal fuelAdjustPrice = Objects.isNull(buildingBillingInfoResultData.getFuelAdjustPrice_A())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getFuelAdjustPrice_A();
        BigDecimal fuelAdjustPriceOver15 = Objects.isNull(buildingBillingInfoResultData.getFuelAdjustPriceOver15_A())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getFuelAdjustPriceOver15_A();

        if (1 == usageVal.compareTo(BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal()))) {
            fuelAdjustmentFee = BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal())
                    .multiply(fuelAdjustPrice);
            fuelAdjustmentFee = fuelAdjustmentFee.add(fuelAdjustPriceOver15
                    .multiply(usageVal.subtract(BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal()))));
        } else {
            fuelAdjustmentFee = usageVal.multiply(fuelAdjustPrice);
        }

        // 小数第2位で指定
        fuelAdjustmentFee = fuelAdjustmentFee.setScale(2, RoundingMode.DOWN);
        resultBillingAmountResultData.setFuelAdjustmentFee(fuelAdjustmentFee.toString());

        //再エネ賦課金算出
        BigDecimal renewableEnergyLevy = BigDecimal.ZERO;
        BigDecimal renewEnerPrice = Objects.isNull(buildingBillingInfoResultData.getRenewEnerPrice_A())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getRenewEnerPrice_A();
        BigDecimal renewEnerPriceOver15 = Objects.isNull(buildingBillingInfoResultData.getRenewEnerPriceOver15_A())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getRenewEnerPriceOver15_A();

        if (1 == usageVal.compareTo(BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal()))) {
            renewableEnergyLevy = BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal())
                    .multiply(renewEnerPrice);
            renewableEnergyLevy = renewableEnergyLevy.add(renewEnerPriceOver15
                    .multiply(usageVal.subtract(BigDecimal.valueOf(mUnitPriceList.get(0).getId().getLimitUsageVal()))));
        } else {
            renewableEnergyLevy = usageVal.multiply(renewEnerPrice);
        }

        // 小数第2位で指定
        renewableEnergyLevy = renewableEnergyLevy.setScale(2, RoundingMode.DOWN);
        resultBillingAmountResultData.setRenewableEnergyLevy(renewableEnergyLevy.toString());

        //高圧一括割額算出
        BigDecimal highVoltageBulkDiscount = BigDecimal.ZERO;
        resultBillingAmountResultData.setHighVoltageBulkDiscount(highVoltageBulkDiscount.toString());

        //割引額算出
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal discountRate = Objects.isNull(buildingBillingInfoResultData.getDiscountRate())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getDiscountRate();

        discountAmount = usageVolumeFee.add(fuelAdjustmentFee).add(renewableEnergyLevy)
                .subtract(highVoltageBulkDiscount).multiply(discountRate

                        .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
        resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());

        //使用料金算出
        BigDecimal usageFee = BigDecimal.ZERO;

        usageFee = usageVolumeFee.add(fuelAdjustmentFee).add(renewableEnergyLevy).subtract(highVoltageBulkDiscount)
                .subtract(discountAmount);
        usageFee = fractionalProcessing(buildingBillingInfoResultData.getFractionalProcessing(), usageFee);

        resultBillingAmountResultData.setUsageFee(usageFee.toString());
    }

    /**
     * 従量電灯Bの請求金額算出
     *
     * @param buildingBillingInfoResultData DBレスポンスデータ
     * @param mUnitPriceList
     * @param usageVal
     * @param resultBillingAmountResultData APIレスポンスパラメーター用(参照で詰めていく)
     * @throws Exception
     */
    private void usageVolumeFee_B(ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData,
            List<MUnitPrice> mUnitPriceList, BigDecimal usageVal,
            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {

        //基本料金算出
        BigDecimal basicCharge = BigDecimal.ZERO;
        BigDecimal basicPrice = Objects.isNull(buildingBillingInfoResultData.getBasicPrice_B())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getBasicPrice_B();
        BigDecimal contractCapacity = Objects.isNull(buildingBillingInfoResultData.getContractCapacity())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getContractCapacity();

        if (1 == usageVal.compareTo(BigDecimal.valueOf(0))) {
            basicCharge = basicPrice.multiply(contractCapacity);
        } else {
            basicCharge = basicPrice
                    .multiply(contractCapacity)
                    .divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_UP);
        }

        resultBillingAmountResultData.setBasicCharge(basicCharge.toString());

        //各種テーブル、テナント料金情報テーブル、料金単価情報テーブル、料金メニュー(従量電灯B)テーブルのレコード存在しない場合
        if (Objects.isNull(buildingBillingInfoResultData.getMVariousPK())
                || Objects.isNull(buildingBillingInfoResultData.getMTenantPriceInfoPK())
                || CollectionUtils.isEmpty(mUnitPriceList)
                || Objects.isNull(buildingBillingInfoResultData.getMPriceMenuLightbPK())) {
            resultBillingAmountResultData.setUsageVolumeFee("0");
            resultBillingAmountResultData.setFuelAdjustmentFee("0");
            resultBillingAmountResultData.setRenewableEnergyLevy("0");
            resultBillingAmountResultData.setHighVoltageBulkDiscount("0");
            resultBillingAmountResultData.setDiscountAmount("0");
            resultBillingAmountResultData.setUsageFee("0");
            return;
        }

        //従量料金算出
        Long prevUsageVal = 0L;
        BigDecimal usageVolumeFee = BigDecimal.ZERO;

        for (MUnitPrice mUnitPrice : mUnitPriceList) {
            BigDecimal unitPrice = Objects.isNull(mUnitPrice.getUnitPrice()) ? BigDecimal.ZERO
                    : mUnitPrice.getUnitPrice();

            if (1 == usageVal.compareTo(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal()))) {
                usageVolumeFee = usageVolumeFee
                        .add(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal() - prevUsageVal)
                                .multiply(unitPrice));
                prevUsageVal = mUnitPrice.getId().getLimitUsageVal();
            } else {
                usageVolumeFee = usageVolumeFee
                        .add(usageVal.subtract(BigDecimal.valueOf(prevUsageVal)).multiply(unitPrice));
                break;
            }
        }
        // 小数第2位で切り捨て
        usageVolumeFee = usageVolumeFee.setScale(2, RoundingMode.DOWN);

        resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());

        //燃料調整額算出
        BigDecimal fuelAdjustmentFee = BigDecimal.ZERO;
        BigDecimal fuelAdjustPrice = Objects.isNull(buildingBillingInfoResultData.getFuelAdjustPrice_B())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getFuelAdjustPrice_B();

        fuelAdjustmentFee = usageVal.multiply(fuelAdjustPrice);
        // 小数第2位で指定
        fuelAdjustmentFee = fuelAdjustmentFee.setScale(2, RoundingMode.DOWN);

        resultBillingAmountResultData.setFuelAdjustmentFee(fuelAdjustmentFee.toString());

        //再エネ賦課金算出
        BigDecimal renewableEnergyLevy = BigDecimal.ZERO;
        BigDecimal renewEnerPrice = Objects.isNull(buildingBillingInfoResultData.getRenewEnerPrice_B())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getRenewEnerPrice_B();

        renewableEnergyLevy = usageVal.multiply(renewEnerPrice);
        // 小数第2位で切り捨て
        renewableEnergyLevy = renewableEnergyLevy.setScale(2, RoundingMode.DOWN);
        resultBillingAmountResultData.setRenewableEnergyLevy(renewableEnergyLevy.toString());

        // 高圧一括割額算出
        BigDecimal highVoltageBulkDiscount = BigDecimal.ZERO;

        resultBillingAmountResultData.setHighVoltageBulkDiscount(highVoltageBulkDiscount.toString());

        //割引額算出
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal discountRate = Objects.isNull(buildingBillingInfoResultData.getDiscountRate())
                ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getDiscountRate();

        discountAmount = basicCharge.add(usageVolumeFee).add(fuelAdjustmentFee).add(renewableEnergyLevy)
                .subtract(highVoltageBulkDiscount).multiply(discountRate
                        .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));

        resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());

        //使用料金算出
        BigDecimal usageFee = BigDecimal.ZERO;

        usageFee = basicCharge.add(usageVolumeFee).add(fuelAdjustmentFee).add(renewableEnergyLevy)
                .subtract(highVoltageBulkDiscount).subtract(discountAmount);
        usageFee = fractionalProcessing(buildingBillingInfoResultData.getFractionalProcessing(), usageFee);

        resultBillingAmountResultData.setUsageFee(usageFee.toString());
    }

    /**
     * その他の請求金額算出
     * 計算に必要なカラムがNULLの場合は0として処理
     *
     * @param buildingBillingInfoResultData DBレスポンスデータ
     * @param mUnitPriceList
     * @param usageVal
     * @param resultBillingAmountResultData APIレスポンスパラメーター用(参照で詰めていく)
     * @throws Exception
     */
    private void usageVolumeFee_Other(ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData,
            List<MUnitPrice> mUnitPriceList, BigDecimal usageVal,
            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {


        //基本料金算出: 基本料金の値があれば必ず表示する
        BigDecimal basicCharge = Objects.isNull(buildingBillingInfoResultData.getBasicPrice()) ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getBasicPrice();

        resultBillingAmountResultData.setBasicCharge(basicCharge.toString());

        //各種テーブル テナント料金情報テーブル 料金単価情報テーブルが存在しない場合
        if (Objects.isNull(buildingBillingInfoResultData.getMVariousPK())
                || Objects.isNull(buildingBillingInfoResultData.getMTenantPriceInfoPK())
                || CollectionUtils.isEmpty(mUnitPriceList)) {
            resultBillingAmountResultData.setUsageVolumeFee("0");
            resultBillingAmountResultData.setHighVoltageBulkDiscount("0");
            resultBillingAmountResultData.setDiscountAmount("0");
            resultBillingAmountResultData.setUsageFee("0");

            return;
        }

        //従量料金算出
        Long prevUsageVal = 0L;
        BigDecimal usageVolumeFee = BigDecimal.ZERO;

        for (MUnitPrice mUnitPrice : mUnitPriceList) {
            BigDecimal unitPrice = Objects.isNull(mUnitPrice.getUnitPrice()) ? BigDecimal.ZERO
                    : mUnitPrice.getUnitPrice();

            if (1 == usageVal.compareTo(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal()))) {
                usageVolumeFee = usageVolumeFee
                        .add(BigDecimal.valueOf(mUnitPrice.getId().getLimitUsageVal() - prevUsageVal)
                                .multiply(unitPrice));
                prevUsageVal = mUnitPrice.getId().getLimitUsageVal();
            } else {
                usageVolumeFee = usageVolumeFee
                        .add(usageVal.subtract(BigDecimal.valueOf(prevUsageVal)).multiply(unitPrice));
                break;
            }
        }
        // 小数第2位で切り捨て
        usageVolumeFee = usageVolumeFee.setScale(2, RoundingMode.DOWN);

        resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());

        // 高圧一括割額算出
        BigDecimal highVoltageBulkDiscount = BigDecimal.ZERO;
        resultBillingAmountResultData.setHighVoltageBulkDiscount(highVoltageBulkDiscount.toString());

        //割引額算出
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal descountRate = Objects.isNull(buildingBillingInfoResultData.getDiscountRate()) ? BigDecimal.ZERO
                : buildingBillingInfoResultData.getDiscountRate();

        discountAmount = basicCharge.add(usageVolumeFee).multiply(descountRate
                .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));

        resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());

        //使用料金算出
        BigDecimal usageFee = BigDecimal.ZERO;

        usageFee = basicCharge.add(usageVolumeFee).subtract(discountAmount);
        usageFee = fractionalProcessing(buildingBillingInfoResultData.getFractionalProcessing(), usageFee);

        resultBillingAmountResultData.setUsageFee(usageFee.toString());
    }

    public void createBillingAmountCalculation(BigDecimal usageFees,
            ListSmsBillingAmountDataResultData billingAmountDataResultData) {

        //グループ按分小計
        BigDecimal meterGroupTotalCharges = BigDecimal.ZERO;

        for (ListSmsBillingAmountDataGroupTotalChargeResultData meterGroup : billingAmountDataResultData
                .getMeterGroupTotalChargeList()) {
            meterGroupTotalCharges = meterGroupTotalCharges.add(new BigDecimal(meterGroup.getGroupTotalCharge()));
        }
        meterGroupTotalCharges = fractionalProcessing(billingAmountDataResultData.getFractionalProcessing(),
                meterGroupTotalCharges);

        billingAmountDataResultData.setMeterGroupTotalCharges(meterGroupTotalCharges.toString());

        //固定費小計
        BigDecimal fixedCosts = BigDecimal.ZERO;

        for (ListSmsBillingAmountDataFixedCostResultData fixedCost : billingAmountDataResultData.getFixedCostList()) {
            if (null == fixedCost.getFixedCost() || fixedCost.getFixedCost().isEmpty()) {
                continue;
            }
            fixedCosts = fixedCosts.add(BigDecimal.valueOf(Integer.parseInt(fixedCost.getFixedCost())));
        }
        billingAmountDataResultData.setFixedCosts(fixedCosts.toString());

        //メーター小計
        billingAmountDataResultData.setUsageFees(usageFees.toString());

        //請求金額
        BigDecimal billingAmount = BigDecimal.ZERO;
        BigDecimal taxExcludedAmount = BigDecimal.ZERO;

        taxExcludedAmount = usageFees.add(fixedCosts).add(meterGroupTotalCharges);
        //消費税
        BigDecimal consumptionTax = BigDecimal.ZERO;

        //消費税率
        BigDecimal salesTaxRate = new BigDecimal(billingAmountDataResultData.getSalesTaxRate());
        if (billingAmountDataResultData.getSalesTaxTreatment().equals("1")) {
            //内税
            consumptionTax = taxExcludedAmount
                    .multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP))
                    .divide(BigDecimal.valueOf(1)
                            .add(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP)), 3,
                            RoundingMode.HALF_UP);
            //請求金額
            billingAmount = taxExcludedAmount;
        } else {
            //外税
            consumptionTax = taxExcludedAmount
                    .multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
            //請求金額に外税加算
            billingAmount = taxExcludedAmount.add(consumptionTax);
        }
        consumptionTax = fractionalProcessing(billingAmountDataResultData.getFractionalProcessing(), consumptionTax);
        billingAmountDataResultData.setConsumptionTax(consumptionTax.toString());
        taxExcludedAmount = fractionalProcessing(billingAmountDataResultData.getFractionalProcessing(),
                taxExcludedAmount);
        billingAmountDataResultData.setTaxExcludedAmount(taxExcludedAmount.toString());
        billingAmount = fractionalProcessing(billingAmountDataResultData.getFractionalProcessing(), billingAmount);
        billingAmountDataResultData.setBillingAmount(billingAmount.toString());
    }

    /**
     * 数値の丸め計算を行う
     *
     * @param fractionalProcessing
     * @param value
     * @return
     */
    private BigDecimal fractionalProcessing(String fractionalProcessing, BigDecimal value) {

        BigDecimal result = BigDecimal.valueOf(0);

        switch (fractionalProcessing) {
        case "1":
            //四捨五入
            result = value.setScale(0, RoundingMode.HALF_UP);
            break;
        case "2":
            //切り捨て
            result = value.setScale(0, RoundingMode.DOWN);
            break;
        case "3":
            //切り上げ
            result = value.setScale(0, RoundingMode.UP);
            break;
        default:
            //不正値(そのまま返す)
            result = value;
            break;
        }

        return result;
    }

    /**
     * メーターフィルター
     * @return
     */
    private List<ListSmsBillingAmountDataInfoResultData> meterDataFilter (List<ListSmsBillingAmountDataInfoResultData> targetList, ListSmsBillingAmountDataParameter parameter) {
        // メーターフィルター
        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getCorpId());
        keyList.put(BuildingPersonDevDataParam.CORP_ID, corpIdList);

        // 建物ID 必須
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(parameter.getBuildingId());
        keyList.put(BuildingPersonDevDataParam.BUILDING_ID, buildingIdList);

        // ログイン担当者企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(parameter.getLoginCorpId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_CORP_ID, loginCorpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(parameter.getLoginPersonId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_PERSON_ID, personIdList);

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);

        // フィルターリストと重複しているデータ以外を削除
        targetList.removeAll(
                targetList.stream()
                .filter(x -> filterList.stream().noneMatch(y -> y.getDevId().equals(x.getDevId()) && y.getMeterMngId().equals(x.getMeterMngId())))
                .collect(Collectors.toList()));


       return targetList;
    }


}
