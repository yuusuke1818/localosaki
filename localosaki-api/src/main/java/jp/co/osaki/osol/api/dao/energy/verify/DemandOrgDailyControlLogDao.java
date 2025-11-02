package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgDailyControlLogParameter;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TLoadControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TempHumidControlLogVerifyResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogControlDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogKindResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogTimesResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyKensyoDataTimeDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.EventControlLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayCalLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleSetLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleTimeLogListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.TLoadControlLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.TempHumidControlLogVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MProductSpecServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.ProductControlLoadFlgSeparateUtility;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況・日報・制御履歴　明細 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class DemandOrgDailyControlLogDao extends OsolApiDao<DemandOrgDailyControlLogParameter> {

    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    private final TLoadControlLogServiceDaoImpl tLoadControlLogServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;
    private final SmControlLoadListServiceDaoImpl smControlLoadListServiceDaoImpl;
    private final TempHumidControlLogVerifyServiceDaoImpl tempHumidControlLogServiceDaoImpl;
    private final EventControlLogServiceDaoImpl eventControlLogServiceDaoImpl;
    private final SmControlScheduleLogListServiceDaoImpl smControlScheduleLogListServiceDaoImpl;
    private final SmControlScheduleSetLogListServiceDaoImpl smControlScheduleSetLogListServiceDaoImpl;
    private final SmControlScheduleTimeLogListServiceDaoImpl smControlScheduleTimeLogListServiceDaoImpl;
    private final SmControlHolidayLogListServiceDaoImpl smControlHolidayLogListServiceDaoImpl;
    private final SmControlHolidayCalLogListServiceDaoImpl smControlHolidayCalLogListServiceDaoImpl;

    /**
     * 制御種別：デマンド
     */
    private final static String KIND_DEMAND = "ﾃﾞﾏﾝﾄﾞ";

    /**
     * 制御種別：スケジュール
     */
    private final static String KIND_SCHEDULE = "ｽｹｼﾞｭｰﾙ";

    /**
     * 制御種別：温度
     */
    private final static String KIND_TEMP = "温度";

    /**
     * 制御種別：イベント
     */
    private final static String KIND_EVENT = "ｲﾍﾞﾝﾄ";

    public DemandOrgDailyControlLogDao() {
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        mProductSpecServiceDaoImpl = new MProductSpecServiceDaoImpl();
        tLoadControlLogServiceDaoImpl = new TLoadControlLogServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
        smControlLoadListServiceDaoImpl = new SmControlLoadListServiceDaoImpl();
        tempHumidControlLogServiceDaoImpl = new TempHumidControlLogVerifyServiceDaoImpl();
        eventControlLogServiceDaoImpl = new EventControlLogServiceDaoImpl();
        smControlScheduleLogListServiceDaoImpl = new SmControlScheduleLogListServiceDaoImpl();
        smControlScheduleSetLogListServiceDaoImpl = new SmControlScheduleSetLogListServiceDaoImpl();
        smControlScheduleTimeLogListServiceDaoImpl = new SmControlScheduleTimeLogListServiceDaoImpl();
        smControlHolidayLogListServiceDaoImpl = new SmControlHolidayLogListServiceDaoImpl();
        smControlHolidayCalLogListServiceDaoImpl = new SmControlHolidayCalLogListServiceDaoImpl();
    }

    @Override
    public DemandOrgDailyControlLogResult query(DemandOrgDailyControlLogParameter parameter) throws Exception {
        List<DemandOrgDailyControlLogDetailResultData> resultList = new ArrayList<>();
        Integer demandControlLoadCount;
        Integer eventControlLoadCount;
        Integer scheduleControlLoadCount;
        String productCd;
        String recordYmdHmFrom;
        String recordYmdHmTo;
        String recordYmdHmsFrom;
        String recordYmdHmsTo;
        Timestamp settingUpdateDateTimeFrom;
        Timestamp settingUpdateDateTimeTo;
        List<TLoadControlLogResult> tLoadControlLogResultSetList;
        List<TempHumidControlLogVerifyResult> tempHumidControlLogResultSetList;
        List<EventControlLogResult> eventControlLogResultSetList = new ArrayList<>();
        List<EventControlLogResult> tempEventControlLogResultSetList;
        LinkedHashSet<CommonControlLoadResult> controlLoadSet;
        LinkedHashSet<CommonControlLoadResult> tempHumidControlLoadSet;
        LinkedHashSet<CommonControlLoadResult> eventControlLoadSet;
        LinkedHashSet<CommonScheduleResult> commonScheduleResultSet = null;
        LinkedHashSet<CommonDemandControlTimeTableResult> controlLoadTimeTableSet = null;
        LinkedHashSet<CommonDemandControlTimeTableResult> scheduleTimeTableSet = null;
        LinkedHashSet<CommonDemandControlTimeTableResult> controlEventTimeTableSet = null;
        List<CommonDemandControlSummaryResult> controlLoadSummaryList = null;
        List<CommonDemandControlSummaryResult> scheduleSummaryList = null;
        List<CommonDemandControlSummaryResult> controlEventSummaryList = null;
        BigDecimal detailSummaryControlCount;
        BigDecimal detailSummaryControlTime;
        Integer beforeControlLoad;
        BigDecimal beforeJigenNo;
        List<Integer[]> cutTimeList;
        String beforeControlStatus;
        ProductControlLoadFlgSeparateListResult productControlLoadData = null;
        boolean afterG2Flg;
        boolean beforeG2Flg;

        //当日を取得する
        Date nowDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(new Date(getServerDateTime().getTime()), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、24時間を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(new BigDecimal("24"));
        }

        //機器情報を取得する
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(parameter.getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smList == null || smList.size() != 1) {
            //機器情報が取得できない場合は、処理を終了する
            return new DemandOrgDailyControlLogResult();
        } else {
            //製品情報を取得する
            MProductSpec productParam = new MProductSpec();
            productParam.setProductCd(smList.get(0).getProductCd());
            MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
            if (productSpec == null) {
                //製品情報が取得できない場合は、処理を終了する
                return new DemandOrgDailyControlLogResult();
            } else {
                demandControlLoadCount = productSpec.getLoadControlOutput();
                eventControlLoadCount = productSpec.getLoadControlOutput();
                scheduleControlLoadCount = productSpec.getLoadControlOutput();
                productCd = productSpec.getProductCd();
            }
        }

        //製品制御負荷情報を取得する
        ProductControlLoadListDetailResultData productControlParam = DemandEmsUtility
                .getProductControlLoadListParam(productCd, null, null);
        List<ProductControlLoadListDetailResultData> productControlLoadList = getResultList(
                productControlLoadListServiceDaoImpl, productControlParam);
        if (productControlLoadList == null || productControlLoadList.isEmpty()) {
            return null;
        }

        // 対象商品がG2以前か否かを取得する
        beforeG2Flg = DemandVerifyUtility.isBeforeG2(smList.get(0).getProductCd());
        // 対象商品がG2以降か否かを取得する
        afterG2Flg = DemandVerifyUtility.isAfterG2(smList.get(0).getProductCd());

        if(!beforeG2Flg) {
            productControlLoadData = ProductControlLoadFlgSeparateUtility.setProductControlLoadFlgSeparateList(productControlLoadList);
            if(productControlLoadData == null) {
                return new DemandOrgDailyControlLogResult();
            }else {
                //controlLoadCountを書き換える
                demandControlLoadCount = productControlLoadData.getDemandControlFlgData().getControlLoadCount();
                eventControlLoadCount = productControlLoadData.getEventControlFlgData().getControlLoadCount();
                scheduleControlLoadCount = productControlLoadData.getScheduleControlFlgData().getControlLoadCount();
            }
        }

        //機器制御負荷情報を取得する
        SmControlLoadListDetailResultData smControlLoadParam = DemandEmsUtility.getSmControlLoadListParam(
                parameter.getSmId(), null, null);
        List<SmControlLoadListDetailResultData> smControlLoadList = getResultList(smControlLoadListServiceDaoImpl,
                smControlLoadParam);

        //集計範囲を取得する
        SummaryRangeForDayResult summaryRange = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getTimesOfDay(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //負荷制御ログ上の開始、終了を設定する
        recordYmdHmFrom = DemandVerifyUtility.getRecordYmdHmFrom(summaryRange.getMeasurementDateFrom(),
                summaryRange.getJigenNoFrom());
        recordYmdHmTo = DemandVerifyUtility.getRecordYmdHmTo(summaryRange.getMeasurementDateTo(),
                summaryRange.getJigenNoTo());
        //イベントログ、温湿度制御ログ上の開始、終了を設定する
        recordYmdHmsFrom = DemandVerifyUtility.getRecordYmdHmsFromForDaily(summaryRange.getMeasurementDateFrom(),
                summaryRange.getJigenNoFrom());
        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForDaily(summaryRange.getMeasurementDateTo(),
                summaryRange.getJigenNoTo());
        //スケジュール上の開始、終了を設定する
        settingUpdateDateTimeFrom = DemandVerifyUtility
                .getSettingUpdateDateTimeFrom(summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom());
        settingUpdateDateTimeTo = DemandVerifyUtility.getSettingUpdateDateTimeTo(summaryRange.getMeasurementDateTo(),
                summaryRange.getJigenNoTo());

        // ヘッダー
        List<DemandOrgDailyKensyoDataTimeDetailResultData> kensyoDetailHeaderList = DemandDataUtility
                .createDailyKensyoDetailHeader(summaryRange);

        // 制御履歴で全て取得（null）または、イベント制御履歴（"1"）のEα, Eα２以外の場合
        if (parameter.getControlKind() == null
                || !(ApiCodeValueConstants.CONTROL_KIND.EVENT.getVal().equals(parameter.getControlKind()) && !beforeG2Flg)) {

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

            // 制御履歴で全て取得（null）または、負荷制御履歴（"0"）の場合
            if (parameter.getControlKind() == null
                    || ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal().equals(parameter.getControlKind())) {

                //負荷制御ログを取得する
                if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                    //Fromが未来（当日以降）の場合は取得しない
                    tLoadControlLogResultSetList = new ArrayList<>();
                } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                    //Toが未来日（当日以降）の場合は、前日の24:00:00までのデータを取得
                    recordYmdHmTo = DemandVerifyUtility.getRecordYmdHmTo(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48));
                    tLoadControlLogResultSetList = getLoadControlLogList(parameter.getSmId(), recordYmdHmFrom, recordYmdHmTo);
                } else {
                    //上記以外は指定範囲で取得
                    tLoadControlLogResultSetList = getLoadControlLogList(parameter.getSmId(), recordYmdHmFrom, recordYmdHmTo);
                }

                if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tLoadControlLogResultSetList != null
                        && !tLoadControlLogResultSetList.isEmpty()) {
                    //Toが未来日（当日以降）の場合、最終レコードの精査
                    tLoadControlLogResultSetList = DemandVerifyUtility.addLoadControlLogRecord(tLoadControlLogResultSetList,
                            DateUtility.conversionDate(DemandVerifyUtility.getRecordYmdHmTo(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48)),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
                }

                //制御状態の桁数チェック
                if (!DemandVerifyUtility.checkLoadControlList(tLoadControlLogResultSetList, demandControlLoadCount)) {
                    //桁数が不正の場合、処理を終了する。
                    return new DemandOrgDailyControlLogResult();
                }

                //負荷ごとに振り分ける
                controlLoadSet = DemandVerifyUtility.createControlLoadSet(tLoadControlLogResultSetList, demandControlLoadCount, recordYmdHmFrom);

                //時限Noを付与する
                if(beforeG2Flg) {
                    controlLoadTimeTableSet = DemandVerifyUtility.createDemandControlTimeTable(controlLoadSet);
                } else {
                    controlLoadTimeTableSet = DemandVerifyUtility.createDemandControlTimeTable(controlLoadSet, productControlLoadData.getDemandControlFlgData());
                }
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

            //スケジュール情報に時限Noを付与する
            scheduleTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableResult(commonScheduleResultSet);

            //スケジュールによる制御を除く
            controlLoadTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                    controlLoadTimeTableSet,
                    settingUpdateDateTimeTo, Boolean.FALSE);

            // 制御履歴で全て取得（null）または、負荷制御履歴（"0"）の場合
            if (parameter.getControlKind() == null
                    || ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal().equals(parameter.getControlKind())) {

                //サマリー情報を作成する（制御情報）
                if(beforeG2Flg) {
                    controlLoadSummaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(controlLoadTimeTableSet, null,
                            summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                            summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), demandControlLoadCount);
                } else {
                    controlLoadSummaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(controlLoadTimeTableSet, null,
                            summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                            summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), productControlLoadData.getDemandControlFlgData());
                }
            }

            //サマリー情報を作成する（スケジュール情報）
            if(beforeG2Flg) {
                scheduleSummaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(scheduleTimeTableSet, null,
                        summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), scheduleControlLoadCount);
            } else {
                scheduleSummaryList = DemandVerifyUtility.createDemandControlTimeTableSummary(scheduleTimeTableSet, null,
                        summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), productControlLoadData.getScheduleControlFlgData());
            }
        }


        // 制御履歴で全て取得（null）または、イベント制御履歴（"1"）の場合
        if (parameter.getControlKind() == null
                || ApiCodeValueConstants.CONTROL_KIND.EVENT.getVal().equals(parameter.getControlKind())) {

            //イベント制御ログ、温湿度制御ログを取得する
            if (OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd)) {
                //温湿度制御ログを取得する
                if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                    //Fromが未来日（以降）の場合、取得しない
                    tempHumidControlLogResultSetList = new ArrayList<>();
                } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                    //Toが未来日（当日以降）の場合、前日の24:00:00にして取得
                    recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48));
                    tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                            recordYmdHmsTo);
                } else {
                    //上記以外の場合、指定範囲で取得
                    tempHumidControlLogResultSetList = getTempHumidControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                            recordYmdHmsTo);
                }

                if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempHumidControlLogResultSetList != null
                        && !tempHumidControlLogResultSetList.isEmpty()) {
                    //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                    tempHumidControlLogResultSetList = DemandVerifyUtility.addTempHumidLogRecord(
                            tempHumidControlLogResultSetList,
                            DateUtility.conversionDate(
                                    DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48)),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                }

                //負荷ごとに振り分ける
                tempHumidControlLoadSet = DemandVerifyUtility.createTempHumidControlSet(tempHumidControlLogResultSetList,
                        eventControlLoadCount, recordYmdHmsFrom);

                //時限Noを付与する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(tempHumidControlLoadSet);

                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                        controlEventTimeTableSet,
                        settingUpdateDateTimeTo, Boolean.FALSE);

                //サマリー情報を作成する
                controlEventSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryDaily(
                        controlEventTimeTableSet,
                        summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), eventControlLoadCount);
            } else if(OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)){
                //イベント制御ログを取得する
                for (int i = 1; i <= eventControlLoadCount; i++) {
                    if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                        //Fromが未来日（当日以降）の場合、取得しない
                        tempEventControlLogResultSetList = new ArrayList<>();
                    } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                        //Toが未来日（当日以降）の場合、前日の24:00:00まで取得
                        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48));
                        tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                                recordYmdHmsTo, BigDecimal.valueOf(i));
                    } else {
                        //上記以外の場合、指定範囲内で取得
                        tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                                recordYmdHmsTo, BigDecimal.valueOf(i));
                    }

                    if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
                            && !tempEventControlLogResultSetList.isEmpty()) {
                        //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                        tempEventControlLogResultSetList = DemandVerifyUtility.addEventLogRecord(
                                tempEventControlLogResultSetList,
                                DateUtility.conversionDate(
                                        DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48)),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                productCd, i);
                    }

                    if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                        eventControlLogResultSetList.addAll(tempEventControlLogResultSetList);
                    }
                }

                //負荷ごとに振り分ける
                eventControlLoadSet = DemandVerifyUtility.createEventControlSet(eventControlLogResultSetList,
                        eventControlLoadCount, recordYmdHmsFrom, productCd);

                //時限Noを付与する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(eventControlLoadSet);

                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(commonScheduleResultSet,
                        controlEventTimeTableSet, settingUpdateDateTimeTo, Boolean.FALSE);

                //サマリー情報を作成する
                controlEventSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryDaily(
                        controlEventTimeTableSet,
                        summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(), eventControlLoadCount);
            } else {
                //Eα以降
                //イベント制御ログを取得する
                for(ProductControlLoadListDetailResultData eventFlgData : productControlLoadData.getEventControlFlgData().getProductControlLoadList()) {
                    if (summaryRange.getMeasurementDateFrom().compareTo(nowDate) >= 0) {
                        //Fromが未来日（当日以降）の場合、取得しない
                        tempEventControlLogResultSetList = new ArrayList<>();
                    } else if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0) {
                        //Toが未来日（当日以降）の場合、前日の24:00:00まで取得
                        recordYmdHmsTo = DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48));
                        tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                                recordYmdHmsTo, eventFlgData.getControlLoad());
                    } else {
                        //上記以外の場合、指定範囲内で取得
                        tempEventControlLogResultSetList = getEventControlLogList(parameter.getSmId(), recordYmdHmsFrom,
                                recordYmdHmsTo, eventFlgData.getControlLoad());
                    }

                    if (summaryRange.getMeasurementDateTo().compareTo(nowDate) >= 0 && tempEventControlLogResultSetList != null
                            && !tempEventControlLogResultSetList.isEmpty()) {
                        //Toが未来日（当日以降）で対象のログがある場合最終レコードの精査をする
                        tempEventControlLogResultSetList = DemandVerifyUtility.addEventLogRecord(
                                tempEventControlLogResultSetList, DateUtility.conversionDate(
                                        DemandVerifyUtility.getRecordYmdHmsToForDaily(DateUtility.plusDay(nowDate, -1), BigDecimal.valueOf(48)),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                productCd, eventFlgData.getControlLoad().intValue());
                    }

                    if (tempEventControlLogResultSetList != null && !tempEventControlLogResultSetList.isEmpty()) {
                        eventControlLogResultSetList.addAll(tempEventControlLogResultSetList);
                    }
                }

                //負荷ごとに振り分ける
                eventControlLoadSet = DemandVerifyUtility.createEventControlSet(eventControlLogResultSetList, eventControlLoadCount,
                        recordYmdHmsFrom, productCd, productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //時限Noを付与する
                controlEventTimeTableSet = DemandVerifyUtility.createEventControlTimeTable(eventControlLoadSet,
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());

                //スケジュールによる制御を除く
                controlEventTimeTableSet = DemandVerifyUtility.createScheduleControlTimeTableSet(new LinkedHashSet<>(),
                        controlEventTimeTableSet,
                        settingUpdateDateTimeTo, Boolean.FALSE);

                //サマリー情報を作成する
                controlEventSummaryList = DemandVerifyUtility.createEventControlTimeTableSummaryDaily(
                        controlEventTimeTableSet,
                        summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoFrom(), summaryRange.getJigenNoTo(),
                        productControlLoadData.getEventControlFlgData().getProductControlLoadList());
            }
        }

        //取得した情報に基づいて、制御情報と時間帯情報を作成する。
        //念のため、制御負荷順にソートしておく
        productControlLoadList = productControlLoadList.stream()
                .sorted(Comparator.comparing(ProductControlLoadListDetailResultData::getControlLoad,
                        Comparator.naturalOrder()))
                .collect(Collectors.toList());
        for (ProductControlLoadListDetailResultData productControlLoad : productControlLoadList) {
            DemandOrgDailyControlLogDetailResultData result = new DemandOrgDailyControlLogDetailResultData();
            result.setControlLoad(productControlLoad.getControlLoad().intValue());
            result.setControlLoadCircuit(productControlLoad.getControlLoadCircuit());
            for (SmControlLoadListDetailResultData smControlLoad : smControlLoadList) {
                if (smControlLoad.getControlLoad().compareTo(productControlLoad.getControlLoad()) == 0) {
                    result.setControlLoadName(smControlLoad.getControlLoadName());
                    break;
                }
            }
            List<DemandOrgDailyControlLogControlDetailResultData> detailList = new ArrayList<>();
            //デマンド制御情報の作成
            if(beforeG2Flg || OsolConstants.FLG_ON.equals(productControlLoad.getDemandControlFlg())) {
                // 制御履歴で全て取得（null）または、負荷制御履歴（"0"）の場合
                if (parameter.getControlKind() == null
                        || ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal().equals(parameter.getControlKind())) {
                    DemandOrgDailyControlLogControlDetailResultData demandDetail = new DemandOrgDailyControlLogControlDetailResultData();
                    demandDetail.setControlCount(null);
                    demandDetail.setControlTime(null);
                    demandDetail.setControlKind(KIND_DEMAND);
                    List<DemandOrgDailyControlLogKindResultData> demandKindList = new ArrayList<>();
                    for (DemandOrgDailyKensyoDataTimeDetailResultData kensyoDetailHeader : kensyoDetailHeaderList) {
                        DemandOrgDailyControlLogKindResultData demandKind = new DemandOrgDailyControlLogKindResultData();
                        demandKind.setMeasurementDate(kensyoDetailHeader.getMeasurementDate());
                        demandKind.setJigenNo(kensyoDetailHeader.getJigenNo());
                        demandKind.setTimeTitle(kensyoDetailHeader.getTimeTitle());
                        demandKind.setControlTime(null);
                        demandKind.setMinutes(null);
                        demandKindList.add(demandKind);
                    }
                    demandDetail.setKindList(demandKindList);
                    detailList.add(demandDetail);
                }
            }

            //スケジュール制御情報の作成
            if(beforeG2Flg || OsolConstants.FLG_ON.equals(productControlLoad.getScheduleControlFlg())) {
                // 制御履歴で全て取得（null）の場合
                if (parameter.getControlKind() == null) {
                    DemandOrgDailyControlLogControlDetailResultData scheduleDetail = new DemandOrgDailyControlLogControlDetailResultData();
                    scheduleDetail.setControlCount(null);
                    scheduleDetail.setControlTime(null);
                    scheduleDetail.setControlKind(KIND_SCHEDULE);
                    List<DemandOrgDailyControlLogKindResultData> scheduleKindList = new ArrayList<>();
                    for (DemandOrgDailyKensyoDataTimeDetailResultData kensyoDetailHeader : kensyoDetailHeaderList) {
                        DemandOrgDailyControlLogKindResultData scheduleKind = new DemandOrgDailyControlLogKindResultData();
                        scheduleKind.setMeasurementDate(kensyoDetailHeader.getMeasurementDate());
                        scheduleKind.setJigenNo(kensyoDetailHeader.getJigenNo());
                        scheduleKind.setTimeTitle(kensyoDetailHeader.getTimeTitle());
                        scheduleKind.setControlTime(null);
                        scheduleKind.setMinutes(null);
                        scheduleKindList.add(scheduleKind);
                    }
                    scheduleDetail.setKindList(scheduleKindList);
                    detailList.add(scheduleDetail);
                }
            }

            //温湿度・イベント制御情報の作成
            if(beforeG2Flg || OsolConstants.FLG_ON.equals(productControlLoad.getEventControlFlg())) {
                // 制御履歴で全て取得（null）または、イベント制御履歴（"1"）の場合
                if (parameter.getControlKind() == null
                        || ApiCodeValueConstants.CONTROL_KIND.EVENT.getVal().equals(parameter.getControlKind())) {
                    DemandOrgDailyControlLogControlDetailResultData eventDetail = new DemandOrgDailyControlLogControlDetailResultData();
                    eventDetail.setControlCount(null);
                    eventDetail.setControlTime(null);
                    if(afterG2Flg) {
                        eventDetail.setControlKind(KIND_EVENT);
                    } else {
                        eventDetail.setControlKind(KIND_TEMP);
                    }
                    List<DemandOrgDailyControlLogKindResultData> eventKindList = new ArrayList<>();
                    for (DemandOrgDailyKensyoDataTimeDetailResultData kensyoDetailHeader : kensyoDetailHeaderList) {
                        DemandOrgDailyControlLogKindResultData eventKind = new DemandOrgDailyControlLogKindResultData();
                        eventKind.setMeasurementDate(kensyoDetailHeader.getMeasurementDate());
                        eventKind.setJigenNo(kensyoDetailHeader.getJigenNo());
                        eventKind.setTimeTitle(kensyoDetailHeader.getTimeTitle());
                        eventKind.setControlTime(null);
                        eventKind.setMinutes(null);
                        eventKindList.add(eventKind);
                    }
                    eventDetail.setKindList(eventKindList);
                    detailList.add(eventDetail);
                }
            }

            result.setDetailList(detailList);

            resultList.add(result);
        }


        // 制御履歴で全て取得（null）または、負荷制御履歴（"0"）の場合
        if (parameter.getControlKind() == null
                || ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal().equals(parameter.getControlKind())) {

            //デマンド制御のサマリー情報を設定する
            if (controlLoadSummaryList != null && !controlLoadSummaryList.isEmpty()) {
                //念のため、サマリー情報を制御負荷順にソートする
                controlLoadSummaryList = controlLoadSummaryList.stream().sorted(
                        Comparator.comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder()))
                        .collect(Collectors.toList());

                detailSummaryControlCount = null;
                detailSummaryControlTime = null;
                beforeControlLoad = null;
                for (CommonDemandControlSummaryResult controlLoadSummary : controlLoadSummaryList) {
                    if (beforeControlLoad != null && !beforeControlLoad.equals(controlLoadSummary.getControlLoad())) {
                        //制御負荷が変更になった場合、前の制御負荷のサマリ値をセットする
                        for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                            if (beforeControlLoad.equals(result.getControlLoad())) {
                                for (DemandOrgDailyControlLogControlDetailResultData detailResult : result
                                        .getDetailList()) {
                                    if (KIND_DEMAND.equals(detailResult.getControlKind())) {
                                        detailResult.setControlCount(detailSummaryControlCount);
                                        detailResult.setControlTime(detailSummaryControlTime);
                                    }
                                }
                            }
                        }
                        detailSummaryControlCount = null;
                        detailSummaryControlTime = null;
                    }

                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (controlLoadSummary.getControlLoad().equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (KIND_DEMAND.equals(detailResult.getControlKind())) {
                                    for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                        if (controlLoadSummary.getMeasurementDate().equals(kindResult.getMeasurementDate())
                                                && controlLoadSummary.getJigenNo()
                                                        .compareTo(kindResult.getJigenNo()) == 0) {
                                            if (controlLoadSummary.getControlCount() != null) {
                                                if (detailSummaryControlCount == null) {
                                                    detailSummaryControlCount = controlLoadSummary.getControlCount();
                                                } else {
                                                    detailSummaryControlCount = detailSummaryControlCount
                                                            .add(controlLoadSummary.getControlCount());
                                                }
                                            }
                                            if (controlLoadSummary.getControlTimeSummary() != null) {
                                                if (detailSummaryControlTime == null) {
                                                    detailSummaryControlTime = controlLoadSummary.getControlTimeSummary();
                                                } else {
                                                    detailSummaryControlTime = detailSummaryControlTime
                                                            .add(controlLoadSummary.getControlTimeSummary());
                                                }
                                                kindResult.setControlTime(controlLoadSummary.getControlTimeSummary());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    beforeControlLoad = controlLoadSummary.getControlLoad();
                }

                if (beforeControlLoad != null) {
                    //最終レコードの処理
                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (beforeControlLoad.equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (KIND_DEMAND.equals(detailResult.getControlKind())) {
                                    detailResult.setControlCount(detailSummaryControlCount);
                                    detailResult.setControlTime(detailSummaryControlTime);
                                }
                            }
                        }
                    }
                }
            }

            //デマンド制御のタイムテーブル情報を設定する
            if (controlLoadTimeTableSet != null && !controlLoadTimeTableSet.isEmpty() && controlLoadSummaryList != null
                    && !controlLoadSummaryList.isEmpty()) {
                //念のため、タイムテーブル情報を制御負荷、計測年月日（時分）順にソートする
                List<CommonDemandControlTimeTableResult> controlLoadTimeTableList = new ArrayList<>(
                        controlLoadTimeTableSet);
                controlLoadTimeTableList = controlLoadTimeTableList.stream().sorted(Comparator
                        .comparing(CommonDemandControlTimeTableResult::getControlLoad, Comparator.naturalOrder())
                        .thenComparing(CommonDemandControlTimeTableResult::getMeasurementDate, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
                controlLoadTimeTableSet = new LinkedHashSet<>(controlLoadTimeTableList);
                //念のため、サマリーテーブル情報を制御負荷、計測年月日、時限NO順にソートする
                controlLoadSummaryList = controlLoadSummaryList.stream()
                        .sorted(Comparator
                                .comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder())
                                .thenComparing(CommonDemandControlSummaryResult::getMeasurementDate,
                                        Comparator.naturalOrder())
                                .thenComparing(CommonDemandControlSummaryResult::getJigenNo, Comparator.naturalOrder()))
                        .collect(Collectors.toList());

                beforeControlLoad = null;
                beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                Integer[] cutTime = new Integer[2];

                //サマリテーブルでループする
                for (CommonDemandControlSummaryResult controlSummary : controlLoadSummaryList) {
                    beforeJigenNo = null;
                    cutTimeList = new ArrayList<>();
                    if (beforeControlLoad != null && !beforeControlLoad.equals(controlSummary.getControlLoad())) {
                        beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                    }
                    beforeControlLoad = controlSummary.getControlLoad();
                    if (controlSummary.getControlCount() != null) {
                        //制御回数が設定されている場合（時限内に一度は、制御が発生している場合）、タイムテーブル情報をループして、制御情報を取得する
                        for (CommonDemandControlTimeTableResult controlLoadTimeTable : controlLoadTimeTableSet) {
                            Date timeTableDate = DateUtility
                                    .conversionDate(DateUtility.changeDateFormat(controlLoadTimeTable.getMeasurementDate(),
                                            DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
                            if (!controlLoadTimeTable.getControlLoad().equals(controlSummary.getControlLoad())) {
                                //制御負荷が異なる場合は、次のレコードへ
                                continue;
                            }
                            if (timeTableDate.after(controlSummary.getMeasurementDate()) || (timeTableDate
                                    .equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) > 0)) {
                                //未来の時限になった場合、ループを終了する
                                break;
                            } else if (timeTableDate.before(controlSummary.getMeasurementDate()) || (timeTableDate
                                    .equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) < 0)) {
                                //過去の時限の場合、前のステータスを保持しておく（時限またぎを想定）
                                beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                beforeJigenNo = controlLoadTimeTable.getJigenNo();
                            } else if (timeTableDate.equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) == 0) {
                                //同じ時限の場合
                                if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlStatus)) {
                                    //前が開放の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が遮断の場合、開始時間を取得
                                        cutTime = new Integer[2];
                                        cutTime[0] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getJigenNo());
                                        beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                        beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                    }
                                } else if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatus)) {
                                    //前が遮断の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_FREE
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が開放の場合
                                        if (beforeJigenNo != null
                                                && beforeJigenNo.compareTo(controlLoadTimeTable.getJigenNo()) != 0) {
                                            //前と時限Noが異なる場合、開始時間を取得
                                            cutTime = new Integer[2];
                                            //開始は0
                                            cutTime[0] = 0;
                                        }
                                        //終了時間を取得
                                        if (cutTime[1] == null) {
                                            cutTime[1] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                    controlLoadTimeTable.getMeasurementDate(),
                                                    controlLoadTimeTable.getJigenNo());
                                            if (!cutTime[0].equals(cutTime[1])) {
                                                cutTimeList.add(cutTime);
                                            }
                                        }
                                        beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                        beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                    }

                                } else if (beforeControlStatus == null) {
                                    //1件目の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が遮断の場合、開始時間を取得
                                        cutTime = new Integer[2];
                                        cutTime[0] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getJigenNo());
                                    }
                                    beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                    beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                }
                            }
                        }

                        //最後のレコードの処理
                        if (cutTime[1] == null) {
                            //終了時刻が設定されていない場合、30を設定して処理を終了
                            cutTime[1] = 30;
                            cutTimeList.add(cutTime);
                        }
                    } else if (controlSummary.getControlTimeSummary() != null
                            && controlSummary.getControlTimeSummary().compareTo(BigDecimal.ZERO) != 0) {
                        //制御回数は設定されていないが、制御時間が設定されている場合（前の時限から遮断がまたがっている）
                        cutTime = new Integer[2];
                        //開始は0
                        cutTime[0] = 0;
                        //終了は、時限に設定されている制御時間
                        cutTime[1] = controlSummary.getControlTimeSummary().intValue();
                        cutTimeList.add(cutTime);
                    }

                    //所定の箇所に結果を詰める
                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (result.getControlLoad().equals(controlSummary.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (KIND_DEMAND.equals(detailResult.getControlKind())) {
                                    for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                        if (kindResult.getMeasurementDate().equals(controlSummary.getMeasurementDate())
                                                && kindResult.getJigenNo().compareTo(controlSummary.getJigenNo()) == 0) {
                                            if (!cutTimeList.isEmpty()) {
                                                DemandOrgDailyControlLogTimesResultData timeResult = new DemandOrgDailyControlLogTimesResultData();
                                                timeResult.setTimesArray(cutTimeList.toArray(new Integer[0][]));
                                                kindResult.setMinutes(timeResult);
                                                kindResult.setControlTime(controlSummary.getControlTimeSummary());
                                            } else {
                                                kindResult.setMinutes(new DemandOrgDailyControlLogTimesResultData());
                                                kindResult.setControlTime(controlSummary.getControlTimeSummary());
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


        // 制御履歴で全て取得（null）の場合
        if (parameter.getControlKind() == null) {

            //スケジュール制御のサマリー情報を設定する
            if (scheduleSummaryList != null && !scheduleSummaryList.isEmpty()) {
                //念のため、サマリー情報を制御負荷順にソートする
                scheduleSummaryList = scheduleSummaryList.stream().sorted(
                        Comparator.comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder()))
                        .collect(Collectors.toList());

                detailSummaryControlCount = null;
                detailSummaryControlTime = null;
                beforeControlLoad = null;
                for (CommonDemandControlSummaryResult scheduleSummary : scheduleSummaryList) {
                    if (beforeControlLoad != null && !beforeControlLoad.equals(scheduleSummary.getControlLoad())) {
                        //制御負荷が変更になった場合、前の制御負荷のサマリ値をセットする
                        for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                            if (beforeControlLoad.equals(result.getControlLoad())) {
                                for (DemandOrgDailyControlLogControlDetailResultData detailResult : result
                                        .getDetailList()) {
                                    if (KIND_SCHEDULE.equals(detailResult.getControlKind())) {
                                        detailResult.setControlCount(detailSummaryControlCount);
                                        detailResult.setControlTime(detailSummaryControlTime);
                                    }
                                }
                            }
                        }
                        detailSummaryControlCount = null;
                        detailSummaryControlTime = null;
                    }

                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (scheduleSummary.getControlLoad().equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (KIND_SCHEDULE.equals(detailResult.getControlKind())) {
                                    for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                        if (scheduleSummary.getMeasurementDate().equals(kindResult.getMeasurementDate())
                                                && scheduleSummary.getJigenNo()
                                                        .compareTo(kindResult.getJigenNo()) == 0) {
                                            if (scheduleSummary.getControlCount() != null) {
                                                if (detailSummaryControlCount == null) {
                                                    detailSummaryControlCount = scheduleSummary.getControlCount();
                                                } else {
                                                    detailSummaryControlCount = detailSummaryControlCount
                                                            .add(scheduleSummary.getControlCount());
                                                }
                                            }
                                            if (scheduleSummary.getControlTimeSummary() != null) {
                                                if (detailSummaryControlTime == null) {
                                                    detailSummaryControlTime = scheduleSummary.getControlTimeSummary();
                                                } else {
                                                    detailSummaryControlTime = detailSummaryControlTime
                                                            .add(scheduleSummary.getControlTimeSummary());
                                                }
                                                kindResult.setControlTime(scheduleSummary.getControlTimeSummary());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    beforeControlLoad = scheduleSummary.getControlLoad();
                }

                if (beforeControlLoad != null) {
                    //最終レコードの処理
                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (beforeControlLoad.equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (KIND_SCHEDULE.equals(detailResult.getControlKind())) {
                                    detailResult.setControlCount(detailSummaryControlCount);
                                    detailResult.setControlTime(detailSummaryControlTime);
                                }
                            }
                        }
                    }
                }
            }
        }


        // 制御履歴で全て取得（null）または、イベント制御履歴（"1"）の場合
        if (parameter.getControlKind() == null
                || ApiCodeValueConstants.CONTROL_KIND.EVENT.getVal().equals(parameter.getControlKind())) {

            //イベント制御・温湿度制御のサマリー情報を設定する
            if (controlEventSummaryList != null && !controlEventSummaryList.isEmpty()) {
                //念のため、サマリー情報を制御負荷順にソートする
                controlEventSummaryList = controlEventSummaryList.stream().sorted(
                        Comparator.comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder()))
                        .collect(Collectors.toList());

                detailSummaryControlCount = null;
                detailSummaryControlTime = null;
                beforeControlLoad = null;
                for (CommonDemandControlSummaryResult controlEventSummary : controlEventSummaryList) {
                    if (beforeControlLoad != null && !beforeControlLoad.equals(controlEventSummary.getControlLoad())) {
                        //制御負荷が変更になった場合、前の制御負荷のサマリ値をセットする
                        for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                            if (beforeControlLoad.equals(result.getControlLoad())) {
                                for (DemandOrgDailyControlLogControlDetailResultData detailResult : result
                                        .getDetailList()) {
                                    if(afterG2Flg) {
                                        if (KIND_EVENT.equals(detailResult.getControlKind())) {
                                            detailResult.setControlCount(detailSummaryControlCount);
                                            detailResult.setControlTime(detailSummaryControlTime);
                                        }
                                    } else {
                                        if (KIND_TEMP.equals(detailResult.getControlKind())) {
                                            detailResult.setControlCount(detailSummaryControlCount);
                                            detailResult.setControlTime(detailSummaryControlTime);
                                        }
                                    }
                                }
                            }
                        }
                        detailSummaryControlCount = null;
                        detailSummaryControlTime = null;
                    }

                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (controlEventSummary.getControlLoad().equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if(afterG2Flg) {
                                    if (KIND_EVENT.equals(detailResult.getControlKind())) {
                                        for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                            if (controlEventSummary.getMeasurementDate().equals(kindResult.getMeasurementDate())
                                                    && controlEventSummary.getJigenNo()
                                                            .compareTo(kindResult.getJigenNo()) == 0) {
                                                if (controlEventSummary.getControlCount() != null) {
                                                    if (detailSummaryControlCount == null) {
                                                        detailSummaryControlCount = controlEventSummary.getControlCount();
                                                    } else {
                                                        detailSummaryControlCount = detailSummaryControlCount
                                                                .add(controlEventSummary.getControlCount());
                                                    }
                                                }
                                                if (controlEventSummary.getControlTimeSummary() != null) {
                                                    if (detailSummaryControlTime == null) {
                                                        detailSummaryControlTime = controlEventSummary.getControlTimeSummary();
                                                    } else {
                                                        detailSummaryControlTime = detailSummaryControlTime
                                                                .add(controlEventSummary.getControlTimeSummary());
                                                    }
                                                    kindResult.setControlTime(controlEventSummary.getControlTimeSummary());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (KIND_TEMP.equals(detailResult.getControlKind())) {
                                        for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                            if (controlEventSummary.getMeasurementDate().equals(kindResult.getMeasurementDate())
                                                    && controlEventSummary.getJigenNo()
                                                            .compareTo(kindResult.getJigenNo()) == 0) {
                                                if (controlEventSummary.getControlCount() != null) {
                                                    if (detailSummaryControlCount == null) {
                                                        detailSummaryControlCount = controlEventSummary.getControlCount();
                                                    } else {
                                                        detailSummaryControlCount = detailSummaryControlCount
                                                                .add(controlEventSummary.getControlCount());
                                                    }
                                                }
                                                if (controlEventSummary.getControlTimeSummary() != null) {
                                                    if (detailSummaryControlTime == null) {
                                                        detailSummaryControlTime = controlEventSummary.getControlTimeSummary();
                                                    } else {
                                                        detailSummaryControlTime = detailSummaryControlTime
                                                                .add(controlEventSummary.getControlTimeSummary());
                                                    }
                                                    kindResult.setControlTime(controlEventSummary.getControlTimeSummary());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    beforeControlLoad = controlEventSummary.getControlLoad();
                }

                if (beforeControlLoad != null) {
                    //最終レコードの処理
                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (beforeControlLoad.equals(result.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if(afterG2Flg) {
                                    if (KIND_EVENT.equals(detailResult.getControlKind())) {
                                        detailResult.setControlCount(detailSummaryControlCount);
                                        detailResult.setControlTime(detailSummaryControlTime);
                                    }
                                } else {
                                    if (KIND_TEMP.equals(detailResult.getControlKind())) {
                                        detailResult.setControlCount(detailSummaryControlCount);
                                        detailResult.setControlTime(detailSummaryControlTime);
                                    }
                                }
                            }
                        }
                    }
                }

                //スケジュール制御のタイムテーブル情報を設定する
                if (scheduleTimeTableSet != null && !scheduleTimeTableSet.isEmpty() && scheduleSummaryList != null
                        && !scheduleSummaryList.isEmpty()) {
                    //念のため、タイムテーブル情報を制御負荷、計測年月日（時分）順にソートする
                    List<CommonDemandControlTimeTableResult> scheduleTimeTableList = new ArrayList<>(
                            scheduleTimeTableSet);
                    scheduleTimeTableList = scheduleTimeTableList.stream().sorted(Comparator
                            .comparing(CommonDemandControlTimeTableResult::getControlLoad, Comparator.naturalOrder())
                            .thenComparing(CommonDemandControlTimeTableResult::getMeasurementDate, Comparator.naturalOrder()))
                            .collect(Collectors.toList());
                    scheduleTimeTableSet = new LinkedHashSet<>(scheduleTimeTableList);
                    //念のため、サマリーテーブル情報を制御負荷、計測年月日、時限NO順にソートする
                    scheduleSummaryList = scheduleSummaryList.stream()
                            .sorted(Comparator
                                    .comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder())
                                    .thenComparing(CommonDemandControlSummaryResult::getMeasurementDate,
                                            Comparator.naturalOrder())
                                    .thenComparing(CommonDemandControlSummaryResult::getJigenNo, Comparator.naturalOrder()))
                            .collect(Collectors.toList());

                    beforeControlLoad = null;
                    beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                    Integer[] cutTime = new Integer[2];

                    //サマリテーブルでループする
                    for (CommonDemandControlSummaryResult scheduleSummay : scheduleSummaryList) {
                        beforeJigenNo = null;
                        cutTimeList = new ArrayList<>();
                        if (beforeControlLoad != null && !beforeControlLoad.equals(scheduleSummay.getControlLoad())) {
                            beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                        }
                        beforeControlLoad = scheduleSummay.getControlLoad();
                        if (scheduleSummay.getControlCount() != null) {
                            //制御回数が設定されている場合（時限内に一度は、制御が発生している場合）、タイムテーブル情報をループして、制御情報を取得する
                            for (CommonDemandControlTimeTableResult scheduleTimeTable : scheduleTimeTableSet) {
                                Date timeTableDate = DateUtility
                                        .conversionDate(DateUtility.changeDateFormat(scheduleTimeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
                                if (!scheduleTimeTable.getControlLoad().equals(scheduleSummay.getControlLoad())) {
                                    //制御負荷が異なる場合は、次のレコードへ
                                    continue;
                                }
                                if (timeTableDate.after(scheduleSummay.getMeasurementDate()) || (timeTableDate
                                        .equals(scheduleSummay.getMeasurementDate())
                                        && scheduleTimeTable.getJigenNo().compareTo(scheduleSummay.getJigenNo()) > 0)) {
                                    //未来の時限になった場合、ループを終了する
                                    break;
                                } else if (timeTableDate.before(scheduleSummay.getMeasurementDate()) || (timeTableDate
                                        .equals(scheduleSummay.getMeasurementDate())
                                        && scheduleTimeTable.getJigenNo().compareTo(scheduleSummay.getJigenNo()) < 0)) {
                                    //過去の時限の場合、前のステータスを保持しておく（時限またぎを想定）
                                    beforeControlStatus = scheduleTimeTable.getControlStatus();
                                    beforeJigenNo = scheduleTimeTable.getJigenNo();
                                } else if (timeTableDate.equals(scheduleSummay.getMeasurementDate())
                                        && scheduleTimeTable.getJigenNo().compareTo(scheduleSummay.getJigenNo()) == 0) {
                                    //同じ時限の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlStatus)) {
                                        //前が開放の場合
                                        if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                                .equals(scheduleTimeTable.getControlStatus())) {
                                            //今が遮断の場合、開始時間を取得
                                            cutTime = new Integer[2];
                                            cutTime[0] = getMinute(scheduleTimeTable.getMeasurementDate(),
                                                    scheduleTimeTable.getMeasurementDate(),
                                                    scheduleTimeTable.getJigenNo());
                                            beforeControlStatus = scheduleTimeTable.getControlStatus();
                                            beforeJigenNo = scheduleTimeTable.getJigenNo();
                                        }
                                    } else if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatus)) {
                                        //前が遮断の場合
                                        if (ApiSimpleConstants.LOAD_CONTROL_FREE
                                                .equals(scheduleTimeTable.getControlStatus())) {
                                            //今が開放の場合
                                            if (beforeJigenNo != null
                                                    && beforeJigenNo.compareTo(scheduleTimeTable.getJigenNo()) != 0) {
                                                //前と時限Noが異なる場合、開始時間を取得
                                                cutTime = new Integer[2];
                                                //開始は0
                                                cutTime[0] = 0;
                                            }
                                            //終了時間を取得
                                            if (cutTime[1] == null) {
                                                cutTime[1] = getMinute(scheduleTimeTable.getMeasurementDate(),
                                                        scheduleTimeTable.getMeasurementDate(),
                                                        scheduleTimeTable.getJigenNo());
                                                if (!cutTime[0].equals(cutTime[1])) {
                                                    cutTimeList.add(cutTime);
                                                }
                                            }
                                            beforeControlStatus = scheduleTimeTable.getControlStatus();
                                            beforeJigenNo = scheduleTimeTable.getJigenNo();
                                        }

                                    } else if (beforeControlStatus == null) {
                                        //1件目の場合
                                        if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                                .equals(scheduleTimeTable.getControlStatus())) {
                                            //今が遮断の場合、開始時間を取得
                                            cutTime = new Integer[2];
                                            cutTime[0] = getMinute(scheduleTimeTable.getMeasurementDate(),
                                                    scheduleTimeTable.getMeasurementDate(),
                                                    scheduleTimeTable.getJigenNo());
                                        }
                                        beforeControlStatus = scheduleTimeTable.getControlStatus();
                                        beforeJigenNo = scheduleTimeTable.getJigenNo();
                                    }
                                }
                            }

                            //最後のレコードの処理
                            if (cutTime[1] == null) {
                                //終了時刻が設定されていない場合、30を設定して処理を終了
                                cutTime[1] = 30;
                                cutTimeList.add(cutTime);
                            }
                        } else if (scheduleSummay.getControlTimeSummary() != null
                                && scheduleSummay.getControlTimeSummary().compareTo(BigDecimal.ZERO) != 0) {
                            //制御回数は設定されていないが、制御時間が設定されている場合（前の時限から遮断がまたがっている）
                            cutTime = new Integer[2];
                            //開始は0
                            cutTime[0] = 0;
                            //終了は、時限に設定されている制御時間
                            cutTime[1] = scheduleSummay.getControlTimeSummary().intValue();
                            cutTimeList.add(cutTime);
                        }

                        //所定の箇所に結果を詰める
                        for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                            if (result.getControlLoad().equals(scheduleSummay.getControlLoad())) {
                                for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                    if (KIND_SCHEDULE.equals(detailResult.getControlKind())) {
                                        for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                            if (kindResult.getMeasurementDate().equals(scheduleSummay.getMeasurementDate())
                                                    && kindResult.getJigenNo().compareTo(scheduleSummay.getJigenNo()) == 0) {
                                                if (!cutTimeList.isEmpty()) {
                                                    DemandOrgDailyControlLogTimesResultData timeResult = new DemandOrgDailyControlLogTimesResultData();
                                                    timeResult.setTimesArray(cutTimeList.toArray(new Integer[0][]));
                                                    kindResult.setMinutes(timeResult);
                                                    kindResult.setControlTime(scheduleSummay.getControlTimeSummary());
                                                } else {
                                                    kindResult.setMinutes(new DemandOrgDailyControlLogTimesResultData());
                                                    kindResult.setControlTime(scheduleSummay.getControlTimeSummary());
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

            //イベント制御・温湿度制御のタイムテーブル情報を設定する
            if (controlEventTimeTableSet != null && !controlEventTimeTableSet.isEmpty() && controlEventSummaryList != null
                    && !controlEventSummaryList.isEmpty()) {
                //念のため、タイムテーブル情報を制御負荷、計測年月日（時分）順にソートする
                List<CommonDemandControlTimeTableResult> controlEventTimeTableList = new ArrayList<>(
                        controlEventTimeTableSet);
                controlEventTimeTableList = controlEventTimeTableList.stream().sorted(Comparator
                        .comparing(CommonDemandControlTimeTableResult::getControlLoad, Comparator.naturalOrder())
                        .thenComparing(CommonDemandControlTimeTableResult::getMeasurementDate, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
                controlEventTimeTableSet = new LinkedHashSet<>(controlEventTimeTableList);

                //念のため、サマリーテーブル情報を制御負荷、計測年月日、時限NO順にソートする
                controlEventSummaryList = controlEventSummaryList.stream()
                        .sorted(Comparator
                                .comparing(CommonDemandControlSummaryResult::getControlLoad, Comparator.naturalOrder())
                                .thenComparing(CommonDemandControlSummaryResult::getMeasurementDate,
                                        Comparator.naturalOrder())
                                .thenComparing(CommonDemandControlSummaryResult::getJigenNo, Comparator.naturalOrder()))
                        .collect(Collectors.toList());

                beforeControlLoad = null;
                beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                Integer[] cutTime = new Integer[2];

                //サマリテーブルでループする
                for (CommonDemandControlSummaryResult controlSummary : controlEventSummaryList) {
                    beforeJigenNo = null;
                    cutTimeList = new ArrayList<>();
                    if (beforeControlLoad != null && !beforeControlLoad.equals(controlSummary.getControlLoad())) {
                        beforeControlStatus = ApiSimpleConstants.LOAD_CONTROL_FREE;
                    }
                    beforeControlLoad = controlSummary.getControlLoad();
                    if (controlSummary.getControlCount() != null) {
                        //制御回数が設定されている場合（時限内に一度は、制御が発生している場合）、タイムテーブル情報をループして、制御情報を取得する
                        for (CommonDemandControlTimeTableResult controlLoadTimeTable : controlEventTimeTableSet) {
                            Date timeTableDate = DateUtility
                                    .conversionDate(DateUtility.changeDateFormat(controlLoadTimeTable.getMeasurementDate(),
                                            DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
                            if (!controlLoadTimeTable.getControlLoad().equals(controlSummary.getControlLoad())) {
                                //制御負荷が異なる場合は、次のレコードへ
                                continue;
                            }
                            if (timeTableDate.after(controlSummary.getMeasurementDate()) || (timeTableDate
                                    .equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) > 0)) {
                                //未来の時限になった場合、ループを終了する
                                break;
                            } else if (timeTableDate.before(controlSummary.getMeasurementDate()) || (timeTableDate
                                    .equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) < 0)) {
                                //過去の時限の場合、前のステータスを保持しておく（時限またぎを想定）
                                beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                beforeJigenNo = controlLoadTimeTable.getJigenNo();
                            } else if (timeTableDate.equals(controlSummary.getMeasurementDate())
                                    && controlLoadTimeTable.getJigenNo().compareTo(controlSummary.getJigenNo()) == 0) {
                                //同じ時限の場合
                                if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlStatus)) {
                                    //前が開放の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が遮断の場合、開始時間を取得
                                        cutTime = new Integer[2];
                                        cutTime[0] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getJigenNo());
                                        beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                        beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                    }
                                } else if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatus)) {
                                    //前が遮断の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_FREE
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が開放の場合
                                        if (beforeJigenNo != null
                                                && beforeJigenNo.compareTo(controlLoadTimeTable.getJigenNo()) != 0) {
                                            //前と時限Noが異なる場合、開始時間を取得
                                            cutTime = new Integer[2];
                                            //開始は0
                                            cutTime[0] = 0;
                                        }
                                        //終了時間を取得
                                        if (cutTime[1] == null) {
                                            cutTime[1] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                    controlLoadTimeTable.getMeasurementDate(),
                                                    controlLoadTimeTable.getJigenNo());
                                            if (!cutTime[0].equals(cutTime[1]) || cutTime[1] != 0
                                                    || !"00".equals(DateUtility
                                                            .changeDateFormat(controlLoadTimeTable.getMeasurementDate(),
                                                                    DateUtility.DATE_FORMAT_HHMMSS)
                                                            .substring(4))) {
                                                if (cutTime[0].equals(cutTime[1])) {
                                                    //開始と終了が同じ場合は1を加算
                                                    cutTime[1] = cutTime[1] + 1;
                                                }
                                                cutTimeList.add(cutTime);
                                            }
                                        }
                                        beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                        beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                    }
                                } else if (beforeControlStatus == null) {
                                    //1件目の場合
                                    if (ApiSimpleConstants.LOAD_CONTROL_CUT
                                            .equals(controlLoadTimeTable.getControlStatus())) {
                                        //今が遮断の場合、開始時間を取得
                                        cutTime = new Integer[2];
                                        cutTime[0] = getMinute(controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getMeasurementDate(),
                                                controlLoadTimeTable.getJigenNo());
                                    }
                                    beforeControlStatus = controlLoadTimeTable.getControlStatus();
                                    beforeJigenNo = controlLoadTimeTable.getJigenNo();
                                }
                            }
                        }

                        //最後のレコードの処理
                        if (cutTime[1] == null) {
                            //終了時刻が設定されていない場合、30を設定して処理を終了
                            cutTime[1] = 30;
                            cutTimeList.add(cutTime);
                        }
                    } else if (controlSummary.getControlTimeSummary() != null
                            && controlSummary.getControlTimeSummary().compareTo(BigDecimal.ZERO) != 0) {
                        //制御回数は設定されていないが、制御時間が設定されている場合（前の時限から遮断がまたがっている）
                        cutTime = new Integer[2];
                        //開始は0
                        cutTime[0] = 0;
                        //終了は、時限に設定されている制御時間
                        cutTime[1] = controlSummary.getControlTimeSummary().intValue();
                        if (cutTime[0].equals(cutTime[1])) {
                            //開始と終了が同じ場合は1を加算
                            cutTime[1] = cutTime[1] + 1;
                        }
                        cutTimeList.add(cutTime);
                    }

                    //所定の箇所に結果を詰める
                    for (DemandOrgDailyControlLogDetailResultData result : resultList) {
                        if (result.getControlLoad().equals(controlSummary.getControlLoad())) {
                            for (DemandOrgDailyControlLogControlDetailResultData detailResult : result.getDetailList()) {
                                if (afterG2Flg) {
                                    if (KIND_EVENT.equals(detailResult.getControlKind())) {
                                        for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                            if (kindResult.getMeasurementDate().equals(controlSummary.getMeasurementDate())
                                                    && kindResult.getJigenNo().compareTo(controlSummary.getJigenNo()) == 0) {
                                                if (!cutTimeList.isEmpty()) {
                                                    DemandOrgDailyControlLogTimesResultData timeResult = new DemandOrgDailyControlLogTimesResultData();
                                                    timeResult.setTimesArray(cutTimeList.toArray(new Integer[0][]));
                                                    kindResult.setMinutes(timeResult);
                                                    kindResult.setControlTime(controlSummary.getControlTimeSummary());
                                                } else {
                                                    kindResult.setMinutes(new DemandOrgDailyControlLogTimesResultData());
                                                    kindResult.setControlTime(controlSummary.getControlTimeSummary());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (KIND_TEMP.equals(detailResult.getControlKind())) {
                                        for (DemandOrgDailyControlLogKindResultData kindResult : detailResult.getKindList()) {
                                            if (kindResult.getMeasurementDate().equals(controlSummary.getMeasurementDate())
                                                    && kindResult.getJigenNo().compareTo(controlSummary.getJigenNo()) == 0) {
                                                if (!cutTimeList.isEmpty()) {
                                                    DemandOrgDailyControlLogTimesResultData timeResult = new DemandOrgDailyControlLogTimesResultData();
                                                    timeResult.setTimesArray(cutTimeList.toArray(new Integer[0][]));
                                                    kindResult.setMinutes(timeResult);
                                                    kindResult.setControlTime(controlSummary.getControlTimeSummary());
                                                } else {
                                                    kindResult.setMinutes(new DemandOrgDailyControlLogTimesResultData());
                                                    kindResult.setControlTime(controlSummary.getControlTimeSummary());
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
        }

        return new DemandOrgDailyControlLogResult(resultList);
    }

    /**
     * 指定時限からの経過時間を算出する。
     *
     * @param measurementDate
     * @param fromMeasurementDate
     * @param jigenNo
     * @return
     */
    private Integer getMinute(Date measurementDate, Date fromMeasurementDate, BigDecimal jigenNo) {

        //時限Noから時間を取得
        String hhmm = DemandDataUtility.changeJigenNoToHHMM(jigenNo, false);
        Date fromDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(fromMeasurementDate, DateUtility.DATE_FORMAT_YYYYMMDD).concat(hhmm),
                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

        Long diff = measurementDate.getTime() - fromDate.getTime();
        return (int) (diff / 1000 / 60);
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
        //ただし、先頭レコードが範囲の開始と同じ場合は処理をしない
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
