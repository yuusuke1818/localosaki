package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLineListUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineListUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineListUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineListUpdateResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpLineListDetailResultData;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物系統一覧更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "BuildingLineListUpdateBean")
@RequestScoped
public class BuildingLineListUpdateBean extends OsolApiBean<BuildingLineListUpdateParameter>
        implements BaseApiBean<BuildingLineListUpdateParameter, BuildingLineListUpdateResponse> {

    private BuildingLineListUpdateParameter parameter = new BuildingLineListUpdateParameter();

    private BuildingLineListUpdateResponse response = new BuildingLineListUpdateResponse();

    @EJB
    BuildingLineListUpdateDao buildingLineListUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingLineListUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingLineListUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingLineListUpdateResponse execute() throws Exception {
        BuildingLineListUpdateParameter param = new BuildingLineListUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        /** 初期表示フラグが1のカウント */
        int initialCount = 0;

        for (CorpLineListDetailResultData line : param.getResultSet().getDetail().getLineList()) {
            // 初期表示フラグが1
            if (OsolConstants.FLG_ON.equals(line.getInitialViewFlg())) {
                initialCount++;
                // 初期表示フラグが1が2件存在
                if (initialCount == 2) {
                    response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                    return response;
                }
            // 初期表示フラグが0,1,null以外
            }else if (line.getInitialViewFlg() != null && !OsolConstants.FLG_OFF.equals(line.getInitialViewFlg())) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }
        }

        BuildingLineListUpdateResult result = buildingLineListUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
