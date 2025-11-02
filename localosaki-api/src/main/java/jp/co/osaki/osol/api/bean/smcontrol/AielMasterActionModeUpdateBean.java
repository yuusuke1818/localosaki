package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterActionModeUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterActionModeUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterActionModeUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A210013Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * Aiel Master 動作モード（設定） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AielMasterActionModeUpdateBean")
@RequestScoped
public class AielMasterActionModeUpdateBean extends AbstractApiBean<AielMasterActionModeUpdateResult, AielMasterActionModeUpdateParameter> {

    @EJB
    private AielMasterActionModeUpdateDao dao;

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
    protected <T extends BaseParam> T initParam(AielMasterActionModeUpdateParameter parameter) {
        A210013Param param = new A210013Param();

        if (param != null) {
            param.setUpdateDBflg(parameter.isUpdateDBflg());
            param.setActionMode(parameter.getActionMode());
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
