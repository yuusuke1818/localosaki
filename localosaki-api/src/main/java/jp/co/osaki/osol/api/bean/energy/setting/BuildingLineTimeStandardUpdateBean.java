package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLineTimeStandardUpdateDao;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineTimeStandardUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineTimeStandardUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineTimeStandardUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 時限標準値データ更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLineTimeStandardUpdateBean")
@RequestScoped
public class BuildingLineTimeStandardUpdateBean extends OsolApiBean<BuildingLineTimeStandardUpdateParameter>
        implements BaseApiBean<BuildingLineTimeStandardUpdateParameter, BuildingLineTimeStandardUpdateResponse> {

    private BuildingLineTimeStandardUpdateParameter parameter = new BuildingLineTimeStandardUpdateParameter();

    private BuildingLineTimeStandardUpdateResponse response = new BuildingLineTimeStandardUpdateResponse();

    @EJB
    BuildingLineTimeStandardUpdateDao buildingLineTimeStandardUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLineTimeStandardUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLineTimeStandardUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLineTimeStandardUpdateResponse execute() throws Exception {
        BuildingLineTimeStandardUpdateParameter param = new BuildingLineTimeStandardUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLineTimeStandardUpdateResult result = buildingLineTimeStandardUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
