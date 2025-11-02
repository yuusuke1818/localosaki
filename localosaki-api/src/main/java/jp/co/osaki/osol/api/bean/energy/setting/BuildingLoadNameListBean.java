package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLoadNameListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLoadNameListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLoadNameListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLoadNameListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物負荷名称設定一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLoadNameListBean")
@RequestScoped
public class BuildingLoadNameListBean extends OsolApiBean<BuildingLoadNameListParameter>
        implements BaseApiBean<BuildingLoadNameListParameter, BuildingLoadNameListResponse> {

    private BuildingLoadNameListParameter parameter = new BuildingLoadNameListParameter();

    private BuildingLoadNameListResponse response = new BuildingLoadNameListResponse();

    @EJB
    BuildingLoadNameListDao buildingLoadNameListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLoadNameListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLoadNameListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLoadNameListResponse execute() throws Exception {
        BuildingLoadNameListParameter param = new BuildingLoadNameListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLoadNameListResult result = buildingLoadNameListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
