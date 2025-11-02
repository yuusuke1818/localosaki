package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayUsedReportExcelDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayUsedReportExcelDataResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayUsedReportExcelDataLineResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayUsedReportExcelDataPointResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayUsedReportExcelDataSmResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTimeStandardsServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.MLineTimeStandard;
import jp.co.osaki.osol.entity.MLineTimeStandardPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況（個別・日報・系統別使用量） Responseクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class DemandOrgDayUsedReportExcelDataDao extends OsolApiDao<DemandOrgDayUsedReportExcelDataParameter> {

    private static final int JIGEN_NO_CNT = 48;
    private static final String LINE_NAME_AMEDAS = "アメダス（外気温）";
    private static final String LINE_TARGET_AMEDAS = "-";
    private static final String KIND_USED = "使用量";
    private static final String KIND_OVER = "標準外使用量";
    private static final String KIND_AMEDAS = "計測値";
    private static final String UNIT_AMEDAS = "℃";
    private static final String POINT_NO_SRC = "SRC";
    private static final String POINT_NM_SRC = "受電";

    //TODO EntityServiceDaoを使わない
    private final MGraphElementServiceDaoImpl graphElementServiceDaoImpl;
    private final MLineTimeStandardsServiceDaoImpl lineTimeStandardsServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;

    public DemandOrgDayUsedReportExcelDataDao() {
        graphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        lineTimeStandardsServiceDaoImpl = new MLineTimeStandardsServiceDaoImpl();
        buildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
    }

    @Override
    public DemandOrgDayUsedReportExcelDataResult query(DemandOrgDayUsedReportExcelDataParameter parameter)
            throws Exception {
        DemandOrgDayUsedReportExcelDataResult result = new DemandOrgDayUsedReportExcelDataResult();

        //時刻がNULLの場合、0000を設定
        if (CheckUtility.isNullOrEmpty(parameter.getTimesOfDay())) {
            parameter.setTimesOfDay("0000");
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、24（時間）を設定
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

        // デマンド情報日報用集計範囲取得
        SummaryRangeForDayResult summaryRange = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getTimesOfDay(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        // ヘッダー：時限
        result.setHeaderTimeList(getHeaderTimeList(summaryRange));

        // 系統
        result.setLineList(getLineList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                parameter.getLineGroupId(), parameter.getGraphId(), summaryRange, parameter.getPrecision(),
                parameter.getBelowAccuracyControl()));

        // ポイント
        result.setSmList(getSmList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                parameter.getLineGroupId(), parameter.getGraphId(), summaryRange, parameter.getPrecision(),
                parameter.getBelowAccuracyControl()));

        return result;
    }

    /**
     * ヘッダー：時限
     *
     * @param day
     * @param time
     * @param type
     * @param range
     * @return
     * @throws ParseException
     */
    private List<String> getHeaderTimeList(SummaryRangeForDayResult summaryRange) throws ParseException {

        List<String> headerTimeList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm～");

        Date _dtFr = DateUtility.conversionDate(summaryRange.getRangeFrom(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Date _dtTo = DateUtility.conversionDate(summaryRange.getRangeTo(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Calendar cal = Calendar.getInstance();
        cal.setTime(_dtFr);
        while (cal.getTime().compareTo(_dtTo) <= 0) {
            headerTimeList.add(sdf.format(cal.getTime()));
            cal.add(Calendar.MINUTE, 30);
        }
        return headerTimeList;
    }

    /**
     * 系統
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param graphId
     * @param summaryRange
     * @param decimal
     * @param control
     * @return
     */
    private List<DemandOrgDayUsedReportExcelDataLineResultData> getLineList(String corpId,
            Long buildingId,
            Long lineGroupId,
            Long graphId,
            SummaryRangeForDayResult summaryRange,
            Integer decimal,
            String control) {
        List<DemandOrgDayUsedReportExcelDataLineResultData> ret = new ArrayList<>();

        // グラフ要素設定
        if (graphId != null) {
            MGraphElement _param = new MGraphElement();
            MGraphElementPK _id = new MGraphElementPK();
            _id.setCorpId(corpId);
            _id.setBuildingId(buildingId);
            _id.setLineGroupId(lineGroupId);
            _id.setGraphId(graphId);
            _param.setId(_id);
            _param.setGraphElementType(ApiCodeValueConstants.GRAPH_VALUE_TYPE.DEMAND.getVal());
            List<MGraphElement> graphList = getResultList(graphElementServiceDaoImpl, _param);
            if (graphList != null) {
                for (MGraphElement _graph : graphList) {
                    if (!CheckUtility.isNullOrEmpty(_graph.getGraphLineNo())) {
                        ret.addAll(getLineList(corpId,
                                buildingId,
                                lineGroupId,
                                _graph.getGraphLineNo(),
                                summaryRange,
                                decimal,
                                control));
                    }
                }
            }
        } else {
            // 全て
            ret = getLineList(corpId, buildingId, lineGroupId, "", summaryRange, decimal, control);
        }

        // 外気温
        boolean isTemp = false;
        if (graphId != null) {
            MGraphElement _param = new MGraphElement();
            MGraphElementPK _id = new MGraphElementPK();
            _id.setCorpId(corpId);
            _id.setBuildingId(buildingId);
            _id.setLineGroupId(lineGroupId);
            _id.setGraphId(graphId);
            _param.setId(_id);
            _param.setGraphElementType(ApiCodeValueConstants.GRAPH_VALUE_TYPE.AMEDAS.getVal());
            List<MGraphElement> graphList = getResultList(graphElementServiceDaoImpl, _param);
            if (graphList != null && !graphList.isEmpty()) {
                isTemp = true;
            }
        } else {
            MBuildingDm _param = new MBuildingDm();
            MBuildingDmPK id = new MBuildingDmPK();
            id.setCorpId(corpId);
            id.setBuildingId(buildingId);
            _param.setId(id);
            MBuildingDm entity = find(buildingDmServiceDaoImpl, _param);
            if (entity != null
                    && ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_ON.getVal()
                            .equals(entity.getOutAirTempDispFlg())) {
                isTemp = true;
            }
        }
        if (isTemp) {
            //デマンド日報からデータを取得する
            CommonDemandDayReportListResult dayListParam = DemandEmsUtility.getDayReportListParam(corpId, buildingId,
                    summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom(),
                    summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoTo());
            List<CommonDemandDayReportListResult> dayList = getResultList(commonDemandDayReportListServiceDaoImpl,
                    dayListParam);
            DemandOrgDayUsedReportExcelDataLineResultData rs = new DemandOrgDayUsedReportExcelDataLineResultData();
            rs.setLineFlg(false);
            rs.setLineName(LINE_NAME_AMEDAS);
            rs.setLineTargetName(LINE_TARGET_AMEDAS);
            //
            List<String> _kindList = new ArrayList<>();
            _kindList.add(KIND_AMEDAS);
            rs.setKindList(_kindList);
            // 単位
            List<String> _unitList = new ArrayList<>();
            _unitList.add(UNIT_AMEDAS);
            rs.setUnitList(_unitList);
            // 使用量・標準外使用量
            List<List<BigDecimal>> valueList = new ArrayList<>();
            List<BigDecimal> sumValues = new ArrayList<>();
            List<BigDecimal> maxValues = new ArrayList<>();
            List<BigDecimal> minValues = new ArrayList<>();
            List<BigDecimal> averageValues = new ArrayList<>();
            editOutTemperatureList(dayList,
                    summaryRange.getJigenNoFrom(),
                    decimal,
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal()),
                    valueList,
                    sumValues,
                    maxValues,
                    minValues,
                    averageValues);
            // セット
            rs.setValueList(valueList);
            rs.setSumValues(sumValues);
            rs.setMaxValues(maxValues);
            rs.setMinValues(minValues);
            rs.setAverageValues(averageValues);
            ret.add(rs);
        }

        return ret;
    }

    /**
     * 系統
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param summaryRange
     * @param decimal
     * @param control
     * @return
     */
    private List<DemandOrgDayUsedReportExcelDataLineResultData> getLineList(String corpId,
            Long buildingId,
            Long lineGroupId,
            String lineNo,
            SummaryRangeForDayResult summaryRange,
            Integer decimal,
            String control) {
        List<DemandOrgDayUsedReportExcelDataLineResultData> ret = new ArrayList<>();

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(corpId, lineGroupId, lineNo,
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);
        if (lineList != null && !lineList.isEmpty()) {
            //系統番号順にソートする
            lineList.sort((LineListDetailResultData l1, LineListDetailResultData l2) -> {
                int i1 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(l1.getLineNo()) ? 0
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(l1.getLineNo()) ? Integer.MAX_VALUE
                                : Integer.parseInt(l1.getLineNo());
                int i2 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(l2.getLineNo()) ? 0
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(l2.getLineNo()) ? Integer.MAX_VALUE
                                : Integer.parseInt(l2.getLineNo());
                return i1 - i2;
            });
            for (LineListDetailResultData line : lineList) {
                DemandOrgDayUsedReportExcelDataLineResultData rs = new DemandOrgDayUsedReportExcelDataLineResultData();
                rs.setLineFlg(true);
                rs.setLineGroupId(line.getLineGroupId());
                rs.setLineNo(line.getLineNo());
                rs.setLineName(line.getLineName());
                rs.setLineTarget(line.getLineTarget());
                rs.setLineTargetName(ApiCodeValueConstants.LINE_TARGET.getName(line.getLineTarget()));
                // 分類
                List<String> kindList = new ArrayList<>();
                kindList.add(KIND_USED);
                kindList.add(KIND_OVER);
                rs.setKindList(kindList);
                // 単位
                List<String> unitList = new ArrayList<>();
                unitList.add(line.getLineUnit());
                unitList.add(line.getLineUnit());
                rs.setUnitList(unitList);
                // 使用量・標準外使用量
                List<List<BigDecimal>> valueList = new ArrayList<>();
                List<BigDecimal> sumValues = new ArrayList<>();
                List<BigDecimal> maxValues = new ArrayList<>();
                List<BigDecimal> minValues = new ArrayList<>();
                List<BigDecimal> averageValues = new ArrayList<>();
                // 使用量：デマンド日報系統からデータを取得する
                CommonDemandDayReportLineListResult dayLineParam = DemandEmsUtility.getDayReportLineListParam(corpId,
                        buildingId, lineGroupId, line.getLineNo(), summaryRange.getMeasurementDateFrom(),
                        summaryRange.getJigenNoFrom(), summaryRange.getMeasurementDateTo(),
                        summaryRange.getJigenNoTo());
                List<CommonDemandDayReportLineListResult> dayLineList = getResultList(
                        commonDemandDayReportLineListServiceDaoImpl, dayLineParam);
                // 編集
                editDayLineList(
                        line.getLineTarget(),
                        dayLineList,
                        summaryRange.getJigenNoFrom(),
                        decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control),
                        valueList,
                        sumValues,
                        maxValues,
                        minValues,
                        averageValues);
                // セット
                rs.setValueList(valueList);
                rs.setSumValues(sumValues);
                rs.setMaxValues(maxValues);
                rs.setMinValues(minValues);
                rs.setAverageValues(averageValues);

                ret.add(rs);
            }
        }

        return ret;
    }

    /**
     * 系統　編集
     *
     * @param lineTarget
     * @param dayLineList
     * @param jigenNoFrom
     * @param scale
     * @param roundMode
     * @param valueList
     * @param sumValues
     * @param maxValues
     * @param minValues
     * @param averageValues
     */
    private void editDayLineList(
            String lineTarget,
            List<CommonDemandDayReportLineListResult> dayLineList,
            BigDecimal jigenNoFrom,
            int scale,
            int roundMode,
            List<List<BigDecimal>> valueList,
            List<BigDecimal> sumValues,
            List<BigDecimal> maxValues,
            List<BigDecimal> minValues,
            List<BigDecimal> averageValues) {

        int _cnt = 0;
        Iterator<CommonDemandDayReportLineListResult> iterator = null;
        CommonDemandDayReportLineListResult _rs = null;

        // 使用量
        List<BigDecimal> _valueList1 = new ArrayList<>();
        BigDecimal _sum1 = null;
        BigDecimal _max1 = null;
        BigDecimal _min1 = null;
        // 標準外使用量
        List<BigDecimal> _valueList2 = new ArrayList<>();
        BigDecimal _sum2 = null;
        BigDecimal _max2 = null;
        BigDecimal _min2 = null;

        if (dayLineList != null && !dayLineList.isEmpty()) {
            // ソート
            dayLineList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(
                    dayLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
            iterator = dayLineList.iterator();
            _rs = iterator.next();
        }

        int _curJigenNo = jigenNoFrom.intValue();
        for (int i = 0; i < JIGEN_NO_CNT; i++) {

            BigDecimal lineVal = null; // 系統電力
            BigDecimal diff = null; // 系統上限基準電力

            if (_rs != null && _rs.getJigenNo().intValue() == _curJigenNo) {
                // 系統電力
                lineVal = _rs.getLineValueKw();
                _sum1 = (_sum1 == null) ? lineVal : _sum1.add(lineVal);
                _max1 = (_max1 == null || _max1.compareTo(lineVal) < 0) ? lineVal : _max1;
                _min1 = (_min1 == null || _min1.compareTo(lineVal) > 0) ? lineVal : _min1;

                // 建物系統時限標準値
                MLineTimeStandard param = new MLineTimeStandard();
                MLineTimeStandardPK id = new MLineTimeStandardPK();
                id.setCorpId(_rs.getCorpId());
                id.setBuildingId(_rs.getBuildingId());
                id.setLineGroupId(_rs.getLineGroupId());
                id.setLineNo(_rs.getLineNo());
                id.setJigenNo(_rs.getJigenNo());
                param.setId(id);
                MLineTimeStandard entity = find(lineTimeStandardsServiceDaoImpl, param);
                if (entity != null) {
                    // 系統上限基準電力
                    if (lineVal != null && entity.getLineLimitStandardKw() != null) {
                        if (lineVal.compareTo(entity.getLineLimitStandardKw()) > 0) {
                            diff = lineVal.subtract(entity.getLineLimitStandardKw());
                        } else {
                            diff = BigDecimal.ZERO;
                        }
                    }
                }
                if (diff != null) {
                    _sum2 = (_sum2 == null) ? diff : _sum2.add(diff);
                    _max2 = (_max2 == null || _max2.compareTo(diff) < 0) ? diff : _max2;
                    _min2 = (_min2 == null || _min2.compareTo(diff) > 0) ? diff : _min2;
                }

                _cnt++;
                if (iterator != null && iterator.hasNext()) {
                    _rs = iterator.next();
                }
            }

            //
            _valueList1.add(lineVal);
            _valueList2.add(diff);

            // インクリメント
            _curJigenNo++;
            if (_curJigenNo > JIGEN_NO_CNT) {
                _curJigenNo = 1;
            }
        }

        // 集計
        valueList.add(_valueList1);
        valueList.add(_valueList2);
        sumValues.add(_sum1 == null ? null
                : ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(lineTarget)
                        ? _sum1
                        : _sum1.multiply(new BigDecimal("0.5")).setScale(scale, roundMode)); // kW→kWh  小数第一位に丸める(小数第二位を切り捨て)
        sumValues.add(_sum2 == null ? null
                : ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(lineTarget)
                        ? _sum2
                        : _sum2.multiply(new BigDecimal("0.5")).setScale(scale, roundMode)); // kW→kWh 小数第一位に丸める(小数第二位を切り捨て)
        maxValues.add(_max1);
        maxValues.add(_max2);
        minValues.add(_min1);
        minValues.add(_min2);
        if (_sum1 != null) {
            averageValues.add(_sum1.divide(new BigDecimal(_cnt),
                    scale,
                    roundMode));
        } else {
            averageValues.add(null);
        }
        if (_sum2 != null) {
            averageValues.add(_sum2.divide(new BigDecimal(_cnt),
                    scale,
                    roundMode));
        } else {
            averageValues.add(null);
        }
    }

    /**
     * 外気温　編集
     *
     * @param dayList
     * @param jigenNoFrom
     * @param scale
     * @param roundMode
     * @param valueList
     * @param sumValues
     * @param maxValues
     * @param minValues
     * @param averageValues
     */
    private void editOutTemperatureList(List<CommonDemandDayReportListResult> dayList,
            BigDecimal jigenNoFrom,
            int scale,
            int roundMode,
            List<List<BigDecimal>> valueList,
            List<BigDecimal> sumValues,
            List<BigDecimal> maxValues,
            List<BigDecimal> minValues,
            List<BigDecimal> averageValues) {
        Iterator<CommonDemandDayReportListResult> iterator = null;
        CommonDemandDayReportListResult _rs = null;
        int _cnt = 0;

        // 使用量
        List<BigDecimal> _valueList = new ArrayList<>();
        BigDecimal _sum = null;
        BigDecimal _max = null;
        BigDecimal _min = null;

        if (dayList != null && !dayList.isEmpty()) {
            // ソート
            dayList = SortUtility.sortCommonDemandDayReportListByMeasurement(
                    dayList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
            iterator = dayList.iterator();
            _rs = iterator.next();
        }
        int _curJigenNo = jigenNoFrom.intValue();
        for (int i = 0; i < JIGEN_NO_CNT; i++) {
            BigDecimal _temp = null;
            if (_rs != null && _rs.getJigenNo().intValue() == _curJigenNo) {

                // 外気温
                if (_rs.getOutAirTemp() != null) {
                    _temp = _rs.getOutAirTemp();
                    _sum = (_sum == null) ? _temp : _sum.add(_temp);
                    _max = (_max == null || _max.compareTo(_temp) < 0) ? _temp : _max;
                    _min = (_min == null || _min.compareTo(_temp) > 0) ? _temp : _min;
                    _cnt++;
                }

                if (iterator != null && iterator.hasNext()) {
                    _rs = iterator.next();
                }
            }
            _valueList.add(_temp);

            // インクリメント
            _curJigenNo++;
            if (_curJigenNo > JIGEN_NO_CNT) {
                _curJigenNo = 1;
            }
        }

        // 集計
        valueList.add(_valueList);
        sumValues.add(_sum);
        maxValues.add(_max);
        minValues.add(_min);
        if (_sum != null) {
            averageValues.add(_sum.divide(new BigDecimal(_cnt),
                    scale,
                    roundMode));
        } else {
            averageValues.add(null);
        }
    }

    /**
     * ポイント
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param graphId
     * @param summaryRange
     * @param decimal
     * @param control
     * @return
     */
    private List<DemandOrgDayUsedReportExcelDataSmResultData> getSmList(String corpId,
            Long buildingId,
            Long lineGroupId,
            Long graphId,
            SummaryRangeForDayResult summaryRange,
            Integer decimal,
            String control) {
        List<DemandOrgDayUsedReportExcelDataSmResultData> ret = new ArrayList<>();

        // グラフ要素設定
        if (graphId != null) {
            MGraphElement _param = new MGraphElement();
            MGraphElementPK _id = new MGraphElementPK();
            _id.setCorpId(corpId);
            _id.setBuildingId(buildingId);
            _id.setLineGroupId(lineGroupId);
            _id.setGraphId(graphId);
            _param.setId(_id);
            _param.setGraphElementType(ApiCodeValueConstants.GRAPH_VALUE_TYPE.ANALOG.getVal());
            List<MGraphElement> graphList = getResultList(graphElementServiceDaoImpl, _param);
            if (graphList != null) {
                for (MGraphElement _graph : graphList) {
                    if (_graph.getGraphSmId() != null && !CheckUtility.isNullOrEmpty(_graph.getGraphPointNo())) {
                        List<DemandOrgDayUsedReportExcelDataPointResultData> _pointList = null;
                        for (DemandOrgDayUsedReportExcelDataSmResultData _sm : ret) {
                            if (_sm.getSmId().equals(_graph.getGraphSmId())) {
                                _pointList = _sm.getPointList();
                                break;
                            }
                        }
                        if (_pointList != null) {
                            _pointList.addAll(getPointList(corpId,
                                    buildingId,
                                    _graph.getGraphSmId(),
                                    _graph.getGraphPointNo(),
                                    summaryRange,
                                    decimal,
                                    control));
                        } else {
                            ret.addAll(getSmList(corpId,
                                    buildingId,
                                    _graph.getGraphSmId(),
                                    _graph.getGraphPointNo(),
                                    summaryRange,
                                    decimal,
                                    control));
                        }
                    }
                }
            }
        } else {
            // 全て
            ret = getSmList(corpId, buildingId, null, "", summaryRange, decimal, control);
        }

        return ret;
    }

    /**
     * ポイント
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param summaryRange
     * @param decimal
     * @param control
     * @return
     */
    private List<DemandOrgDayUsedReportExcelDataSmResultData> getSmList(String corpId,
            Long buildingId,
            Long smId,
            String pointNo,
            SummaryRangeForDayResult summaryRange,
            Integer decimal,
            String control) {
        List<DemandOrgDayUsedReportExcelDataSmResultData> ret = new ArrayList<>();

        // 建物機器
        DemandBuildingSmListDetailResultData param = new DemandBuildingSmListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        List<DemandBuildingSmListDetailResultData> smList = getResultList(demandBuildingSmListServiceDaoImpl, param);
        if (smList != null && !smList.isEmpty()) {
            for (DemandBuildingSmListDetailResultData rsSm : smList) {

                // 日報・ポイント
                DemandOrgDayUsedReportExcelDataSmResultData rs = new DemandOrgDayUsedReportExcelDataSmResultData();
                rs.setSmId(rsSm.getSmId());
                rs.setProductCd(rsSm.getProductCd());
                rs.setSmAddress(rsSm.getSmAddress());
                rs.setProductName(rsSm.getProductName());

                // 建物機器ポイント
                rs.setPointList(
                        getPointList(corpId, buildingId, rsSm.getSmId(), pointNo, summaryRange, decimal, control));
                ret.add(rs);
            }
        }

        return ret;
    }

    /**
     * デマンド日報ポイント
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param summaryRange
     * @param decimal
     * @param control
     * @return
     */
    private List<DemandOrgDayUsedReportExcelDataPointResultData> getPointList(String corpId,
            Long buildingId,
            Long smId,
            String pointNo,
            SummaryRangeForDayResult summaryRange,
            Integer decimal,
            String control) {
        List<DemandOrgDayUsedReportExcelDataPointResultData> pointList = new ArrayList<>();

        DemandBuildingSmPointListDetailResultData _param = new DemandBuildingSmPointListDetailResultData();
        _param.setCorpId(corpId);
        _param.setBuildingId(buildingId);
        _param.setSmId(smId);
        _param.setPointNo(pointNo);
        List<DemandBuildingSmPointListDetailResultData> _ret = getResultList(demandBuildingSmPointListServiceDaoImpl,
                _param);
        if (_ret != null && !_ret.isEmpty()) {
            for (DemandBuildingSmPointListDetailResultData rsPoint : _ret) {
                DemandOrgDayUsedReportExcelDataPointResultData _point = new DemandOrgDayUsedReportExcelDataPointResultData();
                _point.setPointNo(POINT_NO_SRC.equals(rsPoint.getPointNo()) ? POINT_NM_SRC : rsPoint.getPointNo());
                _point.setPointName(rsPoint.getPointName());
                _point.setPointType(ApiCodeValueConstants.POINT_TYPE.getName(rsPoint.getPointType()));
                _point.setKind(KIND_AMEDAS);
                _point.setPointUnit(rsPoint.getPointUnit());

                // デマンド日報ポイント
                List<BigDecimal> values = new ArrayList<>();
                List<BigDecimal> sumVals = new ArrayList<>();
                CommonDemandDayReportPointListResult lstDayRepPointParam = DemandEmsUtility.getDayReportPointListParam(
                        corpId, buildingId, smId, summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom(),
                        summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoTo(), rsPoint.getPointNo(),
                        rsPoint.getPointNo());
                List<CommonDemandDayReportPointListResult> lstDayRepPoint = getResultList(
                        commonDemandDayReportPointListServiceDaoImpl, lstDayRepPointParam);

                // 編集
                editDayPointList(lstDayRepPoint,
                        summaryRange.getJigenNoFrom(),
                        decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal()),
                        values,
                        sumVals);

                _point.setValues(values);
                if (sumVals.size() > 0) {
                    if (ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal().equals(rsPoint.getPointType())) {
                        // アナログの合計は出さない
                        _point.setSum(null);
                    } else if (ApiGenericTypeConstants.POINT_TYPE.PULSE.getVal().equals(rsPoint.getPointType())) {
                        if (CheckUtility.isNullOrEmpty(rsPoint.getPointUnit())) {
                            // パルスで単位未設定：kW→kWh
                            if (sumVals.get(0) != null) {
                                _point.setSum(sumVals.get(0).divide(new BigDecimal(2), 1, RoundingMode.DOWN));
                            } else {
                                _point.setSum(sumVals.get(0));
                            }
                        } else {
                            _point.setSum(sumVals.get(0));
                        }
                    }
                }
                if (sumVals.size() > 1) {
                    _point.setMax(sumVals.get(1));
                }
                if (sumVals.size() > 2) {
                    _point.setMin(sumVals.get(2));
                }
                if (sumVals.size() > 3) {
                    _point.setAverage(sumVals.get(3));
                }
                pointList.add(_point);
            }
        }

        // ソート
        if (CheckUtility.isNullOrEmpty(pointNo) && pointList != null && pointList.size() > 1) {
            pointList.sort(
                    (DemandOrgDayUsedReportExcelDataPointResultData rs1,
                            DemandOrgDayUsedReportExcelDataPointResultData rs2) -> {
                        String point1 = POINT_NM_SRC.equals(rs1.getPointNo()) ? "" : rs1.getPointNo();
                        String point2 = POINT_NM_SRC.equals(rs2.getPointNo()) ? "" : rs2.getPointNo();
                        return point1.compareTo(point2);
                    });
        }
        return pointList;
    }

    /**
     * ポイント編集
     *
     * @param pointList
     * @param jigenNoFrom
     * @param scale
     * @param roundMode
     * @param values
     * @param sumValues 0:合計、1:最大、2:最小。3:平均
     */
    private void editDayPointList(List<CommonDemandDayReportPointListResult> pointList,
            BigDecimal jigenNoFrom,
            int scale,
            int roundMode,
            List<BigDecimal> values,
            List<BigDecimal> sumValues) {
        BigDecimal[] sumVals = new BigDecimal[4];
        Iterator<CommonDemandDayReportPointListResult> iterator = null;
        CommonDemandDayReportPointListResult rs = null;
        int _cnt = 0;

        if (pointList != null && !pointList.isEmpty()) {
            // ソート
            pointList = SortUtility.sortCommonDemandDayReportPointListByMeasurement(
                    pointList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
            iterator = pointList.iterator();
            rs = iterator.next();
        }
        int _curJigenNo = jigenNoFrom.intValue();
        for (int i = 0; i < JIGEN_NO_CNT; i++) {

            BigDecimal _temp = null;
            if (rs != null && rs.getJigenNo().intValue() == _curJigenNo) {
                // 外気温
                _temp = rs.getPointVal();

                if(_temp != null) {
                    sumVals[0] = (sumVals[0] == null) ? _temp : sumVals[0].add(_temp);
                    sumVals[1] = (sumVals[1] == null || sumVals[1].compareTo(_temp) < 0) ? _temp : sumVals[1];
                    sumVals[2] = (sumVals[2] == null || sumVals[2].compareTo(_temp) > 0) ? _temp : sumVals[2];
                }
                _cnt++;
                if (iterator != null && iterator.hasNext()) {
                    rs = iterator.next();
                }
            }
            values.add(_temp);

            // インクリメント
            _curJigenNo++;
            if (_curJigenNo > JIGEN_NO_CNT) {
                _curJigenNo = 1;
            }
        }

        // 平均
        sumVals[3] = (sumVals[0] == null)
                ? null
                : sumVals[0].divide(new BigDecimal(_cnt), scale, roundMode);
        //
        sumValues.addAll(Arrays.asList(sumVals));
    }

}
