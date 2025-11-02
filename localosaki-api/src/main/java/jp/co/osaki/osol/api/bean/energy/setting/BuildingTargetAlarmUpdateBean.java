package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingTargetAlarmUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingTargetAlarmUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingTargetAlarmUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingTargetAlarmUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物目標超過警報更新 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "BuildingTargetAlarmUpdateBean")
@RequestScoped
public class BuildingTargetAlarmUpdateBean extends OsolApiBean<BuildingTargetAlarmUpdateParameter>
        implements BaseApiBean<BuildingTargetAlarmUpdateParameter, BuildingTargetAlarmUpdateResponse> {

    private BuildingTargetAlarmUpdateParameter parameter = new BuildingTargetAlarmUpdateParameter();

    private BuildingTargetAlarmUpdateResponse response = new BuildingTargetAlarmUpdateResponse();

    @EJB
    BuildingTargetAlarmUpdateDao buildingTargetAlarmUpdateDao;

    @Override
    public BuildingTargetAlarmUpdateParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(BuildingTargetAlarmUpdateParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public BuildingTargetAlarmUpdateResponse execute() throws Exception {
        BuildingTargetAlarmUpdateParameter param = new BuildingTargetAlarmUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingTargetAlarmUpdateResult result = buildingTargetAlarmUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
