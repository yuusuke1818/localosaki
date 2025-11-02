package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.HalfHourReportSrcSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.HalfHourReportSrcSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.HalfHourReportSrcSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200065Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 受電30分計測データ（取得）Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "HalfHourReportSrcSelectBean")
@RequestScoped
public class HalfHourReportSrcSelectBean extends AbstractApiBean<HalfHourReportSrcSelectResult, HalfHourReportSrcSelectParameter> {

    @EJB
    HalfHourReportSrcSelectDao halfHourReportSrcSelectDao;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#getSmCntrolDao()
     */
    @Override
    protected SmCntrolDao getSmCntrolDao() {
        // TODO 自動生成されたメソッド・スタブ
        return halfHourReportSrcSelectDao;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#initParam(jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> T initParam(HalfHourReportSrcSelectParameter parameter) {

        A200065Param param = new A200065Param();

        if(param != null) {
            param.setHistoryCount(parameter.getHistoryCount());
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
