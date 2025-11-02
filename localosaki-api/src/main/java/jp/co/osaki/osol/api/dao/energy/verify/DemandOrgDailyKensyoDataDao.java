/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgDailyKensyoDataParameter;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyKensyoDataResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.TLoadControlLogResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
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
import jp.co.osaki.osol.api.utility.energy.setting.ProductControlLoadFlgSeparateUtility;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況・日報・デマンド制御検証 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class DemandOrgDailyKensyoDataDao extends OsolApiDao<DemandOrgDailyKensyoDataParameter> {

    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    private final MSmLineControlLoadVerifyServiceDaoImpl mSmLineControlLoadVerifyServiceDaoImpl;
    private final MSmLineVerifyServiceDaoImpl mSmLineVerifyServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final TLoadControlLogServiceDaoImpl tLoadControlLogServiceDaoImpl;
    private final SmControlScheduleLogListServiceDaoImpl smControlScheduleLogListServiceDaoImpl;
    private final SmControlScheduleSetLogListServiceDaoImpl smControlScheduleSetLogListServiceDaoImpl;
    private final SmControlScheduleTimeLogListServiceDaoImpl smControlScheduleTimeLogListServiceDaoImpl;
    private final SmControlHolidayLogListServiceDaoImpl smControlHolidayLogListServiceDaoImpl;
    private final SmControlHolidayCalLogListServiceDaoImpl smControlHolidayCalLogListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    public DemandOrgDailyKensyoDataDao() {
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        mProductSpecServiceDaoImpl = new MProductSpecServiceDaoImpl();
        mSmLineControlLoadVerifyServiceDaoImpl = new MSmLineControlLoadVerifyServiceDaoImpl();
        mSmLineVerifyServiceDaoImpl = new MSmLineVerifyServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        tLoadControlLogServiceDaoImpl = new TLoadControlLogServiceDaoImpl();
        smControlScheduleLogListServiceDaoImpl = new SmControlScheduleLogListServiceDaoImpl();
        smControlScheduleSetLogListServiceDaoImpl = new SmControlScheduleSetLogListServiceDaoImpl();
        smControlScheduleTimeLogListServiceDaoImpl = new SmControlScheduleTimeLogListServiceDaoImpl();
        smControlHolidayLogListServiceDaoImpl = new SmControlHolidayLogListServiceDaoImpl();
        smControlHolidayCalLogListServiceDaoImpl = new SmControlHolidayCalLogListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
    }

    @Override
    public DemandOrgDailyKensyoDataResult query(DemandOrgDailyKensyoDataParameter parameter) throws Exception {
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
        ProductControlLoadFlgSeparateListResult productControlLoadData = null;
        boolean beforeG2Flg;

        //当日を取得する
        Date nowDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(new Date(getServerDateTime().getTime()), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //系統番号がNULLの場合、ALLを設定
        if (CheckUtility.isNullOrEmpty(parameter.getLineNo())) {
            parameter.setLineNo(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal());
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、24時間を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(new BigDecimal("24"));
        }

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、切り捨てを設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
        }

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
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
            if (productSpec == null) {
                //製品情報が取得できない場合は、処理を終了する
                return new DemandOrgDailyKensyoDataResult();
            } else {
                controlLoadCount = productSpec.getLoadControlOutput();
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
                return new DemandOrgDailyKensyoDataResult();
            }else {
                //controlLoadCountを書き換える
                controlLoadCount = productControlLoadData.getDemandControlFlgData().getControlLoadCount();
            }
        }

        //機器系統制御負荷検証情報を取得する。
        MSmLineControlLoadVerify smLineControlVerifyParam = DemandVerifyUtility.getSmLineControlLoadVerifyParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(),
                parameter.getSmId());
        List<MSmLineControlLoadVerify> smLineControlVerifyList = getResultList(mSmLineControlLoadVerifyServiceDaoImpl,
                smLineControlVerifyParam);

        //機器系統検証情報を取得する。
        MSmLineVerify smLineVerifyParam = DemandVerifyUtility.getSmLineVerifyParam(parameter.getOperationCorpId(),
                parameter.getBuildingId(), parameter.getLineGroupId(), parameter.getLineNo(), parameter.getSmId());
        List<MSmLineVerify> smLineVerifyList = getResultList(mSmLineVerifyServiceDaoImpl, smLineVerifyParam);
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            headerResultSet.setBasicRateUnitPrice(null);
        } else {
            headerResultSet.setBasicRateUnitPrice(smLineVerifyList.get(0).getBasicRateUnitPrice());
        }

        //集計範囲を取得する
        SummaryRangeForDayResult summaryRange = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getTimesOfDay(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

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

        if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
            //Fromが未来日（当日以降）の場合は取得しない
            monthReportList = new ArrayList<>();
        } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
            //Toが未来日（当日以降）の場合は前日にして取得
            monthReportParam.setMeasurementDateTo(DateUtility.plusDay(nowDate, -1));
            monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        } else {
            //上記以外は指定範囲で取得
            monthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        }

        if (monthReportList == null || monthReportList.size() != 1) {
            headerResultSet.setTargetPowerValue(null);
        } else {
            headerResultSet.setTargetPowerValue(monthReportList.get(0).getTargetKw());
        }

        // 時間帯別実績情報を作成する
        timeDetailActualList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);
        timeDetailPredictionList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);
        timeDetailReductionList = DemandDataUtility.createDailyKensyoDetailHeader(summaryRange);

        //デマンド日報系統から実績値を取得する
        CommonDemandDayReportLineListResult dayReportLineParam = DemandEmsUtility.getDayReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(), summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom(),
                summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoTo());

        if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
            //Fromが未来日（当日以降）の場合は取得しない
            dayRepLineList = new ArrayList<>();
        } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
            //Toが未来日（当日以降）の場合は、前日の24:00:00までとしてデータを取得
            dayReportLineParam.setMeasurementDateTo(DateUtility.plusDay(nowDate, -1));
            dayReportLineParam.setJigenNoTo(BigDecimal.valueOf(48));
            dayRepLineList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportLineParam);
        } else {
            //上記以外は指定範囲で取得
            dayRepLineList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportLineParam);
        }

        //取得した実績値を時間帯別実績に詰める
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
        if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
            //Fromが未来日の場合は、取得しない
            tLoadControlLogResultSetList = new ArrayList<>();
        } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
          //Toが未来日（当日以降）の場合は、前日の24:00:00までのデータを取得
            recordYmdHmTo = DemandVerifyUtility.getRecordYmdHmTo(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48));
            tLoadControlLogResultSetList = getLoadControlLogList(parameter.getSmId(), recordYmdHmFrom, recordYmdHmTo);
        } else {
            //上記以外の場合、指定の範囲で取得
            tLoadControlLogResultSetList = getLoadControlLogList(parameter.getSmId(), recordYmdHmFrom, recordYmdHmTo);
        }

        if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tLoadControlLogResultSetList != null
                && !tLoadControlLogResultSetList.isEmpty()) {
            //Toが未来日の場合、最終レコードの精査
            tLoadControlLogResultSetList = DemandVerifyUtility.addLoadControlLogRecord(tLoadControlLogResultSetList,
                    DateUtility.conversionDate(DemandVerifyUtility.getRecordYmdHmTo(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48)),
                            DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
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
            averageActual = summaryActual.divide(countActual, 10, BigDecimal.ROUND_HALF_UP);
        }
        if (countPrediction != null && summaryPrediction != null) {
            averagePrediction = summaryPrediction.divide(countPrediction, 10, BigDecimal.ROUND_HALF_UP);
        }
        if (countReduction != null && summaryReduction != null) {
            averageReduction = summaryReduction.divide(countReduction, 10, BigDecimal.ROUND_HALF_UP);
        }

        //予測値
        for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailPredictionList) {
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
        for (DemandOrgDailyKensyoDataTimeDetailResultData timeDetail : timeDetailReductionList) {
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
