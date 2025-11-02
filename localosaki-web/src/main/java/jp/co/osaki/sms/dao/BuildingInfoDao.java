package jp.co.osaki.sms.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.BuildingInfoDaoImpl;

/**
 * 建物情報取得Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class BuildingInfoDao extends SmsDao {

    private final BuildingInfoDaoImpl buildingInfoDaoImpl;

    public BuildingInfoDao() {
        buildingInfoDaoImpl = new BuildingInfoDaoImpl();
    }

    /**
     * 建物情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 建物情報
     */
    public TBuilding getBuildingInfo(String corpId, Long buildingId) {
        TBuildingPK tBuildingPk = new TBuildingPK();
        tBuildingPk.setCorpId(corpId);
        tBuildingPk.setBuildingId(buildingId);

        TBuilding tBuilding = new TBuilding();
        tBuilding.setId(tBuildingPk);
        return find(buildingInfoDaoImpl, tBuilding);
    }
}
