package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.DeleteSmsMeterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.DeleteSmsMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.DeleteSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.DeleteSmsMeterResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター削除要求 Beanクラス
 * @author sagi_h
 *
 */
@Named(value = "DeleteSmsMeterBean")
@RequestScoped
public class DeleteSmsMeterBean extends OsolApiBean<DeleteSmsMeterParameter>
        implements BaseApiBean<DeleteSmsMeterParameter, DeleteSmsMeterResponse> {
    private DeleteSmsMeterParameter parameter = new DeleteSmsMeterParameter();
    private DeleteSmsMeterResponse response = new DeleteSmsMeterResponse();

    @EJB
    DeleteSmsMeterDao deleteSmsMeterDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DeleteSmsMeterParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DeleteSmsMeterParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DeleteSmsMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DeleteSmsMeterParameter param = new DeleteSmsMeterParameter();
        copyOsolApiParameter(this.parameter,param);
        param.setResult(this.parameter.getResult());
        param.setSendFlg(this.parameter.getSendFlg());

        DeleteSmsMeterResult result = deleteSmsMeterDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
