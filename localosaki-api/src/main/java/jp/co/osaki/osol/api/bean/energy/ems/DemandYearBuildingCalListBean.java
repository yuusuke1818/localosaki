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
import jp.co.osaki.osol.api.dao.energy.ems.DemandYearBuildingCalListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandYearBuildingCalListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandYearBuildingCalListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandYearBuildingCalListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド年報建物カレンダ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandYearBuildingCalListBean")
@RequestScoped
public class DemandYearBuildingCalListBean extends OsolApiBean<DemandYearBuildingCalListParameter>
        implements BaseApiBean<DemandYearBuildingCalListParameter, DemandYearBuildingCalListResponse> {

    private DemandYearBuildingCalListParameter parameter = new DemandYearBuildingCalListParameter();

    private DemandYearBuildingCalListResponse response = new DemandYearBuildingCalListResponse();

    @EJB
    DemandYearBuildingCalListDao demandYearBuildingCalListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandYearBuildingCalListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandYearBuildingCalListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandYearBuildingCalListResponse execute() throws Exception {
        DemandYearBuildingCalListParameter param = new DemandYearBuildingCalListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setYearNoFrom(this.parameter.getYearNoFrom());
        param.setYearNoTo(this.parameter.getYearNoTo());
        param.setCalYmFrom(this.parameter.getCalYmFrom());
        param.setCalYmTo(this.parameter.getCalYmTo());
        param.setBaseDate(this.parameter.getBaseDate());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //FromToのバリデーションチェック
        if (!CheckUtility.isNullOrEmpty(param.getYearNoTo()) && CheckUtility.isNullOrEmpty(param.getYearNoFrom())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (!CheckUtility.isNullOrEmpty(param.getCalYmTo()) && CheckUtility.isNullOrEmpty(param.getCalYmFrom())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        //系統のバリデーションチェック
        if (!CheckUtility.isNullOrEmpty(param.getLineNo()) && param.getLineGroupId() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (param.getLineGroupId() != null && CheckUtility.isNullOrEmpty(param.getLineNo())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandYearBuildingCalListResult result = demandYearBuildingCalListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
