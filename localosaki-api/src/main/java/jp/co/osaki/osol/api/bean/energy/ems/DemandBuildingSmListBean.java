/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.DemandBuildingSmListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandBuildingSmListParameter;
import jp.co.osaki.osol.api.response.energy.setting.DemandBuildingSmListResponse;
import jp.co.osaki.osol.api.result.energy.setting.DemandBuildingSmListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物機器データ取得処理 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandBuildingSmListBean")
@RequestScoped
public class DemandBuildingSmListBean extends OsolApiBean<DemandBuildingSmListParameter>
        implements BaseApiBean<DemandBuildingSmListParameter, DemandBuildingSmListResponse> {

    private DemandBuildingSmListParameter parameter = new DemandBuildingSmListParameter();

    private DemandBuildingSmListResponse response = new DemandBuildingSmListResponse();

    @EJB
    private DemandBuildingSmListDao demandBuildingSmListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandBuildingSmListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandBuildingSmListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandBuildingSmListResponse execute() throws Exception {
        DemandBuildingSmListParameter param = new DemandBuildingSmListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandBuildingSmListResult result = demandBuildingSmListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
