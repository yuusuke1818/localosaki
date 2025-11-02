/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarDayReportParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarDayReportResult;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンド情報カレンダー（日報用） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandCalendarDayReportDao extends OsolApiDao<DemandCalendarDayReportParameter> {

    public DemandCalendarDayReportDao() {
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandCalendarDayReportResult query(DemandCalendarDayReportParameter parameter) throws Exception {
        DemandCalendarDayReportResult result = new DemandCalendarDayReportResult();
        result.setNowDate(DateUtility.conversionDate(
                DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD));
        result.setBeforeDate(DateUtility.conversionDate(
                DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                        DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD));
        result.setDefaultYear(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY));
        result.setYearList(DemandEmsUtility.getYearList(result.getDefaultYear(), parameter.getSumPeriod()));

        return result;
    }

}
