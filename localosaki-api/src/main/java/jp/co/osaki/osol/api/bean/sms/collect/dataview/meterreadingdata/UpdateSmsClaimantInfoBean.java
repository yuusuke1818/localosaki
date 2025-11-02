package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 請求者情報設定API Beanクラス.
 *
 * @author yoshida.shi
 */
@Named(value = "updateSmsClaimantInfoBean")
@RequestScoped
public class UpdateSmsClaimantInfoBean extends OsolApiBean<UpdateSmsClaimantInfoParameter>
    implements BaseApiBean<UpdateSmsClaimantInfoParameter, UpdateSmsClaimantInfoResponse> {

    private UpdateSmsClaimantInfoParameter parameter = new UpdateSmsClaimantInfoParameter();

    private UpdateSmsClaimantInfoResponse response = new UpdateSmsClaimantInfoResponse();

    @EJB
    UpdateSmsClaimantInfoDao updateSmsClaimantInfoDao;

    @Override
    public UpdateSmsClaimantInfoResponse execute() throws Exception {

        UpdateSmsClaimantInfoParameter param = new UpdateSmsClaimantInfoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setPersonCorpId(this.parameter.getPersonCorpId());
        param.setPersonId(this.parameter.getPersonId());
        param.setVersion(this.parameter.getVersion());
        param.setClaimantName(this.parameter.getClaimantName());
        param.setRegNo(this.parameter.getRegNo());

        UpdateSmsClaimantInfoResult result = updateSmsClaimantInfoDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    @Override
    public UpdateSmsClaimantInfoParameter getParameter() {

        return this.parameter;
    }

    @Override
    public void setParameter(UpdateSmsClaimantInfoParameter parameter) {

        this.parameter = parameter;
    }

}
