/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.signage;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolAwsS3FileTransfer;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.signage.SignageApkContentImageDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkContentImageParameter;
import jp.co.osaki.osol.api.response.signage.SignageApkContentImageResponse;
import jp.co.osaki.osol.api.result.signage.SignageApkContentImageResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * アプリからサイネージコンテンツの画像を取得する時に利用 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageApkContentImageBean")
@RequestScoped
public class SignageApkContentImageBean extends OsolApiBean<SignageApkContentImageParameter>
        implements BaseApiBean<SignageApkContentImageParameter, SignageApkContentImageResponse> {

    private SignageApkContentImageParameter parameter = new SignageApkContentImageParameter();

    private SignageApkContentImageResponse response = new SignageApkContentImageResponse();

    @Inject
    private OsolAwsS3FileTransfer osolAwsS3FileTransfer;

    @EJB
    private SignageApkContentImageDao signageApkContentImageDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageApkContentImageParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SignageApkContentImageParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageApkContentImageResponse execute() throws Exception {
        SignageApkContentImageParameter param = new SignageApkContentImageParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setImageFileName(this.parameter.getImageFileName());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageApkContentImageResult result = new SignageApkContentImageResult();

        String imageFillFullPath = ApiSimpleConstants.IMAGE_LOCAL_PATH.concat(param.getImageFileName());
        //S3からダウンロード
        osolAwsS3FileTransfer.downloadFileFromS3(imageFillFullPath);

        response.setImageFilePath(imageFillFullPath);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
