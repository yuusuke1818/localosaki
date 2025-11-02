/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.DemandVerifySettingUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandVerifySettingUpdateParameter;
import jp.co.osaki.osol.api.response.energy.verify.DemandVerifySettingUpdateResponse;
import jp.co.osaki.osol.api.result.energy.verify.DemandVerifySettingUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド検証設定変更更新 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "DemandVerifySettingUpdateBean")
@RequestScoped
public class DemandVerifySettingUpdateBean extends OsolApiBean<DemandVerifySettingUpdateParameter>
        implements BaseApiBean<DemandVerifySettingUpdateParameter, DemandVerifySettingUpdateResponse> {

    private DemandVerifySettingUpdateParameter parameter = new DemandVerifySettingUpdateParameter();

    private DemandVerifySettingUpdateResponse response = new DemandVerifySettingUpdateResponse();

    @EJB
    private DemandVerifySettingUpdateDao demandVerifySettingUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandVerifySettingUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandVerifySettingUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandVerifySettingUpdateResponse execute() throws Exception {
        DemandVerifySettingUpdateParameter param = new DemandVerifySettingUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setCorpVersion(this.parameter.getCorpVersion());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingVersion(this.parameter.getBuildingVersion());
        param.setSmId(this.parameter.getSmId());
        param.setSmVersion(this.parameter.getSmVersion());
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandVerifySettingUpdateResult result = demandVerifySettingUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
