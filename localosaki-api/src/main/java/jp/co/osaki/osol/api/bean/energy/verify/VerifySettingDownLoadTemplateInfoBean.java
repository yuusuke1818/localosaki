package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.VerifySettingDownLoadTemplateInfoDao;
import jp.co.osaki.osol.api.parameter.energy.verify.VerifySettingDownLoadTemplateInfoParameter;
import jp.co.osaki.osol.api.response.energy.verify.VerifySettingDownLoadTemplateInfoResponse;
import jp.co.osaki.osol.api.result.energy.verify.VerifySettingDownLoadTemplateInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 検証ダウンロードテンプレート情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "VerifySettingDownLoadTemplateInfoBean")
@RequestScoped
public class VerifySettingDownLoadTemplateInfoBean extends OsolApiBean<VerifySettingDownLoadTemplateInfoParameter>
        implements BaseApiBean<VerifySettingDownLoadTemplateInfoParameter, VerifySettingDownLoadTemplateInfoResponse> {

    private VerifySettingDownLoadTemplateInfoParameter parameter = new VerifySettingDownLoadTemplateInfoParameter();

    private VerifySettingDownLoadTemplateInfoResponse response = new VerifySettingDownLoadTemplateInfoResponse();

    @EJB
    private VerifySettingDownLoadTemplateInfoDao verifySettingDownLoadTemplateInfoDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public VerifySettingDownLoadTemplateInfoParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(VerifySettingDownLoadTemplateInfoParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public VerifySettingDownLoadTemplateInfoResponse execute() throws Exception {
        VerifySettingDownLoadTemplateInfoParameter param = new VerifySettingDownLoadTemplateInfoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setProductCd(this.parameter.getProductCd());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        VerifySettingDownLoadTemplateInfoResult result = verifySettingDownLoadTemplateInfoDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
