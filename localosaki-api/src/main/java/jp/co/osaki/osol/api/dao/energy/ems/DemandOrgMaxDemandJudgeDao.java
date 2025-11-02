/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMaxDemandJudgeParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMaxDemandJudgeResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsOrgUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;

/**
 * 最大デマンド取得可能判定 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgMaxDemandJudgeDao extends OsolApiDao<DemandOrgMaxDemandJudgeParameter> {

    private final static String PRODUCT_CD_FV2 = "00";

    private final static String PRODUCT_CD_FVP = "03";

    private final static String PRODUCT_CD_FVP_ALPHA = "06";

    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public DemandOrgMaxDemandJudgeDao() {
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgMaxDemandJudgeResult query(DemandOrgMaxDemandJudgeParameter parameter) throws Exception {
        DemandOrgMaxDemandJudgeResult result = new DemandOrgMaxDemandJudgeResult();

        //建物・テナント情報を取得する
        AllBuildingListDetailResultData buildingParam = DemandEmsOrgUtility
                .getOrgBuildingInfoParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        buildingParam.setCorpId(parameter.getOperationCorpId());
        buildingParam.setBuildingId(parameter.getBuildingId());
        List<AllBuildingListDetailResultData> buildingList = getResultList(idBuildingSelectServiceDaoImpl,
                buildingParam);
        // フィルター処理
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.size() != 1) {
            result.setMaxDemandJudge(Boolean.FALSE);
            return result;
        }

        //建物機器情報を取得する
        DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);

        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(demandBuildingSmListServiceDaoImpl,
                buildingSmParam);
        if (buildingSmList == null || buildingSmList.isEmpty()) {
            result.setMaxDemandJudge(Boolean.FALSE);
            return result;
        }

        for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
            //機器情報を取得する
            SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(buildingSm.getSmId());
            List<SmSelectResult> smInfoList = getResultList(smSelectResultServiceDaoImpl, smParam);
            if (smInfoList == null || smInfoList.size() != 1) {
                result.setMaxDemandJudge(Boolean.FALSE);
                return result;
            }

            if (!PRODUCT_CD_FV2.equals(smInfoList.get(0).getProductCd())
                    && !PRODUCT_CD_FVP.equals(smInfoList.get(0).getProductCd())
                    && !PRODUCT_CD_FVP_ALPHA.equals(smInfoList.get(0).getProductCd())) {
                //上記いずれの製品にも該当しない場合、最大デマンドは表示しない
                result.setMaxDemandJudge(Boolean.FALSE);
                return result;
            }
        }

        result.setMaxDemandJudge(Boolean.TRUE);
        return result;
    }

}
