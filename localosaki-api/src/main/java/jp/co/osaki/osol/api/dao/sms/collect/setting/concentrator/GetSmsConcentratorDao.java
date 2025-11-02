package jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.GetSmsConcentratorParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.GetSmsConcentratorResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.concentrator.MConcentratorServiceDaoImpl;
import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK;

/**
 * コンセントレータ管理 コンセントレータ情報取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class GetSmsConcentratorDao extends OsolApiDao<GetSmsConcentratorParameter> {

    private final MConcentratorServiceDaoImpl concentratorDaoImpl;

    public GetSmsConcentratorDao() {
        concentratorDaoImpl = new MConcentratorServiceDaoImpl();
    }

    @Override
    public GetSmsConcentratorResult query(GetSmsConcentratorParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        MConcentrator target = new MConcentrator();
        MConcentratorPK targetPk = new MConcentratorPK();
        targetPk.setDevId(parameter.getDevId());
        targetPk.setConcentId(parameter.getConcentId());
        target.setId(targetPk);

        // ServiceDaoクラスにてクエリ実行
        MConcentrator entity = find(concentratorDaoImpl, target);

        GetSmsConcentratorResult result = new GetSmsConcentratorResult();
        result.setConcentratorList(setResultData(entity));
        return result;
    }

    private ListSmsConcentratorsResultData setResultData (MConcentrator entity) {
        ListSmsConcentratorsResultData resultData = new ListSmsConcentratorsResultData();
        resultData.setDevId(entity.getId().getDevId());
        resultData.setConcentId(entity.getId().getConcentId());
        resultData.setRecDate(entity.getRecDate());
        resultData.setRecMan(entity.getRecMan());
        resultData.setCommandFlg(entity.getCommandFlg());
        resultData.setSrvEnt(entity.getSrvEnt());
        resultData.setIpAddr(entity.getIpAddr());
        resultData.setConcentSta(entity.getConcentSta());
        resultData.setName(entity.getName());
        resultData.setMemo(entity.getMemo());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
