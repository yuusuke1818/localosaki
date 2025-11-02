/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.input;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.input.CoefficientHistoryManageListDao;
import jp.co.osaki.osol.api.parameter.energy.input.CoefficientHistoryManageListParameter;
import jp.co.osaki.osol.api.response.energy.input.CoefficientHistoryManageListResponse;
import jp.co.osaki.osol.api.result.energy.input.CoefficientHistoryManageListResult;
import jp.co.osaki.osol.api.utility.common.ApiValidateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 係数履歴管理マスタ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "CoefficientHistoryManageListBean")
@RequestScoped
public class CoefficientHistoryManageListBean extends OsolApiBean<CoefficientHistoryManageListParameter>
        implements BaseApiBean<CoefficientHistoryManageListParameter, CoefficientHistoryManageListResponse> {

    private CoefficientHistoryManageListParameter parameter = new CoefficientHistoryManageListParameter();

    private CoefficientHistoryManageListResponse response = new CoefficientHistoryManageListResponse();

    @EJB
    CoefficientHistoryManageListDao coefficientHistoryManageListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CoefficientHistoryManageListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CoefficientHistoryManageListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CoefficientHistoryManageListResponse execute() throws Exception {
        CoefficientHistoryManageListParameter param = new CoefficientHistoryManageListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());
        param.setDayAndNightType(this.parameter.getDayAndNightType());
        param.setCalYmFrom(this.parameter.getCalYmFrom());
        param.setCalYmTo(this.parameter.getCalYmTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (!ApiValidateUtility.validateCalYmFromTo(param.getCalYmFrom(), param.getCalYmTo())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        CoefficientHistoryManageListResult result = coefficientHistoryManageListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
