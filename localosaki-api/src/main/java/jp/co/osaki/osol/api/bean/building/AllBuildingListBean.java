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
import jp.co.osaki.osol.api.dao.building.AllBuildingListDao;
import jp.co.osaki.osol.api.parameter.building.AllBuildingListParameter;
import jp.co.osaki.osol.api.response.building.AllBuildingListResponse;
import jp.co.osaki.osol.api.result.building.AllBuildingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物・テナント一覧取得（全建物・テナント） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "AllBuildingListBean")
@RequestScoped
public class AllBuildingListBean extends OsolApiBean<AllBuildingListParameter>
        implements BaseApiBean<AllBuildingListParameter, AllBuildingListResponse> {

    private AllBuildingListParameter parameter = new AllBuildingListParameter();

    private AllBuildingListResponse response = new AllBuildingListResponse();

    @EJB
    AllBuildingListDao allBuildingListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AllBuildingListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AllBuildingListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AllBuildingListResponse execute() throws Exception {
        AllBuildingListParameter param = new AllBuildingListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setTotalTargetYm(this.parameter.getTotalTargetYm());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AllBuildingListResult result = allBuildingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
