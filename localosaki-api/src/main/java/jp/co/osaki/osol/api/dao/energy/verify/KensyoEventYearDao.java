package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoEventYearParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.energy.verify.KensyoEventYearResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TempHumidControlLogVerifyResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlySmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlyTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlYearlyMonthSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventYearDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventYearHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventYearTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.EventControlLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayCalLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleSetLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleTimeLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.TempHumidControlLogVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MProductSpecServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmControlLoadVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineControlLoadVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.api.utility.energy.setting.ProductControlLoadFlgSeparateUtility;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MSmControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * イベント制御検証・年報 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class KensyoEventYearDao extends OsolApiDao<KensyoEventYearParameter> {

    //TODO EntityServiceDaoクラスを使わないほうがいい
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    private final MSmLineVerifyServiceDaoImpl mSmLineVerifyServiceDaoImpl;
    private final MSmLineControlLoadVerifyServiceDaoImpl mSmLineControlLoadVerifyServiceDaoImpl;
    private final MSmControlLoadVerifyServiceDaoImpl mSmControlLoadVerifyServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final TempHumidControlLogVerifyServiceDaoImpl tempHumidControlLogServiceDaoImpl;
    private final EventControlLogServiceDaoImpl eventControlLogServiceDaoImpl;
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

    public KensyoEventYearDao() {
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        mProductSpecServiceDaoImpl = new MProductSpecServiceDaoImpl();
        mSmLineVerifyServiceDaoImpl = new MSmLineVerifyServiceDaoImpl();
        mSmLineControlLoadVerifyServiceDaoImpl = new MSmLineControlLoadVerifyServiceDaoImpl();
        mSmControlLoadVerifyServiceDaoImpl = new MSmControlLoadVerifyServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        tempHumidControlLogServiceDaoImpl = new TempHumidControlLogVerifyServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        eventControlLogServiceDaoImpl = new EventControlLogServiceDaoImpl();
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
    public KensyoEventYearResult query(KensyoEventYearParameter parameter) throws Exception {

        KensyoEventYearResult result = new KensyoEventYearResult();
        KensyoEventYearHeaderResultData header = new KensyoEventYearHeaderResultData();
        KensyoEventYearDetailResultData beforeLoadFactorDetail = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData beforeUsedValue = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData afterUsedValue = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData reductionValue = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData controlCount = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData proposalUsedValue = new KensyoEventYearDetailResultData();
        KensyoEventYearDetailResultData judgeValue = new KensyoEventYearDetailResultData();
        BigDecimal summaryBeforeLoadFactorDetail = null;
        BigDecimal countBeforeLoadFactorDetail = null;
        BigDecimal maxBeforeLoadFactorDetail = null;
        BigDecimal minBeforeLoadFactorDetail = null;
        BigDecimal summaryBeforeUsedValue = null;
        BigDecimal countBeforeUsedValue = null;
        BigDecimal maxBeforeUsedValue = null;
        BigDecimal minBeforeUsedValue = null;
        BigDecimal summaryAfterUsedValue = null;
        BigDecimal countAfterUsedValue = null;
        BigDecimal maxAfterUsedValue = null;
        BigDecimal minAfterUsedValue = null;
        BigDecimal summaryReductionValue = null;
        BigDecimal countReductionValue = null;
        BigDecimal maxReductionValue = null;
        BigDecimal minReductionValue = null;
        BigDecimal summaryControlCount = null;
        BigDecimal countControlCount = null;
        BigDecimal maxControlCount = null;
        BigDecimal minControlCount = null;
        BigDecimal summaryProposalUsedValue = null;
        BigDecimal countProposalUsedValue = null;
        BigDecimal maxProposalUsedValue = null;
        BigDecimal minProposalUsedValue = null;
        Integer controlLoadCount;
        String recordYmdHmsFrom;
        String recordYmdHmsTo;
        Timestamp settingUpdateDateTimeFrom;
        Timestamp settingUpdateDateTimeTo;
        String productCd;
        Date measurementDateFrom = null;
        Date measurementDateTo = null;
        BigDecimal overMonthCount = null;
        List<CommonDemandMonthReportLineListResult> monthlyReportList;
        List<TempHumidControlLogVerifyResult> tempHumidControlLogResultSetList;
        List<EventControlLogResult> eventControlLogResultSetList;
        List<EventControlLogResult> tempEventControlLogResultSetList;
        LinkedHashSet<CommonControlLoadResult> tempHumidControlLoadSet;
        LinkedHashSet<CommonControlLoadResult> eventControlLoadSet;
        LinkedHashSet<CommonDemandControlTimeTableResult> controlEventTimeTableSet;
        LinkedHashSet<CommonScheduleResult> commonScheduleResultSet;
        List<CommonEventControlMonthlyTimeTableResult> timeTableSummaryList;
        List<CommonEventControlMonthlySmSummaryResult> smSummaryList;
        List<CommonEventControlYearlyMonthSummaryResult> monthSummaryList = new ArrayList<>();
        CommonEventControlYearlyMonthSummaryResult monthSummary;
        List<DemandCalendarYearData> calList = new ArrayList<>();
        String sumDate = null;
        ProductControlLoadFlgSeparateListResult productControlLoadData = null;
        boolean beforeG2Flg;

        //最終レコード保持用
        TempHumidControlLogVerifyResult tempHumidLastResult = null;
        Map<BigDecimal, EventControlLogResult> eventLastResultMap = new HashMap<>();

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

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、1（年）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
        }

        //機器情報を取得する
        SmSelectResult smSelectParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smSelectParam);
        if (smList == null || smList.size() != 1) {
            //機器情報が取得できない場合は、処理を終了する
            return new KensyoEventYearResult();
        } else {
            header.setSmAddress(smList.get(0).getSmAddress());
            header.setIpAddress(smList.get(0).getIpAddress());
            //製品情報を取得する
            MProductSpec productParam = new MProductSpec();
            productParam.setProductCd(smList.get(0).getProductCd());
            MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
            if (productSpec == null || OsolConstants.FLG_ON.equals(productSpec.getDelFlg())) {
                //製品情報が取得できない場合または削除されている場合は、処理を終了する
                return new KensyoEventYearResult();
            } else {
                controlLoadCount = productSpec.getLoadControlOutput();
                header.setProductName(productSpec.getProductName());
                productCd = productSpec.getProductCd();
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
                return new KensyoEventYearResult();
            }else {
                //controlLoadCountを書き換える
                controlLoadCount = productControlLoadData.getEventControlFlgData().getControlLoadCount();
            }
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
            header.setBasicRateUnitPrice(null);
        } else {
            header.setBasicRateUnitPrice(smLineVerifyList.get(0).getCommodityChargeUnitPrice());
        }

        //機器制御負荷検証を取得する
        MSmControlLoadVerify smControlParam = DemandVerifyUtility.getSmControlLoadVerifyParam(parameter.getSmId());
        List<MSmControlLoadVerify> smControlLoadList = getResultList(mSmControlLoadVerifyServiceDaoImpl,
                smControlParam);

        //系統情報を取得する
        LineListDetailResultData lineListParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineListParam);
        if (lineList == null || lineList.size() != 1) {
            header.setLineUnit(null);
        } else if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
            header.setLineUnit(lineList.get(0).getLineUnit());
        } else {
            header.setLineUnit(ApiSimpleConstants.UNIT_USE_POWER);
        }

        //年報カレンダを取得する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new KensyoEventYearResult();
            }
            calList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            sumDate = corpDemandList.get(0).getSumDate();
            if (calList == null || calList.isEmpty()) {
                return new KensyoEventYearResult();
            }
        } else {
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
                //建物デマンド情報を取得する
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
                List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                        buildingDemandListServiceDaoImpl, buildingDemandParam);
                if (buildingDemandList == null || buildingDemandList.size() != 1) {
                    return new KensyoEventYearResult();
                } else {
                    sumDate = buildingDemandList.get(0).getSumDate();
                }
            }

            calList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    sumDate);
            if (calList == null || calList.isEmpty()) {
                return new KensyoEventYearResult();
            }
        }

        //集計範囲を取得する
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(),
                parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //時間帯別実績リストを作成する
        beforeLoadFactorDetail.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        beforeUsedValue.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        afterUsedValue.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        reductionValue.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        controlCount.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        proposalUsedValue.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));
        judgeValue.setTimeResultList(DemandDataUtility.createEventYearlyKensyoDetailHeader(summaryRange));

        //集計範囲開始から集計範囲終了まで月単位で処理を行う
        String currentYear = summaryRange.getYearFrom();
        BigDecimal currentMonth = summaryRange.getMonthFrom();
        int index = -1;
        do {

            //年月の移動を行う
            if (index != -1) {
                if (currentMonth.intValue() == 12) {
                    //12月の場合
                    currentYear = String.valueOf(Integer.valueOf(currentYear) + 1);
                    currentMonth = BigDecimal.ONE;
                } else {
                    currentMonth = currentMonth.add(BigDecimal.ONE);
                }

            }

            //インデックスのカウントアップ
            index++;

            //年報カレンダから開始、終了を取得する
            String filterYear = currentYear;
            BigDecimal filterMonth = currentMonth;
            List<DemandCalendarYearData> tempCalList = calList.stream()
                    .filter(i -> filterYear.equals(i.getYearNo()) && filterMonth.compareTo(i.getMonthNo()) == 0)
                    .collect(Collectors.toList());
            if (tempCalList != null && tempCalList.size() == 1) {
                measurementDateFrom = tempCalList.get(0).getMonthStartDate();
                measurementDateTo = tempCalList.get(0).getMonthEndDate();
            }

            //イベントログ、温湿度制御ログ上の開始、終了を設定する
            recordYmdHmsFrom = DemandVerifyUtility.getRecordYmdHmsFromForMonthlyYearly(measurementDateFrom);
            recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(measurementDateTo);

            //スケジュール上の開始、終了を設定する
            settingUpdateDateTimeFrom = DemandVerifyUtility
                    .getSettingUpdateDateTimeFrom(measurementDateFrom, BigDecimal.ONE);
            settingUpdateDateTimeTo = DemandVerifyUtility.getSettingUpdateDateTimeTo(measurementDateTo,
                    new BigDecimal("48"));

            //デマンド月報からデータを取得する
            CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), measurementDateFrom, measurementDateTo);

            if (measurementDateFrom.compareTo(nowDate) >= 0) {
                //開始が当日以降の場合、取得しない
                monthlyReportList = new ArrayList<>();
            } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                //終了が当日以降の場合、前日までのデータを取得する
                monthReportParam.setMeasurementDateTo(DateUtility.plusDay(nowDate, -1));
                monthlyReportList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);
            } else {
                //上記以外は元の指定範囲で取得する
                monthlyReportList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);
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
            scheduleLogParam = DemandVerifyUtility.getSmControlScheduleLogListParam(null, parameter.getSmId(), null,
                    null, settingUpdateDateTimeFrom);
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
                            .getSmControlScheduleSetLogListParam(schedule.getSmControlScheduleLogId(),
                                    parameter.getSmId(),
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
            SmControlHolidayLogListDetailResultData holidayLogParam = DemandVerifyUtility
                    .getSmControlHolidayLogListParam(
                            null, parameter.getSmId(), settingUpdateDateTimeFrom, settingUpdateDateTimeTo, null);
            List<SmControlHolidayLogListDetailResultData> holidayList = getResultList(
                    smControlHolidayLogListServiceDaoImpl,
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

            //イベント制御ログ、温湿度制御ログを取得する
            if (OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd)) {

                //温湿度制御ログを取得する
                if (index == 0) {
                    //初回のみ範囲のレコードと1つ前のレコードを取得
                    if (measurementDateFrom.compareTo(nowDate) >= 0) {
                        //Fromが未来（当日以降）の場合は取得しない
                        tempHumidControlLogResultSetList = new ArrayList<>();
                    } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                        //Toが未来（当日以降）の場合、前日までにして取得
                        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                        tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(),
                                recordYmdHmsFrom, recordYmdHmsTo, Boolean.TRUE);
                    } else {
                        //上記以外は指定範囲で取得
                        tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(),
                                recordYmdHmsFrom, recordYmdHmsTo, Boolean.TRUE);
                    }

                    //最終レコードを1つ前のレコードとして保持
                    if (tempHumidControlLogResultSetList != null && !tempHumidControlLogResultSetList.isEmpty()) {
                        tempHumidLastResult = tempHumidControlLogResultSetList
                                .get(tempHumidControlLogResultSetList.size() - 1);
                    }
                } else {
                    //2回目以降は範囲のレコードのみ取得
                    if (measurementDateFrom.compareTo(nowDate) >= 0) {
                        //Fromが未来（当日以降）の場合は取得しない
                        tempHumidControlLogResultSetList = new ArrayList<>();
                    } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                        //Toが未来（当日以降）の場合、前日までにして取得
                        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                        tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(),
                                recordYmdHmsFrom, recordYmdHmsTo, Boolean.FALSE);
                    } else {
                        //上記以外は指定範囲で取得
                        tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(),
                                recordYmdHmsFrom, recordYmdHmsTo, Boolean.FALSE);
                    }

                    if (tempHumidLastResult != null) {
                        //1つ前のレコード（前回の最終レコード）を追加する
                        if (tempHumidControlLogResultSetList == null || tempHumidControlLogResultSetList.isEmpty()) {
                            //このケースは前回の最終レコードの追加のみ。今回も最終レコードになるので
                            tempHumidControlLogResultSetList = new ArrayList<>();
                            tempHumidControlLogResultSetList.add(tempHumidLastResult);
                        } else {
                            if (!tempHumidControlLogResultSetList.get(0).getRecordYmdhms().equals(recordYmdHmsFrom)) {
                                //Fromと異なる場合だけ、前回の最終レコードを追加
                                tempHumidControlLogResultSetList.add(0, tempHumidLastResult);
                            }
                            //最終レコードを1つ前のレコードとして保持
                            tempHumidLastResult = tempHumidControlLogResultSetList
                                    .get(tempHumidControlLogResultSetList.size() - 1);
                        }
                    }
                }

                if (measurementDateTo.compareTo(nowDate) >= 0 && tempHumidControlLogResultSetList != null
                        && !tempHumidControlLogResultSetList.isEmpty()) {
                    //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                    tempHumidControlLogResultSetList = DemandVerifyUtility.addTempHumidLogRecord(
                            tempHumidControlLogResultSetList,
                            DateUtility.conversionDate(DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1)),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                }

                //負荷ごとに振り分ける
                tempHumidControlLoadSet = DemandVerifyUtility.createTempHumidControlSet(
                        tempHumidControlLogResultSetList,
                        controlLoadCount, recordYmdHmsFrom);

                //分解する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(tempHumidControlLoadSet);

                //スケジュールのstart/Endの状態を設定する
                commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                        scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo);

                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(
                        commonScheduleResultSet,
                        controlEventTimeTableSet, settingUpdateDateTimeTo, Boolean.TRUE);

                //タイムテーブルを作成する
                timeTableSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryMonthlyYearly(
                        controlEventTimeTableSet,
                        measurementDateFrom,
                        measurementDateTo, controlLoadCount, monthlyReportList, smLineControlVerifyList,
                        smLineVerifyList,
                        smControlLoadList, commonScheduleResultSet);

                //機器サマリを作成する
                smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                        controlLoadCount,
                        monthlyReportList,
                        smLineControlVerifyList, smLineVerifyList, smControlLoadList, parameter.getOperationCorpId(),
                        calList, sumDate);

                //月単位のサマリ情報を作成する
                monthSummary = DemandVerifyUtility.createEventControlMonthSummaryYearly(smSummaryList, smLineVerifyList,
                        controlLoadCount, smLineControlVerifyList, smControlLoadList, currentYear, currentMonth);

            } else if(OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)){

                eventControlLogResultSetList = new ArrayList<>();
                //イベント制御ログを取得する（制御負荷数分ループする)
                for (int i = 1; i <= controlLoadCount; i++) {
                    if (index == 0) {
                        //初回のみ範囲のレコードと1つ前のレコードを取得する
                        if (measurementDateFrom.compareTo(nowDate) >= 0) {
                            //Fromが未来（当日以降）の場合は取得しない
                            tempEventControlLogResultSetList = new ArrayList<>();
                        } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                            //Toが未来（当日以降）の場合、前日までにして取得
                            recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, BigDecimal.valueOf(i), Boolean.TRUE);
                        } else {
                            //上記以外は指定範囲で取得
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, BigDecimal.valueOf(i), Boolean.TRUE);
                        }

                        //最終レコードを1つ前のレコードとして保持
                        if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                            eventLastResultMap.put(BigDecimal.valueOf(i),
                                    tempEventControlLogResultSetList.get(tempEventControlLogResultSetList.size() - 1));
                        }
                    } else {
                        //2回目以降は範囲のレコードのみ取得
                        if (measurementDateFrom.compareTo(nowDate) >= 0) {
                            //Fromが未来（当日以降）の場合は取得しない
                            tempEventControlLogResultSetList = new ArrayList<>();
                        } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                            //Toが未来（当日以降）の場合、前日までにして取得
                            recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, BigDecimal.valueOf(i), Boolean.FALSE);
                        } else {
                            //上記以外は指定範囲で取得
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, BigDecimal.valueOf(i), Boolean.FALSE);
                        }

                        if (eventLastResultMap.get(BigDecimal.valueOf(i)) != null) {
                            //1つ前のレコード（前回の最終レコード）を追加する
                            if (tempEventControlLogResultSetList == null
                                    || tempEventControlLogResultSetList.isEmpty()) {
                                //このケースは前回の最終レコードの追加のみ。今回も最終レコードになるので
                                tempEventControlLogResultSetList = new ArrayList<>();
                                tempEventControlLogResultSetList.add(eventLastResultMap.get(BigDecimal.valueOf(i)));
                            } else {
                                if (!tempEventControlLogResultSetList.get(0).getRecordYmdhms()
                                        .equals(recordYmdHmsFrom)) {
                                    //Fromと異なる場合だけ、前回の最終レコードを追加
                                    tempEventControlLogResultSetList.add(0,
                                            eventLastResultMap.get(BigDecimal.valueOf(i)));
                                }
                                //最終レコードを1つ前のレコードとして保持
                                eventLastResultMap.put(BigDecimal.valueOf(i),
                                        tempEventControlLogResultSetList
                                                .get(tempEventControlLogResultSetList.size() - 1));
                            }
                        }
                    }

                    if (measurementDateTo.compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
                            && !tempEventControlLogResultSetList.isEmpty()) {
                        //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                        tempEventControlLogResultSetList = DemandVerifyUtility.addEventLogRecord(
                                tempEventControlLogResultSetList,
                                DateUtility.conversionDate(
                                        DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1)),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                productCd, i);
                    }

                    if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                        eventControlLogResultSetList.addAll(tempEventControlLogResultSetList);
                    }
                }

                //負荷ごとに振り分ける
                eventControlLoadSet = DemandVerifyUtility.createEventControlSet(eventControlLogResultSetList,
                        controlLoadCount, recordYmdHmsFrom, productCd);

                //分解する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(eventControlLoadSet);

                //スケジュールのstart/Endの状態を設定する
                commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                        scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo);

                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                        controlEventTimeTableSet, settingUpdateDateTimeTo, Boolean.TRUE);

                //タイムテーブルを作成する
                timeTableSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryMonthlyYearly(
                        controlEventTimeTableSet, measurementDateFrom, measurementDateTo, controlLoadCount,
                        monthlyReportList, smLineControlVerifyList, smLineVerifyList,
                        smControlLoadList, commonScheduleResultSet);

                //機器サマリを作成する
                smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                        controlLoadCount, monthlyReportList, smLineControlVerifyList, smLineVerifyList,
                        smControlLoadList, parameter.getOperationCorpId(), calList, sumDate);

                //月単位のサマリ情報を作成する
                monthSummary = DemandVerifyUtility.createEventControlMonthSummaryYearly(smSummaryList, smLineVerifyList,
                        controlLoadCount, smLineControlVerifyList, smControlLoadList, currentYear, currentMonth);

            } else {
                //Eα以降
                eventControlLogResultSetList = new ArrayList<>();
                //イベント制御ログを取得する（対象制御負荷数分ループする)
                for (ProductControlLoadListDetailResultData eventFlgData : productControlLoadData.getEventControlFlgData().getProductControlLoadList()) {
                    if (index == 0) {
                        //初回のみ範囲のレコードと1つ前のレコードを取得する
                        if (measurementDateFrom.compareTo(nowDate) >= 0) {
                            //Fromが未来（当日以降）の場合は取得しない
                            tempEventControlLogResultSetList = new ArrayList<>();
                        } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                            //Toが未来（当日）の場合、前日までにして取得
                            recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad(), Boolean.TRUE);
                        } else {
                            //上記以外は指定範囲で取得
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad(), Boolean.TRUE);
                        }

                        //最終レコードを1つ前のレコードとして保持
                        if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                            eventLastResultMap.put(eventFlgData.getControlLoad(),
                                    tempEventControlLogResultSetList.get(tempEventControlLogResultSetList.size() - 1));
                        }
                    } else {
                        //2回目以降は範囲のレコードのみ取得
                        if (measurementDateFrom.compareTo(nowDate) >= 0) {
                            //Fromが未来（当日以降）の場合は取得しない
                            tempEventControlLogResultSetList = new ArrayList<>();
                        } else if (measurementDateTo.compareTo(nowDate) >= 0) {
                            //Toが未来（当日以降）の場合、前日までにして取得
                            recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad(), Boolean.FALSE);
                        } else {
                            //上記以外は指定範囲で取得
                            tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(),
                                    recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad(), Boolean.FALSE);
                        }

                        if (eventLastResultMap.get(eventFlgData.getControlLoad()) != null) {
                            //1つ前のレコード（前回の最終レコード）を追加する
                            if (tempEventControlLogResultSetList == null
                                    || tempEventControlLogResultSetList.isEmpty()) {
                                //このケースは前回の最終レコードの追加のみ。今回も最終レコードになるので
                                tempEventControlLogResultSetList = new ArrayList<>();
                                tempEventControlLogResultSetList.add(eventLastResultMap.get(eventFlgData.getControlLoad()));
                            } else {
                                if (!tempEventControlLogResultSetList.get(0).getRecordYmdhms()
                                        .equals(recordYmdHmsFrom)) {
                                    //Fromと異なる場合だけ、前回の最終レコードを追加
                                    tempEventControlLogResultSetList.add(0, eventLastResultMap.get(eventFlgData.getControlLoad()));
                                }
                                //最終レコードを1つ前のレコードとして保持
                                eventLastResultMap.put(eventFlgData.getControlLoad(),
                                        tempEventControlLogResultSetList
                                                .get(tempEventControlLogResultSetList.size() - 1));
                            }
                        }
                    }

                    if (measurementDateTo.compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
                            && !tempEventControlLogResultSetList.isEmpty()) {
                        //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                        tempEventControlLogResultSetList = DemandVerifyUtility.addEventLogRecord(
                                tempEventControlLogResultSetList,
                                DateUtility.conversionDate(
                                        DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1)),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                productCd, eventFlgData.getControlLoad().intValue());
                    }

                    if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                        eventControlLogResultSetList.addAll(tempEventControlLogResultSetList);
                    }
                }

                //負荷ごとに振り分ける
                eventControlLoadSet = DemandVerifyUtility.createEventControlSet(eventControlLogResultSetList,
                        controlLoadCount, recordYmdHmsFrom, productCd,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //分解する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(eventControlLoadSet,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //スケジュールのstart/Endの状態を設定する
                if(productControlLoadData.getScheduleControlFlgData().getProductControlLoadList() != null
                        && !productControlLoadData.getScheduleControlFlgData().getProductControlLoadList().isEmpty()) {
                    commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                            scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                            settingUpdateDateTimeTo);
                } else {
                    commonScheduleResultSet = new LinkedHashSet<>();
                }


                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                        controlEventTimeTableSet, settingUpdateDateTimeTo, Boolean.TRUE);

                //タイムテーブルを作成する
                timeTableSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryMonthlyYearly(
                        controlEventTimeTableSet, measurementDateFrom, measurementDateTo,
                        monthlyReportList, smLineControlVerifyList, smLineVerifyList,
                        smControlLoadList, commonScheduleResultSet,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //機器サマリを作成する
                smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                        monthlyReportList, smLineControlVerifyList, smLineVerifyList,
                        smControlLoadList, parameter.getOperationCorpId(), calList, sumDate,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //月単位のサマリ情報を作成する
                monthSummary = DemandVerifyUtility.createEventControlMonthSummaryYearly(smSummaryList, smLineVerifyList,
                        controlLoadCount, smLineControlVerifyList, smControlLoadList, currentYear, currentMonth,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

            }

            //月単位のサマリ情報を元に対象月の各項目の計算を行う
            if (monthSummary != null) {
                //制御前負荷率
                beforeLoadFactorDetail.getTimeResultList().get(index)
                        .setValue(monthSummary.getBeforeControlLoadFactor());
                if (beforeLoadFactorDetail.getTimeResultList().get(index).getValue() != null) {
                    if (summaryBeforeLoadFactorDetail == null) {
                        summaryBeforeLoadFactorDetail = beforeLoadFactorDetail.getTimeResultList().get(index)
                                .getValue();
                    } else {
                        summaryBeforeLoadFactorDetail = summaryBeforeLoadFactorDetail
                                .add(beforeLoadFactorDetail.getTimeResultList().get(index).getValue());
                    }
                    if (countBeforeLoadFactorDetail == null) {
                        countBeforeLoadFactorDetail = BigDecimal.ONE;
                    } else {
                        countBeforeLoadFactorDetail = countBeforeLoadFactorDetail.add(BigDecimal.ONE);
                    }
                    if (maxBeforeLoadFactorDetail == null) {
                        maxBeforeLoadFactorDetail = beforeLoadFactorDetail.getTimeResultList().get(index).getValue();
                    } else if (beforeLoadFactorDetail.getTimeResultList().get(index).getValue()
                            .compareTo(maxBeforeLoadFactorDetail) >= 0) {
                        maxBeforeLoadFactorDetail = beforeLoadFactorDetail.getTimeResultList().get(index).getValue();
                    }
                    if (minBeforeLoadFactorDetail == null) {
                        minBeforeLoadFactorDetail = beforeLoadFactorDetail.getTimeResultList().get(index).getValue();
                    } else if (beforeLoadFactorDetail.getTimeResultList().get(index).getValue()
                            .compareTo(minBeforeLoadFactorDetail) < 0) {
                        minBeforeLoadFactorDetail = beforeLoadFactorDetail.getTimeResultList().get(index).getValue();
                    }
                }

                //制御前使用量（補正後制御前使用量）
                beforeUsedValue.getTimeResultList().get(index)
                        .setValue(monthSummary.getCorrectionBeforeControlUsedValue());
                if (beforeUsedValue.getTimeResultList().get(index).getValue() != null) {
                    if (summaryBeforeUsedValue == null) {
                        summaryBeforeUsedValue = beforeUsedValue.getTimeResultList().get(index).getValue();
                    } else {
                        summaryBeforeUsedValue = summaryBeforeUsedValue
                                .add(beforeUsedValue.getTimeResultList().get(index).getValue());
                    }
                    if (countBeforeUsedValue == null) {
                        countBeforeUsedValue = BigDecimal.ONE;
                    } else {
                        countBeforeUsedValue = countBeforeUsedValue.add(BigDecimal.ONE);
                    }
                    if (maxBeforeUsedValue == null) {
                        maxBeforeUsedValue = beforeUsedValue.getTimeResultList().get(index).getValue();
                    } else if (beforeUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(maxBeforeUsedValue) >= 0) {
                        maxBeforeUsedValue = beforeUsedValue.getTimeResultList().get(index).getValue();
                    }
                    if (minBeforeUsedValue == null) {
                        minBeforeUsedValue = beforeUsedValue.getTimeResultList().get(index).getValue();
                    } else if (beforeUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(minBeforeUsedValue) < 0) {
                        minBeforeUsedValue = beforeUsedValue.getTimeResultList().get(index).getValue();
                    }
                }

                //制御後使用量
                afterUsedValue.getTimeResultList().get(index).setValue(monthSummary.getAfterControlUsedValue());
                if (afterUsedValue.getTimeResultList().get(index).getValue() != null) {
                    if (summaryAfterUsedValue == null) {
                        summaryAfterUsedValue = afterUsedValue.getTimeResultList().get(index).getValue();
                    } else {
                        summaryAfterUsedValue = summaryAfterUsedValue
                                .add(afterUsedValue.getTimeResultList().get(index).getValue());
                    }
                    if (countAfterUsedValue == null) {
                        countAfterUsedValue = BigDecimal.ONE;
                    } else {
                        countAfterUsedValue = countAfterUsedValue.add(BigDecimal.ONE);
                    }
                    if (maxAfterUsedValue == null) {
                        maxAfterUsedValue = afterUsedValue.getTimeResultList().get(index).getValue();
                    } else if (afterUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(maxAfterUsedValue) >= 0) {
                        maxAfterUsedValue = afterUsedValue.getTimeResultList().get(index).getValue();
                    }
                    if (minAfterUsedValue == null) {
                        minAfterUsedValue = afterUsedValue.getTimeResultList().get(index).getValue();
                    } else if (afterUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(minAfterUsedValue) < 0) {
                        minAfterUsedValue = afterUsedValue.getTimeResultList().get(index).getValue();
                    }
                }

                //削減量（補正後削減量）
                reductionValue.getTimeResultList().get(index).setValue(monthSummary.getCorrectionReductionValue());
                if (reductionValue.getTimeResultList().get(index).getValue() != null) {
                    if (summaryReductionValue == null) {
                        summaryReductionValue = reductionValue.getTimeResultList().get(index).getValue();
                    } else {
                        summaryReductionValue = summaryReductionValue
                                .add(reductionValue.getTimeResultList().get(index).getValue());
                    }
                    if (countReductionValue == null) {
                        countReductionValue = BigDecimal.ONE;
                    } else {
                        countReductionValue = countReductionValue.add(BigDecimal.ONE);
                    }
                    if (maxReductionValue == null) {
                        maxReductionValue = reductionValue.getTimeResultList().get(index).getValue();
                    } else if (reductionValue.getTimeResultList().get(index).getValue()
                            .compareTo(maxReductionValue) >= 0) {
                        maxReductionValue = reductionValue.getTimeResultList().get(index).getValue();
                    }
                    if (minReductionValue == null) {
                        minReductionValue = reductionValue.getTimeResultList().get(index).getValue();
                    } else if (reductionValue.getTimeResultList().get(index).getValue()
                            .compareTo(minReductionValue) < 0) {
                        minReductionValue = reductionValue.getTimeResultList().get(index).getValue();
                    }
                }

                //制御回数
                controlCount.getTimeResultList().get(index).setValue(monthSummary.getControlCount());
                if (controlCount.getTimeResultList().get(index).getValue() != null) {
                    if (summaryControlCount == null) {
                        summaryControlCount = controlCount.getTimeResultList().get(index).getValue();
                    } else {
                        summaryControlCount = summaryControlCount
                                .add(controlCount.getTimeResultList().get(index).getValue());
                    }
                    if (countControlCount == null) {
                        countControlCount = BigDecimal.ONE;
                    } else {
                        countControlCount = countControlCount.add(BigDecimal.ONE);
                    }
                    if (maxControlCount == null) {
                        maxControlCount = controlCount.getTimeResultList().get(index).getValue();
                    } else if (controlCount.getTimeResultList().get(index).getValue().compareTo(maxControlCount) >= 0) {
                        maxControlCount = controlCount.getTimeResultList().get(index).getValue();
                    }
                    if (minControlCount == null) {
                        minControlCount = controlCount.getTimeResultList().get(index).getValue();
                    } else if (controlCount.getTimeResultList().get(index).getValue().compareTo(minControlCount) < 0) {
                        minControlCount = controlCount.getTimeResultList().get(index).getValue();
                    }
                }

                //提案使用量
                proposalUsedValue.getTimeResultList().get(index).setValue(monthSummary.getProposalUsedValue());
                if (proposalUsedValue.getTimeResultList().get(index).getValue() != null) {
                    if (summaryProposalUsedValue == null) {
                        summaryProposalUsedValue = proposalUsedValue.getTimeResultList().get(index).getValue();
                    } else {
                        summaryProposalUsedValue = summaryProposalUsedValue
                                .add(proposalUsedValue.getTimeResultList().get(index).getValue());
                    }
                    if (countProposalUsedValue == null) {
                        countProposalUsedValue = BigDecimal.ONE;
                    } else {
                        countProposalUsedValue = countProposalUsedValue.add(BigDecimal.ONE);
                    }
                    if (maxProposalUsedValue == null) {
                        maxProposalUsedValue = proposalUsedValue.getTimeResultList().get(index).getValue();
                    } else if (proposalUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(maxProposalUsedValue) >= 0) {
                        maxProposalUsedValue = proposalUsedValue.getTimeResultList().get(index).getValue();
                    }
                    if (minProposalUsedValue == null) {
                        minProposalUsedValue = proposalUsedValue.getTimeResultList().get(index).getValue();
                    } else if (proposalUsedValue.getTimeResultList().get(index).getValue()
                            .compareTo(minProposalUsedValue) < 0) {
                        minProposalUsedValue = proposalUsedValue.getTimeResultList().get(index).getValue();
                    }
                }

                //判定
                judgeValue.getTimeResultList().get(index).setJudge(monthSummary.getJudge());
                if (judgeValue.getTimeResultList().get(index).getJudge() != null
                        && !judgeValue.getTimeResultList().get(index).getJudge()) {
                    if (overMonthCount == null) {
                        overMonthCount = BigDecimal.ONE;
                    } else {
                        overMonthCount = overMonthCount.add(BigDecimal.ONE);
                    }
                }

                monthSummaryList.add(monthSummary);

            }

        } while (!(currentYear.equals(summaryRange.getYearTo())
                && currentMonth.compareTo(summaryRange.getMonthTo()) == 0));

        //サマリ値を設定する
        //制御前負荷率
        beforeLoadFactorDetail.setSummaryValue(null);
        beforeLoadFactorDetail.setMaxValue(maxBeforeLoadFactorDetail);
        beforeLoadFactorDetail.setMinValue(minBeforeLoadFactorDetail);
        if (summaryBeforeLoadFactorDetail == null || countBeforeLoadFactorDetail == null) {
            beforeLoadFactorDetail.setAverageValue(null);
        } else {
            beforeLoadFactorDetail.setAverageValue(
                    summaryBeforeLoadFactorDetail.divide(countBeforeLoadFactorDetail, 10, BigDecimal.ROUND_HALF_UP));
        }
        // 制御前使用量
        beforeUsedValue.setSummaryValue(summaryBeforeUsedValue);
        beforeUsedValue.setMaxValue(maxBeforeUsedValue);
        beforeUsedValue.setMinValue(minBeforeUsedValue);
        if (summaryBeforeUsedValue == null || countBeforeUsedValue == null) {
            beforeUsedValue.setAverageValue(null);
        } else {
            beforeUsedValue
                    .setAverageValue(summaryBeforeUsedValue.divide(countBeforeUsedValue, 10, BigDecimal.ROUND_HALF_UP));
        }

        // 制御後使用量
        afterUsedValue.setSummaryValue(summaryAfterUsedValue);
        afterUsedValue.setMaxValue(maxAfterUsedValue);
        afterUsedValue.setMinValue(minAfterUsedValue);
        if (summaryAfterUsedValue == null || countAfterUsedValue == null) {
            afterUsedValue.setAverageValue(null);
        } else {
            afterUsedValue
                    .setAverageValue(summaryAfterUsedValue.divide(countAfterUsedValue, 10, BigDecimal.ROUND_HALF_UP));
        }
        //削減量
        reductionValue.setSummaryValue(summaryReductionValue);
        reductionValue.setMaxValue(maxReductionValue);
        reductionValue.setMinValue(minReductionValue);
        if (summaryReductionValue == null || countReductionValue == null) {
            reductionValue.setAverageValue(null);
        } else {
            reductionValue
                    .setAverageValue(summaryReductionValue.divide(countReductionValue, 10, BigDecimal.ROUND_HALF_UP));
        }

        // 制御回数
        controlCount.setSummaryValue(summaryControlCount);
        controlCount.setMaxValue(maxControlCount);
        controlCount.setMinValue(minControlCount);
        if (summaryControlCount == null || countControlCount == null) {
            controlCount.setAverageValue(null);
        } else {
            controlCount.setAverageValue(summaryControlCount.divide(countControlCount, 10, BigDecimal.ROUND_HALF_UP));
        }
        //提案使用量
        proposalUsedValue.setSummaryValue(summaryProposalUsedValue);
        proposalUsedValue.setMaxValue(maxProposalUsedValue);
        proposalUsedValue.setMinValue(minProposalUsedValue);
        if (summaryProposalUsedValue == null || countProposalUsedValue == null) {
            proposalUsedValue.setAverageValue(null);
        } else {
            proposalUsedValue.setAverageValue(
                    summaryProposalUsedValue.divide(countProposalUsedValue, 10, BigDecimal.ROUND_HALF_UP));
        }
        //判定
        if (summaryAfterUsedValue == null || summaryProposalUsedValue == null) {
            judgeValue.setSummaryJudge(null);
        } else {
            judgeValue.setSummaryJudge(summaryAfterUsedValue.compareTo(summaryProposalUsedValue) <= 0);
        }

        judgeValue.setMaxValue(null);
        judgeValue.setMinValue(null);
        judgeValue.setAverageValue(null);

        //ヘッダ部を設定する
        //削減量
        header.setReductionValue(summaryReductionValue);
        //削減従量料金
        if (header.getBasicRateUnitPrice() == null || header.getReductionValue() == null) {
            header.setReductionPay(null);
        } else {
            header.setReductionPay(
                    header.getBasicRateUnitPrice().multiply(header.getReductionValue()).setScale(10,
                            RoundingMode.DOWN));
        }
        //制御回数
        header.setControlCount(summaryControlCount);
        //実績使用量
        header.setUsedValue(summaryAfterUsedValue);
        //実績従量料金
        if (header.getBasicRateUnitPrice() == null || header.getUsedValue() == null) {
            header.setUsedPay(null);
        } else {
            header.setUsedPay(header.getBasicRateUnitPrice().multiply(header.getUsedValue()).setScale(10,
                    RoundingMode.DOWN));
        }
        //予測使用量
        header.setPredictionValue(summaryBeforeUsedValue);
        //予測従量料金
        if (header.getBasicRateUnitPrice() == null || header.getPredictionValue() == null) {
            header.setPredictionPay(null);
        } else {
            header.setPredictionPay(
                    header.getBasicRateUnitPrice().multiply(header.getPredictionValue()).setScale(10,
                            RoundingMode.DOWN));
        }
        //合計使用量判定
        header.setSummaryUsedValueJudge(judgeValue.getSummaryJudge());
        //提案使用量超過月
        header.setProposalUsedOverCount(overMonthCount);
        //提案使用量合計
        header.setSummaryProposalUsedValue(summaryProposalUsedValue);
        //実績使用量合計
        header.setSummaryUsedValue(summaryAfterUsedValue);

        //結果値を小数点以下第2位を切り捨て
        //制御前負荷率
        if (beforeLoadFactorDetail.getMaxValue() != null) {
            beforeLoadFactorDetail
                    .setMaxValue(beforeLoadFactorDetail.getMaxValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (beforeLoadFactorDetail.getMinValue() != null) {
            beforeLoadFactorDetail
                    .setMinValue(beforeLoadFactorDetail.getMinValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (beforeLoadFactorDetail.getAverageValue() != null) {
            beforeLoadFactorDetail
                    .setAverageValue(beforeLoadFactorDetail.getAverageValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        for (KensyoEventYearTimeDetailResultData timeDetail : beforeLoadFactorDetail.getTimeResultList()) {
            if (timeDetail.getValue() != null) {
                timeDetail.setValue(timeDetail.getValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        //制御前使用量
        if (beforeUsedValue.getSummaryValue() != null) {
            beforeUsedValue.setSummaryValue(beforeUsedValue.getSummaryValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (beforeUsedValue.getMaxValue() != null) {
            beforeUsedValue.setMaxValue(beforeUsedValue.getMaxValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (beforeUsedValue.getMaxValue() != null) {
            beforeUsedValue.setMinValue(beforeUsedValue.getMinValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (beforeUsedValue.getAverageValue() != null) {
            beforeUsedValue.setAverageValue(beforeUsedValue.getAverageValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        for (KensyoEventYearTimeDetailResultData timeDetail : beforeUsedValue.getTimeResultList()) {
            if (timeDetail.getValue() != null) {
                timeDetail.setValue(timeDetail.getValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        //制御後使用量
        if (afterUsedValue.getSummaryValue() != null) {
            afterUsedValue.setSummaryValue(afterUsedValue.getSummaryValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (afterUsedValue.getMaxValue() != null) {
            afterUsedValue.setMaxValue(afterUsedValue.getMaxValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (afterUsedValue.getMaxValue() != null) {
            afterUsedValue.setMinValue(afterUsedValue.getMinValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (afterUsedValue.getAverageValue() != null) {
            afterUsedValue.setAverageValue(afterUsedValue.getAverageValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        for (KensyoEventYearTimeDetailResultData timeDetail : afterUsedValue.getTimeResultList()) {
            if (timeDetail.getValue() != null) {
                timeDetail.setValue(timeDetail.getValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        //削減量
        if (reductionValue.getSummaryValue() != null) {
            reductionValue.setSummaryValue(reductionValue.getSummaryValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (reductionValue.getMaxValue() != null) {
            reductionValue.setMaxValue(reductionValue.getMaxValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (reductionValue.getMaxValue() != null) {
            reductionValue.setMinValue(reductionValue.getMinValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        if (reductionValue.getAverageValue() != null) {
            reductionValue.setAverageValue(reductionValue.getAverageValue().setScale(1, BigDecimal.ROUND_DOWN));
        }
        for (KensyoEventYearTimeDetailResultData timeDetail : reductionValue.getTimeResultList()) {
            if (timeDetail.getValue() != null) {
                timeDetail.setValue(timeDetail.getValue().setScale(1, BigDecimal.ROUND_DOWN));
            }
        }

        //結果を順に詰めていく
        result.setHeader(header);
        result.setBeforeLoadFactorDetail(beforeLoadFactorDetail);
        result.setBeforeUsedValue(beforeUsedValue);
        result.setAfterUsedValue(afterUsedValue);
        result.setReductionValue(reductionValue);
        result.setControlCount(controlCount);
        result.setProposalUsedValue(proposalUsedValue);
        result.setJudgeValue(judgeValue);

        return result;
    }

    /**
     * 指定範囲内の温湿度制御ログを取得する
     *
     * @param smId
     * @param recordYmdHmsFrom
     * @param recordYmdHmsTo
     * @param lastResultGetFlg
     * @return
     */
    private List<TempHumidControlLogVerifyResult> getTempHumidControlLogList(Long smId, String recordYmdHmsFrom,
            String recordYmdHmsTo, Boolean lastResultGetFlg) {
        List<TempHumidControlLogVerifyResult> resultList;
        //範囲内の温湿度制御ログ情報を取得する。
        TempHumidControlLogVerifyResult param = new TempHumidControlLogVerifyResult();
        param.setSmId(smId);
        param.setRecordYmdhmsMin(recordYmdHmsFrom);
        param.setRecordYmdhmsMax(recordYmdHmsTo);
        resultList = getResultList(tempHumidControlLogServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            resultList = new ArrayList<>();
        }

        //フラグが立っている場合のみ1つ前のデータを取得する（前日または前時限からのまたぎの場合、制御回数をカウントしてはいけないため）
        if (lastResultGetFlg) {
            if (resultList.isEmpty() || !resultList.get(0).getRecordYmdhms().equals(recordYmdHmsFrom)) {
                param = new TempHumidControlLogVerifyResult();
                param.setSmId(smId);
                param.setRecordYmdhmsTo(recordYmdHmsFrom);
                List<TempHumidControlLogVerifyResult> tempResultList = getResultList(tempHumidControlLogServiceDaoImpl,
                        param);
                if (tempResultList == null || tempResultList.isEmpty()) {
                    return resultList;
                } else {
                    //先頭のデータのみresultListの先頭に挿入する
                    resultList.add(0, tempResultList.get(0));
                }
            }
        }

        return resultList;
    }

    /**
     * 指定範囲内の指定制御負荷のイベント制御ログを取得する
     *
     * @param smId
     * @param recordYmdHmsFrom
     * @param recordYmdHmsTo
     * @param controlLoad
     * @param lastResultGetFlg
     * @return
     */
    public final List<EventControlLogResult> getEventControlLogList(Long smId, String recordYmdHmsFrom,
            String recordYmdHmsTo, BigDecimal controlLoad, Boolean lastResultGetFlg) {
        List<EventControlLogResult> resultList = new ArrayList<>();

        //範囲内のイベント制御ログ情報を取得する。
        EventControlLogResult param = new EventControlLogResult();
        param.setSmId(smId);
        param.setControlLoad(controlLoad);
        param.setRecordYmdhmsMin(recordYmdHmsFrom);
        param.setRecordYmdhmsMax(recordYmdHmsTo);
        param.setOrderDesc(0);
        resultList = getResultList(eventControlLogServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            resultList = new ArrayList<>();
        }

        //フラグが立っている場合のみ1つ前のデータを取得する（前日または前時限からのまたぎの場合、制御回数をカウントしてはいけないため）
        if (lastResultGetFlg) {
            if (resultList.isEmpty()
                    || !resultList.get(0).getRecordYmdhms().equals(recordYmdHmsFrom)) {
                param = new EventControlLogResult();
                param.setSmId(smId);
                param.setControlLoad(controlLoad);
                param.setRecordYmdhmsTo(recordYmdHmsFrom);
                param.setOrderDesc(1);
                List<EventControlLogResult> tempResultList = getResultList(eventControlLogServiceDaoImpl, param);
                if (tempResultList == null || tempResultList.isEmpty()) {
                } else {
                    //先頭のデータのみresultListの先頭に挿入する
                    resultList.add(0, tempResultList.get(0));
                }
            }
        }

        return resultList;
    }

}
