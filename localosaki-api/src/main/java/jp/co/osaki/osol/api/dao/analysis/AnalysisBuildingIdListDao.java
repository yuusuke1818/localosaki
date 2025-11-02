package jp.co.osaki.osol.api.dao.analysis;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisBuildingIdListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisBuildingIdListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisBuildingIdListServiceDaoImpl;

/**
 *
 *集計分析 EMS実績 対象建物取得(建物ID指定) Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisBuildingIdListDao extends OsolApiDao<AnalysisBuildingIdListParameter> {

    private final AnalysisBuildingIdListServiceDaoImpl analysisBuildingIdListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisBuildingIdListDao() {
        analysisBuildingIdListServiceDaoImpl = new AnalysisBuildingIdListServiceDaoImpl();
    }

    @Override
    public AnalysisBuildingIdListResult query(AnalysisBuildingIdListParameter parameter) throws Exception {

        AnalysisBuildingIdListResult result = new AnalysisBuildingIdListResult();

        AnalysisAllBuildingListResultData param = new AnalysisAllBuildingListResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setBelongTenantFlg(parameter.getBelongTenantFlg());

        List<AnalysisAllBuildingListResultData> resultList = getResultList(analysisBuildingIdListServiceDaoImpl,param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));
        //ソート
        resultList = resultList.stream().sorted(Comparator.comparing(AnalysisAllBuildingListResultData::getBuildingNo,
                Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());

        result.setTargetBuildingList(resultList);

        return result;
    }

}
