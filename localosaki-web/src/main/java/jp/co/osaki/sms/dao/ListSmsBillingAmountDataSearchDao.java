package jp.co.osaki.sms.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.tuple.Triple;
import org.jboss.logging.Logger;

import com.cronutils.utils.StringUtils;

import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDataByTenant;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDetailData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.CalculatedForEachMeterType;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataFixedCostResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataGroupTotalChargeResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterGroup;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.SmsTenantInfoResultDate;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.utility.NumberUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.ListSmsMeterGroupServiceDaoImpl;
import jp.co.osaki.sms.servicedao.MUnitPriceServiceDaoImpl;
import jp.co.osaki.sms.servicedao.TenantSmsServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseConstants;

/**
 * 請求金額データ取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class ListSmsBillingAmountDataSearchDao extends SmsDao {

    private final MUnitPriceServiceDaoImpl mUnitPriceServiceDaoImpl;
    private final TenantSmsServiceDaoImpl tenantSmsServiceDaoImpl;
    private final ListSmsMeterGroupServiceDaoImpl listSmsMeterGroupServiceDaoImpl;
    public static final Logger eventLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.EVENT.getVal());

    public ListSmsBillingAmountDataSearchDao() {
        mUnitPriceServiceDaoImpl = new MUnitPriceServiceDaoImpl();
        tenantSmsServiceDaoImpl = new TenantSmsServiceDaoImpl();
        listSmsMeterGroupServiceDaoImpl = new ListSmsMeterGroupServiceDaoImpl();

    }

    public BillingAmountDataByTenant getBillingAmount(List<SmsTenantInfoResultDate> SmsTenantInfoResultDateList,
            Boolean isProratedChargeFlg, Boolean isFixedCostFlg)
            throws Exception {

        List<ListSmsBillingAmountDataResultData> billingAmountDataResultDataList = new ArrayList<>();

        for (SmsTenantInfoResultDate dataList : SmsTenantInfoResultDateList) {

            Long prevBuildingId = null;
            BigDecimal usageFees = BigDecimal.valueOf(0);
            ListSmsBillingAmountDataResultData billingAmountDataResultData = null;
            List<ListSmsBillingAmountDataDetailResultData> billingAmountResultDataList = null;

            MUnitPrice mUnitPrice = new MUnitPrice();
            MUnitPricePK mUnitPricePK = new MUnitPricePK();

            ListSmsBillingAmountDataInfoResultData listSmsBillingAmountDataInfoResultData = dataList
                    .getListSmsBillingAmountDataInfoResultData();

            mUnitPricePK.setPricePlanId(listSmsBillingAmountDataInfoResultData.getPricePlanId());
            mUnitPrice.setId(mUnitPricePK);

            TInspectionMeterSvr resultTInspectionMeterSvr = dataList.getResultTInspectionMeterSvr();

            String corpId = listSmsBillingAmountDataInfoResultData.getCorpId();
            Long buildingId = listSmsBillingAmountDataInfoResultData.getBuildingId();
            String year = resultTInspectionMeterSvr.getId().getInspYear();
            String month = resultTInspectionMeterSvr.getId().getInspMonth();

            //メーターグループ情報処理
            ListSmsMeterGroup meterGroupTotalChargeResultData = new ListSmsMeterGroup();
            meterGroupTotalChargeResultData.setCorpId(corpId);
            meterGroupTotalChargeResultData.setBuildingId(buildingId);
            meterGroupTotalChargeResultData.setYear(year);
            meterGroupTotalChargeResultData.setMonth(month);

            //メーターグループ情報取得処理
            List<ListSmsMeterGroup> listSmsMeterGroupList = getResultList(
                    listSmsMeterGroupServiceDaoImpl, meterGroupTotalChargeResultData);

            //料金単価情報取得処理
            List<MUnitPrice> resultMUnitPriceList = getResultList(mUnitPriceServiceDaoImpl, mUnitPrice);

            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData = new ListSmsBillingAmountDataDetailResultData();
            resultBillingAmountResultData.setBuildingBillingInfoResultData(listSmsBillingAmountDataInfoResultData);
            resultBillingAmountResultData.setResultMUnitPriceList(resultMUnitPriceList);
            resultBillingAmountResultData.setResultTInspectionMeterSvr(resultTInspectionMeterSvr);

            // 料金計算
            createBillingAmountDataDetailResultData(listSmsBillingAmountDataInfoResultData, resultTInspectionMeterSvr,
                    resultMUnitPriceList, resultBillingAmountResultData);

            if (prevBuildingId != listSmsBillingAmountDataInfoResultData.getBuildingId()) {

                usageFees = BigDecimal.valueOf(0);
                billingAmountDataResultData = new ListSmsBillingAmountDataResultData();
                billingAmountResultDataList = new ArrayList<>();

                billingAmountDataResultData.setBuildingNo(listSmsBillingAmountDataInfoResultData.getBuildingNo());
                billingAmountDataResultData.setBuildingName(listSmsBillingAmountDataInfoResultData.getBuildingName());
                billingAmountDataResultData
                        .setBuildingId(listSmsBillingAmountDataInfoResultData.getBuildingId().toString());
                billingAmountDataResultData.setTenantId(listSmsBillingAmountDataInfoResultData.getTenantId());

                billingAmountDataResultData.setInspYear(Integer.parseInt(resultTInspectionMeterSvr.getId().getInspYear()));
                billingAmountDataResultData.setInspMonth(Integer.parseInt(resultTInspectionMeterSvr.getId().getInspMonth()));
                billingAmountDataResultData.setInspMonthNo(resultTInspectionMeterSvr.getId().getInspMonthNo().longValue());
                billingAmountDataResultData.settInspectionMeterSvr(resultTInspectionMeterSvr);

                // グループ1～10按分率 取得
                MTenantSm mTenantSmsTarget = new MTenantSm();
                mTenantSmsTarget.setId(new MTenantSmPK());
                mTenantSmsTarget.getId().setCorpId(listSmsBillingAmountDataInfoResultData.getCorpId());
                mTenantSmsTarget.getId().setBuildingId(listSmsBillingAmountDataInfoResultData.getBuildingId()); // 建物ID(テナント)
                MTenantSm mTenantSmsEntity = find(tenantSmsServiceDaoImpl, mTenantSmsTarget);

                // グループ1～10按分率 をセットして、グループ合計料金(groupTotalCharge)を算出する
                List<ListSmsBillingAmountDataGroupTotalChargeResultData> resultMeterGroupTotalChargeResultDataList = new ArrayList<>();

                if (!(listSmsMeterGroupList == null)) {
                    for (int i = 0; i < 10; i++) {
                        //按分料金チェックボックスにチェックが付いていない場合はグループ料金を0にする
                        if (isProratedChargeFlg) {
                            if (i < listSmsMeterGroupList.size()) {
                                resultMeterGroupTotalChargeResultDataList
                                        .add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                                listSmsMeterGroupList.get(i).getMeterGroupName(),
                                                listSmsMeterGroupList.get(i).getMeterGroupId(),
                                                listSmsMeterGroupList.get(i).getGroupPrice(), mTenantSmsEntity));
                            } else {
                                resultMeterGroupTotalChargeResultDataList
                                        .add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                                null, null, null, null));
                            }
                        } else {
                            if (i < listSmsMeterGroupList.size()) {
                                resultMeterGroupTotalChargeResultDataList
                                        .add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                                listSmsMeterGroupList.get(i).getMeterGroupName(),
                                                listSmsMeterGroupList.get(i).getMeterGroupId(),
                                                BigDecimal.ZERO, mTenantSmsEntity));
                            } else {
                                resultMeterGroupTotalChargeResultDataList
                                        .add(new ListSmsBillingAmountDataGroupTotalChargeResultData(
                                                null, null, null, null));
                            }
                        }

                    }
                }

                billingAmountDataResultData.setMeterGroupTotalChargeList(resultMeterGroupTotalChargeResultDataList);

                List<ListSmsBillingAmountDataFixedCostResultData> fixedCostResultDataList = new ArrayList<>();

                //固定費チェックボックスにチェックが付いていない場合は固定費を0にする
                if (isFixedCostFlg) {
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData1 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName1(),
                            listSmsBillingAmountDataInfoResultData.getFixedCost1(),
                            String.format("%,d", Integer.parseInt(listSmsBillingAmountDataInfoResultData.getFixedCost1())));
                    fixedCostResultDataList.add(fixedCostResultData1);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData2 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName2(),
                            listSmsBillingAmountDataInfoResultData.getFixedCost2(),
                            String.format("%,d", Integer.parseInt(listSmsBillingAmountDataInfoResultData.getFixedCost2())));
                    fixedCostResultDataList.add(fixedCostResultData2);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData3 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName3(),
                            listSmsBillingAmountDataInfoResultData.getFixedCost3(),
                            String.format("%,d", Integer.parseInt(listSmsBillingAmountDataInfoResultData.getFixedCost3())));
                    fixedCostResultDataList.add(fixedCostResultData3);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData4 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName4(),
                            listSmsBillingAmountDataInfoResultData.getFixedCost4(),
                            String.format("%,d", Integer.parseInt(listSmsBillingAmountDataInfoResultData.getFixedCost4())));
                    fixedCostResultDataList.add(fixedCostResultData4);
                    billingAmountDataResultData.setFixedCostList(fixedCostResultDataList);
                }else {
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData1 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName1(),
                            "0",
                            "0");
                    fixedCostResultDataList.add(fixedCostResultData1);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData2 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName2(),
                            "0",
                            "0");
                    fixedCostResultDataList.add(fixedCostResultData2);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData3 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName3(),
                            "0",
                            "0");
                    fixedCostResultDataList.add(fixedCostResultData3);
                    ListSmsBillingAmountDataFixedCostResultData fixedCostResultData4 = new ListSmsBillingAmountDataFixedCostResultData(
                            listSmsBillingAmountDataInfoResultData.getFixedCostName4(),
                            "0",
                            "0");
                    fixedCostResultDataList.add(fixedCostResultData4);
                    billingAmountDataResultData.setFixedCostList(fixedCostResultDataList);

                }

                // 各種設定テーブルがなければ固定値
                if (Objects.isNull(listSmsBillingAmountDataInfoResultData.getMVariousPK())) {
                    billingAmountDataResultData.setSalesTaxRate(SmsConstants.SALE_TAX_RATE);
                    billingAmountDataResultData.setSalesTaxTreatment(SmsConstants.SALE_TAX_DEAL);
                    billingAmountDataResultData
                            .setFractionalProcessing(SmsConstants.DECIMAL_FRACTION);
                } else {
                    String salesTaxRate = Objects.isNull(listSmsBillingAmountDataInfoResultData.getSalesTaxRate())
                            ? SmsConstants.SALE_TAX_RATE
                            : listSmsBillingAmountDataInfoResultData.getSalesTaxRate().toString();
                    billingAmountDataResultData.setSalesTaxRate(salesTaxRate);

                    String salesTaxTreatment = StringUtils
                            .isEmpty(listSmsBillingAmountDataInfoResultData.getSalesTaxTreatment())
                                    ? SmsConstants.SALE_TAX_DEAL
                                    : listSmsBillingAmountDataInfoResultData.getSalesTaxTreatment();
                    billingAmountDataResultData.setSalesTaxTreatment(salesTaxTreatment);

                    String fractionalProcessing = StringUtils
                            .isEmpty(listSmsBillingAmountDataInfoResultData.getFractionalProcessing())
                                    ? SmsConstants.DECIMAL_FRACTION
                                    : listSmsBillingAmountDataInfoResultData.getFractionalProcessing();
                    billingAmountDataResultData
                            .setFractionalProcessing(fractionalProcessing);
                }

                //メーター種別名を格納
                billingAmountDataResultData.setMeterType(listSmsBillingAmountDataInfoResultData.getMeterType());
                billingAmountDataResultData.setMeterTypeName(listSmsBillingAmountDataInfoResultData.getMeterTypeName());

                //装置IDを格納
                billingAmountDataResultData.setDevId(listSmsBillingAmountDataInfoResultData.getDevId());

                //計器IDを格納
                billingAmountDataResultData.setMeterMngId(listSmsBillingAmountDataInfoResultData.getMeterMngId());

                billingAmountDataResultData.setBillingAmountDataList(billingAmountResultDataList);
                billingAmountDataResultDataList.add(billingAmountDataResultData);

            }

            usageFees = usageFees.add(new BigDecimal(resultBillingAmountResultData.getUsageFee()));

            billingAmountResultDataList.add(resultBillingAmountResultData);

            prevBuildingId = listSmsBillingAmountDataInfoResultData.getBuildingId();

            if (null != prevBuildingId) {
                //請求金額・消費税
                createBillingAmountCalculation(usageFees, billingAmountDataResultData, isProratedChargeFlg,
                        isFixedCostFlg);
                //ステータス
                billingAmountDataResultData.setStatus(resultBillingAmountResultData.getBillingAmountStatus());
            }
        }

        //使用量を合算処理
        //メーター種別単位でKeyMapを作成
        MultiKeyMap<String, CalculatedForEachMeterType> meterTypeNameMap = new MultiKeyMap<String, CalculatedForEachMeterType>();

        for (ListSmsBillingAmountDataResultData listSmsBillingAmountDataResultData : billingAmountDataResultDataList) {
            String meterTypeName = listSmsBillingAmountDataResultData.getMeterTypeName();
            String devId = listSmsBillingAmountDataResultData.getDevId();
            String meterMngId = listSmsBillingAmountDataResultData.getMeterMngId().toString();

            if (meterTypeNameMap.get(devId,meterMngId,meterTypeName) != null
                    || meterTypeNameMap.containsKey(devId,meterMngId,meterTypeName)) {

                CalculatedForEachMeterType calculatedForEachMeterType = meterTypeNameMap.get(devId,meterMngId,meterTypeName);

                calculatedForEachMeterType.getAmountDatas().add(listSmsBillingAmountDataResultData);

//                meterTypeNameMap.put(devId, meterMngId, meterTypeName, calculatedForEachMeterType);

                //使用料を合算する
                BigDecimal sumUsageFees = calculatedForEachMeterType.getSumUsageFees();
                calculatedForEachMeterType.setSumUsageFees(
                        sumUsageFees.add(new BigDecimal(listSmsBillingAmountDataResultData.getUsageFees())));
                meterTypeNameMap.put(devId,meterMngId,meterTypeName, calculatedForEachMeterType);

            } else {

                CalculatedForEachMeterType calculatedForEachMeterType = new CalculatedForEachMeterType();

                calculatedForEachMeterType.getAmountDatas().add(listSmsBillingAmountDataResultData);

//                meterTypeNameMap.put(devId, meterMngId, meterTypeName, calculatedForEachMeterType);

                calculatedForEachMeterType
                        .setSumUsageFees(new BigDecimal(listSmsBillingAmountDataResultData.getUsageFees()));
                calculatedForEachMeterType.setListSmsBillingAmountDataResultData(listSmsBillingAmountDataResultData);
                meterTypeNameMap.put(devId,meterMngId,meterTypeName,calculatedForEachMeterType);
            }
        }

        billingAmountDataResultDataList.stream();

		//メーター種別ごとに請求金額を計算
		List<ListSmsBillingAmountDataResultData> amountDataResultList = new ArrayList<>();

		for (CalculatedForEachMeterType calculatedForEachMeterType : meterTypeNameMap.values()) {

			// 検針年、検針月、月検針番号でソート
			List<ListSmsBillingAmountDataResultData> sortedAmountDatas //
				= calculatedForEachMeterType.getAmountDatas().stream() //
					.sorted( //
							Comparator.comparing(ListSmsBillingAmountDataResultData::getInspYear) // 検針年
							.thenComparing(ListSmsBillingAmountDataResultData::getInspMonth) // 検針月
							.thenComparing(ListSmsBillingAmountDataResultData::getInspMonthNo)) // 月検針番号
//					.peek(e -> System.out.println("---- " + e)) //
					.collect(Collectors.toList()); // ソート

			// 合算使用料
			BigDecimal usageFeesSum = BigDecimal.ZERO;

			// 合算今回使用量
			BigDecimal usageValSum = BigDecimal.ZERO;

			// 今回検針値(最新のレコードを参照)
			BigDecimal latestInspVal = BigDecimal.ZERO;

			// 前回検針値(最古のレコードを参照)
			BigDecimal prevInspVal = BigDecimal.ZERO;

			// 最古の検針日
			String oldestInspDate = null;

			// 請求データが存在する場合に以下を計算
			if (!sortedAmountDatas.isEmpty()) {

				// 検針完了で抽出
				List<ListSmsBillingAmountDataResultData> compBillingAmountDataResultDatas = sortedAmountDatas.stream() //
						.filter(e -> BigDecimal.ONE.equals(e.gettInspectionMeterSvr().getEndFlg())) // 処理が「完了」のものでフィルタ
						.collect(Collectors.toList());

				usageFeesSum = compBillingAmountDataResultDatas.stream() //
						.map(e -> new BigDecimal(e.getUsageFees())) //
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				usageValSum = compBillingAmountDataResultDatas.stream()
						.map(e -> new BigDecimal(e.getBillingAmountDataList().get(0).getUsageVal())) //
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				latestInspVal = compBillingAmountDataResultDatas //
						.get(sortedAmountDatas.size() - 1).gettInspectionMeterSvr().getLatestInspVal();

				prevInspVal = compBillingAmountDataResultDatas //
						.get(0).gettInspectionMeterSvr().getPrevInspVal();

				if (compBillingAmountDataResultDatas.get(0).gettInspectionMeterSvr().getPrevInspDate() != null) {
					oldestInspDate = new SimpleDateFormat("yyyy/MM/dd") //
							.format(compBillingAmountDataResultDatas.get(0).gettInspectionMeterSvr().getPrevInspDate());
				}
			}

			ListSmsBillingAmountDataResultData billingAmountDataResultData = calculatedForEachMeterType
					.getListSmsBillingAmountDataResultData();

			// TODO とりあえずココに書き込む(不自然なので直すかも)
			// 使用量
			billingAmountDataResultData.getBillingAmountDataList().get(0).setUsageVal(usageValSum.toString());
			// 今回検針値
			if (latestInspVal != null) {
				billingAmountDataResultData.getBillingAmountDataList().get(0).setLatestInspVal(latestInspVal.toString());
			}
			// 前回検針値
			if (prevInspVal != null) {
				billingAmountDataResultData.getBillingAmountDataList().get(0).setPrevInspVal(prevInspVal.toString());
			}
			// 最古の検針日
			billingAmountDataResultData.getBillingAmountDataList().get(0).setOldestInspDate(oldestInspDate);

			amountRecalculation(usageValSum, billingAmountDataResultData.getBillingAmountDataList().get(0));

			//請求金額・消費税
			createBillingAmountCalculation( //
					new BigDecimal(billingAmountDataResultData.getBillingAmountDataList().get(0).getUsageFee()), //
					billingAmountDataResultData, //
					isProratedChargeFlg, //
					isFixedCostFlg);

			amountDataResultList.add(billingAmountDataResultData);
		}

		List<BillingAmountDetailData> amountDetails = new ArrayList<BillingAmountDetailData>();

		amountDataResultList.stream().sorted(Comparator.comparing(ListSmsBillingAmountDataResultData::getMeterMngId)).forEach(e -> {

			BillingAmountDetailData detailData = new BillingAmountDetailData();
			detailData.setMeterType(e.getMeterType());
			detailData.setMeterTypeName(e.getMeterTypeName());
			// TODO 以下の分岐が画面の<c:if>でうまく動かなかった為、こちらで実装する
			// 電灯の場合はメニュー名
			if (e.getMeterType() == 1) {
				detailData.setPriceMenuName(e.getBillingAmountDataList().get(0).getPriceMenuName());
			}
			// 電灯以外はガス、水道等のメーター種別名とする
			else {
				detailData.setPriceMenuName(e.getMeterTypeName());
			}
			detailData.setDevId(e.getDevId());
			detailData.setMeterMngId(e.getMeterMngId());
			detailData.setMeterId(e.getBillingAmountDataList().get(0).getMeterId());
			detailData.setMemo(e.getBillingAmountDataList().get(0).getMemo());
			detailData.setStatus(e.getStatus());
			detailData.setThisInspVal(e.getBillingAmountDataList().get(0).getLatestInspVal());
			detailData.setPrevInspVal(e.getBillingAmountDataList().get(0).getPrevInspVal());
			detailData.setMultipleRate(e.getBillingAmountDataList().get(0).getMultipleRate());
			detailData.setUsageVal(e.getBillingAmountDataList().get(0).getUsageVal());
			detailData.setBasicCharge(e.getBillingAmountDataList().get(0).getBasicCharge());
			detailData.setDispBasicCharge(e.getBillingAmountDataList().get(0).getBasicCharge() != null ? NumberUtility.numberToStringKeepDeciDigit(e.getBillingAmountDataList().get(0).getBasicCharge(), 2, true) : e.getBillingAmountDataList().get(0).getBasicCharge());
			detailData.setUsageVolumeFee(e.getBillingAmountDataList().get(0).getUsageVolumeFee());
			detailData.setDispUsageVolumeFee(e.getBillingAmountDataList().get(0).getUsageVolumeFee() != null ? NumberUtility.numberToStringKeepDeciDigit(e.getBillingAmountDataList().get(0).getUsageVolumeFee(), 2, true) : e.getBillingAmountDataList().get(0).getUsageVolumeFee());
			detailData.setFuelAdjustmentFee(e.getBillingAmountDataList().get(0).getFuelAdjustmentFee());
			detailData.setDispFuelAdjustmentFee(e.getBillingAmountDataList().get(0).getFuelAdjustmentFee() != null ? NumberUtility.numberToStringKeepDeciDigit(e.getBillingAmountDataList().get(0).getFuelAdjustmentFee(), 2, true) : e.getBillingAmountDataList().get(0).getFuelAdjustmentFee());
			detailData.setRenewableEnergyLevy(e.getBillingAmountDataList().get(0).getRenewableEnergyLevy());
			detailData.setDispRenewableEnergyLevy(e.getBillingAmountDataList().get(0).getRenewableEnergyLevy() != null ? NumberUtility.numberToStringKeepDeciDigit(e.getBillingAmountDataList().get(0).getRenewableEnergyLevy(), 2, true) : e.getBillingAmountDataList().get(0).getRenewableEnergyLevy());
			detailData.setDiscountAmount(e.getBillingAmountDataList().get(0).getDiscountAmount());
			detailData.setDispDiscountAmount(e.getBillingAmountDataList().get(0).getDiscountAmount() != null ? NumberUtility.numberToStringKeepDeciDigit(e.getBillingAmountDataList().get(0).getDiscountAmount(), 2, true) : e.getBillingAmountDataList().get(0).getDiscountAmount());
			detailData.setUsageFee(e.getBillingAmountDataList().get(0).getUsageFee());
			detailData.setDispUsageFee(String.format("%,d", Integer.parseInt(e.getBillingAmountDataList().get(0).getUsageFee())));
			detailData.setTax(e.getConsumptionTax());
			detailData.setDispTax(String.format("%,d", Integer.parseInt(e.getConsumptionTax())));
			detailData.setUnitUsageBased(e.getBillingAmountDataList().get(0).getUnitUsageBased());
			detailData.setLatestInspDate(e.getBillingAmountDataList().get(0).getLatestInspDate());
			detailData.setOldestInspDate(e.getBillingAmountDataList().get(0).getOldestInspDate());
			detailData.setDispInspDate(e.getBillingAmountDataList().get(0).getOldestInspDate() != null ? e.getBillingAmountDataList().get(0).getOldestInspDate() + "～" + e.getBillingAmountDataList().get(0).getLatestInspDate() : e.getBillingAmountDataList().get(0).getLatestInspDate());

			// PDFに出力する従量料金は「従量料金＋燃料調整費」(丸め無し)とする
			BigDecimal calUsageVolumeFee = BigDecimal.ZERO;
			BigDecimal calFuelAdjustmentFee = BigDecimal.ZERO;

			if (e.getBillingAmountDataList().get(0).getUsageVolumeFee() != null) {
				calUsageVolumeFee = new BigDecimal(e.getBillingAmountDataList().get(0).getUsageVolumeFee());
			}
			if (e.getBillingAmountDataList().get(0).getFuelAdjustmentFee() != null) {
				calFuelAdjustmentFee = new BigDecimal(e.getBillingAmountDataList().get(0).getFuelAdjustmentFee());
			}
			detailData.setUsageVolumeFeeForPdf(calUsageVolumeFee.add(calFuelAdjustmentFee).toString());

			amountDetails.add(detailData);
		});

		// メーター小計
		BigDecimal meterSubtotal = amountDetails.stream() //
				.map(e -> new BigDecimal(e.getUsageFee())) //
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 固定費小計
		BigDecimal fixedCostSubtotal = amountDataResultList.get(0).getFixedCostList().stream() //
				.map(e -> new BigDecimal(e.getFixedCost())) //
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// グループ按分小計
		BigDecimal groupProrateSubtotal = BigDecimal.ZERO; // FIXME

		// 請求合計
		BigDecimal sumUsageFee = meterSubtotal.add(fixedCostSubtotal).add(groupProrateSubtotal);

		// 消費税
		BigDecimal sumTax = BigDecimal.ZERO;

		String salesTaxTreatment = amountDataResultList.get(0).getSalesTaxTreatment();
		BigDecimal salesTaxRate = new BigDecimal(amountDataResultList.get(0).getSalesTaxRate());
		String fractionalProcessing = amountDataResultList.get(0).getFractionalProcessing();

		// 消費税
		if (salesTaxTreatment.equals(SmsConstants.TAX_TYPE.INCLUDED.getVal())) {

			// 内税
			sumTax = sumUsageFee
					.multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP))
					.divide(BigDecimal.valueOf(1)
							.add(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP)), 3,
							RoundingMode.HALF_UP);
		}
		else {

			// 外税
			sumTax = sumUsageFee
					.multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
			//請求金額に外税加算
			sumUsageFee = sumUsageFee.add(sumTax);
		}

		// 小数部端数処理
		// 四捨五入の場合
		if ("1".equals(fractionalProcessing)) {
			sumUsageFee = sumUsageFee.setScale(0, RoundingMode.HALF_UP);
			sumTax = sumTax.setScale(0, RoundingMode.HALF_UP);
		}
		// 切り捨ての場合
		else if ("2".equals(fractionalProcessing)) {
			sumUsageFee = sumUsageFee.setScale(0, RoundingMode.DOWN);
			sumTax = sumTax.setScale(0, RoundingMode.DOWN);
		}
		// 切り上げの場合
		else if ("3".equals(fractionalProcessing)) {
			sumUsageFee = sumUsageFee.setScale(0, RoundingMode.UP);
			sumTax = sumTax.setScale(0, RoundingMode.UP);
		}
		else {
			// 処理なし
		}

		List<String> incompleteStatus = amountDataResultList.stream() //
				.map(e -> e.getStatus()) // ステータスを抽出
				.filter(e -> !"完了".equals(e)) // 完了でないものでフィルタ
				.collect(Collectors.toList());

		BillingAmountDataByTenant amountDataByTenant = new BillingAmountDataByTenant();
		// テナントID
		amountDataByTenant.setTenantId(amountDataResultList.get(0).getTenantId());
		// 建物ID
		amountDataByTenant.setBuildingId(amountDataResultList.get(0).getBuildingId());
		// テナント番号
		amountDataByTenant.setBuildingNo(amountDataResultList.get(0).getBuildingNo());
		// テナント名
		amountDataByTenant.setBuildingName(amountDataResultList.get(0).getBuildingName());
		// 請求合計
		amountDataByTenant.setSumUsageFee(sumUsageFee.toString());
		// 消費税
		amountDataByTenant.setSumTax(sumTax.toString());
		// 表示用：請求合計
		amountDataByTenant.setDispSumUsageFee(String.format("%,d", Integer.parseInt(sumUsageFee.toString())));
		// 表示用：消費税
		amountDataByTenant.setDispSumTax(String.format("%,d", Integer.parseInt(sumTax.toString())));
		// 固定費小計
		amountDataByTenant.setFixedCostSubtotal(fixedCostSubtotal.toString());
		// 表示用：固定費小計
		amountDataByTenant.setDispFixedCostSubtotal(String.format("%,d", Integer.parseInt(fixedCostSubtotal.toString())));
		// 集約ステータス
		amountDataByTenant.setAggregateStatus(incompleteStatus.isEmpty() ? "完了" : "未完了あり");
		// メーター小計
		amountDataByTenant.setMeterSubtotal(meterSubtotal.toString());
		// 表示用：メーター小計
		amountDataByTenant.setDispMeterSubtotal(String.format("%,d", Integer.parseInt(meterSubtotal.toString())));
		// 消費税率
		amountDataByTenant.setSalesTaxRate(salesTaxRate.toString());
		// 消費税扱い
		amountDataByTenant.setSalesTaxTreatment(salesTaxTreatment);
		// グループ按分小計
		amountDataByTenant.setGroupProrateSubtotal(groupProrateSubtotal.toString());
		// 表示用：グループ按分小計
		amountDataByTenant.setDispGroupProrateSubtotal(String.format("%,d", Integer.parseInt(groupProrateSubtotal.toString())));
		// グループ按分詳細
		amountDataByTenant.setMeterGroupProrates(amountDataResultList.get(0).getMeterGroupTotalChargeList());
		// 固定費詳細
		amountDataByTenant.setFixedCosts(amountDataResultList.get(0).getFixedCostList());
		// メーター詳細
		amountDataByTenant.setBillingAmountDetails(amountDetails);
		// 小数部端数処理
		amountDataByTenant.setFractionalProcessing(fractionalProcessing);

		eventLogger.debug("-***-" + amountDataByTenant);

        ListSmsBillingAmountDataResult result = new ListSmsBillingAmountDataResult();
        result.setBillingAmountDataResultDataList(amountDataResultList);

        return amountDataByTenant;
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
        resultBillingAmountResultData.setLatestInspVal(Objects.isNull(tInspectionMeterSvr.getLatestInspVal()) ? "-"
                : tInspectionMeterSvr.getLatestInspVal().toString());
        resultBillingAmountResultData.setPrevInspVal(Objects.isNull(tInspectionMeterSvr.getPrevInspVal()) ? "-"
                : tInspectionMeterSvr.getPrevInspVal().toString());
        resultBillingAmountResultData.setMultipleRate(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? null
                : tInspectionMeterSvr.getMultipleRate().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        resultBillingAmountResultData.setLatestInspDate(Objects.isNull(tInspectionMeterSvr.getLatestInspDate()) ? null
                : sdf.format(tInspectionMeterSvr.getLatestInspDate()));

        BigDecimal usageVal = BigDecimal.ZERO;

        if (!Objects.isNull(tInspectionMeterSvr.getLatestInspVal())
                && !Objects.isNull(tInspectionMeterSvr.getPrevInspVal())) {
            if (1 == tInspectionMeterSvr.getPrevInspVal().compareTo(tInspectionMeterSvr.getLatestInspVal())) {
                usageVal = tInspectionMeterSvr.getLatestInspVal().add(BigDecimal.valueOf(100000))
                        .subtract(tInspectionMeterSvr.getPrevInspVal())
                        .multiply(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? BigDecimal.ZERO
                                : tInspectionMeterSvr.getMultipleRate());
            } else {
                usageVal = tInspectionMeterSvr.getLatestInspVal().subtract(tInspectionMeterSvr.getPrevInspVal())
                        .multiply(Objects.isNull(tInspectionMeterSvr.getMultipleRate()) ? BigDecimal.ZERO
                                : tInspectionMeterSvr.getMultipleRate());
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
            usageVolumeFee_A(
            		buildingBillingInfoResultData, //
            		mUnitPriceList, //
            		usageVal, //
            		resultBillingAmountResultData);

        } else if (1 == buildingBillingInfoResultData.getMeterType()
                && "2".equals(buildingBillingInfoResultData.getPriceMenuNo())) {

            //従量電灯Bの請求金額算出
            usageVolumeFee_B(
            		buildingBillingInfoResultData, //
            		mUnitPriceList, //
            		usageVal, //
            		resultBillingAmountResultData);
        } else {

            //その他の請求金額算出
            usageVolumeFee_Other(
            		buildingBillingInfoResultData, //
            		mUnitPriceList, //
            		usageVal, //
                    resultBillingAmountResultData);
        }

    }

    private void amountRecalculation(BigDecimal usageVal, ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {

        if (1 == resultBillingAmountResultData.getBuildingBillingInfoResultData().getMeterType()
                && "1".equals(resultBillingAmountResultData.getBuildingBillingInfoResultData().getPriceMenuNo())) {

            //従量電灯Aの請求金額算出
            usageVolumeFee_A(
            		resultBillingAmountResultData.getBuildingBillingInfoResultData(), //
            		resultBillingAmountResultData.getResultMUnitPriceList(), //
            		usageVal, //
            		resultBillingAmountResultData);

        } else if (1 == resultBillingAmountResultData.getBuildingBillingInfoResultData().getMeterType()
                && "2".equals(resultBillingAmountResultData.getBuildingBillingInfoResultData().getPriceMenuNo())) {

            //従量電灯Bの請求金額算出
            usageVolumeFee_B(
            		resultBillingAmountResultData.getBuildingBillingInfoResultData(), //
            		resultBillingAmountResultData.getResultMUnitPriceList(), //
            		usageVal, //
            		resultBillingAmountResultData);
        } else {

            //その他の請求金額算出
            usageVolumeFee_Other(
            		resultBillingAmountResultData.getBuildingBillingInfoResultData(), //
            		resultBillingAmountResultData.getResultMUnitPriceList(), //
            		usageVal, //
                    resultBillingAmountResultData);
        }
    }

    /**
     * 使用量を料金単価毎に分割し従量範囲の金額を計算。
     *
     * @param usageVal 使用量
     * @param mUnitPriceList 料金単価
     */
    private List<Triple<MUnitPrice, BigDecimal, BigDecimal>> usageValSplit(BigDecimal usageVal, List<MUnitPrice> mUnitPriceList) {

    	if (usageVal == null || mUnitPriceList == null) {
    		throw new RuntimeException("使用量、または料金単価が存在しません。");
    	}

    	List<Triple<MUnitPrice, BigDecimal, BigDecimal>> usageValSplits = new ArrayList<Triple<MUnitPrice, BigDecimal, BigDecimal>>();

    	// 使用量残
    	BigDecimal remaUsageVal = usageVal;

    	// 下限従量値
    	BigDecimal minUsageVal = BigDecimal.ZERO;

    	// 料金単価レコード数分ループ
    	for (MUnitPrice mUnitPrice : mUnitPriceList) {

    		// 上限従量値
    		BigDecimal maxUsageVal = new BigDecimal(mUnitPrice.getId().getLimitUsageVal());

    		// 料金単価
    		BigDecimal price = mUnitPrice.getUnitPrice() == null ? BigDecimal.ZERO : mUnitPrice.getUnitPrice();

    		// 下限従量値～上限従量値のキャパ
    		BigDecimal capacity = maxUsageVal.subtract(minUsageVal);

    		// 使用残量からキャパを引く
    		// この計算結果がプラス値かマイナス値かで以降分岐
    		BigDecimal subVal = remaUsageVal.subtract(capacity);

    		// プラス値の場合、次の従量範囲が存在する
    		if (subVal.compareTo(BigDecimal.ZERO) > 0) {

    			// 次の従量範囲が存在する場合は「キャパ」を使用
    			usageValSplits.add(Triple.of(mUnitPrice, capacity, capacity.multiply(price)));

        		remaUsageVal = subVal;
    		}
    		// マイナス値の場合、現従量範囲に収まる
    		else {

    			// 現従量範囲に収まる場合は「使用量残」を使用
    			usageValSplits.add(Triple.of(mUnitPrice, remaUsageVal, remaUsageVal.multiply(price)));

    			// 使用量残は0となる
        		remaUsageVal = BigDecimal.ZERO;
    		}

    		minUsageVal = maxUsageVal;
    	}

    	usageValSplits.forEach(triple -> { //
    		eventLogger.debug( //
        			"[上限値:" + triple.getLeft().getId().getLimitUsageVal() + "] " + //
    				"[分割使用量:" + triple.getMiddle() + "] " + //
        			"[従量単価:" + triple.getLeft().getUnitPrice() + "] " + //
        			"[従量範囲金額:" + triple.getRight() + "]");
    	});

    	return usageValSplits;
    }

    /**
     * 小数部端数処理よりBigDecimalの"丸め"を取得。
     *
     * @param fractionalProcessing 小数部端数処理(m_various.decimal_fraction)
     */
    private int getBigDecimalRound(String fractionalProcessing) {

    	// 四捨五入
    	if ("1".equals(fractionalProcessing)) {
    		return BigDecimal.ROUND_HALF_UP;
    	}
    	// 切り捨て
    	else if ("2".equals(fractionalProcessing)) {
    		return BigDecimal.ROUND_DOWN;
    	}
    	// 切り上げ
    	else if ("3".equals(fractionalProcessing)) {
    		return BigDecimal.ROUND_UP;
    	}
    	else {
    		// 未設定の場合は四捨五入とする
    		return BigDecimal.ROUND_HALF_UP;
    	}
    }

    /**
     * 従量電灯Aの請求金額算出.
     *
     */
    private void usageVolumeFee_A(ListSmsBillingAmountDataInfoResultData buildingBillingInfoResultData,
            List<MUnitPrice> mUnitPriceList, BigDecimal usageVal,
            ListSmsBillingAmountDataDetailResultData resultBillingAmountResultData) throws Exception {

    	eventLogger.debug("●●● 従量電灯Aの請求金額算出 開始 ●●●");

    	eventLogger.debug(buildingBillingInfoResultData);
    	eventLogger.debug("使用量:" + usageVal);
    	List<Triple<MUnitPrice, BigDecimal, BigDecimal>> usageValSplits = usageValSplit(usageVal, mUnitPriceList);

    	// (従量電灯A)最低料金
    	BigDecimal lowestPriceA = buildingBillingInfoResultData.getLowestPrice_A();
    	// (従量電灯A)燃料費調整額(最初の15kWhまで)
    	BigDecimal fuelAdjustPriceA = buildingBillingInfoResultData.getFuelAdjustPrice_A();
    	// (従量電灯A)燃料費調整額(15kWh超過)
    	BigDecimal fuelAdjustPriceOver15A = buildingBillingInfoResultData.getFuelAdjustPriceOver15_A();
    	// (従量電灯A)再エネ賦課金(最初の15kWhまで)
    	BigDecimal renewEnerPriceA = buildingBillingInfoResultData.getRenewEnerPrice_A();
    	// (従量電灯A)再エネ賦課金(15kWh超過)
    	BigDecimal renewEnerPriceOver15A = buildingBillingInfoResultData.getRenewEnerPriceOver15_A();

    	eventLogger.debug("最低料金:" + lowestPriceA);
    	eventLogger.debug("燃料費調整額(最初の●kWhまで):" + fuelAdjustPriceA);
    	eventLogger.debug("燃料費調整額(●kWh超過):" + fuelAdjustPriceOver15A);
    	eventLogger.debug("再エネ賦課金(最初の●kWhまで):" + renewEnerPriceA);
    	eventLogger.debug("再エネ賦課金(●kWh超過):" + renewEnerPriceOver15A);

    	int round = getBigDecimalRound(buildingBillingInfoResultData.getFractionalProcessing());

    	eventLogger.debug("小数部端数処理(4:四捨五入、1:切り捨て、0:切り上げ):" + round);

    	// ①従量料金算出
    	// 従量範囲1を除いて「従量料金をサマリー」し「最低料金」を加算
    	BigDecimal usageVolumeFee = usageValSplits.subList(1, usageValSplits.size()).stream() // 従量範囲1を除外
    			.map(e -> e.getRight()) // 計算済みの従量範囲料金
    			.reduce(BigDecimal.ZERO, BigDecimal::add) // 従量範囲料金をサマリー
    			.add(lowestPriceA); // 最低料金を加算

    	eventLogger.debug("①従量料金:" + usageVolumeFee);

    	// ②燃料調整費算出
    	// 従量範囲1を除いて「使用量をサマリーし燃料費調整額(15kWh超過)とかけ合わせ」「燃料費調整額(最初の15kWhまで)」を加算
    	BigDecimal fuelAdjustmentFee = usageValSplits.subList(1, usageValSplits.size()).stream() // 従量範囲1を除外
    			.map(e -> e.getMiddle()) // 使用量
    			.reduce(BigDecimal.ZERO, BigDecimal::add) // 使用量をサマリー
    			.multiply(fuelAdjustPriceOver15A) // 燃料費調整額(15kWh超過)とかけ合わせ
    			.add(fuelAdjustPriceA); // 燃料費調整額(最初の15kWhまで)を加算

    	eventLogger.debug("②燃料調整費:" + fuelAdjustmentFee);

    	// ③再エネ賦課金算出
    	// 従量範囲1を除いて「使用量をサマリーし再エネ賦課金(15kWh超過)とかけ合わせ」「再エネ賦課金(最初の15kWhまで)」を加算
    	BigDecimal renewableEnergyLevy = usageValSplits.subList(1, usageValSplits.size()).stream() // 従量範囲1を除外
    			.map(e -> e.getMiddle()) // 使用量
    			.reduce(BigDecimal.ZERO, BigDecimal::add) // 使用量をサマリー
    			.multiply(renewEnerPriceOver15A) // 再エネ賦課金(15kWh超過)とかけ合わせ
    			.add(renewEnerPriceA); // 再エネ賦課金(最初の15kWhまで)を加算

    	eventLogger.debug("③再エネ賦課金:" + renewableEnergyLevy);

    	// 割引率
    	BigDecimal discountRate = //
    			buildingBillingInfoResultData.getDiscountRate() == null ? //
    					BigDecimal.ZERO : buildingBillingInfoResultData.getDiscountRate();

    	eventLogger.debug("割引率(%):" + discountRate);

    	// 使用料金(元金)計算
    	// 使用料金(元金) ＝ ( ①従量料金 ＋ ②燃料調整費 ) ＋ ( ③再エネ賦課金 )
    	BigDecimal usageFeePrincipal = ((usageVolumeFee.add(fuelAdjustmentFee).setScale(0, round)) // ①従量料金 ＋ ②燃料調整費：整数値に丸め
    	.add( //
    			renewableEnergyLevy.setScale(0, round)) // ＋ ( ③再エネ賦課金 )：整数値に丸め
    	); //

    	// 使用料金(割引後)計算
    	// 使用料金(割引後) ＝ 使用料金(元金) × (100% － 割引率)
    	BigDecimal usageFee = (usageFeePrincipal.multiply((new BigDecimal(100).subtract(discountRate)).divide(new BigDecimal(100)))).setScale(0, round);

    	// 割引額算出
    	BigDecimal discountAmount = usageFeePrincipal.subtract(usageFee);

    	eventLogger.debug("使用料金(元金):" + usageFeePrincipal);

    	eventLogger.debug("割引額:" + discountAmount);

    	eventLogger.debug("使用料金(割引後):" + usageFee);

    	// 使用量
    	resultBillingAmountResultData.setUsageVal(usageVal.toString());
    	// 従量料金
    	resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());
    	// 燃料調整費
    	resultBillingAmountResultData.setFuelAdjustmentFee(fuelAdjustmentFee.toString());
    	// 再エネ賦課金
    	resultBillingAmountResultData.setRenewableEnergyLevy(renewableEnergyLevy.toString());
    	// 高圧一括割額
    	resultBillingAmountResultData.setHighVoltageBulkDiscount(BigDecimal.ZERO.toString());
    	// 割引額
    	resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());
    	// 使用料金(割引後)※マイナスの場合は0とする
    	if (BigDecimal.ZERO.compareTo(usageFee) > 0 ) {
    		usageFee = BigDecimal.ZERO;
    	}
    	resultBillingAmountResultData.setUsageFee(usageFee.toString());

    	eventLogger.debug(resultBillingAmountResultData);

    	eventLogger.debug("●●● 従量電灯Aの請求金額算出 終了 ●●●");
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

    	eventLogger.debug("◆◆◆ 従量電灯Bの請求金額算出 開始 ◆◆◆");

    	eventLogger.debug(buildingBillingInfoResultData);
    	eventLogger.debug("使用量:" + usageVal);
    	List<Triple<MUnitPrice, BigDecimal, BigDecimal>> usageValSplits = usageValSplit(usageVal, mUnitPriceList);

    	// (従量電灯B)基本料金
    	BigDecimal basicPriceB = buildingBillingInfoResultData.getBasicPrice_B();
    	// (従量電灯B)燃料費調整額
    	BigDecimal fuelAdjustPriceB = buildingBillingInfoResultData.getFuelAdjustPrice_B();
    	// (従量電灯B)再エネ賦課金
    	BigDecimal renewEnerPriceB = buildingBillingInfoResultData.getRenewEnerPrice_B();

    	eventLogger.debug("基本料金:" + basicPriceB);
    	eventLogger.debug("燃料費調整額:" + fuelAdjustPriceB);
    	eventLogger.debug("再エネ賦課金:" + renewEnerPriceB);

    	int round = getBigDecimalRound(buildingBillingInfoResultData.getFractionalProcessing());

    	eventLogger.debug("小数部端数処理(4:四捨五入、1:切り捨て、0:切り上げ):" + round);

    	// ①基本料金算出
    	BigDecimal basicCharge = basicPriceB;
    	// 使用量が「0」の場合、基本料金は半額とする。
    	if (BigDecimal.ZERO.compareTo(usageVal) == 0) {
    		eventLogger.debug("使用量が0なので基本料金は半額");
    		basicCharge = basicPriceB.divide(new BigDecimal(2));
    	}

    	eventLogger.debug("①基本料金:" + basicCharge);

    	// ②従量料金算出
    	// 従量範囲料金をサマリー
    	BigDecimal usageVolumeFee = usageValSplits.stream() //
    			.map(e -> e.getRight()) // 計算済みの従量範囲料金
    			.reduce(BigDecimal.ZERO, BigDecimal::add); // 従量範囲料金をサマリー

    	eventLogger.debug("②従量料金:" + usageVolumeFee);

    	// ③燃料調整費算出
    	// 使用量をサマリーし燃料費調整額とかけ合わせ
    	BigDecimal fuelAdjustmentFee = usageValSplits.stream() //
    			.map(e -> e.getMiddle()) // 使用量
    			.reduce(BigDecimal.ZERO, BigDecimal::add) // 使用量をサマリー
    			.multiply(fuelAdjustPriceB); // 燃料費調整額とかけ合わせ

    	eventLogger.debug("③燃料調整費:" + fuelAdjustmentFee);

    	// ④再エネ賦課金算出
    	// 使用量をサマリーし再エネ賦課金とかけ合わせ
    	BigDecimal renewableEnergyLevy = usageValSplits.stream() //
    			.map(e -> e.getMiddle()) // 使用量
    			.reduce(BigDecimal.ZERO, BigDecimal::add) // 使用量をサマリー
    			.multiply(renewEnerPriceB); // 再エネ賦課金とかけ合わせ

    	eventLogger.debug("④再エネ賦課金:" + renewableEnergyLevy);

    	// 割引率
    	BigDecimal discountRate = //
    			buildingBillingInfoResultData.getDiscountRate() == null ? //
    					BigDecimal.ZERO : buildingBillingInfoResultData.getDiscountRate();

    	eventLogger.debug("割引率(%):" + discountRate);

    	// 使用料金(元金)計算
    	// 使用料金(元金) ＝ ( ①基本料金 ＋ ②従量料金 ＋ ③燃料調整費 ) ＋ ( ④再エネ賦課金 )
    	BigDecimal usageFeePrincipal = ((basicCharge.add(usageVolumeFee).add(fuelAdjustmentFee).setScale(0, round)) // ①基本料金 ＋ ②従量料金 ＋ ③燃料調整費：整数値に丸め
    	.add( //
    			renewableEnergyLevy.setScale(0, round)) // ＋ ( ③再エネ賦課金 )：整数値に丸め
    	); //

    	// 使用料金(割引後)計算
    	// 使用料金(割引後) ＝ 使用料金(元金) × (100% － 割引率)
    	BigDecimal usageFee = (usageFeePrincipal.multiply((new BigDecimal(100).subtract(discountRate)).divide(new BigDecimal(100)))).setScale(0, round);

    	// 割引額算出
    	BigDecimal discountAmount = usageFeePrincipal.subtract(usageFee);

    	eventLogger.debug("使用料金(元金):" + usageFeePrincipal);

    	eventLogger.debug("割引額:" + discountAmount);

    	eventLogger.debug("使用料金(割引後):" + usageFee);

    	// 使用量
    	resultBillingAmountResultData.setUsageVal(usageVal.toString());
    	// 基本料金
    	resultBillingAmountResultData.setBasicCharge(basicCharge.toString());
    	// 従量料金
    	resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());
    	// 燃料調整費
    	resultBillingAmountResultData.setFuelAdjustmentFee(fuelAdjustmentFee.toString());
    	// 再エネ賦課金
    	resultBillingAmountResultData.setRenewableEnergyLevy(renewableEnergyLevy.toString());
    	// 高圧一括割額
    	resultBillingAmountResultData.setHighVoltageBulkDiscount(BigDecimal.ZERO.toString());
    	// 割引額
    	resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());
    	// 使用料金(割引後)※マイナスの場合は0とする
    	if (BigDecimal.ZERO.compareTo(usageFee) > 0 ) {
    		usageFee = BigDecimal.ZERO;
    	}
    	resultBillingAmountResultData.setUsageFee(usageFee.toString());

    	eventLogger.debug(resultBillingAmountResultData);

    	eventLogger.debug("◆◆◆ 従量電灯Bの請求金額算出 終了 ◆◆◆");
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

    	eventLogger.debug("▼▼▼ その他の請求金額算出 開始 ▼▼▼");

    	eventLogger.debug(buildingBillingInfoResultData);
    	eventLogger.debug("使用量:" + usageVal);
    	List<Triple<MUnitPrice, BigDecimal, BigDecimal>> usageValSplits = usageValSplit(usageVal, mUnitPriceList);

    	// (その他)基本料金
    	BigDecimal basicPrice = buildingBillingInfoResultData.getBasicPrice();

    	eventLogger.debug("基本料金:" + basicPrice);

    	int round = getBigDecimalRound(buildingBillingInfoResultData.getFractionalProcessing());

    	eventLogger.debug("小数部端数処理(4:四捨五入、1:切り捨て、0:切り上げ):" + round);

    	// (その他)基本料金
    	BigDecimal basicCharge = basicPrice == null ? BigDecimal.ZERO : basicPrice;

    	eventLogger.debug("①基本料金:" + basicCharge);

    	// ②従量料金算出
    	// 従量範囲料金をサマリー
    	BigDecimal usageVolumeFee = usageValSplits.stream() //
    			.map(e -> e.getRight()) // 計算済みの従量範囲料金
    			.reduce(BigDecimal.ZERO, BigDecimal::add); // 従量範囲料金をサマリー

    	eventLogger.debug("②従量料金:" + usageVolumeFee);

    	// 割引率
    	BigDecimal discountRate = //
    			buildingBillingInfoResultData.getDiscountRate() == null ? //
    					BigDecimal.ZERO : buildingBillingInfoResultData.getDiscountRate();

    	eventLogger.debug("割引率(%):" + discountRate);

    	// 使用料金(元金)計算
    	// 使用料金(元金) ＝ ( ①基本料金 ＋ ②従量料金 )
    	BigDecimal usageFeePrincipal = basicCharge.add(usageVolumeFee).setScale(0, round); // ①基本料金 ＋ ②従量料金：整数値に丸め

    	// 使用料金(割引後)計算
    	// 使用料金(割引後) ＝ 使用料金(元金) × (100% － 割引率)
    	BigDecimal usageFee = (usageFeePrincipal.multiply((new BigDecimal(100).subtract(discountRate)).divide(new BigDecimal(100)))).setScale(0, round);

    	// 割引額算出
    	BigDecimal discountAmount = usageFeePrincipal.subtract(usageFee);

    	eventLogger.debug("使用料金(元金):" + usageFeePrincipal);

    	eventLogger.debug("割引額:" + discountAmount);

    	eventLogger.debug("使用料金(割引後):" + usageFee);

    	// 使用量
    	resultBillingAmountResultData.setUsageVal(usageVal.toString());
    	// 基本料金
    	resultBillingAmountResultData.setBasicCharge(basicCharge.toString());
    	// 従量料金
    	resultBillingAmountResultData.setUsageVolumeFee(usageVolumeFee.toString());
    	// 高圧一括割額
    	resultBillingAmountResultData.setHighVoltageBulkDiscount(BigDecimal.ZERO.toString());
    	// 割引額
    	resultBillingAmountResultData.setDiscountAmount(discountAmount.toString());
    	// 使用料金(割引後)※マイナスの場合は0とする
    	if (BigDecimal.ZERO.compareTo(usageFee) > 0 ) {
    		usageFee = BigDecimal.ZERO;
    	}
    	resultBillingAmountResultData.setUsageFee(usageFee.toString());

    	eventLogger.debug(resultBillingAmountResultData);

    	eventLogger.debug("▼▼▼ その他の請求金額算出 終了 ▼▼▼");
    }

    public void createBillingAmountCalculation(BigDecimal usageFees,
            ListSmsBillingAmountDataResultData billingAmountDataResultData, Boolean isProratedChargeFlg,
            Boolean isFixedCostFlg) {

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

        for (ListSmsBillingAmountDataFixedCostResultData fixedCost : billingAmountDataResultData
                .getFixedCostList()) {
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
        if (billingAmountDataResultData.getSalesTaxTreatment().equals(SmsConstants.TAX_TYPE.INCLUDED.getVal())) {
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

}
