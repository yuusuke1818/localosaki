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
import jp.co.osaki.osol.api.dao.smoperation.SmOperationDayRepPointDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationDayRepPointParameter;
import jp.co.osaki.osol.api.response.smoperation.SmOperationDayRepPointResponse;
import jp.co.osaki.osol.api.result.smoperation.SmOperationDayRepPointResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御DB閲覧 日報データ取得 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "SmOperationDayRepPointBean")
@RequestScoped
public class SmOperationDayRepPointBean extends OsolApiBean<SmOperationDayRepPointParameter>
        implements BaseApiBean<SmOperationDayRepPointParameter, SmOperationDayRepPointResponse> {

    private SmOperationDayRepPointParameter parameter = new SmOperationDayRepPointParameter();

    private SmOperationDayRepPointResponse response = new SmOperationDayRepPointResponse();

    @EJB
    SmOperationDayRepPointDao smOperationDayRepPointDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmOperationDayRepPointParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmOperationDayRepPointParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmOperationDayRepPointResponse execute() throws Exception {
        SmOperationDayRepPointParameter param = new SmOperationDayRepPointParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setMeasurementDateFrom(this.parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(this.parameter.getMeasurementDateTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmOperationDayRepPointResult result = smOperationDayRepPointDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
