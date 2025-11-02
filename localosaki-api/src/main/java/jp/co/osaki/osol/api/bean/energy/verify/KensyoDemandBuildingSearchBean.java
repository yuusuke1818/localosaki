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
import jp.co.osaki.osol.api.dao.energy.verify.KensyoDemandBuildingSearchDao;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoDemandBuildingSearchParameter;
import jp.co.osaki.osol.api.response.energy.verify.KensyoDemandBuildingSearchResponse;
import jp.co.osaki.osol.api.result.energy.verify.KensyoDemandBuildingSearchResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 検証画面用建物検索 Beanクラス
 * <BR>
 * ユーザーが使用状況にて確認できる建物の一覧を返す。
 *
 * @author n-takada
 */
@Named(value = "KensyoDemandBuildingSearchBean")
@RequestScoped
public class KensyoDemandBuildingSearchBean extends OsolApiBean<KensyoDemandBuildingSearchParameter>
        implements BaseApiBean<KensyoDemandBuildingSearchParameter, KensyoDemandBuildingSearchResponse> {

    private KensyoDemandBuildingSearchParameter parameter = new KensyoDemandBuildingSearchParameter();

    private KensyoDemandBuildingSearchResponse response = new KensyoDemandBuildingSearchResponse();

    @EJB
    private KensyoDemandBuildingSearchDao kensyoDemandBuildingSearchDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public KensyoDemandBuildingSearchParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(KensyoDemandBuildingSearchParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public KensyoDemandBuildingSearchResponse execute() throws Exception {
        KensyoDemandBuildingSearchParameter param = new KensyoDemandBuildingSearchParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNo(this.parameter.getBuildingNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        KensyoDemandBuildingSearchResult result = kensyoDemandBuildingSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
