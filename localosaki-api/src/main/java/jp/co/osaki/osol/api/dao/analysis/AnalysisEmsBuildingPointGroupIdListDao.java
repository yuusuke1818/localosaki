package jp.co.osaki.osol.api.dao.analysis;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointGroupIdListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointGroupIdListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointGroupIdResult;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsBuildingPointGroupIdListServiceDaoImpl;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Daoクラス
 *
 * @author yonezawa.a
 *
 */
@Stateless
public class AnalysisEmsBuildingPointGroupIdListDao extends OsolApiDao<AnalysisEmsBuildingPointGroupIdListParameter> {

    private final AnalysisEmsBuildingPointGroupIdListServiceDaoImpl analysisEmsBuildingPointGroupIdListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsBuildingPointGroupIdListDao() {
        analysisEmsBuildingPointGroupIdListServiceDaoImpl = new AnalysisEmsBuildingPointGroupIdListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsBuildingPointGroupIdListResult query(AnalysisEmsBuildingPointGroupIdListParameter parameter) throws Exception {

        AnalysisEmsBuildingPointGroupIdListResult result = new AnalysisEmsBuildingPointGroupIdListResult();
        AnalysisEmsBuildingPointGroupIdResult param = new AnalysisEmsBuildingPointGroupIdResult();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        List<AnalysisEmsBuildingPointGroupIdResult> resultList = getResultList(
                analysisEmsBuildingPointGroupIdListServiceDaoImpl, param);

        result.setAnalysisEmsBuildingPointGroupIdList(resultList);

        return result;
    }

}
