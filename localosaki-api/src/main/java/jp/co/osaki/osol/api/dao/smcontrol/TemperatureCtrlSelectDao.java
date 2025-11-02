package jp.co.osaki.osol.api.dao.smcontrol;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;

/**
 *
 * 温度制御(取得) Dao クラス.
 *
 * @author da_yamano
 *
 */
@Stateless
public class TemperatureCtrlSelectDao extends BaseSmControlDao {

    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;

    public TemperatureCtrlSelectDao() {
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
    }

    public List<DemandBuildingSmPointListDetailResultData> getSmPointData(String corpId, Long buildingId, Long smId) {
        DemandBuildingSmPointListDetailResultData param = new DemandBuildingSmPointListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setSmId(smId);
        return getResultList(demandBuildingSmPointListServiceDaoImpl, param);
    }

}
