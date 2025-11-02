package jp.co.osaki.osol.api.dao.plan;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.plan.PlanExclusiveInfoListParameter;
import jp.co.osaki.osol.api.result.plan.PlanExclusiveInfoListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.plan.PlanExclusiveInfoListBuildingResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;

/**
 * 計画履行情報登録用排他情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class PlanExclusiveInfoListDao extends OsolApiDao<PlanExclusiveInfoListParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;

    public PlanExclusiveInfoListDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public PlanExclusiveInfoListResult query(PlanExclusiveInfoListParameter parameter) throws Exception {

        //企業情報を取得する
        CommonCorpExclusionResult param = new CommonCorpExclusionResult();
        param.setCorpId(parameter.getOperationCorpId());

        List<CommonCorpExclusionResult> corpResultList = getResultList(commonCorpExclusionServiceDaoImpl, param);
        if (corpResultList == null || corpResultList.size() != 1) {
            return new PlanExclusiveInfoListResult();
        }

        //建物情報を取得する
        AllBuildingListDetailResultData buildingParam = new AllBuildingListDetailResultData();
        buildingParam.setCorpId(parameter.getOperationCorpId());
        List<AllBuildingListDetailResultData> tempBuildingList = getResultList(allBuildingListServiceDaoImpl,
                buildingParam);

        if (tempBuildingList == null || tempBuildingList.isEmpty()) {
            return new PlanExclusiveInfoListResult(corpResultList.get(0).getCorpId(),
                    corpResultList.get(0).getVersion(),
                    new ArrayList<>());
        } else {
            List<PlanExclusiveInfoListBuildingResultData> buildingList = new ArrayList<>();
            for (AllBuildingListDetailResultData temp : tempBuildingList) {
                buildingList.add(new PlanExclusiveInfoListBuildingResultData(temp.getCorpId(), temp.getBuildingId(),
                        temp.getVersion()));
            }

            return new PlanExclusiveInfoListResult(corpResultList.get(0).getCorpId(),
                    corpResultList.get(0).getVersion(),
                    buildingList);
        }

    }

}
