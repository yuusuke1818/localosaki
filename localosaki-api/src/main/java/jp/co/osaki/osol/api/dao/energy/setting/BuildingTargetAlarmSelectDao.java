package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingTargetAlarmSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingTargetAlarmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmSelectDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingTargetAlarm;
import jp.co.osaki.osol.entity.MBuildingTargetAlarmPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物目標超過警報取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingTargetAlarmSelectDao extends OsolApiDao<BuildingTargetAlarmSelectParameter> {

    //TODO 参照系にEntityServiceDaoクラスの禁止
    private final MBuildingTargetAlarmServiceDaoImpl mBuildingTargetAlarmServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingTargetAlarmSelectDao() {
        mBuildingTargetAlarmServiceDaoImpl = new MBuildingTargetAlarmServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingTargetAlarmSelectResult query(BuildingTargetAlarmSelectParameter parameter) {
        BuildingTargetAlarmSelectResult result = new BuildingTargetAlarmSelectResult();

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
            return new BuildingTargetAlarmSelectResult();
        }

        //建物目標超過警報を取得する
        MBuildingTargetAlarm paramAlarm = new MBuildingTargetAlarm();
        MBuildingTargetAlarmPK pkParamAlarm = new MBuildingTargetAlarmPK();
        pkParamAlarm.setCorpId(parameter.getOperationCorpId());
        pkParamAlarm.setBuildingId(parameter.getBuildingId());
        paramAlarm.setId(pkParamAlarm);
        MBuildingTargetAlarm alarm = find(mBuildingTargetAlarmServiceDaoImpl, paramAlarm);
        if (alarm == null) {
            result.setDetail(new BuildingTargetAlarmSelectDetailResultData());
        } else {
            BuildingTargetAlarmSelectDetailResultData detail = new BuildingTargetAlarmSelectDetailResultData();
            detail.setDetectDayOfWeek(alarm.getDetectDayOfWeek());
            detail.setDetectTime( convTimeToDate(alarm.getDetectTime()));
            detail.setAlertMailAddress1(alarm.getAlertMailAddress1());
            detail.setAlertMailAddress2(alarm.getAlertMailAddress2());
            detail.setAlertMailAddress3(alarm.getAlertMailAddress3());
            detail.setAlertMailAddress4(alarm.getAlertMailAddress4());
            detail.setAlertMailAddress5(alarm.getAlertMailAddress5());
            detail.setAlertMailAddress6(alarm.getAlertMailAddress6());
            detail.setAlertMailAddress7(alarm.getAlertMailAddress7());
            detail.setAlertMailAddress8(alarm.getAlertMailAddress8());
            detail.setAlertMailAddress9(alarm.getAlertMailAddress9());
            detail.setAlertMailAddress10(alarm.getAlertMailAddress10());
            detail.setAlertIntermalMailAddress1(alarm.getAlertIntermalMailAddress1());
            detail.setAlertIntermalMailAddress2(alarm.getAlertIntermalMailAddress2());
            detail.setMailWillSendTime(convTimeToDate(alarm.getMailWillSendTime()));
            detail.setMailLastSendTime(alarm.getMailLastSendTime());
            detail.setMonthlyAlarmFlg(alarm.getMonthlyAlarmFlg());
            detail.setPeriodAlarmFlg(alarm.getPeriodAlarmFlg());
            detail.setYearAlarmFlg(alarm.getYearAlarmFlg());
            detail.setMonthlyAlarmLockDate(alarm.getMonthlyAlarmLockDate());
            detail.setPeriodAlarmLockDate(alarm.getPeriodAlarmLockDate());
            detail.setYearAlarmLockDate(alarm.getYearAlarmLockDate());
            detail.setSharePeriod(alarm.getSharePeriod());
            detail.setTargetKwhMonthlyOverDate(alarm.getTargetKwhMonthlyOverDate());
            detail.setTargetKwhPeriodOverDate(alarm.getTargetKwhPeriodOverDate());
            detail.setTargetKwhYearOverDate(alarm.getTargetKwhYearOverDate());
            detail.setDelFlg(alarm.getDelFlg());
            detail.setVersion(alarm.getVersion());
            result.setDetail(detail);
        }

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());

        return result;
    }

    private Date convTimeToDate (Time time) {
        if(time == null) {
            return null;
        }
        return new Date(time.getTime());
    }

}
