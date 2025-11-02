/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.grouping;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.grouping.ParentGroupListDao;
import jp.co.osaki.osol.api.parameter.grouping.ParentGroupListParameter;
import jp.co.osaki.osol.api.response.grouping.ParentGroupListResponse;
import jp.co.osaki.osol.api.result.grouping.ParentGroupListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 親グループ一覧取得処理 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "ParentGroupListBean")
@RequestScoped
public class ParentGroupListBean extends OsolApiBean<ParentGroupListParameter>
        implements BaseApiBean<ParentGroupListParameter, ParentGroupListResponse> {

    private ParentGroupListParameter parameter = new ParentGroupListParameter();

    private ParentGroupListResponse response = new ParentGroupListResponse();

    @EJB
    ParentGroupListDao parentGroupListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ParentGroupListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ParentGroupListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public ParentGroupListResponse execute() throws Exception {
        ParentGroupListParameter param = new ParentGroupListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ParentGroupListResult result = parentGroupListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
