package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoEventMonthParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.energy.verify.KensyoEventMonthResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TempHumidControlLogVerifyResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlySmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlyTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventMonthDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventMonthHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoEventMonthTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
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
 * イベント制御検証・月報
 *
 * @author t_hirata
 */
@Stateless
public class KensyoEventMonthDao extends OsolApiDao<KensyoEventMonthParameter> {

    //TODO EntityServiceDaoクラスを使わないほうがいい
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    private final MSmLineVerifyServiceDaoImpl mSmLineVerifyServiceDaoImpl;
    private final MSmLineControlLoadVerifyServiceDaoImpl mSmLineControlLoadVerifyServiceDaoImpl;
    private final MSmControlLoadVerifyServiceDaoImpl mSmControlLoadVerifyServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final TempHumidControlLogVerifyServiceDaoImpl tempHumidControlLogServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final EventControlLogServiceDaoImpl eventControlLogServiceDaoImpl;
    private final SmControlScheduleLogListServiceDaoImpl smControlScheduleLogListServiceDaoImpl;
    private final SmControlScheduleSetLogListServiceDaoImpl smControlScheduleSetLogListServiceDaoImpl;
    private final SmControlScheduleTimeLogListServiceDaoImpl smControlScheduleTimeLogListServiceDaoImpl;
    private final SmControlHolidayLogListServiceDaoImpl smControlHolidayLogListServiceDaoImpl;
    private final SmControlHolidayCalLogListServiceDaoImpl smControlHolidayCalLogListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    public KensyoEventMonthDao() {
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
        smControlScheduleLogListServiceDaoImpl = new SmControlScheduleLogListServiceDaoImpl();
        smControlScheduleSetLogListServiceDaoImpl = new SmControlScheduleSetLogListServiceDaoImpl();
        smControlScheduleTimeLogListServiceDaoImpl = new SmControlScheduleTimeLogListServiceDaoImpl();
        smControlHolidayLogListServiceDaoImpl = new SmControlHolidayLogListServiceDaoImpl();
        smControlHolidayCalLogListServiceDaoImpl = new SmControlHolidayCalLogListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
    }

    @Override
    public KensyoEventMonthResult query(KensyoEventMonthParameter parameter) throws Exception {
        KensyoEventMonthResult result = new KensyoEventMonthResult();
        KensyoEventMonthHeaderResultData header = new KensyoEventMonthHeaderResultData();
        KensyoEventMonthDetailResultData beforeLoadFactorDetail = new KensyoEventMonthDetailResultData();
        KensyoEventMonthDetailResultData beforeUsedValue = new KensyoEventMonthDetailResultData();
        KensyoEventMonthDetailResultData afterUsedValue = new KensyoEventMonthDetailResultData();
        KensyoEventMonthDetailResultData reductionValue = new KensyoEventMonthDetailResultData();
        KensyoEventMonthDetailResultData controlCount = new KensyoEventMonthDetailResultData();
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
        Integer controlLoadCount;
        String recordYmdHmsFrom;
        String recordYmdHmsTo;
        Timestamp settingUpdateDateTimeFrom;
        Timestamp settingUpdateDateTimeTo;
        String productCd;
        List<CommonDemandMonthReportLineListResult> monthlyReportList;
        List<TempHumidControlLogVerifyResult> tempHumidControlLogResultSetList;
        List<EventControlLogResult> eventControlLogResultSetList = new ArrayList<>();
        List<EventControlLogResult> tempEventControlLogResultSetList;
        LinkedHashSet<CommonControlLoadResult> tempHumidControlLoadSet;
        LinkedHashSet<CommonControlLoadResult> eventControlLoadSet;
        LinkedHashSet<CommonDemandControlTimeTableResult> controlEventTimeTableSet;
        LinkedHashSet<CommonScheduleResult> commonScheduleResultSet;
        List<CommonEventControlMonthlyTimeTableResult> timeTableSummaryList;
        List<CommonEventControlMonthlySmSummaryResult> smSummaryList;
        List<DemandCalendarYearData> corpCalList;
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

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、1（ヶ月）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
        }

        //企業集計年報カレンダを取得する
        CorpDemandSelectResult corpDemandParam = new CorpDemandSelectResult();
        corpDemandParam.setCorpId(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new KensyoEventMonthResult();
        }
        corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());
        if (corpCalList == null || corpCalList.isEmpty()) {
            return new KensyoEventMonthResult();
        }

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smList == null || smList.size() != 1) {
            return new KensyoEventMonthResult();
        }
        header.setSmAddress(smList.get(0).getSmAddress());
        header.setIpAddress(smList.get(0).getIpAddress());
        //製品情報を取得する
        MProductSpec productParam = new MProductSpec();
        productParam.setProductCd(smList.get(0).getProductCd());
        MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
        if (productSpec == null || OsolConstants.FLG_ON.equals(productSpec.getDelFlg())) {
            //製品情報が取得できない場合または削除されている場合は、処理を終了する
            return new KensyoEventMonthResult();
        } else {
            controlLoadCount = productSpec.getLoadControlOutput();
            header.setProductName(productSpec.getProductName());
            productCd = productSpec.getProductCd();
        }

        // 対象商品がG2以前か否かを取得する
        beforeG2Flg = DemandVerifyUtility.isBeforeG2(smList.get(0).getProductCd());

        //製品制御負荷情報を取得する（G2より後の製品のみ）
        if(!beforeG2Flg) {
            ProductControlLoadListDetailResultData productControlLoadParam = DemandVerifyUtility.getProductControlLoadParam(smList.get(0).getProductCd());
            List<ProductControlLoadListDetailResultData> productControlLoadList = getResultList(productControlLoadListServiceDaoImpl, productControlLoadParam);
            productControlLoadData = ProductControlLoadFlgSeparateUtility.setProductControlLoadFlgSeparateList(productControlLoadList);
            if(productControlLoadData == null) {
                return new KensyoEventMonthResult();
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

        //集計範囲を取得する
        SummaryRangeForMonthResult summaryRange = SummaryRangeUtility.getSummaryRangeForMonth(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //イベントログ、温湿度制御ログ上の開始、終了を設定する
        recordYmdHmsFrom = DemandVerifyUtility
                .getRecordYmdHmsFromForMonthlyYearly(summaryRange.getMeasurementDateFrom());
        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(summaryRange.getMeasurementDateTo());

        //スケジュール上の開始、終了を設定する
        settingUpdateDateTimeFrom = DemandVerifyUtility
                .getSettingUpdateDateTimeFrom(summaryRange.getMeasurementDateFrom(), BigDecimal.ONE);
        settingUpdateDateTimeTo = DemandVerifyUtility.getSettingUpdateDateTimeTo(summaryRange.getMeasurementDateTo(),
                new BigDecimal("48"));

        //時間帯別実績リストを作成する
        beforeLoadFactorDetail.setTimeResultList(DemandDataUtility.createEventMonthlyKensyoDetailHeader(summaryRange));
        beforeUsedValue.setTimeResultList(DemandDataUtility.createEventMonthlyKensyoDetailHeader(summaryRange));
        afterUsedValue.setTimeResultList(DemandDataUtility.createEventMonthlyKensyoDetailHeader(summaryRange));
        reductionValue.setTimeResultList(DemandDataUtility.createEventMonthlyKensyoDetailHeader(summaryRange));
        controlCount.setTimeResultList(DemandDataUtility.createEventMonthlyKensyoDetailHeader(summaryRange));

        //デマンド月報系統からデータを取得する
        CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(), summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo());

        if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
            //開始が当日以降の場合、取得しない
            monthlyReportList = new ArrayList<>();
        } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
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

        //イベント制御ログ、温湿度制御ログを取得する
        if (OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd) || OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd)) {

            //温湿度制御ログを取得する

            if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                //Fromが未来（当日以降）の場合は取得しない
                tempHumidControlLogResultSetList = new ArrayList<>();
            } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                //Toが未来（当日以降）の場合、前日までにして取得
                recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                        recordYmdHmsTo);
            } else {
                //上記以外は指定範囲で取得
                tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                        recordYmdHmsTo);
            }

            if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempHumidControlLogResultSetList != null
                    && !tempHumidControlLogResultSetList.isEmpty()) {
                //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                tempHumidControlLogResultSetList = DemandVerifyUtility.addTempHumidLogRecord(
                        tempHumidControlLogResultSetList,
                        DateUtility.conversionDate(DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1)),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
            }

            //負荷ごとに振り分ける
            tempHumidControlLoadSet = DemandVerifyUtility.createTempHumidControlSet(tempHumidControlLogResultSetList,
                    controlLoadCount, recordYmdHmsFrom);

            //分解する
            controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(tempHumidControlLoadSet);

            //スケジュールのstart/Endの状態を設定する
            commonScheduleResultSet = DemandVerifyUtility.createSchedule(scheduleList, scheduleSetList,
                    scheduleTimeList, holidayList, holidayCalList, settingUpdateDateTimeFrom,
                    settingUpdateDateTimeTo);

            //スケジュールによる制御を除く
            controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                    controlEventTimeTableSet, settingUpdateDateTimeTo, Boolean.TRUE);

            //タイムテーブルを作成する
            timeTableSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryMonthlyYearly(
                    controlEventTimeTableSet, summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo(), controlLoadCount, monthlyReportList,
                    smLineControlVerifyList, smLineVerifyList, smControlLoadList, commonScheduleResultSet);

            //機器サマリを作成する
            smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                    controlLoadCount,
                    monthlyReportList,
                    smLineControlVerifyList, smLineVerifyList, smControlLoadList, parameter.getOperationCorpId(),
                    corpCalList, corpDemandList.get(0).getSumDate());

        } else if(OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)) {

            //イベント制御ログを取得する
            for (int i = 1; i <= controlLoadCount; i++) {
                if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                    //Fromが未来（当日以降）の場合は取得しない
                    tempEventControlLogResultSetList = new ArrayList<>();
                } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                    //Toが未来（当日以降）の場合、前日までにして取得
                    recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                    tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                            recordYmdHmsTo, BigDecimal.valueOf(i));
                } else {
                    //上記以外は指定範囲で取得
                    tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                            recordYmdHmsTo, BigDecimal.valueOf(i));
                }

                if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
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
                    controlLoadCount,
                    recordYmdHmsFrom, productCd);

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
                    controlEventTimeTableSet, summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo(), controlLoadCount, monthlyReportList,
                    smLineControlVerifyList, smLineVerifyList, smControlLoadList, commonScheduleResultSet);

            //機器サマリを作成する
            smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                    controlLoadCount,
                    monthlyReportList,
                    smLineControlVerifyList, smLineVerifyList, smControlLoadList, parameter.getOperationCorpId(),
                    corpCalList, corpDemandList.get(0).getSumDate());

        } else {
            // Eα以降の製品
            //イベント制御ログを取得する
            for (ProductControlLoadListDetailResultData eventFlgData : productControlLoadData.getEventControlFlgData().getProductControlLoadList()) {
                if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                    //Fromが未来（当日以降）の場合は取得しない
                    tempEventControlLogResultSetList = new ArrayList<>();
                } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                    //Toが未来（当日以降）の場合、前日までにして取得
                    recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1));
                    tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad());
                } else {
                    //上記以外は指定範囲で取得
                    tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom, recordYmdHmsTo, eventFlgData.getControlLoad());
                }

                if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
                        && !tempEventControlLogResultSetList.isEmpty()) {
                    //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                    tempEventControlLogResultSetList = DemandVerifyUtility.addEventLogRecord(tempEventControlLogResultSetList,
                            DateUtility.conversionDate(DemandVerifyUtility.getRecordYmdHmsToForMonthlyYearly(DateUtility.plusDay(nowDate, -1)), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                            productCd, eventFlgData.getControlLoad().intValue());
                }

                if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                    eventControlLogResultSetList.addAll(tempEventControlLogResultSetList);
                }
            }

            //負荷ごとに振り分ける
            eventControlLoadSet = DemandVerifyUtility.createEventControlSet(eventControlLogResultSetList, controlLoadCount,
                    recordYmdHmsFrom, productCd, productControlLoadData.getEventControlFlgData().getProductControlLoadList());

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
                    controlEventTimeTableSet, summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo(), monthlyReportList,
                    smLineControlVerifyList, smLineVerifyList, smControlLoadList, commonScheduleResultSet,
                    productControlLoadData.getEventControlFlgData().getProductControlLoadList());

            //機器サマリを作成する
            smSummaryList = DemandVerifyUtility.createEventControlSmSummaryMonthly(timeTableSummaryList,
                    monthlyReportList, smLineControlVerifyList, smLineVerifyList, smControlLoadList, parameter.getOperationCorpId(),
                    corpCalList, corpDemandList.get(0).getSumDate(),
                    productControlLoadData.getEventControlFlgData().getProductControlLoadList());
        }

        //機器サマリをループする
        if (smSummaryList != null && !smSummaryList.isEmpty()) {
            for (CommonEventControlMonthlySmSummaryResult smSummary : smSummaryList) {
                //制御前負荷率
                for (KensyoEventMonthTimeDetailResultData beforeLoadFactor : beforeLoadFactorDetail.getTimeResultList()) {
                    if (smSummary.getMeasurementDate().equals(beforeLoadFactor.getMeasurementDate())) {
                        beforeLoadFactor.setValue(smSummary.getBeforeControlLoadFactor());
                        if (beforeLoadFactor.getValue() != null) {
                            if (summaryBeforeLoadFactorDetail == null) {
                                summaryBeforeLoadFactorDetail = beforeLoadFactor.getValue();
                            } else {
                                summaryBeforeLoadFactorDetail = summaryBeforeLoadFactorDetail
                                        .add(beforeLoadFactor.getValue());
                            }
                            if (countBeforeLoadFactorDetail == null) {
                                countBeforeLoadFactorDetail = BigDecimal.ONE;
                            } else {
                                countBeforeLoadFactorDetail = countBeforeLoadFactorDetail.add(BigDecimal.ONE);
                            }
                            if (maxBeforeLoadFactorDetail == null) {
                                maxBeforeLoadFactorDetail = beforeLoadFactor.getValue();
                            } else if (beforeLoadFactor.getValue().compareTo(maxBeforeLoadFactorDetail) >= 0) {
                                maxBeforeLoadFactorDetail = beforeLoadFactor.getValue();
                            }
                            if (minBeforeLoadFactorDetail == null) {
                                minBeforeLoadFactorDetail = beforeLoadFactor.getValue();
                            } else if (beforeLoadFactor.getValue().compareTo(minBeforeLoadFactorDetail) < 0) {
                                minBeforeLoadFactorDetail = beforeLoadFactor.getValue();
                            }
                        }
                        break;
                    }

                }
                //制御前使用量（補正後制御前使用量）
                for (KensyoEventMonthTimeDetailResultData beforeUsed : beforeUsedValue.getTimeResultList()) {
                    if (smSummary.getMeasurementDate().equals(beforeUsed.getMeasurementDate())) {
                        beforeUsed.setValue(smSummary.getCorrectionBeforeControlUsedValue());
                        if (beforeUsed.getValue() != null) {
                            if (summaryBeforeUsedValue == null) {
                                summaryBeforeUsedValue = beforeUsed.getValue();
                            } else {
                                summaryBeforeUsedValue = summaryBeforeUsedValue.add(beforeUsed.getValue());
                            }
                            if (countBeforeUsedValue == null) {
                                countBeforeUsedValue = BigDecimal.ONE;
                            } else {
                                countBeforeUsedValue = countBeforeUsedValue.add(BigDecimal.ONE);
                            }
                            if (maxBeforeUsedValue == null) {
                                maxBeforeUsedValue = beforeUsed.getValue();
                            } else if (beforeUsed.getValue().compareTo(maxBeforeUsedValue) >= 0) {
                                maxBeforeUsedValue = beforeUsed.getValue();
                            }
                            if (minBeforeUsedValue == null) {
                                minBeforeUsedValue = beforeUsed.getValue();
                            } else if (beforeUsed.getValue().compareTo(minBeforeUsedValue) < 0) {
                                minBeforeUsedValue = beforeUsed.getValue();
                            }
                        }
                        break;
                    }
                }
                //制御後使用量
                for (KensyoEventMonthTimeDetailResultData afterUsed : afterUsedValue.getTimeResultList()) {
                    if (smSummary.getMeasurementDate().equals(afterUsed.getMeasurementDate())) {
                        afterUsed.setValue(smSummary.getAfterControlUsedValue());
                        if (afterUsed.getValue() != null) {
                            if (summaryAfterUsedValue == null) {
                                summaryAfterUsedValue = afterUsed.getValue();
                            } else {
                                summaryAfterUsedValue = summaryAfterUsedValue.add(afterUsed.getValue());
                            }
                            if (countAfterUsedValue == null) {
                                countAfterUsedValue = BigDecimal.ONE;
                            } else {
                                countAfterUsedValue = countAfterUsedValue.add(BigDecimal.ONE);
                            }
                            if (maxAfterUsedValue == null) {
                                maxAfterUsedValue = afterUsed.getValue();
                            } else if (afterUsed.getValue().compareTo(maxAfterUsedValue) >= 0) {
                                maxAfterUsedValue = afterUsed.getValue();
                            }
                            if (minAfterUsedValue == null) {
                                minAfterUsedValue = afterUsed.getValue();
                            } else if (afterUsed.getValue().compareTo(minAfterUsedValue) < 0) {
                                minAfterUsedValue = afterUsed.getValue();
                            }
                        }
                        break;
                    }
                }
                //削減量（補正後削減量）
                for (KensyoEventMonthTimeDetailResultData reduction : reductionValue.getTimeResultList()) {
                    if (smSummary.getMeasurementDate().equals(reduction.getMeasurementDate())) {
                        reduction.setValue(smSummary.getCorrectionReductionValue());
                        if (reduction.getValue() != null) {
                            if (summaryReductionValue == null) {
                                summaryReductionValue = reduction.getValue();
                            } else {
                                summaryReductionValue = summaryReductionValue.add(reduction.getValue());
                            }
                            if (countReductionValue == null) {
                                countReductionValue = BigDecimal.ONE;
                            } else {
                                countReductionValue = countReductionValue.add(BigDecimal.ONE);
                            }
                            if (maxReductionValue == null) {
                                maxReductionValue = reduction.getValue();
                            } else if (reduction.getValue().compareTo(maxReductionValue) >= 0) {
                                maxReductionValue = reduction.getValue();
                            }
                            if (minReductionValue == null) {
                                minReductionValue = reduction.getValue();
                            } else if (reduction.getValue().compareTo(minReductionValue) < 0) {
                                minReductionValue = reduction.getValue();
                            }
                        }
                        break;
                    }

                }
                //制御回数
                for (KensyoEventMonthTimeDetailResultData control : controlCount.getTimeResultList()) {
                    if (smSummary.getMeasurementDate().equals(control.getMeasurementDate())) {
                        control.setValue(smSummary.getControlCount());
                        if (control.getValue() != null) {
                            if (summaryControlCount == null) {
                                summaryControlCount = control.getValue();
                            } else {
                                summaryControlCount = summaryControlCount.add(control.getValue());
                            }
                            if (countControlCount == null) {
                                countControlCount = BigDecimal.ONE;
                            } else {
                                countControlCount = countControlCount.add(BigDecimal.ONE);
                            }
                            if (maxControlCount == null) {
                                maxControlCount = control.getValue();
                            } else if (control.getValue().compareTo(maxControlCount) >= 0) {
                                maxControlCount = control.getValue();
                            }
                            if (minControlCount == null) {
                                minControlCount = control.getValue();
                            } else if (control.getValue().compareTo(minControlCount) < 0) {
                                minControlCount = control.getValue();
                            }
                        }
                        break;
                    }
                }
            }
        }

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
        // 削減量
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
        for (KensyoEventMonthTimeDetailResultData timeDetail : beforeLoadFactorDetail.getTimeResultList()) {
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
        for (KensyoEventMonthTimeDetailResultData timeDetail : beforeUsedValue.getTimeResultList()) {
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
        for (KensyoEventMonthTimeDetailResultData timeDetail : afterUsedValue.getTimeResultList()) {
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
        for (KensyoEventMonthTimeDetailResultData timeDetail : reductionValue.getTimeResultList()) {
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

        return result;
    }

    /**
     * 指定範囲内の温湿度制御ログを取得する
     *
     * @param smId
     * @param recordYmdHmsFrom
     * @param recordYmdHmsTo
     * @return
     */
    private List<TempHumidControlLogVerifyResult> getTempHumidControlLogList(Long smId, String recordYmdHmsFrom,
            String recordYmdHmsTo) {
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

        //1つ前のデータを取得する（前日または前時限からのまたぎの場合、制御回数をカウントしてはいけないため）
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

        return resultList;
    }

    /**
     * 指定範囲内のイベント制御ログを取得する
     *
     * @param smId
     * @param recordYmdHmsFrom
     * @param recordYmdHmsTo
     * @param controlLoad
     * @return
     */
    public final List<EventControlLogResult> getEventControlLogList(Long smId, String recordYmdHmsFrom,
            String recordYmdHmsTo, BigDecimal controlLoad) {
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

        //1つ前のデータを取得する（前日または前時限からのまたぎの場合、制御回数をカウントしてはいけないため）
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

        return resultList;
    }

}
