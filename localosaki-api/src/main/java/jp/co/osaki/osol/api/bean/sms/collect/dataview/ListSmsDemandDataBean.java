package jp.co.osaki.osol.api.bean.sms.collect.dataview;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.ListSmsDemandDataDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDemandParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDemandResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDemandDataResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsDemandDataBean")
@RequestScoped
public class ListSmsDemandDataBean extends OsolApiBean<ListSmsDemandParameter>
implements BaseApiBean<ListSmsDemandParameter, ListSmsDemandResponse> {

    private ListSmsDemandParameter parameter = new ListSmsDemandParameter();

    private ListSmsDemandResponse response = new ListSmsDemandResponse();

    @EJB
    private ListSmsDemandDataDao dao;

    @Override
    public ListSmsDemandResponse execute() throws Exception {
        ListSmsDemandParameter param = new ListSmsDemandParameter();
        copyOsolApiParameter(this.parameter, param);

        ListSmsDemandDataResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

    @Override
    public ListSmsDemandParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsDemandParameter parameter) {
        this.parameter = parameter;
    }

}
