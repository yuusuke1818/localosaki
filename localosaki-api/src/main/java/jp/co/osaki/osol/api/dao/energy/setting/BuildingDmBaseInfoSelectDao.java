package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmBaseInfoSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmBaseInfoSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物デマンド基本情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class BuildingDmBaseInfoSelectDao extends OsolApiDao<BuildingDmBaseInfoSelectParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public BuildingDmBaseInfoSelectDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    @Override
    public BuildingDmBaseInfoSelectResult query(BuildingDmBaseInfoSelectParameter parameter) throws Exception {

        BuildingDmBaseInfoSelectResult result = new BuildingDmBaseInfoSelectResult();

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
            return new BuildingDmBaseInfoSelectResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDmParam = EnergySettingUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDmList = getResultList(buildingDemandListServiceDaoImpl,
                buildingDmParam);
        if (buildingDmList == null || buildingDmList.size() != 1) {
            return new BuildingDmBaseInfoSelectResult();
        } else {
            result.setBuildingDmDetail(buildingDmList.get(0));
        }

        //集計デマンド情報を取得する
        AggregateDmListDetailResultData aggregateDmParam = EnergySettingUtility
                .getAggregateDmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);
        result.setAggregateDmList(getResultList(aggregateDmListServiceDaoImpl, aggregateDmParam));

        //集計デマンド系統情報を取得する
        AggregateDmLineListDetailResultData aggregateDmLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), null, null, null, null, null);
        result.setAggregateDmLineList(getResultList(aggregateDmLineListServiceDaoImpl, aggregateDmLineParam));

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());

        return result;
    }

}
