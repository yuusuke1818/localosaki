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
import jp.co.osaki.osol.api.dao.energy.setting.DemandGraphElementListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.DemandGraphElementListParameter;
import jp.co.osaki.osol.api.response.energy.setting.DemandGraphElementListResponse;
import jp.co.osaki.osol.api.result.energy.setting.DemandGraphElementListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * グラフ要素設定情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandGraphElementListBean")
@RequestScoped
public class DemandGraphElementListBean extends OsolApiBean<DemandGraphElementListParameter>
        implements BaseApiBean<DemandGraphElementListParameter, DemandGraphElementListResponse> {

    private DemandGraphElementListParameter parameter = new DemandGraphElementListParameter();

    private DemandGraphElementListResponse response = new DemandGraphElementListResponse();

    @EJB
    DemandGraphElementListDao demandGraphElementListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandGraphElementListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandGraphElementListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public DemandGraphElementListResponse execute() throws Exception {
        DemandGraphElementListParameter param = new DemandGraphElementListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());
        param.setGraphElementId(this.parameter.getGraphElementId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (parameter.getGraphElementId() != null && parameter.getGraphId() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandGraphElementListResult result = demandGraphElementListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
