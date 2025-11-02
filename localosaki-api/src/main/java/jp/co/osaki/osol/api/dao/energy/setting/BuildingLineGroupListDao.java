package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineGroupListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineGroupListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineGroupListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物系統グループ一覧取得 Daoクラス
 * @author t_hirata
 */
@Stateless
public class BuildingLineGroupListDao extends OsolApiDao<BuildingLineGroupListParameter> {

    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingLineGroupListDao() {
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public BuildingLineGroupListResult query(BuildingLineGroupListParameter parameter) throws Exception {
        BuildingLineGroupListResult result = new BuildingLineGroupListResult();
        BuildingLineGroupListDetailResultData detail = new BuildingLineGroupListDetailResultData();
        List<CorpLineListDetailResultData> lineList = new ArrayList<>();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
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
            return new BuildingLineGroupListResult();
        }

        //企業標準系統情報を取得する
        List<CorpLineListDetailResultData> corpLineList = getLineGroupList(
                parameter.getOperationCorpId(),
                ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal(),
                null);

        if (corpLineList != null && corpLineList.size() == 1) {
            lineList.add(corpLineList.get(0));
        }

        //建物系統を取得する
        List<CorpLineListDetailResultData> buildingLineList = getLineGroupList(parameter.getOperationCorpId(),
                ApiGenericTypeConstants.LINE_GROUP_TYPE.INDIVIDUAL.getVal(), parameter.getBuildingId());

        if (buildingLineList != null && !buildingLineList.isEmpty()) {
            lineList.addAll(buildingLineList);
        }

        detail.setLineList(lineList);
        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setDetail(detail);

        return result;
    }

    /**
     * 指定の系統グループ情報を取得する
     * @param loginUserId
     * @param loginPersonId
     * @param loginUserCorpId
     * @param corpId
     * @param lineGroupType
     * @param buildingId
     * @return
     */
    private List<CorpLineListDetailResultData> getLineGroupList(String corpId,
            String lineGroupType,
            Long buildingId) throws Exception {
        List<CorpLineListDetailResultData> ret = new ArrayList<>();

        // 系統グループ情報を取得
        LineGroupSearchDetailResultData param = new LineGroupSearchDetailResultData();
        param.setCorpId(corpId);
        param.setLineGroupType(lineGroupType);
        param.setBuildingId(buildingId);
        List<LineGroupSearchDetailResultData> _ret = getResultList(lineGroupSearchServiceDaoImpl, param);
        if (_ret != null) {
            for (LineGroupSearchDetailResultData _rs : _ret) {
                CorpLineListDetailResultData lineListBaseResultSet = new CorpLineListDetailResultData();
                lineListBaseResultSet.setLineGroupId(_rs.getLineGroupId());
                lineListBaseResultSet.setLineGroupName(_rs.getLineGroupName());
                lineListBaseResultSet.setLineGroupType(_rs.getLineGroupType());
                lineListBaseResultSet.setInitialViewFlg(_rs.getInitialViewFlg());
                lineListBaseResultSet.setDelFlg(_rs.getDelFlg());
                lineListBaseResultSet.setLineGroupVersion(_rs.getGroupVersion());

                // 系統情報を取得
                LineListDetailResultData _param = new LineListDetailResultData();
                _param.setCorpId(corpId);
                _param.setLineGroupId(_rs.getLineGroupId());
                List<LineListDetailResultData> __ret = getResultList(lineListServiceDaoImpl, _param);
                if (__ret != null) {
                    List<LineListDetailResultData> lineList = new ArrayList<>();
                    for (LineListDetailResultData __rs : __ret) {
                        if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(__rs.getLineNo())) {
                            // 全体
                            LineListDetailResultData lineAll = new LineListDetailResultData();
                            lineAll.setLineNo(__rs.getLineNo());
                            lineAll.setLineType(__rs.getLineType());
                            lineAll.setLineName(__rs.getLineName());
                            lineAll.setLineTarget(__rs.getLineTarget());
                            lineAll.setLineEnableFlg(__rs.getLineEnableFlg());
                            lineAll.setDelFlg(__rs.getDelFlg());
                            lineAll.setVersion(__rs.getVersion());
                            lineListBaseResultSet.setLineAll(lineAll);
                        } else if (ApiCodeValueConstants.LINE_TARGET.ETC.getVal().equals(__rs.getLineNo())) {
                            // その他
                            LineListDetailResultData lineEtc = new LineListDetailResultData();
                            lineEtc.setLineNo(__rs.getLineNo());
                            lineEtc.setLineType(__rs.getLineType());
                            lineEtc.setLineName(__rs.getLineName());
                            lineEtc.setLineTarget(__rs.getLineTarget());
                            lineEtc.setLineEnableFlg(__rs.getLineEnableFlg());
                            lineEtc.setDelFlg(__rs.getDelFlg());
                            lineEtc.setVersion(__rs.getVersion());
                            lineListBaseResultSet.setLineEtc(lineEtc);
                        } else {
                            lineList.add(__rs);
                        }
                    }
                    // ソート
                    lineList.sort(
                            (LineListDetailResultData rs1, LineListDetailResultData rs2) -> rs1.getLineNo()
                                    .compareTo(rs2.getLineNo()));

                    lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                        Integer l1 = Integer.parseInt(rs1.getLineNo());
                        Integer l2 = Integer.parseInt(rs2.getLineNo());
                        return l1.compareTo(l2);
                    });

                    lineListBaseResultSet.setLineList(lineList);
                }
                ret.add(lineListBaseResultSet);
            }
        }

        // ソート
        ret.sort(
                (CorpLineListDetailResultData rs1, CorpLineListDetailResultData rs2) -> rs1.getLineGroupId()
                        .compareTo(rs2.getLineGroupId()));

        return ret;
    }

}
