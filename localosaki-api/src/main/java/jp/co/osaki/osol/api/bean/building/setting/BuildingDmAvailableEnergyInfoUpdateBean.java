/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.building.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.building.setting.BuildingDmAvailableEnergyInfoUpdateDao;
import jp.co.osaki.osol.api.parameter.building.setting.BuildingDmAvailableEnergyInfoUpdateParameter;
import jp.co.osaki.osol.api.response.building.setting.BuildingDmAvailableEnergyInfoUpdateResponse;
import jp.co.osaki.osol.api.result.building.setting.BuildingDmAvailableEnergyInfoUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー情報更新 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "BuildingDmAvailableEnergyInfoUpdateBean")
@RequestScoped
public class BuildingDmAvailableEnergyInfoUpdateBean extends OsolApiBean<BuildingDmAvailableEnergyInfoUpdateParameter>
        implements BaseApiBean<BuildingDmAvailableEnergyInfoUpdateParameter, BuildingDmAvailableEnergyInfoUpdateResponse> {

    private BuildingDmAvailableEnergyInfoUpdateParameter parameter = new BuildingDmAvailableEnergyInfoUpdateParameter();

    private BuildingDmAvailableEnergyInfoUpdateResponse response = new BuildingDmAvailableEnergyInfoUpdateResponse();

    @EJB
    BuildingDmAvailableEnergyInfoUpdateDao BuildingDmAvailableEnergyInfoUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmAvailableEnergyInfoUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmAvailableEnergyInfoUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDmAvailableEnergyInfoUpdateResponse execute() throws Exception {
        BuildingDmAvailableEnergyInfoUpdateParameter param = new BuildingDmAvailableEnergyInfoUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDmAvailableEnergyInfoUpdateResult result = BuildingDmAvailableEnergyInfoUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
