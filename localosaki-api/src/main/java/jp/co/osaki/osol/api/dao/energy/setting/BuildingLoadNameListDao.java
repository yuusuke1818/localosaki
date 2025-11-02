package jp.co.osaki.osol.api.dao.energy.setting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLoadNameListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLoadNameListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLoadNameListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物負荷名称設定一覧取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingLoadNameListDao extends OsolApiDao<BuildingLoadNameListParameter> {

    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmControlLoadListServiceDaoImpl smControlLoadListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingLoadNameListDao() {
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smControlLoadListServiceDaoImpl = new SmControlLoadListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingLoadNameListResult query(BuildingLoadNameListParameter parameter) throws Exception {
        BuildingLoadNameListResult result = new BuildingLoadNameListResult();
        List<BuildingLoadNameListDetailResultData> detailList = new ArrayList<>();

        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            //選択中企業が設定されている場合は、そちらを優先
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
            return new BuildingLoadNameListResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(parameter.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new BuildingLoadNameListResult();
        }

        //建物機器データを取得する
        DemandBuildingSmListDetailResultData paramBuilding = new DemandBuildingSmListDetailResultData();
        paramBuilding.setCorpId(parameter.getOperationCorpId());
        paramBuilding.setBuildingId(parameter.getBuildingId());
        paramBuilding.setSmId(parameter.getSmId());
        List<DemandBuildingSmListDetailResultData> buildingList = getResultList(demandBuildingSmListServiceDaoImpl,
                paramBuilding);
        if (buildingList == null || buildingList.size() != 1) {
            return new BuildingLoadNameListResult();
        }

        //負荷制御出力数分、処理を繰り返す
        for (int i = 1; i <= buildingList.get(0).getLoadControlOutput(); i++) {
            BuildingLoadNameListDetailResultData detail = new BuildingLoadNameListDetailResultData();
            detail.setControlLoad(i);
            //製品制御負荷情報を取得する
            ProductControlLoadListDetailResultData paramProduct = new ProductControlLoadListDetailResultData();
            paramProduct.setProductCd(buildingList.get(0).getProductCd());
            paramProduct.setControlLoadFrom(new BigDecimal(i));
            paramProduct.setControlLoadTo(new BigDecimal(i));
            List<ProductControlLoadListDetailResultData> productList = getResultList(
                    productControlLoadListServiceDaoImpl,
                    paramProduct);
            if (productList == null || productList.size() != 1) {
                detail.setControlLoadCircuit(null);
            } else {
                detail.setControlLoadCircuit(productList.get(0).getControlLoadCircuit());
            }
            //機器制御負荷情報を取得する
            SmControlLoadListDetailResultData paramSm = new SmControlLoadListDetailResultData();
            paramSm.setSmId(parameter.getSmId());
            paramSm.setControlLoadFrom(new BigDecimal(i));
            paramSm.setControlLoadTo(new BigDecimal(i));
            List<SmControlLoadListDetailResultData> smList = getResultList(smControlLoadListServiceDaoImpl, paramSm);
            if (smList == null || smList.size() != 1) {
                detail.setControlLoadName(null);
                detail.setControlLoadMemo(null);
                detail.setControlLoadShutOffTime(null);
                detail.setControlLoadShutOffRank(null);
                detail.setControlLoadShutOffCapacity(null);
                detail.setDelFlg(null);
                detail.setVersion(null);
            } else {
                detail.setControlLoadName(smList.get(0).getControlLoadName());
                detail.setControlLoadMemo(smList.get(0).getControlLoadMemo());
                detail.setControlLoadShutOffTime(smList.get(0).getControlLoadShutOffTime());
                detail.setControlLoadShutOffRank(smList.get(0).getControlLoadShutOffRank());
                detail.setControlLoadShutOffCapacity(smList.get(0).getControlLoadShutOffCapacity());
                detail.setDelFlg(smList.get(0).getDelFlg());
                detail.setVersion(smList.get(0).getVersion());
            }
            detailList.add(detail);
        }

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setSmId(exSmList.get(0).getSmId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setSmVersion(exSmList.get(0).getVersion());
        result.setDetailList(detailList);

        return result;
    }

}
