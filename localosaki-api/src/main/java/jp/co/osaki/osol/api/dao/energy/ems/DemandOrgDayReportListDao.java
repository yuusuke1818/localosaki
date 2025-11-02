/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;

/**
 * エネルギー使用状況実績取得（個別・日報・系統別使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgDayReportListDao extends OsolApiDao<DemandOrgDayReportListParameter> {

    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;

    public DemandOrgDayReportListDao() {
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
    }

    @Override
    public DemandOrgDayReportListResult query(DemandOrgDayReportListParameter parameter) throws Exception {
        CommonDemandDayReportListResult param = new CommonDemandDayReportListResult();
        DemandOrgDayReportListResult result = new DemandOrgDayReportListResult();

        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setJigenNoFrom(parameter.getJigenNoFrom());
        param.setJigenNoTo(parameter.getJigenNoTo());

        List<CommonDemandDayReportListResult>  resultList = getResultList(commonDemandDayReportListServiceDaoImpl,param);

        result.setCommonDemandDayReportList(resultList);

        return result;
    }


}
