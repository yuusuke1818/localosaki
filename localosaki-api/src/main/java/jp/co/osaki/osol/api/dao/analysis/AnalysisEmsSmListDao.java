package jp.co.osaki.osol.api.dao.analysis;

import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsSmListParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsSmListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsSmListResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;

/**
 *
 * 集計・分析 EMS実績 対象機器取得 Daoクラス
 *
 * @author y-maruta
 *
 */
@Stateless
public class AnalysisEmsSmListDao extends OsolApiDao<AnalysisEmsSmListParameter> {

    private final AnalysisEmsSmListServiceDaoImpl analysisEmsSmListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisEmsSmListDao() {
        analysisEmsSmListServiceDaoImpl = new AnalysisEmsSmListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
    }

    @Override
    public AnalysisEmsSmListResult query(AnalysisEmsSmListParameter parameter) throws Exception {

        AnalysisEmsSmListResult result = new AnalysisEmsSmListResult();

        AnalysisEmsSmListResultData param = new AnalysisEmsSmListResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());

        List<AnalysisEmsSmListResultData> resultList = getResultList(analysisEmsSmListServiceDaoImpl, param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        //各企業のポイント情報取得
        for (AnalysisEmsSmListResultData smResult : resultList) {

            DemandBuildingSmPointListDetailResultData pointParam = new DemandBuildingSmPointListDetailResultData();
            pointParam.setCorpId(smResult.getCorpId());
            pointParam.setBuildingId(smResult.getBuildingId());
            pointParam.setSmId(smResult.getSmId());
            List<DemandBuildingSmPointListDetailResultData> pointList = getResultList(
                    demandBuildingSmPointListServiceDaoImpl, pointParam);

            //ソート
            pointList.sort(Comparator.comparing(o -> o.getPointNo()));
            //SRCを先頭に持ってくる
            DemandBuildingSmPointListDetailResultData tmpPoint = null;
            for(DemandBuildingSmPointListDetailResultData point :pointList) {
                if(point.getPointNo().equals("SRC")) {
                    tmpPoint = point;
                    break;
                }
            }
            if(tmpPoint != null) {
                pointList.remove(tmpPoint);
                pointList.add(0, tmpPoint);
            }

            smResult.setSmPointList(pointList);

        }

        result.setTargetSmList(resultList);

        return result;
    }

}
