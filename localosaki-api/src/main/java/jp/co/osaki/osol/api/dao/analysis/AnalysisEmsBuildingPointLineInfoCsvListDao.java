package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointLineInfoCsvListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointLineInfoCsvListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsBuildingPointLineInfoListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Daoクラス
 *
 * @author yonezawa.a
 *
 */
@Stateless
public class AnalysisEmsBuildingPointLineInfoCsvListDao extends OsolApiDao<AnalysisEmsBuildingPointLineInfoCsvListParameter> {

    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;
    private final AnalysisEmsBuildingPointLineInfoListServiceDaoImpl analysisEmsBuildingPointLineInfoListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingPointLineInfoCsvListDao() {
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
        analysisEmsBuildingPointLineInfoListServiceDaoImpl = new AnalysisEmsBuildingPointLineInfoListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingPointLineInfoCsvListResult query(AnalysisEmsBuildingPointLineInfoCsvListParameter parameter) throws Exception {

        AnalysisEmsBuildingPointLineInfoCsvListResult result = new AnalysisEmsBuildingPointLineInfoCsvListResult();
        CommonDemandDayReportLineListResult param = new CommonDemandDayReportLineListResult();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        List<CommonDemandDayReportLineListResult> resultList = getResultList(
                commonDemandDayReportLineListServiceDaoImpl, param);

        //計測日時、時限Noでソートする
        resultList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(resultList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        //外気温取得
        CommonDemandDayReportListResult paramOutAirTemp = new CommonDemandDayReportListResult();
        paramOutAirTemp.setCorpId(parameter.getOperationCorpId());
        paramOutAirTemp.setBuildingId(parameter.getBuildingId());
        paramOutAirTemp.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        paramOutAirTemp.setMeasurementDateTo(parameter.getMeasurementDateTo());
        paramOutAirTemp.setJigenNoFrom(new BigDecimal(1));
        paramOutAirTemp.setJigenNoTo(new BigDecimal(48));

        List<CommonDemandDayReportListResult> outAirTempResultList = getResultList(commonDemandDayReportListServiceDaoImpl,paramOutAirTemp);

        //計測日時、時限Noでソートする
        outAirTempResultList = SortUtility.sortCommonDemandDayReportListByMeasurement(outAirTempResultList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setPointCsvList(resultList);
        result.setOutAirTempList(outAirTempResultList);

        return result;
    }

}
