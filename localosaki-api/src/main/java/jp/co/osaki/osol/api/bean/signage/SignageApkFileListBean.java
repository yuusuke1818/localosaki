/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.signage;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.signage.SignageApkFileListDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkFileListParameter;
import jp.co.osaki.osol.api.response.signage.SignageApkFileListResponse;
import jp.co.osaki.osol.api.result.signage.SignageApkFileListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * サイネージファイル取得 Beanクラス
 * <BR>
 * パラメーターの企業ID、操作中企業ID、パーソンID、建物Noで登録されている<br>
 * サイネージ画像ファイル（広告、業務連絡）のファイル名をxmlで返却する。<br>
 *
 * @author n-takada
 */
@Named(value = "SignageApkFileListBean")
@RequestScoped
public class SignageApkFileListBean extends OsolApiBean<SignageApkFileListParameter>
        implements BaseApiBean<SignageApkFileListParameter, SignageApkFileListResponse> {

    private SignageApkFileListParameter parameter = new SignageApkFileListParameter();

    private SignageApkFileListResponse response = new SignageApkFileListResponse();

    @EJB
    private SignageApkFileListDao signageApkFileListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageApkFileListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SignageApkFileListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageApkFileListResponse execute() throws Exception {
        SignageApkFileListParameter param = new SignageApkFileListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageApkFileListResult result = signageApkFileListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
