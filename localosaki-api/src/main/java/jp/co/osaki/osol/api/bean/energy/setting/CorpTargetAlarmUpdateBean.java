package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpTargetAlarmUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpTargetAlarmUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業目標超過警報建物更新 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "CorpTargetAlarmUpdateBean")
@RequestScoped
public class CorpTargetAlarmUpdateBean extends OsolApiBean<CorpTargetAlarmUpdateParameter>
        implements BaseApiBean<CorpTargetAlarmUpdateParameter, CorpTargetAlarmUpdateResponse> {

    private CorpTargetAlarmUpdateParameter parameter = new CorpTargetAlarmUpdateParameter();

    private CorpTargetAlarmUpdateResponse response = new CorpTargetAlarmUpdateResponse();

    @EJB
    CorpTargetAlarmUpdateDao corpTargetAlarmUpdateDao;

    @Override
    public CorpTargetAlarmUpdateParameter getParameter() {
        // TODO 自動生成されたメソッド・スタブ
        return parameter;
    }

    @Override
    public void setParameter(CorpTargetAlarmUpdateParameter parameter) {
        // TODO 自動生成されたメソッド・スタブ
        this.parameter = parameter;
    }

    @Override
    public CorpTargetAlarmUpdateResponse execute() throws Exception {
        CorpTargetAlarmUpdateParameter param = new CorpTargetAlarmUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpTargetAlarmUpdateResult result = corpTargetAlarmUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    //    @Override
    //    public ApiResponse doCall(HttpServletRequest request) {
    //
    //        CorpTargetAlarmUpdateResponse response = new CorpTargetAlarmUpdateResponse();
    //        Map<String, String> paramMap = Utility.changeParamToMap(request);
    //        String corpId = paramMap.get(ApiParamKeyConstants.CORP_ID);
    //        String userIdString = paramMap.get(ApiParamKeyConstants.LOGIN_USER_ID);
    //        String json = paramMap.get(ApiParamKeyConstants.RESULT_SET);
    //        CorpTargetAlarmUpdateResult rs = new Gson().fromJson(json, CorpTargetAlarmUpdateResult.class);
    //
    //        if (validate(paramMap)) {
    //            response.setResultCd(OsolApiResponseCode.NO_PARAMETER_ERROR);
    //            return response;
    //        }
    //
    //        if (rs != null) {
    //            // 企業目標超過警報更新
    //            corpTargetAlarmWriteDao.write(corpId,
    //                                          rs.getCorpTargetAlarmResultSet(),
    //                                          rs.getListTargetAlarmBuilding(),
    //                                          Long.parseLong(userIdString));
    //        }
    //
    //        response.setResultCd(OsolApiResponseCode.API_OK);
    //        return response;
    //    }
    //
    //    private boolean validate(Map<String, String> paramMap) {
    //        boolean errorFlg = false;
    //
    //        return errorFlg;
    //    }

}
