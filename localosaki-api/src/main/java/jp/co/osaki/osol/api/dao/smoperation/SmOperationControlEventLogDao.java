/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.smoperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationControlEventLogParameter;
import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.api.result.smoperation.SmOperationControlEventLogResult;
import jp.co.osaki.osol.api.servicedao.energy.verify.EventControlLogServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御DB閲覧 イベント制御履歴取得 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class SmOperationControlEventLogDao extends OsolApiDao<SmOperationControlEventLogParameter> {

    private final EventControlLogServiceDaoImpl eventControlLogServiceDaoImpl;

    public SmOperationControlEventLogDao() {
        eventControlLogServiceDaoImpl = new EventControlLogServiceDaoImpl();

    }

    @Override
    public SmOperationControlEventLogResult query(SmOperationControlEventLogParameter parameter) throws Exception {
        Long smId = parameter.getSmId();
        Date recordDate = parameter.getRecordDate();
        BigDecimal controlLoad = parameter.getControlLoad();
        Integer sumPeriod = parameter.getSumPeriod();
        String sumPeriodCalcType = parameter.getSumPeriodCalcType();

        SmOperationControlEventLogResult result = new SmOperationControlEventLogResult();

        //イベント制御履歴を取得する
        EventControlLogResult param = new EventControlLogResult();
        param.setSmId(smId);
        param.setControlLoad(controlLoad);

        //集計期間がnullの場合、1を設定する
        if(sumPeriod == null) {
            sumPeriod = 1;
        }

        //集計期間計算方法がnullまたは空文字の場合、2:まで を設定する
       if(CheckUtility.isNullOrEmpty(sumPeriodCalcType)) {
           sumPeriodCalcType = ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal();
       }

        //集計期間計算方法分岐
        if(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal().equals(sumPeriodCalcType)) {
            //集計期間計算方法が 0:から の場合
            //取得開始日時を作成する
            param.setRecordYmdFrom(DateUtility.changeDateFormat(recordDate, DateUtility.DATE_FORMAT_YYYYMMDD));

            //取得終了日時を作成する
            param.setRecordYmdTo(DateUtility.changeDateFormat(DateUtility.plusDay(recordDate,sumPeriod - 1), DateUtility.DATE_FORMAT_YYYYMMDD));
        }else if(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.LAST.getVal().equals(sumPeriodCalcType)) {
            //集計期間計算方法が 2:まで の場合
            //取得開始日時を作成する
            param.setRecordYmdFrom(DateUtility.changeDateFormat(DateUtility.plusDay(recordDate,(-1 * (sumPeriod-1))), DateUtility.DATE_FORMAT_YYYYMMDD));

            //取得終了日時を作成する
            param.setRecordYmdTo(DateUtility.changeDateFormat(recordDate, DateUtility.DATE_FORMAT_YYYYMMDD));
        }else {
            //集計期間計算方法が 想定外の場合、処理を終了する
            return result;
        }

        List<EventControlLogResult> eventControlLogResultList = getResultList(eventControlLogServiceDaoImpl,param);

        //データ取得に失敗またはデータが存在しない場合は処理を終了する
        if(eventControlLogResultList == null || eventControlLogResultList.size() == 0) {
            return result;
        }

        result.setEventControlLogResult(eventControlLogResultList);

        return result;
    }
}
