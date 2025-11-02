package jp.co.osaki.osol.api.dao.energy.ems;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionJudgeParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionJudgeResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsOrgUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * AI予測表示判定 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class DemandOrgAiPredictionJudgeDao extends OsolApiDao<DemandOrgAiPredictionJudgeParameter> {

    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;

    public DemandOrgAiPredictionJudgeDao() {
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
    }

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgAiPredictionJudgeResult query(DemandOrgAiPredictionJudgeParameter parameter) throws Exception {

        DemandOrgAiPredictionJudgeResult result = new DemandOrgAiPredictionJudgeResult();

        Date targetDate = DateUtility.conversionDate(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);

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
            result.setAiPredictionJudge(Boolean.FALSE);
            result.setSmId(null);
            return result;
        }

        //建物機器情報を取得する
        DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);

        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(demandBuildingSmListServiceDaoImpl,
                buildingSmParam);
        if (buildingSmList == null || buildingSmList.isEmpty()) {
            result.setAiPredictionJudge(Boolean.FALSE);
            result.setEa2NotAielMaster(Boolean.FALSE);
            result.setNotEa2OrMultiSm(Boolean.TRUE);
            result.setSmId(null);
            return result;
        }

        int targetCount = 0;
        Long targetSmId = null;
        boolean targetSmFlg = false;
        boolean notAielMasterFlg = false;
        boolean notEa2OrMultiFlg = false;
        for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
            //機器情報を取得する
            SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(buildingSm.getSmId());
            List<SmSelectResult> smInfoList = getResultList(smSelectResultServiceDaoImpl, smParam);
            if (smInfoList == null || smInfoList.isEmpty()) {
                result.setAiPredictionJudge(Boolean.FALSE);
                result.setEa2NotAielMaster(Boolean.FALSE);
                result.setNotEa2OrMultiSm(Boolean.TRUE);
                result.setSmId(null);
                return result;
            }

            if(smInfoList.get(0).getStartDate() != null && smInfoList.get(0).getStartDate().after(targetDate)) {
                //計測開始日が設定されていて本日より後の場合は対象外
                continue;
            }
            if(smInfoList.get(0).getEndDate() == null || smInfoList.get(0).getEndDate().equals(targetDate) || smInfoList.get(0).getEndDate().after(targetDate)) {
                //計測終了日がNULLまたは本日または本日より後の場合は対象になる
                targetCount++;
                if(targetCount > 1) {
                    //対象となる機器が1台を超えた場合は処理を終了する
                    result.setAiPredictionJudge(Boolean.FALSE);
                    result.setEa2NotAielMaster(Boolean.FALSE);
                    result.setNotEa2OrMultiSm(Boolean.TRUE);
                    result.setSmId(null);
                    return result;
                }
            } else {
                //上記以外は対象外
                continue;
            }

            if(!OsolConstants.PRODUCT_CD.FVP_E_ALPHA2.getVal().equals(smInfoList.get(0).getProductCd())) {
                //Eα2以外の場合は対象外
                notEa2OrMultiFlg = true;
                continue;
            } else {
                if(OsolConstants.FLG_OFF.equals(smInfoList.get(0).getAielMasterConnectFlg())) {
                    //AielMaster接続がOFFの場合は対象外
                    notAielMasterFlg = true;
                    continue;
                } else {
                    targetSmFlg = true;
                    targetSmId = smInfoList.get(0).getSmId();
                }
            }


        }

        result.setAiPredictionJudge(targetSmFlg);
        if(!targetSmFlg && !notAielMasterFlg && !notEa2OrMultiFlg) {
            result.setEa2NotAielMaster(Boolean.FALSE);
            result.setNotEa2OrMultiSm(Boolean.TRUE);
        } else {
            result.setEa2NotAielMaster(notAielMasterFlg);
            result.setNotEa2OrMultiSm(notEa2OrMultiFlg);
        }
        if(targetSmFlg) {
            result.setSmId(targetSmId);
        } else {
            result.setSmId(null);
        }
        return result;
    }


}
