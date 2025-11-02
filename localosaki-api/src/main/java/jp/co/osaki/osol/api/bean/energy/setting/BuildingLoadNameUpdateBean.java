package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLoadNameUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLoadNameUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLoadNameUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLoadNameUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物負荷名称設定一覧更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLoadNameUpdateBean")
@RequestScoped
public class BuildingLoadNameUpdateBean extends OsolApiBean<BuildingLoadNameUpdateParameter>
        implements BaseApiBean<BuildingLoadNameUpdateParameter, BuildingLoadNameUpdateResponse> {

    private BuildingLoadNameUpdateParameter parameter = new BuildingLoadNameUpdateParameter();

    private BuildingLoadNameUpdateResponse response = new BuildingLoadNameUpdateResponse();

    @EJB
    BuildingLoadNameUpdateDao buildingLoadNameUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLoadNameUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLoadNameUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLoadNameUpdateResponse execute() throws Exception {
        BuildingLoadNameUpdateParameter param = new BuildingLoadNameUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLoadNameUpdateResult result = buildingLoadNameUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
