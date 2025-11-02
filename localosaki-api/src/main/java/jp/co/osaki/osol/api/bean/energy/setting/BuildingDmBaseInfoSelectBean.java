package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDmBaseInfoSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmBaseInfoSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDmBaseInfoSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmBaseInfoSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物デマンド基本情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "BuildingDmBaseInfoSelectBean")
@RequestScoped
public class BuildingDmBaseInfoSelectBean extends OsolApiBean<BuildingDmBaseInfoSelectParameter>
        implements BaseApiBean<BuildingDmBaseInfoSelectParameter, BuildingDmBaseInfoSelectResponse> {

    private BuildingDmBaseInfoSelectParameter parameter = new BuildingDmBaseInfoSelectParameter();

    private BuildingDmBaseInfoSelectResponse response = new BuildingDmBaseInfoSelectResponse();

    @EJB
    BuildingDmBaseInfoSelectDao buildingDmBaseInfoSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDmBaseInfoSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDmBaseInfoSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDmBaseInfoSelectResponse execute() throws Exception {
        BuildingDmBaseInfoSelectParameter param = new BuildingDmBaseInfoSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDmBaseInfoSelectResult result = buildingDmBaseInfoSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
