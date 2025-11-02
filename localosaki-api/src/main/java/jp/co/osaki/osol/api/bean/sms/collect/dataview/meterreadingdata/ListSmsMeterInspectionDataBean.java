package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataSearchDao;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoSearchDao;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.ListSmsDevRelationDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataResult;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoResult;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsDevRelationResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 検針データ取得API Beanクラス.
 *
 * @author hosono.s
 */
@Named(value = "ListSmsMeterInspectionDataBean")
@RequestScoped
public class ListSmsMeterInspectionDataBean extends OsolApiBean<ListSmsMeterInspectionDataParameter>
        implements BaseApiBean<ListSmsMeterInspectionDataParameter, ListSmsMeterInspectionDataResponse> {

    private ListSmsMeterInspectionDataParameter parameter = new ListSmsMeterInspectionDataParameter();

    private ListSmsMeterInspectionDataResponse response = new ListSmsMeterInspectionDataResponse();

    @EJB
    ListSmsMeterInspectionDataSearchDao ListSmsMeterInspectionDataDao;

    @EJB
    ListSmsMonthMeterReadingNoSearchDao listSmsMonthMeterReadingNoSearchDao;


    @EJB
    ListSmsDevRelationDao listSmsDevRelationDao;

    @Override
    public ListSmsMeterInspectionDataResponse execute() throws Exception {
    	ListSmsMonthMeterReadingNoParameter inspnoparam = new ListSmsMonthMeterReadingNoParameter();
    	copyOsolApiParameter(this.parameter, inspnoparam);
    	inspnoparam.setCorpId(this.parameter.getCorpId());
    	inspnoparam.setBuildingId(this.parameter.getBuildingId());
        inspnoparam.setYear(this.parameter.getYear());
        inspnoparam.setMonth(this.parameter.getMonth());

        if (!CheckUtility.isNullOrEmpty(this.parameter.getDevId())) {
        	inspnoparam.setDevId(this.parameter.getDevId());

        } else {
        	String strDevIdList = "";
        	ListSmsDevRelationParameter devParameter = new ListSmsDevRelationParameter();
        	devParameter.setBean("ListSmsDevRelationBean");
        	devParameter.setCorpId(this.parameter.getCorpId());
        	devParameter.setBuildingId(this.parameter.getBuildingId());
        	ListSmsDevRelationResult devRelationResult = listSmsDevRelationDao.query(devParameter);
        	if (!devRelationResult.getDetailList().isEmpty()) {
        		for (int i = 0; i < devRelationResult.getDetailList().size(); i++) {
        			if (i > 0) {
        				strDevIdList = strDevIdList + ",";
        			}
        			strDevIdList = strDevIdList + devRelationResult.getDetailList().get(i).getDevId();
        		}
        	} else {	/* devId指定なしで、対象建物にdevIdが紐づいていない場合 */
                ListSmsMeterInspectionDataResult result = new ListSmsMeterInspectionDataResult();
                response.setResultCode(OsolApiResultCode.API_ERROR_DATABASE_NO_RESULT);
                response.setResult(result);
                return response;
        	}
        	inspnoparam.setDevIdListStr(strDevIdList);
        }

    	ListSmsMonthMeterReadingNoResult monthNoResult = listSmsMonthMeterReadingNoSearchDao.query(inspnoparam);
		List<ListSmsMeterReadingDataMonthNoResultData> monthNoResultList = monthNoResult.getMonthMeterReadingNoDataResultData().getMonthMeterReadingNoResultDataList();
		ListSmsMeterInspectionDataParameter param = new ListSmsMeterInspectionDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setDevIdListStr(null);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
//        param.setMonthInspectionNo(this.parameter.getMonthInspectionNo());
        Long lngMonthInspectionNo = this.parameter.getMonthInspectionNo();
        param.setInspectionType(this.parameter.getInspectionType());

        ListSmsMeterInspectionDataResult result = new ListSmsMeterInspectionDataResult();
        List<ListSmsMeterInspectionDataResultData> meterInspectionDataList = new ArrayList<>();
		for (ListSmsMeterReadingDataMonthNoResultData monthNo : monthNoResultList) {
			param.setInspectionType(this.parameter.getInspectionType());
			if (!CheckUtility.isNullOrEmpty(param.getInspectionType())) {
				if (!monthNo.getMeterReadingType().contentEquals(param.getInspectionType())) {
					continue;
				}
			}
	        param.setDevId(monthNo.getDevId());
	        if (lngMonthInspectionNo == null) {
	        	param.setMonthInspectionNo(monthNo.getMonthMeterReadingNo());
	        } else {
	        	if (lngMonthInspectionNo.equals(monthNo.getMonthMeterReadingNo())) {
	        		param.setMonthInspectionNo(lngMonthInspectionNo);
	        	} else {
	        		continue;
	        	}
	        }
	        result = ListSmsMeterInspectionDataDao.query(param);

	        List<ListSmsMeterInspectionDataResultData> meterInspectionAddDataList = result.getMeterInspectionDataResultDataList();
	        meterInspectionDataList.addAll(meterInspectionAddDataList);
		}
		result.setMeterInspectionDataResultDataList(meterInspectionDataList);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    @Override
    public ListSmsMeterInspectionDataParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsMeterInspectionDataParameter parameter) {
        this.parameter = parameter;
    }

}