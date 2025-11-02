package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.AggregateDmLineListParameter;
import jp.co.osaki.osol.api.result.energy.setting.AggregateDmLineListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;

/**
 * 集計デマンド系統情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class AggregateDmLineListDao extends OsolApiDao<AggregateDmLineListParameter> {

    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AggregateDmLineListDao() {
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public AggregateDmLineListResult query(AggregateDmLineListParameter parameter) throws Exception {
        AggregateDmLineListDetailResultData param = EnergySettingUtility.getAggregateDmLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                parameter.getLineNo(), parameter.getAggregateDmCorpId(), parameter.getAggregateDmBuildingId(),
                parameter.getAggregateDmId());
        List<AggregateDmLineListDetailResultData> resultList = getResultList(aggregateDmLineListServiceDaoImpl, param);

        resultList = corpDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            return new AggregateDmLineListResult();
        }

        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            return new AggregateDmLineListResult();
        } else {
            return new AggregateDmLineListResult(resultList);
        }
    }

}
