package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ScheduleSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ScheduleSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.ScheduleSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200004Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * スケジュール(取得) Bean クラス.
 *
 * @author Takemura
 *
 */
@Named(value = SmControlConstants.SCHEDULE_SELECT)
@RequestScoped
public class ScheduleSelectBean extends AbstractApiBean<ScheduleSelectResult,ScheduleSelectParameter>{

    @EJB
    private ScheduleSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200004Param> res = (FvpCtrlMngResponse<A200004Param>) response;

        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // dao呼出
        dao.updateSchedule(res,super.loginUserId);
    }

    @Override
    protected <T extends BaseParam> T initParam(ScheduleSelectParameter parameter) {
        A200004Param param = new A200004Param();

        if(parameter!=null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setPageAssignment(parameter.getPageAssignment());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

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
            String pageAssignment = ((A200004Param)param).getPageAssignment();
            //桁数チェックとnullチェックを行う
            if(pageAssignment==null || !(pageAssignment.matches("[0-9]"))) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "pageAssignment", pageAssignment);
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
            }
        }
        return true;
    }

}
