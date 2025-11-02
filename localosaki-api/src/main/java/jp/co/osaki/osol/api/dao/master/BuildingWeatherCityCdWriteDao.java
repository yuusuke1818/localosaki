package jp.co.osaki.osol.api.dao.master;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.master.BuildingWeatherCityCdWriteParameter;
import jp.co.osaki.osol.api.result.master.BuildingWeatherCityCdWriteResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MWeatherCityServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MWeatherCity;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 市区町村コード書き込み Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class BuildingWeatherCityCdWriteDao extends OsolApiDao<BuildingWeatherCityCdWriteParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingServiceDaoImpl;
    private final MWeatherCityServiceDaoImpl mWeatherCityServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    public BuildingWeatherCityCdWriteDao() {
        tBuildingServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mWeatherCityServiceDaoImpl = new MWeatherCityServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();

    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingWeatherCityCdWriteResult query(BuildingWeatherCityCdWriteParameter parameter) throws Exception {
        TBuilding exBuilding;
        BuildingWeatherCityCdWriteResult result = new BuildingWeatherCityCdWriteResult();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        exBuilding = buildingExclusiveCheck(parameter);
        if (exBuilding == null) {
            //排他エラー
            throw new OptimisticLockException();
        }

        MWeatherCity searchMwc = new MWeatherCity();
        searchMwc.setCityCd(parameter.getWeatherCityCd());
        MWeatherCity mwc = find(mWeatherCityServiceDaoImpl, searchMwc);

        exBuilding.setMWeatherCity(mwc);

        // 共通項目修正
        exBuilding.setUpdateUserId(loginUserId);
        exBuilding.setUpdateDate(serverDateTime);

        //建物の更新
        merge(tBuildingServiceDaoImpl, exBuilding);

        //更新後のデータの取得
        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingWeatherCityCdWriteResult();
        }

        result.setCorpId(exBuildingList.get(0).getCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        return result;
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingWeatherCityCdWriteParameter parameter) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(parameter.getOperationCorpId());
        pkBuildingParam.setBuildingId(parameter.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(parameter.getBuildingVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exBuilding;
        }
    }

}
