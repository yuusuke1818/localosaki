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
import jp.co.osaki.osol.api.dao.building.IdBuildingSelectDao;
import jp.co.osaki.osol.api.parameter.building.IdBuildingSelectParameter;
import jp.co.osaki.osol.api.response.building.IdBuildingSelectResponse;
import jp.co.osaki.osol.api.result.building.IdBuildingSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物・テナント一覧取得（建物ID指定） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "IdBuildingSelectBean")
@RequestScoped
public class IdBuildingSelectBean extends OsolApiBean<IdBuildingSelectParameter>
        implements BaseApiBean<IdBuildingSelectParameter, IdBuildingSelectResponse> {

    private IdBuildingSelectParameter parameter = new IdBuildingSelectParameter();

    private IdBuildingSelectResponse response = new IdBuildingSelectResponse();

    @EJB
    IdBuildingSelectDao idBuildingSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public IdBuildingSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(IdBuildingSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public IdBuildingSelectResponse execute() throws Exception {
        IdBuildingSelectParameter param = new IdBuildingSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTotalTargetYm(this.parameter.getTotalTargetYm());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        IdBuildingSelectResult result = idBuildingSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
