package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;

/**
 *
 * 集計・分析 EMS実績 建物別ポイント実績値取得 Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisEmsBuildingPointListDao extends OsolApiDao<AnalysisEmsBuildingPointListParameter> {

    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingPointListDao() {
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingPointListResult query(AnalysisEmsBuildingPointListParameter parameter) throws Exception {

        AnalysisEmsBuildingPointListResult result = new AnalysisEmsBuildingPointListResult();

        CommonDemandDayReportPointListResult param = new CommonDemandDayReportPointListResult();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setJigenNoFrom(new BigDecimal("0"));
        param.setJigenNoTo(new BigDecimal("48"));

        List<CommonDemandDayReportPointListResult> resultList = getResultList(
                commonDemandDayReportPointListServiceDaoImpl, param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setDemandDayReportPointList(resultList);

        return result;
    }

}
