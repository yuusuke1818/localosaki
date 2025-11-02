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
import jp.co.osaki.osol.api.dao.building.GetBuildingSmsDao;
import jp.co.osaki.osol.api.parameter.building.GetBuildingSmsParameter;
import jp.co.osaki.osol.api.response.building.GetBuildingSmsResponse;
import jp.co.osaki.osol.api.result.building.GetBuildingSmsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * SMS建物情報 取得API Beanクラス
 *
 * @author yoneda_y
 */
@Named(value = "GetBuildingSmsBean")
@RequestScoped
public class GetBuildingSmsBean extends OsolApiBean<GetBuildingSmsParameter>
        implements BaseApiBean<GetBuildingSmsParameter, GetBuildingSmsResponse> {

    private GetBuildingSmsParameter parameter = new GetBuildingSmsParameter();

    private GetBuildingSmsResponse response = new GetBuildingSmsResponse();

    @EJB
    GetBuildingSmsDao getBuildingSmsDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GetBuildingSmsParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GetBuildingSmsParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public GetBuildingSmsResponse execute() throws Exception {
        GetBuildingSmsParameter param = new GetBuildingSmsParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetBuildingSmsResult result = getBuildingSmsDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
