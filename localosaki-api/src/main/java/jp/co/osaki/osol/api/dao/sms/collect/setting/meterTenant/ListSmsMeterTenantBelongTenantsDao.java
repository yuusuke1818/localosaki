package jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsResult;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.SearchTenantSmsResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.SearchTenantSmsServiceDaoImpl;

@Stateless
public class ListSmsMeterTenantBelongTenantsDao extends OsolApiDao<ListSmsMeterTenantBelongTenantsParameter> {

    // 所属建物に属するテナント
    private final SearchTenantSmsServiceDaoImpl searchTenantSmsServiceDaoImpl;

    public ListSmsMeterTenantBelongTenantsDao() {
        searchTenantSmsServiceDaoImpl = new SearchTenantSmsServiceDaoImpl();
    }

    @Override
    public ListSmsMeterTenantBelongTenantsResult query(ListSmsMeterTenantBelongTenantsParameter parameter) throws Exception {

        SearchTenantSmsResultData resultData = new SearchTenantSmsResultData();
        resultData.setDivisionCorpId(parameter.getCorpId());
        resultData.setDivisionBuildingId(parameter.getBuildingId());

        List<SearchTenantSmsResultData> resultList = getResultList(searchTenantSmsServiceDaoImpl, resultData);

        ListSmsMeterTenantBelongTenantsResult result = new ListSmsMeterTenantBelongTenantsResult();
        result.setResultDataList(resultList);

        return result;
    }

}
