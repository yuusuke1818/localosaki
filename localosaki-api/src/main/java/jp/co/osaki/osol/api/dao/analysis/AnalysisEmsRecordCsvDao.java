package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsRecordCsvParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsGroupLineRecordCsvResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsRecordCsvResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsRecordCsvServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsTempRecordCsvServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisGroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * 集計・分析 EMS実績 系統別計測値CSV出力情報取得 Daoクラス
 * 取得方法はAnalysisEmsBuildingSummaryDaoと類似するため、API、resultbeanなども同一のものを使用する。
 *
 * @author yonezawa.a
 *
 */
@Stateless
public class AnalysisEmsRecordCsvDao extends OsolApiDao<AnalysisEmsRecordCsvParameter> {

    private final AnalysisGroupBuildingListServiceDaoImpl analysisGroupBuildingListServiceDaoImpl;
    private final AnalysisEmsRecordCsvServiceDaoImpl analysisEmsRecordCsvServiceDaoImpl;
    private final AnalysisEmsTempRecordCsvServiceDaoImpl analysisEmsTempRecordCsvServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsRecordCsvDao() {
        analysisGroupBuildingListServiceDaoImpl = new AnalysisGroupBuildingListServiceDaoImpl();
        analysisEmsRecordCsvServiceDaoImpl = new AnalysisEmsRecordCsvServiceDaoImpl();
        analysisEmsTempRecordCsvServiceDaoImpl = new AnalysisEmsTempRecordCsvServiceDaoImpl();
    }

    @Override
    public AnalysisEmsGroupLineRecordCsvResult query(AnalysisEmsRecordCsvParameter parameter) throws Exception {

        AnalysisEmsGroupLineRecordCsvResult result = new AnalysisEmsGroupLineRecordCsvResult();
        List<AnalysisEmsRecordCsvResultData> resultList = new ArrayList<AnalysisEmsRecordCsvResultData>();

        AnalysisAllBuildingListResultData targetBuildingParam = new AnalysisAllBuildingListResultData();
        targetBuildingParam.setCorpId(parameter.getOperationCorpId());
        targetBuildingParam.setParentGroupId(parameter.getParentGroupId());
        targetBuildingParam.setChildGroupId(parameter.getChildGroupId());

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、四捨五入を設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
        }

        //パラメータ設定(建物毎サマリ)
        AnalysisEmsRecordCsvResultData buildingSummaryParam = new AnalysisEmsRecordCsvResultData();
        //企業ID
        buildingSummaryParam.setCorpId(parameter.getOperationCorpId());
        //建物ID
        buildingSummaryParam.setBuildingId(parameter.getBuildingId());
        //系統グループID
        buildingSummaryParam.setLineGroupId(parameter.getLineGroupId());
        // 系統番号
        buildingSummaryParam.setLineNo(parameter.getLineNo());
        //集計範囲From
        buildingSummaryParam.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        //集計範囲To
        buildingSummaryParam.setMeasurementDateTo(parameter.getMeasurementDateTo());
        buildingSummaryParam.setExtendsPlusMinus12hEnabled(parameter.getEmsExtendsPlusMinus12hEnabled());
        // 選択した系統
        buildingSummaryParam.setSelectedLineId(parameter.getSelectedLineId());

        List<AnalysisEmsRecordCsvResultData> buildingSummaryResultList;
        if (ApiCodeValueConstants.ANALYSIS_LINE_TYPE.TEMP.getVal().equals(parameter.getLineNo())) {
            // 外気温
            buildingSummaryResultList = getResultList(
                    analysisEmsTempRecordCsvServiceDaoImpl, buildingSummaryParam);
        } else {
            // 選択された系統
            buildingSummaryResultList = getResultList(
                    analysisEmsRecordCsvServiceDaoImpl, buildingSummaryParam);
        }

        if (!parameter.getSelectedLineId().equals("0")) {
            // 使用量以外の場合、平均値を算出する
            for (AnalysisEmsRecordCsvResultData buildingSummary : buildingSummaryResultList) {
                if (buildingSummary.getDataCount() != 0) {
                    BigDecimal average = buildingSummary.getSummary()
                            .divide(BigDecimal.valueOf(buildingSummary.getDataCount()), 1, BigDecimal.ROUND_DOWN);
                    average.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl()));
                    buildingSummary.setAverage(average);
                } else {
                    if (buildingSummary.getSummary() != null) {
                        buildingSummary.setAverage(new BigDecimal("0"));
                    }
                }
            }
        }

        if (buildingSummaryResultList != null && buildingSummaryResultList.size() != 0) {
            resultList.addAll(buildingSummaryResultList);
        }

        result.setRecordCsvList(resultList);

        return result;
    }

}