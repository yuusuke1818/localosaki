package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLinePointListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLinePointListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLinePointListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLinePointListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物系統ポイント一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLinePointListBean")
@RequestScoped
public class BuildingLinePointListBean extends OsolApiBean<BuildingLinePointListParameter>
        implements BaseApiBean<BuildingLinePointListParameter, BuildingLinePointListResponse> {

    private BuildingLinePointListParameter parameter = new BuildingLinePointListParameter();

    private BuildingLinePointListResponse response = new BuildingLinePointListResponse();

    @EJB
    BuildingLinePointListDao buildingLinePointListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLinePointListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLinePointListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLinePointListResponse execute() throws Exception {
        BuildingLinePointListParameter param = new BuildingLinePointListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLinePointListResult result = buildingLinePointListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
