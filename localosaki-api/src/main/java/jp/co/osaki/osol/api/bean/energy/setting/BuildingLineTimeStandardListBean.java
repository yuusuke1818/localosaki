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
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLineTimeStandardListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineTimeStandardListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineTimeStandardListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineTimeStandardListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物系統時限標準値取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "BuildingLineTimeStandardListBean")
@RequestScoped
public class BuildingLineTimeStandardListBean extends OsolApiBean<BuildingLineTimeStandardListParameter>
        implements BaseApiBean<BuildingLineTimeStandardListParameter, BuildingLineTimeStandardListResponse> {

    private BuildingLineTimeStandardListParameter parameter = new BuildingLineTimeStandardListParameter();

    private BuildingLineTimeStandardListResponse response = new BuildingLineTimeStandardListResponse();

    @EJB
    BuildingLineTimeStandardListDao buildingLineTimeStandardListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLineTimeStandardListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLineTimeStandardListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLineTimeStandardListResponse execute() throws Exception {
        BuildingLineTimeStandardListParameter param = new BuildingLineTimeStandardListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLineTimeStandardListResult result = buildingLineTimeStandardListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
