package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.verify.DemandOrgDailyControlLogDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgDailyControlLogParameter;
import jp.co.osaki.osol.api.response.energy.verify.DemandOrgDailyControlLogResponse;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyControlLogResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況・日報・制御履歴 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "DemandOrgDailyControlLogBean")
@RequestScoped
public class DemandOrgDailyControlLogBean extends OsolApiBean<DemandOrgDailyControlLogParameter>
        implements BaseApiBean<DemandOrgDailyControlLogParameter, DemandOrgDailyControlLogResponse> {

    private DemandOrgDailyControlLogParameter parameter = new DemandOrgDailyControlLogParameter();

    private DemandOrgDailyControlLogResponse response = new DemandOrgDailyControlLogResponse();

    @EJB
    private DemandOrgDailyControlLogDao demandOrgDailyControlLogDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDailyControlLogParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDailyControlLogParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDailyControlLogResponse execute() throws Exception {
        DemandOrgDailyControlLogParameter param = new DemandOrgDailyControlLogParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());
        param.setYmd(this.parameter.getYmd());
        param.setTimesOfDay(this.parameter.getTimesOfDay());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラーメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getSumPeriodCalcType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(param.getSumPeriodCalcType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_VERIFY_SUM_PERIOD_MAX_HOUR)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgDailyControlLogResult result = demandOrgDailyControlLogDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    //    @Override
    //    public ApiResponse doCall(HttpServletRequest request) {
    //
    //        DemandOrgDailyControlLogResponse response = new DemandOrgDailyControlLogResponse();
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
    //        // 機器ID
    //        Long smId;
    //        if (CheckUtility.isNullOrEmpty(paramMap.get(ApiParamKeyConstants.SM_ID))) {
    //            smId = null;
    //        } else {
    //            try {
    //                smId = Long.parseLong(paramMap.get(ApiParamKeyConstants.SM_ID));
    //            } catch (NumberFormatException ex) {
    //                setResponse(response, null, OsolApiResponseCode.NO_PARAMETER_ERROR);
    //                return response;
    //            }
    //
    //        }
    //
    //        //年月日
    //        String day = paramMap.get(ApiParamKeyConstants.DAY);
    //        //時刻
    //        String time = paramMap.get(ApiParamKeyConstants.TIME);
    //        //NULLの場合、"0000"とみなす
    //        if (CheckUtility.isNullOrEmpty(time)) {
    //            time = "0000";
    //        }
    //        //集計期間計算方法
    //        String type = paramMap.get(ApiParamKeyConstants.TYPE);
    //        //nullの場合、0：からとみなす
    //        if (CheckUtility.isNullOrEmpty(type)) {
    //            type = ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal();
    //        }
    //        //集計期間
    //        BigDecimal range;
    //        if (CheckUtility.isNullOrEmpty(paramMap.get(ApiParamKeyConstants.RANGE))) {
    //            //nullの場合、24時間とみなす
    //            range = new BigDecimal(24);
    //        } else {
    //            range = new BigDecimal(paramMap.get(ApiParamKeyConstants.RANGE));
    //        }
    //
    //        if (!ApiValidateUtility.validateApiCommonParameter(loginUserId, loginPersonId, loginUserCorpId, corpId)
    //                || !ApiValidateUtility.validateDay(day, DateUtility.DATE_FORMAT_YYYYMMDD)
    //                || !validate(time, type)) {
    //            setResponse(response, null, OsolApiResponseCode.NO_PARAMETER_ERROR);
    //            return response;
    //        }
    //
    //        try {
    //            List<DemandOrgDailyControlLogResult> rs = demandOrgDailyControlLogDao.search(corpId, buildingId, smId, day, time, type, range);
    //            setResponse(response, rs, OsolApiResponseCode.API_OK);
    //        } catch (Exception ex) {
    //            setResponse(response, null, OsolApiResponseCode.API_ERROR);
    //            return response;
    //        }
    //        return response;
    //    }
    //
    //    /**
    //     * Responseに値をセットする
    //     *
    //     * @param response
    //     * @param resultList
    //     * @param resultCode
    //     */
    //    private void setResponse(DemandOrgDailyControlLogResponse response, List<DemandOrgDailyControlLogResult> resultList, OsolApiResponseCode resultCode) {
    //        response.setResultCd(resultCode);
    //        response.setResultList(resultList);
    //    }
    //
    //    /**
    //     * パラメータの入力チェック
    //     *
    //     * @param time
    //     * @param type
    //     * @return
    //     */
    //    private boolean validate(String time, String type) {
    //        //時刻のチェック
    //        int hh;
    //        int mm;
    //        try {
    //            hh = Integer.parseInt(time.substring(0, 3));
    //            mm = Integer.parseInt(time.substring(3));
    //            if (hh < 0 && hh > 24) {
    //                //時間の範囲外
    //                return false;
    //            }
    //            if (mm != 0) {
    //                //分は00のみ
    //                return false;
    //            }
    //        } catch (NumberFormatException ex) {
    //            //数値ではない
    //            return false;
    //        }
    //
    //        //集計期間計算方法のチェック
    //        if (CheckUtility.isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(type))) {
    //            //名称が取得できない
    //            return false;
    //        }
    //
    //        return true;
    //    }

}
