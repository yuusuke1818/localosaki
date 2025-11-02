package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.ListSmsMeterMeterTypeDaoImple;

/**
 * メーター管理 メーター種別一覧取得 Daoクラス
 * @author kimura.m
 */
@Stateless
public class ListSmsMeterMeterTypeDao extends OsolApiDao<ListSmsMeterParameter> {

    private final ListSmsMeterMeterTypeDaoImple meterTypeDaoImple;

    public ListSmsMeterMeterTypeDao() {
        meterTypeDaoImple = new ListSmsMeterMeterTypeDaoImple();
    }

    @Override
    public ListSmsMeterResult query(ListSmsMeterParameter parameter) throws Exception {

        ListSmsMeterResultData target = new ListSmsMeterResultData();
        target.setCorpId(parameter.getCorpId());
        target.setBuildingId(parameter.getBuildingId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsMeterResultData> meterTypeList = getResultList(meterTypeDaoImple, target);

        List<ListSmsMeterResultData> list = new ArrayList<>();
        if (meterTypeList != null) {
            for (ListSmsMeterResultData meterType : meterTypeList) {
                list.add(new ListSmsMeterResultData(meterType.getMeterType(), meterType.getMeterTypeName()));
            }
        }
        ListSmsMeterResult result = new ListSmsMeterResult();
        result.setList(list);
        return result;
    }
}
