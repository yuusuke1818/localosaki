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
import jp.co.osaki.osol.api.dao.master.CommonWeatherCityDao;
import jp.co.osaki.osol.api.parameter.master.CommonWeatherCityParameter;
import jp.co.osaki.osol.api.response.master.CommonWeatherCityResponse;
import jp.co.osaki.osol.api.result.master.CommonWeatherCityResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 天気情報市町村マスタ検索 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "CommonWeatherCityBean")
@RequestScoped
public class CommonWeatherCityBean extends OsolApiBean<CommonWeatherCityParameter>
        implements BaseApiBean<CommonWeatherCityParameter, CommonWeatherCityResponse> {

    private CommonWeatherCityParameter parameter = new CommonWeatherCityParameter();

    private CommonWeatherCityResponse response = new CommonWeatherCityResponse();

    @EJB
    private CommonWeatherCityDao commonWeatherCityDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CommonWeatherCityParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(CommonWeatherCityParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CommonWeatherCityResponse execute() throws Exception {
        CommonWeatherCityParameter param = new CommonWeatherCityParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.parameter.getCommonWeatherParameter() != null
                && this.parameter.getCommonWeatherParameter().getPrefectureCdList() != null
                && !this.parameter.getCommonWeatherParameter().getPrefectureCdList().isEmpty()) {
            param.setCommonWeatherParameter(this.parameter.getCommonWeatherParameter());
        }
        param.setCityName(this.parameter.getCityName());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CommonWeatherCityResult result = commonWeatherCityDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
