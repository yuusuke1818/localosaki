package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDmInitialDataSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmInitialDataSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDmInitialDataSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmInitialDataSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物導入前データ取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingDmInitialDataSelectBean")
@RequestScoped
public class BuildingDmInitialDataSelectBean extends OsolApiBean<BuildingDmInitialDataSelectParameter>
        implements BaseApiBean<BuildingDmInitialDataSelectParameter, BuildingDmInitialDataSelectResponse> {

    private BuildingDmInitialDataSelectParameter parameter = new BuildingDmInitialDataSelectParameter();

    private BuildingDmInitialDataSelectResponse response = new BuildingDmInitialDataSelectResponse();

    @EJB
    BuildingDmInitialDataSelectDao buildingDmInitialDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmInitialDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmInitialDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public BuildingDmInitialDataSelectResponse execute() throws Exception {
        BuildingDmInitialDataSelectParameter param = new BuildingDmInitialDataSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDmInitialDataSelectResult result = buildingDmInitialDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
