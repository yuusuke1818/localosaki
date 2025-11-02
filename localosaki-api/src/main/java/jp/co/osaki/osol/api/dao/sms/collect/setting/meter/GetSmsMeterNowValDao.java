package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.util.List;

import javax.ejb.Stateless;

import com.amazonaws.util.CollectionUtils;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsMeterNowValParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetSmsMeterNowValResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.GetSmsMeterNowValDaoImpl;

/**
 * メーター管理 現在値取得 Daoクラス
 * @author sagi_h
 */
@Stateless
public class GetSmsMeterNowValDao extends OsolApiDao<GetSmsMeterNowValParameter> {

    private final GetSmsMeterNowValDaoImpl getSmsMeterNowValDaoImpl;

    public GetSmsMeterNowValDao() {
        getSmsMeterNowValDaoImpl = new GetSmsMeterNowValDaoImpl();
    }
    @Override
    public GetSmsMeterNowValResult query(GetSmsMeterNowValParameter parameter) throws Exception {
        GetSmsMeterNowValResult param = new GetSmsMeterNowValResult();
        param.setDevId(parameter.getDevId());
        param.setMeterMngId(parameter.getMeterMngId());

        List<GetSmsMeterNowValResult> resultList = getResultList(getSmsMeterNowValDaoImpl,param);
        GetSmsMeterNowValResult result = new GetSmsMeterNowValResult();
        if(!CollectionUtils.isNullOrEmpty(resultList)) {
            result = resultList.get(0);
        }
        return result;
    }
}
