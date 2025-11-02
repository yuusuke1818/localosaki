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
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.dao.building.setting.AvailableEnergyListDao;
import jp.co.osaki.osol.api.parameter.building.setting.AvailableEnergyListParameter;
import jp.co.osaki.osol.api.response.building.setting.AvailableEnergyListResponse;
import jp.co.osaki.osol.api.result.building.setting.AvailableEnergyListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "AvailableEnergyListBean")
@RequestScoped
public class AvailableEnergyListBean extends OsolApiBean<AvailableEnergyListParameter>
        implements BaseApiBean<AvailableEnergyListParameter, AvailableEnergyListResponse> {

    private AvailableEnergyListParameter parameter = new AvailableEnergyListParameter();

    private AvailableEnergyListResponse response = new AvailableEnergyListResponse();

    @EJB
    AvailableEnergyListDao availableEnergyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AvailableEnergyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AvailableEnergyListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AvailableEnergyListResponse execute() throws Exception {
        AvailableEnergyListParameter param = new AvailableEnergyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());
        param.setContractId(this.parameter.getContractId());
        param.setEnergyYmFrom(this.parameter.getEnergyYmFrom());
        param.setEnergyYmTo(this.parameter.getEnergyYmTo());
        param.setEnergyYmPoint(this.parameter.getEnergyYmPoint());
        param.setDayAndNightType(this.parameter.getDayAndNightType());
        param.setEngSupplyType(this.parameter.getEngSupplyType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //エネルギー利用年月Toが入力されているが、エネルギー利用年月Fromが未入力の場合
        if(param.getEnergyYmTo() != null && param.getEnergyYmFrom() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        //エネルギー利用年月Fromの内容チェック
        if (param.getEnergyYmFrom() != null &&
                DateUtility.changeDateFormat(param.getEnergyYmFrom(),DateUtility.DATE_FORMAT_DD).equals("01")) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        //エネルギー利用年月Toの内容チェック
        if ( param.getEnergyYmTo() != null &&
                DateUtility.changeDateFormat(param.getEnergyYmTo(),DateUtility.DATE_FORMAT_DD).equals("01")) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        //エネルギー利用基準年月の内容チェック
        if (param.getEnergyYmPoint() != null &&
                DateUtility.changeDateFormat(param.getEnergyYmPoint(),DateUtility.DATE_FORMAT_DD).equals("01")) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        //昼夜区分の内容チェック
        if (!CheckUtility.isNullOrEmpty(param.getDayAndNightType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.DAY_AND_NIGHT_TYPE.getName(param.getDayAndNightType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        //供給区分の内容チェック
        if (!CheckUtility.isNullOrEmpty(param.getEngSupplyType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.ENG_SUPPLY_TYPE.getName(param.getEngSupplyType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        AvailableEnergyListResult result = availableEnergyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
