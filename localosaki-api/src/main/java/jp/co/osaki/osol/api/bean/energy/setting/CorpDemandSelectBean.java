/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpDemandSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpDemandSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpDemandSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業デマンド取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "CorpDemandSelectBean")
@RequestScoped
public class CorpDemandSelectBean extends OsolApiBean<CorpDemandSelectParameter>
        implements BaseApiBean<CorpDemandSelectParameter, CorpDemandSelectResponse> {

    @EJB
    private CorpDemandSelectDao corpDemandSelectDao;

    private CorpDemandSelectParameter parameter = new CorpDemandSelectParameter();

    private CorpDemandSelectResponse response = new CorpDemandSelectResponse();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CorpDemandSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpDemandSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CorpDemandSelectResponse execute() throws Exception {
        CorpDemandSelectParameter param = new CorpDemandSelectParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpDemandSelectResult result = corpDemandSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
