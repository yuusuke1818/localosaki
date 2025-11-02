/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.smoperation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smoperation.SmOperationControlEventLogDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationControlEventLogParameter;
import jp.co.osaki.osol.api.response.smoperation.SmOperationControlEventLogResponse;
import jp.co.osaki.osol.api.result.smoperation.SmOperationControlEventLogResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御DB閲覧 イベント制御履歴取得 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "SmOperationControlEventLogBean")
@RequestScoped
public class SmOperationControlEventLogBean extends OsolApiBean<SmOperationControlEventLogParameter>
        implements BaseApiBean<SmOperationControlEventLogParameter, SmOperationControlEventLogResponse> {

    private SmOperationControlEventLogParameter parameter = new SmOperationControlEventLogParameter();

    private SmOperationControlEventLogResponse response = new SmOperationControlEventLogResponse();

    @EJB
    SmOperationControlEventLogDao smOperationControlEventLogDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmOperationControlEventLogParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmOperationControlEventLogParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmOperationControlEventLogResponse execute() throws Exception {
        SmOperationControlEventLogParameter param = new SmOperationControlEventLogParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());
        param.setRecordDate(this.parameter.getRecordDate());
        param.setControlLoad(this.parameter.getControlLoad());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmOperationControlEventLogResult result = smOperationControlEventLogDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
