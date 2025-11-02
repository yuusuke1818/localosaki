package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingSummaryParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingSummaryResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsAllLineSummaryResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsBuildingSummaryServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * 集計・分析 EMS実績 建物別サマリー取得 Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisEmsBuildingSummaryDao extends OsolApiDao<AnalysisEmsBuildingSummaryParameter> {

    private final AnalysisEmsBuildingSummaryServiceDaoImpl analysisEmsBuildingSummaryServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingSummaryDao() {
        analysisEmsBuildingSummaryServiceDaoImpl = new AnalysisEmsBuildingSummaryServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingSummaryResult query(AnalysisEmsBuildingSummaryParameter parameter) throws Exception {

        AnalysisEmsBuildingSummaryResult result = new AnalysisEmsBuildingSummaryResult();

        AnalysisEmsAllLineSummaryResultData param = new AnalysisEmsAllLineSummaryResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、四捨五入を設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
        }

        List<AnalysisEmsAllLineSummaryResultData> resultList = getResultList(analysisEmsBuildingSummaryServiceDaoImpl,
                param);

        //外気温取得
        CommonDemandDayReportListResult paramOutAirTemp = new CommonDemandDayReportListResult();
        paramOutAirTemp.setCorpId(parameter.getOperationCorpId());
        paramOutAirTemp.setBuildingId(parameter.getBuildingId());
        paramOutAirTemp.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        paramOutAirTemp.setMeasurementDateTo(parameter.getMeasurementDateTo());
        paramOutAirTemp.setJigenNoFrom(new BigDecimal(1));
        paramOutAirTemp.setJigenNoTo(new BigDecimal(48));

        List<CommonDemandDayReportListResult> outAirTempResultList = getResultList(commonDemandDayReportListServiceDaoImpl,paramOutAirTemp);

        for (BigDecimal i = BigDecimal.ONE; i.compareTo(new BigDecimal(48)) <= 0; i = i.add(BigDecimal.ONE)) {
            AnalysisEmsAllLineSummaryResultData tempData = new AnalysisEmsAllLineSummaryResultData();
            BigDecimal index = new BigDecimal(i.toPlainString());
            List<CommonDemandDayReportListResult> valList = outAirTempResultList.stream().filter(o -> o.getJigenNo().equals(index)).collect(Collectors.toList());

            BigDecimal sum = null;
            BigDecimal max = null;
            BigDecimal min = null;
            Long count = new Long(0);

            //外気温の集計を行う

            for(CommonDemandDayReportListResult valData : valList) {
                if(valData.getOutAirTemp() != null) {
                    if(sum == null) {
                        sum = valData.getOutAirTemp();
                    }else {
                        sum = sum.add(valData.getOutAirTemp());
                    }


                    if(max == null) {
                        max = valData.getOutAirTemp();
                    }else {
                        if(max.compareTo(valData.getOutAirTemp()) < 0) {
                            max = valData.getOutAirTemp();
                        }
                    }

                    if(min == null) {
                        min = valData.getOutAirTemp();
                    }else {
                        if(min.compareTo(valData.getOutAirTemp()) > 0) {
                            min = valData.getOutAirTemp();
                        }
                    }

                    count = count + new Long(1);
                }
            }

            if (sum != null) {
                tempData.setCorpId(parameter.getOperationCorpId());
                tempData.setBuildingId(parameter.getBuildingId());
                tempData.setLineNo("");
                tempData.setJigenNo(i);
                tempData.setSummary(sum);
                tempData.setMax(max);
                tempData.setMin(min);
                tempData.setDataCount(count);

                resultList.add(tempData);
            }

        }

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        //各集計値の精度を設定
        for(AnalysisEmsAllLineSummaryResultData summaryResult:resultList) {
            //合計
            summaryResult.setSummary(summaryResult.getSummary().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));

          //平均
            if(!summaryResult.getDataCount().equals(new Long(0))) {
                BigDecimal average = summaryResult.getSummary().divide(BigDecimal.valueOf(summaryResult.getDataCount()),10, BigDecimal.ROUND_HALF_UP);
                average.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
                summaryResult.setAverage(average);
            }else {
                summaryResult.setAverage(new BigDecimal("0"));
            }

          //最大
            summaryResult.setMax(summaryResult.getMax().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));

          //最小
            summaryResult.setMin(summaryResult.getMin().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        result.setBuildingSummaryList(resultList);

        return result;
    }

}
