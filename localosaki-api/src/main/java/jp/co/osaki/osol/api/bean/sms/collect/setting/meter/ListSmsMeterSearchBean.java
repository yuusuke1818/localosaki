package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.ListSmsMeterSearchDao;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.ListSmsDevRelationDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsDevRelationResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター管理 メーター情報一覧取得 Beanクラス
 * @author kimura.m
 */
@Named(value = "ListSmsMeterSearchBean")
@RequestScoped
public class ListSmsMeterSearchBean extends OsolApiBean<ListSmsMeterParameter>
implements BaseApiBean<ListSmsMeterParameter, ListSmsMeterResponse> {

    private ListSmsMeterResponse response = new ListSmsMeterResponse();
    private ListSmsMeterParameter parameter = new ListSmsMeterParameter();

    @EJB
    ListSmsMeterSearchDao listSmsMeterSearchDao;

    @EJB
    ListSmsDevRelationDao listSmsDevRelationDao;

    @Override
    public ListSmsMeterParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsMeterParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterParameter param = new ListSmsMeterParameter();
        List<String> devIdList = new ArrayList<>();

        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setOffset(this.parameter.getOffset());
        param.setAmount(this.parameter.getAmount());
        if (!CheckUtility.isNullOrEmpty(this.parameter.getMeterKind())) {
            param.setMeterKind(this.parameter.getMeterKind());
        }
        if (!CheckUtility.isNullOrEmpty(this.parameter.getDevId())) {
        	devIdList.add(this.parameter.getDevId());
        } else {
        	ListSmsDevRelationParameter devParameter = new ListSmsDevRelationParameter();
        	devParameter.setBean("ListSmsDevRelationBean");
        	devParameter.setCorpId(this.parameter.getCorpId());
        	devParameter.setBuildingId(this.parameter.getBuildingId());
        	ListSmsDevRelationResult devRelationResult = listSmsDevRelationDao.query(devParameter);
        	if (!devRelationResult.getDetailList().isEmpty()) {
        		for (int i = 0; i < devRelationResult.getDetailList().size(); i++) {
        			devIdList.add(devRelationResult.getDetailList().get(i).getDevId());
        		}
        	}
        }
        ListSmsMeterResult result = new ListSmsMeterResult();
        List<ListSmsMeterResultData> resultMeterInfoListTmp = new ArrayList<>();
        for (String devIdTmp : devIdList) {
        	param.setDevId(devIdTmp);
        	result = listSmsMeterSearchDao.query(param);
            List<ListSmsMeterResultData> convertList = result.getList();
            resultMeterInfoListTmp.addAll(convertList);
        }
        result.setList(resultMeterInfoListTmp);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);


        return response;
    }
}
