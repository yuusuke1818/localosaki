package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLinePointListUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLinePointListUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLinePointListUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLinePointListUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物系統ポイント一覧更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLinePointListUpdateBean")
@RequestScoped
public class BuildingLinePointListUpdateBean extends OsolApiBean<BuildingLinePointListUpdateParameter>
        implements BaseApiBean<BuildingLinePointListUpdateParameter, BuildingLinePointListUpdateResponse> {

    private BuildingLinePointListUpdateParameter parameter = new BuildingLinePointListUpdateParameter();

    private BuildingLinePointListUpdateResponse response = new BuildingLinePointListUpdateResponse();

    @EJB
    BuildingLinePointListUpdateDao buildingLinePointListUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLinePointListUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLinePointListUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLinePointListUpdateResponse execute() throws Exception {
        BuildingLinePointListUpdateParameter param = new BuildingLinePointListUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLinePointListUpdateResult result = buildingLinePointListUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
