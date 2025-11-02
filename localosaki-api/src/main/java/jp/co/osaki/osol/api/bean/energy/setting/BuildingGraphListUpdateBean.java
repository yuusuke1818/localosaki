package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingGraphListUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingGraphListUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingGraphListUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingGraphListUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物グラフ設定一覧更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingGraphListUpdateBean")
@RequestScoped
public class BuildingGraphListUpdateBean extends OsolApiBean<BuildingGraphListUpdateParameter>
        implements BaseApiBean<BuildingGraphListUpdateParameter, BuildingGraphListUpdateResponse> {

    private BuildingGraphListUpdateParameter parameter = new BuildingGraphListUpdateParameter();

    private BuildingGraphListUpdateResponse response = new BuildingGraphListUpdateResponse();

    @EJB
    BuildingGraphListUpdateDao buildingGraphListUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingGraphListUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingGraphListUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingGraphListUpdateResponse execute() throws Exception {
        BuildingGraphListUpdateParameter param = new BuildingGraphListUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingGraphListUpdateResult result = buildingGraphListUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
