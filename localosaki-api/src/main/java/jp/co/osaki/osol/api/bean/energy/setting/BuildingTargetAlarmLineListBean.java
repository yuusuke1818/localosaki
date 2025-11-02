package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingTargetAlarmLineListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingTargetAlarmLineListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingTargetAlarmLineListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingTargetAlarmLineListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物目標超過警報系統一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingTargetAlarmLineListBean")
@RequestScoped
public class BuildingTargetAlarmLineListBean extends OsolApiBean<BuildingTargetAlarmLineListParameter>
        implements BaseApiBean<BuildingTargetAlarmLineListParameter, BuildingTargetAlarmLineListResponse> {

    private BuildingTargetAlarmLineListParameter parameter = new BuildingTargetAlarmLineListParameter();

    private BuildingTargetAlarmLineListResponse response = new BuildingTargetAlarmLineListResponse();

    @EJB
    BuildingTargetAlarmLineListDao buildingTargetAlarmLineListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingTargetAlarmLineListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingTargetAlarmLineListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingTargetAlarmLineListResponse execute() throws Exception {
        BuildingTargetAlarmLineListParameter param = new BuildingTargetAlarmLineListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingTargetAlarmLineListResult result = buildingTargetAlarmLineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
