package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDmInitialDataUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmInitialDataUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDmInitialDataUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmInitialDataUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物導入前データ更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingDmInitialDataUpdateBean")
@RequestScoped
public class BuildingDmInitialDataUpdateBean extends OsolApiBean<BuildingDmInitialDataUpdateParameter>
        implements BaseApiBean<BuildingDmInitialDataUpdateParameter, BuildingDmInitialDataUpdateResponse> {

    private BuildingDmInitialDataUpdateParameter parameter = new BuildingDmInitialDataUpdateParameter();

    private BuildingDmInitialDataUpdateResponse response = new BuildingDmInitialDataUpdateResponse();

    @EJB
    BuildingDmInitialDataUpdateDao buildingDmInitialDataUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmInitialDataUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmInitialDataUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDmInitialDataUpdateResponse execute() throws Exception {
        BuildingDmInitialDataUpdateParameter param = new BuildingDmInitialDataUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDmInitialDataUpdateResult result = buildingDmInitialDataUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
