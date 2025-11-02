package jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.ListSmsBuildingDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsBuildingParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.ListSmsBuildingResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsBuildingResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsBuildingBean")
@RequestScoped
public class ListSmsBuildingBean extends OsolApiBean<ListSmsBuildingParameter>
        implements BaseApiBean<ListSmsBuildingParameter, ListSmsBuildingResponse> {

    private ListSmsBuildingParameter parameter = new ListSmsBuildingParameter();

    private ListSmsBuildingResponse response = new ListSmsBuildingResponse();

    @EJB
    ListSmsBuildingDao listSmsBuildingDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ListSmsBuildingParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ListSmsBuildingParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsBuildingResponse execute() throws Exception {
        ListSmsBuildingParameter param = new ListSmsBuildingParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsBuildingResult result = listSmsBuildingDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
