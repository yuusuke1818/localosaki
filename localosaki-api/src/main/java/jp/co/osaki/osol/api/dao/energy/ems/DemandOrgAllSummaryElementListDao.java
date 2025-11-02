/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAllSummaryElementListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAllSummaryElementListResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgAllSummaryElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;

/**
 * エネルギー使用状況（個別）_建物・全集計要素情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgAllSummaryElementListDao extends OsolApiDao<DemandOrgAllSummaryElementListParameter> {

    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    public DemandOrgAllSummaryElementListDao() {
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    @Override
    public DemandOrgAllSummaryElementListResult query(DemandOrgAllSummaryElementListParameter parameter)
            throws Exception {

        List<DemandOrgAllSummaryElementListDetailResultData> list = new ArrayList<>();
        int i = 1;

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), null, ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);
        //系統番号順にソートする
        lineList = SortUtility.sortLineListByLineNo(lineList, ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        for (LineListDetailResultData line : lineList) {
            if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(line.getLineNo())) {
                //全体の場合
                list.add(new DemandOrgAllSummaryElementListDetailResultData(
                        i,
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        null,
                        null));
            } else if (ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                //ロギングの場合
                list.add(new DemandOrgAllSummaryElementListDetailResultData(
                        i,
                        line.getLineName(),
                        line.getLineUnit(),
                        line.getLineUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        null,
                        null));
            } else {
                //ロギング以外の場合
                list.add(new DemandOrgAllSummaryElementListDetailResultData(
                        i,
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        null,
                        null));
            }
            i++;
        }

        //建物機器ポイント情報を取得する
        DemandBuildingSmPointListDetailResultData buildingSmPointParam = DemandEmsUtility
                .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null, null);
        List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                demandBuildingSmPointListServiceDaoImpl, buildingSmPointParam);
        if (buildingSmPointList != null && !buildingSmPointList.isEmpty()) {
            //機器ID、ポイント番号でソートする
            buildingSmPointList = buildingSmPointList.stream().sorted(Comparator
                    .comparing(DemandBuildingSmPointListDetailResultData::getSmId,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(DemandBuildingSmPointListDetailResultData::getPointNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        for (DemandBuildingSmPointListDetailResultData demandSmPoint : buildingSmPointList) {
            if (ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal().equals(demandSmPoint.getPointSumFlg())) {
                continue;
            } else {
                //機器ポイント情報を取得
                SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo());
                List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);
                if (smPointList == null || smPointList.size() != 1
                        || !ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal()
                                .equals(smPointList.get(0).getPointType())) {
                    continue;
                }
                list.add(new DemandOrgAllSummaryElementListDetailResultData(
                        i,
                        demandSmPoint.getPointName(),
                        demandSmPoint.getPointUnit(),
                        demandSmPoint.getPointUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal(),
                        null,
                        null,
                        null,
                        demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo()));
                i++;
            }
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(buildingDemandListServiceDaoImpl,
                buildingDemandParam);
        if (buildingDemandList != null && buildingDemandList.size() == 1
                && !ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF.getVal()
                        .equals(buildingDemandList.get(0).getOutAirTempDispFlg())) {
            list.add(new DemandOrgAllSummaryElementListDetailResultData(
                    i,
                    ApiSimpleConstants.OUT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal(),
                    null,
                    null,
                    null,
                    null,
                    null));
        }

        //グラフ自動配色を取得する
        if (!list.isEmpty()) {
            List<String> colorList = DemandGraphAutoColorUtility.getGraphAutoColorList(list.size());
            if (colorList.size() == list.size()) {
                //全色取得できた場合のみ設定
                for (int j = 0; j < colorList.size(); j++) {
                    list.get(j).setGraphColor(colorList.get(j));
                }
            }
        }

        return new DemandOrgAllSummaryElementListResult(list);
    }

}
