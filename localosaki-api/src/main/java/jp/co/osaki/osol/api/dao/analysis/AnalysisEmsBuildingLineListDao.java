package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingLineListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;

/**
 *
 * 集計・分析 EMS実績 建物別系統実績値取得 Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisEmsBuildingLineListDao extends OsolApiDao<AnalysisEmsBuildingLineListParameter> {

    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingLineListDao() {
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingLineListResult query(AnalysisEmsBuildingLineListParameter parameter) throws Exception {

        AnalysisEmsBuildingLineListResult result = new AnalysisEmsBuildingLineListResult();

        CommonDemandDayReportLineListResult param = new CommonDemandDayReportLineListResult();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        List<CommonDemandDayReportLineListResult> resultList = getResultList(
                commonDemandDayReportLineListServiceDaoImpl, param);

        //外気温取得
        CommonDemandDayReportListResult paramOutAirTemp = new CommonDemandDayReportListResult();
        paramOutAirTemp.setCorpId(parameter.getOperationCorpId());
        paramOutAirTemp.setBuildingId(parameter.getBuildingId());
        paramOutAirTemp.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        paramOutAirTemp.setMeasurementDateTo(parameter.getMeasurementDateTo());
        paramOutAirTemp.setJigenNoFrom(new BigDecimal(1));
        paramOutAirTemp.setJigenNoTo(new BigDecimal(48));

        List<CommonDemandDayReportListResult> outAirTempResultList = getResultList(commonDemandDayReportListServiceDaoImpl,paramOutAirTemp);

        for(CommonDemandDayReportListResult outAirTempData : outAirTempResultList) {
            CommonDemandDayReportLineListResult tempData = new CommonDemandDayReportLineListResult();
            tempData.setCorpId(outAirTempData.getCorpId());
            tempData.setBuildingId(outAirTempData.getBuildingId());
            tempData.setMeasurementDate(outAirTempData.getMeasurementDate());
            tempData.setLineNo("");
            tempData.setJigenNo(outAirTempData.getJigenNo());
            tempData.setLineValueKw(outAirTempData.getOutAirTemp());

            resultList.add(tempData);
        }

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setDemandDayReportLineList(resultList);

        return result;
    }

}
