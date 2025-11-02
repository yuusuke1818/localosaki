package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 請求者情報取得API Beanクラス.
 *
 * @author yoshida.shi
 */
@Named(value = "getSmsClaimantInfoBean")
@RequestScoped
public class GetSmsClaimantInfoBean extends OsolApiBean<GetSmsClaimantInfoParameter>
    implements BaseApiBean<GetSmsClaimantInfoParameter, GetSmsClaimantInfoResponse> {

    private GetSmsClaimantInfoParameter parameter = new GetSmsClaimantInfoParameter();

    private GetSmsClaimantInfoResponse response = new GetSmsClaimantInfoResponse();

    @EJB
    GetSmsClaimantInfoDao getSmsClaimantInfoDao;

    @Override
    public GetSmsClaimantInfoResponse execute() throws Exception {

        GetSmsClaimantInfoParameter param = new GetSmsClaimantInfoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setPersonCorpId(this.parameter.getPersonCorpId());
        param.setPersonId(this.parameter.getPersonId());

        GetSmsClaimantInfoResult result = getSmsClaimantInfoDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    @Override
    public GetSmsClaimantInfoParameter getParameter() {

        return this.parameter;
    }

    @Override
    public void setParameter(GetSmsClaimantInfoParameter parameter) {

        this.parameter = parameter;
    }

}
