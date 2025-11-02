package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterActionModeSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterActionModeSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterActionModeSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A210012Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * Aiel Master 動作モード（取得） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AielMasterActionModeSelectBean")
@RequestScoped
public class AielMasterActionModeSelectBean extends AbstractApiBean<AielMasterActionModeSelectResult, AielMasterActionModeSelectParameter> {

    @EJB
    private AielMasterActionModeSelectDao dao;

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
    protected <T extends BaseParam> T initParam(AielMasterActionModeSelectParameter parameter) {
        A210012Param param = new A210012Param();

        if (param != null) {
            param.setUpdateDBflg(parameter.isUpdateDBflg());
            param.setSelectDate(DateUtility.changeDateFormat(dao.getServerDateTime(), DateUtility.DATE_FORMAT_YYMMDDHHMM));
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
        if (!super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        return true;
    }



}
