package jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.UpdateSmsRelationDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.UpdateSmsRelationParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.UpdateSmsRelationResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.UpdateSmsRelationResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "UpdateSmsRelationBean")
@RequestScoped
public class UpdateSmsRelationBean extends OsolApiBean<UpdateSmsRelationParameter>
        implements BaseApiBean<UpdateSmsRelationParameter, UpdateSmsRelationResponse> {

    private UpdateSmsRelationParameter parameter = new UpdateSmsRelationParameter();

    private UpdateSmsRelationResponse response = new UpdateSmsRelationResponse();

    @EJB
    UpdateSmsRelationDao UpdateSmsRelationDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsRelationParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsRelationParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsRelationResponse execute() throws Exception {
        UpdateSmsRelationParameter param = new UpdateSmsRelationParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsRelationResult result = UpdateSmsRelationDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
