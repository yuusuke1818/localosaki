package jp.co.osaki.osol.api.dao.sms.collect.setting.repeater;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.ListSmsRepeatersParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.ListSmsRepeatersResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.repeater.ListSmsRepeaterServiceDaoImpl;

/**
 * 中継装置管理 中継装置一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class ListSmsRepeatersDao extends OsolApiDao<ListSmsRepeatersParameter> {

    private final ListSmsRepeaterServiceDaoImpl repeaterDaoImpl;

    public ListSmsRepeatersDao() {
        repeaterDaoImpl = new ListSmsRepeaterServiceDaoImpl();
    }

    @Override
    public ListSmsRepeatersResult query(ListSmsRepeatersParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsRepeatersResultData repeaterResultData = new ListSmsRepeatersResultData();
        repeaterResultData.setDevId(parameter.getDevId());
        repeaterResultData.setRepeaterMngId(parameter.getRepeaterMngId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsRepeatersResultData> resultRepeatersList = getResultList(repeaterDaoImpl, repeaterResultData);


        ListSmsRepeatersResult result = new ListSmsRepeatersResult();
        result.setRepeaterList(resultRepeatersList);
        return result;
    }
}
