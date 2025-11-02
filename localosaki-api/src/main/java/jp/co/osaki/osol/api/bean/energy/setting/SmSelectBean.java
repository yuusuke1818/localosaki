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
import jp.co.osaki.osol.api.dao.energy.setting.SmSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器情報取得処理 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "SmSelectBean")
@RequestScoped
public class SmSelectBean extends OsolApiBean<SmSelectParameter>
        implements BaseApiBean<SmSelectParameter, SmSelectResponse> {

    private SmSelectParameter parameter = new SmSelectParameter();

    private SmSelectResponse response = new SmSelectResponse();

    @EJB
    SmSelectDao smSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmSelectResponse execute() throws Exception {
        SmSelectParameter param = new SmSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmSelectResult result = smSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
