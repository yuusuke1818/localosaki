package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingGraphListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingGraphListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingGraphListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingGraphListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物グラフ設定一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingGraphListBean")
@RequestScoped
public class BuildingGraphListBean extends OsolApiBean<BuildingGraphListParameter>
        implements BaseApiBean<BuildingGraphListParameter, BuildingGraphListResponse> {

    private BuildingGraphListParameter parameter = new BuildingGraphListParameter();

    private BuildingGraphListResponse response = new BuildingGraphListResponse();

    @EJB
    BuildingGraphListDao buildingGraphListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingGraphListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingGraphListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingGraphListResponse execute() throws Exception {
        BuildingGraphListParameter param = new BuildingGraphListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingGraphListResult result = buildingGraphListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
