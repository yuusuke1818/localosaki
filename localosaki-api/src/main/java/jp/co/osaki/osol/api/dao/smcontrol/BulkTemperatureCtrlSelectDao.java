package jp.co.osaki.osol.api.dao.smcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.BulkTemperatureCtrlSelectDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.mng.param.A200041Param;

/**
 *
 * 複数建物・テナント一括 温度制御(取得) Dao クラス.
 *
 * @author f_takemura
 *
 */
@Stateless
public class BulkTemperatureCtrlSelectDao extends BaseSmControlDao {

    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;

    public BulkTemperatureCtrlSelectDao() {
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
    }

    /**
     * 建物ポイント情報を取得する
     * @param corpId
     * @param parameterList
     * @param smPrmList
     * @param
     * @return
     */
    public List<BulkTemperatureCtrlSelectDetailResultData> getBuildingPointInfo(String corpId,
            List<A200041Param> parameterList) {

        List<BulkTemperatureCtrlSelectDetailResultData> resultList = new ArrayList<>();

        for (A200041Param parameter : parameterList) {
            BulkTemperatureCtrlSelectDetailResultData result = new BulkTemperatureCtrlSelectDetailResultData();
            if (OsolConstants.FLG_ON.equals(parameter.getGetCurrentTemperatureFlg())) {
                //現在温度取得フラグ要の場合のみ取得
                DemandBuildingSmPointListDetailResultData buildingParam = new DemandBuildingSmPointListDetailResultData();
                buildingParam.setCorpId(corpId);
                buildingParam.setBuildingId(parameter.getBuildingId());
                buildingParam.setSmId(parameter.getSmId());
                List<DemandBuildingSmPointListDetailResultData> buildingList = getResultList(
                        demandBuildingSmPointListServiceDaoImpl, buildingParam);
                result.setPointList(buildingList);
            } else {
                //データが取得できなかった場合は1件、機器情報をセットする
                DemandBuildingSmPointListDetailResultData pointData = new DemandBuildingSmPointListDetailResultData();
                pointData.setSmId(parameter.getSmId());
                List<DemandBuildingSmPointListDetailResultData> newPointList = new ArrayList<>();
                newPointList.add(pointData);
                result.setPointList(newPointList);
            }
            resultList.add(result);
        }

        return resultList;
    }

}
