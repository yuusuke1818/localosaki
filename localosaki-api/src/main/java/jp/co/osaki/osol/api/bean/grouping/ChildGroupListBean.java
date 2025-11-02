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
import jp.co.osaki.osol.api.dao.grouping.ChildGroupListDao;
import jp.co.osaki.osol.api.parameter.grouping.ChildGroupListParameter;
import jp.co.osaki.osol.api.response.grouping.ChildGroupListResponse;
import jp.co.osaki.osol.api.result.grouping.ChildGroupListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 子グループ一覧取得処理 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "ChildGroupListBean")
@RequestScoped
public class ChildGroupListBean extends OsolApiBean<ChildGroupListParameter>
        implements BaseApiBean<ChildGroupListParameter, ChildGroupListResponse> {

    private ChildGroupListParameter parameter = new ChildGroupListParameter();

    private ChildGroupListResponse response = new ChildGroupListResponse();

    @EJB
    ChildGroupListDao childGroupListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ChildGroupListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ChildGroupListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public ChildGroupListResponse execute() throws Exception {
        ChildGroupListParameter param = new ChildGroupListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setParentGroupId(this.parameter.getParentGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ChildGroupListResult result = childGroupListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
