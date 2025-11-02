package jp.co.osaki.osol.api.utility.energy.setting;

import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;

/**
 * エネルギー使用状況（設定）関連のUtilityクラス
 * @author ya-ishida
 *
 */
public class EnergySettingUtility {

    /**
     * 建物デマンド一覧取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @return
     */
    public static BuildingDemandListDetailResultData getBuildingDemandListParam(String corpId, Long buildingId) {
        BuildingDemandListDetailResultData param = new BuildingDemandListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        return param;
    }

    /**
     * 集計デマンド情報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param aggregateDmId
     * @return
     */
    public static AggregateDmListDetailResultData getAggregateDmListParam(String corpId, Long buildingId,
            Long aggregateDmId) {
        AggregateDmListDetailResultData param = new AggregateDmListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setAggregateDmId(aggregateDmId);
        return param;
    }

    /**
     * 集計デマンド系統情報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param aggregateDmCorpId
     * @param aggregateDmBuildingId
     * @param aggregateDmId
     * @return
     */
    public static AggregateDmLineListDetailResultData getAggregateDmLineListParam(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, String aggregateDmCorpId, Long aggregateDmBuildingId, Long aggregateDmId) {
        AggregateDmLineListDetailResultData param = new AggregateDmLineListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setAggregateDmCorpId(aggregateDmCorpId);
        param.setAggregateDmBuildingId(aggregateDmBuildingId);
        param.setAggregateDmId(aggregateDmId);
        return param;
    }

}
