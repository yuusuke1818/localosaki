package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingSmCountDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingSmCountParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingSmCountResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingSmCountResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物機器データ件数取得処理 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingSmCountBean")
@RequestScoped
public class BuildingSmCountBean extends OsolApiBean<BuildingSmCountParameter>
        implements BaseApiBean<BuildingSmCountParameter, BuildingSmCountResponse> {

    private BuildingSmCountParameter parameter = new BuildingSmCountParameter();

    private BuildingSmCountResponse response = new BuildingSmCountResponse();

    @EJB
    private BuildingSmCountDao buildingSmCountDao;

    /* (非 Javadoc)
    * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
    */
    @Override
    public BuildingSmCountParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingSmCountParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingSmCountResponse execute() throws Exception {
        BuildingSmCountParameter param = new BuildingSmCountParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingSmCountResult result = buildingSmCountDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
