package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmBuildingListParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmBuildingListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonPrefectureResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpTargetAlarmBuildingListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonPrefectureServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;

/**
 * 企業目標超過警報建物一覧取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class CorpTargetAlarmBuildingListDao extends OsolApiDao<CorpTargetAlarmBuildingListParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonPrefectureServiceDaoImpl commonPrefectureServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    /** 建物状況：稼働中 */
    private static final int BUILDING_SITUATION_NOW = 1;
    /** 建物状況：稼動終了 */
    private static final int BUILDING_SITUATION_END = 2;
    /** 建物状況：削除済 */
    private static final int BUILDING_SITUATION_DEL = 3;

    public CorpTargetAlarmBuildingListDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonPrefectureServiceDaoImpl = new CommonPrefectureServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public CorpTargetAlarmBuildingListResult query(CorpTargetAlarmBuildingListParameter parameter) throws Exception {
        CorpTargetAlarmBuildingListResult result = new CorpTargetAlarmBuildingListResult();
        List<CorpTargetAlarmBuildingListDetailResultData> detailListResult = new ArrayList<>();

        //稼動状況を判断する時間
        Timestamp serverDateTime = getServerDateTime();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //企業のフィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new CorpTargetAlarmBuildingListResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = new BuildingDemandListDetailResultData();
        demandParam.setCorpId(parameter.getOperationCorpId());
        List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);

        //建物のフィルター処理を行う
        demandList = buildingDataFilterDao.applyDataFilter(demandList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (demandList != null && !demandList.isEmpty()) {
            for (BuildingDemandListDetailResultData demand : demandList) {
                CorpTargetAlarmBuildingListDetailResultData detail = new CorpTargetAlarmBuildingListDetailResultData();
                //排他建物情報を取得する
                CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
                exBuildingParam.setCorpId(demand.getCorpId());
                exBuildingParam.setBuildingId(demand.getBuildingId());
                List<CommonBuildingExclusionResult> exBuildingList = getResultList(
                        commonBuildingExclusionServiceDaoImpl, exBuildingParam);

                if (exBuildingList != null && exBuildingList.size() == 1) {
                    detail.setVersion(exBuildingList.get(0).getVersion());
                }

                //建物デマンド情報を設定する
                detail.setCorpId(demand.getCorpId());
                detail.setBuildingId(demand.getBuildingId());
                detail.setTarget(demand.getCoopTargetAlarmFlg());

                //建物情報を取得する
                AllBuildingListDetailResultData buildingParam = new AllBuildingListDetailResultData();
                buildingParam.setCorpId(demand.getCorpId());
                buildingParam.setBuildingId(demand.getBuildingId());
                List<AllBuildingListDetailResultData> buildingList = getResultList(idBuildingSelectServiceDaoImpl,
                        buildingParam);

                if (buildingList != null && buildingList.size() == 1) {
                    detail.setBuildingNo(buildingList.get(0).getBuildingNo());
                    detail.setBuildingName(buildingList.get(0).getBuildingName());
                    detail.setPrefectureCd(buildingList.get(0).getPrefectureCd());
                    detail.setAddress(buildingList.get(0).getAddress());
                    detail.setAddressBuilding(buildingList.get(0).getAddressBuilding());
                    detail.setNyukyoTypeCd(buildingList.get(0).getNyukyoTypeCd());
                    detail.setBuildingType(buildingList.get(0).getBuildingType());

                    //都道府県名
                    CommonPrefectureResult prefectureParam = new CommonPrefectureResult();
                    prefectureParam.setPrefectureCd(buildingList.get(0).getPrefectureCd());
                    List<CommonPrefectureResult> prefectureList = getResultList(commonPrefectureServiceDaoImpl,
                            prefectureParam);
                    if (prefectureList != null && prefectureList.size() == 1) {
                        detail.setPrefectureName(prefectureList.get(0).getPrefectureName());
                    }

                    // 稼働状況
                    if (buildingList.get(0).getBuildingDelDate() != null) {
                        detail.setStatus(BUILDING_SITUATION_DEL);
                    } else if (buildingList.get(0).getTotalEndYm() == null
                            || buildingList.get(0).getTotalEndYm().compareTo(serverDateTime) >= 0) {
                        detail.setStatus(BUILDING_SITUATION_NOW);
                    } else if (buildingList.get(0).getTotalEndYm() != null
                            && buildingList.get(0).getTotalEndYm().compareTo(serverDateTime) < 0) {
                        detail.setStatus(BUILDING_SITUATION_END);
                    }
                }

                detailListResult.add(detail);
            }
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setCorpVersion(exCorpList.get(0).getVersion());
        result.setDetailList(detailListResult);

        return result;
    }
}
