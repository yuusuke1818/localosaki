package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsCheckMeterParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsCheckMeterResult;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeter;

/**
 * 確定漏れ管理番号 一覧 検索 (確定前検針データ一覧表示画面用) Daoクラス
 * @author kobayashi.sho
 */
@Stateless
public class ListSmsCheckMeterDao extends OsolApiDao<ListSmsCheckMeterParameter> {

    /** セッション. */
    @Resource
    private SessionContext sessionContext;

    // 確定前検針データ一覧 取得
    private final MeterServiceDaoImpl meterServiceDaoImpl;

    public ListSmsCheckMeterDao() {
        meterServiceDaoImpl = new MeterServiceDaoImpl();
    }

    @Override
    public ListSmsCheckMeterResult query(ListSmsCheckMeterParameter parameter) throws Exception {

        // 検索条件設定
        Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
        targetMap.put(MeterServiceDaoImpl.DEV_ID,
                Arrays.asList(parameter.getDevId()));       // 装置ID
        targetMap.put(MeterServiceDaoImpl.EXCLUSION_METER_MNG_ID,
                parameter.getMeterMngIds().getMeterMngIdList().stream()
                    .collect(Collectors.toList()));         // 確定するメーター管理番号一覧

        // 確定漏れ管理番号 一覧 検索 (確定前検針データ一覧表示画面用)
        List<MMeter> entityList = getResultList(meterServiceDaoImpl, targetMap);
        List<Long> meterMngIdList = null;
        if (entityList != null && !entityList.isEmpty()) {
            // 確定漏れ管理番号あり → 処理を中断して 強制実行確認ダイアログ を表示する
            meterMngIdList = entityList.stream()
                    .map(row -> row.getId().getMeterMngId())
                    .collect(Collectors.toList());
        }

        return new ListSmsCheckMeterResult(meterMngIdList);
    }

}
