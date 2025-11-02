/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.DemandOrgKensyoSmInfoDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgKensyoSmInfoParameter;
import jp.co.osaki.osol.api.response.energy.verify.DemandOrgKensyoSmInfoResponse;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgKensyoSmInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー検証 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "DemandOrgKensyoSmInfoBean")
@RequestScoped
public class DemandOrgKensyoSmInfoBean extends OsolApiBean<DemandOrgKensyoSmInfoParameter>
        implements BaseApiBean<DemandOrgKensyoSmInfoParameter, DemandOrgKensyoSmInfoResponse> {

    private DemandOrgKensyoSmInfoParameter parameter = new DemandOrgKensyoSmInfoParameter();

    private DemandOrgKensyoSmInfoResponse response = new DemandOrgKensyoSmInfoResponse();

    @EJB
    private DemandOrgKensyoSmInfoDao demandOrgKensyoSmInfoDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgKensyoSmInfoParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgKensyoSmInfoParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgKensyoSmInfoResponse execute() throws Exception {
        DemandOrgKensyoSmInfoParameter param = new DemandOrgKensyoSmInfoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgKensyoSmInfoResult result = demandOrgKensyoSmInfoDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
