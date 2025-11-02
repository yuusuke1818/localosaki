package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingTargetAlarmLineListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingTargetAlarmLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmLineListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmTimeServiceDaoImpl;
import jp.co.osaki.osol.entity.MLineTargetAlarm;
import jp.co.osaki.osol.entity.MLineTargetAlarmPK;
import jp.co.osaki.osol.entity.MLineTargetAlarmTime;
import jp.co.osaki.osol.entity.MLineTargetAlarmTimePK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物目標超過警報系統一覧取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class BuildingTargetAlarmLineListDao extends OsolApiDao<BuildingTargetAlarmLineListParameter> {

    //TODO 参照系でEntityServiceDaoを使わない
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final MLineTargetAlarmServiceDaoImpl lineTargetAlarmServiceDaoImpl;
    private final MLineTargetAlarmTimeServiceDaoImpl lineTargetAlarmTimeServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingTargetAlarmLineListDao() {
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        lineTargetAlarmServiceDaoImpl = new MLineTargetAlarmServiceDaoImpl();
        lineTargetAlarmTimeServiceDaoImpl = new MLineTargetAlarmTimeServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingTargetAlarmLineListResult query(BuildingTargetAlarmLineListParameter parameter) throws Exception {
        BuildingTargetAlarmLineListResult result = new BuildingTargetAlarmLineListResult();
        List<BuildingTargetAlarmLineListDetailResultData> detailList = new ArrayList<>();

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
            return new BuildingTargetAlarmLineListResult();
        }

        //系統情報を取得する
        LineListDetailResultData paramLine = new LineListDetailResultData();
        paramLine.setCorpId(parameter.getOperationCorpId());
        paramLine.setLineGroupId(parameter.getLineGroupId());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, paramLine);
        if (lineList != null && !lineList.isEmpty()) {
            // ソート
            lineList.sort((LineListDetailResultData o1, LineListDetailResultData o2) -> {
                Long no1 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(o1.getLineTarget())
                        ? Long.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(o1.getLineTarget()) ? Long.MAX_VALUE
                                : Long.parseLong(o1.getLineNo());
                Long no2 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(o2.getLineTarget())
                        ? Long.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(o2.getLineTarget()) ? Long.MAX_VALUE
                                : Long.parseLong(o2.getLineNo());
                return no1.compareTo(no2);
            });

            for (LineListDetailResultData line : lineList) {
                BuildingTargetAlarmLineListDetailResultData detail = new BuildingTargetAlarmLineListDetailResultData();
                List<BuildingTargetAlarmLineListTimeResultData> timeDetailList = new ArrayList<>();
                detail.setLineNo(line.getLineNo());
                detail.setLineName(line.getLineName());
                //建物系統目標超過警報情報を取得する
                MLineTargetAlarm paramAlarm = new MLineTargetAlarm();
                MLineTargetAlarmPK pkParamAlarm = new MLineTargetAlarmPK();
                pkParamAlarm.setCorpId(parameter.getOperationCorpId());
                pkParamAlarm.setBuildingId(parameter.getBuildingId());
                pkParamAlarm.setLineGroupId(parameter.getLineGroupId());
                pkParamAlarm.setLineNo(line.getLineNo());
                paramAlarm.setId(pkParamAlarm);
                MLineTargetAlarm alarm = find(lineTargetAlarmServiceDaoImpl, paramAlarm);
                if (alarm == null) {
                    //取得できない場合は次のレコードへ
                    detail.setLineOverRate(null);
                    detail.setOverRateDetectFlg(null);
                    detail.setContinueDays(null);
                    detail.setDelFlg(null);
                    detail.setVersion(null);
                    detail.setTimeList(new ArrayList<>());
                    detailList.add(detail);
                    continue;
                }
                detail.setLineOverRate(alarm.getLineOverRate());
                detail.setOverRateDetectFlg(alarm.getOverRateDetectFlg());
                detail.setContinueDays(alarm.getContinueDays());
                detail.setDelFlg(alarm.getDelFlg());
                detail.setVersion(alarm.getVersion());
                //建物系統目標超過警報時間を取得する
                MLineTargetAlarmTime paramTime = new MLineTargetAlarmTime();
                MLineTargetAlarmTimePK pkParamTime = new MLineTargetAlarmTimePK();
                pkParamTime.setCorpId(parameter.getOperationCorpId());
                pkParamTime.setBuildingId(parameter.getBuildingId());
                pkParamTime.setLineGroupId(parameter.getLineGroupId());
                pkParamTime.setLineNo(line.getLineNo());
                paramTime.setId(pkParamTime);
                List<MLineTargetAlarmTime> timeList = getResultList(lineTargetAlarmTimeServiceDaoImpl, paramTime);
                if (timeList == null || timeList.isEmpty()) {
                    detail.setTimeList(new ArrayList<>());
                } else {
                    for (MLineTargetAlarmTime time : timeList) {
                        BuildingTargetAlarmLineListTimeResultData timeDetail = new BuildingTargetAlarmLineListTimeResultData();
                        timeDetail.setJigenNo(time.getId().getJigenNo());
                        timeDetail.setDetectFlg(time.getDetectFlg());
                        timeDetail.setVersion(time.getVersion());
                        timeDetail.setDelFlg(time.getDelFlg());
                        timeDetailList.add(timeDetail);
                    }
                    detail.setTimeList(timeDetailList);
                }
                detailList.add(detail);
            }
        }

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setLineGroupId(parameter.getLineGroupId());
        result.setDetailList(detailList);
        return result;
    }

}
