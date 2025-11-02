/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportPointCountListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportPointCountListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportPointCountListResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;

/**
 * エネルギー使用状況実績取得（個別・日報・手入力ポイント） デマンド日報ポイント数Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgDayReportPointCountListDao extends OsolApiDao<DemandOrgDayReportPointCountListParameter> {

    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;

    public DemandOrgDayReportPointCountListDao() {
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
    }

    @Override
    public DemandOrgDayReportPointCountListResult query(DemandOrgDayReportPointCountListParameter parameter) throws Exception {
        CommonDemandDayReportPointListResult param = new CommonDemandDayReportPointListResult();
        DemandOrgDayReportPointCountListResult result = new DemandOrgDayReportPointCountListResult();

        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDate());
        param.setMeasurementDateTo(parameter.getMeasurementDate());
        param.setJigenNoFrom(new BigDecimal(1));
        param.setJigenNoTo(new BigDecimal(48));



        List<CommonDemandDayReportPointListResult>  resultList = getResultList(commonDemandDayReportPointListServiceDaoImpl,param);

        BigDecimal currentJigen = new BigDecimal(1);

        List<DemandOrgDayReportPointCountListResultData> pointCountList = new ArrayList<>();
        while(currentJigen.compareTo(new BigDecimal(48)) <= 0) {
            DemandOrgDayReportPointCountListResultData pointCount = new DemandOrgDayReportPointCountListResultData();
            BigDecimal targetJigen = new BigDecimal(currentJigen.toPlainString());
            Long count = resultList.stream().filter(o -> o.getJigenNo().equals(targetJigen)).count();
            pointCount.setCorpId(parameter.getOperationCorpId());
            pointCount.setBuildingId(parameter.getBuildingId());
            pointCount.setMeasurementDate(parameter.getMeasurementDate());
            pointCount.setJigenNo(currentJigen);
            pointCount.setPointCount(count);

            pointCountList.add(pointCount);

            currentJigen = currentJigen.add(BigDecimal.ONE);

        }

        result.setDemandDayReportPointCountList(pointCountList);

        return result;
    }


}
