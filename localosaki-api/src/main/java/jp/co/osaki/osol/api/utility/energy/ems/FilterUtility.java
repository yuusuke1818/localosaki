/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.energy.ems;

import java.util.List;
import java.util.stream.Collectors;

import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * フィルターユーティリティクラス
 *
 * @author ya-ishida
 */
public class FilterUtility {

    /**
     * デマンドデータ実績取得処理（建物・テナント一覧絞込み・建物単体）
     *
     * @param result
     * @param buildingNo
     * @param buildingName
     * @return
     */
    public static AllBuildingListDetailResultData filterDemandBuildingListByBuilding(AllBuildingListDetailResultData result, String buildingNo, String buildingName) {

        if (!CheckUtility.isNullOrEmpty(buildingNo) && !CheckUtility.isNullOrEmpty(buildingName)) {
            if (result.getBuildingNo().equals(buildingNo) || result.getBuildingName().contains(buildingName)) {
                //建物・テナント番号の完全一致か建物・テナント名の部分一致
                return result;
            } else {
                return null;
            }
        } else if (!CheckUtility.isNullOrEmpty(buildingNo) && CheckUtility.isNullOrEmpty(buildingName)) {
            if (result.getBuildingNo().equals(buildingNo)) {
                //建物・テナント番号の完全一致
                return result;
            } else {
                return null;
            }
        } else if (CheckUtility.isNullOrEmpty(buildingNo) && !CheckUtility.isNullOrEmpty(buildingName)) {
            if (result.getBuildingName().contains(buildingName)) {
                //建物・テナント名の部分一致
                return result;
            } else {
                return null;
            }
        } else {
            return result;
        }
    }

    /**
     * デマンドデータ実績取得処理（建物・テナント一覧絞込み・リストから）
     *
     * @param resultList
     * @param buildingNo
     * @param buildingName
     * @return
     */
    public static List<AllBuildingListDetailResultData> filterDemandBuildingListByBuilding(List<AllBuildingListDetailResultData> resultList, String buildingNo, String buildingName) {

        //単体でチェックした結果nullが返却されていないデータのみフィルタリングする
        resultList = resultList.stream().filter(i -> filterDemandBuildingListByBuilding(i, buildingNo, buildingName) != null).collect(Collectors.toList());

        return resultList;
    }

    /**
     * デマンドデータ実績取得処理（建物・テナント一覧絞込み・建物単体）
     *
     * @param result
     * @param buildingNoOrName
     * @return
     */
    public static AllBuildingListDetailResultData filterDemandBuildingListByBuilding(AllBuildingListDetailResultData result, String buildingNoOrName) {
        if (CheckUtility.isNullOrEmpty(buildingNoOrName)) {
            return result;
        }
        if (result.getBuildingNo().contains(buildingNoOrName)) {
            return result;
        }
        if (result.getBuildingName().contains(buildingNoOrName)) {
            return result;
        }
        return null;
    }
}
