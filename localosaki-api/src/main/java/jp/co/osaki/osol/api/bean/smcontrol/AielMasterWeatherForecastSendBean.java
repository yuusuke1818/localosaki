package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterWeatherForecastSendDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterWeatherForecastSendParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterWeatherForecastSendResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A210007Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 気象予報データ送信 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AielMasterWeatherForecastSendBean")
@RequestScoped
public class AielMasterWeatherForecastSendBean extends AbstractApiBean<AielMasterWeatherForecastSendResult, AielMasterWeatherForecastSendParameter>{

    @EJB
    private AielMasterWeatherForecastSendDao dao;

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
    protected <T extends BaseParam> T initParam(AielMasterWeatherForecastSendParameter parameter) {

        A210007Param param = new Gson().fromJson(parameter.getResult(), A210007Param.class);
        param.setUpdateDBflg(parameter.isUpdateDBflg());

        // 送信日時が設定されていないまたは日付が正しくない場合は、現在時刻（hhmmは0固定）を元に送信日時を設定する
        if(CheckUtility.isNullOrEmpty(param.getSendDate()) || DateUtility.conversionDate(param.getSendDate(), DateUtility.DATE_FORMAT_YYMMDDHHMM) == null) {
            param.setSendDate(DateUtility.changeDateFormat(
                    DateUtility.conversionDate(DateUtility.changeDateFormat(
                            dao.getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYMMDDHHMM));
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



}
