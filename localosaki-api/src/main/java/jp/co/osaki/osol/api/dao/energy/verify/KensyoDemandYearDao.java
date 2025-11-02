package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoDemandYearParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyKensyoDataResult;
import jp.co.osaki.osol.api.result.energy.verify.KensyoDemandYearResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.TLoadControlLogResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandYearDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandYearHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandYearTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayCalLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleSetLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleTimeLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.TLoadControlLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MProductSpecServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineControlLoadVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.api.utility.energy.setting.ProductControlLoadFlgSeparateUtility;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンド制御検証・年報 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class KensyoDemandYearDao extends OsolApiDao<KensyoDemandYearParameter> {

    //TODO EntityServiceDaoクラスを使わないほうがいい
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    private final MSmLineVerifyServiceDaoImpl mSmLineVerifyServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final MSmLineControlLoadVerifyServiceDaoImpl mSmLineControlLoadVerifyServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final TLoadControlLogServiceDaoImpl tLoadControlLogServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;
    private final SmControlScheduleLogListServiceDaoImpl smControlScheduleLogListServiceDaoImpl;
    private final SmControlScheduleSetLogListServiceDaoImpl smControlScheduleSetLogListServiceDaoImpl;
    private final SmControlScheduleTimeLogListServiceDaoImpl smControlScheduleTimeLogListServiceDaoImpl;
    private final SmControlHolidayLogListServiceDaoImpl smControlHolidayLogListServiceDaoImpl;
    private final SmControlHolidayCalLogListServiceDaoImpl smControlHolidayCalLogListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    @EJB
    private DemandOrgDailyKensyoDataDao demandOrgDailyKensyoDataDao;

    public KensyoDemandYearDao() {
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        mProductSpecServiceDaoImpl = new MProductSpecServiceDaoImpl();
        mSmLineVerifyServiceDaoImpl = new MSmLineVerifyServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        mSmLineControlLoadVerifyServiceDaoImpl = new MSmLineControlLoadVerifyServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        tLoadControlLogServiceDaoImpl = new TLoadControlLogServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
        smControlScheduleLogListServiceDaoImpl = new SmControlScheduleLogListServiceDaoImpl();
        smControlScheduleSetLogListServiceDaoImpl = new SmControlScheduleSetLogListServiceDaoImpl();
        smControlScheduleTimeLogListServiceDaoImpl = new SmControlScheduleTimeLogListServiceDaoImpl();
        smControlHolidayLogListServiceDaoImpl = new SmControlHolidayLogListServiceDaoImpl();
        smControlHolidayCalLogListServiceDaoImpl = new SmControlHolidayCalLogListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public KensyoDemandYearResult query(KensyoDemandYearParameter parameter) throws Exception {
        KensyoDemandYearResult rootResultSet = new KensyoDemandYearResult();
        KensyoDemandYearHeaderResultData headerResultSet = new KensyoDemandYearHeaderResultData();
        List<KensyoDemandYearDetailResultData> detailList = new ArrayList<>();
        List<KensyoDemandYearTimeDetailResultData> timeDetailActualList;
        List<KensyoDemandYearTimeDetailResultData> timeDetailPredictionList;
        List<KensyoDemandYearTimeDetailResultData> timeDetailControlCountList;
        List<KensyoDemandYearTimeDetailResultData> timeDetailReductionList;
        List<DemandCalendarYearData> calList = new ArrayList<>();
        Date currentDate;

        BigDecimal monthlyActual;
        BigDecimal monthlyPrediction;
        BigDecimal monthlyControlCount;
        BigDecimal monthlyReduction;

        Date maxActualMeasurementDate;
        Date maxPredictionMeasurementDate;
        Date maxReductionMeasurementDate;

        BigDecimal maxActualJigenNo;
        BigDecimal maxPredictionJigenNo;
        BigDecimal maxReductionJigenNo;

        BigDecimal maxActual;
        BigDecimal minActual;
        BigDecimal summaryActual;
        BigDecimal countActual;
        BigDecimal averageActual;
        BigDecimal maxPrediction;
        BigDecimal minPrediction;
        BigDecimal summaryPrediction;
        BigDecimal countPrediction;
        BigDecimal averagePrediction;
        BigDecimal maxControlCount;
        BigDecimal minControlCount;
        BigDecimal summaryControlCount;
        BigDecimal countControlCount;
        BigDecimal averageControlCount;
        BigDecimal maxReduction;
        BigDecimal minReduction;
        BigDecimal summaryReduction;
        BigDecimal countReduction;
        BigDecimal averageReduction;

        Date measurementDateFrom;
        Date measurementDateTo;
        String beforeCalYm;
        String currentCalYm;

        List<CommonDemandMonthReportListResult> monthReportList;

        ProductControlLoadFlgSeparateListResult productControlLoadData = null;
        boolean beforeG2Flg;

        //当日を取得する
        Date nowDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(new Date(getServerDateTime().getTime()), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //集計日がNULLの場合、企業集計を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSummaryKind())) {
            parameter.setSummaryKind(ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal());
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、1（年）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
        }

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smList == null || smList.size() != 1) {
            //機器情報が取得できない場合は、処理を終了する
            return new KensyoDemandYearResult();
        } else {
            headerResultSet.setSmAddress(smList.get(0).getSmAddress());
            headerResultSet.setIpAddress(smList.get(0).getIpAddress());
            //製品情報を取得する
            MProductSpec productParam = new MProductSpec();
            productParam.setProductCd(smList.get(0).getProductCd());
            MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
            if (productSpec == null || OsolConstants.FLG_ON.equals(productSpec.getDelFlg())) {
                //製品情報が取得できない場合または削除されているは、処理を終了する
                return new KensyoDemandYearResult();
            } else {
                headerResultSet.setProductName(productSpec.getProductName());
            }
        }

        // 対象商品がG2以前か否かを取得する
        beforeG2Flg = DemandVerifyUtility.isBeforeG2(smList.get(0).getProductCd());

        //製品制御負荷情報を取得する（G2より後の製品のみ）
        if(!beforeG2Flg) {
            ProductControlLoadListDetailResultData productControlLoadParam = DemandVerifyUtility.getProductControlLoadParam(smList.get(0).getProductCd());
            List<ProductControlLoadListDetailResultData> productControlLoadList = getResultList(productControlLoadListServiceDaoImpl, productControlLoadParam);
            productControlLoadData = ProductControlLoadFlgSeparateUtility.setProductControlLoadFlgSeparateList(productControlLoadList);
            if(productControlLoadData == null) {
                return new KensyoDemandYearResult();
            }
        }

        //機器系統検証情報を取得する。
        MSmLineVerify param = DemandVerifyUtility.getSmLineVerifyParam(parameter.getOperationCorpId(),
                parameter.getBuildingId(), parameter.getLineGroupId(), parameter.getLineNo(), parameter.getSmId());
        List<MSmLineVerify> smLineVerifyList = getResultList(mSmLineVerifyServiceDaoImpl, param);
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            headerResultSet.setBasicRateUnitPrice(null);
        } else {
            headerResultSet.setBasicRateUnitPrice(smLineVerifyList.get(0).getBasicRateUnitPrice());
        }

        // 集計期間
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(),
                parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //集計範囲年月
        Integer calYmFrom = Integer.parseInt(summaryRange.getYearFrom().concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getMonthFrom().intValue())));
        Integer calYmTo = Integer.parseInt(summaryRange.getYearTo().concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getMonthTo().intValue())));

        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new KensyoDemandYearResult();
        }

        //カレンダ情報を取得する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            // 企業集計
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new KensyoDemandYearResult();
            }
            List<DemandCalendarYearData> tempCorpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom,
                    yearNoTo, corpDemandList.get(0).getSumDate());
            if (tempCorpCalList == null || tempCorpCalList.isEmpty()) {
                return new KensyoDemandYearResult();
            } else {
                for (DemandCalendarYearData tempCorpCal : tempCorpCalList) {
                    if (calYmFrom <= Integer.parseInt(tempCorpCal.getYearNo()
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    tempCorpCal.getMonthNo().intValue())))
                            && Integer.parseInt(tempCorpCal.getYearNo()
                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                            tempCorpCal.getMonthNo().intValue()))) <= calYmTo) {
                        //集計範囲内のデータのみ格納する
                        calList.add(tempCorpCal);
                    }
                }
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計
            String sumDate = null;

            //集計デマンド系統情報を取得する
            AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), null, null, null);
            List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                    aggregateDmLineListServiceDaoImpl, aggregateLineParam);
            if (aggregateLineList != null && aggregateLineList.size() == 1) {
                //集計デマンド情報を取得する
                AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(),
                        aggregateLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                        aggregateParam);
                if (aggregateList != null && aggregateList.size() == 1) {
                    sumDate = aggregateList.get(0).getSumDate();
                }
            }

            if (CheckUtility.isNullOrEmpty(sumDate)) {
                sumDate = buildingDemandList.get(0).getSumDate();
            }

            List<DemandCalendarYearData> tempBuildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom,
                    yearNoTo, sumDate);
            if (tempBuildingCalList == null || tempBuildingCalList.isEmpty()) {
                return new KensyoDemandYearResult();
            } else {
                for (DemandCalendarYearData tempBuildingCal : tempBuildingCalList) {
                    if (calYmFrom <= Integer.parseInt(tempBuildingCal.getYearNo()
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    tempBuildingCal.getMonthNo().intValue())))
                            && Integer.parseInt(tempBuildingCal.getYearNo()
                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                            tempBuildingCal.getMonthNo().intValue()))) <= calYmTo) {
                        //集計範囲内のデータのみ格納する
                        calList.add(tempBuildingCal);
                    }
                }
            }
        }

        if (calList.isEmpty()) {
            return new KensyoDemandYearResult();
        } else {
            //取得したカレンダをカレンダ年月順にソートする
            calList = calList.stream()
                    .sorted(Comparator.comparing(DemandCalendarYearData::getYearNo, Comparator.naturalOrder())
                            .thenComparing(Comparator.comparing(DemandCalendarYearData::getMonthNo,
                                    Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            //計測開始年月日Fromはカレンダ先頭の月開始日
            measurementDateFrom = calList.get(0).getMonthStartDate();
            //計測年月日Toはカレンダ最終の月終了日
            measurementDateTo = calList.get(calList.size() - 1).getMonthEndDate();
        }

        //目標電力を取得する
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), measurementDateFrom, measurementDateTo);

        if (measurementDateFrom.compareTo(nowDate) >= 0) {
            //Fromが未来日（当日以降）の場合は取得しない
            monthReportList = new ArrayList<>();
        } else if (measurementDateTo.compareTo(nowDate) >= 0) {
            //Toが未来日（当日以降）の場合は当日にして取得
            monthReportParam.setMeasurementDateTo(DateUtility.plusDay(nowDate, -1));
            monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        } else {
            //上記以外は指定範囲で取得
            monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        }

        if (monthReportList == null || monthReportList.isEmpty()) {
            headerResultSet.setTargetPowerValue(null);
        } else {
            headerResultSet.setTargetPowerValue(monthReportList.get(0).getTargetKw());
        }

        //契約電力を取得する
        headerResultSet.setContractPowerValue(buildingDemandList.get(0).getContractKw());

        //時間帯別実績情報を作成する
        timeDetailActualList = DemandDataUtility.createYearlyKensyoDetailHeader(summaryRange);
        timeDetailPredictionList = DemandDataUtility.createYearlyKensyoDetailHeader(summaryRange);
        timeDetailControlCountList = DemandDataUtility.createYearlyKensyoDetailHeader(summaryRange);
        timeDetailReductionList = DemandDataUtility.createYearlyKensyoDetailHeader(summaryRange);

        currentDate = measurementDateFrom;

        monthlyActual = null;
        monthlyPrediction = null;
        monthlyControlCount = null;
        monthlyReduction = null;

        maxActualMeasurementDate = null;
        maxPredictionMeasurementDate = null;
        maxReductionMeasurementDate = null;

        maxActualJigenNo = null;
        maxPredictionJigenNo = null;
        maxReductionJigenNo = null;

        maxActual = null;
        minActual = null;
        summaryActual = null;
        countActual = null;
        averageActual = null;
        maxPrediction = null;
        minPrediction = null;
        summaryPrediction = null;
        countPrediction = null;
        averagePrediction = null;
        maxControlCount = null;
        minControlCount = null;
        summaryControlCount = null;
        countControlCount = null;
        averageControlCount = null;
        maxReduction = null;
        minReduction = null;
        summaryReduction = null;
        countReduction = null;
        averageReduction = null;

        beforeCalYm = null;

        //集計期間の日報データを取得し、最大値を取得する
        while (!currentDate.after(measurementDateTo)) {

            //指定日のカレンダ年月を取得する
            currentCalYm = null;
            for (DemandCalendarYearData cal : calList) {
                if (cal.getMonthStartDate().compareTo(currentDate) <= 0
                        && cal.getMonthEndDate().compareTo(currentDate) >= 0) {
                    currentCalYm = cal.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, cal.getMonthNo().intValue()));
                    break;
                }
            }

            if (currentCalYm == null) {
                //カレンダ年月が取得できな場合は、処理を終了
                return new KensyoDemandYearResult();
            }

            if (beforeCalYm != null && !beforeCalYm.equals(currentCalYm)) {
                //カレンダ年月が変わった場合は終了処理を行う

                //実績値
                for (KensyoDemandYearTimeDetailResultData timeDetailActual : timeDetailActualList) {
                    if (timeDetailActual.getCalY().concat(timeDetailActual.getCalM()).equals(beforeCalYm)) {
                        timeDetailActual.setResultValue(monthlyActual);
                        if (monthlyActual != null) {
                            if (minActual == null) {
                                minActual = monthlyActual;
                            } else if (monthlyActual.compareTo(minActual) < 0) {
                                minActual = monthlyActual;
                            }
                            if (summaryActual == null) {
                                summaryActual = monthlyActual;
                            } else {
                                summaryActual = summaryActual.add(monthlyActual);
                            }
                            if (countActual == null) {
                                countActual = BigDecimal.ONE;
                            } else {
                                countActual = countActual.add(BigDecimal.ONE);
                            }
                        }
                        break;
                    }
                }

                //予測値
                for (KensyoDemandYearTimeDetailResultData timeDetailPrediction : timeDetailPredictionList) {
                    if (timeDetailPrediction.getCalY().concat(timeDetailPrediction.getCalM()).equals(beforeCalYm)) {
                        timeDetailPrediction.setResultValue(monthlyPrediction);
                        if (monthlyPrediction != null) {
                            if (minPrediction == null) {
                                minPrediction = monthlyPrediction;
                            } else if (monthlyPrediction.compareTo(minPrediction) < 0) {
                                minPrediction = monthlyPrediction;
                            }
                            if (summaryPrediction == null) {
                                summaryPrediction = monthlyPrediction;
                            } else {
                                summaryPrediction = summaryPrediction.add(monthlyPrediction);
                            }
                            if (countPrediction == null) {
                                countPrediction = BigDecimal.ONE;
                            } else {
                                countPrediction = countPrediction.add(BigDecimal.ONE);
                            }
                        }
                        break;
                    }
                }

                //制御回数
                for (KensyoDemandYearTimeDetailResultData timeDetailControlCount : timeDetailControlCountList) {
                    if (timeDetailControlCount.getCalY().concat(timeDetailControlCount.getCalM()).equals(beforeCalYm)) {
                        timeDetailControlCount.setResultValue(monthlyControlCount);
                        if (monthlyControlCount != null) {
                            if (maxControlCount == null) {
                                maxControlCount = monthlyControlCount;
                            } else if (monthlyControlCount.compareTo(maxControlCount) >= 0) {
                                maxControlCount = monthlyControlCount;
                            }
                            if (minControlCount == null) {
                                minControlCount = monthlyControlCount;
                            } else if (monthlyControlCount.compareTo(minControlCount) < 0) {
                                minControlCount = monthlyControlCount;
                            }
                            if (summaryControlCount == null) {
                                summaryControlCount = monthlyControlCount;
                            } else {
                                summaryControlCount = summaryControlCount.add(monthlyControlCount);
                            }
                            if (countControlCount == null) {
                                countControlCount = BigDecimal.ONE;
                            } else {
                                countControlCount = countControlCount.add(BigDecimal.ONE);
                            }
                        }
                        break;
                    }
                }

                //削減値
                for (KensyoDemandYearTimeDetailResultData timeDetailReduction : timeDetailReductionList) {
                    if (timeDetailReduction.getCalY().concat(timeDetailReduction.getCalM()).equals(beforeCalYm)) {
                        timeDetailReduction.setResultValue(monthlyReduction);
                        if (monthlyReduction != null) {
                            if (minReduction == null) {
                                minReduction = monthlyReduction;
                            } else if (monthlyReduction.compareTo(minReduction) < 0) {
                                minReduction = monthlyReduction;
                            }
                            if (summaryReduction == null) {
                                summaryReduction = monthlyReduction;
                            } else {
                                summaryReduction = summaryReduction.add(monthlyReduction);
                            }
                            if (countReduction == null) {
                                countReduction = BigDecimal.ONE;
                            } else {
                                countReduction = countReduction.add(BigDecimal.ONE);
                            }
                        }
                        break;
                    }
                }

                monthlyActual = null;
                monthlyPrediction = null;
                monthlyControlCount = null;
                monthlyReduction = null;
            }

            //指定日の日報のデマンド検証データを取得する
            DemandOrgDailyKensyoDataResult dailyKensyoDataResultSet = getDemandOrgDailyKensyoDataResultSet(parameter,
                    currentDate, nowDate, productControlLoadData, beforeG2Flg);

            if (dailyKensyoDataResultSet != null) {
                //実績値
                if (dailyKensyoDataResultSet.getDetailList() != null
                        && !dailyKensyoDataResultSet.getDetailList().isEmpty()) {
                    for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetailActual : dailyKensyoDataResultSet
                            .getDetailList().get(0).getTimeResultList()) {
                        if (timeDetailActual.getResultValue() != null) {
                            if (monthlyActual == null) {
                                monthlyActual = timeDetailActual.getResultValue();
                            } else if (timeDetailActual.getResultValue().compareTo(monthlyActual) >= 0) {
                                monthlyActual = timeDetailActual.getResultValue();
                            }

                            if (maxActual == null) {
                                maxActual = timeDetailActual.getResultValue();
                                maxActualMeasurementDate = timeDetailActual.getMeasurementDate();
                                maxActualJigenNo = timeDetailActual.getJigenNo();
                            } else if (timeDetailActual.getResultValue().compareTo(maxActual) >= 0) {
                                maxActual = timeDetailActual.getResultValue();
                                maxActualMeasurementDate = timeDetailActual.getMeasurementDate();
                                maxActualJigenNo = timeDetailActual.getJigenNo();
                            }
                        }
                    }
                }

                //予測値
                if (dailyKensyoDataResultSet.getDetailList() != null
                        && dailyKensyoDataResultSet.getDetailList().size() >= 2) {
                    for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetailPrediction : dailyKensyoDataResultSet
                            .getDetailList().get(1).getTimeResultList()) {
                        if (timeDetailPrediction.getResultValue() != null) {
                            if (monthlyPrediction == null) {
                                monthlyPrediction = timeDetailPrediction.getResultValue();
                            } else if (timeDetailPrediction.getResultValue().compareTo(monthlyPrediction) >= 0) {
                                monthlyPrediction = timeDetailPrediction.getResultValue();
                            }
                            if (maxPrediction == null) {
                                maxPrediction = timeDetailPrediction.getResultValue();
                                maxPredictionMeasurementDate = timeDetailPrediction.getMeasurementDate();
                                maxPredictionJigenNo = timeDetailPrediction.getJigenNo();
                            } else if (timeDetailPrediction.getResultValue().compareTo(maxPrediction) >= 0) {
                                maxPrediction = timeDetailPrediction.getResultValue();
                                maxPredictionMeasurementDate = timeDetailPrediction.getMeasurementDate();
                                maxPredictionJigenNo = timeDetailPrediction.getJigenNo();
                            }
                        }
                    }
                }

                //制御回数
                if (dailyKensyoDataResultSet.getHeader() != null) {
                    if (dailyKensyoDataResultSet.getHeader().getControlCount() != null) {
                        if (monthlyControlCount == null) {
                            monthlyControlCount = dailyKensyoDataResultSet.getHeader().getControlCount();
                        } else {
                            monthlyControlCount = monthlyControlCount
                                    .add(dailyKensyoDataResultSet.getHeader().getControlCount());
                        }
                    }
                }

                //削減値
                if (dailyKensyoDataResultSet.getDetailList() != null
                        && dailyKensyoDataResultSet.getDetailList().size() >= 3) {
                    for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetailReduction : dailyKensyoDataResultSet
                            .getDetailList().get(2).getTimeResultList()) {
                        if (timeDetailReduction.getResultValue() != null) {
                            if (monthlyReduction == null) {
                                monthlyReduction = timeDetailReduction.getResultValue();
                            } else if (timeDetailReduction.getResultValue().compareTo(monthlyReduction) >= 0) {
                                monthlyReduction = timeDetailReduction.getResultValue();
                            }
                            if (maxReduction == null) {
                                maxReduction = timeDetailReduction.getResultValue();
                                maxReductionMeasurementDate = timeDetailReduction.getMeasurementDate();
                                maxReductionJigenNo = timeDetailReduction.getJigenNo();
                            } else if (timeDetailReduction.getResultValue().compareTo(maxReduction) >= 0) {
                                maxReduction = timeDetailReduction.getResultValue();
                                maxReductionMeasurementDate = timeDetailReduction.getMeasurementDate();
                                maxReductionJigenNo = timeDetailReduction.getJigenNo();
                            }
                        }
                    }
                }

            }

            beforeCalYm = currentCalYm;
            //指定日を1日進める
            currentDate = DateUtility.plusDay(currentDate, 1);

        }

        //最終レコードの処理
        //実績値
        for (KensyoDemandYearTimeDetailResultData timeDetailActual : timeDetailActualList) {
            if (timeDetailActual.getCalY().concat(timeDetailActual.getCalM()).equals(beforeCalYm)) {
                timeDetailActual.setResultValue(monthlyActual);
                if (monthlyActual != null) {
                    if (summaryActual == null) {
                        summaryActual = monthlyActual;
                    } else {
                        summaryActual = summaryActual.add(monthlyActual);
                    }
                    if (countActual == null) {
                        countActual = BigDecimal.ONE;
                    } else {
                        countActual = countActual.add(BigDecimal.ONE);
                    }
                }
                break;
            }
        }

        //予測値
        for (KensyoDemandYearTimeDetailResultData timeDetailPrediction : timeDetailPredictionList) {
            if (timeDetailPrediction.getCalY().concat(timeDetailPrediction.getCalM()).equals(beforeCalYm)) {
                timeDetailPrediction.setResultValue(monthlyPrediction);
                if (monthlyPrediction != null) {
                    if (summaryPrediction == null) {
                        summaryPrediction = monthlyPrediction;
                    } else {
                        summaryPrediction = summaryPrediction.add(monthlyPrediction);
                    }
                    if (countPrediction == null) {
                        countPrediction = BigDecimal.ONE;
                    } else {
                        countPrediction = countPrediction.add(BigDecimal.ONE);
                    }
                }
                break;
            }
        }

        //制御回数
        for (KensyoDemandYearTimeDetailResultData timeDetailControlCount : timeDetailControlCountList) {
            if (timeDetailControlCount.getCalY().concat(timeDetailControlCount.getCalM()).equals(beforeCalYm)) {
                timeDetailControlCount.setResultValue(monthlyControlCount);
                if (monthlyControlCount != null) {
                    if (maxControlCount == null) {
                        maxControlCount = monthlyControlCount;
                    } else if (monthlyControlCount.compareTo(maxControlCount) >= 0) {
                        maxControlCount = monthlyControlCount;
                    }
                    if (minControlCount == null) {
                        minControlCount = monthlyControlCount;
                    } else if (monthlyControlCount.compareTo(minControlCount) < 0) {
                        minControlCount = monthlyControlCount;
                    }
                    if (summaryControlCount == null) {
                        summaryControlCount = monthlyControlCount;
                    } else {
                        summaryControlCount = summaryControlCount.add(monthlyControlCount);
                    }
                    if (countControlCount == null) {
                        countControlCount = BigDecimal.ONE;
                    } else {
                        countControlCount = countControlCount.add(BigDecimal.ONE);
                    }
                }
                break;
            }
        }

        //削減値
        for (KensyoDemandYearTimeDetailResultData timeDetailReduction : timeDetailReductionList) {
            if (timeDetailReduction.getCalY().concat(timeDetailReduction.getCalM()).equals(beforeCalYm)) {
                timeDetailReduction.setResultValue(monthlyReduction);
                if (monthlyReduction != null) {
                    if (summaryReduction == null) {
                        summaryReduction = monthlyReduction;
                    } else {
                        summaryReduction = summaryReduction.add(monthlyReduction);
                    }
                    if (countReduction == null) {
                        countReduction = BigDecimal.ONE;
                    } else {
                        countReduction = countReduction.add(BigDecimal.ONE);
                    }
                }
                break;
            }
        }

        //平均値を算出する
        if (countActual != null && summaryActual != null) {
            averageActual = summaryActual.divide(countActual, 10, BigDecimal.ROUND_HALF_UP);
        }
        if (countPrediction != null && summaryPrediction != null) {
            averagePrediction = summaryPrediction.divide(countPrediction, 10, BigDecimal.ROUND_HALF_UP);
        }
        if (countControlCount != null && summaryControlCount != null) {
            averageControlCount = summaryControlCount.divide(countControlCount, 0, BigDecimal.ROUND_HALF_UP);
        }
        if (countReduction != null && summaryReduction != null) {
            averageReduction = summaryReduction.divide(countReduction, 10, BigDecimal.ROUND_HALF_UP);
        }

        //結果値の小数第2位を切り捨てする
        //予測値
        for (KensyoDemandYearTimeDetailResultData timeDetail : timeDetailPredictionList) {
            if (timeDetail.getResultValue() != null) {
                timeDetail.setResultValue(timeDetail.getResultValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        if (maxPrediction != null) {
            maxPrediction = maxPrediction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        if (minPrediction != null) {
            minPrediction = minPrediction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        //削減値
        for (KensyoDemandYearTimeDetailResultData timeDetail : timeDetailReductionList) {
            if (timeDetail.getResultValue() != null) {
                timeDetail.setResultValue(timeDetail.getResultValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        if (maxReduction != null) {
            maxReduction = maxReduction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        if (minReduction != null) {
            minReduction = minReduction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        //平均値
        if (averageActual != null) {
            averageActual = averageActual.setScale(1, BigDecimal.ROUND_DOWN);
        }

        if (averagePrediction != null) {
            averagePrediction = averagePrediction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        if (averageReduction != null) {
            averageReduction = averageReduction.setScale(1, BigDecimal.ROUND_DOWN);
        }

        //実績値を詰める
        KensyoDemandYearDetailResultData detailActual = new KensyoDemandYearDetailResultData();
        detailActual.setColumnHeaderTitle("実績値（制御後）　　kW");
        detailActual.setTimeResultList(timeDetailActualList);
        detailActual.setMaxValue(maxActual);
        detailActual.setMinValue(minActual);
        detailActual.setAverageValue(averageActual);

        //予測値を詰める
        KensyoDemandYearDetailResultData detailPrediction = new KensyoDemandYearDetailResultData();
        detailPrediction.setColumnHeaderTitle("未制御時の予測値　　kW");
        detailPrediction.setTimeResultList(timeDetailPredictionList);
        detailPrediction.setMaxValue(maxPrediction);
        detailPrediction.setMinValue(minPrediction);
        detailPrediction.setAverageValue(averagePrediction);

        //制御回数を詰める
        KensyoDemandYearDetailResultData detailControlCount = new KensyoDemandYearDetailResultData();
        detailControlCount.setColumnHeaderTitle("制御回数　　　　　　回");
        detailControlCount.setTimeResultList(timeDetailControlCountList);
        detailControlCount.setMaxValue(maxControlCount);
        detailControlCount.setMinValue(minControlCount);
        detailControlCount.setAverageValue(averageControlCount);

        //削減値を詰める
        KensyoDemandYearDetailResultData detailReduction = new KensyoDemandYearDetailResultData();
        detailReduction.setColumnHeaderTitle("削減値　　　　　　　kW");
        detailReduction.setTimeResultList(timeDetailReductionList);
        detailReduction.setMaxValue(maxReduction);
        detailReduction.setMinValue(minReduction);
        detailReduction.setAverageValue(averageReduction);

        //明細情報を詰める
        detailList.add(detailActual);
        detailList.add(detailPrediction);
        detailList.add(detailControlCount);
        detailList.add(detailReduction);

        //ヘッダ情報を詰める
        headerResultSet.setControlCount(summaryControlCount);
        headerResultSet.setMaxDemandValue(maxActual);
        if (maxActual == null) {
            headerResultSet.setMaxDemandTime(null);
        } else {
            headerResultSet.setMaxDemandTime(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(maxActualMeasurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                            .concat(DemandDataUtility.changeJigenNoToHHMM(maxActualJigenNo, false)),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
        }
        headerResultSet.setMaxPredictionValue(maxPrediction);
        if (maxPrediction == null) {
            headerResultSet.setMaxPredictionTime(null);
        } else {
            headerResultSet.setMaxPredictionTime(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(maxPredictionMeasurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                            .concat(DemandDataUtility.changeJigenNoToHHMM(maxPredictionJigenNo, false)),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
        }
        headerResultSet.setMaxReductionValue(maxReduction);
        if (maxReduction == null) {
            headerResultSet.setMaxReductionTime(null);
        } else {
            headerResultSet.setMaxReductionTime(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(maxReductionMeasurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                            .concat(DemandDataUtility.changeJigenNoToHHMM(maxReductionJigenNo, false)),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
        }

        //総評を算出する
        headerResultSet.setPowerFactor(ApiSimpleConstants.POWER_FACTOR);
        if (headerResultSet.getControlCount() != null) {
            if (headerResultSet.getControlCount().compareTo(BigDecimal.ZERO) == 0) {
                if (headerResultSet.getBasicRateUnitPrice() != null && headerResultSet.getMaxDemandValue() != null
                        && headerResultSet.getContractPowerValue() != null) {
                    headerResultSet.setDemandKensyoResultValue(headerResultSet.getBasicRateUnitPrice().multiply(
                            (headerResultSet.getContractPowerValue().subtract(headerResultSet.getMaxDemandValue()))
                                    .multiply(headerResultSet.getPowerFactor()).multiply(new BigDecimal("12"))));
                    headerResultSet.setDemandKensyoResultReduction(
                            headerResultSet.getContractPowerValue().subtract(headerResultSet.getMaxDemandValue()));
                } else {
                    headerResultSet.setDemandKensyoResultValue(null);
                    headerResultSet.setDemandKensyoResultReduction(null);
                }
            } else if (headerResultSet.getControlCount().compareTo(BigDecimal.ZERO) > 0) {
                if (headerResultSet.getBasicRateUnitPrice() != null && headerResultSet.getMaxDemandValue() != null
                        && headerResultSet.getMaxPredictionValue() != null) {
                    headerResultSet.setDemandKensyoResultValue(headerResultSet.getBasicRateUnitPrice().multiply(
                            (headerResultSet.getMaxPredictionValue().subtract(headerResultSet.getMaxDemandValue()))
                                    .multiply(headerResultSet.getPowerFactor()).multiply(new BigDecimal("12"))));
                    headerResultSet.setDemandKensyoResultReduction(headerResultSet.getMaxReductionValue());
                } else {
                    headerResultSet.setDemandKensyoResultValue(null);
                    headerResultSet.setDemandKensyoResultReduction(null);
                }
            } else {
                headerResultSet.setDemandKensyoResultValue(null);
                headerResultSet.setDemandKensyoResultReduction(null);
            }
        } else {
            headerResultSet.setDemandKensyoResultValue(null);
            headerResultSet.setDemandKensyoResultReduction(null);
        }

        //結果を詰める
        rootResultSet.setHeader(headerResultSet);
        rootResultSet.setDetailList(detailList);

        return rootResultSet;
    }

    /**
     * 日報の情報を取得する
     * @param parameter
     * @param currentDate
     * @param serverDateTime
     * @param productControlLoadData
     * @param beforeG2Flg
     * @return
     * @throws ParseException
     */
    private DemandOrgDailyKensyoDataResult getDemandOrgDailyKensyoDataResultSet(
            KensyoDemandYearParameter parameter, Date currentDate, Date serverDateTime,
            ProductControlLoadFlgSeparateListResult productControlLoadData, boolean beforeG2Flg) throws ParseException {
        DemandOrgDailyKensyoDataResult rootResultSet = new DemandOrgDailyKensyoDataResult();
        DemandOrgDailyKensyoDataHeaderResultData headerResultSet = new DemandOrgDailyKensyoDataHeaderResultData();
        List<DemandOrgDailyKensyoDataDetailResultData> detailList = new ArrayList<>();
        List<DemandOrgDailyKensyoDataTimeDetailResultData> timeDetailActualList;
        List<DemandOrgDailyKensyoDataTimeDetailResultData> timeDetailPredictionList;
        List<DemandOrgDailyKensyoDataTimeDetailResultData> timeDetailReductionList;
        LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet;
        LinkedHashSet<CommonScheduleResult> commonScheduleResultSet;
        List<CommonDemandControlSummaryResult> summaryList;
        List<CommonDemandControlSmSummaryResult> smSummaryList;
        Integer controlLoadCount;
        LinkedHashSet<CommonControlLoadResult> controlLoadSet;
        String recordYmdHmFrom;
        String recordYmdHmTo;
        Timestamp settingUpdateDateTimeFrom;
        Timestamp settingUpdateDateTimeTo;
        BigDecimal controlCount = null;
        BigDecimal maxActual = null;
        BigDecimal minActual = null;
        BigDecimal summaryActual = null;
        BigDecimal countActual = null;
        BigDecimal averageActual = null;
        BigDecimal maxPrediction = null;
        BigDecimal minPrediction = null;
        BigDecimal summaryPrediction = null;
        BigDecimal countPrediction = null;
        BigDecimal averagePrediction = null;
        BigDecimal maxReduction = null;
        BigDecimal minReduction = null;
        BigDecimal summaryReduction = null;
        BigDecimal countReduction = null;
        BigDecimal averageReduction = null;
        List<CommonDemandMonthReportListResult> monthReportList;
        List<CommonDemandDayReportLineListResult> dayRepLineList;
        List<TLoadControlLogResult> tLoadControlLogResultSetList;

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        smParam.setSmId(parameter.getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smList == null || smList.size() != 1) {
            //機器情報が取得できない場合は、処理を終了する
            return new DemandOrgDailyKensyoDataResult();
        } else {
            headerResultSet.setSmAddress(smList.get(0).getSmAddress());
            headerResultSet.setIpAddress(smList.get(0).getIpAddress());
            //製品情報を取得する
            MProductSpec productParam = new MProductSpec();
            productParam.setProductCd(smList.get(0).getProductCd());
            MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
            if (productSpec == null || OsolConstants.FLG_ON.equals(productSpec.getDelFlg())) {
                //製品情報が取得できないまたは削除されている場合は、処理を終了する
                return new DemandOrgDailyKensyoDataResult();
            } else {
                controlLoadCount = productSpec.getLoadControlOutput();
                headerResultSet.setProductName(productSpec.getProductName());
            }
        }

        if(!beforeG2Flg) {
            controlLoadCount = productControlLoadData.getDemandControlFlgData().getControlLoadCount();
        }

        //機器系統制御負荷検証情報を取得する。
        MSmLineControlLoadVerify loadParam = DemandVerifyUtility.getSmLineControlLoadVerifyParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(), parameter.getSmId());
        List<MSmLineControlLoadVerify> smLineControlVerifyList = getResultList(mSmLineControlLoadVerifyServiceDaoImpl,
                loadParam);

        //機器系統検証情報を取得する。
        MSmLineVerify lineParam = DemandVerifyUtility.getSmLineVerifyParam(parameter.getOperationCorpId(),
                parameter.getBuildingId(), parameter.getLineGroupId(), parameter.getLineNo(), parameter.getSmId());

        List<MSmLineVerify> smLineVerifyList = getResultList(mSmLineVerifyServiceDaoImpl, lineParam);
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            headerResultSet.setBasicRateUnitPrice(null);
        } else {
            headerResultSet.setBasicRateUnitPrice(smLineVerifyList.get(0).getBasicRateUnitPrice());
        }

        //集計範囲を取得する
        SummaryRangeForDayResult summaryRange = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                "0000",
                ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal(),
                new BigDecimal("24"));

        //制御ログ上の開始、終了を設定する
        recordYmdHmFrom = DemandVerifyUtility.getRecordYmdHmFrom(summaryRange.getMeasurementDateFrom(),
                summaryRange.getJigenNoFrom());
        recordYmdHmTo = DemandVerifyUtility.getRecordYmdHmTo(summaryRange.getMeasurementDateTo(),
                summaryRange.getJigenNoTo());

        //スケジュール上の開始、終了を設定する
        settingUpdateDateTimeFrom = DemandVerifyUtility
                .getSettingUpdateDateTimeFrom(summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom());
        settingUpdateDateTimeTo = DemandVerifyUtility.getSettingUpdateDateTimeTo(summaryRange.getMeasurementDateTo(),
                summaryRange.getJigenNoTo());

        //目標電力を取得する
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), summaryRange.getMeasurementDateFrom(),
                summaryRange.getMeasurementDateTo());

        if (summaryRange.getMeasurementDateFrom().compareTo(serverDateTime) >= 0) {
            //未来日（当日以降）の場合は取得しない
            monthReportList = new ArrayList<>();
        } else {
            //上記以外は指定範囲で取得
            monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        }

        if (monthReportList == null || monthReportList.isEmpty()) {
            headerResultSet.setTargetPowerValue(null);
        } else {
            headerResultSet.setTargetPowerValue(monthReportList.get(0).getTargetKw());
        }

        // 時間帯別実績情報を作成する
        timeDetailActualList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);
        timeDetailPredictionList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);
        timeDetailReductionList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);

        //デマンド日報系統から実績値を取得する
        CommonDemandDayReportLineListResult dayReportParam = DemandEmsUtility.getDayReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(), summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom(),
                summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoTo());

        if (summaryRange.getMeasurementDateFrom().compareTo(serverDateTime) >= 0) {
            //未来日（当日以降）の場合は取得しない
            dayRepLineList = new ArrayList<>();
        } else {
            //上記以外は指定範囲で取得
            dayRepLineList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportParam);
        }

        //取得した実績値を時間帯別実績に詰める
        if (dayRepLineList != null && !dayRepLineList.isEmpty()) {
            for (CommonDemandDayReportLineListResult dayRepLine : dayRepLineList) {
                for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailActualList) {
                    if (timeDetail.getMeasurementDate().equals(dayRepLine.getMeasurementDate())
                            && timeDetail.getJigenNo().compareTo(dayRepLine.getJigenNo()) == 0) {
                        //一致した場合は、値を設定し、このループを抜ける
                        timeDetail.setResultValue(dayRepLine.getLineValueKw());
                        break;
                    }
                }
            }
        }

        //機器制御スケジュール履歴取得（範囲内）
        SmControlScheduleLogListDetailResultData scheduleLogParam = DemandVerifyUtility
                .getSmControlScheduleLogListParam(null, parameter.getSmId(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, null);
        List<SmControlScheduleLogListDetailResultData> scheduleList = getResultList(
                smControlScheduleLogListServiceDaoImpl, scheduleLogParam);
        if (scheduleList == null || scheduleList.isEmpty()) {
            scheduleList = new ArrayList<>();
        }

        //機器制御スケジュール履歴取得（範囲外・1つ前）
        scheduleLogParam = DemandVerifyUtility.getSmControlScheduleLogListParam(null, parameter.getSmId(), null, null,
                settingUpdateDateTimeFrom);
        List<SmControlScheduleLogListDetailResultData> tempScheduleList = getResultList(
                smControlScheduleLogListServiceDaoImpl, scheduleLogParam);
        if (tempScheduleList != null && !tempScheduleList.isEmpty()) {
            //機器制御スケジュール履歴IDの降順にソート
            tempScheduleList = DemandVerifyUtility.sortScheduleLogList(tempScheduleList,
                    ApiCodeValueConstants.SORT_ORDER.DESC.getVal());
            //先頭レコードのみ範囲内のデータに追加する
            scheduleList.add(tempScheduleList.get(0));

        }

        if (scheduleList != null && !scheduleList.isEmpty()) {
            //機器制御スケジュール履歴IDの昇順にソート
            scheduleList = DemandVerifyUtility.sortScheduleLogList(scheduleList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        //機器制御スケジュール設定履歴、機器制御スケジュール時間帯履歴取得
        List<SmControlScheduleSetLogListDetailResultData> scheduleSetList = new ArrayList<>();
        List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeList = new ArrayList<>();

        if (!scheduleList.isEmpty()) {
            for (SmControlScheduleLogListDetailResultData schedule : scheduleList) {
                SmControlScheduleSetLogListDetailResultData setParam = DemandVerifyUtility
                        .getSmControlScheduleSetLogListParam(schedule.getSmControlScheduleLogId(), parameter.getSmId(),
                                null, null, null, null);
                scheduleSetList.addAll(getResultList(smControlScheduleSetLogListServiceDaoImpl, setParam));
                SmControlScheduleTimeLogListDetailResultData timeParam = DemandVerifyUtility
                        .getSmControlScheduleTimeLogListParam(schedule.getSmControlScheduleLogId(),
                                parameter.getSmId(), null, null, null, null);
                scheduleTimeList.addAll(getResultList(smControlScheduleTimeLogListServiceDaoImpl, timeParam));
            }

            //機器制御スケジュール設定履歴ID、制御負荷、対象月順にソート
            scheduleSetList = DemandVerifyUtility.sortScheduleSetLogList(scheduleSetList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //機器制御スケジュール設定履歴ID、パターン番号、時間帯番号順にソート
            scheduleTimeList = DemandVerifyUtility.sortScheduleTimeLogList(scheduleTimeList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //機器制御スケジュール時間帯履歴の情報を電気が使える時間帯から電気が使えない時間帯に変換する
            scheduleTimeList = DemandVerifyUtility.createTimeSchedule(scheduleTimeList);

        }

        //機器制御祝日設定履歴取得（範囲内）
        SmControlHolidayLogListDetailResultData holidayLogParam = DemandVerifyUtility.getSmControlHolidayLogListParam(
                null, parameter.getSmId(), settingUpdateDateTimeFrom, settingUpdateDateTimeTo, null);
        List<SmControlHolidayLogListDetailResultData> holidayList = getResultList(smControlHolidayLogListServiceDaoImpl,
                holidayLogParam);
        if (holidayList == null || holidayList.isEmpty()) {
            holidayList = new ArrayList<>();
        }

        //機器制御祝日設定履歴取得（範囲外・1つ前）
        holidayLogParam = DemandVerifyUtility.getSmControlHolidayLogListParam(null, parameter.getSmId(), null, null,
                settingUpdateDateTimeFrom);
        List<SmControlHolidayLogListDetailResultData> tempHolidayList = getResultList(
                smControlHolidayLogListServiceDaoImpl, holidayLogParam);
        if (tempHolidayList != null && !tempHolidayList.isEmpty()) {
            //機器制御祝日設定履歴IDの降順でソート
            tempHolidayList = DemandVerifyUtility.sortHolidayLogList(tempHolidayList,
                    ApiCodeValueConstants.SORT_ORDER.DESC.getVal());
            //先頭のレコードのみ範囲内のデータに追加する
            holidayList.add(tempHolidayList.get(0));
        }

        //機器制御祝日設定カレンダ履歴取得
        List<SmControlHolidayCalLogListDetailResultData> holidayCalList = new ArrayList<>();

        if (!holidayList.isEmpty()) {
            for (SmControlHolidayLogListDetailResultData holiday : holidayList) {
                SmControlHolidayCalLogListDetailResultData holidayCalParam = DemandVerifyUtility
                        .getSmControlHolidayCalLogListParam(holiday.getSmControlHolidayLogId(), parameter.getSmId(),
                                null, null);
                holidayCalList.addAll(getResultList(smControlHolidayCalLogListServiceDaoImpl, holidayCalParam));
            }
        }

        //機器制御祝日設定履歴ID、祝日月日でソート
        holidayCalList = DemandVerifyUtility.sortHolidayCalLogList(holidayCalList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        // 負荷制御履歴取得
        //範囲内の負荷制御ログ情報を取得する。
        if (summaryRange.getMeasurementDateFrom().compareTo(serverDateTime) >= 0) {
            //未来日（当日以降）の場合は取得しない
            tLoadControlLogResultSetList = new ArrayList<>();
        } else {
            //上記以外は指定範囲で取得
            tLoadControlLogResultSetList = getLoadControlLogList(parameter.getSmId(), recordYmdHmFrom, recordYmdHmTo);
        }

        //制御状態の桁数チェック
        if (!DemandVerifyUtility.checkLoadControlList(tLoadControlLogResultSetList, controlLoadCount)) {
            //桁数が不正の場合、処理を終了する。
            return new DemandOrgDailyKensyoDataResult();
        }

        //負荷ごとに振り分ける
        controlLoadSet = DemandVerifyUtility.createControlLoadSet(tLoadControlLogResultSetList, controlLoadCount,
                recordYmdHmFrom);

        //時限Noを付与する
        if(beforeG2Flg) {
            timeTableSet = DemandVerifyUtility.createDemandControlTimeTable(controlLoadSet);
        } else {
            timeTableSet = DemandVerifyUtility.createDemandControlTimeTable(controlLoadSet,productControlLoadData.getDemandControlFlgData());
        }

        //スケジュールのstart/Endの状態を設定する
        if(beforeG2Flg) {
            commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                    scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                    settingUpdateDateTimeTo);
        } else if (productControlLoadData.getScheduleControlFlgData().getProductControlLoadList() != null
                && !productControlLoadData.getScheduleControlFlgData().getProductControlLoadList().isEmpty()) {
            commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                    scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                    settingUpdateDateTimeTo);
        } else {
            commonScheduleResultSet = new LinkedHashSet<>();
        }

        //スケジュールによる制御を除く
        timeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet, timeTableSet,
                settingUpdateDateTimeTo, Boolean.FALSE);

        //サマリー情報を作成する
        if(beforeG2Flg) {
            summaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(timeTableSet, smLineControlVerifyList,
                    summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(),
                    controlLoadCount);
        } else {
            summaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(timeTableSet, smLineControlVerifyList, summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), productControlLoadData.getDemandControlFlgData());
        }

        //機器サマリー情報を作成する
        smSummaryList = DemandVerifyUtility.createDemandControlSmSummary(summaryList);

        if (smSummaryList != null && !smSummaryList.isEmpty()) {
            //機器サマリー情報の内容を時間帯別実績情報に詰める
            controlCount = BigDecimal.ZERO;
            for (CommonDemandControlSmSummaryResult smSummary : smSummaryList) {
                if (smSummary.getControlCount() != null) {
                    controlCount = controlCount.add(smSummary.getControlCount());
                }
                for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailReductionList) {
                    if (timeDetail.getMeasurementDate().equals(smSummary.getMeasurementDate())
                            && timeDetail.getJigenNo().compareTo(smSummary.getJigenNo()) == 0) {
                        //一致した場合は、削減値を設定し、このループを抜ける
                        timeDetail.setResultValue(smSummary.getReductionValue());
                        break;
                    }
                }
            }
        }

        //明細のサマリ部とヘッダ部を算出する
        for (int i = 0; i <= timeDetailActualList.size() - 1; i++) {
            if (timeDetailActualList.get(i).getResultValue() == null) {
                //実績値が取得できない場合、予測値も削減値もNULL
                timeDetailPredictionList.get(i).setResultValue(null);
                timeDetailReductionList.get(i).setResultValue(null);
            } else if (timeDetailReductionList.get(i).getResultValue() == null) {
                //削減値が取得できない場合は、予測値は実績値と同じ、削減値は0に設定する
                timeDetailReductionList.get(i).setResultValue(BigDecimal.ZERO);
                timeDetailPredictionList.get(i).setResultValue(timeDetailActualList.get(i).getResultValue());
            } else {
                //上記以外は、実績値 + 削減値が予測値
                timeDetailPredictionList.get(i)
                        .setResultValue(timeDetailActualList.get(i).getResultValue()
                                .add(timeDetailReductionList.get(i).getResultValue()));
            }
        }

        //実績値の精査
        for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailActualList) {
            if (timeDetail.getResultValue() != null) {
                if (maxActual == null) {
                    maxActual = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(maxActual) > 0) {
                    maxActual = timeDetail.getResultValue();
                }
                if (minActual == null) {
                    minActual = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(minActual) < 0) {
                    minActual = timeDetail.getResultValue();
                }
                if (summaryActual == null) {
                    summaryActual = timeDetail.getResultValue();
                } else {
                    summaryActual = summaryActual.add(timeDetail.getResultValue());
                }
                if (countActual == null) {
                    countActual = BigDecimal.ONE;
                } else {
                    countActual = countActual.add(BigDecimal.ONE);
                }
            }
        }

        //予測値の精査
        for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailPredictionList) {
            if (timeDetail.getResultValue() != null) {
                if (maxPrediction == null) {
                    maxPrediction = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(maxPrediction) > 0) {
                    maxPrediction = timeDetail.getResultValue();
                }
                if (minPrediction == null) {
                    minPrediction = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(minPrediction) < 0) {
                    minPrediction = timeDetail.getResultValue();
                }
                if (summaryPrediction == null) {
                    summaryPrediction = timeDetail.getResultValue();
                } else {
                    summaryPrediction = summaryPrediction.add(timeDetail.getResultValue());
                }
                if (countPrediction == null) {
                    countPrediction = BigDecimal.ONE;
                } else {
                    countPrediction = countPrediction.add(BigDecimal.ONE);
                }
            }
        }

        //削減値の精査
        for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailReductionList) {
            if (timeDetail.getResultValue() != null) {
                if (maxReduction == null) {
                    maxReduction = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(maxReduction) > 0) {
                    maxReduction = timeDetail.getResultValue();
                }
                if (minReduction == null) {
                    minReduction = timeDetail.getResultValue();
                } else if (timeDetail.getResultValue().compareTo(minReduction) < 0) {
                    minReduction = timeDetail.getResultValue();
                }
                if (summaryReduction == null) {
                    summaryReduction = timeDetail.getResultValue();
                } else {
                    summaryReduction = summaryReduction.add(timeDetail.getResultValue());
                }
                if (countReduction == null) {
                    countReduction = BigDecimal.ONE;
                } else {
                    countReduction = countReduction.add(BigDecimal.ONE);
                }
            }
        }

        //平均値を算出する
        if (countActual != null && summaryActual != null) {
            averageActual = summaryActual.divide(countActual, 1, BigDecimal.ROUND_HALF_UP);
        }
        if (countPrediction != null && summaryPrediction != null) {
            averagePrediction = summaryPrediction.divide(countPrediction, 1, BigDecimal.ROUND_HALF_UP);
        }
        if (countReduction != null && summaryReduction != null) {
            averageReduction = summaryReduction.divide(countReduction, 1, BigDecimal.ROUND_HALF_UP);
        }

        //実績値を詰める
        DemandOrgDailyKensyoDataDetailResultData detailActual = new DemandOrgDailyKensyoDataDetailResultData();
        detailActual.setColumnHeaderTitle("実績値（制御後）　　kW");
        detailActual.setTimeResultList(timeDetailActualList);
        detailActual.setMaxValue(maxActual);
        detailActual.setMinValue(minActual);
        detailActual.setAverageValue(averageActual);

        //予測値を詰める
        DemandOrgDailyKensyoDataDetailResultData detailPrediction = new DemandOrgDailyKensyoDataDetailResultData();
        detailPrediction.setColumnHeaderTitle("未制御時の予測値　　kW");
        detailPrediction.setTimeResultList(timeDetailPredictionList);
        detailPrediction.setMaxValue(maxPrediction);
        detailPrediction.setMinValue(minPrediction);
        detailPrediction.setAverageValue(averagePrediction);

        //削減値を詰める
        DemandOrgDailyKensyoDataDetailResultData detailReduction = new DemandOrgDailyKensyoDataDetailResultData();
        detailReduction.setColumnHeaderTitle("削減値　　　　　　　kW");
        detailReduction.setTimeResultList(timeDetailReductionList);
        detailReduction.setMaxValue(maxReduction);
        detailReduction.setMinValue(minReduction);
        detailReduction.setAverageValue(averageReduction);

        //明細情報を詰める
        detailList.add(detailActual);
        detailList.add(detailPrediction);
        detailList.add(detailReduction);

        //ヘッダ情報を詰める
        if (controlCount == null && maxActual != null) {
            headerResultSet.setControlCount(BigDecimal.ZERO);
        } else {
            headerResultSet.setControlCount(controlCount);
        }
        headerResultSet.setMaxDemandValue(maxActual);
        headerResultSet.setMaxPredictionValue(maxPrediction);
        headerResultSet.setMaxReductionValue(maxReduction);

        //結果を詰める
        rootResultSet.setHeader(headerResultSet);
        rootResultSet.setDetailList(detailList);
        return rootResultSet;
    }

    /**
     * 指定範囲内の負荷制御ログを取得する
     * @param smId
     * @param recordYmdHmFrom
     * @param recordYmdHmTo
     * @return
     */
    private List<TLoadControlLogResult> getLoadControlLogList(Long smId, String recordYmdHmFrom, String recordYmdHmTo) {
        List<TLoadControlLogResult> resultList;
        //範囲内の負荷制御ログ情報を取得する
        TLoadControlLogResult loadControlLogResultSetParam = DemandVerifyUtility
                .getLoadControlLogResultParam(smId, recordYmdHmFrom, recordYmdHmTo, null, 1);
        resultList = getResultList(tLoadControlLogServiceDaoImpl, loadControlLogResultSetParam);
        if (resultList == null || resultList.isEmpty()) {
            resultList = new ArrayList<>();
        }

        //1つ前のデータを取得する（前日または前時限からのまたぎの場合、制御回数をカウントしてはいけないため）
        //ただし、先頭レコードが範囲の開始と同じ場合は処理をしない
        if (resultList.isEmpty() || !resultList.get(0).getRecordYmdhm().equals(recordYmdHmFrom)) {
            loadControlLogResultSetParam = DemandVerifyUtility.getLoadControlLogResultParam(smId, null, null,
                    recordYmdHmFrom, 2);
            List<TLoadControlLogResult> tempResultList = getResultList(tLoadControlLogServiceDaoImpl,
                    loadControlLogResultSetParam);
            if (tempResultList == null || tempResultList.isEmpty()) {
                return resultList;
            } else {
                //先頭のデータのみresultListの先頭に挿入する
                resultList.add(0, tempResultList.get(0));
            }
        }

        return resultList;
    }

}
