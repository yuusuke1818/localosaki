package jp.co.osaki.osol.api.dao.analysis;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisGroupBuildingListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisGroupBuildingListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisGroupBuildingListServiceDaoImpl;

/**
 *
 *集計分析 EMS実績 対象建物取得(グループ指定) Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisGroupBuildingListDao extends OsolApiDao<AnalysisGroupBuildingListParameter> {

    private final AnalysisGroupBuildingListServiceDaoImpl analysisGroupBuildingListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisGroupBuildingListDao() {
        analysisGroupBuildingListServiceDaoImpl = new AnalysisGroupBuildingListServiceDaoImpl();
    }

    @Override
    public AnalysisGroupBuildingListResult query(AnalysisGroupBuildingListParameter parameter) throws Exception {

        AnalysisGroupBuildingListResult result = new AnalysisGroupBuildingListResult();

        AnalysisAllBuildingListResultData param = new AnalysisAllBuildingListResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setParentGroupId(parameter.getParentGroupId());
        param.setChildGroupId(parameter.getChildGroupId());

        List<AnalysisAllBuildingListResultData> resultList = getResultList(analysisGroupBuildingListServiceDaoImpl,param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setTargetBuildingList(resultList);

        return result;
    }

}
