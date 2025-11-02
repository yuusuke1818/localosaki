package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.AggregateDmListParameter;
import jp.co.osaki.osol.api.result.energy.setting.AggregateDmListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;

/**
 * 集計デマンド情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class AggregateDmListDao extends OsolApiDao<AggregateDmListParameter> {

    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AggregateDmListDao() {
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public AggregateDmListResult query(AggregateDmListParameter parameter) throws Exception {
        AggregateDmListDetailResultData param = EnergySettingUtility.getAggregateDmListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getAggregateDmId());
        List<AggregateDmListDetailResultData> resultList = getResultList(aggregateDmListServiceDaoImpl, param);

        resultList = corpDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            return new AggregateDmListResult();
        }

        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            return new AggregateDmListResult();
        } else {
            return new AggregateDmListResult(resultList);
        }
    }

}
