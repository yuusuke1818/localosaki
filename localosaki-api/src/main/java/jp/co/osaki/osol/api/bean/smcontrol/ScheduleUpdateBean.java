package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ScheduleUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ScheduleUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.ScheduleUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200005Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * スケジュール(設定) Bean クラス
 *
 * @autho Sakamoto
 *
 */
@Named(value = SmControlConstants.SCHEDULE_UPDATE)
@RequestScoped
public class ScheduleUpdateBean extends AbstractApiBean<ScheduleUpdateResult,ScheduleUpdateParameter>{

    @EJB
    private ScheduleUpdateDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(ScheduleUpdateParameter parameter) {
        A200005Param param = new Gson().fromJson(parameter.getResult(), A200005Param.class);

        param.setPageAssignment(parameter.getPageAssignment());

        param.setUpdateDBflg(parameter.isUpdateDBflg());

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    //機種依存チェック(FV2、FVPa(D)、FVPa(G2)以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if(!super.isFV2(smPrm)
                && !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        //FVPa(G2)機種依存項目チェック
        if (super.isFVPaG2(smPrm))
        {
            String pageAssignment = ((A200005Param)param).getPageAssignment();
            //桁数チェックとnullチェックを行う
            if(pageAssignment==null || !(pageAssignment.matches("[0-9]"))) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "pageAssignment", pageAssignment);
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
            }
        }

        //listサイズチェック
        List<Map<String, Object>> loadList = ((A200005Param)param).getLoadList();

        if(super.isFV2(smPrm) && !(loadList.size() == 6)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isFVPaD(smPrm) && !(loadList.size() == 12)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isFVPaG2(smPrm) && !(loadList.size() == 12)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }
        if(super.isFV2(smPrm) || super.isFVPaD(smPrm) || super.isFVPaG2(smPrm)) {
            for(Map<String, Object> load : loadList) {
                // 入れ子リスト取得
                Object strSettingMonthScheduleList = load.get("settingMonthScheduleList");
                List<Map<String,String>> mmScheduleList =
                        new Gson().fromJson(String.valueOf(strSettingMonthScheduleList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                if(mmScheduleList.size() != SmControlConstants.SCHEDULE_LOAD_LIST_MM_SCHEDULELIST) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "mmScheduleList.size()", String.valueOf(mmScheduleList.size()));
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }
            }
        }

        //FVPa(D)、FVPa(G2)機種依存項目チェック
        if (super.isFVPaD(smPrm) || super.isFVPaG2(smPrm)) {
            //list内の機種依存の項目をバリデーションチェック
            List<Map<String, String>> list = ((A200005Param)param).getSettingSchedulePatternList();

            for(Map<String, String> map : list) {

                String dutySelect = map.get("dutySelect");
                //桁数チェックとnullチェックを行う
                if(dutySelect==null || !(dutySelect.matches("[0-9A-Za-z]"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "dutySelect", dutySelect);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }
            }
        }

        return true;
    }


    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200005Param> res = (FvpCtrlMngResponse<A200005Param>) response;

        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // dao呼出
        dao.updateSchedule(res,super.loginUserId);
    }

}
