/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.master;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.master.BuildingWeatherCityCdWriteDao;
import jp.co.osaki.osol.api.parameter.master.BuildingWeatherCityCdWriteParameter;
import jp.co.osaki.osol.api.response.master.BuildingWeatherCityCdWriteResponse;
import jp.co.osaki.osol.api.result.master.BuildingWeatherCityCdWriteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 市区町村コード書き込み Beanクラス
 *
 * @author n-takada
 */
@Named(value = "BuildingWeatherCityCdWriteBean")
@RequestScoped
public class BuildingWeatherCityCdWriteBean extends OsolApiBean<BuildingWeatherCityCdWriteParameter>
        implements BaseApiBean<BuildingWeatherCityCdWriteParameter, BuildingWeatherCityCdWriteResponse> {

    private BuildingWeatherCityCdWriteParameter parameter = new BuildingWeatherCityCdWriteParameter();

    private BuildingWeatherCityCdWriteResponse response = new BuildingWeatherCityCdWriteResponse();

    @EJB
    private BuildingWeatherCityCdWriteDao buildingWeatherCityCdWriteDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingWeatherCityCdWriteParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingWeatherCityCdWriteParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingWeatherCityCdWriteResponse execute() throws Exception {
        BuildingWeatherCityCdWriteParameter param = new BuildingWeatherCityCdWriteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingVersion(this.parameter.getBuildingVersion());
        param.setWeatherCityCd(this.parameter.getWeatherCityCd());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingWeatherCityCdWriteResult result = buildingWeatherCityCdWriteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
