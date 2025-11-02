package jp.co.osaki.osol.api.bean.sms.collect.setting.meterreading.autoinspection;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ更新API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "InsertSmsAutoInspectionBean")
@RequestScoped
public class InsertSmsAutoInspectionBean extends OsolApiBean<InsertSmsAutoInspectionParameter>
implements BaseApiBean<InsertSmsAutoInspectionParameter, InsertSmsAutoInspectionResponse> {

    private InsertSmsAutoInspectionParameter parameter = new InsertSmsAutoInspectionParameter();

    private InsertSmsAutoInspectionResponse response = new InsertSmsAutoInspectionResponse();

    @EJB
    InsertSmsAutoInspectionDao dao;

	@Override
	public InsertSmsAutoInspectionParameter getParameter() {
		return parameter;
	}

	@Override
	public void setParameter(InsertSmsAutoInspectionParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public InsertSmsAutoInspectionResponse execute() throws Exception {
        InsertSmsAutoInspectionParameter param = new InsertSmsAutoInspectionParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());
        param.setRequest(this.parameter.getRequest());

        InsertSmsAutoInspectionResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
	}

}
