/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingDemandListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDemandListParameter;
import jp.co.osaki.osol.api.response.energy.setting.BuildingDemandListResponse;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDemandListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物デマンド情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "BuildingDemandListBean")
@RequestScoped
public class BuildingDemandListBean extends OsolApiBean<BuildingDemandListParameter>
        implements BaseApiBean<BuildingDemandListParameter, BuildingDemandListResponse> {

    private BuildingDemandListParameter parameter = new BuildingDemandListParameter();

    private BuildingDemandListResponse response = new BuildingDemandListResponse();

    @EJB
    BuildingDemandListDao buildingDemandListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public BuildingDemandListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(BuildingDemandListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BuildingDemandListResponse execute() throws Exception {
        BuildingDemandListParameter param = new BuildingDemandListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        BuildingDemandListResult result = buildingDemandListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    //    @Override
    //    public ApiResponse doCall(HttpServletRequest request) {
    //        BuildingDemandListResponse response = new BuildingDemandListResponse();
    //        List<BuildingDemandListResult> list;
    //
    //        Map<String, String> paramMap = Utility.changeParamToMap(request);
    //
    //        //ログインユーザーID
    //        Long loginUserId;
    //        if (!CheckUtility.isNullOrEmpty(paramMap.get(ApiParamKeyConstants.LOGIN_USER_ID))) {
    //            try {
    //                loginUserId = Long.parseLong(paramMap.get(ApiParamKeyConstants.LOGIN_USER_ID));
    //            } catch (NumberFormatException ex) {
    //                setResponse(response, null, OsolApiResponseCode.NO_PARAMETER_ERROR);
    //                return response;
    //            }
    //        } else {
    //            loginUserId = null;
    //        }
    //
    //        //ログインパーソンID
    //        String loginPersonId = paramMap.get(ApiParamKeyConstants.LOGIN_PERSON_ID);
    //
    //        //自企業
    //        String loginUserCorpId = paramMap.get(ApiParamKeyConstants.LOGIN_USER_CORP_ID);
    //
    //        // 企業ID
    //        String corpId = paramMap.get(ApiParamKeyConstants.CORP_ID);
    //
    //        //建物ID
    //        Long buildingId;
    //        if (CheckUtility.isNullOrEmpty(paramMap.get(ApiParamKeyConstants.BUILDING_ID))) {
    //            buildingId = null;
    //        } else {
    //            try {
    //                buildingId = Long.parseLong(paramMap.get(ApiParamKeyConstants.BUILDING_ID));
    //            } catch (NumberFormatException ex) {
    //                setResponse(response, null, OsolApiResponseCode.NO_PARAMETER_ERROR);
    //                return response;
    //            }
    //
    //        }
    //
    //        if (!ApiValidateUtility.validateApiCommonParameter(loginUserId, loginPersonId, loginUserCorpId, corpId)) {
    //            setResponse(response, null, OsolApiResponseCode.NO_PARAMETER_ERROR);
    //            return response;
    //        }
    //
    //        list = buildingDemandListDao.getBuildingDemandList(loginUserId, loginPersonId, loginUserCorpId, corpId, buildingId);
    //        setResponse(response, list, OsolApiResponseCode.API_OK);
    //        return response;
    //    }
    //
    //    private void setResponse(BuildingDemandListResponse response, List<BuildingDemandListResult> list, OsolApiResponseCode resultCode) {
    //        response.setResultCd(resultCode);
    //        response.setResultList(list);
    //    }

}
