package jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.ListSmsDevRelationDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.ListSmsDevRelationResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsDevRelationResult;
import jp.co.osaki.osol.utility.CheckUtility;	// 2024-11-27
import jp.skygroup.enl.webap.base.api.BaseApiBean;


@Named(value = "ListSmsDevRelationBean")
@RequestScoped
public class ListSmsDevRelationBean extends OsolApiBean<ListSmsDevRelationParameter>
        implements BaseApiBean<ListSmsDevRelationParameter, ListSmsDevRelationResponse> {

    private ListSmsDevRelationParameter parameter = new ListSmsDevRelationParameter();

    private ListSmsDevRelationResponse response = new ListSmsDevRelationResponse();

    @EJB
    ListSmsDevRelationDao ListSmsDevRelationDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ListSmsDevRelationParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ListSmsDevRelationParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsDevRelationResponse execute() throws Exception {
        ListSmsDevRelationParameter param = new ListSmsDevRelationParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setDevId(this.parameter.getDevId());

        // 2024-11-28
        if (CheckUtility.isNullOrEmpty(param.getAuthHash())) {
        	// 外部APIからの呼び出しではAuthHashがnull
        	if (CheckUtility.isNullOrEmpty(param.getCorpId())) {
	        	// corpIdが設定されていなければ、corpIdにoperationCorpIdをセットする。
	        	param.setCorpId(this.parameter.getOperationCorpId());
	        }
	        else {
	        	if (!param.getCorpId().equals(param.getOperationCorpId())) {
	            	// operationCorpIdとcorpId等しくなければcorpIdに""をセットする。
	        		param.setCorpId("");
	        	}
	        }
        }
        	// 2024-11-28 ここまで

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsDevRelationResult result = ListSmsDevRelationDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
