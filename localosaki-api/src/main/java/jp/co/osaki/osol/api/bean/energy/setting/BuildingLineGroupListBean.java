package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLineGroupListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineGroupListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineGroupListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineGroupListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物系統グループ一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLineGroupListBean")
@RequestScoped
public class BuildingLineGroupListBean extends OsolApiBean<BuildingLineGroupListParameter>
        implements BaseApiBean<BuildingLineGroupListParameter, BuildingLineGroupListResponse> {

    private BuildingLineGroupListParameter parameter = new BuildingLineGroupListParameter();

    private BuildingLineGroupListResponse response = new BuildingLineGroupListResponse();

    @EJB
    BuildingLineGroupListDao buildingLineGroupListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLineGroupListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLineGroupListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLineGroupListResponse execute() throws Exception {
        BuildingLineGroupListParameter param = new BuildingLineGroupListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingLineGroupListResult result = buildingLineGroupListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
