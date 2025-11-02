package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsWirelessIdHistoryParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetSmsWirelessIdHistoryResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.GetSmsWirelessIdHistoryResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.THopidHistoryServiceDaoImpl;
import jp.co.osaki.osol.entity.THopidHistory;
import jp.co.osaki.osol.entity.THopidHistoryPK;

/**
 * 無線ID履歴取得API DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class GetSmsWirelessIdHistoryDao extends OsolApiDao<GetSmsWirelessIdHistoryParameter> {

    private final THopidHistoryServiceDaoImpl tHopidHistoryServiceDaoImpl;

    public GetSmsWirelessIdHistoryDao() {
        tHopidHistoryServiceDaoImpl = new THopidHistoryServiceDaoImpl();
    }
    @Override
    public GetSmsWirelessIdHistoryResult query(GetSmsWirelessIdHistoryParameter parameter) throws Exception {
        /** 取得件数 */
        int amount = parameter.getAmount().intValue();
        // 検索用にエンティティ設定
        THopidHistory target = new THopidHistory();
        THopidHistoryPK targetPK = new THopidHistoryPK();
        targetPK.setDevId(parameter.getDevId());

        target.setId(targetPK);

        // 指定された装置IDの無線ID履歴を取得
        final List<THopidHistory> entityList = getResultList(tHopidHistoryServiceDaoImpl, target);

        /** 結果返却用リスト */
        List<GetSmsWirelessIdHistoryResultData> resultList = new ArrayList<GetSmsWirelessIdHistoryResultData>();

        // 指定件数が取得リストの件数より多ければ取得件数までに絞る
        if(entityList.size() < amount) {
            amount = entityList.size();
        }

        // 指定された件数にリストを絞る
        for(int i = 0; i < amount; i++) {
            resultList.add(new GetSmsWirelessIdHistoryResultData(entityList.get(i)));
        }

        GetSmsWirelessIdHistoryResult result = new GetSmsWirelessIdHistoryResult();
        result.setList(resultList);

        return result;
    }

}
