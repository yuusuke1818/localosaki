package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkScheduleSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BulkScheduleSelectEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200059Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;

/**
*
* 複数建物・テナント一括 スケジュール(取得)Ea Bean クラス.
*
* @author nishida.t
*/
@Named(value = SmControlConstants.BULK_SCHEDULE_SELECT_EA)
@RequestScoped
public class BulkScheduleSelectEaBean extends AbstructBulkApiBean <BulkScheduleSelectEaResult, BulkSmControlApiParameter> {

    @EJB
    private BulkScheduleSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){

        // 一括スケジュール取得
        List<A200059Param> paramList = new ArrayList<>();

        // リスト作成
        for(Map<String, String> p : parameterList) {

            A200059Param param = new A200059Param();
            String scheduleControlInfo = p.get("scheduleControlInfo");
            String settingChangeHist = p.get("settingChangeHist");
            boolean updateDBflg = Boolean.valueOf(p.get("updateDBflg"));

            // 未設定時、1をセット
            if (CheckUtility.isNullOrEmpty(scheduleControlInfo)) {
                scheduleControlInfo = "1";
            } else {
                scheduleControlInfo = scheduleControlInfo.replaceAll("\"", "");
            }

            // 未設定時、0をセット
            if (CheckUtility.isNullOrEmpty(settingChangeHist)) {
                settingChangeHist = SmControlConstants.SETTING_CHG_HIST_LATEST;
            } else {
                settingChangeHist = settingChangeHist.replaceAll("\"", "");
            }

            // コマンド設定
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)) {
                param.setCommand("V" + settingChangeHist + SmControlConstants.SCHEDULE_CONTROL_INFO_1_CMD);
            } else if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
                param.setCommand("V" + settingChangeHist + SmControlConstants.SCHEDULE_CONTROL_INFO_2_CMD);
            } else {
                // Not Process
            }

            //リクエスト仕様内、固有の値を設定
            param.setScheduleControlInfo(scheduleControlInfo);
            param.setSettingChangeHist(settingChangeHist);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        return paramList;
    }


    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        // 対応機器チェック
        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        String scheduleControlInfo = ((A200059Param)param).getScheduleControlInfo();
        if (!SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)
                && !SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "scheduleControlInfo", scheduleControlInfo);
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        return true;
    }

}
