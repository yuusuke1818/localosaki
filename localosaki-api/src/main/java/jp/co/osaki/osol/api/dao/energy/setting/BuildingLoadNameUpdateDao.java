package jp.co.osaki.osol.api.dao.energy.setting;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLoadNameUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingLoadNameUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLoadNameUpdateResult;
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
import jp.co.osaki.osol.api.servicedao.entity.MSmControlLoadServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物負荷名称設定一覧更新 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingLoadNameUpdateDao extends OsolApiDao<BuildingLoadNameUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;
    private final MSmControlLoadServiceDaoImpl mSmControlLoadServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmControlLoadListServiceDaoImpl smControlLoadListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    public BuildingLoadNameUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
        mSmControlLoadServiceDaoImpl = new MSmControlLoadServiceDaoImpl();
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
    public BuildingLoadNameUpdateResult query(BuildingLoadNameUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;
        MSmPrm exSm;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingLoadNameUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingLoadNameUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingLoadNameUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器情報の排他チェック
        if (resultSet.getSmId() == null) {
            return new BuildingLoadNameUpdateResult();
        } else {
            exSm = smExclusiveCheck(resultSet);
            if (exSm == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器制御負荷情報の更新
        updateSmControlLoadData(resultSet, serverDateTime, loginUserId);

        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);
        //機器情報の更新
        exSm.setUpdateDate(serverDateTime);
        exSm.setUpdateUserId(loginUserId);
        merge(mSmPrmServiceDaoImpl, exSm);

        //更新後の情報を取得する
        return getNewControlLoadData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingLoadNameUpdateRequest result) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(result.getCorpId());
        pkBuildingParam.setBuildingId(result.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(result.getBuildingVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exBuilding;
        }
    }

    /**
     * 機器の排他チェックを行う
     * @param result
     * @return
     */
    private MSmPrm smExclusiveCheck(BuildingLoadNameUpdateRequest result) throws Exception {
        MSmPrm smParam = new MSmPrm();
        smParam.setSmId(result.getSmId());
        MSmPrm exSm = find(mSmPrmServiceDaoImpl, smParam);
        if (exSm == null || !exSm.getVersion().equals(result.getSmVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exSm;
        }
    }

    /**
     * 機器制御負荷情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateSmControlLoadData(BuildingLoadNameUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {

        MSmControlLoad paramLoad;
        MSmControlLoad updateLoad = null;
        MSmControlLoadPK pkParamLoad;
        MSmControlLoadPK pkUpdateLoad;
        Boolean newLoadFlg;

        for (BuildingLoadNameListDetailResultData detail : result.getDetailList()) {
            //機器制御負荷を更新
            paramLoad = new MSmControlLoad();
            pkParamLoad = new MSmControlLoadPK();
            pkParamLoad.setSmId(result.getSmId());
            pkParamLoad.setControlLoad(new BigDecimal(detail.getControlLoad()));
            paramLoad.setId(pkParamLoad);
            updateLoad = find(mSmControlLoadServiceDaoImpl, paramLoad);
            if (updateLoad == null) {
                newLoadFlg = Boolean.TRUE;
                if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                    //新規かつ削除の場合、次のレコードへ
                    continue;
                }
                updateLoad = new MSmControlLoad();
                pkUpdateLoad = new MSmControlLoadPK();
                pkUpdateLoad.setSmId(result.getSmId());
                pkUpdateLoad.setControlLoad(new BigDecimal(detail.getControlLoad()));
                updateLoad.setId(pkUpdateLoad);
                updateLoad.setCreateDate(serverDateTime);
                updateLoad.setCreateUserId(loginUserId);
            } else {
                newLoadFlg = Boolean.FALSE;
            }

            updateLoad.setControlLoadName(detail.getControlLoadName());
            updateLoad.setControlLoadMemo(detail.getControlLoadMemo());
            updateLoad.setControlLoadShutOffTime(detail.getControlLoadShutOffTime());
            updateLoad.setControlLoadShutOffRank(detail.getControlLoadShutOffRank());
            updateLoad.setControlLoadShutOffCapacity(detail.getControlLoadShutOffCapacity());
            updateLoad.setDelFlg(detail.getDelFlg());
            updateLoad.setUpdateDate(serverDateTime);
            updateLoad.setUpdateUserId(loginUserId);

            if (newLoadFlg) {
                persist(mSmControlLoadServiceDaoImpl, updateLoad);
            } else {
                merge(mSmControlLoadServiceDaoImpl, updateLoad);
            }
        }
    }

    /**
     * 更新後の負荷情報を取得する
     * @param result
     * @return
     */
    private BuildingLoadNameUpdateResult getNewControlLoadData(BuildingLoadNameUpdateRequest result) throws Exception {
        BuildingLoadNameUpdateResult newResult = new BuildingLoadNameUpdateResult();
        List<BuildingLoadNameListDetailResultData> detailList = new ArrayList<>();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingLoadNameUpdateResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(result.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new BuildingLoadNameUpdateResult();
        }

        //建物機器データを取得する
        DemandBuildingSmListDetailResultData paramBuilding = new DemandBuildingSmListDetailResultData();
        paramBuilding.setCorpId(result.getCorpId());
        paramBuilding.setBuildingId(result.getBuildingId());
        paramBuilding.setSmId(result.getSmId());
        List<DemandBuildingSmListDetailResultData> buildingList = getResultList(demandBuildingSmListServiceDaoImpl,
                paramBuilding);
        if (buildingList == null || buildingList.size() != 1) {
            return new BuildingLoadNameUpdateResult();
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
            paramSm.setSmId(result.getSmId());
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

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setSmId(exSmList.get(0).getSmId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setSmVersion(exSmList.get(0).getVersion());
        newResult.setDetailList(detailList);

        return newResult;
    }

}
