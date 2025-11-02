/**
 *
 */
package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.InsertSmsMeterNowValCommandDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterNowValCommandParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.InsertSmsMeterNowValCommandResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.InsertSmsMeterNowValCommandResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター現在値要求 Beanクラス
 * @author sagi_h
 *
 */
@Named(value = "InsertSmsMeterNowValCommandBean")
@RequestScoped
public class InsertSmsMeterNowValCommandBean extends OsolApiBean<InsertSmsMeterNowValCommandParameter>
            implements BaseApiBean<InsertSmsMeterNowValCommandParameter, InsertSmsMeterNowValCommandResponse>  {


    private InsertSmsMeterNowValCommandParameter parameter = new InsertSmsMeterNowValCommandParameter();
    private InsertSmsMeterNowValCommandResponse response = new InsertSmsMeterNowValCommandResponse();

    @EJB
    InsertSmsMeterNowValCommandDao insertSmsMeterNowValCommandDao;

    @Override
    public InsertSmsMeterNowValCommandResponse execute() throws Exception {
        InsertSmsMeterNowValCommandParameter param = new InsertSmsMeterNowValCommandParameter();
        copyOsolApiParameter(this.parameter,param);
        param.setDevId(this.parameter.getDevId());
        param.setMeterMngId(this.parameter.getMeterMngId());

        InsertSmsMeterNowValCommandResult result = insertSmsMeterNowValCommandDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }


    @Override
    public InsertSmsMeterNowValCommandParameter getParameter() {
        return parameter;
    }


    @Override
    public void setParameter(InsertSmsMeterNowValCommandParameter parameter) {
        this.parameter = parameter;
    }


}
