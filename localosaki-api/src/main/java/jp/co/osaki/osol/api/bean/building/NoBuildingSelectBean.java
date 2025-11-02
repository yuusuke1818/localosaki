/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.building;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.building.NoBuildingSelectDao;
import jp.co.osaki.osol.api.parameter.building.NoBuildingSelectParameter;
import jp.co.osaki.osol.api.response.building.NoBuildingSelectResponse;
import jp.co.osaki.osol.api.result.building.NoBuildingSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物・テナント一覧取得（建物・テナント番号指定） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "NoBuildingSelectBean")
@RequestScoped
public class NoBuildingSelectBean extends OsolApiBean<NoBuildingSelectParameter>
        implements BaseApiBean<NoBuildingSelectParameter, NoBuildingSelectResponse> {

    private NoBuildingSelectParameter parameter = new NoBuildingSelectParameter();

    private NoBuildingSelectResponse response = new NoBuildingSelectResponse();

    @EJB
    NoBuildingSelectDao noBuildingSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public NoBuildingSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(NoBuildingSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public NoBuildingSelectResponse execute() throws Exception {
        NoBuildingSelectParameter param = new NoBuildingSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setTotalTargetYm(this.parameter.getTotalTargetYm());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        NoBuildingSelectResult result = noBuildingSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
