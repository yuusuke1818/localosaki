package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsMeterMngIdByDevIdDaoImple;

/**
 * メーター管理 メーター情報のメーター管理ID一覧取得（条件：装置IDのみ） Daoクラス
 * @author kimura.m
 */
@Stateless
public class ListSmsMeterMngIdByDevIdDao extends OsolApiDao<ListSmsMeterParameter> {

    private final ListSmsMeterMngIdByDevIdDaoImple meterMngIdByDevIdDaoImple;

    public ListSmsMeterMngIdByDevIdDao() {
        meterMngIdByDevIdDaoImple = new ListSmsMeterMngIdByDevIdDaoImple();
    }

    @Override
    public ListSmsMeterResult query(ListSmsMeterParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsMeterResultData meterResultData = new ListSmsMeterResultData();
        meterResultData.setDevId(parameter.getDevId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsMeterResultData> resultMeterInfoList = getResultList(meterMngIdByDevIdDaoImple, meterResultData);

        ListSmsMeterResult result = new ListSmsMeterResult();
        result.setList(resultMeterInfoList);
        return result;
    }
}
