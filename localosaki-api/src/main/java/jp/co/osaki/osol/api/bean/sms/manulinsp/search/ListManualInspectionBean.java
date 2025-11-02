package jp.co.osaki.osol.api.bean.sms.manulinsp.search;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.manualinsp.search.ListManualInspectionDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.search.ListManualInspectionParameter;
import jp.co.osaki.osol.api.response.sms.manualinsp.search.ListManualInspectionResponse;
import jp.co.osaki.osol.api.result.sms.manualinsp.search.ListManualInspectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 任意検針一覧取得 API beanクラス
 *
 * @author yonezawa.a
 *
 */
@Named(value = "ListManualInspectionBean")
@RequestScoped
public class ListManualInspectionBean extends OsolApiBean<ListManualInspectionParameter>
        implements BaseApiBean<ListManualInspectionParameter, ListManualInspectionResponse> {

    private ListManualInspectionParameter parameter = new ListManualInspectionParameter();

    private ListManualInspectionResponse response = new ListManualInspectionResponse();

    @EJB
    ListManualInspectionDao listManualInspectionDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ListManualInspectionParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ListManualInspectionParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListManualInspectionResponse execute() throws Exception {
        ListManualInspectionParameter param = new ListManualInspectionParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListManualInspectionResult result = listManualInspectionDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
