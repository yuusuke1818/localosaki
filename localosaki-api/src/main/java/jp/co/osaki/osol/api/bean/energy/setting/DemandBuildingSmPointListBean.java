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
import jp.co.osaki.osol.api.dao.energy.setting.DemandBuildingSmPointListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.DemandBuildingSmPointListParameter;
import jp.co.osaki.osol.api.response.energy.setting.DemandBuildingSmPointListResponse;
import jp.co.osaki.osol.api.result.energy.setting.DemandBuildingSmPointListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物機器ポイント情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandBuildingSmPointListBean")
@RequestScoped
public class DemandBuildingSmPointListBean extends OsolApiBean<DemandBuildingSmPointListParameter>
        implements BaseApiBean<DemandBuildingSmPointListParameter, DemandBuildingSmPointListResponse> {

    private DemandBuildingSmPointListParameter parameter = new DemandBuildingSmPointListParameter();

    private DemandBuildingSmPointListResponse response = new DemandBuildingSmPointListResponse();

    @EJB
    DemandBuildingSmPointListDao demandBuildingSmPointListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandBuildingSmPointListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandBuildingSmPointListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandBuildingSmPointListResponse execute() throws Exception {
        DemandBuildingSmPointListParameter param = new DemandBuildingSmPointListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setPointNo(this.parameter.getPointNo());
        param.setPointType(this.parameter.getPointType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandBuildingSmPointListResult result = demandBuildingSmPointListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
