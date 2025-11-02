package jp.co.osaki.osol.api.bean.plan;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.plan.PlanFulfillmentWriteDao;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentWriteParameter;
import jp.co.osaki.osol.api.response.plan.PlanFulfillmentWriteResponse;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentWriteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行情報入力 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "PlanFulfillmentWriteBean")
@RequestScoped
public class PlanFulfillmentWriteBean extends OsolApiBean<PlanFulfillmentWriteParameter>
        implements BaseApiBean<PlanFulfillmentWriteParameter, PlanFulfillmentWriteResponse> {

    private PlanFulfillmentWriteParameter parameter = new PlanFulfillmentWriteParameter();

    private PlanFulfillmentWriteResponse response = new PlanFulfillmentWriteResponse();

    @EJB
    private PlanFulfillmentWriteDao planFulfillmentWriteDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanFulfillmentWriteParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanFulfillmentWriteParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public PlanFulfillmentWriteResponse execute() throws Exception {
        PlanFulfillmentWriteParameter param = new PlanFulfillmentWriteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanFulfillmentWriteResult result = planFulfillmentWriteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
