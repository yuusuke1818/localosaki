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
import jp.co.osaki.osol.api.dao.building.setting.AvailableEnergyLineListDao;
import jp.co.osaki.osol.api.parameter.building.setting.AvailableEnergyLineListParameter;
import jp.co.osaki.osol.api.response.building.setting.AvailableEnergyLineListResponse;
import jp.co.osaki.osol.api.result.building.setting.AvailableEnergyLineListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー系統情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "AvailableEnergyLineListBean")
@RequestScoped
public class AvailableEnergyLineListBean extends OsolApiBean<AvailableEnergyLineListParameter>
        implements BaseApiBean<AvailableEnergyLineListParameter, AvailableEnergyLineListResponse> {

    private AvailableEnergyLineListParameter parameter = new AvailableEnergyLineListParameter();

    private AvailableEnergyLineListResponse response = new AvailableEnergyLineListResponse();

    @EJB
    AvailableEnergyLineListDao AvailableEnergyLineListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AvailableEnergyLineListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AvailableEnergyLineListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AvailableEnergyLineListResponse execute() throws Exception {
        AvailableEnergyLineListParameter param = new AvailableEnergyLineListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());
        param.setContractId(this.parameter.getContractId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //系統グループIDが入力されているが、系統番号が未入力の場合
        //または系統番号が入力されているが、系統グループIDが未入力の場合
        if((param.getLineGroupId() != null && CheckUtility.isNullOrEmpty(param.getLineNo()))
                || (!CheckUtility.isNullOrEmpty(param.getLineNo()) && param.getLineGroupId() == null )) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        AvailableEnergyLineListResult result = AvailableEnergyLineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
