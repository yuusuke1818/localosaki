package jp.co.osaki.osol.api.dao.analysis;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisAllBuildingListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisAllBuildingListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisAllBuildingListServiceDaoImpl;

/**
 *
 *集計分析 EMS実績 対象建物取得(全建物) Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisAllBuildingListDao extends OsolApiDao<AnalysisAllBuildingListParameter> {

    private final AnalysisAllBuildingListServiceDaoImpl analysisAllBuildingListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisAllBuildingListDao() {
        analysisAllBuildingListServiceDaoImpl = new AnalysisAllBuildingListServiceDaoImpl();
    }

    @Override
    public AnalysisAllBuildingListResult query(AnalysisAllBuildingListParameter parameter) throws Exception {

        AnalysisAllBuildingListResult result = new AnalysisAllBuildingListResult();

        AnalysisAllBuildingListResultData param = new AnalysisAllBuildingListResultData();
        param.setCorpId(parameter.getOperationCorpId());

        List<AnalysisAllBuildingListResultData> resultList = getResultList(analysisAllBuildingListServiceDaoImpl,
                param);
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
