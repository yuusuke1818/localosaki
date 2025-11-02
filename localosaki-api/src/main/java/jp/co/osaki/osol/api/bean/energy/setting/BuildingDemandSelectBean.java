package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDemandSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDemandSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDemandSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDemandSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物デマンド取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingDemandSelectBean")
@RequestScoped
public class BuildingDemandSelectBean extends OsolApiBean<BuildingDemandSelectParameter>
        implements BaseApiBean<BuildingDemandSelectParameter, BuildingDemandSelectResponse> {

    private BuildingDemandSelectParameter parameter = new BuildingDemandSelectParameter();

    private BuildingDemandSelectResponse response = new BuildingDemandSelectResponse();

    @EJB
    BuildingDemandSelectDao buildingDemandSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDemandSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDemandSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDemandSelectResponse execute() throws Exception {
        BuildingDemandSelectParameter param = new BuildingDemandSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDemandSelectResult result = buildingDemandSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
