package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.ListExpiredMeterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListExpiredMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListExpiredMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListExpiredMeterBean")
@RequestScoped
public class ListExpiredMeterBean extends OsolApiBean<ListExpiredMeterParameter>
implements BaseApiBean<ListExpiredMeterParameter, ListExpiredMeterResponse>{
    private ListExpiredMeterResponse response = new ListExpiredMeterResponse();
    private ListExpiredMeterParameter parameter = new ListExpiredMeterParameter();

    @EJB
    ListExpiredMeterDao listExpiredMeterDao;

    @Override
    public ListExpiredMeterParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListExpiredMeterParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListExpiredMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListExpiredMeterParameter param = new ListExpiredMeterParameter();

        copyOsolApiParameter(this.parameter, param);

        param.setDevId(this.parameter.getDevId());
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setExpiredMeter(this.parameter.getExpiredMeter());

        ListSmsMeterResult result = listExpiredMeterDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
