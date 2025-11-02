/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.generic;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.generic.CommonSearchBuildingDao;
import jp.co.osaki.osol.api.parameter.generic.CommonSearchBuildingParameter;
import jp.co.osaki.osol.api.response.generic.CommonSearchBuildingResponse;
import jp.co.osaki.osol.api.result.generic.CommonSearchBuildingResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 汎用建物検索 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "CommonSearchBuildingBean")
@RequestScoped
public class CommonSearchBuildingBean extends OsolApiBean<CommonSearchBuildingParameter>
        implements BaseApiBean<CommonSearchBuildingParameter, CommonSearchBuildingResponse> {

    private CommonSearchBuildingParameter parameter = new CommonSearchBuildingParameter();

    private CommonSearchBuildingResponse response = new CommonSearchBuildingResponse();

    @EJB
    CommonSearchBuildingDao commonSearchBuildingDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CommonSearchBuildingParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CommonSearchBuildingParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CommonSearchBuildingResponse execute() throws Exception {
        CommonSearchBuildingParameter param = new CommonSearchBuildingParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setCommonSearchParameter(this.parameter.getCommonSearchParameter());
        param.setBuildingTenantCd(this.parameter.getBuildingTenantCd());
        param.setNyukyoTypeCd(this.parameter.getNyukyoTypeCd());
        param.setBuildingStatus(this.parameter.getBuildingStatus());
        param.setOsakiFlg(this.parameter.getOsakiFlg());
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

        CommonSearchBuildingResult result = commonSearchBuildingDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
