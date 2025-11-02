package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDmBaseInfoUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmBaseInfoUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDmBaseInfoUpdateRequest;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDmBaseInfoUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmBaseInfoUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物デマンド基本情報更新 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "BuildingDmBaseInfoUpdateBean")
@RequestScoped
public class BuildingDmBaseInfoUpdateBean extends OsolApiBean<BuildingDmBaseInfoUpdateParameter>
        implements BaseApiBean<BuildingDmBaseInfoUpdateParameter, BuildingDmBaseInfoUpdateResponse> {

    private BuildingDmBaseInfoUpdateParameter parameter = new BuildingDmBaseInfoUpdateParameter();

    private BuildingDmBaseInfoUpdateResponse response = new BuildingDmBaseInfoUpdateResponse();

    @EJB
    BuildingDmBaseInfoUpdateDao buildingDmBaseInfoUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmBaseInfoUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmBaseInfoUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDmBaseInfoUpdateResponse execute() throws Exception {
        BuildingDmBaseInfoUpdateParameter param = new BuildingDmBaseInfoUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //Requestのバリデーションを行う
        //JSON⇒Resultに変換
        BuildingDmBaseInfoUpdateRequest resultSet = parameter.getResultSet();

        if (resultSet.getAggregateDmList() != null && !resultSet.getAggregateDmList().isEmpty()
                && resultSet.getBuildingDmDetail() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        if (resultSet.getAggregateDmLineList() != null && !resultSet.getAggregateDmLineList().isEmpty()
                && (resultSet.getAggregateDmList() == null || resultSet.getAggregateDmList().isEmpty())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        BuildingDmBaseInfoUpdateResult result = buildingDmBaseInfoUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
