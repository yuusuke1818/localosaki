package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDemandSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物デマンド取得 Resultクラス
 * @author ya-ishida
 *
 */
@Stateless
public class BuildingDemandSelectDao extends OsolApiDao<BuildingDemandSelectParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingDemandSelectDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public BuildingDemandSelectResult query(BuildingDemandSelectParameter parameter) throws Exception {
        BuildingDemandSelectResult result = new BuildingDemandSelectResult();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        //フィルタ処理を行う
        exBuildingList = buildingDataFilterDao.applyDataFilter(exBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingDemandSelectResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = new BuildingDemandListDetailResultData();
        demandParam.setCorpId(parameter.getOperationCorpId());
        demandParam.setBuildingId(parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> resultList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);
        if (resultList == null || resultList.size() != 1) {
            return new BuildingDemandSelectResult();
        } else {
            result.setCorpId(parameter.getOperationCorpId());
            result.setBuildingId(exBuildingList.get(0).getBuildingId());
            result.setBuildingVersion(exBuildingList.get(0).getVersion());
            result.setDetail(resultList.get(0));
        }

        return result;
    }

}
