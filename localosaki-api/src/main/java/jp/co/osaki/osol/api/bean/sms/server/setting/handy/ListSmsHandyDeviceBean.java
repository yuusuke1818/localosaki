package jp.co.osaki.osol.api.bean.sms.server.setting.handy;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.handy.ListSmsHandyDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.ListSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.ListSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.ListSmsHandyDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsHandyDeviceBean")
@RequestScoped
public class ListSmsHandyDeviceBean extends OsolApiBean<ListSmsHandyDeviceParameter>
        implements BaseApiBean<ListSmsHandyDeviceParameter, ListSmsHandyDeviceResponse> {

    private ListSmsHandyDeviceParameter parameter = new ListSmsHandyDeviceParameter();

    private ListSmsHandyDeviceResponse response = new ListSmsHandyDeviceResponse();

    @EJB
    ListSmsHandyDeviceDao listSmsHandyDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ListSmsHandyDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ListSmsHandyDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsHandyDeviceResponse execute() throws Exception {
        ListSmsHandyDeviceParameter param = new ListSmsHandyDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsHandyDeviceResult result = listSmsHandyDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
