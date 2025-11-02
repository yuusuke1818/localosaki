package jp.co.osaki.osol.api.bean.sms.server.setting.collect;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.collect.ListSmsCollectDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.ListSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.collect.ListSmsCollectDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.ListSmsCollectDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsCollectDeviceBean")
@RequestScoped
public class ListSmsCollectDeviceBean extends OsolApiBean<ListSmsCollectDeviceParameter>
        implements BaseApiBean<ListSmsCollectDeviceParameter, ListSmsCollectDeviceResponse> {

    private ListSmsCollectDeviceParameter parameter = new ListSmsCollectDeviceParameter();

    private ListSmsCollectDeviceResponse response = new ListSmsCollectDeviceResponse();

    @EJB
    ListSmsCollectDeviceDao listSmsCollectDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ListSmsCollectDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ListSmsCollectDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsCollectDeviceResponse execute() throws Exception {
        ListSmsCollectDeviceParameter param = new ListSmsCollectDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsCollectDeviceResult result = listSmsCollectDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
