package jp.co.osaki.osol.api.dao.smcontrol;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SettingEventCtrlSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.LinkSettingListServiceDaoImpl;

/**
 *
 * イベント制御設定(取得) Dao クラス.
 *
 * @author t_sakamoto
 *
 */
@Stateless
public class SettingEventCtrlSelectDao extends BaseSmControlDao {

    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final LinkSettingListServiceDaoImpl linkSettingListServiceDaoImpl;

    public SettingEventCtrlSelectDao() {
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        linkSettingListServiceDaoImpl = new LinkSettingListServiceDaoImpl();
    }

    /**
     * 建物ポイント情報と機器空調設定情報を取得する
     * @param corpId
     * @param buildingId
     * @param SmId
     * @param getCurrentTemperatureFlg
     * @return
     */
    public SettingEventCtrlSelectDetailResultData getData(String corpId, Long buildingId, Long smId,
            Integer getCurrentTemperatureFlg) {

        SettingEventCtrlSelectDetailResultData result = new SettingEventCtrlSelectDetailResultData();

        //機器空調設定情報を取得する
        LinkSettingExtractResultData linkParam = new LinkSettingExtractResultData();
        linkParam.setSmId(smId);
        List<LinkSettingExtractResultData> linkList = getResultList(linkSettingListServiceDaoImpl, linkParam);
        result.setLinkSettingExtractList(linkList);

        if (OsolConstants.FLG_ON.equals(getCurrentTemperatureFlg)) {
            //現在温度取得の場合、建物ポイントを取得する
            DemandBuildingSmPointListDetailResultData buildingParam = new DemandBuildingSmPointListDetailResultData();
            buildingParam.setCorpId(corpId);
            buildingParam.setBuildingId(buildingId);
            buildingParam.setSmId(smId);
            List<DemandBuildingSmPointListDetailResultData> buildingList = getResultList(
                    demandBuildingSmPointListServiceDaoImpl, buildingParam);
            result.setPointList(buildingList);
        }

        return result;
    }
}
