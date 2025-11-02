package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.analysis. AnalysisEmsGroupLineSummaryParameter;
import jp.co.osaki.osol.api.result.analysis. AnalysisEmsGroupLineSummaryResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsAllLineSummaryResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsGroupLineSummaryResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsBuildingSummaryServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisGroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * 集計・分析 EMS実績 系統別サマリー取得(グループ指定) Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisEmsGroupLineSummaryDao extends OsolApiDao<AnalysisEmsGroupLineSummaryParameter> {

    private final  AnalysisGroupBuildingListServiceDaoImpl   analysisGroupBuildingListServiceDaoImpl;
    private final AnalysisEmsBuildingSummaryServiceDaoImpl  analysisEmsBuildingSummaryServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl  commonDemandDayReportListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsGroupLineSummaryDao() {
         analysisGroupBuildingListServiceDaoImpl = new  AnalysisGroupBuildingListServiceDaoImpl();
         analysisEmsBuildingSummaryServiceDaoImpl = new AnalysisEmsBuildingSummaryServiceDaoImpl();
         commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsGroupLineSummaryResult query(AnalysisEmsGroupLineSummaryParameter parameter) throws Exception {

        AnalysisEmsGroupLineSummaryResult result = new AnalysisEmsGroupLineSummaryResult();
        result.setGroupLineSummaryList(new ArrayList<AnalysisEmsGroupLineSummaryResultData>());

        AnalysisAllBuildingListResultData targetBuildingParam = new  AnalysisAllBuildingListResultData();
        targetBuildingParam.setCorpId(parameter.getOperationCorpId());
        targetBuildingParam.setParentGroupId(parameter.getParentGroupId());
        targetBuildingParam.setChildGroupId(parameter.getChildGroupId());

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、四捨五入を設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
        }

        List<AnalysisAllBuildingListResultData> targetBuildingresultList = getResultList(
                analysisGroupBuildingListServiceDaoImpl, targetBuildingParam);

        // フィルター処理
        targetBuildingresultList = buildingDataFilterDao.applyDataFilter(targetBuildingresultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));
        //対象建物毎のサマリーから、系統別のサマリーを作成する
        for (AnalysisAllBuildingListResultData buildingSummaryResult : targetBuildingresultList) {

            //パラメータ設定(建物毎サマリ)
            AnalysisEmsAllLineSummaryResultData buildingSummaryParam = new AnalysisEmsAllLineSummaryResultData();
            //企業ID
            buildingSummaryParam.setCorpId(parameter.getOperationCorpId());
            //建物ID
            buildingSummaryParam.setBuildingId(buildingSummaryResult.getBuildingId());
            //系統グループID
            buildingSummaryParam.setLineGroupId(parameter.getLineGroupId());
            //集計範囲From
            buildingSummaryParam.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
            //集計範囲To
            buildingSummaryParam.setMeasurementDateTo(parameter.getMeasurementDateTo());

            List<AnalysisEmsAllLineSummaryResultData> buildingSummaryResultList = getResultList(
                    analysisEmsBuildingSummaryServiceDaoImpl, buildingSummaryParam);

            if (buildingSummaryResult.getOutAirTempDispFlg().equals(OsolConstants.FLG_ON)) {
                //外気温取得
                CommonDemandDayReportListResult paramOutAirTemp = new CommonDemandDayReportListResult();
                paramOutAirTemp.setCorpId(parameter.getOperationCorpId());
                paramOutAirTemp.setBuildingId(buildingSummaryResult.getBuildingId());
                paramOutAirTemp.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
                paramOutAirTemp.setMeasurementDateTo(parameter.getMeasurementDateTo());
                paramOutAirTemp.setJigenNoFrom(new BigDecimal(1));
                paramOutAirTemp.setJigenNoTo(new BigDecimal(48));

                List<CommonDemandDayReportListResult> outAirTempResultList = getResultList(
                        commonDemandDayReportListServiceDaoImpl, paramOutAirTemp);
                for (BigDecimal i = BigDecimal.ONE; i.compareTo(new BigDecimal(48)) <= 0; i = i.add(BigDecimal.ONE)) {

                    AnalysisEmsAllLineSummaryResultData tempData = new AnalysisEmsAllLineSummaryResultData();
                    BigDecimal index = new BigDecimal(i.toPlainString());
                    List<CommonDemandDayReportListResult> valList = outAirTempResultList.stream()
                            .filter(o -> o.getJigenNo().equals(index)).collect(Collectors.toList());

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
                        tempData.setBuildingId(buildingSummaryResult.getBuildingId());
                        tempData.setLineNo("");
                        tempData.setJigenNo(i);
                        tempData.setSummary(sum);
                        tempData.setMax(max);
                        tempData.setMin(min);
                        tempData.setDataCount(count);

                        buildingSummaryResultList.add(tempData);
                    }

                }

            }

            //系統毎のサマリーを作成する
            for (AnalysisEmsAllLineSummaryResultData buildingSummary : buildingSummaryResultList) {

                //系統、時限存在FLG
                boolean existFlg = false;

                //建物毎のサマリーデータを、系統、時限別に集計する
                for(AnalysisEmsGroupLineSummaryResultData resultData : result.getGroupLineSummaryList()) {
                    //全体サマリーデータに系統、時限が一致するデータがある場合は集計処理を行う。
                    if(buildingSummaryResult.getChildGroupId().equals(resultData.getChildGroupId())
                            && buildingSummary.getLineNo().equals(resultData.getLineNo())
                            && buildingSummary.getJigenNo().equals(resultData.getJigenNo())) {
                        //合計
                        resultData.setSummary(resultData.getSummary().add(buildingSummary.getSummary()));

                        //最大
                        if(resultData.getMax().compareTo(buildingSummary.getMax()) < 0 ) {
                            resultData.setMax(buildingSummary.getMax());
                        }

                        //最小
                        if(resultData.getMin().compareTo(buildingSummary.getMin()) > 0 ) {
                            resultData.setMin(buildingSummary.getMin());
                        }

                        //計測回数
                        resultData.setDataCount(resultData.getDataCount() + buildingSummary.getDataCount());

                        //存在FLGをON
                        existFlg = true;

                        break;
                    }

                }

                //系統、時限のデータが存在しなかった場合、建物別サマリーデータを初期値として格納する
                if(!existFlg) {

                    result.getGroupLineSummaryList().add(
                                new AnalysisEmsGroupLineSummaryResultData(
                                        parameter.getOperationCorpId(),
                                        buildingSummaryResult.getParentGroupId(),
                                        buildingSummaryResult.getChildGroupId(),
                                        parameter.getLineGroupId(),
                                        buildingSummary.getLineNo(),
                                        buildingSummary.getJigenNo(),
                                        buildingSummary.getSummary(),
                                        buildingSummary.getMax(),
                                        buildingSummary.getMin(),
                                        buildingSummary.getDataCount()
                                )
                            );
                }

            }

        }

        //平均値を算出する
        for (AnalysisEmsGroupLineSummaryResultData buildingSummary : result.getGroupLineSummaryList()) {

            //平均
            if(buildingSummary.getDataCount() != 0) {
                BigDecimal average = buildingSummary.getSummary().divide(BigDecimal.valueOf(buildingSummary.getDataCount()),10, BigDecimal.ROUND_HALF_UP);
                average.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
                buildingSummary.setAverage(average);
            }else {
                buildingSummary.setAverage(new BigDecimal("0"));
            }

        }

        return result;
    }

}
