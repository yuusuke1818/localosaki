package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspMonthNoParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsInspMonthNoResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterSvrSearchResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterSvrSearchServiceDaoImpl;

/**
 * 月検針連番一覧 取得(連番プルダウン用) Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class ListSmsInspMonthNoDao extends OsolApiDao<ListSmsInspMonthNoParameter> {

    // 検針結果データ(サーバ用)
    private final InspectionMeterSvrSearchServiceDaoImpl inspectionMeterSvrSearchServiceDaoImpl;

    public ListSmsInspMonthNoDao() {
        inspectionMeterSvrSearchServiceDaoImpl = new InspectionMeterSvrSearchServiceDaoImpl();
    }

    @Override
    public ListSmsInspMonthNoResult query(ListSmsInspMonthNoParameter parameter) throws Exception {
        // 検索条件セット
        InspectionMeterSvrSearchResultData target = new InspectionMeterSvrSearchResultData(
                parameter.getDevId(),       // 装置ID
                parameter.getInspYear(),    // 検針年
                parameter.getInspMonth(),   // 検針月
                parameter.getInspType()     // 検針種別：R:定期検針 T:臨時検針
        );

        // 検索
        List<InspectionMeterSvrSearchResultData> entityList = getResultList(inspectionMeterSvrSearchServiceDaoImpl, target);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }

        List<Long> inspMonthNoList = new ArrayList<Long>();
        entityList.stream().forEach(row -> inspMonthNoList.add(row.getInspMonthNo()));

        return new ListSmsInspMonthNoResult(inspMonthNoList);
    }
}
