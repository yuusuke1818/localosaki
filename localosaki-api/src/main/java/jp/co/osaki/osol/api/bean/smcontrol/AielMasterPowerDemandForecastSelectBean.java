package jp.co.osaki.osol.api.bean.smcontrol;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterPowerDemandForecastSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterPowerDemandForecastSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterPowerDemandForecastSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A210009Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 需要電力予測データ（取得） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AielMasterPowerDemandForecastSelectBean")
@RequestScoped
public class AielMasterPowerDemandForecastSelectBean
    extends AbstractApiBean<AielMasterPowerDemandForecastSelectResult, AielMasterPowerDemandForecastSelectParameter> {

    @EJB
    private AielMasterPowerDemandForecastSelectDao dao;

    private AielMasterPowerDemandForecastSelectParameter parameter;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#getSmCntrolDao()
     */
    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#initParam(jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter)
     */
    @Override
    protected <T extends BaseParam> T initParam(AielMasterPowerDemandForecastSelectParameter parameter) {

        A210009Param param = new A210009Param();
        this.parameter = parameter;

        if(param != null) {
            if(parameter.getSelectDate() != null) {
                param.setSelectDate(DateUtility.changeDateFormat(parameter.getSelectDate(), DateUtility.DATE_FORMAT_YYMMDD).concat("0000"));
            } else {
                param.setSelectDate(DateUtility.changeDateFormat(dao.getServerDateTime(), DateUtility.DATE_FORMAT_YYMMDD).concat("0000"));
            }
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#checkSmPrm(jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData, jp.co.osaki.osol.mng.param.BaseParam)
     */
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        // Eα2以外はエラー
        if(!super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }
        return true;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#callDao(jp.co.osaki.osol.mng.FvpCtrlMngResponse)
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        //パラメータ取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A210009Param> res = (FvpCtrlMngResponse<A210009Param>) response;

        //param生成
        A210009Param param = (A210009Param)res.getParam();

        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        Timestamp serverDateTime = dao.getServerDateTime();

        if(param.getForecastTimeList() != null && !param.getForecastTimeList().isEmpty()) {
            // 需要電力予測30分値データの入れ替え
            dao.deleteDemandPowerForecastTime(res.getSmId());
            dao.insertDemandPowerForecastTime(this.parameter, res, serverDateTime);
        }

        if(param.getForecastDayList() != null && !param.getForecastDayList().isEmpty()) {
            // 需要電力予測1日値データの入れ替え
            dao.deleteDemandPowerForecastDay(res.getSmId());
            dao.insertDemandPowerForecastDay(this.parameter, res, serverDateTime);
        }
    }

}
