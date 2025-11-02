package jp.co.osaki.osol.api.dao.sms.collect.setting.errorinfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.errorinfo.ListSmsErrorInfoParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.errorinfo.ListSmsErrorInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.errorinfo.ListSmsErrorInfoResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.errorinfo.ListSmsErrorInfoConcentratorServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.errorinfo.ListSmsErrorInfoDevPrmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.errorinfo.ListSmsErrorInfoMeterServiceDaoImpl;

/**
 * データ収集装置 機器管理 異常情報画面 データ取得API Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class ListSmsErrorInfoDao extends OsolApiDao<ListSmsErrorInfoParameter> {

    private final ListSmsErrorInfoDevPrmServiceDaoImpl devPrmDaoImpl;
    private final ListSmsErrorInfoConcentratorServiceDaoImpl concentratorDaoImpl;
    private final ListSmsErrorInfoMeterServiceDaoImpl meterDaoImpl;

    public ListSmsErrorInfoDao() {
        devPrmDaoImpl = new ListSmsErrorInfoDevPrmServiceDaoImpl();
        concentratorDaoImpl = new ListSmsErrorInfoConcentratorServiceDaoImpl();
        meterDaoImpl = new ListSmsErrorInfoMeterServiceDaoImpl();
    }

    @Override
    public ListSmsErrorInfoResult query(ListSmsErrorInfoParameter parameter) throws Exception {
        ListSmsErrorInfoResultData param = new ListSmsErrorInfoResultData();
        param.setCorpId(parameter.getCorpId());
        param.setBuildingId(parameter.getBuildingId());

        ListSmsErrorInfoResult result = new ListSmsErrorInfoResult();
        result.setDevPrmMap(createDevIdDataMap(getResultList(devPrmDaoImpl, param)));
        result.setConcentMap(createDevIdDataMap(getResultList(concentratorDaoImpl, param)));
        result.setMeterMap(createDevIdDataMap(getResultList(meterDaoImpl, param)));

        return result;
    }

    /**
     * 装置・ID単位のデータMapを生成.
     *
     * @param targetDataList 対象データList
     * @return 装置・ID単位のデータMap
     */
    private Map<String, Map<Long, ListSmsErrorInfoResultData>> createDevIdDataMap(
            List<ListSmsErrorInfoResultData> targetDataList) {

        Map<String, Map<Long, ListSmsErrorInfoResultData>> devIdDataMap = new LinkedHashMap<>();
        for (int i = 0; i < targetDataList.size(); i++) {
            ListSmsErrorInfoResultData targetData = targetDataList.get(i);

            String devId = targetData.getDevId();

            Long dataId = targetData.getDataId();
            if (dataId == null) {
                dataId = (long) i;
            }

            Map<Long, ListSmsErrorInfoResultData> idDataMap = devIdDataMap.getOrDefault(devId, new LinkedHashMap<>());

            idDataMap.put(dataId, targetData);

            devIdDataMap.put(devId, idDataMap);
        }

        return devIdDataMap;
    }
}
