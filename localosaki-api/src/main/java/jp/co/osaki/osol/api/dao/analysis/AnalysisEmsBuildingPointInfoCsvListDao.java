package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointInfoCsvListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointInfoCsvListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsBuildingPointInfoListServiceDaoImpl;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Daoクラス
 *
 * @author yonezawa.a
 *
 */
@Stateless
public class AnalysisEmsBuildingPointInfoCsvListDao extends OsolApiDao<AnalysisEmsBuildingPointInfoCsvListParameter> {

    private final AnalysisEmsBuildingPointInfoListServiceDaoImpl analysisEmsBuildingPointInfoListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingPointInfoCsvListDao() {
        analysisEmsBuildingPointInfoListServiceDaoImpl = new AnalysisEmsBuildingPointInfoListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingPointInfoCsvListResult query(AnalysisEmsBuildingPointInfoCsvListParameter parameter) throws Exception {

        AnalysisEmsBuildingPointInfoCsvListResult result = new AnalysisEmsBuildingPointInfoCsvListResult();

        CommonDemandDayReportPointListResult param = new CommonDemandDayReportPointListResult();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setSmId(parameter.getSmId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setJigenNoFrom(new BigDecimal("1"));
        param.setJigenNoTo(new BigDecimal("48"));

        List<CommonDemandDayReportPointListResult> resultList = getResultList(
                analysisEmsBuildingPointInfoListServiceDaoImpl, param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setDemandDayReportPointList(resultList);

        return result;
    }

}
