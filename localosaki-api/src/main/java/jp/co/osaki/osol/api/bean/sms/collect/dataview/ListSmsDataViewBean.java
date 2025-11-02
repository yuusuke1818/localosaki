package jp.co.osaki.osol.api.bean.sms.collect.dataview;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.ListSmsDataViewDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDataViewResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 データ表示機能API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "ListSmsDataViewBean")
@RequestScoped
public class ListSmsDataViewBean extends OsolApiBean<ListSmsDataViewParameter>
        implements BaseApiBean<ListSmsDataViewParameter, ListSmsDataViewResponse> {

    private ListSmsDataViewParameter parameter = new ListSmsDataViewParameter();

    private ListSmsDataViewResponse response = new ListSmsDataViewResponse();

    @EJB
    ListSmsDataViewDao loadSurveyDataListDao;

    @Override
    public ListSmsDataViewParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsDataViewParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsDataViewResponse execute() throws Exception {
        ListSmsDataViewParameter param = new ListSmsDataViewParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());
        param.setDevId(this.parameter.getDevId());
        param.setForwardDiract(this.parameter.isForwardDiract());
        param.setUse(this.parameter.isUse());
        param.setDispTimeUnit(this.parameter.getDispTimeUnit());
        param.setTargetDateTimeFormat(this.parameter.getTargetDateTimeFormat());
        param.setRequest(this.parameter.getRequest());

        ListSmsDataViewResult result = loadSurveyDataListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
