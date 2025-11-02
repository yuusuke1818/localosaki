package jp.co.osaki.osol.api.dao.sms.collect.setting.repeater;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.GetSmsRepeaterParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.GetSmsRepeaterResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.repeater.MRepeaterServiceDaoImpl;
import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.MRepeaterPK;

/**
 * 中継装置管理 中継装置情報取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class GetSmsRepeaterDao extends OsolApiDao<GetSmsRepeaterParameter> {

    private final MRepeaterServiceDaoImpl repeaterDaoImpl;

    public GetSmsRepeaterDao() {
        repeaterDaoImpl = new MRepeaterServiceDaoImpl();
    }

    @Override
    public GetSmsRepeaterResult query(GetSmsRepeaterParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        MRepeater target = new MRepeater();
        MRepeaterPK targetPk = new MRepeaterPK();
        targetPk.setDevId(parameter.getDevId());
        targetPk.setRepeaterMngId(parameter.getRepeaterMngId());
        target.setId(targetPk);

        // ServiceDaoクラスにてクエリ実行
        MRepeater entity = find(repeaterDaoImpl, target);

        GetSmsRepeaterResult result = new GetSmsRepeaterResult();
        result.setRepeater(setResultData(entity));
        return result;
    }

    private ListSmsRepeatersResultData setResultData (MRepeater entity) {
        ListSmsRepeatersResultData resultData = new ListSmsRepeatersResultData();
        resultData.setDevId(entity.getId().getDevId());
        resultData.setRepeaterMngId(entity.getId().getRepeaterMngId());
        resultData.setRecDate(entity.getRecDate());
        resultData.setRecMan(entity.getRecMan());
        resultData.setCommandFlg(entity.getCommandFlg());
        resultData.setSrvEnt(entity.getSrvEnt());
        resultData.setRepeaterId(entity.getRepeaterId());
        resultData.setMemo(entity.getMemo());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
