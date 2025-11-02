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
import jp.co.osaki.osol.api.dao.building.setting.BuildingDmAvailableEnergyInfoSelectDao;
import jp.co.osaki.osol.api.parameter.building.setting.BuildingDmAvailableEnergyInfoSelectParameter;
import jp.co.osaki.osol.api.response.building.setting.BuildingDmAvailableEnergyInfoSelectResponse;
import jp.co.osaki.osol.api.result.building.setting.BuildingDmAvailableEnergyInfoSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "BuildingDmAvailableEnergyInfoSelectBean")
@RequestScoped
public class BuildingDmAvailableEnergyInfoSelectBean extends OsolApiBean<BuildingDmAvailableEnergyInfoSelectParameter>
        implements BaseApiBean<BuildingDmAvailableEnergyInfoSelectParameter, BuildingDmAvailableEnergyInfoSelectResponse> {

    private BuildingDmAvailableEnergyInfoSelectParameter parameter = new BuildingDmAvailableEnergyInfoSelectParameter();

    private BuildingDmAvailableEnergyInfoSelectResponse response = new BuildingDmAvailableEnergyInfoSelectResponse();

    @EJB
    BuildingDmAvailableEnergyInfoSelectDao BuildingDmAvailableEnergyInfoSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmAvailableEnergyInfoSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmAvailableEnergyInfoSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDmAvailableEnergyInfoSelectResponse execute() throws Exception {
        BuildingDmAvailableEnergyInfoSelectParameter param = new BuildingDmAvailableEnergyInfoSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());
        param.setContractId(this.parameter.getContractId());
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDmAvailableEnergyInfoSelectResult result = BuildingDmAvailableEnergyInfoSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
