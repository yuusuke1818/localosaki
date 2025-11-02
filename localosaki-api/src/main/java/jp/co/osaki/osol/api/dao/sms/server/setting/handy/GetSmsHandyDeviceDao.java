/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.handy;

import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.GetSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.GetSmsHandyDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;

/**
 * 装置情報取得（ハンディ端末のみ） Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class GetSmsHandyDeviceDao extends OsolApiDao<GetSmsHandyDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;

    public GetSmsHandyDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
    }

    @Override
    public GetSmsHandyDeviceResult query(GetSmsHandyDeviceParameter parameter) throws Exception {
        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        GetSmsHandyDeviceResult result = null;
        if (Objects.nonNull(findMDevPrm)) {
            result = new GetSmsHandyDeviceResult(findMDevPrm);
        }
        return result;
    }

}
