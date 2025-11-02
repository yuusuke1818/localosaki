package jp.co.osaki.osol.api.bean.smoperation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smoperation.SmOperationBuildingSearchDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationBuildingSearchParameter;
import jp.co.osaki.osol.api.response.smoperation.SmOperationBuildingSearchResponse;
import jp.co.osaki.osol.api.result.smoperation.SmOperationBuildingSearchResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物機器制御情報検索 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmOperationBuildingSearchBean")
@RequestScoped
public class SmOperationBuildingSearchBean extends OsolApiBean<SmOperationBuildingSearchParameter>
        implements BaseApiBean<SmOperationBuildingSearchParameter, SmOperationBuildingSearchResponse> {

    private SmOperationBuildingSearchParameter parameter = new SmOperationBuildingSearchParameter();

    private SmOperationBuildingSearchResponse response = new SmOperationBuildingSearchResponse();

    @EJB
    private SmOperationBuildingSearchDao smOperationBuildingSearchDao;

    @Override
    public SmOperationBuildingSearchParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(SmOperationBuildingSearchParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SmOperationBuildingSearchResponse execute() throws Exception {
        SmOperationBuildingSearchParameter param = new SmOperationBuildingSearchParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNo(this.parameter.getBuildingNo());
        // 都道府県
        param.setSmOperationSearchParameter(this.parameter.getSmOperationSearchParameter());
        param.setBuildingTenantCd(this.parameter.getBuildingTenantCd());
        param.setNyukyoTypeCd(this.parameter.getNyukyoTypeCd());
        param.setBuildingStatus(this.parameter.getBuildingStatus());
        param.setOsakiFlg(this.parameter.getOsakiFlg());
        param.setBuildingCorpId(this.parameter.getBuildingCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setDivisionCorpId(this.parameter.getDivisionCorpId());
        param.setDivisionBuildingId(this.parameter.getDivisionBuildingId());
        param.setInputBuildingBorrowByTenant(this.parameter.getInputBuildingBorrowByTenant());
        param.setProductCd(this.parameter.getProductCd());
        param.setIpAddress(this.parameter.getIpAddress());
        // 建物グルーピング
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmOperationBuildingSearchResult result = smOperationBuildingSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
