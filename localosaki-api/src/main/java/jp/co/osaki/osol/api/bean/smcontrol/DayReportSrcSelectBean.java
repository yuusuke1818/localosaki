package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DayReportSrcSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DayReportSrcSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.DayReportSrcSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200063Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 受電日報（取得） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "DayReportSrcSelectBean")
@RequestScoped
public class DayReportSrcSelectBean extends AbstractApiBean<DayReportSrcSelectResult, DayReportSrcSelectParameter> {

    @EJB
    private DayReportSrcSelectDao dayReportSrcSelectDao;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#getSmCntrolDao()
     */
    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dayReportSrcSelectDao;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#initParam(jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> T initParam(DayReportSrcSelectParameter parameter) {

        A200063Param param = new A200063Param();

        if(param != null) {
            param.setDaysAgo(parameter.getDaysAgo());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        return (T)param;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#checkSmPrm(jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData, jp.co.osaki.osol.mng.param.BaseParam)
     */
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        // 機種依存チェック(Ea, Ea2以外はエラー)
        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        return true;
    }



}
