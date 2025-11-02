/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.plan;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.plan.PlanBuildingTenantSearchDao;
import jp.co.osaki.osol.api.parameter.plan.PlanBuildingTenantSearchParameter;
import jp.co.osaki.osol.api.response.plan.PlanBuildingTenantSearchResponse;
import jp.co.osaki.osol.api.result.plan.PlanBuildingTenantSearchResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行建物・テナント検索 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "PlanBuildingTenantSearchBean")
@RequestScoped
public class PlanBuildingTenantSearchBean extends OsolApiBean<PlanBuildingTenantSearchParameter>
        implements BaseApiBean<PlanBuildingTenantSearchParameter, PlanBuildingTenantSearchResponse> {

    private PlanBuildingTenantSearchParameter parameter = new PlanBuildingTenantSearchParameter();

    private PlanBuildingTenantSearchResponse response = new PlanBuildingTenantSearchResponse();

    @EJB
    PlanBuildingTenantSearchDao planBuildingTenantSearchDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanBuildingTenantSearchParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanBuildingTenantSearchParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public PlanBuildingTenantSearchResponse execute() throws Exception {
        PlanBuildingTenantSearchParameter param = new PlanBuildingTenantSearchParameter();
        copyOsolApiParameter(this.parameter, param);
        // 都道府県
        param.setPlanSearchRequest(this.parameter.getPlanSearchRequest());
        param.setNyukyoTypeCd(this.parameter.getNyukyoTypeCd());
        param.setBuildingStatus(this.parameter.getBuildingStatus());
        param.setBuildingTenantCd(this.parameter.getBuildingTenantCd());
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setOsakiFlg(this.parameter.getOsakiFlg());
        param.setInAlwaysPlanFlg(this.parameter.getInAlwaysPlanFlg());
        param.setBuildingCorpId(this.parameter.getBuildingCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setDivisionCorpId(this.parameter.getDivisionCorpId());
        param.setDivisionBuildingId(this.parameter.getDivisionBuildingId());
        param.setInputBuildingBorrowByTenant(this.parameter.getInputBuildingBorrowByTenant());
        // 建物グルーピング
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanBuildingTenantSearchResult result = planBuildingTenantSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
