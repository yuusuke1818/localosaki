package jp.co.osaki.osol.api.bean.sms.collect.dataview;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

//import jp.co.osaki.osol.access.filter.dao.DevPrmDataFilterDao;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewAllDevParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDataViewAllDevResponse;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsDataViewAllDevBean")
@RequestScoped
public class ListSmsDataViewAllDevBean extends OsolApiBean<ListSmsDataViewAllDevParameter>
implements BaseApiBean<ListSmsDataViewAllDevParameter, ListSmsDataViewAllDevResponse> {

    private ListSmsDataViewAllDevParameter parameter = new ListSmsDataViewAllDevParameter();

    private ListSmsDataViewAllDevResponse response = new ListSmsDataViewAllDevResponse();


    @Override
    public ListSmsDataViewAllDevParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsDataViewAllDevParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsDataViewAllDevResponse execute() throws Exception {
        ListSmsDataViewAllDevParameter param = new ListSmsDataViewAllDevParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());
        param.setDevIdList(this.parameter.getDevIdList());
        param.setForwardDiract(this.parameter.isForwardDiract());
        param.setUse(this.parameter.isUse());
        param.setDispTimeUnit(this.parameter.getDispTimeUnit());
        param.setTargetDateTimeFormat(this.parameter.getTargetDateTimeFormat());
        param.setRequest(this.parameter.getRequest());

//        ListSmsDataViewResult result = loadSurveyDataListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
//        response.setResult(result);
        return response;
    }

}
