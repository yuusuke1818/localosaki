package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingSmCountParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingSmCountResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物機器データ件数取得処理 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class BuildingSmCountDao extends OsolApiDao<BuildingSmCountParameter> {

    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public BuildingSmCountDao() {
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingSmCountResult query(BuildingSmCountParameter parameter) throws Exception {
        BuildingSmCountResult result = new BuildingSmCountResult();

        //建物機器情報を取得する
        List<DemandBuildingSmListDetailResultData> resultList;
        DemandBuildingSmListDetailResultData listparam = new DemandBuildingSmListDetailResultData();
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            //設定されている場合は、こちらを優先
            listparam.setCorpId(parameter.getSelectedCorpId());
        } else {
            listparam.setCorpId(parameter.getOperationCorpId());
        }
        listparam.setBuildingId(parameter.getBuildingId());
        resultList = getResultList(demandBuildingSmListServiceDaoImpl, listparam);

        //フィルタ処理を行う
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            result.setCount(0);
        } else {
            result.setCount(resultList.size());
        }
        return result;
    }

}
