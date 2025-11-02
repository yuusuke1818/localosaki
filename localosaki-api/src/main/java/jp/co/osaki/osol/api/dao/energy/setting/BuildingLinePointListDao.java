package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLinePointListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLinePointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLinePointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLinePointListLineResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLinePointServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物系統ポイント一覧
 *
 * @author t_hirata
 */
@Stateless
public class BuildingLinePointListDao extends OsolApiDao<BuildingLinePointListParameter> {

    //TODO EntityServiceDaoを使わない
    private final MSmLinePointServiceDaoImpl smLinePointServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingLinePointListDao() {
        smLinePointServiceDaoImpl = new MSmLinePointServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingLinePointListResult query(BuildingLinePointListParameter parameter) throws Exception {
        BuildingLinePointListResult result = new BuildingLinePointListResult();
        List<BuildingLinePointListDetailResultData> detailList = new ArrayList<>();

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
            return new BuildingLinePointListResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(parameter.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new BuildingLinePointListResult();
        }

        //機器ポイントを取得する
        SmPointListDetailResultData paramSmPoint = new SmPointListDetailResultData();
        paramSmPoint.setSmId(parameter.getSmId());
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, paramSmPoint);
        if (smPointList != null && !smPointList.isEmpty()) {
            for (SmPointListDetailResultData smPoint : smPointList) {
                BuildingLinePointListDetailResultData detail = new BuildingLinePointListDetailResultData();
                detail.setPointNo(smPoint.getPointNo());
                detail.setPointType(smPoint.getPointType());
                detail.setDmCorrectionFactor(smPoint.getDmCorrectionFactor());
                detail.setAnalogOffSetValue(smPoint.getAnalogOffSetValue());
                detail.setAnalogConversionFactor(smPoint.getAnalogConversionFactor());
                detail.setVersionSmPoint(smPoint.getVersion());
                //建物機器ポイントを取得する
                DemandBuildingSmPointListDetailResultData paramBuildingPoint = new DemandBuildingSmPointListDetailResultData();
                //DemandBuildingSmPointListResult paramBuildingPoint = new DemandBuildingSmPointListResult();
                paramBuildingPoint.setCorpId(parameter.getOperationCorpId());
                paramBuildingPoint.setBuildingId(parameter.getBuildingId());
                paramBuildingPoint.setSmId(parameter.getSmId());
                paramBuildingPoint.setPointNo(smPoint.getPointNo());
                List<DemandBuildingSmPointListDetailResultData> buildingPointList = getResultList(
                        demandBuildingSmPointListServiceDaoImpl, paramBuildingPoint);
                if (buildingPointList != null && buildingPointList.size() == 1) {
                    detail.setPointName(buildingPointList.get(0).getPointName());
                    detail.setPointUnit(buildingPointList.get(0).getPointUnit());
                    detail.setPointSumFlg(buildingPointList.get(0).getPointSumFlg());
                    detail.setDelFlg(buildingPointList.get(0).getDelFlg());
                    detail.setVersionBuildingSmPoint(buildingPointList.get(0).getVersion());
                }

                //系統情報を取得する
                List<BuildingLinePointListLineResultData> lineDetailList = new ArrayList<>();
                LineListDetailResultData paramLine = new LineListDetailResultData();
                paramLine.setCorpId(parameter.getOperationCorpId());
                paramLine.setLineGroupId(parameter.getLineGroupId());
                List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, paramLine);
                if (lineList != null && !lineList.isEmpty()) {
                    for (LineListDetailResultData line : lineList) {
                        BuildingLinePointListLineResultData lineDetail = new BuildingLinePointListLineResultData();
                        lineDetail.setLineNo(line.getLineNo());
                        lineDetail.setLineName(line.getLineName());
                        lineDetail.setInputEnableFlg(line.getInputEnableFlg());
                        //機器系統ポイント情報を取得する
                        MSmLinePoint paramLinePoint = new MSmLinePoint();
                        MSmLinePointPK pkParamLinePoint = new MSmLinePointPK();
                        pkParamLinePoint.setCorpId(parameter.getOperationCorpId());
                        pkParamLinePoint.setBuildingId(parameter.getBuildingId());
                        pkParamLinePoint.setLineGroupId(parameter.getLineGroupId());
                        pkParamLinePoint.setLineNo(line.getLineNo());
                        pkParamLinePoint.setSmId(parameter.getSmId());
                        pkParamLinePoint.setPointNo(smPoint.getPointNo());
                        paramLinePoint.setId(pkParamLinePoint);
                        MSmLinePoint linePointData = find(smLinePointServiceDaoImpl, paramLinePoint);
                        if (linePointData != null) {
                            lineDetail.setPointCalcType(linePointData.getPointCalcType());
                            lineDetail.setComment(linePointData.getComment());
                            lineDetail.setVersion(linePointData.getVersion());
                            lineDetail.setDelFlg(linePointData.getDelFlg());
                        }
                        lineDetailList.add(lineDetail);
                    }
                }

                detail.setSmLinePointList(lineDetailList);
                detailList.add(detail);
            }
        }

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setSmId(exSmList.get(0).getSmId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setSmVersion(exSmList.get(0).getVersion());
        result.setLineGroupId(parameter.getLineGroupId());
        result.setPointList(detailList);
        return result;
    }

}
