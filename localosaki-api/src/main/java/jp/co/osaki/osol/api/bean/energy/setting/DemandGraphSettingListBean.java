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
import jp.co.osaki.osol.api.dao.energy.setting.DemandGraphSettingListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.DemandGraphSettingListParameter;
import jp.co.osaki.osol.api.response.energy.setting.DemandGraphSettingListResponse;
import jp.co.osaki.osol.api.result.energy.setting.DemandGraphSettingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * グラフ設定情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandGraphSettingListBean")
@RequestScoped
public class DemandGraphSettingListBean extends OsolApiBean<DemandGraphSettingListParameter>
        implements BaseApiBean<DemandGraphSettingListParameter, DemandGraphSettingListResponse> {

    private DemandGraphSettingListParameter parameter = new DemandGraphSettingListParameter();

    private DemandGraphSettingListResponse response = new DemandGraphSettingListResponse();

    @EJB
    DemandGraphSettingListDao demandGraphSettingListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandGraphSettingListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandGraphSettingListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandGraphSettingListResponse execute() throws Exception {
        DemandGraphSettingListParameter param = new DemandGraphSettingListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandGraphSettingListResult result = demandGraphSettingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
