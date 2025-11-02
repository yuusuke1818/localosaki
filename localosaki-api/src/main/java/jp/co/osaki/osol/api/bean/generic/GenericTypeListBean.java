/*
] * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.generic;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.generic.GenericTypeListDao;
import jp.co.osaki.osol.api.parameter.generic.GenericTypeListParameter;
import jp.co.osaki.osol.api.response.generic.GenericTypeListResponse;
import jp.co.osaki.osol.api.result.generic.GenericTypeListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 汎用区分マスタ取得 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "GenericTypeListBean")
@RequestScoped
public class GenericTypeListBean extends OsolApiBean<GenericTypeListParameter>
        implements BaseApiBean<GenericTypeListParameter, GenericTypeListResponse> {

    private GenericTypeListParameter parameter = new GenericTypeListParameter();

    private GenericTypeListResponse response = new GenericTypeListResponse();

    @EJB
    GenericTypeListDao genericTypeListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GenericTypeListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GenericTypeListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public GenericTypeListResponse execute() throws Exception {
        GenericTypeListParameter param = new GenericTypeListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setGroupCode(this.parameter.getGroupCode());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GenericTypeListResult result = genericTypeListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
