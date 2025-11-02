package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.Co2CoefficientSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.Co2CoefficientSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.Co2CoefficientSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.Co2CoefficientSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * CO2排出係数取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "Co2CoefficientSelectBean")
@RequestScoped
public class Co2CoefficientSelectBean extends OsolApiBean<Co2CoefficientSelectParameter>
        implements BaseApiBean<Co2CoefficientSelectParameter, Co2CoefficientSelectResponse> {

    private Co2CoefficientSelectParameter parameter = new Co2CoefficientSelectParameter();

    private Co2CoefficientSelectResponse response = new Co2CoefficientSelectResponse();

    @EJB
    Co2CoefficientSelectDao co2CoefficientSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public Co2CoefficientSelectParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(Co2CoefficientSelectParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Co2CoefficientSelectResponse execute() throws Exception {
        Co2CoefficientSelectParameter param = new Co2CoefficientSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngId(this.parameter.getEngId());
        param.setContractId(this.parameter.getContractId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        Co2CoefficientSelectResult result = co2CoefficientSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
